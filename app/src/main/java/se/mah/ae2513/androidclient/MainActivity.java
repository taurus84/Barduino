package se.mah.ae2513.androidclient;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * The class creates a connection to a server where user chooses
 * ip-address and port number of the server.
 */
public class MainActivity extends ActionBarActivity implements Communicator  {

    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment_Update fragUp;
    private Fragment_Edit fragEdit;
    private Fragment_Mixer fragMix;
    private boolean bool = true;
    private TextView liquid1, liquid2,liquid3,liquid4;
    private TCPClient client;
    private Entity entity = Entity.getInstance();
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);
        createFragments();
        setComponents();
    }

    public void connectToServer() {
        //message STOP returned from server if socket closes
        if(client != null) {
            client.sendMessage("STOP");
        }
        client = new TCPClient(entity.getIpNbr(),entity.getPortNbr(), this);
        client.start();
    }

    /**
     * Setting up buttons and editTexts
     */
    private void setComponents() {

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.connect_now) {
            connectToServer();
        }else if (id == R.id.edit_ip_and_port ){
            fragmentIP();
        } else if (id == R.id.update_now) {
            update();
        } else if (id == R.id.mixer) {
            fragmentMixer();
        } else if (id == R.id.disconnect) {
            //closeConnection();
            //timer = new Timer();
            //timer.schedule(new ButtonChanger(), 1000, 5000 );
        } else if (id == R.id.testButton) {
            //setLiquids();
            //Log.i("Ingredients", entity.getLiquids(1));
            //entity.setLiquids("INGREDIENTS:hallon,äpple,smuts,unk");
            updateFluids();
            //setLiquidsOnFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createFragments() {

        fragUp = new Fragment_Update();
        fragEdit = new Fragment_Edit();
        fragMix = new Fragment_Mixer();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.add(R.id.fr_id, fragEdit);
        transaction.commit();
    }

    private void update() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragUp);
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
        sendMessage("STOP");
    }

    public void setConnectedButton(final boolean bool) {
        //to avoid exception android.view.ViewRoot$CalledFromWrongThreadException:
        //Only the original thread that created a view hierarchy can touch its views.
        //This method allows the action to be run in the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bool) {
                    findViewById(R.id.topLeftOff).setVisibility(View.GONE);
                    findViewById(R.id.topLeftOn).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.topLeftOff).setVisibility(View.VISIBLE);
                    findViewById(R.id.topLeftOn).setVisibility(View.GONE);
                }

            }
        });
    }
    public void msgFromClient(String message) {
        if(message.contains("AVAILABLE")) {
            setConnectedButton(true);
        } else if( message.contains("ERROR")) {
            setConnectedButton(false);
        } else if(message.contains("GROGOK")) {
            setConnectedButton(true);
        } else if(message.contains("INGREDIENTS")) {
            setConnectedButton(true);
            setLiquids(message);
        }
    }

    public void setLiquids(final String liquids) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //entity.setLiquidSpecific("Äpple", 0);
                entity.setLiquids(liquids);
              //  liquid1 = (TextView) findViewById(R.id.liquid1);
              //  liquid1.setText(entity.getLiquids(0));

                //fragUp.setTextFromServer(entity.getServerMessage());
            }
        });
    }

    public void updateFluids() {

        client.sendMessage("INGREDIENTS");
    }

    public void setLiquidsOnFragment() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                liquid1 = (TextView) findViewById(R.id.liquid1);
                liquid2 = (TextView) findViewById(R.id.liquid2);
                liquid3 = (TextView) findViewById(R.id.liquid3);
                liquid4 = (TextView) findViewById(R.id.liquid4);
                liquid1.setText(entity.getLiquids(0));
                liquid2.setText(entity.getLiquids(1));
                liquid3.setText(entity.getLiquids(2));
                liquid4.setText(entity.getLiquids(3));
            }
        });
    }


    //implemented method for interface Communication
    @Override
    public void doSomething() {
    }

    //implemented method for interface Communication
    @Override
    public void sendMessage(String message) {

        Log.i("Message:", message);
        client.sendMessage(message);
    }

    public void sendAvareqToServer() {
        sendMessage("AVAREQ");
    }
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new CheckServer(), 2000,5000);
    }

    private class CheckServer extends TimerTask {

        @Override
        public void run() {
            sendAvareqToServer();

        }
    }
}