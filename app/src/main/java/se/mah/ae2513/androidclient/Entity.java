package se.mah.ae2513.androidclient;

import android.view.View;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by John on 15-04-08.
 */
public class Entity {

    private static Entity entity = new Entity();
    private int portNbr = 4444;
    private String ipNbr, username, password;
    private ArrayList<String> liquids;

    private ArrayList<Integer> liquidPrices;
   // private String[] liquids = new String[4];
    private String status = "Not connected";
    private int balance = 0, maxVolume = 25;
//    private int nbrOfFluids = getLiquids().size();



    private Entity() {
    }


    public static Entity getInstance() {
        return entity;
    }

    public synchronized int getMaxVolume() {
        return maxVolume;
    }

    public synchronized void setMaxVolume(int volume) {
        this.maxVolume = volume;
    }

    public synchronized ArrayList<String> getLiquids() {
        return liquids;
    }

    public synchronized void setLiquids(ArrayList<String> liquids) {
/*        StringTokenizer st = new StringTokenizer(ingredients,":");
        String waste = st.nextToken();
        String allIngredients = st.nextToken();
        StringTokenizer st2 = new StringTokenizer(allIngredients,",");
        liquids[0] = st2.nextToken();
        liquids[1] = st2.nextToken();
        liquids[2] = st2.nextToken();
        liquids[3] = st2.nextToken();

        liquids[0] = ingredients[0];
        liquids[1] = ingredients[1];
        liquids[2] = ingredients[2];
        liquids[3] = ingredients[3];
*/
        this.liquids = liquids;
    }


    public synchronized ArrayList<Integer> getLiquidPrices() {
        return liquidPrices;
    }

    public synchronized void setLiquidPrices(ArrayList<Integer> liquidPrices) {
        this.liquidPrices = liquidPrices;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }
    public synchronized String getStatus() {
        return status;
    }

    public synchronized int getPortNbr() {
        return portNbr;
    }

    public synchronized void setPortNbr(int portNbr) {
        this.portNbr = portNbr;
    }

    public synchronized String getIpNbr() {
        return ipNbr;
    }

    public synchronized void setIpNbr(String ipNbr) {
        this.ipNbr = ipNbr;
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public synchronized void setBalance(int balance) {
        this.balance = balance;
    }
}
