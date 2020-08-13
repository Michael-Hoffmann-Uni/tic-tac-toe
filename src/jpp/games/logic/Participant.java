package jpp.games.logic;

import jpp.games.model.*;

import java.util.function.Function;

/**
 * Damit der Spielzustadn auch ausgelegsen werden kann braucht es diese Klasse.
 * Diese Klasse beschreibt einen Spielteilnehmer oder Zuschauer.
 * Instancen dieser Klasse können am Spiel teilnehmen.
 * Die Informationen über das Spiel werden durch die Game Klasse verterteilt und
 * an de Spieler über die vorgegeben Methoden weiter geleitet.
 * Mehr Informationen stehen dem Spieler nicht zur Verfügung.
 *
 */
public interface Participant<BoardState, Move, PlayerType> {

    static <BoardState, Move, PlayerType> Participant<BoardState, Move, PlayerType> createConsoleVisitor(Function<BoardState, String> boardFormatter, Function<Action<Move, PlayerType>, String> actionFormatter){
        //throw new IllegalStateException("Not implemented yet!");
        if(boardFormatter == null || actionFormatter == null)
            throw new NullPointerException("boardFormatter or actionFormatter is null.");
        return new Participant_Impl<BoardState, Move, PlayerType>(boardFormatter, actionFormatter);
        //return null;
        //TODO ActionFormatter -> actionFormatter geändert , hoffe das war okay
    }

    /**
     * Informiert den Spieler, dass das Spielbrett einen neuen Zustand hat.
     *
     * @param gameState
     */
    void updateState(GameState<BoardState, PlayerType> gameState);

    /**
     * Ein Spieler oder Zuschauer hat das Spiel betreten.
     *
     * @param player
     */
    void notifyJoined(PlayerType player);

    /**
     * Ein Spieler oder Zuschauer hat das Spiel verlassen.
     *
     * @param player
     */
    void notifyLeft(PlayerType player);

    /**
     * Es gab einen Spielsieger, und das Spiel ist beendet.
     *
     * @param player null falls Unentschieden
     */
    void notifyEnd(PlayerType player);

    /**
     * Ein Zug wurde vom angegeben Spieler ausgeführt.
     * Ein neuer Spielstand wird durch updateGameState übertragen werden,
     * falls das Spiel nicht durch den Zug beendet wurde.
     *
     * @param action
     */
    void notifyAction(Action<Move, PlayerType> action);

}
