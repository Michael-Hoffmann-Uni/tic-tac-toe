package jpp.games.logic;

import jpp.games.model.Action;
import jpp.games.networking.server.handler.game.RemoteIO;
import jpp.games.networking.server.handler.game.RemotePlayer;

import java.util.function.Function;

public interface Player<BoardState, Move, PlayerType> extends Participant<BoardState, Move, PlayerType>, Solver<BoardState, Move, PlayerType> {

    static <BoardState, Move, PlayerType> Player<BoardState, Move, PlayerType> createAlgorithmPlayer(Solver<BoardState, Move, PlayerType> solver){
        return new AlgorithmPlayer_Impl(solver);
    }

    static <BoardState, Move, PlayerType> Player<BoardState, Move, PlayerType> createConsolePlayer(Function<BoardState, String> boardFormatter,
                                      Function<Action<Move, PlayerType>, String> actionFormatter,
                                      Function<String, Action<Move, PlayerType>> actionParser){
        return new ConsolePlayer_Impl(boardFormatter, actionFormatter, actionParser);
    }

    static <BoardState, Move, PlayerType> Player<BoardState, Move, PlayerType> createRemotePlayer(String userName,
                                                                                                  RemoteIO remoteIO,
                                                                                                  Function<BoardState, String> boardFormatter,
                                                                                                  Function<String, Action<Move, PlayerType>> ActionParser){
        return new RemotePlayer<>(userName, remoteIO, boardFormatter, ActionParser);
    }
}
