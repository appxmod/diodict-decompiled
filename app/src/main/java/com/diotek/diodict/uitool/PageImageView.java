package com.diotek.diodict.uitool;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;
import com.diotek.diodict.uitool.TouchGesture;
import com.diodict.decompiled.R;
import java.util.EventListener;

/* loaded from: classes.dex */
public class PageImageView {
    private Context mContext;
    private LinearLayout[] mHelpLayoutPageView;
    private RadioGroup mPageBar;
    private ViewFlipper mParent;
    private RadioButton[] mPageGridViewDot = null;
    private int mTotalLienarLayoutViewPage = 5;
    private int mCurrentPage = 0;
    TouchGesture layoutOnTouchListener = new TouchGesture();
    TouchGesture.TouchGestureOnTouchListener mTouchGestureOnTouchListener = new TouchGesture.TouchGestureOnTouchListener() { // from class: com.diotek.diodict.uitool.PageImageView.1
        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingClick() {
            return false;
        }

        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingNext() {
            PageImageView.this.startFlingNext();
            return false;
        }

        @Override // com.diotek.diodict.uitool.TouchGesture.TouchGestureOnTouchListener
        public boolean callBackFlingPrev() {
            PageImageView.this.startFlingPrev();
            return false;
        }
    };

    /* loaded from: classes.dex */
    public interface PageImageViewOnClickListener extends EventListener {
        void onItemClick(AdapterView<?> adapterView, View view, int i, long j);

        void onItemLongClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    public PageImageView(Context context, ViewFlipper parent, LinearLayout[] pageLinears, RadioGroup pageBar) {
        this.mHelpLayoutPageView = null;
        this.mParent = null;
        this.mPageBar = null;
        this.mContext = null;
        this.mContext = context;
        this.mParent = parent;
        this.mPageBar = pageBar;
        this.mHelpLayoutPageView = pageLinears;
    }

    public int createImagepageView(int orientation) {
        this.mPageGridViewDot = new RadioButton[5];
        for (int i = 0; i < 5; i++) {
            this.mHelpLayoutPageView[i].setClickable(true);
            this.mHelpLayoutPageView[i].setOnTouchListener(this.layoutOnTouchListener);
            this.layoutOnTouchListener.setOnTouchClickListener(this.mTouchGestureOnTouchListener);
            RadioButton dot = new RadioButton(this.mContext);
            dot.setBackgroundResource(0);
            dot.setButtonDrawable(R.drawable.pagedot);
            dot.setMaxWidth(37);
            dot.setMaxHeight(24);
            this.mPageGridViewDot[i] = dot;
            this.mPageBar.addView(this.mPageGridViewDot[i]);
        }
        this.mPageGridViewDot[0].setSelected(true);
        return this.mTotalLienarLayoutViewPage;
    }

    public void startFlingNext() {
        if (this.mCurrentPage < this.mTotalLienarLayoutViewPage - 1) {
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

    public void initpageView(RadioButton PageDot) {
        RadioButton dot = new RadioButton(this.mContext);
        dot.setBackgroundResource(R.drawable.pagedot);
        this.mPageBar.addView(dot);
    }

    public void setPageView(int Page) {
        if (Page < this.mTotalLienarLayoutViewPage || Page >= 0) {
            while (this.mCurrentPage != Page) {
                if (this.mCurrentPage < Page) {
                    startFlingNext();
                } else if (this.mCurrentPage > Page) {
                    startFlingPrev();
                }
            }
        }
    }

    public int getCurrentPage() {
        return this.mCurrentPage;
    }
}
