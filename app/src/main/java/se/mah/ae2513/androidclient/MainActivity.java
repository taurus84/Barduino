package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class creates a connection to a server where user chooses
 * ip-address and port number of the server.
 */
public class MainActivity extends Activity implements Communicator  {

    private Entity entity = Entity.getInstance();
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment_Update fragUpdate;
    private Fragment_Edit fragEdit;
    private Fragment_Mixer fragMix;
    private Fragment_Login fragLogin;
    private TCPClient client;
    private Timer timer;
    private TextView tvLogin;
    private boolean myDrink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);
        createFragments();
        initializeComponents();
        //setTextTesting(ip);
        entity.setIpNbr(getIntent().getStringExtra("ipnumber"));
        entity.setPortNbr(Integer.parseInt(getIntent().getStringExtra("port")));
        entity.setUsername(getIntent().getStringExtra("username"));
        entity.setPassword(getIntent().getStringExtra("password"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        connectToServer();
        startTimer();
        //testLogin();
    }

    private void initializeComponents() {
        tvLogin = (TextView) findViewById(R.id.tvLoginLogoff);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvLogin.getText().equals("Sign in")) {
                    //testLogin();
                    fragmentLogin();
                } else {
                    closeConnection();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        login();
    }

    private void setTvLogOut() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLogin.setText("Sign out");
            }
        });
    }

    private void setTvLogIn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLogin.setText("Sign in");
            }
        });
    }

    public void connectToServer() {
        //message STOP returned from server if socket closes
        if(client != null) {
            Toast.makeText(getApplicationContext(), "New Connection", Toast.LENGTH_SHORT).show();
            client.sendMessage("STOP");
            setTvLogIn();
        }
        client = new TCPClient(entity.getIpNbr(),entity.getPortNbr(), this);
        client.start();
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
            case R.id.mixer:
                //fragmentMixer();
                closeConnection();
                break;
            case R.id.testButton:
                setTvLogOut();
                break;
            case R.id.logInOut:
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
        fragLogin = new Fragment_Login();
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

    private void fragmentLogin() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragLogin);
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
                entity.setServerStatus("No Arduino");
                showOrderButton(false);
            } else if(errorType.equals("BUSY")) {
                entity.setServerStatus("BUSY");
                showOrderButton(false);
            } else if(errorType.equals("NOLOGIN")) {
                login();
                entity.setServerStatus("Not logged in");
                showOrderButton(false);
            }
        } else if(message.split(" ")[0].equals("LOGIN")) {
            String login = message.split(" ")[1];
            if(login.equals("BAD")) {
                entity.setServerStatus("Not logged in");
                //Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
            } else if(login.equals("OK")) {
                entity.setServerStatus("Loggin in...");
                entity.setLoggedIn(true);
                updateFluidsFromServer();
                //Toast.makeText(getApplicationContext(), "You are now logged in", Toast.LENGTH_SHORT).show();
                setTvLogOut();
            }
        } else if(message.contains("AVAILABLE")) {
            if(myDrink) {
                entity.setServerStatus("Finished!");
                myDrink = !myDrink;
            } else {
                entity.setServerStatus("Order Drink");
                showOrderButton(true);
            }
        } else if(message.contains("GROGOK")) {
            //setTextOnButtonWithString("Wait..");
            myDrink = !myDrink;
            entity.setServerStatus("Wait...");

            showOrderButton(false);
        } else if(message.contains("INGREDIENTS")) {
            if(fragMix.isVisible()) {
                showOrderButton(true);
            }
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
        //ActionMenuItemView itemIn = (ActionMenuItemView) findViewById(R.id.login);
        //itemIn.setTextColor(0xffffff);
        //itemOut.setVisible(true);

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

    private void setTextOnButtonWithString(final String string) {
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