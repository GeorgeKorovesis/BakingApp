package com.example.korg.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by korg on 31/12/2017.
 */

class Network {

    static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        if (connMgr != null) {
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            boolean isWifiConn = false;
            if (networkInfo != null) {
                isWifiConn = networkInfo.isConnected();
            }

            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            boolean isMobileConn = false;
            if (networkInfo != null) {
                 isMobileConn = networkInfo.isConnected();
            }
            return (isWifiConn || isMobileConn);
        }
        return false;
    }
}
