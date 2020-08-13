package jpp.games.logic;

import jpp.games.model.GameState;

import java.util.List;
import java.util.Optional;

/**
 * Ein Spiel ist eine Klasse, die dafür zuständig ist den gesamten Spielzusatnd zu verwalten.
 * Dazu zählen zum Beispiel Spieler, Figuren, Rundenzahl, etc.
 * Diese Klasse fordert neue Spielzüge von den Spielern an, verteilt Spielstände und verwaltet allgemeine
 * Eigenschaften des Spiels, wie den Spielsieger.
 */
public interface Game<BoardState, Move, PlayerType> {

    
    /**
     * Erzeugt eine Instanz eines neuen Spiel fuer zwei Spieler.
     */
    static <BoardState, Move, PlayerType> Game<BoardState, Move, PlayerType> createTwoPlayerGame(){
        return new Game_Impl<>();
    }

    /**
     * Startet ein neues Spiel.
     * D.h. dass das Spielbrett auf Anfangszustand gesetzt wird.
     * Das Spiel darf nicht bereits laufen.
     *
     * @param rules beschreibt das Verhalten des Spielbretts.
     */
    void start(Rules<BoardState, Move, PlayerType> rules, GameState<BoardState, PlayerType> initialBoardState);

    /**
     * Fügt einen Beobachter dem Spiel hinzu.
     * Es wird nicht gefordert, dass diese Methode während des Spiels neue Spieler zulassen muss.
     *
     * @param visitor
     * @return true falls die Aktion erfolgreich war
     */
    boolean join(Participant<BoardState, Move, PlayerType> visitor);

    /**
     * Fügt einen Spieler dem Spiel hinzu.
     * Es wird nicht gefordert, dass diese Methode während des Spiels neue Spieler zulassen muss.
     *
     * @param visitor
     * @return true falls die Aktion erfolgreich war
     */
    boolean join(Player<BoardState, Move, PlayerType> player);


    /**
     * Entfernt einen Zuschauer vom Spiel.
     *
     * @param visitor
     * @return true falls die Aktion erfolgreich war
     */
    boolean leave(Participant<BoardState, Move, PlayerType> visitor);

    /**
     * Entfernt einen Spieler vom Spiel.
     * Wenn das Spiel läuft, verliert dieser Spieler.
     *
     * @param player
     * @return true falls die Aktion erfolgreich war
     */
    boolean leave(Player<BoardState, Move, PlayerType> player);

    /**
     * Gibt den Gewinner des letztn Spiels zurück.
     * Es ist nicht definiert, was zurück gegeben wird, während ein Spiel läuft.
     *
     * @return the winner or null if draw or not ended yet.
     */
    Optional<PlayerType> getWinner();

    /**
     *
     * @return wahr, falls das Spiel gerade läuft
     */
    boolean isRunning();

    /**
     * Führt eine Aktualisierung des Spielzustandes aus.
     * D.h. dass im single threaded modus ein Spielzug angefordert, ausgewertet und der Spielstand aktualisiert wird.
     * Dann ist die Methode blockierend.
     *  1. Verteile board state
     *  2. fordere Zug an
     *  3. üperprüfe Spielende
     *
     * @return wahr, wenn das Spiel noch nicht beendet ist.
     */
    boolean update();

    /**
     * Beendet das Spiel.
     * Es wird auf Siegpositionen geprüft und den Gewinner gesetzt.
     * Und alle benachrichtigt.
     */
    void end();
    
    
    /**
     * Gibt eine Ansicht der aktuellen Spieler zurueck.
     */
    List<Player<BoardState, Move, PlayerType>> getPlayers();
    
    /**
     * Gibt eine Ansicht der aktuellen Beobachter zurueck.
     */
    List<Participant<BoardState, Move, PlayerType>> getVisitors();
    
    /**
     * Gibt eine Ansicht des aktuellen Spielstands
     */
    GameState<BoardState, PlayerType> getGameState();
}
