package com.diotek.diodict;

import android.app.Activity;
import android.content.Context;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Activitymanager {
    private Context mContext;
    private static volatile Activitymanager mInstance = null;
    private static ArrayList<Activity> activityList = new ArrayList<>();

    private Activitymanager(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public static Activitymanager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (Activitymanager.class) {
                if (mInstance == null && context != null) {
                    mInstance = new Activitymanager(context);
                }
            }
        }
        return mInstance;
    }

    public void addActivity(Activity activity) {
        if (activityList != null && this.mContext != null) {
            activityList.add(activity);
        }
    }

    public void finishPrevActivity() {
        if (activityList != null && this.mContext != null && activityList.size() > 0) {
            int index = activityList.size() - 1;
            activityList.get(index).finish();
            activityList.remove(index);
        }
    }
}
