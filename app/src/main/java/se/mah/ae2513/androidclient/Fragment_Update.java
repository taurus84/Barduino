package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by John on 15-04-07.
 */
public class Fragment_Update extends Fragment implements View.OnClickListener{

    Communicator comm;
    Button button, button2, button3;
    EditText editText;
    TextView tvMess;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.update_layout, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get reference to EditText in fragment
        editText = (EditText)getActivity().findViewById(R.id.etMessage);
        button = (Button)getActivity().findViewById(R.id.avareqToServer);
        button2 = (Button)getActivity().findViewById(R.id.grogToServer);
        button3 = (Button) getActivity().findViewById(R.id.btnSendMessage);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        tvMess = (TextView) getActivity().findViewById(R.id.tvServerMessage);
        //type convert mainActivity to interface Communicator
        comm = (Communicator) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avareqToServer:
                comm.sendMessage("AVAREQ");
                break;
            case R.id.grogToServer:
                comm.sendMessage("GROG 99");
                break;
            case R.id.btnSendMessage:
                comm.sendMessage(editText.getText().toString());
        }
    }
}
