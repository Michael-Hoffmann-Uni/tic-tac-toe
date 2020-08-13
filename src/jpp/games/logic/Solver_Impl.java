package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.GameState;

import java.util.Random;
import java.util.Set;

public class Solver_Impl<BoardState, Move, PlayerType> implements Solver<BoardState, Move, PlayerType> {
    private PlayerType player;
    private BoardState boardstate;
    private Move move;
    private Rules rules;
    private GameState gameState;

    public Solver_Impl() {

    }

    @Override
    public void setPlayerType(PlayerType playerType) {
        this.player = playerType;
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
        this.boardstate = gameState.getBoardState();
    }

    @Override
    public Action<Move, PlayerType> requestAction() {
        Set<Action<Move, PlayerType>> possActions = rules.getActions(gameState, player);
        int size = possActions.size();
        if (size == 0)
            throw new IllegalStateException("No valid move possible.");
        Random rand = new Random();
        int choice = rand.nextInt(size);
        return (Action<Move, PlayerType>) possActions.toArray()[choice];
    }
}
