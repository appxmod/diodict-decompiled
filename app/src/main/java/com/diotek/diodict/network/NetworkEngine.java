package com.diotek.diodict.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

/* loaded from: classes.dex */
public class NetworkEngine {
    private static Context mContext = null;
    private static Handler mHandler = new Handler();
    private static boolean DEBUG = false;
    private static Runnable m3GHandler = new Runnable() { // from class: com.diotek.diodict.network.NetworkEngine.1
        @Override // java.lang.Runnable
        public void run() {
            if (NetworkEngine.mContext != null) {
                Toast.makeText(NetworkEngine.mContext, "DEBUG : 3G Connected", 0).show();
            }
        }
    };
    private static Runnable m4GHandler = new Runnable() { // from class: com.diotek.diodict.network.NetworkEngine.2
        @Override // java.lang.Runnable
        public void run() {
            if (NetworkEngine.mContext != null) {
                Toast.makeText(NetworkEngine.mContext, "DEBUG : 4G Connected", 0).show();
            }
        }
    };

    public static int getNetworkState(Context context) {
        int stateNetwork = getConnectedNetworkState(context);
        if (stateNetwork == 1 || stateNetwork == 3) {
            return 1;
        }
        return stateNetwork;
    }

    public static int getConnectedNetworkState(Context context) {
        ConnectivityManager cMgr = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        if (cMgr == null) {
            return 0;
        }
        try {
            NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                return 0;
            }
            boolean isMobile = false;
            boolean isWifi = false;
            boolean isWiMax = false;
            NetworkInfo netInfoMobileWifi = cMgr.getNetworkInfo(6);
            if (netInfoMobileWifi != null) {
                isWiMax = netInfoMobileWifi.isConnectedOrConnecting();
            }
            NetworkInfo netInfoMobileWifi2 = cMgr.getNetworkInfo(0);
            if (netInfoMobileWifi2 != null) {
                isMobile = netInfoMobileWifi2.isConnectedOrConnecting();
            }
            NetworkInfo netInfoMobileWifi3 = cMgr.getNetworkInfo(1);
            if (netInfoMobileWifi3 != null) {
                isWifi = netInfoMobileWifi3.isConnectedOrConnecting();
            }
            if (isWifi) {
                return 2;
            }
            if (isMobile || netInfo.getState() == NetworkInfo.State.CONNECTED) {
                if (!DEBUG) {
                    return 1;
                }
                mContext = context.getApplicationContext();
                mHandler.post(m3GHandler);
                return 1;
            } else if (!isWiMax) {
                return 0;
            } else {
                if (!DEBUG) {
                    return 3;
                }
                mContext = context.getApplicationContext();
                mHandler.post(m4GHandler);
                return 3;
            }
        } catch (SecurityException e) {
            return 0;
        }
    }

    private static int getNetworkType(int networkState) {
        switch (networkState) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 6;
            default:
                return 0;
        }
    }

    public static NetworkInfo getNetworkInfo(Context context, int networkState) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo ni = cm.getNetworkInfo(getNetworkType(networkState));
        return ni;
    }
}
