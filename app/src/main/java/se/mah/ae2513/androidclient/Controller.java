package se.mah.ae2513.androidclient;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by John on 15-04-08.
 */
public class Controller {

    private Entity entity = Entity.getInstance();
    private TCPClient mTcpClient;
    private MainActivity main;
    private Timer timer, buttonChanger;


    public Controller(MainActivity mainActivity) {

        main = mainActivity;

    }

    public void connectToServer() {
        //message STOP returned from server if socket closes
        if(mTcpClient != null) {
            mTcpClient.sendMessage("STOP");
        }
      /*
        mTcpClient = new TCPClient(entity.getIpNbr(),entity.getPortNbr(), new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
               if(message.contains("ERROR")) {
                    main.setConnectedButton(false);
                }
            }
        });
        */
        mTcpClient = new TCPClient(entity.getIpNbr(),entity.getPortNbr(),this);
        mTcpClient.start();
        getStatus();
        changeButtons();
    }

    public void sendMessageToServer(String message) {

        mTcpClient.sendMessage(message);
    }

    public void closeConnection(){

        timer.cancel();
        sendMessageToServer("STOP");

    }

    public void messageReceived(String message) {

        if(message.equals("DAVID")) {
            main.doSomething();
        }
        main.setServerMessage(message);
        Log.i("Controller prints: ", message);
    }

    public void getStatus() {
        timer = new Timer();
        timer.schedule(new CheckServer(), 5000, 10000 );
    }

    private void changeButtons() {
        buttonChanger = new Timer();
        buttonChanger.schedule(new ButtonChanger(), 0,1000);
    }

    /**
     * Inner class for checking connection with server by sending
     * AVAREQ messages, with an interval determined when TimerTask()
     * is called.
     */
    private class CheckServer extends TimerTask {

        @Override
        public void run() {
            sendMessageToServer("AVAREQ");

        }
    }

    private class ButtonChanger extends TimerTask {

        boolean toggler;
        @Override
        public void run() {

            main.setConnectedButton(toggler);
            toggler = !toggler;
        }
    }
}
