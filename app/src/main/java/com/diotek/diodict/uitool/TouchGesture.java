package com.diotek.diodict.uitool;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import java.util.EventListener;

/* loaded from: classes.dex */
public class TouchGesture implements View.OnTouchListener {
    private static final int EDIC_TOUCH_H = 20;
    private static final int EDIC_TOUCH_T = 100;
    private static final int EDIC_TOUCH_V = 20;
    private static int mDragState = 0;
    private int m_DownX;
    private int m_DownY;
    private int m_PrevX;
    private int m_PrevY;
    private long m_nDownT;
    private int m_nDrgState;
    protected boolean mDraged = false;
    private boolean mShowSelector = false;
    private Drawable mDownSelectorDrawable = null;
    private Drawable mUpSelectorDrawable = null;
    private Handler mHandler = new Handler();
    private TouchGestureOnTouchListener mTouchGestureCallBack = null;
    Runnable mRunnableLongClick = new Runnable() { // from class: com.diotek.diodict.uitool.TouchGesture.1
        @Override // java.lang.Runnable
        public void run() {
            long UpT = System.currentTimeMillis();
            if (TouchGesture.this.m_nDownT + 100 < UpT && TouchGesture.this.m_nDrgState == 1) {
                TouchGesture.this.m_nDrgState = 6;
                int unused = TouchGesture.mDragState = 6;
            }
            TouchGesture.this.mDraged = false;
        }
    };

    /* loaded from: classes.dex */
    public interface TouchGestureOnTouchListener extends EventListener {
        boolean callBackFlingClick();

        boolean callBackFlingNext();

        boolean callBackFlingPrev();
    }

    public void setSelector(boolean bShowSelector, Drawable down, Drawable up) {
        this.mShowSelector = bShowSelector;
        this.mDownSelectorDrawable = down;
        this.mUpSelectorDrawable = up;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case 0:
                this.mDraged = false;
                MeanListMotionDown(v, x, y);
                return false;
            case 1:
                return MeanListMotionUp(v, x, y);
            case 2:
                this.mDraged = true;
                return MeanListMotionMove(v, x, y);
            default:
                return false;
        }
    }

    public void setOnTouchClickListener(TouchGestureOnTouchListener listener) {
        this.mTouchGestureCallBack = listener;
    }

    public boolean MeanListMotionDown(View v, int x, int y) {
        if (this.mShowSelector && this.mDownSelectorDrawable != null) {
            ((GridView) v).setSelector(this.mDownSelectorDrawable);
        }
        touchDown(x, y);
        return true;
    }

    public boolean MeanListMotionMove(View v, int x, int y) {
        mDragState = touchMove(x, y);
        return true;
    }

    public boolean MeanListMotionUp(View v, int x, int y) {
        if (this.mShowSelector && this.mUpSelectorDrawable != null) {
            ((GridView) v).setSelector(this.mUpSelectorDrawable);
            v.invalidate();
        }
        mDragState = touchUp(x, y);
        switch (mDragState) {
            case 9:
                this.mTouchGestureCallBack.callBackFlingPrev();
                return true;
            case 10:
                this.mTouchGestureCallBack.callBackFlingNext();
                return true;
            default:
                return false;
        }
    }

    public void touchDown(int x, int y) {
        this.m_nDrgState = 1;
        long lTickCount = System.currentTimeMillis();
        this.m_nDownT = lTickCount;
        this.m_DownX = x;
        this.m_PrevX = x;
        this.m_DownY = y;
        this.m_PrevY = y;
        this.mHandler.postDelayed(this.mRunnableLongClick, 100L);
    }

    public int touchUp(int x, int y) {
        long UpT = System.currentTimeMillis();
        this.mHandler.removeCallbacks(this.mRunnableLongClick);
        int RetVal = this.m_nDrgState;
        if (this.m_nDrgState == 6) {
            setDragDirection(x, y);
        }
        if (this.m_nDownT + 100 < UpT) {
            switch (this.m_nDrgState) {
                case 1:
                    RetVal = 6;
                    break;
                case 2:
                    RetVal = 7;
                    break;
                case 3:
                    RetVal = 8;
                    break;
                case 4:
                    RetVal = 9;
                    break;
                case 5:
                    RetVal = 10;
                    break;
            }
        }
        this.mDraged = false;
        this.m_nDrgState = 0;
        return RetVal;
    }

    public int touchMove(int X, int Y) {
        if (this.m_nDrgState != 0) {
            setDragDirection(X, Y);
        }
        this.m_PrevX = X;
        this.m_PrevY = Y;
        return this.m_nDrgState;
    }

    private void setDragDirection(int X, int Y) {
        if (this.m_DownX - 20 >= X || this.m_DownX + 20 <= X || this.m_DownY - 20 >= Y || this.m_DownY + 20 <= Y) {
            if (Math.abs(X - this.m_PrevX) < Math.abs(Y - this.m_PrevY)) {
                if (Y - this.m_PrevY > 0) {
                    if (this.m_nDrgState == 1) {
                        this.m_nDrgState = 2;
                    } else if (this.m_nDrgState != 2) {
                        this.m_nDrgState = 11;
                    }
                } else if (this.m_nDrgState == 1) {
                    this.m_nDrgState = 3;
                } else if (this.m_nDrgState != 3) {
                    this.m_nDrgState = 11;
                }
            } else if (X - this.m_PrevX != 0) {
                if (X - this.m_PrevX > 0) {
                    if (this.m_nDrgState == 1) {
                        this.m_nDrgState = 4;
                    } else if (this.m_nDrgState != 4) {
                        this.m_nDrgState = 11;
                    }
                } else if (this.m_nDrgState == 1) {
                    this.m_nDrgState = 5;
                } else if (this.m_nDrgState != 5) {
                    this.m_nDrgState = 11;
                }
            }
        }
    }

    public boolean getDraged() {
        return this.mDraged;
    }

    public int getdragState() {
        return mDragState;
    }

    /* loaded from: classes.dex */
    public class EDIC_DRG_STATE {
        public static final int EDIC_DGR_LONG = 6;
        public static final int EDIC_DRG_CLICK = 1;
        public static final int EDIC_DRG_DRAG = 11;
        public static final int EDIC_DRG_DRAG_D = 8;
        public static final int EDIC_DRG_DRAG_L = 10;
        public static final int EDIC_DRG_DRAG_R = 9;
        public static final int EDIC_DRG_DRAG_U = 7;
        public static final int EDIC_DRG_FLICK_D = 3;
        public static final int EDIC_DRG_FLICK_L = 5;
        public static final int EDIC_DRG_FLICK_R = 4;
        public static final int EDIC_DRG_FLICK_U = 2;
        public static final int EDIC_DRG_UNKNOWN = 0;

        public EDIC_DRG_STATE() {
        }
    }
}
