package com.BlizzardArmory;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The type Blizzard armory.
 */
public class BlizzardArmory extends Application {

    private static BlizzardArmory instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static BlizzardArmory getInstance() {
        return instance;
    }

    /**
     * Has network boolean.
     *
     * @return the boolean
     */
    public static boolean hasNetwork() {
        return instance.isNetworkConnected();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}