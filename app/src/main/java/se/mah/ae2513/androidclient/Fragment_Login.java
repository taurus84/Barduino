package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by David on 2015-04-18.
 */
public class Fragment_Login extends Fragment {
    EditText username, password;
    Button login, cancel;
    Communicator comm;
    Entity entity = Entity.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_layout, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        username = (EditText) getActivity().findViewById(R.id.etUsername_FragLogin);
        username.setText(entity.getUsername());
        password = (EditText) getActivity().findViewById(R.id.etPassword_FragLogin);
        login = (Button) getActivity().findViewById(R.id.btnLogin_fragLogin);
        cancel = (Button) getActivity().findViewById(R.id.btnCancel_fragLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                entity.setUsername(username.getText().toString());
                entity.setPassword(password.getText().toString());
                String message = "LOGIN " + entity.getUsername() +
                        ":" + entity.getPassword();
                comm.sendMessage(message);
                    getActivity().getFragmentManager().popBackStack();


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }
}
