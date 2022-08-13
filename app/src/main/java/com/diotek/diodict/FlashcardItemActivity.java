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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.diotek.diodict.mean.CursorMeanController;
import com.diotek.diodict.mean.ExtendScrollView;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.FileLinkTagViewManager;
import com.diotek.diodict.mean.HyperSimpleViewModule;
import com.diotek.diodict.uitool.CheckableLayout;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.PopupFlashcardGridAdapter;
import com.diotek.diodict.uitool.TabView;
import com.diotek.diodict.uitool.TextImageButton;
import com.diotek.diodict.uitool.UITools;
import com.diotek.diodict.uitool.WordListAdapter;
import com.diodict.decompiled.R;
import com.diotek.diodict.utils.GlobalOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class FlashcardItemActivity extends ListMeanViewActivity {
    private static final int DIALOG_COPY = 1;
    private static final int DIALOG_DELETE = 0;
    private static final int DIALOG_MAKE_DIALOG = 2;
    private static final int DIALOG_SORT_DIALOG = 3;
    private static final int FLASHCARDITEM_COPY_COMPLETE = 2;
    private static final int FLASHCARDITEM_COPY_IDLE = 0;
    private static final int FLASHCARDITEM_COPY_RUNNING = 1;
    private static final int FLASHCARDITEM_DELETE_COMPLETE = 2;
    private static final int FLASHCARDITEM_DELETE_IDLE = 0;
    private static final int FLASHCARDITEM_DELETE_RUNNING = 1;
    static final int FLASHCARDLISTMODE_CHECKED = 1;
    static final int FLASHCARDLISTMODE_IDLE = 0;
    static final int FLASHCARDLISTMODE_NOITEM = 2;
    private static final int HANDLER_MSG_FLASHCARD_COPY = 1;
    private static final int HANDLER_MSG_FLASHCARD_DELELTE = 0;
    private static final int ITEM_EXIST = 1;
    private static final int ITEM_SAVED = 0;
    private static final int ITEM_SAVED_NOTALL = 2;
    private RelativeLayout mCopyToFlashcardPopLayout;
    ArrayList<HashMap<String, Object>> mFlashcardFolderListViewItems;
    private GridView mFlashcardGridView;
    ArrayList<HashMap<String, Object>> mFlashcardItemListViewItems;
    WordListAdapter mFlashcardItemListViewAdapter = null;
    WordListAdapter mFlashcardItemListViewCheckedAdapter = null;
    PopupFlashcardGridAdapter mFlashcardFolderListViewAdapter = null;
    private String mInputWordbookName = null;
    private ProgressDialog mProgressDialog = null;
    private ListView mFlashcardItemListView = null;
    private TextView mEmptyTextView = null;
    private TextView mWordbookName = null;
    private TextView mWordCountView = null;
    private Button mFlashcardItemCradleBtn = null;
    private Button mFlashcardItemStudyBtn = null;
    private Button mFlashcardItemDictationBtn = null;
    private CheckBox mFlashCardItemEditSelectAllBtn = null;
    private Button mFlashcardItemEditDeleteBtn = null;
    private Button mFlashcardItemEditCopyBtn = null;
    private Button mFlashcardItemEditBtn = null;
    private Button mFlashcardItemEditSortBtn = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardOk = null;
    private TextImageButton mFlashcardItemEditCopyToFlashcardCancel = null;
    private CheckBox mFlashcardItemEditCopyToFlashcardCheckBox = null;
    private RelativeLayout mFlashcardItemEditLayout = null;
    private LinearLayout mFlashcardTitleLayout = null;
    private LinearLayout mSelectAllLayout = null;
    private LinearLayout mFlashcardLeftLayout = null;
    private RelativeLayout mAddFlashcardLayout = null;
    private Dialog mSortDialog = null;
    private RadioButton mCard1 = null;
    private RadioButton mCard2 = null;
    private RadioButton mCard3 = null;
    private CheckBox mSortByNameCheckBox = null;
    private CheckBox mSortByTimeCheckBox = null;
    private CheckBox mSortByDictCheckBox = null;
    private TextImageButton mBtnMakeWordbookOk = null;
    private TextImageButton mBtnMakeWordbookCancel = null;
    private View mMeanContentBottomView = null;
    private TextView mSelectAllTextView = null;
    private int mFlashcardListMode = 0;
    private int mFolderId = -1;
    private int mWordCount = 0;
    private String mWordbookFolderName = "";
    private int mSort = 0;
    private Cursor mCursor = null;
    private boolean[] mCheckedWordbookList = {false};
    CursorMeanController mCursorMeanController = null;
    private int mWordbookType = 1;
    private boolean IsCallCheckedListener = true;
    private int mTabViewPos = 0;
    private int mFlashcardItemDeleteState = 0;
    private int mFlashcardItemCopyState = 0;
    private int mFlashcardItemCopyResult = 0;
    private Handler mProgressHandler = null;
    private Thread mThread = null;
    private int mLastWordPos = 0;
    private int mHeaderCount = 0;
    private boolean mIsFlashcardPopup = false;
    private SparseBooleanArray mCheckedItems = null;
    private int mInitDictType = -1;
    private int mInitSUID = -1;
    private String mInitKeyword = null;
    private boolean mIsCreate = true;
    private ExtendTextView.ExtendTextCallback mAutoUpdateLayoutCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.FlashcardItemActivity.4
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            LayoutTransition.updateLayout(true, FlashcardItemActivity.this.mStandardInnerLeftLayout, FlashcardItemActivity.this.mMainRightLayout, FlashcardItemActivity.this.getApplicationContext());
            return true;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            if (str.equals(ExtendTextView.GESTURE_SWIPE_RIGHT)) {
                LayoutTransition.updateLayoutWithExtends(false, FlashcardItemActivity.this.mStandardInnerLeftLayout, FlashcardItemActivity.this.mMainRightLayout, FlashcardItemActivity.this.mAnimationStartCallback, FlashcardItemActivity.this.mAnimationEndCallback, FlashcardItemActivity.this.getApplicationContext());
            } else {
                LayoutTransition.updateLayoutWithExtends(true, FlashcardItemActivity.this.mStandardInnerLeftLayout, FlashcardItemActivity.this.mMainRightLayout, FlashcardItemActivity.this.mAnimationStartCallback, FlashcardItemActivity.this.mAnimationEndCallback, FlashcardItemActivity.this.getApplicationContext());
            }
            return false;
        }
    };
    TabView.TabViewOnClickListener mMeanTabViewOnClickListener = new TabView.TabViewOnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.5
        @Override // com.diotek.diodict.uitool.TabView.TabViewOnClickListener
        public void onClick(View v, int nPos) {
            if (FlashcardItemActivity.this.mTextView != null) {
                FlashcardItemActivity.this.mTextView.forceScrollStop();
            }
            FlashcardItemActivity.this.mTabViewPos = nPos;
            FlashcardItemActivity.this.mHandler.removeCallbacks(FlashcardItemActivity.this.mRunnableUpdateTabView);
            FlashcardItemActivity.this.mHandler.postDelayed(FlashcardItemActivity.this.mRunnableUpdateTabView, 300L);
            if (FlashcardItemActivity.this.mMeanTabView != null) {
                FlashcardItemActivity.this.mMeanTabView.setBtnSelected(FlashcardItemActivity.this.mTabViewPos);
            }
        }
    };
    View.OnClickListener mFlashcardSelectAllLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean isChecked = !FlashcardItemActivity.this.mFlashCardItemEditSelectAllBtn.isChecked();
            FlashcardItemActivity.this.mFlashCardItemEditSelectAllBtn.setChecked(isChecked);
        }
    };
    CompoundButton.OnCheckedChangeListener mSelectAllBtnOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.7
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!FlashcardItemActivity.this.IsCallCheckedListener) {
                FlashcardItemActivity.this.IsCallCheckedListener = true;
            } else if (!isChecked) {
                FlashcardItemActivity.this.mFlashcardItemListView.clearChoices();
                FlashcardItemActivity.this.mFlashcardItemListView.requestLayout();
            } else {
                for (int i = 0; i < FlashcardItemActivity.this.mFlashcardItemListView.getCount(); i++) {
                    HashMap<String, Object> itemData = FlashcardItemActivity.this.mFlashcardItemListViewItems.get(i);
                    if (itemData != null && !FlashcardItemActivity.this.isHeaderItem(itemData)) {
                        FlashcardItemActivity.this.mFlashcardItemListView.setItemChecked(i, isChecked);
                    }
                }
            }
        }
    };
    View.OnClickListener mFlashcardItemEditDeleteBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (FlashcardItemActivity.this.mLayoutMode == 1) {
                FlashcardItemActivity.this.setSmallMeanView();
            } else {
                FlashcardItemActivity.this.runFlashcardItemEditDeleteBtn(v);
            }
        }
    };
    View.OnClickListener mFlashcardItemEditCopyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (FlashcardItemActivity.this.mLayoutMode != 1) {
                FlashcardItemActivity.this.initPopupControll();
                FlashcardItemActivity.this.showFlashcardListPopUp(false);
                return;
            }
            FlashcardItemActivity.this.setSmallMeanView();
        }
    };
    View.OnClickListener mFlashcardItemEditBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.runFlashcardEditBtn();
        }
    };
    View.OnClickListener mFlashcardItemEditSortBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.11
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!FlashcardItemActivity.this.isSmallMeanView()) {
                FlashcardItemActivity.this.closeExtendedMean();
            } else {
                FlashcardItemActivity.this.showDialog(3);
            }
        }
    };
    AdapterView.OnItemClickListener mFlashcardItemListViewOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.12
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            FlashcardItemActivity.this.mMeanTabView.setBtnSelected(0);
            FlashcardItemActivity.this.runSearchListView(position, v);
        }
    };
    AdapterView.OnItemClickListener mFlashcardGridViewOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.13
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            boolean z = true;
            FlashcardItemActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
            FlashcardItemActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.setChecked(!FlashcardItemActivity.this.mFlashcardItemEditCopyToFlashcardCheckBox.isChecked());
            boolean[] zArr = FlashcardItemActivity.this.mCheckedWordbookList;
            if (FlashcardItemActivity.this.mCheckedWordbookList[position]) {
                z = false;
            }
            zArr[position] = z;
            FlashcardItemActivity.this.mFlashcardFolderListViewItems.get(position).put(DictInfo.ListItem_WordbookFolderChecked, Boolean.valueOf(FlashcardItemActivity.this.mCheckedWordbookList[position]));
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.14
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean IsCheckedFlashcard = false;
            int i = 0;
            while (true) {
                if (i >= FlashcardItemActivity.this.mCheckedWordbookList.length) {
                    break;
                } else if (!FlashcardItemActivity.this.mCheckedWordbookList[i]) {
                    i++;
                } else {
                    IsCheckedFlashcard = true;
                    break;
                }
            }
            if (IsCheckedFlashcard) {
                FlashcardItemActivity.this.mFlashcardItemCopyState = 1;
                FlashcardItemActivity.this.showDialog(1);
                FlashcardItemActivity.this.mThread = new Thread(new Runnable() { // from class: com.diotek.diodict.FlashcardItemActivity.14.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FlashcardItemActivity.this.copyToFlashcardWordbookItem();
                        FlashcardItemActivity.this.mProgressHandler.sendEmptyMessage(1);
                    }
                });
                FlashcardItemActivity.this.mThread.start();
                return;
            }
            FlashcardItemActivity.this.showToast(FlashcardItemActivity.this.getResources().getString(R.string.selectFlashcardToCopy));
        }
    };
    View.OnClickListener mFlashcardItemEditCopyToFlashcardCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.15
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.hideFlashcardPopup();
        }
    };
    View.OnClickListener mFlashcardItemCradleBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.16
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.runFlashcardItemCradleBtn();
        }
    };
    View.OnClickListener mFlashcardItemStudyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.17
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.runFlashcardItemStudyBtn();
        }
    };
    View.OnClickListener mFlashcardItemDictationBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.18
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (DictUtils.checkSoundMode(FlashcardItemActivity.this.getApplicationContext())) {
                FlashcardItemActivity.this.runFlashcardItemDictationBtn();
            }
        }
    };
    View.OnClickListener mAddFlashcardLayoutOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.19
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            v.setSelected(true);
            FlashcardItemActivity.this.makeWordbook();
            v.setSelected(false);
        }
    };
    CompoundButton.OnCheckedChangeListener mCard1OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.20
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardItemActivity.this.mWordbookType = 1;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard2OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.21
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardItemActivity.this.mWordbookType = 2;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard3OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.22
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardItemActivity.this.mWordbookType = 3;
            }
        }
    };
    View.OnClickListener mBtnMakeWordbookOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.23
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.runBtnMakeWordbookOk(v);
        }
    };
    View.OnClickListener mBtnMakeWordbookCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.24
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.runBtnMakeWordbookCancel(v);
        }
    };
    private Runnable mSwingBackUpdateLayoutCallback = new Runnable() { // from class: com.diotek.diodict.FlashcardItemActivity.25
        @Override // java.lang.Runnable
        public void run() {
            LayoutTransition.updateLayout(false, FlashcardItemActivity.this.mStandardInnerLeftLayout, FlashcardItemActivity.this.mMainRightLayout, FlashcardItemActivity.this.getApplicationContext());
        }
    };
    CompoundButton.OnCheckedChangeListener sortByNameCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.26
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardItemActivity.this.mSort = 0;
                FlashcardItemActivity.this.mSortByTimeCheckBox.setChecked(false);
                FlashcardItemActivity.this.mSortByDictCheckBox.setChecked(false);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener sortByTimeCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.27
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardItemActivity.this.mSort = 2;
                FlashcardItemActivity.this.mSortByNameCheckBox.setChecked(false);
                FlashcardItemActivity.this.mSortByDictCheckBox.setChecked(false);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener sortByDictCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.28
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardItemActivity.this.mSort = 4;
                FlashcardItemActivity.this.mSortByNameCheckBox.setChecked(false);
                FlashcardItemActivity.this.mSortByTimeCheckBox.setChecked(false);
            }
        }
    };
    View.OnClickListener sortByASCBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.29
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.updateFlashcardItemListViewItems();
            FlashcardItemActivity.this.mLastWordPos = 0;
            FlashcardItemActivity.this.mCursorMeanController.setSort(FlashcardItemActivity.this.mSort);
            FlashcardItemActivity.this.prepareCursorMeanController();
            FlashcardItemActivity.this.showSortCompleteToast(FlashcardItemActivity.this.mSort);
            DictUtils.setFlashcardSortToPreference(FlashcardItemActivity.this.getApplicationContext(), FlashcardItemActivity.this.mSort);
            DioDictDatabase.sendCurFlashcardSortmode(FlashcardItemActivity.this, FlashcardItemActivity.this.mSort, FlashcardItemActivity.this.mFolderId);
            FlashcardItemActivity.this.removeDialog(3);
        }
    };
    View.OnClickListener sortByDSCBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.30
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.access$1912(FlashcardItemActivity.this, 1);
            FlashcardItemActivity.this.mLastWordPos = 0;
            FlashcardItemActivity.this.updateFlashcardItemListViewItems();
            FlashcardItemActivity.this.mCursorMeanController.setSort(FlashcardItemActivity.this.mSort);
            FlashcardItemActivity.this.prepareCursorMeanController();
            FlashcardItemActivity.this.showSortCompleteToast(FlashcardItemActivity.this.mSort);
            DictUtils.setFlashcardSortToPreference(FlashcardItemActivity.this.getApplicationContext(), FlashcardItemActivity.this.mSort);
            DioDictDatabase.sendCurFlashcardSortmode(FlashcardItemActivity.this, FlashcardItemActivity.this.mSort, FlashcardItemActivity.this.mFolderId);
            FlashcardItemActivity.this.removeDialog(3);
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.FlashcardItemActivity.31
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(FlashcardItemActivity.this);
        }
    };
    ExtendTextView.ExtendTextCallback mStartHyperCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.FlashcardItemActivity.32
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            FlashcardItemActivity.this.mHyperSimpleViewModule.startHyperSimple(str);
            return false;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            if (FlashcardItemActivity.this.mTextView != null) {
                FlashcardItemActivity.this.mHyperSimpleViewModule.startHyperSimple(FlashcardItemActivity.this.mTextView.getSelectedString());
                return false;
            }
            return false;
        }
    };
    HyperSimpleViewModule.HyperSimpleViewModuleCallback mHyperSimpleViewModuleCallback = new HyperSimpleViewModule.HyperSimpleViewModuleCallback() { // from class: com.diotek.diodict.FlashcardItemActivity.33
        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runDetailBtn(int meanpos) {
            Intent intent = new Intent();
            intent.setClass(FlashcardItemActivity.this, HyperFlashcardActivity.class);
            intent.putExtra(DictInfo.INTENT_HYPER_MEANPOS, meanpos);
            if (FlashcardItemActivity.this.mSelectAllLayout.getVisibility() == 0) {
                intent.putExtra(DictInfo.INTENT_HYPER_ISCHECKLIST, true);
            } else {
                intent.putExtra(DictInfo.INTENT_HYPER_ISCHECKLIST, false);
            }
            FlashcardItemActivity.this.startActivity(intent);
        }

        @Override // com.diotek.diodict.mean.HyperSimpleViewModule.HyperSimpleViewModuleCallback
        public void runExitBtn() {
            if (FlashcardItemActivity.this.mTextView != null) {
                FlashcardItemActivity.this.mTextView.clearSelection();
            }
        }
    };
    Runnable mRunnableUpdateTabView = new Runnable() { // from class: com.diotek.diodict.FlashcardItemActivity.34
        @Override // java.lang.Runnable
        public void run() {
            FlashcardItemActivity.this.runnableMeanTabView();
        }
    };
    View.OnTouchListener mMeanContentBottomViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.FlashcardItemActivity.35
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View arg0, MotionEvent event) {
            ScrollView scrollView = (ScrollView) FlashcardItemActivity.this.mTextView.getParent();
            if (scrollView == null) {
                return false;
            }
            int minHeight = FlashcardItemActivity.this.mTextView.getMeasuredHeight();
            MotionEvent tmpEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX(), event.getY() + minHeight, event.getPressure(), event.getSize(), event.getMetaState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags());
            FlashcardItemActivity.this.mTextView.onTouchEvent(tmpEvent);
            tmpEvent.recycle();
            return false;
        }
    };
    private View.OnClickListener mMemoIconOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.36
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardItemActivity.this.runListMemoBtn(Integer.parseInt(v.getTag().toString()));
        }
    };
    private View.OnFocusChangeListener mSelectAllTextViewOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.FlashcardItemActivity.37
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.isFocused()) {
                FlashcardItemActivity.this.mSelectAllTextView.setBackgroundColor(FlashcardItemActivity.this.getResources().getColor(R.color.textColor_focus));
                FlashcardItemActivity.this.mSelectAllLayout.setBackgroundColor(FlashcardItemActivity.this.getResources().getColor(R.color.textColor_focus));
                return;
            }
            FlashcardItemActivity.this.mSelectAllTextView.setBackgroundColor(FlashcardItemActivity.this.getResources().getColor(R.color.transparent));
            FlashcardItemActivity.this.mSelectAllLayout.setBackgroundColor(FlashcardItemActivity.this.getResources().getColor(R.color.transparent));
        }
    };

    static /* synthetic */ int access$1912(FlashcardItemActivity x0, int x1) {
        int i = x0.mSort + x1;
        x0.mSort = i;
        return i;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        this.mCurrentMenuId = R.id.menu_flashcard;
        if (super.onCreateActivity(savedInstanceState)) {
            this.mSort = DictUtils.getFlashcardSortFromPreference(getApplicationContext());
            initActivity();
            setProgressHandler();
        }
    }

    private void prepareInitialPosition() {
        this.mCursorMeanController.setSort(this.mSort);
        updateFlashcardItemListViewItems();
        if (this.mInitKeyword != null && this.mInitKeyword.length() > 0) {
            this.mLastWordPos = getFlashCardItemPostion(this.mInitDictType, this.mInitSUID, this.mInitKeyword);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onPause() {
        this.mFileLinkTagViewManager.onPause();
        this.mHyperSimpleViewModule.onPause();
        super.onPause();
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        this.mIsCreate = false;
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing()) {
            this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
            removeDialog(2);
            showDialog(2);
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
        this.mOrientation = getResources().getConfiguration().orientation;
        super.onConfigurationChanged(newConfig);
        if (this.mHyperSimpleViewModule != null && this.mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
            this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
        }
        if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
            this.mFileLinkTagViewManager.closeFileLinkPopup();
        }
        memoryInitialize(true);
        initActivity();
        this.mFlashcardItemEditBtn.requestFocus();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        removeDialog(1);
        removeDialog(0);
        super.onSaveInstanceState(outState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.ListMeanViewActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        if (this.mTextView != null) {
            this.mTextView.onDestroy();
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
                if (this.mProgressDialog == null) {
                    this.mProgressDialog = new ProgressDialog(this);
                }
                this.mProgressDialog.setMessage(getResources().getString(R.string.copying_flashcard_items));
                this.mProgressDialog.setIndeterminate(true);
                this.mProgressDialog.setCancelable(false);
                return this.mProgressDialog;
            case 2:
                return createMakeWordbookDialog();
            case 3:
                return createSortDialog();
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override // com.diotek.diodict.ListMeanViewActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (runKeyCodeBack()) {
                    return true;
                }
                this.bByFlashcardItem = true;
                this.nFlashcardFolderId = this.mFolderId;
                runFlashCardBtn(true);
                break;
            case 23:
            case 66:
                if ((this.mSelectAllTextView != null && this.mSelectAllTextView.isFocused()) || (this.mFlashCardItemEditSelectAllBtn != null && this.mFlashCardItemEditSelectAllBtn.isFocused())) {
                    this.mFlashCardItemEditSelectAllBtn.setChecked(!this.mFlashCardItemEditSelectAllBtn.isChecked());
                    return true;
                } else if ((this.mTextView != null && this.mTextView.isFocusable()) || (this.mMainMeanScrollView != null && this.mMainMeanScrollView.isFocusable())) {
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int nTheme = this.mCursorMeanController.getTheme();
        if (nTheme != DictUtils.getFontThemeFromPreference(getApplicationContext()) && this.mFlashcardListMode != 2) {
            this.mCursorMeanController.refreshViewNoDelay();
        }
        if (requestCode == 8 && resultCode == -1) {
            saveToMemoDB(data);
            updateFlashcardItemListViewItems();
            setLayoutMode(0, true);
            this.mLastWordPos = 0;
            if (this.mFlashcardItemListView.getCount() > 0) {
                prepareCursorMeanController();
            }
            this.mFlashcardItemListView.setSelection(0);
        }
    }

    private void initActivity() {
        backupParameters();
        setContentView(R.layout.flashcarditem_layout);
        prepareExtraData();
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        prepareContentLayout();
        restoreState();
        prepareInitialPosition();
        prepareCursorMeanController();
    }

    public void prepareExtraData() {
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            this.mFolderId = extra.getInt(DictInfo.INTENT_WORDBOOKFOLDERID);
            this.mWordCount = extra.getInt(DictInfo.INTENT_WORDCOUNT);
            this.mWordbookFolderName = intent.getExtras().getString(DictInfo.INTENT_WORDBOOKNAME);
            this.mInitDictType = extra.getInt(DictInfo.INTENT_WORDINFO_DICTTYPE);
            this.mInitSUID = extra.getInt(DictInfo.INTENT_WORDINFO_SUID);
            this.mInitKeyword = extra.getString(DictInfo.INTENT_WORDINFO_KEYWORD);
        }
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
        prepareMeanTabView();
        prepareMeanToolLayout();
        prepareMeanContentLayout();
        prepareMeanTTSLayout();
    }

    public void prepareListLayout() {
        prepareFlashcardName();
        prepareFlashcardItemListView();
    }

    public void prepareMeanToolLayout() {
        this.mFlashcardItemCradleBtn = (Button) findViewById(R.id.CradleBtn);
        this.mFlashcardItemDictationBtn = (Button) findViewById(R.id.DictationBtn);
        this.mFlashcardItemStudyBtn = (Button) findViewById(R.id.StudyBtn);
        if (Dependency.isContainCradleMode()) {
            this.mFlashcardItemCradleBtn.setOnClickListener(this.mFlashcardItemCradleBtnOnClickListener);
        } else {
            this.mFlashcardItemCradleBtn.setVisibility(View.GONE);
        }
        if (Dependency.isContainStudyMode()) {
            this.mFlashcardItemStudyBtn.setOnClickListener(this.mFlashcardItemStudyBtnOnClickListener);
        } else {
            this.mFlashcardItemStudyBtn.setVisibility(View.GONE);
        }
        if (Dependency.isContainDictationMode()) {
            this.mFlashcardItemDictationBtn.setOnClickListener(this.mFlashcardItemDictationBtnOnClickListener);
        } else {
            this.mFlashcardItemDictationBtn.setVisibility(View.GONE);
        }
    }

    public void prepareListToolLayout() {
        this.mFlashcardItemEditLayout = (RelativeLayout) findViewById(R.id.FlashcardItemEditLayout);
        this.mFlashcardItemEditBtn = (Button) findViewById(R.id.FlashcardItemEditBtn);
        this.mFlashcardItemEditSortBtn = (Button) findViewById(R.id.FlashcardItemEditSortBtn);
        this.mFlashcardItemEditDeleteBtn = (Button) findViewById(R.id.FlashcardItemEditDeleteBtn);
        this.mFlashcardItemEditCopyBtn = (Button) findViewById(R.id.FlashcardItemEditCopyBtn);
        this.mFlashCardItemEditSelectAllBtn = (CheckBox) findViewById(R.id.selectAll);
        this.mFlashcardItemEditBtn.setOnClickListener(this.mFlashcardItemEditBtnOnClickListener);
        this.mFlashcardItemEditSortBtn.setOnClickListener(this.mFlashcardItemEditSortBtnOnClickListener);
        this.mFlashcardItemEditDeleteBtn.setOnClickListener(this.mFlashcardItemEditDeleteBtnOnClickListener);
        this.mFlashcardItemEditCopyBtn.setOnClickListener(this.mFlashcardItemEditCopyBtnOnClickListener);
        this.mFlashCardItemEditSelectAllBtn.setOnCheckedChangeListener(this.mSelectAllBtnOnCheckedChangeListener);
        this.mFlashcardTitleLayout = (LinearLayout) findViewById(R.id.FlashcardTitle);
        this.mSelectAllLayout = (LinearLayout) findViewById(R.id.SelectAllLayout);
        this.mSelectAllLayout.setOnClickListener(this.mFlashcardSelectAllLayoutOnClickListener);
        this.mSelectAllTextView = (TextView) findViewById(R.id.SelectAllTextView);
        this.mSelectAllTextView.setOnFocusChangeListener(this.mSelectAllTextViewOnFocusChangeListener);
    }

    public void prepareFlashcardName() {
        this.mWordbookName = (TextView) findViewById(R.id.WordbookName);
        this.mWordCountView = (TextView) findViewById(R.id.WordCount);
    }

    public void prepareFlashcardItemListView() {
        this.mEmptyTextView = (TextView) findViewById(R.id.EmptyTextView);
        this.mFlashcardItemListView = (ListView) findViewById(R.id.FlashcardItemListView);
        if (this.mFlashcardItemListViewItems == null) {
            this.mFlashcardItemListViewItems = new ArrayList<>();
        }
        this.mFlashcardItemListViewAdapter = new WordListAdapter(getApplicationContext(), R.layout.flashcarditem_rowitem_layout, this.mFlashcardItemListViewItems);
        this.mFlashcardItemListViewCheckedAdapter = new WordListAdapter(getApplicationContext(), R.layout.flashcarditem_rowitem_copy_layout, this.mFlashcardItemListViewItems);
        this.mFlashcardItemListView.setAdapter((ListAdapter) this.mFlashcardItemListViewAdapter);
        this.mFlashcardItemListView.setVerticalFadingEdgeEnabled(true);
        this.mFlashcardItemListView.setOnItemClickListener(this.mFlashcardItemListViewOnItemClickListener);
        this.mFlashcardItemListViewAdapter.setMemoOnClickListener(this.mMemoIconOnClickListener);
        updateFlashcardItemListViewItems();
    }

    public void prepareMeanContentLayout() {
        super.prepareMeanContentLayout(false);
        if (this.mEmptyViewTitleTop != null) {
            this.mEmptyViewTitleTop.setVisibility(View.GONE);
        }
        this.mMainRightLayout = (LinearLayout) findViewById(R.id.SearchContentInnerRightLayout);
        this.mStandardInnerLeftLayout = (LinearLayout) findViewById(R.id.SearchContentStandardInnerLeftLayout);
        this.mMainMeanTitleTextView = (TextView) findViewById(R.id.MeanTitleTextView);
        prepareMeanContentLayout_byConfiguration(getResources().getConfiguration().orientation);
        this.mTextView = (ExtendTextView) findViewById(R.id.MeanContentTextView);
        this.mTextView.setOnFocusChangeListener(this.mMainMeanContentTextViewOnFocusChangeListener);
        this.mCopyToFlashcardPopLayout = (RelativeLayout) findViewById(R.id.copyToFlashcardPopLayout);
        this.mFlashcardLeftLayout = (LinearLayout) findViewById(R.id.SearchContentInnerLeftLayout);
        this.mMeanContentBottomView = findViewById(R.id.MeanContentBottomView);
        this.mMeanContentBottomView.setOnTouchListener(this.mMeanContentBottomViewOnTouchListener);
        this.mMainMeanScrollView = (ExtendScrollView) findViewById(R.id.scrollview);
        this.mMainMeanTitleTextView.setFocusable(false);
        this.mTextView.setFocusable(true);
        this.mMainMeanScrollView.setFocusable(false);
        this.mMeanContentBottomView.setFocusable(false);
        if (this.mFlashcardLeftLayout != null && getResources().getConfiguration().orientation == 2) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mFlashcardLeftLayout.getLayoutParams();
            params.weight = 1.5f;
            this.mFlashcardLeftLayout.requestLayout();
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) this.mStandardInnerLeftLayout.getLayoutParams();
            params2.weight = 1.5f;
            this.mStandardInnerLeftLayout.requestLayout();
        }
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.FlashcardItemContentRelativeLayout);
        LinearLayout parent_sub = (LinearLayout) findViewById(R.id.MeanContentLayout);
        this.mFileLinkTagViewManager = new FileLinkTagViewManager(this, this.mEngine, this.mTextView, parent, parent_sub, this.mThemeModeCallback);
        this.mCursorMeanController = new CursorMeanController(this, this.mMainMeanTitleTextView, this.mTextView, null, this.mMeanTabView, this.mEngine, this.mThemeModeCallback, this.mFileLinkTagViewManager, null);
        this.mHyperSimpleViewModule = new HyperSimpleViewModule(this, this.mHyperSimpleViewModuleCallback, parent, parent_sub, this.mTextView);
        this.mCursorMeanController.setMeanContentTextViewCallback(this.mStartHyperCallback, this.mAutoUpdateLayoutCallback, true, null);
        this.mBaseMeanController = this.mCursorMeanController;
    }

    public void prepareCursorMeanController() {
        if (this.mFlashcardItemListViewItems != null && this.mFlashcardItemListViewItems.size() > 0 && this.mFlashcardListMode != 2) {
            if (isListDictSort()) {
                if (this.mLastWordPos == 0) {
                    this.mLastWordPos++;
                }
                runSearchListView(this.mLastWordPos, null);
                return;
            }
            this.mCursorMeanController.setMeanView(DioDictDatabase.getTableName(this.mWordbookFolderName), this.mWordbookFolderName, this.mFolderId, this.mSort, this.mLastWordPos, true);
            return;
        }
        setLayoutMode(2, false);
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

    public void setProgressHandler() {
        this.mProgressHandler = new Handler() { // from class: com.diotek.diodict.FlashcardItemActivity.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (FlashcardItemActivity.this.mFlashcardItemDeleteState == 2) {
                            FlashcardItemActivity.this.removeDialog(0);
                            FlashcardItemActivity.this.updateFlashcardItemListViewItems();
                            FlashcardItemActivity.this.setLayoutMode(0, false);
                            FlashcardItemActivity.this.mLastWordPos = 0;
                            if (FlashcardItemActivity.this.mFlashcardItemListView.getCount() > 0) {
                                FlashcardItemActivity.this.prepareCursorMeanController();
                            }
                            FlashcardItemActivity.this.mFlashcardItemListView.setSelection(0);
                            FlashcardItemActivity.this.mFlashcardItemDeleteState = 0;
                            return;
                        }
                        return;
                    case 1:
                        if (FlashcardItemActivity.this.mFlashcardItemCopyState == 2) {
                            FlashcardItemActivity.this.removeDialog(1);
                            FlashcardItemActivity.this.initFlashcardItemList();
                            FlashcardItemActivity.this.setLayoutMode(0, false);
                            switch (FlashcardItemActivity.this.mFlashcardItemCopyResult) {
                                case 1:
                                    FlashcardItemActivity.this.showCopyToFlashcardLayout(false, false, true);
                                    FlashcardItemActivity.this.showToast(FlashcardItemActivity.this.getResources().getString(R.string.alreadyExistWord));
                                    break;
                                case 2:
                                    FlashcardItemActivity.this.showToast(FlashcardItemActivity.this.getResources().getString(R.string.someOfWordAlreadyExist));
                                    break;
                                default:
                                    FlashcardItemActivity.this.showCopyToFlashcardLayout(false, false, true);
                                    FlashcardItemActivity.this.showToast(FlashcardItemActivity.this.getResources().getString(R.string.savedWord));
                                    break;
                            }
                            FlashcardItemActivity.this.mFlashcardItemCopyState = 0;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void runSearchListView(int nPos, View view) {
        if (this.mTextView != null) {
            this.mTextView.forceScrollStop();
        }
        if (!isHeaderItem(this.mFlashcardItemListViewItems.get(nPos))) {
            if (this.mFlashcardListMode == 0) {
                runSearchListViewIdle(nPos);
                this.mLastWordPos = nPos;
            } else if (this.mFlashcardListMode == 1) {
                CheckableLayout layout = (CheckableLayout) view;
                if (layout != null && layout.isChecked() && getCheckedItemArray().size() > 0) {
                    getCheckedItemArray().delete(nPos);
                }
                if (view == null) {
                    HashMap<String, Object> itemData = this.mFlashcardItemListViewItems.get(nPos);
                    this.mCursorMeanController.setMeanViewKeywordInfo(Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString()), itemData.get(DictInfo.ListItem_Keyword).toString(), Integer.parseInt(itemData.get("suid").toString()), 1);
                } else if (this.mFlashCardItemEditSelectAllBtn.isChecked()) {
                    this.IsCallCheckedListener = false;
                    this.mFlashCardItemEditSelectAllBtn.setChecked(false);
                } else if (getCheckedItemArray().size() == this.mFlashcardItemListView.getCount() - this.mHeaderCount) {
                    this.IsCallCheckedListener = false;
                    this.mFlashCardItemEditSelectAllBtn.setChecked(true);
                }
            }
        }
        this.mHandler.post(this.mSwingBackUpdateLayoutCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runFlashcardEditBtn() {
        if (!isSmallMeanView()) {
            closeExtendedMean();
        } else if (this.mFlashcardListMode != 1) {
            setLayoutMode(1, false);
        }
    }

    public void runFlashcardItemEditDeleteBtn(View v) {
        if (getCheckedItemArray().size() == 0) {
            showToast(getResources().getString(R.string.selectItemToDelete));
        } else {
            new AlertDialog.Builder(this).setMessage(R.string.dialog_delete_selectedwords).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.FlashcardItemActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    FlashcardItemActivity.this.deleteWordbookItems();
                }
            }).setCancelable(true).show();
        }
    }

    public void runFlashcardItemCradleBtn() {
        finish();
        Intent intent = new Intent();
        intent.setClass(this, CradleActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
    }

    public void runFlashcardItemStudyBtn() {
        finish();
        Intent intent = new Intent();
        intent.setClass(this, StudyActivity.class);
        intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, this.mFolderId);
        intent.putExtra(DictInfo.INTENT_WORDCOUNT, this.mWordCount);
        intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, this.mWordbookFolderName);
        startActivity(intent);
    }

    public void runFlashcardItemDictationBtn() {
        ArrayList<Integer> al = DioDictDatabase.getDictTypeListfromCursor(getApplicationContext(), this.mWordbookFolderName, this.mFolderId, this.mSort, false, false);
        if (al.size() == 0) {
            showToast(getResources().getString(R.string.can_not_use_dictation));
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

    public boolean runMakeWordbookOK() {
        if (DioDictDatabase.getWordbookFolderCount(this) >= 40) {
            showToast(getResources().getString(R.string.alreadyMaxWordbook));
            return false;
        }
        this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
        this.mInputWordbookName = CommonUtils.checkSpaceInWBName(this.mInputWordbookName);
        if (this.mInputWordbookName == null || this.mInputWordbookName.equals("")) {
            showToast(getResources().getString(R.string.input_wordbookname, 0));
            return false;
        } else if (DioDictDatabase.addWordbookFolder(this, this.mInputWordbookName, this.mWordbookType) == 2) {
            showToast(getResources().getString(R.string.alreadyExistWordbook));
            return false;
        } else {
            if (this.mCursor == null) {
                this.mCursor = DioDictDatabase.getWordbookFolderCursor(this);
            } else {
                this.mCursor.close();
                this.mCursor = DioDictDatabase.getWordbookFolderCursor(this);
            }
            return true;
        }
    }

    public void runBtnMakeWordbookOk(View v) {
        if (runMakeWordbookOK()) {
            updateWordbookFolderItems(false);
            removeDialog(2);
            this.mAddFlashcardLayout.setSelected(false);
        }
    }

    public void runBtnMakeWordbookCancel(View v) {
        removeDialog(2);
        this.mAddFlashcardLayout.setSelected(false);
    }

    public void runSearchListViewIdle(int nPos) {
        runMeanTabView(0);
        selectTabAll();
        HashMap<String, Object> itemData = this.mFlashcardItemListViewItems.get(nPos);
        this.mCursorMeanController.setMeanViewKeywordInfo(Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString()), itemData.get(DictInfo.ListItem_Keyword).toString(), Integer.parseInt(itemData.get("suid").toString()), 1);
    }

    public void runMeanTabView(int nPos) {
        this.mCursorMeanController.setDisplayMode(nPos);
        this.mCursorMeanController.refreshContentView();
    }

    public boolean runKeyCodeBack() {
		if (clearTextViewSelection()) return true;
        if (this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
            hideFlashcardPopup();
            return true;
        } else if (this.mTextView.gripShowing()) {
            this.mTextView.clearSelection();
            this.mTextView.forceInvalidate();
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
            closeExtendedMean();
            return true;
        } else if (this.mFlashcardListMode != 1) {
            return false;
        } else {
            setLayoutMode(0, false);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runnableMeanTabView() {
        if (this.mTextView != null) {
            this.mTextView.stopInvilidate();
        }
        runMeanTabView(this.mTabViewPos);
    }

    private int getFlashCardItemPostion(int dictType, int suid, String keyword) {
        for (int i = 0; i < this.mFlashcardItemListViewItems.size(); i++) {
            HashMap<String, Object> item = this.mFlashcardItemListViewItems.get(i);
            Integer ndictType = (Integer) item.get(DictInfo.ListItem_DictType);
            Integer nSuid = (Integer) item.get("suid");
            String nKeyword = (String) item.get(DictInfo.ListItem_Keyword);
            if (dictType == ndictType.intValue() && suid == nSuid.intValue() && keyword.equals(nKeyword)) {
                return i;
            }
        }
        int i2 = this.mLastWordPos;
        return i2;
    }

    private boolean isEmptyFlashcardCursor() {
        Cursor tCursor = getFlashcardCursor();
        if (tCursor == null) {
            return true;
        }
        tCursor.close();
        return false;
    }

    private Cursor getFlashcardCursor() {
        return DioDictDatabase.getItemsCursor(this, this.mWordbookFolderName, this.mFolderId, this.mSort);
    }

    public boolean updateFlashcardItemListViewItems() {
        if (this.mFlashcardItemDeleteState == 1) {
            return false;
        }
        this.mWordCount = DioDictDatabase.getItemsCount(this, this.mWordbookFolderName, this.mFolderId, this.mSort);
        this.mWordbookName.setText(this.mWordbookFolderName);
        this.mWordCountView.setText("(" + this.mWordCount + ")");
        Cursor tCursor = getFlashcardCursor();
        this.mFlashcardItemListViewItems.clear();
        this.mHeaderCount = 0;
        if (tCursor == null) {
            showHideEmptyTextView(true);
            this.mFlashcardItemListViewAdapter.notifyDataSetChanged();
            return false;
        }
        showHideEmptyTextView(false);
        if (isListDictSort()) {
            boolean isAsc = true;
            if (this.mSort == 5) {
                isAsc = false;
            }
            ArrayList<Pair<String, Integer>> dicNameList = DioDictDatabase.getDictListSortByDicName(getApplicationContext(), this.mWordbookFolderName, this.mFolderId, isAsc);
            for (int index = 0; index < dicNameList.size(); index++) {
                HashMap<String, Object> titleItem = new HashMap<>();
                titleItem.put(DictInfo.ListItem_Header, dicNameList.get(index).first);
                Integer[] dicType = {(Integer) dicNameList.get(index).second};
                Cursor curDictypeItems = DioDictDatabase.getItemsCursorByDictType(getApplicationContext(), dicType, this.mWordbookFolderName, this.mFolderId, this.mSort);
                if (curDictypeItems != null) {
                    this.mFlashcardItemListViewItems.add(titleItem);
                    this.mHeaderCount++;
                    do {
                        int nColumnIndex = curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME);
                        HashMap<String, Object> nItems = new HashMap<>();
                        nItems.put(DictInfo.ListItem_DictIcon, Integer.valueOf(curDictypeItems.getInt(curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                        nItems.put(DictInfo.ListItem_DictType, Integer.valueOf(curDictypeItems.getInt(curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                        nItems.put(DictInfo.ListItem_Keyword, curDictypeItems.getString(nColumnIndex));
                        nItems.put("suid", Integer.valueOf(curDictypeItems.getInt(curDictypeItems.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID))));
                        this.mFlashcardItemListViewItems.add(nItems);
                    } while (curDictypeItems.moveToNext());
                    curDictypeItems.close();
                }
            }
        } else {
            do {
                int nColumnIndex2 = tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME);
                HashMap<String, Object> nItems2 = new HashMap<>();
                nItems2.put(DictInfo.ListItem_DictIcon, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                nItems2.put(DictInfo.ListItem_DictType, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE))));
                nItems2.put(DictInfo.ListItem_Keyword, tCursor.getString(nColumnIndex2));
                nItems2.put("suid", Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID))));
                this.mFlashcardItemListViewItems.add(nItems2);
            } while (tCursor.moveToNext());
        }
        this.mFlashcardItemListViewAdapter.notifyDataSetChanged();
        this.mFlashcardItemListViewCheckedAdapter.notifyDataSetChanged();
        this.mFlashcardItemListView.setSelection(0);
        tCursor.close();
        return true;
    }

    public void showHideEmptyTextView(boolean nVisible) {
        if (nVisible) {
            this.mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            this.mEmptyTextView.setVisibility(View.GONE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteWordbookItems() {
        if (this.mFlashcardItemListView != null && getCheckedItemArray() != null) {
            if (getCheckedItemArray().size() > 5) {
                this.mFlashcardItemDeleteState = 1;
                showDialog(0);
                new Thread(new Runnable() { // from class: com.diotek.diodict.FlashcardItemActivity.38
                    @Override // java.lang.Runnable
                    public void run() {
                        FlashcardItemActivity.this.deleteItems();
                        FlashcardItemActivity.this.mProgressHandler.sendEmptyMessage(0);
                    }
                }).start();
                return;
            }
            deleteItems();
            this.mProgressHandler.sendEmptyMessage(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteItems() {
        SparseBooleanArray CheckedItems = getCheckedItemArray();
        for (int i = this.mFlashcardItemListViewItems.size() - 1; i >= 0; i--) {
            HashMap<String, Object> itemData = this.mFlashcardItemListViewItems.get(i);
            if (!isHeaderItem(itemData) && CheckedItems.get(i)) {
                deleteItem(Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString()), itemData.get(DictInfo.ListItem_Keyword).toString(), Integer.parseInt(itemData.get("suid").toString()));
            }
        }
        this.mFlashcardItemDeleteState = 2;
    }

    private void deleteItem(int dbtype, String keyword, int suid) {
        if (this.mWordbookFolderName.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            DioDictDatabase.deleteMemo(this, dbtype, keyword, suid);
        } else if (this.mWordbookFolderName.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            if (this.mTextView != null) {
                this.mTextView.removeAllMarker();
            }
            DioDictDatabase.deleteMarker(this, dbtype, keyword, suid);
        } else {
            DioDictDatabase.deleteWordbookItem(this, dbtype, keyword, suid, this.mFolderId);
        }
    }

    public boolean copyToFlashcardWordbookItem() {
        String[] folderId = {String.valueOf(this.mFolderId)};
        boolean wordSaved = false;
        boolean wordExist = false;
        SparseBooleanArray CheckedItems = getCheckedItemArray();
        Cursor wordbookFolderCursor = DioDictDatabase.getWordbookFolderCursor(this);
        if (wordbookFolderCursor == null) {
            return false;
        }
        wordbookFolderCursor.moveToFirst();
        for (int k = 0; k < this.mFlashcardItemListViewItems.size(); k++) {
            HashMap<String, Object> itemData = this.mFlashcardItemListViewItems.get(k);
            if (!isHeaderItem(itemData) && CheckedItems.get(k)) {
                int dicType = Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString());
                String word = itemData.get(DictInfo.ListItem_Keyword).toString();
                int suid = Integer.parseInt(itemData.get("suid").toString());
                for (int i = 0; i < this.mCheckedWordbookList.length; i++) {
                    if (this.mCheckedWordbookList[i]) {
                        wordbookFolderCursor.moveToPosition(i);
                        int wbnameId = wordbookFolderCursor.getInt(wordbookFolderCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
                        if (DioDictDatabase.addWordbookItem(this, dicType, word, suid, wbnameId) == 2) {
                            wordExist = true;
                        } else {
                            wordSaved = true;
                        }
                    }
                }
            }
        }
        this.mFlashcardItemCopyState = 2;
        if (!wordSaved && !wordExist) {
            wordbookFolderCursor.close();
            return false;
        }
        if (!wordSaved && wordExist) {
            this.mFlashcardItemCopyResult = 1;
        } else if (wordSaved) {
            if (wordExist) {
                this.mFlashcardItemCopyResult = 2;
            } else {
                this.mFlashcardItemCopyResult = 0;
            }
        }
        wordbookFolderCursor.close();
        return true;
    }

    public Dialog createSortDialog() {
        if (this.mSortDialog != null) {
            removeDialog(3);
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

    public void setLayoutMode(int layoutMode, boolean bForce) {
        if (this.mFlashcardListMode != layoutMode || bForce) {
            switch (layoutMode) {
                case 0:
                    showItemsEditLayout(false);
                    initFlashcardItemList();
                    this.mFlashcardItemListView.setChoiceMode(0);
                    this.mFlashcardItemListView.setAdapter((ListAdapter) this.mFlashcardItemListViewAdapter);
                    break;
                case 1:
                    showItemsEditLayout(true);
                    this.mFlashcardItemListView.setAdapter((ListAdapter) this.mFlashcardItemListViewCheckedAdapter);
                    this.mFlashcardItemListView.setChoiceMode(2);
                    break;
                case 2:
                    setNoItemList();
                    break;
            }
            if (isEmptyFlashcardCursor() && (layoutMode == 0 || layoutMode == 1)) {
                setNoItemList();
                layoutMode = 2;
            }
            this.mFlashcardListMode = layoutMode;
            boolean isFocusable = false;
            if (layoutMode != 2) {
                isFocusable = true;
                if (isVisiableView(this.mFlashcardItemEditBtn)) {
                    this.mFlashcardItemEditBtn.requestFocus();
                } else if (isVisiableView(this.mFlashcardItemEditDeleteBtn)) {
                    this.mFlashcardItemEditDeleteBtn.requestFocus();
                }
            }
            setFocusableInLayoutMode(isFocusable);
        }
    }

    private void setNoItemList() {
        showItemsEditLayout(false);
        this.mFlashcardItemEditBtn.setEnabled(false);
        this.mFlashcardItemEditBtn.requestLayout();
        this.mFlashcardItemEditSortBtn.setEnabled(false);
        this.mFlashcardItemEditSortBtn.requestLayout();
        this.mMainMeanTitleTextView.setText("");
        this.mMainMeanTitleTextView.requestLayout();
        this.mTextView.setText("");
        this.mFlashcardTitleLayout.setVisibility(View.GONE);
        if (this.mFlashcardItemCradleBtn != null) {
            this.mFlashcardItemCradleBtn.setEnabled(false);
        }
        if (this.mFlashcardItemStudyBtn != null) {
            this.mFlashcardItemStudyBtn.setEnabled(false);
        }
        if (this.mFlashcardItemDictationBtn != null) {
            this.mFlashcardItemDictationBtn.setEnabled(false);
        }
    }

    public void showItemsEditLayout(boolean bShow) {
        if (bShow) {
            this.mFlashcardItemEditBtn.setVisibility(View.GONE);
            this.mFlashcardItemEditSortBtn.setVisibility(View.GONE);
            this.mFlashcardItemEditDeleteBtn.setVisibility(View.VISIBLE);
            this.mFlashcardItemEditCopyBtn.setVisibility(View.VISIBLE);
            this.mFlashcardTitleLayout.setVisibility(View.GONE);
            this.mSelectAllLayout.setVisibility(View.VISIBLE);
            return;
        }
        this.mFlashcardItemEditBtn.setVisibility(View.VISIBLE);
        this.mFlashcardItemEditSortBtn.setVisibility(View.VISIBLE);
        this.mFlashcardItemEditDeleteBtn.setVisibility(View.GONE);
        this.mFlashcardItemEditCopyBtn.setVisibility(View.GONE);
        this.mFlashcardTitleLayout.setVisibility(View.VISIBLE);
        this.mFlashCardItemEditSelectAllBtn.setChecked(false);
        this.mSelectAllLayout.setVisibility(View.GONE);
    }

    public void setSortMode(int sortmode) {
        this.mSort = sortmode;
    }

    public void updateWordbookFolderItems(boolean existCheckedList) {
        if (this.mFlashcardFolderListViewItems != null) {
            this.mFlashcardFolderListViewItems.clear();
        }
        Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(this);
        if (tCursor != null) {
            if (!existCheckedList) {
                this.mCheckedWordbookList = new boolean[tCursor.getCount()];
            }
            do {
                int index = tCursor.getPosition();
                HashMap<String, Object> mFlashcardRow = new HashMap<>();
                mFlashcardRow.put(DictInfo.ListItem_WordbookName, tCursor.getString(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME)));
                mFlashcardRow.put(DictInfo.ListItem_WordCount, Integer.valueOf(DioDictDatabase.getWordbookItemCount(this, tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID)))));
                mFlashcardRow.put(DictInfo.ListItem_WordbookFolderId, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID))));
                mFlashcardRow.put(DictInfo.ListItem_WordbookFolderType, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE))));
                if (existCheckedList) {
                    mFlashcardRow.put(DictInfo.ListItem_WordbookFolderChecked, Boolean.valueOf(this.mCheckedWordbookList[index]));
                } else {
                    mFlashcardRow.put(DictInfo.ListItem_WordbookFolderChecked, false);
                }
                addRowToFlashcardArrayList(index, mFlashcardRow);
            } while (tCursor.moveToNext());
            tCursor.close();
            if (this.mFlashcardFolderListViewAdapter != null) {
                this.mFlashcardFolderListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addRowToFlashcardArrayList(int CursorPos, HashMap<String, Object> row) {
        this.mFlashcardFolderListViewItems.add(row);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int showCopyToFlashcardLayout(boolean isShow, boolean stiker, boolean bAnimation) {
        boolean z = true;
        if (this.mFlashcardItemEditCopyToFlashcardOk != null) {
            this.mFlashcardItemEditCopyToFlashcardOk.setEnabled(isShow);
            this.mFlashcardItemEditCopyToFlashcardOk.setClickable(isShow);
            this.mFlashcardItemEditCopyToFlashcardCancel.setEnabled(isShow);
            this.mFlashcardItemEditCopyToFlashcardCancel.setClickable(isShow);
        }
        if (isShow) {
            this.mCopyToFlashcardPopLayout.setVisibility(View.VISIBLE);
            if (this.mFlashcardFolderListViewItems.isEmpty()) {
                this.mAddFlashcardLayout.requestFocus();
            } else {
                this.mFlashcardGridView.requestFocus();
            }
        } else {
            this.mCopyToFlashcardPopLayout.setVisibility(View.GONE);
        }
        if (bAnimation) {
            if (isShow) {
                LayoutTransition.trasition(this.mCopyToFlashcardPopLayout, isShow, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 250, false, !isShow);
            } else if (this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
                LayoutTransition.trasition(this.mCopyToFlashcardPopLayout, isShow, LayoutTransition.DIRECT_RIGHT_TO_LEFT, 250, false, !isShow);
                this.mFlashcardItemEditBtn.requestFocus();
            }
        }
        if (isShow) {
            z = false;
        }
        setFocusableFlashcardActivity(z);
        return 250;
    }

    public void showFlashcardListPopUp(boolean existCheckedList) {
        if (getCheckedItemArray().size() == 0 && !existCheckedList) {
            showToast(getResources().getString(R.string.selectItemToCopy));
            return;
        }
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        this.mFlashcardFolderListViewItems = new ArrayList<>();
        this.mFlashcardFolderListViewAdapter = new PopupFlashcardGridAdapter(this, this.mFlashcardFolderListViewItems, R.layout.flashcard_rowitem_s_copy_layout, from, to);
        this.mFlashcardFolderListViewAdapter.setCurrentFolder(this.mWordbookFolderName);
        this.mFlashcardGridView = (GridView) findViewById(R.id.copyToFlashcardGridView);
        this.mFlashcardItemEditCopyToFlashcardOk = (TextImageButton) findViewById(R.id.button_ok);
        this.mFlashcardItemEditCopyToFlashcardCancel = (TextImageButton) findViewById(R.id.button_cancel);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mFlashcardItemEditCopyToFlashcardOk.setTextSize((int) (GlobalOptions.density * 14.0f));
            this.mFlashcardItemEditCopyToFlashcardCancel.setTextSize((int) (GlobalOptions.density * 14.0f));
        }
        this.mAddFlashcardLayout = (RelativeLayout) findViewById(R.id.addCard);
        this.mFlashcardGridView.setAdapter((ListAdapter) this.mFlashcardFolderListViewAdapter);
        this.mFlashcardGridView.setOnItemClickListener(this.mFlashcardGridViewOnItemClickListener);
        this.mFlashcardItemEditCopyToFlashcardOk.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardOkOnClickListener);
        this.mFlashcardItemEditCopyToFlashcardCancel.setOnClickListener(this.mFlashcardItemEditCopyToFlashcardCancelOnClickListener);
        this.mAddFlashcardLayout.setOnClickListener(this.mAddFlashcardLayoutOnClickListener);
        this.mCopyToFlashcardPopLayout.findViewById(R.id.emptyFlashcard).setVisibility(View.GONE);
        updateWordbookFolderItems(existCheckedList);
        showCopyToFlashcardLayout(true, false, true);
    }

    public void initFlashcardItemList() {
        for (int i = 0; i < this.mCheckedWordbookList.length; i++) {
            this.mCheckedWordbookList[i] = false;
        }
        hideFlashcardPopup();
        this.mFlashcardItemListView.clearChoices();
        this.mFlashCardItemEditSelectAllBtn.setChecked(false);
    }

    public void makeWordbook() {
        int nCount = DioDictDatabase.getWordbookFolderCount(this);
        if (nCount >= 40) {
            showToast(getResources().getString(R.string.alreadyMaxWordbook));
            return;
        }
        showDialog(2);
        this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        this.mWordbookDialog.show();
    }

    public Dialog createMakeWordbookDialog() {
        if (this.mWordbookDialog != null) {
            removeDialog(2);
        }
        this.mWordbookType = 1;
        this.mWordbookDialog = new Dialog(this);
        this.mWordbookDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mWordbookDialog.requestWindowFeature(1);
        this.mWordbookDialog.setContentView(R.layout.flashcard_makedialog_layout);
        this.mWordbookDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.diotek.diodict.FlashcardItemActivity.39
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                FlashcardItemActivity.this.removeDialog(2);
                FlashcardItemActivity.this.mAddFlashcardLayout.setSelected(false);
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
        String string = getDefaultWordbookName();
        tv.setText(string);
        tv.setSelection(string.length());
        this.mBtnMakeWordbookOk = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_ok);
        this.mBtnMakeWordbookOk.setOnClickListener(this.mBtnMakeWordbookOkOnClickListener);
        this.mBtnMakeWordbookCancel = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_cancel);
        this.mBtnMakeWordbookCancel.setOnClickListener(this.mBtnMakeWordbookCancelOnClickListener);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mBtnMakeWordbookOk.setTextSize((int) (GlobalOptions.density * 14.0f));
            this.mBtnMakeWordbookCancel.setTextSize((int) (GlobalOptions.density * 14.0f));
        }
        return this.mWordbookDialog;
    }

    public void hideFlashcardPopup() {
        showCopyToFlashcardLayout(false, false, true);
    }

    private void backupParameters() {
        if (this.mCopyToFlashcardPopLayout != null) {
            if (this.mCopyToFlashcardPopLayout.getVisibility() == 0) {
                this.mIsFlashcardPopup = true;
            } else {
                this.mIsFlashcardPopup = false;
            }
        }
        if (this.mFlashcardItemListView != null) {
            this.mCheckedItems = getCheckedItemArray();
        }
    }

    private void restoreState() {
        this.mCursorMeanController.setSort(this.mSort);
        if (this.mFlashcardItemCopyState != 1 && this.mFlashcardItemDeleteState != 1) {
            setLayoutMode(this.mFlashcardListMode, true);
            restoreCheckedListState();
            if (this.mIsFlashcardPopup) {
                showFlashcardListPopUp(true);
            }
        }
        if (this.mLayoutMode == 1) {
            setWideMeanView(true);
        }
    }

    private void restoreCheckedListState() {
        if (this.mFlashcardListMode == 1 && this.mCheckedItems != null && this.mCheckedItems.size() > 0) {
            for (int index = 0; index < this.mFlashcardItemListView.getCount(); index++) {
                if (this.mCheckedItems.get(index)) {
                    getCheckedItemArray().append(index, true);
                }
            }
            if (this.mCheckedItems.size() == this.mFlashcardItemListView.getCount() - this.mHeaderCount) {
                this.mFlashCardItemEditSelectAllBtn.setChecked(true);
            }
        }
    }

    private SparseBooleanArray getCheckedItemArray() {
        return this.mFlashcardItemListView.getCheckedItemPositions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeExtendedMean() {
        LayoutTransition.updateLayoutWithExtends(false, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, getApplicationContext());
    }

    public void runListMemoBtn(int pos) {
        Intent intent = new Intent();
        intent.setClass(this, MemoActivity.class);
        intent.setFlags(603979776);
        String time_string = "";
        String data = "";
        int skin = 1;
        HashMap<String, Object> itemData = this.mFlashcardItemListViewItems.get(pos);
        int dbtype = Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString());
        String keyword = itemData.get(DictInfo.ListItem_Keyword).toString();
        int suid = Integer.parseInt(itemData.get("suid").toString());
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
            if (this.mFlashcardItemListView != null) {
                this.mFlashcardItemListView.invalidateViews();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHeaderItem(HashMap<String, Object> item) {
        String headerString;
        return (item == null || (headerString = (String) item.get(DictInfo.ListItem_Header)) == null || headerString.length() <= 0) ? false : true;
    }

    private boolean isListDictSort() {
        return this.mSort == 4 || this.mSort == 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPopupControll() {
        if (this.mTextView != null) {
            this.mTextView.clearSelection();
            this.mTextView.invalidate();
        }
    }

    private void memoryInitialize(boolean isconfigChange) {
        if (this.mFlashcardItemEditLayout != null) {
            UITools.recycleDrawable(this.mFlashcardItemEditLayout.getBackground(), false, isconfigChange);
            this.mFlashcardItemEditLayout = null;
        }
        System.gc();
    }

    public void setFocusableFlashcardActivity(boolean bFocus) {
        this.mFlashcardItemListView.setFocusable(bFocus);
        for (int i = 0; i < this.mMeanTabView.getTotalCount(); i++) {
            this.mMeanTabView.getButton(i).setFocusable(bFocus);
        }
        this.mTextView.setFocusable(bFocus);
        this.mFlashcardItemEditDeleteBtn.setFocusable(bFocus);
        this.mFlashcardItemEditCopyBtn.setFocusable(bFocus);
        this.mFlashcardItemEditSortBtn.setFocusable(bFocus);
        this.mFlashCardItemEditSelectAllBtn.setFocusable(bFocus);
        this.mSelectAllTextView.setFocusable(bFocus);
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
        this.mFlashcardItemCradleBtn.setFocusable(bFocus);
        this.mFlashcardItemStudyBtn.setFocusable(bFocus);
        this.mFlashcardItemDictationBtn.setFocusable(bFocus);
    }

    public void setFocusableInLayoutMode(boolean bFocus) {
        this.mTextView.setFocusable(bFocus);
        this.mFlashcardItemEditBtn.setFocusable(bFocus);
        this.mFlashcardItemEditSortBtn.setFocusable(bFocus);
        if (this.mFlashcardItemCradleBtn != null) {
            this.mFlashcardItemCradleBtn.setFocusable(bFocus);
        }
        if (this.mFlashcardItemStudyBtn != null) {
            this.mFlashcardItemStudyBtn.setFocusable(bFocus);
        }
        if (this.mFlashcardItemDictationBtn != null) {
            this.mFlashcardItemDictationBtn.setFocusable(bFocus);
        }
    }
}
