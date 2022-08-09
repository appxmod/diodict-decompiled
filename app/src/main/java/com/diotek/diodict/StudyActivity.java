package com.diotek.diodict;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.diotek.diodict.uitool.UITools;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class StudyActivity extends BaseActivity {
    static final int RETRY_CORRECTWORD = 0;
    static final int RETRY_WRONGWORD = 1;
    private Vibrator mVibrator;
    ImageButton mEffectBtn = null;
    Button mCradleBtn = null;
    Button mStudyBtn = null;
    Button mDictationBtn = null;
    Button mNextBtn = null;
    ImageView mEmark = null;
    TextView mStudyInfoTextView = null;
    CheckBox mSpeakerOnOffBtn = null;
    CheckBox mShowMeanBtn = null;
    ImageButton mDeleteBtn = null;
    ImageButton mCorrectBtn = null;
    ImageButton mWrongBtn = null;
    CheckBox mUsBtn = null;
    CheckBox mUkBtn = null;
    TextView mMeanTitleView = null;
    ExtendTextView mMeanContentView = null;
    TextView mMeanInfoTextView = null;
    ScrollView mMeanScrollView = null;
    ImageView mCorrectImageView = null;
    ImageView mWrongImageView = null;
    ImageView mCorrectDioboy = null;
    ImageView mWrongDioboy = null;
    RelativeLayout mQuestionLayout = null;
    RelativeLayout mAnswerLayout = null;
    TextView mTotalCountTextView = null;
    TextView mCorrectCountTextView = null;
    TextView mWrongCountTextView = null;
    TextView mMeanWordCount = null;
    ImageView mResultStateImageView = null;
    Button mChangeFlashcardBtn = null;
    Button mRetryBtn = null;
    TextView mRestudyCorrectWordButton = null;
    TextView mRestudyWrongWordButton = null;
    CursorMeanController mMeanController = null;
    int mFolderId = -1;
    int mWordCount = 0;
    String mWordbookFolderName = "";
    int mSort = 0;
    int mSortItemChoice = 0;
    private boolean mIsSpeakOn = true;
    private boolean mIsAvailTTS = true;
    private boolean mIsShowMean = false;
    private boolean mIsTitleOnly = true;
    private Toast toast = null;
    private int mEffectMode = 0;
    int mCorrectAnswerCount = 0;
    int mWrongAnswerCount = 0;
    private int mCurrentWordPos = 0;
    private final int STUDY_NORMAL_MODE = 0;
    private final int STUDY_RETRY_WRONGWORD_MODE = 1;
    private final int STUDY_RETRY_CORRECTWORD_MODE = 2;
    private final int STUDY_STATE_STUDYING = 0;
    private final int STUDY_STATE_RESULT = 1;
    private int mStateStudy = 0;
    private List<Integer> mStudyList = new ArrayList();
    private List<Integer> mStudyWrongPosList = new ArrayList();
    private List<Integer> mStudyCorrectPosList = new ArrayList();
    private boolean mIsReStudy = false;
    private int mIsStudyMode = 0;
    private int mStudyTotalWordCount = 0;
    private final int NEXT_STEP_TITLE = 1;
    private final int NEXT_STEP_MEAN = 2;
    private int mNextStep = 1;
    private final int EXCELLENT_DICTATION_THRESHOLD = 90;
    private final int GOOD_DICTATION_THRESHOLD = 60;
    private int mTTSLang = 0;
    private boolean mIsCreate = true;
    private Integer[] mDicTypes = null;
    Handler mStudyHandler = new Handler() { // from class: com.diotek.diodict.StudyActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    StudyActivity.this.setResultLayout(true);
                    return;
                case 3:
                default:
                    return;
                case 4:
                    StudyActivity.this.mNextBtn.setVisibility(View.INVISIBLE);
                    StudyActivity.this.showInfoText(0);
                    if (StudyActivity.this.mCurrentWordPos == StudyActivity.this.mStudyTotalWordCount) {
                        StudyActivity.this.mStudyHandler.sendEmptyMessage(2);
                        StudyActivity.this.hideQuestionContentImageView();
                        StudyActivity.this.hideMeanView();
                        return;
                    }
                    StudyActivity.this.mMeanTitleView.setText("");
                    StudyActivity.this.hideQuestionContentImageView();
                    StudyActivity.this.hideMeanView();
                    StudyActivity.this.mMeanController.setNextTitleView();
                    StudyActivity.this.mMeanWordCount.setText((StudyActivity.this.mCurrentWordPos + 1) + "/" + StudyActivity.this.mStudyTotalWordCount);
                    StudyActivity.this.setAnswerBtnClick(true);
                    StudyActivity.this.mMeanWordCount.setVisibility(View.VISIBLE);
                    StudyActivity.this.speakTTS();
                    return;
                case 5:
                    StudyActivity.this.hideQuestionContentImageView();
                    StudyActivity.this.showMeanView();
                    return;
                case 6:
                    StudyActivity.this.mNextBtn.setVisibility(View.INVISIBLE);
                    if (StudyActivity.this.mCurrentWordPos >= StudyActivity.this.mStudyTotalWordCount) {
                        if (StudyActivity.this.mCurrentWordPos == StudyActivity.this.mStudyTotalWordCount) {
                            StudyActivity.this.hideQuestionContentImageView();
                            StudyActivity.this.hideMeanView();
                            StudyActivity.this.mStudyHandler.sendEmptyMessage(2);
                            return;
                        }
                        return;
                    }
                    StudyActivity.this.mMeanTitleView.setText("");
                    StudyActivity.this.hideQuestionContentImageView();
                    StudyActivity.this.hideMeanView();
                    StudyActivity.this.mMeanController.setTitleViewByPos(((Integer) StudyActivity.this.mStudyList.get(StudyActivity.this.mCurrentWordPos)).intValue(), 0);
                    StudyActivity.this.mMeanWordCount.setText((StudyActivity.this.mCurrentWordPos + 1) + "/" + StudyActivity.this.mStudyTotalWordCount);
                    StudyActivity.this.setAnswerBtnClick(true);
                    StudyActivity.this.mMeanWordCount.setVisibility(View.VISIBLE);
                    StudyActivity.this.speakTTS();
                    return;
            }
        }
    };
    View.OnClickListener mStudyContentInfoTextViewOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
        }
    };
    View.OnClickListener mCorrectBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runAnswer(1);
        }
    };
    View.OnClickListener mWrongBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runAnswer(2);
        }
    };
    View.OnClickListener mEffectBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.toggleEffectBtn();
        }
    };
    View.OnClickListener mCradleBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runCradleBtn();
        }
    };
    View.OnClickListener mStudyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runStudyBtn();
        }
    };
    View.OnClickListener mDictationBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runDictationBtn();
        }
    };
    View.OnClickListener mUsUkBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            int newLang;
            boolean z = true;
            if (!TTSEngine.isPlayIdleState()) {
                if (v.getId() == R.id.ToggleUkBtn) {
                    CheckBox checkBox = StudyActivity.this.mUkBtn;
                    if (StudyActivity.this.mUkBtn.isChecked()) {
                        z = false;
                    }
                    checkBox.setChecked(z);
                    return;
                }
                CheckBox checkBox2 = StudyActivity.this.mUsBtn;
                if (StudyActivity.this.mUsBtn.isChecked()) {
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
            if (StudyActivity.this.mTTSLang != newLang) {
                StudyActivity.this.mTTSLang = newLang;
                StudyActivity.this.speakTTS();
            }
            StudyActivity.this.setUsUkBtnChecked();
        }
    };
    CompoundButton.OnCheckedChangeListener mSpeakerOnOffBtnOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.StudyActivity.10
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                StudyActivity.this.mIsSpeakOn = false;
            } else {
                StudyActivity.this.mIsSpeakOn = true;
            }
            StudyActivity.this.setSpeakUsUkBtn();
        }
    };
    View.OnClickListener mRestudyCorrectWordLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.11
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runRetry(0);
        }
    };
    View.OnClickListener mRestudyWrongWordLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.12
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runRetry(1);
        }
    };
    View.OnFocusChangeListener mRestudyCorrectWordLayoutOnFocusListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.StudyActivity.13
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                ((TextView) v).setTextColor(StudyActivity.this.getResources().getColor(R.color.study_memorized_textColor2_sel));
                ((TextView) v).setBackgroundColor(StudyActivity.this.getResources().getColor(R.color.study_answer_selectbg));
                return;
            }
            ((TextView) v).setTextColor(StudyActivity.this.getResources().getColor(R.color.study_memorized_textColor2));
            ((TextView) v).setBackgroundColor(StudyActivity.this.getResources().getColor(R.color.transparent));
        }
    };
    View.OnFocusChangeListener mRestudyWrongWordLayoutOnFocusListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.StudyActivity.14
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                ((TextView) v).setTextColor(StudyActivity.this.getResources().getColor(R.color.study_unknown_textColor2_sel));
                ((TextView) v).setBackgroundColor(StudyActivity.this.getResources().getColor(R.color.study_answer_selectbg));
                return;
            }
            ((TextView) v).setTextColor(StudyActivity.this.getResources().getColor(R.color.study_unknown_textColor2));
            ((TextView) v).setBackgroundColor(StudyActivity.this.getResources().getColor(R.color.transparent));
        }
    };
    View.OnClickListener mChangeFlashcardBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.15
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runFlashCardSelect();
        }
    };
    View.OnClickListener mRetryBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.16
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runRetryBtn();
        }
    };
    View.OnClickListener mShowMeanBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.17
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.mIsShowMean = !StudyActivity.this.mIsShowMean;
            StudyActivity.this.mShowMeanBtn.setChecked(StudyActivity.this.mIsShowMean);
            if (StudyActivity.this.mIsShowMean) {
                StudyActivity.this.showToastCenter(StudyActivity.this.getString(R.string.study_remindWordMean));
            } else {
                StudyActivity.this.showToastCenter(StudyActivity.this.getString(R.string.study_doNotRemindWordMean));
            }
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.StudyActivity.18
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(StudyActivity.this);
        }
    };
    ExtendTextView.AfterSetMeanViewCallback mAfterSetMeanViewCallback = new ExtendTextView.AfterSetMeanViewCallback() { // from class: com.diotek.diodict.StudyActivity.19
        @Override // com.diotek.diodict.mean.ExtendTextView.AfterSetMeanViewCallback
        public int afterSetMean() {
            if (StudyActivity.this.mNextBtn != null) {
                StudyActivity.this.mNextBtn.setVisibility(View.VISIBLE);
            }
            StudyActivity.this.setSpeakUsUkBtn();
            StudyActivity.this.speakTTS();
            return 0;
        }
    };
    BaseMeanController.MeanTitleTextSizeUpdateCallback mMeanTitleTextSizeUpdateCallback = new BaseMeanController.MeanTitleTextSizeUpdateCallback() { // from class: com.diotek.diodict.StudyActivity.20
        @Override // com.diotek.diodict.mean.BaseMeanController.MeanTitleTextSizeUpdateCallback
        public void setFontSize() {
            StudyActivity.this.mMeanTitleView.setTextSize(1, StudyActivity.this.mMeanController.getTitleFontSize(StudyActivity.this.mIsTitleOnly));
            StudyActivity.this.mMeanTitleView.requestLayout();
            StudyActivity.this.setSpeakUsUkBtn();
        }
    };
    View.OnClickListener mNextBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.StudyActivity.21
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            StudyActivity.this.runNextBtn();
        }
    };
    Runnable mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.StudyActivity.22
        @Override // java.lang.Runnable
        public void run() {
            String TTSWord = StudyActivity.this.mMeanContentView.getKeyword();
            int TTSDbType = StudyActivity.this.mMeanContentView.getDbtype();
            if (TTSWord != null) {
                CommonUtils.stopTTS();
            }
            CommonUtils.playTTS(StudyActivity.this.mTTSLang, TTSWord, TTSDbType, 1);
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
            initSounds();
            initVibrator();
            pressTopButton(this.mStudyBtn);
        }
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        boolean z = false;
        this.mIsCreate = false;
        hideQuestionContentImageView();
        if (this.mStudyHandler != null) {
            if (this.mStudyHandler.hasMessages(4)) {
                this.mStudyHandler.removeMessages(4);
            }
            if (this.mStudyHandler.hasMessages(5)) {
                this.mStudyHandler.removeMessages(5);
            }
        }
        CheckBox checkBox = this.mSpeakerOnOffBtn;
        if (!this.mIsSpeakOn) {
            z = true;
        }
        checkBox.setChecked(z);
        initActivity();
        super.onConfigurationChanged(newConfig);
        pressTopButton(this.mStudyBtn);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onPause() {
        stopTTS();
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        if (this.mStudyHandler != null) {
            if (this.mStudyHandler.hasMessages(4)) {
                this.mStudyHandler.removeMessages(4);
            }
            if (this.mStudyHandler.hasMessages(5)) {
                this.mStudyHandler.removeMessages(5);
            }
            if (this.mStudyHandler.hasMessages(6)) {
                this.mStudyHandler.removeMessages(6);
            }
            this.mStudyHandler = null;
        }
        destroyData();
        memoryInitialize(false);
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (runKeyCodeBack()) {
                    return true;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void initActivity() {
        if (!this.mIsCreate) {
            destroyData();
            memoryInitialize(true);
        }
        setContentView(R.layout.study_layout);
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        prepareContentLayout();
        restoreData();
    }

    private void restoreData() {
        if (this.mStateStudy == 1) {
            if (this.mStudyList != null && this.mStudyList.size() > this.mCurrentWordPos) {
                prepareMeanController(this.mStudyList.get(this.mCurrentWordPos).intValue());
            }
            setResultLayout(false);
        } else if (this.mCurrentWordPos == this.mStudyTotalWordCount && this.mIsTitleOnly && !this.mIsShowMean) {
            if (this.mStudyList != null && this.mStudyList.size() > this.mCurrentWordPos) {
                prepareMeanController(this.mStudyList.get(this.mCurrentWordPos).intValue());
            }
            hideQuestionContentImageView();
            hideMeanView();
            this.mStudyHandler.sendEmptyMessage(2);
        } else {
            runStudyMode();
        }
    }

    public void prepareContentLayout() {
        initializeStudyList();
        prepareContentTopLayout();
        prepareContentMeanLayout();
    }

    public void prepareContentTopLayout() {
        this.mEffectBtn = (ImageButton) findViewById(R.id.SoundBtn);
        if (Dependency.isContainCradleMode()) {
            this.mCradleBtn = (Button) findViewById(R.id.CradleBtn);
        } else {
            findViewById(R.id.CradleBtn).setVisibility(View.GONE);
        }
        this.mStudyBtn = (Button) findViewById(R.id.StudyBtn);
        this.mDictationBtn = (Button) findViewById(R.id.DictationBtn);
        this.mEffectBtn.setOnClickListener(this.mEffectBtnOnClickListener);
        this.mStudyBtn.setOnClickListener(this.mStudyBtnOnClickListener);
        if (this.mCradleBtn != null) {
            this.mCradleBtn.setOnClickListener(this.mCradleBtnOnClickListener);
        }
        this.mDictationBtn.setOnClickListener(this.mDictationBtnOnClickListener);
        if (!Dependency.isContainHandWrightReocg() || EngineManager3rd.getSupporTTS() == null) {
            this.mDictationBtn.setVisibility(View.GONE);
        }
        prepareContentEffectBtn();
    }

    private void prepareContentEffectBtn() {
        this.mEffectMode = DictUtils.getDictationFeedbackModeFromPreference(this);
        reBuildEffectBtn();
    }

    public void prepareContentMeanLayout() {
        prepareContentMeanQuestionLayout();
        prepareContentMeanQuestionBottomLayout();
        prepareContentMeanAnswerLayout();
    }

    public void prepareContentMeanQuestionLayout() {
        this.mQuestionLayout = (RelativeLayout) findViewById(R.id.StudyQuestionLayout);
        this.mMeanTitleView = (TextView) findViewById(R.id.StudyMeanTitleView);
        this.mMeanWordCount = (TextView) findViewById(R.id.CountTextView);
        this.mMeanContentView = (ExtendTextView) findViewById(R.id.StudyMeanContentView);
        this.mMeanInfoTextView = (TextView) findViewById(R.id.StudyContentMeanInfoTextView);
        this.mCorrectImageView = (ImageView) findViewById(R.id.CorrectImageView);
        this.mWrongImageView = (ImageView) findViewById(R.id.WrongImageView);
        this.mCorrectDioboy = (ImageView) findViewById(R.id.CorrectDioboy);
        this.mWrongDioboy = (ImageView) findViewById(R.id.WrongDioboy);
        this.mSpeakerOnOffBtn = (CheckBox) findViewById(R.id.SpeakerBtn);
        this.mUsBtn = (CheckBox) findViewById(R.id.ToggleUsBtn);
        this.mUkBtn = (CheckBox) findViewById(R.id.ToggleUkBtn);
        this.mMeanScrollView = (ScrollView) findViewById(R.id.StudyMeanScrollView);
        this.mNextBtn = (Button) findViewById(R.id.nextBtn);
        this.mShowMeanBtn = (CheckBox) findViewById(R.id.ShowMeanBtn);
        this.mEmark = (ImageView) findViewById(R.id.emark);
        this.mStudyInfoTextView = (TextView) findViewById(R.id.StudyContentInfoTextView);
        this.mNextBtn.setOnClickListener(this.mNextBtnOnClickListener);
        this.mSpeakerOnOffBtn.setOnCheckedChangeListener(this.mSpeakerOnOffBtnOnCheckedChangeListener);
        this.mMeanController = new CursorMeanController(this, this.mMeanTitleView, this.mMeanContentView, null, null, this.mEngine, this.mThemeModeCallback, null, null);
        this.mMeanController.setMeanContentTextViewCallback(null, null, false, this.mAfterSetMeanViewCallback);
        this.mMeanController.setMeanTitleTextSizeUpdateCallback(this.mMeanTitleTextSizeUpdateCallback);
        this.mShowMeanBtn.setOnClickListener(this.mShowMeanBtnOnClickListener);
        this.mUsBtn.setOnClickListener(this.mUsUkBtnOnClickListener);
        this.mUkBtn.setOnClickListener(this.mUsUkBtnOnClickListener);
        this.mMeanWordCount.setText(String.valueOf(this.mWordCount - this.mCurrentWordPos) + "/" + this.mWordCount);
        setUsUkBtnChecked();
        if (!Dependency.isContainTTS()) {
            this.mSpeakerOnOffBtn.setVisibility(View.GONE);
            this.mUsBtn.setVisibility(View.GONE);
            this.mUkBtn.setVisibility(View.GONE);
        }
        prepareStudyInfoLayout();
    }

    public void prepareMeanController(int pos) {
        this.mMeanController.setMeanView(DioDictDatabase.getTableName(this.mWordbookFolderName), this.mWordbookFolderName, this.mFolderId, this.mSort, this.mCurrentWordPos, false);
    }

    public void prepareContentMeanAnswerLayout() {
        this.mAnswerLayout = (RelativeLayout) findViewById(R.id.AnswerLayout);
        this.mTotalCountTextView = (TextView) findViewById(R.id.TotalCountTextView);
        this.mCorrectCountTextView = (TextView) findViewById(R.id.CorrectWordCountTextView);
        this.mWrongCountTextView = (TextView) findViewById(R.id.WrongWordCountTextView);
        this.mResultStateImageView = (ImageView) findViewById(R.id.ResultStateImageView);
        this.mChangeFlashcardBtn = (Button) findViewById(R.id.ChangeFlashcardBtn);
        this.mRestudyCorrectWordButton = (TextView) findViewById(R.id.RestudyCorrectWordBtn);
        this.mRestudyWrongWordButton = (TextView) findViewById(R.id.RestudyWrongWordBtn);
        this.mRetryBtn = (Button) findViewById(R.id.RetryBtn);
        this.mRestudyCorrectWordButton.setOnClickListener(this.mRestudyCorrectWordLayoutOnClickListener);
        this.mRestudyCorrectWordButton.setOnFocusChangeListener(this.mRestudyCorrectWordLayoutOnFocusListener);
        this.mRestudyWrongWordButton.setOnClickListener(this.mRestudyWrongWordLayoutOnClickListener);
        this.mRestudyWrongWordButton.setOnFocusChangeListener(this.mRestudyWrongWordLayoutOnFocusListener);
        this.mChangeFlashcardBtn.setOnClickListener(this.mChangeFlashcardBtnOnClickListener);
        this.mRetryBtn.setOnClickListener(this.mRetryBtnOnClickListener);
        if (CommonUtils.isLowResolutionDevice(this)) {
            prepareAnswerLayoutLowResolution();
        }
    }

    public void prepareContentMeanQuestionBottomLayout() {
        this.mCorrectBtn = (ImageButton) findViewById(R.id.CorrectBtn);
        this.mWrongBtn = (ImageButton) findViewById(R.id.WrongBtn);
        this.mCorrectBtn.setOnClickListener(this.mCorrectBtnOnClickListener);
        this.mWrongBtn.setOnClickListener(this.mWrongBtnOnClickListener);
    }

    private void prepareStudyInfoLayout() {
        LinearLayout layout;
        RelativeLayout.LayoutParams param;
        if (Dependency.isContainCradleMode() && Dependency.isContainDictationMode() && getResources().getConfiguration().orientation == 2 && getResources().getDisplayMetrics().widthPixels == 320 && (layout = (LinearLayout) findViewById(R.id.StudyInfoLayout)) != null && (param = (RelativeLayout.LayoutParams) layout.getLayoutParams()) != null) {
            param.addRule(9);
            param.setMargins(getResources().getDimensionPixelSize(R.dimen.studyinfotext_marginLeft), 0, 0, 0);
        }
    }

    private void prepareAnswerLayoutLowResolution() {
        if (CommonUtils.isLowResolutionDevice(this)) {
            if (getResources().getConfiguration().orientation == 1) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.ResultChoiceButtonLayout);
                if (layout != null) {
                    layout.setOrientation(0);
                }
                if (CommonUtils.isQVGADevice(this)) {
                    ImageView resultImage = (ImageView) findViewById(R.id.ResultStateImageView);
                    if (resultImage != null) {
                        resultImage.setVisibility(View.GONE);
                    }
                    RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) layout.getLayoutParams();
                    if (param != null) {
                        param.addRule(14);
                    }
                    RelativeLayout content_layout = (RelativeLayout) findViewById(R.id.ContentResultChoiceLayout);
                    if (content_layout != null) {
                        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(-2, getResources().getDimensionPixelSize(R.dimen.resultchoicebutton_height));
                        param1.addRule(12);
                        param1.addRule(3, R.id.ContentResultLayout);
                        content_layout.setLayoutParams(param1);
                        content_layout.setGravity(1);
                    }
                }
            }
            if (getResources().getConfiguration().orientation == 2 && getResources().getDisplayMetrics().widthPixels == 320) {
                ImageView resultImage2 = (ImageView) findViewById(R.id.ResultStateImageView);
                if (resultImage2 != null) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
                    params.addRule(11);
                    params.addRule(12);
                    resultImage2.setLayoutParams(params);
                }
                RelativeLayout choicebtn_layout = (RelativeLayout) findViewById(R.id.ContentResultChoiceLayout);
                if (choicebtn_layout != null) {
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.resultchoicelayout_width), getResources().getDimensionPixelSize(R.dimen.resultchoicelayout_height));
                    params2.addRule(10);
                    params2.addRule(11);
                    params2.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.resultchoicelayout_marginBottom));
                    choicebtn_layout.setLayoutParams(params2);
                }
                LinearLayout content_layout2 = (LinearLayout) findViewById(R.id.ContentResultLayout);
                if (content_layout2 != null) {
                    ((RelativeLayout.LayoutParams) content_layout2.getLayoutParams()).addRule(9);
                }
            }
        }
    }

    public void initSounds() {
        if (DictUtils.mSoundPool == null) {
            DictUtils.mSoundPool = new SoundPool(6, 1, 0);
        }
        if (DictUtils.mSoundPoolMap == null) {
            DictUtils.mSoundPoolMap = new HashMap<>();
            DictUtils.mSoundPoolMap.put(0, Integer.valueOf(DictUtils.mSoundPool.load(this, R.raw.correct, 1)));
            DictUtils.mSoundPoolMap.put(1, Integer.valueOf(DictUtils.mSoundPool.load(this, R.raw.wrong, 1)));
            DictUtils.mSoundPoolMap.put(2, Integer.valueOf(DictUtils.mSoundPool.load(this, R.raw.result_excellent, 1)));
            DictUtils.mSoundPoolMap.put(3, Integer.valueOf(DictUtils.mSoundPool.load(this, R.raw.result_good, 1)));
            DictUtils.mSoundPoolMap.put(4, Integer.valueOf(DictUtils.mSoundPool.load(this, R.raw.result_oops, 1)));
            DictUtils.mSoundPoolMap.put(5, Integer.valueOf(DictUtils.mSoundPool.load(this, R.raw.question, 1)));
        }
        if (DictUtils.mAudioManager == null) {
            DictUtils.mAudioManager = (AudioManager) getSystemService("audio");
        }
    }

    public void initVibrator() {
        if (this.mVibrator == null) {
            this.mVibrator = (Vibrator) getSystemService("vibrator");
        }
    }

    public void runStudyMode() {
        this.mStateStudy = 0;
        this.mShowMeanBtn.setChecked(this.mIsShowMean);
        if (!this.mIsTitleOnly || (this.mIsTitleOnly && this.mIsShowMean && this.mNextStep == 2)) {
            prepareMeanController(this.mCurrentWordPos - 1);
            showMeanView();
        } else {
            prepareMeanController(this.mStudyList.get(this.mCurrentWordPos).intValue());
            this.mMeanController.setTitleViewByPos(this.mStudyList.get(this.mCurrentWordPos).intValue(), 0);
            this.mQuestionLayout.setVisibility(View.VISIBLE);
            this.mNextBtn.setVisibility(View.INVISIBLE);
            this.mMeanWordCount.setVisibility(View.VISIBLE);
            this.mMeanWordCount.setText((this.mCurrentWordPos + 1) + "/" + this.mStudyTotalWordCount);
            showInfoText(0);
            setSpeakUsUkBtn();
            speakTTS();
        }
        this.mAnswerLayout.setVisibility(View.GONE);
        setAnswerBtnClick(true);
    }

    public void runAnswer(int answer) {
        if (answer == 1) {
            playFeedback(0);
            setAnswerBtnClick(false);
            this.mCorrectImageView.setVisibility(View.VISIBLE);
            this.mCorrectDioboy.setVisibility(View.VISIBLE);
            this.mCorrectAnswerCount++;
            if (this.mCurrentWordPos < this.mStudyList.size()) {
                List<Integer> list = this.mStudyCorrectPosList;
                List<Integer> list2 = this.mStudyList;
                int i = this.mCurrentWordPos;
                this.mCurrentWordPos = i + 1;
                list.add(list2.get(i));
            }
        } else if (answer == 2) {
            playFeedback(1);
            setAnswerBtnClick(false);
            this.mWrongImageView.setVisibility(View.VISIBLE);
            this.mWrongDioboy.setVisibility(View.VISIBLE);
            this.mWrongAnswerCount++;
            if (this.mCurrentWordPos < this.mStudyList.size()) {
                List<Integer> list3 = this.mStudyWrongPosList;
                List<Integer> list4 = this.mStudyList;
                int i2 = this.mCurrentWordPos;
                this.mCurrentWordPos = i2 + 1;
                list3.add(list4.get(i2));
            }
        }
        this.mMeanWordCount.setVisibility(View.GONE);
        sendMessages();
    }

    private void sendMessages() {
        if (!this.mIsReStudy) {
            if (!this.mIsShowMean) {
                this.mNextStep = 1;
                this.mStudyHandler.sendEmptyMessageDelayed(4, 2000L);
                return;
            }
            this.mNextStep = 2;
            this.mStudyHandler.sendEmptyMessageDelayed(5, 2000L);
        } else if (!this.mIsShowMean) {
            this.mNextStep = 1;
            this.mStudyHandler.sendEmptyMessageDelayed(6, 2000L);
        } else {
            this.mNextStep = 2;
            this.mStudyHandler.sendEmptyMessageDelayed(5, 2000L);
        }
    }

    public void runCradleBtn() {
        finish();
        Intent intent = new Intent();
        intent.setClass(this, CradleActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
    }

    public void runStudyBtn() {
    }

    public void runDictationBtn() {
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

    public void runRetryBtn() {
        this.mIsReStudy = false;
        this.mIsCreate = true;
        initializeStudyList();
        runStudyMode();
    }

    public void runRetry(int retryWord) {
        if (retryWord == 0) {
            if (this.mStudyCorrectPosList.size() != 0) {
                this.mIsStudyMode = 2;
            } else {
                return;
            }
        } else if (retryWord == 1) {
            if (this.mStudyWrongPosList.size() != 0) {
                this.mIsStudyMode = 1;
            } else {
                return;
            }
        }
        this.mIsReStudy = true;
        this.mIsCreate = true;
        initializeStudyList();
        runStudyMode();
    }

    public boolean runKeyCodeBack() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void runNextBtn() {
        this.mMeanContentView.setText("");
        this.mNextStep = 1;
        if (this.mIsReStudy) {
            this.mStudyHandler.sendEmptyMessage(6);
        } else {
            this.mStudyHandler.sendEmptyMessage(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runFlashCardSelect() {
        finish();
        Intent intent = new Intent();
        intent.putExtra(DictInfo.INTENT_FLASHCARD_ENTERING_MODE, toString());
        intent.setClass(this, FlashcardActivity.class);
        startActivity(intent);
    }

    public void setResultLayout(boolean playResultSound) {
        this.mStateStudy = 1;
        this.mQuestionLayout.setVisibility(View.GONE);
        this.mAnswerLayout.setVisibility(View.VISIBLE);
        showInfoText(4);
        int correctWordCount = this.mStudyCorrectPosList == null ? 0 : this.mStudyCorrectPosList.size();
        int wrongWordCount = this.mStudyWrongPosList == null ? 0 : this.mStudyWrongPosList.size();
        int mCorrectWordPercent = (correctWordCount * 100) / this.mWordCount;
        int res = R.drawable.dioboy_excellent;
        int soundId = 2;
        if (mCorrectWordPercent < 60) {
            res = R.drawable.dioboy_oops;
            soundId = 4;
        } else if (mCorrectWordPercent < 90) {
            res = R.drawable.dioboy_good;
            soundId = 3;
        }
        if (playResultSound) {
            setAnswerBtnClick(true);
            playFeedback(soundId);
        }
        this.mResultStateImageView.setBackgroundResource(res);
        String totalCount = getResources().getString(R.string.study_totalcount);
        this.mTotalCountTextView.setText(totalCount.replaceAll("%d", String.valueOf(this.mWordCount)));
        this.mCorrectCountTextView.setText(String.valueOf(correctWordCount));
        this.mWrongCountTextView.setText(String.valueOf(wrongWordCount));
        this.mMeanTitleView.setText("");
        this.mCurrentWordPos = 0;
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

    public void hideQuestionContentImageView() {
        this.mCorrectImageView.setVisibility(View.GONE);
        this.mWrongImageView.setVisibility(View.GONE);
        this.mCorrectDioboy.setVisibility(View.GONE);
        this.mWrongDioboy.setVisibility(View.GONE);
    }

    public void hideMeanView() {
        this.mIsTitleOnly = true;
        this.mMeanInfoTextView.setVisibility(View.VISIBLE);
        this.mCorrectBtn.setVisibility(View.VISIBLE);
        this.mWrongBtn.setVisibility(View.VISIBLE);
        this.mMeanScrollView.setVisibility(View.GONE);
    }

    public void showMeanView() {
        this.mIsTitleOnly = false;
        showInfoText(4);
        this.mMeanTitleView.setText("");
        this.mMeanInfoTextView.setVisibility(View.GONE);
        this.mCorrectBtn.setVisibility(View.INVISIBLE);
        this.mWrongBtn.setVisibility(View.INVISIBLE);
        this.mMeanController.setMeanViewByPos(this.mStudyList.get(this.mCurrentWordPos - 1).intValue(), 0);
        this.mMeanScrollView.setVisibility(View.VISIBLE);
        this.mMeanWordCount.setVisibility(View.GONE);
        this.mMeanTitleView.setTextSize(1, this.mMeanController.getTitleFontSize(this.mIsTitleOnly));
        this.mMeanTitleView.requestLayout();
    }

    public void playFeedback(int soundId) {
        if (this.mCorrectBtn.isClickable() && this.mWrongBtn.isClickable()) {
            switch (this.mEffectMode) {
                case 0:
                    PlaySound(soundId);
                    return;
                case 1:
                    playVibration(soundId);
                    return;
                default:
                    return;
            }
        }
    }

    public synchronized void PlaySound(int sound) {
        if (DictUtils.mAudioManager != null && this.mEffectMode == 0) {
            int systemVol = DictUtils.mAudioManager.getStreamVolume(3);
            int systemMaxVol = DictUtils.mAudioManager.getStreamMaxVolume(3);
            float streamVolume = (systemVol / systemMaxVol) / 4.0f;
            DictUtils.mSoundPool.play(DictUtils.mSoundPoolMap.get(Integer.valueOf(sound)).intValue(), streamVolume, streamVolume, 1, 0, 1.0f);
        }
    }

    private void playVibration(int sound) {
        if (this.mVibrator == null) {
            this.mVibrator = (Vibrator) getSystemService("vibrator");
        }
        long[] pattern = DictUtils.sVibratePattern[sound];
        if (this.mVibrator != null) {
            this.mVibrator.vibrate(pattern, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void speakTTS() {
        if (this.mIsSpeakOn && this.mIsAvailTTS) {
            new Thread(this.mRunnablePlayTTS).start();
        }
    }

    private void stopTTS() {
        CommonUtils.stopTTS();
    }

    private void showUsUkBtn(boolean bShow) {
        if (!Dependency.isContainTTS()) {
            bShow = false;
            this.mIsSpeakOn = false;
        }
        if (bShow && this.mIsSpeakOn) {
            int nLang = EngineInfo3rd.TTS_KOREAN;
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
        String word = this.mMeanContentView.getKeyword();
        if (word != null) {
            int codepage = DictUtils.getCodePage(word);
            this.mIsAvailTTS = EngineInfo3rd.IsTTSAvailableCodePage(codepage);
            if (codepage != -1) {
                if (CommonUtils.isUselessTTSWord(word, this.mMeanContentView.getDbtype())) {
                    this.mIsAvailTTS = false;
                }
                if (this.mIsAvailTTS) {
                    if ((!this.mIsSpeakOn) != this.mSpeakerOnOffBtn.isChecked()) {
                        this.mSpeakerOnOffBtn.setChecked(!this.mIsSpeakOn);
                    }
                    this.mSpeakerOnOffBtn.setVisibility(View.VISIBLE);
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
                this.mSpeakerOnOffBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void reBuildEffectBtn() {
        switch (this.mEffectMode) {
            case 0:
                this.mEffectBtn.setImageResource(R.drawable.icon_sound);
                return;
            case 1:
                this.mEffectBtn.setImageResource(R.drawable.icon_vibe);
                return;
            case 2:
                this.mEffectBtn.setImageResource(R.drawable.icon_mute);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleEffectBtn() {
        this.mEffectMode = (this.mEffectMode + 1) % 3;
        reBuildEffectBtn();
        DictUtils.setDictationFeedbackModeToPreference(this, this.mEffectMode);
    }

    private void initializeStudyList() {
        if (this.mStudyList != null && this.mIsCreate) {
            this.mStudyList.clear();
            if (this.mIsReStudy) {
                if (this.mIsStudyMode == 1) {
                    for (int i = 0; i < this.mStudyWrongPosList.size(); i++) {
                        this.mStudyList.add(this.mStudyWrongPosList.get(i));
                    }
                    this.mStudyWrongPosList.clear();
                } else if (this.mIsStudyMode == 2) {
                    for (int i2 = 0; i2 < this.mStudyCorrectPosList.size(); i2++) {
                        this.mStudyList.add(this.mStudyCorrectPosList.get(i2));
                    }
                    this.mStudyCorrectPosList.clear();
                }
            } else {
                this.mStudyWrongPosList.clear();
                this.mStudyCorrectPosList.clear();
                for (int i3 = 0; i3 < this.mWordCount; i3++) {
                    this.mStudyList.add(Integer.valueOf(i3));
                }
                this.mIsStudyMode = 0;
            }
            this.mStudyTotalWordCount = this.mStudyList.size();
            this.mCorrectAnswerCount = 0;
            this.mWrongAnswerCount = 0;
            this.mCurrentWordPos = 0;
            this.mNextStep = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInfoText(int nVisibility) {
        if (this.mEmark != null) {
            this.mEmark.setVisibility(nVisibility);
        }
        this.mStudyInfoTextView.setVisibility(nVisibility);
    }

    private void memoryInitialize(boolean isconfigChange) {
        if (this.mCorrectImageView != null) {
            UITools.recycleDrawable(this.mCorrectImageView.getBackground(), false, isconfigChange);
            this.mCorrectImageView = null;
        }
        if (this.mWrongImageView != null) {
            UITools.recycleDrawable(this.mWrongImageView.getBackground(), false, isconfigChange);
            this.mWrongImageView = null;
        }
        if (this.mCorrectDioboy != null) {
            UITools.recycleDrawable(this.mCorrectDioboy.getBackground(), false, isconfigChange);
            this.mCorrectDioboy = null;
        }
        if (this.mWrongDioboy != null) {
            UITools.recycleDrawable(this.mWrongDioboy.getBackground(), false, isconfigChange);
            this.mWrongDioboy = null;
        }
        System.gc();
    }

    private void destroyData() {
        if (this.mMeanContentView != null) {
            this.mMeanContentView.onDestroy();
            this.mMeanContentView = null;
        }
        if (this.mMeanController != null) {
            this.mMeanController.onDestory();
            this.mMeanController = null;
        }
    }

    public void setAnswerBtnClick(boolean click) {
        this.mCorrectBtn.setClickable(click);
        this.mWrongBtn.setClickable(click);
    }
}
