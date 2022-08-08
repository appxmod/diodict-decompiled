package com.diotek.diodict.dependency.vendor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;

/* loaded from: classes.dex */
public class Vendor {
    public void startInit(Context context) {
        DictDBManager.InitDBManager(context);
        DictDBManager.setUseDBbyResID(Dependency.getDevice().getSupportDBResList());
        Intent intent = ((Activity) context).getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null || bundle.getInt(DictInfo.INTENT_STARTSTATE) == DictInfo.INTENT_NEWSTART) {
            intent.putExtra(DictInfo.INTENT_STARTSTATE, DictInfo.INTENT_ALREADYSTART1ST);
            intent.setFlags(268435456);
            intent.setComponent(new ComponentName(context.getPackageName(), "com.diotek.diodict.InitActivity"));
            ((Activity) context).startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
            ((Activity) context).finish();
        }
    }

    public void setDictInfoToEngine() {
    }

    public boolean isSupportCradle() {
        return true;
    }

    public boolean isSupportTiffany() {
        return false;
    }

    public void initializeForService(Context context) {
        DictDBManager.setUseDBbyResID(Dependency.getDevice().getSupportDBResList());
    }
}
