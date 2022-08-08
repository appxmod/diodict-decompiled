package com.nemustech.tiffany.world;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/* loaded from: classes.dex */
public class TFAnimationSet {
    public static final int TYPE_CENTER_ZOOM = 5;
    public static final int TYPE_DIAGONAL_FLING = 1;
    public static final int TYPE_DIAGONAL_SCALE = 2;
    public static final int TYPE_LINE_ZOOM_SCALE = 7;
    public static final int TYPE_LINE_ZOOM_TRANSLATE = 6;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_PROJECTOR_BOARD_DOWN = 4;
    public static final int TYPE_PROJECTOR_BOARD_UP = 3;

    /* JADX WARN: Multi-variable type inference failed */
    public Animation getAnimation(int type, boolean isInAnimation) {
        Animation resultAnimation;
        Animation resultAnimation2;
        switch (type) {
            case 1:
                if (isInAnimation) {
                    resultAnimation2 = new AlphaAnimation(0.0f, 1.0f);
                } else {
                    AnimationSet as = new AnimationSet(true);
                    ScaleAnimation sa = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 0, 0.5f, 0, 0.5f);
                    sa.setInterpolator(new AccelerateInterpolator());
                    sa.setFillAfter(false);
                    RotateAnimation ra = new RotateAnimation(0.0f, 90.0f, 1, 1.0f, 1, 1.0f);
                    AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
                    as.addAnimation(sa);
                    as.addAnimation(ra);
                    as.addAnimation(aa);
                    resultAnimation2 = as;
                }
                resultAnimation2.setDuration(1000L);
                return resultAnimation2;
            case 2:
                if (isInAnimation) {
                    resultAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 1.0f, 1, 1.0f);
                    resultAnimation.setInterpolator(new AccelerateInterpolator());
                    resultAnimation.setFillAfter(false);
                } else {
                    resultAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 1, 0.0f, 1, 0.0f);
                    resultAnimation.setInterpolator(new AccelerateInterpolator());
                    resultAnimation.setFillAfter(false);
                }
                resultAnimation.setDuration(1000L);
                return resultAnimation;
            case 3:
                if (isInAnimation) {
                    return null;
                }
                Animation resultAnimation3 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f, 1, 0.0f, 1, 0.0f);
                resultAnimation3.setInterpolator(new AnticipateOvershootInterpolator());
                resultAnimation3.setFillAfter(false);
                resultAnimation3.setDuration(2000L);
                return resultAnimation3;
            case 4:
                if (isInAnimation) {
                    return null;
                }
                Animation resultAnimation4 = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, 1, 0.0f, 1, 0.0f);
                resultAnimation4.setInterpolator(new BounceInterpolator());
                resultAnimation4.setFillAfter(false);
                resultAnimation4.setDuration(2000L);
                return resultAnimation4;
            case 5:
                if (!isInAnimation) {
                    return null;
                }
                Animation resultAnimation5 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
                resultAnimation5.setInterpolator(new AccelerateDecelerateInterpolator());
                resultAnimation5.setFillAfter(false);
                resultAnimation5.setDuration(1000L);
                return resultAnimation5;
            case 6:
                if (!isInAnimation) {
                    return null;
                }
                AnimationSet as2 = new AnimationSet(true);
                ScaleAnimation sa2 = new ScaleAnimation(1.0f, 1.0f, 0.005f, 0.005f);
                TranslateAnimation ta = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.4975f, 1, 0.4975f);
                ta.setDuration(1000L);
                as2.addAnimation(sa2);
                as2.addAnimation(ta);
                return as2;
            case 7:
                if (!isInAnimation) {
                    return null;
                }
                Animation resultAnimation6 = new ScaleAnimation(1.0f, 1.0f, 0.005f, 1.0f, 1, 0.5f, 1, 0.5f);
                resultAnimation6.setDuration(1000L);
                return resultAnimation6;
            default:
                return null;
        }
    }
}
