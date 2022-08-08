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
import com.diotek.diodict3.phone.ZirconiaCheckListener;
import com.diotek.diodict3.phone.samsung.chn.R;
import com.samsung.zirconia.Zirconia;

/* loaded from: classes.dex */
public class DioAuthActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private Zirconia mZirconia = null;
    private Context mContext = null;
    private Thread mStartInitThread = new Thread() { // from class: com.diotek.diodict3.phone.DioAuthActivity.1
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            DioAuthActivity.this.mHandler.postDelayed(DioAuthActivity.this.mStartAuthRunnable, 10L);
        }
    };
    private Runnable mStartAuthRunnable = new Runnable() { // from class: com.diotek.diodict3.phone.DioAuthActivity.2
        @Override // java.lang.Runnable
        public void run() {
            switch (KindOfMarket.mKindOfMarket) {
                case 2:
                    break;
                default:
                    return;
                case 6:
                    DioAuthActivity.this.checkZirconia();
                    break;
            }
            DioAuthActivity.this.startAuthentication();
        }
    };
    private ZirconiaCheckListener.ZirconiaCallback mZirconiaSuccessCallback = new ZirconiaCheckListener.ZirconiaCallback() { // from class: com.diotek.diodict3.phone.DioAuthActivity.3
        @Override // com.diotek.diodict3.phone.ZirconiaCheckListener.ZirconiaCallback
        public void run() {
            DioAuthActivity.this.mHandler.postDelayed(DioAuthActivity.this.mStartInitRunnable, 10L);
        }
    };
    private ZirconiaCheckListener.ZirconiaCallback mZirconiaFailedCallback = new ZirconiaCheckListener.ZirconiaCallback() { // from class: com.diotek.diodict3.phone.DioAuthActivity.4
        @Override // com.diotek.diodict3.phone.ZirconiaCheckListener.ZirconiaCallback
        public void run() {
            DioAuthActivity.this.mHandler.postDelayed(DioAuthActivity.this.mStartFinishRunnable, 50L);
        }
    };
    private Runnable mStartInitRunnable = new Runnable() { // from class: com.diotek.diodict3.phone.DioAuthActivity.5
        @Override // java.lang.Runnable
        public void run() {
            DioAuthActivity.this.startInitActivity();
        }
    };
    private Runnable mStartFinishRunnable = new Runnable() { // from class: com.diotek.diodict3.phone.DioAuthActivity.6
        @Override // java.lang.Runnable
        public void run() {
            if (11 != 62 && 11 != 61) {
                if (11 != 22) {
                    if (11 != 71) {
                        if (11 != 31) {
                            if (11 != 11) {
                                if (11 != 82) {
                                    if (11 != 21) {
                                        if (11 != 23) {
                                            if (11 != 81) {
                                                if (11 == 50) {
                                                    Toast.makeText(DioAuthActivity.this, "license error", 0).show();
                                                }
                                            } else {
                                                Toast.makeText(DioAuthActivity.this, "key error", 0).show();
                                            }
                                        } else {
                                            Toast.makeText(DioAuthActivity.this, "invalid value", 0).show();
                                        }
                                    } else {
                                        Toast.makeText(DioAuthActivity.this, "client mismatch", 0).show();
                                    }
                                } else {
                                    Toast.makeText(DioAuthActivity.this, "modified error", 0).show();
                                }
                            } else {
                                Toast.makeText(DioAuthActivity.this, "purchase error", 0).show();
                            }
                        } else {
                            Toast.makeText(DioAuthActivity.this, "cannot check", 0).show();
                        }
                    } else {
                        Toast.makeText(DioAuthActivity.this, "server mismatch", 0).show();
                    }
                } else {
                    Toast.makeText(DioAuthActivity.this, "version mismatch", 0).show();
                }
            } else {
                Toast.makeText(DioAuthActivity.this, (int) R.string.regist_connect_fail, 0).show();
            }
            DioAuthActivity.this.finish();
        }
    };
    IDioDictCheckDBCallback authInitCallback = new IDioDictCheckDBCallback.Stub() { // from class: com.diotek.diodict3.phone.DioAuthActivity.7
        @Override // com.diotek.diodict.auth.service.IDioDictCheckDBCallback
        public int afterAuthInit(int nRes) {
            if (nRes == 1) {
                Settings.setDictAuthStateToPreference(DioAuthActivity.this, true);
                DioAuthActivity.this.startInitActivity();
                return nRes;
            }
            int stateNetwork = NetworkEngine.getNetworkState(DioAuthActivity.this);
            if (stateNetwork == 0) {
                if (Settings.getDictAuthStateFromPreference(DioAuthActivity.this)) {
                    DioAuthActivity.this.startInitActivity();
                    return 1;
                }
                Settings.setDictAuthStateToPreference(DioAuthActivity.this, false);
            } else {
                Settings.setDictAuthStateToPreference(DioAuthActivity.this, false);
            }
            DioAuthActivity.this.finish();
            return nRes;
        }
    };

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
        InitDioDict();
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

    public static boolean checkAuth(Context context) {
        return true;
    }

    public void InitDioDict() {
        this.mContext = this;
        Dependency.Init(this);
        if (!Dependency.getDevice().checkDRM()) {
            Toast.makeText(this, (int) R.string.model_check, 0).show();
            finish();
        } else if (Dependency.getDevice().isSamsungAppsVersion()) {
            KindOfMarket.mKindOfMarket = 6;
            this.mStartInitThread.start();
        } else if (Dependency.getDevice().isDioStoreVersion()) {
            KindOfMarket.mKindOfMarket = 2;
            this.mStartInitThread.start();
        } else {
            startInitActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startAuthentication() {
        DioDictCheckDBModule.setCheckDBCallback(this.authInitCallback);
        if (!DioDictCheckDBModule.innerStartBuyDB(this, false)) {
            Settings.setDictAuthStateToPreference(this, false);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startInitActivity() {
        Dependency.getVendor().startInit(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkZirconia() {
        this.mZirconia = new Zirconia(this);
        ZirconiaCheckListener listener = new ZirconiaCheckListener(this.mZirconiaSuccessCallback, this.mZirconiaFailedCallback);
        this.mZirconia.setLicenseCheckListener(listener);
        this.mZirconia.checkLicense(false, false);
        int err = this.mZirconia.getError();
        if (err == 62 || err == 61) {
            Toast.makeText(this, (int) R.string.regist_connect_fail, 0).show();
        }
    }
}
