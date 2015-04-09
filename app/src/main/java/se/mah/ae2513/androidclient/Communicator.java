package se.mah.ae2513.androidclient;

/**
 * Interface for communicating with fragments.
 * Classes implementing this interface must implement methods
 * Created by David Tran on 15-04-09.
 */
public interface Communicator {

    public void doSomething();

    public void connectNow();

    public void sendMessage(String string);
}
