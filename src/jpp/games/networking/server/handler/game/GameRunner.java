package jpp.games.networking.server.handler.game;

import jpp.games.logic.Game;

import java.util.function.Consumer;

public class GameRunner<BoardState, Move, PlayerType> implements Runnable {
    private String gameId;
    private Game<BoardState, Move, PlayerType> game;
    private Consumer<String> gameEndCallback;

    public GameRunner(String gameId, Game<BoardState, Move, PlayerType> game, Consumer<String> gameEndCallback) {
        if (game == null || gameId == null || gameEndCallback == null) {
            throw new NullPointerException("gameId, game, or gameEndCallback is null.");
        } else {
            this.gameId = gameId;
            this.game = game;
            this.gameEndCallback = gameEndCallback;
        }
    }

    @Override
    public void run() {
        while (game.update()) ;
        gameEndCallback.accept(gameId);
    }
}
