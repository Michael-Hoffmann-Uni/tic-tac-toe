package jpp.games.networking.server.handler.game;

public interface RemoteIO {

    static RemoteIO createRemoteIO() {
        return new RemoteIO_Impl();
    }

    void putInput(String input);

    String getInput();

    void putOutput(String output);

    String getOutput();

}
