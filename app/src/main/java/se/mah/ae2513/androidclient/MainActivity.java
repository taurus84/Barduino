package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class creates a connection to a server where user chooses
 * ip-address and port number of the server.
 */
public class MainActivity extends ActionBarActivity implements Communicator  {

    private Entity entity = Entity.getInstance();
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment_Update fragUpdate;
    private Fragment_Edit fragEdit;
    private Fragment_Mixer fragMix;
    private TCPClient client;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);
        createFragments();
        //setTextTesting(ip);
        entity.setIpNbr(getIntent().getStringExtra("ipnumber"));
        entity.setPortNbr(Integer.parseInt(getIntent().getStringExtra("port")));
        entity.setUsername(getIntent().getStringExtra("username"));
        entity.setPassword(getIntent().getStringExtra("password"));
        connectToServer();




    }

    public void connectToServer() {
        //message STOP returned from server if socket closes
        if(client != null) {
            Toast.makeText(getApplicationContext(), "New Connection", Toast.LENGTH_SHORT).show();
            client.sendMessage("STOP");
        }
        client = new TCPClient(entity.getIpNbr(),entity.getPortNbr(), this);
        client.start();
        startTimer();
        login();

    }

    private void login() {
        String message = "LOGIN " + entity.getUsername() +
                        ":" + entity.getPassword();
        sendMessage(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.connect_now:
                //connectToServer();
                sendMessage("LOGIN test:password");
                break;
            case R.id.edit_ip_and_port:
                fragmentIP();
                break;
            case R.id.update_now:
                fragmentUpdate();
                break;
            case R.id.mixer:
                fragmentMixer();
                break;
            case R.id.disconnect:

                //stop timer
                if(timer != null) {
                    timer.cancel();
                }
                if(client != null) {
                    closeConnection();
                }
                finish();
                break;
            case R.id.testButton:
                //updateFluidsFromServer();
               /* if(fragMix.isVisible()) {
                    String text = rand.nextInt(100) + 100 + "";
                    fragMix.setButtonText(text);
                    break;
                }
                */
                //setTextOnButton();
                //sendMessage("INGREDIENTS");
                //fragMix.showOrderButton(false);
                Toast.makeText(getApplicationContext(), entity.getUsername(), Toast.LENGTH_SHORT).show();
                login();

                break;
            case R.id.startTimer:
                //startTimer();
                //finish();
                Toast.makeText(getApplicationContext(), entity.getPassword(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Creating all fragments to be used in the program.
     */
    private void createFragments() {

        fragUpdate = new Fragment_Update();
        fragEdit = new Fragment_Edit();
        fragMix = new Fragment_Mixer();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.add(R.id.fr_id, fragMix);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*<<<<<<<<<<fragments>>>>>>>>>>>>>>>>>>>>>>>*/
   private void fragmentUpdate() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragUpdate);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fragmentIP() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragEdit);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fragmentMixer() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragMix);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void closeConnection(){
        if(timer != null)
            timer.cancel();
        if(client != null) {
            sendMessage("STOP");
        }
    }

    /**
     * Method receives messages from server and setting the
     * serverstatus to entity. Depending on messages the button
     * on fragment mixer will either active or inactive.
     * @param message
     */
    public void msgFromServer(String message) {

        if(message.split(" ")[0].equals("ERROR")) {
            String errorType = message.split(" ")[1];
            if(errorType.equals("NOCONNECTION")) {
                entity.setServerStatus("ERROR");
                showOrderButton(false);
            } else if(errorType.equals("BUSY")) {
                entity.setServerStatus("BUSY");
                showOrderButton(false);
            } else if(errorType.equals("NOLOGIN")) {
                entity.setServerStatus("Not logged in");
                showOrderButton(false);
            }
        } else if(message.contains("AVAILABLE")) {
            entity.setServerStatus("AVAILABLE");
            showOrderButton(true);
        } else if(message.contains("GROGOK")) {
            //some code
        } else if(message.contains("INGREDIENTS")) {
            entity.setServerStatus("AVAILABLE");
                if(fragMix.isVisible()) {
                    showOrderButton(true);
                }
            login();
            setLiquids(message);

        }


        setTextOnButton();
    }

    public void setLiquids(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] liquids = string.split(":")[1].split(",");
                entity.setLiquids(liquids);
                if (fragMix.isVisible())
                    fragMix.setTextLiquids();
            }
        });
    }

    public void showOrderButton(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fragMix.isVisible())
                    fragMix.showButton(show);
            }
        });
    }

    public void updateFluidsFromServer() {

        if(client != null) {
            client.sendMessage("INGREDIENTS");
        }
    }

    //implemented method for interface Communication
    @Override
    public void doSomething() {
    }

    //implemented method for interface Communication
    @Override
    public void sendMessage(String message) {

        if(client != null) {
            client.sendMessage(message);
        }
    }

    public void sendAvareqToServer() {
        sendMessage("AVAREQ");
    }
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new CheckServer(), 2000,5000);
        //timer.schedule(new ButtonSwitcher(), 0, 2000);
    }

    public void connectionDown() {
        if(timer != null)
            timer.cancel();
        finish();
    }

    private class CheckServer extends TimerTask {

        @Override
        public void run() {
            sendAvareqToServer();

        }
    }

    private class ButtonSwitcher extends TimerTask {

        @Override
        public void run() {
            setTextOnButton();

        }
    }

    private void setTextOnButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fragMix.isVisible()) {
                    //String text = rand.nextInt(Integer.MAX_VALUE) + 100000 + "";
                    fragMix.setButtonText(entity.getServerStatus());
                }
            }
        });
    }

    private void setTextTesting(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fragMix.isVisible()) {
                    fragMix.setButtonText(string);
                }
            }
        });
    }
}