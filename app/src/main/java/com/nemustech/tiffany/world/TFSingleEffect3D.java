package com.nemustech.tiffany.world;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.nemustech.tiffany.world.TFEffect;
import com.nemustech.tiffany.world.TFWorld;

/* loaded from: classes.dex */
public abstract class TFSingleEffect3D {
    public static final int INITIAL = 0;
    public static final int PREPARED = 1;
    public static final int RUNNING = 2;
    public static final int STOPPING = 3;
    private static final String TAG = "TFSingleEffect3D";
    protected TFEffect.AnimationEventListener mAnimationEventListener;
    protected Context mContext;
    public TFWorld.OnEffectFinishListener mEffectFinish;
    protected Handler mHandler;
    protected int mState;
    protected Bitmap[] mTextureBitmap;
    protected View[] mView;
    protected TFWorld mWorld;

    public abstract void OnFinishEffect();

    public abstract void OnPrepareEffect();

    public abstract void OnStartEffect();

    public abstract void OnStopEffect();

    public TFSingleEffect3D(Context context, TFWorld world) {
        this.mEffectFinish = new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFSingleEffect3D.1
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                Log.d(TFSingleEffect3D.TAG, "EffectFinish");
                TFSingleEffect3D.this.mState = 0;
                TFSingleEffect3D.this.OnFinishEffect();
            }
        };
        this.mView = new View[2];
        this.mTextureBitmap = new Bitmap[2];
        this.mContext = context;
        this.mWorld = world;
        this.mHandler = new Handler();
        this.mState = 0;
    }

    public TFSingleEffect3D(Context context) {
        this(context, null);
    }

    public void pause() {
        Log.i(TAG, "pause");
        this.mWorld.pause();
    }

    public void resume() {
        Log.i(TAG, "resume");
        this.mWorld.resume();
    }

    public void stop() {
        Log.i(TAG, "stop");
        this.mWorld.stop();
    }

    public boolean isActive() {
        int state = getState();
        return state == 2 || state == 3;
    }

    public void addView(int index, View view) {
        addView(index, view, null);
    }

    public void addView(int index, View view, Bitmap bitmap) {
        expectState(0);
        if (index >= 0 && index <= 1) {
            this.mView[index] = view;
            this.mTextureBitmap[index] = bitmap;
        }
    }

    public void showEffect() {
        expectState(0);
        prepareEffect();
        startEffect();
    }

    public int getState() {
        return this.mState;
    }

    public boolean expectState(int state) {
        return expectState(state, true);
    }

    public boolean expectState(int state, boolean exception) {
        int s = getState();
        if (s != state) {
            if (exception) {
                throw new IllegalStateException("Expected: " + state + " Current: " + s);
            }
            return false;
        }
        return true;
    }

    public void prepareEffect() {
        expectState(0);
        this.mState = 1;
        OnPrepareEffect();
    }

    public void startEffect() {
        expectState(1);
        this.mState = 2;
        OnStartEffect();
    }

    public void stopEffect() {
        expectState(2);
        this.mState = 3;
        OnStopEffect();
    }

    public void prepareTextureBitmap(int index) {
        if (this.mView[index] != null && this.mTextureBitmap[index] == null) {
            this.mTextureBitmap[index] = getViewBitmap(this.mView[index]);
        }
    }

    public void layoutModelOverView(TFModel m, View v) {
        v.getLocationInWindow(uiCoord);
        int[] uiCoord = {0, 0, v.getWidth(), v.getHeight()};
        float[] worldCoord = new float[4];
        this.mWorld.toWorldCoord(uiCoord, worldCoord);
        if (m instanceof TFPanel) {
            ((TFPanel) m).setSize(worldCoord[2], worldCoord[3]);
        } else if (m instanceof TFPagePanel) {
            ((TFPagePanel) m).setSize(worldCoord[2], worldCoord[3]);
        } else {
            Log.e(TAG, "UNKNOWN MODEL");
        }
        m.locate(worldCoord[0], worldCoord[1], 0.0f);
    }

    public void clearViews() {
        this.mView[0] = null;
        this.mView[1] = null;
        this.mTextureBitmap[0] = null;
        this.mTextureBitmap[1] = null;
    }

    public TFWorld getWorld() {
        return this.mWorld;
    }

    public void setWorld(TFWorld world) {
        this.mWorld = world;
    }

    public static Bitmap getViewBitmap(View v) {
        boolean enabled = v.isDrawingCacheEnabled();
        if (!enabled) {
            v.setDrawingCacheEnabled(true);
        }
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        if (!enabled) {
            v.setDrawingCacheEnabled(false);
        }
        return bitmap;
    }
}
