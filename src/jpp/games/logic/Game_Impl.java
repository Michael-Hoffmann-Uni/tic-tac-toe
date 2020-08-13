package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.GameState;
import jpp.games.tictactoe.PlayerType_ttt;

import java.util.*;

public class Game_Impl<BoardState, Move, PlayerType> implements Game<BoardState, Move, PlayerType> {

    private List<Participant<BoardState, Move, PlayerType>> participants;
    private List<Player<BoardState, Move, PlayerType>> players;
    private boolean isRunning;
    private Rules rules;
    private GameState gameState;
    private Player<BoardState, Move, PlayerType> currentPlayer = null;
    private boolean fail = false;
    private boolean isStarted = false;

    public Game_Impl() {
        this.participants = new ArrayList<>();
        this.players = new ArrayList<>();
        isRunning = false;
    }

    @Override
    public void start(Rules<BoardState, Move, PlayerType> rules, GameState<BoardState, PlayerType> initialBoardState) {
        System.out.println(isRunning);
        if (isRunning())
            throw new IllegalStateException("Game is already running!");
        if (rules == null || initialBoardState == null)
            throw new NullPointerException("Rules or initialBoardState is null.");
        isRunning = true;
        isStarted = true;
        if (players.isEmpty())
            end();
        if (players.size() == 1)
            end();
        this.rules = rules;
        for (Player<BoardState, Move, PlayerType> player : players) {
            player.setRules(rules);
        }
        this.gameState = initialBoardState;
    }

    @Override
    public boolean join(Participant<BoardState, Move, PlayerType> visitor) {
        if (visitor == null)
            return false;
        //list of visitors
        int sizeBefore = participants.size();
        participants.add(visitor);
        int sizeAfter = participants.size();
        if (sizeAfter > sizeBefore) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean join(Player<BoardState, Move, PlayerType> player) {
        if (player == null)
            return false;
        //list of players
        int sizeBefore = players.size();
        if (players.size() == 2)
            return false;
        if (isRunning)
            return false;
        if (players.size() == 1 && players.get(0).equals(player))
            return false;
        for (Player<BoardState, Move, PlayerType> gamer : players) {
            if (gamer.getPlayerType().equals(player.getPlayerType())) {
                return false;
            }
        }
        players.add(player);
        int sizeAfter = players.size();
        if (sizeAfter > sizeBefore) {
            for (Participant participant : participants) {
                participant.notifyJoined(player.getPlayerType());
            }
            for (Player gamer : players) {
                gamer.notifyJoined(player.getPlayerType());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean leave(Participant<BoardState, Move, PlayerType> visitor) {
        if (visitor == null)
            return false;
        int sizeBefore = participants.size();
        participants.remove(visitor);
        int sizeAfter = participants.size();
        if (sizeAfter < sizeBefore) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean leave(Player<BoardState, Move, PlayerType> player) {
        if (player == null)
            return false;
        int sizeBefore = players.size();
        players.remove(player);
        int sizeAfter = players.size();
        if (sizeAfter < sizeBefore) {
            for (Player gamer : players) {
                gamer.notifyLeft(player.getPlayerType());
            }
            for (Participant participant : participants) {
                participant.notifyLeft(player.getPlayerType());
            }
            if (isRunning) {
                end();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<PlayerType> getWinner() {
//        if(isRunning)
//            return Optional.empty();
        if (!isStarted)
            return Optional.empty();
        if (players.isEmpty() && !rules.isEndState(gameState))
            return Optional.empty();
        if (players.size() == 1 && !rules.isEndState(gameState))
            return Optional.of(players.get(0).getPlayerType()); //in case a player left
        if (fail == false) { //in case of VALID move
            if (!rules.isEndState(gameState)) {
                return Optional.empty();
            } else {
                return rules.getWinner(gameState);
            }
        } else { //in case of INVALID move
            if (currentPlayer.getPlayerType() == PlayerType_ttt.CROSS) {
                return Optional.of((PlayerType) PlayerType_ttt.CROSS);
            } else {
                return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean update() {
        /*
        1. Verteilt den aktuellen Spielzustand an alle Spieler.
        2. Fordert den aktuellen Spieler auf einen Zug zu machen.
        3. Wendet den Zug auf das Spielfeld an.
        4. Falls der Zug ungültig war, soll das Spiel beendet werden, mit dem andern Spieler als Sieger.
        5. Informiert alle Spieler über gültigen Zug.
        6. Überprüft auf Spielende und beendet ggf. das Spiel.
         */
        //1.
        //System.out.println("1.");
        for (Player<BoardState, Move, PlayerType> player : players) {
            player.updateState(gameState);
        }
        for (Participant<BoardState, Move, PlayerType> participant : participants) {
            participant.updateState(gameState);
        }
        //2.
        //System.out.println("2.");
        Action<Move, PlayerType> nextAction = null;

        System.out.println("Your turn " + gameState.getActivePlayer() + "!");
        for (Player<BoardState, Move, PlayerType> gamer : players) {
            if (gamer.getPlayerType().equals(gameState.getActivePlayer())) {
                currentPlayer = gamer;
            }
        }
        nextAction = currentPlayer.requestAction();
        //3.
        //System.out.println("3.");
        gameState = rules.apply(gameState, nextAction);
        //4.
        //TODO ?? Evtl rules_impl.apply, siehe Vermerk dort - oder wenn requestAction null liefert
        if (gameState == null) {
            fail = true;
            end();
            return false;
        }
        //5.
        //System.out.println("5.");
        for (Player<BoardState, Move, PlayerType> player : players) {
            player.notifyAction(nextAction);
        }
        for (Participant<BoardState, Move, PlayerType> participant : participants) {
            participant.notifyAction(nextAction);
        }
        //6.
        //System.out.println("6.");
        if (!rules.isEndState(gameState)) {
            return true;
        } else {
            //ungefragt eingefügt, ausgabe des neuen boardstate nicht verlangt - evtl sogar falsch
            for (Player<BoardState, Move, PlayerType> player : players) {
                player.updateState(gameState);
            }
            // ------------
            end();
            return false;
        }
    }

    @Override
    public void end() {
        if (rules == null || isRunning == false) {
            for (Player<BoardState, Move, PlayerType> player : players) {
                player.notifyEnd(null);
            }
            for (Participant<BoardState, Move, PlayerType> participant : participants) {
                participant.notifyEnd(null);
            }
        } else {
            Optional<PlayerType> optWinner = getWinner();
            PlayerType winner;
            if (optWinner.isPresent()) {
                winner = optWinner.get();
            } else {
                winner = null;
            }
            for (Player<BoardState, Move, PlayerType> player : players) {
                player.notifyEnd(winner);
            }
            for (Participant<BoardState, Move, PlayerType> participant : participants) {
                participant.notifyEnd(winner);
            }
        }
        isRunning = false;
    }

    @Override
    public List<Player<BoardState, Move, PlayerType>> getPlayers() {
        return players;
    }

    @Override
    public List<Participant<BoardState, Move, PlayerType>> getVisitors() {
        return participants;
    }

    @Override
    public GameState<BoardState, PlayerType> getGameState() {
        return gameState;
    }
}
