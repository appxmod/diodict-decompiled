package com.diotek.diodict;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.DictEditText;
import com.diotek.diodict.uitool.UITools;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class HyperSearchActivity extends HyperCommonActivity {
    private boolean mShowChangeDict;
    private boolean mShowSearch;
    private String mTitle;
    private RelativeLayout mInputLayout = null;
    private ImageButton mSearchWordBtn = null;
    private ImageButton mVoiceSearchBtn = null;
    private String mInputWord = "";
    protected boolean mEnableChangeDict = true;
    private ImageButton mChangeDictionaryBtn = null;
    private ImageButton mChangeLanguageBtn = null;
    private EditText mSearchEditText = null;
    private ImageButton mClearBtn = null;
    View.OnClickListener mFinishOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperSearchActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperSearchActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mBtnDeleteTextBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperSearchActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperSearchActivity.this.mEdittextWordbookName = (EditText) HyperSearchActivity.this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
            if (HyperSearchActivity.this.mEdittextWordbookName != null) {
                HyperSearchActivity.this.mEdittextWordbookName.setText("");
            }
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        if (super.onCreateActivity(savedInstanceState)) {
            this.mCurrentMenuId = R.id.menu_search;
            initActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.HyperCommonActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
			toolbarWidgets.isVisible = false;
            if (resultCode == -1) {
                saveToMemoDB(data);
            }
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (runKeyCodeBack()) {
                    return true;
                }
                break;
            case 21:
            case 22:
				toolbarWidgets.setFocusMarker(keyCode != 21);
                return true;
			case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
				if (toolbarWidgets.onKeyUp_ENTER()) {
					return true;
				}
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // com.diotek.diodict.HyperCommonActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleSaveMarkerObject();
        memoryInitialize(true);
        initActivity();
        reloadWordbookDialog();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.HyperCommonActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity
    public void onDestroy() {
        memoryInitialize(false);
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.HyperCommonActivity
    public void runHyperDetailPrevNextSearchBtn(boolean bNext) {
        handleSaveMarkerObject();
        if (toolbarWidgets.isMarkerShowing()) {
			toolbarWidgets.mMarkerPopup.dismiss();
			toolbarWidgets.mMarkerPopup = null;
        }
        super.runHyperDetailPrevNextSearchBtn(bNext);
    }

    private void initActivity() {
        backupParameters();
        setContentView(R.layout.hypersearch_layout);
        prepareTitleLayout(R.string.title_search, this.mIsCreate);
        prepareContentLayout();
        if (!this.mIsCreate) {
            restoreState();
            setHyperDetailPrevNextSearchBtn();
            restoreExtraData();
        }
        showHyperMoveBtnLayout();
        runHyperDeatilSearchListView(this.mHyperDetailMeanPos);
        restoreMakeDialog();
    }

    @Override // com.diotek.diodict.HyperCommonActivity
    public Bundle prepareExtraData() {
        Bundle extra = super.prepareExtraData();
        if (extra != null) {
            this.mInputWord = extra.getString(DictInfo.INTENT_SAVE_INPUTWORD);
            this.mTitle = extra.getString(DictInfo.INTENT_HYPER_DICTNAME);
            this.mShowChangeDict = extra.getBoolean(DictInfo.INTENT_HYPER_SHOW_CHANGEDICT);
            this.mShowSearch = extra.getBoolean(DictInfo.INTENT_HYPER_SHOW_SEARCH);
        }
        return extra;
    }

    public void prepareContentLayout() {
        prepareMeanTTSLayout();
        prepareHyperContentTopLayout();
        prepareHyperListView();
        prepareMeanContentLayout();
        prepareMeanToolLayout();
        prepareFlashcardPopupLayout();
    }

    public void prepareHyperContentTopLayout() {
        this.mInputLayout = (RelativeLayout) findViewById(R.id.InputLayout);
        this.mChangeDictionaryBtn = (ImageButton) findViewById(R.id.ChangeDictionaryBtn);
        this.mChangeLanguageBtn = (ImageButton) findViewById(R.id.ChangeLanguageBtn);
        this.mSearchEditText = (DictEditText) findViewById(R.id.SearchEditText);
        this.mSearchEditText.setText(this.mInputWord);
        this.mClearBtn = (ImageButton) findViewById(R.id.ClearBtn);
        this.mSearchWordBtn = (ImageButton) findViewById(R.id.SearchWordBtn);
        if (this.mEngine.getSupportMainDictionary().length > 1) {
            this.mChangeDictionaryBtn.setOnClickListener(this.mFinishOnClickListener);
        } else {
            this.mChangeDictionaryBtn.setVisibility(View.GONE);
            this.mEnableChangeDict = false;
        }
        this.mChangeLanguageBtn.setOnClickListener(this.mFinishOnClickListener);
        this.mSearchEditText.setOnClickListener(this.mFinishOnClickListener);
        this.mClearBtn.setOnClickListener(this.mFinishOnClickListener);
        this.mSearchWordBtn.setOnClickListener(this.mFinishOnClickListener);
        this.mVoiceSearchBtn = (ImageButton) findViewById(R.id.VoiceSearchBtn);
        this.mVoiceSearchBtn.setOnClickListener(this.mFinishOnClickListener);
        int nDicType = DictUtils.getSearchLastDictFromPreference(this);
        if (this.mShowChangeDict) {
            this.mChangeLanguageBtn.setVisibility(View.VISIBLE);
        }
        if (this.mShowSearch) {
            this.mSearchWordBtn.setVisibility(View.VISIBLE);
        }
        if (this.mTitle != null && this.mSearchDBNameTextView != null) {
            this.mSearchDBNameTextView.setText(this.mTitle);
        }
        if (this.mInputWord == null || this.mInputWord.length() == 0) {
            ImageButton clearBtn = (ImageButton) findViewById(R.id.ClearBtn);
            clearBtn.setVisibility(View.GONE);
        }
        if (this.mSearchEditText != null) {
            this.mSearchEditText.setHint(DictDBManager.getCurDictHint(nDicType));
            this.mSearchEditText.setFocusable(false);
        }
        int nSearchMethod = DictUtils.getSearchLastTypeFromPreference(this);
        if (Dependency.isChina()) {
            if (nDicType == 65520) {
                showHideSearchWordBtn(0);
            } else {
                setSearchWordBtnBySearchMethod(nSearchMethod);
            }
        }
        setChangeLanguagBtn(this.mEngine.getCurDict());
    }

    public void setSearchWordBtnBySearchMethod(int nSearchMethod) {
        if (nSearchMethod == 1 || nSearchMethod == 512) {
            showHideSearchWordBtn(8);
        } else {
            showHideSearchWordBtn(0);
        }
    }

    public void showHideSearchWordBtn(int nVisible) {
        if (this.mSearchWordBtn != null) {
            this.mSearchWordBtn.setVisibility(nVisible);
        }
        if (this.mVoiceSearchBtn != null && !this.mUseVoiceSearch) {
            if (nVisible == 0) {
                this.mVoiceSearchBtn.setVisibility(View.GONE);
                this.mSearchWordBtn.setBackgroundResource(R.drawable.searchbtn_end);
                return;
            }
            this.mVoiceSearchBtn.setVisibility(View.VISIBLE);
            this.mVoiceSearchBtn.setBackgroundResource(R.drawable.searchedittext_end);
        }
    }

    public void prepareMeanToolLayout() {
		ViewGroup mMeanToolbarLayout = findViewById(R.id.MeanToolbarLayout);
        this.mMarkerBtn = mMeanToolbarLayout.findViewById(R.id.MarkerBtn);
        this.mFontBtn = mMeanToolbarLayout.findViewById(R.id.FontBtn);
        this.mMemoBtn = mMeanToolbarLayout.findViewById(R.id.MemoBtn);
        this.mSaveBtn = mMeanToolbarLayout.findViewById(R.id.SaveBtn);
		ViewUtils.setOnClickListenersOneDepth(mMeanToolbarLayout, toolbarWidgets.btnOnClickListener, 1, null);
		mMeanToolbarLayout.setVisibility(View.VISIBLE);
    }

    protected void runHyperDetailExitBtn() {
        closeHyperDetailView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.diotek.diodict.HyperCommonActivity
    public void runHyperDeatilSearchListView(int nPos) {
        super.runHyperDeatilSearchListView(nPos);
        UITools.prepareMemoSkin(this, this.mHyperHistoryDict, this.mHyperHistoryWord, this.mHyperHistorySUID, this.mMemoBtn);
    }

    public boolean runKeyCodeBack() {
		if (clearTextViewSelection()) return true;
		if (this.mCopyToFlashcardLayout.getVisibility() == 0) {
			showCopyToFlashcardLayout(false);
			this.mSaveBtn.setSelected(false);
			return true;
		} else if (this.mTextView.gripShowing()) {
			this.mTextView.clearSelection();
			this.mTextView.forceInvalidate();
			return true;
		} else if (this.mHyperSimpleViewModule != null && this.mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
			this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
			return true;
		} else if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
			this.mFileLinkTagViewManager.closeFileLinkPopup();
			return true;
		} else if (toolbarWidgets.destroyData()) {
			return false;
		} else {
			runHyperDetailExitBtn();
			return true;
		}
	}

    protected void saveToMemoDB(Intent intent) {
        String memoDataString = intent.getExtras().getString(DictInfo.INTENT_MEMO_INFO_DATA);
        int skin = intent.getExtras().getInt(DictInfo.INTENT_MEMO_INFO_SKIN);
        if (memoDataString != null && memoDataString.length() >= 0) {
            int dbtyp = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_DICT, 1);
            String keyword = intent.getStringExtra(DictInfo.INTENT_MEMO_INFO_WORD);
            int suid = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_SUID, 1);
            if (memoDataString.length() == 0) {
                DioDictDatabase.deleteMemo(getApplicationContext(), dbtyp, keyword, suid);
                Toast.makeText(this, getResources().getString(R.string.memo_deleted), 0).show();
            } else {
                DioDictDatabase.addMemo(getApplicationContext(), dbtyp, keyword, suid, memoDataString, skin);
                Toast.makeText(this, getResources().getString(R.string.memo_saved), 0).show();
            }
            UITools.prepareMemoSkin(this, this.mHyperHistoryDict, this.mHyperHistoryWord, this.mHyperHistorySUID, this.mMemoBtn);
            if (this.mHyperDetailListView != null) {
                this.mHyperDetailListView.invalidateViews();
            }
        }
    }

    public void setSearchDBName() {
        this.mSearchDBNameTextView.setText(DictInfo.mCurrentDBName);
    }

    public void toggleButtonSelect(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setSelected(true);
        }
    }

    private void memoryInitialize(boolean isconfigChange) {
        if (this.mInputLayout != null) {
            UITools.recycleDrawable(this.mInputLayout.getBackground(), false, isconfigChange);
            this.mInputLayout = null;
        }
        System.gc();
    }

    @Override // com.diotek.diodict.HyperCommonActivity
    public void setFocusableActivity(boolean bFocus) {
        super.setFocusableActivity(bFocus);
        this.mChangeDictionaryBtn.setFocusable(bFocus);
        this.mChangeLanguageBtn.setFocusable(bFocus);
        this.mSearchWordBtn.setFocusable(bFocus);
        this.mVoiceSearchBtn.setFocusable(bFocus);
        this.mMarkerBtn.setFocusable(bFocus);
        this.mFontBtn.setFocusable(bFocus);
        this.mMemoBtn.setFocusable(bFocus);
        this.mSaveBtn.setFocusable(bFocus);
        this.mClearBtn.setFocusable(bFocus);
    }

    private void setChangeLanguagBtn(int nDicType) {
        if (this.mEnableChangeDict) {
            this.mChangeLanguageBtn.setBackgroundResource(R.drawable.changelang);
        } else {
            this.mChangeLanguageBtn.setBackgroundResource(R.drawable.changelang_left_eng);
        }
    }
}
