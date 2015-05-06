package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setComponents();
    }

    private void setComponents() {
        et_Username_Register = (EditText) findViewById(R.id.etUsername_register);
        etPassword_Register = (EditText) findViewById(R.id.etPassword_register);
        etPassword_Register.setTransformationMethod(new PasswordTransformationMethod());
        etPassword_Register_2 = (EditText) findViewById(R.id.etPassword_register_2);
        etPassword_Register_2.setTransformationMethod(new PasswordTransformationMethod());
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = etPassword_Register.getText().toString();
                String password2 = etPassword_Register_2.getText().toString();
                String username =  et_Username_Register.getText().toString();
                if(password1.equals(password2)) {
                    connectToserver(username, password1);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String[] userData = {et_Username_Register.getText().toString(),etPassword_Register.getText().toString()};
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("USER ", userData);
                    //setResult(USER_CREATED, returnIntent);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                   incorrectInput();
                }
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
            finish();
        } else {
            //meddela felet
        }
    }
}
