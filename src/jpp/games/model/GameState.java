package jpp.games.model;

/**
 * Ein Spielstand vereint den Zusatnd des Spielbretts mit logischen Informationen,
 * die Aus dem Spielverlauf entstehen (zB. die Rundenzahl, Anzahl Punkte)
 * Der GameState sollte unveränderlich implementiert werden.
 */
public interface GameState<BoardState, PlayerType> {

    static <BoardState, PlayerType> GameState<BoardState, PlayerType> create(BoardState initialBoardState, PlayerType activePlayer){
        //throw new IllegalStateException("Not implemented yet!");
        if(initialBoardState == null || activePlayer == null)
            throw new NullPointerException("InitialBoardstate or activePlayer is null!");
        return (GameState<BoardState, PlayerType>) new GameState_Impl(initialBoardState, activePlayer);
    }

    int getRound();
    PlayerType getActivePlayer();
    BoardState getBoardState();
    GameState<BoardState, PlayerType> update(BoardState newBoardState, PlayerType activePlayer);

}
