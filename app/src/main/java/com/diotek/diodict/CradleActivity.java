package com.diotek.diodict;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.TTSEngine;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.CursorMeanController;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.LinearFlingLayout;
import com.diotek.diodict.uitool.TabView;
import com.diotek.diodict.uitool.TouchGesture;
import com.diotek.diodict.uitool.UITools;
import com.diodict.decompiled.R;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.utils.GlobalOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class CradleActivity extends BaseActivity {
    private static final int CLOCK = 1;
    private static final int CLOCK_REFRESH_TIME = 3000;
    private static final int CRADLE_MODE = 0;
    private static final int CRADLE_PREV = 2;
    private static final int MMDDYYYY = 1;
    private static final int SEC = 1000;
    private static final int YYYYMMDD = 0;
    private PopupWindow mPreviewPopup;
    private TextView mPreviewText;
    Button mDurationBtn = null;
    Button mCradleBtn = null;
    Button mStudyBtn = null;
    Button mDictationBtn = null;
    TabView mTabView = null;
    Button mMeanTabTitleBtn = null;
    Button mMeanTabAllBtn = null;
    Button mSetDurationOkBtn = null;
    Button mSetDurationCancelBtn = null;
    CheckBox mSpeakerBtn = null;
    ImageButton mSetClockCloseBtn = null;
    CheckBox mUsBtn = null;
    CheckBox mUkBtn = null;
    TextView mMeanTitleView = null;
    TextView mMeanWordCount = null;
    TextView mClockHourText = null;
    TextView mClockMinuteText = null;
    TextView mClockAMPM = null;
    TextView mClockDay = null;
    TextView mClockYearMonthDay = null;
    ScrollView mMeanScrollView = null;
    RelativeLayout mSetDurationLayout = null;
    RelativeLayout mWrapSetDurationLayout = null;
    RelativeLayout mClockLayout = null;
    RelativeLayout mWrapSetClockLayout = null;
    RelativeLayout mSetClockLayout = null;
    CheckBox mHour24CheckBox = null;
    RadioButton mDateFormBtn1 = null;
    RadioButton mDateFormBtn2 = null;
    SeekBar mTimeDurationSeekBar = null;
    LinearFlingLayout mExplainLayout = null;
    RelativeLayout mCradleStartLayout = null;
    Button mCradleStart = null;
    CursorMeanController mMeanController = null;
    int mFolderId = -1;
    int mWordCount = 0;
    String mWordbookFolderName = "";
    int mSort = 0;
    int mSortItemChoice = 0;
    private int mDuration = 3000;
    private int mTempDuration = 0;
    private Date date = null;
    private boolean is24HType = false;
    private int DateType = 1;
    private boolean mIsSpeakOn = true;
    private boolean mIsAvailTTS = true;
    private boolean mIsTitleOnly = true;
    private int mCurrentWordPos = 0;
    private int mTabViewPos = 0;
    private boolean mIsReadyToShowNextWord = false;
    private int previewLayout = 0;
    private boolean mIsTracking = false;
    private int mTTSLang = 0;
    private boolean mIsPaused = false;
    private boolean mIsCreate = false;
    private boolean mIsPopupClose = false;
    private Toast toast = null;
    private Integer[] mDicTypes = null;
    TTSEngine.OnTTSPlayed OnTTSPlayedcallback = new TTSEngine.OnTTSPlayed() { // from class: com.diotek.diodict.CradleActivity.1
        @Override // com.diotek.diodict.engine.TTSEngine.OnTTSPlayed
        public void setNextWordAfterTTsPlayed() {
            CradleActivity.this.mIsReadyToShowNextWord = true;
            if (CradleActivity.this.mCradleHandler != null && !CradleActivity.this.mCradleHandler.hasMessages(0) && CradleActivity.this.mDuration != 0 && CradleActivity.this.mIsTitleOnly && CradleActivity.this.mWrapSetDurationLayout.getVisibility() != 0 && CradleActivity.this.mIsSpeakOn && CradleActivity.this.mWordCount > 1) {
                CradleActivity.this.cradleStart(true);
            }
        }
    };
    View.OnClickListener mDurationBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            v.setSelected(true);
            CradleActivity.this.runDurationBtn();
        }
    };
    View.OnClickListener mSetDurationOkBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.mDuration = CradleActivity.this.mTempDuration;
            DictUtils.setDurationTimeToPreference(CradleActivity.this, CradleActivity.this.mDuration);
            CradleActivity.this.setDurationText();
            CradleActivity.this.mWrapSetDurationLayout.setVisibility(View.GONE);
            CradleActivity.this.setFocasableCradleLayout(true);
            CradleActivity.this.mIsPopupClose = true;
            CradleActivity.this.cradleStart(true);
        }
    };
    View.OnClickListener mSetDurationCancelBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.mWrapSetDurationLayout.setVisibility(View.GONE);
            CradleActivity.this.setFocasableCradleLayout(true);
            CradleActivity.this.mIsPopupClose = true;
            CradleActivity.this.cradleStart(true);
        }
    };
    View.OnClickListener mStudyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.runStudyBtn();
        }
    };
    View.OnClickListener mDictationOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.DictationBtn();
        }
    };
    View.OnClickListener mMeanTabTitleBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.runMeanTabTitleBtn();
        }
    };
    View.OnClickListener mMeanTabAllBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.runMeanTabAllBtn();
        }
    };
    CompoundButton.OnCheckedChangeListener mSpeakerBtnOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.CradleActivity.9
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CradleActivity.this.runSpeakerBtn(isChecked);
        }
    };
    View.OnClickListener mClockLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            v.setSelected(true);
            CradleActivity.this.runClockBtn();
        }
    };
    CompoundButton.OnCheckedChangeListener mHour24CheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.CradleActivity.11
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                CradleActivity.this.is24HType = true;
                CradleActivity.this.mClockAMPM.setVisibility(View.INVISIBLE);
                CradleActivity.this.mCradleHandler.removeMessages(1);
                CradleActivity.this.mCradleHandler.sendEmptyMessage(1);
                return;
            }
            CradleActivity.this.is24HType = false;
            CradleActivity.this.mClockAMPM.setVisibility(View.VISIBLE);
            CradleActivity.this.mCradleHandler.removeMessages(1);
            CradleActivity.this.mCradleHandler.sendEmptyMessage(1);
        }
    };
    View.OnClickListener mHour24CheckBoxOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.12
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
        }
    };
    View.OnClickListener mDateFormBtn1OnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.13
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.DateType = 1;
            DictUtils.setCradleDateFormatToPreference(CradleActivity.this, CradleActivity.this.DateType);
            CradleActivity.this.mCradleHandler.removeMessages(1);
            CradleActivity.this.mCradleHandler.sendEmptyMessage(1);
        }
    };
    View.OnClickListener mDateFormBtn2OnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.14
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.DateType = 0;
            DictUtils.setCradleDateFormatToPreference(CradleActivity.this, CradleActivity.this.DateType);
            CradleActivity.this.mCradleHandler.removeMessages(1);
            CradleActivity.this.mCradleHandler.sendEmptyMessage(1);
        }
    };
    View.OnClickListener mCradleStartOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.15
        @Override // android.view.View.OnClickListener
        public void onClick(View arg0) {
            CradleActivity.this.mIsPaused = false;
            CradleActivity.this.setVisiableCradleStartLayout(false);
            CradleActivity.this.cradleStart(false);
        }
    };
    SeekBar.OnSeekBarChangeListener mTimeDurationSeekBarOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.diotek.diodict.CradleActivity.16
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            CradleActivity.this.mTempDuration = seekBar.getProgress() * 1000;
            CradleActivity.this.mIsTracking = false;
            CradleActivity.this.dismissTimePreview();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            CradleActivity.this.mIsTracking = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (CradleActivity.this.mPreviewText != null) {
                if (progress != 0) {
                    CradleActivity.this.mPreviewText.setTextSize(1, 26.66f);
                    CradleActivity.this.mPreviewText.setText("" + progress);
                } else {
                    String str = CradleActivity.this.getResources().getString(R.string.timeGridTextManual);
                    if (str.length() > 3) {
                        CradleActivity.this.mPreviewText.setTextSize(1, 13.0f);
                    } else {
                        CradleActivity.this.mPreviewText.setTextSize(1, 20.0f);
                    }
                    CradleActivity.this.mPreviewText.setText(str);
                }
                CradleActivity.this.mTempDuration = progress * 1000;
            }
        }
    };
    private View.OnTouchListener mTimeDurationSeekBarOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.CradleActivity.17
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View arg0, MotionEvent event) {
            if (event.getAction() == 2 && CradleActivity.this.mIsTracking) {
                CradleActivity.this.showAtLocationTimePreview(event.getX());
                return false;
            }
            return false;
        }
    };
    View.OnClickListener mSetClockCloseBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.18
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.hideWrapSetClockLayout();
        }
    };
    View.OnClickListener mUsUkBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.19
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            int newLang;
            boolean z = true;
            if (!TTSEngine.isPlayIdleState()) {
                if (v.getId() == R.id.ToggleUkBtn) {
                    CheckBox checkBox = CradleActivity.this.mUkBtn;
                    if (CradleActivity.this.mUkBtn.isChecked()) {
                        z = false;
                    }
                    checkBox.setChecked(z);
                    return;
                }
                CheckBox checkBox2 = CradleActivity.this.mUsBtn;
                if (CradleActivity.this.mUsBtn.isChecked()) {
                    z = false;
                }
                checkBox2.setChecked(z);
                return;
            }
            if (v.getId() == R.id.ToggleUkBtn) {
                newLang = 1;
            } else {
                newLang = 0;
            }
            if (CradleActivity.this.mTTSLang != newLang) {
                CradleActivity.this.mTTSLang = newLang;
                CradleActivity.this.speakTTS();
            }
            CradleActivity.this.setUsUkBtnChecked();
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.CradleActivity.20
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(CradleActivity.this);
        }
    };
    ExtendTextView.AfterSetMeanViewCallback mAfterSetMeanViewCallback = new ExtendTextView.AfterSetMeanViewCallback() { // from class: com.diotek.diodict.CradleActivity.21
        @Override // com.diotek.diodict.mean.ExtendTextView.AfterSetMeanViewCallback
        public int afterSetMean() {
            CradleActivity.this.setSpeakUsUkBtn();
            CradleActivity.this.speakTTS();
            return 0;
        }
    };
    BaseMeanController.MeanTitleTextSizeUpdateCallback mMeanTitleTextSizeUpdateCallback = new BaseMeanController.MeanTitleTextSizeUpdateCallback() { // from class: com.diotek.diodict.CradleActivity.22
        @Override // com.diotek.diodict.mean.BaseMeanController.MeanTitleTextSizeUpdateCallback
        public void setFontSize() {
            CradleActivity.this.mMeanTitleView.setTextSize(1, CradleActivity.this.mMeanController.getTitleFontSize(CradleActivity.this.mIsTitleOnly));
            CradleActivity.this.mMeanTitleView.requestLayout();
            CradleActivity.this.setSpeakUsUkBtn();
        }
    };
    View.OnClickListener mWrapSetClockLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.23
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.hideWrapSetClockLayout();
        }
    };
    View.OnClickListener mWrapSetDurationLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.24
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CradleActivity.this.mWrapSetDurationLayout.setVisibility(View.GONE);
            CradleActivity.this.dismissTimePreview();
            CradleActivity.this.mIsPopupClose = true;
            CradleActivity.this.cradleStart(true);
        }
    };
    View.OnTouchListener mMeanTitleViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.CradleActivity.25
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };
    TouchGesture flingGestureDetector = new TouchGesture();
    TouchGesture.TouchGestureOnTouchListener flingGestureListener = new TouchGesture.TouchGestureOnTouchListener() { // from class: com.diotek.diodict.CradleActivity.26
        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingClick() {
			CMN.debug("fling::callBackFlingClick");
            return false;
        }

        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingNext() {
			CMN.debug("fling::callBackFlingNext");
            CradleActivity.this.mIsReadyToShowNextWord = true;
            CradleActivity.this.mCradleHandler.sendEmptyMessage(NextMessage);
            return false;
        }

        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingPrev() {
			CMN.debug("fling::callBackFlingNext");
            CradleActivity.this.mIsReadyToShowNextWord = true;
            CradleActivity.this.mCradleHandler.sendEmptyMessage(2);
            return false;
        }
    };
    Runnable mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.CradleActivity.27
        @Override // java.lang.Runnable
        public void run() {
            if (CradleActivity.this.mTextView != null) {
                String TTSWord = CradleActivity.this.mTextView.getKeyword();
                int TTSDbtype = CradleActivity.this.mTextView.getDbtype();
                if (TTSWord != null) {
                    CommonUtils.stopTTS();
                    CommonUtils.playTTS(CradleActivity.this.mTTSLang, TTSWord, TTSDbtype, 1);
                }
            }
        }
    };
    View.OnClickListener mMeanScrollViewOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.CradleActivity.28
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
        }
    };
    View.OnTouchListener mMeanScrollViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.CradleActivity.29
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };
    TabView.TabViewOnClickListener mTabViewOnClickListener = new TabView.TabViewOnClickListener() { // from class: com.diotek.diodict.CradleActivity.30
        @Override // com.diotek.diodict.uitool.TabView.TabViewOnClickListener
        public void onClick(View v, int nPos) {
            if (CradleActivity.this.mTabViewPos != nPos) {
                CradleActivity.this.mTabViewPos = nPos;
                if (CradleActivity.this.mTabView != null) {
                    CradleActivity.this.mTabView.setBtnSelected(CradleActivity.this.mTabViewPos);
                }
                if (CradleActivity.this.mTabViewPos != 0) {
                    if (CradleActivity.this.mTabViewPos == 1) {
                        CradleActivity.this.runMeanTabAllBtn();
                        return;
                    }
                    return;
                }
                CradleActivity.this.runMeanTabTitleBtn();
            } else if (CradleActivity.this.mTabView != null) {
                CradleActivity.this.mTabView.setBtnSelected(CradleActivity.this.mTabViewPos);
            }
        }
    };
    Handler mCradleHandler = new Handler() { // from class: com.diotek.diodict.CradleActivity.31
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            String AMPM;
            String Hours;
            super.handleMessage(msg);
            switch (msg.what) {
                case NextMessage:
                    if ((CradleActivity.this.mIsReadyToShowNextWord || !CradleActivity.this.mIsSpeakOn)
							&& !CradleActivity.this.mIsPaused) {
						if(hasTextSelection()) {
							mCradleHandler.sendEmptyMessageDelayed(NextMessage, mDuration);
							return;
						}
                        CradleActivity.this.setNextWord();
                        CradleActivity.this.mCurrentWordPos = CradleActivity.this.mMeanController.getWordPos();
                        if (CradleActivity.this.mDuration != 0) {
                            CradleActivity.this.cradleStart(true);
                        }
                        if (CradleActivity.this.mIsTitleOnly) {
                            CradleActivity.this.setSpeakUsUkBtn();
                            CradleActivity.this.speakTTS();
                        }
                        if (!CradleActivity.this.mIsSpeakOn || CradleActivity.this.mIsAvailTTS) {
                            CradleActivity.this.mIsReadyToShowNextWord = false;
                            return;
                        }
                        return;
                    }
                    return;
                case 1:
                    CradleActivity.this.date = new Date(System.currentTimeMillis());
                    int nHours = CradleActivity.this.date.getHours();
                    if (nHours < 12) {
                        AMPM = CradleActivity.this.getResources().getString(R.string.AM);
                    } else {
                        AMPM = CradleActivity.this.getResources().getString(R.string.PM);
                        if (!CradleActivity.this.is24HType) {
                            nHours -= 12;
                        }
                    }
                    if (nHours < 10) {
                        if (!CradleActivity.this.is24HType && nHours == 0) {
                            Hours = String.valueOf(nHours + 12);
                        } else {
                            Hours = "0" + String.valueOf(nHours);
                        }
                    } else {
                        Hours = String.valueOf(nHours);
                    }
                    String Minutes = CradleActivity.this.date.getMinutes() < 10 ? "0" + String.valueOf(CradleActivity.this.date.getMinutes()) : String.valueOf(CradleActivity.this.date.getMinutes());
                    CradleActivity.this.mClockHourText.setText(Hours);
                    CradleActivity.this.mClockMinuteText.setText(Minutes);
                    CradleActivity.this.mClockAMPM.setText(AMPM);
                    CradleActivity.this.mClockDay.setText(CradleActivity.this.getDay(CradleActivity.this.date.getDay()));
                    CradleActivity.this.mClockYearMonthDay.setText(CradleActivity.this.getYearMonthDay());
                    CradleActivity.this.mCradleHandler.sendEmptyMessageDelayed(1, 3000L);
                    return;
                case 2:
                    if (CradleActivity.this.setPrevWord() != 3) {
                        CradleActivity.this.mMeanWordCount.setText((CradleActivity.this.mMeanController.getWordPos() + 1) + "/" + CradleActivity.this.mWordCount);
                    }
                    CradleActivity.this.mCurrentWordPos = CradleActivity.this.mMeanController.getWordPos();
                    if (CradleActivity.this.mIsTitleOnly) {
                        CradleActivity.this.speakTTS();
                        CradleActivity.this.setSpeakUsUkBtn();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
	
	@Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (super.onCreateActivity(savedInstanceState)) {
            if (Build.VERSION.SDK_INT < 11) {
                requestWindowFeature(7);
            }
            this.mCurrentMenuId = R.id.menu_flashcard;
            Intent intent = getIntent();
            this.mFolderId = intent.getExtras().getInt(DictInfo.INTENT_WORDBOOKFOLDERID);
            this.mWordCount = intent.getExtras().getInt(DictInfo.INTENT_WORDCOUNT);
            this.mWordbookFolderName = intent.getExtras().getString(DictInfo.INTENT_WORDBOOKNAME);
            this.mIsCreate = true;
            ArrayList<Integer> al = DioDictDatabase.getDictTypeListfromCursorAvailTTS(getApplicationContext(), this.mWordbookFolderName, this.mFolderId, this.mSort, true);
            if (al.size() > 0) {
                this.mDicTypes = new Integer[al.size()];
                for (int i = 0; i < al.size(); i++) {
                    this.mDicTypes[i] = al.get(i);
                }
            }
            if (Dependency.isContainTTS()) {
                setVolumeControlStream(3);
            }
            initActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onResume() {
        pressTopButton(this.mCradleBtn);
        if (this.mCradleStartLayout != null && this.mIsPaused) {
            setVisiableCradleStartLayout(true);
        }
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onPause() {
        this.mIsPaused = true;
        if (this.mCradleHandler != null && this.mCradleHandler.hasMessages(0)) {
            this.mCradleHandler.removeMessages(0);
        }
        stopTTS();
        super.onPause();
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        boolean z = true;
        super.onConfigurationChanged(newConfig);
        destroyData();
        memoryInitialize(true);
        this.mIsCreate = false;
        pressTopButton(this.mCradleBtn);
        initActivity();
        CheckBox checkBox = this.mSpeakerBtn;
        if (this.mIsSpeakOn) {
            z = false;
        }
        checkBox.setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        if (this.mCradleHandler != null) {
            if (this.mCradleHandler.hasMessages(0)) {
                this.mCradleHandler.removeMessages(0);
            }
            if (this.mCradleHandler.hasMessages(1)) {
                this.mCradleHandler.removeMessages(1);
            }
            this.mCradleHandler = null;
        }
        if (this.mPreviewPopup != null && this.mPreviewPopup.isShowing()) {
            this.mPreviewPopup.dismiss();
            this.mPreviewPopup = null;
        }
        destroyData();
        TTSEngine.setNextWordCallback(null);
        memoryInitialize(false);
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.mWrapSetClockLayout.getVisibility() == 0) {
                    hideWrapSetClockLayout();
                    return true;
                } else if (this.mWrapSetDurationLayout.getVisibility() == 0) {
                    this.mWrapSetDurationLayout.setVisibility(View.GONE);
                    setFocasableCradleLayout(true);
                    this.mIsPopupClose = true;
                    cradleStart(true);
                    return true;
                } else if (runKeyCodeBack()) {
                    return true;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void initActivity() {
        setContentView(R.layout.cradle_layout);
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        prepareContentLayout();
        prepareCallback();
        pressTopButton(this.mCradleBtn);
        restoreData();
        if (this.mIsCreate) {
            runCradleMode(true);
        } else {
            runCradleMode(false);
        }
    }

    public void prepareContentLayout() {
        prepareTabView();
        prepareContentTopLayout();
        this.mMeanTabTitleBtn = (Button) findViewById(R.id.MeanTabTitleBtn);
        this.mMeanTabAllBtn = (Button) findViewById(R.id.MeanTabAllBtn);
        if (this.mMeanTabTitleBtn != null) {
            this.mMeanTabTitleBtn.setOnClickListener(this.mMeanTabTitleBtnOnClickListener);
            this.mMeanTabTitleBtn.setSelected(true);
        }
        if (this.mMeanTabAllBtn != null) {
            this.mMeanTabAllBtn.setOnClickListener(this.mMeanTabAllBtnOnClickListener);
        }
        prepareContentMeanLayout();
        this.mMeanTitleView.setTextSize(1, this.mMeanController.getTitleFontSize(this.mIsTitleOnly));
        this.mMeanWordCount = (TextView) findViewById(R.id.CountTextView);
        this.mMeanWordCount.setText((this.mMeanController.getWordPos() + 2) + "/" + this.mWordCount);
        prepareContentTimeLayout();
        this.mCradleStartLayout = (RelativeLayout) findViewById(R.id.cradleStartLayout);
        this.mCradleStart = (Button) findViewById(R.id.cradleStart);
        this.mCradleStart.setOnClickListener(this.mCradleStartOnClickListener);
    }

    public void prepareTabView() {
        this.mTabView = (TabView) findViewById(R.id.CradleTabView);
        if (this.mTabView != null) {
            this.mTabView.getButton(0).setBackgroundResource(R.drawable.cradle_tab);
            this.mTabView.getButton(1).setBackgroundResource(R.drawable.cradle_tab);
            this.mTabView.getButton(0).setText(getResources().getString(R.string.viewWord));
            this.mTabView.getButton(1).setText(getResources().getString(R.string.viewAll));
            String locale = Locale.getDefault().getISO3Language();
            float density = GlobalOptions.density;
            if (locale.contains("eng")) {
                this.mTabView.getButton(0).setTextSize((int) (density * 11.5d));
                this.mTabView.getButton(1).setTextSize((int) (density * 11.5d));
            }
            this.mTabView.getButton(0).setFocusable(true);
            this.mTabView.getButton(1).setFocusable(true);
            this.mTabView.setBtnSelected(this.mTabViewPos);
            this.mTabView.setOnClickListener(this.mTabViewOnClickListener);
        }
    }

    public void prepareContentTopLayout() {
        this.mDurationBtn = (Button) findViewById(R.id.DurationBtn);
        this.mSetDurationOkBtn = (Button) findViewById(R.id.setDurationOkBtn);
        this.mSetDurationCancelBtn = (Button) findViewById(R.id.setDurationCancelBtn);
        this.mCradleBtn = (Button) findViewById(R.id.CradleBtn);
        this.mStudyBtn = (Button) findViewById(R.id.StudyBtn);
        this.mDictationBtn = (Button) findViewById(R.id.DictationBtn);
        this.mSetDurationLayout = (RelativeLayout) findViewById(R.id.setDurationLayout);
        this.mWrapSetDurationLayout = (RelativeLayout) findViewById(R.id.wrapSetDurarionLayout);
        this.mTimeDurationSeekBar = (SeekBar) findViewById(R.id.recogTimeSeekBar);
        setPrefDuration();
        setDurationText();
        this.mDurationBtn.setOnClickListener(this.mDurationBtnOnClickListener);
        this.mSetDurationOkBtn.setOnClickListener(this.mSetDurationOkBtnOnClickListener);
        this.mSetDurationCancelBtn.setOnClickListener(this.mSetDurationCancelBtnOnClickListener);
        this.mStudyBtn.setOnClickListener(this.mStudyBtnOnClickListener);
        this.mDictationBtn.setOnClickListener(this.mDictationOnClickListener);
        this.mWrapSetDurationLayout.setOnClickListener(this.mWrapSetDurationLayoutOnClickListener);
        this.mTimeDurationSeekBar.setOnSeekBarChangeListener(this.mTimeDurationSeekBarOnSeekBarChangeListener);
        this.mTimeDurationSeekBar.setOnTouchListener(this.mTimeDurationSeekBarOnTouchListener);
        if (Dependency.isContainHandWrightReocg() && EngineManager3rd.getSupporTTS() != null) {
            this.mDictationBtn.setVisibility(View.VISIBLE);
        } else {
            this.mDictationBtn.setVisibility(View.GONE);
        }
        prepareTimePreviewLayout();
    }

    private void prepareTimePreviewLayout() {
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        this.mPreviewPopup = new PopupWindow(getApplicationContext());
        this.previewLayout = R.layout.cradle_timepreview_popup;
        if (this.previewLayout != 0) {
            this.mPreviewText = (TextView) inflate.inflate(this.previewLayout, (ViewGroup) null);
            this.mPreviewPopup.setContentView(this.mPreviewText);
            this.mPreviewPopup.setBackgroundDrawable(null);
        }
        this.mPreviewPopup.setTouchable(false);
    }

    public void prepareContentMeanLayout() {
        View exp = findViewById(R.id.CradleContentMeanLayout);
        this.mExplainLayout = (LinearFlingLayout) exp.findViewById(R.id.card);
        this.mMeanTitleView = (TextView) mExplainLayout.findViewById(R.id.CradleMeanTitleView);
        this.mTextView = (ExtendTextView) mExplainLayout.findViewById(R.id.CradleMeanContentView);
		mTextView.setEnableTextSelect(true);
        this.mMeanScrollView = (ScrollView) mExplainLayout.findViewById(R.id.CradleMeanScrollView);
		
		View btm= exp.findViewById(R.id.CradleContentMeanBottomLayout);
        this.mSpeakerBtn = (CheckBox) btm.findViewById(R.id.SpeakerBtn);
        this.mUsBtn = (CheckBox) btm.findViewById(R.id.ToggleUsBtn);
        this.mUkBtn = (CheckBox) btm.findViewById(R.id.ToggleUkBtn);
		
        this.mSpeakerBtn.setOnCheckedChangeListener(this.mSpeakerBtnOnCheckedChangeListener);
        this.mMeanController = new CursorMeanController(this, this.mMeanTitleView, this.mTextView, null, null, this.mEngine, this.mThemeModeCallback, null, null);
        this.mMeanController.setMeanContentTextViewCallback(null, null, false, this.mAfterSetMeanViewCallback);
        this.mMeanController.setMeanView(DioDictDatabase.getTableName(this.mWordbookFolderName), this.mWordbookFolderName, this.mFolderId, this.mSort, 0, false);
        this.mMeanController.setMeanTitleTextSizeUpdateCallback(this.mMeanTitleTextSizeUpdateCallback);
        this.mUsBtn.setOnClickListener(this.mUsUkBtnOnClickListener);
        this.mUkBtn.setOnClickListener(this.mUsUkBtnOnClickListener);
//        this.mMeanTitleView.setOnTouchListener(this.mMeanTitleViewOnTouchListener);
        this.mMeanScrollView.setOnTouchListener(this.mMeanScrollViewOnTouchListener);
        this.mMeanScrollView.setOnClickListener(this.mMeanScrollViewOnClickListener);
        setUsUkBtnChecked();
		
		flingGestureDetector.setOnTouchClickListener(this.flingGestureListener);
		// mTextView.setOnTouchListener(this.flingGestureDetector);
		// mMeanTitleView.setOnTouchListener(this.flingGestureDetector);
		mExplainLayout.setOnTouchListener(this.flingGestureDetector);
    }

    public void prepareMeanController(int pos) {
        this.mMeanController.setTitleView(DioDictDatabase.getTableName(this.mWordbookFolderName), this.mFolderId, this.mSort, pos);
        this.mCurrentWordPos = pos;
    }

    public void prepareContentTimeLayout() {
        setPrefClockForm();
        this.mClockLayout = (RelativeLayout) findViewById(R.id.ClockLayout);
        this.mWrapSetClockLayout = (RelativeLayout) findViewById(R.id.wrapSetClockLayout);
        this.mSetClockLayout = (RelativeLayout) findViewById(R.id.SetClockLayout);
        this.mClockHourText = (TextView) findViewById(R.id.ClockHour);
        this.mClockMinuteText = (TextView) findViewById(R.id.ClockMinute);
        this.mClockAMPM = (TextView) findViewById(R.id.ClockAMPM);
        this.mClockDay = (TextView) findViewById(R.id.ClockDay);
        this.mClockYearMonthDay = (TextView) findViewById(R.id.ClockDateForm);
        this.mDateFormBtn1 = (RadioButton) findViewById(R.id.dateForm01);
        this.mDateFormBtn2 = (RadioButton) findViewById(R.id.dateForm02);
        this.mHour24CheckBox = (CheckBox) findViewById(R.id.hour24CheckBox);
        this.mSetClockCloseBtn = (ImageButton) findViewById(R.id.closeBtn);
        this.mWrapSetClockLayout.setOnClickListener(this.mWrapSetClockLayoutOnClickListener);
        this.mDateFormBtn1.setOnClickListener(this.mDateFormBtn1OnClickListener);
        this.mDateFormBtn2.setOnClickListener(this.mDateFormBtn2OnClickListener);
        this.mClockLayout.setOnClickListener(this.mClockLayoutOnClickListener);
        this.mHour24CheckBox.setOnClickListener(this.mHour24CheckBoxOnClickListener);
        this.mHour24CheckBox.setOnCheckedChangeListener(this.mHour24CheckBoxOnCheckedChangeListener);
        this.mSetClockCloseBtn.setOnClickListener(this.mSetClockCloseBtnOnClickListener);
        if (this.DateType == 1) {
            this.mDateFormBtn1.setChecked(true);
        } else {
            this.mDateFormBtn2.setChecked(true);
        }
    }

    public void prepareCallback() {
        TTSEngine.setNextWordCallback(this.OnTTSPlayedcallback);
    }

    public void runCradleMode(boolean isFirst) {
        setSpeakUsUkBtn();
        cradleStart(true);
        this.mCradleHandler.sendEmptyMessage(1);
        if (isFirst) {
            speakTTS();
        }
    }

    public void runDurationBtn() {
        this.mCradleHandler.removeMessages(0);
        this.mTimeDurationSeekBar.setPressed(true);
        this.mTimeDurationSeekBar.setProgress(this.mDuration / 1000);
        this.mWrapSetDurationLayout.setVisibility(View.VISIBLE);
        setFocasableCradleLayout(false);
        this.mTimeDurationSeekBar.requestFocus();
        this.mSetDurationOkBtn.setFocusable(true);
        this.mSetDurationCancelBtn.setFocusable(true);
    }

    public void runClockBtn() {
        this.mWrapSetClockLayout.setVisibility(View.VISIBLE);
        setFocasableCradleLayout(false);
        this.mHour24CheckBox.requestFocus();
    }

    public void runStudyBtn() {
        finish();
        Intent intent = new Intent();
        intent.setClass(this, StudyActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
    }

    public void DictationBtn() {
        ArrayList<Integer> al = DioDictDatabase.getDictTypeListfromCursorAvailTTS(getApplicationContext(), this.mWordbookFolderName, this.mFolderId, this.mSort, true);
        if (al.size() == 0) {
            showToastCenter(getResources().getString(R.string.can_not_use_dictation));
            return;
        }
        finish();
        Intent intent = new Intent();
        intent.setClass(this, DictationActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
    }

    public void runMeanTabTitleBtn() {
        if (this.mTabView == null) {
            this.mMeanTabTitleBtn.setSelected(true);
            this.mMeanTabAllBtn.setSelected(false);
        }
        this.mMeanScrollView.setVisibility(View.GONE);
        this.mMeanTitleView.setText("");
        this.mMeanController.setTitleViewByPos(this.mCurrentWordPos, 0);
        setSpeakUsUkBtn();
        speakTTS();
        this.mIsTitleOnly = true;
        this.mMeanTitleView.setTextSize(1, this.mMeanController.getTitleFontSize(this.mIsTitleOnly));
        cradleStart(true);
    }

    public void runMeanTabAllBtn() {
        if (this.mCradleHandler.hasMessages(0)) {
            this.mCradleHandler.removeMessages(0);
        }
        if (this.mTabView == null) {
            this.mMeanTabTitleBtn.setSelected(false);
            this.mMeanTabAllBtn.setSelected(true);
        }
        this.mMeanTitleView.setText("");
        this.mMeanController.setMeanViewByPos(this.mCurrentWordPos, 0);
        this.mMeanScrollView.setVisibility(View.VISIBLE);
        this.mIsTitleOnly = false;
        this.mMeanTitleView.setTextSize(1, this.mMeanController.getTitleFontSize(this.mIsTitleOnly));
    }

    public void runSpeakerBtn(boolean isChecked) {
        if (isChecked) {
            this.mIsSpeakOn = false;
        } else {
            this.mIsSpeakOn = true;
        }
        this.mIsReadyToShowNextWord = true;
        setSpeakUsUkBtn();
    }

    public boolean runKeyCodeBack() {
		if (clearTextViewSelection()) return true;
        finish();
        finalizeSound();
        Intent intent = new Intent();
        intent.setClass(this, FlashcardItemActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
        return true;
    }

    public String getDay(int day) {
        switch (day + 1) {
            case 1:
                return getResources().getString(R.string.sunday);
            case 2:
                return getResources().getString(R.string.monday);
            case 3:
                return getResources().getString(R.string.tuesday);
            case 4:
                return getResources().getString(R.string.wednesday);
            case 5:
                return getResources().getString(R.string.thursday);
            case 6:
                return getResources().getString(R.string.friday);
            case 7:
                return getResources().getString(R.string.saturday);
            default:
                return null;
        }
    }

    public String getYearMonthDay() {
        switch (this.DateType) {
            case 0:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                return dateFormat.format(Long.valueOf(this.date.getTime()));
            case 1:
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM.dd.yyyy");
                return dateFormat2.format(Long.valueOf(this.date.getTime()));
            default:
                return null;
        }
    }

    public void setDurationText() {
        if (this.mDuration == 0) {
            this.mDurationBtn.setText(getResources().getString(R.string.timeGridTextManual));
            return;
        }
        String timeDuration = getResources().getString(R.string.duration5sec);
        this.mDurationBtn.setText(timeDuration.replaceAll("%d", String.valueOf(this.mDuration / 1000)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissTimePreview() {
        if (this.mPreviewPopup != null && this.mPreviewPopup.isShowing()) {
            this.mPreviewPopup.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAtLocationTimePreview(float x) {
        PopupWindow previewPopup = this.mPreviewPopup;
        int[] mOffsetInWindow = null;
        if (previewPopup != null) {
            if (0 == 0) {
                mOffsetInWindow = new int[2];
                this.mSetDurationLayout.getLocationInWindow(mOffsetInWindow);
            }
            int mPopupPreviewX = (int) x;
            int mPopupPreviewY = this.mTimeDurationSeekBar.getTop() - 88;
            if (mPopupPreviewX < 0) {
                mPopupPreviewX = 0;
            }
            if (previewPopup.isShowing()) {
                previewPopup.update(mOffsetInWindow[0] + mPopupPreviewX, mOffsetInWindow[1] + mPopupPreviewY, 88, 88);
                return;
            }
            previewPopup.setWidth(88);
            previewPopup.setHeight(88);
            previewPopup.showAtLocation(this.mSetDurationLayout, 0, mOffsetInWindow[0] + mPopupPreviewX, mOffsetInWindow[1] + mPopupPreviewY);
        }
    }

    public void showToastCenter(String msg) {
        if (this.toast == null) {
            this.toast = Toast.makeText(this, msg, 0);
        } else if (this.toast != null) {
            if (this.toast.getView().isShown()) {
                this.toast.cancel();
            }
            this.toast.setText(msg);
        }
        this.toast.setGravity(17, 0, 0);
        this.toast.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void speakTTS() {
        if (this.mIsSpeakOn) {
            if (this.mIsAvailTTS) {
                new Thread(this.mRunnablePlayTTS).start();
                return;
            }
            this.mIsReadyToShowNextWord = true;
            if (this.mCradleHandler != null && this.mDuration != 0 && this.mIsTitleOnly && this.mWrapSetDurationLayout.getVisibility() != 0 && this.mWordCount > 1) {
                cradleStart(true);
            }
        }
    }

    private void stopTTS() {
        CommonUtils.stopTTS();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideWrapSetClockLayout() {
        this.mClockLayout.setSelected(false);
        this.mWrapSetClockLayout.setVisibility(View.GONE);
        setFocasableCradleLayout(true);
    }

    private void showUsUkBtn(boolean bShow) {
        int nLang = EngineInfo3rd.TTS_KOREAN;
        if (bShow && this.mIsSpeakOn) {
            if (DictDBManager.getCpENGDictionary(this.mEngine.getCurDict())) {
                this.mUsBtn.setButtonDrawable(R.drawable.toggle_us);
                this.mUkBtn.setButtonDrawable(R.drawable.toggle_uk);
                nLang = 65536;
            } else if (DictDBManager.getCpCHNDictionary(this.mEngine.getCurDict())) {
                this.mUsBtn.setButtonDrawable(R.drawable.toggle_zh_cn);
                this.mUkBtn.setButtonDrawable(R.drawable.toggle_yue_cn);
                nLang = EngineInfo3rd.TTS_CHINESE;
            }
            if (DictUtils.checkExistSecoundTTSFile(nLang)) {
                this.mUsBtn.setVisibility(View.VISIBLE);
                this.mUkBtn.setVisibility(View.VISIBLE);
                setUsUkBtnChecked();
                return;
            }
            this.mUsBtn.setVisibility(View.INVISIBLE);
            this.mUkBtn.setVisibility(View.INVISIBLE);
            return;
        }
        this.mUsBtn.setVisibility(View.INVISIBLE);
        this.mUkBtn.setVisibility(View.INVISIBLE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUsUkBtnChecked() {
        switch (this.mTTSLang) {
            case 0:
                this.mUsBtn.setChecked(true);
                this.mUkBtn.setChecked(false);
                return;
            case 1:
                this.mUsBtn.setChecked(false);
                this.mUkBtn.setChecked(true);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpeakUsUkBtn() {
        String word = this.mTextView.getKeyword();
        if (word != null) {
            int codepage = DictUtils.getCodePage(word);
            this.mIsAvailTTS = EngineInfo3rd.IsTTSAvailableCodePage(codepage);
            if (codepage != -1) {
                if (CommonUtils.isUselessTTSWord(word, this.mTextView.getDbtype())) {
                    this.mIsAvailTTS = false;
                }
                if (this.mIsAvailTTS) {
                    this.mSpeakerBtn.setEnabled(true);
                    switch (codepage) {
                        case 0:
                        case DictInfo.CP_1250 /* 1250 */:
                        case DictInfo.CP_LT1 /* 1252 */:
                        case DictInfo.CP_TUR /* 1254 */:
                        case DictInfo.CP_BAL /* 1257 */:
                        case DictInfo.CP_CRL /* 21866 */:
                            showUsUkBtn(true);
                            return;
                        case DictInfo.CP_JPN /* 932 */:
                        case DictInfo.CP_KOR /* 949 */:
                            showUsUkBtn(false);
                            return;
                        case DictInfo.CP_CHN /* 936 */:
                            showUsUkBtn(true);
                            return;
                        default:
                            showUsUkBtn(false);
                            return;
                    }
                }
                showUsUkBtn(false);
                this.mSpeakerBtn.setEnabled(false);
            }
        }
    }

	final static int NextMessage = 0;
    /* JADX INFO: Access modifiers changed from: private */
    public void cradleStart(boolean bDelay) {
        if (this.mCradleHandler != null) {
            if (this.mCradleHandler.hasMessages(NextMessage)) {
                this.mCradleHandler.removeMessages(NextMessage);
            }
            if (this.mIsTitleOnly) {
                if (this.mWordCount > 1 && this.mDuration != 0) {
                    if (bDelay) {
                        this.mCradleHandler.sendEmptyMessageDelayed(NextMessage, this.mDuration);
                    } else {
                        this.mCradleHandler.sendEmptyMessage(NextMessage);
                    }
                }
            } else if (this.mIsPopupClose) {
                this.mIsPopupClose = false;
            } else {
                runMeanTabAllBtn();
            }
        }
    }

    private void setPrefDuration() {
        this.mDuration = DictUtils.getDurationTimeFromPreference(this);
    }

    private void setPrefClockForm() {
        this.DateType = DictUtils.getCradleDateFormatFromPreference(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNextWord() {
        if (this.mIsTitleOnly) {
            if (this.mMeanController.setNextTitleView() == 3) {
                this.mMeanController.setTitleViewByPos(0, 0);
                this.mMeanWordCount.setText("1/" + this.mWordCount);
                return;
            }
            this.mMeanWordCount.setText((this.mMeanController.getWordPos() + 1) + "/" + this.mWordCount);
        } else if (this.mMeanController.setNextMeanView() == 3) {
            this.mMeanController.setMeanViewByPos(0, 0);
            this.mMeanWordCount.setText("1/" + this.mWordCount);
        } else {
            this.mMeanWordCount.setText((this.mMeanController.getWordPos() + 1) + "/" + this.mWordCount);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setPrevWord() {
        if (this.mIsTitleOnly) {
            if (this.mMeanController.setPrevTitleView() == 3) {
                this.mMeanController.setTitleViewByPos(this.mWordCount - 1, 0);
                this.mMeanWordCount.setText(this.mWordCount + "/" + this.mWordCount);
                return 3;
            }
        } else if (this.mMeanController.setPrevMeanView() == 3) {
            this.mMeanController.setMeanViewByPos(this.mWordCount - 1, 0);
            this.mMeanWordCount.setText(this.mWordCount + "/" + this.mWordCount);
            return 3;
        }
        return 0;
    }

    private void restoreData() {
        setDurationText();
        prepareMeanController(this.mCurrentWordPos);
        this.mMeanWordCount.setText((this.mCurrentWordPos + 1) + "/" + this.mWordCount);
        if (this.mIsTitleOnly) {
            if (this.mMeanTabTitleBtn != null) {
                this.mMeanTabTitleBtn.setSelected(true);
                this.mMeanTabAllBtn.setSelected(false);
            } else if (this.mTabView != null) {
                this.mTabViewPos = 0;
                this.mTabView.setBtnSelected(this.mTabViewPos);
            }
        } else if (this.mMeanTabTitleBtn != null) {
            this.mMeanTabTitleBtn.setSelected(false);
            this.mMeanTabAllBtn.setSelected(true);
        } else if (this.mTabView != null) {
            this.mTabViewPos = 1;
            this.mTabView.setBtnSelected(this.mTabViewPos);
        }
        if (this.mIsPaused && this.mCradleStartLayout != null) {
            setVisiableCradleStartLayout(true);
        }
    }

    private void memoryInitialize(boolean isconfigChange) {
        if (this.mClockLayout != null) {
            UITools.recycleDrawable(this.mClockLayout.getBackground(), false, isconfigChange);
            this.mClockLayout = null;
        }
        if (this.mSetClockCloseBtn != null) {
            UITools.recycleDrawable(this.mSetClockCloseBtn.getBackground(), false, isconfigChange);
            this.mSetClockCloseBtn = null;
        }
        if (this.mCradleStart != null) {
            UITools.recycleDrawable(this.mCradleStart.getBackground(), false, isconfigChange);
        }
        System.gc();
    }

    private void destroyData() {
        if (this.mTextView != null) {
            this.mTextView.onDestroy();
            this.mTextView = null;
        }
        if (this.mMeanController != null) {
            this.mMeanController.onDestory();
            this.mMeanController = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVisiableCradleStartLayout(boolean visiable) {
        if (this.mCradleStartLayout != null) {
            if (visiable) {
                this.mCradleStartLayout.setVisibility(View.VISIBLE);
            } else {
                this.mCradleStartLayout.setVisibility(View.GONE);
            }
            setFocusableCradleStartLayout(visiable);
        }
    }

    private void setFocusableCradleStartLayout(boolean focus) {
        setFocasableCradleLayout(!focus);
        if (focus) {
            this.mCradleStartLayout.bringToFront();
            this.mCradleStartLayout.requestFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFocasableCradleLayout(boolean focus) {
        if (isVisiableView(this.mDurationBtn)) {
            this.mDurationBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mCradleBtn)) {
            this.mCradleBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mStudyBtn)) {
            this.mStudyBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mDictationBtn)) {
            this.mDictationBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mMeanTabTitleBtn)) {
            this.mMeanTabTitleBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mMeanTabAllBtn)) {
            this.mMeanTabAllBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mSpeakerBtn)) {
            this.mSpeakerBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mUsBtn)) {
            this.mUsBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mUkBtn)) {
            this.mUkBtn.setFocusable(focus);
        }
        if (isVisiableView(this.mTabView)) {
            if (isVisiableView(this.mTabView.getButton(0))) {
                this.mTabView.getButton(0).setFocusable(focus);
            }
            if (isVisiableView(this.mTabView.getButton(1))) {
                this.mTabView.getButton(1).setFocusable(focus);
            }
        }
        if (isVisiableView(this.mClockLayout)) {
            this.mClockLayout.setFocusable(focus);
        }
    }
}
