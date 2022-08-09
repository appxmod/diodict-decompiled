package com.diotek.diodict;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.CursorMeanController;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.RecognizeView;
import com.diotek.diodict.uitool.UITools;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.lang.Character;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class DictationActivity extends BaseActivity {
    private static final int DICTATION_RESULT = 1;
    private static final int NEXT_WORD = 0;
    private int mCorrectWordPercent;
    private int mCurrentOrientation;
    private Vibrator mVibrator;
    private ViewSwitcher switcher;
    private final int DICTATION_STATE_IDLE = 0;
    private final int DICTATION_STATE_DICTATING = 1;
    private final int DICTATION_STATE_RESULT = 2;
    private Controls mControlPort = new Controls();
    private Controls mControlLand = new Controls();
    ImageButton mEffectBtn = null;
    Button mCradleBtn = null;
    Button mStudyBtn = null;
    Button mDictationBtn = null;
    RadioButton mShowMeanBtn = null;
    Button mListenBtn = null;
    RelativeLayout mQuestionLayout = null;
    RelativeLayout mResultLayout = null;
    RelativeLayout mKeywordPaperLayout = null;
    ScrollView mKeywordScrollView = null;
    RelativeLayout mInnerMeanPaperLayout = null;
    TextView mRestudyCorrectWordButton = null;
    TextView mRestudyWrongWordButton = null;
    TextView mCorrectCountTextView = null;
    TextView mWrongCountTextView = null;
    TextView mTotalCountTextView = null;
    TextView mResultTotalCountTextView = null;
    TextView mResultCorrectWordCountTextView = null;
    TextView mResultWrongWordCountTextView = null;
    Button mStartBtn = null;
    RecognizeView mRecognizeView = null;
    View mBoardBgView = null;
    RelativeLayout mWrapRecognizeLayout = null;
    LinearLayout mListenLayout = null;
    ImageButton mDelBtn = null;
    Button mPassBtn = null;
    Button mOkBtn = null;
    Button mChangeFlashcardBtn = null;
    Button mRetryBtn = null;
    ExtendTextView mMeanContentView = null;
    ScrollView mMeanScrollView = null;
    ImageView mDictationCorrectImage = null;
    ImageView mDictationWrongImage = null;
    ImageView mResultStateImageView = null;
    RelativeLayout mDictationKeywordPaperRootLayout = null;
    CursorMeanController mMeanController = null;
    int mFolderId = -1;
    int mWordCount = 0;
    String mWordbookFolderName = "";
    int mSort = 0;
    Cursor mQuizCursor = null;
    int mMaxQuizWord = 0;
    int mCorrectAnswerCount = 0;
    int mWrongAnswerCount = 0;
    String mTTSWord = null;
    String mCorrectWord = null;
    int mInputWordPos = 0;
    TextView[] mCorrectTextViews = null;
    private boolean isStartPortrait = true;
    private int mCurrentWordPos = 0;
    private int SCROLLVIEW_MAX_LINE = 3;
    private int FIRST_ONELINE_CHAR_COUNT = 8;
    private int SECOND_ONELINE_CHAR_COUNT = 10;
    private int THIRD_ONELINE_CHAR_COUNT = 13;
    private int FIRST_CHAR_MAX_COUNT = 8;
    private int SECOND_CHAR_MAX_COUNT = 20;
    private int THIRD_CHAR_MAX_COUNT = 39;
    private int FIRST_CHAR_MAX_WIDTH = 32;
    private int FIRST_CHAR_MAX_HEIGHT = 40;
    private int SECOND_CHAR_MAX_WIDTH = 25;
    private int SECOND_CHAR_MAX_HEIGHT = 33;
    private int THIRD_CHAR_MAX_WIDTH = 20;
    private int THIRD_CHAR_MAX_HEIGHT = 27;
    private final int DICTATION_NORMAL_MODE = 0;
    private final int DICTATION_RETRY_WRONGWORD_MODE = 1;
    private final int DICTATION_RETRY_CORRECTWORD_MODE = 2;
    protected boolean mOperationMutex = false;
    private List<Integer> mDictactionList = new ArrayList();
    private List<Integer> mDictactionWrongPosList = new ArrayList();
    private List<Integer> mDictactionCorrectPosList = new ArrayList();
    private boolean mIsReDictation = false;
    private int mIsDictationMode = 0;
    private int mDictationTotalWordCount = 0;
    private int mDictationState = 0;
    private StringBuilder mInputString = new StringBuilder();
    private boolean mIsShowMeaning = false;
    private int mAnswerLines = 1;
    private final int mShowMeanBtnHeight = 42;
    private final int SHOW_PEDING_TIME = DictUtils.DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE;
    private final int EXCELLENT_DICTATION_THRESHOLD = 90;
    private final int GOOD_DICTATION_THRESHOLD = 60;
    private boolean mIsPending = false;
    private int mEffectMode = 0;
    private int mTTSLang = 0;
    private boolean mIsCreate = true;
    private boolean mIsAnimating = false;
    private Integer[] mDicTypes = null;
    private int mMeanScrollHeight = 80;
    private Handler mDictationHandler = new Handler() { // from class: com.diotek.diodict.DictationActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DictationActivity.this.setEnableBottomBtns();
            switch (msg.what) {
                case 0:
                    DictationActivity.this.prepareNextQuestion();
                    return;
                case 1:
                    DictationActivity.this.hideQuestionLayout();
                    return;
                default:
                    return;
            }
        }
    };
    private View.OnClickListener mOperationBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!DictationActivity.this.mOperationMutex) {
                switch (v.getId()) {
                    case R.id.listenBtn_land /* 2131099719 */:
                    case R.id.listenBtn /* 2131099759 */:
                        if (DictationActivity.this.mDictationState == 1) {
                            DictationActivity.this.runListenBtn();
                            return;
                        }
                        return;
                    case R.id.DictationStartBtn_land /* 2131099748 */:
                    case R.id.DictationStartBtn /* 2131099775 */:
                        if (DictationActivity.this.mDictationState == 0) {
                            DictationActivity.this.mOperationMutex = true;
                            DictationActivity.this.runStartBtn();
                            return;
                        }
                        return;
                    case R.id.DelBtn_land /* 2131099753 */:
                    case R.id.DelBtn /* 2131099780 */:
                        if (DictationActivity.this.mDictationState == 1) {
                            DictationActivity.this.runDelBtn();
                            return;
                        }
                        return;
                    case R.id.passBtn_land /* 2131099754 */:
                    case R.id.passBtn /* 2131099781 */:
                        if (DictationActivity.this.mDictationState == 1) {
                            CommonUtils.stopTTS();
                            DictationActivity.this.mOperationMutex = true;
                            DictationActivity.this.runPassBtn();
                            return;
                        }
                        return;
                    case R.id.OkBtn_land /* 2131099755 */:
                    case R.id.OkBtn /* 2131099782 */:
                        if (DictationActivity.this.mDictationState == 1) {
                            CommonUtils.stopTTS();
                            DictationActivity.this.mOperationMutex = true;
                            DictationActivity.this.runOkBtn();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    };
    View.OnClickListener mEffectBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            DictationActivity.this.toggleEffectBtn();
        }
    };
    View.OnClickListener mCradleBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            DictationActivity.this.runCradleBtn();
        }
    };
    View.OnClickListener mStudyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            DictationActivity.this.runStudyBtn();
        }
    };
    RecognizeView.RecognizeResultCallback mRecognizeResultCallback = new RecognizeView.RecognizeResultCallback() { // from class: com.diotek.diodict.DictationActivity.6
        @Override // com.diotek.diodict.uitool.RecognizeView.RecognizeResultCallback
        public void setResult(String result) {
            if (!DictationActivity.this.mIsAnimating) {
                DictationActivity.this.setCorrectTextView(result);
            }
        }
    };
    View.OnClickListener mRestudyCorrectWordBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (DictationActivity.this.mDictactionCorrectPosList != null && DictationActivity.this.mDictactionCorrectPosList.size() != 0) {
                DictationActivity.this.mCurrentWordPos = 0;
                DictationActivity.this.mIsReDictation = true;
                DictationActivity.this.mIsCreate = true;
                DictationActivity.this.mIsDictationMode = 2;
                DictationActivity.this.initializeDictationList();
                DictationActivity.this.setEnableQuizBtns(true);
                DictationActivity.this.showQuestionLayout();
                DictationActivity.this.prepareNextQuestion();
            }
        }
    };
    View.OnClickListener mRestudyWrongWordBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (DictationActivity.this.mDictactionWrongPosList != null && DictationActivity.this.mDictactionWrongPosList.size() != 0) {
                DictationActivity.this.mCurrentWordPos = 0;
                DictationActivity.this.mIsReDictation = true;
                DictationActivity.this.mIsCreate = true;
                DictationActivity.this.mIsDictationMode = 1;
                DictationActivity.this.initializeDictationList();
                DictationActivity.this.setEnableQuizBtns(true);
                DictationActivity.this.showQuestionLayout();
                DictationActivity.this.prepareNextQuestion();
            }
        }
    };
    View.OnFocusChangeListener mRestudyCorrectWordBtnOnFocusListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.DictationActivity.9
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                ((TextView) v).setTextColor(DictationActivity.this.getResources().getColor(R.color.study_memorized_textColor2_sel));
                ((TextView) v).setBackgroundColor(DictationActivity.this.getResources().getColor(R.color.study_answer_selectbg));
                return;
            }
            ((TextView) v).setTextColor(DictationActivity.this.getResources().getColor(R.color.study_memorized_textColor2));
            ((TextView) v).setBackgroundColor(DictationActivity.this.getResources().getColor(R.color.transparent));
        }
    };
    View.OnFocusChangeListener mRestudyWrongWordBtnOnFocusListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.DictationActivity.10
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                ((TextView) v).setTextColor(DictationActivity.this.getResources().getColor(R.color.study_unknown_textColor2_sel));
                ((TextView) v).setBackgroundColor(DictationActivity.this.getResources().getColor(R.color.study_answer_selectbg));
                return;
            }
            ((TextView) v).setTextColor(DictationActivity.this.getResources().getColor(R.color.study_unknown_textColor2));
            ((TextView) v).setBackgroundColor(DictationActivity.this.getResources().getColor(R.color.transparent));
        }
    };
    View.OnClickListener mShowMeanBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.11
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            DictationActivity.this.runShowMeanBtn();
        }
    };
    View.OnClickListener mChangeFlashcardBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.12
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            DictationActivity.this.runFlashCardSelect();
        }
    };
    View.OnClickListener mRetryBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.DictationActivity.13
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            DictationActivity.this.runRetryBtn();
        }
    };
    View.OnTouchListener mWrapRecognizeLayoutOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.DictationActivity.14
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View arg0, MotionEvent arg1) {
            return true;
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.DictationActivity.15
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(DictationActivity.this);
        }
    };
    Runnable mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.DictationActivity.16
        @Override // java.lang.Runnable
        public void run() {
            int TTSDbType = -1;
            if (DictationActivity.this.mQuizCursor != null) {
                TTSDbType = DictationActivity.this.mQuizCursor.getInt(DictationActivity.this.mQuizCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE));
            }
            CommonUtils.playTTS(DictationActivity.this.mTTSLang, DictationActivity.this.mTTSWord, TTSDbType, 1);
        }
    };
    private View.OnFocusChangeListener mMeanScrollViewFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.DictationActivity.17
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (DictationActivity.this.mInnerMeanPaperLayout != null) {
                if (hasFocus) {
                    DictationActivity.this.mInnerMeanPaperLayout.setBackgroundResource(R.drawable.meanpaper_bg_f);
                } else {
                    DictationActivity.this.mInnerMeanPaperLayout.setBackgroundResource(R.drawable.meanpaper_bg);
                }
            }
        }
    };
    View.OnFocusChangeListener mKeywordScrollViewOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.DictationActivity.18
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (DictationActivity.this.mDictationKeywordPaperRootLayout != null) {
                if (hasFocus) {
                    DictationActivity.this.mDictationKeywordPaperRootLayout.setBackgroundDrawable(DictationActivity.this.getResources().getDrawable(R.drawable.dictation_bg_f));
                } else {
                    DictationActivity.this.mDictationKeywordPaperRootLayout.setBackgroundDrawable(DictationActivity.this.getResources().getDrawable(R.drawable.dictation_bg));
                }
                float nDensity = CommonUtils.getDeviceDensity(DictationActivity.this);
                DictationActivity.this.mKeywordScrollView.setPadding(0, (int) (45.0f * nDensity), 0, (int) (21.0f * nDensity));
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Controls {
        public View mBoardBgView;
        public Button mChangeFlashcardBtn;
        public TextView mCorrectCountTextView;
        public Button mCradleBtn;
        public ImageButton mDelBtn;
        public Button mDictationBtn;
        public ImageView mDictationCorrectImage;
        public RelativeLayout mDictationKeywordPaperRootLayout;
        public ImageView mDictationWrongImage;
        public ImageButton mEffectBtn;
        public RelativeLayout mInnerMeanPaperLayout;
        public RelativeLayout mKeywordPaperLayout;
        public ScrollView mKeywordScrollView;
        public Button mListenBtn;
        public LinearLayout mListenLayout;
        public ExtendTextView mMeanContentView;
        public ScrollView mMeanScrollView;
        public Button mOkBtn;
        public Button mPassBtn;
        public RelativeLayout mQuestionLayout;
        public RecognizeView mRecognizeView;
        public TextView mRestudyCorrectWordButton;
        public TextView mRestudyWrongWordButton;
        public TextView mResultCorrectWordCountTextView;
        public RelativeLayout mResultLayout;
        public ImageView mResultStateImageView;
        public TextView mResultTotalCountTextView;
        public TextView mResultWrongWordCountTextView;
        public Button mRetryBtn;
        public RadioButton mShowMeanBtn;
        public Button mStartBtn;
        public Button mStudyBtn;
        public TextView mTotalCountTextView;
        public RelativeLayout mWrapRecognizeLayout;
        public TextView mWrongCountTextView;

        private Controls() {
            this.mEffectBtn = null;
            this.mCradleBtn = null;
            this.mStudyBtn = null;
            this.mDictationBtn = null;
            this.mShowMeanBtn = null;
            this.mListenBtn = null;
            this.mQuestionLayout = null;
            this.mResultLayout = null;
            this.mKeywordPaperLayout = null;
            this.mKeywordScrollView = null;
            this.mInnerMeanPaperLayout = null;
            this.mRestudyCorrectWordButton = null;
            this.mRestudyWrongWordButton = null;
            this.mCorrectCountTextView = null;
            this.mWrongCountTextView = null;
            this.mTotalCountTextView = null;
            this.mResultTotalCountTextView = null;
            this.mResultCorrectWordCountTextView = null;
            this.mResultWrongWordCountTextView = null;
            this.mStartBtn = null;
            this.mRecognizeView = null;
            this.mBoardBgView = null;
            this.mWrapRecognizeLayout = null;
            this.mListenLayout = null;
            this.mDelBtn = null;
            this.mPassBtn = null;
            this.mOkBtn = null;
            this.mChangeFlashcardBtn = null;
            this.mRetryBtn = null;
            this.mMeanContentView = null;
            this.mMeanScrollView = null;
            this.mDictationCorrectImage = null;
            this.mDictationWrongImage = null;
            this.mResultStateImageView = null;
            this.mDictationKeywordPaperRootLayout = null;
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (super.onCreateActivity(savedInstanceState)) {
            float nDensity = CommonUtils.getDeviceDensity(this);
            this.mMeanScrollHeight = (int) (this.mMeanScrollHeight * nDensity);
            if (this.mMeanScrollHeight < 0) {
                this.mMeanScrollHeight = DHWR.DLANG_HINDI;
            }
            if (CommonUtils.is1280H_xhdpiDevice(this)) {
                this.mMeanScrollHeight = 320;
            }
            if (Build.VERSION.SDK_INT < 11) {
                requestWindowFeature(7);
            }
            this.mCurrentMenuId = R.id.menu_flashcard;
            Intent intent = getIntent();
            this.mFolderId = intent.getExtras().getInt(DictInfo.INTENT_WORDBOOKFOLDERID);
            this.mWordCount = intent.getExtras().getInt(DictInfo.INTENT_WORDCOUNT);
            this.mWordbookFolderName = intent.getExtras().getString(DictInfo.INTENT_WORDBOOKNAME);
            if (Dependency.isContainTTS()) {
                setVolumeControlStream(3);
            }
            this.mIsCreate = true;
            getDimens();
            prepareExtraData();
            initActivity();
            initFeedbackEffect();
            pressTopButton(this.mDictationBtn);
        }
    }

    private void getDimens() {
        this.FIRST_CHAR_MAX_WIDTH = getResources().getDimensionPixelSize(R.dimen.first_char_max_width);
        this.FIRST_CHAR_MAX_HEIGHT = getResources().getDimensionPixelSize(R.dimen.first_char_max_height);
        this.SECOND_CHAR_MAX_WIDTH = getResources().getDimensionPixelSize(R.dimen.second_char_max_width);
        this.SECOND_CHAR_MAX_HEIGHT = getResources().getDimensionPixelSize(R.dimen.second_char_max_height);
        this.THIRD_CHAR_MAX_WIDTH = getResources().getDimensionPixelSize(R.dimen.third_char_max_width);
        this.THIRD_CHAR_MAX_HEIGHT = getResources().getDimensionPixelSize(R.dimen.third_char_max_height);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mIsCreate = false;
        if (this.mCurrentOrientation != newConfig.orientation) {
            this.mCurrentOrientation = newConfig.orientation;
            if (newConfig.orientation == 1) {
                if (this.isStartPortrait) {
                    this.switcher.showNext();
                } else {
                    this.switcher.showPrevious();
                }
                swapControls(this.mControlPort);
            } else {
                if (this.isStartPortrait) {
                    this.switcher.showPrevious();
                } else {
                    this.switcher.showNext();
                }
                swapControls(this.mControlLand);
            }
            restoreData();
        }
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        this.mCradleBtn.setText(R.string.cradle);
        this.mStudyBtn.setText(R.string.study);
        this.mDictationBtn.setText(R.string.dictation);
        this.mStartBtn.setText(R.string.start);
        this.mPassBtn.setText(R.string.pass);
        this.mOkBtn.setText(R.string.ok);
        ((TextView) this.mListenLayout.getChildAt(1)).setText(R.string.dictation_top_text);
        ((TextView) findViewById(R.id.CorrectWordTextView)).setText(R.string.study_correctcount);
        ((TextView) findViewById(R.id.CorrectWordTextView_land)).setText(R.string.study_correctcount);
        this.mRestudyCorrectWordButton.setText(R.string.study_retry_correctWord);
        ((TextView) findViewById(R.id.WrongWordTextView)).setText(R.string.study_wrongcount);
        ((TextView) findViewById(R.id.WrongWordTextView_land)).setText(R.string.study_wrongcount);
        this.mRestudyWrongWordButton.setText(R.string.study_retry_wrongWord);
        String totalCount = getResources().getString(R.string.study_totalcount);
        this.mResultTotalCountTextView.setText(totalCount.replaceAll("%d", String.valueOf(this.mWordCount)));
        if (this.mCurrentOrientation != newConfig.orientation) {
            configChangeResultLayout();
        }
        this.mRetryBtn.setText(R.string.study_retry);
        this.mChangeFlashcardBtn.setText(R.string.study_change_flashcard);
        this.mListenBtn = (Button) findViewById(R.id.listenBtn);
        this.mStartBtn = (Button) findViewById(R.id.DictationStartBtn);
        this.mDelBtn = (ImageButton) findViewById(R.id.DelBtn);
        this.mPassBtn = (Button) findViewById(R.id.passBtn);
        this.mOkBtn = (Button) findViewById(R.id.OkBtn);
        this.mRecognizeView = (RecognizeView) findViewById(R.id.recognizeView);
        pressTopButton(this.mDictationBtn);
        reBuildEffectBtn();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onPause() {
        CommonUtils.stopTTS();
        this.mCurrentOrientation = getResources().getConfiguration().orientation;
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        this.mDictationHandler.removeMessages(0);
        this.mDictationHandler.removeMessages(1);
        destroyData();
        memoryInitialize(false);
        closeQuizCursor();
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
        } else {
            setContentView(R.layout.dictation_layout);
            this.switcher = (ViewSwitcher) findViewById(R.id.DictationLandPortSwitcher);
            if (getResources().getConfiguration().orientation == 2) {
                this.switcher.showNext();
                this.isStartPortrait = false;
            }
        }
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        prepareContentLayout();
        restoreData();
    }

    private void restoreData() {
        prepareMeanController(this.mCurrentWordPos);
        if (this.mDictationState == 2) {
            setScoreTextView();
            if (this.mIsPending) {
                this.mDictationHandler.sendEmptyMessageDelayed(1, 300L);
            } else {
                restoreResultLayout();
            }
        } else if (this.mDictationState == 1) {
            showQuestionLayout();
            setEnableQuizBtns(true);
            showListenButton(true);
            restoreNextQuestion(this.mIsPending);
            prepareMeanController(this.mDictactionList.get(this.mCurrentWordPos).intValue());
            restoreShowMean();
        }
    }

    public void prepareExtraData() {
        Bundle extra = getIntent().getExtras();
        this.mFolderId = extra.getInt(DictInfo.INTENT_WORDBOOKFOLDERID);
        prepareQuizCursor();
    }

    public void prepareQuizCursor() {
        ArrayList<Integer> al = DioDictDatabase.getDictTypeListfromCursorAvailTTS(getApplicationContext(), this.mWordbookFolderName, this.mFolderId, this.mSort, true);
        if (al.size() != 0) {
            this.mDicTypes = new Integer[al.size()];
            for (int i = 0; i < al.size(); i++) {
                this.mDicTypes[i] = al.get(i);
            }
            this.mQuizCursor = DioDictDatabase.getItemsCursorByDictType(getApplicationContext(), this.mDicTypes, this.mWordbookFolderName, this.mFolderId, this.mSort);
            if (this.mQuizCursor != null) {
                int count = this.mQuizCursor.getCount();
                this.mMaxQuizWord = count;
                this.mWordCount = count;
            }
        }
    }

    public void prepareContentLayout() {
        initializeDictationList();
        prepareContentTopLayout();
        prepareContentMeanLayout();
        prepareContentBottomLayout();
        prepareContentAnswerLayout();
        setControls(this.mControlPort);
        prepareContentTopLayout_land();
        prepareContentMeanLayout_land();
        prepareContentBottomLayout_land();
        prepareContentAnswerLayout_land();
        setControls(this.mControlLand);
        if (this.isStartPortrait) {
            swapControls(this.mControlPort);
        }
    }

    public void prepareContentTopLayout() {
        this.mEffectBtn = (ImageButton) findViewById(R.id.SoundBtn);
        this.mCradleBtn = (Button) findViewById(R.id.CradleBtn);
        this.mStudyBtn = (Button) findViewById(R.id.StudyBtn);
        this.mDictationBtn = (Button) findViewById(R.id.DictationBtn);
        this.mEffectBtn.setOnClickListener(this.mEffectBtnOnClickListener);
        this.mCradleBtn.setOnClickListener(this.mCradleBtnOnClickListener);
        this.mStudyBtn.setOnClickListener(this.mStudyBtnOnClickListener);
        prepareContentEffectBtn();
    }

    private void prepareContentEffectBtn() {
        this.mEffectMode = DictUtils.getDictationFeedbackModeFromPreference(this);
        reBuildEffectBtn();
    }

    public void prepareContentMeanLayout() {
        this.mMeanContentView = (ExtendTextView) findViewById(R.id.DictationMeanContentView);
        this.mMeanScrollView = (ScrollView) findViewById(R.id.DictationMeanScrollView);
        this.mMeanContentView.setOnFocusChangeListener(this.mMeanScrollViewFocusChangeListener);
        this.mShowMeanBtn = (RadioButton) findViewById(R.id.ShowMeanBtn);
        this.mShowMeanBtn.setOnClickListener(this.mShowMeanBtnOnClickListener);
        if (this.mMeanController == null) {
            this.mMeanController = new CursorMeanController(this, null, this.mMeanContentView, null, null, this.mEngine, this.mThemeModeCallback, null, null, 1);
        } else {
            this.mMeanController.setViews((TextView) null, this.mMeanContentView);
        }
        this.mMeanController.setDicTypeList(this.mDicTypes);
        this.mMeanController.setDisplayMode(1);
        this.mMeanController.setMeanTabMode();
    }

    public void prepareMeanController(int pos) {
        this.mMeanController.setViews((TextView) null, this.mMeanContentView);
        this.mMeanController.setMeanView(DioDictDatabase.getTableName(this.mWordbookFolderName), this.mWordbookFolderName, this.mFolderId, this.mSort, pos, false);
        this.mMeanController.setDisplayMode(1);
        this.mMeanController.setMeanTabMode();
    }

    public void prepareContentBottomLayout() {
        this.mListenLayout = (LinearLayout) findViewById(R.id.ListenLayout);
        this.mQuestionLayout = (RelativeLayout) findViewById(R.id.DictationContentLayout);
        this.mResultLayout = (RelativeLayout) findViewById(R.id.AnswerLayout);
        this.mKeywordPaperLayout = (RelativeLayout) findViewById(R.id.DictationKeywordPaperLayout);
        this.mKeywordScrollView = (ScrollView) findViewById(R.id.DictationKeywordScrollView);
        this.mKeywordScrollView.setOnFocusChangeListener(this.mKeywordScrollViewOnFocusChangeListener);
        this.mInnerMeanPaperLayout = (RelativeLayout) findViewById(R.id.DictationInnerMeanPaperLayout);
        this.mCorrectCountTextView = (TextView) findViewById(R.id.correctCountTextView);
        this.mWrongCountTextView = (TextView) findViewById(R.id.wrongCountTextView);
        this.mTotalCountTextView = (TextView) findViewById(R.id.Dictation_WordCount);
        this.mDictationCorrectImage = (ImageView) findViewById(R.id.DictationCorrectImage);
        this.mDictationWrongImage = (ImageView) findViewById(R.id.DictationWrongImage);
        this.mBoardBgView = findViewById(R.id.boardBgView);
        this.mWrapRecognizeLayout = (RelativeLayout) findViewById(R.id.wrapRecognizeLayout);
        this.mWrapRecognizeLayout.setOnTouchListener(this.mWrapRecognizeLayoutOnTouchListener);
        this.mDictationKeywordPaperRootLayout = (RelativeLayout) findViewById(R.id.DictationKeywordPaperRootLayout);
        this.mListenBtn = (Button) findViewById(R.id.listenBtn);
        this.mStartBtn = (Button) findViewById(R.id.DictationStartBtn);
        this.mDelBtn = (ImageButton) findViewById(R.id.DelBtn);
        this.mPassBtn = (Button) findViewById(R.id.passBtn);
        this.mOkBtn = (Button) findViewById(R.id.OkBtn);
        this.mRecognizeView = (RecognizeView) findViewById(R.id.recognizeView);
        this.mRecognizeView.setResultCabllback(this.mRecognizeResultCallback);
        this.mListenBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mStartBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mDelBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mPassBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mOkBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        if (this.mIsCreate) {
            setEnableQuizBtns(false);
        }
        this.mTotalCountTextView.setText(String.valueOf(this.mWordCount - this.mCurrentWordPos) + "/" + String.valueOf(this.mWordCount));
    }

    public void prepareContentAnswerLayout() {
        this.mResultStateImageView = (ImageView) findViewById(R.id.ResultStateImageView);
        this.mResultTotalCountTextView = (TextView) findViewById(R.id.TotalCountTextView);
        this.mResultCorrectWordCountTextView = (TextView) findViewById(R.id.CorrectWordCountTextView);
        this.mResultWrongWordCountTextView = (TextView) findViewById(R.id.WrongWordCountTextView);
        this.mRestudyCorrectWordButton = (TextView) findViewById(R.id.RestudyCorrectWordBtn);
        this.mRestudyWrongWordButton = (TextView) findViewById(R.id.RestudyWrongWordBtn);
        this.mChangeFlashcardBtn = (Button) findViewById(R.id.ChangeFlashcardBtn);
        this.mRetryBtn = (Button) findViewById(R.id.RetryBtn);
        this.mRestudyCorrectWordButton.setOnClickListener(this.mRestudyCorrectWordBtnOnClickListener);
        this.mRestudyCorrectWordButton.setOnFocusChangeListener(this.mRestudyCorrectWordBtnOnFocusListener);
        this.mRestudyWrongWordButton.setOnClickListener(this.mRestudyWrongWordBtnOnClickListener);
        this.mRestudyWrongWordButton.setOnFocusChangeListener(this.mRestudyWrongWordBtnOnFocusListener);
        this.mChangeFlashcardBtn.setOnClickListener(this.mChangeFlashcardBtnOnClickListener);
        this.mRetryBtn.setOnClickListener(this.mRetryBtnOnClickListener);
    }

    public void prepareKeyword() {
        if (this.mQuizCursor != null && !this.mQuizCursor.isClosed()) {
            this.mTTSWord = this.mQuizCursor.getString(this.mQuizCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME));
            String correctWord = DictUtils.makeCorrectWord(this.mTTSWord);
            this.mCorrectWord = DictUtils.getMakeSearchKeyword(correctWord);
        }
    }

    public void prepareKeywordTextView() {
        initQuizTextView();
        this.mCorrectTextViews = new TextView[this.mCorrectWord.length()];
        for (int i = 0; i < this.mCorrectWord.length(); i++) {
            this.mCorrectTextViews[i] = new TextView(getApplicationContext());
            this.mCorrectTextViews[i].setTypeface(DictUtils.createfont());
            this.mCorrectTextViews[i].setId(DictInfo.mDictationResId + i);
            this.mCorrectTextViews[i].setFocusable(false);
            if (isContainSymbol(i)) {
                setSymbolTextView(this.mCorrectTextViews[i], i);
            } else {
                this.mCorrectTextViews[i].setBackgroundResource(R.drawable.dictation_blank_nor);
            }
            RelativeLayout.LayoutParams params = getCorrectTextViewsLayoutParams(this.mCorrectWord.length(), i);
            this.mCorrectTextViews[i].setLayoutParams(params);
            this.mKeywordPaperLayout.addView(this.mCorrectTextViews[i]);
        }
        int idx = 0;
        while (isContainSymbol(idx)) {
            idx++;
        }
        if (idx < this.mCorrectWord.length()) {
            this.mCorrectTextViews[idx].setBackgroundResource(R.drawable.dictation_blank_sel);
        }
        this.mKeywordPaperLayout.requestLayout();
    }

    private void restoreKeywordTextView() {
        initKeywordPaperLayout();
        this.mCorrectTextViews = new TextView[this.mCorrectWord.length()];
        int inputIndex = 0;
        for (int i = 0; i < this.mCorrectWord.length(); i++) {
            this.mCorrectTextViews[i] = new TextView(getApplicationContext());
            this.mCorrectTextViews[i].setId(DictInfo.mDictationResId + i);
            if (isContainSymbol(i)) {
                setSymbolTextView(this.mCorrectTextViews[i], i);
                RelativeLayout.LayoutParams params = getCorrectTextViewsLayoutParams(this.mCorrectWord.length(), i);
                this.mCorrectTextViews[i].setLayoutParams(params);
                this.mKeywordPaperLayout.addView(this.mCorrectTextViews[i]);
            } else {
                if (this.mInputString != null && inputIndex < this.mInputString.length()) {
                    String input = "" + this.mInputString.charAt(inputIndex);
                    this.mCorrectTextViews[i].setText(input);
                    this.mCorrectTextViews[i].setTextSize(1, getFontSize());
                    this.mCorrectTextViews[i].setTextColor(-16777216);
                    this.mCorrectTextViews[i].setBackgroundColor(0);
                } else if (this.mInputString != null && inputIndex == this.mInputString.length()) {
                    this.mCorrectTextViews[i].setBackgroundResource(R.drawable.dictation_blank_sel);
                } else {
                    this.mCorrectTextViews[i].setBackgroundResource(R.drawable.dictation_blank_nor);
                }
                RelativeLayout.LayoutParams params2 = getCorrectTextViewsLayoutParams(this.mCorrectWord.length(), i);
                this.mCorrectTextViews[i].setLayoutParams(params2);
                this.mKeywordPaperLayout.addView(this.mCorrectTextViews[i]);
                inputIndex++;
            }
        }
        this.mKeywordPaperLayout.requestLayout();
    }

    public void prepareNextQuestion() {
        this.mInputWordPos = 0;
        if (this.mInputString != null && this.mInputString.length() > 0) {
            this.mInputString.delete(0, this.mInputString.length());
        }
        this.mQuizCursor.moveToPosition(this.mDictactionList.get(this.mCurrentWordPos).intValue());
        setScoreTextView();
        prepareKeyword();
        runListenBtn();
        prepareKeywordTextView();
        setRecogInputType();
        this.mMeanController.setMeanViewByPos(this.mDictactionList.get(this.mCurrentWordPos).intValue(), 0);
        this.mOperationMutex = false;
        this.mIsPending = false;
    }

    private void restoreNextQuestion(boolean isPending) {
        setScoreTextView();
        this.mQuizCursor.moveToPosition(this.mDictactionList.get(this.mCurrentWordPos).intValue());
        if (isPending) {
            this.mInputWordPos = 0;
            if (this.mInputString != null && this.mInputString.length() > 0) {
                this.mInputString.delete(0, this.mInputString.length());
            }
        }
        hideCorrectWrongImage();
        prepareKeyword();
        if (isPending) {
            runListenBtn();
            this.mIsPending = false;
        }
        restoreKeywordTextView();
        setRecogInputType();
        this.mOperationMutex = false;
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
        finish();
        Intent intent = new Intent();
        intent.setClass(this, StudyActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
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

    public void runStartBtn() {
        setEnableQuizBtns(true);
        prepareNextQuestion();
    }

    public void runDelBtn() {
        this.mWrapRecognizeLayout.setVisibility(8);
        setCorrectTextView("");
    }

    private void setDictationResult(boolean isCorrect) {
        if (isCorrect) {
            this.mCorrectAnswerCount++;
            this.mDictactionCorrectPosList.add(this.mDictactionList.get(this.mCurrentWordPos));
            return;
        }
        this.mWrongAnswerCount++;
        this.mDictactionWrongPosList.add(this.mDictactionList.get(this.mCurrentWordPos));
    }

    public void runPassBtn() {
        if (!this.mIsAnimating) {
            this.mIsAnimating = true;
            this.mOkBtn.setEnabled(false);
            this.mPassBtn.setEnabled(false);
            setDictationResult(false);
            this.mWrapRecognizeLayout.setVisibility(8);
            dictationNextStep(false);
        }
    }

    private void dictationNextStep(boolean isDelay) {
        int delayTime = isDelay ? 2000 : 1;
        if (this.mCurrentWordPos < this.mDictationTotalWordCount - 1) {
            this.mCurrentWordPos++;
            this.mDictationHandler.sendEmptyMessageDelayed(0, delayTime);
            this.mIsPending = true;
            return;
        }
        setScoreTextView();
        this.mDictationState = 2;
        this.mIsPending = true;
        this.mDictationHandler.sendEmptyMessageDelayed(1, delayTime);
    }

    public void runOkBtn() {
        if (!this.mIsAnimating) {
            this.mIsAnimating = true;
            this.mWrapRecognizeLayout.setVisibility(8);
            this.mOkBtn.setEnabled(false);
            this.mPassBtn.setEnabled(false);
            showCurrentWordDictiationResult();
            dictationNextStep(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runListenBtn() {
        new Thread(this.mRunnablePlayTTS).start();
    }

    public void runShowMeanBtn() {
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) this.mInnerMeanPaperLayout.getLayoutParams();
        if (this.mMeanScrollView.getVisibility() == 0) {
            this.mIsShowMeaning = false;
            this.mMeanScrollView.setVisibility(8);
            param.height = -2;
            this.mShowMeanBtn.setChecked(false);
            this.mShowMeanBtn.setHeight(52);
        } else {
            this.mIsShowMeaning = true;
            this.mMeanScrollView.setVisibility(0);
            param.height = this.mMeanScrollHeight;
            this.mShowMeanBtn.setChecked(true);
            this.mShowMeanBtn.setHeight(72);
        }
        this.mInnerMeanPaperLayout.requestLayout();
    }

    private void restoreShowMean() {
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) this.mInnerMeanPaperLayout.getLayoutParams();
        if (!this.mIsShowMeaning) {
            this.mMeanScrollView.setVisibility(8);
            param.height = -2;
            this.mShowMeanBtn.setChecked(false);
            this.mShowMeanBtn.setHeight(52);
        } else {
            this.mMeanScrollView.setVisibility(0);
            param.height = this.mMeanScrollHeight;
            this.mShowMeanBtn.setChecked(true);
            this.mShowMeanBtn.setHeight(72);
        }
        this.mMeanController.setMeanViewByPos(this.mDictactionList.get(this.mCurrentWordPos).intValue(), 0);
        this.mInnerMeanPaperLayout.requestLayout();
    }

    public void runFlashcardBtn() {
        finish();
        Intent intent = new Intent();
        intent.setClass(this, FlashcardActivity.class);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runFlashCardSelect() {
        finish();
        Intent intent = new Intent();
        intent.putExtra(DictInfo.INTENT_FLASHCARD_ENTERING_MODE, toString());
        intent.setClass(this, FlashcardActivity.class);
        startActivity(intent);
    }

    public void runRetryBtn() {
        this.mCorrectAnswerCount = 0;
        this.mWrongAnswerCount = 0;
        this.mCurrentWordPos = 0;
        this.mIsReDictation = false;
        this.mIsCreate = true;
        initializeDictationList();
        setEnableQuizBtns(true);
        showQuestionLayout();
        this.mQuizCursor.moveToFirst();
        prepareNextQuestion();
    }

    public void closeQuizCursor() {
        if (this.mQuizCursor != null) {
            this.mQuizCursor.close();
        }
    }

    private void calculateDictationResult() {
        int correctCount = this.mDictactionCorrectPosList == null ? 0 : this.mDictactionCorrectPosList.size();
        this.mCorrectWordPercent = (correctCount * 100) / this.mWordCount;
    }

    private void playSoundDictationResult() {
        int soundId = 2;
        if (this.mCorrectWordPercent < 60) {
            soundId = 4;
        } else if (this.mCorrectWordPercent < 90) {
            soundId = 3;
        }
        PlayFeedbackEffect(soundId);
    }

    private void showImageDictationResult() {
        int res = R.drawable.dioboy_excellent;
        if (this.mCorrectWordPercent < 60) {
            res = R.drawable.dioboy_oops;
        } else if (this.mCorrectWordPercent < 90) {
            res = R.drawable.dioboy_good;
        }
        this.mResultStateImageView.setBackgroundResource(res);
        this.mResultStateImageView.invalidate();
    }

    private void showDictationResultFeedback() {
        calculateDictationResult();
        playSoundDictationResult();
        showImageDictationResult();
    }

    private void restoreResultLayout() {
        calculateDictationResult();
        showImageDictationResult();
        showResultLayout();
        showListenButton(false);
    }

    private void showResultLayout() {
        initKeywordPaperLayout();
        this.mQuestionLayout.setVisibility(8);
        this.mResultLayout.setVisibility(0);
        this.mOperationMutex = false;
    }

    public void hideQuestionLayout() {
        this.mDictationState = 2;
        showDictationResultFeedback();
        showListenButton(false);
        showResultLayout();
        this.mIsPending = false;
    }

    public void showQuestionLayout() {
        this.mDictationState = 1;
        showListenButton(true);
        this.mQuestionLayout.setVisibility(0);
        this.mResultLayout.setVisibility(8);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeDictationList() {
        if (this.mDictactionList != null && this.mIsCreate) {
            this.mDictactionList.clear();
            if (this.mIsReDictation) {
                if (this.mIsDictationMode == 1) {
                    for (int i = 0; i < this.mDictactionWrongPosList.size(); i++) {
                        this.mDictactionList.add(this.mDictactionWrongPosList.get(i));
                    }
                    this.mDictactionWrongPosList.clear();
                } else if (this.mIsDictationMode == 2) {
                    for (int i2 = 0; i2 < this.mDictactionCorrectPosList.size(); i2++) {
                        this.mDictactionList.add(this.mDictactionCorrectPosList.get(i2));
                    }
                    this.mDictactionCorrectPosList.clear();
                }
            } else {
                this.mDictactionWrongPosList.clear();
                this.mDictactionCorrectPosList.clear();
                for (int i3 = 0; i3 < this.mWordCount; i3++) {
                    this.mDictactionList.add(Integer.valueOf(i3));
                }
                this.mIsDictationMode = 0;
            }
            this.mDictationTotalWordCount = this.mDictactionList.size();
            this.mCorrectAnswerCount = 0;
            this.mWrongAnswerCount = 0;
            this.mCurrentWordPos = 0;
            this.mQuizCursor.moveToPosition(this.mDictactionList.get(this.mCurrentWordPos).intValue());
        }
    }

    private void showFeedback(boolean isCorrect) {
        if (isCorrect) {
            showCorrectImage();
            PlayFeedbackEffect(0);
            return;
        }
        repareWrongChar();
        showWrongImage();
        PlayFeedbackEffect(1);
    }

    public void showCurrentWordDictiationResult() {
        String userInputWord = "";
        for (int i = 0; i < this.mCorrectWord.length(); i++) {
            userInputWord = userInputWord.concat((String) this.mCorrectTextViews[i].getText());
        }
        if (userInputWord == null || userInputWord.equals("")) {
            setDictationResult(false);
            showFeedback(false);
            return;
        }
        boolean bSame = compareCorrectKeyword(userInputWord);
        if (bSame) {
            setDictationResult(true);
            showFeedback(true);
            return;
        }
        setDictationResult(false);
        showFeedback(false);
    }

    public void initQuizTextView() {
        hideCorrectWrongImage();
        initKeywordPaperLayout();
        this.mInputWordPos = 0;
    }

    private void memoryInitialize(boolean isconfigChange) {
        initKeywordPaperLayout();
        if (this.mDictationCorrectImage != null) {
            UITools.recycleDrawable(this.mDictationCorrectImage.getBackground(), false, isconfigChange);
            this.mDictationCorrectImage = null;
        }
        if (this.mDictationWrongImage != null) {
            UITools.recycleDrawable(this.mDictationWrongImage.getBackground(), false, isconfigChange);
            this.mDictationWrongImage = null;
        }
        if (this.mWrapRecognizeLayout != null) {
            UITools.recycleDrawable(this.mWrapRecognizeLayout.getBackground(), false, isconfigChange);
            this.mWrapRecognizeLayout = null;
        }
        if (this.mBoardBgView != null) {
            UITools.recycleDrawable(this.mBoardBgView.getBackground(), false, isconfigChange);
            this.mBoardBgView = null;
        }
        if (this.mRecognizeView != null) {
            this.mRecognizeView.onDestroy();
            this.mRecognizeView = null;
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

    private void initKeywordPaperLayout() {
        if (this.mCorrectTextViews != null) {
            int cnt = this.mCorrectTextViews.length;
            for (int i = 0; i < cnt; i++) {
                UITools.recycleDrawable(this.mCorrectTextViews[i].getBackground(), false, false);
                this.mCorrectTextViews[i] = null;
            }
            this.mCorrectTextViews = null;
        }
        if (this.mKeywordPaperLayout != null) {
            int count = this.mKeywordPaperLayout.getChildCount();
            for (int i2 = 0; i2 < count; i2++) {
                Drawable bg = this.mKeywordPaperLayout.getChildAt(i2).getBackground();
                if (bg != null) {
                    bg.setCallback(null);
                }
            }
            this.mKeywordPaperLayout.removeAllViews();
            this.mKeywordScrollView.scrollTo(0, 0);
            System.gc();
        }
    }

    private RelativeLayout.LayoutParams getCorrectTextViewsLayoutParams(int nTotalCount, int nPos) {
        RelativeLayout.LayoutParams params;
        int maxCharCount = this.FIRST_CHAR_MAX_COUNT;
        int maxLineCharCount = this.FIRST_ONELINE_CHAR_COUNT;
        if (nTotalCount < this.FIRST_CHAR_MAX_COUNT) {
            this.mAnswerLines = 1;
            params = new RelativeLayout.LayoutParams(this.FIRST_CHAR_MAX_WIDTH, this.FIRST_CHAR_MAX_HEIGHT);
        } else if (nTotalCount < this.SECOND_CHAR_MAX_COUNT) {
            this.mAnswerLines = 2;
            maxCharCount = this.SECOND_CHAR_MAX_COUNT;
            maxLineCharCount = this.SECOND_ONELINE_CHAR_COUNT;
            params = new RelativeLayout.LayoutParams(this.SECOND_CHAR_MAX_WIDTH, this.SECOND_CHAR_MAX_HEIGHT);
        } else if (nTotalCount < this.THIRD_CHAR_MAX_COUNT) {
            this.mAnswerLines = 3;
            maxCharCount = this.THIRD_CHAR_MAX_COUNT;
            maxLineCharCount = this.THIRD_ONELINE_CHAR_COUNT;
            params = new RelativeLayout.LayoutParams(this.THIRD_CHAR_MAX_WIDTH, this.THIRD_CHAR_MAX_HEIGHT);
        } else {
            this.mAnswerLines = 3;
            maxCharCount = nTotalCount;
            maxLineCharCount = this.THIRD_ONELINE_CHAR_COUNT;
            params = new RelativeLayout.LayoutParams(this.THIRD_CHAR_MAX_WIDTH, this.THIRD_CHAR_MAX_HEIGHT);
        }
        int nTopMargin = 5;
        if (nPos == 0) {
            params.addRule(9);
            params.setMargins(0, 5, 0, 0);
        } else {
            int nLeftMargin = 4;
            if (nTotalCount <= maxCharCount) {
                if (nPos >= maxLineCharCount) {
                    nTopMargin = 5;
                    if (nPos % maxLineCharCount == 0) {
                        nLeftMargin = 0;
                        params.addRule(5, DictInfo.mDictationResId + (nPos - maxLineCharCount));
                    } else {
                        params.addRule(1, DictInfo.mDictationResId + (nPos - 1));
                    }
                    params.addRule(3, DictInfo.mDictationResId + (nPos - maxLineCharCount));
                } else {
                    params.addRule(1, DictInfo.mDictationResId + (nPos - 1));
                }
            }
            params.setMargins(nLeftMargin, nTopMargin, 0, 0);
        }
        return params;
    }

    private void repareWrongChar() {
        for (int i = 0; i < this.mCorrectWord.length(); i++) {
            TextView tv = this.mCorrectTextViews[i];
            if (tv.getText().equals("")) {
                String character = this.mCorrectWord.substring(i, i + 1);
                this.mCorrectTextViews[i].setText(character);
                this.mCorrectTextViews[i].setTextSize(1, getFontSize());
                this.mCorrectTextViews[i].setTextColor(-65536);
                this.mCorrectTextViews[i].setBackgroundColor(0);
            } else if (tv.getText().charAt(0) != this.mCorrectWord.charAt(i)) {
                String character2 = this.mCorrectWord.substring(i, i + 1);
                this.mCorrectTextViews[i].setText(character2);
                this.mCorrectTextViews[i].setTextSize(1, getFontSize());
                this.mCorrectTextViews[i].setTextColor(-65536);
                this.mCorrectTextViews[i].setBackgroundColor(0);
            } else {
                String character3 = this.mCorrectWord.substring(i, i + 1);
                this.mCorrectTextViews[i].setText(character3);
                this.mCorrectTextViews[i].setTextSize(1, getFontSize());
                this.mCorrectTextViews[i].setTextColor(-16777216);
                this.mCorrectTextViews[i].setBackgroundColor(0);
            }
        }
    }

    private boolean compareCorrectKeyword(String answerWord) {
        boolean bRet = this.mCorrectWord.equalsIgnoreCase(answerWord);
        return bRet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCorrectTextView(String result) {
        int nInputWordCurLine;
        int nInputWordCurLine2;
        if (this.mCorrectTextViews == null || result == null) {
            if (this.mRecognizeView != null) {
                this.mRecognizeView.Clear();
                return;
            }
            return;
        }
        if (result.equals("")) {
            while (isContainSymbol(this.mInputWordPos - 1) && this.mInputWordPos > 0) {
                this.mInputWordPos--;
            }
            if (this.mInputWordPos > 0) {
                nInputWordCurLine = (this.mInputWordPos / this.THIRD_ONELINE_CHAR_COUNT) + 1;
                this.mInputWordPos--;
                nInputWordCurLine2 = (this.mInputWordPos / this.THIRD_ONELINE_CHAR_COUNT) + 1;
                if (this.mCorrectTextViews.length > this.mInputWordPos) {
                    this.mCorrectTextViews[this.mInputWordPos].setText(result);
                    this.mCorrectTextViews[this.mInputWordPos].setBackgroundResource(R.drawable.dictation_blank_sel);
                    int idx = 1;
                    while (true) {
                        if (this.mInputWordPos + idx >= this.mCorrectTextViews.length) {
                            break;
                        } else if (!isContainSymbol(this.mInputWordPos + idx)) {
                            this.mCorrectTextViews[this.mInputWordPos + idx].setBackgroundResource(R.drawable.dictation_blank_nor);
                            break;
                        } else {
                            idx++;
                        }
                    }
                    if (this.mInputString != null) {
                        this.mInputString.deleteCharAt(this.mInputString.length() - 1);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            while (isContainSymbol(this.mInputWordPos) && this.mCorrectTextViews.length > this.mInputWordPos) {
                this.mInputWordPos++;
            }
            if (this.mCorrectTextViews.length > this.mInputWordPos) {
                this.mCorrectTextViews[this.mInputWordPos].setText(result);
                this.mCorrectTextViews[this.mInputWordPos].setTextSize(1, getFontSize());
                this.mCorrectTextViews[this.mInputWordPos].setTextColor(-16777216);
                this.mCorrectTextViews[this.mInputWordPos].setBackgroundColor(0);
                while (true) {
                    if (this.mInputWordPos + 1 >= this.mCorrectTextViews.length) {
                        break;
                    } else if (!isContainSymbol(this.mInputWordPos + 1)) {
                        this.mCorrectTextViews[this.mInputWordPos + 1].setBackgroundResource(R.drawable.dictation_blank_sel);
                        break;
                    } else {
                        this.mInputWordPos++;
                    }
                }
                nInputWordCurLine = (this.mInputWordPos / this.THIRD_ONELINE_CHAR_COUNT) + 1;
                this.mInputWordPos++;
                nInputWordCurLine2 = (this.mInputWordPos / this.THIRD_ONELINE_CHAR_COUNT) + 1;
                if (this.mInputString != null) {
                    this.mInputString.append(result);
                }
            } else {
                return;
            }
        }
        if (this.mInputWordPos >= this.THIRD_CHAR_MAX_COUNT - 1 && this.mKeywordScrollView != null) {
            if (this.mInputWordPos == this.THIRD_CHAR_MAX_COUNT + (this.THIRD_ONELINE_CHAR_COUNT * (nInputWordCurLine - this.SCROLLVIEW_MAX_LINE)) && nInputWordCurLine < nInputWordCurLine2) {
                this.mKeywordScrollView.scrollBy(0, this.THIRD_CHAR_MAX_HEIGHT + 5);
            } else if (this.mInputWordPos == (this.THIRD_CHAR_MAX_COUNT + (this.THIRD_ONELINE_CHAR_COUNT * (nInputWordCurLine2 - this.SCROLLVIEW_MAX_LINE))) - 1 && nInputWordCurLine > nInputWordCurLine2) {
                this.mKeywordScrollView.scrollBy(0, -(this.THIRD_CHAR_MAX_HEIGHT + 5));
            }
        }
        if (this.mInputWordPos >= this.mCorrectWord.length() && this.mWrapRecognizeLayout != null) {
            this.mWrapRecognizeLayout.setVisibility(0);
        }
        setRecogInputType();
    }

    private void setScoreTextView() {
        String totalCount = getResources().getString(R.string.study_totalcount);
        this.mResultTotalCountTextView.setText(totalCount.replaceAll("%d", String.valueOf(this.mWordCount)));
        int correctWordCount = this.mDictactionCorrectPosList == null ? 0 : this.mDictactionCorrectPosList.size();
        int wrongWordCount = this.mDictactionWrongPosList == null ? 0 : this.mDictactionWrongPosList.size();
        this.mCorrectCountTextView.setText(Integer.toString(this.mCorrectAnswerCount));
        this.mWrongCountTextView.setText(Integer.toString(this.mWrongAnswerCount));
        this.mResultCorrectWordCountTextView.setText(String.valueOf(correctWordCount));
        this.mResultWrongWordCountTextView.setText(String.valueOf(wrongWordCount));
        this.mTotalCountTextView.setText(String.valueOf(this.mDictationTotalWordCount - this.mCurrentWordPos) + "/" + String.valueOf(this.mDictationTotalWordCount));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnableQuizBtns(boolean bEnable) {
        if (bEnable) {
            this.mStartBtn.setVisibility(8);
        } else {
            this.mStartBtn.setVisibility(0);
        }
        if (bEnable) {
            this.mDictationState = 1;
            this.mShowMeanBtn.setVisibility(0);
            this.mRecognizeView.setVisibility(0);
            this.mWrapRecognizeLayout.setVisibility(8);
        } else {
            this.mDictationState = 0;
            this.mShowMeanBtn.setVisibility(8);
            this.mRecognizeView.setVisibility(8);
            this.mWrapRecognizeLayout.setVisibility(0);
        }
        this.mListenBtn.setEnabled(bEnable);
        this.mInnerMeanPaperLayout.setEnabled(bEnable);
        this.mDelBtn.setEnabled(bEnable);
        this.mPassBtn.setEnabled(bEnable);
        this.mOkBtn.setEnabled(bEnable);
        this.mShowMeanBtn.setEnabled(bEnable);
        this.mListenBtn.setFocusable(bEnable);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001d, code lost:
        if (r1 >= r5.mCorrectWord.length()) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001f, code lost:
        r2 = "" + r5.mCorrectWord.charAt(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003c, code lost:
        if (com.diotek.diodict.engine.DictUtils.isFirstNumber(r2) == false) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003e, code lost:
        r5.mRecognizeView.SetRecogMode(6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0044, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0045, code lost:
        r0 = com.diotek.diodict.engine.DictUtils.getCodePage(r2);
        setRecogInputTypeWithCodePage(r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void setRecogInputType() {
        // throw new UnsupportedOperationException("Method not decompiled: com.diotek.diodict.DictationActivity.setRecogInputType():void");
		System.out.println("fatal setRecogInputType!"+mInputWordPos);
//		int correctWordPos = this.mInputWordPos;
//		while (isContainSymbol(correctWordPos) && correctWordPos >= 0 && correctWordPos < this.mCorrectWord.length()) {
//			correctWordPos++;
//		}
		// 追加???
		//this.mInputWordPos = correctWordPos;
		String str;
		int i;
		for (i = this.mInputWordPos; isContainSymbol(i) && i >= 0 && i < this.mCorrectWord.length(); i++);
		if (i >= 0 && i < this.mCorrectWord.length()) {
			str = "" + this.mCorrectWord.charAt(i);
			if (DictUtils.isFirstNumber(str)) {
				this.mRecognizeView.SetRecogMode(6);
				return;
			}
		} else {
			return;
		}
		setRecogInputTypeWithCodePage(DictUtils.getCodePage(str), str);
	}

    private void setRecogInputTypeWithCodePage(int codepage, String word) {
        if (codepage != -1) {
            switch (codepage) {
                case 0:
                case DictInfo.CP_1250 /* 1250 */:
                case DictInfo.CP_LT1 /* 1252 */:
                case DictInfo.CP_TUR /* 1254 */:
                case DictInfo.CP_BAL /* 1257 */:
                case DictInfo.CP_CRL /* 21866 */:
                    this.mRecognizeView.SetRecogMode(0);
                    return;
                case DictInfo.CP_JPN /* 932 */:
                    if (word != null && word.length() > 0) {
                        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(word.charAt(0));
                        if (Character.UnicodeBlock.HIRAGANA.equals(unicodeBlock)) {
                            this.mRecognizeView.SetRecogMode(2);
                            return;
                        } else if (Character.UnicodeBlock.KATAKANA.equals(unicodeBlock) || Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(unicodeBlock)) {
                            this.mRecognizeView.SetRecogMode(3);
                            return;
                        } else {
                            return;
                        }
                    }
                    this.mRecognizeView.SetRecogMode(2);
                    return;
                case DictInfo.CP_CHN /* 936 */:
                    this.mRecognizeView.SetRecogMode(1);
                    return;
                case DictInfo.CP_KOR /* 949 */:
                    this.mRecognizeView.SetRecogMode(4);
                    return;
                default:
                    this.mRecognizeView.SetRecogMode(0);
                    return;
            }
        }
    }

    private void showCorrectImage() {
        this.mDictationCorrectImage.setVisibility(0);
    }

    private void showWrongImage() {
        this.mDictationWrongImage.setVisibility(0);
    }

    private void hideCorrectWrongImage() {
        this.mDictationCorrectImage.setVisibility(8);
        this.mDictationWrongImage.setVisibility(8);
    }

    private void showListenButton(boolean isShow) {
        if (isShow) {
            this.mListenLayout.setVisibility(0);
        } else {
            this.mListenLayout.setVisibility(4);
        }
    }

    private void initFeedbackEffect() {
        initVibrator();
        initSounds();
    }

    private void initSounds() {
        if (DictUtils.mSoundPool == null) {
            DictUtils.mSoundPool = new SoundPool(6, 1, 0);
        }
        if (DictUtils.mSoundPoolMap == null) {
            DictUtils.mSoundPoolMap = new HashMap<>();
            DictUtils.mSoundPoolMap.put(0, Integer.valueOf(DictUtils.mSoundPool.load(getApplicationContext(), R.raw.correct, 1)));
            DictUtils.mSoundPoolMap.put(1, Integer.valueOf(DictUtils.mSoundPool.load(getApplicationContext(), R.raw.wrong, 1)));
            DictUtils.mSoundPoolMap.put(2, Integer.valueOf(DictUtils.mSoundPool.load(getApplicationContext(), R.raw.result_excellent, 1)));
            DictUtils.mSoundPoolMap.put(3, Integer.valueOf(DictUtils.mSoundPool.load(getApplicationContext(), R.raw.result_good, 1)));
            DictUtils.mSoundPoolMap.put(4, Integer.valueOf(DictUtils.mSoundPool.load(getApplicationContext(), R.raw.result_oops, 1)));
            DictUtils.mSoundPoolMap.put(5, Integer.valueOf(DictUtils.mSoundPool.load(getApplicationContext(), R.raw.question, 1)));
        }
        if (DictUtils.mAudioManager == null) {
            DictUtils.mAudioManager = (AudioManager) getSystemService("audio");
        }
    }

    private void playSound(int sound) {
        if (DictUtils.mAudioManager != null && DictUtils.mSoundPool != null) {
            int systemVol = DictUtils.mAudioManager.getStreamVolume(3);
            int systemMaxVol = DictUtils.mAudioManager.getStreamMaxVolume(3);
            float streamVolume = (systemVol / systemMaxVol) / 4.0f;
            DictUtils.mSoundPool.play(DictUtils.mSoundPoolMap.get(Integer.valueOf(sound)).intValue(), streamVolume, streamVolume, 1, 0, 1.0f);
        }
    }

    private void initVibrator() {
        if (this.mVibrator == null) {
            this.mVibrator = (Vibrator) getSystemService("vibrator");
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

    private void PlayFeedbackEffect(int sound) {
        if (this.mEffectMode == 0) {
            playSound(sound);
        } else if (this.mEffectMode == 1) {
            playVibration(sound);
        }
    }

    private int getFontSize() {
        switch (this.mAnswerLines) {
            case 2:
                return 20;
            case 3:
                return 15;
            default:
                return 27;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnableBottomBtns() {
        if (this.mIsAnimating) {
            this.mIsAnimating = false;
            this.mOkBtn.setEnabled(true);
            this.mPassBtn.setEnabled(true);
        }
    }

    private void swapControls(Controls control) {
        this.mEffectBtn = control.mEffectBtn;
        this.mCradleBtn = control.mCradleBtn;
        this.mStudyBtn = control.mStudyBtn;
        this.mDictationBtn = control.mDictationBtn;
        this.mShowMeanBtn = control.mShowMeanBtn;
        this.mListenBtn = control.mListenBtn;
        this.mQuestionLayout = control.mQuestionLayout;
        this.mResultLayout = control.mResultLayout;
        this.mKeywordPaperLayout = control.mKeywordPaperLayout;
        this.mKeywordScrollView = control.mKeywordScrollView;
        this.mInnerMeanPaperLayout = control.mInnerMeanPaperLayout;
        this.mRestudyCorrectWordButton = control.mRestudyCorrectWordButton;
        this.mRestudyWrongWordButton = control.mRestudyWrongWordButton;
        this.mCorrectCountTextView = control.mCorrectCountTextView;
        this.mWrongCountTextView = control.mWrongCountTextView;
        this.mTotalCountTextView = control.mTotalCountTextView;
        this.mResultTotalCountTextView = control.mResultTotalCountTextView;
        this.mResultCorrectWordCountTextView = control.mResultCorrectWordCountTextView;
        this.mResultWrongWordCountTextView = control.mResultWrongWordCountTextView;
        this.mStartBtn = control.mStartBtn;
        this.mRecognizeView = control.mRecognizeView;
        this.mBoardBgView = control.mBoardBgView;
        this.mWrapRecognizeLayout = control.mWrapRecognizeLayout;
        this.mListenLayout = control.mListenLayout;
        this.mDelBtn = control.mDelBtn;
        this.mPassBtn = control.mPassBtn;
        this.mOkBtn = control.mOkBtn;
        this.mChangeFlashcardBtn = control.mChangeFlashcardBtn;
        this.mRetryBtn = control.mRetryBtn;
        this.mMeanContentView = control.mMeanContentView;
        this.mMeanScrollView = control.mMeanScrollView;
        this.mDictationCorrectImage = control.mDictationCorrectImage;
        this.mDictationWrongImage = control.mDictationWrongImage;
        this.mResultStateImageView = control.mResultStateImageView;
        this.mDictationKeywordPaperRootLayout = control.mDictationKeywordPaperRootLayout;
    }

    private void setControls(Controls control) {
        control.mEffectBtn = this.mEffectBtn;
        control.mCradleBtn = this.mCradleBtn;
        control.mStudyBtn = this.mStudyBtn;
        control.mDictationBtn = this.mDictationBtn;
        control.mShowMeanBtn = this.mShowMeanBtn;
        control.mListenBtn = this.mListenBtn;
        control.mQuestionLayout = this.mQuestionLayout;
        control.mResultLayout = this.mResultLayout;
        control.mKeywordPaperLayout = this.mKeywordPaperLayout;
        control.mKeywordScrollView = this.mKeywordScrollView;
        control.mInnerMeanPaperLayout = this.mInnerMeanPaperLayout;
        control.mRestudyCorrectWordButton = this.mRestudyCorrectWordButton;
        control.mRestudyWrongWordButton = this.mRestudyWrongWordButton;
        control.mCorrectCountTextView = this.mCorrectCountTextView;
        control.mWrongCountTextView = this.mWrongCountTextView;
        control.mTotalCountTextView = this.mTotalCountTextView;
        control.mResultTotalCountTextView = this.mResultTotalCountTextView;
        control.mResultCorrectWordCountTextView = this.mResultCorrectWordCountTextView;
        control.mResultWrongWordCountTextView = this.mResultWrongWordCountTextView;
        control.mStartBtn = this.mStartBtn;
        control.mRecognizeView = this.mRecognizeView;
        control.mBoardBgView = this.mBoardBgView;
        control.mWrapRecognizeLayout = this.mWrapRecognizeLayout;
        control.mListenLayout = this.mListenLayout;
        control.mDelBtn = this.mDelBtn;
        control.mPassBtn = this.mPassBtn;
        control.mOkBtn = this.mOkBtn;
        control.mChangeFlashcardBtn = this.mChangeFlashcardBtn;
        control.mRetryBtn = this.mRetryBtn;
        control.mMeanContentView = this.mMeanContentView;
        control.mMeanScrollView = this.mMeanScrollView;
        control.mDictationCorrectImage = this.mDictationCorrectImage;
        control.mDictationWrongImage = this.mDictationWrongImage;
        control.mResultStateImageView = this.mResultStateImageView;
        control.mDictationKeywordPaperRootLayout = this.mDictationKeywordPaperRootLayout;
    }

    public void prepareContentTopLayout_land() {
        this.mEffectBtn = (ImageButton) findViewById(R.id.SoundBtn_land);
        this.mCradleBtn = (Button) findViewById(R.id.CradleBtn_land);
        this.mStudyBtn = (Button) findViewById(R.id.StudyBtn_land);
        this.mDictationBtn = (Button) findViewById(R.id.DictationBtn_land);
        this.mEffectBtn.setOnClickListener(this.mEffectBtnOnClickListener);
        this.mCradleBtn.setOnClickListener(this.mCradleBtnOnClickListener);
        this.mStudyBtn.setOnClickListener(this.mStudyBtnOnClickListener);
        prepareContentEffectBtn();
    }

    public void prepareContentMeanLayout_land() {
        this.mMeanContentView = (ExtendTextView) findViewById(R.id.DictationMeanContentView_land);
        this.mMeanScrollView = (ScrollView) findViewById(R.id.DictationMeanScrollView_land);
        this.mShowMeanBtn = (RadioButton) findViewById(R.id.ShowMeanBtn_land);
        this.mShowMeanBtn.setOnClickListener(this.mShowMeanBtnOnClickListener);
        if (this.mMeanController == null) {
            this.mMeanController = new CursorMeanController(this, null, this.mMeanContentView, null, null, this.mEngine, this.mThemeModeCallback, null, null, 1);
        } else {
            this.mMeanController.setViews((TextView) null, this.mMeanContentView);
        }
        this.mMeanController.setDicTypeList(this.mDicTypes);
    }

    public void prepareContentBottomLayout_land() {
        this.mListenLayout = (LinearLayout) findViewById(R.id.ListenLayout_land);
        this.mQuestionLayout = (RelativeLayout) findViewById(R.id.DictationContentLayout_land);
        this.mResultLayout = (RelativeLayout) findViewById(R.id.AnswerLayout_land);
        this.mKeywordPaperLayout = (RelativeLayout) findViewById(R.id.DictationKeywordPaperLayout_land);
        this.mKeywordScrollView = (ScrollView) findViewById(R.id.DictationKeywordScrollView_land);
        this.mKeywordScrollView.setOnFocusChangeListener(this.mKeywordScrollViewOnFocusChangeListener);
        this.mInnerMeanPaperLayout = (RelativeLayout) findViewById(R.id.DictationInnerMeanPaperLayout_land);
        this.mCorrectCountTextView = (TextView) findViewById(R.id.correctCountTextView_land);
        this.mWrongCountTextView = (TextView) findViewById(R.id.wrongCountTextView_land);
        this.mTotalCountTextView = (TextView) findViewById(R.id.Dictation_WordCount_land);
        this.mDictationCorrectImage = (ImageView) findViewById(R.id.DictationCorrectImage_land);
        this.mDictationWrongImage = (ImageView) findViewById(R.id.DictationWrongImage_land);
        this.mBoardBgView = findViewById(R.id.boardBgView_land);
        this.mWrapRecognizeLayout = (RelativeLayout) findViewById(R.id.wrapRecognizeLayout_land);
        this.mWrapRecognizeLayout.setOnTouchListener(this.mWrapRecognizeLayoutOnTouchListener);
        this.mDictationKeywordPaperRootLayout = (RelativeLayout) findViewById(R.id.DictationKeywordPaperRootLayout_land);
        this.mListenBtn = (Button) findViewById(R.id.listenBtn_land);
        this.mStartBtn = (Button) findViewById(R.id.DictationStartBtn_land);
        this.mDelBtn = (ImageButton) findViewById(R.id.DelBtn_land);
        this.mPassBtn = (Button) findViewById(R.id.passBtn_land);
        this.mOkBtn = (Button) findViewById(R.id.OkBtn_land);
        this.mRecognizeView = (RecognizeView) findViewById(R.id.recognizeView_land);
        this.mRecognizeView.setResultCabllback(this.mRecognizeResultCallback);
        this.mListenBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mStartBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mDelBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mPassBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        this.mOkBtn.setOnClickListener(this.mOperationBtnOnClickListener);
        if (this.mIsCreate) {
            setEnableQuizBtns(false);
        }
        this.mTotalCountTextView.setText(String.valueOf(this.mWordCount - this.mCurrentWordPos) + "/" + String.valueOf(this.mWordCount));
    }

    public void prepareContentAnswerLayout_land() {
        this.mResultStateImageView = (ImageView) findViewById(R.id.ResultStateImageView_land);
        this.mResultTotalCountTextView = (TextView) findViewById(R.id.TotalCountTextView_land);
        this.mResultCorrectWordCountTextView = (TextView) findViewById(R.id.CorrectWordCountTextView_land);
        this.mResultWrongWordCountTextView = (TextView) findViewById(R.id.WrongWordCountTextView_land);
        this.mRestudyCorrectWordButton = (TextView) findViewById(R.id.RestudyCorrectWordBtn_land);
        this.mRestudyWrongWordButton = (TextView) findViewById(R.id.RestudyWrongWordBtn_land);
        this.mChangeFlashcardBtn = (Button) findViewById(R.id.ChangeFlashcardBtn_land);
        this.mRetryBtn = (Button) findViewById(R.id.RetryBtn_land);
        this.mRestudyCorrectWordButton.setOnClickListener(this.mRestudyCorrectWordBtnOnClickListener);
        this.mRestudyCorrectWordButton.setOnFocusChangeListener(this.mRestudyCorrectWordBtnOnFocusListener);
        this.mRestudyWrongWordButton.setOnClickListener(this.mRestudyWrongWordBtnOnClickListener);
        this.mRestudyWrongWordButton.setOnFocusChangeListener(this.mRestudyWrongWordBtnOnFocusListener);
        this.mChangeFlashcardBtn.setOnClickListener(this.mChangeFlashcardBtnOnClickListener);
        this.mRetryBtn.setOnClickListener(this.mRetryBtnOnClickListener);
    }

    public void configChangeResultLayout() {
        LinearLayout resultcorrectWordLayout;
        LinearLayout resultwrongWordLayout;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.resultwordlayout_height));
        if (this.mCurrentOrientation == 1) {
            resultcorrectWordLayout = (LinearLayout) findViewById(R.id.ResultcorrectWordLayout);
            resultwrongWordLayout = (LinearLayout) findViewById(R.id.ResultwrongWordLayout);
        } else {
            resultcorrectWordLayout = (LinearLayout) findViewById(R.id.ResultcorrectWordLayout_land);
            resultwrongWordLayout = (LinearLayout) findViewById(R.id.ResultwrongWordLayout_land);
        }
        if (resultcorrectWordLayout != null) {
            resultcorrectWordLayout.setLayoutParams(params);
        }
        if (resultwrongWordLayout != null) {
            resultwrongWordLayout.setLayoutParams(params);
        }
    }

    private TextView setSymbolTextView(TextView symbolView, int viewIndex) {
        if (symbolView == null) {
            return null;
        }
        Character symbol = ' ';
        if (this.mCorrectWord != null && this.mCorrectWord.length() > viewIndex && this.mCorrectWord.charAt(viewIndex) != 0) {
            symbol = Character.valueOf(this.mCorrectWord.charAt(viewIndex));
        }
        symbolView.setText(symbol.toString());
        symbolView.setTextSize(1, getFontSize());
        symbolView.setTextColor(-16777216);
        symbolView.setBackgroundColor(0);
        return symbolView;
    }

    private boolean isContainSymbol(int symbolIndex) {
        return this.mCorrectWord != null && this.mCorrectWord.length() > symbolIndex && symbolIndex >= 0 && !DictUtils.isWordSeperator(this.mCorrectWord.charAt(symbolIndex));
    }
}
