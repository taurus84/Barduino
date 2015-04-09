package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by John on 15-04-07.
 */
public class Fragment_Edit extends Fragment implements View.OnClickListener {
    Button buttonSave, buttonConnectNow;
    Communicator comm;
    Entity entity = Entity.getInstance();
    EditText etIP, etPort;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.edit_layout, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        buttonSave = (Button) getActivity().findViewById(R.id.btnSave);
        buttonConnectNow = (Button) getActivity().findViewById(R.id.btnConnectNow);
        buttonSave.setOnClickListener(this);
        buttonConnectNow.setOnClickListener(this);
        etIP = (EditText) getActivity().findViewById(R.id.etIP);
        etPort = (EditText) getActivity().findViewById(R.id.etPort);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                entity.setIpNbr( etIP.getText().toString());
                entity.setPortNbr(Integer.parseInt(etPort.getText().toString()));
                break;
            case R.id.btnConnectNow:
                entity.setIpNbr( etIP.getText().toString());
                entity.setPortNbr(Integer.parseInt(etPort.getText().toString()));
                //comm.connectNow();
                comm.doSomething();
                break;
        }

    }
}
