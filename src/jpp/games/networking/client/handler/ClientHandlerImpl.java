package jpp.games.networking.client.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

public class ClientHandlerImpl implements ClientHandler {

    private UriComponentsBuilder base;
    private RestTemplate restTemplate;
    private String accessKey = "";

    private UriComponentsBuilder getBase() {
        return base.cloneBuilder();
    }

    public ClientHandlerImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void setServerUri(String serverUri) {
        this.base = UriComponentsBuilder.fromUriString(serverUri);
    }

    @Override
    public String registerUser(String username) throws HttpClientErrorException {
        String uri = getBase().path("register").queryParam("name", username).toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        accessKey = response.getBody();
        return accessKey;
    }

    @Override
    public String createGame(String gameId) throws HttpClientErrorException {
        String uri = getBase().path(accessKey).path("/create").queryParam("gameId", gameId).toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    @Override
    public String joinGame(String gameId) throws HttpClientErrorException {
        String uri = getBase().path(accessKey).path("/join").queryParam("gameId", gameId).toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    @Override
    public String leaveGame() throws HttpClientErrorException {
        String uri = getBase().path(accessKey).path("/leave").toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    @Override
    public String getAvailableGames() throws HttpClientErrorException {
        String uri = getBase().path(accessKey).path("/listGames").toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String gameList = response.getBody();
        return gameList != null ? gameList : "";
    }

    @Override
    public String getUpdate() throws HttpClientErrorException {
        String uri = getBase().path(accessKey).path("/getUpdate").toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    @Override
    public String makeMove(String move) throws HttpClientErrorException {
        String uri = getBase().path(accessKey).path("/makeMove").queryParam("move", move).toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    @Override
    public String getHighscoreList() throws HttpClientErrorException {
        String uri = getBase().path("/highscore").toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    @Override
    public String getScore(String name) throws HttpClientErrorException {
        String uri = getBase().path("/score").queryParam("name", name).toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }
}
