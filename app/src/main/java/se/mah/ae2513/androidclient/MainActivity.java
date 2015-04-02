package se.mah.ae2513.androidclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


/**
 * The class creates a connection to a server where user chooses
 * ip-address and port number of the server.
 */
public class MainActivity extends ActionBarActivity {

    private Button connectButton;
    private EditText etIP, etPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setComponents();
        setupConnectButton();
    }

    private void setComponents() {
        etIP = (EditText) findViewById(R.id.textIP);
        etPort = (EditText) findViewById(R.id.textPort);
    }

    private void setupConnectButton() {
        Button connectButton = (Button) findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String ipNbr = etIP.getText().toString();
                int port = Integer.parseInt(etPort.getText().toString());
                Thread thread = new Thread(new TCPClient(ipNbr, port));
                thread.start();

            }
        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
