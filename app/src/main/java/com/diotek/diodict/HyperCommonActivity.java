package com.diotek.diodict;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import android.widget.TextView;
import android.widget.Toast;
import com.diotek.diodict.anim.LayoutTransition;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.FileLinkTagViewManager;
import com.diotek.diodict.mean.HyperSimpleViewModule;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.mean.SearchMeanController;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.CustomPopupRelativeLayout;
import com.diotek.diodict.uitool.PopupFlashcardGridAdapter;
import com.diotek.diodict.uitool.TabView;
import com.diotek.diodict.uitool.TextImageButton;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class HyperCommonActivity extends BaseActivity {
    private static final int DIALOG_MAKE = 0;
    protected PopupFlashcardGridAdapter mFlashcardFolderListViewAdapter = null;
    protected ArrayList<HashMap<String, Object>> mHyperHistoryList = null;
    protected ArrayList<HashMap<String, Object>> mFlashcardFolderListViewItems = new ArrayList<>();
    protected SearchMeanController mHyperDetailMeanController = null;
    protected HyperSimpleViewModule mHyperSimpleViewModule = null;
    protected FileLinkTagViewManager mFileLinkTagViewManager = null;
    protected PopupWindow mFontSizeChangePopup = null;
    protected PopupWindow mMarkerColorChangePopup = null;
    protected Dialog mWordbookDialog = null;
    protected RadioButton mCard1 = null;
    protected RadioButton mCard2 = null;
    protected RadioButton mCard3 = null;
    protected EditText mFlashcardEditText = null;
    protected TextImageButton mBtnMakeWordbookOk = null;
    protected TextImageButton mBtnMakeWordbookCancel = null;
    protected ImageButton mSaveBtn = null;
    protected ImageButton mMarkerBtn = null;
    protected ImageButton mFontBtn = null;
    protected ImageButton mMemoBtn = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardOk = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardCancel = null;
    private RelativeLayout mAddWordbookTextView = null;
    private GridView mFlashcardGridView = null;
    private CheckBox mFlashcardItemEditCopyToFlashcardCheckBox = null;
    protected RelativeLayout mCopyToFlashcardLayout = null;
    protected ListView mHyperDetailListView = null;
    protected TextView mHyperDetailEmptyTextView = null;
    protected TabView mHyperDetailTabView = null;
    protected TextView mHyperDetailMeanTitleTextView = null;
    protected ExtendTextView mHyperDetailMeanContentTextView = null;
    protected ScrollView mHyperDetailScrollView = null;
    protected ImageView mHyperDetailBookmark = null;
    protected LinearLayout mHyperMeanMoveBtnLayout = null;
    protected Button mHyperDetailPrevSearchBtn = null;
    protected Button mHyperDetailNextSearchBtn = null;
    protected EditText mEdittextWordbookName = null;
    protected String mInputWordbookName = null;
    protected TextView mInputWordbookNameTextView = null;
    protected String mBackupCardName = null;
    private int mWordbookType = 1;
    protected int mHyperDetailMeanPos = 0;
    protected String mHyperHistoryWord = null;
    protected boolean mIsBackgroundCheckedList = false;
    protected int mHyperHistorySUID = -1;
    protected int mHyperHistoryDict = -1;
    protected int mHyperHistoryCurPos = 0;
    protected boolean[] mCheckedWordbookList = {false};
    protected int mTabViewPos = 0;
    private int mCurrentDict = -1;
    protected TTSManager mTTSManager = null;
    protected boolean mIsCreate = true;
    protected boolean mIsFlashcardPopup = false;
    View.OnClickListener mHyperDetailPrevSearchBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runHyperDetailPrevNextSearchBtn(false);
        }
    };
    View.OnClickListener mHyperDetailNextSearchBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runHyperDetailPrevNextSearchBtn(true);
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.HyperCommonActivity.4
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(HyperCommonActivity.this.getApplicationContext());
        }
    };
    HyperSimpleViewModule.HyperSimpleViewModuleCallback mHyperSimpleViewModuleCallback = new HyperSimpleViewModule.HyperSimpleViewModuleCallback() { // from class: com.diotek.diodict.HyperCommonActivity.5
        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runDetailBtn(int meanpos) {
            HyperCommonActivity.this.mHyperDetailMeanPos = meanpos;
            HyperCommonActivity.this.mHyperHistoryWord = HyperCommonActivity.this.mEngine.getResultListKeywordByPos(HyperCommonActivity.this.mHyperDetailMeanPos, 1);
            HyperCommonActivity.this.mHyperHistorySUID = HyperCommonActivity.this.mEngine.getResultListSUIDByPos(HyperCommonActivity.this.mHyperDetailMeanPos, 1);
            HyperCommonActivity.this.mHyperHistoryDict = HyperCommonActivity.this.mEngine.getResultListDictByPos(HyperCommonActivity.this.mHyperDetailMeanPos, 1);
            HyperCommonActivity.this.addHyperHistoryList(HyperCommonActivity.this.mHyperHistoryWord, HyperCommonActivity.this.mHyperHistorySUID, HyperCommonActivity.this.mHyperHistoryDict);
            HyperCommonActivity.this.showHyperMoveBtnLayout();
            HyperCommonActivity.this.runHyperDeatilSearchListView(HyperCommonActivity.this.mHyperDetailMeanPos);
            HyperCommonActivity.this.setHyperDetailPrevNextSearchBtn();
        }

        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runExitBtn() {
            if (HyperCommonActivity.this.mHyperDetailMeanContentTextView != null) {
                HyperCommonActivity.this.mHyperDetailMeanContentTextView.initTextSelect();
            }
        }
    };
    Runnable mRunnableUpdateTabView = new Runnable() { // from class: com.diotek.diodict.HyperCommonActivity.6
        @Override // java.lang.Runnable
        public void run() {
            HyperCommonActivity.this.runnableMeanTabView();
        }
    };
    TabView.TabViewOnClickListener mHyperDetailTabViewOnClickListener = new TabView.TabViewOnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.7
        @Override // com.diotek.diodict.uitool.TabView.TabViewOnClickListener
        public void onClick(View v, int nPos) {
            HyperCommonActivity.this.mTabViewPos = nPos;
            HyperCommonActivity.this.mHandler.removeCallbacks(HyperCommonActivity.this.mRunnableUpdateTabView);
            HyperCommonActivity.this.mHandler.postDelayed(HyperCommonActivity.this.mRunnableUpdateTabView, 300L);
            if (HyperCommonActivity.this.mHyperDetailTabView != null) {
                HyperCommonActivity.this.mHyperDetailTabView.setBtnSelected(HyperCommonActivity.this.mTabViewPos);
            }
        }
    };
    View.OnTouchListener mMarkerColorPopupOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.HyperCommonActivity.8
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 4 || event.getAction() == 3) {
                if (HyperCommonActivity.this.mMarkerColorChangePopup != null) {
                    HyperCommonActivity.this.mMarkerColorChangePopup.dismiss();
                    HyperCommonActivity.this.mMarkerColorChangePopup = null;
                }
                return true;
            }
            return false;
        }
    };
    protected View.OnClickListener mFontSizeChangeOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean isUpdate = true;
            int[] fontSizeList = HyperCommonActivity.this.getResources().getIntArray(R.array.value_font_size);
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
            if (HyperCommonActivity.this.mHyperDetailMeanContentTextView != null && isUpdate) {
                HyperCommonActivity.this.mHyperDetailMeanContentTextView.setTextSize(fontSizeList[fontSizeIndex]);
                DictUtils.setFontSizeToPreference(HyperCommonActivity.this.getApplicationContext(), fontSizeIndex);
            }
            if (HyperCommonActivity.this.mFontSizeChangePopup != null && HyperCommonActivity.this.mFontSizeChangePopup.isShowing()) {
                HyperCommonActivity.this.mFontSizeChangePopup.dismiss();
            }
        }
    };
    private View.OnClickListener mMarkerColorChangeOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean colorChange = true;
            int[] colorList = HyperCommonActivity.this.getResources().getIntArray(R.array.value_marker_color_adv);
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
                    HyperCommonActivity.this.dismissMarkerColorChangePopup();
                    colorChange = false;
                    break;
            }
            if (HyperCommonActivity.this.mHyperDetailMeanContentTextView != null && colorChange) {
                HyperCommonActivity.this.mHyperDetailMeanContentTextView.setMarkerColor(colorList[markerColorIndex]);
                if (markerColorIndex < 5) {
                    DictUtils.setMarkerColorToPreference(HyperCommonActivity.this.getApplicationContext(), markerColorIndex);
                }
            }
        }
    };
    private Runnable mHideOnlySoftInput = new Runnable() { // from class: com.diotek.diodict.HyperCommonActivity.11
        @Override // java.lang.Runnable
        public void run() {
            InputMethodManager imm = (InputMethodManager) HyperCommonActivity.this.getApplicationContext().getSystemService("input_method");
            if (imm != null && HyperCommonActivity.this.mFlashcardEditText != null) {
                imm.hideSoftInputFromWindow(HyperCommonActivity.this.mFlashcardEditText.getWindowToken(), 0);
            } else {
                Log.e("imm", "mhideOnlySoftInput():imm or mSearchInput is null");
            }
        }
    };
    private Runnable mShowOnlySoftInput = new Runnable() { // from class: com.diotek.diodict.HyperCommonActivity.12
        @Override // java.lang.Runnable
        public void run() {
            InputMethodManager imm = (InputMethodManager) HyperCommonActivity.this.getApplicationContext().getSystemService("input_method");
            if (imm != null && HyperCommonActivity.this.mFlashcardEditText != null) {
                imm.showSoftInput(HyperCommonActivity.this.mFlashcardEditText, 0);
            } else {
                Log.e("imm", "mshowOnlySoftInput():imm or mSearchInput is null");
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard1OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HyperCommonActivity.13
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HyperCommonActivity.this.mWordbookType = 1;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard2OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HyperCommonActivity.14
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HyperCommonActivity.this.mWordbookType = 2;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard3OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HyperCommonActivity.15
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HyperCommonActivity.this.mWordbookType = 3;
            }
        }
    };
    ExtendTextView.ExtendTextCallback mStartHyperCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.HyperCommonActivity.16
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            HyperCommonActivity.this.mCurrentDict = HyperCommonActivity.this.mEngine.getCurDict();
            HyperCommonActivity.this.mHyperSimpleViewModule.startHyperSimple(str);
            return false;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            if (HyperCommonActivity.this.mHyperDetailMeanContentTextView != null) {
                HyperCommonActivity.this.mHyperSimpleViewModule.startHyperSimple(HyperCommonActivity.this.mHyperDetailMeanContentTextView.getSelectedString());
                return false;
            }
            return false;
        }
    };
    View.OnClickListener mBtnMakeWordbookOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.17
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runBtnMakeWordbookOk(v);
        }
    };
    View.OnClickListener mBtnMakeWordbookCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.18
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runBtnMakeWordbookCancel(v);
        }
    };
    View.OnClickListener mSaveBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.19
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runSaveBtn();
        }
    };
    AdapterView.OnItemClickListener mFlashcardGridViewOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.20
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            boolean z = true;
            HyperCommonActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
            HyperCommonActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.setChecked(!HyperCommonActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.isChecked());
            boolean[] zArr = HyperCommonActivity.this.mCheckedWordbookList;
            if (HyperCommonActivity.this.mCheckedWordbookList[position]) {
                z = false;
            }
            zArr[position] = z;
            HyperCommonActivity.this.mFlashcardFolderListViewItems.get(position).put(DictInfo.ListItem_WordbookFolderChecked, Boolean.valueOf(HyperCommonActivity.this.mCheckedWordbookList[position]));
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.21
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runFlashcardItemEditCopyToFlashcardOk();
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.22
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperCommonActivity.this.runFlashcardItemEditCopyToFlashcardCancel();
        }
    };
    View.OnClickListener mAddWordbookTextViewOnCLickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.23
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            v.setSelected(true);
            HyperCommonActivity.this.makeWordbook();
            v.setSelected(false);
        }
    };
    View.OnFocusChangeListener mCardOnFocusChangedListner = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.HyperCommonActivity.24
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean bFocus) {
            if (bFocus) {
                ((RadioButton) view).setChecked(true);
            }
        }
    };
    View.OnFocusChangeListener mHyperDetailMeanContentTextViewOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.HyperCommonActivity.25
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            ImageView meanSeparator = (ImageView) HyperCommonActivity.this.findViewById(R.id.separator);
            if (meanSeparator != null) {
                if (hasFocus) {
                    meanSeparator.setVisibility(0);
                } else {
                    meanSeparator.setVisibility(8);
                }
            }
        }
    };
    protected Runnable mRunTiffanyStiker = new Runnable() { // from class: com.diotek.diodict.HyperCommonActivity.26
        @Override // java.lang.Runnable
        public void run() {
            if (HyperCommonActivity.this.mTfTrans != null) {
                Window win = HyperCommonActivity.this.getWindow();
                View root = win.getDecorView();
                HyperCommonActivity.this.mTfTrans.setRootView(root);
                HyperCommonActivity.this.mTfTrans.setTransToView(HyperCommonActivity.this.mHyperDetailBookmark);
                if (win.isActive()) {
                    try {
                        HyperCommonActivity.this.mTfTrans.transition(13, 0);
                        return;
                    } catch (OutOfMemoryError e) {
                        System.gc();
                        return;
                    }
                }
                return;
            }
            HyperCommonActivity.this.mHyperDetailBookmark.setVisibility(0);
        }
    };
    Runnable mRunSaveFlashcardItem = new Runnable() { // from class: com.diotek.diodict.HyperCommonActivity.27
        @Override // java.lang.Runnable
        public void run() {
            Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(HyperCommonActivity.this.getApplicationContext());
            if (tCursor == null) {
                Toast.makeText(HyperCommonActivity.this, (int) R.string.selectFlashcardToSave, 0).show();
                return;
            }
            boolean wordSaved = false;
            boolean wordExist = false;
            String msg = null;
            tCursor.moveToFirst();
            boolean trans = false;
            int dicType = HyperCommonActivity.this.mHyperDetailMeanController.getDicType();
            String word = HyperCommonActivity.this.mHyperDetailMeanController.getWord();
            int suid = HyperCommonActivity.this.mHyperDetailMeanController.getSuid();
            for (int i = 0; i < HyperCommonActivity.this.mCheckedWordbookList.length; i++) {
                if (HyperCommonActivity.this.mCheckedWordbookList[i]) {
                    tCursor.moveToPosition(i);
                    int wbnameId = tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
                    if (DioDictDatabase.addWordbookItem(HyperCommonActivity.this.getApplicationContext(), dicType, word, suid, wbnameId) == 2) {
                        wordExist = true;
                    } else {
                        trans = true;
                        wordSaved = true;
                    }
                }
            }
            tCursor.close();
            if (!wordSaved && !wordExist) {
                Toast.makeText(HyperCommonActivity.this, HyperCommonActivity.this.getResources().getString(R.string.selectFlashcardToSave), 0).show();
                return;
            }
            if (wordSaved) {
                msg = HyperCommonActivity.this.getResources().getString(R.string.savedWord);
            } else if (!wordSaved && wordExist) {
                msg = HyperCommonActivity.this.getResources().getString(R.string.alreadyExistWord);
            }
            Toast.makeText(HyperCommonActivity.this, msg, 0).show();
            int duration = HyperCommonActivity.this.showCopyToFlashcardLayout(false);
            HyperCommonActivity.this.mSaveBtn.setSelected(false);
            if (trans) {
                HyperCommonActivity.this.mHandler.postDelayed(HyperCommonActivity.this.mRunTiffanyStiker, duration * 2);
            }
        }
    };
    PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.HyperCommonActivity.28
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            HyperCommonActivity.this.setClickableMeanToolBar(true);
        }
    };
    RadioGroup markerGroup = null;
    ImageView mMarkerCloseBtn = null;
    int MARKER_TAG = 100;
    View.OnClickListener mEditClearBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperCommonActivity.29
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (HyperCommonActivity.this.mInputWordbookNameTextView != null) {
                if (HyperCommonActivity.this.mEdittextWordbookName != null) {
                    HyperCommonActivity.this.mEdittextWordbookName.setText("");
                }
                HyperCommonActivity.this.mInputWordbookNameTextView.setVisibility(0);
            }
        }
    };
    TextWatcher mWordbookEditWatcher = new TextWatcher() { // from class: com.diotek.diodict.HyperCommonActivity.30
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            HyperCommonActivity.this.hideEditMessage();
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            HyperCommonActivity.this.mBackupCardName = s.toString();
            if (HyperCommonActivity.this.mBackupCardName.length() == 0 && HyperCommonActivity.this.mInputWordbookNameTextView != null) {
                HyperCommonActivity.this.mInputWordbookNameTextView.setVisibility(0);
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity
    public boolean onCreateActivity(Bundle savedInstanceState) {
        if (!super.onCreateActivity(savedInstanceState)) {
            return false;
        }
        if (Dependency.isContainTTS()) {
            setVolumeControlStream(3);
        }
        this.mIsCreate = true;
        prepareExtraData();
        return true;
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return createMakeWordbookDialog();
            default:
                return super.onCreateDialog(id);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onPause() {
        handleSaveMarkerObject();
        this.mFileLinkTagViewManager.onPause();
        this.mHyperSimpleViewModule.onPause();
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        if (this.mHyperDetailMeanController != null) {
            this.mHyperDetailMeanController.onDestory();
        }
        closePopupWindow();
        if (this.mTTSManager != null) {
            this.mTTSManager.onTerminateTTS();
        }
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity
    public String getPlayTTSWord(boolean bKeyword) {
        String word = "";
        if (this.mHyperDetailMeanContentTextView != null) {
            if (bKeyword) {
                word = this.mHyperDetailMeanContentTextView.getKeyword();
            } else {
                word = this.mHyperDetailMeanContentTextView.getKeyword();
            }
        }
        if (word == null) {
            String word2 = super.getPlayTTSWord(bKeyword);
            return word2;
        }
        return word;
    }

    @Override // com.diotek.diodict.uitool.BaseActivity
    protected int getPlayTTSDbType() {
        if (this.mHyperDetailMeanContentTextView != null) {
            return this.mHyperDetailMeanContentTextView.getDbtype();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int nTheme = this.mHyperDetailMeanController.getTheme();
        if (nTheme != DictUtils.getFontThemeFromPreference(getApplicationContext())) {
            this.mHyperDetailMeanController.refreshViewNoDelay();
        }
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        this.mIsCreate = false;
        closePopupWindow();
        super.onConfigurationChanged(newConfig);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        this.mActivityManager.finishPrevActivity();
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Bundle prepareExtraData() {
        Bundle extra;
        Intent intent = getIntent();
        if (intent != null && (extra = intent.getExtras()) != null) {
            this.mHyperDetailMeanPos = extra.getInt(DictInfo.INTENT_HYPER_MEANPOS);
            this.mHyperHistoryWord = this.mEngine.getResultListKeywordByPos(this.mHyperDetailMeanPos, 1);
            this.mHyperHistorySUID = this.mEngine.getResultListSUIDByPos(this.mHyperDetailMeanPos, 1);
            this.mHyperHistoryDict = this.mEngine.getResultListDictByPos(this.mHyperDetailMeanPos, 1);
            this.mIsBackgroundCheckedList = extra.getBoolean(DictInfo.INTENT_HYPER_ISCHECKLIST);
            return extra;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void restoreExtraData() {
        this.mHyperHistoryWord = this.mEngine.getResultListKeywordByPos(this.mHyperDetailMeanPos, 1);
        this.mHyperHistorySUID = this.mEngine.getResultListSUIDByPos(this.mHyperDetailMeanPos, 1);
        this.mHyperHistoryDict = this.mEngine.getResultListDictByPos(this.mHyperDetailMeanPos, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareHyperListView() {
        if (this.mHyperHistoryList == null) {
            this.mHyperHistoryList = new ArrayList<>();
            addHyperHistoryList(this.mHyperHistoryWord, this.mHyperHistorySUID, this.mHyperHistoryDict);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareMeanContentLayout() {
        this.mHyperDetailMeanTitleTextView = (TextView) findViewById(R.id.MeanTitleTextView);
        this.mHyperDetailMeanContentTextView = (ExtendTextView) findViewById(R.id.MeanContentTextView);
        this.mHyperDetailMeanContentTextView.setOnFocusChangeListener(this.mHyperDetailMeanContentTextViewOnFocusChangeListener);
        this.mHyperDetailBookmark = (ImageView) findViewById(R.id.bookmark);
        this.mHyperDetailScrollView = (ScrollView) findViewById(R.id.scrollview);
        prepareHyperMeanTabView();
        this.mHyperDetailPrevSearchBtn = (Button) findViewById(R.id.SearchHyperLeftBtn);
        this.mHyperDetailPrevSearchBtn.setOnClickListener(this.mHyperDetailPrevSearchBtnOnClickListener);
        this.mHyperDetailNextSearchBtn = (Button) findViewById(R.id.SearchHyperRightBtn);
        this.mHyperDetailNextSearchBtn.setOnClickListener(this.mHyperDetailNextSearchBtnOnClickListener);
        setHyperDetailPrevNextSearchBtn();
        this.mHyperDetailMeanContentTextView.setFocusable(true);
        this.mHyperDetailScrollView.setFocusable(false);
        this.mCopyToFlashcardLayout = (RelativeLayout) findViewById(R.id.copyToFlashcardPopLayout);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.SearchRightLayout);
        parent.setOnTouchListener(new View.OnTouchListener() { // from class: com.diotek.diodict.HyperCommonActivity.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        LinearLayout parent_sub = (LinearLayout) findViewById(R.id.MeanContentLayout);
        this.mFileLinkTagViewManager = new FileLinkTagViewManager(this, this.mEngine, this.mHyperDetailMeanContentTextView, parent, parent_sub, this.mThemeModeCallback);
        this.mHyperDetailMeanController = new SearchMeanController(this, this.mHyperDetailMeanTitleTextView, this.mHyperDetailMeanContentTextView, this.mHyperDetailBookmark, this.mHyperDetailTabView, this.mEngine, false, this.mThemeModeCallback, this.mFileLinkTagViewManager, this.mTTSManager.mTTSLayoutCallback);
        this.mHyperSimpleViewModule = new HyperSimpleViewModule(this, this.mHyperSimpleViewModuleCallback, parent, parent_sub, this.mHyperDetailMeanContentTextView);
        this.mHyperDetailMeanController.setMeanContentTextViewCallback(this.mStartHyperCallback, null, true, null);
        this.mTTSManager.setExtendTextView(this.mHyperDetailMeanContentTextView);
        this.mHyperDetailMeanContentTextView.setEnableTextSelect(true);
    }

    protected void prepareHyperMeanTabView() {
        this.mHyperDetailTabView = (TabView) findViewById(R.id.MeanTabView);
        this.mHyperDetailTabView.getButton(0).setBackgroundResource(R.drawable.tab);
        this.mHyperDetailTabView.getButton(1).setBackgroundResource(R.drawable.tab);
        this.mHyperDetailTabView.getButton(2).setBackgroundResource(R.drawable.tab);
        this.mHyperDetailTabView.getButton(3).setBackgroundResource(R.drawable.tab);
        this.mHyperDetailTabView.getButton(0).setText(getResources().getString(R.string.All));
        this.mHyperDetailTabView.getButton(1).setText(getResources().getString(R.string.Mean));
        this.mHyperDetailTabView.getButton(2).setText(getResources().getString(R.string.Phrases));
        this.mHyperDetailTabView.getButton(3).setText(getResources().getString(R.string.Example));
        this.mHyperDetailTabView.getButton(0).setSelected(true);
        this.mHyperDetailTabView.setOnClickListener(this.mHyperDetailTabViewOnClickListener);
        this.mHyperDetailTabView.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareMeanTTSLayout() {
        if (this.mTTSManager == null) {
            this.mTTSManager = new TTSManager(this);
        }
        this.mTTSManager.prepareMeanTTSLayout(this.mTTSOnClickListner);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareFlashcardPopupLayout() {
        ((TextView) this.mCopyToFlashcardLayout.findViewById(R.id.copyToFlashcardPopTitle)).setText(R.string.selectFlashcardToSave);
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

    protected void runHyperDetailTabView(int nPos, boolean isRefresh) {
        this.mHyperDetailMeanController.setDisplayMode(nPos);
        if (isRefresh) {
            this.mHyperDetailMeanController.refreshContentView();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runHyperDeatilSearchListView(int nPos) {
        this.mHyperDetailMeanPos = nPos;
        this.mHyperDetailMeanController.setMeanViewByPos(nPos, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runHyperDetailPrevNextSearchBtn(boolean bNext) {
        int tmpCurPos;
        if (this.mHyperHistoryList != null) {
            if (bNext) {
                tmpCurPos = this.mHyperHistoryCurPos + 1;
                if (tmpCurPos >= this.mHyperHistoryList.size()) {
                    return;
                }
            } else {
                tmpCurPos = this.mHyperHistoryCurPos - 1;
                if (tmpCurPos < 0) {
                    return;
                }
            }
            HashMap<String, Object> item = this.mHyperHistoryList.get(tmpCurPos);
            String hyperWord = (String) item.get(DictInfo.INTENT_HYPER_WORD);
            int hyperSUID = ((Integer) item.get(DictInfo.INTENT_HYPER_SUID)).intValue();
            int hyperDict = ((Integer) item.get(DictInfo.INTENT_HYPER_DICT)).intValue();
            this.mHyperHistoryCurPos = tmpCurPos;
            if (this.mHyperSimpleViewModule.hyperSearchWord(hyperDict, hyperWord, hyperSUID, true)) {
                this.mHyperHistoryDict = hyperDict;
                this.mHyperHistoryWord = hyperWord;
                this.mHyperHistorySUID = hyperSUID;
                runHyperDeatilSearchListView(this.mEngine.getResultListKeywordPos(1));
                setHyperDetailPrevNextSearchBtn();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runSaveBtn() {
        if (this.mHyperDetailMeanContentTextView != null) {
            this.mHyperDetailMeanContentTextView.initTextSelect();
            this.mHyperDetailMeanContentTextView.invalidate();
        }
        showFlashcardListPop(false);
        showSoftInputMethod(false);
    }

    protected void runMeanTabView(int nMode, boolean isRefresh) {
        handleSaveMarkerObject();
        this.mHyperDetailMeanController.setDisplayMode(nMode);
        if (isRefresh) {
            this.mHyperDetailMeanController.refreshContentView();
        }
        showSoftInputMethod(false);
    }

    protected boolean runMakeWordbookOK() {
        if (DioDictDatabase.getWordbookFolderCount(getApplicationContext()) >= 40) {
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
        } else if (DioDictDatabase.addWordbookFolder(getApplicationContext(), this.mInputWordbookName, this.mWordbookType) == 2) {
            Toast.makeText(this, getResources().getString(R.string.alreadyExistWordbook), 0).show();
            return false;
        } else {
            return true;
        }
    }

    protected void runBtnMakeWordbookOk(View v) {
        if (runMakeWordbookOK()) {
            updateWordbookFolderItems(false);
            removeDialog(0);
        }
        this.mAddWordbookTextView.setSelected(false);
    }

    protected void runBtnMakeWordbookCancel(View v) {
        removeDialog(0);
        this.mAddWordbookTextView.setSelected(false);
    }

    protected void runnableMeanTabView() {
        initPopupControll();
        if (this.mHyperDetailMeanContentTextView != null) {
            this.mHyperDetailMeanContentTextView.stopInvilidate();
        }
        runMeanTabView(this.mTabViewPos, true);
    }

    protected void runFlashcardItemEditCopyToFlashcardOk() {
        this.mHandler.removeCallbacks(this.mRunSaveFlashcardItem);
        this.mHandler.postDelayed(this.mRunSaveFlashcardItem, 300L);
    }

    protected void runFlashcardItemEditCopyToFlashcardCancel() {
        showCopyToFlashcardLayout(false);
        this.mSaveBtn.setSelected(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void closeHyperDetailView() {
        resetHyperHistory();
        finish();
    }

    /* loaded from: classes.dex */
    private class PopupTouchInterceptor implements View.OnTouchListener {
        private PopupTouchInterceptor() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if ((action != 0 || HyperCommonActivity.this.mMarkerColorChangePopup == null || !HyperCommonActivity.this.mMarkerColorChangePopup.isShowing()) && action == 1) {
            }
            if (action == 4 && HyperCommonActivity.this.mMarkerColorChangePopup != null && HyperCommonActivity.this.mMarkerColorChangePopup.isShowing()) {
                HyperCommonActivity.this.mMarkerColorChangePopup.dismiss();
                return true;
            }
            return false;
        }
    }

    protected void addHyperHistoryList(String hyperWord, int hyperSUID, int hyperDict) {
        int mHyperHistoryListSize = this.mHyperHistoryList.size();
        if (this.mHyperHistoryCurPos + 1 < mHyperHistoryListSize) {
            for (int i = mHyperHistoryListSize - 1; i > this.mHyperHistoryCurPos; i--) {
                this.mHyperHistoryList.remove(i);
            }
        }
        HashMap<String, Object> item = new HashMap<>();
        item.put(DictInfo.INTENT_HYPER_WORD, hyperWord);
        item.put(DictInfo.INTENT_HYPER_SUID, Integer.valueOf(hyperSUID));
        item.put(DictInfo.INTENT_HYPER_DICT, Integer.valueOf(hyperDict));
        this.mHyperHistoryList.add(item);
        this.mHyperHistoryCurPos = this.mHyperHistoryList.size() - 1;
    }

    protected void updateDisplayTabMenu() {
        this.mHyperDetailTabView.getButton(2).setEnabled(this.mHyperDetailMeanController.isAvailable_Idiom());
        this.mHyperDetailTabView.getButton(3).setEnabled(this.mHyperDetailMeanController.isAvailable_Example());
    }

    public void setHyperDetailPrevNextSearchBtn() {
        if (this.mHyperHistoryList != null) {
            HashMap<String, Object> item = null;
            if (this.mHyperHistoryCurPos - 1 >= 0) {
                HashMap<String, Object> item2 = this.mHyperHistoryList.get(this.mHyperHistoryCurPos - 1);
                item = item2;
            }
            int width = getResources().getDimensionPixelSize(R.dimen.hyper_prevnext_btn_width);
            if (item != null) {
                String str = (String) TextUtils.ellipsize((String) item.get(DictInfo.INTENT_HYPER_WORD), this.mHyperDetailPrevSearchBtn.getPaint(), width, TextUtils.TruncateAt.END);
                this.mHyperDetailPrevSearchBtn.setTypeface(DictUtils.createfont());
                this.mHyperDetailPrevSearchBtn.setText(str);
                this.mHyperDetailPrevSearchBtn.setEnabled(true);
                this.mHyperDetailPrevSearchBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.hyper_left_arrow), (Drawable) null, (Drawable) null, (Drawable) null);
            } else {
                this.mHyperDetailPrevSearchBtn.setText("");
                this.mHyperDetailPrevSearchBtn.setEnabled(false);
                this.mHyperDetailPrevSearchBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            HashMap<String, Object> item3 = null;
            if (this.mHyperHistoryCurPos + 1 < this.mHyperHistoryList.size()) {
                HashMap<String, Object> item4 = this.mHyperHistoryList.get(this.mHyperHistoryCurPos + 1);
                item3 = item4;
            }
            if (item3 != null) {
                String str2 = (String) TextUtils.ellipsize((String) item3.get(DictInfo.INTENT_HYPER_WORD), this.mHyperDetailNextSearchBtn.getPaint(), width, TextUtils.TruncateAt.END);
                this.mHyperDetailNextSearchBtn.setTypeface(DictUtils.createfont());
                this.mHyperDetailNextSearchBtn.setText(str2);
                this.mHyperDetailNextSearchBtn.setEnabled(true);
                this.mHyperDetailNextSearchBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, getResources().getDrawable(R.drawable.hyper_right_arrow), (Drawable) null);
                return;
            }
            this.mHyperDetailNextSearchBtn.setText("");
            this.mHyperDetailNextSearchBtn.setEnabled(false);
            this.mHyperDetailNextSearchBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showHyperMoveBtnLayout() {
        if (getResources().getConfiguration().orientation == 1) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.HyperMeanMoveBtnLayout);
            if (this.mHyperHistoryList.size() < 2) {
                layout.setVisibility(8);
                return;
            } else {
                layout.setVisibility(0);
                return;
            }
        }
        RelativeLayout leftlayout = (RelativeLayout) findViewById(R.id.SearchHyperLeftBtnLayout);
        RelativeLayout rightlayout = (RelativeLayout) findViewById(R.id.SearchHyperRightBtnLayout);
        LinearLayout meanttslayout = (LinearLayout) findViewById(R.id.MeanTTSLayout);
        if (this.mHyperHistoryList.size() < 2) {
            leftlayout.setVisibility(4);
            rightlayout.setVisibility(4);
            meanttslayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.trans));
            return;
        }
        leftlayout.setVisibility(0);
        rightlayout.setVisibility(0);
        meanttslayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.hyper_ttslayout_bg_land));
    }

    public void showFlashcardListPop(boolean existCheckedList) {
        updateWordbookFolderItems(existCheckedList);
        showCopyToFlashcardLayout(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int showCopyToFlashcardLayout(boolean isShow) {
        boolean z = true;
        if (isShow) {
            setClickableMeanToolBar(false);
            this.mCopyToFlashcardLayout.setVisibility(0);
            if (this.mFlashcardFolderListViewItems.isEmpty()) {
                ((RelativeLayout) findViewById(R.id.addCard)).requestFocus();
            } else {
                this.mFlashcardGridView.requestFocus();
            }
        } else {
            setClickableMeanToolBar(true);
        }
        LayoutTransition.trasition(this.mCopyToFlashcardLayout, isShow, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 250, false, !isShow);
        if (isShow) {
            z = false;
        }
        setFocusableHyperActivity(z);
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

    protected void initPopupControll() {
        dismissMarkerColorChangePopup();
        if (this.mHyperDetailMeanContentTextView != null) {
            this.mHyperDetailMeanContentTextView.initTextSelect();
            this.mHyperDetailMeanContentTextView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dismissMarkerColorChangePopup() {
        if (this.mMarkerColorChangePopup == null || !this.mMarkerColorChangePopup.isShowing()) {
            return false;
        }
        this.mMarkerColorChangePopup.dismiss();
        this.mMarkerColorChangePopup = null;
        this.mHyperDetailMeanContentTextView.setMakerMode(false);
        setFocusableHyperActivity(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dismissFontChangePopup() {
        if (this.mFontSizeChangePopup != null) {
            this.mFontSizeChangePopup.dismiss();
            this.mFontSizeChangePopup = null;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
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

    public void showMarkerColorChangePopupMenu() {
        int[] colorList = getResources().getIntArray(R.array.value_marker_color_adv);
        int[] windowLocation = new int[2];
        RelativeLayout parents = (RelativeLayout) findViewById(R.id.SearchRightLayout);
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        CustomPopupRelativeLayout PopupContent = (CustomPopupRelativeLayout) inflate.inflate(R.layout.marker_color_select_popup, (ViewGroup) null);
        this.markerGroup = (RadioGroup) PopupContent.findViewById(R.id.marker_group);
        parents.getLocationInWindow(windowLocation);
        parents.setOnTouchListener(this.mMarkerColorPopupOnTouchListener);
        float density = CommonUtils.getDeviceDensity(this);
        int popupWidth = getResources().getConfiguration().orientation == 1 ? (int) ((313.3f * density) + 0.5f) : (int) ((337.3f * density) + 0.5f);
        ImageView view = (ImageView) PopupContent.findViewById(R.id.marker_popup_bg_view);
        int popupHeight = view.getBackground().getIntrinsicHeight();
        int popupX = parents.getWidth() - popupWidth;
        if (this.mMarkerColorChangePopup == null) {
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
            this.mMarkerColorChangePopup = CommonUtils.makeWindowWithPopupWindow(getApplicationContext(), 0, PopupContent, null, this.mOnDismissListener, false);
            if (this.mMarkerColorChangePopup != null) {
                this.mMarkerColorChangePopup.setOutsideTouchable(true);
                this.mMarkerColorChangePopup.setTouchInterceptor(new PopupTouchInterceptor());
            }
        } else {
            this.markerGroup.clearCheck();
        }
        int markerColorIndex = DictUtils.getMarkerColorFromPreference(getApplicationContext());
        ((RadioButton) this.markerGroup.getChildAt(markerColorIndex)).setChecked(true);
        this.mHyperDetailMeanController.setMeanContentTextViewMarkerColor(colorList[markerColorIndex]);
        if (this.mMarkerColorChangePopup != null) {
            if (this.mMarkerColorChangePopup.isShowing()) {
                this.mMarkerColorChangePopup.update(windowLocation[0] + popupX, windowLocation[1] + 0, popupWidth, popupHeight);
            } else {
                this.mMarkerColorChangePopup.setWidth(popupWidth);
                this.mMarkerColorChangePopup.setHeight(popupHeight);
                this.mMarkerColorChangePopup.showAtLocation(parents, 0, windowLocation[0] + popupX, windowLocation[1] + 0);
            }
        }
        setFocusableHyperActivity(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFocusMarker(boolean bRight) {
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleSaveMarkerObject() {
        if (this.mHyperDetailMeanContentTextView != null) {
            this.mHyperDetailMeanContentTextView.saveMarkerObject();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
            this.mFontSizeChangePopup = CommonUtils.makeWindowWithPopupWindow(getApplicationContext(), 0, PopupContent, getResources().getDrawable(R.drawable.popup_back), this.mOnDismissListener);
            for (int i = 0; i < group.getChildCount(); i++) {
                ((RadioButton) group.getChildAt(i)).setOnClickListener(this.mFontSizeChangeOnClickListener);
            }
            ((ImageView) PopupContent.findViewById(R.id.font_close)).setOnClickListener(this.mFontSizeChangeOnClickListener);
        }
        int fontSizeIndex = DictUtils.getFontSizeFromPreference(getApplicationContext());
        ((RadioButton) group.getChildAt(fontSizeIndex)).setChecked(true);
        int[] fontSizeList = getResources().getIntArray(R.array.value_font_size);
        this.mHyperDetailMeanController.setMeanContentTextViewTextSize(fontSizeList[fontSizeIndex]);
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

    protected void updateWordbookFolderItems(boolean existCheckedList) {
        if (this.mFlashcardFolderListViewItems != null) {
            this.mFlashcardFolderListViewItems.clear();
        }
        Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(getApplicationContext());
        TextView emptyFlashcardTextView = (TextView) this.mCopyToFlashcardLayout.findViewById(R.id.emptyFlashcard);
        emptyFlashcardTextView.setText(R.string.empty_flashcard);
        if (tCursor == null) {
            emptyFlashcardTextView.setVisibility(0);
            return;
        }
        emptyFlashcardTextView.setVisibility(8);
        if (!existCheckedList) {
            this.mCheckedWordbookList = new boolean[tCursor.getCount()];
        }
        do {
            int i = tCursor.getPosition();
            HashMap<String, Object> mFlashcardRow = new HashMap<>();
            mFlashcardRow.put(DictInfo.ListItem_WordbookName, tCursor.getString(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME)));
            mFlashcardRow.put(DictInfo.ListItem_WordCount, Integer.valueOf(DioDictDatabase.getWordbookItemCount(getApplicationContext(), tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID)))));
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

    public void resetHyperHistory() {
        for (int i = this.mHyperHistoryList.size() - 1; i >= 0; i--) {
            this.mHyperHistoryList.remove(i);
        }
    }

    public void makeWordbook() {
        int nCount = DioDictDatabase.getWordbookFolderCount(getApplicationContext());
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
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mBtnMakeWordbookOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mBtnMakeWordbookCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        this.mBackupCardName = defaultName;
        return this.mWordbookDialog;
    }

    public String getDefaultWordbookName() {
        String DefaultName = getResources().getString(R.string.default_wordbookname);
        String RetName = null;
        for (int i = 1; i <= 40; i++) {
            RetName = DefaultName + i;
            if (!DioDictDatabase.existWordbookFolder(getApplicationContext(), RetName)) {
                break;
            }
        }
        return RetName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showSoftInputMethod(boolean isShow) {
        if (this.mHyperDetailMeanContentTextView != null && isShow) {
            this.mHyperDetailMeanContentTextView.initTextSelect();
            this.mHyperDetailMeanContentTextView.invalidate();
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

    protected void restoreHyperList() {
        if (this.mHyperHistoryList == null) {
            MSG.l(3, "mHyperHistoryList is null ");
        } else if (this.mHyperHistoryCurPos >= this.mHyperHistoryList.size()) {
            MSG.l(3, "OutOfIndex ");
        } else {
            HashMap<String, Object> item = this.mHyperHistoryList.get(this.mHyperHistoryCurPos);
            String hyperWord = (String) item.get(DictInfo.INTENT_HYPER_WORD);
            int hyperSUID = ((Integer) item.get(DictInfo.INTENT_HYPER_SUID)).intValue();
            int hyperDict = ((Integer) item.get(DictInfo.INTENT_HYPER_DICT)).intValue();
            if (!this.mHyperSimpleViewModule.hyperSearchWord(hyperDict, hyperWord, hyperSUID, true)) {
            }
        }
    }

    protected void closePopupWindow() {
        if (this.mHyperDetailMeanContentTextView != null && this.mHyperDetailMeanContentTextView.isActiveTextSelectGrip()) {
            this.mHyperDetailMeanContentTextView.initTextSelect();
        }
        if (this.mMarkerColorChangePopup != null) {
            this.mMarkerColorChangePopup.dismiss();
            this.mMarkerColorChangePopup = null;
            this.mHyperDetailMeanContentTextView.setMakerMode(false);
        }
        if (this.mFontSizeChangePopup != null) {
            this.mFontSizeChangePopup.dismiss();
            this.mFontSizeChangePopup = null;
        }
        if (this.mHyperSimpleViewModule != null) {
            this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
        }
        if (this.mFileLinkTagViewManager != null) {
            this.mFileLinkTagViewManager.closeFileLinkPopup();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void backupParameters() {
        if (this.mCopyToFlashcardLayout != null) {
            if (this.mCopyToFlashcardLayout.getVisibility() == 0) {
                this.mIsFlashcardPopup = true;
            } else {
                this.mIsFlashcardPopup = false;
            }
        }
    }

    public void restoreState() {
        if (this.mIsFlashcardPopup) {
            showFlashcardListPop(true);
        }
        if (this.mCurrentDict != -1) {
            restoreHyperList();
        }
    }

    public void restoreMakeDialog() {
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing() && this.mEdittextWordbookName != null) {
            this.mEdittextWordbookName.setText(this.mBackupCardName);
            this.mEdittextWordbookName.setSelection(this.mBackupCardName.length());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideEditMessage() {
        if (this.mInputWordbookNameTextView != null) {
            this.mInputWordbookNameTextView.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reloadWordbookDialog() {
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing()) {
            this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
            removeDialog(0);
            showDialog(0);
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
            if (!this.mInputWordbookName.equals("")) {
                this.mEdittextWordbookName.setText(this.mInputWordbookName);
            }
            this.mEdittextWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
            ImageButton clearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
            if (clearBtn != null) {
                clearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
            }
        }
    }

    public void setFocusableHyperActivity(boolean bFocus) {
        if (this.mTTSManager != null) {
            this.mTTSManager.setFocusTTSBtn(bFocus);
        }
        if (this.mHyperDetailListView != null) {
            this.mHyperDetailListView.setFocusable(bFocus);
        }
        if (this.mHyperDetailTabView != null) {
            for (int i = 0; i < this.mHyperDetailTabView.getTotalCount(); i++) {
                this.mHyperDetailTabView.getButton(i).setFocusable(bFocus);
            }
        }
        if (this.mHyperDetailMeanContentTextView != null) {
            this.mHyperDetailMeanContentTextView.setFocusable(bFocus);
            this.mHyperDetailMeanContentTextView.setFocusableInTouchMode(bFocus);
        }
    }
}
