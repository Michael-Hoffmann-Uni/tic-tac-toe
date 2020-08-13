package jpp.games.networking.client.handler;

import org.springframework.web.client.HttpClientErrorException;

public interface ClientHandler {
    void setServerUri(String serverUri);

    String registerUser(String username) throws HttpClientErrorException;

    String createGame(String gameId) throws HttpClientErrorException;

    String joinGame(String gameId) throws HttpClientErrorException;

    String leaveGame() throws HttpClientErrorException;

    String getAvailableGames() throws HttpClientErrorException;

    String getUpdate() throws HttpClientErrorException;

    String makeMove(String move) throws HttpClientErrorException;

    String getHighscoreList() throws HttpClientErrorException;

    String getScore(String name) throws HttpClientErrorException;
}
