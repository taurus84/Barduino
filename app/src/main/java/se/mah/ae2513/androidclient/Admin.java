package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is used for the Admin screen. With right username and password
 * the user can access this activity.
 * From the admin screen, the admin can change the totalvolume allowed and the
 * max volume for single fluids.
 *
 * Created by David Tran on 15-05-20.
 */
public class Admin extends Activity implements View.OnClickListener {

    private EditText tvVolumeTotal, tvVolumeSingle;
    private Button btnSaveTotal, btnSaveSingle, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);
        setComponents();
    }

    //declare the Views used in class
    private void setComponents() {
        tvVolumeTotal = (EditText) findViewById(R.id.tvAdminVolume);
        tvVolumeSingle = (EditText) findViewById(R.id.tvAdminVolumeSingle);
        btnSaveTotal = (Button) findViewById(R.id.btnAdminSetVolume);
        btnSaveSingle = (Button) findViewById(R.id.btnAdminSetSingleVolume);
        btnCancel = (Button)findViewById(R.id.btnAdminCancel);
        //onclickListeners
        btnSaveTotal.setOnClickListener(this);
        btnSaveSingle.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /**
     * Handles the button clicks
     * @param v the pressed button
     */
    @Override
    public void onClick(View v) {
        if(v == btnSaveTotal) {
            if (!tvVolumeTotal.equals("")) {
                try {
                    //save it to the Entity class
                    Entity.getInstance().setMaxVolume(Integer.parseInt(tvVolumeTotal.getText().toString()));
                } catch (NumberFormatException e) {
                    //if admin types other than numbers, set max to 0
                    Entity.getInstance().setMaxVolume(0);
                }
                finish();
            }
        } else if (v == btnSaveSingle) {
            if (!tvVolumeSingle.equals("")) {
                try {
                    //save it to the Entity class
                    Entity.getInstance().setMaxVolumeSingleContainer(Integer.parseInt(tvVolumeSingle.getText().toString()));
                } catch (NumberFormatException e) {
                    //if admin types other than numbers, set max to 0
                    Entity.getInstance().setMaxVolumeSingleContainer(0);
                }
                finish();
            }
        } else if(v == btnCancel) {
            finish();
        }
    }

    /**
     * Finish the activity when back is pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
