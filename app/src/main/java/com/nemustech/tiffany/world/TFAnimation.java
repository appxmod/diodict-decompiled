package com.nemustech.tiffany.world;

import android.util.Log;

/* loaded from: classes.dex */
public abstract class TFAnimation {
    public static final int RESET = 0;
    public static final int START = 1;
    public static final int STOP = 2;
    private static final String TAG = "TFAnimation";
    protected TFObject mActiveSubject;
    private AnimationEventListener mAnimationEventListener;
    protected int mDuration;
    protected int mElapse;
    protected int mState;
    protected TFObject mSubject;
    protected int mTicks;
    protected int mTime;

    /* loaded from: classes.dex */
    public interface AnimationEventListener {
        void onAnimationEnd(TFAnimation tFAnimation);

        void onAnimationStart(TFAnimation tFAnimation);
    }

    public abstract void OnFrame();

    public abstract boolean OnStart();

    public abstract boolean OnStop();

    public TFAnimation() {
        reset();
    }

    public TFObject getSubject() {
        return this.mSubject;
    }

    public void setDuration(int duration) {
        if (this.mState != 0) {
            Log.d(TAG, "setDuration: invalid state");
        } else {
            this.mDuration = duration;
        }
    }

    public int getDuration() {
        return this.mDuration;
    }

    public int getTime() {
        return this.mTime > this.mDuration ? this.mDuration : this.mTime;
    }

    public float getT() {
        float t = this.mTime / this.mDuration;
        if (t > 1.0f) {
            return 1.0f;
        }
        return t;
    }

    public void setT(float t) {
        this.mTime = (int) (this.mDuration * t);
        this.mTicks = 0;
    }

    public void start() {
        if (this.mState != 0 && this.mState != 2) {
            Log.d(TAG, "start: invalid state");
            return;
        }
        this.mState = 1;
        this.mTime = 0;
        this.mTicks = 0;
        if (OnStart() && this.mAnimationEventListener != null) {
            this.mAnimationEventListener.onAnimationStart(this);
        }
    }

    public void stop() {
        if (this.mState != 2 && this.mState != 0) {
            this.mState = 2;
            if (OnStop() && this.mAnimationEventListener != null) {
                this.mAnimationEventListener.onAnimationEnd(this);
            }
        }
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean tryStop) {
        if (tryStop && this.mState == 1) {
            stop();
        }
        this.mState = 0;
        this.mDuration = 0;
        this.mTime = 0;
        this.mElapse = 30;
        this.mTicks = 0;
    }

    public boolean update(int tickPassed) {
        if (!hasStarted()) {
            return false;
        }
        if (updateTime(tickPassed)) {
            if (this.mTime >= this.mDuration) {
                stop();
                return false;
            }
            OnFrame();
        }
        return true;
    }

    public boolean updateTime(int tickPassed) {
        if (this.mState != 1) {
            return false;
        }
        this.mTicks += tickPassed;
        if (this.mTicks < this.mElapse) {
            return false;
        }
        this.mTicks -= this.mElapse;
        this.mTime += this.mElapse;
        return true;
    }

    public boolean hasStarted() {
        return this.mState == 1;
    }

    public boolean hasEnded() {
        return this.mState == 2;
    }

    public void setAnimationEventListener(AnimationEventListener animationListener) {
        this.mAnimationEventListener = animationListener;
    }

    public AnimationEventListener getAnimationEventListener() {
        return this.mAnimationEventListener;
    }
}
