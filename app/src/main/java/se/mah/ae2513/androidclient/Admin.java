package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by helengronvall on 15-05-20.
 */
public class Admin extends Activity {

    private EditText tvVolumeTotal, tvVolumeSingle;
    private Button btnSaveTotal, btnSaveSingle, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);

        tvVolumeTotal = (EditText) findViewById(R.id.tvAdminVolume);
        tvVolumeSingle = (EditText) findViewById(R.id.tvAdminVolumeSingle);
        btnSaveTotal = (Button) findViewById(R.id.btnAdminSetVolume);
        btnSaveTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvVolumeTotal.equals("")) {
                    try {
                        Entity.getInstance().setMaxVolume(Integer.parseInt(tvVolumeTotal.getText().toString()));
                    } catch (NumberFormatException e) {
                        Entity.getInstance().setMaxVolume(0);
                    }
                    finish();
                }
            }
        });
        btnSaveSingle = (Button) findViewById(R.id.btnAdminSetSingleVolume);
        btnSaveSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvVolumeSingle.equals("")) {
                    try {
                        Entity.getInstance().setMaxVolumeSingleContainer(Integer.parseInt(tvVolumeSingle.getText().toString()));
                    } catch (NumberFormatException e) {
                        Entity.getInstance().setMaxVolumeSingleContainer(0);
                    }
                    finish();
                }
            }
        });

        btnCancel = (Button)findViewById(R.id.btnAdminCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
