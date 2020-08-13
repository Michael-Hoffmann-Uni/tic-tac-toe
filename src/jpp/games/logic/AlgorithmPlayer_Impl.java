package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.GameState;

public class AlgorithmPlayer_Impl<BoardState, Move, PlayerType> implements Player<BoardState, Move, PlayerType> {

    private Solver<BoardState, Move, PlayerType> solver;

    public AlgorithmPlayer_Impl(Solver solver) {
        if (solver == null)
            throw new NullPointerException("Solver is null.");
        this.solver = solver;
    }

    @Override
    public void setPlayerType(PlayerType playerType) {
        if (playerType == null)
            throw new NullPointerException("PlayerType is null.");
        solver.setPlayerType(playerType);
        System.out.println("Computer plays " + playerType.toString());
    }

    @Override
    public PlayerType getPlayerType() {
        return solver.getPlayerType();
    }

    @Override
    public void setRules(Rules<BoardState, Move, PlayerType> rules) {
        if (rules == null)
            throw new NullPointerException("Rules is null.");
        solver.setRules(rules);
    }

    @Override
    public void updateState(GameState<BoardState, PlayerType> gameState) {
        solver.updateState(gameState);
    }

    @Override
    public Action<Move, PlayerType> requestAction() {
        System.out.println("Solver is now choosing an action.");
        return solver.requestAction();
    }

    @Override
    public void notifyJoined(PlayerType player) {
        //optional
        //System.out.println("AlgoPlayer " + player.toString() + " joined the game.");
    }

    @Override
    public void notifyLeft(PlayerType player) {
        //optional
        //System.out.print("AlgoPlayer " + player.toString() + " left.");
    }

    @Override
    public void notifyEnd(PlayerType player) {
        //optional
        if (player == null) {
            //System.out.print("Game ended in draw!");
        } else {
            //System.out.print("AlgoPlayer " + player.toString() + " won the game.");
        }
    }

    @Override
    public void notifyAction(Action<Move, PlayerType> action) {
        //optional
        PlayerType player = action.getPlayer();
        Move move = action.getMove();
        //System.out.print("AlgoPlayer " + player.toString() + " made a move: " + move.toString());
    }
}
