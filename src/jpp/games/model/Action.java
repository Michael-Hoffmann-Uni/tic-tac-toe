package jpp.games.model;

/**
 * Um einen Spielzug zu beschreiben, definieren wir das Action-Interface.
 * Eine Action hat jeweils ein PlayerType (der Spieler der die Action ausführt)
 * un eine Zielposition.
 */
public interface Action<Move, PlayerType> {

    static <Move, PlayerType> Action<Move, PlayerType> create(PlayerType playerType, Move move){
        //throw new IllegalStateException("Not implemented yet!");
        if(playerType == null || move == null)
            throw new NullPointerException("playerType or move is null!");
        return (Action<Move, PlayerType>) new Action_Impl(move, playerType);
    }

    PlayerType getPlayer();
    Move getMove();
}
