package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;

/**
 * This class is an Activity for the Login screen.
 * It lets the user either register a new user or log in to Barduino system
 *
 * Created by David Tran and John Tengvall 2015-04-17.
 */
public class Login extends Activity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private final int NO_CONNECTION = 0, LOGGED_OUT = 1, LOGIN_BAD = 2, RE_LOGIN = 3, SOCKET_TCP_ERROR = 4;
    private Entity entity = Entity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Login", "onStart is called");
        setComponents();
    }

    private void setComponents() {
        etUsername = (EditText) findViewById(R.id.etUsername_Login);
        etPassword = (EditText) findViewById(R.id.etPassword_Login);
        btnLogin = (Button) findViewById(R.id.btn_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preLogin();

            }
        });
        btnRegister = (Button) findViewById(R.id.btnRegister);
        //if Register button is pressed a new Activity is opened
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".Register");
                startActivityForResult(intent, 2);
            }
        });
    }

    /*
     * If admin is logging in, the Admin class/activity is shown.
     * Else login() is called
     */
    private void preLogin() {
        String adminUsername = entity.getAdminUsername();
        String adminPassword = entity.getAdminPassword();
        if(etUsername.getText().toString().toLowerCase().equals(adminUsername) &&
                etPassword.getText().toString().toLowerCase().equals(adminPassword)) {
            adminChoice();
        } else {
            login();
        }
    }

    /*
     * This method opens the MainActivity and parses along the username and password
     */
    private void login() {
        Intent intent = new Intent(".MainActivity");
        String username = etUsername.getText().toString().toLowerCase();
        String password = etPassword.getText().toString();
        //if username and password is ok, MainActivity is started
        if(checkValidUsernameAndPassword(username, password)) {
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivityForResult(intent, 1);
        }
    }

    /*
     * When returned from Register activity or MainActivity different parses can
     * be thrown back.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == LOGGED_OUT) {
                makeToast("Signed out");
            } else if (resultCode == NO_CONNECTION) {
                makeToast("No connection to server");
            } else if (resultCode == LOGIN_BAD) {
                makeToast("Wrong username or password");
            } else if(resultCode == RE_LOGIN) {
                login();
            } else if(resultCode == SOCKET_TCP_ERROR) {
                login();
            }
        }
        else if(requestCode == 2) {
            if (resultCode == 1) {
                //extract aktivity result. Contains username and password
                String username = data.getStringExtra("username");
                etUsername.setText(username);
            }
        }
    }

    /*
     * This method checks if the input in username and password is ok.
     * @return boolean true or false depending if the input is ok
     */
    private boolean checkValidUsernameAndPassword(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            makeToast("You forgot to fill in all boxes!");
            return false;
        } else if(username.contains(",")
                ||username.contains(" ")
                ||username.contains(":")) {
            makeToast("Invalid characters in Username field!");
            return false;
        } else if(password.contains(",")
                ||password.contains(" ")
                ||password.contains(":")) {
            makeToast("Invalid characters in Password field!");
            return false;
        } else {
            return true;
        }
    }

    /*
     * This method lets the admin choose to go to Admin screen or login
     * to Barduino system.
     */
    private void adminChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hello Admin!")
                .setMessage("Admin screen or proceed to Barduino?")
                .setCancelable(true)
                .setNegativeButton("Admin screen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(".Admin");
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Barduino", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        login();
                    }
                });

        builder.create().show();        // create and show the alert dialog
    }

    /*
     * Creates a Toast screen with information for the user. A small textbox flashing up
     * for a few seconds
     */
    private void makeToast(String toastMessage) {
        Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
        toast.show();
    }
}
