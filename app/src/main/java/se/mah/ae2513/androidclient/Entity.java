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
    private String[] liquids = new String[4];
    private String serverStatus = "Not connected";

    private Entity() {
    }

    public static Entity getInstance() {
        return entity;
    }

    public synchronized String getLiquids(int i) {
        return liquids[i];
    }

    public synchronized void setLiquids(String ingredients) {
        StringTokenizer st = new StringTokenizer(ingredients,":");
        String waste = st.nextToken();
        String allIngredients = st.nextToken();
        StringTokenizer st2 = new StringTokenizer(allIngredients,",");
        liquids[0] = st2.nextToken();
        liquids[1] = st2.nextToken();
        liquids[2] = st2.nextToken();
        liquids[3] = st2.nextToken();
    }
    public synchronized void setServerStatus(String status) {
        this.serverStatus = status;
    }
    public synchronized String getServerStatus() {
        return serverStatus;
    }

    public synchronized void setLiquidSpecific(String liquid, int i) {
        liquids[i] = liquid;
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
