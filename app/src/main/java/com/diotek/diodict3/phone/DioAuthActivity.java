package com.diotek.diodict3.phone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.diotek.diodict.KindOfMarket;
import com.diotek.diodict.auth.DioDictCheckDBModule;
import com.diotek.diodict.auth.Settings;
import com.diotek.diodict.auth.service.IDioDictCheckDBCallback;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.network.NetworkEngine;
import com.diotek.diodict.uitool.BaseActivity;
import com.diodict.decompiled.R;
import com.samsung.zirconia.Zirconia;

/* loaded from: classes.dex */
public class DioAuthActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private Context mContext = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(1);
        }
        prepareBroadcast();
        Intent intent = getIntent().getExtras() != null ? getIntent() : new Intent();
        Bundle bundle = intent.getExtras();
        if (bundle == null || bundle.getInt(DictInfo.INTENT_STARTSTATE) != DictInfo.INTENT_NEWSTART) {
            intent.putExtra(DictInfo.INTENT_STARTSTATE, DictInfo.INTENT_NEWSTART);
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        return DioDictCheckDBModule.onCreateDialog(this, id);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
		this.mContext = this;
		Dependency.Init(this);
		startInitActivity();
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        setContentView(R.layout.intro_layout);
        super.onConfigurationChanged(newConfig);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onDestroy() {
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startInitActivity() {
        Dependency.getVendor().startInit(this.mContext);
    }
}
