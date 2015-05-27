package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class creates a connection to a server where user chooses
 * ip-address and port number of the server.
 */
public class MainActivity extends Activity implements Communicator, View.OnClickListener {
    private Entity entity = Entity.getInstance();
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private FloatingActionMenu actionMenu;
    private FloatingActionButtonNew actionButton;
    private Fragment_Main fragmentMain;
    private TCPClient client;
    private Timer timer;
    private TextView tvBalance;
    private boolean myDrink, loginOK;
    private final int SHORT = 1, LONG = 2, NO_CONNECTION = 0,
            LOGGED_OUT = 1, LOGIN_BAD = 2, RE_LOGIN = 3, SOCKET_TCP_ERROR = 4,
            NO_ACTION = 0, UPDATE_BALANCE = 1, UPDATE = 2;
    private final boolean ORDER_BUTTON_GONE = false, ORDER_BUTTON_VISIBLE = true;
    private Intent returnIntent;
    private static final String TAG_UPDATE = "update";
    private static final String TAG_LOGOUT = "logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);
        createFragments();
        initializeComponents();
        entity.setUsername(getIntent().getStringExtra("username"));
        entity.setPassword(getIntent().getStringExtra("password"));
        returnIntent = getIntent();
        connectToServer();
        while(!client.isConnected()) {
            if(client.ConnectFailed()) {
                break;
            }
        }
        if(client.isConnected() && !client.ConnectFailed()) {
            login();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loginOK) {
            startTimer();
            Log.d("Bar", "Timer running again");
        }
    }

    private void initializeComponents() {
        tvBalance = (TextView) findViewById(R.id.tvBalance);

        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.coin);
        actionButton = new FloatingActionButtonNew.Builder(this)
                .setBackgroundDrawable(R.drawable.coin)
                .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
                .build();
        ImageView icon1 = new ImageView(this);
        ImageView icon2 = new ImageView(this);
        icon2.setImageResource(R.drawable.sync);
        ImageView icon3 = new ImageView(this);
        icon3.setImageResource(R.drawable.logout);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        SubActionButton button2 = itemBuilder.setContentView(icon2).build();
        SubActionButton button3 = itemBuilder.setContentView(icon3).build();
        button2.setTag(TAG_UPDATE);
        button3.setTag(TAG_LOGOUT);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .setStartAngle(90)
                .setEndAngle(135)
                .attachTo(actionButton)
                .build();
    }

    private void confirmSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign out!")
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        signOut();
                    }
                });
        builder.create().show();        // create and show the alert dialog
    }

    private void signOut() {
        closeConnection();
        setResult(LOGGED_OUT, returnIntent);
        finish();
    }

    public void connectToServer() {
        //message STOP returned from server if socket closes
        if(client != null) {
            Toast.makeText(getApplicationContext(), "New Connection", Toast.LENGTH_SHORT).show();
            client.sendMessage("STOP");
        }
        client = new TCPClient(entity.getIpNbr(),entity.getPortNbr(), this);
        client.start();
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
        return super.onOptionsItemSelected(item);
    }

    /*
     * Creating all fragments to be used in the program.
     */
    private void createFragments() {
        fragmentMain = new Fragment_Main();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.add(R.id.fr_id, fragmentMain);
        transaction.commit();
    }

    public void closeConnection(){
        stopTimer();
        if(client != null) {
            sendMessage("STOP");
        }
    }

    /**
     * Method receives messages from server and setting the
     * serverstatus to entity. Depending on messages the button
     * on fragmentMain will either be shown or hidden.
     * If hidden, a status string is showing
     * @param message
     */
    public void msgFromServer(String message) {
        if(message.split(" ")[0].equals("ERROR")) {
            String errorType = message.split(" ")[1];
            if(errorType.equals("NOCONNECTION")) {
                entity.setStatus("No Arduino");
                showOrderButton(ORDER_BUTTON_GONE);
            } else if(errorType.equals("BUSY")) {
                if(myDrink) {
                    entity.setStatus("Barduino is making your drink");
                } else {
                    entity.setStatus("Barduino is busy with another drink");
                }
                showOrderButton(ORDER_BUTTON_GONE);
            } else if(errorType.equals("NOLOGIN")) {
                entity.setStatus("Not logged in");
                stopTimer();
                showOrderButton(ORDER_BUTTON_GONE);
            } else if(errorType.equals("BLOCKED")) {
                alertDialog("Message from Barduino", "You have been blocked", "OK", NO_ACTION);
            } else if(errorType.contains("INSUFFICIENT")) {
                alertDialog("Insufficient funds", "Barduinos prices have changed\nFluid prices will be updated", "OK", UPDATE);
            }
        } else if(message.split(" ")[0].equals("LOGIN")) {
            String login = message.split(" ")[1];
            if(login.equals("BAD")) {
                //end activity, out to login screen
                setResult(LOGIN_BAD, returnIntent);
                loginOK = false;
                finish();
            } else if(login.equals("OK")) {
                entity.setBalance(Integer.parseInt(message.split(" ")[2]));
                entity.setStatus("Initializing...");
                loginOK = true;
                updateBalance();
                updateFluidsFromServer();
                startTimer();
            }
        } else if(message.contains("AVAILABLE")) {
            if (myDrink) {
                entity.setStatus("Finished!");
                myDrink = false;
                //enable refresh and log out
                actionButton.setClickable(true);
                alertDialog("Your grog is done!", "New balance: " +
                        entity.getBalance() + "kr", "OK", UPDATE_BALANCE);
            } else {
                showOrderButton(ORDER_BUTTON_VISIBLE);
            }
        } else if(message.contains("GROGOK")) {
            myDrink = true;
            //avoid logging out or refresh while barduino's working on this clients drink
            actionButton.setClickable(false);
            int balance = Integer.parseInt(message.split(" ")[1]);
            entity.setBalance(balance);
            entity.setStatus("Sending grog...");


            showOrderButton(false);
        } else if(message.contains("INGREDIENTS")) {
            if(fragmentMain.isVisible()) {
            }
            setLiquids(message);
        }
        setTextOnStatus();
    }

    private void setLiquids(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> liquids = new ArrayList<String>();
                ArrayList<Integer> price = new ArrayList<Integer>();
                String str = null;
                try {
                    str = string.split(":")[1];
                    String[] liquidList = str.split(",");
                    for (int i = 0; i < liquidList.length; i++) {
                        liquids.add(liquidList[i].split("<cost>")[0]);
                        price.add(Integer.parseInt(liquidList[i].split("<cost>")[1]));
                    }
                    entity.setLiquids(liquids);
                    entity.setLiquidPrices(price);
                    if (fragmentMain.isVisible())
                        fragmentMain.setLiquids();
                } catch (ArrayIndexOutOfBoundsException e) {
                    alertDialog("Error", "Barduino has no available fluids", "OK", 0);
                }
            }
        });
    }

    private void showOrderButton(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fragmentMain.isVisible()) ;
                fragmentMain.showOrderButton(show);
            }
        });
    }

    private void updateFluidsFromServer() {
        if(client != null) {
            client.sendMessage("INGREDIENTS");
        }
    }

    //implemented method for interface Communication
    @Override
    public void sendMessage(String message) {
        if(client != null) {
            client.sendMessage(message);
        }
    }

    /*
    Timer to check server status
     */
    private void startTimer() {
        stopTimer();
        timer = new Timer();
        //timer starts after 2000ms, and the run method is call every 2000ms
        timer.schedule(new CheckServer(), 2000, 2000);
    }

    /*
    Inner class for the timer
     */
    private class CheckServer extends TimerTask {
        @Override
        public void run() {
            sendAvareqToServer();
        }
    }

    /*
    Checking server status by sending text string AVAREQ.
    Server will answer
    */
    private void sendAvareqToServer() {
        sendMessage("AVAREQ");
    }

    private void stopTimer() {
        if(timer != null) {
            timer.cancel();
        }
    }

    /**
     * Method to finish the activity when connection to server is lost.
     */
    public void connectionDown() {
        if(timer != null)
            timer.cancel();
        setResult(NO_CONNECTION, returnIntent);
        finish();
    }

    public void connectionDownSOCKET() {
        if(timer != null)
            timer.cancel();
        setResult(SOCKET_TCP_ERROR, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
    }

    /**
     * onClick method for Floating Action Button menu
     * @param v the clicked button
     */
    @Override
    public void onClick(View v) {
        if(v.getTag().equals(TAG_UPDATE)) {
            update();
        } else if(v.getTag().equals(TAG_LOGOUT)) {
            confirmSignOut();
        }
    }

    private void setTextOnStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fragmentMain.isVisible()) {
                    fragmentMain.setStatusText(entity.getStatus());
                }
            }
        });
    }

    private void updateBalance() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvBalance.setText(entity.getBalance() + "");
            }
        });
    }

    private void makeToast(final String message, final int length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(length == SHORT) {
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
                    toast.show();
                } else if(length == LONG ) {
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
                    toast.show();
                }

            }
        });
    }

    private void alertDialog(final String title, final String message, final String textButton, final int option) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                // Setting Dialog Title
                alertDialog.setTitle(title);
                // Setting Dialog Message
                alertDialog.setMessage(message);
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.logo_b);
                // Setting OK Button
                alertDialog.setButton(textButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        doOnAlertDialogOk(option);
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        });
    }

    private void doOnAlertDialogOk(int option) {
        switch (option) {
            case NO_ACTION:
                break;
            case UPDATE_BALANCE:
                updateBalance();
                break;
            case UPDATE:
                update();
                break;
        }
    }

    private void update() {
        closeConnection();
        setResult(RE_LOGIN, returnIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null) {
            timer.cancel();
            Log.d("Bar", "Timer stopped");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Bar", "onStop is called");
    }
}