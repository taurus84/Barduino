package se.mah.ae2513.androidclient;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

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
    private LayoutInflater l;
    private RelativeLayout fluidBox;
    private int idTracker = 100;
    private boolean firstComponent = true;
    private ArrayList<SeekBar> seekBars = new ArrayList<SeekBar>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.test_layout2, container, false);

        return v;

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        l = getActivity().getLayoutInflater();
        fluidBox = (RelativeLayout)getActivity().findViewById(R.id.fluidBox);
/*
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

        */

    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
/*
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
*/
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void setTextLiquids() {
       /* liquid1.setText(entity.getLiquids(0));
        liquid2.setText(entity.getLiquids(1));
        liquid3.setText(entity.getLiquids(2));
        liquid4.setText(entity.getLiquids(3));
        */

        View v = l.inflate(R.layout.fluid_item, fluidBox, false);

        //setup params
        RelativeLayout.LayoutParams loRules = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //has to have an id to put things below.
        v.setId(0 + idTracker);
        idTracker++;
        //no components put in?
        if(firstComponent) {
            loRules.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            firstComponent = false;
        } else {
            loRules.addRule(RelativeLayout.BELOW, v.getId()-1);
        }
        //margins of the v (RelativeLayout)
        loRules.setMargins(0, 0, 0, 0);

        //find Views (children) in the inflated ViewGrup so we can manipulate them
        final TextView tvVol = (TextView) v.findViewById(R.id.testVolume);
        TextView tvFluid = (TextView) v.findViewById(R.id.testFluid);
        SeekBar seekX = (SeekBar) v.findViewById(R.id.testSeek);
        seekBars.add(seekX);
        //seekbar listener
        seekX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //changing text when sliding on the seekbar
                tvVol.setText(seekBar.getProgress() + " cl");
                //changeTotalVolume();
                //tinging seekbar background
                seekBar.getBackground().setAlpha(255 - seekBar.getProgress() * 10);
                //seekBar.getBackground().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.ADD));
                //seekBar.getBackground().setColorFilter(new PorterDuffColorFilter(16777215/seekBar.getProgress(), PorterDuff.Mode.DARKEN));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        tvVol.setText(0 + " cl");
        tvFluid.setText("Fluid: " + v.getId());

        //add the view v to the container(parent) with the params defined in loRules
        fluidBox.addView(v, loRules);


    }
}
