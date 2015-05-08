package se.mah.ae2513.androidclient;

import android.system.ErrnoException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by David on 2015-04-02.
 */
public class TCPClient extends Thread {

    private String ipNumber;
    private int port;
    private PrintWriter out;
    private BufferedReader in;
    private String serverMessage;
    private MainActivity mainActivity;
    private boolean connected, connectFailed;

    public TCPClient(String ipNumber, int port, MainActivity mainActivity) {
        this.ipNumber = ipNumber;
        this.port = port;
        this.mainActivity = mainActivity;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            //Prints out the message on consol in debugging perpose
            Log.i("MESSAGE TO SERVER", message);
            out.flush();
        }
    }

    public void run() {
        try {
            //create a socket to make the connection with the server
            //Socket socket = new Socket(ipNumber, port);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ipNumber, port), 5000);

            Log.i("Connected to ip: ", ipNumber);

            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                connected = true;
                //in this while the client listens for the messages sent by the server
                while (true) {
                    serverMessage = in.readLine();

                    if(serverMessage.equals("STOP")) {
                        break;
                    }
                    if (serverMessage != null ) {
                        mainActivity.msgFromServer(serverMessage);
                        //Prints out the message on consol in debugging perpose
                        //mainActivity.setServerMessage(serverMessage);
                        Log.i("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                    }
                    serverMessage = null;

                }
                in.close();
                out.close();
                socket.close();
                Log.i("Meddelande: ", "Socket closed");
            }   //was before Exception e, may find more Exceptions on the road
                catch (SocketTimeoutException e) {
                    Log.i("ERROR", "Timeout");
                    connectFailed = true;
                    connected = false;
                    mainActivity.connectionDown();
            } catch (SocketException e) {
                Log.i("ERROR", "Socket");
                connectFailed = true;
                connected = false;
                mainActivity.connectionDownSOCKET();
            } catch (NullPointerException e) {
                Log.i("ERROR", "Nullpointer");
                connectFailed = true;
                connected = false;
                mainActivity.connectionDown();
            }
        } catch (IOException e) {
            Log.i("ERROR", "IOException");
            connectFailed = true;
            connected = false;
            mainActivity.connectionDown();
        } catch (IllegalArgumentException e) {
            Log.i("ERROR", "Socket");
            connectFailed = true;
            connected = false;
            mainActivity.connectionDown();
        }
    }

    public boolean isConnected() {
        return connected;
    }
    public boolean ConnectFailed() {
        return connectFailed;
    }
}
