package com.diotek.diodict.uitool;

import android.annotation.SuppressLint;
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
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;

import com.diotek.diodict.Activitymanager;
import com.diotek.diodict.FlashcardActivity;
import com.diotek.diodict.HelpActivity;
import com.diotek.diodict.HistoryActivity;
import com.diotek.diodict.MultiShareActivity;
import com.diotek.diodict.Preference;
import com.diotek.diodict.SearchListActivity;
import com.diotek.diodict.SettingActivity;
import com.diotek.diodict.ViewUtils;
import com.diotek.diodict.WikipediaActivity;
import com.diotek.diodict.anim.TiffanyTransition;
import com.diotek.diodict.auth.TimeLimitAuth;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.dtestui.MeanToolbarWidgets;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.EngineNative3rd;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.FileLinkTagViewManager;
import com.diotek.diodict.mean.HyperSimpleViewModule;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.mean.SearchMeanController;
import com.diotek.diodict.utils.CMN;
import com.diodict.decompiled.R;
import com.diotek.diodict.utils.GlobalOptions;

import java.lang.ref.WeakReference;
import java.util.List;

@SuppressLint("WrongConstant")
public abstract class BaseActivity extends Activity {
    protected static int FINISH_ACTIVITY = -5;
    public static final int NOT_SUPPORT_MENU_ACTIVITY = 1001;
    public static final int REQUEST_CODE_FLASCHCARD = 1002;
    public static final int REQUEST_CODE_HISTORY = 1003;
    public static final int REQUEST_CODE_HYPERSEARCH = 1004;
    public static final int RESET_ALL_ACTIVITY = 1001;
	public List<View> wViews;
	
	protected final WeakReference<BaseActivity> thisRef = new WeakReference<>(this);
	
	public Resources mResources;
	/** the main textView */
	public ExtendTextView mTextView = null;
	public DictEditText etSearch = null;
	
	public FileLinkTagViewManager mFileLinkTagViewManager = null;
	public HyperSimpleViewModule mHyperSimpleViewModule = null;
	
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
	public Preference preference;
	public MeanToolbarWidgets toolbarWidgets;
	public SearchMeanController mSearchMeanController = null;
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
	
    public boolean onCreateActivity(Bundle savedInstanceState) {
		if (GlobalOptions.density==0) {
			DisplayMetrics dm = getResources().getDisplayMetrics();
			GlobalOptions.density = dm.density;
			GlobalOptions.ldpi = CommonUtils.isLowResolutionDevice(this);
		}
        if (this.DEBUG) {
            StrictMode.enableDefaults();
        }
        checkRunByWidget();
        return onCreateActivity(savedInstanceState, this.mIsRunbyWidget);
    }

    protected boolean onCreateActivity(Bundle savedInstanceState, boolean bInitDBManager) {
		preference = Preference.getInstance(this);
		toolbarWidgets = new MeanToolbarWidgets(this);
		mResources = getResources();
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
        onTerminateTTS();
        super.onPause();
		if (preference != null) {
			preference.check(false);
		}
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
		if (MultiShareActivity.activity!=thisRef) {
			MultiShareActivity.activity = thisRef;
		}
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
		CMN.debug("onDestroy...");
        super.onDestroy();
        closeBroadcast();
		thisRef.clear();
        if (EngineNative3rd.getLastErr() != 0) {
            System.runFinalizersOnExit(true);
            System.exit(0); // ???
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
	
	protected void runBackBtn() {
		onBackPressed();
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
		CMN.debug("onConfigurationChanged...");
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
                tv.setVisibility(View.VISIBLE);
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
	
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	protected void setTitleActionBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.app_title_layout, (ViewGroup) null);
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setCustomView(mCustomView, new ActionBar.LayoutParams(-1, -1));
            bar.setBackgroundDrawable(mResources.getDrawable(R.color.CustomWindowTitleBackground));
            // bar.setHomeButtonEnabled(false);
            int change = bar.getDisplayOptions() ^ 16;
            bar.setDisplayOptions(change, 16);
            bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				try {
					Toolbar toolbar = (Toolbar) ViewUtils.getViewItemByClass(ViewUtils.getNthParentNullable(bar.getCustomView(), 1), 0, Toolbar.class);
					// ???????????????????????????
					ViewUtils.execSimple("$.mContentInsets.setRelative[int,int](0, 0)", null, toolbar);
					ImageButton navBtn = (ImageButton) ViewUtils.getViewItemByClass(toolbar, 0, Toolbar.class, ImageButton.class);
					if (navBtn!=null) {
						ImageButton newBtn = new ImageButton(this);
						newBtn.setLayoutParams(navBtn.getLayoutParams());
						newBtn.setImageDrawable(navBtn.getDrawable());
						newBtn.setBackground(navBtn.getBackground());
						newBtn.setAlpha(0.85f);
						newBtn.setOnClickListener(v -> runBackBtn());
						ViewGroup vp = (ViewGroup) toolbar.findViewById(R.id.title_text).getParent();
						vp.setPadding((int) GlobalOptions.density, 0, 0, 0);
						ViewUtils.addViewToParent(newBtn, vp, 0);
						bar.setDisplayHomeAsUpEnabled(false);
					}
				} catch (Exception e) {
					CMN.debug(e);
				}
			}
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
	
	@Override
	public void finish() {
		try {
			throw new RuntimeException();
		} catch (Exception e) {
			CMN.debug("finish called...", e);
		}
		super.finish();
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
            this.mTTSStopPopup = CommonUtils.makeWindowWithPopupWindowTTS(this, 0, PopupContent, mResources.getDrawable(R.drawable.trans), Dependency.getDevice().checkFocusableModel());
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
	 
	protected String lastSharedText;
	
	public String getTextTarget() {
		CMN.debug("getTextTarget::", this);
		try {
			ExtendTextView tv = null;
			if (mFileLinkTagViewManager!=null && mFileLinkTagViewManager.isShowingLinkTextPopup()) {
				tv = mFileLinkTagViewManager.mTextView;
			}
			else if (mHyperSimpleViewModule!=null && mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
				tv = mHyperSimpleViewModule.mTextView;
			}
			String ret;
			if (tv!=null) {
				ret = tv.gripShowing() ? tv.getTextSelection() : tv.getKeyword();
			}
			else ret = mTextView!=null && mTextView.gripShowing()?mTextView.getTextSelection():null;
			if (!TextUtils.isEmpty(ret)) {
				lastSharedText = ret;
				return ret;
			}
			View foca = getCurrentFocus();
			if (foca instanceof TextView && ((TextView) foca).hasSelection()) {
				return ViewUtils.getTextSelection((TextView) foca);
			}
			return lastSharedText;
		} catch (Exception e) {
			CMN.debug(e);
			return "";
		}
	}
	
	public boolean hasTextSelection() {
		if (mTextView!=null && mTextView.isSelectedText()) {
			return true;
		}
		View foca = getCurrentFocus();
		if (foca instanceof TextView && ((TextView) foca).hasSelection()) {
			// CMN.Log("hasTextSel::", tv.getText().subSequence(st, ed));
			return true;
		}
		return false;
	}
	
	protected boolean fakingFocus;
	
	public final boolean clearTextViewSelection() {
		View foca = getCurrentFocus();
		if (foca instanceof TextView && ((TextView) foca).hasSelection() && !(foca instanceof ExtendTextView)) {
			//if(etSearch!=null) etSearch.setFocusable(false);
			fakingFocus = true; // annoying etSearch onFocusChanged logic!
			ViewUtils.clearTextSelection((TextView) foca);
			fakingFocus = false;
			//if(etSearch!=null) etSearch.setFocusable(true);
			return true;
		}
		return false;
	}
	
	public void setFocusableActivity(boolean b) {
	
	}
	
	public void moveTaskToFront() {
		ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		if (manager != null)
			manager.moveTaskToFront(getTaskId(), 0);
	}
}
