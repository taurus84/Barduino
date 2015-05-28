package se.mah.ae2513.androidclient;

/**
 * Interface for communicating with fragments.
 *
 * Classes implementing this interface must implement methods
 * By using this interface a fragment can communicate with its parent.
 *
 * Created by David Tran on 15-04-09.
 */
public interface Communicator {

    void connectToServer();
    void sendMessage(String string);

    
}
