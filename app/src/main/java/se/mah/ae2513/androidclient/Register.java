package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    private final int USER_CREATED = 0,CANCEL = 1;
    private InputMethodManager imm;


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
        etPassword_Register_2 = (EditText) findViewById(R.id.etPassword_register_2);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPassword_Register.getText().toString().equals(etPassword_Register_2.getText().toString())) {

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String[] userData = {et_Username_Register.getText().toString(),":",etPassword_Register.getText().toString()};
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("USER ", userData);
                    setResult(USER_CREATED, returnIntent);
                    finish();
                } else {
                   incorrectInput();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setResult(CANCEL);
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

}
