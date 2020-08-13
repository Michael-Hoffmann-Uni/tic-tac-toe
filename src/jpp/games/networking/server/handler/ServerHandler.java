package jpp.games.networking.server.handler;

import jpp.games.networking.server.Server;
import jpp.games.networking.server.database.DatabaseAccess;
import jpp.games.networking.server.database.DatabaseAccess_Impl;
import jpp.games.networking.server.handler.game.RemoteIO;
import jpp.games.networking.server.handler.game.RemoteIO_Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@RestController
public class ServerHandler {
    private Server server;
    private DatabaseAccess database;
    private ConcurrentMap<String, RemoteIO> remoteIOs;

    @Autowired
    public ServerHandler(Server server) {
        this.server = server;
        this.database = server.getDatabaseImplementation();
        this.remoteIOs = server.getRemoteIOs();
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestParam(name = "name") String username) {
        Optional user = database.createUser(username);
        if (user.isPresent() && username.length() >= 2) {
            String key = (String) user.get();
            remoteIOs.put(username, new RemoteIO_Impl());
            return new ResponseEntity<String>(key, HttpStatus.ACCEPTED);
        } else {
            throw new IllegalArgumentException("This username already exists or username is too short.");
        }
    }

    @GetMapping("/highscore")
    public ResponseEntity<String> getHighScore() {
        String highScore = database.getHighscoreList();
        return new ResponseEntity<String>(highScore, HttpStatus.ACCEPTED);
    }

    @GetMapping("/score")
    public ResponseEntity<String> getScore(@RequestParam(name = "name") String username) {
        if (database.getScore(username).isPresent()) {
            String score = database.getScore(username).get().toString();
            String scoreAndName = score + "\t" + username;
            return new ResponseEntity<String>(scoreAndName, HttpStatus.ACCEPTED);
        } else {
            throw new IllegalArgumentException("A player with this name does not exist.");
        }
    }
}
