package com.diotek.diodict;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.uitool.UITools;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class HyperFlashcardActivity extends HyperCommonActivity {
    private RelativeLayout mFlashcardItemEditLayout = null;
    private Button mFlashcardItemCradleBtn = null;
    private Button mFlashcardItemStudyBtn = null;
    private Button mFlashcardItemDictationBtn = null;
    private Button mFlashcardItemEditDeleteBtn = null;
    private Button mFlashcardItemEditCopyBtn = null;
    private Button mFlashcardItemEditBtn = null;
    private Button mFlashcardItemEditSortBtn = null;
    View.OnClickListener mFlashcardItemEditDeleteBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mFlashcardItemEditCopyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mFlashcardItemEditSortBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mFlashcardItemEditBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mFlashcardItemCradleBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mFlashcardItemStudyBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mFlashcardItemDictationBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mSaveBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperFlashcardActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperFlashcardActivity.this.runSaveBtn();
            HyperFlashcardActivity.this.setFocusableHyperActivity(false);
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        this.mCurrentMenuId = R.id.menu_flashcard;
        if (super.onCreateActivity(savedInstanceState)) {
            initActivity();
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
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // com.diotek.diodict.HyperCommonActivity, com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    private void initActivity() {
        backupParameters();
        setContentView(R.layout.hyperflashcard_layout);
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        prepareContentLayout();
        if (!this.mIsCreate) {
            restoreState();
            setHyperDetailPrevNextSearchBtn();
            restoreExtraData();
        }
        showHyperMoveBtnLayout();
        runHyperDeatilSearchListView(this.mHyperDetailMeanPos);
    }

    public void prepareContentLayout() {
        prepareHyperContentTopLayout();
        prepareHyperListView();
        prepareMeanTTSLayout();
        prepareMeanContentLayout();
        prepareFlashcardPopupLayout();
        showSoftInputMethod(false);
        findViewById(R.id.EmptyViewTitleTop).setVisibility(View.GONE);
    }

    public void prepareHyperContentTopLayout() {
        prepareListToolLayout();
        prepareMeanToolLayout();
    }

    public void prepareMeanToolLayout() {
        this.mFlashcardItemCradleBtn = (Button) findViewById(R.id.CradleBtn);
        this.mFlashcardItemStudyBtn = (Button) findViewById(R.id.StudyBtn);
        this.mFlashcardItemDictationBtn = (Button) findViewById(R.id.DictationBtn);
        this.mFlashcardItemCradleBtn.setOnClickListener(this.mFlashcardItemCradleBtnOnClickListener);
        this.mFlashcardItemStudyBtn.setOnClickListener(this.mFlashcardItemStudyBtnOnClickListener);
        this.mFlashcardItemDictationBtn.setOnClickListener(this.mFlashcardItemDictationBtnOnClickListener);
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
        this.mFlashcardItemEditBtn.setOnClickListener(this.mFlashcardItemEditBtnOnClickListener);
        this.mFlashcardItemEditSortBtn.setOnClickListener(this.mFlashcardItemEditSortBtnOnClickListener);
        this.mFlashcardItemEditDeleteBtn.setOnClickListener(this.mFlashcardItemEditDeleteBtnOnClickListener);
        this.mFlashcardItemEditCopyBtn.setOnClickListener(this.mFlashcardItemEditCopyBtnOnClickListener);
        if (this.mIsBackgroundCheckedList) {
            this.mFlashcardItemEditBtn.setVisibility(View.GONE);
            this.mFlashcardItemEditSortBtn.setVisibility(View.GONE);
            this.mFlashcardItemEditDeleteBtn.setVisibility(View.VISIBLE);
            this.mFlashcardItemEditCopyBtn.setVisibility(View.VISIBLE);
            return;
        }
        this.mFlashcardItemEditBtn.setVisibility(View.VISIBLE);
        this.mFlashcardItemEditSortBtn.setVisibility(View.VISIBLE);
        this.mFlashcardItemEditDeleteBtn.setVisibility(View.GONE);
        this.mFlashcardItemEditCopyBtn.setVisibility(View.GONE);
    }

    public boolean runKeyCodeBack() {
        if (this.mHyperSimpleViewModule != null && this.mHyperSimpleViewModule.isShowingHyperDialogPopup()) {
            this.mHyperSimpleViewModule.closeHyperTextSummaryPopup(false);
        } else if (this.mFileLinkTagViewManager != null && this.mFileLinkTagViewManager.isShowingLinkTextPopup()) {
            this.mFileLinkTagViewManager.closeFileLinkPopup();
        } else {
            runHyperDetailExitBtn();
        }
        return true;
    }

    protected void runHyperDetailExitBtn() {
        closeHyperDetailView();
    }

    private void memoryInitialize(boolean isconfigChange) {
        if (this.mFlashcardItemEditLayout != null) {
            UITools.recycleDrawable(this.mFlashcardItemEditLayout.getBackground(), false, isconfigChange);
            this.mFlashcardItemEditLayout = null;
        }
        System.gc();
    }

    @Override // com.diotek.diodict.HyperCommonActivity
    public void setFocusableHyperActivity(boolean bFocus) {
        super.setFocusableHyperActivity(bFocus);
        this.mFlashcardItemEditDeleteBtn.setFocusable(bFocus);
        this.mFlashcardItemEditCopyBtn.setFocusable(bFocus);
        this.mFlashcardItemEditSortBtn.setFocusable(bFocus);
        this.mFlashcardItemCradleBtn.setFocusable(bFocus);
        this.mFlashcardItemStudyBtn.setFocusable(bFocus);
        this.mFlashcardItemDictationBtn.setFocusable(bFocus);
    }
}
