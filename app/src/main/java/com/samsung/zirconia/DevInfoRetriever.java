package com.samsung.zirconia;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class DevInfoRetriever {
    private String deviceMIN;
    private String deviceOEMInfo = Build.PRODUCT;
    private String deviceSerialNumber;
    private String deviceSubscriberNumber;
    private boolean isEmulator;

    public DevInfoRetriever(Activity activity) {
        Context mAppContext = activity.getApplicationContext();
        TelephonyManager tMgr = (TelephonyManager) mAppContext.getSystemService("phone");
        String deviceIMEI = tMgr.getDeviceId();
        String flashSerialNumber = getSystemProperties(mAppContext, "ro.serialno", "Unknown");
        this.deviceSubscriberNumber = tMgr.getSubscriberId();
        this.deviceMIN = tMgr.getLine1Number();
        if (deviceIMEI != null && deviceIMEI.compareTo("000000000000000") == 0) {
            this.isEmulator = true;
        } else {
            this.isEmulator = false;
        }
        if (!this.isEmulator && tMgr.getPhoneType() == 0) {
            if (flashSerialNumber == null || flashSerialNumber.equals("Unknown")) {
                this.deviceSerialNumber = "000000000000000";
                return;
            } else {
                this.deviceSerialNumber = flashSerialNumber;
                return;
            }
        }
        this.deviceSerialNumber = deviceIMEI;
    }

    public String getIMEI() {
        return this.deviceSerialNumber == null ? "" : this.deviceSerialNumber;
    }

    public String getIMSI() {
        return this.deviceSubscriberNumber == null ? "" : this.deviceSubscriberNumber;
    }

    public String getModel() {
        return this.deviceOEMInfo == null ? "" : this.deviceOEMInfo;
    }

    public String getMIN() {
        return this.deviceMIN == null ? "" : this.deviceMIN;
    }

    public boolean isEmulator() {
        return this.isEmulator;
    }

    private static String getSystemProperties(Context context, String key, String def) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = {String.class, String.class};
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = {new String(key), new String(def)};
            String ret = (String) get.invoke(SystemProperties, params);
            return ret;
        } catch (IllegalArgumentException e) {
            return def;
        } catch (Exception e2) {
            return def;
        }
    }
}
