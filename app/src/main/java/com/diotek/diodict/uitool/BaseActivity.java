package com.diotek.diodict.uitool;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.diotek.diodict.Activitymanager;
import com.diotek.diodict.FlashcardActivity;
import com.diotek.diodict.HelpActivity;
import com.diotek.diodict.HistoryActivity;
import com.diotek.diodict.SearchListActivity;
import com.diotek.diodict.SettingActivity;
import com.diotek.diodict.WikipediaActivity;
import com.diotek.diodict.anim.TiffanyTransition;
import com.diotek.diodict.auth.TimeLimitAuth;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.EngineNative3rd;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict3.phone.samsung.chn.R;

import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseActivity extends Activity {
    protected static int FINISH_ACTIVITY = -5;
    public static final int NOT_SUPPORT_MENU_ACTIVITY = 1001;
    public static final int REQUEST_CODE_FLASCHCARD = 1002;
    public static final int REQUEST_CODE_HISTORY = 1003;
    public static final int REQUEST_CODE_HYPERSEARCH = 1004;
    public static final int RESET_ALL_ACTIVITY = 1001;
	public List<View> wViews;
	
	protected TextView mSearchDBNameTextView = null;
    private View mRunnableTTSBtn = null;
    public Handler mHandler = new Handler();
    public TiffanyTransition mTfTrans = null;
    protected int mCurrentMenuId = -1;
    private boolean mIsRunbyWidget = false;
    protected EngineManager3rd mEngine = null;
    protected Activitymanager mActivityManager = null;
    private final int mb = 1048576;
    private final int supportMinHeapMemory = 48;
    protected boolean bByFlashcardItem = false;
    protected int nFlashcardFolderId = -1;
    private Toast mToast = null;
    private boolean mbConfigChange = false;
    protected boolean mUseVoiceSearch = false;
    private boolean DEBUG = false;
    DialogInterface.OnClickListener mDBCheckDialogOkBtnOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.uitool.BaseActivity.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface arg0, int arg1) {
            ActivityManager tasksManager = (ActivityManager) BaseActivity.this.getSystemService("activity");
            tasksManager.restartPackage(BaseActivity.this.getPackageName());
            DictInfo.m_DBCheckDialog = null;
        }
    };
    DialogInterface.OnCancelListener mDBCheckDialogOnCancelListener = new DialogInterface.OnCancelListener() { // from class: com.diotek.diodict.uitool.BaseActivity.2
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface arg0) {
            DictInfo.m_DBCheckDialog.dismiss();
            DictInfo.m_DBCheckDialog = null;
        }
    };
    BroadcastReceiver m_Receiver = new BroadcastReceiver() { // from class: com.diotek.diodict.uitool.BaseActivity.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.showExitDialog(context);
        }
    };
    protected View.OnClickListener mTTSOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.BaseActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            BaseActivity.this.mRunnableTTSBtn = v;
            if (!CommonUtils.isPlaying()) {
                switch (BaseActivity.this.mRunnableTTSBtn.getId()) {
                    case R.id.USRepeatBtn /* 2131099973 */:
                    case R.id.UKRepeatBtn /* 2131099975 */:
                        BaseActivity.this.showTTSStopPopup(BaseActivity.this.mRunnableTTSBtn);
                        break;
                }
            }
            new Thread(BaseActivity.this.mRunnablePlayTTS).start();
        }
    };
    Runnable mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.uitool.BaseActivity.5
        @Override // java.lang.Runnable
        public void run() {
            switch (BaseActivity.this.mRunnableTTSBtn.getId()) {
                case R.id.USOnceBtn /* 2131099972 */:
                    BaseActivity.this.runUSTTSOnceBtn();
                    return;
                case R.id.USRepeatBtn /* 2131099973 */:
                    BaseActivity.this.runUSTTSRepeatBtn();
                    return;
                case R.id.UKOnceBtn /* 2131099974 */:
                    BaseActivity.this.runUKTTSOnceBtn();
                    return;
                case R.id.UKRepeatBtn /* 2131099975 */:
                    BaseActivity.this.runUKTTSRepeatBtn();
                    return;
                default:
                    return;
            }
        }
    };
    private PopupWindow mTTSStopPopup = null;
    private View.OnKeyListener mContentOnKeyListener = new View.OnKeyListener() { // from class: com.diotek.diodict.uitool.BaseActivity.6
        @Override // android.view.View.OnKeyListener
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getAction() == 1 && BaseActivity.this.isTTSRepeat()) {
                BaseActivity.this.onTerminateTTS();
                return true;
            } else if ((keyCode == 24 || keyCode == 25) && event.getAction() == 0) {
                BaseActivity.this.setVolume(keyCode);
                return true;
            } else {
                return false;
            }
        }
    };
    protected PopupWindow.OnDismissListener mTTSRepeatStopOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.uitool.BaseActivity.7
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            CommonUtils.stopTTS();
            BaseActivity.this.mTTSStopPopup = null;
        }
    };
    protected View.OnClickListener mTTSRepeatStopOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.BaseActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            BaseActivity.this.onTerminateTTS();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean onCreateActivity(Bundle savedInstanceState) {
        if (this.DEBUG) {
            StrictMode.enableDefaults();
        }
        checkRunByWidget();
        return onCreateActivity(savedInstanceState, this.mIsRunbyWidget);
    }

    protected boolean onCreateActivity(Bundle savedInstanceState, boolean bInitDBManager) {
        if (this.DEBUG) {
            StrictMode.enableDefaults();
        }
        super.onCreate(savedInstanceState);
        this.mEngine = EngineManager3rd.getInstance(getApplicationContext());
        this.mActivityManager = Activitymanager.getInstance(getApplicationContext());
        prepareBroadcast();
        MSG.l(2, "call LibIsValidDBHandle");
        if (!this.mEngine.getIsValidDBHandle() || this.mEngine.getSupportDictionary() == null) {
            MSG.l(2, "Invalid DB Handle");
            if (bInitDBManager) {
                MSG.l(2, "call initDBManager");
                if (!this.mEngine.initDBManager() || !this.mEngine.initDicEngine(DictUtils.getLastDictFromPreference(this), "", -1)) {
                    Toast.makeText(this, (int) R.string.no_external_storage_card, 0).show();
                    finish();
                    return false;
                }
            } else {
                setResult(FINISH_ACTIVITY);
                finish();
                return false;
            }
        }
        prepareTiffany();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        overridePendingTransition(0, 0);
        onTerminateTTS();
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        overridePendingTransition(0, 0);
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        closeBroadcast();
        if (EngineNative3rd.getLastErr() != 0) {
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mCurrentMenuId == 1001) {
            return false;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        if (this.mCurrentMenuId != -1) {
            menu.removeItem(this.mCurrentMenuId);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (this.mbConfigChange) {
            this.mbConfigChange = false;
            if (menu != null) {
                menu.clear();
                onCreateOptionsMenu(menu);
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        finalizeSound();
        switch (item.getItemId()) {
            case R.id.menu_search /* 2131100206 */:
                runSearchBtn(true);
                return true;
            case R.id.menu_flashcard /* 2131100207 */:
                runFlashCardBtn(true);
                return true;
            case R.id.menu_history /* 2131100208 */:
                runHistoryBtn(true);
                return true;
            case R.id.menu_setting /* 2131100209 */:
                runSettingBtn(false);
                return true;
            case R.id.menu_help /* 2131100210 */:
                runHelpBtn(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1001) {
            setResult(1001);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        onTerminateTTS();
        this.mbConfigChange = true;
        super.onConfigurationChanged(newConfig);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getPlayTTSWord(boolean bKeyword) {
        return "";
    }

    protected int getPlayTTSDbType() {
        return -1;
    }

    public void prepareTitleLayout(int stringId, boolean isCreate) {
        if (isCreate) {
            if (Build.VERSION.SDK_INT < 11) {
                setCustomTitle();
            } else {
                setTitleActionBar();
            }
            if (TimeLimitAuth.isTestVersion()) {
                FontFitTextView tv = (FontFitTextView) findViewById(R.id.authTestText);
                String str = tv.getText().toString() + TimeLimitAuth.getTimelimitDate();
                tv.setText(str);
                tv.setVisibility(0);
            }
            this.mSearchDBNameTextView = (TextView) findViewById(R.id.title_text);
        }
        if (this.mSearchDBNameTextView != null) {
            this.mSearchDBNameTextView.setText(stringId);
        }
    }

    protected void setCustomTitle() {
        getWindow().setFeatureInt(7, R.layout.app_title_layout);
    }

    protected void setTitleActionBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.app_title_layout, (ViewGroup) null);
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setCustomView(mCustomView, new ActionBar.LayoutParams(-1, -1));
            bar.setBackgroundDrawable(getResources().getDrawable(R.color.CustomWindowTitleBackground));
            bar.setHomeButtonEnabled(false);
            int change = bar.getDisplayOptions() ^ 16;
            bar.setDisplayOptions(change, 16);
        }
    }

    public void prepareBroadcast() {
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentfilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentfilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentfilter.addAction("android.intent.action.MEDIA_EJECT");
        intentfilter.addDataScheme("file");
        if (this.m_Receiver != null) {
            registerReceiver(this.m_Receiver, intentfilter);
        }
    }

    private void prepareTiffany() {
        long maxMemory = Runtime.getRuntime().maxMemory() / 1048576;
        if (maxMemory >= 48 && Dependency.getVendor().isSupportTiffany()) {
            this.mTfTrans = new TiffanyTransition(this);
        }
    }

    public void runSearchBtn(boolean bFinish) {
        if (bFinish) {
            setResult(1001);
            finish();
        }
        Intent intent = new Intent();
        intent.setClass(this, SearchListActivity.class);
        intent.setFlags(603979776);
        startActivity(intent);
    }

    public void runFlashCardBtn(boolean bFinish) {
        if (bFinish) {
            setResult(1001);
            finish();
        }
        Intent intent = new Intent();
        if (this.bByFlashcardItem) {
            intent.putExtra(DictInfo.INTENT_FLASHCARD_FOLER_ID, this.nFlashcardFolderId);
        }
        intent.setClass(this, FlashcardActivity.class);
        startActivityForResult(intent, 1002);
    }

    public void runHistoryBtn(boolean bFinish) {
        if (bFinish) {
            setResult(1001);
            finish();
        }
        Intent intent = new Intent();
        intent.setClass(this, HistoryActivity.class);
        startActivityForResult(intent, 1003);
    }

    public void runSettingBtn(boolean bFinish) {
        if (bFinish) {
            finish();
        }
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        startActivityForResult(intent, 1001);
    }

    public void runHelpBtn(boolean bFinish) {
        if (bFinish) {
            finish();
        }
        Intent intent = new Intent();
        intent.setClass(this, HelpActivity.class);
        startActivityForResult(intent, 1001);
    }

    public void runWikipediaBtn(boolean bFinish) {
        if (bFinish) {
            finish();
        }
        Intent intent = new Intent();
        intent.setClass(this, WikipediaActivity.class);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runUSTTSOnceBtn() {
        CommonUtils.playTTS(0, getPlayTTSWord(true), getPlayTTSDbType(), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runUKTTSOnceBtn() {
        CommonUtils.playTTS(1, getPlayTTSWord(true), getPlayTTSDbType(), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runUSTTSRepeatBtn() {
        CommonUtils.playTTS(0, getPlayTTSWord(true), getPlayTTSDbType(), -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runUKTTSRepeatBtn() {
        CommonUtils.playTTS(1, getPlayTTSWord(true), getPlayTTSDbType(), -1);
    }

    public void closeBroadcast() {
        int apiLevel = Integer.parseInt(Build.VERSION.SDK);
        if (this.m_Receiver != null) {
            if (apiLevel >= 7) {
                try {
                    unregisterReceiver(this.m_Receiver);
                } catch (IllegalArgumentException e) {
                    this.m_Receiver = null;
                }
            } else if (this.m_Receiver != null) {
                unregisterReceiver(this.m_Receiver);
            }
        }
    }

    public void showExitDialog(Context context) {
        if (DictInfo.m_DBCheckDialog == null) {
            DictInfo.m_DBCheckDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.Warning)).setMessage(getString(R.string.no_external_storage_card)).setPositiveButton(getString(R.string.ok), this.mDBCheckDialogOkBtnOnClickListener).setOnCancelListener(this.mDBCheckDialogOnCancelListener).create();
            DictInfo.m_DBCheckDialog.show();
            return;
        }
        ((Activity) context).finish();
    }

    private void checkRunByWidget() {
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            this.mIsRunbyWidget = extra.getBoolean(DictInfo.INTENT_RUN_BY_WIDGET);
        }
    }

    protected void showTTSStopPopup(View parents) {
        int[] windowLocation = new int[2];
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        ImageView PopupContent = (ImageView) inflate.inflate(R.layout.tts_repeat_stop_popup, (ViewGroup) null);
        parents.getLocationInWindow(windowLocation);
        PopupContent.measure(0, 0);
        int popupWidth = PopupContent.getMeasuredWidth();
        int popupHeight = PopupContent.getMeasuredHeight();
        if (this.mTTSStopPopup == null) {
            this.mTTSStopPopup = CommonUtils.makeWindowWithPopupWindowTTS(this, 0, PopupContent, getResources().getDrawable(R.drawable.trans), Dependency.getDevice().checkFocusableModel());
            ((ImageView) PopupContent.findViewById(R.id.tts_repeat_stop)).setOnClickListener(this.mTTSRepeatStopOnClickListener);
            this.mTTSStopPopup.setOnDismissListener(this.mTTSRepeatStopOnDismissListener);
            PopupContent.setOnKeyListener(this.mContentOnKeyListener);
        }
        if (this.mTTSStopPopup != null) {
            if (this.mTTSStopPopup.isShowing()) {
                this.mTTSStopPopup.update(windowLocation[0] + 0, windowLocation[1] + 0, popupWidth, popupHeight);
            } else {
                this.mTTSStopPopup.setWidth(popupWidth);
                this.mTTSStopPopup.setHeight(popupHeight);
                this.mTTSStopPopup.showAtLocation(parents, 0, windowLocation[0] + 0, windowLocation[1] + 0);
            }
            PopupContent.requestFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isTTSRepeat() {
        return this.mTTSStopPopup != null && this.mTTSStopPopup.isShowing();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dismissTTSRepeat() {
        if (this.mTTSStopPopup != null && this.mTTSStopPopup.isShowing()) {
            this.mTTSStopPopup.dismiss();
            this.mTTSStopPopup = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVolume(int keyCode) {
        AudioManager am = (AudioManager) getSystemService("audio");
        if (am != null) {
            am.adjustSuggestedStreamVolume(keyCode == 24 ? 1 : -1, 3, 17);
        }
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1 && isTTSRepeat()) {
            onTerminateTTS();
        }
        return super.onTouchEvent(event);
    }

    protected void onTerminateTTS() {
        CommonUtils.stopTTS();
        if (this.mTTSStopPopup != null) {
            this.mTTSStopPopup.dismiss();
            this.mTTSStopPopup = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finalizeSound() {
        if (DictUtils.mAudioManager != null && DictUtils.mSoundPool != null && DictUtils.mSoundPoolMap != null) {
            DictUtils.mSoundPool.release();
            DictUtils.mSoundPoolMap.clear();
            DictUtils.mSoundPool = null;
            DictUtils.mSoundPoolMap = null;
            DictUtils.mAudioManager = null;
            System.gc();
        }
    }

    public void showToast(String msg) {
        if (this.mToast == null) {
            this.mToast = Toast.makeText(this, msg, 0);
        } else {
            if (this.mToast.getView().isShown()) {
                this.mToast.cancel();
            }
            this.mToast.setText(msg);
        }
        this.mToast.show();
    }

    protected void printMemoryLog() {
        MSG.l(2, "runtime FreeMemory  : " + (Runtime.getRuntime().freeMemory() / 1048576) + " mb");
        MSG.l(2, "runtime MaxMamory   : " + (Runtime.getRuntime().maxMemory() / 1048576) + " mb");
        MSG.l(2, "runtime totalMemory : " + (Runtime.getRuntime().totalMemory() / 1048576) + " mb");
        MSG.l(2, "Debug NativeHeapFreeMemory    : " + (Debug.getNativeHeapFreeSize() / 1024) + " bytes");
        MSG.l(2, "Debug NativeHeapAllocatedSize : " + (Debug.getNativeHeapAllocatedSize() / 1048576) + " mb");
        MSG.l(2, "Debug NativeHeapSize          : " + (Debug.getNativeHeapSize() / 1048576) + " mb");
    }

    public boolean pressTopButton(Button btn) {
        if (isVisiableView(btn)) {
            btn.setSelected(true);
            btn.setPressed(true);
            btn.requestFocus();
            return true;
        }
        return false;
    }

    public boolean isVisiableView(View view) {
        return view != null && view.getVisibility() == 0;
    }
}
