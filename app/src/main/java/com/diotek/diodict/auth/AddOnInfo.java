package com.diotek.diodict.auth;

import android.content.Context;
import com.diotek.diodict3.phone.samsung.chn.R;

/* loaded from: classes.dex */
public class AddOnInfo {
    public static final int DELAY_CONNECT_NETWORK = 7000;
    public static final int DELAY_DOWNLOAD_NETWORK = 60000;
    public static final int DELAY_UPDATE_NOTI_POPUP = 7000;
    public static final int HANDLER_MSG_CHANGETITLE = 1;
    public static final int HANDLER_MSG_COMPLETE = 6;
    public static final int HANDLER_MSG_DEVICE_PATH_ERROR = 7;
    public static final int HANDLER_MSG_DOWNLOADERRORPOPUP = 3;
    public static final int HANDLER_MSG_INCORRECT_FILE = 8;
    public static final int HANDLER_MSG_NETWORK_ERROR = 5;
    public static final int HANDLER_MSG_NOENOUGHMEMORYPOPUP = 4;
    public static final int HANDLER_MSG_NOERROR = 2;
    public static final int HANDLER_MSG_USER_CANCEL = 9;
    public static final String INTENT_DOWNLOADURI = "updateURL";
    public static final int NETWORK_STATE_CONNECTED = 2;
    public static final int NETWORK_STATE_NOTCONNETED = 0;
    public static final int NETWORK_STATE_ONLY_3G = 1;
    public static final int NETWORK_STATE_ONLY_4G = 3;
    public static final String PREF_DOWNLOAD_FILE_PATH = "download_file_path";
    public static final int PREF_SETTINGS_CHECKING_DATE_INVALID_VAL = -1;
    public static final String PREF_SETTINGS_ENABLE_UPDATE_PERIODIC = "settings_enable_update_periodic";
    public static final String PREF_SETTINGS_ENABLE_UPDATE_PERIODIC_MYSHARED = "mySharedPref_enable_update_periodic_boolean";
    public static final String PREF_SETTINGS_PERIODIC_CHECKING_DATE = "settings_periodic_checking_date";
    public static final String PREF_SETTINGS_UPDATE_PERIODIC = "settings_update_periodic";
    public static final String PREF_SETTINGS_UPDATE_VERSION = "settings_update_version";
    public static final int RETURNCODE_ERROR_EXIST_UPDATE = 1;
    public static final int RETURNCODE_ERROR_HTTP_FAIL = 65536;
    public static final int RETURNCODE_ERROR_INCORRECT_URL = -2;
    public static final int RETURNCODE_ERROR_NETWORK_STATE = 2;
    public static final int RETURNCODE_ERROR_SERVER_CONTENTS = 4;
    public static final int RETURNCODE_ERROR_SETTINGS = 3;
    public static final int RETURNCODE_ERROR_THE_LATEST = 0;
    public static final int RETURNCODE_ERROR_UNKNOWN = -1;
    public static String CHECKUPDATE_DEBUG = "Y";
    public static int LIMITEDYEAR = 2011;
    public static int LIMITEDMONTH = 4;
    public static int LIMITEDDAY = 30;
    public static boolean TRY_TO_INSTALL_APK = false;

    public static String getszDicID(Context context) {
        return context.getResources().getString(R.string.szDicID);
    }

    public static String getszDicID1(Context context) {
        return context.getResources().getString(R.string.szDicID1);
    }

    public static int getnPartNum(Context context) {
        return context.getResources().getInteger(R.integer.nPartNum);
    }

    public static int getlDicID(Context context) {
        return context.getResources().getInteger(R.integer.lDicID);
    }
}
