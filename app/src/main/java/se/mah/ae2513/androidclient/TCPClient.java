package se.mah.ae2513.androidclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by David on 2015-04-02.
 */
public class TCPClient extends Thread {

    private String ipNumber;
    private int port;
    private PrintWriter out;
    private BufferedReader in;
    private String serverMessage;
    private boolean mRun = false;
    private MainActivity mainActivity;

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
        //mRun = true;
        try {
            //create a socket to make the connection with the server
            Socket socket = new Socket(ipNumber, port);
            Log.i("Connected to ip: ", ipNumber);

            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            }
        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
        }
    }
}
