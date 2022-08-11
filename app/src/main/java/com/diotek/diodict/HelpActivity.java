package com.diotek.diodict;

import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.diotek.diodict.anim.LayoutTransition;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diodict.decompiled.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class HelpActivity extends BaseActivity {
    private final int MANUAL_TAB = 0;
    private final int NOTIFY_TAB = 1;
    private final int ABOUT_TAB = 2;
    private boolean mIsCreate = false;
    private int mCurrentTab = 0;
    private ArrayList<notifyInfo> notiInfoList = null;
    private String mCurLanguage = null;
    private RadioButton mHelpTab1 = null;
    private RadioButton mHelpTab2 = null;
    private RadioButton mHelpTab3 = null;
    private ScrollView mHelpManualLayout = null;
    private LinearLayout mHelpNotiLayout = null;
    RelativeLayout mDictationLayout = null;
    private RelativeLayout mAboutLayout = null;
    private LinearLayout mInnerAboutLayout = null;
    private WebView mNotiWebview = null;
    private GestureOverlayView mGesture = null;
    private TextView mLeftDicName = null;
    private TextView mCenterDicName = null;
    private TextView mRightDicName = null;
    int curIdx = -1;
    private boolean bAnimRight = false;
    private final int COMPANY_MASK = 3840;
    private final int ALL_MASK = 65535;
    CompoundButton.OnCheckedChangeListener mHelpTabCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HelpActivity.3
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            if (isChecked) {
                if (button == HelpActivity.this.mHelpTab1) {
                    HelpActivity.this.mCurrentTab = 0;
                    HelpActivity.this.mHelpManualLayout.setVisibility(View.VISIBLE);
                    if (Dependency.isContainHandWrightReocg()) {
                        HelpActivity.this.mDictationLayout.setVisibility(View.GONE);
                    }
                    HelpActivity.this.mHelpNotiLayout.setVisibility(View.GONE);
                    HelpActivity.this.mAboutLayout.setVisibility(View.GONE);
                    HelpActivity.this.mHelpManualLayout.scrollTo(0, 0);
                } else if (button == HelpActivity.this.mHelpTab2) {
                    HelpActivity.this.mCurrentTab = 1;
                    HelpActivity.this.mHelpManualLayout.setVisibility(View.GONE);
                    HelpActivity.this.mHelpNotiLayout.setVisibility(View.VISIBLE);
                    HelpActivity.this.mAboutLayout.setVisibility(View.GONE);
                    HelpActivity.this.mHelpNotiLayout.scrollTo(0, 0);
                } else if (button == HelpActivity.this.mHelpTab3) {
                    HelpActivity.this.mCurrentTab = 2;
                    HelpActivity.this.mHelpManualLayout.setVisibility(View.GONE);
                    HelpActivity.this.mHelpNotiLayout.setVisibility(View.GONE);
                    HelpActivity.this.mAboutLayout.setVisibility(View.VISIBLE);
                    HelpActivity.this.mAboutLayout.scrollTo(0, 0);
                }
            }
        }
    };
    View.OnFocusChangeListener mDicNameOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.HelpActivity.4
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() != HelpActivity.this.mLeftDicName.getId()) {
                if (v.getId() == HelpActivity.this.mRightDicName.getId() && hasFocus) {
                    HelpActivity.this.bAnimRight = true;
                    HelpActivity.this.onShowLeftDicnameEnd();
                    HelpActivity.this.mCenterDicName.requestFocus();
                }
            } else if (hasFocus) {
                HelpActivity.this.bAnimRight = false;
                HelpActivity.this.onShowLeftDicnameEnd();
                HelpActivity.this.mCenterDicName.requestFocus();
            }
        }
    };
    private Runnable mShowLeftNotify = new Runnable() { // from class: com.diotek.diodict.HelpActivity.5
        @Override // java.lang.Runnable
        public void run() {
            ViewGroup views = (ViewGroup) HelpActivity.this.findViewById(R.id.notifyTextViewLayout);
            if (views != null) {
                LayoutTransition.trasition(views, false, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 100, false, false);
            }
        }
    };
    private Runnable mShowRightNotify = new Runnable() { // from class: com.diotek.diodict.HelpActivity.6
        @Override // java.lang.Runnable
        public void run() {
            ViewGroup views = (ViewGroup) HelpActivity.this.findViewById(R.id.notifyTextViewLayout);
            if (views != null) {
                LayoutTransition.trasition(views, false, LayoutTransition.DIRECT_LEFT_TO_RIGHT, 100, false, false);
            }
        }
    };
    LayoutTransition.AnimationCallback mAnimationEndCallback = new LayoutTransition.AnimationCallback() { // from class: com.diotek.diodict.HelpActivity.7
        @Override // com.diotek.diodict.anim.LayoutTransition.AnimationCallback
        public void run(boolean bExtension) {
            MSG.l(1, "animationEndCALLBACK - run()");
            HelpActivity.this.onShowLeftDicnameEnd();
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        this.mCurrentMenuId = 1001;
        this.mCurLanguage = getResources().getConfiguration().locale.getLanguage();
        if (super.onCreateActivity(savedInstanceState)) {
            if (Dependency.isContainTTS()) {
                setVolumeControlStream(3);
            }
            this.mIsCreate = true;
            initActivity(false);
        }
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mIsCreate = false;
        boolean isChangedLanguage = false;
        if (this.mCurLanguage == null || !this.mCurLanguage.equalsIgnoreCase(newConfig.locale.getLanguage())) {
            isChangedLanguage = true;
            this.mCurLanguage = newConfig.locale.getLanguage();
        }
        initActivity(isChangedLanguage);
        setCurrentTab(this.mCurrentTab);
        this.mHelpTab1.requestFocus();
    }

    private void initActivity(boolean isChangedLanguage) {
        if (this.mIsCreate || isChangedLanguage) {
            setContentView(R.layout.help_layout);
            prepareTitleLayout(R.string.title_help, this.mIsCreate);
        }
        prepareContentLayout(isChangedLanguage);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mNotiWebview != null) {
            this.mNotiWebview.saveState(outState);
        }
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (this.mNotiWebview != null) {
            this.mNotiWebview.restoreState(savedInstanceState);
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 19:
                if (!this.mHelpTab2.isFocused()) {
                    return false;
                }
                this.mHelpTab1.setFocusable(true);
                this.mHelpTab3.setFocusable(true);
                return false;
            case 20:
                if (!this.mLeftDicName.isFocused() && !this.mCenterDicName.isFocused() && !this.mRightDicName.isFocused()) {
                    return false;
                }
                this.mHelpTab1.setFocusable(false);
                this.mHelpTab3.setFocusable(false);
                return false;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void prepareContentLayout(boolean isChangedLanguage) {
        if (this.mIsCreate || isChangedLanguage) {
            this.mHelpTab1 = (RadioButton) findViewById(R.id.HelpTab1);
            this.mHelpTab2 = (RadioButton) findViewById(R.id.HelpTab2);
            this.mHelpTab3 = (RadioButton) findViewById(R.id.HelpTab3);
            this.mHelpManualLayout = (ScrollView) findViewById(R.id.HelpManualScroll);
            this.mDictationLayout = (RelativeLayout) findViewById(R.id.HelpManual3ContentLayout03);
            this.mHelpNotiLayout = (LinearLayout) findViewById(R.id.HelpNotiLayout);
            this.mAboutLayout = (RelativeLayout) findViewById(R.id.HelpAboutLayout);
            this.mInnerAboutLayout = (LinearLayout) findViewById(R.id.about_dictlistlayout);
            this.mHelpTab1.setOnCheckedChangeListener(this.mHelpTabCheckedChangeListener);
            this.mHelpTab2.setOnCheckedChangeListener(this.mHelpTabCheckedChangeListener);
            this.mHelpTab3.setOnCheckedChangeListener(this.mHelpTabCheckedChangeListener);
        }
        prepareMenualLayout();
        prepareNotifyLayout(isChangedLanguage);
        prepareAboutLayout();
    }

    public void prepareMenualLayout() {
        LinearLayout mHangloLayout = (LinearLayout) findViewById(R.id.hanguloSearchContent);
        TextView mHangloTitle = (TextView) findViewById(R.id.manualPage1Content05title);
        if (mHangloTitle.getText().equals("")) {
            mHangloLayout.setVisibility(View.GONE);
        }
        if (!Dependency.isContainCradleMode()) {
            ((RelativeLayout) findViewById(R.id.HelpManual3ContentLayout01)).setVisibility(View.GONE);
        }
        if (!Dependency.isContainStudyMode()) {
            ((RelativeLayout) findViewById(R.id.HelpManual3ContentLayout02)).setVisibility(View.GONE);
        }
        if (!Dependency.isContainHandWrightReocg()) {
            ((RelativeLayout) findViewById(R.id.HelpManual3ContentLayout03)).setVisibility(View.GONE);
        }
        if (Dependency.getDevice().isLiteVersion() || !Dependency.isContainTTS()) {
            ((RelativeLayout) findViewById(R.id.HelpManual2ContentLayout04)).setVisibility(View.GONE);
        }
        if (!Dependency.isContainHandWrightReocg() || !Dependency.isContainCradleMode()) {
            ((ImageView) findViewById(R.id.HelpManual03ContentImageView1)).setVisibility(View.GONE);
            if (!Dependency.isContainStudyMode()) {
                ((TextView) findViewById(R.id.manual_page3_title)).setVisibility(View.GONE);
            }
        }
        if (!Dependency.getLocaleName().contains("Korea")) {
            ((LinearLayout) findViewById(R.id.exampleSearchContent)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.parseSearchContent)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.spellSearchContent)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.spellSearchContent_ex)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.hanguloSearchContent)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.initialSearchContent)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.initialSearchContent_ex)).setVisibility(View.GONE);
        }
    }

    public void prepareNotifyLayout(boolean isChangedLanguage) {
        this.mNotiWebview = (WebView) findViewById(R.id.notifyWebView);
        this.mGesture = (GestureOverlayView) findViewById(R.id.dicNotiGesturesOverlay);
        this.mGesture.addOnGestureListener(new GesturesProcessor());
        this.mLeftDicName = (TextView) findViewById(R.id.notifyLeftTextView);
        this.mLeftDicName.setFocusable(true);
        this.mLeftDicName.setOnFocusChangeListener(this.mDicNameOnFocusChangeListener);
        this.mCenterDicName = (TextView) findViewById(R.id.notifyCenterTextView);
        this.mCenterDicName.setFocusable(true);
        this.mCenterDicName.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.diotek.diodict.HelpActivity.1
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View v, boolean hasFocus) {
                int textcolorId = R.color.notify_center_color;
                if (hasFocus) {
                    textcolorId = R.color.textColor_focus;
                }
                ((TextView) v).setTextColor(HelpActivity.this.getResources().getColor(textcolorId));
            }
        });
        this.mRightDicName = (TextView) findViewById(R.id.notifyRightTextView);
        this.mRightDicName.setFocusable(true);
        this.mRightDicName.setOnFocusChangeListener(this.mDicNameOnFocusChangeListener);
        if (this.notiInfoList == null || isChangedLanguage) {
            int nDicType = DictUtils.getLastDictFromPreference(this);
            Integer[] dictlist = this.mEngine.getSupportDictionary();
            this.notiInfoList = new ArrayList<>();
            for (int i = 0; i < dictlist.length; i++) {
                String filename = DictDBManager.getDictNotifyFileName(dictlist[i].intValue());
                String locale = Locale.getDefault().getISO3Language();
                if (!filename.equals("notify_none")) {
                    if (filename.contains("OxfordEngChn_ec")) {
                        new notifyInfo(dictlist[i].intValue(), getResources().getString(R.string.notify_oxford_chinese_engchn_title), filename);
                    } else if (filename.contains("OxfordEngChn_ce")) {
                        new notifyInfo(dictlist[i].intValue(), getResources().getString(R.string.notify_oxford_chinese_chneng_title), filename);
                    }
                    if (filename.contains("MantouChnKor") && (locale.contains("zho") || locale.contains("chn"))) {
                        filename = filename + "_chn";
                    } else if (filename.contains("ObunshaEngJpn") && !locale.contains("jpn")) {
                        filename = filename + "_eng";
                    } else if (filename.contains("NewAceJpnKor") && locale.contains("eng")) {
                        filename = filename + "_eng";
                    } else if (filename.contains("NewAceJpnKor") && locale.contains("jpn")) {
                        filename = filename + "_jpn";
                    }
                    notifyInfo item = new notifyInfo(dictlist[i].intValue(), DictDBManager.getDictName(dictlist[i].intValue()), filename);
                    if (!this.notiInfoList.contains(item)) {
                        this.notiInfoList.add(item);
                    }
                }
                if (this.curIdx == -1 && dictlist[i].intValue() == nDicType) {
                    this.curIdx = this.notiInfoList.size() - 1;
                }
            }
            if (this.curIdx == -1) {
                this.curIdx = 0;
            }
        }
        if (this.notiInfoList.size() == 0) {
            this.mHelpTab2.setVisibility(View.GONE);
        } else {
            if ((this.curIdx < 0 || this.curIdx > this.notiInfoList.size() - 1) && this.notiInfoList.size() > 0) {
                this.curIdx = 0;
            }
            if (this.curIdx < 1) {
                this.mLeftDicName.setText("");
            } else {
                this.mLeftDicName.setText(this.notiInfoList.get(this.curIdx - 1).cDicName);
            }
            this.mCenterDicName.setText(this.notiInfoList.get(this.curIdx).cDicName);
            if (this.curIdx > this.notiInfoList.size() - 2) {
                this.mRightDicName.setText("");
            } else {
                this.mRightDicName.setText(this.notiInfoList.get(this.curIdx + 1).cDicName);
            }
            if ((this.mIsCreate || isChangedLanguage) && this.mNotiWebview != null) {
                String filePath = "file:///" + DictInfo.NOTIFYPATH + this.notiInfoList.get(this.curIdx).cNotiFilename + ".html";
                this.mNotiWebview.getSettings().setSupportZoom(false);
                this.mNotiWebview.setScrollBarStyle(33554432);
                this.mNotiWebview.getSettings().setLoadsImagesAutomatically(true);
                if (isChangedLanguage) {
                    this.mNotiWebview.setWebViewClient(new WebViewClient() { // from class: com.diotek.diodict.HelpActivity.2
                        @Override // android.webkit.WebViewClient
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            HelpActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.diotek.diodict.HelpActivity.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    HelpActivity.this.mNotiWebview.reload();
                                }
                            }, 100L);
                        }
                    });
                }
                this.mNotiWebview.setBackgroundColor(16247741);
                this.mNotiWebview.loadUrl(filePath);
                this.mNotiWebview.setNetworkAvailable(false);
            }
        }
        MSG.l(1, "inprepare curIdx : " + Integer.toString(this.curIdx));
        LayoutTransition.setCallback(null, this.mAnimationEndCallback);
    }

    public void prepareAboutLayout() {
        String copyright;
        RelativeLayout aboutInnerLayout = (RelativeLayout) findViewById(R.id.aboutInnerLayout);
        aboutInnerLayout.setVisibility(View.VISIBLE);
        TextView builTextView = (TextView) findViewById(R.id.about_buildtextview);
        initializeBuildDate(builTextView);
        Integer[] ShowDictList = this.mEngine.getSupportDictionary();
        if (ShowDictList != null) {
            ArrayList<Pair<Integer, ArrayList<String>>> nDictArray = new ArrayList<>();
            nDictArray.clear();
            for (Integer currentDict : ShowDictList) {
                if (currentDict.intValue() != 65520) {
                    int companyIndex = getContainIndex(nDictArray, currentDict, 3840);
                    if (companyIndex > -1) {
                        int allMaskIndex = getContainIndex(nDictArray, currentDict, 65535);
                        if (allMaskIndex <= -1 && (copyright = DictDBManager.getDictProductString(currentDict.intValue())) != null && (EngineInfo3rd.getIndependenceMain(currentDict.intValue()) || EngineInfo3rd.IsIndependenceSub(currentDict.intValue()))) {
                            ((ArrayList) nDictArray.get(companyIndex).second).add(copyright);
                        }
                    } else {
                        String copyright2 = DictDBManager.getDictProductString(currentDict.intValue());
                        nDictArray.add(new Pair<>(currentDict, new ArrayList()));
                        if (copyright2 != null && (EngineInfo3rd.getIndependenceMain(currentDict.intValue()) || EngineInfo3rd.IsIndependenceSub(currentDict.intValue()))) {
                            ((ArrayList) nDictArray.get(nDictArray.size() - 1).second).add(copyright2);
                        }
                    }
                }
            }
            addGapLayout(this.mInnerAboutLayout, 40);
            for (int j = 0; j < nDictArray.size(); j++) {
                Drawable companyLogo = DictDBManager.getDictCompanyLogo(((Integer) nDictArray.get(j).first).intValue());
                if (companyLogo != null) {
                    addCompanyLogo(this.mInnerAboutLayout, companyLogo);
                    addGapLayout(this.mInnerAboutLayout, 10);
                }
                ArrayList<String> copyrightList = (ArrayList) nDictArray.get(j).second;
                for (int idx = 0; idx < copyrightList.size(); idx++) {
                    addProductCopyright(this.mInnerAboutLayout, copyrightList.get(idx));
                }
                addGapLayout(this.mInnerAboutLayout, 20);
            }
            addGapLayout(this.mInnerAboutLayout, 20);
        }
    }

    private int getContainIndex(ArrayList<Pair<Integer, ArrayList<String>>> Array, Integer value, int mask) {
        if (Array == null || Array.isEmpty()) {
            return -1;
        }
        int i = 0;
        while (i < Array.size()) {
            if ((((Integer) Array.get(i).first).intValue() & mask) != (value.intValue() & mask)) {
                i++;
            } else {
                int natkorId = getResources().getInteger(R.integer.DEDT_NATKOREAN_KORTOKOR);
                int natkoroldId = getResources().getInteger(R.integer.DEDT_NATKOREAN_OLDKOR);
                if ((value.intValue() == natkorId || value.intValue() == natkoroldId) && ((Integer) Array.get(i).first).intValue() != natkorId && ((Integer) Array.get(i).first).intValue() != natkoroldId) {
                    return -1;
                }
                return i;
            }
        }
        return -1;
    }

    public void addCompanyLogo(LinearLayout innerAboutLayout, Drawable nResId) {
        ImageView aboutImageView = new ImageView(this);
        aboutImageView.setBackgroundDrawable(nResId);
        aboutImageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        aboutImageView.requestLayout();
        innerAboutLayout.addView(aboutImageView);
    }

    public void addProductCopyright(LinearLayout innerAboutLayout, String nResId) {
        TextView aboutTextView = new TextView(this);
        aboutTextView.setText(nResId);
        aboutTextView.setGravity(17);
        aboutTextView.setTextColor(-16777216);
        if (CommonUtils.isLowResolutionDevice(this)) {
            aboutTextView.setTextSize(16.0f);
        } else {
            aboutTextView.setTextSize(12.0f);
        }
        aboutTextView.setPadding(30, 0, 30, 5);
        aboutTextView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        aboutTextView.requestLayout();
        innerAboutLayout.addView(aboutTextView);
    }

    public void addGapLayout(LinearLayout innerAboutLayout, int height) {
        LinearLayout gapLayout = new LinearLayout(this);
        gapLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, height));
        gapLayout.requestLayout();
        innerAboutLayout.addView(gapLayout);
    }

    private void initializeBuildDate(TextView builTextView) {
        SimpleDateFormat dateFormat;
        Calendar buildCalendar = Calendar.getInstance();
        Date buildData = buildCalendar.getTime();
        buildData.setYear(112);
        buildData.setMonth(4);
        buildData.setDate(17);
        buildData.setHours(18);
        buildData.setMinutes(35);
        buildData.setSeconds(13);
        String locale = Locale.getDefault().getISO3Language();
        if (locale.contains("jpn")) {
            dateFormat = new SimpleDateFormat("yyyy年MM月dd日（EEE）HH:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("yyyy.MM.dd EEE HH:mm:ss");
        }
        String dateString = dateFormat.format(Long.valueOf(buildData.getTime()));
        builTextView.setText(dateString + " (Build)");
    }

    private void setCurrentTab(int nTabMode) {
        switch (nTabMode) {
            case 0:
                this.mHelpTab1.setChecked(true);
                break;
            case 1:
                this.mHelpTab2.setChecked(true);
                break;
            case 2:
                this.mHelpTab3.setChecked(true);
                break;
        }
        this.mCurrentTab = nTabMode;
    }

    private void memoryInitialize(boolean isconfigChange) {
        System.gc();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onShowLeftDicnameEnd() {
        if (this.bAnimRight) {
            this.curIdx++;
        } else {
            this.curIdx--;
        }
        if (this.curIdx > 0) {
            this.mLeftDicName.setText(this.notiInfoList.get(this.curIdx - 1).cDicName);
        } else {
            this.mLeftDicName.setText("");
            this.curIdx = 0;
        }
        if (this.curIdx < this.notiInfoList.size() - 1) {
            this.mRightDicName.setText(this.notiInfoList.get(this.curIdx + 1).cDicName);
        } else {
            this.mRightDicName.setText("");
            this.curIdx = this.notiInfoList.size() - 1;
        }
        this.mCenterDicName.setText(this.notiInfoList.get(this.curIdx).cDicName);
        MSG.l(1, "onEnd curIdx : " + Integer.toString(this.curIdx));
        String filePath = "file:///" + DictInfo.NOTIFYPATH + this.notiInfoList.get(this.curIdx).cNotiFilename + ".html";
        this.mNotiWebview.flingScroll(0, 0);
        this.mNotiWebview.getSettings().setSupportZoom(false);
        this.mNotiWebview.setScrollBarStyle(33554432);
        this.mNotiWebview.loadUrl(filePath);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class notifyInfo {
        public String cDicName;
        public int cDicType;
        public String cNotiFilename;

        public notifyInfo(int dictype, String dicname, String filename) {
            this.cDicType = dictype;
            this.cDicName = dicname;
            this.cNotiFilename = filename;
        }

        public boolean equals(Object o) {
            notifyInfo oTemp = (notifyInfo) o;
            return this.cDicType == oTemp.cDicType && this.cDicName.equals(oTemp.cDicName) && this.cNotiFilename.equals(oTemp.cNotiFilename);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GesturesProcessor implements GestureOverlayView.OnGestureListener {
        private int oldX;

        private GesturesProcessor() {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            int diff = ((int) event.getX()) - this.oldX;
            MSG.l(1, "curIdx : " + Integer.toString(HelpActivity.this.curIdx));
            MSG.l(1, "notiInfoList : " + Integer.toString(HelpActivity.this.notiInfoList.size()));
            if (diff <= 0) {
                if (HelpActivity.this.curIdx <= HelpActivity.this.notiInfoList.size() - 2) {
                    HelpActivity.this.mHandler.post(HelpActivity.this.mShowRightNotify);
                    HelpActivity.this.bAnimRight = true;
                }
            } else if (HelpActivity.this.curIdx >= 1) {
                HelpActivity.this.mHandler.post(HelpActivity.this.mShowLeftNotify);
                HelpActivity.this.bAnimRight = false;
            }
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            this.oldX = (int) event.getX();
        }
    }
}
