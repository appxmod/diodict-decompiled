package com.diotek.diodict;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.diotek.diodict.anim.LayoutTransition;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.dhwr.b2c.kor.DioGuestureDetector;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictType;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.TTSEngine;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.ExtendScrollView;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.FileLinkTagViewManager;
import com.diotek.diodict.mean.HyperSimpleViewModule;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.mean.SearchMeanController;
import com.diotek.diodict.uitool.CandidateBox;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.CustomPopupLinearLayout;
import com.diotek.diodict.uitool.DictEditText;
import com.diotek.diodict.uitool.PageGridView;
import com.diotek.diodict.uitool.PopupFlashcardGridAdapter;
import com.diotek.diodict.uitool.TabView;
import com.diotek.diodict.uitool.TextImageButton;
import com.diotek.diodict.uitool.UITools;
import com.diotek.diodict.uitool.WordListAdapter;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.utils.F1ag;
import com.diotek.diodict.utils.GlobalOptions;
import com.diotek.diodict3.phone.DioAuthActivity;
import com.diodict.decompiled.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class SearchListActivity extends ListMeanViewActivity {
    static final int COUNT_PER_PAGE = 6;
    private static final String DEFAULT_SEARCH_WORD = "";
    private static final int DIALOG_COPY = 1;
    private static final int DIALOG_MAKE = 0;
    private static final int HANDLER_MSG_COPY = 1;
    private static final int ITEM_COPY_COMPLETE = 2;
    private static final int ITEM_COPY_IDLE = 0;
    private static final int ITEM_COPY_RUNNING = 1;
    private static final int ITEM_EXIST = 1;
    private static final int ITEM_SAVED = 0;
    private static final int SAVEDIALOG_POST_DELAYTIME = 1000;
    static final int SEARCH_TIME_DELAY = 500;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private GridView mFlashcardGridView;
    private GestureOverlayView mGestures;
    private DioGuestureDetector mGuestureDetector;
    private boolean mIsGesture;
    private GestureLibrary mLibrary;
    private String mOldResultKeyword;
    private int mOldResultPos;
    ArrayList<HashMap<String, Object>> mSearchListViewItems;
    private int mTotalSearchVoiceLanguage;
    private static int mItemCopyState = 0;
    private static boolean runCallBackTTSLayout = false;
    private static HashMap<String, String> halfToWholeMarkMap = new HashMap<>();
    private static HashMap<String, String> regularMarkMap = new HashMap<>();
    WordListAdapter mSearchListViewAdapter = null;
    WordListAdapter mSearchListViewTotalAdapter = null;
    SimpleAdapter mFlashcardFolderListViewAdapter = null;
    ArrayList<HashMap<String, Object>> mChangeDictPopupItems = new ArrayList<>();
    ArrayList<HashMap<String, Object>> mFlashcardFolderListViewItems = new ArrayList<>();
    ArrayList<String> mVoiceRecognitionLanguageItems = new ArrayList<>();
    private String mPreLastWordFromEngine = null;
    private String mInputWordbookName = null;
    SearchMeanController mSearchMeanController = null;
    private RelativeLayout mAddWordbookTextView = null;
    private ImageButton mChangeDictionaryBtn = null;
    private ImageButton mChangeLanguageBtn = null;
    private ImageButton mClearBtn = null;
    private ImageButton mSearchWordBtn = null;
    private ImageButton mVoiceSearchBtn = null;
    private RadioButton mCard1 = null;
    private RadioButton mCard2 = null;
    private RadioButton mCard3 = null;
    private ImageButton mMarkerBtn = null;
    private ImageButton mFontBtn = null;
    private ImageButton mMemoBtn = null;
    private ImageButton mSaveBtn = null;
    private ListView mSearchListView = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardOk = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardCancel = null;
    private TextImageButton mBtnMakeWordbookOk = null;
    private TextImageButton mBtnMakeWordbookCancel = null;
    private CheckBox mFlashcardItemEditCopyToFlashcardCheckBox = null;
    private RelativeLayout mCopyToFlashcardLayout = null;
    private RelativeLayout mMainEmptyLayout = null;
    private LinearLayout mSearchListEmptyLayout = null;
    private TextView mEmptyTitle = null;
    private TextView mEmptyInfo = null;
    private View mMeanContentBottomView = null;
    RelativeLayout mChangeDictPopupLayout = null;
    PageGridView mChangeDictPopupGridView = null;
    ImageButton mChangeDictPopupExitBtn = null;
    ViewFlipper mChangeDictPopupViewFlipper = null;
    private boolean[] mCheckedWordbookList = {false};
    int mDisplayMode = 0;
    EngineManager3rd.SearchMethodInfo mSearchMethodWord = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodIdiom = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodExample = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodSpellCheck = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodHangulro = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodInitial = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodPinyin = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodOldKor = null;
    EngineManager3rd.SearchMethodInfo mSearchMethodTotal = null;
    EngineManager3rd.SearchMethodInfo mCurrentSearchMethod = null;
    private Cursor mCursor = null;
    private LinearLayout mMainLeftLayout = null;
    private boolean mIsShowFlashcardPop = false;
    private int mWordbookType = 1;
    private PopupWindow mFontSizeChangePopup = null;
    private PopupWindow mMarkerColorChangePopup = null;
    private AlertDialog mOptionDlg = null;
    ArrayList<String> mVoiceRecognizeMatches = null;
    private Rect mHwrRectArea = new Rect();
    HyperSimpleViewModule mHyperSimpleViewModule = null;
    private FileLinkTagViewManager mFileLinkTagViewManager = null;
    private PopupWindow mSearchMethodChangePopup = null;
    private TextView mCurrentSearchMethodTextView = null;
    private ImageView mSearchMethodArrowBtn = null;
    private final int MEANING_UPDATE_TIME_DELAY = SEARCH_TIME_DELAY;
    private AlertDialog mVoiceLanguageDlg = null;
    private int mTabViewPos = 0;
    private int mGestureRecognitionDelayTime = DictUtils.DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE;
    private ProgressDialog mProgressDialog = null;
    private Handler mProgressHandler = null;
    private int mItemCopyResult = 0;
    private int mLastWordPos = 0;
    private String mLastSearchWord = null;
    protected boolean isForceGetMeaning = false;
    private boolean isRealTimeSearchStop = false;
    private boolean mIsSymbolKeyword = false;
    private Runnable copyDialogRunnable = null;
    String mWillSearchText = "";
    private CandidateBox mCandiBox = null;
    private ArrayList<Integer> mTotalSearchPosition = new ArrayList<>();
    protected boolean mEnableChangeDict = true;
    private boolean isWantFinnish = false;
    private Runnable mFinishFlagInitialize = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.2
        @Override // java.lang.Runnable
        public void run() {
            SearchListActivity.this.isWantFinnish = false;
        }
    };
    private View.OnTouchListener mMainEmptyOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.5
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View arg0, MotionEvent me) {
            if (me.getAction() == 0) {
                SearchListActivity.this.mMainEmptyLayout.setVisibility(View.GONE);
                return false;
            }
            return false;
        }
    };
    private Runnable mUpdateMeanView = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.6
        @Override // java.lang.Runnable
        public void run() {
            if (!SearchListActivity.this.isForceGetMeaning) {
                SearchListActivity.this.updateCurrentKeywordMeaning();
            } else {
                SearchListActivity.this.isForceGetMeaning = false;
            }
        }
    };
    private Runnable mSearchWordRunnable = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.7
        @Override // java.lang.Runnable
        public void run() {
            SearchListActivity.this.AutoChangeLanguage(SearchListActivity.this.mWillSearchText, false, false);
            SearchListActivity.this.searchWord(SearchListActivity.this.mWillSearchText, true);
            if (SearchListActivity.this.mEngine.getResultListCount(0) > 0) {
                SearchListActivity.this.mHandler.postDelayed(SearchListActivity.this.mUpdateMeanView, 500L);
            } else {
                SearchListActivity.this.mSearchMeanController.refreshEmptyView();
            }
        }
    };
    private TextWatcher tw1 = new TextWatcher() { // from class: com.diotek.diodict.SearchListActivity.8
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            String word = s.toString();
            SearchListActivity.this.showClearButton(word);
            SearchListActivity.this.searchWordOnTextInput(word);
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    public View.OnClickListener mVoiceSearchOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (v.getId() == R.id.VoiceSearchBtn) {
                SearchListActivity.this.startVoiceRecognition();
            }
        }
    };
    View.OnClickListener mSearchEditTextOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runSearchEditText();
        }
    };
    private View.OnKeyListener mSearchEditTextOnKeyListener = new View.OnKeyListener() { // from class: com.diotek.diodict.SearchListActivity.11
        @Override // android.view.View.OnKeyListener
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            RelativeLayout guideView = (RelativeLayout) SearchListActivity.this.findViewById(R.id.search_guide_layout);
            if (guideView != null && guideView.getVisibility() == 0) {
                guideView.setVisibility(View.GONE);
                return false;
            }
            int action = event.getAction();
            switch (keyCode) {
                case 66:
                    return (action == 0 && SearchListActivity.this.runKeyCodeEnter()) ? true : true;
                default:
                    return false;
            }
        }
    };
    View.OnFocusChangeListener mSearchEditTextOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.SearchListActivity.12
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (!fakingFocus && SearchListActivity.this.mVoiceSearchBtn != null && !SearchListActivity.this.mUseVoiceSearch && SearchListActivity.this.mSearchWordBtn != null && SearchListActivity.this.mSearchWordBtn.getVisibility() != 0) {
                if (hasFocus) {
                    SearchListActivity.this.mVoiceSearchBtn.setBackgroundResource(R.drawable.searchedittext_end_f);
                } else {
                    SearchListActivity.this.mVoiceSearchBtn.setBackgroundResource(R.drawable.searchedittext_end);
                }
                SearchListActivity.this.showSoftInputMethod(hasFocus);
            }
        }
    };
    View.OnClickListener mClearBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.13
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runClearBtn();
        }
    };
    View.OnClickListener mSearchWordBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.14
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runSearchWordBtnOnly();
        }
    };
    AbsListView.OnScrollListener mSearchListViewOnScrollLisnter = new AbsListView.OnScrollListener() { // from class: com.diotek.diodict.SearchListActivity.15
        int firstvisibleindex = 0;
        int visibleitemcount = 0;
        int totalitemcount = 0;

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstvisibleindex = firstVisibleItem;
            this.visibleitemcount = visibleItemCount;
            this.totalitemcount = totalItemCount;
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (this.totalitemcount > this.visibleitemcount && this.totalitemcount != this.visibleitemcount && scrollState != 1) {
                if (this.firstvisibleindex == 0) {
                    if (SearchListActivity.this.mEngine.getResultListKeywordPos(0) != 0) {
                        SearchListActivity.this.searchPrevList();
                    }
                } else if (this.firstvisibleindex + this.visibleitemcount == this.totalitemcount && SearchListActivity.this.mEngine.getResultListCount(0) >= 100) {
                    SearchListActivity.this.searchNextList();
                }
            }
        }
    };
    AdapterView.OnItemClickListener mSearchListViewOnItemClickLisner = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.SearchListActivity.16
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            SearchListActivity.this.mHandler.removeCallbacks(SearchListActivity.this.mUpdateMeanView);
            SearchListActivity.this.mHandler.removeCallbacks(SearchListActivity.this.mSearchWordRunnable);
            if (SearchListActivity.this.mEngine.getCurDict() == 65520) {
                position = ((Integer) SearchListActivity.this.mTotalSearchPosition.get(position)).intValue();
            }
            SearchListActivity.this.runSearchListView(position, true);
        }
    };
    AdapterView.OnItemSelectedListener mSearchListViewOnItemSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.diotek.diodict.SearchListActivity.17
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == SearchListActivity.this.mSearchListViewItems.size() - 1) {
                if (SearchListActivity.this.mEngine.getResultListCount(0) >= 100) {
                    SearchListActivity.this.searchNextList();
                }
            } else if (position == 0 && SearchListActivity.this.mEngine.getResultListKeywordPos(0) != 0) {
                SearchListActivity.this.searchPrevList();
            }
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    View.OnClickListener mSearchMethodOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.18
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runSearchMethod(v);
        }
    };
    View.OnClickListener mMarkerBtnOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.19
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runMarkerBtn();
        }
    };
    View.OnClickListener mFontBtnOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.20
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runFontBtn();
        }
    };
    View.OnClickListener mMemoBtnOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.21
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runMemoBtn();
        }
    };
    Runnable mRunnableUpdateTabView = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.22
        @Override // java.lang.Runnable
        public void run() {
            SearchListActivity.this.runnableMeanTabView();
        }
    };
    TabView.TabViewOnClickListener mMeanTabViewOnClickListener = new TabView.TabViewOnClickListener() { // from class: com.diotek.diodict.SearchListActivity.23
        @Override // com.diotek.diodict.uitool.TabView.TabViewOnClickListener
        public void onClick(View v, int nPos) {
            if (SearchListActivity.this.mTabViewPos == nPos) {
                if (SearchListActivity.this.mMeanTabView != null) {
                    SearchListActivity.this.mMeanTabView.setBtnSelected(SearchListActivity.this.mTabViewPos);
                    return;
                }
                return;
            }
            if (SearchListActivity.this.mTextView != null) {
                SearchListActivity.this.mTextView.forceScrollStop();
            }
            SearchListActivity.this.mTabViewPos = nPos;
            SearchListActivity.this.mHandler.removeCallbacks(SearchListActivity.this.mRunnableUpdateTabView);
            SearchListActivity.this.mHandler.postDelayed(SearchListActivity.this.mRunnableUpdateTabView, 300L);
            if (SearchListActivity.this.mMeanTabView != null) {
                SearchListActivity.this.mMeanTabView.setBtnSelected(SearchListActivity.this.mTabViewPos);
            }
        }
    };
    View.OnClickListener mSaveBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.24
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runSaveBtn();
        }
    };
    private ExtendTextView.ExtendTextCallback mAutoUpdateLayoutCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.SearchListActivity.25
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            LayoutTransition.updateLayout(true, SearchListActivity.this.mStandardInnerLeftLayout, SearchListActivity.this.mMainRightLayout, SearchListActivity.this);
            return true;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            if (str.equals(ExtendTextView.GESTURE_SWIPE_RIGHT)) {
				mTextView.mSelectRealmPreferLong = 0;
                LayoutTransition.updateLayoutWithExtends(false, SearchListActivity.this.mStandardInnerLeftLayout, SearchListActivity.this.mMainRightLayout, SearchListActivity.this.mAnimationStartCallback, SearchListActivity.this.mAnimationEndCallback, SearchListActivity.this);
            } else {
				mTextView.mSelectRealmPreferLong = 2;
                LayoutTransition.updateLayoutWithExtends(true, SearchListActivity.this.mStandardInnerLeftLayout, SearchListActivity.this.mMainRightLayout, SearchListActivity.this.mAnimationStartCallback, SearchListActivity.this.mAnimationEndCallback, SearchListActivity.this);
            }
            return false;
        }
    };
    private Runnable mSwingBackUpdateLayoutCallback = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.26
        @Override // java.lang.Runnable
        public void run() {
            SearchListActivity.this.setSmallMeanView();
        }
    };
    private View.OnClickListener mSearchMethodChangeOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.27
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.changeSearchMethod(v.getId(), ((TextView) v).getText().toString());
        }
    };
    private View.OnClickListener mFontSizeChangeOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.28
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean isUpdate = true;
            int[] fontSizeList = SearchListActivity.this.getResources().getIntArray(R.array.value_font_size);
            int fontSizeIndex = 0;
            switch (v.getId()) {
                case R.id.font_10 /* 2131099855 */:
                    fontSizeIndex = 0;
                    break;
                case R.id.font_12 /* 2131099856 */:
                    fontSizeIndex = 1;
                    break;
                case R.id.font_14 /* 2131099857 */:
                    fontSizeIndex = 2;
                    break;
                case R.id.font_16 /* 2131099858 */:
                    fontSizeIndex = 3;
                    break;
                case R.id.font_18 /* 2131099859 */:
                    fontSizeIndex = 4;
                    break;
                case R.id.font_close /* 2131099860 */:
                    isUpdate = false;
                    break;
            }
            if (SearchListActivity.this.mTextView != null && isUpdate) {
                SearchListActivity.this.mTextView.setTextSize(fontSizeList[fontSizeIndex]);
                DictUtils.setFontSizeToPreference(SearchListActivity.this, fontSizeIndex);
            }
            SearchListActivity.this.dismissFontSizeChangePopup();
        }
    };
    private View.OnTouchListener layoutOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.29
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 4 || event.getAction() == 3) {
                if (SearchListActivity.this.mMarkerColorChangePopup != null) {
                    SearchListActivity.this.mMarkerColorChangePopup.dismiss();
                    SearchListActivity.this.mMarkerColorChangePopup = null;
                }
                return true;
            }
            return false;
        }
    };
    private View.OnClickListener mMarkerColorChangeOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.30
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean colorChange = true;
            int[] colorList = SearchListActivity.this.getResources().getIntArray(R.array.value_marker_color_adv);
            int markerColorIndex = 0;
            switch (v.getId()) {
                case R.id.marker_color_blue /* 2131100005 */:
                    markerColorIndex = 0;
                    break;
                case R.id.marker_color_red /* 2131100006 */:
                    markerColorIndex = 1;
                    break;
                case R.id.marker_color_green /* 2131100007 */:
                    markerColorIndex = 2;
                    break;
                case R.id.marker_color_pink /* 2131100008 */:
                    markerColorIndex = 3;
                    break;
                case R.id.marker_color_yellow /* 2131100009 */:
                    markerColorIndex = 4;
                    break;
                case R.id.marker_color_white /* 2131100010 */:
                    markerColorIndex = 5;
                    break;
                case R.id.marker_close /* 2131100011 */:
                    SearchListActivity.this.dismissMarkerColorChangePopup();
                    colorChange = false;
                    break;
            }
            if (SearchListActivity.this.mTextView != null && colorChange) {
                SearchListActivity.this.mTextView.setMarkerColor(colorList[markerColorIndex]);
                if (markerColorIndex < 5) {
                    DictUtils.setMarkerColorToPreference(SearchListActivity.this, markerColorIndex);
                }
            }
        }
    };
    DialogInterface.OnClickListener onPopupCancelListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.31
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if (SearchListActivity.this.mOptionDlg != null) {
                SearchListActivity.this.mOptionDlg.dismiss();
            }
        }
    };
    DialogInterface.OnClickListener mVoiceLanguageDialogClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.32
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface di, int position) {
            SearchListActivity.this.mTotalSearchVoiceLanguage = position;
            SearchListActivity.this.startVoiceRecognitionActivityWithLanguage(SearchListActivity.this.mVoiceRecognitionLanguageItems.get(SearchListActivity.this.mTotalSearchVoiceLanguage));
            di.dismiss();
            SearchListActivity.this.mVoiceLanguageDlg = null;
        }
    };
    DialogInterface.OnClickListener mVoiceLanguageDialogCancelListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.33
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if (SearchListActivity.this.mVoiceLanguageDlg != null) {
                SearchListActivity.this.mVoiceLanguageDlg.dismiss();
            }
        }
    };
    DialogInterface.OnClickListener mMarkerColorClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.34
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface di, int position) {
            SearchListActivity.this.onSelectColor(position);
            di.dismiss();
        }
    };
    PageGridView.PageGridViewOnClickListener mChangeDictPopupOnItemClickListener = new PageGridView.PageGridViewOnClickListener() { // from class: com.diotek.diodict.SearchListActivity.35
        @Override // com.diotek.diodict.uitool.PageGridView.PageGridViewOnClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            SearchListActivity.this.runChangeDictPopup(position);
        }

        @Override // com.diotek.diodict.uitool.PageGridView.PageGridViewOnClickListener
        public void onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
        }
    };
    View.OnClickListener mChangeDictPopupOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.36
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runChangeDictPopupExitBtn();
        }
    };
    DialogInterface.OnClickListener mVoiceRecognizePositiveOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.37
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            SearchListActivity.this.startVoiceRecognition();
        }
    };
    DialogInterface.OnClickListener mVoiceRecognizeNegativeOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.38
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    DialogInterface.OnClickListener mVoiceRecognizeListOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.39
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface di, int position) {
            selectVoiceRecognizeResult(position);
            di.dismiss();
        }

        private void selectVoiceRecognizeResult(int position) {
            String result = SearchListActivity.this.mVoiceRecognizeMatches.get(position);
            if (result.length() > 0 && SearchListActivity.this.etSearch != null) {
                if (SearchListActivity.this.mEngine.getCurDict() == 65520) {
                    SearchListActivity.this.directSearchMeaning(result, true, true);
                    SearchListActivity.this.etSearch.setSelection(result.length());
                    return;
                }
                SearchListActivity.this.etSearch.setText(result);
                SearchListActivity.this.etSearch.setSelection(result.length());
            }
        }
    };
    View.OnClickListener mChangeDictionaryBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.40
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runChangeDictionaryBtn();
        }
    };
    View.OnClickListener mChangeLanguageBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.41
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.isRealTimeSearchStop = true;
            SearchListActivity.this.etSearch.setText("");
            SearchListActivity.this.mWillSearchText = "";
            SearchListActivity.this.AutoChangeLanguage("", true, true);
            SearchListActivity.this.mCurrentSearchMethod = SearchListActivity.this.mSearchMethodWord;
            SearchListActivity.this.mSearchWordBtn.setVisibility(View.GONE);
            SearchListActivity.this.setEnableSaveButton(true);
            if (SearchListActivity.this.mLayoutMode == 1) {
                SearchListActivity.this.setSmallMeanView();
            }
            SearchListActivity.this.showSoftInputMethod(true);
        }
    };
    View.OnTouchListener mMeanTextViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.42
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            SearchListActivity.this.showSoftInputMethod(false);
            return false;
        }
    };
    private View.OnTouchListener mGestureOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.43
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View arg0, MotionEvent arg1) {
            SearchListActivity.this.mIsGesture = true;
            return true;
        }
    };
    private GestureOverlayView.OnGesturePerformedListener mGesturePerformeListener = new GestureOverlayView.OnGesturePerformedListener() { // from class: com.diotek.diodict.SearchListActivity.44
        @Override // android.gesture.GestureOverlayView.OnGesturePerformedListener
        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        }
    };
    View.OnClickListener mCopyToFlashcardLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.45
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.46
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (SearchListActivity.this.isSelectedSaveToFlashcardFolder()) {
                int unused = SearchListActivity.mItemCopyState = 1;
                SearchListActivity.this.mFlashcardItemEditCopyToFlashcardOk.setEnabled(false);
                SearchListActivity.this.mFlashcardItemEditCopyToFlashcardOk.setClickable(false);
                SearchListActivity.this.copyDialogRunnable = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.46.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SearchListActivity.this.showDialog(1);
                    }
                };
                SearchListActivity.this.mProgressHandler.postDelayed(SearchListActivity.this.copyDialogRunnable, 1000L);
                new Thread(new Runnable() { // from class: com.diotek.diodict.SearchListActivity.46.2
                    @Override // java.lang.Runnable
                    public void run() {
                        SearchListActivity.this.runSaveFlashcardItem();
                        SearchListActivity.this.mProgressHandler.sendEmptyMessage(1);
                        int unused2 = SearchListActivity.mItemCopyState = 2;
                    }
                }).start();
                return;
            }
            SearchListActivity.this.showToast(SearchListActivity.this.getResources().getString(R.string.selectFlashcardToSave));
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.47
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (SearchListActivity.this.mCursor != null) {
                SearchListActivity.this.mCursor.close();
                SearchListActivity.this.mCursor = null;
            }
            SearchListActivity.this.showCopyToFlashcardLayout(false, false, true);
        }
    };
    View.OnClickListener mAddWordbookTextViewOnCLickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.48
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            v.setSelected(true);
            SearchListActivity.this.makeWordbook();
            v.setSelected(false);
        }
    };
    CompoundButton.OnCheckedChangeListener mCard1OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.SearchListActivity.49
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SearchListActivity.this.mWordbookType = 1;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard2OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.SearchListActivity.50
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SearchListActivity.this.mWordbookType = 2;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard3OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.SearchListActivity.51
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SearchListActivity.this.mWordbookType = 3;
            }
        }
    };
    View.OnClickListener mBtnMakeWordbookOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.52
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runBtnMakeWordbookOk(v);
            SearchListActivity.this.mFlashcardFolderListViewAdapter.notifyDataSetChanged();
            SearchListActivity.this.mAddWordbookTextView.setSelected(false);
        }
    };
    View.OnClickListener mBtnMakeWordbookCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.53
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runBtnMakeWordbookCancel(v);
            SearchListActivity.this.mAddWordbookTextView.setSelected(false);
        }
    };
    AdapterView.OnItemClickListener mFlashcardGridViewOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.SearchListActivity.54
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            boolean z = true;
            SearchListActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
            SearchListActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.setChecked(!SearchListActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.isChecked());
            boolean[] zArr = SearchListActivity.this.mCheckedWordbookList;
            if (SearchListActivity.this.mCheckedWordbookList[position]) {
                z = false;
            }
            zArr[position] = z;
            SearchListActivity.this.mFlashcardFolderListViewItems.get(position).put(DictInfo.ListItem_WordbookFolderChecked, Boolean.valueOf(SearchListActivity.this.mCheckedWordbookList[position]));
        }
    };
    private View.OnClickListener mMemoIconOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.55
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runListMemoBtn(Integer.parseInt(v.getTag().toString()));
        }
    };
    View.OnClickListener mChangeDictPopupExitBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.56
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SearchListActivity.this.runChangeDictPopupExitBtn();
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.SearchListActivity.57
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(SearchListActivity.this);
        }
    };
    ExtendTextView.ExtendTextCallback mStartHyperCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.SearchListActivity.58
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            DictUtils.setSearchLastDictToPreference(SearchListActivity.this, SearchListActivity.this.mEngine.getCurDict());
            DictUtils.setSearchLastTypeToPreference(SearchListActivity.this, SearchListActivity.this.mEngine.getCurrentSearchMethodId());
            SearchListActivity.this.handleSaveMarkerObject();
            return SearchListActivity.this.mHyperSimpleViewModule.startHyperSimple(str);
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            if (SearchListActivity.this.mTextView != null) {
                DictUtils.setSearchLastDictToPreference(SearchListActivity.this, SearchListActivity.this.mEngine.getCurDict());
                return SearchListActivity.this.mHyperSimpleViewModule.startHyperSimple(SearchListActivity.this.mTextView.getSelectedString());
            }
            return false;
        }
    };
    HyperSimpleViewModule.HyperSimpleViewModuleCallback mHyperSimpleViewModuleCallback = new HyperSimpleViewModule.HyperSimpleViewModuleCallback() { // from class: com.diotek.diodict.SearchListActivity.59
        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runDetailBtn(int meanpos) {
            String word;
            boolean z = true;
            if (SearchListActivity.this.mCurrentSearchMethod != SearchListActivity.this.mSearchMethodWord) {
                word = SearchListActivity.this.mLastSearchWord;
            } else {
                word = SearchListActivity.this.mSearchMeanController.getWord();
            }
            DictUtils.setSearchLastWordToPreference(SearchListActivity.this, word);
            Intent intent = new Intent();
            intent.setClass(SearchListActivity.this, HyperSearchActivity.class);
            intent.putExtra(DictInfo.INTENT_SAVE_INPUTWORD, SearchListActivity.this.etSearch.getText().toString());
            intent.putExtra(DictInfo.INTENT_HYPER_MEANPOS, meanpos);
            intent.putExtra(DictInfo.INTENT_HYPER_DICTNAME, DictInfo.mCurrentDBName);
            intent.putExtra(DictInfo.INTENT_HYPER_SHOW_SEARCH, SearchListActivity.this.mSearchWordBtn.getVisibility() == 0);
            if (SearchListActivity.this.mChangeLanguageBtn.getVisibility() != 0) {
                z = false;
            }
            intent.putExtra(DictInfo.INTENT_HYPER_SHOW_CHANGEDICT, z);
            SearchListActivity.this.startActivityForResult(intent, 1004);
        }

        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runExitBtn() {
            SearchListActivity.this.initSelection();
        }
    };
    View.OnTouchListener mMeanContentBottomViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.60
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView scrollView = (ScrollView) SearchListActivity.this.mTextView.getParent();
            if (scrollView == null) {
                return false;
            }
            int minHeight = SearchListActivity.this.mTextView.getMeasuredHeight();
            MotionEvent tmpEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX(), event.getY() + minHeight, event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags());
            SearchListActivity.this.showSoftInputMethod(false);
            SearchListActivity.this.mTextView.onTouchEvent(tmpEvent);
            tmpEvent.recycle();
            return false;
        }
    };
    View.OnTouchListener mParentOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.61
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };
    View.OnClickListener mPopupOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.62
        @Override // android.view.View.OnClickListener
        public void onClick(View arg0) {
            SearchListActivity.this.onTerminatePopup();
        }
    };
    CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback mCustomPopupLinearLayoutOnKeyListenerCallback = new CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback() { // from class: com.diotek.diodict.SearchListActivity.63
        @Override // com.diotek.diodict.uitool.CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback
        public void runOnKeyListener(KeyEvent event) {
            SearchListActivity.this.onTerminatePopup();
        }
    };
    View.OnFocusChangeListener mMainMeanContentTextViewOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.SearchListActivity.64
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            ImageView meanSeparator = (ImageView) SearchListActivity.this.findViewById(R.id.separator);
            if (meanSeparator != null) {
                if (hasFocus) {
                    meanSeparator.setVisibility(View.VISIBLE);
                } else {
                    meanSeparator.setVisibility(View.GONE);
                }
            }
        }
    };
    private Runnable runInitGestureLayout = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.65
        @Override // java.lang.Runnable
        public void run() {
            SearchListActivity.this.initGestureLayout();
        }
    };
    private Runnable setFocusableChangeDictionary = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.66
        @Override // java.lang.Runnable
        public void run() {
            SearchListActivity.this.setFocusableChangeDictionary(true);
        }
    };
    private Runnable mHwrGuestureDetector = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.67
        @Override // java.lang.Runnable
        public void run() {
            if (!SearchListActivity.this.isHwrRectInit()) {
                SearchListActivity.this.setHandWritingArea();
            }
            if (!SearchListActivity.this.mIsGesture) {
                SearchListActivity.this.mGuestureDetector.clearInk();
                return;
            }
            boolean isKorInitialSearch = SearchListActivity.this.mEngine.getCurrentSearchMethodId() == 32;
            if (DictDBManager.isOldKorDict(SearchListActivity.this.mEngine.getCurDict())) {
                isKorInitialSearch = true;
            }
            SearchListActivity.this.mGuestureDetector.setLanguageMode(EngineInfo3rd.getIMEMode(SearchListActivity.this.mEngine.getCurDict(), SearchListActivity.this.mEngine.getCurrentSearchMethodId()), isKorInitialSearch);
            String strResult = SearchListActivity.this.mGuestureDetector.recognizeSentenceGesetureDetect();
            SearchListActivity.this.mGuestureDetector.setSuggestionActive(true);
            boolean bRecogTextExist = strResult.length() > 0;
            boolean bRecogTextIsDot = strResult.length() == 1 && strResult.equals(".");
            boolean bRecogTextIsAsterisk = strResult.length() == 1 && strResult.equals("*");
            if (bRecogTextExist && !bRecogTextIsDot) {
                if (bRecogTextIsAsterisk || !SearchListActivity.this.mGuestureDetector.isSingleCharMode()) {
                    SearchListActivity.this.addStringToEdit(strResult);
                } else {
                    SearchListActivity.this.mCandiBox.setCandidate(SearchListActivity.this.mGuestureDetector.getCandidate());
                    SearchListActivity.this.mCandiBox.show();
                }
            }
            SearchListActivity.this.initGestureLayout();
            SearchListActivity.this.mIsGesture = false;
        }
    };
    PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.SearchListActivity.68
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            SearchListActivity.this.setClickableMeanToolBar(true);
        }
    };
    RadioGroup markerGroup = null;
    ImageView mMarkerCloseBtn = null;
    int MARKER_TAG = 100;
    private Runnable mHideOnlySoftInput = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.69
        @Override // java.lang.Runnable
        public void run() {
            InputMethodManager imm = (InputMethodManager) SearchListActivity.this.getApplicationContext().getSystemService("input_method");
            if (imm != null && SearchListActivity.this.etSearch != null) {
                imm.hideSoftInputFromWindow(SearchListActivity.this.etSearch.getWindowToken(), 0);
            } else {
                Log.e("imm", "mhideOnlySoftInput():imm or mSearchInput is null");
            }
        }
    };
    private Runnable mShowOnlySoftInput = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.70
        @Override // java.lang.Runnable
        public void run() {
            InputMethodManager imm = (InputMethodManager) SearchListActivity.this.getApplicationContext().getSystemService("input_method");
            if (imm != null && SearchListActivity.this.etSearch != null) {
                int imeInfo = 0;
                if (CommonUtils.isUseKeypadNoExtractUI()) {
                    imeInfo = 1;
                }
                imm.showSoftInput(SearchListActivity.this.etSearch, imeInfo);
                return;
            }
            Log.e("imm", "mshowOnlySoftInput():imm or mSearchInput is null");
        }
    };
    private boolean mGestureLayoutUpdate = false;
    private Runnable mRunTiffanyStiker = new Runnable() { // from class: com.diotek.diodict.SearchListActivity.71
        @Override // java.lang.Runnable
        public void run() {
            if (SearchListActivity.this.mTfTrans != null) {
                Window win = SearchListActivity.this.getWindow();
                View root = win.getDecorView();
                SearchListActivity.this.mTfTrans.setRootView(root);
                SearchListActivity.this.mTfTrans.setTransToView(SearchListActivity.this.mMainMeanBookmarkImageView);
                if (win.isActive()) {
                    try {
                        SearchListActivity.this.mTfTrans.transition(13, 0);
                        return;
                    } catch (OutOfMemoryError e) {
                        System.gc();
                        return;
                    }
                }
                return;
            }
            SearchListActivity.this.mMainMeanBookmarkImageView.setVisibility(View.VISIBLE);
        }
    };
    private BaseMeanController.MeanControllerCallback mTTSLayoutCallback = new BaseMeanController.MeanControllerCallback() { // from class: com.diotek.diodict.SearchListActivity.73
        @Override // com.diotek.diodict.mean.BaseMeanController.MeanControllerCallback
        public boolean run() {
            if (SearchListActivity.runCallBackTTSLayout) {
                boolean unused = SearchListActivity.runCallBackTTSLayout = false;
                SearchListActivity.this.showHideTTSLayout(true);
            }
            return false;
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        CommonUtils.setKeypadNoExtractUI(this);
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        setContentView(R.layout.search_content_layout);
        prepareTitleLayout(R.string.title_search, true);
        if (!super.onCreateActivity(savedInstanceState)) {
            Intent intent = getIntent();
            intent.setClass(this, DioAuthActivity.class);
            startActivity(intent);
            return;
        }
        if (Dependency.isContainHandWrightReocg()) {
            this.mCandiBox = new CandidateBox(new CandidateBox.AddStringToEdit() { // from class: com.diotek.diodict.SearchListActivity.1
                @Override // com.diotek.diodict.uitool.CandidateBox.AddStringToEdit
                public void addString(String toAddString) {
                    SearchListActivity.this.addStringToEdit(toAddString);
                }
            });
        }
        initActivity(true);
	
		if (GlobalOptions.density==0) {
			DisplayMetrics dm = getResources().getDisplayMetrics();
			GlobalOptions.density = dm.density;
		}
		
		// automatically dismiss the text relocation dialog
		if (!GlobalOptions.hideTextRecDlg) {
			GlobalOptions.hideTextRecDlg = true;
			F1ag fag = new F1ag();
			Runnable findWndRun = new Runnable() {
				@Override
				public void run() {
					//ViewUtils.logAllViews();
					//mHandler.postDelayed(this, 1000);
					try {
						List<View> wnds = ViewUtils.getWindowManagerViews(SearchListActivity.this);
						if (wnds.size()>=3) {
							for (int i = 0; i < wnds.size(); i++) {
								View sv = ((ViewGroup)wnds.get(i)).getChildAt(0);
								sv = ViewUtils.getViewItemByClass(sv, 0, FrameLayout.class, FrameLayout.class, View.class, ScrollView.class);
								// CMN.Log("wnds.get(i)::", wnds.get(i), wnds.get(i) instanceof ViewGroup);
								if (sv!=null) {
									sv = sv.findViewById(android.R.id.button1);
									if (sv!=null) {
										sv.performClick();
										return;
									}
								}
							}
						}
					} catch (Exception e) {
						CMN.debug(e);
					}
					if (fag.val++<3) {
						mHandler.postDelayed(this, 250);
					}
				}
			};
			mHandler.postDelayed(findWndRun, 200);
		}
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onPause() {
        this.mHandler.removeCallbacks(this.mUpdateMeanView);
        this.mHandler.removeCallbacks(this.mSearchWordRunnable);
        handleSaveMarkerObject();
        this.mFileLinkTagViewManager.onPause();
        this.mHyperSimpleViewModule.onPause();
        super.onPause();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        handleSaveMarkerObject();
        destroyData(true);
        destroyChangeDictPopupImage(true);
        destroyImage(true);
        initActivity(false);
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing()) {
            this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
            removeDialog(0);
            showDialog(0);
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
            if (!this.mInputWordbookName.equals("")) {
                this.mEdittextWordbookName.setText(this.mInputWordbookName);
            }
            this.mEdittextWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
            this.mInputWordbookNameTextView = (TextView) this.mWordbookDialog.findViewById(R.id.editview_editwordbook);
            ImageButton clearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
            if (clearBtn != null) {
                clearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
            }
        }
        if (this.mCurrentSearchMethodTextView != null && this.mCurrentSearchMethod != null) {
            this.mCurrentSearchMethodTextView.setText(this.mCurrentSearchMethod.getNameID());
        }
        super.onConfigurationChanged(newConfig);
    }

    public void destroyImage(boolean isconfigChange) {
        if (!isconfigChange && this.mChangeDictionaryBtn != null) {
            UITools.recycleDrawable(this.mChangeDictionaryBtn.getDrawable(), false, isconfigChange);
        }
        if (!isconfigChange && this.mChangeLanguageBtn != null) {
            UITools.recycleDrawable(this.mChangeLanguageBtn.getDrawable(), false, isconfigChange);
        }
        if (!isconfigChange && this.mClearBtn != null) {
            UITools.recycleDrawable(this.mClearBtn.getDrawable(), false, isconfigChange);
        }
        if (!isconfigChange && this.mSearchWordBtn != null) {
            UITools.recycleDrawable(this.mSearchWordBtn.getDrawable(), false, isconfigChange);
        }
        if (!isconfigChange && this.mVoiceSearchBtn != null) {
            UITools.recycleDrawable(this.mVoiceSearchBtn.getDrawable(), false, isconfigChange);
        }
        if (this.mMarkerBtn != null) {
            UITools.recycleDrawable(this.mMarkerBtn.getDrawable(), false, isconfigChange);
        }
        if (this.mFontBtn != null) {
            UITools.recycleDrawable(this.mFontBtn.getDrawable(), false, isconfigChange);
        }
        if (this.mMemoBtn != null) {
            UITools.recycleDrawable(this.mMemoBtn.getDrawable(), false, isconfigChange);
        }
        if (this.mSaveBtn != null) {
            UITools.recycleDrawable(this.mSaveBtn.getDrawable(), false, isconfigChange);
        }
        if (!isconfigChange && this.mChangeDictPopupExitBtn != null) {
            UITools.recycleDrawable(this.mChangeDictPopupExitBtn.getDrawable(), false, isconfigChange);
        }
        if (!isconfigChange && this.mSearchMethodArrowBtn != null) {
            UITools.recycleDrawable(this.mSearchMethodArrowBtn.getBackground(), false, isconfigChange);
        }
        if (!isconfigChange && this.mFileLinkTagViewManager != null) {
            this.mFileLinkTagViewManager.destory();
        }
        this.mMarkerBtn = null;
        this.mFontBtn = null;
        this.mMemoBtn = null;
        this.mSaveBtn = null;
        if (!isconfigChange) {
            this.mChangeDictionaryBtn = null;
            this.mChangeLanguageBtn = null;
            this.mClearBtn = null;
            this.mSearchWordBtn = null;
            this.mVoiceSearchBtn = null;
            this.mChangeDictPopupExitBtn = null;
            this.mSearchMethodArrowBtn = null;
            RelativeLayout inputLayout = (RelativeLayout) findViewById(R.id.InputLayout);
            if (inputLayout != null) {
                UITools.recycleDrawable(inputLayout.getBackground(), false, isconfigChange);
            }
            System.gc();
        }
    }

    public void destroyChangeDictPopupImage(boolean isconfigChange) {
        LinearLayout topLayout = (LinearLayout) findViewById(R.id.changedPopupTopLayout);
        if (topLayout != null) {
            UITools.recycleDrawable(topLayout.getBackground(), false, isconfigChange);
        }
        ImageView titleIcon = (ImageView) findViewById(R.id.titleIcon);
        if (titleIcon != null) {
            UITools.recycleDrawable(titleIcon.getBackground(), false, isconfigChange);
        }
        LinearLayout heightLayout = (LinearLayout) findViewById(R.id.gridviewHeightGuidLayout);
        if (heightLayout != null) {
            UITools.recycleDrawable(heightLayout.getBackground(), false, isconfigChange);
        }
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.gridviewGrouplayout);
        if (flipper != null) {
            UITools.recycleDrawable(flipper.getBackground(), false, isconfigChange);
        }
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.changedPopupDotLayout);
        if (dotLayout != null) {
            UITools.recycleDrawable(dotLayout.getBackground(), false, isconfigChange);
        }
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.changedPopupBottomLayout);
        if (bottomLayout != null) {
            UITools.recycleDrawable(bottomLayout.getBackground(), false, isconfigChange);
        }
    }

    public void destroyData(boolean isconfigChange) {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mRunTiffanyStiker);
            this.mHandler.removeCallbacks(this.runInitGestureLayout);
            this.mHandler.removeCallbacks(this.mHwrGuestureDetector);
            this.mHandler.removeCallbacks(this.mUpdateMeanView);
            this.mHandler.removeCallbacks(this.mSearchWordRunnable);
        }
        if (!isconfigChange && this.mSearchMeanController != null) {
            this.mSearchMeanController.onDestory();
        }
        if (!isconfigChange && this.mTextView != null) {
            this.mTextView.onDestroy();
        }
        if (this.mMarkerColorChangePopup != null) {
            this.mMarkerColorChangePopup.dismiss();
            this.mMarkerColorChangePopup = null;
            if (this.mTextView != null) {
                this.mTextView.setMarkerMode(false);
            }
        }
        if (this.mFontSizeChangePopup != null) {
            this.mFontSizeChangePopup.dismiss();
            this.mFontSizeChangePopup = null;
        }
        if (this.mHyperSimpleViewModule != null) {
            this.mHyperSimpleViewModule.onDestory();
        }
        if (this.mFileLinkTagViewManager != null) {
            this.mFileLinkTagViewManager.onDestory();
        }
        if (this.mSearchMethodChangePopup != null) {
            this.mSearchMethodChangePopup.dismiss();
        }
    }

    private void initActivity(boolean bCreate) {
        if (bCreate) {
            initVariables();
            setProgressHandler();
            prepareContentLayout();
            restoreSearchInfo();
            prepareGuideLayout();
            prepareRegularWordHashTable();
        } else {
            int dicType = this.mSearchMeanController.getDicType();
            String keyword = this.mSearchMeanController.getWord();
            int suid = this.mSearchMeanController.getSuid();
            int listCount = this.mSearchListViewAdapter.getCount();
            this.mOrientation = getResources().getConfiguration().orientation;
            prepareMainRightLayout();
            UITools.prepareMemoSkin(this, dicType, keyword, suid, this.mMemoBtn);
            if (listCount > 0) {
                runMeanTabView(0, false);
                selectTabAll();
                updateDisplayTabMenu();
                if (keyword == null) {
                    prepareMeanController(this.mLastWordPos);
                } else {
                    this.mSearchMeanController.setMeanViewKeywordInfo(dicType, keyword, suid, 0);
                }
            } else {
                showHideEmptyLayout(true);
            }
            this.mTabViewPos = 0;
            if (this.mLayoutMode == 1) {
                this.mGestures.setVisibility(View.GONE);
                setWideMeanView(true);
                if (this.mEngine.getCurDict() == 65520) {
                    runCallBackTTSLayout = true;
                }
                showSoftInputMethod(false);
            } else {
                setSmallMeanView();
            }
            if (this.mIsShowFlashcardPop) {
                showFlashcardListPop(true);
            } else {
                showCopyToFlashcardLayout(false, false, false);
            }
        }
        DictInfo.mCurrentDBName = DictDBManager.getDictName(this.mEngine.getCurDict());
        setSearchDBName();
        restoreMakeDialog();
        if (Dependency.isContainHandWrightReocg()) {
            this.mCandiBox.Init(this);
        }
    }

    private void initVariables() {
        this.mCurrentMenuId = R.id.menu_search;
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            this.mLastSearchWord = intent.getExtras().getString(DictInfo.INTENT_WORDINFO_KEYWORD);
        }
        int dictype = DictUtils.getSearchLastDictFromPreference(this);
        if (dictype == -1) {
            int dictype2 = DictUtils.getLastDictFromPreference(this);
            DictUtils.setSearchLastDictToPreference(this, dictype2);
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onDestroy() {
        initializeSearchEditText();
        destroyData(false);
        destroyImage(false);
        destroyChangeDictPopupImage(false);
        super.onDestroy();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onResume() {
        if (this.mEngine.getCurDict() == 65520) {
            enableGesture(false);
        } else {
            enableGesture(true);
        }
        super.onResume();
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != FINISH_ACTIVITY) {
            if (this.mSearchListViewAdapter.getCount() > 0) {
                int nTheme = this.mSearchMeanController.getTheme();
                if (nTheme != DictUtils.getFontThemeFromPreference(this)) {
                    this.mSearchMeanController.refreshViewNoDelay();
                }
            }
            if (Dependency.isContainHandWrightReocg()) {
                initHandWritingDelayTime();
                if (this.mGestures != null) {
                    this.mGestures.setFadeOffset(this.mGestureRecognitionDelayTime);
                }
            }
            if ((this.mTextView != null && this.mEngine.getCurDict() != 65520 && this.mSearchListViewAdapter.isEmpty()) || (this.mEngine.getCurDict() == 65520 && this.mSearchListViewTotalAdapter.isEmpty())) {
                changeDictionary(this.mTextView.getDbtype(), true);
                showHideSearchMethodLayout();
            } else if (resultCode == 1001 || requestCode == 1002 || requestCode == 1003) {
                restoreDicType(true);
                if (this.mSearchMeanController.getWord() != null) {
                    this.mSearchMeanController.refreshBookmark();
                }
                if (this.mLayoutMode != 0) {
                    dismissFlashcardCopyPopup(false);
                    dismissMarkerColorChangePopup();
                    if (this.mTextView.gripShowing()) {
                        initSelection();
                    }
                    setSmallMeanView();
                }
                this.mSearchListViewAdapter.notifyDataSetChanged();
            } else if (requestCode == 1004) {
                initSelection();
                int nDBType = DictUtils.getSearchLastDictFromPreference(this);
                String word = DictUtils.getSearchLastWordFromPreference(this);
                if (nDBType != this.mEngine.getCurDict()) {
                    this.mEngine.setDicType(nDBType);
                }
                if (nDBType != 65520) {
                    this.mSearchMeanController.changeDicType(nDBType);
                }
                this.mEngine.setSearchMethod(this.mCurrentSearchMethod);
                if (word == null) {
                    word = "";
                }
                searchWord(word, true);
                this.mFontSizeChangePopup = null;
                setFontSizeFromPreference(false);
                checkSymbolKeyword();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == -1) {
                    switch (requestCode) {
                        case 8:
                            saveToMemoDB(data);
                            break;
                        case VOICE_RECOGNITION_REQUEST_CODE /* 1234 */:
                            onVoiceRecognizeResult(data);
                            break;
                    }
                }
                if (requestCode == 8) {
                    setClickableMeanToolBar(true);
                    this.mMemoBtn.requestFocus();
                }
            }
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity
	public void setSmallMeanView() {
        this.mGestures.setVisibility(View.VISIBLE);
        super.setSmallMeanView();
    }

    private String getVoiceReconizeCurrentLanguage() {
        if (1024 == this.mEngine.getCurrentSearchMethodId()) {
            if (this.mVoiceRecognitionLanguageItems.size() == 0) {
                buildSupportDictionaryLanguageList();
                if (this.mVoiceRecognitionLanguageItems.size() != 0) {
                    String language = this.mVoiceRecognitionLanguageItems.get(this.mTotalSearchVoiceLanguage);
                    return language;
                }
                String language2 = Locale.US.toString();
                return language2;
            } else if (this.mTotalSearchVoiceLanguage < this.mVoiceRecognitionLanguageItems.size()) {
                String language3 = this.mVoiceRecognitionLanguageItems.get(this.mTotalSearchVoiceLanguage);
                return language3;
            } else {
                this.mTotalSearchVoiceLanguage = 0;
                String language4 = this.mVoiceRecognitionLanguageItems.get(this.mTotalSearchVoiceLanguage);
                return language4;
            }
        }
        String language5 = EngineInfo3rd.getInputLangaugeStr(this.mEngine.getCurDict(), this.mEngine.getCurrentSearchMethodId());
        return language5;
    }

    private void onVoiceRecognizeResult(Intent data) {
        ArrayList<String> matches = data.getStringArrayListExtra("android.speech.extra.RESULTS");
        ArrayList<String> temp = new ArrayList<>();
        String language = getVoiceReconizeCurrentLanguage();
        if (Locale.KOREA.toString().equals(language)) {
            for (int i = 0; i < matches.size(); i++) {
                String result = matches.get(i);
                if (DictUtils.getCodePage(result) == 949) {
                    temp.add(result);
                }
            }
        } else if (Locale.JAPAN.toString().equals(language)) {
            for (int i2 = 0; i2 < matches.size(); i2++) {
                String result2 = matches.get(i2);
                if (DictUtils.getCodePage(result2) == 932 || DictUtils.getCodePage(result2) == 936) {
                    temp.add(result2);
                }
            }
        } else if (Locale.CHINA.toString().equals(language)) {
            for (int i3 = 0; i3 < matches.size(); i3++) {
                String result3 = matches.get(i3);
                if (DictUtils.getCodePage(result3) == 936) {
                    temp.add(result3);
                }
            }
        } else if (Locale.US.toString().equals(language) || Locale.FRANCE.toString().equals(language) || Locale.GERMANY.toString().equals(language)) {
            for (int i4 = 0; i4 < matches.size(); i4++) {
                String result4 = matches.get(i4);
                if (DictUtils.isEnglish(result4) || DictUtils.isLatin(result4) || DictUtils.isFirstNumber(result4)) {
                    temp.add(result4);
                }
            }
        }
        if (temp.size() > 0) {
            showVoiceRecognizeResult(temp);
            return;
        }
        Toast.makeText(this, getResources().getString(R.string.voice_no_match), 0).show();
        directSearchMeaning("", true, false);
        this.etSearch.setSelection(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void directSearchMeaning(String word, boolean isDefaultPos, boolean bAddHistory) {
        this.isForceGetMeaning = true;
        this.etSearch.setText(word);
        this.etSearch.setSelection(this.etSearch.getText().toString().length());
        searchWord(word, true);
        if (isDefaultPos) {
            this.mLastWordPos = this.mEngine.getResultListKeywordPos(0);
            runSearchListView(this.mLastWordPos, bAddHistory);
            this.mHandler.post(this.mSwingBackUpdateLayoutCallback);
        }
        this.isForceGetMeaning = false;
    }

    @Override // com.diotek.diodict.ListMeanViewActivity
	public void onMeanViewExtensionStart() {
        this.mGestures.setVisibility(View.GONE);
        if (Dependency.isContainHandWrightReocg()) {
            this.mCandiBox.hide();
        }
        if (this.mTextView != null) {
            UITools.prepareMemoSkin(this, this.mTextView.getDbtype(), this.mTextView.getKeyword(), this.mTextView.getSuid(), this.mMemoBtn);
        }
        runSaveHistory(this.mLastWordPos);
        super.onMeanViewExtensionStart();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity
	public void onMeanViewReductionStart() {
        this.mGestures.setVisibility(View.VISIBLE);
        super.onMeanViewReductionStart();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isVisiableView(this.mSearchListEmptyLayout) && isVisiableView(this.mMainEmptyLayout)) {
            this.mMainEmptyLayout.setVisibility(View.GONE);
        }
        switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
                if (runKeyCodeBack()) {
                    return true;
                }
                if (this.mStandardInnerLeftLayout.getVisibility() == 8 || this.mLayoutMode == 1) {
                    LayoutTransition.updateLayoutWithExtends(false, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, this);
                    if (this.etSearch != null) {
                        this.etSearch.requestFocus();
                    }
                    return true;
                } else if (this.isWantFinnish || Dependency.isJapan()) {
                    TTSEngine.DestroyTTS();
                    this.mEngine.terminateEngine();
                    handleSaveMarkerObject();
                    System.runFinalizersOnExit(true);
                    System.exit(0);
                    finish();
                    break;
                } else {
                    this.mHandler.removeCallbacks(this.mFinishFlagInitialize);
                    this.isWantFinnish = true;
                    Toast.makeText(this, getString(R.string.app_exit_info), 0).show();
                    this.mHandler.postDelayed(this.mFinishFlagInitialize, 2000L);
                    return true;
                }
			case 19:
            case 20:
                return true;
            case 21:
            case 22:
                if (this.mMarkerColorChangePopup != null && this.mMarkerColorChangePopup.isShowing() && this.markerGroup != null) {
                    if (keyCode == 21) {
                        setFocusMarker(false);
                    } else {
                        setFocusMarker(true);
                    }
                }
                return true;
            case 23:
            case 66:
                if (this.etSearch == null || !this.etSearch.isFocused()) {
                    if ((this.mTextView != null && this.mTextView.isFocusable()) || (this.mMainMeanScrollView != null && this.mMainMeanScrollView.isFocusable())) {
                        LayoutTransition.updateLayoutWithExtends(true, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, this);
                        return true;
                    } else if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
                        this.mFileLinkTagViewManager.setFocusLinkTextPopup();
                        return true;
                    } else if (this.mChangeDictionaryBtn.isFocusable() || this.mChangeLanguageBtn.isFocusable() || this.etSearch.isFocusable()) {
                        LayoutTransition.updateLayoutWithExtends(false, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, this);
                        return true;
                    } else {
                        if (this.mMarkerColorChangePopup != null && this.mMarkerColorChangePopup.isShowing() && this.markerGroup != null) {
                            if (this.mMarkerCloseBtn.isFocused()) {
                                dismissMarkerColorChangePopup();
                                this.mMarkerBtn.requestFocus();
                                return true;
                            }
                            RadioButton view = (RadioButton) this.markerGroup.findFocus();
                            int[] colorList = getResources().getIntArray(R.array.value_marker_color_adv);
                            if (this.mTextView != null && view != null && !view.isChecked()) {
                                int viewIdx = ((Integer) view.getTag(view.getId())).intValue();
                                this.mTextView.setMarkerColor(colorList[viewIdx]);
                                view.setChecked(true);
                                if (viewIdx < 5) {
                                    DictUtils.setMarkerColorToPreference(this, viewIdx);
                                }
                                return true;
                            }
                        }
                        if (runKeyCodeEnter()) {
                            return true;
                        }
                    }
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return createMakeWordbookDialog();
            case 1:
                if (this.mProgressDialog == null) {
                    this.mProgressDialog = new ProgressDialog(this);
                }
                this.mProgressDialog.setMessage(getResources().getString(R.string.copying_flashcard_items));
                this.mProgressDialog.setIndeterminate(true);
                this.mProgressDialog.setCancelable(false);
                return this.mProgressDialog;
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_flashcard /* 2131100207 */:
                runFlashCardBtn(false);
                return true;
            case R.id.menu_history /* 2131100208 */:
                runHistoryBtn(false);
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

    protected void setProgressHandler() {
        this.mProgressHandler = new Handler() { // from class: com.diotek.diodict.SearchListActivity.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (SearchListActivity.mItemCopyState == 2) {
                            SearchListActivity.this.removeDialog(1);
                            if (SearchListActivity.this.mItemCopyResult == 0) {
                                SearchListActivity.this.showCopyToFlashcardLayout(false, true, true);
                                Toast.makeText(SearchListActivity.this, SearchListActivity.this.getResources().getString(R.string.savedWord), 0).show();
                            } else if (SearchListActivity.this.mItemCopyResult == 1) {
                                SearchListActivity.this.showCopyToFlashcardLayout(false, false, true);
                                Toast.makeText(SearchListActivity.this, SearchListActivity.this.getResources().getString(R.string.alreadyExistWord), 0).show();
                            }
                            SearchListActivity.this.mSaveBtn.setSelected(false);
                            SearchListActivity.this.mProgressHandler.removeCallbacks(SearchListActivity.this.copyDialogRunnable);
                            int unused = SearchListActivity.mItemCopyState = 0;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void prepareContentLayout() {
        prepareEmptyLayout();
        prepareMainLeftLayout();
        prepareMainRightLayout();
        initGestureAttribute();
        prepareChangeDictPopupLayout();
        prepareFlashcardPopupLayout();
    }

    private void prepareEmptyLayout() {
        this.mMainEmptyLayout = (RelativeLayout) findViewById(R.id.EmptyLayout);
        this.mMainEmptyLayout.setOnTouchListener(this.mMainEmptyOnTouchListener);
        this.mEmptyTitle = (TextView) findViewById(R.id.empty_title);
        this.mEmptyInfo = (TextView) findViewById(R.id.empty_info);
        this.mSearchListEmptyLayout = (LinearLayout) findViewById(R.id.SearchListEmptyLayout);
    }

    private void prepareLeftLayoutWeight(int configuration) {
        if (this.mMainLeftLayout != null && this.mStandardInnerLeftLayout != null) {
            float weight = 1.13f;
            if (configuration == 2) {
                weight = 1.5f;
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mMainLeftLayout.getLayoutParams();
            if (params != null) {
                params.weight = weight;
                this.mMainLeftLayout.requestLayout();
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) this.mStandardInnerLeftLayout.getLayoutParams();
                if (params2 != null) {
                    params2.weight = weight;
                    this.mStandardInnerLeftLayout.requestLayout();
                }
            }
        }
    }

    public void prepareMainLeftLayout() {
        this.mMainLeftLayout = (LinearLayout) findViewById(R.id.SearchContentInnerLeftLayout);
        prepareSearchMethodLayout();
        prepareInputLayout();
        prepareSearchListView();
    }

    public void prepareMainRightLayout() {
        this.mMainRightLayout = (LinearLayout) findViewById(R.id.SearchContentInnerRightLayout);
        this.mStandardInnerLeftLayout = (LinearLayout) findViewById(R.id.SearchContentStandardInnerLeftLayout);
        prepareMeanToolLayout();
        prepareMeanTabView();
        prepareMeanContentLayout();
        prepareMeanTTSLayout();
    }

    public void prepareInputLayout() {
        this.mChangeDictionaryBtn = (ImageButton) findViewById(R.id.ChangeDictionaryBtn);
        if (this.mEngine.getSupportMainDictionary().length > 1) {
            this.mChangeDictionaryBtn.setOnClickListener(this.mChangeDictionaryBtnOnClickListener);
        } else {
            this.mChangeDictionaryBtn.setVisibility(View.GONE);
            this.mEnableChangeDict = false;
        }
        this.mChangeLanguageBtn = (ImageButton) findViewById(R.id.ChangeLanguageBtn);
        this.mChangeLanguageBtn.setOnClickListener(this.mChangeLanguageBtnOnClickListener);
        this.etSearch = (DictEditText) findViewById(R.id.SearchEditText);
        this.mClearBtn = (ImageButton) findViewById(R.id.ClearBtn);
        this.mSearchWordBtn = (ImageButton) findViewById(R.id.SearchWordBtn);
        this.etSearch.addTextChangedListener(this.tw1);
//		etSearch.setClickable(false);
//		etSearch.setLongClickable(false);
        this.etSearch.setOnClickListener(this.mSearchEditTextOnClickListener);
        this.etSearch.setOnKeyListener(this.mSearchEditTextOnKeyListener);
        this.etSearch.setOnFocusChangeListener(this.mSearchEditTextOnFocusChangeListener);
        this.etSearch.enableInputType(true);
        this.mClearBtn.setOnClickListener(this.mClearBtnOnClickListener);
        this.mSearchWordBtn.setOnClickListener(this.mSearchWordBtnOnClickListener);
        showHideSearchWordBtn(8);
        prepareVoiceInputLayout();
        showHideChangeLanguage();
    }

    public void prepareGuideLayout() {
        if (DictUtils.isFirstLoadingFromPreference(this)) {
            RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.SearchContentRelativeLayout);
            LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
            RelativeLayout guideLayout = (RelativeLayout) inflate.inflate(R.layout.search_guide, (ViewGroup) null);
            mainLayout.addView(guideLayout);
            guideLayout.setOnTouchListener(new View.OnTouchListener() { // from class: com.diotek.diodict.SearchListActivity.4
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == 1) {
                        ((RelativeLayout) SearchListActivity.this.findViewById(R.id.search_guide_layout)).setVisibility(View.GONE);
                        SearchListActivity.this.showSoftInputMethod(true);
                    }
                    return true;
                }
            });
            return;
        }
        showSoftInputMethod(true);
    }

    private void prepareVoiceInputLayout() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0);
        this.mVoiceSearchBtn = (ImageButton) findViewById(R.id.VoiceSearchBtn);
        if (activities.size() != 0 && !DictDBManager.isOldKorDict(this.mEngine.getCurDict()) && !Build.MODEL.equals("sdk") && !Dependency.isChina()) {
            this.mVoiceSearchBtn.setVisibility(View.VISIBLE);
            this.mVoiceSearchBtn.setFocusable(true);
            this.mVoiceSearchBtn.setEnabled(true);
            this.mVoiceSearchBtn.setOnClickListener(this.mVoiceSearchOnClickListener);
            this.mUseVoiceSearch = true;
            return;
        }
        this.mVoiceSearchBtn.setBackgroundResource(R.drawable.searchedittext_end_f);
        this.mVoiceSearchBtn.setFocusable(false);
        this.mVoiceSearchBtn.setEnabled(false);
        this.mUseVoiceSearch = false;
    }

    public void prepareSearchMethodLayout() {
        LinearLayout searchMethodLayout = (LinearLayout) findViewById(R.id.SearchMethodLayout);
        this.mSearchMethodArrowBtn = (ImageView) searchMethodLayout.findViewById(R.id.searchMethodArrow);
        this.mCurrentSearchMethodTextView = (TextView) searchMethodLayout.findViewById(R.id.SearchMethodTextView);
        searchMethodLayout.setFocusable(true);
        searchMethodLayout.setClickable(true);
        searchMethodLayout.setOnClickListener(this.mSearchMethodOnClickListener);
        showHideSearchMethodLayout();
    }

    public void prepareSearchListView() {
        this.mSearchListView = (ListView) findViewById(R.id.SearchListView);
        this.mSearchListViewItems = new ArrayList<>();
        this.mSearchListViewAdapter = new WordListAdapter(this, R.layout.search_rowitem_layout, this.mSearchListViewItems);
        this.mSearchListViewTotalAdapter = new WordListAdapter(this, R.layout.search_rowitem_layout, this.mSearchListViewItems);
        if (this.mEngine.getCurDict() == 65520) {
            this.mSearchListView.setAdapter((ListAdapter) this.mSearchListViewTotalAdapter);
        } else {
            this.mSearchListView.setAdapter((ListAdapter) this.mSearchListViewAdapter);
        }
        this.mSearchListViewAdapter.setMemoOnClickListener(this.mMemoIconOnClickListener);
        this.mSearchListViewTotalAdapter.setMemoOnClickListener(this.mMemoIconOnClickListener);
        this.mSearchListView.setOnScrollListener(this.mSearchListViewOnScrollLisnter);
        this.mSearchListView.setOnItemClickListener(this.mSearchListViewOnItemClickLisner);
        this.mSearchListView.setOnItemSelectedListener(this.mSearchListViewOnItemSelectedListener);
    }

    public void prepareMeanToolLayout() {
        this.mMeanToolbarLayout = (LinearLayout) findViewById(R.id.MeanToolbarLayout);
        this.mMarkerBtn = (ImageButton) findViewById(R.id.MarkerBtn);
        this.mFontBtn = (ImageButton) findViewById(R.id.FontBtn);
        this.mMemoBtn = (ImageButton) findViewById(R.id.MemoBtn);
        this.mSaveBtn = (ImageButton) findViewById(R.id.SaveBtn);
        this.mMarkerBtn.setOnClickListener(this.mMarkerBtnOnClickListner);
        this.mFontBtn.setOnClickListener(this.mFontBtnOnClickListner);
        this.mMemoBtn.setOnClickListener(this.mMemoBtnOnClickListner);
        this.mSaveBtn.setOnClickListener(this.mSaveBtnOnClickListener);
    }

    public void prepareMeanContentLayout() {
        super.prepareMeanContentLayout(true);
        if (this.mMainMeanTitleTextView == null) {
            this.mMainMeanTitleTextView = (TextView) findViewById(R.id.MeanTitleTextView);
            this.mTextView = (ExtendTextView) findViewById(R.id.MeanContentTextView);
            this.mTextView.setOnTouchListener(this.mMeanTextViewOnTouchListener);
            this.mTextView.setOnFocusChangeListener(this.mMainMeanContentTextViewOnFocusChangeListener);
            this.mMeanContentBottomView = findViewById(R.id.MeanContentBottomView);
            this.mMeanContentBottomView.setOnTouchListener(this.mMeanContentBottomViewOnTouchListener);
            this.mMainMeanScrollView = (ExtendScrollView) findViewById(R.id.scrollview);
            this.mMainMeanScrollView.setOnFocusChangeListener(this.mMainMeanContentTextViewOnFocusChangeListener);
            this.mTextView.setFocusable(true);
            this.mMainMeanScrollView.setFocusable(false);
            this.mMeanContentBottomView.setFocusable(false);
        }
        prepareMeanContentLayout_byConfiguration(getResources().getConfiguration().orientation);
        this.mCopyToFlashcardLayout = (RelativeLayout) findViewById(R.id.copyToFlashcardPopLayout);
        if (this.mSearchMeanController == null) {
            ((RelativeLayout) findViewById(R.id.SearchRightLayout)).setOnTouchListener(this.mParentOnTouchListener);
            RelativeLayout parent = (RelativeLayout) findViewById(R.id.SearchContentRelativeLayout);
            LinearLayout parent_sub = (LinearLayout) findViewById(R.id.MeanContentLayout);
            this.mFileLinkTagViewManager = new FileLinkTagViewManager(this, this.mEngine, this.mTextView, parent, parent_sub, this.mThemeModeCallback);
            this.mSearchMeanController = new SearchMeanController(this, this.mMainMeanTitleTextView, this.mTextView, this.mMainMeanBookmarkImageView, this.mMeanTabView, this.mEngine, false, this.mThemeModeCallback, this.mFileLinkTagViewManager, this.mTTSLayoutCallback);
            this.mHyperSimpleViewModule = new HyperSimpleViewModule(this, this.mHyperSimpleViewModuleCallback, parent, parent_sub, this.mTextView);
            this.mSearchMeanController.setMeanContentTextViewCallback(this.mStartHyperCallback, this.mAutoUpdateLayoutCallback, true, null);
            this.mBaseMeanController = this.mSearchMeanController;
            return;
        }
        this.mSearchMeanController.setViews(this.mMainMeanBookmarkImageView, this.mMeanTabView);
    }

    public void prepareMeanController(int pos) {
        this.mSearchMeanController.setMeanViewByPos(pos, 0);
    }

    public void prepareMeanTTSLayout() {
        this.mUSOnceBtn = (Button) findViewById(R.id.USOnceBtn);
        this.mUSRepeatBtn = (ImageButton) findViewById(R.id.USRepeatBtn);
        this.mUKOnceBtn = (Button) findViewById(R.id.UKOnceBtn);
        this.mUKRepeatBtn = (ImageButton) findViewById(R.id.UKRepeatBtn);
        this.mUSOnceBtn.setOnClickListener(this.mTTSOnClickListner);
        this.mUSRepeatBtn.setOnClickListener(this.mTTSOnClickListner);
        this.mUKOnceBtn.setOnClickListener(this.mTTSOnClickListner);
        this.mUKRepeatBtn.setOnClickListener(this.mTTSOnClickListner);
        showHideTTSLayout(false);
    }

    @Override // com.diotek.diodict.ListMeanViewActivity
    public void prepareMeanTabView() {
        RelativeLayout meanright_layout = (RelativeLayout) findViewById(R.id.MeanContentRightLayout);
        if (meanright_layout != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
            meanright_layout.removeAllViews();
            if (getResources().getConfiguration().orientation == 1) {
                if (this.mPortMeanTabView == null) {
                    this.mPortMeanTabView = inflater.inflate(R.layout.mean_content_tabview_layout, (ViewGroup) null);
                }
                meanright_layout.addView(this.mPortMeanTabView);
            } else if (getResources().getConfiguration().orientation == 2) {
                if (this.mLandMeanTabView == null) {
                    this.mLandMeanTabView = inflater.inflate(R.layout.mean_content_tabview_layout, (ViewGroup) null);
                }
                meanright_layout.addView(this.mLandMeanTabView);
            }
        }
        super.prepareMeanTabView();
        prepareLeftLayoutWeight(getResources().getConfiguration().orientation);
        this.mMeanTabView.setOnClickListener(this.mMeanTabViewOnClickListener);
    }

    public void prepareChangeDictPopupLayout() {
        this.mChangeDictPopupLayout = (RelativeLayout) findViewById(R.id.gridviewbgLayout);
        this.mChangeDictPopupLayout.setOnClickListener(this.mChangeDictPopupOnClickListener);
        this.mChangeDictPopupViewFlipper = (ViewFlipper) findViewById(R.id.gridviewGrouplayout);
        RadioGroup pageDotLayout = (RadioGroup) findViewById(R.id.pageBarLayout);
        this.mChangeDictPopupGridView = new PageGridView(this, this.mChangeDictPopupViewFlipper, pageDotLayout);
        this.mChangeDictPopupGridView.setOnItemClickListener(this.mChangeDictPopupOnItemClickListener);
        this.mChangeDictPopupExitBtn = (ImageButton) findViewById(R.id.change_popup_titleexit);
        this.mChangeDictPopupExitBtn.setOnClickListener(this.mChangeDictPopupExitBtnOnClickListener);
        updateChangeDictPopupItems();
        this.mChangeDictPopupGridView.createChangeDictPageGridView(this.mChangeDictPopupItems, 6);
        this.mChangeDictPopupLayout.setVisibility(View.GONE);
    }

    public void prepareChangeDictLitePopupLayout() {
        this.mChangeDictPopupLayout = (RelativeLayout) findViewById(R.id.gridviewbgLayout);
        this.mChangeDictPopupLayout.setOnClickListener(this.mChangeDictPopupOnClickListener);
    }

    public void prepareFlashcardPopupLayout() {
        this.mFlashcardItemEditCopyToFlashcardOk = (TextImageButton) findViewById(R.id.button_ok);
        this.mFlashcardItemEditCopyToFlashcardCancel = (TextImageButton) findViewById(R.id.button_cancel);
        this.mFlashcardItemEditCopyToFlashcardOk.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardOkOnClickListener);
        this.mFlashcardItemEditCopyToFlashcardCancel.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardCancelOnClickListener);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mFlashcardItemEditCopyToFlashcardOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mFlashcardItemEditCopyToFlashcardCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        this.mAddWordbookTextView = (RelativeLayout) findViewById(R.id.addCard);
        this.mAddWordbookTextView.setOnClickListener(this.mAddWordbookTextViewOnCLickListener);
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        this.mFlashcardFolderListViewAdapter = new PopupFlashcardGridAdapter(this, this.mFlashcardFolderListViewItems, R.layout.flashcard_rowitem_s_copy_layout, from, to);
        this.mFlashcardGridView = (GridView) findViewById(R.id.copyToFlashcardGridView);
        this.mFlashcardGridView.setAdapter((ListAdapter) this.mFlashcardFolderListViewAdapter);
        this.mFlashcardGridView.setOnItemClickListener(this.mFlashcardGridViewOnItemClickListener);
    }

    public void runSearchListView(int nPos, boolean bAddHistory) {
        if (this.mTextView != null) {
            this.mTextView.forceScrollStop();
        }
        dismissMarkerColorChangePopup();
        runMeanTabView(0, false);
        selectTabAll();
        this.mSearchMeanController.setMeanViewByPos(nPos, 0);
        updateDisplayTabMenu();
        this.mLastWordPos = nPos;
        this.mOldResultPos = nPos;
        this.mOldResultKeyword = this.mEngine.getResultListKeywordByPos(nPos, 0);
        if (bAddHistory) {
            runSaveHistory(nPos);
        }
    }

    private void runSearchMeaning(int nPos) {
        dismissMarkerColorChangePopup();
        runOnlyMeanTabView(0, false);
        this.mSearchMeanController.setMeanViewByPos(nPos, 0);
        updateDisplayTabMenu();
        selectTabAll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runMarkerBtn() {
        if (this.mTextView != null && !this.mTextView.isMarkerMode()) {
            setClickableMeanToolBar(false);
            this.mTextView.clearSelection();
            showMarkerColorChangePopupMenu();
            this.mTextView.setMarkerMode(true);
        }
        showSoftInputMethod(false);
    }

    private void runOnlyMeanTabView(int nMode, boolean isRefresh) {
        handleSaveMarkerObject();
        this.mSearchMeanController.setDisplayMode(nMode);
        if (isRefresh) {
            this.mSearchMeanController.refreshContentView();
        }
    }

    public void runMeanTabView(int nMode, boolean isRefresh) {
        this.mTabViewPos = nMode;
        handleSaveMarkerObject();
        this.mSearchMeanController.setDisplayMode(nMode);
        if (isRefresh) {
            this.mSearchMeanController.refreshContentView();
        }
    }

    public void runClearBtn() {
        int nDicType = this.mEngine.getCurDict();
        if (DictDBManager.isKanjiDictionary(nDicType)) {
            this.mEngine.setDicType(EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(nDicType, false));
        }
        if (this.etSearch != null && this.etSearch.getText().length() > 0) {
            if (!this.mEngine.isRealTimeSearchSupport()) {
                directSearchMeaning("", true, false);
            } else {
                this.etSearch.setText("");
            }
        }
        this.etSearch.requestFocus();
    }

    public void runSearchWordBtn(boolean isHideSoftInput) {
        if (isHideSoftInput) {
            showSoftInputMethod(false);
        }
        String word = "";
        if (this.etSearch != null) {
            word = this.etSearch.getText().toString();
        }
        searchWord(word, true);
        runSearchListView(this.mEngine.getResultListKeywordPos(0), false);
    }

    public void runSearchWordBtnOnly() {
        showSoftInputMethod(false);
        String word = "";
        if (this.etSearch != null) {
            word = this.etSearch.getText().toString();
        }
        directSearchMeaning(word, true, true);
    }

    public void runSearchWordBtn(String word, int nPos) {
        searchWord(word, true);
        runSearchListView(nPos, false);
    }

    public void runSearchWordBtn(int nPos) {
        String word = "";
        if (this.etSearch != null) {
            word = this.etSearch.getText().toString();
        }
        runSearchWordBtn(word, nPos);
    }

    public void runSearchMethod(View v) {
        showSearchMethodChangePopupMenu();
    }

    public void runChangeDictPopup(int nPos) {
        int nRealPos = nPos + (this.mChangeDictPopupGridView.getCurrentPage() * 6);
        int nDictType = this.mEngine.getSupportMainDictionary()[nRealPos].intValue();
        if (nDictType == this.mEngine.getCurDict()) {
            runChangeDictPopupExitBtn();
            return;
        }
        initializeSearchEditText();
        changeDictionary(nDictType, false);
        if (nDictType == 65520) {
            this.mSearchListView.setAdapter((ListAdapter) this.mSearchListViewTotalAdapter);
            enableGesture(false);
        } else {
            enableGesture(true);
            this.mSearchListView.setAdapter((ListAdapter) this.mSearchListViewAdapter);
        }
        this.mCurrentSearchMethod = this.mEngine.getSearchMethod();
        updateSearchListViewItems(0);
        showHideSearchMethodLayout();
        setSearchWordBtnBySearchMethod(this.mEngine.getCurrentSearchMethodId());
        runChangeDictPopupExitBtn();
        setEnableSaveButton(true);
    }

    public void runListMemoBtn(int pos) {
        Intent intent = new Intent();
        intent.setClass(this, MemoActivity.class);
        intent.setFlags(603979776);
        String time_string = "";
        String data = "";
        int skin = 1;
        int dbtype = this.mEngine.getResultDicTypeByPos(pos, 0);
        String keyword = this.mEngine.getResultListKeywordByPos(pos, 0);
        int suid = this.mEngine.getResultListSUIDByPos(pos, 0);
        if (DioDictDatabase.existMemo(this, dbtype, keyword, suid)) {
            Cursor c = DioDictDatabase.getMemoCursorWith(this, dbtype, keyword, suid);
            if (c != null) {
                int nMemoIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_MEMO);
                int nTimeIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_TIME);
                int nSkinIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE);
                data = c.getString(nMemoIdx);
                long time = c.getLong(nTimeIdx);
                time_string = DictUtils.getDateString(time);
                skin = c.getInt(nSkinIdx);
                c.close();
            } else {
                return;
            }
        }
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_TIME, time_string);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_DATA, data);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_SKIN, skin);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_DICT, dbtype);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_WORD, keyword);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_SUID, suid);
        startActivityForResult(intent, 8);
        showSoftInputMethod(false);
    }

    public void runMemoBtn() {
        setClickableMeanToolBar(false);
        Intent intent = new Intent();
        intent.setClass(this, MemoActivity.class);
        intent.setFlags(603979776);
        String time_string = "";
        String data = "";
        int skin = 1;
        if (this.mTextView != null) {
            int dbtype = this.mTextView.getDbtype();
            String keyword = this.mTextView.getKeyword();
            int suid = this.mTextView.getSuid();
            if (DioDictDatabase.existMemo(this, dbtype, keyword, suid)) {
                Cursor c = DioDictDatabase.getMemoCursorWith(this, dbtype, keyword, suid);
                if (c != null) {
                    int nMemoIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_MEMO);
                    int nTimeIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_TIME);
                    int nSkinIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE);
                    data = c.getString(nMemoIdx);
                    long time = c.getLong(nTimeIdx);
                    time_string = DictUtils.getDateString(time);
                    skin = c.getInt(nSkinIdx);
                    c.close();
                } else {
                    return;
                }
            }
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_TIME, time_string);
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_DATA, data);
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_SKIN, skin);
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_DICT, dbtype);
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_WORD, keyword);
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_SUID, suid);
            startActivityForResult(intent, 8);
            showSoftInputMethod(false);
            return;
        }
        MSG.l(2, "runMemoBtn(): error ");
    }

    public void runFontBtn() {
        if (this.mTextView != null) {
            this.mTextView.clearSelection();
        }
        setClickableMeanToolBar(false);
        showFontSizeChangePopupMenu();
        showSoftInputMethod(false);
    }

    public void runSaveHistory(int position) {
        int DicType = this.mEngine.getResultDicTypeByPos(position, 0);
        String Word = this.mEngine.getResultListKeywordByPos(position, 0);
        if (Word != null) {
            int SUID = this.mEngine.getResultListSUIDByPos(position, 0);
            DioDictDatabase.addHistory(this, DicType, Word, SUID);
        }
    }

    public void runChangeDictPopupExitBtn() {
        setFocusableChangeDictionary(false);
        this.mChangeDictPopupLayout.setVisibility(View.GONE);
    }

    public boolean runKeyCodeBack() {
		if (clearTextViewSelection()) return true;
        if (this.mChangeDictPopupLayout.getVisibility() == 0) {
            setFocusableChangeDictionary(false);
            this.mChangeDictPopupLayout.setVisibility(View.GONE);
            return true;
        } else if (dismissFlashcardCopyPopup(true)) {
            return true;
        } else {
            if (isTTSRepeat()) {
                dismissTTSRepeat();
                return true;
            } else if (this.mTextView.gripShowing()) {
                initSelection();
                return true;
            } else if (this.mHyperSimpleViewModule != null && this.mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
                this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
                return true;
            } else if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
                this.mFileLinkTagViewManager.closeFileLinkPopup();
                return true;
            } else {
                return dismissMarkerColorChangePopup();
            }
        }
    }

    public boolean runKeyCodeEnter() {
        runSearchWordBtnOnly();
        return true;
    }

    public void runChangeDictionaryBtn() {
        initPopupControll();
        this.mChangeDictPopupLayout.bringToFront();
        this.mChangeDictPopupLayout.setVisibility(View.VISIBLE);
        showSoftInputMethod(false);
        this.mChangeDictPopupGridView.notifyAllGridViewAdapter();
        this.mHandler.postDelayed(this.setFocusableChangeDictionary, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFocusableChangeDictionary(boolean bFocus) {
        setFocusableSearchActivity(!bFocus);
        if (this.mChangeDictPopupViewFlipper != null) {
            this.mChangeDictPopupViewFlipper.setFocusable(false);
        }
        this.mChangeDictPopupExitBtn.setFocusable(bFocus);
        if (bFocus) {
            this.mChangeDictPopupGridView.getWordbookFolderGridView(this.mChangeDictPopupGridView.getCurrentPage()).requestFocus();
        }
    }

    public void runSearchEditText() {
        this.mHandler.post(this.mSwingBackUpdateLayoutCallback);
        dismissMarkerColorChangePopup();
        this.etSearch.requestFocus();
        showSoftInputMethod(true);
    }

    public void runSaveBtn() {
        initSelection();
        setClickableMeanToolBar(false);
        showFlashcardListPop(false);
        showSoftInputMethod(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runnableMeanTabView() {
        dismissMarkerColorChangePopup();
        if (this.mTextView != null) {
            this.mTextView.stopInvilidate();
        }
        runMeanTabView(this.mTabViewPos, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentKeywordMeaning() {
        int pos = this.mEngine.getResultListKeywordPos(0);
        String keyword = this.mEngine.getResultListKeywordByPos(pos, 0);
        if (keyword != null) {
            if (this.mOldResultKeyword == null || this.mOldResultPos != pos || keyword.compareTo(this.mOldResultKeyword) != 0) {
                this.mOldResultPos = pos;
                this.mOldResultKeyword = keyword;
                this.mLastWordPos = pos;
                runSearchMeaning(pos);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startVoiceRecognition() {
        if (1024 == this.mEngine.getCurrentSearchMethodId()) {
            showVoiceRecognitionLanguageSelectDialog();
        } else {
            startVoiceRecognitionActivity();
        }
    }

    protected void showVoiceRecognitionLanguageSelectDialog() {
        if (this.mVoiceLanguageDlg == null || !this.mVoiceLanguageDlg.isShowing()) {
            CharSequence[] language = buildSupportDictionaryLanguageList();
            AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
            Dialog.setCancelable(true);
            Dialog.setNegativeButton(R.string.cancel, this.mVoiceLanguageDialogCancelListener);
            Dialog.setSingleChoiceItems(language, 0, this.mVoiceLanguageDialogClickListener);
            Dialog.setTitle(getResources().getString(R.string.totalsearch_language_select));
            this.mVoiceLanguageDlg = Dialog.create();
            Window win = this.mVoiceLanguageDlg.getWindow();
            WindowManager.LayoutParams wlp = win.getAttributes();
            wlp.type = 1003;
            win.setAttributes(wlp);
            this.mVoiceLanguageDlg.show();
        }
    }

    private CharSequence[] buildSupportDictionaryLanguageList() {
        this.mVoiceRecognitionLanguageItems.clear();
        Integer[] nDicTypes = this.mEngine.getSupportDictionary();
        for (Integer num : nDicTypes) {
            String language = DictDBManager.getDictVoiceLang(num.intValue());
            if (!this.mVoiceRecognitionLanguageItems.contains(language)) {
                this.mVoiceRecognitionLanguageItems.add(language);
            }
        }
        CharSequence[] languageList = new CharSequence[this.mVoiceRecognitionLanguageItems.size()];
        for (int j = 0; j < this.mVoiceRecognitionLanguageItems.size(); j++) {
            languageList[j] = getStrLanguageCountry(this.mVoiceRecognitionLanguageItems.get(j));
        }
        return languageList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSelectedSaveToFlashcardFolder() {
        if (this.mCheckedWordbookList == null) {
            return false;
        }
        for (int i = 0; i < this.mCheckedWordbookList.length; i++) {
            if (this.mCheckedWordbookList[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean runSaveFlashcardItem() {
        if (this.mCheckedWordbookList == null) {
            return false;
        }
        boolean[] nCheckedWordbookList = new boolean[this.mCheckedWordbookList.length];
        for (int i = 0; i < this.mCheckedWordbookList.length; i++) {
            nCheckedWordbookList[i] = this.mCheckedWordbookList[i];
        }
        Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(this);
        if (tCursor == null) {
            return false;
        }
        boolean wordSaved = false;
        boolean wordExist = false;
        tCursor.moveToFirst();
        int dicType = this.mSearchMeanController.getDicType();
        String word = this.mSearchMeanController.getWord();
        int suid = this.mSearchMeanController.getSuid();
        for (int i2 = 0; i2 < nCheckedWordbookList.length; i2++) {
            if (nCheckedWordbookList[i2]) {
                tCursor.moveToPosition(i2);
                int wbnameId = tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
                if (DioDictDatabase.addWordbookItem(this, dicType, word, suid, wbnameId) == 2) {
                    wordExist = true;
                } else {
                    wordSaved = true;
                }
            }
        }
        if (wordSaved) {
            this.mItemCopyResult = 0;
        } else if (!wordSaved && wordExist) {
            this.mItemCopyResult = 1;
        }
        tCursor.close();
        mItemCopyState = 2;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTerminatePopup() {
        dismissMarkerColorChangePopup();
        dismissFlashcardCopyPopup(false);
        dismissFontSizeChangePopup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PopupTouchInterceptor implements View.OnTouchListener {
        private PopupTouchInterceptor() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if ((action != 0 || SearchListActivity.this.mMarkerColorChangePopup == null || !SearchListActivity.this.mMarkerColorChangePopup.isShowing()) && action == 1) {
            }
            if (action == 4 && SearchListActivity.this.mMarkerColorChangePopup != null && SearchListActivity.this.mMarkerColorChangePopup.isShowing()) {
                SearchListActivity.this.mMarkerColorChangePopup.dismiss();
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GesturesProcessor implements GestureOverlayView.OnGestureListener {
        private GesturesProcessor() {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            SearchListActivity.this.mHandler.removeCallbacks(SearchListActivity.this.runInitGestureLayout);
            if (event.getAction() == 0) {
                SearchListActivity.this.showSoftInputMethod(false);
                SearchListActivity.this.extendGestureLayout();
            }
            float drawX = event.getX();
            float drawY = event.getY();
            if (SearchListActivity.this.mGuestureDetector.getInkCounter() > 0) {
                SearchListActivity.this.mHandler.removeCallbacks(SearchListActivity.this.mHwrGuestureDetector);
            } else {
                SearchListActivity.this.mIsGesture = false;
            }
            SearchListActivity.this.mGuestureDetector.addPoint((short) drawX, (short) drawY);
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
            float drawX = event.getX();
            float drawY = event.getY();
            if (SearchListActivity.this.mGuestureDetector.getInkCounter() > 0) {
                SearchListActivity.this.mHandler.removeCallbacks(SearchListActivity.this.mHwrGuestureDetector);
            }
            SearchListActivity.this.mGuestureDetector.addPoint((short) drawX, (short) drawY);
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            SearchListActivity.this.mGuestureDetector.endStroke();
            if (!SearchListActivity.this.mIsGesture) {
                SearchListActivity.this.mHandler.postDelayed(SearchListActivity.this.runInitGestureLayout, SearchListActivity.this.mGestureRecognitionDelayTime);
                SearchListActivity.this.mCandiBox.hide();
            }
            if (SearchListActivity.this.mGuestureDetector.recognizeGestureBackSpace()) {
                SearchListActivity.this.mGestures.cancelGesture();
                SearchListActivity.this.initGestureLayout();
                SearchListActivity.this.handleDelete();
                SearchListActivity.this.mIsGesture = false;
                return;
            }
            SearchListActivity.this.onRecognizeStart();
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecognizeStart() {
        this.mHandler.removeCallbacks(this.mHwrGuestureDetector);
        this.mHandler.postDelayed(this.mHwrGuestureDetector, this.mGestureRecognitionDelayTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStringToEdit(String recogString) {
        int startPos;
        StringBuilder oldStr = new StringBuilder(this.etSearch.getText().toString());
        int startPos2 = Math.min(this.etSearch.getSelectionStart(), this.etSearch.getSelectionEnd());
        int endPos = Math.max(this.etSearch.getSelectionStart(), this.etSearch.getSelectionEnd());
        boolean bPreviousTextExist = oldStr.toString().length() > 0;
        boolean bOldKorDict = DictDBManager.isOldKorDict(this.mEngine.getCurDict());
        boolean bOverMaxText = oldStr.toString().length() >= 30;
        boolean bSelectTextExist = startPos2 != endPos;
        if (bPreviousTextExist && !bOldKorDict) {
            if (bOverMaxText) {
                maximumStringWarnningMessageShow();
                this.mIsGesture = false;
                return;
            }
            if (bSelectTextExist) {
                oldStr.delete(startPos2, endPos);
                oldStr.insert(startPos2, recogString);
                startPos = startPos2 + recogString.length();
                this.etSearch.setText(oldStr.toString());
            } else {
                oldStr.insert(startPos2, recogString);
                startPos = startPos2 + recogString.length();
                this.etSearch.setText(oldStr.toString());
            }
            this.etSearch.setSelection(startPos);
            return;
        }
        this.etSearch.setText(recogString);
        if (!bOldKorDict) {
            this.etSearch.setSelection(this.etSearch.getText().toString().length());
        }
    }

    private void initializeSearchEditText() {
        if (this.etSearch != null) {
            this.etSearch.setText("");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchWordOnTextInput(String word) {
        if (this.mEngine.isRealTimeSearchSupport() && !this.isRealTimeSearchStop) {
            if (!this.mWillSearchText.equals(word)) {
                this.mHandler.removeCallbacks(this.mUpdateMeanView);
                this.mHandler.removeCallbacks(this.mSearchWordRunnable);
                this.mWillSearchText = word;
                if (DictDBManager.getCpCHNDictionary(this.mEngine.getCurDict())) {
                    if (this.mEngine.getCurrentSearchMethodId() == 1 && DictUtils.getCodePage(word) == 0) {
                        changeSearchMethod(R.id.SearchMethodPinyin, getResources().getString(R.string.pinyin_search));
                    } else if (this.mEngine.getCurrentSearchMethodId() == 512 && DictUtils.getCodePage(word) == 936) {
                        this.mEngine.setDicType(DictDBManager.getOriginalDicTypeByNotIndependenceDicType(this.mEngine.getCurDict()));
                        changeSearchMethod(R.id.SearchMethodWord, getResources().getString(R.string.word_search));
                    }
                }
                this.mHandler.postDelayed(this.mSearchWordRunnable, 500L);
            } else {
                return;
            }
        }
        if (this.isRealTimeSearchStop) {
            this.isRealTimeSearchStop = false;
        }
    }

    public boolean searchWord(String word, boolean isUpdateList) {
        if (word == null) {
            return false;
        }
        String searchWord = null;
        if (Dependency.isJapan()) {
            searchWord = convertRegularWord(word);
        }
        if (searchWord == null) {
            searchWord = word;
        }
        this.mPreLastWordFromEngine = "";
        this.mLastSearchWord = searchWord;
        boolean bRet = this.mEngine.searchByCheckWildChar(searchWord, searchWord, 0);
        if (isUpdateList) {
            updateSearchListViewItems(0);
        }
        return bRet;
    }

    public boolean searchWord(String word, int suid, String originalWord, int nResultListType) {
        if (word == null) {
            return false;
        }
        String searchWord = null;
        if (Dependency.isJapan()) {
            searchWord = convertRegularWord(word);
        }
        if (searchWord == null) {
            searchWord = word;
        }
        this.mPreLastWordFromEngine = "";
        this.mLastSearchWord = searchWord;
        boolean bRet = this.mEngine.searchByCheckWildChar(searchWord, suid, originalWord, nResultListType);
        if (nResultListType == 0) {
            updateSearchListViewItems(nResultListType);
        }
        return bRet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchPrevList() {
        String enginefirstword = this.mEngine.getEngineFirstWord();
        String curfirstword = this.mEngine.getResultList(0).getWordList(0).getKeyword();
        if (enginefirstword != null && curfirstword != null) {
            if (enginefirstword == null || !curfirstword.equals(enginefirstword)) {
                int nSUID = this.mEngine.getResultList(0).getWordList(0).getSUID();
                if (searchWord(curfirstword, nSUID, this.etSearch.getText().toString(), 0)) {
                    this.mPreLastWordFromEngine = curfirstword;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchNextList() {
        int pos = this.mSearchListViewItems.size() - 1;
        String enginelastword = this.mEngine.getEngineLastWord();
        String curlastword = this.mEngine.getResultList(0).getWordList(pos).getKeyword();
        if (enginelastword != null && curlastword != null) {
            if (this.mPreLastWordFromEngine == null || !curlastword.equals(this.mPreLastWordFromEngine)) {
                if (enginelastword == null || !curlastword.equals(enginelastword)) {
                    int nSUID = this.mEngine.getResultList(0).getWordList(pos).getSUID();
                    if (searchWord(curlastword, nSUID, this.etSearch.getText().toString(), 0)) {
                        this.mPreLastWordFromEngine = curlastword;
                    }
                }
            }
        }
    }

    public boolean updateSearchListViewItems(int nResultListType) {
        int maxItems = this.mEngine.getResultListCount(nResultListType);
        this.mSearchListViewItems.clear();
        this.mTotalSearchPosition.clear();
        if (maxItems > 0) {
            int prevDicType = -1;
            showHideEmptyLayout(false);
            int i = 0;
            while (i < maxItems) {
                HashMap<String, Object> item = new HashMap<>();
                if (this.mEngine.getCurDict() == 65520) {
                    this.mTotalSearchPosition.add(Integer.valueOf(i));
                    int dictype = this.mEngine.getResultDicTypeByPos(i, 0);
                    if (prevDicType != dictype) {
                        item.put(DictInfo.ListItem_Header, DictDBManager.getDictName(dictype));
                        this.mSearchListViewItems.add(item);
                        prevDicType = dictype;
                        i--;
                        i++;
                    }
                }
                item.put(DictInfo.ListItem_Keyword, this.mEngine.getResultList(nResultListType).getWordList(i).getKeyword());
                item.put(DictInfo.ListItem_DictType, Integer.valueOf(this.mEngine.getResultListDictByPos(i, 0)));
                item.put("suid", Integer.valueOf(this.mEngine.getResultListSUIDByPos(i, 0)));
                this.mSearchListViewItems.add(item);
                i++;
            }
            this.mSearchListViewAdapter.notifyDataSetChanged();
            this.mSearchListViewTotalAdapter.notifyDataSetChanged();
            this.mSearchListView.setSelection(this.mEngine.getResultListKeywordPos(nResultListType));
            return true;
        }
        showHideEmptyLayout(true);
        this.mSearchListViewAdapter.notifyDataSetChanged();
        this.mSearchListViewTotalAdapter.notifyDataSetChanged();
        return false;
    }

    public int getEnableSearchMethod(LinearLayout layout) {
        int active_method = 0;
        TextView tvWord = (TextView) layout.findViewById(R.id.SearchMethodWord);
        TextView tvPhrase = (TextView) layout.findViewById(R.id.SearchMethodPhrase);
        TextView tvExample = (TextView) layout.findViewById(R.id.SearchMethodExample);
        TextView tvHangulro = (TextView) layout.findViewById(R.id.SearchMethodHangulro);
        TextView tvSpellCheck = (TextView) layout.findViewById(R.id.SearchMethodSpellCheck);
        TextView tvInitial = (TextView) layout.findViewById(R.id.SearchMethodInitial);
        TextView tvPinyin = (TextView) layout.findViewById(R.id.SearchMethodPinyin);
        TextView tvOldKor = (TextView) layout.findViewById(R.id.SearchMethodOldKor);
        TextView tvTotal = (TextView) layout.findViewById(R.id.SearchMethodTotal);
        int current_search_method = this.mEngine.getCurrentSearchMethodId();
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView view = (TextView) layout.getChildAt(i);
            view.setSelected(false);
            view.setVisibility(View.GONE);
        }
        for (int i2 = 0; i2 < this.mEngine.getSupportSearchMethodInfo().length; i2++) {
            EngineManager3rd.SearchMethodInfo searchMethodInfo = this.mEngine.getSupportSearchMethodInfo()[i2];
            switch (searchMethodInfo.getId()) {
                case 1:
                    int nVisible = getEnableMethod(tvWord.getId());
                    tvWord.setVisibility(nVisible);
                    if (nVisible != 0) {
                        break;
                    } else {
                        if (current_search_method == 1) {
                            tvWord.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 2:
                    int nVisible2 = getEnableMethod(tvPhrase.getId());
                    tvPhrase.setVisibility(nVisible2);
                    if (nVisible2 != 0) {
                        break;
                    } else {
                        if (current_search_method == 2) {
                            tvPhrase.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 4:
                    int nVisible3 = getEnableMethod(tvExample.getId());
                    tvExample.setVisibility(nVisible3);
                    if (nVisible3 != 0) {
                        break;
                    } else {
                        if (current_search_method == 4) {
                            tvExample.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 8:
                    int nVisible4 = getEnableMethod(tvHangulro.getId());
                    tvHangulro.setVisibility(nVisible4);
                    if (nVisible4 != 0) {
                        break;
                    } else {
                        if (current_search_method == 8) {
                            tvHangulro.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 16:
                    int nVisible5 = getEnableMethod(tvSpellCheck.getId());
                    tvSpellCheck.setVisibility(nVisible5);
                    if (nVisible5 != 0) {
                        break;
                    } else {
                        if (current_search_method == 16) {
                            tvSpellCheck.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 32:
                    int nVisible6 = getEnableMethod(tvInitial.getId());
                    tvInitial.setVisibility(nVisible6);
                    if (nVisible6 != 0) {
                        break;
                    } else {
                        if (current_search_method == 32) {
                            tvInitial.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 64:
                    int nVisible7 = getEnableMethod(tvOldKor.getId());
                    tvOldKor.setVisibility(nVisible7);
                    if (nVisible7 != 0) {
                        break;
                    } else {
                        if (current_search_method == 64) {
                            tvOldKor.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case DictType.SEARCHTYPE_PINYIN /* 512 */:
                    int nVisible8 = getEnableMethod(tvPinyin.getId());
                    tvPinyin.setVisibility(nVisible8);
                    if (nVisible8 != 0) {
                        break;
                    } else {
                        if (current_search_method == 512) {
                            tvPinyin.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
                case 1024:
                    int nVisible9 = getEnableMethod(tvTotal.getId());
                    tvTotal.setVisibility(nVisible9);
                    if (nVisible9 != 0) {
                        break;
                    } else {
                        if (current_search_method == 1024) {
                            tvTotal.setSelected(true);
                        }
                        active_method++;
                        break;
                    }
            }
        }
        return active_method;
    }

    public void showHideSearchMethodLayout() {
        int visible = 0;
        int cnt = 0;
        for (int i = 0; i < this.mEngine.getSupportSearchMethodInfo().length; i++) {
            EngineManager3rd.SearchMethodInfo searchMethodInfo = this.mEngine.getSupportSearchMethodInfo()[i];
            switch (searchMethodInfo.getId()) {
                case 1:
                    this.mSearchMethodWord = searchMethodInfo;
                    cnt++;
                    if (this.mCurrentSearchMethodTextView != null) {
                        this.mCurrentSearchMethodTextView.setText(getResources().getString(R.string.word_search));
                        break;
                    }
                    break;
                case 2:
                    int nIdiomDicType = DictDBManager.getExampleDicTypeByCurDicType(this.mEngine.getCurDict());
                    String szIdiomDBName = DictInfo.DBPATH + DictDBManager.getDictFilename(nIdiomDicType) + DictInfo.DBEXTENSION;
                    if (DictUtils.checkExistFile(szIdiomDBName)) {
                        this.mSearchMethodIdiom = searchMethodInfo;
                        cnt++;
                        break;
                    }
                    break;
                case 4:
                    int nExampleDicType = DictDBManager.getExampleDicTypeByCurDicType(this.mEngine.getCurDict());
                    String szExampleDBName = DictInfo.DBPATH + DictDBManager.getDictFilename(nExampleDicType) + DictInfo.DBEXTENSION;
                    if (DictUtils.checkExistFile(szExampleDBName)) {
                        this.mSearchMethodExample = searchMethodInfo;
                        cnt++;
                        break;
                    }
                    break;
                case 8:
                    if (!Dependency.isGoAbroad()) {
                        this.mSearchMethodHangulro = searchMethodInfo;
                        cnt++;
                        break;
                    }
                    break;
                case 16:
                    int nOriginalDictType = EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(this.mEngine.getCurDict(), false);
                    String szSpellCheckDBName = DictInfo.DBPATH + DictDBManager.getDictSpellCheckFileName(nOriginalDictType) + DictInfo.DBEXTENSION;
                    if (DictUtils.checkExistFile(szSpellCheckDBName)) {
                        this.mSearchMethodSpellCheck = searchMethodInfo;
                        cnt++;
                        break;
                    }
                    break;
                case 32:
                    this.mSearchMethodInitial = searchMethodInfo;
                    cnt++;
                    break;
                case 64:
                    this.mSearchMethodOldKor = searchMethodInfo;
                    cnt++;
                    break;
                case DictType.SEARCHTYPE_PINYIN /* 512 */:
                    this.mSearchMethodPinyin = searchMethodInfo;
                    cnt++;
                    break;
                case 1024:
                    this.mSearchMethodTotal = searchMethodInfo;
                    if (this.mEngine.getSupportSearchMethodInfo().length == 1) {
                        this.mEngine.setSearchMethod(this.mSearchMethodTotal);
                        if (this.mCurrentSearchMethodTextView != null) {
                            this.mCurrentSearchMethodTextView.setText(getResources().getString(R.string.total_search));
                        }
                        visible = 8;
                        setSearchWordBtnBySearchMethod(this.mEngine.getCurrentSearchMethodId());
                        cnt = 1;
                        break;
                    }
                    break;
            }
            if (this.mSearchMethodArrowBtn != null) {
                this.mSearchMethodArrowBtn.setVisibility(visible);
            }
            if (cnt == 1) {
                ((LinearLayout) findViewById(R.id.SearchMethodLayout)).setClickable(false);
                ((LinearLayout) findViewById(R.id.SearchMethodLayout)).setFocusable(false);
                this.mSearchMethodArrowBtn.setVisibility(View.GONE);
            } else {
                ((LinearLayout) findViewById(R.id.SearchMethodLayout)).setClickable(true);
                ((LinearLayout) findViewById(R.id.SearchMethodLayout)).setFocusable(true);
                this.mSearchMethodArrowBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showHideSearchWordBtn(int nVisible) {
        boolean bVisible = nVisible == 0;
        if (this.mSearchWordBtn != null) {
            this.mSearchWordBtn.setVisibility(nVisible);
            this.mSearchWordBtn.setFocusable(bVisible);
        }
        if (this.mVoiceSearchBtn != null && !this.mUseVoiceSearch) {
            if (bVisible) {
                this.mVoiceSearchBtn.setVisibility(View.GONE);
                this.mVoiceSearchBtn.setEnabled(false);
                this.mVoiceSearchBtn.setFocusable(false);
                this.mSearchWordBtn.setBackgroundResource(R.drawable.searchbtn_end);
                return;
            }
            this.mVoiceSearchBtn.setVisibility(View.VISIBLE);
            this.mVoiceSearchBtn.setEnabled(false);
            this.mVoiceSearchBtn.setFocusable(false);
            this.mVoiceSearchBtn.setBackgroundResource(R.drawable.searchedittext_end_f);
        }
    }

    private void setEnableCommonButton(boolean isEnable) {
        setEnableTTSButton(isEnable);
        setClickableMeanToolBar(isEnable);
    }

    public void showHideEmptyLayout(boolean nVisible) {
        boolean z = true;
        if (nVisible) {
            setEmptyInfomation();
            if (this.mMainEmptyLayout != null) {
                if (this.mEngine.getCurrentSearchMethodId() == 1) {
                    this.mMainEmptyLayout.setVisibility(View.GONE);
                    this.mTextView.setFocusable(true);
                } else {
                    this.mMainEmptyLayout.setVisibility(View.VISIBLE);
                    this.mTextView.setFocusable(false);
                }
            }
            if (this.mSearchListEmptyLayout != null && this.mSearchListEmptyLayout.getVisibility() == 8) {
                this.mSearchListEmptyLayout.setVisibility(View.VISIBLE);
                this.mSearchListEmptyLayout.setFocusable(false);
            }
        } else {
            if (this.mMainEmptyLayout != null && this.mMainEmptyLayout.getVisibility() == 0) {
                this.mMainEmptyLayout.setVisibility(View.GONE);
            }
            if (this.mSearchListEmptyLayout != null && this.mSearchListEmptyLayout.getVisibility() == 0) {
                this.mSearchListEmptyLayout.setVisibility(View.GONE);
            }
        }
        if (nVisible) {
            z = false;
        }
        setEnableCommonButton(z);
    }

    private void setEmptyInfomation() {
        int titleResId = R.string.hangulro_search;
        int infoResId = R.string.hangulro_info;
        int method = this.mEngine.getCurrentSearchMethodId();
        switch (method) {
            case 2:
                titleResId = R.string.idiom_title;
                infoResId = R.string.idiom_info;
                break;
            case 4:
                titleResId = R.string.example_search;
                infoResId = R.string.example_info;
                break;
            case 8:
                titleResId = R.string.hangulro_search;
                if (DictDBManager.getCpJPNDictionary(this.mEngine.getCurDict())) {
                    infoResId = R.string.hangulro_jpn_info;
                    break;
                } else if (DictDBManager.getCpCHNDictionary(this.mEngine.getCurDict())) {
                    infoResId = R.string.hangulro_chn_info;
                    break;
                } else {
                    infoResId = R.string.hangulro_info;
                    break;
                }
            case 16:
                titleResId = R.string.spellcheck_search;
                infoResId = R.string.spellcheck_info;
                break;
            case 32:
                titleResId = R.string.initial_search;
                infoResId = R.string.initial_info;
                break;
            case 64:
                titleResId = R.string.oldkor_search;
                infoResId = R.string.oldkor_info;
                break;
            case DictType.SEARCHTYPE_PINYIN /* 512 */:
                titleResId = R.string.pinyin_search;
                infoResId = R.string.pinyin_info;
                break;
            case 1024:
                titleResId = R.string.total_search;
                infoResId = R.string.totalsearch_info;
                break;
        }
        if (this.mEmptyTitle != null) {
            this.mEmptyTitle.setText(getResources().getString(titleResId));
        }
        if (this.mEmptyInfo != null) {
            this.mEmptyInfo.setText(getResources().getString(infoResId));
        }
    }

    public void setClickableMeanToolBar(boolean bClickable) {
        if (this.mMarkerBtn != null) {
            this.mMarkerBtn.setClickable(bClickable);
        }
        if (this.mFontBtn != null) {
            this.mFontBtn.setClickable(bClickable);
        }
        if (this.mMemoBtn != null) {
            this.mMemoBtn.setClickable(bClickable);
        }
        if (this.mSaveBtn != null) {
            this.mSaveBtn.setClickable(bClickable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnableSaveButton(boolean bEnable) {
        this.mSaveBtn.setEnabled(bEnable);
        this.mMarkerBtn.setEnabled(bEnable);
        this.mMemoBtn.setEnabled(bEnable);
    }

    public void setSearchWordBtnBySearchMethod(int nSearchMethod) {
        if (nSearchMethod == 1 || nSearchMethod == 512) {
            showHideSearchWordBtn(8);
        } else {
            showHideSearchWordBtn(0);
        }
    }

    public void changeDictionaryOnly(int nDictType, boolean bChangeMean) {
        changeDictionaryAndSetDictName(nDictType);
        DictUtils.setSearchLastDictToPreference(this, nDictType);
        showHideChangeLanguage();
    }

    public void changeDictionary(int nDictType, boolean bChangeMean) {
        changeDictionaryAndSetDictName(nDictType);
        DictUtils.setSearchLastDictToPreference(this, nDictType);
        if (nDictType != 65520) {
            this.mSearchMeanController.changeDicType(nDictType);
        }
        runSearchWordBtn(true);
        showHideChangeLanguage();
        if (this.mLayoutMode != 0) {
            setSmallMeanView();
        }
    }

    public void changeDictionaryAndSetDictName(int nDicType) {
        this.mEngine.changeDictionary(nDicType, "", -1);
        DictInfo.mCurrentDBName = DictDBManager.getDictName(this.mEngine.getCurDict());
        setSearchDBName();
        if (this.etSearch != null) {
            this.etSearch.setHint(DictDBManager.getCurDictHint(this.mEngine.getCurDict()));
        }
    }

    private void setChangeLanguagBtn(int nDicType) {
        if (this.mEnableChangeDict) {
            this.mChangeLanguageBtn.setBackgroundResource(R.drawable.changelang);
        } else {
            this.mChangeLanguageBtn.setBackgroundResource(R.drawable.changelang_left_eng);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeSearchMethod(int searchMethodId, String searchMethodName) {
        int visible = 0;
        boolean bEnableSaveBtn = true;
        switch (searchMethodId) {
            case R.id.SearchMethodWord /* 2131100097 */:
                this.mCurrentSearchMethod = this.mSearchMethodWord;
                int nDicType = this.mEngine.getCurDict();
                if (DictDBManager.isPinyinDictionary(nDicType)) {
                    this.mEngine.setDicType(DictDBManager.getOriginalDicTypeByNotIndependenceDicType(nDicType));
                    break;
                }
                break;
            case R.id.SearchMethodPhrase /* 2131100098 */:
                bEnableSaveBtn = false;
                this.mCurrentSearchMethod = this.mSearchMethodIdiom;
                break;
            case R.id.SearchMethodExample /* 2131100099 */:
                bEnableSaveBtn = false;
                this.mCurrentSearchMethod = this.mSearchMethodExample;
                break;
            case R.id.SearchMethodHangulro /* 2131100100 */:
                this.mCurrentSearchMethod = this.mSearchMethodHangulro;
                initializeSearchEditText();
                break;
            case R.id.SearchMethodSpellCheck /* 2131100101 */:
                this.mCurrentSearchMethod = this.mSearchMethodSpellCheck;
                break;
            case R.id.SearchMethodInitial /* 2131100102 */:
                this.mCurrentSearchMethod = this.mSearchMethodInitial;
                initializeSearchEditText();
                break;
            case R.id.SearchMethodPinyin /* 2131100103 */:
                this.mCurrentSearchMethod = this.mSearchMethodPinyin;
                break;
            case R.id.SearchMethodOldKor /* 2131100104 */:
                this.mCurrentSearchMethod = this.mSearchMethodOldKor;
                break;
            case R.id.SearchMethodTotal /* 2131100105 */:
                this.mCurrentSearchMethod = this.mSearchMethodTotal;
                visible = 8;
                break;
        }
        this.mEngine.setSearchMethod(this.mCurrentSearchMethod);
        setEnableSaveButton(bEnableSaveBtn);
        if (this.mCurrentSearchMethodTextView != null) {
            this.mCurrentSearchMethodTextView.setText(searchMethodName);
            this.mCurrentSearchMethodTextView.invalidate();
        }
        if (this.mSearchMethodArrowBtn != null) {
            this.mSearchMethodArrowBtn.setVisibility(visible);
        }
        setSearchWordBtnBySearchMethod(this.mEngine.getCurrentSearchMethodId());
        runSearchWordBtn(false);
        if (this.mSearchMethodChangePopup != null && this.mSearchMethodChangePopup.isShowing()) {
            this.mSearchMethodChangePopup.dismiss();
            this.mSearchMethodChangePopup = null;
        }
        this.mLastWordPos = this.mEngine.getResultListKeywordPos(0);
    }

    public void updateDisplayTabMenu() {
        this.mMeanTabView.getButton(2).setEnabled(this.mSearchMeanController.isAvailable_Idiom());
        this.mMeanTabView.getButton(3).setEnabled(this.mSearchMeanController.isAvailable_Example());
    }

    private void saveToMemoDB(Intent intent) {
        String memoDataString = intent.getExtras().getString(DictInfo.INTENT_MEMO_INFO_DATA);
        int skin = intent.getExtras().getInt(DictInfo.INTENT_MEMO_INFO_SKIN);
        if (memoDataString != null && memoDataString.length() >= 0) {
            int dbtyp = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_DICT, 1);
            String keyword = intent.getStringExtra(DictInfo.INTENT_MEMO_INFO_WORD);
            int suid = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_SUID, 1);
            if (memoDataString.length() == 0) {
                DioDictDatabase.deleteMemo(this, dbtyp, keyword, suid);
                Toast.makeText(this, getResources().getText(R.string.memo_deleted), 0).show();
            } else {
                DioDictDatabase.addMemo(this, dbtyp, keyword, suid, memoDataString, skin);
                Toast.makeText(this, getResources().getText(R.string.memo_saved), 0).show();
            }
            if (this.mTextView != null) {
                UITools.prepareMemoSkin(this, this.mTextView.getDbtype(), this.mTextView.getKeyword(), this.mTextView.getSuid(), this.mMemoBtn);
            }
            if (this.mSearchListView != null) {
                this.mSearchListView.invalidateViews();
            }
        }
    }

    public void showFontSizeChangePopupMenu() {
        int[] windowLocation = new int[2];
        RelativeLayout parents = (RelativeLayout) findViewById(R.id.SearchRightLayout);
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        RelativeLayout PopupContent = (RelativeLayout) inflate.inflate(R.layout.fontsize_select_popup, (ViewGroup) null);
        RadioGroup group = (RadioGroup) PopupContent.findViewById(R.id.font_group);
        parents.getLocationInWindow(windowLocation);
        float density = CommonUtils.getDeviceDensity(this);
        int popupWidth = getResources().getConfiguration().orientation == 1 ? (int) ((313.3f * density) + 0.5f) : (int) ((337.3f * density) + 0.5f);
        ImageView view = (ImageView) PopupContent.findViewById(R.id.font_popup_bg_view);
        int popupHeight = view.getBackground().getIntrinsicHeight();
        int popupX = parents.getWidth() - popupWidth;
        if (this.mFontSizeChangePopup == null) {
            this.mFontSizeChangePopup = CommonUtils.makeWindowWithPopupWindow(this, 0, PopupContent, getResources().getDrawable(R.drawable.popup_back), this.mOnDismissListener);
            for (int i = 0; i < group.getChildCount(); i++) {
                ((RadioButton) group.getChildAt(i)).setOnClickListener(this.mFontSizeChangeOnClickListener);
            }
            ((ImageView) PopupContent.findViewById(R.id.font_close)).setOnClickListener(this.mFontSizeChangeOnClickListener);
        }
        int fontSizeIndex = setFontSizeFromPreference();
        ((RadioButton) group.getChildAt(fontSizeIndex)).setChecked(true);
        if (this.mFontSizeChangePopup != null) {
            if (this.mFontSizeChangePopup.isShowing()) {
                this.mFontSizeChangePopup.update(windowLocation[0] + popupX, windowLocation[1] + 0, popupWidth, popupHeight);
                return;
            }
            this.mFontSizeChangePopup.setWidth(popupWidth);
            this.mFontSizeChangePopup.setHeight(popupHeight);
            this.mFontSizeChangePopup.showAtLocation(parents, 0, windowLocation[0] + popupX, windowLocation[1] + 0);
        }
    }

    private int setFontSizeFromPreference(boolean isStoreCurrentTopOffset) {
        int fontSizeIndex = DictUtils.getFontSizeFromPreference(this);
        int[] fontSizeList = getResources().getIntArray(R.array.value_font_size);
        this.mSearchMeanController.setMeanContentTextViewTextSize(fontSizeList[fontSizeIndex], isStoreCurrentTopOffset);
        return fontSizeIndex;
    }

    private int setFontSizeFromPreference() {
        return setFontSizeFromPreference(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean dismissMarkerColorChangePopup() {
        if (this.mMarkerColorChangePopup == null || !this.mMarkerColorChangePopup.isShowing()) {
            return false;
        }
        this.mMarkerColorChangePopup.dismiss();
        this.mMarkerColorChangePopup = null;
        this.mTextView.setMarkerMode(false);
        setFocusableSearchActivity(true);
        return true;
    }

    private boolean dismissFlashcardCopyPopup(boolean bAnimation) {
        if (this.mCopyToFlashcardLayout.getVisibility() == 0) {
            showCopyToFlashcardLayout(false, false, bAnimation);
            this.mCopyToFlashcardLayout.setVisibility(View.GONE);
            this.mSaveBtn.setSelected(false);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean dismissFontSizeChangePopup() {
        if (this.mFontSizeChangePopup == null || !this.mFontSizeChangePopup.isShowing()) {
            return false;
        }
        this.mFontSizeChangePopup.dismiss();
        return true;
    }

    public void showMarkerColorChangePopupMenu() {
        int[] colorList = getResources().getIntArray(R.array.value_marker_color_adv);
        int[] windowLocation = new int[2];
        RelativeLayout parents = (RelativeLayout) findViewById(R.id.SearchRightLayout);
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        RelativeLayout PopupContent = (RelativeLayout) inflate.inflate(R.layout.marker_color_select_popup, (ViewGroup) null);
        this.markerGroup = (RadioGroup) PopupContent.findViewById(R.id.marker_group);
        parents.getLocationInWindow(windowLocation);
        parents.setOnTouchListener(this.layoutOnTouchListener);
        float density = CommonUtils.getDeviceDensity(this);
        int popupWidth = getResources().getConfiguration().orientation == 1 ? (int) ((313.3f * density) + 0.5f) : (int) ((337.3f * density) + 0.5f);
        ImageView view = (ImageView) PopupContent.findViewById(R.id.marker_popup_bg_view);
        int popupHeight = view.getBackground().getIntrinsicHeight();
        int popupX = parents.getWidth() - popupWidth;
        if (this.mMarkerColorChangePopup == null) {
            this.mMarkerColorChangePopup = CommonUtils.makeWindowWithPopupWindow(this, 0, PopupContent, null, this.mOnDismissListener, false);
            for (int i = 0; i < this.markerGroup.getChildCount(); i++) {
                RadioButton btn = (RadioButton) this.markerGroup.getChildAt(i);
                btn.setTag(btn.getId(), Integer.valueOf(i));
                btn.setOnClickListener(this.mMarkerColorChangeOnClickListener);
                btn.setFocusable(true);
            }
            this.mMarkerCloseBtn = (ImageView) PopupContent.findViewById(R.id.marker_close);
            this.mMarkerCloseBtn.setOnClickListener(this.mMarkerColorChangeOnClickListener);
            this.mMarkerCloseBtn.setTag(this.mMarkerCloseBtn.getId(), 6);
            this.mMarkerCloseBtn.setFocusable(true);
            this.mMarkerColorChangePopup.setOutsideTouchable(true);
            this.mMarkerColorChangePopup.setTouchInterceptor(new PopupTouchInterceptor());
        } else {
            this.markerGroup.clearCheck();
        }
        int markerColorIndex = DictUtils.getMarkerColorFromPreference(this);
        ((RadioButton) this.markerGroup.getChildAt(markerColorIndex)).setChecked(true);
        ((RadioButton) this.markerGroup.getChildAt(markerColorIndex)).requestFocusFromTouch();
        this.mSearchMeanController.setMeanContentTextViewMarkerColor(colorList[markerColorIndex]);
        if (this.mMarkerColorChangePopup != null) {
            if (this.mMarkerColorChangePopup.isShowing()) {
                this.mMarkerColorChangePopup.update(windowLocation[0] + popupX, windowLocation[1] + 0, popupWidth, popupHeight);
            } else {
                this.mMarkerColorChangePopup.setWidth(popupWidth);
                this.mMarkerColorChangePopup.setHeight(popupHeight);
                this.mMarkerColorChangePopup.showAtLocation(parents, 0, windowLocation[0] + popupX, windowLocation[1] + 0);
            }
        }
        setFocusableSearchActivity(false);
    }

    private void setFocusMarker(boolean bRight) {
        View view = this.markerGroup.findFocus();
        if (view == null) {
            if (this.mMarkerCloseBtn.isFocused()) {
                View view2 = this.mMarkerCloseBtn;
                if (!bRight) {
                    this.markerGroup.getChildAt(this.markerGroup.getChildCount() - 1).requestFocus();
                    return;
                }
                return;
            }
            this.markerGroup.getChildAt(0).requestFocusFromTouch();
            this.markerGroup.getChildAt(0).setFocusableInTouchMode(false);
            return;
        }
        int idx = ((Integer) view.getTag(view.getId())).intValue();
        if (bRight) {
            if (idx >= 0 && idx < this.markerGroup.getChildCount() - 1) {
                this.markerGroup.getChildAt(idx + 1).requestFocus();
            } else if (idx == this.markerGroup.getChildCount() - 1) {
                int i = idx + 1;
                this.mMarkerCloseBtn.requestFocus();
            }
        } else if (idx <= this.markerGroup.getChildCount() && idx > 0) {
            this.markerGroup.getChildAt(idx - 1).requestFocus();
        }
    }

    protected void onSelectColor(int position) {
        int[] colorList = getResources().getIntArray(R.array.value_marker_color);
        this.mTextView.setMarkerColor(colorList[position]);
    }

    protected void handleSaveMarkerObject() {
        if (this.mTextView != null) {
            this.mTextView.saveMarkerObject();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSoftInputMethod(boolean isShow) {
        if (isShow) {
            initSelection();
            this.etSearch.requestFocus();
        }
        showHideSystemInputMethod(isShow);
    }

    private void showHideSystemInputMethod(boolean bShow) {
        if (bShow) {
            this.mHandler.postDelayed(this.mShowOnlySoftInput, 300L);
        } else {
            this.mHandler.post(this.mHideOnlySoftInput);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startVoiceRecognitionActivityWithLanguage(String language) {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        String prompt = getResources().getString(R.string.voice_input_prompt);
        intent.putExtra("android.speech.extra.LANGUAGE", language);
        intent.putExtra("android.speech.extra.PROMPT", prompt);
        intent.putExtra("android.speech.extra.MAX_RESULTS", 10);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        String language = EngineInfo3rd.getInputLangaugeStr(this.mEngine.getCurDict(), this.mEngine.getCurrentSearchMethodId());
        String prompt = getResources().getString(R.string.voice_input_prompt);
        intent.putExtra("android.speech.extra.LANGUAGE", language);
        intent.putExtra("android.speech.extra.PROMPT", prompt);
        intent.putExtra("android.speech.extra.MAX_RESULTS", 10);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    private String getStrLanguageCountry(String language) {
        String lanCountry = getResources().getString(R.string.voice_lang_eng);
        if (Locale.KOREA.toString().equals(language)) {
            String lanCountry2 = getResources().getString(R.string.voice_lang_kor);
            return lanCountry2;
        } else if (Locale.JAPAN.toString().equals(language)) {
            String lanCountry3 = getResources().getString(R.string.voice_lang_jpn);
            return lanCountry3;
        } else if (Locale.CHINA.toString().equals(language)) {
            String lanCountry4 = getResources().getString(R.string.voice_lang_chn);
            return lanCountry4;
        } else if (Locale.GERMANY.toString().equals(language)) {
            String lanCountry5 = getResources().getString(R.string.voice_lang_ger);
            return lanCountry5;
        } else if (Locale.FRANCE.toString().equals(language)) {
            String lanCountry6 = getResources().getString(R.string.voice_lang_fra);
            return lanCountry6;
        } else {
            return lanCountry;
        }
    }

    public void showVoiceRecognizeResult(ArrayList<String> matches) {
        this.mVoiceRecognizeMatches = matches;
        AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setCancelable(true);
        Dialog.setPositiveButton(R.string.retry, this.mVoiceRecognizePositiveOnClickListener);
        Dialog.setNegativeButton(R.string.cancel, this.mVoiceRecognizeNegativeOnClickListener);
        Dialog.setAdapter(new ArrayAdapter(this, (int) R.layout.simple_list, matches), this.mVoiceRecognizeListOnClickListener);
        Dialog.setTitle(getResources().getString(R.string.voice_result));
        AlertDialog dlg = Dialog.create();
        Window win = dlg.getWindow();
        WindowManager.LayoutParams wlp = win.getAttributes();
        wlp.type = 1003;
        win.setAttributes(wlp);
        dlg.show();
    }

    public void updateChangeDictPopupItems() {
        this.mChangeDictPopupItems.clear();
        Integer[] nDicTypes = this.mEngine.getSupportMainDictionary();
        for (int i = 0; i < nDicTypes.length; i++) {
            HashMap<String, Object> changeDictPopupRow = new HashMap<>();
            changeDictPopupRow.put(DictInfo.ListItem_DictIcon, DictDBManager.getDictIcon(nDicTypes[i].intValue()));
            changeDictPopupRow.put(DictInfo.ListItem_DictType, nDicTypes[i]);
            this.mChangeDictPopupItems.add(changeDictPopupRow);
        }
    }

    public int checkExtraDBType(int dicType, String word) {
        int codepage = DictUtils.getCodePage(word);
        if (DictDBManager.isKanjiDictionary(dicType) && codepage != 936) {
            return DictDBManager.getOriginalDicTypeByNotIndependenceDicType(dicType);
        }
        return dicType;
    }

    public void AutoChangeLanguage(String word, boolean bChangeListMean, boolean bCompulsion) {
        int nDicType;
        if ((DictDBManager.getDictAutoChange(this.mEngine.getCurDict()) || DictDBManager.isExtraDBType(this.mEngine.getCurDict())) && (nDicType = checkExtraDBType(DictDBManager.getAutoDicType(this.mEngine.getCurDict(), bCompulsion, word), word)) != this.mEngine.getCurDict()) {
            changeDictionaryOnly(nDicType, bChangeListMean);
            if (bChangeListMean) {
                updateSearchListViewItems(0);
                this.mHandler.postDelayed(this.mUpdateMeanView, 500L);
            }
            showHideSearchMethodLayout();
        }
    }

    private void maximumStringWarnningMessageShow() {
        Toast.makeText(this, getResources().getString(R.string.maximum_string_input), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDelete() {
        StringBuilder oldStr = new StringBuilder(this.etSearch.getText().toString());
        if (oldStr.toString().length() > 0) {
            int startPos = Math.min(this.etSearch.getSelectionStart(), this.etSearch.getSelectionEnd());
            int endPos = Math.max(this.etSearch.getSelectionStart(), this.etSearch.getSelectionEnd());
            if (startPos != endPos) {
                oldStr.delete(startPos, endPos);
            } else if (startPos != 0) {
                startPos--;
                oldStr.deleteCharAt(startPos);
            }
            this.etSearch.setText(oldStr.toString());
            this.etSearch.setSelection(startPos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHandWritingArea() {
        Display display = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        RelativeLayout parents = (RelativeLayout) findViewById(R.id.InputLayout);
        parents.measure(0, 0);
        int otherHeight = parents.getHeight();
        this.mHwrRectArea.set(0, otherHeight, display.getWidth(), display.getHeight() - otherHeight);
        this.mGuestureDetector.setWritingArea(this.mHwrRectArea);
    }

    private void initializeHwrRect() {
        if (this.mHwrRectArea != null) {
            this.mHwrRectArea.set(-1, -1, -1, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHwrRectInit() {
        return !this.mHwrRectArea.isEmpty();
    }

    private void initHandWritingDelayTime() {
        this.mGestureRecognitionDelayTime = DictUtils.getRecogTimeFromPreference(this);
        if (this.mGestureRecognitionDelayTime < 300) {
            this.mGestureRecognitionDelayTime = DictUtils.DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE;
        }
    }

    private void initHandWriting() {
        if (this.mGuestureDetector == null) {
            this.mGuestureDetector = new DioGuestureDetector();
        }
        if (this.mGuestureDetector != null) {
            this.mGuestureDetector.setContext(this);
            this.mGuestureDetector.Create();
        }
        initHandWritingDelayTime();
        initializeHwrRect();
        this.mIsGesture = false;
    }

    private void initGestureAttribute() {
        if (Dependency.isContainHandWrightReocg()) {
            if (this.mLibrary == null) {
                this.mLibrary = GestureLibraries.fromRawResource(this, R.raw.actions);
                if (!this.mLibrary.load()) {
                    finish();
                }
            }
            this.mGestures = (GestureOverlayView) findViewById(R.id.gestures);
            initHandWriting();
            this.mGestures.removeAllOnGestureListeners();
            this.mGestures.addOnGesturePerformedListener(this.mGesturePerformeListener);
            this.mGestures.setOnTouchListener(this.mGestureOnTouchListener);
            this.mGestures.addOnGestureListener(new GesturesProcessor());
            this.mGestures.setBackgroundColor(16777215);
            this.mGestures.setGestureColor(getResources().getColor(R.color.gesture_color));
            this.mGestures.setDrawingCacheBackgroundColor(16777215);
            this.mGestures.setAlwaysDrawnWithCacheEnabled(true);
            this.mGestures.setFadeOffset(this.mGestureRecognitionDelayTime);
            enableGesture(true);
            return;
        }
        this.mGestures = (GestureOverlayView) findViewById(R.id.gestures);
        enableGesture(false);
    }

    private void enableGesture(boolean isEnable) {
        if (isEnable && Dependency.isContainHandWrightReocg()) {
            boolean isUsed = DictUtils.getGestureRecognitionFromPreference(this);
            if (isUsed) {
                if (this.mGestures == null) {
                    initGestureAttribute();
                    return;
                }
                this.mGestures.setEnabled(true);
                this.mGestures.setUncertainGestureColor(getResources().getColor(R.color.gesture_uncertain_color));
            } else if (this.mGestures != null) {
                this.mGestures.setEnabled(false);
                this.mGestures.setUncertainGestureColor(0);
            }
        } else if (this.mGestures != null) {
            this.mGestures.setEnabled(false);
            this.mGestures.setUncertainGestureColor(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extendGestureLayout() {
        if (this.mGestures != null && !this.mGestureLayoutUpdate) {
            this.mGestureLayoutUpdate = true;
            this.mGestures.bringToFront();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initGestureLayout() {
        if (this.mGestures != null) {
            this.mGestureLayoutUpdate = false;
            LinearLayout parents = (LinearLayout) findViewById(R.id.SearchContentStandardRightLayout);
            parents.bringToFront();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int showCopyToFlashcardLayout(boolean isShow, boolean sticker, boolean bAnimation) {
        boolean z = true;
        this.mFlashcardItemEditCopyToFlashcardOk.setEnabled(isShow);
        this.mFlashcardItemEditCopyToFlashcardOk.setClickable(isShow);
        this.mFlashcardItemEditCopyToFlashcardCancel.setEnabled(isShow);
        this.mFlashcardItemEditCopyToFlashcardCancel.setClickable(isShow);
        if (isShow) {
            setClickableMeanToolBar(false);
            this.mCopyToFlashcardLayout.setVisibility(View.VISIBLE);
        } else {
            setClickableMeanToolBar(true);
        }
        this.mIsShowFlashcardPop = isShow;
        if (bAnimation) {
            LayoutTransition.trasition(this.mCopyToFlashcardLayout, isShow, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 250, false, !isShow);
        } else if (isShow) {
            this.mCopyToFlashcardLayout.setVisibility(View.VISIBLE);
        } else {
            this.mCopyToFlashcardLayout.setVisibility(View.GONE);
        }
        if (sticker) {
            this.mHandler.postDelayed(this.mRunTiffanyStiker, (long) SEARCH_TIME_DELAY);
        }
        if (isShow) {
            z = false;
        }
        setFocusableSearchActivity(z);
        if (isShow) {
            if (this.mFlashcardFolderListViewItems.isEmpty()) {
                this.mAddWordbookTextView.requestFocus();
            } else {
                this.mFlashcardGridView.requestFocus();
            }
        } else {
            this.mSaveBtn.requestFocus();
        }
        return 250;
    }

    public void showFlashcardListPop(boolean existCheckedList) {
        ((TextView) this.mCopyToFlashcardLayout.findViewById(R.id.copyToFlashcardPopTitle)).setText(getResources().getString(R.string.selectFlashcardToSave));
        this.mFlashcardItemEditCopyToFlashcardOk = (TextImageButton) findViewById(R.id.button_ok);
        this.mFlashcardItemEditCopyToFlashcardCancel = (TextImageButton) findViewById(R.id.button_cancel);
        this.mFlashcardItemEditCopyToFlashcardOk.setText(R.string.ok);
        this.mFlashcardItemEditCopyToFlashcardCancel.setText(R.string.cancel);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mFlashcardItemEditCopyToFlashcardOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mFlashcardItemEditCopyToFlashcardCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        this.mFlashcardItemEditCopyToFlashcardOk.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardOkOnClickListener);
        this.mFlashcardItemEditCopyToFlashcardCancel.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardCancelOnClickListener);
        this.mAddWordbookTextView = (RelativeLayout) findViewById(R.id.addCard);
        this.mAddWordbookTextView.setOnClickListener(this.mAddWordbookTextViewOnCLickListener);
        ((TextView) this.mAddWordbookTextView.getChildAt(0)).setText(R.string.addFlashcard);
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        this.mFlashcardFolderListViewAdapter = new PopupFlashcardGridAdapter(this, this.mFlashcardFolderListViewItems, R.layout.flashcard_rowitem_s_copy_layout, from, to);
        this.mFlashcardGridView = (GridView) findViewById(R.id.copyToFlashcardGridView);
        this.mFlashcardGridView.setAdapter((ListAdapter) this.mFlashcardFolderListViewAdapter);
        this.mFlashcardGridView.setOnItemClickListener(this.mFlashcardGridViewOnItemClickListener);
        this.mCursor = DioDictDatabase.getWordbookFolderCursor(this);
        updateWordbookFolderItems(existCheckedList);
        showCopyToFlashcardLayout(true, false, true);
    }

    public void makeWordbook() {
        int nCount = DioDictDatabase.getWordbookFolderCount(this);
        if (nCount >= 40) {
            Toast.makeText(this, (int) R.string.alreadyMaxWordbook, 0).show();
            return;
        }
        showDialog(0);
        this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        this.mEdittextWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
        this.mInputWordbookNameTextView = (TextView) this.mWordbookDialog.findViewById(R.id.editview_editwordbook);
        ImageButton clearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
        if (clearBtn != null) {
            clearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
        }
        this.mWordbookDialog.show();
    }

    public Dialog createMakeWordbookDialog() {
        if (this.mWordbookDialog != null) {
            removeDialog(0);
        }
        String defaultName = getDefaultWordbookName();
        this.mWordbookType = 1;
        this.mWordbookDialog = new Dialog(this);
        this.mWordbookDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mWordbookDialog.requestWindowFeature(1);
        this.mWordbookDialog.setContentView(R.layout.flashcard_makedialog_layout);
        this.mWordbookDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.diotek.diodict.SearchListActivity.72
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                SearchListActivity.this.runBtnMakeWordbookCancel(null);
                SearchListActivity.this.mAddWordbookTextView.setSelected(false);
            }
        });
        EditText tv = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        this.mCard1 = (RadioButton) this.mWordbookDialog.findViewById(R.id.card1);
        this.mCard2 = (RadioButton) this.mWordbookDialog.findViewById(R.id.card2);
        this.mCard3 = (RadioButton) this.mWordbookDialog.findViewById(R.id.card3);
        this.mCard1.setOnCheckedChangeListener(this.mCard1OnCheckedChangeListener);
        this.mCard2.setOnCheckedChangeListener(this.mCard2OnCheckedChangeListener);
        this.mCard3.setOnCheckedChangeListener(this.mCard3OnCheckedChangeListener);
        this.mCard1.setOnFocusChangeListener(this.mCardOnFocusChangedListner);
        this.mCard2.setOnFocusChangeListener(this.mCardOnFocusChangedListner);
        this.mCard3.setOnFocusChangeListener(this.mCardOnFocusChangedListner);
        this.mCard1.setChecked(true);
        tv.setText(defaultName);
        tv.setSelection(defaultName.length());
        this.mBtnMakeWordbookOk = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_ok);
        this.mBtnMakeWordbookOk.setOnClickListener(this.mBtnMakeWordbookOkOnClickListener);
        this.mBtnMakeWordbookCancel = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_cancel);
        this.mBtnMakeWordbookCancel.setOnClickListener(this.mBtnMakeWordbookCancelOnClickListener);
        this.mBackupCardName = defaultName;
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mBtnMakeWordbookOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mBtnMakeWordbookCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        return this.mWordbookDialog;
    }

    public void runBtnMakeWordbookOk(View v) {
        if (runMakeWordbookOK()) {
            updateWordbookFolderItems(false);
            removeDialog(0);
        }
    }

    public void runBtnMakeWordbookCancel(View v) {
        removeDialog(0);
    }

    public boolean runMakeWordbookOK() {
        if (DioDictDatabase.getWordbookFolderCount(this) >= 40) {
            Toast.makeText(this, (int) R.string.alreadyMaxWordbook, 0).show();
            return false;
        }
        if (this.mEdittextWordbookName == null) {
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        }
        this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
        if (this.mInputWordbookName.equals("")) {
            Toast.makeText(this, (int) R.string.input_wordbookname, 0).show();
            return false;
        } else if (DioDictDatabase.addWordbookFolder(this, this.mInputWordbookName, this.mWordbookType) == 2) {
            Toast.makeText(this, getResources().getString(R.string.alreadyExistWordbook), 0).show();
            return false;
        } else {
            return true;
        }
    }

    public void updateWordbookFolderItems(boolean existCheckedList) {
        if (this.mFlashcardFolderListViewItems != null) {
            this.mFlashcardFolderListViewItems.clear();
        }
        Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(this);
        TextView emptyFlashcardTextView = (TextView) this.mCopyToFlashcardLayout.findViewById(R.id.emptyFlashcard);
        emptyFlashcardTextView.setText(R.string.empty_flashcard);
        if (tCursor == null) {
            emptyFlashcardTextView.setVisibility(View.VISIBLE);
            this.mCheckedWordbookList = null;
            return;
        }
        emptyFlashcardTextView.setVisibility(View.GONE);
        if (!existCheckedList) {
            this.mCheckedWordbookList = new boolean[tCursor.getCount()];
        }
        do {
            int i = tCursor.getPosition();
            HashMap<String, Object> mFlashcardRow = new HashMap<>();
            mFlashcardRow.put(DictInfo.ListItem_WordbookName, tCursor.getString(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME)));
            mFlashcardRow.put(DictInfo.ListItem_WordCount, Integer.valueOf(DioDictDatabase.getWordbookItemCount(this, tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID)))));
            mFlashcardRow.put(DictInfo.ListItem_WordbookFolderId, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID))));
            mFlashcardRow.put(DictInfo.ListItem_WordbookFolderType, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE))));
            if (existCheckedList) {
                mFlashcardRow.put(DictInfo.ListItem_WordbookFolderChecked, Boolean.valueOf(this.mCheckedWordbookList[i]));
            } else {
                mFlashcardRow.put(DictInfo.ListItem_WordbookFolderChecked, false);
            }
            addRowToFlashcardArrayList(i, mFlashcardRow);
        } while (tCursor.moveToNext());
        tCursor.close();
        if (this.mFlashcardFolderListViewAdapter != null) {
            this.mFlashcardFolderListViewAdapter.notifyDataSetChanged();
        }
    }

    public void addRowToFlashcardArrayList(int CursorPos, HashMap<String, Object> row) {
        this.mFlashcardFolderListViewItems.add(row);
    }

    public void setSearchDBName() {
        this.mSearchDBNameTextView.setText(DictInfo.mCurrentDBName);
        if (this.etSearch != null) {
            this.etSearch.setHint(DictDBManager.getCurDictHint(this.mEngine.getCurDict()));
        }
    }

    public void setFocusableSearchActivity(boolean bFocus) {
        this.mChangeDictionaryBtn.setFocusable(bFocus);
        ((LinearLayout) findViewById(R.id.SearchMethodLayout)).setFocusable(bFocus);
        this.mChangeLanguageBtn.setFocusable(bFocus);
        this.mSearchWordBtn.setFocusable(bFocus);
        this.mVoiceSearchBtn.setFocusable(bFocus);
        this.mMarkerBtn.setFocusable(bFocus);
        this.mFontBtn.setFocusable(bFocus);
        this.mMemoBtn.setFocusable(bFocus);
        this.mSaveBtn.setFocusable(bFocus);
        if (this.mUSOnceBtn != null) {
            this.mUSOnceBtn.setFocusable(bFocus);
        }
        if (this.mUSRepeatBtn != null) {
            this.mUSRepeatBtn.setFocusable(bFocus);
        }
        if (this.mUKOnceBtn != null) {
            this.mUKOnceBtn.setFocusable(bFocus);
        }
        if (this.mUKRepeatBtn != null) {
            this.mUKRepeatBtn.setFocusable(bFocus);
        }
        this.mSearchListView.setFocusable(bFocus);
        for (int i = 0; i < this.mMeanTabView.getTotalCount(); i++) {
            this.mMeanTabView.getButton(i).setFocusable(bFocus);
        }
        this.etSearch.setFocusable(bFocus);
        this.etSearch.setFocusableInTouchMode(bFocus);
        this.mClearBtn.setFocusable(bFocus);
        if (this.mSearchListEmptyLayout.getVisibility() != 0) {
            this.mTextView.setFocusable(bFocus);
            this.mTextView.setFocusableInTouchMode(bFocus);
        }
    }

    private void initPopupControll() {
        dismissMarkerColorChangePopup();
        initSelection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSelection() {
        if (this.mTextView != null) {
            this.mTextView.clearSelection();
            this.mTextView.invalidate();
        }
    }

    public int getEnableMethod(int nViewId) {
        switch (nViewId) {
            case R.id.SearchMethodWord /* 2131100097 */:
                if (this.mSearchMethodWord != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodPhrase /* 2131100098 */:
                if (this.mSearchMethodIdiom != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodExample /* 2131100099 */:
                if (this.mSearchMethodExample != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodHangulro /* 2131100100 */:
                if (this.mSearchMethodHangulro != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodSpellCheck /* 2131100101 */:
                if (this.mSearchMethodSpellCheck != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodInitial /* 2131100102 */:
                if (this.mSearchMethodInitial != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodPinyin /* 2131100103 */:
                if (this.mSearchMethodPinyin != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodOldKor /* 2131100104 */:
                if (this.mSearchMethodOldKor != null) {
                    return 0;
                }
                break;
            case R.id.SearchMethodTotal /* 2131100105 */:
                if (this.mSearchMethodTotal != null) {
                    return 0;
                }
                break;
        }
        return 8;
    }

    public void showSearchMethodChangePopupMenu() {
        int[] windowLocation = new int[2];
        RelativeLayout parents = (RelativeLayout) findViewById(R.id.SearchLeftLayout);
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        CustomPopupLinearLayout PopupContent = (CustomPopupLinearLayout) inflate.inflate(R.layout.search_method_popup, (ViewGroup) null);
        LinearLayout layout = (LinearLayout) PopupContent.findViewById(R.id.search_method_top_layout);
        parents.getLocationInWindow(windowLocation);
        int popupWidth = this.mMainLeftLayout.getMeasuredWidth();
        int popupHeight = (int) getResources().getDimension(R.dimen.searchmethod_height);
        if (this.mSearchMethodChangePopup == null) {
            PopupContent.setOnKeyListenerCallback(this.mCustomPopupLinearLayoutOnKeyListenerCallback);
            this.mSearchMethodChangePopup = CommonUtils.makeWindowWithPopupWindow(this, 0, PopupContent, getResources().getDrawable(R.drawable.popup_back), null);
            for (int i = 0; i < layout.getChildCount(); i++) {
                TextView tv = (TextView) layout.getChildAt(i);
                if (tv != null) {
                    tv.setOnClickListener(this.mSearchMethodChangeOnClickListener);
                    tv.setFocusable(true);
                }
            }
            PopupContent.setOnClickListener(this.mPopupOnClickListener);
        }
        int actives = getEnableSearchMethod(layout);
        if (actives == 1) {
            this.mSearchMethodChangePopup.dismiss();
            this.mSearchMethodChangePopup = null;
            return;
        }
        this.mSearchMethodChangePopup.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.SearchListActivity.74
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                SearchListActivity.this.mSearchMethodChangePopup = null;
            }
        });
        int popupHeight2 = (popupHeight * actives) + 25;
        if (this.mSearchMethodChangePopup != null) {
            if (this.mSearchMethodChangePopup.isShowing()) {
                this.mSearchMethodChangePopup.update(windowLocation[0] + 0, windowLocation[1] + 0, popupWidth, popupHeight2);
                return;
            }
            this.mSearchMethodChangePopup.setWidth(popupWidth);
            this.mSearchMethodChangePopup.setHeight(popupHeight2);
            this.mSearchMethodChangePopup.showAtLocation(parents, 0, windowLocation[0] + 0, windowLocation[1] + 0);
        }
    }

    private void restoreDicType(boolean bSearchWord) {
        this.mEngine.setSearchMethod(this.mCurrentSearchMethod);
        int nDBType = DictUtils.getSearchLastDictFromPreference(this);
        if (nDBType != -1 && nDBType != this.mEngine.getCurDict()) {
            this.mEngine.setDicType(nDBType);
            if (bSearchWord) {
                if (nDBType != 65520 && this.mEngine.getResultList(0) != null) {
                    this.mLastWordPos = this.mEngine.getResultListKeywordPos(0);
                    if (this.mEngine.getResultList(0) != null) {
                        int nSUID = this.mEngine.getResultList(0).getWordList(this.mLastWordPos).getSUID();
                        searchWord(this.mLastSearchWord, nSUID, this.etSearch.getText().toString(), 0);
                        return;
                    }
                    searchWord(this.mLastSearchWord, true);
                    return;
                }
                searchWord(this.mLastSearchWord, true);
            }
        }
    }

    public void restoreSearchInfo() {
        this.isForceGetMeaning = true;
        this.mEngine.setSearchMethod(this.mSearchMethodWord);
        this.mCurrentSearchMethod = this.mSearchMethodWord;
        setSearchWordBtnBySearchMethod(this.mEngine.getCurrentSearchMethodId());
        if (this.mLastSearchWord != null) {
            this.etSearch.setText(this.mLastSearchWord);
            this.etSearch.setSelection(this.mLastSearchWord.length());
        }
        this.mLastWordPos = this.mEngine.getResultListKeywordPos(0);
        updateSearchListViewItems(0);
        runSearchListView(this.mLastWordPos, false);
        this.isForceGetMeaning = false;
    }

    private void showHideChangeLanguage() {
        int nDicType = DictDBManager.getAutoDicType(this.mEngine.getCurDict(), true, "");
        if (nDicType != this.mEngine.getCurDict()) {
            this.mChangeLanguageBtn.setVisibility(View.VISIBLE);
        } else {
            this.mChangeLanguageBtn.setVisibility(View.GONE);
        }
        setChangeLanguagBtn(this.mEngine.getCurDict());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showClearButton(String str) {
        if (this.mClearBtn != null) {
            if (str.length() > 0) {
                this.mClearBtn.setVisibility(View.VISIBLE);
            } else {
                this.mClearBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity
    protected void checkSymbolKeyword() {
        int nDictype = this.mEngine.getCurDict();
        if (nDictype == getResources().getInteger(R.integer.DEDT_OXFORD_NEW_AMERICAN_DICTIONARY)) {
            if (this.mSearchMeanController != null) {
                String keyword = this.mSearchMeanController.getWord();
                if (keyword == null) {
                    if (this.mOldResultKeyword != null) {
                        keyword = this.mOldResultKeyword;
                    } else {
                        return;
                    }
                }
                boolean bTempResult = !EngineInfo3rd.IsTTSAvailableKeyword(keyword);
                if (this.mIsSymbolKeyword && !bTempResult) {
                    setEnableSaveButton(true);
                    setEnableCommonButton(true);
                }
                this.mIsSymbolKeyword = bTempResult;
                if (this.mIsSymbolKeyword) {
                    setEnableSaveButton(false);
                    setEnableTTSButton(false);
                }
            }
        } else if (this.mIsSymbolKeyword) {
            this.mIsSymbolKeyword = false;
        }
    }

    private String convertRegularWord(String word) {
        String wholeMarkWord;
        if (word == null || word.length() <= 0) {
            return "";
        }
        StringBuffer convertedWord = new StringBuffer();
        char[] arr$ = word.toCharArray();
        for (char curChar : arr$) {
            if (halfToWholeMarkMap.containsKey(String.valueOf(curChar))) {
                int halfMarkIdx = word.indexOf(curChar);
                if (word.length() > halfMarkIdx + 1 && ((word.charAt(halfMarkIdx + 1) == 65438 || word.charAt(halfMarkIdx + 1) == 65439) && (wholeMarkWord = regularMarkMap.get(word.substring(halfMarkIdx, halfMarkIdx + 2))) != null)) {
                    convertedWord.append(wholeMarkWord);
                } else {
                    String wholeMarkWord2 = halfToWholeMarkMap.get(String.valueOf(curChar));
                    if (wholeMarkWord2 != null) {
                        convertedWord.append(wholeMarkWord2);
                    }
                }
            }
            if (curChar != 65438 && curChar != 65439) {
                convertedWord.append(String.valueOf(curChar));
            }
        }
        if (convertedWord.length() <= 0) {
            convertedWord.append("");
        }
        return convertedWord.toString();
    }

    private void prepareRegularWordHashTable() {
        halfToWholeMarkMap.put("ｱ", "ア");
        halfToWholeMarkMap.put("ｴ", "エ");
        halfToWholeMarkMap.put("ﾊ", "ハ");
        halfToWholeMarkMap.put("ﾍ", "ヘ");
        halfToWholeMarkMap.put("ﾋ", "ヒ");
        halfToWholeMarkMap.put("ﾎ", "ホ");
        halfToWholeMarkMap.put("ﾌ", "フ");
        halfToWholeMarkMap.put("ｲ", "イ");
        halfToWholeMarkMap.put("ｶ", "カ");
        halfToWholeMarkMap.put("ｹ", "ケ");
        halfToWholeMarkMap.put("ｷ", "キ");
        halfToWholeMarkMap.put("ｺ", "コ");
        halfToWholeMarkMap.put("ｸ", "ク");
        halfToWholeMarkMap.put("ﾏ", "マ");
        halfToWholeMarkMap.put("ﾒ", "メ");
        halfToWholeMarkMap.put("ﾐ", "ミ");
        halfToWholeMarkMap.put("ﾓ", "モ");
        halfToWholeMarkMap.put("ﾑ", "ム");
        halfToWholeMarkMap.put("ﾝ", "ン");
        halfToWholeMarkMap.put("ﾅ", "ナ");
        halfToWholeMarkMap.put("ﾈ", "ネ");
        halfToWholeMarkMap.put("ﾆ", "ニ");
        halfToWholeMarkMap.put("ﾉ", "ノ");
        halfToWholeMarkMap.put("ﾇ", "ヌ");
        halfToWholeMarkMap.put("ｵ", "オ");
        halfToWholeMarkMap.put("ﾗ", "ラ");
        halfToWholeMarkMap.put("ﾚ", "レ");
        halfToWholeMarkMap.put("ﾘ", "リ");
        halfToWholeMarkMap.put("ﾛ", "ロ");
        halfToWholeMarkMap.put("ﾙ", "ル");
        halfToWholeMarkMap.put("ｻ", "サ");
        halfToWholeMarkMap.put("ｾ", "セ");
        halfToWholeMarkMap.put("ｼ", "シ");
        halfToWholeMarkMap.put("ｧ", "ァ");
        halfToWholeMarkMap.put("ｪ", "ェ");
        halfToWholeMarkMap.put("ｨ", "ィ");
        halfToWholeMarkMap.put("ｫ", "ォ");
        halfToWholeMarkMap.put("ｯ", "ッ");
        halfToWholeMarkMap.put("ｩ", "ゥ");
        halfToWholeMarkMap.put("ｬ", "ャ");
        halfToWholeMarkMap.put("ｮ", "ョ");
        halfToWholeMarkMap.put("ｭ", "ュ");
        halfToWholeMarkMap.put("ｿ", "ソ");
        halfToWholeMarkMap.put("ｽ", "ス");
        halfToWholeMarkMap.put("ﾀ", "タ");
        halfToWholeMarkMap.put("ﾃ", "テ");
        halfToWholeMarkMap.put("ﾁ", "チ");
        halfToWholeMarkMap.put("ﾄ", "ト");
        halfToWholeMarkMap.put("ﾂ", "ツ");
        halfToWholeMarkMap.put("ｳ", "ウ");
        halfToWholeMarkMap.put("ﾜ", "ワ");
        halfToWholeMarkMap.put("ｦ", "ヲ");
        halfToWholeMarkMap.put("ﾔ", "ヤ");
        halfToWholeMarkMap.put("ﾖ", "ヨ");
        halfToWholeMarkMap.put("ﾕ", "ユ");
        regularMarkMap.put("ﾊﾟ", "パ");
        regularMarkMap.put("ﾊﾞ", "バ");
        regularMarkMap.put("ﾍﾟ", "ペ");
        regularMarkMap.put("ﾍﾞ", "ベ");
        regularMarkMap.put("ﾋﾟ", "ピ");
        regularMarkMap.put("ﾋﾞ", "ビ");
        regularMarkMap.put("ﾎﾟ", "ポ");
        regularMarkMap.put("ﾎﾞ", "ボ");
        regularMarkMap.put("ﾌﾟ", "プ");
        regularMarkMap.put("ﾌﾞ", "ブ");
        regularMarkMap.put("ｶﾞ", "ガ");
        regularMarkMap.put("ｹﾞ", "ゲ");
        regularMarkMap.put("ｷﾞ", "ギ");
        regularMarkMap.put("ｺﾞ", "ゴ");
        regularMarkMap.put("ｸﾞ", "グ");
        regularMarkMap.put("ｻﾞ", "ザ");
        regularMarkMap.put("ｾﾞ", "ゼ");
        regularMarkMap.put("ｼﾞ", "ジ");
        regularMarkMap.put("ｿﾞ", "ゾ");
        regularMarkMap.put("ｽﾞ", "ズ");
        regularMarkMap.put("ﾀﾞ", "ダ");
        regularMarkMap.put("ﾃﾞ", "デ");
        regularMarkMap.put("ﾁﾞ", "ヂ");
        regularMarkMap.put("ﾄﾞ", "ド");
        regularMarkMap.put("ﾂﾞ", "ヅ");
        regularMarkMap.put("ｳﾞ", "ヴ");
        regularMarkMap.put("ﾜﾞ", "ヷ");
        regularMarkMap.put("ｦﾞ", "ヺ");
    }
}
