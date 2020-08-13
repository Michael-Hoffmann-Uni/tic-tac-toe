package jpp.games.networking.server.handler;

import jpp.games.logic.Game;
import jpp.games.logic.Player;
import jpp.games.logic.Rules;
import jpp.games.model.Action;
import jpp.games.model.GameState;
import jpp.games.networking.server.Server;
import jpp.games.networking.server.database.DatabaseAccess;
import jpp.games.networking.server.handler.game.GameRunner;
import jpp.games.networking.server.handler.game.RemoteIO;
import jpp.games.networking.server.handler.game.RemotePlayer;
import jpp.games.tictactoe.Adapter;
import jpp.games.tictactoe.PlayerType_ttt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

@RestController
public class GameHandler<BoardState, Move, PlayerType> {

    private Server server;
    private DatabaseAccess database;
    private ConcurrentMap<String, RemoteIO> remoteIOs;
    private Map<String, Game> gameMap;
    private Adapter<BoardState, Move, PlayerType> adapter;

    @Autowired
    public GameHandler(Server server) {
        this.server = server;
        this.database = server.getDatabaseImplementation();
        this.remoteIOs = server.getRemoteIOs();
        this.gameMap = new HashMap<>();
        this.adapter = new Adapter<>();
    }

    public void gameEndCallback(String gameId) {
        //get game
        Game game = gameMap.get(gameId);

        //find winner-name
        String winnerName = "";
        if (game.getWinner().isPresent()) {
            PlayerType winner = (PlayerType) game.getWinner().get();
            List<RemotePlayer> playerList = game.getPlayers();
            for (RemotePlayer player : playerList) {
                if (player.getPlayerType() == winner) {
                    winnerName = player.getName();
                }
            }
        }
        //increase score
        database.increaseScore(winnerName);
        //gameId remove entry from map
        gameMap.remove(gameId);
    }

    @GetMapping("/{key}/create")
    public ResponseEntity<String> createNewGame(@PathVariable("key") String key, @RequestParam(name = "gameId") String id) throws IllegalAccessException {
        if (!database.getName(key).isPresent()) {
            throw new IllegalAccessException("This user has no access.");
        } else {
            if (gameMap.containsKey(id)) {
                throw new IllegalArgumentException("A game with this id exists already");
            } else {
                //CREATE a game without a player
                Game<BoardState, Move, PlayerType> gameLogic = Game.createTwoPlayerGame();
                //put <gameId, gameLogic>
                gameMap.put(id, gameLogic);
                return new ResponseEntity<String>("Game with ID: " + id + " was created.", HttpStatus.ACCEPTED);
            }
        }
    }

    @GetMapping("/{key}/listGames")
    public ResponseEntity<String> getGameList(@PathVariable("key") String key) throws IllegalAccessException {
        if (!database.getName(key).isPresent()) {
            throw new IllegalAccessException("This user has no access.");
        } else {
            String outputString = "";
            for (Map.Entry<String, Game> entry : gameMap.entrySet()) {
                //One player joined already
                if (entry.getValue().getPlayers().size() == 1) {
                    RemotePlayer player1 = (RemotePlayer) entry.getValue().getPlayers().get(0);
                    outputString = outputString + entry.getKey() + "\t" + player1.getName() + "\n";
                }
                //no player joined yet
                if (entry.getValue().getPlayers().isEmpty()) {
                    outputString = outputString + entry.getKey() + "\t" + "No Player" + "\n";
                }
            }
            return new ResponseEntity<String>(outputString, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/{key}/join")
    public ResponseEntity<String> joinGame(@PathVariable("key") String key, @RequestParam(name = "gameId") String id) throws IllegalAccessException {
        if (!database.getName(key).isPresent()) {
            throw new IllegalAccessException("This user has no access.");
        } else {
            //TODO what if game is already running aka has already 2 players?
            //check if game with this id exists
            if (!gameMap.containsKey(id))
                throw new IllegalArgumentException("There is no game with this id.");
            Game gameLogic = gameMap.get(id);
            List<RemotePlayer<BoardState, Move, PlayerType>> remotePlayers = gameLogic.getPlayers();
            //check how many players are in game
            if (remotePlayers.isEmpty()) {
                //NO PLAYERS IN GAME
                //create player1
                String player1Name = database.getName(key).get();
                RemoteIO player2RemoteIO = server.getRemoteIOs().get(player1Name);
                RemotePlayer player1 = (RemotePlayer) Player.createRemotePlayer(
                        player1Name, player2RemoteIO, this::formatBoard, this::formatInputCross
                );
                player1.setPlayerType(adapter.party("CROSS"));
                gameLogic.join(player1);
                return new ResponseEntity<String>(player1Name + " joined game with id " + id + ".", HttpStatus.ACCEPTED);
            } else if (remotePlayers.size() == 1) {
                //ONE PLAYER IN GAME
                //check if player1 is trying to join again
                RemotePlayer player1 = (RemotePlayer) gameLogic.getPlayers().get(0);
                String player1Name = player1.getName();
                String userName = database.getName(key).get();
                if (userName.equals(player1Name))
                    throw new IllegalStateException("You already joined this game.");
                //if not ... check if player1 has CROSS or CIRCLE
                PlayerType_ttt player1PlayerType = (PlayerType_ttt) player1.getPlayerType();
                //create player2 depending on what playerType player1 has
                String player2Name = database.getName(key).get();
                RemoteIO player2RemoteIO = server.getRemoteIOs().get(player2Name);
                RemotePlayer player2;
                if (player1PlayerType.equals(PlayerType_ttt.CROSS)) {
                    player2 = (RemotePlayer) Player.createRemotePlayer(
                            player2Name, player2RemoteIO, this::formatBoard, this::formatInputCircle
                    );
                    player2.setPlayerType(adapter.party("CIRCLE"));
                } else {
                    player2 = (RemotePlayer) Player.createRemotePlayer(
                            player2Name, player2RemoteIO, this::formatBoard, this::formatInputCross
                    );
                    player2.setPlayerType(adapter.party("CROSS"));
                }
                //Let player2 join
                gameLogic.join(player2);
                //start game
                Rules<BoardState, Move, PlayerType> boardLogic = adapter.rules();
                gameLogic.start(boardLogic, GameState.create(adapter.emptyBoard(), adapter.party("CROSS")));
                //message to every player "A new game started. Your opponent is ..."
                player1.notifyStart(player2);
                player2.notifyStart(player1);

                //put game into thread and keep running
                GameRunner gameRunner = new GameRunner(id, gameLogic, this::gameEndCallback);
                Thread thread = new Thread(gameRunner);
                thread.start();
                return new ResponseEntity<String>(player2Name + " joined game with id " + id + " and game started!", HttpStatus.ACCEPTED);
            } else {
                //TWO PLAYERS IN GAME
                return new ResponseEntity<String>("Game with id " + id + " already has two players.", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/{key}/leave")
    public ResponseEntity<String> leave(@PathVariable("key") String key) throws IllegalAccessException {
        if (!database.getName(key).isPresent()) {
            throw new IllegalAccessException("This user has no access.");
        } else {
            boolean playerLeft = false;
            //get user name first from database
            String playerName = database.getName(key).get();

            //for all games in gameMap
            for (Map.Entry<String, Game> entry : gameMap.entrySet()) {
                Game game = entry.getValue();
                List<Player<BoardState, Move, PlayerType>> players = game.getPlayers();

                //copy players to avoid conflict on remove
                List<RemotePlayer<BoardState, Move, PlayerType>> playersCopy = new ArrayList<>();
                for (Player player : players) {
                    playersCopy.add((RemotePlayer) player);
                }

                //for all players in game - remove if this player is part of it
                for (Player<BoardState, Move, PlayerType> player : playersCopy) {
                    //if matching name -> getPlayer
                    if (((RemotePlayer) player).getName().equals(playerName)) {
                        //if game isRunning - leave leads to end + game has to be deleted
                        if (game.isRunning()) {
                            playerLeft = game.leave(player);
                            gameEndCallback(entry.getKey());
                        } else { //game never started - leave is okay
                            playerLeft = game.leave(player);
                        }
                    }
                }
            }
            if (!playerLeft)
                throw new IllegalStateException("This player is not in a game and cannot leave.");
            return new ResponseEntity<String>(key, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/{key}/getUpdate")
    public ResponseEntity<String> getUpdate(@PathVariable("key") String key) throws IllegalAccessException {
        if (!database.getName(key).isPresent()) {
            throw new IllegalAccessException("This user has no access.");
        } else {
            //get playerName
            String playerName = database.getName(key).get();
            //access remoteIOMap
            RemoteIO remoteIO = remoteIOs.get(playerName);
            //pull output and form outputString
            String outputString = remoteIO.getOutput();
            //return outputString in ResponseEntity
            return new ResponseEntity<String>(outputString, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/{key}/makeMove")
    public ResponseEntity<String> makeMove(@PathVariable("key") String key, @RequestParam(name = "move") String move) throws IllegalAccessException {
        if (!database.getName(key).isPresent()) {
            throw new IllegalAccessException("This user has no access.");
        } else {
            //get playerName
            String playerName = database.getName(key).get();
            //access remoteIOMap
            RemoteIO remoteIO = remoteIOs.get(playerName);
            //push input
            remoteIO.putInput(move);
            return new ResponseEntity<String>("This move was transmitted to the server: " + move, HttpStatus.ACCEPTED);
        }
    }

    private void gameEndCallback(Object o) {
        //this::gameEndCallback hat nicht für String funktioniert, deshalb der Umweg über object ...
        if (o instanceof String) {
            gameEndCallback((String) o);
        } else {
            gameEndCallback(o.toString());
        }
    }

    private String formatBoard(BoardState boardState) {
        return getPlayerType(boardState, adapter.position("R1C1")) + "|" +
                getPlayerType(boardState, adapter.position("R1C2")) + "|" +
                getPlayerType(boardState, adapter.position("R1C3")) + "\n" +
                "-|-|-\n" +
                getPlayerType(boardState, adapter.position("R2C1")) + "|" +
                getPlayerType(boardState, adapter.position("R2C2")) + "|" +
                getPlayerType(boardState, adapter.position("R2C3")) + "\n" +
                "-|-|-\n" +
                getPlayerType(boardState, adapter.position("R3C1")) + "|" +
                getPlayerType(boardState, adapter.position("R3C2")) + "|" +
                getPlayerType(boardState, adapter.position("R3C3"));
    }

    private String getPlayerType(BoardState boardState, Move pos) {
        if (adapter.getPositions(boardState, adapter.party("CROSS")).contains(pos)) {
            return "X";
        }
        if (adapter.getPositions(boardState, adapter.party("CIRCLE")).contains(pos)) {
            return "O";
        }
        return " ";
    }

    private Action<Move, PlayerType> formatInputCross(String pos) {
        try {
            return Action.create(adapter.party("CROSS"), adapter.position(pos));
        } catch (Exception ex) {
        }
        return null;
    }

    private Action<Move, PlayerType> formatInputCircle(String pos) {
        try {
            return Action.create(adapter.party("CIRCLE"), adapter.position(pos));
        } catch (Exception ex) {
        }
        return null;
    }
}
