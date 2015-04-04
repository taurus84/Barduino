package se.mah.ae2513.androidclient;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

    private Button connectButton, send;
    private EditText etIP, etPort, stringText;
    private TCPClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setComponents();
      //  connectToServer();
        setupConnectButton();


    }

    /**
     * *** Not used ***
     */
    private void connectToServer() {
        try {
            Socket socket = new Socket("192.168.1.2", 4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting up buttons and editTexts
     */
    private void setComponents() {
        etIP = (EditText) findViewById(R.id.textIP);
        etPort = (EditText) findViewById(R.id.textPort);
        send = (Button) findViewById(R.id.btnSend);
        stringText = (EditText) findViewById(R.id.editText);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from stringText and send it to server
                String message = stringText.getText().toString();
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                    stringText.setText("");
                }
            }
        });


    }

    private void setupConnectButton() {
        Button connectButton = (Button) findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new connectTask().execute("");
                //String ipNbr = etIP.getText().toString();
                //int port = Integer.parseInt(etPort.getText().toString());
                //Thread thread = new Thread(new TCPClient(ipNbr, port));
                //thread.start();
                //Log.i("Testing", "Det funkar kanske");

            }
        });
    }



    /**
     * aSyncTask to be used to perform actions in background while letting
     * the main activity run on foreground. In this example, the background
     * activity is the connection to the server.
     *
     * Three generics in interface AsyncTask
     * 1. Type of references passed to doInBackGround
     * 2. Type of reference passed to onProgressUpdate
     * 3. Type of reference returned by doInBackground
     */
    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... params) {
            mTcpClient = new TCPClient(etIP.getText().toString(), Integer.parseInt(etPort.getText().toString()), new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }
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
