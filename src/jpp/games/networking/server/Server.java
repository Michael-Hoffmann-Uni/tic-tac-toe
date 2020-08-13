package jpp.games.networking.server;

import jpp.games.networking.server.database.DatabaseAccess;
import jpp.games.networking.server.handler.game.RemoteIO;

import java.util.concurrent.ConcurrentMap;

public interface Server {

    DatabaseAccess getDatabaseImplementation();

    ConcurrentMap<String, RemoteIO> getRemoteIOs();
}
