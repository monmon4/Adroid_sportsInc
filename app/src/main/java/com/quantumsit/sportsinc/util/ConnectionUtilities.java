package com.quantumsit.sportsinc.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Bassam on 2/1/2018.
 */

public class ConnectionUtilities {
    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
