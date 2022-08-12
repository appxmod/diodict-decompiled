package com.diotek.diodict;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.diotek.diodict.anim.LayoutTransition;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.ExtendScrollView;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.TabView;
import com.diodict.decompiled.R;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.utils.GlobalOptions;

import java.util.Locale;

/* loaded from: classes.dex */
public class ListMeanViewActivity extends BaseActivity {
    public static final int LAYOUTMODE_SMALL_MEANVIEW = 0;
    public static final int LAYOUTMODE_WIDE_MEANVIEW = 1;
    protected int mOrientation;
    BaseMeanController mBaseMeanController = null;
    protected ExtendScrollView mMainMeanScrollView = null;
    protected Button mUSOnceBtn = null;
    protected ImageButton mUSRepeatBtn = null;
    protected Button mUKOnceBtn = null;
    protected ImageButton mUKRepeatBtn = null;
    protected ImageView mUSTextView = null;
    protected ImageView mUKTextView = null;
    protected LinearLayout mMeanTTSLayout = null;
    protected LinearLayout mStandardInnerLeftLayout = null;
    protected LinearLayout mMainRightLayout = null;
    protected TextView mMainMeanTitleTextView = null;
    protected TabView mMeanTabView = null;
    protected RelativeLayout mMeanTabLayout = null;
    protected ImageView mMainMeanBookmarkImageView = null;
    protected ImageView mBookmarkImageViewNormal = null;
    protected ImageView mBookmarkImageViewLandExtension = null;
    protected View mEmptyViewTitleTop = null;
    protected LinearLayout mMeanToolbarLayout = null;
    protected LinearLayout mListLayout = null;
    protected RelativeLayout mListMeanLayout = null;
    protected Dialog mWordbookDialog = null;
    protected EditText mEdittextWordbookName = null;
    protected TextView mInputWordbookNameTextView = null;
    protected View mPortMeanTabView = null;
    protected View mLandMeanTabView = null;
    protected View mPortCopyToFlashcardLayout = null;
    protected View mLandCopyToFlashcardLayout = null;
    protected String mBackupCardName = null;
    protected RelativeLayout.LayoutParams mBookmarkLayoutParams = null;
    protected int mLayoutMode = 0;
    LayoutTransition.AnimationCallback mAnimationStartCallback = new LayoutTransition.AnimationCallback() { // from class: com.diotek.diodict.ListMeanViewActivity.1
        @Override // com.diotek.diodict.anim.LayoutTransition.AnimationCallback
        public void run(boolean bExtension) {
            if (bExtension) {
                ListMeanViewActivity.this.onMeanViewExtensionStart();
            } else {
                ListMeanViewActivity.this.onMeanViewReductionStart();
            }
        }
    };
    LayoutTransition.AnimationCallback mAnimationEndCallback = new LayoutTransition.AnimationCallback() { // from class: com.diotek.diodict.ListMeanViewActivity.2
        @Override // com.diotek.diodict.anim.LayoutTransition.AnimationCallback
        public void run(boolean bExtension) {
            if (bExtension) {
                ListMeanViewActivity.this.onMeanViewExtensionEnd();
            } else {
                ListMeanViewActivity.this.onMeanViewReductionEnd();
            }
        }
    };
    View.OnClickListener mEditClearBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.ListMeanViewActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (ListMeanViewActivity.this.mInputWordbookNameTextView != null) {
                if (ListMeanViewActivity.this.mEdittextWordbookName != null) {
                    ListMeanViewActivity.this.mEdittextWordbookName.setText("");
                }
                ListMeanViewActivity.this.mInputWordbookNameTextView.setVisibility(View.VISIBLE);
            }
        }
    };
    TextWatcher mWordbookEditWatcher = new TextWatcher() { // from class: com.diotek.diodict.ListMeanViewActivity.4
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            ListMeanViewActivity.this.hideEditMessage();
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            ListMeanViewActivity.this.mBackupCardName = s.toString();
            if (ListMeanViewActivity.this.mBackupCardName.length() == 0 && ListMeanViewActivity.this.mInputWordbookNameTextView != null) {
                ListMeanViewActivity.this.mInputWordbookNameTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    View.OnFocusChangeListener mMainMeanContentTextViewOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.ListMeanViewActivity.5
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            ImageView meanSeparator = (ImageView) ListMeanViewActivity.this.findViewById(R.id.separator);
            if (meanSeparator != null) {
                if (hasFocus) {
                    meanSeparator.setVisibility(View.VISIBLE);
                } else {
                    meanSeparator.setVisibility(View.GONE);
                }
            }
        }
    };
    View.OnFocusChangeListener mCardOnFocusChangedListner = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.ListMeanViewActivity.6
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean bFocus) {
            if (bFocus) {
                ((RadioButton) view).setChecked(true);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity
    public boolean onCreateActivity(Bundle savedInstanceState) {
        this.mLayoutMode = 0;
        if (Dependency.isContainTTS()) {
            setVolumeControlStream(3);
        }
        this.mOrientation = getResources().getConfiguration().orientation;
        return super.onCreateActivity(savedInstanceState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onPause() {
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onResume() {
        if (this.mTextView != null) {
            this.mTextView.refreshMarkerObject();
        }
        super.onResume();
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mOrientation = newConfig.orientation;
        if (this.mTextView.gripShowing()) {
            this.mTextView.clearSelection();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.mStandardInnerLeftLayout.getVisibility() == View.GONE) {
                    LayoutTransition.updateLayoutWithExtends(false, this.mStandardInnerLeftLayout, this.mMainRightLayout, this.mAnimationStartCallback, this.mAnimationEndCallback, getApplicationContext());
                    return true;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        LayoutTransition.onDestroy();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareMeanContentLayout(boolean isEnableBookmark) {
        this.mListMeanLayout = (RelativeLayout) findViewById(R.id.SearchContentStandardLayout);
        this.mListLayout = (LinearLayout) findViewById(R.id.SearchContentStandardLeftLayout);
        this.mEmptyViewTitleTop = findViewById(R.id.EmptyViewTitleTop);
        if (isEnableBookmark) {
            this.mBookmarkImageViewNormal = (ImageView) findViewById(R.id.bookmark);
            this.mBookmarkImageViewLandExtension = (ImageView) findViewById(R.id.bookmark_wide_meanview);
            this.mMainMeanBookmarkImageView = this.mBookmarkImageViewNormal;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareMeanContentLayout_byConfiguration(int configuration) {
        RelativeLayout wrap_layout = (RelativeLayout) findViewById(R.id.copyToFlashcardWrapperLayout);
        LayoutInflater inflater = getLayoutInflater();
        if (wrap_layout != null) {
            wrap_layout.removeAllViews();
        }
        if (configuration == 1) {
            if (wrap_layout != null) {
                if (this.mPortCopyToFlashcardLayout == null) {
                    this.mPortCopyToFlashcardLayout = inflater.inflate(R.layout.save_word_popup_right_layout, (ViewGroup) null);
                }
                wrap_layout.addView(this.mPortCopyToFlashcardLayout);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
            params.addRule(9);
            params.addRule(0, R.id.MeanContentRightLayout);
            params.addRule(3, R.id.EmptyViewTitleTop);
            this.mMainMeanTitleTextView.setLayoutParams(params);
            this.mMainMeanTitleTextView.setMinHeight(getResources().getDimensionPixelSize(R.dimen.title_textview_minheight));
            if (this.mBookmarkImageViewNormal != null) {
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(-2, -2);
                params2.addRule(11);
                params2.addRule(3, R.id.EmptyViewTitleTop);
                params2.setMargins(0, 0, (int) (getResources().getDisplayMetrics().density * 2.6d), 0);
                this.mBookmarkImageViewNormal.setLayoutParams(params2);
            }
        } else if (configuration == 2) {
            if (wrap_layout != null) {
                if (this.mLandCopyToFlashcardLayout == null) {
                    this.mLandCopyToFlashcardLayout = inflater.inflate(R.layout.save_word_popup_right_layout, (ViewGroup) null);
                }
                wrap_layout.addView(this.mLandCopyToFlashcardLayout);
            }
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(-2, -2);
            params3.addRule(10);
            params3.addRule(9);
            params3.addRule(0, R.id.MeanToolbarLayout);
            this.mMainMeanTitleTextView.setLayoutParams(params3);
            this.mMainMeanTitleTextView.setMinHeight(getResources().getDimensionPixelSize(R.dimen.title_textview_minheight));
            if (this.mBookmarkImageViewNormal != null) {
                RelativeLayout.LayoutParams params22 = new RelativeLayout.LayoutParams(-2, -2);
                params22.addRule(10);
                params22.addRule(11);
                params22.setMargins(0, 0, (int) (getResources().getDisplayMetrics().density * 2.6d), 0);
                this.mBookmarkImageViewNormal.setLayoutParams(params22);
            }
        }
        RelativeLayout meantab_paddingTop = (RelativeLayout) findViewById(R.id.MeanTabViewTopPadding);
        if (meantab_paddingTop != null) {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.tabview_toppadding_width), getResources().getDimensionPixelSize(R.dimen.tabview_toppadding_height));
            meantab_paddingTop.setLayoutParams(p);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void prepareMeanTabView() {
        this.mMeanTabLayout = (RelativeLayout) findViewById(R.id.MeanContentRightLayout);
        this.mMeanTabView = (TabView) findViewById(R.id.MeanTabView);
        this.mMeanTabView.getButton(0).setBackgroundResource(R.drawable.tab);
        this.mMeanTabView.getButton(1).setBackgroundResource(R.drawable.tab);
        this.mMeanTabView.getButton(2).setBackgroundResource(R.drawable.tab);
        this.mMeanTabView.getButton(3).setBackgroundResource(R.drawable.tab);
        this.mMeanTabView.setFocusable(false);
        this.mMeanTabView.getButton(0).setFocusable(true);
        this.mMeanTabView.getButton(1).setFocusable(true);
        this.mMeanTabView.getButton(2).setFocusable(true);
        this.mMeanTabView.getButton(3).setFocusable(true);
        this.mMeanTabView.getButton(0).setText(getResources().getString(R.string.All));
        this.mMeanTabView.getButton(1).setText(getResources().getString(R.string.Mean));
        this.mMeanTabView.getButton(2).setText(getResources().getString(R.string.Phrases));
        this.mMeanTabView.getButton(3).setText(getResources().getString(R.string.Example));
        this.mMeanTabView.getButton(0).setSelected(true);
    }

    protected void onMeanViewExtensionEnd() {
        if (this.mLayoutMode == 1) {
            this.mStandardInnerLeftLayout.setVisibility(View.GONE);
            this.mTextView.setEnableTextSelect(true);
            Animation ani = this.mMainRightLayout.getAnimation();
            if (ani != null) {
                ani.setAnimationListener(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onMeanViewExtensionStart() {
        setWideMeanView(false);
    }

    protected void onMeanViewReductionEnd() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onMeanViewReductionStart() {
        setSmallMeanView();
        Animation ani = this.mStandardInnerLeftLayout.getAnimation();
        if (ani != null) {
            ani.setAnimationListener(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSmallMeanView() {
        CommonUtils.stopTTS();
        this.mLayoutMode = 0;
        this.mTextView.setEnableTextSelect(false);
        this.mListLayout.setVisibility(View.VISIBLE);
        if (this.mMeanToolbarLayout != null) {
            this.mMeanToolbarLayout.setVisibility(View.GONE);
        }
        showHideTTSLayout(false);
        this.mStandardInnerLeftLayout.setVisibility(View.VISIBLE);
        this.mMeanTabLayout.setVisibility(View.GONE);
        showHideEmptyViewTitleTop(false);
        this.mMainRightLayout.setBackgroundResource(R.drawable.search_content_right_bg2);
        ((LinearLayout.LayoutParams) this.mStandardInnerLeftLayout.getLayoutParams()).weight = LayoutTransition.getMinMeaningWeight(getApplicationContext());
        if (this.mMainMeanBookmarkImageView != null) {
            this.mMainMeanBookmarkImageView = this.mBookmarkImageViewNormal;
            if (this.mBookmarkImageViewLandExtension.getVisibility() == View.VISIBLE) {
                this.mBookmarkImageViewLandExtension.setVisibility(View.GONE);
                this.mMainMeanBookmarkImageView.setVisibility(View.VISIBLE);
            }
            this.mBaseMeanController.setBookmarkImage(this.mMainMeanBookmarkImageView);
        }
        if (this.mTextView != null && this.mTextView.gripShowing()) {
            this.mTextView.clearSelection();
            this.mTextView.forceInvalidate();
        }
        setImageBookMark(false);
        this.mListMeanLayout.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setWideMeanView(boolean isDirect) {
        this.mLayoutMode = 1;
        if (isDirect) {
            this.mStandardInnerLeftLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mStandardInnerLeftLayout.getLayoutParams();
            params.weight = 0.0f;
            this.mTextView.setEnableTextSelect(true);
        }
        checkSymbolKeyword();
        this.mListLayout.setVisibility(View.GONE);
        if (this.mMeanToolbarLayout != null) {
            this.mMeanToolbarLayout.setVisibility(View.VISIBLE);
        }
        showHideTTSLayout(true);
        this.mMeanTabLayout.setVisibility(View.VISIBLE);
        showHideEmptyViewTitleTop(true);
        this.mMainRightLayout.setBackgroundResource(R.drawable.search_content_right_bg);
        if (this.mMainMeanBookmarkImageView != null) {
            if (this.mMainMeanBookmarkImageView.getVisibility() == View.VISIBLE) {
                this.mBookmarkImageViewNormal.setVisibility(View.GONE);
                this.mBookmarkImageViewLandExtension.setVisibility(View.VISIBLE);
            }
            this.mMainMeanBookmarkImageView = this.mBookmarkImageViewLandExtension;
            this.mBaseMeanController.setBookmarkImage(this.mMainMeanBookmarkImageView);
        }
        setImageBookMark(true);
        this.mListMeanLayout.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void selectTabAll() {
        if (this.mMeanTabView != null) {
            this.mMeanTabView.setBtnSelected(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isSmallMeanView() {
        return this.mLayoutMode == 0;
    }

    public void showHideTTSLayout(boolean bShow) {
		bShow = true;
        int nTTSFisrtBtnResId;
        int nTTSSecondBtnResId;
        Integer[] supportTTS;
        int nLang = 0;
        int nDicType = this.mEngine.getCurDict();
        if (nDicType == 65520) {
            nDicType = getPlayTTSDbType();
        }
        if (DictDBManager.getCpENGDictionary(nDicType)) {
            nLang = 65536;
        } else if (DictDBManager.getCpCHNDictionary(nDicType)) {
            nLang = EngineInfo3rd.TTS_CHINESE;
        } else if (DictDBManager.getCpJPNDictionary(nDicType)) {
            nLang = EngineInfo3rd.TTS_JAPANESE;
        } else if (DictDBManager.getCpKORDictionary(nDicType)) {
            nLang = EngineInfo3rd.TTS_KOREAN;
        }
        boolean bExist = false;
        int nVisibility = View.GONE;
        int nTTSFisrtRepeatBtnResId = R.drawable.repeat;
        int nTTSSecondRepeatBtnResId = R.drawable.repeat;
        String nTTSFirstStr = "";
        String nTTSSecondStr = "";
        float nDensity = GlobalOptions.density;
        float nTTSTextSize = 10.0f;
        int nTTSLeftPadding = getResources().getDimensionPixelSize(R.dimen.mean_tts_btn_paddingLeft);
        if (nTTSLeftPadding <= 0) {
            nTTSLeftPadding = (int) (8.0f * nDensity);
        }
        int nTTSRightPadding = getResources().getDimensionPixelSize(R.dimen.mean_tts_btn_paddingRight);
        if (nTTSRightPadding <= 0) {
            nTTSLeftPadding = (int) (25.0f * nDensity);
        }
        int nUSBtnLeftPadding = nTTSLeftPadding;
        int nUKBtnLeftPadding = nTTSLeftPadding;
        if (Dependency.isContainTTS() && (supportTTS = EngineManager3rd.getSupporTTS()) != null) {
            int i = 0;
            while (true) {
                if (i >= supportTTS.length) {
                    break;
                } else if (nLang != supportTTS[i].intValue()) {
                    i++;
                } else {
                    bExist = true;
                    break;
                }
            }
        }
        if (bExist && bShow) {
            nVisibility = View.VISIBLE;
        }
        this.mUSOnceBtn.setVisibility(nVisibility);
        this.mUSRepeatBtn.setVisibility(nVisibility);
        if (nLang == 65536 || nLang == 65537) {
            if (DictUtils.checkExistSecoundTTSFile(nLang)) {
                if (nLang == 65536) {
                    nTTSFirstStr = getResources().getString(R.string.tts_us);
                    nTTSSecondStr = getResources().getString(R.string.tts_uk);
                    nTTSFisrtBtnResId = R.drawable.tts;
                    nTTSSecondBtnResId = R.drawable.tts;
                    this.mUSOnceBtn.setEnabled(true);
                    this.mUSRepeatBtn.setEnabled(true);
                    this.mUKOnceBtn.setEnabled(true);
                    this.mUKRepeatBtn.setEnabled(true);
                } else {
                    nTTSFirstStr = getResources().getString(R.string.tts_zh_cn);
                    nTTSSecondStr = getResources().getString(R.string.tts_yue_cn);
                    nTTSTextSize = 8.4f;
                    String locale = Locale.getDefault().getISO3Language();
                    if (locale.contains("zho")) {
                        nUSBtnLeftPadding -= (int) (3.0f * nDensity);
                        nUKBtnLeftPadding += (int) (3.0f * nDensity);
                    }
                    if (CommonUtils.isUselessTTSWord(getPlayTTSWord(true), nDicType)) {
                        nTTSFisrtBtnResId = R.drawable.tts_lite;
                        nTTSSecondBtnResId = R.drawable.tts_lite;
                        nTTSFisrtRepeatBtnResId = R.drawable.repeat_lite;
                        nTTSSecondRepeatBtnResId = R.drawable.repeat_lite;
                        this.mUSOnceBtn.setEnabled(false);
                        this.mUSRepeatBtn.setEnabled(false);
                        this.mUKOnceBtn.setEnabled(false);
                        this.mUKRepeatBtn.setEnabled(false);
                    } else {
                        nTTSFisrtBtnResId = R.drawable.tts;
                        nTTSSecondBtnResId = R.drawable.tts;
                        this.mUSOnceBtn.setEnabled(true);
                        this.mUSRepeatBtn.setEnabled(true);
                        this.mUKOnceBtn.setEnabled(true);
                        this.mUKRepeatBtn.setEnabled(true);
                    }
                }
            } else {
                nVisibility = View.GONE;
                nTTSFisrtBtnResId = R.drawable.tts_etc;
                nTTSSecondBtnResId = R.drawable.tts_etc;
            }
        } else {
            nVisibility = View.GONE;
            nTTSFisrtBtnResId = R.drawable.tts_etc;
            nTTSSecondBtnResId = R.drawable.tts_etc;
        }
        this.mUKOnceBtn.setVisibility(nVisibility);
        this.mUKRepeatBtn.setVisibility(nVisibility);
        this.mUSOnceBtn.setText(nTTSFirstStr);
        this.mUSOnceBtn.setTextSize(nTTSTextSize);
        this.mUSOnceBtn.setPadding(nUSBtnLeftPadding, 0, nTTSRightPadding, 0);
        this.mUSOnceBtn.setBackgroundResource(nTTSFisrtBtnResId);
        this.mUKOnceBtn.setBackgroundResource(nTTSSecondBtnResId);
        this.mUKOnceBtn.setText(nTTSSecondStr);
        this.mUKOnceBtn.setTextSize(nTTSTextSize);
        this.mUKOnceBtn.setPadding(nUKBtnLeftPadding, 0, nTTSRightPadding, 0);
        this.mUSRepeatBtn.setBackgroundResource(nTTSFisrtRepeatBtnResId);
        this.mUKRepeatBtn.setBackgroundResource(nTTSSecondRepeatBtnResId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setEnableTTSButton(boolean isEnable) {
        if (this.mUSOnceBtn != null) {
            this.mUSOnceBtn.setEnabled(isEnable);
        }
        if (this.mUSRepeatBtn != null) {
            this.mUSRepeatBtn.setEnabled(isEnable);
        }
        if (this.mUKOnceBtn != null) {
            this.mUKOnceBtn.setEnabled(isEnable);
        }
        if (this.mUKRepeatBtn != null) {
            this.mUKRepeatBtn.setEnabled(isEnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.uitool.BaseActivity
    public String getPlayTTSWord(boolean bKeyword) {
        String word = "";
        if (this.mTextView != null) {
            if (bKeyword) {
                word = this.mTextView.getKeyword();
            } else if (this.mTextView.isSelectedText()) {
                word = this.mTextView.getSelectedString();
            } else {
                word = this.mTextView.getKeyword();
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
        if (this.mTextView != null) {
            return this.mTextView.getDbtype();
        }
        return -1;
    }

    private void showHideEmptyViewTitleTop(boolean bExtensionView) {
        if (this.mEmptyViewTitleTop != null) {
            if (this.mMeanToolbarLayout != null && bExtensionView) {
                this.mEmptyViewTitleTop.setVisibility(View.VISIBLE);
            } else {
                this.mEmptyViewTitleTop.setVisibility(View.GONE);
            }
            if (this.mMeanToolbarLayout != null) {
                this.mMeanToolbarLayout.requestLayout();
            }
        }
    }

    private void setImageBookMark(boolean bExtensionView) {
        if (this.mMainMeanBookmarkImageView != null) {
            if (bExtensionView) {
                this.mMainMeanBookmarkImageView.setBackgroundResource(R.drawable.bookmark);
            } else {
                this.mMainMeanBookmarkImageView.setBackgroundResource(R.drawable.bookmark_long);
            }
        }
    }

    public String getDefaultWordbookName() {
        String DefaultName = getResources().getString(R.string.default_wordbookname);
        String RetName = null;
        for (int i = 1; i <= 40; i++) {
            RetName = DefaultName + i;
            if (!DioDictDatabase.existWordbookFolder(this, RetName)) {
                break;
            }
        }
        return RetName;
    }

    public void restoreMakeDialog() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideEditMessage() {
        if (this.mInputWordbookNameTextView != null) {
            this.mInputWordbookNameTextView.setVisibility(View.GONE);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showSortCompleteToast(int nSort) {
        switch (nSort) {
            case 0:
            case 1:
                showToast(getString(R.string.sortByNameComplete));
                return;
            case 2:
            case 3:
                showToast(getString(R.string.sortByTimeComplete));
                return;
            case 4:
            case 5:
                showToast(getString(R.string.sortByDict));
                return;
            default:
                return;
        }
    }

    protected void checkSymbolKeyword() {
    }
}
