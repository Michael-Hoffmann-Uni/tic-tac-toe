package jpp.games.networking.server.handler.game;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RemoteIO_Impl implements RemoteIO {
    private BlockingQueue<String> inQueue = new LinkedBlockingQueue<String>();
    private BlockingQueue<String> outQueue = new LinkedBlockingQueue<String>();

    @Override
    public void putInput(String input) {
        inQueue.add(input);
    }

    @Override
    public String getInput() {
        try {
            return inQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void putOutput(String output) {
        outQueue.add(output);
    }

    @Override
    public String getOutput() {
        try {
            return outQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }
}
