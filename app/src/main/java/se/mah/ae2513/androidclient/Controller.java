package se.mah.ae2513.androidclient;

/**
 * Created by John on 15-04-08.
 */
public class Controller {

    private Entity entity = Entity.getInstance();
    private TCPClient mTcpClient;
    private MainActivity main;

    public Controller(MainActivity mainActivity) {
        main = mainActivity;
    }

    public void connectToServer() {
        //message STOP returned from server if socket closes
        if(mTcpClient != null) {
            mTcpClient.sendMessage("STOP");
        }
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

    public void closeConnection(){
        sendMessageToServer("STOP");
    }

}
