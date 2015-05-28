package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.concurrent.TimeoutException;

/**
 * This class is the opening of the application. It shows the Barduino logo
 * and after a paus of 5 seconds, which include a progressbar, the application
 * sends a broadcast throughout the network to find the server. When server is found
 * the Login activity starts.
 * Includes inner class GetHostIP, which is doing the server lookup.
 *
 * Created by David Tran 2015-05-08.
 */
public class Splash extends Activity {


    private MediaPlayer barduinoStartSound;
    private ProgressBar progressBar;
    private GetHostIP aSyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        //barduinoStartSound = MediaPlayer.create(Splash.this, R.raw.italiano);
        //barduinoStartSound.start();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //paus for five seconds with a progressbar
        Thread pausTimer = new Thread() {
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        sleep(50);
                        progressBar.setProgress(i);
                    }
                } catch (Exception e) {

                } finally {
                    //find the server and retreive the server IP
                    aSyncTask = new GetHostIP();
                    aSyncTask.execute();
                }
            }
        };
        pausTimer.start();



    }

    /**
     * Inner class which extends AsyncTask.
     * The class sends broadcast, which contains a string, throughout the network.
     * When server gets this specific broadcast string it return a string 'HELLO_CLIENT'
     * This class listens for this specific string and when received, the IP number is stored in
     * the Entity class for further use.
     */
    private class GetHostIP extends AsyncTask<Void, Void, Void> {

        private int UDPportNbr = 28785;
        String hostIPNumber = "";

        //method is called before execution
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //The running method of the AsyncTask class
        @Override
        protected Void doInBackground(Void... arg0) {

            //loop while no IP is found
            while(hostIPNumber.equals("")) {
                //break the loop if the AsyncTask is canceled
                if(isCancelled()) {
                    break;
                }

                try {
                    // Open a random port to send the package
                    DatagramSocket c = new DatagramSocket();
                    c.setBroadcast(true);

                    byte[] sendData = "BARDUINO".getBytes();


                    // Broadcast the message over all the network interfaces
                    Enumeration interfaces = NetworkInterface
                            .getNetworkInterfaces();

                    while (interfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = (NetworkInterface) interfaces
                                .nextElement();

                        if (networkInterface.isLoopback()
                                || !networkInterface.isUp()) {
                            continue; // Don't want to broadcast to the loopback
                            // interface
                        }

                        for (InterfaceAddress interfaceAddress : networkInterface
                                .getInterfaceAddresses()) {
                            InetAddress broadcast = interfaceAddress.getBroadcast();
                            if (broadcast == null) {
                                continue;
                            }

                            // Send the broadcast package!
                            try {
                                DatagramPacket sendPacket = new DatagramPacket(
                                        sendData, sendData.length, broadcast, UDPportNbr);
                                c.send(sendPacket);
                                c.setSoTimeout(5000);

                            } catch (Exception e) {
                            }

                            System.out.println(">>> Request packet sent to: "
                                    + broadcast.getHostAddress() + "; Interface: "
                                    + networkInterface.getDisplayName());
                        }
                    }
                    Log.i("Now waiting for a reply", "");
                    // Wait for a response
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf,
                            recvBuf.length);
                    try {
                        c.receive(receivePacket);
                        // We have a response
                        Log.i("RESPONSE from server: ", receivePacket.getAddress().getHostAddress());

                        // Check if the message is correct
                        String message = new String(receivePacket.getData()).trim();
                        if (message.equals("HELLO_CLIENT")) {
                            Log.i("<<<<<CONNECTED", "TO SERVER>>>>");
                            hostIPNumber = receivePacket.getAddress().getHostAddress();
                        }
                    } catch (SocketTimeoutException e) {
                        Log.e("ERROR", ">>>Exception<<<");
                    }
                    // Close the port
                    c.close();

                } catch (IOException ex) {
                    Log.e("ERROR", ">>>IOEXCEPTION<<<");
                }
            }
            return null;
        }


        //Method is called after execution of doInBackground
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //store the ip number to Entity class
            Entity.getInstance().setIpNbr(hostIPNumber);
            //start new intent
            Intent loginClass = new Intent(Splash.this, Login.class);
            startActivity(loginClass);
        }

    }

    //when the new activity comes in foreground, the onPause is called from this class
    @Override
    protected void onPause() {
        super.onPause();
        //stop eventual sound
        //barduinoStartSound.release();
        Log.d("David", "onPause is called");
        //kill this activity
        finish();
    }
}
