package com.diotek.diodict;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.EngineNative3rd;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.StorageState;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class InitActivity extends BaseActivity {
    private static final int ACTIVITY_LIST = 0;
    private static final int ACTIVITY_LISTSEARCH = 1;
    private static final int MIN_STORAGE_SIZE = 1000000;
    Handler introHandler = null;
    final int Hide_Intro = 0;
    final int Show_Error = 1;
    boolean mNecessityUpdate = false;
    private int m_activitymode = 0;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(1);
        }
        if (StorageState.getInternalStorageFreeSize() < 1000000) {
            MSG.l(2, "increase extra memory size!");
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage(getString(R.string.not_enough_memory));
            d.setCancelable(true);
            d.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.InitActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    InitActivity.this.finish();
                }
            });
        }
        setContentView(R.layout.intro_layout);
        this.mCurrentMenuId = 1001;
        super.onCreate(savedInstanceState);
        this.mEngine = EngineManager3rd.getInstance(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            intent.putExtra(DictInfo.INTENT_STARTSTATE, DictInfo.INTENT_NEWSTART);
        } else {
            if (intent.getExtras().getInt(DictInfo.INTENT_STARTSTATE) == DictInfo.INTENT_ALREADYSTART1ST) {
                intent.putExtra(DictInfo.INTENT_STARTSTATE, DictInfo.INTENT_NEWSTART);
            }
            int folderId = intent.getExtras().getInt(DictInfo.INTENT_WORDBOOKFOLDERID);
            int wordCount = intent.getExtras().getInt(DictInfo.INTENT_WORDCOUNT);
            if (folderId >= 0 && wordCount > 0) {
                this.m_activitymode = 1;
            }
        }
        this.introHandler = new Handler() { // from class: com.diotek.diodict.InitActivity.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        InitActivity.this.startSelectActivity();
                        return;
                    case 1:
                        int i = msg.arg1;
                        InitActivity.this.mEngine.getClass();
                        if (i == 2) {
                            Toast.makeText(InitActivity.this, InitActivity.this.getResources().getString(R.string.no_datafile), 0).show();
                        } else {
                            Toast.makeText(InitActivity.this, "Init fail\nError Code : " + String.valueOf(msg.arg1), 0).show();
                        }
                        if (InitActivity.this.mEngine != null) {
                            InitActivity.this.mEngine.terminateEngine();
                        }
                        InitActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        new Thread(new Runnable() { // from class: com.diotek.diodict.InitActivity.3
            @Override // java.lang.Runnable
            public void run() {
                int dictype;
                int bStarted = InitActivity.this.getIntent().getExtras().getInt(DictInfo.INTENT_STARTSTATE);
                if (bStarted == DictInfo.INTENT_NEWSTART) {
                    String word = "";
                    int suid = -1;
                    if (InitActivity.this.m_activitymode == 1) {
                        Intent intent2 = InitActivity.this.getIntent();
                        Bundle extra = intent2.getExtras();
                        dictype = extra.getInt(DictInfo.INTENT_WORDINFO_DICTTYPE);
                        word = extra.getString(DictInfo.INTENT_WORDINFO_KEYWORD);
                        suid = extra.getInt(DictInfo.INTENT_WORDINFO_SUID);
                    } else {
                        dictype = DictUtils.getLastDictFromPreference(InitActivity.this);
                    }
                    int initRes = InitActivity.this.mEngine.initDicEngineWithResult(dictype, word, suid);
                    InitActivity.this.mEngine.getClass();
                    if (initRes == 0) {
                        try {
                            Thread.sleep(250L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        InitActivity.this.introHandler.sendEmptyMessage(0);
                        InitActivity.this.getIntent().putExtra(DictInfo.INTENT_STARTSTATE, DictInfo.INTENT_ALREADYSTART2ND);
                        return;
                    }
                    MSG.l(2, "fail InitDicEngine");
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.arg1 = initRes;
                    InitActivity.this.introHandler.sendMessage(msg);
                    InitActivity.this.finish();
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (EngineNative3rd.getLastErr() != 0) {
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
    }

    protected void startSelectActivity() {
        if (this.m_activitymode == 0 || this.m_activitymode == 1) {
            startMainList();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        setContentView(R.layout.intro_layout);
        super.onConfigurationChanged(newConfig);
    }

    public void startMainList() {
        if (DioDictDatabase.getWordbookFolderCount(this) == 0) {
            DioDictDatabase.addWordbookFolder(this, getString(R.string.default_wordbookname) + "1", 1);
        }
        Intent intent = getIntent();
        intent.setClass(this, SearchListActivity.class);
        intent.addFlags(343932928);
        if (this.m_activitymode == 1) {
            intent.putExtra(DictInfo.INTENT_WORDSEARCH, true);
        }
        startActivity(intent);
        finish();
    }
}
