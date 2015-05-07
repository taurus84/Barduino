package se.mah.ae2513.androidclient;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by David Tran on 15-04-29.
 */
public class Fragment_Main extends Fragment {

    private TextView tvTotalVolume, tvTotalPrice, tvStatus;
    private Entity entity = Entity.getInstance();
    private Button btnOrder;
    private Communicator comm;
    private LayoutInflater l;
    private RelativeLayout fluidBox;
    private int idTracker = 100;
    private boolean firstComponent = true;
    private ArrayList<SeekBar> seekBars = new ArrayList<SeekBar>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_layout, container, false);

        return v;

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        comm = (Communicator) getActivity();
        super.onActivityCreated(savedInstanceState);
        tvTotalVolume = (TextView) getActivity().findViewById(R.id.tvTotal);
        tvTotalPrice = (TextView) getActivity().findViewById(R.id.tvTotalPrice);
        l = getActivity().getLayoutInflater();
        fluidBox = (RelativeLayout)getActivity().findViewById(R.id.fluidBox);
        tvStatus = (TextView)getActivity().findViewById(R.id.status);
        btnOrder = (Button) getActivity().findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDrink();

            }
        });


    }

    private void orderDrink() {
        int totalVolume = 0;
        if(orderPrice() <= entity.getBalance()) {
            String message = "GROG ";
            for(int i = 0; i < seekBars.size(); i++) {
                int seekBarValue = seekBars.get(i).getProgress();
                totalVolume += seekBarValue;
                if(seekBarValue < 10) {
                    message += "0" + seekBarValue + " ";
                } else {
                    message += seekBarValue + " ";
                }
            }

            if(totalVolume == 0) {
                Toast toast = Toast.makeText(getActivity(), "Maybe you want something in your glass?", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
                toast.show();
            } else {
                receiptWindow(message);
            }
        } else {
            alertDialog("Sorry!", "Unsufficient fonds", "OK", 0);
        }
    }

    private void checkOrderOK () {

    }

    private int orderPrice() {
        int total = 0;
        for(int i = 0; i < seekBars.size(); i++) {
            int seekBarValue = seekBars.get(i).getProgress();
            if(seekBarValue != 0) {
                total += seekBarValue * entity.getLiquidPrices().get(i);
            }
        }
        return total;
    }


    public void setLiquids() {
        int nbrOfFluids = entity.getLiquids().size();
        for(int i = 0; i < nbrOfFluids; i++) {
            setTextLiquids(entity.getLiquids().get(i), entity.getLiquidPrices().get(i));
        }

    }


    public void setTextLiquids(String liquid, int price) {

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
        TextView tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        SeekBar seekX = (SeekBar) v.findViewById(R.id.testSeek);
        seekBars.add(seekX);
        //seekbar listener
        seekX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //changing text when sliding on the seekbar
                tvVol.setText(seekBar.getProgress() + " cl");
                changeTotalVolume();
                //tinging seekbar background
                seekBar.getBackground().setAlpha(255 - seekBar.getProgress() * 10);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        tvVol.setText(0 + " cl");
        tvFluid.setText("Fluid: " + liquid);
        tvPrice.setText(price + " kr/cl");

        //add the view v to the container(parent) with the params defined in loRules
        fluidBox.addView(v, loRules);


    }
    private void changeTotalVolume() {
        int totalVolume = 0;
        for(int i = 0; i < seekBars.size(); i++) {
            totalVolume += seekBars.get(i).getProgress();
        }
        tvTotalVolume.setText(totalVolume + " cl");
        tvTotalPrice.setText("Drink price: " + orderPrice() + "kr");
    }

    public void setStatusText(String text) {
        tvStatus.setText(text);

    }
    public void showOrderButton(boolean show) {
        if(show) {
            tvStatus.setVisibility(View.GONE);
            btnOrder.setVisibility(View.VISIBLE);
        } else {
            btnOrder.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
        }
    }

    private void sendGrog(String message) {
        comm.sendMessage(message);
    }



    public void receiptWindow(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String receiptStr = "";
        for(int i = 0; i < seekBars.size(); i++) {
            int seekBarValue = seekBars.get(i).getProgress();
            if(seekBarValue != 0) {
                receiptStr += entity.getLiquids().get(i) + "\t\t" + seekBarValue + " cl\n";
            }
        }
        receiptStr += "\nTotal Price: " + orderPrice() +" kr";

        builder.setTitle("Receipt")
                .setMessage(receiptStr)
                .setCancelable(true)

                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendGrog(message);

                        Toast toast = Toast.makeText(getActivity(), "Sending grog", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
                        toast.show();
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void alertDialog(String title, String message, String textButton, final int option) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton(textButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                doOnAlertDialogOk(option);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void doOnAlertDialogOk(int option) {
        switch (option) {
            case 0:
                break;
            case 1:
                Toast toast = Toast.makeText(getActivity(), "You clicked ok", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

        }
    }


}
