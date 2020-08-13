package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.Action_Impl;
import jpp.games.model.GameState;
import jpp.games.model.GameState_Impl;
import jpp.games.tictactoe.BoardState_ttt;
import jpp.games.tictactoe.Move_ttt;
import jpp.games.tictactoe.PlayerType_ttt;

import java.util.*;

public class Rules_Impl<BoardState, Move, PlayerType> implements Rules<BoardState, Move, PlayerType> {

    @Override
    public GameState<BoardState, PlayerType> apply(GameState<BoardState, PlayerType> gameState, Action<Move, PlayerType> action) {
        if (gameState == null || action == null)
            throw new NullPointerException("GameState or action is null.");
        BoardState_ttt oldBoardState = (BoardState_ttt) gameState.getBoardState();
        PlayerType_ttt oldPlayerType = (PlayerType_ttt) gameState.getActivePlayer();
        PlayerType_ttt newPlayerType;
        if (oldPlayerType.equals(PlayerType_ttt.CIRCLE)) {
            newPlayerType = PlayerType_ttt.CROSS;
        } else {
            newPlayerType = PlayerType_ttt.CIRCLE;
        }
        HashMap<Move_ttt, PlayerType_ttt> newMap = new HashMap<>(oldBoardState.getMap());

        if (containsMove((Move_ttt) action.getMove()) == true && newMap.containsKey((Move_ttt) action.getMove()) == false) {
            newMap.put((Move_ttt) action.getMove(), (PlayerType_ttt) action.getPlayer());
            return gameState.update((BoardState) new BoardState_ttt(newMap), (PlayerType) newPlayerType);
        } else {
            return null;
        }
    }

    @Override
    public Set<Action<Move, PlayerType>> getActions(GameState<BoardState, PlayerType> gameState, PlayerType player) {
        BoardState_ttt boardState = (BoardState_ttt) gameState.getBoardState();
        Map<Move_ttt, PlayerType_ttt> boardMap = boardState.getMap();

        //create set with all moves from Moves_ttt with given player
        Set<Action<Move, PlayerType>> allActionsSet = new HashSet<>();
        for (Move_ttt move : Move_ttt.values()) {
            allActionsSet.add(new Action_Impl(move, player));
        }

        //create returnActionSet
        Set<Action<Move, PlayerType>> returnActionsSet = new HashSet<>();

        //put those actions from allActionSet that are not used on the board into returnActionSet
        boolean alreadyTaken;
        for (Action<Move, PlayerType> action : allActionsSet) {
            alreadyTaken = false;
            for (Map.Entry<Move_ttt, PlayerType_ttt> entry : boardMap.entrySet()) {
                if (action.getMove().toString().equals(entry.getKey().toString())) {
                    alreadyTaken = true;
                }
            }
            if (!alreadyTaken) {
                returnActionsSet.add(action);
            }
        }
        return returnActionsSet;
    }

    @Override
    public boolean isEndState(GameState<BoardState, PlayerType> gameState) {
        if (gameState.getRound() == 9)
            return true;
        if (!getWinner(gameState).equals(Optional.empty()))
            return true;
        return false;
    }

    @Override
    public Optional<PlayerType> getWinner(GameState<BoardState, PlayerType> gameState) {
        BoardState_ttt boardState = (BoardState_ttt) gameState.getBoardState();
        HashMap<Move_ttt, PlayerType_ttt> map = new HashMap<>(boardState.getMap());

        //WINNER CROSS
        //Row 1 Cross
        if (map.get(Move_ttt.R1C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R1C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R1C3) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //Row 2 Cross
        if (map.get(Move_ttt.R2C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C3) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //Row 3 Cross
        if (map.get(Move_ttt.R3C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R3C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R3C3) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //Col 1 Cross
        if (map.get(Move_ttt.R1C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R3C1) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //Col 2 Cross
        if (map.get(Move_ttt.R1C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R3C2) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //Col 3 Cross
        if (map.get(Move_ttt.R1C3) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C3) == PlayerType_ttt.CROSS && map.get(Move_ttt.R3C3) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //diagonal left up to right low Cross
        if (map.get(Move_ttt.R1C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R3C3) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }
        //diagonal left low to right up Cross
        if (map.get(Move_ttt.R3C1) == PlayerType_ttt.CROSS && map.get(Move_ttt.R2C2) == PlayerType_ttt.CROSS && map.get(Move_ttt.R1C3) == PlayerType_ttt.CROSS) {
            return Optional.of((PlayerType) PlayerType_ttt.CROSS);
        }

        //WINNER CIRCLE
        //Row 1 Circle
        if (map.get(Move_ttt.R1C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R1C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R1C3) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //Row 2 Circle
        if (map.get(Move_ttt.R2C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C3) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //Row 3 Circle
        if (map.get(Move_ttt.R3C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R3C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R3C3) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //Col 1 Circle
        if (map.get(Move_ttt.R1C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R3C1) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //Col 2 Circle
        if (map.get(Move_ttt.R1C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R3C2) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //Col 3 Circle
        if (map.get(Move_ttt.R1C3) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C3) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R3C3) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //diagonal left up to right low Circle
        if (map.get(Move_ttt.R1C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R3C3) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }
        //diagonal left low to right up Circle
        if (map.get(Move_ttt.R3C1) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R2C2) == PlayerType_ttt.CIRCLE && map.get(Move_ttt.R1C3) == PlayerType_ttt.CIRCLE) {
            return Optional.of((PlayerType) PlayerType_ttt.CIRCLE);
        }

        return Optional.empty();
    }

    @Override
    public GameState<BoardState, PlayerType> getInitialGameState() {
        return (GameState) new GameState_Impl<BoardState, PlayerType>((BoardState) new BoardState_ttt(), (PlayerType) PlayerType_ttt.CROSS);
    }

    public boolean containsMove(Move_ttt test) {
        for (Move_ttt moveTtt : Move_ttt.values()) {
            if (moveTtt.name().equals(test.name()))
                return true;
        }
        return false;
    }
}
