package jpp.games.tictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BoardState_ttt {
    final Map<Move_ttt, PlayerType_ttt> map;

    public BoardState_ttt() {
        map = new HashMap<Move_ttt, PlayerType_ttt>();
    }

    public BoardState_ttt(HashMap<Move_ttt, PlayerType_ttt> map) {
        this.map = map;
    }

    public BoardState_ttt put(PlayerType_ttt player, Move_ttt position) {
        map.put(position, player);
        return this;
    }

    public Map<Move_ttt, PlayerType_ttt> getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardState_ttt that = (BoardState_ttt) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
