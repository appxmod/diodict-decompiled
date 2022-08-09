package com.diotek.diodict.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.mean.MSG;
import com.nemustech.tiffany.world.TFEffect;

/* loaded from: classes.dex */
public class TiffanyTransition {
    public static final int ASKEW = 5;
    public static final int CENTER_ZOOM = 18;
    public static final int COLUMN = 6;
    public static final int CUBE = 9;
    public static final int DIAGONAL_FLING = 14;
    public static final int DIAGONAL_SCALE = 15;
    public static final int ENTRANCE = 4;
    public static final int FLIP = 0;
    public static final int GENIE = 20;
    public static final int LINE_ZOOM = 19;
    public static final int MOSAIC = 1;
    public static final int PAGEOVER_BT = 6;
    public static final int PAGEOVER_BY_ANGLE = 11;
    public static final int PAGEOVER_LBRT = 5;
    public static final int PAGEOVER_LR = 4;
    public static final int PAGEOVER_LTRB = 3;
    public static final int PAGEOVER_NONE = -1;
    public static final int PAGEOVER_RBLT = 7;
    public static final int PAGEOVER_RL = 0;
    public static final int PAGEOVER_RTLB = 1;
    public static final int PAGEOVER_TB = 2;
    public static final int PAGE_CURL = 21;
    public static final int PROJECTOR_BOARD_DOWN = 17;
    public static final int PROJECTOR_BOARD_UP = 16;
    public static final int REPLACE = 2;
    public static final int REVOLVING = 3;
    public static final int ROW = 7;
    public static final int SINK = 12;
    public static final int STICKER = 13;
    public static final int TWIST = 8;
    public static final int VPAGEOVER = 10;
    private Bitmap mBitmapListView;
    private Context mContext;
    private TFEffect mEffect;
    private int mEffectID;
    private View mFromView;
    private View mRootView;
    private View mToView;
    private int[] mPageOverDegree = {0, 60, 90, DHWR.DLANG_HINDI, 180, 240, 270, DictUtils.DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE};
    private int mDuration = 500;

    public TiffanyTransition(Context ctx) {
        this.mContext = ctx;
        this.mEffect = new TFEffect(this.mContext);
    }

    public void setRootView(View root) {
        this.mRootView = root;
        this.mRootView.setDrawingCacheEnabled(true);
    }

    public void setTransFromView(View from) {
        this.mFromView = from;
        this.mFromView.setDrawingCacheEnabled(true);
    }

    public void setTransToView(View to) {
        this.mToView = to;
        this.mToView.setDrawingCacheEnabled(true);
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void transition(int effectId, int mode) {
        this.mEffect.init();
        if (this.mRootView != null) {
            this.mBitmapListView = this.mRootView.getDrawingCache();
        }
        this.mEffectID = effectId;
        int defaultDuration = this.mDuration;
        switch (this.mEffectID) {
            case 8:
                TFEffect.TwistParam param = new TFEffect.TwistParam(1000, 900, 0.0f, 180.0f, new OvershootInterpolator());
                this.mEffect.setTwistParam(param);
                break;
            case 11:
                this.mEffect.setEffectParam(this.mPageOverDegree[mode], defaultDuration);
                break;
            case 13:
                if (this.mToView != null) {
                    this.mToView.setVisibility(View.INVISIBLE);
                }
                this.mEffect.addView(this.mToView, 0);
                this.mEffect.setEffectParam(DictUtils.DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE, defaultDuration);
                this.mEffect.showEffect(this.mEffectID, true);
                System.gc();
                return;
            case 21:
                this.mEffect.setEffectParam(330, 1000, new MyBounceInterpolator());
                break;
        }
        if (this.mFromView != null) {
            if (this.mToView != null) {
                if (this.mBitmapListView != null) {
                    this.mEffect.addView(this.mFromView, this.mBitmapListView, 0);
                    this.mEffect.addView(this.mToView, 1);
                    this.mEffect.showEffect(this.mEffectID, false);
                    return;
                }
                MSG.l(2, "Tiffany mBitmapListView is null");
                return;
            }
            MSG.l(2, "Tiffany mToView is null");
            return;
        }
        MSG.l(2, "Tiffany mFromView is null");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MyBounceInterpolator extends BounceInterpolator {
        MyBounceInterpolator() {
        }

        @Override // android.view.animation.BounceInterpolator, android.animation.TimeInterpolator
        public float getInterpolation(float t) {
            return super.getInterpolation((0.7408f * t) / 1.1226f);
        }
    }

    public void onDestroy() {
        if (this.mEffect != null) {
            this.mEffect.init();
            this.mEffect = null;
        }
        if (this.mRootView != null) {
            this.mRootView.setDrawingCacheEnabled(false);
            this.mRootView = null;
        }
        if (this.mFromView != null) {
            this.mFromView.setDrawingCacheEnabled(false);
            this.mFromView = null;
        }
        if (this.mToView != null) {
            this.mToView.setDrawingCacheEnabled(false);
            this.mToView = null;
        }
    }
}
