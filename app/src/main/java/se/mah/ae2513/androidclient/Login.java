package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

/**
 * Created by David on 2015-04-17.
 */
public class Login extends Activity {

    private EditText etUsername, etPassword, etPort, etIP;
    private TextView tvUsername;
    private TextView tvPassword;
    private TextView tvErrorMessage;
    private Button btnLogin;
    private final int NO_CONNECTION = 0, LOGGED_OUT = 1;
    private Timer udpTimer;
    private String serverIP, serverPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectUDP();
        setContentView(R.layout.test_login);

        setComponents();
    }

    private void setComponents() {
        etUsername = (EditText) findViewById(R.id.etUsername_Login);
        etPassword = (EditText) findViewById(R.id.etPassword_Login);
        //etIP = (EditText) findViewById(R.id.etIP);
        //etPort = (EditText) findViewById(R.id.etPort);
        //tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        btnLogin = (Button) findViewById(R.id.btn_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".MainActivity");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!username.isEmpty()) {
                    intent.putExtra("username", username);
                }
                if (!password.isEmpty()) {
                    intent.putExtra("password", password);
                }
                startActivityForResult(intent, 1);

                intent.putExtra("ipnumber", serverIP);
                intent.putExtra("port", serverPort);
                startActivityForResult(intent, 1);

            }
        });

    }

    public void connectUDP() {
        udpTimer = new Timer();
        udpTimer.scheduleAtFixedRate(new UDP(this), 0, 2000);
    }

    public void stopUDPTimer() {
        udpTimer.cancel();
        udpTimer.purge();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == LOGGED_OUT) {
                makeToast("Logged out");
            } else if (resultCode == NO_CONNECTION) {
                //String result=data.getStringExtra("result");
                //Log.i("Hej", "hej");
                makeToast("No connection to server");
            }
        }
    }

    private void makeToast(String toastMessage) {
        Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    public void setIP(String serverIP, int portNbr) {
        this.serverIP = serverIP;
        this.serverPort = Integer.toString(portNbr);
    }
}
