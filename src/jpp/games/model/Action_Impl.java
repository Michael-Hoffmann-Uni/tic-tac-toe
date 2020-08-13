package jpp.games.model;

import java.util.Objects;

public final class Action_Impl<Move, PlayerType> implements Action<Move, PlayerType> {
    final private PlayerType player;
    final private Move move;

    public Action_Impl(Move move, PlayerType player) {
        if (move == null || player == null)
            throw new NullPointerException("Move or player is null.");
        this.player = player;
        this.move = move;
    }

    @Override
    public PlayerType getPlayer() {
        return player;
    }

    @Override
    public Move getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action_Impl<?, ?> action_ = (Action_Impl<?, ?>) o;
        return Objects.equals(player, action_.player) &&
                Objects.equals(move, action_.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, move);
    }


}
