package se.mah.ae2513.androidclient;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by John on 15-04-08.
 */
public class Entity {

    private int portNbr;
    private String ipNbr;
    private static Entity entity = new Entity();
    private ArrayList<String> fluidsList;
    private int nbrOfFluids;

    public int getNbrOfFluids() {
        return nbrOfFluids;
    }

    public void setNbrOfFluids(int nbrOfFluids) {
        this.nbrOfFluids = nbrOfFluids;
    }

    private Entity() {
        portNbr = 4444;
        ipNbr = "192.168.1.53";
    }

    public static Entity getInstance() {
        return entity;
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


}
