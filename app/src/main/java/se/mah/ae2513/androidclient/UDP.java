package se.mah.ae2513.androidclient;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by David Tran on 15-05-04.
 */
public class UDP extends Thread {

    public UDP() {

    }

    public void run() {
        // Find the server using UDP broadcast
        try {
            // Open a random port to send the package
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

            while (true) {

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
                                    sendData, sendData.length, broadcast, 28785);
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
                    if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                        // DO SOMETHING WITH THE SERVER'S IP (for example, store
                        // it in your controller)
                        // Controller_Base.setServerIp(receivePacket.getAddress());
                        Log.i("CONNECTED", "TO SERVER>>>>");
                    }
                } catch (Exception e) {

                }

            }
            // Close the port!

        } catch (IOException ex) {
            // Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE,
            // null, ex);
        }
    }
}
