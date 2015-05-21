package se.mah.ae2513.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by helengronvall on 15-05-20.
 */
public class Admin extends Activity {

    private EditText tvVolume;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);

        tvVolume = (EditText) findViewById(R.id.tvAdminVolume);
        btnSave = (Button) findViewById(R.id.btnAdminSetVolume);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvVolume.equals("")) {
                    Entity.getInstance().setMaxVolume(Integer.parseInt(tvVolume.getText().toString()));
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
