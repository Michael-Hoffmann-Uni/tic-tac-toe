package jpp.games.model;

import java.util.Objects;
import java.util.Set;

public final class GameState_Impl<BoardState, PlayerType> implements GameState<BoardState, PlayerType> {
    private final int round;
    private final PlayerType activePlayer;
    private final BoardState currentBoardState;

    public GameState_Impl(BoardState initialBoardState, PlayerType activePlayer) {
        this.currentBoardState = initialBoardState;
        this.activePlayer = activePlayer;
        this.round = 0;
    }

    public GameState_Impl(BoardState initialBoardState, PlayerType activePlayer, int round) {
        this.currentBoardState = initialBoardState;
        this.activePlayer = activePlayer;
        this.round = round;
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public PlayerType getActivePlayer() {
        return activePlayer;
    }

    @Override
    public BoardState getBoardState() {
        return currentBoardState;
    }

    @Override
    public GameState<BoardState, PlayerType> update(BoardState newBoardState, PlayerType activePlayer) {
        return new GameState_Impl<BoardState, PlayerType>(newBoardState, activePlayer, this.round + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState_Impl<?, ?> that = (GameState_Impl<?, ?>) o;
        return round == that.round &&
                Objects.equals(activePlayer, that.activePlayer) &&
                Objects.equals(currentBoardState, that.currentBoardState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, activePlayer, currentBoardState);
    }
}
