package com.diotek.diodict.uitool;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;
import com.diotek.diodict.GridViewAdapter;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.uitool.TouchGesture;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

/* loaded from: classes.dex */
public class PageGridView {
    public static final int PAGEDOTMODE_CHANGEDICT = 1;
    public static final int PAGEDOTMODE_FLASHCARD = 0;
    private ArrayList<HashMap<String, Object>>[] mArrayItems;
    private Context mContext;
    private RadioGroup mPageBar;
    private ViewFlipper mParent;
    private RadioButton[] mPageGridViewDot = null;
    private GridView[] mWordbookFolderGridView = null;
    private int mTotalGridViewPage = 0;
    private int mCountPerPage = 0;
    private int mCurrentPage = 0;
    GridViewAdapter[] mPageGridViewAdapter = null;
    GridViewAdapter[] mPageGridViewEditAdapter = null;
    GridViewAdapter[] mPageGridViewDeleteAdapter = null;
    TouchGesture.TouchGestureOnTouchListener mTouchGestureOnTouchListener = new TouchGesture.TouchGestureOnTouchListener() { // from class: com.diotek.diodict.uitool.PageGridView.1
        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingClick() {
            return false;
        }

        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingNext() {
            PageGridView.this.startFlingNext();
            return false;
        }

        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingPrev() {
            PageGridView.this.startFlingPrev();
            return false;
        }
    };
    private PageGridViewOnClickListener mBtnClickCallBack = null;
    AdapterView.OnItemClickListener mWordbookFolderGridViewOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.diotek.diodict.uitool.PageGridView.2
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            if (!PageGridView.this.mWordbookFolderGridViewOnTouchListener.getDraged()) {
                if ((PageGridView.this.mWordbookFolderGridViewOnTouchListener.getdragState() == 1 || PageGridView.this.mWordbookFolderGridViewOnTouchListener.getdragState() == 6 || PageGridView.this.mWordbookFolderGridViewOnTouchListener.getdragState() == 0) && PageGridView.this.mBtnClickCallBack != null) {
                    PageGridView.this.mBtnClickCallBack.onItemClick(parent, v, position, arg3);
                }
            }
        }
    };
    AdapterView.OnItemLongClickListener mWordbookFolderGridViewOnItemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: com.diotek.diodict.uitool.PageGridView.3
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long arg3) {
            if ((PageGridView.this.mCurrentPage != 0 || position >= 2) && PageGridView.this.mBtnClickCallBack != null && ((!PageGridView.this.mWordbookFolderGridViewOnTouchListener.getDraged() && PageGridView.this.mWordbookFolderGridViewOnTouchListener.getdragState() == 0) || PageGridView.this.mWordbookFolderGridViewOnTouchListener.getdragState() == 6)) {
                PageGridView.this.mBtnClickCallBack.onItemLongClick(parent, v, position, arg3);
            }
            return true;
        }
    };
    AdapterView.OnItemLongClickListener mChangeDictGridViewOnItemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: com.diotek.diodict.uitool.PageGridView.4
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            return true;
        }
    };
    TouchGesture mWordbookFolderGridViewOnTouchListener = new TouchGesture();
    TouchGesture mChangeDictGridViewOnTouchListener = new TouchGesture();
    View.OnClickListener PageDotOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.PageGridView.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            for (int i = 0; i < PageGridView.this.mTotalGridViewPage; i++) {
                if (PageGridView.this.mPageGridViewDot[i].getId() == v.getId()) {
                    PageGridView.this.setPageView(i);
                    return;
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface PageGridViewOnClickListener extends EventListener {
        void onItemClick(AdapterView<?> adapterView, View view, int i, long j);

        void onItemLongClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    public PageGridView(Context context, ViewFlipper parent, RadioGroup pageBar) {
        this.mParent = null;
        this.mPageBar = null;
        this.mContext = null;
        this.mContext = context;
        this.mParent = parent;
        this.mPageBar = pageBar;
    }

    public int createChangeDictPageGridView(ArrayList<HashMap<String, Object>> arrayItems, int nCountPerPage) {
        this.mCountPerPage = nCountPerPage;
        int nItemTotalSize = arrayItems.size();
        int ntmpItemSize = nItemTotalSize;
        this.mTotalGridViewPage = 0;
        do {
            this.mTotalGridViewPage++;
            ntmpItemSize -= nCountPerPage;
        } while (ntmpItemSize > 0);
        this.mWordbookFolderGridView = new GridView[this.mTotalGridViewPage];
        this.mPageGridViewAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mArrayItems = new ArrayList[this.mTotalGridViewPage];
        this.mPageGridViewDot = new RadioButton[this.mTotalGridViewPage];
        for (int i = 0; i < this.mTotalGridViewPage; i++) {
            float density = CommonUtils.getDeviceDensity(this.mContext);
            GridView gv = new GridView(this.mContext);
            gv.setNumColumns(-1);
            gv.setVerticalSpacing((int) (8.0f * density));
            gv.setHorizontalSpacing((int) (0.0f * density));
            gv.setColumnWidth((int) (78.6d * density));
            gv.setStretchMode(0);
            gv.setGravity(17);
            gv.setPadding((int) (24.0f * density), 0, 0, 0);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -1);
            gv.setLayoutParams(param);
            gv.requestLayout();
            gv.setSelector(R.drawable.changedictpopupicon_sel);
            gv.setDrawSelectorOnTop(true);
            gv.setOnItemClickListener(this.mWordbookFolderGridViewOnItemClickListener);
            gv.setOnItemLongClickListener(this.mChangeDictGridViewOnItemLongClickListener);
            gv.setOnTouchListener(this.mChangeDictGridViewOnTouchListener);
            this.mChangeDictGridViewOnTouchListener.setSelector(true, this.mContext.getResources().getDrawable(R.drawable.changedictpopupicon_sel), this.mContext.getResources().getDrawable(R.color.radiobutton));
            this.mChangeDictGridViewOnTouchListener.setOnTouchClickListener(this.mTouchGestureOnTouchListener);
            this.mWordbookFolderGridView[i] = gv;
            this.mParent.addView(this.mWordbookFolderGridView[i]);
            this.mPageGridViewDot[i] = createPageDotBtn(1);
            this.mPageBar.addView(this.mPageGridViewDot[i]);
        }
        this.mPageGridViewDot[0].setSelected(true);
        String[] from = {DictInfo.ListItem_DictType};
        int[] to = {R.id.changedicttype};
        ArrayList<HashMap<String, Object>> arrayItemsclone = (ArrayList) arrayItems.clone();
        for (int i2 = 0; i2 < this.mTotalGridViewPage; i2++) {
            int start = i2 * nCountPerPage;
            int end = (i2 + 1) * nCountPerPage;
            if (end > nItemTotalSize) {
                end = nItemTotalSize;
            }
            this.mArrayItems[i2] = new ArrayList<>(arrayItemsclone.subList(start, end));
            this.mPageGridViewAdapter[i2] = new GridViewAdapter(this.mContext, this.mArrayItems[i2], R.layout.changedict_rowitem_layout, from, to, 1000);
            this.mWordbookFolderGridView[i2].setAdapter((ListAdapter) this.mPageGridViewAdapter[i2]);
        }
        return this.mTotalGridViewPage;
    }

    public int createFlashcardPageGridView(ArrayList<HashMap<String, Object>> arrayItems, int nCountPerPage, int orientation) {
        this.mCountPerPage = nCountPerPage;
        int nItemTotalSize = arrayItems.size();
        int ntmpItemSize = nItemTotalSize;
        this.mTotalGridViewPage = 0;
        do {
            this.mTotalGridViewPage++;
            ntmpItemSize -= nCountPerPage;
        } while (ntmpItemSize > 0);
        this.mWordbookFolderGridView = new GridView[this.mTotalGridViewPage];
        this.mPageGridViewAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mPageGridViewEditAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mPageGridViewDeleteAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mArrayItems = new ArrayList[this.mTotalGridViewPage];
        this.mPageGridViewDot = new RadioButton[this.mTotalGridViewPage];
        for (int i = 0; i < this.mTotalGridViewPage; i++) {
            GridView gv = new GridView(this.mContext);
            gv.setNumColumns(-1);
            if (CommonUtils.isLowResolutionDevice(this.mContext)) {
                Resources rsc = this.mContext.getResources();
                gv.setVerticalSpacing(rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_vertical_spacing));
                gv.setHorizontalSpacing(rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_horizontal_spacing));
                gv.setPadding(rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_horizontal_padding), rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_paddingTop), rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_horizontal_padding), rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_paddingTop));
            } else {
                gv.setVerticalSpacing(20);
                gv.setHorizontalSpacing(29);
                if (orientation == 2 && this.mCountPerPage <= 5) {
                    gv.setPadding(30, 68, 30, 25);
                } else {
                    gv.setPadding(30, 35, 30, 25);
                }
            }
            gv.setColumnWidth((int) (100.0f * this.mContext.getResources().getDisplayMetrics().density));
            gv.setStretchMode(2);
            gv.setGravity(17);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -1);
            gv.setLayoutParams(param);
            gv.setSelector(17170445);
            gv.setOnItemClickListener(this.mWordbookFolderGridViewOnItemClickListener);
            gv.setOnItemLongClickListener(this.mWordbookFolderGridViewOnItemLongClickListener);
            gv.setOnTouchListener(this.mWordbookFolderGridViewOnTouchListener);
            this.mWordbookFolderGridViewOnTouchListener.setOnTouchClickListener(this.mTouchGestureOnTouchListener);
            this.mWordbookFolderGridView[i] = gv;
            this.mParent.addView(this.mWordbookFolderGridView[i]);
            this.mPageGridViewDot[i] = createPageDotBtn(0);
            this.mPageBar.addView(this.mPageGridViewDot[i]);
        }
        this.mPageGridViewDot[0].setSelected(true);
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        ArrayList<HashMap<String, Object>> arrayItemsclone = (ArrayList) arrayItems.clone();
        for (int i2 = 0; i2 < this.mTotalGridViewPage; i2++) {
            int start = i2 * nCountPerPage;
            int end = (i2 + 1) * nCountPerPage;
            if (end > nItemTotalSize) {
                end = nItemTotalSize;
            }
            this.mArrayItems[i2] = new ArrayList<>(arrayItemsclone.subList(start, end));
            this.mPageGridViewAdapter[i2] = new GridViewAdapter(this.mContext, this.mArrayItems[i2], R.layout.flashcard_rowitem_layout, from, to, 1003);
            this.mPageGridViewEditAdapter[i2] = new GridViewAdapter(this.mContext, this.mArrayItems[i2], R.layout.flashcard_rowitem_edit_layout, from, to, 1004);
            this.mPageGridViewDeleteAdapter[i2] = new GridViewAdapter(this.mContext, this.mArrayItems[i2], R.layout.flashcard_rowitem_delete_layout, from, to, GridViewAdapter.GRIDVIEW_MODE_FLASHCARD_DEL);
            this.mWordbookFolderGridView[i2].setAdapter((ListAdapter) this.mPageGridViewAdapter[i2]);
        }
        return this.mTotalGridViewPage;
    }

    public int notifyFlashcardList(ArrayList<HashMap<String, Object>> arrayItems, int MaxGridView, int orientation) {
        int nItemTotalSize = arrayItems.size();
        int ntmpItemSize = nItemTotalSize;
        this.mTotalGridViewPage = 0;
        if (nItemTotalSize == 0) {
            this.mArrayItems[0].clear();
            pageGridViewAdpaterNotifyDataSetChanged(0);
            int i = this.mTotalGridViewPage + 1;
            this.mTotalGridViewPage = i;
            return i;
        }
        while (ntmpItemSize > 0) {
            this.mTotalGridViewPage++;
            ntmpItemSize -= this.mCountPerPage;
        }
        if (this.mTotalGridViewPage > MaxGridView) {
            addNewPage(arrayItems, orientation);
        } else if (this.mTotalGridViewPage < MaxGridView) {
            this.mPageBar.removeAllViews();
            this.mPageGridViewDot = new RadioButton[this.mTotalGridViewPage];
            for (int i2 = 0; i2 < this.mTotalGridViewPage; i2++) {
                this.mPageGridViewDot[i2] = createPageDotBtn(0);
                this.mPageBar.addView(this.mPageGridViewDot[i2]);
            }
            if (this.mCurrentPage == this.mTotalGridViewPage) {
                startFlingPrev();
            }
            this.mPageGridViewDot[this.mCurrentPage].setSelected(true);
        }
        int nTotalGridViewPage = this.mWordbookFolderGridView.length;
        if (this.mTotalGridViewPage < nTotalGridViewPage) {
            for (int k = nTotalGridViewPage; k > this.mTotalGridViewPage; k--) {
                this.mParent.removeView(this.mWordbookFolderGridView[k - 1]);
                this.mWordbookFolderGridView[k - 1] = null;
            }
        }
        ArrayList<HashMap<String, Object>> arrayItemsclone = (ArrayList) arrayItems.clone();
        for (int i3 = 0; i3 < this.mTotalGridViewPage; i3++) {
            int start = i3 * this.mCountPerPage;
            int end = (i3 + 1) * this.mCountPerPage;
            if (end > nItemTotalSize) {
                end = nItemTotalSize;
            }
            this.mArrayItems[i3].clear();
            for (int j = 0; j < arrayItemsclone.subList(start, end).size(); j++) {
                this.mArrayItems[i3].add(arrayItemsclone.subList(start, end).get(j));
            }
            pageGridViewAdpaterNotifyDataSetChanged(i3);
        }
        return this.mTotalGridViewPage;
    }

    public void addNewPage(ArrayList<HashMap<String, Object>> arrayItems, int orientation) {
        int nItemTotalSize = arrayItems.size();
        int ntmpItemSize = nItemTotalSize;
        this.mPageBar.removeAllViews();
        this.mTotalGridViewPage = 0;
        while (ntmpItemSize > 0) {
            this.mTotalGridViewPage++;
            ntmpItemSize -= this.mCountPerPage;
        }
        this.mPageGridViewAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mPageGridViewEditAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mPageGridViewDeleteAdapter = new GridViewAdapter[this.mTotalGridViewPage];
        this.mArrayItems = new ArrayList[this.mTotalGridViewPage];
        this.mPageGridViewDot = new RadioButton[this.mTotalGridViewPage];
        GridView[] nTmpFolderGridView = new GridView[this.mTotalGridViewPage];
        for (int i = 0; i < this.mTotalGridViewPage - 1; i++) {
            nTmpFolderGridView[i] = this.mWordbookFolderGridView[i];
        }
        this.mWordbookFolderGridView = new GridView[this.mTotalGridViewPage];
        for (int i2 = 0; i2 < this.mTotalGridViewPage; i2++) {
            this.mWordbookFolderGridView[i2] = nTmpFolderGridView[i2];
            this.mPageGridViewDot[i2] = createPageDotBtn(0);
            this.mPageBar.addView(this.mPageGridViewDot[i2]);
        }
        GridView gv = new GridView(this.mContext);
        gv.setNumColumns(-1);
        if (CommonUtils.isLowResolutionDevice(this.mContext)) {
            Resources rsc = this.mContext.getResources();
            gv.setVerticalSpacing(rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_vertical_spacing));
            gv.setHorizontalSpacing(rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_horizontal_spacing));
            gv.setPadding(rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_horizontal_padding), rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_paddingTop), rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_horizontal_padding), rsc.getDimensionPixelSize(R.dimen.fc_pagegridview_paddingTop));
        } else {
            gv.setVerticalSpacing(20);
            gv.setHorizontalSpacing(29);
            if (orientation == 2 && this.mCountPerPage <= 5) {
                gv.setPadding(30, 68, 30, 25);
            } else {
                gv.setPadding(30, 35, 30, 25);
            }
        }
        gv.setColumnWidth((int) (100.0f * this.mContext.getResources().getDisplayMetrics().density));
        gv.setStretchMode(2);
        gv.setGravity(17);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -1);
        gv.setLayoutParams(param);
        gv.setSelector(17170445);
        gv.setOnItemClickListener(this.mWordbookFolderGridViewOnItemClickListener);
        gv.setOnItemLongClickListener(this.mWordbookFolderGridViewOnItemLongClickListener);
        gv.setOnTouchListener(this.mWordbookFolderGridViewOnTouchListener);
        this.mWordbookFolderGridView[this.mTotalGridViewPage - 1] = gv;
        this.mParent.addView(this.mWordbookFolderGridView[this.mTotalGridViewPage - 1]);
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        ArrayList<HashMap<String, Object>> arrayItemsclone = (ArrayList) arrayItems.clone();
        for (int i3 = 0; i3 < this.mTotalGridViewPage; i3++) {
            int start = i3 * this.mCountPerPage;
            int end = (i3 + 1) * this.mCountPerPage;
            if (end > nItemTotalSize) {
                end = nItemTotalSize;
            }
            this.mArrayItems[i3] = new ArrayList<>(arrayItemsclone.subList(start, end));
            this.mPageGridViewAdapter[i3] = new GridViewAdapter(this.mContext, this.mArrayItems[i3], R.layout.flashcard_rowitem_layout, from, to, 1003);
            this.mPageGridViewEditAdapter[i3] = new GridViewAdapter(this.mContext, this.mArrayItems[i3], R.layout.flashcard_rowitem_edit_layout, from, to, 1004);
            this.mPageGridViewDeleteAdapter[i3] = new GridViewAdapter(this.mContext, this.mArrayItems[i3], R.layout.flashcard_rowitem_delete_layout, from, to, GridViewAdapter.GRIDVIEW_MODE_FLASHCARD_DEL);
            this.mWordbookFolderGridView[i3].setAdapter((ListAdapter) this.mPageGridViewAdapter[i3]);
        }
    }

    public void setOnItemClickListener(PageGridViewOnClickListener listener) {
        this.mBtnClickCallBack = listener;
    }

    public void notifyGridViewAdapter() {
        this.mPageGridViewAdapter[this.mCurrentPage].notifyDataSetChanged();
    }

    public void notifyAllGridViewAdapter() {
        for (int i = 0; i < this.mTotalGridViewPage; i++) {
            this.mPageGridViewAdapter[i].notifyDataSetChanged();
        }
    }

    public void startFlingNext() {
        if (this.mCurrentPage < this.mTotalGridViewPage - 1) {
            this.mParent.setInAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.push_left_in));
            this.mParent.setOutAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.push_left_out));
            this.mParent.showNext();
            this.mCurrentPage++;
        }
        setPageBar();
    }

    public void startFlingPrev() {
        if (this.mCurrentPage > 0) {
            this.mParent.setInAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.push_right_in));
            this.mParent.setOutAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.push_right_out));
            this.mParent.showPrevious();
            this.mCurrentPage--;
        }
        setPageBar();
    }

    public void setPageBar() {
        for (int i = 0; i < this.mPageGridViewDot.length; i++) {
            if (i == this.mCurrentPage) {
                this.mPageGridViewDot[i].setSelected(true);
            } else {
                this.mPageGridViewDot[i].setSelected(false);
            }
        }
    }

    public void setPageView(int Page) {
        if (Page < this.mTotalGridViewPage || Page >= 0) {
            while (this.mCurrentPage != Page) {
                if (this.mCurrentPage < Page) {
                    startFlingNext();
                    if (this.mCurrentPage == this.mTotalGridViewPage - 1) {
                        return;
                    }
                } else if (this.mCurrentPage > Page) {
                    startFlingPrev();
                }
            }
        }
    }

    public void setGridViewAdapter(ArrayList<HashMap<String, Object>> arrayItems, int nCountPerPage, int mode) {
        int nItemTotalSize = arrayItems.size();
        String[] from = {DictInfo.ListItem_WordbookName, DictInfo.ListItem_WordCount};
        int[] to = {R.id.wordbooktitle, R.id.numword};
        ArrayList<HashMap<String, Object>> arrayItemsclone = (ArrayList) arrayItems.clone();
        for (int i = 0; i < this.mTotalGridViewPage; i++) {
            int start = i * nCountPerPage;
            int end = (i + 1) * nCountPerPage;
            if (end > nItemTotalSize) {
                end = nItemTotalSize;
            }
            this.mArrayItems[i] = new ArrayList<>(arrayItemsclone.subList(start, end));
            this.mPageGridViewAdapter[i] = new GridViewAdapter(this.mContext, this.mArrayItems[i], R.layout.flashcard_rowitem_layout, from, to, 1003);
            this.mPageGridViewEditAdapter[i] = new GridViewAdapter(this.mContext, this.mArrayItems[i], R.layout.flashcard_rowitem_edit_layout, from, to, 1004);
            this.mPageGridViewDeleteAdapter[i] = new GridViewAdapter(this.mContext, this.mArrayItems[i], R.layout.flashcard_rowitem_delete_layout, from, to, GridViewAdapter.GRIDVIEW_MODE_FLASHCARD_DEL);
            switch (mode) {
                case 0:
                    this.mWordbookFolderGridView[i].setAdapter((ListAdapter) this.mPageGridViewAdapter[i]);
                    break;
                case 1:
                    this.mWordbookFolderGridView[i].setAdapter((ListAdapter) this.mPageGridViewEditAdapter[i]);
                    break;
                case 2:
                    this.mWordbookFolderGridView[i].setAdapter((ListAdapter) this.mPageGridViewDeleteAdapter[i]);
                    break;
            }
        }
    }

    public void pageGridViewAdpaterNotifyDataSetChanged(int nPos) {
        if (this.mPageGridViewAdapter[nPos] != null) {
            this.mPageGridViewAdapter[nPos].notifyDataSetChanged();
        }
        if (this.mPageGridViewEditAdapter[nPos] != null) {
            this.mPageGridViewEditAdapter[nPos].notifyDataSetChanged();
        }
        if (this.mPageGridViewDeleteAdapter[nPos] != null) {
            this.mPageGridViewDeleteAdapter[nPos].notifyDataSetChanged();
        }
    }

    public int getCurrentPage() {
        return this.mCurrentPage;
    }

    public RadioButton createPageDotBtn(int nMode) {
        float density = CommonUtils.getDeviceDensity(this.mContext);
        RadioButton dot = new RadioButton(this.mContext);
        dot.setBackgroundResource(0);
        if (nMode == 1) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (16.0f * density), (int) (13.3f * density));
            dot.setLayoutParams(params);
            dot.setButtonDrawable(this.mContext.getResources().getDrawable(R.drawable.changedict_pagedot));
            dot.setFocusable(false);
        } else {
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams((int) (36.0f * density), (int) (24.0f * density));
            dot.setLayoutParams(params2);
            dot.setPadding(17, 17, 17, 17);
            dot.setButtonDrawable(this.mContext.getResources().getDrawable(R.drawable.pagedot));
        }
        dot.setOnClickListener(this.PageDotOnClickListener);
        dot.requestLayout();
        return dot;
    }

    public GridView getWordbookFolderGridView(int curPage) {
        return this.mWordbookFolderGridView[curPage];
    }
}
