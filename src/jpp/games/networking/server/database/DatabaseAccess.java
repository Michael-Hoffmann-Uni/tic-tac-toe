package jpp.games.networking.server.database;

import java.util.Optional;

public interface DatabaseAccess {

    static DatabaseAccess createDatabaseAccess() {
        return new DatabaseAccess_Impl();
    }

    Optional<String> createUser(String name);

    Optional<String> getName(String key);

    Optional<Integer> increaseScore(String name);

    Optional<Integer> getScore(String name);

    String getHighscoreList();
}
