package jpp.games.tictactoe;

import jpp.games.logic.Rules;
import jpp.games.logic.Rules_Impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class Adapter<BoardState, Move, PlayerType> {

    public Rules<BoardState, Move, PlayerType> rules() {
        //throw new IllegalStateException("Not implemented yet!");
        return (Rules<BoardState, Move, PlayerType>) new Rules_Impl();
    }

    public PlayerType party(String name) {
        if (name == null)
            throw new NullPointerException("Argument name is null");
        //throw new IllegalStateException("Not implemented yet!");
        if (name.equals("CROSS")) {
            return (PlayerType) PlayerType_ttt.CROSS;
        } else if (name.equals("CIRCLE")) {
            return (PlayerType) PlayerType_ttt.CIRCLE;
        } else {
            throw new IllegalArgumentException("This name is not a valid player-type!");
        }
    }

    public Move position(String name) {
        //throw new IllegalStateException("Not implemented yet!");
        if (name == null)
            throw new NullPointerException("Argument name is null!");

        switch (name) {
            case "R1C1":
                return (Move) Move_ttt.R1C1;
            case "R1C2":
                return (Move) Move_ttt.R1C2;
            case "R1C3":
                return (Move) Move_ttt.R1C3;
            case "R2C1":
                return (Move) Move_ttt.R2C1;
            case "R2C2":
                return (Move) Move_ttt.R2C2;
            case "R2C3":
                return (Move) Move_ttt.R2C3;
            case "R3C1":
                return (Move) Move_ttt.R3C1;
            case "R3C2":
                return (Move) Move_ttt.R3C2;
            case "R3C3":
                return (Move) Move_ttt.R3C3;
            default:
                throw new IllegalArgumentException("The argument is not a valid move.");
        }
    }

    public BoardState emptyBoard() {
        return (BoardState) new BoardState_ttt();
    }

    public Set<Move> getPositions(BoardState state, PlayerType playerType) {
        if (state == null || playerType == null) {
            throw new NullPointerException("State or playerType is null");
        }
        //throw new IllegalStateException("Not implemented yet!");
        Set<Move> positions = new HashSet<>();
        for (Map.Entry<Move, PlayerType> entry : this.getMarks(state).entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(playerType)) {
                positions.add(entry.getKey());
            }
        }
        return positions;
    }

    public Map<Move, PlayerType> getMarks(BoardState state) {
        //throw new IllegalStateException("Not implemented yet!");
        BoardState_ttt boardState = (BoardState_ttt) state;
        return (Map<Move, PlayerType>) boardState.getMap();
    }

    public boolean isEmpty(BoardState state, Move position) {
        //throw new IllegalStateException("Not implemented yet!");
        BoardState_ttt boardState = (BoardState_ttt) state;
        Map<Move, PlayerType> boardMap = (Map<Move, PlayerType>) ((BoardState_ttt) state).getMap();
        if (boardMap.get(position) == null) {
            return true;
        } else {
            return false;
        }
    }

    public BoardState put(BoardState state, PlayerType player, Move position) {
        //throw new IllegalStateException("Not implemented yet!");
        BoardState_ttt boardState = (BoardState_ttt) state;
        return (BoardState) boardState.put((PlayerType_ttt) player, (Move_ttt) position);
    }
}
