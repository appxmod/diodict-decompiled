package com.nemustech.tiffany.world;

import android.content.Context;
import android.view.View;

/* loaded from: classes.dex */
public class TFOverlayWorld extends TFWorld {
    protected TFOverlayWindow mOverlayWindow;

    public TFOverlayWorld(float width, float height, float depth, TFOverlayWindow ow) {
        super(width, height, depth);
        this.mOverlayWindow = ow;
        setBackgroundColor(0.0f, 0.0f, 0.0f, 0.0f);
        setTranslucentMode(true);
    }

    public TFOverlayWindow getWindow() {
        return this.mOverlayWindow;
    }

    @Override // com.nemustech.tiffany.world.TFWorld
    public void stop() {
        super.stop();
        if (this.mOverlayWindow != null) {
            this.mOverlayWindow.stop();
        }
    }

    @Override // com.nemustech.tiffany.world.TFWorld
    public TFError show(View v) {
        if (this.mOverlayWindow != null) {
            this.mOverlayWindow.getEffectDecor().addView(v);
            this.mOverlayWindow.show();
        }
        return super.show(v);
    }

    @Override // com.nemustech.tiffany.world.TFWorld
    public void toWorldCoord(int[] uiCoord, float[] worldCoord, int offsetX, int offsetY) {
        super.toWorldCoord(uiCoord, worldCoord, offsetX + this.mOverlayWindow.getX(), offsetY + this.mOverlayWindow.getY());
    }

    public TFView createView(Context context) {
        return new TFView(context);
    }
}
