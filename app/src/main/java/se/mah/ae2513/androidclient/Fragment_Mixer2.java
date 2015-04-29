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
 * Created by David Tran on 15-04-29.
 */
public class Fragment_Mixer2 extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4;
    private TextView cl1,cl2,cl3,cl4,liquid1,liquid2,liquid3,liquid4, tvTotalVolume;
    private Entity entity = Entity.getInstance();
    private Button btnOrder;
    private Communicator comm;
    private int valueSeekBar1, valueSeekBar2, valueSeekBar3, valueSeekBar4, valueTotal;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.test_layout2, container, false);
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        comm = (Communicator) getActivity();
        seekBar1 = (SeekBar) getActivity().findViewById(R.id.seek1);
        seekBar2 = (SeekBar) getActivity().findViewById(R.id.seek2);
        seekBar3 = (SeekBar) getActivity().findViewById(R.id.seek3);
        seekBar4 = (SeekBar) getActivity().findViewById(R.id.seek4);

        cl1 = (TextView) getActivity().findViewById(R.id.tvVolume1);
        cl2 = (TextView) getActivity().findViewById(R.id.tvVolume2);
        cl3 = (TextView) getActivity().findViewById(R.id.tvVolume3);
        cl4 = (TextView) getActivity().findViewById(R.id.tvVolume4);
        tvTotalVolume = (TextView) getActivity().findViewById(R.id.tvTotalCl);

        liquid1 = (TextView) getActivity().findViewById(R.id.tvFluid1);
        liquid2 = (TextView) getActivity().findViewById(R.id.tvFluid2);
        liquid3 = (TextView) getActivity().findViewById(R.id.tvFluid3);
        liquid4 = (TextView) getActivity().findViewById(R.id.tvFluid4);
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

        valueSeekBar1 = seekBar1.getProgress();
        valueSeekBar2 = seekBar2.getProgress();
        valueSeekBar3 = seekBar3.getProgress();
        valueSeekBar4 = seekBar4.getProgress();
        valueTotal = valueSeekBar1 + valueSeekBar2 + valueSeekBar3 + valueSeekBar4;

        cl1.setText(valueSeekBar1 + " cl");
        cl2.setText(valueSeekBar2 + " cl");
        cl3.setText(valueSeekBar3 + " cl");
        cl4.setText(valueSeekBar4 + " cl");
        tvTotalVolume.setText(valueTotal + " cl");
        seekBar1.setBackgroundColor(0xff0000);




    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void setTextLiquids() {
        liquid1.setText(entity.getLiquids(0));
        liquid2.setText(entity.getLiquids(1));
        liquid3.setText(entity.getLiquids(2));
        liquid4.setText(entity.getLiquids(3));
    }
}
