package com.diotek.diodict.mean;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.diotek.diodict.TTSManager;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.CustomPopupLinearLayout;
import com.diotek.diodict3.phone.samsung.chn.R;

/* loaded from: classes.dex */
public class FileLinkTagViewManager {
    private ExtendTextView mContentTextView;
    private Activity mContext;
    private EngineManager3rd mEngine;
    private View mPopupParents;
    private TTSManager mTTSManager;
    private BaseMeanController.ThemeModeCallback mThemeModeCallback;
    private PopupWindow mLinkTextPopup = null;
    private int mLinkPopupHeight = 0;
    private SearchMeanController mLinkMeanController = null;
    PopupWindow.OnDismissListener mLinkTextPopupOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.mean.FileLinkTagViewManager.1
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            FileLinkTagViewManager.this.initTextSelected();
            FileLinkTagViewManager.this.mLinkTextPopup = null;
            if (FileLinkTagViewManager.this.mTTSManager != null) {
                FileLinkTagViewManager.this.mTTSManager.onTerminateTTS();
            }
        }
    };
    CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback mCustomPopupLinearLayoutOnKeyListenerCallback = new CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback() { // from class: com.diotek.diodict.mean.FileLinkTagViewManager.2
        @Override // com.diotek.diodict.uitool.CustomPopupLinearLayout.CustomPopupLinearLayoutOnKeyListenerCallback
        public void runOnKeyListener(KeyEvent event) {
            if (FileLinkTagViewManager.this.mLinkTextPopup != null) {
                FileLinkTagViewManager.this.mLinkTextPopup.dismiss();
                FileLinkTagViewManager.this.mLinkTextPopup = null;
            }
        }
    };
    View.OnClickListener mHyperDialogPopupOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.mean.FileLinkTagViewManager.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (FileLinkTagViewManager.this.mLinkTextPopup != null) {
                FileLinkTagViewManager.this.mLinkTextPopup.dismiss();
                FileLinkTagViewManager.this.mLinkTextPopup = null;
            }
        }
    };

    public FileLinkTagViewManager(Activity context, EngineManager3rd engine, ExtendTextView contentTextView, RelativeLayout popupParentsLayout, LinearLayout popupParentsSubLayout, BaseMeanController.ThemeModeCallback themeModeCallback) {
        this.mContext = null;
        this.mEngine = null;
        this.mContentTextView = null;
        this.mPopupParents = null;
        this.mContext = context;
        this.mEngine = engine;
        this.mContentTextView = contentTextView;
        this.mPopupParents = popupParentsLayout;
        this.mThemeModeCallback = themeModeCallback;
    }

    public FileLinkTagViewManager(Activity context, EngineManager3rd engine, ExtendTextView contentTextView, RelativeLayout popupParentsLayout, RelativeLayout popupParentsSubLayout, BaseMeanController.ThemeModeCallback themeModeCallback) {
        this.mContext = null;
        this.mEngine = null;
        this.mContentTextView = null;
        this.mPopupParents = null;
        this.mContext = context;
        this.mEngine = engine;
        this.mContentTextView = contentTextView;
        this.mPopupParents = popupParentsLayout;
        this.mThemeModeCallback = themeModeCallback;
    }

    public void onPause() {
        if (this.mTTSManager != null) {
            this.mTTSManager.onTerminateTTS();
        }
        destory();
    }

    public void destory() {
        if (this.mTTSManager != null && this.mTTSManager.mTTSStopPopup != null) {
            this.mTTSManager.mTTSStopPopup.dismiss();
            this.mTTSManager.mTTSStopPopup = null;
        }
        if (this.mLinkTextPopup != null) {
            this.mLinkTextPopup.dismiss();
            this.mLinkTextPopup = null;
        }
        if (this.mLinkMeanController != null) {
            this.mLinkMeanController = null;
        }
    }

    public SearchMeanController prepareLinkTextMeanPopup(Context ctx, int dicType, String keyword, int suid) {
        LayoutInflater inflate = (LayoutInflater) ctx.getSystemService("layout_inflater");
        CustomPopupLinearLayout PopupContent = (CustomPopupLinearLayout) inflate.inflate(R.layout.hypertext_summary_layout, (ViewGroup) null);
        if (this.mLinkTextPopup == null) {
            PopupContent.setOnKeyListenerCallback(this.mCustomPopupLinearLayoutOnKeyListenerCallback);
            this.mLinkTextPopup = CommonUtils.makeWindowWithPopupWindow(ctx, 0, PopupContent, ctx.getResources().getDrawable(R.drawable.trans), null, true);
            ((Button) PopupContent.findViewById(R.id.hyper_detail)).setVisibility(8);
            PopupContent.setOnClickListener(this.mHyperDialogPopupOnClickListener);
            this.mLinkTextPopup.setOnDismissListener(this.mLinkTextPopupOnDismissListener);
            ScrollView scrollView = (ScrollView) PopupContent.findViewById(R.id.hyper_scrollview);
            scrollView.setFadingEdgeLength(0);
            ExtendTextView textView = (ExtendTextView) PopupContent.findViewById(R.id.hypertext_mean_textview);
            TextView keywordView = (TextView) PopupContent.findViewById(R.id.hyper_mean_titleview);
            prepareMeanTTSLayout(PopupContent);
            this.mLinkMeanController = new SearchMeanController(ctx, keywordView, textView, null, null, this.mEngine, false, this.mThemeModeCallback, this, this.mTTSManager.mTTSLayoutCallback);
            this.mTTSManager.setExtendTextView(textView);
            if (DictDBManager.getCpCHNDictionary(dicType)) {
                this.mTTSManager.setTitleTextView(keywordView);
            }
            textView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            scrollView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            this.mLinkPopupHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.hyper_text_maximum_height);
        }
        this.mLinkMeanController.setMeanViewKeywordInfo(dicType, keyword, suid, 0);
        showLinkTextMeanPopup(this.mContext);
        return this.mLinkMeanController;
    }

    protected void prepareMeanTTSLayout(View view) {
        this.mTTSManager = new TTSManager(this.mContext, view, this.mPopupParents);
        this.mTTSManager.prepareMeanTTSLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initTextSelected() {
        if (this.mContentTextView != null) {
            this.mContentTextView.initTextSelect();
            this.mContentTextView.forceInvalidate();
        }
    }

    public void closeFileLinkPopup() {
        if (this.mLinkTextPopup != null && this.mLinkTextPopup.isShowing()) {
            initTextSelected();
            this.mLinkTextPopup.dismiss();
            this.mLinkTextPopup = null;
        }
    }

    private void showLinkTextMeanPopup(Context ctx) {
        Display display = this.mContext.getWindowManager().getDefaultDisplay();
        int popupY = display.getHeight() - this.mLinkPopupHeight;
        int popupWidth = this.mPopupParents.getWidth() - 5;
        int popupHeight = this.mLinkPopupHeight;
        if (this.mLinkTextPopup != null) {
            if (this.mLinkTextPopup.isShowing()) {
                this.mLinkTextPopup.update(5, popupY, popupWidth, this.mLinkPopupHeight);
                return;
            }
            this.mLinkTextPopup.setWidth(popupWidth);
            this.mLinkTextPopup.setHeight(popupHeight);
            this.mLinkTextPopup.showAtLocation(this.mPopupParents, 0, 5, popupY);
        }
    }

    public boolean isShowingLinkTextPopup() {
        return this.mLinkTextPopup != null && this.mLinkTextPopup.isShowing();
    }

    public void setFocusLinkTextPopup() {
        this.mLinkTextPopup.setFocusable(true);
        if (this.mTTSManager != null) {
            this.mTTSManager.requestFocusTTSBtn();
        }
    }

    public void onDestory() {
        closeFileLinkPopup();
    }
}
