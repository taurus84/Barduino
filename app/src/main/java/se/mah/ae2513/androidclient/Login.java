package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by David on 2015-04-17.
 */
public class Login extends Activity {

    private EditText etUsername, etPassword, etPort, etIP;
    private TextView tvUsername;
    private TextView tvPassword;
    private TextView tvErrorMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setComponents();
    }

    private void setComponents() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etIP = (EditText) findViewById(R.id.etIP);
        etPort = (EditText) findViewById(R.id.etPort);
        //tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(!username.isEmpty()) {
                    intent.putExtra("username", username);
                }
                if(!password.isEmpty()) {
                    intent.putExtra("password", password);
                }
                String ip = etIP.getText().toString();
                String port = etPort.getText().toString();
                intent.putExtra("ipnumber", ip);
                intent.putExtra("port", port);
                if(ip.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Input ipnummer", Toast.LENGTH_LONG).show();
                } else if(port.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Input Portnummer", Toast.LENGTH_LONG).show();
                } else
                    startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }








}
