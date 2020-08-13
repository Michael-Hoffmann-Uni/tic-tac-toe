package jpp.games.networking.server;

import jpp.games.networking.server.database.DatabaseAccess;
import jpp.games.networking.server.database.DatabaseAccess_Impl;
import jpp.games.networking.server.handler.game.RemoteIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SpringBootApplication
public class Server_Impl implements Server {

    private DatabaseAccess databaseImp;
    private ConcurrentMap<String, RemoteIO> remoteIOs;

    @Autowired
    public Server_Impl() {
        databaseImp = new DatabaseAccess_Impl();
        remoteIOs = new ConcurrentHashMap<>();
    }

    @Override
    public DatabaseAccess getDatabaseImplementation() {
        return databaseImp;
    }

    @Override
    public ConcurrentMap<String, RemoteIO> getRemoteIOs() {
        return remoteIOs;
    }
}
