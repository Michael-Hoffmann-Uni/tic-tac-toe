package jpp.games;


import jpp.games.logic.*;
import jpp.games.model.*;
import jpp.games.tictactoe.Adapter;

public final class TicTacToe<BoardState, Move, PlayerType> {

    public static void main(String[] args) {
        new TicTacToe<>(new Adapter<>()).start();
    }

    private final Adapter<BoardState, Move, PlayerType> adapter;

    public TicTacToe(Adapter<BoardState, Move, PlayerType> adapter) {
        this.adapter = adapter;
    }

    private void start() {
        Game<BoardState, Move, PlayerType> gameLogic = Game.createTwoPlayerGame();
        Rules<BoardState, Move, PlayerType> boardLogic = adapter.rules();

        Player<BoardState, Move, PlayerType> player1 = Player.createConsolePlayer(
                this::formatBoard, TicTacToe::formatAction, this::formatInput);
        Player<BoardState, Move, PlayerType> player2 = Player.createAlgorithmPlayer(Solver.createRandomSolver());

        Participant<BoardState, Move, PlayerType> visitor = Participant.createConsoleVisitor(
                this::formatBoard,
                TicTacToe::formatAction);

        player1.setPlayerType(adapter.party("CROSS"));
        player2.setPlayerType(adapter.party("CIRCLE"));

        gameLogic.join(player1);
        gameLogic.join(player2);
        gameLogic.join(visitor);
        gameLogic.start(boardLogic, GameState.create(adapter.emptyBoard(), adapter.party("CROSS")));
        while (gameLogic.update()) ;
        System.out.println("the winner is " + gameLogic.getWinner().orElse(null));
        gameLogic.leave(player1);
        gameLogic.leave(player2);
    }


    private static <Move, PlayerType> String formatAction(Action<Move, PlayerType> action) {
        return action.getPlayer() + " on " + action.getMove();
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

    //TODO endless loop if only enter is hit... maybe timeout here
    private Action<Move, PlayerType> formatInput(String pos) {
        try {
            return Action.create(adapter.party("CROSS"), adapter.position(pos));
        } catch (Exception ex) {
        }
        return null;
    }
}
