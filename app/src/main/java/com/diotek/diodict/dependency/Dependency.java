package com.diotek.diodict.dependency;

import android.content.Context;
import com.diotek.diodict.dependency.device.Device;
import com.diotek.diodict.dependency.device.DeviceInfo;
import com.diotek.diodict.dependency.locale.Locale;
import com.diotek.diodict.dependency.locale.LocaleInfo;
import com.diotek.diodict.dependency.vendor.Vendor;
import com.diotek.diodict.dependency.vendor.VendorInfo;
import com.diotek.diodict.engine.DictInfo;

/* loaded from: classes.dex */
public class Dependency {
    private static String DeviceName = "";
    private static String LocaleName = "";
    private static String VendorName = "";
    private static Context mContext = null;
    public static boolean CHECK_UPDATE = false;
    private static String DependencyPackage = "com.diotek.diodict3.dependency.";

    public static void Init(Context context) {
        mContext = context;
		if (true) {
			DeviceName = "SamsungChn";
			LocaleName = "China";
			VendorName = "Samsung";
		} else if (context.getPackageName().equals("com.diotek.diodict3.phone.samsung.korea")) {
            DeviceName = "SamsungKorea";
            LocaleName = "Korean";
            VendorName = "Samsung";
        } else if (context.getPackageName().equals("com.diotek.diodict3.phone.samsung.chn.lite")) {
            DeviceName = "SamsungChnLite";
            LocaleName = "China";
            VendorName = "Samsung";
        } else if (context.getPackageName().equals("com.diotek.diodict3.phone.samsung.jpn")) {
            DeviceName = "SamsungJpn";
            LocaleName = "Japan";
            VendorName = "Samsung";
        } else {
            DeviceName = "Developement";
            LocaleName = "China";
            VendorName = "Samsung";
        }
        DeviceInfo.initDevice();
        getDevice();
        VendorInfo.initVendor();
        getVendor();
    }

    public static boolean isInit() {
        return !DeviceName.equalsIgnoreCase("");
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getDeviceName() {
        return !DependencyPackage.equals("") ? DependencyPackage + DeviceName : Device.class.getPackage().getName() + "." + DeviceName;
    }

    public static String getLocaleName() {
        return !DependencyPackage.equals("") ? DependencyPackage + LocaleName : Locale.class.getPackage().getName() + "." + LocaleName;
    }

    public static String getVendorName() {
        return !DependencyPackage.equals("") ? DependencyPackage + VendorName : Vendor.class.getPackage().getName() + "." + VendorName;
    }

    public static Device getDevice() {
        return DeviceInfo.return_Device();
    }

    public static Locale getLocale() {
        return LocaleInfo.return_Locale();
    }

    public static Vendor getVendor() {
        return VendorInfo.return_Vendor();
    }

    public static boolean isGoAbroad() {
        return LocaleName != "Korean";
    }

    public static boolean isBtoB() {
        return VendorName != "BtoC";
    }

    public static boolean isContainJapankeyboard() {
        return LocaleName == "Korean";
    }

    public static boolean isChina() {
        return LocaleName.equals("China");
    }

    public static boolean isContainHandWrightReocg() {
        return getDevice().isContainHandWrightReocg() && !getDevice().isLiteVersion();
    }

    public static boolean isContainCradleMode() {
        return isContainTTS() && getVendor().isSupportCradle() && !getDevice().isLiteVersion();
    }

    public static boolean isContainStudyMode() {
        return !getDevice().isLiteVersion() && !getDevice().isLiteVersion();
    }

    public static boolean isContainDictationMode() {
        return isContainTTS() && isContainHandWrightReocg() && !getDevice().isLiteVersion();
    }

    public static boolean isContainTTS() {
        return getDevice().isContainTTS();
    }

    public static void setContainTTS(boolean bUseTTS) {
        getDevice().setContainTTS(bUseTTS);
    }

    public static String getDBpath() {
        return getDevice().getDBpath();
    }

    public static String getDBpathName() {
        return !isBtoB() ? DictInfo.DIODICT_EXTERNAL_APPPATH : getDevice().getDBfolderName();
    }

    public static boolean isPreload() {
        return getDevice().isPreload();
    }

    public static boolean isContainTiffanyEffect() {
        return VendorName != "Samsung";
    }

    public static boolean isJapan() {
        return LocaleName.equals("Japan");
    }
}
