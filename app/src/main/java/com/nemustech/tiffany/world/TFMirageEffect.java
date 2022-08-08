package com.nemustech.tiffany.world;

import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public class TFMirageEffect extends TFTwistEffect {
    protected Interpolator mTwistScaleInterpolator = null;
    protected float mXOffset = 0.5f;
    protected float mZOffset = 0.5f;

    public TFMirageEffect(TFCustomPanel customPanel) {
        super(customPanel);
    }

    @Override // com.nemustech.tiffany.world.TFTwistEffect
    protected void rotateRow(float[] vertex, int rowIndex, float rowTwist) {
        float twist;
        float scale;
        if (this.mTwistInterpolator == null) {
            twist = rowTwist;
        } else {
            twist = this.mTwistInterpolator.getInterpolation(rowTwist);
        }
        if (this.mTwistScaleInterpolator == null) {
            scale = 1.0f - rowTwist;
        } else {
            scale = 1.0f - this.mTwistScaleInterpolator.getInterpolation(rowTwist);
        }
        float xOffset = this.mXOffset;
        float zOffset = this.mZOffset;
        float theta = ((this.mTargetDegree * twist) * 3.1415927f) / 180.0f;
        float cos = (float) Math.cos(theta);
        float sin = (float) Math.sin(theta);
        for (int i = 0; i <= this.mMeshW; i++) {
            float dx = this.mIdentity[rowIndex + 0] - xOffset;
            float dz = this.mIdentity[rowIndex + 2] - zOffset;
            vertex[rowIndex + 0] = (((dx * cos) - (dz * sin)) * scale) + xOffset;
            vertex[rowIndex + 2] = (((dx * sin) + (dz * cos)) * scale) + zOffset;
            rowIndex += 3;
        }
    }

    public void setTwistScaleInterpolator(Interpolator scale) {
        this.mTwistScaleInterpolator = scale;
    }

    public Interpolator getTwistScaleInterpolator() {
        return this.mTwistScaleInterpolator;
    }

    public void setXOffset(float xOffset) {
        this.mXOffset = xOffset;
    }

    public float getXOffset() {
        return this.mXOffset;
    }

    public void setZOffset(float zOffset) {
        this.mZOffset = zOffset;
    }

    public float getZOffset() {
        return this.mZOffset;
    }
}
