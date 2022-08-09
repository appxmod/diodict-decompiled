package com.diotek.diodict.mean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.TagConverter;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.TabView;
import com.diotek.diodict.uitool.TextImageButton;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class BaseMeanController {
    protected static ProgressDialog mLoadingDialog = null;
    boolean mAddHistory;
    ImageView mBookmark;
    ExtendTextView mContentTextView;
    Context mContext;
    EngineManager3rd mEngine;
    private FileLinkTagViewManager mFileLinkTagViewManager;
    protected int mLoadMeaningMode;
    private MeanControllerCallback mTTSLayoutUpdateCallback;
    TabView mTabView;
    MeaningSeparator mTagConverter;
    ThemeModeCallback mThemeModeCallback;
    TextView mTitleTextView;
    int mDicType = -1;
    String mWord = null;
    int mSUID = -1;
    int mDisplayMode = 15;
    int mWordPos = -1;
    String mTableName = null;
    int mFolderId = 0;
    int mSort = 0;
    int mThemeColorMode = 0;
    protected boolean mNowLoading = false;
    protected int mRunnablePos = 0;
    protected int mRunnableHyper = 0;
    protected int mRunnableMode = 1;
    protected int mRunnableDicType = 0;
    protected String mRunnableWord = null;
    protected int mRunnableSUID = 0;
    boolean mIsDelay = true;
    boolean mIgnoreMeanTab = false;
    boolean mTabViewMeanOnly = false;
    public String mGoFragment = null;
    public MeanTitleTextSizeUpdateCallback mTitleTextFontSizeUpdateCallback = null;
    private TagConverter.LoadListener mLoadListener = new TagConverter.LoadListener() { // from class: com.diotek.diodict.mean.BaseMeanController.1
        @Override // com.diotek.diodict.mean.TagConverter.LoadListener
        public void OnLoad(String keyword, int suid, String fragment) {
            if (BaseMeanController.this.mFileLinkTagViewManager != null && BaseMeanController.this.mContext != null) {
                SearchMeanController popMeanView = BaseMeanController.this.mFileLinkTagViewManager.prepareLinkTextMeanPopup(BaseMeanController.this.mContext, EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(BaseMeanController.this.mDicType, false), keyword, suid);
                popMeanView.mGoFragment = fragment;
            }
        }
    };
    private View.OnTouchListener mTitleTextOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.mean.BaseMeanController.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View arg0, MotionEvent arg1) {
            return true;
        }
    };
    ExtendTextView.TagConverterCallback mTagConverterCallback = new ExtendTextView.TagConverterCallback() { // from class: com.diotek.diodict.mean.BaseMeanController.3
        @Override // com.diotek.diodict.mean.ExtendTextView.TagConverterCallback
        public ArrayList<TagConverter.DictPos> convert_Partial_Index(int dspMode, int start, int end) {
            if (BaseMeanController.this.mTagConverter == null) {
                return null;
            }
            return BaseMeanController.this.mTagConverter.convert_Partial_Index(dspMode, start, end);
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.TagConverterCallback
        public ArrayList<TagConverter.DictPos> convert_Total_Index(int dspMode, int start, int end) {
            if (BaseMeanController.this.mTagConverter == null) {
                return null;
            }
            return BaseMeanController.this.mTagConverter.convert_Total_Index(dspMode, start, end);
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.TagConverterCallback
        public TagConverter.DictPos isLinkOffset(int off) {
            if (BaseMeanController.this.mTagConverter == null) {
                return null;
            }
            return BaseMeanController.this.mTagConverter.isLinkOffset(off);
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.TagConverterCallback
        public boolean isFirstBlock() {
            return BaseMeanController.this.mTagConverter.isFirstBlock();
        }
    };
    public Handler mHandler = new Handler();
    private ExtendTextView.ExtendTextCallback mConfigChangeCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.mean.BaseMeanController.4
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            BaseMeanController.this.mHandler.post(BaseMeanController.this.runConfigChange);
            return true;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            BaseMeanController.this.mHandler.post(BaseMeanController.this.runConfigChange);
            return true;
        }
    };
    private Runnable runConfigChange = new Runnable() { // from class: com.diotek.diodict.mean.BaseMeanController.5
        @Override // java.lang.Runnable
        public void run() {
            BaseMeanController.this.ConfigChange();
        }
    };
    private ExtendTextView.ExtendTextCallback mRunWikiPediaSearchTextCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.mean.BaseMeanController.6
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            return true;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            if (BaseMeanController.this.mContext != null) {
                CommonUtils.startWebSearch(BaseMeanController.this.mContext, str, 1);
            }
            return true;
        }
    };
    private ExtendTextView.ExtendTextCallback mRunGoogleSearchTextCallback = new ExtendTextView.ExtendTextCallback() { // from class: com.diotek.diodict.mean.BaseMeanController.7
        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run() {
            return true;
        }

        @Override // com.diotek.diodict.mean.ExtendTextView.ExtendTextCallback
        public boolean run(String str) {
            if (BaseMeanController.this.mContext != null) {
                CommonUtils.startWebSearch(BaseMeanController.this.mContext, str, 0);
                return true;
            }
            return true;
        }
    };
    DialogInterface.OnDismissListener mLoadingDialogOnDismissLisntener = new DialogInterface.OnDismissListener() { // from class: com.diotek.diodict.mean.BaseMeanController.8
        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface arg0) {
            BaseMeanController.mLoadingDialog = null;
        }
    };
    public final int LOADINGHANDLER_DELAY = 200;
    public final int LOADINGHANDLER_CREATEDIALOG = 0;
    public final int LOADINGHANDLER_PREPAREMEANCONTROLLER = 1;
    public final int LOADINGHANDLER_INITIALIZETHEME = 2;
    public final int LOADINGHANDLER_PREPAREALLVIEW = 3;
    public final int LOADINGHANDLER_REFRESHALLVIEW = 4;
    public final int LOADINGHANDLER_PREPARETITLEVIEW = 5;
    public final int LOADINGHANDLER_REFRESHTITLEVIEW = 6;
    public final int LOADINGHANDLER_UPDATEDISPLAYMODE = 7;
    Handler mLoadingHandler = new Handler() { // from class: com.diotek.diodict.mean.BaseMeanController.9
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    BaseMeanController.this.createLoadingDialog();
                    return;
                case 1:
                case 5:
                case 6:
                default:
                    return;
                case 2:
                    BaseMeanController.this.setTheme(BaseMeanController.this.mThemeModeCallback.getThemeMode());
                    BaseMeanController.this.mLoadingHandler.sendEmptyMessage(3);
                    return;
                case 3:
                    new Thread(new Runnable() { // from class: com.diotek.diodict.mean.BaseMeanController.9.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (BaseMeanController.this.mTabViewMeanOnly) {
                                BaseMeanController.this.mExistContentMean = BaseMeanController.this.loadMeaningWithMode(BaseMeanController.this.mDisplayMode, BaseMeanController.this.mLoadMeaningMode);
                                if (!BaseMeanController.this.mExistContentMean) {
                                    BaseMeanController.this.mExistContentMean = BaseMeanController.this.loadMeaning(false, BaseMeanController.this.mLoadMeaningMode);
                                }
                            } else {
                                BaseMeanController.this.mExistContentMean = BaseMeanController.this.loadMeaning(false, BaseMeanController.this.mLoadMeaningMode);
                                if (BaseMeanController.this.mDisplayMode != 15) {
                                    BaseMeanController.this.mLoadingHandler.sendEmptyMessage(7);
                                    return;
                                }
                            }
                            BaseMeanController.this.mLoadingHandler.sendEmptyMessage(4);
                        }
                    }).start();
                    return;
                case 4:
                    if (BaseMeanController.this.mIgnoreMeanTab) {
                        BaseMeanController.this.loadTitle(true);
                    }
                    BaseMeanController.this.setAllView(false);
                    BaseMeanController.this.mLoadingHandler.removeMessages(0);
                    if (BaseMeanController.mLoadingDialog != null && BaseMeanController.mLoadingDialog.isShowing()) {
                        BaseMeanController.mLoadingDialog.dismiss();
                        BaseMeanController.mLoadingDialog = null;
                    }
                    BaseMeanController.this.mNowLoading = false;
                    return;
                case 7:
                    new Thread(new Runnable() { // from class: com.diotek.diodict.mean.BaseMeanController.9.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (BaseMeanController.this.mTagConverter != null) {
                                BaseMeanController.this.mExistContentMean = BaseMeanController.this.mTagConverter.updateDispalyMode(BaseMeanController.this.mDisplayMode);
                            }
                            BaseMeanController.this.mLoadingHandler.sendEmptyMessage(4);
                        }
                    }).start();
                    return;
            }
        }
    };
    boolean mExistContentMean = false;
    private Runnable mGoFragmentOffset = new Runnable() { // from class: com.diotek.diodict.mean.BaseMeanController.10
        @Override // java.lang.Runnable
        public void run() {
            int goFragmentOffset = BaseMeanController.this.mTagConverter.getFragmentOffset(BaseMeanController.this.mGoFragment);
            BaseMeanController.this.mContentTextView.getLinePositionY(goFragmentOffset - 1);
            BaseMeanController.this.mGoFragment = null;
        }
    };

    /* loaded from: classes.dex */
    public interface MeanControllerCallback {
        boolean run();
    }

    /* loaded from: classes.dex */
    public interface MeanTitleTextSizeUpdateCallback {
        void setFontSize();
    }

    /* loaded from: classes.dex */
    public interface ThemeModeCallback {
        int getThemeMode();
    }

    public abstract int setMeanView(String str, String str2, int i, int i2, int i3, boolean z);

    public abstract void setMeanView(int i);

    public abstract int setMeanViewByPos(int i, int i2);

    public abstract int setMeanViewKeywordInfo(int i, String str, int i2, int i3);

    public abstract int setTitleView(String str, int i, int i2, int i3);

    public abstract int setTitleViewByPos(int i, int i2);

    public BaseMeanController(Context context, TextView titleTextView, ExtendTextView contentTextView, ImageView bookmark, TabView tabView, EngineManager3rd engine, boolean addHistory, ThemeModeCallback themeModeCallback, FileLinkTagViewManager fileLinkTagViewManager, MeanControllerCallback ttsLayoutCallback, int loadingMode) {
        this.mContext = null;
        this.mTitleTextView = null;
        this.mContentTextView = null;
        this.mBookmark = null;
        this.mTabView = null;
        this.mEngine = null;
        this.mTagConverter = null;
        this.mAddHistory = false;
        this.mLoadMeaningMode = 0;
        this.mTTSLayoutUpdateCallback = null;
        this.mThemeModeCallback = null;
        if (context != null && contentTextView != null && engine != null) {
            this.mContext = context;
            this.mTitleTextView = titleTextView;
            if (this.mTitleTextView != null) {
                this.mTitleTextView.setTypeface(DictUtils.createfont());
                this.mTitleTextView.setOnTouchListener(this.mTitleTextOnTouchListener);
            }
            this.mContentTextView = contentTextView;
            this.mBookmark = bookmark;
            this.mTabView = tabView;
            this.mEngine = engine;
            this.mAddHistory = addHistory;
            this.mThemeModeCallback = themeModeCallback;
            this.mContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            this.mFileLinkTagViewManager = fileLinkTagViewManager;
            this.mTTSLayoutUpdateCallback = ttsLayoutCallback;
            if (fileLinkTagViewManager == null) {
                this.mTagConverter = new MeaningSeparator(this.mContext, this.mEngine, this.mThemeModeCallback.getThemeMode(), null);
            } else {
                this.mTagConverter = new MeaningSeparator(this.mContext, this.mEngine, this.mThemeModeCallback.getThemeMode(), this.mLoadListener);
            }
            this.mContentTextView.setTagConverter(this.mTagConverterCallback);
            this.mContentTextView.getScrollView().setMeanController(this);
            this.mTagConverter.setUseSeparateFunction(this.mTabView != null);
            this.mLoadMeaningMode = loadingMode;
        }
    }

    public void setViews(ImageView bookmark, TabView tabView) {
        this.mBookmark = bookmark;
        this.mTabView = tabView;
    }

    public void refreshView() {
        setTabViewEnable(true);
        this.mLoadingHandler.sendEmptyMessageDelayed(0, 200L);
        this.mLoadingHandler.sendEmptyMessage(2);
    }

    public void refreshViewNoDelay() {
        this.mLoadingHandler.sendEmptyMessage(0);
        this.mLoadingHandler.sendEmptyMessage(2);
    }

    public void refreshEmptyView() {
        removeMessage();
        this.mNowLoading = false;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setText("");
        }
        if (this.mContentTextView != null) {
            this.mContentTextView.setParentScrollTo(0, 0);
            this.mContentTextView.setText("");
        }
        if (this.mBookmark != null) {
            this.mBookmark.setVisibility(View.INVISIBLE);
        }
        setTabViewEnable(false);
    }

    public void refreshSettingView() {
        setTheme(this.mThemeModeCallback.getThemeMode());
        this.mExistContentMean = loadMeaning(true, 0);
        setAllView(true);
    }

    public void refreshContentView() {
        if (this.mContentTextView != null) {
            this.mLoadingHandler.sendEmptyMessageDelayed(0, 200L);
            this.mLoadingHandler.sendEmptyMessage(7);
            this.mContentTextView.setParentScrollTo(0, 0);
        }
    }

    public boolean refreshBookmark() {
        if (this.mBookmark == null || this.mContext == null) {
            return false;
        }
        if (DioDictDatabase.existWordbookItem(this.mContext, this.mDicType, this.mWord, this.mSUID, -1)) {
            this.mBookmark.setVisibility(View.VISIBLE);
            return true;
        }
        this.mBookmark.setVisibility(View.INVISIBLE);
        return false;
    }

    public void setBookmarkImage(ImageView bookmark) {
        this.mBookmark = bookmark;
    }

    public void setDisplayMode(int nMode) {
        if (nMode == 0) {
            this.mDisplayMode = 15;
        } else {
            this.mDisplayMode = 1 << (nMode - 1);
        }
        if (this.mContentTextView != null) {
            this.mContentTextView.setDisplayMode(this.mDisplayMode);
        }
    }

    public String getWord() {
        return this.mWord;
    }

    public int getSuid() {
        return this.mSUID;
    }

    public int getDicType() {
        return this.mDicType;
    }

    public int getWordPos() {
        return this.mWordPos;
    }

    public void changeDicType(int nDicType) {
        this.mDicType = nDicType;
    }

    public void setMeanContentTextViewTextSize(int size, boolean isStoreCurrentTopOffset) {
        if (this.mContentTextView.getTextSize() != size) {
            this.mContentTextView.setTextSize(size, isStoreCurrentTopOffset);
        }
    }

    public void setMeanContentTextViewTextSize(int size) {
        this.mContentTextView.setTextSize(size, true);
    }

    public void setMeanContentTextViewTextColor(int color) {
        this.mContentTextView.setTextColor(color);
    }

    public void setMeanContentTextViewCallback(ExtendTextView.ExtendTextCallback hyper, ExtendTextView.ExtendTextCallback updateLayoutCallback, boolean bConfigChange, ExtendTextView.AfterSetMeanViewCallback finishDrawMeanCallback) {
        ExtendTextView.ExtendTextCallback tmpConfigChangeCallback = null;
        if (bConfigChange) {
            tmpConfigChangeCallback = this.mConfigChangeCallback;
        }
        this.mContentTextView.setCallback(hyper, this.mRunGoogleSearchTextCallback, this.mRunWikiPediaSearchTextCallback, null, tmpConfigChangeCallback, updateLayoutCallback, finishDrawMeanCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ConfigChange() {
        if (this.mContentTextView != null) {
            this.mContentTextView.setParentScrollTo(0, this.mContentTextView.getRestoreScrollY());
        }
    }

    public boolean isAvailable_Mean() {
        return (this.mTagConverter.getAvailableDispMode() & 1) > 0;
    }

    public boolean isAvailable_Idiom() {
        return (this.mTagConverter.getAvailableDispMode() & 2) > 0;
    }

    public boolean isAvailable_Example() {
        return (this.mTagConverter.getAvailableDispMode() & 4) > 0;
    }

    public void setMeanContentTextViewMarkerColor(int color) {
        if (this.mContentTextView != null) {
            this.mContentTextView.setMarkerColor(color);
        }
    }

    public void setTabViewEnable(boolean bEnable) {
        if (this.mTabView != null) {
            TextImageButton tmpImageBtn = this.mTabView.getButton(0);
            if (tmpImageBtn != null) {
                tmpImageBtn.setEnabled(bEnable);
            }
            TextImageButton tmpImageBtn2 = this.mTabView.getButton(1);
            if (tmpImageBtn2 != null) {
                tmpImageBtn2.setEnabled(bEnable);
            }
            TextImageButton tmpImageBtn3 = this.mTabView.getButton(2);
            if (tmpImageBtn3 != null) {
                tmpImageBtn3.setEnabled(bEnable);
            }
            TextImageButton tmpImageBtn4 = this.mTabView.getButton(3);
            if (tmpImageBtn4 != null) {
                tmpImageBtn4.setEnabled(bEnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createLoadingDialog() {
        if (mLoadingDialog == null && this.mContext != null) {
            mLoadingDialog = new ProgressDialog(this.mContext);
            mLoadingDialog.setTitle(this.mContext.getResources().getString(R.string.app_name));
            mLoadingDialog.setIndeterminate(true);
            mLoadingDialog.setMessage(this.mContext.getString(R.string.loadingmeanview_str));
            mLoadingDialog.setProgress(0);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setOnDismissListener(this.mLoadingDialogOnDismissLisntener);
            if (!((Activity) this.mContext).isFinishing()) {
                mLoadingDialog.show();
            } else {
                MSG.l(2, "++++++++++ mContext is finishing!!");
            }
        }
    }

    public boolean loadMeaning(boolean bSource, int nMode) {
        if (this.mContext == null) {
            return false;
        }
        if (bSource) {
            return this.mTagConverter.loadMeaningBySource(this.mDicType, this.mContext.getResources().getString(R.string.sample_meanning));
        }
        if (this.mContentTextView != null) {
            this.mContentTextView.setCurWordInfo(this.mDicType, this.mWord, this.mSUID);
        }
        return this.mTagConverter.loadMeaning(this.mDicType, this.mWord, this.mSUID, nMode);
    }

    public boolean loadMeaningWithMode(int dispMode, int nMode) {
        if (this.mContext == null) {
            return false;
        }
        if (this.mContentTextView != null) {
            this.mContentTextView.setCurWordInfo(this.mDicType, this.mWord, this.mSUID);
        }
        return this.mTagConverter.loadMeaningWithMode(this.mDicType, this.mWord, this.mSUID, dispMode);
    }

    public boolean loadTitle(boolean bAllKeyword) {
        int dispMode = 16;
        if (bAllKeyword) {
            dispMode = 112;
        }
        if (this.mContentTextView != null) {
            this.mContentTextView.setCurWordInfo(this.mDicType, this.mWord, this.mSUID);
        }
        if (this.mTagConverter == null) {
            return false;
        }
        return this.mTagConverter.loadMeaningWithMode(this.mDicType, this.mWord, this.mSUID, dispMode);
    }

    public void loadMarker() {
        if (this.mContentTextView.length() > 0 && this.mContext != null) {
            ArrayList<MarkerObject> markerObj = (ArrayList) DioDictDatabase.existMarker(this.mContext, this.mDicType, this.mSUID);
            this.mContentTextView.setMakerObject(markerObj);
        }
    }

    public void addHistory() {
        if (this.mAddHistory && this.mContext != null && DioDictDatabase.existHistoryItem(this.mContext, this.mDicType, this.mWord, this.mSUID) == -1) {
            DioDictDatabase.addHistory(this.mContext, this.mDicType, this.mWord, this.mSUID);
        }
    }

    public void setAllView(boolean bSetting) {
        boolean isInitScroll = true;
        setTitleView();
        CharSequence charSequence = "";
        if (this.mContentTextView != null && this.mTagConverter != null) {
            ExtendTextView extendTextView = this.mContentTextView;
            if (this.mExistContentMean) {
                charSequence = this.mTagConverter.getMeanFieldSpan();
            }
            extendTextView.setText(charSequence);
            if (this.mGoFragment != null) {
                isInitScroll = false;
                this.mHandler.post(this.mGoFragmentOffset);
            }
        }
        if (!bSetting) {
            if (isInitScroll && this.mContentTextView != null) {
                this.mContentTextView.setParentScrollTo(0, 0);
            }
            loadMarker();
            addHistory();
            refreshBookmark();
            refreshTabView();
        }
        if (this.mTTSLayoutUpdateCallback != null) {
            this.mTTSLayoutUpdateCallback.run();
        }
    }

    public boolean isRemainMeaning() {
        return this.mTagConverter != null && !this.mTagConverter.isLastBlock();
    }

    public boolean appendNextMeaning() {
        Spanned spannedNext;
        if (this.mContentTextView == null || this.mTagConverter == null || (spannedNext = this.mTagConverter.getNextMeanFieldSpan()) == null || !this.mExistContentMean) {
            return false;
        }
        this.mContentTextView.append(spannedNext);
        return true;
    }

    public void setTitleView() {
        if (this.mTitleTextView != null) {
            if (this.mContext != null) {
                this.mTitleTextView.setTypeface(DictUtils.createfont());
            }
            this.mTitleTextView.setText(this.mTagConverter.getKeyFieldSpan());
            if (this.mTitleTextFontSizeUpdateCallback != null) {
                this.mTitleTextFontSizeUpdateCallback.setFontSize();
            }
        }
    }

    public TextView getTitleView() {
        return this.mTitleTextView;
    }

    public void setViews(TextView titleTextView, ExtendTextView contentTextView) {
        this.mTitleTextView = titleTextView;
        this.mContentTextView = contentTextView;
        if (this.mContentTextView != null) {
            this.mContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            this.mContentTextView.getScrollView().setMeanController(this);
            if (this.mTagConverterCallback != null) {
                this.mContentTextView.setTagConverter(this.mTagConverterCallback);
            }
        }
    }

    public void refreshTabView() {
        if (this.mTabView != null) {
            LinearLayout tmpBtnLayout = this.mTabView.getButtonLayout(1);
            if (tmpBtnLayout != null) {
                if (isAvailable_Mean()) {
                    tmpBtnLayout.setVisibility(View.VISIBLE);
                } else {
                    tmpBtnLayout.setVisibility(View.GONE);
                }
            }
            LinearLayout tmpBtnLayout2 = this.mTabView.getButtonLayout(2);
            if (tmpBtnLayout2 != null) {
                if (isAvailable_Idiom()) {
                    tmpBtnLayout2.setVisibility(View.VISIBLE);
                } else {
                    tmpBtnLayout2.setVisibility(View.GONE);
                }
            }
            LinearLayout tmpBtnLayout3 = this.mTabView.getButtonLayout(3);
            if (tmpBtnLayout3 != null) {
                if (isAvailable_Example()) {
                    tmpBtnLayout3.setVisibility(View.VISIBLE);
                } else {
                    tmpBtnLayout3.setVisibility(View.GONE);
                }
            }
            TextImageButton AllBtn = this.mTabView.getButton(0);
            if (AllBtn != null) {
                this.mTabView.refreshTabViewPosition();
            }
        }
    }

    public void setTheme(int nTheme) {
        int nMeanColor = this.mTagConverter.setTheme(nTheme);
        this.mContentTextView.setTextColor(nMeanColor);
    }

    public int getTheme() {
        return this.mTagConverter.getTheme();
    }

    public boolean getNowLoading() {
        return this.mNowLoading;
    }

    public void removeMessage() {
        if (this.mLoadingHandler != null) {
            this.mLoadingHandler.removeMessages(0);
            this.mLoadingHandler.removeMessages(1);
            this.mLoadingHandler.removeMessages(2);
            this.mLoadingHandler.removeMessages(3);
            this.mLoadingHandler.removeMessages(4);
            this.mLoadingHandler.removeMessages(5);
            this.mLoadingHandler.removeMessages(6);
            this.mLoadingHandler.removeMessages(7);
        }
    }

    public void dismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void onDestory() {
        removeMessage();
        dismissDialog();
        if (this.mTagConverter != null) {
            this.mTagConverter.destroy();
        }
        this.mContext = null;
        this.mTitleTextView = null;
        this.mBookmark = null;
        this.mTabView = null;
        this.mEngine = null;
        this.mTagConverter = null;
        this.mFileLinkTagViewManager = null;
        this.mTTSLayoutUpdateCallback = null;
        this.mTitleTextFontSizeUpdateCallback = null;
        this.mThemeModeCallback = null;
    }
}
