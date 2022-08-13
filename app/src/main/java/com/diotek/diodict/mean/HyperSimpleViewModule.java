package com.diotek.diodict.mean;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.diotek.diodict.GridViewAdapter;
import com.diotek.diodict.TTSManager;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.CustomPopupLinearLayout;
import com.diodict.decompiled.R;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.utils.GlobalOptions;

import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class HyperSimpleViewModule {
    public static final int HYPERPOPUP_MAX_LINE_COUNT = 4;
    public static final int HYPERPOPUP_MODE_NEARWORD = 1;
    public static final int HYPERPOPUP_MODE_SUMMARY = 2;
    public static final int HYPERPOPUP_MODE_SUPPORTDICT = 0;
    public static final int LAYOUTMODE_MAIN_LINEAR_SUB_RELATIVE = 1;
    public static final int LAYOUTMODE_MAIN_RELATIVE_SUB_LINEAR = 0;
    public static final int LAYOUTMODE_MAIN_RELATIVE_SUB_RELATIVE = 2;
    Activity mContext;
    private EngineManager3rd mEngine;
    HyperSimpleViewModuleCallback mHyperSimpleViewCallback;
    ExtendTextView mMeanContentTextView;
    public ExtendTextView mTextView;
    View mStartParent;
    View mStartParentSub;
    private TTSManager mTTSManager;
    SearchMeanController mHyperDetailMeanController = null;
    PopupWindow mHyperDialogPopup = null;
    int mParentsHeight = 0;
    Integer[] mEnableDictList = null;
    int mHyperSimpleMeanPos = 0;
    String mHyperDetailHistoryWord = null;
    int mHyperDetailHistorySUID = 0;
    int mHyperDetailHistoryDict = 0;
    int mPopupWidth = 0;
    int mPopupHeight = 0;
    String mHyperText = null;
    Button detailBtn = null;
    ArrayList<HashMap<String, Object>> mWordListItems = null;
    ArrayList<HashMap<String, Object>> mDictListItems = null;
    int mLayoutMode = 0;
    String mSelectedWord = null;
    boolean mDismissPopupWithoutHyperText = true;
    View.OnTouchListener mParentRelativeLayoutOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.1
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };
    View.OnClickListener mHyperDialogPopupOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (HyperSimpleViewModule.this.mHyperDialogPopup != null) {
                dismissHyperPopup(false);
                HyperSimpleViewModule.this.mHyperDialogPopup = null;
            }
        }
    };
    CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback mCustomPopupLinearLayoutOnKeyListenerCallback = new CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.3
        @Override // com.diotek.diodict.uitool.CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback
        public void runOnKeyListener(KeyEvent event) {
            if (HyperSimpleViewModule.this.mHyperDialogPopup != null) {
                dismissHyperPopup(true);
                HyperSimpleViewModule.this.mHyperDialogPopup = null;
            }
        }
    };
    View.OnClickListener mHyperDialogBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hyper_detail /* 2131099998 */:
                    HyperSimpleViewModule.this.mDismissPopupWithoutHyperText = false;
                    HyperSimpleViewModule.this.closeHyperTextSummaryPopup(false);
                    HyperSimpleViewModule.this.mHyperSimpleViewCallback.runDetailBtn(HyperSimpleViewModule.this.mHyperSimpleMeanPos);
                    return;
                default:
                    return;
            }
        }
    };
    private PopupWindow.OnDismissListener mHyperTextOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.6
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (HyperSimpleViewModule.this.mDismissPopupWithoutHyperText) {
                HyperSimpleViewModule.this.mEngine.setDicType(DictUtils.getSearchLastDictFromPreference(HyperSimpleViewModule.this.mContext));
                HyperSimpleViewModule.this.mHyperSimpleViewCallback.runExitBtn();
            }
			if (mTextView != null && mTextView.gripShowing()) {
				mTextView.clearSelection();
			}
            HyperSimpleViewModule.this.mHyperDialogPopup = null;
        }
    };
    private PopupWindow.OnDismissListener mHyperTextSupportDictListOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.7
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            if (HyperSimpleViewModule.this.mDismissPopupWithoutHyperText) {
                HyperSimpleViewModule.this.mHyperSimpleViewCallback.runExitBtn();
            }
            HyperSimpleViewModule.this.mHyperDialogPopup = null;
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.8
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return DictUtils.getFontThemeFromPreference(HyperSimpleViewModule.this.mContext);
        }
    };
    AdapterView.OnItemClickListener mHyperDialogPopupOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.9
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HyperSimpleViewModule.this.mDismissPopupWithoutHyperText = false;
            if (HyperSimpleViewModule.this.mHyperDialogPopup != null && HyperSimpleViewModule.this.mHyperDialogPopup.isShowing()) {
                dismissHyperPopup(false);
                HyperSimpleViewModule.this.mHyperDialogPopup = null;
            }
            HyperSimpleViewModule.this.prepareHyperView(HyperSimpleViewModule.this.mEnableDictList[position].intValue(), HyperSimpleViewModule.this.mHyperText);
        }
    };
    AdapterView.OnItemClickListener mNearestListPopupOnItemClickLisner = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.10
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            HyperSimpleViewModule.this.runNearestListPopup(position);
        }
    };

    /* loaded from: classes.dex */
    public interface HyperSimpleViewModuleCallback {
        void runDetailBtn(int i);

        void runExitBtn();
    }

    public HyperSimpleViewModule(Activity context, HyperSimpleViewModuleCallback hyperCallback, RelativeLayout startParentRelativeLayout, LinearLayout startParentSubLinearLayout, ExtendTextView meanContextTextView) {
        this.mEngine = null;
        this.mContext = null;
        this.mStartParent = null;
        this.mStartParentSub = null;
        this.mMeanContentTextView = null;
        this.mHyperSimpleViewCallback = null;
        this.mContext = context;
        this.mEngine = EngineManager3rd.getInstance(this.mContext);
        this.mHyperSimpleViewCallback = hyperCallback;
        this.mStartParent = startParentRelativeLayout;
        this.mStartParentSub = startParentSubLinearLayout;
        startParentRelativeLayout.setOnTouchListener(this.mParentRelativeLayoutOnTouchListener);
        this.mMeanContentTextView = meanContextTextView;
    }

    public HyperSimpleViewModule(Activity context, HyperSimpleViewModuleCallback hyperCallback, RelativeLayout startParentRelativeLayout, RelativeLayout startParentSubRelativeLayout, ExtendTextView meanContextTextView) {
        this.mEngine = null;
        this.mContext = null;
        this.mStartParent = null;
        this.mStartParentSub = null;
        this.mMeanContentTextView = null;
        this.mHyperSimpleViewCallback = null;
        this.mContext = context;
        this.mEngine = EngineManager3rd.getInstance(this.mContext);
        this.mHyperSimpleViewCallback = hyperCallback;
        this.mStartParent = startParentRelativeLayout;
        this.mStartParentSub = startParentSubRelativeLayout;
        startParentRelativeLayout.setOnTouchListener(this.mParentRelativeLayoutOnTouchListener);
        this.mMeanContentTextView = meanContextTextView;
    }

    public void onPause() {
        if (this.mTTSManager != null) {
            this.mTTSManager.onTerminateTTS();
        }
    }

    public void onDestory() {
        closeHyperTextSummaryPopup(true);
    }

    public boolean startHyperSimple(String word) {
        this.mHyperText = word;
        return handleHyperTextCallback(word);
    }

    public boolean handleHyperTextCallback(String word) {
        this.mEnableDictList = DictUtils.makeHyperDicList(word, this.mContext);
        if (this.mEnableDictList == null) {
            return false;
        }
        if (this.mEnableDictList.length > 1) {
            showHyperTextSupportDictSelectPopup();
        } else {
            prepareHyperView(this.mEnableDictList[0].intValue(), word);
        }
        return true;
    }

    private void showHyperTextSupportDictSelectPopup() {
        this.mPopupHeight = createSimpleViewPopup(0);
        showHyperListPopup(this.mPopupHeight);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareHyperView(int nDicType, String word) {
        if (hyperSearchWord(nDicType, word, false)) {
            if (this.mEngine.getHyperTextExactMatch(1)) {
                this.mHyperSimpleMeanPos = this.mEngine.getResultListKeywordPos(1);
                prepareHyperTextSummaryMeanPopup(this.mHyperSimpleMeanPos);
                return;
            }
            this.mSelectedWord = word;
            prepareNearestListPopup();
        }
    }

    private boolean hyperSearchWord(int nDicType, String word, boolean bListUpdate) {
        if (word.length() > DictInfo.MAX_HYPERTEXTWORD_LENGTH) {
            word = word.substring(0, DictInfo.MAX_HYPERTEXTWORD_LENGTH);
        }
        this.mEngine.setDicType(nDicType);
        EngineManager3rd.SearchMethodInfo searchMethodInfoWord = this.mEngine.createSearchMethodInfo(R.string.word_search, 1);
        this.mEngine.setSearchMethod(searchMethodInfoWord);
        return this.mEngine.searchByCheckWildChar(word, word, 1);
    }

    public boolean hyperSearchWord(int nDicType, String word, int suid, boolean bListUpdate) {
        if (word.length() > DictInfo.MAX_HYPERTEXTWORD_LENGTH) {
            word = word.substring(0, DictInfo.MAX_HYPERTEXTWORD_LENGTH);
        }
        this.mEngine.setDicType(nDicType);
        EngineManager3rd.SearchMethodInfo searchMethodInfoWord = this.mEngine.createSearchMethodInfo(R.string.word_search, 1);
        this.mEngine.setSearchMethod(searchMethodInfoWord);
        return this.mEngine.searchByCheckWildChar(word, suid, word, 1);
    }

    public int createSimpleViewPopup(int nHyperDetailPopupMode) {
        CustomPopupLinearLayout PopupContent;
        int popupHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.hyper_text_maximum_height);
        LayoutInflater inflate = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        switch (nHyperDetailPopupMode) {
            case 0:
            case 1:
                PopupContent = (CustomPopupLinearLayout) inflate.inflate(R.layout.hyper_dialog, (ViewGroup) null);
                break;
            case 2:
                PopupContent = (CustomPopupLinearLayout) inflate.inflate(R.layout.hypertext_summary_layout, (ViewGroup) null);
                break;
            default:
                return 0;
        }
        PopupContent.setOnClickListener(this.mHyperDialogPopupOnClickListener);
        PopupContent.setOnKeyListenerCallback(this.mCustomPopupLinearLayoutOnKeyListenerCallback);
        this.mHyperDialogPopup = CommonUtils.makeWindowWithPopupWindow(this.mContext, 0, PopupContent, this.mContext.getResources().getDrawable(R.drawable.trans), null);
        this.mDismissPopupWithoutHyperText = true;
        ScrollView scrollView = (ScrollView) PopupContent.findViewById(R.id.hyper_scrollview);
        ExtendTextView textView = (ExtendTextView) PopupContent.findViewById(R.id.hypertext_mean_textview);
		if(textView!=null) {
			textView.bShowGripsFromPop = true;
			textView.setEnableTextSelect(true);
		}
		mTextView = textView;
		TextView keywordView = (TextView) PopupContent.findViewById(R.id.hyper_mean_titleview);
        TextView title = (TextView) PopupContent.findViewById(R.id.hyper_dialog_title);
        switch (nHyperDetailPopupMode) {
            case 0:
                this.mHyperDialogPopup.setOnDismissListener(this.mHyperTextSupportDictListOnDismissListener);
                String titleText = this.mContext.getResources().getString(R.string.hyper_dlg_select_dict);
                title.setText(titleText);
                title.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                if (!Dependency.getDevice().checkFocusableModel()) {
                    this.mHyperDialogPopup.setFocusable(true);
                    break;
                }
                break;
            case 1:
                this.mHyperDialogPopup.setOnDismissListener(this.mHyperTextOnDismissListener);
                String titleText2 = this.mContext.getResources().getString(R.string.hyper_dlg_nearestword_list);
                title.setText(titleText2);
                title.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                if (!Dependency.getDevice().checkFocusableModel()) {
                    this.mHyperDialogPopup.setFocusable(true);
                    break;
                }
                break;
            case 2:
                this.mHyperDialogPopup.setOnDismissListener(this.mHyperTextOnDismissListener);
                this.detailBtn = (Button) PopupContent.findViewById(R.id.hyper_detail);
                this.detailBtn.setOnClickListener(this.mHyperDialogBtnOnClickListener);
                scrollView.setFadingEdgeLength(0);
                prepareMeanTTSLayout(PopupContent);
                this.mHyperDetailMeanController = new SearchMeanController(this.mContext, keywordView, textView, null, null, this.mEngine, false, this.mThemeModeCallback, null, this.mTTSManager.mTTSLayoutCallback);
                this.mTTSManager.setExtendTextView(textView);
                this.mHyperDetailMeanController.setMeanViewByPos(this.mHyperSimpleMeanPos, 1);
                textView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                scrollView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                if (!Dependency.getDevice().checkFocusableModel()) {
                    this.mHyperDialogPopup.setFocusable(false);
                }
                setFocusDetailBtn();
                break;
            default:
                return 0;
        }
        GridView listView = (GridView) PopupContent.findViewById(R.id.hyper_dialog_list);
        float density = GlobalOptions.density;
        int orientation = this.mContext.getResources().getConfiguration().orientation;
        switch (nHyperDetailPopupMode) {
            case 0:
                setHyperDetailSupportDictListAdapter(listView);
                if (this.mDictListItems != null && orientation == 1) {
                    int linecount = this.mDictListItems.size();
                    if (linecount > 4) {
                        linecount = 4;
                    }
                    popupHeight = (int) (40.0f * density * (linecount + 1));
                }
                if (listView.getChildCount() > 0) {
                    listView.getChildAt(0).requestFocus();
                    break;
                }
                break;
            case 1:
                setNearestListAdapter(listView);
                if (this.mWordListItems != null && orientation == 1) {
                    popupHeight = (int) (40.0f * density * (this.mWordListItems.size() + 1));
                }
                if (listView.getChildCount() > 0) {
                    listView.getChildAt(0).requestFocus();
                    break;
                }
                break;
            case 2:
                break;
            default:
                return 0;
        }
        return popupHeight;
    }

    private void setFocusDetailBtn() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() { // from class: com.diotek.diodict.mean.HyperSimpleViewModule.4
            @Override // java.lang.Runnable
            public void run() {
                if (HyperSimpleViewModule.this.detailBtn != null && HyperSimpleViewModule.this.detailBtn.getVisibility() == 0) {
                    HyperSimpleViewModule.this.detailBtn.requestFocus();
                }
            }
        }, 300L);
    }

    private void showHyperListPopup(int popupHeight) {
        Display display = this.mContext.getWindowManager().getDefaultDisplay();
        int popupY = display.getHeight() - popupHeight;
        this.mPopupWidth = this.mStartParent.getWidth() - 5;
        this.mParentsHeight = this.mStartParent.getHeight();
        this.mStartParent.setOnTouchListener(this.mParentRelativeLayoutOnTouchListener);
        if (this.mHyperDialogPopup != null) {
            if (this.mHyperDialogPopup.isShowing()) {
                this.mHyperDialogPopup.update(5, popupY, this.mPopupWidth, popupHeight);
                return;
            }
            this.mHyperDialogPopup.setWidth(this.mPopupWidth);
            this.mHyperDialogPopup.setHeight(popupHeight);
            this.mHyperDialogPopup.showAtLocation(this.mStartParent, 0, 5, popupY);
        }
    }

    private void prepareNearestListPopup() {
        this.mPopupHeight = createSimpleViewPopup(1);
        showHyperListPopup(this.mPopupHeight);
    }

    public void prepareHyperTextSummaryMeanPopup(int nHyperSimpleMeanPos) {
        this.mHyperDetailHistoryWord = this.mEngine.getResultListKeywordByPos(nHyperSimpleMeanPos, 1);
        this.mHyperDetailHistorySUID = this.mEngine.getResultListSUIDByPos(nHyperSimpleMeanPos, 1);
        this.mHyperDetailHistoryDict = this.mEngine.getResultDicTypeByPos(nHyperSimpleMeanPos, 1);
        this.mPopupHeight = createSimpleViewPopup(2);
        showHyperListPopup(this.mPopupHeight);
    }

    protected void prepareMeanTTSLayout(View view) {
        this.mTTSManager = new TTSManager(this.mContext, view, this.mStartParent);
        this.mTTSManager.prepareMeanTTSLayout();
    }

    public void closeHyperTextSummaryPopup(boolean bGotoDetail) {
        if (this.mTTSManager != null && this.mTTSManager.mTTSStopPopup != null) {
            this.mTTSManager.mTTSStopPopup.dismiss();
            this.mTTSManager.mTTSStopPopup = null;
        }
        if (this.mHyperDialogPopup != null && this.mHyperDialogPopup.isShowing()) {
            dismissHyperPopup(false);
            this.mHyperDialogPopup = null;
        }
    }
	
	private void dismissHyperPopup(boolean back) {
		CMN.debug("dismissHyperPopup");
		if (mTextView != null && mTextView.gripShowing()) {
			mTextView.clearSelection();
			if(back) return;
		}
		mHyperDialogPopup.dismiss();
	}
	
	public boolean isShowingHyperDialogPopup() {
        return this.mHyperDialogPopup != null && this.mHyperDialogPopup.isShowing();
    }

    private void setHyperDetailSupportDictListAdapter(GridView listView) {
        String[] from = {DictInfo.ListItem_Keyword};
        int[] to = {R.id.word};
        this.mDictListItems = new ArrayList<>();
        updateHyperDetailSupportDictListItems();
        GridViewAdapter dictListAdapter = new GridViewAdapter(this.mContext, this.mDictListItems, R.layout.hyper_popup_rowitem_layout, from, to, 1002);
        listView.setAdapter((ListAdapter) dictListAdapter);
        listView.setEnabled(true);
        listView.setOnItemClickListener(this.mHyperDialogPopupOnItemClickListener);
        listView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    private void setNearestListAdapter(GridView listView) {
        String[] from = {DictInfo.ListItem_Keyword};
        int[] to = {R.id.word};
        this.mWordListItems = new ArrayList<>();
        updateNearestListItems();
        GridViewAdapter mWordListAdapter = new GridViewAdapter(this.mContext, this.mWordListItems, R.layout.hyper_popup_rowitem_layout, from, to, 1001);
        listView.setAdapter((ListAdapter) mWordListAdapter);
        listView.setEnabled(true);
        listView.setOnItemClickListener(this.mNearestListPopupOnItemClickLisner);
        listView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    public boolean updateHyperDetailSupportDictListItems() {
        for (int i = 0; i < this.mEnableDictList.length; i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put(DictInfo.ListItem_Keyword, DictDBManager.getDictName(this.mEnableDictList[i].intValue()));
            item.put(DictInfo.ListItem_DictIcon, DictDBManager.getDictListIcon(this.mEnableDictList[i].intValue()));
            this.mDictListItems.add(item);
        }
        return true;
    }

    public boolean updateNearestListItems() {
        String[] hyperlist = DictUtils.makeNearestList(this.mContext);
        int maxItems = hyperlist.length;
        if (maxItems > 0) {
            this.mWordListItems.clear();
            for (String str : hyperlist) {
                HashMap<String, Object> item = new HashMap<>();
                item.put(DictInfo.ListItem_Keyword, str);
                this.mWordListItems.add(item);
            }
            return true;
        }
        return false;
    }

    public void runNearestListPopup(int nPos) {
        if (this.mHyperDialogPopup != null && this.mHyperDialogPopup.isShowing()) {
            this.mDismissPopupWithoutHyperText = false;
            dismissHyperPopup(false);
            this.mHyperDialogPopup = null;
        }
        this.mHyperSimpleMeanPos = DictUtils.getNearestWordFirstPos(this.mEngine.getResultListCount(1)) + nPos;
        prepareHyperTextSummaryMeanPopup(this.mHyperSimpleMeanPos);
    }
}
