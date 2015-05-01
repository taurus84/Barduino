package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by John on 15-05-01.
 */
public class Register extends Activity {
   private EditText etPassword_Register,etPassword_Register_2,et_Username_Register;
   private Button btnSubmit,btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setComponents();
    }

    private void setComponents() {
        et_Username_Register = (EditText) findViewById(R.id.etUsername_register);
        etPassword_Register = (EditText) findViewById(R.id.etPassword_register);
        etPassword_Register_2 = (EditText) findViewById(R.id.etPassword_register_2);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }
}
