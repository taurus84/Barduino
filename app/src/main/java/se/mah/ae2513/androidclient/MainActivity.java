package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Random;
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
    private Fragment_Main fragmentMain;
    private Fragment_Login2 fragLogin2;
    private Fragment_Register fragReg;
    private TCPClient client;
    private Timer timer;
    private TextView tvBalance;
    private boolean myDrink;
    private final int SHORT = 1, LONG = 2, NO_CONNECTION = 0,
            LOGGED_OUT = 1, LOGIN_BAD = 2, RE_LOGIN = 3, SOCKET_TCP_ERROR = 4, NO_ACTION = 0, UPDATE_BALANCE = 1;
    private final boolean ORDER_BUTTON_GONE = false, ORDER_BUTTON_VISIBLE = true;
    private Intent returnIntent;
    private static final String TAG_UPDATE = "update";
    private static final String TAG_LOGOUT = "logout";
    private static final String TAG_3 = "tag3";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);
        createFragments();
        initializeComponents();
        entity.setUsername(getIntent().getStringExtra("username"));
        entity.setPassword(getIntent().getStringExtra("password"));
        returnIntent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onResume() {
        super.onResume();
    }

    private void initializeComponents() {
        tvBalance = (TextView) findViewById(R.id.tvBalance);

        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.coin);
        FloatingActionButtonNew actionButton = new FloatingActionButtonNew.Builder(this)
                .setBackgroundDrawable(R.drawable.coin)
                .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
                        //.setContentView(icon)
                .build();
        ImageView icon1 = new ImageView(this);
        icon1.setImageResource(R.drawable.bar);
        ImageView icon2 = new ImageView(this);
        icon2.setImageResource(R.drawable.sync);
        ImageView icon3 = new ImageView(this);
        icon3.setImageResource(R.drawable.logout);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

     //   SubActionButton button1 = itemBuilder.setContentView(icon1).build();
        SubActionButton button2 = itemBuilder.setContentView(icon2).build();
        SubActionButton button3 = itemBuilder.setContentView(icon3).build();
     //   button1.setTag(TAG_3);
        button2.setTag(TAG_UPDATE);
        button3.setTag(TAG_LOGOUT);

     //   button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        actionMenu = new FloatingActionMenu.Builder(this)
         //       .addSubActionView(button1)
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
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        signOut();
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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

        fragLogin2 = new Fragment_Login2();
        fragmentMain = new Fragment_Main();
        fragReg = new Fragment_Register();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        //transaction.add(R.id.fr_id, fragMix);
        transaction.add(R.id.fr_id, fragmentMain);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    /*<<<<<<<<<<fragments>>>>>>>>>>>>>>>>>>>>>>>*/

    private void fragmentLogin() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragLogin2);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void fragmentRegister() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragReg);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fragmentMixer() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        //transaction.replace(R.id.fr_id, fragMix);
        transaction.addToBackStack(null);
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
     * on fragment mixer will either active or inactive.
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
                alertDialog("Message from Barduino", "You have been blocked", "OK", 0);
            }
        } else if(message.split(" ")[0].equals("LOGIN")) {
            String login = message.split(" ")[1];
            if(login.equals("BAD")) {
                //end activity, out to login screen
                setResult(LOGIN_BAD, returnIntent);
                finish();
            } else if(login.equals("OK")) {
                updateBalance();
                entity.setBalance(Integer.parseInt(message.split(" ")[2]));
                entity.setStatus("Initializing...");
                updateFluidsFromServer();
                startTimer();
            }
        } else if(message.contains("AVAILABLE")) {
            if(myDrink) {
                entity.setStatus("Finished!");
                myDrink = false;
                alertDialog("Your grog is done!", "New balance: " +
                        entity.getBalance() + "kr", "OK", UPDATE_BALANCE);

            } else {
                showOrderButton(ORDER_BUTTON_VISIBLE);
            }
        } else if(message.contains("GROGOK")) {
            //setTextOnButtonWithString("Wait..");
            myDrink = true;
            int balance = Integer.parseInt(message.split(" ")[1]);
            entity.setBalance(balance);
            //updateBalance();
            entity.setStatus("Wait...");


            showOrderButton(false);
        } else if(message.contains("INGREDIENTS")) {
            if(fragmentMain.isVisible()) {
                //showOrderButton(ORDER_BUTTON_VISIBLE);
            }
            setLiquids(message);
            //makeToast("Fluids updated", SHORT);

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
                    //rätta till size från liquids!!
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
                if(fragmentMain.isVisible());
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
    public void doSomething() {
    }

    //implemented method for interface Communication
    @Override
    public void sendMessage(String message) {

        if(client != null) {
            client.sendMessage(message);
        }
    }

    private void sendAvareqToServer() {

        sendMessage("AVAREQ");
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.schedule(new CheckServer(), 2000, 2000);
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

    @Override
    public void onClick(View v) {
        if(v.getTag().equals(TAG_UPDATE)) {
            closeConnection();
            setResult(RE_LOGIN, returnIntent);
            finish();
        } else if(v.getTag().equals(TAG_LOGOUT)) {
            confirmSignOut();
        } else if(v.getTag().equals(TAG_3)) {
            //unused

        }
    }

    private class CheckServer extends TimerTask {
        @Override
        public void run() {
            sendAvareqToServer();
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
                //alertDialog.setIcon(R.drawable.tick);

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

        }
    }


}