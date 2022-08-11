package com.diotek.diodict;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.diotek.diodict.uitool.UITools;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class HyperHistoryActivity extends HyperCommonActivity {
    private RelativeLayout mHistoryToolbarLayout = null;
    private Button mHistoryItemEditBtn = null;
    private Button mHistoryItemSortBtn = null;
    private Button mHistoryItemDeleteBtn = null;
    View.OnClickListener mHistoryItemEditBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperHistoryActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperHistoryActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mHistoryItemSortBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperHistoryActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperHistoryActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mHistoryItemDeleteBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperHistoryActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperHistoryActivity.this.runHyperDetailExitBtn();
        }
    };
    View.OnClickListener mSaveBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.HyperHistoryActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            HyperHistoryActivity.this.runSaveBtn();
            HyperHistoryActivity.this.setFocusableHyperActivity(false);
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        this.mCurrentMenuId = R.id.menu_history;
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
        setContentView(R.layout.hyperhistory_layout);
        prepareTitleLayout(R.string.title_history, this.mIsCreate);
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

    public void prepareContentLayout() {
        prepareHyperContentTopLayout();
        prepareHyperListView();
        prepareMeanTTSLayout();
        prepareMeanContentLayout();
        prepareFlashcardPopupLayout();
        showSoftInputMethod(false);
    }

    public void prepareHyperContentTopLayout() {
        prepareListToolLayout();
    }

    public void prepareListToolLayout() {
        this.mHistoryToolbarLayout = (RelativeLayout) findViewById(R.id.HistoryToolbarLayout);
        this.mHistoryItemEditBtn = (Button) findViewById(R.id.HistoryEditBtn);
        this.mHistoryItemSortBtn = (Button) findViewById(R.id.HistorySortBtn);
        this.mHistoryItemDeleteBtn = (Button) findViewById(R.id.HistoryDeleteBtn);
        this.mMarkerBtn = (ImageButton) findViewById(R.id.MarkerBtn);
        this.mFontBtn = (ImageButton) findViewById(R.id.FontBtn);
        this.mMemoBtn = (ImageButton) findViewById(R.id.MemoBtn);
        this.mSaveBtn = (ImageButton) findViewById(R.id.SaveBtn);
        this.mHistoryItemEditBtn.setOnClickListener(this.mHistoryItemEditBtnOnClickListener);
        this.mHistoryItemSortBtn.setOnClickListener(this.mHistoryItemSortBtnOnClickListener);
        this.mHistoryItemDeleteBtn.setOnClickListener(this.mHistoryItemDeleteBtnOnClickListener);
        this.mSaveBtn.setOnClickListener(this.mSaveBtnOnClickListener);
        LinearLayout meanToolbarLayout = (LinearLayout) findViewById(R.id.MeanToolbarLayout);
        this.mMarkerBtn.setVisibility(View.GONE);
        this.mFontBtn.setVisibility(View.GONE);
        this.mMemoBtn.setVisibility(View.GONE);
        meanToolbarLayout.setVisibility(View.VISIBLE);
        if (this.mIsBackgroundCheckedList) {
            this.mHistoryItemEditBtn.setVisibility(View.GONE);
            this.mHistoryItemSortBtn.setVisibility(View.GONE);
            this.mHistoryItemDeleteBtn.setVisibility(View.VISIBLE);
            return;
        }
        this.mHistoryItemEditBtn.setVisibility(View.VISIBLE);
        this.mHistoryItemSortBtn.setVisibility(View.VISIBLE);
        this.mHistoryItemDeleteBtn.setVisibility(View.GONE);
    }

    public boolean runKeyCodeBack() {
		if (clearTextViewSelection()) return true;
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
        if (this.mHistoryToolbarLayout != null) {
            UITools.recycleDrawable(this.mHistoryToolbarLayout.getBackground(), false, isconfigChange);
            this.mHistoryToolbarLayout = null;
        }
        System.gc();
    }

    @Override // com.diotek.diodict.HyperCommonActivity
    public void setFocusableHyperActivity(boolean bFocus) {
        super.setFocusableHyperActivity(bFocus);
        this.mHistoryItemEditBtn.setFocusable(bFocus);
        this.mHistoryItemDeleteBtn.setFocusable(bFocus);
        this.mHistoryItemSortBtn.setFocusable(bFocus);
        this.mSaveBtn.setFocusable(bFocus);
    }
}
