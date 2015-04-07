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
    PrintWriter out;
    BufferedReader in;
    PrintStream ps;
    InputStreamReader ir;
    private String serverMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;


    public TCPClient(String ipNumber, int port, OnMessageReceived listener ) {
        this.ipNumber = ipNumber;
        this.port = port;
        mMessageListener = listener;

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
        mRun = true;
        try {
            //create a socket to make the connection with the server
            Socket socket = new Socket(ipNumber, port);
            /*PrintStream ps = new PrintStream(socket.getOutputStream());
            Socket socket = new Socket("192.168.1.100", 4444);
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println("Hello server!");
            InputStreamReader ir = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(ir);
            String message = in.readLine();
            */

            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                        //Prints out the message on consol in debugging perpose
                        Log.i("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                    }
                    serverMessage = null;

                }
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            }

        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
