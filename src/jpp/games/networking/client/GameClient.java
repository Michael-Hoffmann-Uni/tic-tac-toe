package jpp.games.networking.client;

import jpp.games.networking.client.handler.ClientHandlerImpl;
import jpp.games.networking.client.handler.ClientHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class GameClient {
    private final String clientName;

    private ClientHandler clientHandler;
    private SimpleConsole console;

    public GameClient(String clientName, SimpleConsole console) throws HttpClientErrorException {
        this.clientName = clientName;

        clientHandler = new ClientHandlerImpl();
        clientHandler.setServerUri(UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(61361).toUriString());
        clientHandler.registerUser(clientName);

        this.console = console;
    }

    public void listGames() {
        try {
            String games = clientHandler.getAvailableGames();
            console.showServerAnswer("Games:\n" + games);
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    public void highscoreList() {
        try {
            String games = clientHandler.getHighscoreList();
            console.showServerAnswer("Highscore:\n" + games);
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    public void getScore(String name) {
        try {
            console.showServerAnswer(clientHandler.getScore(name));
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    public void joinGame(String gameId) {
        try {
            clientHandler.joinGame(gameId);
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    public void leaveGame() {
        try {
            console.showServerAnswer(clientHandler.leaveGame());
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    public void createGame(String gameId) {
        try {
            console.showServerAnswer(clientHandler.createGame(gameId));
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    public void makeMove(String move) {
        try {
            clientHandler.makeMove(move);
        } catch (HttpClientErrorException e) {
            console.showServerAnswer(e.getLocalizedMessage());
        }
    }

    private Thread updateThread() {
        return new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    console.showServerAnswer(clientHandler.getUpdate());
                } catch (HttpClientErrorException e) {
                    console.showServerAnswer(e.getLocalizedMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SimpleConsole console = new SimpleConsole();

        BlockingQueue<String> inputQueue = console.getQueue();
        String clientName = "";
        GameClient client = null;
        do {
            console.show("Enter your name:");
            try {
                clientName = inputQueue.take();
                client = new GameClient(clientName, console);
            } catch (HttpClientErrorException e) {
                console.showServerAnswer(e.getMessage());
            } catch (InterruptedException e) {
            }
        } while (client == null);

        console.show("Hello " + clientName);

        Thread updateThread = client.updateThread();
        updateThread.start();

        String cmd = "";
        while (true) {
            try {
                cmd = inputQueue.take();
                String[] params = cmd.split(" ");

                if (cmd.equals("exit")) {
                    client.leaveGame();
                    console.close();
                    break;
                } else if (cmd.equals("list")) {
                    client.listGames();
                } else if (params.length == 2 && params[0].equals("create")) {
                    client.createGame(params[1]);
                } else if (params.length == 2 && params[0].equals("join")) {
                    client.joinGame(params[1]);
                } else if (params.length == 2 && params[0].equals("play")) {
                    client.makeMove(params[1]);
                } else if (params.length == 2 && params[0].equals("score")) {
                    client.getScore(params[1]);
                } else if (cmd.equals("highscore")) {
                    client.highscoreList();
                } else if (cmd.equals("leave")) {
                    client.leaveGame();
                } else if (cmd.equals("hide")) {
                    console.hideBoard();
                } else if (cmd.equals("show")) {
                    console.showBoard();
                } else {
                    console.show("Commands:\n" +
                            "\tlist - list of open games\n" +
                            "\tcreate [name] - create new game with [name]\n" +
                            "\tjoin [name] - join a game\n" +
                            "\tplay [move] - submit a move to the current game\n" +
                            "\tleave - leave current game\n" +
                            "\thide - hide gameboard\n" +
                            "\tshow - show gameboard\n" +
                            "\texit - exit client");
                }
            } catch (InterruptedException e) {
            }
        }
        updateThread.interrupt();
    }
}
