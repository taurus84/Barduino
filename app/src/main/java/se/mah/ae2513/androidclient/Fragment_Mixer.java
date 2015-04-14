package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by John on 15-04-08.
 */
public class Fragment_Mixer extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4;
    private TextView cl1,cl2,cl3,cl4,liquid1,liquid2,liquid3,liquid4;
    private Entity entity = Entity.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mixer_layout, container, false);
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        seekBar1 = (SeekBar) getActivity().findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) getActivity().findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) getActivity().findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar) getActivity().findViewById(R.id.seekBar4);

        cl1 = (TextView) getActivity().findViewById(R.id.cl1);
        cl2 = (TextView) getActivity().findViewById(R.id.cl2);
        cl3 = (TextView) getActivity().findViewById(R.id.cl3);
        cl4 = (TextView) getActivity().findViewById(R.id.cl4);

        liquid1 = (TextView) getActivity().findViewById(R.id.liquid1);
        liquid2 = (TextView) getActivity().findViewById(R.id.liquid2);
        liquid3 = (TextView) getActivity().findViewById(R.id.liquid3);
        liquid4 = (TextView) getActivity().findViewById(R.id.liquid4);
        liquid1.setText(entity.getLiquids(0));
        liquid2.setText(entity.getLiquids(1));
        liquid3.setText(entity.getLiquids(2));
        liquid4.setText(entity.getLiquids(3));

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);
        seekBar4.setOnSeekBarChangeListener(this);


        

    }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            cl1 = (TextView) getActivity().findViewById(R.id.cl1);
            cl1.setText(seekBar1.getProgress() + " cl");

            cl2 = (TextView) getActivity().findViewById(R.id.cl2);
            cl2.setText(seekBar2.getProgress() + " cl");

            cl3 = (TextView) getActivity().findViewById(R.id.cl3);
            cl3.setText(seekBar3.getProgress() + " cl");

            cl4 = (TextView) getActivity().findViewById(R.id.cl4);
            cl4.setText(seekBar4.getProgress() + " cl");


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    public void orderDrink(){
        Button btnOrder = (Button) getActivity().findViewById(R.id.btnOrder);
        if(SeekBar.getDefaultSize(25,25) > 25){
            btnOrder.setVisibility(View.GONE);
        }
    }

    }

