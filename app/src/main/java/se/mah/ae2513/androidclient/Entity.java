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
    private String ipNbr, username, password;
    private String[] liquids = new String[4];
    private String buttonStatus = "Not connected";
    private boolean loggedIn;

    private Entity() {
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }


    public static Entity getInstance() {
        return entity;
    }

    public synchronized String getLiquids(int i) {
        return liquids[i];
    }

    public synchronized void setLiquids(String[] ingredients) {
/*        StringTokenizer st = new StringTokenizer(ingredients,":");
        String waste = st.nextToken();
        String allIngredients = st.nextToken();
        StringTokenizer st2 = new StringTokenizer(allIngredients,",");
        liquids[0] = st2.nextToken();
        liquids[1] = st2.nextToken();
        liquids[2] = st2.nextToken();
        liquids[3] = st2.nextToken();
*/
        liquids[0] = ingredients[0];
        liquids[1] = ingredients[1];
        liquids[2] = ingredients[2];
        liquids[3] = ingredients[3];

    }
    public synchronized void setButtonStatus(String status) {
        this.buttonStatus = status;
    }
    public synchronized String getButtonStatus() {
        return buttonStatus;
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
}
