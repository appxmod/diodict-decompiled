package com.diotek.diodict.anim;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import com.diotek.diodict.mean.MSG;

/* loaded from: classes.dex */
public class LayoutTransition {
    private static final int MEANVIEW_SLIDING_ANIMATION_DURATION = 800;
    private static final float MEANVIEW_SLIDING_ANIMATION_WEIGHT_MAX = 500.0f;
    private static final float MEANVIEW_SLIDING_ANIMATION_WEIGHT_MIN = 1.13f;
    private static final float MEANVIEW_SLIDING_ANIMATION_WEIGHT_MIN_LAND = 1.5f;
    public static final int MSG_ANIMATION_END = 1000;
    public static final int MSG_ANIMATION_START = 1001;
    private static AnimationCallback mAnimationEndCallback;
    private static AnimationCallback mAnimationStartCallback;
    private static int mDirection;
    public static OnFlashcardPopupLayoutGone stikerCallback;
    public static int DIRECT_LEFT_TO_RIGHT = 0;
    public static int DIRECT_RIGHT_TO_LEFT = 1;
    private static ViewGroup mAnimationPanel = null;
    private static boolean isPanelDissmiss = false;
    private static boolean misEnter = false;
    public static boolean mStiker = false;
    private static Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() { // from class: com.diotek.diodict.anim.LayoutTransition.1
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            boolean z = true;
            if (LayoutTransition.mAnimationPanel != null && !LayoutTransition.misEnter) {
                if (LayoutTransition.isPanelDissmiss) {
                    LayoutTransition.mAnimationPanel.setVisibility(8);
                }
                ViewGroup unused = LayoutTransition.mAnimationPanel = null;
                if (LayoutTransition.mStiker && LayoutTransition.stikerCallback != null) {
                    LayoutTransition.stikerCallback.runTiffanuStiker();
                }
            }
            if (LayoutTransition.mAnimationEndCallback != null) {
                AnimationCallback animationCallback = LayoutTransition.mAnimationEndCallback;
                if (LayoutTransition.mDirection != LayoutTransition.DIRECT_RIGHT_TO_LEFT) {
                    z = false;
                }
                animationCallback.run(z);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    };

    /* loaded from: classes.dex */
    public interface AnimationCallback {
        void run(boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnFlashcardPopupLayoutGone {
        void runTiffanuStiker();
    }

    public void setStikerCallback(OnFlashcardPopupLayoutGone callback) {
        stikerCallback = callback;
    }

    public static void trasition(ViewGroup panel, boolean isEnter, int direct, int duration, boolean stiker, boolean isDissmiss) {
        float fromX;
        float toX;
        mStiker = stiker;
        if (panel == null) {
            MSG.l(2, " " + LayoutTransition.class + " pannel error");
            return;
        }
        mAnimationPanel = panel;
        isPanelDissmiss = isDissmiss;
        misEnter = isEnter;
        AnimationSet set = new AnimationSet(true);
        if (isEnter) {
            Animation animation = new AlphaAnimation(0.5f, 1.0f);
            animation.setDuration(duration);
            set.addAnimation(animation);
        }
        if (direct == DIRECT_LEFT_TO_RIGHT) {
            if (isEnter) {
                fromX = -0.6f;
                toX = 0.0f;
            } else {
                fromX = 0.0f;
                toX = -1.0f;
            }
        } else if (isEnter) {
            fromX = 1.6f;
            toX = 0.0f;
        } else {
            fromX = 0.0f;
            toX = 1.6f;
        }
        Animation animation2 = new TranslateAnimation(2, fromX, 2, toX, 2, 0.0f, 2, 0.0f);
        animation2.setDuration(duration);
        animation2.setAnimationListener(mAnimationListener);
        set.addAnimation(animation2);
        panel.startAnimation(set);
    }

    private static void setMainRightLayoutSlideAnimation(ViewGroup panel, int direct) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(800L);
        set.addAnimation(animation);
        float fromX = 0.01f;
        float toX = 0.0f;
        if (direct == DIRECT_LEFT_TO_RIGHT) {
            fromX = -0.01f;
            toX = 0.0f;
        }
        Animation animation2 = new TranslateAnimation(2, fromX, 2, toX, 2, 0.0f, 2, 0.0f);
        animation2.setDuration(800L);
        animation2.setAnimationListener(mAnimationListener);
        set.addAnimation(animation2);
        panel.startAnimation(set);
    }

    public static void updateLayout(boolean isAuto, LinearLayout standardLayout, LinearLayout layout, Context context) {
        LinearLayout.LayoutParams paramsInnerLeft = (LinearLayout.LayoutParams) standardLayout.getLayoutParams();
        if (paramsInnerLeft.weight == MEANVIEW_SLIDING_ANIMATION_WEIGHT_MAX) {
            decreaseLayout(paramsInnerLeft, layout, getMinMeaningWeight(context));
        } else if (isAuto) {
            increaseLayout(paramsInnerLeft, layout);
        }
    }

    public static void updateLayoutWithExtends(boolean isExtends, LinearLayout standardLayout, LinearLayout layout, AnimationCallback animationStartCallback, AnimationCallback animationEndCallback, Context context) {
        mAnimationStartCallback = animationStartCallback;
        mAnimationEndCallback = animationEndCallback;
        LinearLayout.LayoutParams paramsInnerLeft = (LinearLayout.LayoutParams) standardLayout.getLayoutParams();
        if (isExtends) {
            if (paramsInnerLeft.weight != MEANVIEW_SLIDING_ANIMATION_WEIGHT_MAX) {
                increaseLayout(paramsInnerLeft, layout);
                return;
            }
            return;
        }
        float minWeight = getMinMeaningWeight(context);
        if (paramsInnerLeft.weight != minWeight) {
            decreaseLayout(paramsInnerLeft, layout, minWeight);
        }
    }

    public static void setCallback(AnimationCallback animationStartCallback, AnimationCallback animationEndCallback) {
        mAnimationStartCallback = animationStartCallback;
        mAnimationEndCallback = animationEndCallback;
    }

    private static void increaseLayout(LinearLayout.LayoutParams paramsInnerLeft, LinearLayout layout) {
        if (mAnimationStartCallback != null) {
            mAnimationStartCallback.run(true);
        }
        mDirection = DIRECT_RIGHT_TO_LEFT;
        paramsInnerLeft.weight = MEANVIEW_SLIDING_ANIMATION_WEIGHT_MAX;
        layout.requestLayout();
        setMainRightLayoutSlideAnimation(layout, DIRECT_RIGHT_TO_LEFT);
    }

    private static void decreaseLayout(LinearLayout.LayoutParams paramsInnerLeft, LinearLayout layout, float minWeight) {
        if (mAnimationStartCallback != null) {
            mAnimationStartCallback.run(false);
        }
        mDirection = DIRECT_LEFT_TO_RIGHT;
        setMainRightLayoutSlideAnimation(layout, DIRECT_LEFT_TO_RIGHT);
        paramsInnerLeft.weight = minWeight;
        layout.requestLayout();
    }

    public static float getMinMeaningWeight(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == 1) {
            return MEANVIEW_SLIDING_ANIMATION_WEIGHT_MIN;
        }
        return 1.5f;
    }

    public static void onDestroy() {
        if (mAnimationStartCallback != null) {
            mAnimationStartCallback = null;
        }
        if (mAnimationEndCallback != null) {
            mAnimationEndCallback = null;
        }
        System.gc();
    }
}
