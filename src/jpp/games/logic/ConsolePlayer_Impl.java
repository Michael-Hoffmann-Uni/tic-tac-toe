package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.GameState;
import jpp.games.tictactoe.BoardState_ttt;
import jpp.games.tictactoe.PlayerType_ttt;

import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

public class ConsolePlayer_Impl<BoardState, Move, PlayerType> implements Player<BoardState, Move, PlayerType> {

    private Function<BoardState, String> boardFormatter;
    private Function<Action, String> actionFormatter;
    private Function<String, Action> actionParser;
    private PlayerType player;
    private Rules rules;
    private GameState gameState;
    private BoardState boardState;

    public ConsolePlayer_Impl(Function<BoardState, String> boardFormatter, Function<Action,
            String> actionFormatter, Function<String, Action> actionParser) {
        if (boardFormatter == null || actionFormatter == null || actionParser == null)
            throw new NullPointerException("One of the arguments was null (boardFormatter, actionFormatter, actionParser).");
        this.boardFormatter = boardFormatter;
        this.actionFormatter = actionFormatter;
        this.actionParser = actionParser;
    }

    @Override
    public void setPlayerType(PlayerType playerType) {
        this.player = playerType;
        System.out.println("You play " + player.toString());
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
        System.out.print("New BoardState:\n" + boardFormatter.apply(boardState) + "\n");
    }

    @Override
    public Action<Move, PlayerType> requestAction() {
        System.out.print("Please enter Action:\n");
        Scanner scanner = new Scanner(System.in);
        Action<Move, PlayerType> nextAction = null;
        Set<Action<Move, PlayerType>> actionSet = rules.getActions(gameState, player);
        while (/*scanner.hasNext()*/ true) {
            nextAction = actionParser.apply(scanner.next());
            if (nextAction != null) {
                if (actionSet.contains(nextAction))
                    return nextAction;
            }
            System.out.print("Your Action was invalid. Please enter valid Action:\n");
        }
    }

    @Override
    public void notifyJoined(PlayerType player) {
        System.out.println(player.toString() + " joined the game!");
    }

    @Override
    public void notifyLeft(PlayerType player) {
        System.out.println(player.toString() + " left the game!");
    }

    @Override
    public void notifyEnd(PlayerType player) {
        if (player != null) {
            System.out.println(player.toString() + " won the game!");
        } else {
            System.out.println("Game ended in draw!");
        }
    }

    @Override
    public void notifyAction(Action<Move, PlayerType> action) {
        PlayerType actionPlayer = (PlayerType) action.getPlayer();
        System.out.println(actionPlayer.toString() + " made an action: " + actionFormatter.apply(action));
    }
}
