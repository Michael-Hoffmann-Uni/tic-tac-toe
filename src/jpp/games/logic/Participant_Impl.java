package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.GameState;
import jpp.games.tictactoe.BoardState_ttt;
import jpp.games.tictactoe.Move_ttt;
import jpp.games.tictactoe.PlayerType_ttt;

import java.util.function.Function;

public class Participant_Impl<BoardState, Move, PlayerType> implements Participant<BoardState, Move, PlayerType> {

    private Function<Action<Move, PlayerType>, String> actionFormatter;
    private Function<BoardState, String> boardFormatter;

    public Participant_Impl(Function<BoardState, String> boardFormatter, Function<Action<Move, PlayerType>, String> actionFormatter) {
        this.actionFormatter = actionFormatter;
        this.boardFormatter = boardFormatter;
    }

    @Override
    public void updateState(GameState gameState) {
        System.out.print("New BoardState:\n" + boardFormatter.apply((BoardState) gameState.getBoardState()) + "\n");
    }

    @Override
    public void notifyJoined(PlayerType player) {
        System.out.println(player + " joined the game!");
    }

    @Override
    public void notifyLeft(PlayerType player) {
        System.out.println(player + " left the game!");
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
    public void notifyAction(Action action) {
        PlayerType player = (PlayerType) action.getPlayer();
        System.out.print(player.toString() + " made an action: " + actionFormatter.apply(action) + "\n");
    }
}
