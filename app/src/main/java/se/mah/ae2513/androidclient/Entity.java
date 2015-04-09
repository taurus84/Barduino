package se.mah.ae2513.androidclient;

import android.view.View;

/**
 * Created by John on 15-04-08.
 */
public class Entity {

    private int portNbr;
    private String ipNbr;

    public Entity() {
        portNbr = 4444;
        ipNbr = "192.168.1.53";
    }

    public int getPortNbr() {
        return portNbr;
    }

    public void setPortNbr(int portNbr) {
        this.portNbr = portNbr;
    }

    public String getIpNbr() {
        return ipNbr;
    }

    public void setIpNbr(String ipNbr) {
        this.ipNbr = ipNbr;
    }


    public void setIpAndPort(View view) {

    }
}
