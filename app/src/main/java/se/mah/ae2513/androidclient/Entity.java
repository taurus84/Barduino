package se.mah.ae2513.androidclient;

import android.view.View;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by John on 15-04-08.
 */
public class Entity {

    private static Entity entity = new Entity();
    private int portNbr;
    private String ipNbr;
    private int nbrOfFluids;
    private String serverMessage = "";
    private String[] liquids = new String[4];

    private Entity() {
        portNbr = 4444;
        ipNbr = "192.168.1.53";
    }

    public static Entity getInstance() {
        return entity;
    }

    public synchronized void setIngredients(String ingredients) {
        StringTokenizer st = new StringTokenizer(ingredients,":");
        String waste = st.nextToken();
        String allIngredients = st.nextToken();
        StringTokenizer st2 = new StringTokenizer(allIngredients,",");
        liquids[0] = st2.nextToken();
        liquids[1] = st2.nextToken();
        liquids[2] = st2.nextToken();
        liquids[3] = st2.nextToken();
    }

    public synchronized String getIngredients(int i) {
        return liquids[i];
    }

    public synchronized String getServerMessage() {
        return serverMessage;
    }

    public synchronized void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
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
