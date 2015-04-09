package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by John on 15-04-07.
 */
public class Fragment_Update extends Fragment implements View.OnClickListener{

    Communicator comm;
    Button button, button2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.update_layout, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (Button)getActivity().findViewById(R.id.avareqToServer);
        button2 = (Button)getActivity().findViewById(R.id.grogToServer);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        comm = (Communicator) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avareqToServer:
                comm.doSomething();
                break;
            case R.id.grogToServer:
                comm.sendGrog();
        }

    }
}
