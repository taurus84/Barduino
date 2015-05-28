package se.mah.ae2513.androidclient;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
 * This class is a fragment which from has a dynamic UI depending on how
 * many fluids the server has in the system.
 *
 * This fragment is a part of the layout in MainActivity.
 * The fragment holds its own logic, because in Andoroid only the class which
 * creates a View should be able to change the View.
 *
 * Created by David Tran and John Tengvall 2015-04-29.
 */
public class Fragment_Main extends Fragment {
    private TextView tvTotalVolume, tvTotalPrice, tvStatus, tvVolumeError;
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
        View v = inflater.inflate(R.layout.layout_fragment_main, container, false);
        return v;
    }

    /**
     * This method is called after the called class, MainActivity, is done doing its
     * user interface. This way, collisions can be avoided, which could generate
     * nullpointer exceptions.
     *
     * @param savedInstanceState
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        comm = (Communicator) getActivity();
        super.onActivityCreated(savedInstanceState);
        setComponents();
    }

    //set up the Views
    private void setComponents() {
        tvTotalVolume = (TextView) getActivity().findViewById(R.id.tvTotal);
        tvTotalPrice = (TextView) getActivity().findViewById(R.id.tvTotalPrice);
        tvVolumeError = (TextView) getActivity().findViewById(R.id.tvVolumeOverMax);
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

    /*
     * This method is called when the user press the Order button.
     * It checks if the user has enough credits to order the drink, if the volume
     * of the drink exceeds the maximum allowed.
     */
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
            } else if(totalVolume > entity.getMaxVolume()) {
                alertDialog("ERROR!\n Your drink is too big", "Maximum volume allowed is " + entity.getMaxVolume() + "cl\n\n" +
                                "Please go back and change your order.", "OK", 0);
            } else {
                receiptWindow(message);
            }
        } else {
            alertDialog("Sorry!", "Unsufficient funds", "OK", 0);
        }
    }

    /*
     * The method calculates the cost of the drink
     * @return int the price of the drink
     */
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

    /**
     * This method creates the interface depending on how many liquids the server has.
     */
    public void setLiquids() {
        //how many liquids?
        int nbrOfFluids = entity.getLiquids().size();
        for(int i = 0; i < nbrOfFluids; i++) {
            //set name and price for every liquid
            setLiquids(entity.getLiquids().get(i), entity.getLiquidPrices().get(i));
        }
    }

    /*
     * Method called from setLiquids()
     * @param Sting liquid the name of the liquid
     * @param int price the price for the liquid per centiliter
     */
    private void setLiquids(String liquid, int price) {
        View v = l.inflate(R.layout.fluid_item, fluidBox, false);
        //setup params
        RelativeLayout.LayoutParams loRules = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //has to have an id to use below param in relativeLayout. ID starts from 100
        v.setId(0 + idTracker);
        idTracker++;
        //no components put in yet?
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
        //Maximum allowed volume for each liquid
        seekX.setMax(entity.getMaxVolumeSingleContainer());
        seekBars.add(seekX);
        seekX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //changing text when sliding on the seekbar
                tvVol.setText(seekBar.getProgress() + " cl");
                changeTotalVolume();
                //tinting seekbar background, so the red background is shown when seekbar slides
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
        tvFluid.setText("" + liquid);
        tvPrice.setText(price + " kr/cl");
        //add the view v to the container(parent) with the params defined in loRules
        fluidBox.addView(v, loRules);
    }

    /*
     * This method is called when a slider value is changed.
     * Changes the totalVolume and price of the drink
     */
    private void changeTotalVolume() {
        //declaring the totalVolume
        int totalVolume = 0;
        //setting the maximum allowed drink size. Data from Entity class
        int maxVolume = entity.getMaxVolume();
        //sums up the values from all seekbars
        for(int i = 0; i < seekBars.size(); i++) {
            totalVolume += seekBars.get(i).getProgress();
        }
        //if drink is too big, change textcolor to Red and show an error text
        if(totalVolume > maxVolume) {
            tvTotalVolume.setTextColor(getResources().getColor(R.color.logoColor));
            tvVolumeError.setText("Your drink is too big! Maximum size is " + maxVolume + "cl");
            tvVolumeError.setVisibility(View.VISIBLE);
        } else {
            tvTotalVolume.setTextColor(getResources().getColor(R.color.textColor));
            tvVolumeError.setVisibility(View.GONE);
        }
        //set the textviews with drink size and price
        tvTotalVolume.setText(totalVolume + " cl");
        tvTotalPrice.setText("Drink price: " + orderPrice() + "kr");
    }

    /**
     * This method sets the text on the bottom of the app, which is
     * a status indicator for the user.
     * @param text
     */
    public void setStatusText(String text) {
        tvStatus.setText(text);
    }

    /**
     * This method toggles between either showing the Order button or
     * the status string.
     * @param show
     */
    public void showOrderButton(boolean show) {
        if(show) {
            tvStatus.setVisibility(View.GONE);
            btnOrder.setVisibility(View.VISIBLE);
        } else {
            btnOrder.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
        }
    }

    /*
     * This method is a confirmation window for the user before sending the grog to the server.
     * It generates the string which fits the protocol and if the user confirms it will  call
     * the sendGrog method.
     */
    private void receiptWindow(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Build the string
        String receiptStr = "";
        for(int i = 0; i < seekBars.size(); i++) {
            int seekBarValue = seekBars.get(i).getProgress();
            if(seekBarValue != 0) {
                receiptStr += entity.getLiquids().get(i) + "\t\t" + seekBarValue + " cl\n";
            }
        }
        receiptStr += "\nTotal Price: " + orderPrice() +" kr";
        //the alertdialog preferences
        builder.setTitle("Receipt")
                .setMessage(receiptStr)
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendGrog(message);
                    }
                });
        builder.create().show();
    }

    /*
     * This method uses the Communicator interface to send a message to
     * the server containing the ordered grog
     */
    private void sendGrog(String message) {
        comm.sendMessage(message);
        setStatusText("Please wait...");
        showOrderButton(false);
    }

    /*
     * A method to create an alertDialog to give the user information
     * @param String title the title on the top of the dialog screen
     * @param String message the message to the user
     * @param String textButton the text on the button the user can click, often 'OK'
     * @param int option which action to be called in method doOnAlertDialog
     */
    private void alertDialog(String title, String message, String textButton, final int option) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.logo_b);
        alertDialog.setCancelable(false);
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

    /*
     * Different actions can be executed from an alertDialog.
     */
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
