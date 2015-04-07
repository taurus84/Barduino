package se.mah.ae2513.androidclient;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;




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
      //  setupConnectButton();
        connectWithThread();


    }

    /**
     * Setting up buttons and editTexts
     */
    private void setComponents() {
        etIP = (EditText) findViewById(R.id.etIP);
        etPort = (EditText) findViewById(R.id.etPort);
        send = (Button) findViewById(R.id.btnSend);
        stringText = (EditText) findViewById(R.id.etwrite);
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
    private void connectWithThread(){
        Button connectButton = (Button) findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            connectClicked();
                 mTcpClient = new TCPClient(etIP.getText().toString(),
                        Integer.parseInt(etPort.getText().toString()), new TCPClient.OnMessageReceived() {
                    @Override
                    public void messageReceived(String message) {

                    }

                });
                mTcpClient.start();

            }
        });
    }

    private void connectToServer() {
        mTcpClient = new TCPClient(etIP.getText().toString(),
                Integer.parseInt(etPort.getText().toString()), new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {

            }

        });
        mTcpClient.start();
    }
    public void connectClicked(){
        Intent getConnectLayout = new Intent(this, ThreadActivity.class);
        startActivity(getConnectLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
       // getMenuInflater().inflate(R.layout.connect_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.connect_now) {
           connectToServer();
            Log.i("Message:", "Clicked");
            return true;
        }else if (id == R.id.edit_ip_and_port ){
            fragmentIP();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fragmentIP() {
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        ConnectFragment CF = new ConnectFragment();
        FT.add(R.id.home_screen,CF);
        FT.commit();
    }

}
