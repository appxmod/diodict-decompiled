package com.nemustech.tiffany.world;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes.dex */
public class TFEventHandler {
    private static final String TAG = "TFEventHandler";
    private TFRenderer mRenderer;
    public View.OnTouchListener mTouchListener = new View.OnTouchListener() { // from class: com.nemustech.tiffany.world.TFEventHandler.1
        float mCurX;
        float mCurY;
        long mCurrTick;
        float mOldX;
        float mOldY;

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (TFEventHandler.this.mWorld.mUserTouchListener == null || !TFEventHandler.this.mWorld.mUserTouchListener.onTouch(v, event)) {
                if (TFEventHandler.this.mWorld.isLocked()) {
                    return false;
                }
                int action = event.getAction();
                switch (action) {
                    case 0:
                        this.mCurrTick = SystemClock.uptimeMillis();
                        this.mOldX = event.getX();
                        this.mOldY = event.getY();
                        TFEventHandler.this.mRenderer.handleDown(this.mOldX, this.mOldY, this.mCurrTick);
                        break;
                    case 1:
                        this.mCurX = event.getX();
                        this.mCurY = event.getY();
                        TFEventHandler.this.mRenderer.handleUp(this.mCurX, this.mCurY);
                        this.mOldX = this.mCurX;
                        this.mOldY = this.mCurY;
                        break;
                    case 2:
                        this.mCurrTick = SystemClock.uptimeMillis();
                        this.mCurX = event.getX();
                        this.mCurY = event.getY();
                        TFEventHandler.this.mRenderer.handleDrag(this.mOldX, this.mOldY, this.mCurX, this.mCurY, this.mCurrTick);
                        this.mOldX = this.mCurX;
                        this.mOldY = this.mCurY;
                        break;
                }
                return true;
            }
            return true;
        }
    };
    private TFWorld mWorld;

    public TFEventHandler(TFWorld world, TFRenderer renderer) {
        this.mWorld = world;
        this.mRenderer = renderer;
    }
}
