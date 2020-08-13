package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.model.GameState;

public interface Solver<BoardState, Move, PlayerType> {

    /**
     * Erzeugt einen Solver, der immer zufällig einen Zug gibt.
     * @return
     */
    static <BoardState, Move, PlayerType> Solver<BoardState, Move, PlayerType> createRandomSolver(){
        return new Solver_Impl();
    }

    void setPlayerType(PlayerType playerType);

    PlayerType getPlayerType();

    void setRules(Rules<BoardState, Move, PlayerType> rules);

    /**
     * Informiert den Spieler, dass das Spielbrett einen neuen Zustand hat.
     *
     * @param gameState
     */
    void updateState(GameState<BoardState, PlayerType> gameState);

    /**
     * Fordert einen Zug vom Spieler an.
     * Diese Methode wird niemals vom Spiel aufgerufen, falls de Spieler ein Zuschauer ist, d.h.
     * dass getPlayerType null zurück gibt.
     *
     * @return
     */
    Action<Move, PlayerType> requestAction();


}
