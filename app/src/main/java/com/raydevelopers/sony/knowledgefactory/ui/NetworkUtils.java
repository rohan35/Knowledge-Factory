package com.raydevelopers.sony.knowledgefactory.ui;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by SONY on 08-05-2017.
 */

public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
