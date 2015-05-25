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
    private int balance = 0, maxVolume = 20;
//    private int nbrOfFluids = getLiquids().size();
    private int maxVolumeSingleContainer = 20;



    private Entity() {
    }


    public static Entity getInstance() {
        return entity;
    }

    public synchronized int getMaxVolumeSingleContainer() {
        return maxVolumeSingleContainer;
    }

    public synchronized void setMaxVolumeSingleContainer(int volume) {
        this.maxVolumeSingleContainer = volume;
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
