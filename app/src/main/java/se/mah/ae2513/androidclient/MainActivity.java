package se.mah.ae2513.androidclient;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The class creates a connection to a server where user chooses
 * ip-address and port number of the server.
 */
public class MainActivity extends ActionBarActivity implements Communicator{

    private Button connectButton, send, disCon, btnCon;
    private EditText etIP, etPort, stringText;
    private TCPClient mTcpClient;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment_Connect fragCon;
    private Fragment_Update fragUp;
    private Fragment_Edit fragEdit;
    private Fragment_Start fragStart;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        createFragments();
        initialize();
        setComponents();
      //  setupConnectButton();
        //connectWithThread();
    }

    private void initialize() {

        controller = new Controller(this);

    }

    /**
     * Setting up buttons and editTexts
     */
    private void setComponents() {
  /*      etIP = (EditText) findViewById(R.id.etIP);
        etPort = (EditText) findViewById(R.id.etPort);
        send = (Button) findViewById(R.id.btnSend);
        disCon = (Button) findViewById(R.id.btnDiscon);
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
        disCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTcpClient.setmRun(false);
            }
        });

        btnCon = (Button) findViewById(R.id.buttonConnect);
        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //controller.connectToServer();
                connectToServer();
            }
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
       // getMenuInflater().inflate(R.layout.edit_layout, menu);
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
            controller.connectToServer();
            //connectToServer();
            return true;
        }else if (id == R.id.edit_ip_and_port ){
            fragmentIP();
        } else if (id == R.id.update_now) {
            update();
        } else if (id == R.id.mixer) {
            fragmentMixer();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createFragments() {

        fragCon = new Fragment_Connect();
        fragUp = new Fragment_Update();
        fragEdit = new Fragment_Edit();
        fragStart = new Fragment_Start();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.add(R.id.fr_id,fragEdit);
        transaction.commit();
        //transaction.add(R.id.fr_id,fragUp);
        //transaction.add(R.id.fr_id,fragEdit);
    }

    private void connect() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragCon);
        transaction.addToBackStack(null);
        transaction.commit();
//        transaction.commitAllowingStateLoss();
    }

    private void update() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
  //      fragUp = new Fragment_Update();
        transaction.replace(R.id.fr_id, fragUp);
        transaction.addToBackStack(null);
        transaction.commit();
//        transaction.commitAllowingStateLoss();
    }

    private void fragmentIP() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
  //      fragEdit = new Fragment_Edit();
        transaction.replace(R.id.fr_id, fragEdit);
        transaction.addToBackStack(null);
       transaction.commit();
//        transaction.commitAllowingStateLoss();
    }

    private void fragmentMixer() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_id, fragStart);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void connect(View view) {

    }

    @Override
    public void doSomething() {


        controller.sendMessageToServer("AVAREQ");
    }

    public void sendGrog() {
        controller.sendMessageToServer("GROG 99");
    }

    @Override
    public void connectNow() {
        controller.connectToServer();
    }

}
