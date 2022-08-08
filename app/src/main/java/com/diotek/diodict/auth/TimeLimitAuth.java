package com.diotek.diodict.auth;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class TimeLimitAuth {
    public static final int AUTH_STATE_NORMAL = 110;
    public static final int AUTH_STATE_TEST = 111;
    public static final int AUTH_TIME_MONTH = 3;
    private static int mAuthState = 110;
    private static String mTimelimitDate;
    Context mContext;

    public TimeLimitAuth(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public static boolean checkAuthTime() {
        Date todayDate = new Date();
        Calendar buildCalendar = Calendar.getInstance();
        Date buildData = buildCalendar.getTime();
        buildData.setYear(112);
        buildData.setMonth(7);
        buildData.setDate(17);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        setTimelimitDate(dateFormat.format(Long.valueOf(buildData.getTime())));
        if (todayDate.after(buildData)) {
            return false;
        }
        setTestVersion();
        return true;
    }

    public static boolean isTestVersion() {
        return mAuthState == 111;
    }

    public static void setTestVersion() {
        mAuthState = 111;
    }

    public static String getTimelimitDate() {
        return mTimelimitDate;
    }

    public static void setTimelimitDate(String date) {
        mTimelimitDate = date;
    }
}
