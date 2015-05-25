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
 * Created by David Tran on 15-05-08.
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

        Thread pausTimer = new Thread() {
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        sleep(50);
                        progressBar.setProgress(i);
                    }
                } catch (Exception e) {

                } finally {
                    aSyncTask = new GetHostIP();
                    aSyncTask.execute();
                }
            }
        };
        pausTimer.start();



    }

    /**
     * Async Task to make http call
     */
    private class GetHostIP extends AsyncTask<Void, Void, Void> {

        private int UDPportNbr = 28785;
        String hostIPNumber = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            while(hostIPNumber.equals("")) {
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
                        //String serverIpNbr = receivePacket.getAddress().getHostAddress();

                        // Check if the message is correct
                        String message = new String(receivePacket.getData()).trim();
                        if (message.equals("HELLO_CLIENT")) {
                            // DO SOMETHING WITH THE SERVER'S IP (for example, store
                            // it in your controller)
                            // Controller_Base.setServerIp(receivePacket.getAddress());
                            Log.i("<<<<<CONNECTED", "TO SERVER>>>>");
                            //mainActivity.doSomething();
                            //entity.setIpNbr(serverIpNbr);
                            //entity.setPortNbr(portNbr);
                            //logger.setIP(receivePacket.getAddress().getHostAddress(), portNbr);
                            //Entity.getInstance().setIpNbr(receivePacket.getAddress().getHostAddress());
                            hostIPNumber = receivePacket.getAddress().getHostAddress();

                        }
                    } catch (SocketTimeoutException e) {
                        Log.e("ERROR", ">>>Exception<<<");
                    }


                    // Close the port!
                    c.close();

                } catch (IOException ex) {
                    // Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE,
                    // null, ex);
                    Log.e("ERROR", ">>>IOEXCEPTION<<<");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            Entity.getInstance().setIpNbr(hostIPNumber);
            Intent i = new Intent(Splash.this, Login.class);
            startActivity(i);

            // close this activity

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //barduinoStartSound.release();
        Log.d("David", "onPause is called");
        //aSyncTask.cancel(true);
        finish();
    }
}
