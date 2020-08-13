package jpp.games.networking.server.handler.game;

import jpp.games.logic.Player;
import jpp.games.logic.Rules;
import jpp.games.model.Action;
import jpp.games.model.GameState;

import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

public class RemotePlayer<BoardState, Move, PlayerType> implements Player<BoardState, Move, PlayerType> {

    private Function<BoardState, String> boardFormatter;
    private Function<Action, String> actionFormatter;
    private Function<String, Action<Move, PlayerType>> actionParser;
    private PlayerType player;
    private Rules rules;
    private GameState gameState;
    private BoardState boardState;
    private String name;
    private RemoteIO remoteIO;

    public RemotePlayer(String name, RemoteIO remoteIO, Function<BoardState,
            String> boardFormatter, Function<String, Action<Move, PlayerType>> actionParser) {
        if (name == null || remoteIO == null || boardFormatter == null || actionParser == null)
            throw new NullPointerException("Argument was null: name, remoteIO, boardFormatter or actionParser.");
        this.name = name;
        this.remoteIO = remoteIO;
        this.boardFormatter = boardFormatter;
        this.actionParser = actionParser;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void setPlayerType(PlayerType playerType) {
        this.player = playerType;
        remoteIO.putOutput("You play " + player.toString());
    }

    @Override
    public PlayerType getPlayerType() {
        return player;
    }

    @Override
    public void setRules(Rules<BoardState, Move, PlayerType> rules) {
        this.rules = rules;
    }

    @Override
    public void updateState(GameState<BoardState, PlayerType> gameState) {
        this.gameState = gameState;
        this.boardState = gameState.getBoardState();
        remoteIO.putOutput("New BoardState:\n" + boardFormatter.apply(boardState));
    }

    @Override
    public Action<Move, PlayerType> requestAction() {
        remoteIO.putOutput("Please enter Action:\n");
        //Scanner scanner = new Scanner(System.in);
        Action<Move, PlayerType> nextAction = null;
        while (true) {
            nextAction = actionParser.apply(remoteIO.getInput());
            //nextAction = actionParser.apply(scanner.next());
            Set<Action<Move, PlayerType>> actionSet = rules.getActions(gameState, player);
            if (nextAction == null) {
                remoteIO.putOutput("Your Action was invalid. Please enter valid Action:\n");
            } else {
                for (Action<Move, PlayerType> action : actionSet) {
                    String move = action.getMove().toString();
                    String nextMove = nextAction.getMove().toString();
                    if (action.getMove().toString().equals(nextAction.getMove().toString())) {
                        return nextAction;
                    }
                }
                remoteIO.putOutput("Your Action was invalid. Please enter valid Action:\n");
            }
        }
    }

    @Override
    public void notifyJoined(PlayerType player) {
        remoteIO.putOutput(player.toString() + " joined the game!");
    }

    @Override
    public void notifyLeft(PlayerType player) {
        remoteIO.putOutput(player.toString() + " left the game!");
    }

    @Override
    public void notifyEnd(PlayerType player) {
        if (player != null) {
            remoteIO.putOutput(player.toString() + " won the game!");
        } else {
            remoteIO.putOutput("Game ended in draw!");
        }
    }

    @Override
    public void notifyAction(Action<Move, PlayerType> action) {
        PlayerType actionPlayer = (PlayerType) action.getPlayer();
        remoteIO.putOutput(actionPlayer.toString() + " made an action: " + formatAction(action) + "\n");
    }

    public void notifyStart(PlayerType otherPlayer) {
        remoteIO.putOutput("The game has started. Your opponent is: " + ((RemotePlayer) otherPlayer).getName());
    }

    private String formatAction(Action<Move, PlayerType> action) {
        return action.getPlayer() + " on " + action.getMove();
    }
}
