package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by John on 15-04-30.
 */
public class Fragment_Login2 extends Fragment {
    EditText username, password;
    Button login, register, cancel;
    Communicator comm;
    Entity entity = Entity.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_login, container, false);
        return v;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        username = (EditText) getActivity().findViewById(R.id.etUsername_Login);
        username.setText(entity.getUsername());
        password = (EditText) getActivity().findViewById(R.id.etPassword_Login);
        password.setTransformationMethod(new PasswordTransformationMethod());
        login = (Button) getActivity().findViewById(R.id.btn_Login);
        register = (Button) getActivity().findViewById(R.id.btnRegister);
        cancel = (Button) getActivity().findViewById(R.id.btnCancel_Login);

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".Register");
                startActivityForResult(intent, 2);
            }
        });

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == 1) {
                //extract aktivity result. Contains username and password
                String[] userData = data.getStringArrayExtra("USER ");
                String message = "REGISTER " + userData[0] + userData[1] + userData[2];
                comm.sendMessage(message);
                Log.i("ddd",message);


                //makeToast("Logged out");
            }
            else if(resultCode == 0){
                //for cancelbutton in Register
                Toast.makeText(getActivity(), "Back pressed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
