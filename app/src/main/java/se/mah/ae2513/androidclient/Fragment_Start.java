package se.mah.ae2513.androidclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by John on 15-04-08.
 */
public class Fragment_Start extends Fragment {
    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4;
    private TextView cl1,cl2,cl3,cl4;
    private int seekbarMax = 25;
    private ArrayList <SeekBar> seekBarList = new ArrayList<SeekBar>();
    private ArrayList <TextView> textViewList = new ArrayList<TextView>();
    private ArrayList <TextView> ratioLbls = new ArrayList<TextView>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.start_layout, container, false);
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SeekBarsListener listener = new SeekBarsListener();
        seekBar1 = (SeekBar) getActivity().findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) getActivity().findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) getActivity().findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar) getActivity().findViewById(R.id.seekBar4);
        seekBarList.add(seekBar1);
        seekBarList.add(seekBar2);
        seekBarList.add(seekBar3);
        seekBarList.add(seekBar4);

        textViewList.add(cl1);
        textViewList.add(cl2);
        textViewList.add(cl3);
        textViewList.add(cl4);

        seekBar1.setOnSeekBarChangeListener(listener);
        seekBar2.setOnSeekBarChangeListener(listener);
        seekBar3.setOnSeekBarChangeListener(listener);
        seekBar4.setOnSeekBarChangeListener(listener);
    }

    public class SeekBarsListener implements SeekBar.OnSeekBarChangeListener{

        private void updateValues(Object object) {
            int sumSliders = 0;
            Iterator<TextView> iterLabel;
            Iterator<SeekBar> iterSlider = seekBarList.iterator();
            SeekBar tempSlider;
            TextView tempLabel;

            while (iterSlider.hasNext()) {
                tempSlider = iterSlider.next();
                tempSlider.setMax(seekbarMax);
                sumSliders += tempSlider.getProgress();
            }

            iterSlider = seekBarList.iterator();
            iterLabel = textViewList.iterator();
            int sliderValue;
            double percentageDouble;
            int percentage;

            if (sumSliders > seekbarMax) {
                int decreaseSum = sumSliders - seekbarMax;
                int nbrOfSlidersToShare = 0;

                while (iterSlider.hasNext()) {
                    tempSlider = iterSlider.next();
                    if (object != tempSlider) {
                        if (tempSlider.getProgress() != 0) {
                            nbrOfSlidersToShare++;
                        }
                    }
                }

                iterSlider = seekBarList.iterator();

                while (iterSlider.hasNext()) {
                    tempSlider = iterSlider.next();
                    if (object != tempSlider) {
                        if (tempSlider.getProgress() < (decreaseSum / nbrOfSlidersToShare)) {
                            decreaseSum -= tempSlider.getProgress();
                            tempSlider.setProgress(0);
                        } else {
                            tempSlider.setProgress(tempSlider.getProgress()
                                    - (decreaseSum / nbrOfSlidersToShare));
                        }
                    }
                }

            }

            iterSlider = seekBarList.iterator();

            while (iterSlider.hasNext()) {
                tempLabel = iterLabel.next();
                sliderValue = iterSlider.next().getProgress();
                percentageDouble = ((double) sliderValue / sumSliders) * seekbarMax;
                percentage = (int) percentageDouble;
                if (percentageDouble - percentage > 0.5)
                    percentage++;

                tempLabel.setText(percentage + " cl");
            }
        }


        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateValues(seekBar);
            //cl1 = (TextView) getActivity().findViewById(R.id.cl1);
            //cl1.setText(seekBar1.getProgress() + " cl");

            //cl2 = (TextView) getActivity().findViewById(R.id.cl2);
            //cl2.setText(seekBar2.getProgress() + " cl");

            //cl3 = (TextView) getActivity().findViewById(R.id.cl3);
            //cl3.setText(seekBar3.getProgress() + " cl");

            //cl4 = (TextView) getActivity().findViewById(R.id.cl4);
            //cl4.setText(seekBar4.getProgress() + " cl");


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
