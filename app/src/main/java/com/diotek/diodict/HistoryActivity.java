package com.diotek.diodict;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.diotek.diodict.anim.LayoutTransition;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.CursorMeanController;
import com.diotek.diodict.mean.ExtendScrollView;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.FileLinkTagViewManager;
import com.diotek.diodict.mean.HyperSimpleViewModule;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.CheckableLayout;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.PopupFlashcardGridAdapter;
import com.diotek.diodict.uitool.TabView;
import com.diotek.diodict.uitool.TextImageButton;
import com.diotek.diodict.uitool.UITools;
import com.diotek.diodict.uitool.WordListAdapter;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class HistoryActivity extends ListMeanViewActivity {
    private static final int DIALOG_DELETE = 0;
    private static final int DIALOG_MAKE_DIALOG = 1;
    private static final int DIALOG_SORT_DIALOG = 2;
    private static final int HANDLER_MSG_HISTORY_DELETE = 2;
    static final int HISTORYLISTMODE_DELETE = 1;
    static final int HISTORYLISTMODE_IDLE = 0;
    static final int HISTORYLISTMODE_NOITEM = 2;
    private static final int HISTORY_DELETE_COMPLETE = 2;
    private static final int HISTORY_DELETE_IDLE = 0;
    private static final int HISTORY_DELETE_RUNNING = 1;
    private RelativeLayout mCopyToFlashcardPopLayout;
    private GridView mFlashcardGridView;
    ArrayList<HashMap<String, Object>> mHistoryListViewItems;
    WordListAdapter mHistoryListViewAdapter = null;
    WordListAdapter mHistoryListViewDeleteAdapter = null;
    PopupFlashcardGridAdapter mFlashcardFolderListViewAdapter = null;
    ArrayList<HashMap<String, Object>> mFlashcardFolderListViewItems = new ArrayList<>();
    private String mInputWordbookName = null;
    private ProgressDialog mProgressDialog = null;
    private RelativeLayout mHistoryToolbarLayout = null;
    private RadioButton mCard1 = null;
    private RadioButton mCard2 = null;
    private RadioButton mCard3 = null;
    private CheckBox mSortByNameCheckBox = null;
    private CheckBox mSortByTimeCheckBox = null;
    private CheckBox mSortByDictCheckBox = null;
    private ImageButton mMarkerBtn = null;
    private ImageButton mFontBtn = null;
    private ImageButton mMemoBtn = null;
    private ImageButton mSaveBtn = null;
    private ListView mHistoryListView = null;
    private TextView mEmptyTextView = null;
    private RelativeLayout mAddWordbookLayout = null;
    private CheckBox mFlashcardItemEditCopyToFlashcardCheckBox = null;
    private Button mHistoryItemDeleteBtn = null;
    private Button mHistoryItemEditBtn = null;
    private Button mHistoryItemSortBtn = null;
    private CheckBox mHistorySelectAllBtn = null;
    private LinearLayout mSelectAllLayout = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardOk = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardCancel = null;
    private Dialog mSortDialog = null;
    private TextImageButton mBtnMakeWordbookOk = null;
    private TextImageButton mBtnMakeWordbookCancel = null;
    private View mMeanContentBottomView = null;
    private TextView mSelectAllTextView = null;
    private boolean mIsCreate = true;
    CursorMeanController mCursorMeanController = null;
    private boolean[] mCheckedWordbookList = {false};
    private int mSort = 3;
    private int mHistoryListMode = 0;
    private int mWordbookType = 1;
    private int mTabViewPos = 0;
    private int mHistoryDeleteState = 0;
    Handler mHandler = new Handler();
    private int mLastWordPos = 0;
    private int mLastListPos = 0;
    private int mHeaderCount = 0;
    private int mCurDay = -1;
    private boolean IsCallCheckedListener = true;
    private Handler mProgressHandler = null;
    protected HyperSimpleViewModule mHyperSimpleViewModule = null;
    private FileLinkTagViewManager mFileLinkTagViewManager = null;
    private boolean mIsFlashcardPopup = false;
    private SparseBooleanArray mCheckedItems = null;
    private final Integer[] arrayDay = {Integer.valueOf((int) R.string.sunday), Integer.valueOf((int) R.string.monday), Integer.valueOf((int) R.string.tuesday), Integer.valueOf((int) R.string.wednesday), Integer.valueOf((int) R.string.thursday), Integer.valueOf((int) R.string.friday), Integer.valueOf((int) R.string.saturday)};
    TabView.TabViewOnClickListener mMeanTabViewOnClickListener = new TabView.TabViewOnClickListener() { // from class: com.diotek.diodict.HistoryActivity.2
        @Override // com.diotek.diodict.uitool.TabView.TabViewOnClickListener
        public void onClick(View v, int nPos) {
            HistoryActivity.this.mTabViewPos = nPos;
            HistoryActivity.this.mHandler.removeCallbacks(HistoryActivity.this.mRunnableUpdateTabView);
            HistoryActivity.this.mHandler.postDelayed(HistoryActivity.this.mRunnableUpdateTabView, 300L);
            if (HistoryActivity.this.mMeanTabView != null) {
                HistoryActivity.this.mMeanTabView.setBtnSelected(HistoryActivity.this.mTabViewPos);
            }
        }
    };
    AdapterView.OnItemClickListener mSearchListViewOnItemClickLisner = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.HistoryActivity.3
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            HistoryActivity.this.runSearchListView(position, v, false);
        }
    };
    View.OnClickListener mHistoryItemEditBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!HistoryActivity.this.isSmallMeanView()) {
                HistoryActivity.this.closeExtendedMean();
            } else {
                HistoryActivity.this.runHistoryItemEditDeleteBtn(v);
            }
        }
    };
    View.OnClickListener mHistoryItemSortBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!HistoryActivity.this.isSmallMeanView()) {
                HistoryActivity.this.closeExtendedMean();
            } else {
                HistoryActivity.this.runHistoryItemEditSortBtn(v);
            }
        }
    };
    View.OnClickListener mHistoryDeleteBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (HistoryActivity.this.mLayoutMode != 1) {
                HistoryActivity.this.runHistoryDeleteBtn();
            } else {
                HistoryActivity.this.setSmallMeanView();
            }
        }
    };
    View.OnClickListener mHistorySelectAllLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean isChecked = !HistoryActivity.this.mHistorySelectAllBtn.isChecked();
            HistoryActivity.this.mHistorySelectAllBtn.setChecked(isChecked);
        }
    };
    CompoundButton.OnCheckedChangeListener mSelectAllBtnOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.8
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!HistoryActivity.this.IsCallCheckedListener) {
                HistoryActivity.this.IsCallCheckedListener = true;
            } else if (!isChecked) {
                HistoryActivity.this.mHistoryListView.clearChoices();
                HistoryActivity.this.mHistoryListView.requestLayout();
            } else {
                for (int i = 0; i < HistoryActivity.this.mHistoryListView.getCount(); i++) {
                    HashMap<String, Object> itemData = HistoryActivity.this.mHistoryListViewItems.get(i);
                    if (itemData != null && !HistoryActivity.this.isHeaderItem(itemData)) {
                        HistoryActivity.this.mHistoryListView.setItemChecked(i, isChecked);
                    }
                }
            }
        }
    };
    private ExtendTextView.ExtendTextCallback mAutoUpdateLayoutCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.HistoryActivity.9
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            LayoutTransition.updateLayout(true, HistoryActivity.this.mStandardInnerLeftLayout, HistoryActivity.this.mMainRightLayout, HistoryActivity.this.getApplicationContext());
            return true;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            if (str.equals(ExtendTextView.GESTURE_SWIPE_RIGHT)) {
                LayoutTransition.updateLayoutWithExtends(false, HistoryActivity.this.mStandardInnerLeftLayout, HistoryActivity.this.mMainRightLayout, HistoryActivity.this.mAnimationStartCallback, HistoryActivity.this.mAnimationEndCallback, HistoryActivity.this.getApplicationContext());
            } else {
                LayoutTransition.updateLayoutWithExtends(true, HistoryActivity.this.mStandardInnerLeftLayout, HistoryActivity.this.mMainRightLayout, HistoryActivity.this.mAnimationStartCallback, HistoryActivity.this.mAnimationEndCallback, HistoryActivity.this.getApplicationContext());
            }
            return false;
        }
    };
    View.OnClickListener mSaveBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HistoryActivity.this.initPopupControll();
            HistoryActivity.this.showFlashcardListPop(false);
        }
    };
    AdapterView.OnItemClickListener mFlashcardGridViewOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.HistoryActivity.11
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            boolean z = true;
            HistoryActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
            HistoryActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.setChecked(!HistoryActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.isChecked());
            boolean[] zArr = HistoryActivity.this.mCheckedWordbookList;
            if (HistoryActivity.this.mCheckedWordbookList[position]) {
                z = false;
            }
            zArr[position] = z;
            HistoryActivity.this.mFlashcardFolderListViewItems.get(position).put(DictInfo.ListItem_WordbookFolderChecked, Boolean.valueOf(HistoryActivity.this.mCheckedWordbookList[position]));
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.12
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            String msg;
            if (HistoryActivity.this.isSelectedSaveToFlashcardFolder()) {
                boolean trans = false;
                Cursor tFlashcardCursor = DioDictDatabase.getWordbookFolderCursor(HistoryActivity.this.getApplicationContext());
                if (tFlashcardCursor != null) {
                    int dicType = HistoryActivity.this.mCursorMeanController.getDicType();
                    String word = HistoryActivity.this.mCursorMeanController.getWord();
                    int suid = HistoryActivity.this.mCursorMeanController.getSuid();
                    for (int i = 0; i < HistoryActivity.this.mCheckedWordbookList.length; i++) {
                        if (HistoryActivity.this.mCheckedWordbookList[i]) {
                            tFlashcardCursor.moveToPosition(i);
                            int wbnameId = tFlashcardCursor.getInt(tFlashcardCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
                            if (DioDictDatabase.addWordbookItem(HistoryActivity.this.getApplicationContext(), dicType, word, suid, wbnameId) == 2) {
                                msg = HistoryActivity.this.getResources().getString(R.string.alreadyExistWord);
                            } else {
                                msg = HistoryActivity.this.getResources().getString(R.string.savedWord);
                                trans = true;
                            }
                            HistoryActivity.this.showToast(msg);
                        }
                    }
                    tFlashcardCursor.close();
                    int duration = HistoryActivity.this.hideFlashcardPopup();
                    if (trans) {
                        HistoryActivity.this.mHandler.postDelayed(HistoryActivity.this.mRunTiffanyStiker, duration * 2);
                        return;
                    }
                    return;
                }
                return;
            }
            HistoryActivity.this.showToast(HistoryActivity.this.getResources().getString(R.string.selectFlashcardToSave));
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.13
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HistoryActivity.this.hideFlashcardPopup();
        }
    };
    View.OnClickListener mAddWordbookTextViewOnCLickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.14
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            v.setSelected(true);
            HistoryActivity.this.makeWordbook();
            v.setSelected(false);
        }
    };
    CompoundButton.OnCheckedChangeListener mCard1OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.15
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HistoryActivity.this.mWordbookType = 1;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard2OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.16
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HistoryActivity.this.mWordbookType = 2;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard3OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.17
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HistoryActivity.this.mWordbookType = 3;
            }
        }
    };
    View.OnClickListener mBtnMakeWordbookOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.18
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HistoryActivity.this.mAddWordbookLayout.setSelected(false);
            HistoryActivity.this.runBtnMakeWordbookOk(v);
            if (HistoryActivity.this.mFlashcardFolderListViewAdapter != null) {
                HistoryActivity.this.mFlashcardFolderListViewAdapter.notifyDataSetChanged();
            }
        }
    };
    View.OnClickListener mBtnMakeWordbookCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.19
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HistoryActivity.this.mAddWordbookLayout.setSelected(false);
            HistoryActivity.this.runBtnMakeWordbookCancel(v);
        }
    };
    private Runnable mSwingBackUpdateLayoutCallback = new Runnable() { // from class: com.diotek.diodict.HistoryActivity.20
        @Override // java.lang.Runnable
        public void run() {
            LayoutTransition.updateLayout(false, HistoryActivity.this.mStandardInnerLeftLayout, HistoryActivity.this.mMainRightLayout, HistoryActivity.this.getApplicationContext());
        }
    };
    CompoundButton.OnCheckedChangeListener sortByNameCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.21
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HistoryActivity.this.mSort = 0;
                HistoryActivity.this.mSortByTimeCheckBox.setChecked(false);
                HistoryActivity.this.mSortByDictCheckBox.setChecked(false);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener sortByTimeCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.22
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HistoryActivity.this.mSort = 2;
                HistoryActivity.this.mSortByNameCheckBox.setChecked(false);
                HistoryActivity.this.mSortByDictCheckBox.setChecked(false);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener sortByDictCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.HistoryActivity.23
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                HistoryActivity.this.mSort = 4;
                HistoryActivity.this.mSortByNameCheckBox.setChecked(false);
                HistoryActivity.this.mSortByTimeCheckBox.setChecked(false);
            }
        }
    };
    View.OnClickListener sortByASCBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.24
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HistoryActivity.this.removeDialog(2);
            DictUtils.setSearchCursorInfoToPreference(HistoryActivity.this.getApplicationContext(), HistoryActivity.this.mSort, HistoryActivity.this.mLastListPos);
            HistoryActivity.this.updateHistoryListViewItems();
            HistoryActivity.this.initSelection();
            HistoryActivity.this.prepareCursorMeanController();
            HistoryActivity.this.showSortCompleteToast(HistoryActivity.this.mSort);
        }
    };
    View.OnClickListener sortByDSCBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.25
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HistoryActivity.this.removeDialog(2);
            HistoryActivity.access$412(HistoryActivity.this, 1);
            DictUtils.setSearchCursorInfoToPreference(HistoryActivity.this.getApplicationContext(), HistoryActivity.this.mSort, HistoryActivity.this.mLastListPos);
            HistoryActivity.this.updateHistoryListViewItems();
            HistoryActivity.this.initSelection();
            HistoryActivity.this.prepareCursorMeanController();
            HistoryActivity.this.showSortCompleteToast(HistoryActivity.this.mSort);
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.HistoryActivity.26
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(HistoryActivity.this.getApplicationContext());
        }
    };
    ExtendTextView.ExtendTextCallback mStartHyperCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.HistoryActivity.27
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            HistoryActivity.this.mHyperSimpleViewModule.startHyperSimple(str);
            return false;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            if (HistoryActivity.this.mMainMeanContentTextView != null) {
                HistoryActivity.this.mHyperSimpleViewModule.startHyperSimple(HistoryActivity.this.mMainMeanContentTextView.getSelectedString());
                return false;
            }
            return false;
        }
    };
    HyperSimpleViewModule.HyperSimpleViewModuleCallback mHyperSimpleViewModuleCallback = new HyperSimpleViewModule.HyperSimpleViewModuleCallback() { // from class: com.diotek.diodict.HistoryActivity.28
        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runDetailBtn(int meanpos) {
            DictUtils.setSearchCursorInfoToPreference(HistoryActivity.this.getApplicationContext(), HistoryActivity.this.mSort, HistoryActivity.this.mLastListPos);
            Intent intent = new Intent();
            intent.setClass(HistoryActivity.this, HyperHistoryActivity.class);
            intent.putExtra(DictInfo.INTENT_HYPER_MEANPOS, meanpos);
            if (HistoryActivity.this.mSelectAllLayout.getVisibility() == 0) {
                intent.putExtra(DictInfo.INTENT_HYPER_ISCHECKLIST, true);
            } else {
                intent.putExtra(DictInfo.INTENT_HYPER_ISCHECKLIST, false);
            }
            HistoryActivity.this.mActivityManager.addActivity(HistoryActivity.this);
            HistoryActivity.this.startActivity(intent);
        }

        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runExitBtn() {
            if (HistoryActivity.this.mMainMeanContentTextView != null) {
                HistoryActivity.this.mMainMeanContentTextView.initTextSelect();
            }
        }
    };
    Runnable mRunnableUpdateTabView = new Runnable() { // from class: com.diotek.diodict.HistoryActivity.29
        @Override // java.lang.Runnable
        public void run() {
            HistoryActivity.this.runnableMeanTabView();
        }
    };
    View.OnTouchListener mMeanContentBottomViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.HistoryActivity.30
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView scrollView = (ScrollView) HistoryActivity.this.mMainMeanContentTextView.getParent();
            if (scrollView == null) {
                return false;
            }
            int minHeight = HistoryActivity.this.mMainMeanContentTextView.getMeasuredHeight();
            MotionEvent tmpEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX(), event.getY() + minHeight, event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags());
            HistoryActivity.this.mMainMeanContentTextView.onTouchEvent(tmpEvent);
            tmpEvent.recycle();
            return false;
        }
    };
    private View.OnFocusChangeListener mSelectAllTextViewOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.HistoryActivity.31
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.isFocused()) {
                HistoryActivity.this.mSelectAllTextView.setBackgroundColor(HistoryActivity.this.getResources().getColor(R.color.textColor_focus));
                HistoryActivity.this.mSelectAllLayout.setBackgroundColor(HistoryActivity.this.getResources().getColor(R.color.textColor_focus));
                return;
            }
            HistoryActivity.this.mSelectAllTextView.setBackgroundColor(HistoryActivity.this.getResources().getColor(R.color.transparent));
            HistoryActivity.this.mSelectAllLayout.setBackgroundColor(HistoryActivity.this.getResources().getColor(R.color.transparent));
        }
    };
    private Runnable mRunTiffanyStiker = new Runnable() { // from class: com.diotek.diodict.HistoryActivity.35
        @Override // java.lang.Runnable
        public void run() {
            if (HistoryActivity.this.mTfTrans != null) {
                Window win = HistoryActivity.this.getWindow();
                View root = win.getDecorView();
                HistoryActivity.this.mTfTrans.setRootView(root);
                HistoryActivity.this.mTfTrans.setTransToView(HistoryActivity.this.mMainMeanBookmarkImageView);
                if (win.isActive()) {
                    try {
                        HistoryActivity.this.mTfTrans.transition(13, 0);
                        return;
                    } catch (OutOfMemoryError e) {
                        System.gc();
                        return;
                    }
                }
                return;
            }
            HistoryActivity.this.mMainMeanBookmarkImageView.setVisibility(0);
        }
    };

    static /* synthetic */ int access$412(HistoryActivity x0, int x1) {
        int i = x0.mSort + x1;
        x0.mSort = i;
        return i;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        setContentView(R.layout.history_layout);
        this.mCurrentMenuId = R.id.menu_history;
        if (super.onCreateActivity(savedInstanceState)) {
            this.mIsCreate = true;
            initActivity(savedInstanceState);
            setProgressHandler();
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BundleKey.LISTMODE, this.mHistoryListMode);
        outState.putInt(BundleKey.SORT, this.mSort);
        outState.putInt(BundleKey.LAYOUTMODE, this.mLayoutMode);
        outState.putInt(BundleKey.LAST_POS, this.mLastWordPos);
        outState.putBooleanArray(BundleKey.CHECKED_WORDBOOK_LIST, this.mCheckedWordbookList);
        DictUtils.setSearchCursorInfoToPreference(getApplicationContext(), this.mSort, this.mLastListPos);
        if (this.mCopyToFlashcardPopLayout != null && this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
            outState.putBoolean(BundleKey.SAVE_POPUP, true);
        } else {
            outState.putBoolean(BundleKey.SAVE_POPUP, false);
        }
        super.onSaveInstanceState(outState);
    }

    private void prepareSearchExtraInfo() {
        int selectPos = this.mLastListPos;
        if ((isListDictSort() || isListTimeSort()) && this.mLastListPos == 0) {
            this.mLastListPos = 1;
        }
        if ((isListDictSort() || isListTimeSort()) && selectPos == 1) {
            selectPos = 0;
        }
        setListSelectPos(this.mLastListPos);
        if (this.mHistoryListView != null) {
            this.mHistoryListView.setSelection(selectPos);
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onResume() {
        super.onResume();
        prepareSearchExtraInfo();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onPause() {
        handleSaveMarkerObject();
        this.mFileLinkTagViewManager.onPause();
        this.mHyperSimpleViewModule.onPause();
        super.onPause();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        this.mOrientation = getResources().getConfiguration().orientation;
        super.onConfigurationChanged(newConfig);
        this.mIsCreate = false;
        memoryInitialize(true);
        if (this.mHyperSimpleViewModule != null && this.mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
            this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
        }
        if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
            this.mFileLinkTagViewManager.closeFileLinkPopup();
        }
        initActivity(null);
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing()) {
            this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
            removeDialog(1);
            showDialog(1);
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
        if (isVisiableView(this.mHistoryItemEditBtn)) {
            this.mHistoryItemEditBtn.requestFocus();
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onDestroy() {
        if (this.mMainMeanContentTextView != null) {
            this.mMainMeanContentTextView.onDestroy();
        }
        if (this.mCursorMeanController != null) {
            this.mCursorMeanController.onDestory();
        }
        memoryInitialize(false);
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                if (this.mProgressDialog == null) {
                    this.mProgressDialog = new ProgressDialog(this);
                }
                this.mProgressDialog.setMessage(getResources().getString(R.string.delete_progress));
                this.mProgressDialog.setIndeterminate(true);
                this.mProgressDialog.setCancelable(false);
                return this.mProgressDialog;
            case 1:
                return createMakeWordbookDialog();
            case 2:
                return createSortDialog();
            default:
                return super.onCreateDialog(id);
        }
    }

    public boolean runKeyCodeBack() {
        if (this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
            hideFlashcardPopup();
            return true;
        } else if (this.mMainMeanContentTextView.isActiveTextSelectGrip()) {
            this.mMainMeanContentTextView.initTextSelect();
            this.mMainMeanContentTextView.forceInvalidate();
            return true;
        } else if (isTTSRepeat()) {
            dismissTTSRepeat();
            return true;
        } else if (this.mHyperSimpleViewModule != null && this.mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
            this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
            return true;
        } else if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
            this.mFileLinkTagViewManager.closeFileLinkPopup();
            return true;
        } else if (this.mStandardInnerLeftLayout.getVisibility() == 8) {
            LayoutTransition.updateLayoutWithExtends(false, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, getApplicationContext());
            return true;
        } else if (this.mHistoryListMode != 1) {
            return false;
        } else {
            setLayoutMode(0);
            return true;
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (runKeyCodeBack()) {
                    return true;
                }
                runSearchBtn(true);
                break;
            case 23:
            case 66:
                if ((this.mSelectAllTextView != null && this.mSelectAllTextView.isFocused()) || (this.mHistorySelectAllBtn != null && this.mHistorySelectAllBtn.isFocused())) {
                    this.mHistorySelectAllBtn.setChecked(!this.mHistorySelectAllBtn.isChecked());
                    return true;
                } else if ((this.mMainMeanContentTextView != null && this.mMainMeanContentTextView.isFocusable()) || (this.mMainMeanScrollView != null && this.mMainMeanScrollView.isFocusable())) {
                    LayoutTransition.updateLayoutWithExtends(true, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, this);
                    return true;
                } else if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
                    this.mFileLinkTagViewManager.setFocusLinkTextPopup();
                    return true;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int nTheme = this.mCursorMeanController.getTheme();
        if (nTheme != DictUtils.getFontThemeFromPreference(getApplicationContext()) && this.mHistoryListMode != 2) {
            this.mCursorMeanController.refreshViewNoDelay();
        }
    }

    private void initActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            backupParameters();
        }
        setContentView(R.layout.history_layout);
        prepareTitleLayout(R.string.title_history, this.mIsCreate);
        if (savedInstanceState == null) {
            this.mSort = DictUtils.getSearchCursorInfoSortFromPreference(getApplicationContext());
        }
        prepareContentLayout();
        restoreState(savedInstanceState);
        prepareCursorMeanController();
        restoreMakeDialog();
    }

    public void prepareContentLayout() {
        prepareMainLeftLayout();
        prepareMainRightLayout();
    }

    public void prepareMainLeftLayout() {
        prepareListToolLayout();
        prepareListLayout();
    }

    public void prepareMainRightLayout() {
        prepareMeanToolLayout();
        prepareMeanTabView();
        prepareMeanContentLayout();
        prepareMeanTTSLayout();
    }

    public void prepareListLayout() {
        prepareHistoryListView();
    }

    public void prepareMeanToolLayout() {
        this.mHistoryToolbarLayout = (RelativeLayout) findViewById(R.id.HistoryToolbarLayout);
        this.mMeanToolbarLayout = (LinearLayout) findViewById(R.id.MeanToolbarLayout);
        this.mMarkerBtn = (ImageButton) findViewById(R.id.MarkerBtn);
        this.mFontBtn = (ImageButton) findViewById(R.id.FontBtn);
        this.mMemoBtn = (ImageButton) findViewById(R.id.MemoBtn);
        this.mSaveBtn = (ImageButton) findViewById(R.id.SaveBtn);
        this.mSaveBtn.setOnClickListener(this.mSaveBtnOnClickListener);
        this.mMarkerBtn.setVisibility(8);
        this.mFontBtn.setVisibility(8);
        this.mMemoBtn.setVisibility(8);
        this.mSaveBtn.setVisibility(0);
    }

    public void prepareMeanContentLayout() {
        super.prepareMeanContentLayout(true);
        this.mMainRightLayout = (LinearLayout) findViewById(R.id.SearchContentInnerRightLayout);
        this.mStandardInnerLeftLayout = (LinearLayout) findViewById(R.id.SearchContentStandardInnerLeftLayout);
        this.mMainMeanTitleTextView = (TextView) findViewById(R.id.MeanTitleTextView);
        this.mMainMeanContentTextView = (ExtendTextView) findViewById(R.id.MeanContentTextView);
        this.mMainMeanContentTextView.setOnFocusChangeListener(this.mMainMeanContentTextViewOnFocusChangeListener);
        this.mMainMeanBookmarkImageView = (ImageView) findViewById(R.id.bookmark);
        prepareMeanContentLayout_byConfiguration(getResources().getConfiguration().orientation);
        this.mCopyToFlashcardPopLayout = (RelativeLayout) findViewById(R.id.copyToFlashcardPopLayout);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.HistoryContentRelativeLayout);
        LinearLayout parent_sub = (LinearLayout) findViewById(R.id.MeanContentLayout);
        this.mMainMeanScrollView = (ExtendScrollView) findViewById(R.id.scrollview);
        this.mMeanContentBottomView = findViewById(R.id.MeanContentBottomView);
        this.mMeanContentBottomView.setOnTouchListener(this.mMeanContentBottomViewOnTouchListener);
        this.mMainMeanTitleTextView.setFocusable(false);
        this.mMainMeanContentTextView.setFocusable(true);
        this.mMainMeanScrollView.setFocusable(false);
        this.mMeanContentBottomView.setFocusable(false);
        this.mFileLinkTagViewManager = new FileLinkTagViewManager(this, this.mEngine, this.mMainMeanContentTextView, parent, parent_sub, this.mThemeModeCallback);
        this.mCursorMeanController = new CursorMeanController(this, this.mMainMeanTitleTextView, this.mMainMeanContentTextView, this.mMainMeanBookmarkImageView, this.mMeanTabView, this.mEngine, this.mThemeModeCallback, this.mFileLinkTagViewManager, null);
        this.mHyperSimpleViewModule = new HyperSimpleViewModule(this, this.mHyperSimpleViewModuleCallback, parent, parent_sub, this.mMainMeanContentTextView);
        this.mCursorMeanController.setMeanContentTextViewCallback(this.mStartHyperCallback, this.mAutoUpdateLayoutCallback, true, null);
        this.mBaseMeanController = this.mCursorMeanController;
    }

    public void prepareCursorMeanController() {
        if (this.mHistoryListViewItems != null && this.mHistoryListViewItems.size() > 0 && this.mHistoryListMode != 2) {
            if (this.mLastListPos < this.mHistoryListViewItems.size()) {
                if ((isListDictSort() || isListTimeSort()) && this.mLastListPos == 0) {
                    this.mLastListPos = 1;
                }
                HashMap<String, Object> item = this.mHistoryListViewItems.get(this.mLastListPos);
                if (item != null) {
                    if (isListDictSort()) {
                        this.mLastWordPos = this.mLastListPos;
                    } else {
                        this.mLastWordPos = ((Integer) item.get(DictInfo.ListItem_cursorPos)).intValue();
                    }
                } else {
                    this.mLastListPos = 0;
                    this.mLastWordPos = 0;
                }
            } else {
                this.mLastListPos = 0;
                this.mLastWordPos = 0;
            }
            if (isListDictSort()) {
                runSearchListView(this.mLastWordPos, null, true);
                return;
            }
            this.mCursorMeanController.setSort(this.mSort);
            if (this.mCursorMeanController.setMeanView(DioDictDatabaseInfo.TABLENAME_HISTORYITEM, null, 0, this.mSort, this.mLastWordPos, true) == 1) {
                setLayoutMode(2);
                return;
            }
            return;
        }
        setLayoutMode(2);
    }

    public void prepareListToolLayout() {
        this.mHistoryItemEditBtn = (Button) findViewById(R.id.HistoryEditBtn);
        this.mHistoryItemSortBtn = (Button) findViewById(R.id.HistorySortBtn);
        this.mHistoryItemDeleteBtn = (Button) findViewById(R.id.HistoryDeleteBtn);
        this.mHistorySelectAllBtn = (CheckBox) findViewById(R.id.selectAll);
        this.mHistoryItemEditBtn.setOnClickListener(this.mHistoryItemEditBtnOnClickListener);
        this.mHistoryItemSortBtn.setOnClickListener(this.mHistoryItemSortBtnOnClickListener);
        this.mHistoryItemDeleteBtn.setOnClickListener(this.mHistoryDeleteBtnOnClickListener);
        this.mHistorySelectAllBtn.setOnCheckedChangeListener(this.mSelectAllBtnOnCheckedChangeListener);
        this.mSelectAllLayout = (LinearLayout) findViewById(R.id.SelectAllLayout);
        this.mSelectAllLayout.setOnClickListener(this.mHistorySelectAllLayoutOnClickListener);
        this.mSelectAllTextView = (TextView) findViewById(R.id.SelectAllTextView);
        this.mSelectAllTextView.setOnFocusChangeListener(this.mSelectAllTextViewOnFocusChangeListener);
    }

    public void prepareHistoryListView() {
        this.mHistoryListView = (ListView) findViewById(R.id.HistoryListView);
        if (this.mHistoryListViewItems == null) {
            this.mHistoryListViewItems = new ArrayList<>();
        }
        this.mHistoryListViewAdapter = new WordListAdapter(getApplicationContext(), R.layout.historyitem_rowitem_layout, this.mHistoryListViewItems);
        this.mHistoryListViewDeleteAdapter = new WordListAdapter(getApplicationContext(), R.layout.historyitem_rowitem_delete_layout, this.mHistoryListViewItems);
        this.mHistoryListView.setAdapter((ListAdapter) this.mHistoryListViewAdapter);
        this.mHistoryListView.setOnItemClickListener(this.mSearchListViewOnItemClickLisner);
        this.mEmptyTextView = (TextView) findViewById(R.id.EmptyTextView);
        updateHistoryListViewItems();
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
        super.prepareMeanTabView();
        this.mMeanTabView.setOnClickListener(this.mMeanTabViewOnClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isListTimeSort() {
        return this.mSort == 2 || this.mSort == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isListDictSort() {
        return this.mSort == 4 || this.mSort == 5;
    }

    public void setProgressHandler() {
        this.mProgressHandler = new Handler() { // from class: com.diotek.diodict.HistoryActivity.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 2:
                        if (HistoryActivity.this.mHistoryDeleteState != 1) {
                            HistoryActivity.this.dismissDialog();
                            HistoryActivity.this.removeDialog(0);
                            HistoryActivity.this.showHideEmptyTextView(0);
                            HistoryActivity.this.mHistoryListViewAdapter.notifyDataSetChanged();
                            HistoryActivity.this.mHistoryListViewDeleteAdapter.notifyDataSetChanged();
                            HistoryActivity.this.updateHistoryListViewItems();
                            HistoryActivity.this.setLayoutMode(0);
                            if (HistoryActivity.this.mHistoryListMode != 2) {
                                if (!HistoryActivity.this.isListDictSort()) {
                                    HistoryActivity.this.mCursorMeanController.setMeanView(DioDictDatabaseInfo.TABLENAME_HISTORYITEM, null, 0, HistoryActivity.this.mSort, HistoryActivity.this.mLastWordPos, true);
                                } else {
                                    HistoryActivity.this.prepareCursorMeanController();
                                }
                            }
                            if ((HistoryActivity.this.isListDictSort() || HistoryActivity.this.isListTimeSort()) && HistoryActivity.this.mHistoryListView.getChildCount() > 1) {
                                HistoryActivity.this.mHistoryListView.setSelection(1);
                            } else {
                                HistoryActivity.this.mHistoryListView.setSelection(0);
                            }
                            HistoryActivity.this.mSelectAllTextView.requestFocus();
                            HistoryActivity.this.mHistoryDeleteState = 0;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void runHistoryItemEditDeleteBtn(View v) {
        if (this.mHistoryListMode != 1) {
            setLayoutMode(1);
        } else {
            setLayoutMode(0);
        }
    }

    public void runHistoryItemEditSortBtn(View v) {
        showDialog(2);
    }

    private boolean isDifferenceWord(int pos) {
        int dbtype = ((Integer) this.mHistoryListViewItems.get(pos).get(DictInfo.ListItem_DictType)).intValue();
        int suid = ((Integer) this.mHistoryListViewItems.get(pos).get("suid")).intValue();
        int meanDbtype = this.mMainMeanContentTextView.getDbtype();
        int meanSuid = this.mMainMeanContentTextView.getSuid();
        if (dbtype != meanDbtype || suid != meanSuid) {
            return true;
        }
        return false;
    }

    public void runBtnMakeWordbookOk(View v) {
        if (runMakeWordbookOK()) {
            updateWordbookFolderItems(false);
            removeDialog(1);
        }
    }

    public void runBtnMakeWordbookCancel(View v) {
        removeDialog(1);
    }

    public boolean runMakeWordbookOK() {
        if (DioDictDatabase.getWordbookFolderCount(getApplicationContext()) >= 40) {
            showToast(getString(R.string.alreadyMaxWordbook));
            return false;
        }
        if (this.mEdittextWordbookName == null) {
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        }
        this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
        this.mInputWordbookName = CommonUtils.checkSpaceInWBName(this.mInputWordbookName);
        if (this.mInputWordbookName == null || this.mInputWordbookName.equals("")) {
            showToast(getString(R.string.input_wordbookname));
            return false;
        } else if (DioDictDatabase.addWordbookFolder(getApplicationContext(), this.mInputWordbookName, this.mWordbookType) == 2) {
            showToast(getString(R.string.alreadyExistWordbook));
            return false;
        } else {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runnableMeanTabView() {
        if (this.mMainMeanContentTextView != null) {
            this.mMainMeanContentTextView.stopInvilidate();
        }
        runMeanTabView(this.mTabViewPos);
    }

    private void setListSelectPos(int pos) {
        if (this.mHistoryListViewAdapter != null) {
            this.mHistoryListViewAdapter.notifyDataSetInvalidated();
        }
        this.mLastListPos = pos;
    }

    public void runSearchListView(int nPos, View view, boolean always) {
        if (this.mMainMeanContentTextView != null) {
            this.mMainMeanContentTextView.forceScrollStop();
        }
        if (!isHeaderItem(this.mHistoryListViewItems.get(nPos))) {
            if (this.mHistoryListMode == 0) {
                if (isDifferenceWord(nPos) || always) {
                    this.mLastWordPos = nPos;
                    setListSelectPos(nPos);
                    runMeanTabView(0);
                    selectTabAll();
                    HashMap<String, Object> itemData = this.mHistoryListViewItems.get(nPos);
                    this.mCursorMeanController.setMeanViewKeywordInfo(Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString()), itemData.get(DictInfo.ListItem_Keyword).toString(), Integer.parseInt(itemData.get("suid").toString()), 1);
                }
            } else if (this.mHistoryListMode == 1) {
                if (view.getId() != R.id.history_rowitem_delete_layout || view.getId() < 0) {
                    MSG.l("runSearchListView() : view ID = " + view.getId());
                    return;
                }
                CheckableLayout layout = (CheckableLayout) view;
                if (layout != null && layout.isChecked() && getCheckedItemArray().size() > 0) {
                    getCheckedItemArray().delete(nPos);
                }
                if (always) {
                    HashMap<String, Object> itemData2 = this.mHistoryListViewItems.get(nPos);
                    this.mCursorMeanController.setMeanViewKeywordInfo(Integer.parseInt(itemData2.get(DictInfo.ListItem_DictType).toString()), itemData2.get(DictInfo.ListItem_Keyword).toString(), Integer.parseInt(itemData2.get("suid").toString()), 1);
                } else if (this.mHistorySelectAllBtn.isChecked()) {
                    this.IsCallCheckedListener = false;
                    this.mHistorySelectAllBtn.setChecked(false);
                } else if (getCheckedItemArray().size() == this.mHistoryListView.getCount() - this.mHeaderCount) {
                    this.IsCallCheckedListener = false;
                    this.mHistorySelectAllBtn.setChecked(true);
                }
            }
        }
        this.mHandler.post(this.mSwingBackUpdateLayoutCallback);
    }

    public void runMeanTabView(int nMode) {
        this.mCursorMeanController.setDisplayMode(nMode);
        this.mCursorMeanController.refreshContentView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runHistoryDeleteBtn() {
        if (getCheckedItemArray().size() == 0) {
            showToast(getResources().getString(R.string.selectItemToDelete));
        } else {
            new AlertDialog.Builder(this).setMessage(R.string.dialog_delete_selectedwords).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.33
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.HistoryActivity.32
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    HistoryActivity.this.runDelete();
                }
            }).setCancelable(true).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runDelete() {
        if (this.mHistoryListView != null && getCheckedItemArray() != null) {
            if (getCheckedItemArray().size() > 5) {
                this.mHistoryDeleteState = 1;
                showDialog(0);
                new Thread(new Runnable() { // from class: com.diotek.diodict.HistoryActivity.34
                    @Override // java.lang.Runnable
                    public void run() {
                        HistoryActivity.this.deleteHistoryItems();
                        HistoryActivity.this.mProgressHandler.sendEmptyMessage(2);
                        HistoryActivity.this.mProgressHandler.postAtFrontOfQueue(new Runnable() { // from class: com.diotek.diodict.HistoryActivity.34.1
                            @Override // java.lang.Runnable
                            public void run() {
                                HistoryActivity.this.setRequestedOrientation(-1);
                            }
                        });
                    }
                }).start();
                return;
            }
            deleteHistoryItems();
            this.mProgressHandler.sendEmptyMessage(2);
        }
    }

    public void deleteHistoryItems() {
        SparseBooleanArray CheckedItems = getCheckedItemArray();
        for (int i = this.mHistoryListViewItems.size() - 1; i >= 0; i--) {
            HashMap<String, Object> item = this.mHistoryListViewItems.get(i);
            if (!isHeaderItem(item) && CheckedItems.get(i)) {
                DioDictDatabase.deleteHistory(getApplicationContext(), Integer.parseInt(item.get(DictInfo.ListItem_DictType).toString()), item.get(DictInfo.ListItem_Keyword).toString(), Integer.parseInt(item.get("suid").toString()));
            }
        }
        initSelection();
        this.mHistoryDeleteState = 2;
    }

    private boolean isEmptyHistoryCursor() {
        Cursor tCursor = getHistoryCursor();
        if (tCursor == null) {
            return true;
        }
        tCursor.close();
        return false;
    }

    private Cursor getHistoryCursor() {
        return DioDictDatabase.getHistoryCursor(getApplicationContext(), this.mSort);
    }

    public int updateHistoryListViewItems() {
        if (this.mHistoryDeleteState == 1) {
            return 0;
        }
        this.mCurDay = -1;
        Cursor tCursor = getHistoryCursor();
        this.mHistoryListViewItems.clear();
        this.mHeaderCount = 0;
        if (tCursor == null) {
            showHideEmptyTextView(0);
            this.mHistoryListViewAdapter.notifyDataSetChanged();
            this.mHistoryListViewDeleteAdapter.notifyDataSetChanged();
            return 1;
        }
        showHideEmptyTextView(8);
        int nFolderId = tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
        if (isListDictSort()) {
            boolean isAsc = true;
            if (this.mSort == 5) {
                isAsc = false;
            }
            ArrayList<Pair<String, Integer>> dicNameList = DioDictDatabase.getDictListSortByDicName(getApplicationContext(), DioDictDatabaseInfo.FOLDERNAME_HISTORY, nFolderId, isAsc);
            for (int index = 0; index < dicNameList.size(); index++) {
                HashMap<String, Object> titleItem = new HashMap<>();
                titleItem.put(DictInfo.ListItem_Header, dicNameList.get(index).first);
                Integer[] dicType = {(Integer) dicNameList.get(index).second};
                Cursor curDictypeItems = DioDictDatabase.getItemsCursorByDictType(getApplicationContext(), dicType, DioDictDatabaseInfo.FOLDERNAME_HISTORY, nFolderId, this.mSort);
                if (curDictypeItems != null) {
                    this.mHistoryListViewItems.add(titleItem);
                    this.mHeaderCount++;
                    do {
                        int nColumnIndex = curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME);
                        HashMap<String, Object> nItems = new HashMap<>();
                        nItems.put(DictInfo.ListItem_DictIcon, Integer.valueOf(curDictypeItems.getInt(curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                        nItems.put(DictInfo.ListItem_DictType, Integer.valueOf(curDictypeItems.getInt(curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                        nItems.put(DictInfo.ListItem_Keyword, curDictypeItems.getString(nColumnIndex));
                        nItems.put("suid", Integer.valueOf(curDictypeItems.getInt(curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID))));
                        this.mHistoryListViewItems.add(nItems);
                    } while (curDictypeItems.moveToNext());
                    curDictypeItems.close();
                }
            }
        } else {
            do {
                int nColumnIndex2 = tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME);
                Date historyDate = new Date(tCursor.getLong(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_TIME)));
                HashMap<String, Object> nItems2 = new HashMap<>();
                if (isListTimeSort()) {
                    addTimeHeaderItem(historyDate);
                }
                nItems2.put(DictInfo.ListItem_Keyword, tCursor.getString(nColumnIndex2));
                nItems2.put(DictInfo.ListItem_DictIcon, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                nItems2.put(DictInfo.ListItem_DictType, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                nItems2.put("suid", Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID))));
                nItems2.put(DictInfo.ListItem_cursorPos, Integer.valueOf(tCursor.getPosition()));
                this.mHistoryListViewItems.add(nItems2);
            } while (tCursor.moveToNext());
            tCursor.close();
        }
        this.mHistoryListViewAdapter.notifyDataSetChanged();
        this.mHistoryListViewDeleteAdapter.notifyDataSetChanged();
        this.mHistoryListView.setSelection(0);
        return 0;
    }

    public void showHideEmptyTextView(int nVisible) {
        this.mEmptyTextView.setVisibility(nVisible);
    }

    private void addTimeHeaderItem(Date historyDate) {
        boolean bToday = isToday(historyDate);
        boolean bYesterday = isYesterday(historyDate);
        HashMap<String, Object> nTitleItems = new HashMap<>();
        if (isListTimeSort()) {
            if (bToday && this.mCurDay != 7) {
                nTitleItems.put(DictInfo.ListItem_Header, getString(R.string.today));
                this.mCurDay = 7;
                this.mHeaderCount++;
                this.mHistoryListViewItems.add(nTitleItems);
            } else if (bYesterday && this.mCurDay != 10) {
                nTitleItems.put(DictInfo.ListItem_Header, getString(R.string.yesterday));
                this.mCurDay = 10;
                this.mHeaderCount++;
                this.mHistoryListViewItems.add(nTitleItems);
            } else if (!bToday && !bYesterday && isThisWeek(historyDate) && this.mCurDay != historyDate.getDay()) {
                nTitleItems.put(DictInfo.ListItem_Header, getString(this.arrayDay[historyDate.getDay()].intValue()));
                this.mCurDay = historyDate.getDay();
                this.mHeaderCount++;
                this.mHistoryListViewItems.add(nTitleItems);
            } else if (!bToday && !bYesterday && !isThisWeek(historyDate) && isThisMonth(historyDate) && this.mCurDay != 8) {
                nTitleItems.put(DictInfo.ListItem_Header, getString(R.string.ThisMonth));
                this.mCurDay = 8;
                this.mHeaderCount++;
                this.mHistoryListViewItems.add(nTitleItems);
            } else if (!bToday && !bYesterday && !isThisWeek(historyDate)) {
                if ((!isThisMonth(historyDate)) && this.mCurDay != 9) {
                    nTitleItems.put(DictInfo.ListItem_Header, getString(R.string.oldItems));
                    this.mCurDay = 9;
                    this.mHeaderCount++;
                    this.mHistoryListViewItems.add(nTitleItems);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHeaderItem(HashMap<String, Object> item) {
        String headerString;
        return (item == null || (headerString = (String) item.get(DictInfo.ListItem_Header)) == null || headerString.length() <= 0) ? false : true;
    }

    public boolean isToday(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        Date todayDate = todayCalendar.getTime();
        return todayDate.getDay() == date.getDay() && todayDate.getMonth() == date.getMonth() && todayDate.getYear() == date.getYear() && todayDate.getDate() == date.getDate();
    }

    public boolean isYesterday(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        Date todayDate = todayCalendar.getTime();
        Date yesterdayDate = todayCalendar.getTime();
        yesterdayDate.setTime(todayDate.getTime() - 86400000);
        return yesterdayDate.getDay() == date.getDay() && yesterdayDate.getMonth() == date.getMonth() && yesterdayDate.getYear() == date.getYear() && yesterdayDate.getDate() == date.getDate();
    }

    public boolean isThisWeek(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        Date todayDate = todayCalendar.getTime();
        return todayDate.getTime() - 604800000 < date.getTime();
    }

    public boolean isThisMonth(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        Date todayDate = todayCalendar.getTime();
        return todayDate.getMonth() == date.getMonth() && todayDate.getYear() == date.getYear();
    }

    public void setLayoutMode(int layoutMode) {
        switch (layoutMode) {
            case 0:
                showItemsEditLayout(false);
                this.mHistoryListView.clearChoices();
                this.mHistorySelectAllBtn.setChecked(false);
                this.mHistoryListView.setChoiceMode(0);
                this.mHistoryListView.setAdapter((ListAdapter) this.mHistoryListViewAdapter);
                break;
            case 1:
                showItemsEditLayout(true);
                this.mHistoryListView.setChoiceMode(2);
                this.mHistoryListView.setAdapter((ListAdapter) this.mHistoryListViewDeleteAdapter);
                break;
            case 2:
                setNoItemList();
                break;
        }
        if (isEmptyHistoryCursor() && (layoutMode == 0 || layoutMode == 1)) {
            setNoItemList();
            layoutMode = 2;
        }
        this.mHistoryListMode = layoutMode;
        boolean isFocusable = false;
        if (layoutMode != 2) {
            isFocusable = true;
            if (isVisiableView(this.mHistoryItemEditBtn)) {
                this.mHistoryItemEditBtn.requestFocus();
            } else if (isVisiableView(this.mHistoryItemDeleteBtn)) {
                this.mHistoryItemDeleteBtn.requestFocus();
            }
        }
        setFocusableInLayoutMode(isFocusable);
    }

    private void setNoItemList() {
        showItemsEditLayout(false);
        if (this.mCursorMeanController != null) {
            this.mCursorMeanController.refreshEmptyView();
        }
        this.mHistoryItemEditBtn.setEnabled(false);
        this.mHistoryItemSortBtn.setEnabled(false);
        this.mHistoryItemSortBtn.setVisibility(0);
        this.mMainMeanTitleTextView.setText("");
        this.mMainMeanTitleTextView.requestLayout();
        this.mMainMeanContentTextView.setText("");
        this.mSelectAllLayout.setVisibility(8);
        this.mSaveBtn.setEnabled(false);
    }

    public void showItemsEditLayout(boolean bShow) {
        if (bShow) {
            this.mHistoryItemEditBtn.setVisibility(8);
            this.mHistoryItemSortBtn.setVisibility(8);
            this.mHistoryItemDeleteBtn.setVisibility(0);
            this.mSelectAllLayout.setVisibility(0);
            return;
        }
        this.mHistoryItemEditBtn.setVisibility(0);
        this.mHistoryItemSortBtn.setVisibility(0);
        this.mHistoryItemDeleteBtn.setVisibility(8);
        this.mSelectAllLayout.setVisibility(8);
    }

    public void setSortMode(int sortmode) {
        this.mSort = sortmode;
    }

    protected void handleSaveMarkerObject() {
        if (this.mMainMeanContentTextView != null) {
            this.mMainMeanContentTextView.saveMarkerObject();
        }
    }

    private int showCopyToFlashcardLayout(boolean isShow) {
        boolean z = true;
        if (isShow) {
            this.mCopyToFlashcardPopLayout.setVisibility(0);
            if (this.mFlashcardFolderListViewItems.isEmpty()) {
                ((RelativeLayout) findViewById(R.id.addCard)).requestFocus();
            } else {
                this.mFlashcardGridView.requestFocus();
            }
        } else {
            this.mCopyToFlashcardPopLayout.setVisibility(8);
        }
        if (isShow) {
            LayoutTransition.trasition(this.mCopyToFlashcardPopLayout, isShow, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 250, false, !isShow);
        } else if (this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
            LayoutTransition.trasition(this.mCopyToFlashcardPopLayout, isShow, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 250, false, !isShow);
            this.mSaveBtn.requestFocus();
        }
        if (isShow) {
            z = false;
        }
        setFocusableHistoryActivity(z);
        return 250;
    }

    public void showFlashcardListPop(boolean existCheckedList) {
        ((TextView) this.mCopyToFlashcardPopLayout.findViewById(R.id.copyToFlashcardPopTitle)).setText(getResources().getString(R.string.selectFlashcardToSave));
        this.mFlashcardItemEditCopyToFlashcardOk = (TextImageButton) findViewById(R.id.button_ok);
        this.mFlashcardItemEditCopyToFlashcardCancel = (TextImageButton) findViewById(R.id.button_cancel);
        this.mFlashcardItemEditCopyToFlashcardOk.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardOkOnClickListener);
        this.mFlashcardItemEditCopyToFlashcardCancel.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardCancelOnClickListener);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mFlashcardItemEditCopyToFlashcardOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mFlashcardItemEditCopyToFlashcardCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        this.mAddWordbookLayout = (RelativeLayout) findViewById(R.id.addCard);
        this.mAddWordbookLayout.setOnClickListener(this.mAddWordbookTextViewOnCLickListener);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mFlashcardItemEditCopyToFlashcardOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mFlashcardItemEditCopyToFlashcardCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        this.mFlashcardFolderListViewAdapter = new PopupFlashcardGridAdapter(this, this.mFlashcardFolderListViewItems, R.layout.flashcard_rowitem_s_copy_layout, from, to);
        this.mFlashcardGridView = (GridView) findViewById(R.id.copyToFlashcardGridView);
        this.mFlashcardGridView.setAdapter((ListAdapter) this.mFlashcardFolderListViewAdapter);
        this.mFlashcardGridView.setOnItemClickListener(this.mFlashcardGridViewOnItemClickListener);
        updateWordbookFolderItems(existCheckedList);
        showCopyToFlashcardLayout(true);
    }

    public void updateWordbookFolderItems(boolean existCheckedList) {
        if (this.mFlashcardFolderListViewItems != null) {
            this.mFlashcardFolderListViewItems.clear();
        }
        Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(getApplicationContext());
        TextView emptyFlashcardTextView = (TextView) this.mCopyToFlashcardPopLayout.findViewById(R.id.emptyFlashcard);
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

    public Dialog createMakeWordbookDialog() {
        if (this.mWordbookDialog != null) {
            removeDialog(1);
        }
        String defaultName = getDefaultWordbookName();
        this.mWordbookType = 1;
        this.mWordbookDialog = new Dialog(this);
        this.mWordbookDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mWordbookDialog.requestWindowFeature(1);
        this.mWordbookDialog.setContentView(R.layout.flashcard_makedialog_layout);
        this.mWordbookDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.diotek.diodict.HistoryActivity.36
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                HistoryActivity.this.runBtnMakeWordbookCancel(null);
                HistoryActivity.this.mAddWordbookLayout.setSelected(false);
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
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mBtnMakeWordbookOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mBtnMakeWordbookCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        this.mBackupCardName = defaultName;
        return this.mWordbookDialog;
    }

    public Dialog createSortDialog() {
        if (this.mSortDialog != null) {
            removeDialog(2);
        }
        this.mSortDialog = new Dialog(this);
        this.mSortDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mSortDialog.requestWindowFeature(1);
        this.mSortDialog.setContentView(R.layout.sort_dialog_layout);
        this.mSortByNameCheckBox = (CheckBox) this.mSortDialog.findViewById(R.id.sortByNameCheckBox);
        this.mSortByTimeCheckBox = (CheckBox) this.mSortDialog.findViewById(R.id.sortByTimeCheckBox);
        this.mSortByDictCheckBox = (CheckBox) this.mSortDialog.findViewById(R.id.sortByDictCheckBox);
        this.mSortByNameCheckBox.setOnCheckedChangeListener(this.sortByNameCheckBoxOnCheckedChangeListener);
        this.mSortByTimeCheckBox.setOnCheckedChangeListener(this.sortByTimeCheckBoxOnCheckedChangeListener);
        this.mSortByDictCheckBox.setOnCheckedChangeListener(this.sortByDictCheckBoxOnCheckedChangeListener);
        String systemLanguage = getResources().getConfiguration().locale.getDisplayLanguage(Locale.ENGLISH);
        if (systemLanguage.equalsIgnoreCase("Japanese") || systemLanguage.equalsIgnoreCase("Korean")) {
            int textSize = getResources().getDimensionPixelSize(R.dimen.flashcard_newpopup_btn_fontSize);
            ((TextImageButton) this.mSortDialog.findViewById(R.id.byASCBtn)).setTextSize(textSize);
            ((TextImageButton) this.mSortDialog.findViewById(R.id.byDSCBtn)).setTextSize(textSize);
        }
        ((TextImageButton) this.mSortDialog.findViewById(R.id.byASCBtn)).setOnClickListener(this.sortByASCBtnOnClickListener);
        ((TextImageButton) this.mSortDialog.findViewById(R.id.byDSCBtn)).setOnClickListener(this.sortByDSCBtnOnClickListener);
        if (this.mSort < 2) {
            this.mSortByNameCheckBox.setChecked(true);
        } else if (this.mSort < 4) {
            this.mSortByTimeCheckBox.setChecked(true);
        } else {
            this.mSortByDictCheckBox.setChecked(true);
        }
        return this.mSortDialog;
    }

    public void makeWordbook() {
        int nCount = DioDictDatabase.getWordbookFolderCount(getApplicationContext());
        if (nCount >= 40) {
            showToast(getString(R.string.alreadyMaxWordbook));
            return;
        }
        showDialog(1);
        this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        this.mEdittextWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
        this.mInputWordbookNameTextView = (TextView) this.mWordbookDialog.findViewById(R.id.editview_editwordbook);
        ImageButton clearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
        if (clearBtn != null) {
            clearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
        }
        this.mWordbookDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPopupControll() {
        if (this.mMainMeanContentTextView != null) {
            this.mMainMeanContentTextView.initTextSelect();
            this.mMainMeanContentTextView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int hideFlashcardPopup() {
        int duration = showCopyToFlashcardLayout(false);
        return duration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    private void backupParameters() {
        if (this.mCopyToFlashcardPopLayout != null) {
            if (this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
                this.mIsFlashcardPopup = true;
            } else {
                this.mIsFlashcardPopup = false;
            }
        }
        if (this.mHistoryListView != null) {
            this.mCheckedItems = getCheckedItemArray();
        }
    }

    private void restoreState(Bundle bundle) {
        if (bundle != null) {
            this.mSort = bundle.getInt(BundleKey.SORT);
            this.mLastWordPos = bundle.getInt(BundleKey.LAST_POS);
            this.mLastListPos = DictUtils.getSearchCursorInfoWordPosFromPreference(getApplicationContext());
            this.mHistoryListMode = bundle.getInt(BundleKey.LISTMODE);
            this.mIsFlashcardPopup = bundle.getBoolean(BundleKey.SAVE_POPUP);
            this.mLayoutMode = bundle.getInt(BundleKey.LAYOUTMODE);
            if (this.mIsFlashcardPopup) {
                this.mCheckedWordbookList = new boolean[DioDictDatabase.getWordbookFolderCount(getApplicationContext())];
                this.mCheckedWordbookList = bundle.getBooleanArray(BundleKey.CHECKED_WORDBOOK_LIST);
            }
        }
        switch (this.mHistoryListMode) {
            case 0:
                setLayoutMode(0);
                if (this.mLayoutMode == 1) {
                    setWideMeanView(true);
                    break;
                }
                break;
            case 1:
                setLayoutMode(1);
                if (this.mLayoutMode == 1) {
                    setWideMeanView(true);
                    break;
                }
                break;
            case 2:
                setLayoutMode(2);
                break;
        }
        this.mCursorMeanController.setSort(this.mSort);
        restoreCheckedListState();
        if (this.mIsFlashcardPopup) {
            showFlashcardListPop(true);
        }
    }

    private void restoreCheckedListState() {
        if (this.mHistoryListMode == 1 && this.mCheckedItems != null && this.mCheckedItems.size() > 0) {
            for (int index = 0; index < this.mHistoryListView.getCount(); index++) {
                if (this.mCheckedItems.get(index)) {
                    getCheckedItemArray().append(index, true);
                }
            }
            if (this.mCheckedItems.size() == this.mHistoryListView.getCount() - this.mHeaderCount) {
                this.mHistorySelectAllBtn.setChecked(true);
            }
        }
    }

    private SparseBooleanArray getCheckedItemArray() {
        return this.mHistoryListView.getCheckedItemPositions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeExtendedMean() {
        LayoutTransition.updateLayoutWithExtends(false, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, getApplicationContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSelection() {
        this.mLastWordPos = 0;
        if (isListDictSort() || isListTimeSort()) {
            setListSelectPos(this.mLastWordPos + 1);
        } else {
            setListSelectPos(this.mLastWordPos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSelectedSaveToFlashcardFolder() {
        for (int i = 0; i < this.mCheckedWordbookList.length; i++) {
            if (this.mCheckedWordbookList[i]) {
                return true;
            }
        }
        return false;
    }

    private void memoryInitialize(boolean isconfigChange) {
        if (this.mHistoryToolbarLayout != null) {
            UITools.recycleDrawable(this.mHistoryToolbarLayout.getBackground(), false, isconfigChange);
            this.mHistoryToolbarLayout = null;
        }
        System.gc();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity
    protected void checkSymbolKeyword() {
        if (this.mCursorMeanController != null) {
            int nDictype = this.mCursorMeanController.getDicType();
            if (nDictype == getResources().getInteger(R.integer.DEDT_OXFORD_NEW_AMERICAN_DICTIONARY)) {
                String keyword = this.mCursorMeanController.getWord();
                if (EngineInfo3rd.IsTTSAvailableKeyword(keyword)) {
                    setEnableTTSButton(true);
                    this.mSaveBtn.setEnabled(true);
                    return;
                }
                setEnableTTSButton(false);
                this.mSaveBtn.setEnabled(false);
                return;
            }
            setEnableTTSButton(true);
            this.mSaveBtn.setEnabled(true);
        }
    }

    public void setFocusableHistoryActivity(boolean bFocus) {
        this.mHistoryListView.setFocusable(bFocus);
        for (int i = 0; i < this.mMeanTabView.getTotalCount(); i++) {
            this.mMeanTabView.getButton(i).setFocusable(bFocus);
        }
        this.mMainMeanContentTextView.setFocusable(bFocus);
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
        this.mHistoryItemEditBtn.setFocusable(bFocus);
        this.mHistoryItemDeleteBtn.setFocusable(bFocus);
        this.mHistoryItemSortBtn.setFocusable(bFocus);
        this.mSelectAllTextView.setFocusable(bFocus);
        this.mSaveBtn.setFocusable(bFocus);
    }

    private void setFocusableInLayoutMode(boolean bFocus) {
        this.mMainMeanContentTextView.setFocusable(bFocus);
        this.mHistoryItemEditBtn.setFocusable(bFocus);
        this.mHistoryItemSortBtn.setFocusable(bFocus);
    }
}
