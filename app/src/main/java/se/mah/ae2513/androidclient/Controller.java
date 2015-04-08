package se.mah.ae2513.androidclient;

/**
 * Created by John on 15-04-08.
 */
public class Controller {

    private Entity entity;
    private TCPClient mTcpClient;
    private MainActivity main;

    public Controller(MainActivity mainActivity) {
        main = mainActivity;
        entity = new Entity();
    }

    public void connectToServer() {
        mTcpClient = new TCPClient(entity.getIpNbr(),entity.getPortNbr(), new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
            }
        });
        mTcpClient.start();
    }

    public void sendMessageToServer(String message) {
        mTcpClient.sendMessage(message);
    }

}
