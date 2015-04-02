package se.mah.ae2513.androidclient;

import android.widget.Toast;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by David on 2015-04-02.
 */
public class TCPClient implements Runnable {

    private String ipNumber;
    private int port;

    public TCPClient(String ipNumber, int port) {
        this.ipNumber = ipNumber;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket("192.168.1.2", 4444);
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println("Hello server!");
            InputStreamReader ir = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            String message = br.readLine();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
