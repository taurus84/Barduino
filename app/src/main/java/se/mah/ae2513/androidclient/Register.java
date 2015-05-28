package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is an Activity for registration of a new user
 * Created by John on 15-05-01.
 */
public class Register extends Activity {
    private EditText etPassword_Register,etPassword_Register_2,et_Username_Register;
    private Button btnSubmit,btnCancel;
    private Entity entity = Entity.getInstance();
    private final int  NO_ACTION = 0,REGISTRATION_OK = 1;
    private InputMethodManager imm;
    private TCPRegister client;
    private int serverPortNbr = 4444;
    private Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        returnIntent = getIntent();
        setComponents();
    }

    private void setComponents() {
        et_Username_Register = (EditText) findViewById(R.id.etUsername_register);
        etPassword_Register = (EditText) findViewById(R.id.etPassword_register);
        etPassword_Register_2 = (EditText) findViewById(R.id.etPassword_register_2);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * This method checks in the input is ok, and if ok a connection
     * to the server will be established and a message is sent to server.
     */
    private void registerUser() {
        String password1 = etPassword_Register.getText().toString();
        String password2 = etPassword_Register_2.getText().toString();
        String username =  et_Username_Register.getText().toString().toLowerCase();
        if(checkValidUsernameAndPassword(username, password1, password2)) {
            connectToserver(username, password1);
        }
    }

    /*
     * This method checks if the input on 'username' and 'password' is ok
     * @param String username
     * @param String password1 the string in first password row
     * @param String password2 the string in second password row
     * @return boolean ok or not
     */
    private boolean checkValidUsernameAndPassword(String username, String password1, String password2) {
        if(username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            alertDialog("Error!", "You forgot to fill in all boxes!", "OK", NO_ACTION);
            return false;
        } else if(username.contains(",")
                ||username.contains(" ")
                ||username.contains(":")) {
            alertDialog("Username Error!", "Your username contains invalid characters!", "OK", 0);
            return false;
        } else if(password1.contains(",")
                ||password1.contains(" ")
                ||password1.contains(":")
                ||password2.contains(",")
                ||password2.contains(" ")
                ||password2.contains(":")) {
            alertDialog("Error!", "Your password contains invalid characters!", "OK", 0);
            return false;
        } else if(!password1.equals(password2)) {
            alertDialog("Password Error!", "Your passwords do not match!", "OK", 0);
            return false;
        } else {
            return true;
        }
    }

    private void connectToserver(String username, String password) {
        client = new TCPRegister(entity.getIpNbr(), serverPortNbr, this);
        client.start();
        while(!client.isConnected()) {
            if(client.ConnectFailed()) {
                break;
            }
        }
        if(client.isConnected() && !client.ConnectFailed()) {
            String message = "REGISTER " + username + ":" + password;
            client.sendMessage(message);
            Log.i("Skickat till server:", message);
        }
    }

    /**
     * This method handles the messages from server. The message either contains
     * 'REGISTER OK' or 'REGISTER BAD'
     * @param serverMessage
     */
    public void msgFromServer(String serverMessage) {
        if(serverMessage.equals("REGISTER OK")) {
            //Success registration
            alertDialog("", "Your registration was successful!", "Press here to return to login screen", REGISTRATION_OK);
        } else if(serverMessage.equals("REGISTER BAD")) {
            alertDialog("Error!", "You are already registered!", "OK", 0);
        }
        client.sendMessage("STOP");
    }

    /*
     * A method to create an alertDialog to give the user information
     * @param String title the title on the top of the dialog screen
     * @param String message the message to the user
     * @param String textButton the text on the button the user can click, often 'OK'
     * @param int option which action to be called in method doOnAlertDialog
     */
    private void alertDialog(final String title, final String message, final String textButton, final int option) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
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

    /*
     * Different actions can be executed from an alertDialog.
     */
    private void doOnAlertDialogOk(int option) {
        switch (option) {
            case NO_ACTION:
                break;
            case REGISTRATION_OK:
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                returnIntent.putExtra("username", et_Username_Register.getText().toString());
                setResult(REGISTRATION_OK, returnIntent);
                finish();
        }
    }
}
