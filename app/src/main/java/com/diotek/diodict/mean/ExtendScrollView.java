package com.diotek.diodict.mean;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class ExtendScrollView extends ScrollView {
    public static final boolean isUseLoadingPopup = true;
    private ExtendTextView mExtendTextView;
    private PopupWindow mLoadingPopup;
    private BaseMeanController mMeanController;
    private Handler mHandler = new Handler();
    private Runnable mAppendNextMeaningRunnable = new Runnable() { // from class: com.diotek.diodict.mean.ExtendScrollView.1
        @Override // java.lang.Runnable
        public void run() {
            ExtendScrollView.this.mMeanController.appendNextMeaning();
        }
    };
	private boolean scrollable = true;
	
	public ExtendScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMeanController(BaseMeanController meanController) {
        this.mMeanController = meanController;
    }
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(!scrollable) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
		if(!scrollable) {
			return true;
		}
        if (this.mExtendTextView == null) {
            this.mExtendTextView = (ExtendTextView) getChildAt(getChildCount() - 1);
            if (this.mExtendTextView == null) {
                return super.onTouchEvent(ev);
            }
        }
        if (this.mExtendTextView.isIntercepteScrollTouchEvent()) {
            return this.mExtendTextView.onTouchEvent(ev, getScrollY());
        }
        this.mExtendTextView.initializeSelectTextArea();
        return super.onTouchEvent(ev);
    }

    @Override // android.view.View
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.mExtendTextView != null) {
            this.mExtendTextView.dismissTextSelectGrip();
            this.mExtendTextView.initializeSelectTextArea();
        }
        if (this.mMeanController != null && isReachedToBottom() && this.mMeanController.isRemainMeaning()) {
            showLoadingPopup();
            this.mHandler.postDelayed(this.mAppendNextMeaningRunnable, 10L);
        }
    }

    private boolean isReachedToBottom() {
        View view = getChildAt(getChildCount() - 1);
        return view.getBottom() - (getHeight() + getScrollY()) <= 0;
    }

    private void createLoadingPopup() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService("layout_inflater");
        this.mLoadingPopup = new PopupWindow(getContext());
        TextView loadingTV = (TextView) inflate.inflate(R.layout.scrollview_loading_popup, (ViewGroup) null);
        this.mLoadingPopup.setContentView(loadingTV);
        this.mLoadingPopup.setBackgroundDrawable(null);
        this.mLoadingPopup.setTouchable(false);
        showLoadingPopup();
    }

    private void showLoadingPopup() {
        if (this.mLoadingPopup == null) {
            createLoadingPopup();
        }
        PopupWindow previewPopup = this.mLoadingPopup;
        int[] mOffsetInWindow = null;
        if (previewPopup != null) {
            if (0 == 0) {
                mOffsetInWindow = new int[2];
                getLocationInWindow(mOffsetInWindow);
            }
            int mPopupPreviewX = (getWidth() - 150) >> 1;
            int mPopupPreviewY = (getHeight() - 40) - 10;
            if (mPopupPreviewX < 0) {
                mPopupPreviewX = 0;
            }
            if (previewPopup.isShowing()) {
                previewPopup.update(mOffsetInWindow[0] + mPopupPreviewX, mOffsetInWindow[1] + mPopupPreviewY, 150, 40);
            } else {
                IBinder binder = getWindowToken();
                if (binder == null || !binder.isBinderAlive()) {
                    MSG.l(1, "In showLoadingPopup(), binder tocken is invalid.");
                    return;
                }
                previewPopup.setWidth(150);
                previewPopup.setHeight(40);
                previewPopup.showAtLocation(this, 0, mOffsetInWindow[0] + mPopupPreviewX, mOffsetInWindow[1] + mPopupPreviewY);
            }
            invalidate();
        }
    }

    private void dismissLoadingPopup() {
        if (this.mLoadingPopup != null && this.mLoadingPopup.isShowing()) {
            this.mLoadingPopup.dismiss();
        }
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        dismissLoadingPopup();
    }

    public void destroy() {
        this.mHandler.removeCallbacks(this.mAppendNextMeaningRunnable);
        dismissLoadingPopup();
    }
	
	public void setScrollEnabled(boolean isScrollEnable) {
		this.scrollable = isScrollEnable;
	}
}
