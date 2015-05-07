package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by John on 15-05-01.
 */
public class Register extends Activity {
    private EditText etPassword_Register,etPassword_Register_2,et_Username_Register;
    private Button btnSubmit,btnCancel;
    private Entity entity = Entity.getInstance();
    private final int USER_CREATED = 1,CANCEL = 0;
    private InputMethodManager imm;
    private TCPRegister client;
    private int serverPortNbr = 4444;
    private final int NO_ACTION = 0;
    private Intent returnIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        returnIntent = getIntent();
        setComponents();
    }

    private void setComponents() {
        et_Username_Register = (EditText) findViewById(R.id.etUsername_register);
        etPassword_Register = (EditText) findViewById(R.id.etPassword_register);
        //etPassword_Register.setTransformationMethod(new PasswordTransformationMethod());
        etPassword_Register_2 = (EditText) findViewById(R.id.etPassword_register_2);
        //etPassword_Register_2.setTransformationMethod(new PasswordTransformationMethod());
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = etPassword_Register.getText().toString();
                String password2 = etPassword_Register_2.getText().toString();
                String username =  et_Username_Register.getText().toString();
                if(checkValidUsernameAndPassword(username, password1, password2)) {
                    connectToserver(username, password1);
                }

                /*if(password1.equals(password2)) {

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String[] userData = {et_Username_Register.getText().toString(),etPassword_Register.getText().toString()};
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("USER ", userData);
                    //setResult(USER_CREATED, returnIntent);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                   incorrectInput();

                }*/
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


    private void incorrectInput() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Incorrect input");
        alertDialog.setMessage("Your passwords do not match");
        alertDialog.setCancelable(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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

    public void msgFromServer(String serverMessage) {
        if(serverMessage.equals("REGISTER OK")) {
            alertDialog("", "Your registration was successful!", "Press here to return to login screen", 1);
            //setResult(RESULT_OK, returnIntent);
            //finish();
        } else if(serverMessage.equals("REGISTER BAD")) {
            alertDialog("Error!", "You are already registered!", "OK", 0);
        }
    }

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
            case 1:
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                finish();


        }
    }
}
