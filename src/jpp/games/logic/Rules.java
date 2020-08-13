package jpp.games.logic;

import jpp.games.model.*;

import java.util.Optional;
import java.util.Set;

/**
 * Beschreibt wie ein Spielbrett verändert werden kann und wie diese Veränderung ausgeführt wird.
 */
public interface Rules<BoardState, Move, PlayerType> {

    /**
     * Wende den Zug auf das BoardState an.
     * Da Spielbretter unveränderlich sind soll sich die
     * Änderung nur im zurckgegebenen Spielbrett wiederfinden.
     *
     * @param gameState
     * @param action
     * @return null falls der Zug nicht möglich war, weil illegal, null, nicht vorhanden, etc.
     */
    GameState<BoardState, PlayerType> apply(GameState<BoardState, PlayerType> gameState, Action<Move, PlayerType> action);

    /**
     *
     * @param gameState
     * @param player
     * @return ein Set mit Zügen, die der Spieler auf das Spielfeld ausführen könnte.
     */
    Set<Action<Move, PlayerType>> getActions(GameState<BoardState, PlayerType> gameState, PlayerType player);

    /**
     * Gibt true zurück, wenn das Spiel zuende ist.
     *
     * @param gameState
     * @return
     */
    boolean isEndState(GameState<BoardState, PlayerType> gameState);

    /**
     * Gibt den Gewinner zurück, falls es einen gibt.
     * @param gameState
     * @return
     */
    Optional<PlayerType> getWinner(GameState<BoardState, PlayerType> gameState);

    /**
     * Erzeugt den Ausgangszustand des Spiels.
     *
     * @return
     */
    GameState<BoardState, PlayerType> getInitialGameState();
}
