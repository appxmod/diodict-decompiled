package com.nemustech.tiffany.world;

import android.view.animation.Interpolator;
import com.nemustech.tiffany.world.TFCustomPanel;

/* loaded from: classes.dex */
public class TFTwistEffect extends TFCustomPanel.Blender {
    protected final TFCustomPanel mCustomPanel;
    protected int mDuration;
    protected final float[] mIdentity;
    protected final int mMeshH;
    protected final int mMeshW;
    protected float mStartDegree;
    protected float mTargetDegree;
    protected int mTwistDuration;
    protected final TFCustomPanel.Time mTime = new TFCustomPanel.Time();
    protected Interpolator mTwistInterpolator = null;

    public TFTwistEffect(TFCustomPanel customPanel) {
        this.mCustomPanel = customPanel;
        this.mMeshW = customPanel.getMeshWidth();
        this.mMeshH = customPanel.getMeshHeight();
        this.mIdentity = new float[(this.mMeshH + 1) * (this.mMeshW + 1) * 3];
        this.mCustomPanel.loadIdentityVertex(this.mIdentity);
        setDuration(1000, 0);
        setStartDegree(0.0f);
        setTargetDegree(180.0f);
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onStart() {
        this.mTime.start(this.mDuration, 50);
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onFrame(int tick) {
        if (this.mTime.update(tick)) {
            float[] vertex = this.mCustomPanel.getVertex();
            blend(vertex);
            this.mCustomPanel.requestUpdateVertex();
        }
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onEnd() {
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public boolean hasEnded() {
        return this.mTime.hasEnded();
    }

    protected void blend(float[] vertex) {
        int ticks = this.mTime.getTicks();
        for (int j = 0; j <= this.mMeshH; j++) {
            int rowIndex = (this.mMeshW + 1) * j * 3;
            float rowTwist = getRowTwist(j, ticks);
            rotateRow(vertex, rowIndex, rowTwist);
        }
    }

    protected float getRowTwist(int row, int ticks) {
        int rowWait = ((this.mDuration - this.mTwistDuration) * row) / this.mMeshH;
        if (ticks < rowWait) {
            return 0.0f;
        }
        if (ticks >= this.mTwistDuration + rowWait) {
            return 1.0f;
        }
        float rowTwist = (ticks - rowWait) / this.mTwistDuration;
        return rowTwist;
    }

    protected void rotateRow(float[] vertex, int rowIndex, float rowTwist) {
        float twist;
        if (this.mTwistInterpolator == null) {
            twist = rowTwist;
        } else {
            twist = this.mTwistInterpolator.getInterpolation(rowTwist);
        }
        float theta = ((this.mStartDegree + ((this.mTargetDegree - this.mStartDegree) * twist)) * 3.1415927f) / 180.0f;
        float cos = (float) Math.cos(theta);
        float sin = (float) Math.sin(theta);
        for (int i = 0; i <= this.mMeshW; i++) {
            float dx = this.mIdentity[rowIndex + 0] - 0.5f;
            float dz = this.mIdentity[rowIndex + 2];
            vertex[rowIndex + 0] = ((dx * cos) - (dz * sin)) + 0.5f;
            vertex[rowIndex + 2] = (dx * sin) + (dz * cos);
            rowIndex += 3;
        }
    }

    public void setDuration(int duration, int twistDuration) {
        if (twistDuration <= 0 || twistDuration >= duration) {
            twistDuration = (duration * 3) / 4;
        }
        this.mDuration = duration;
        this.mTwistDuration = twistDuration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public int getTwistDuration() {
        return this.mTwistDuration;
    }

    public void setTargetDegree(float targetDegree) {
        this.mTargetDegree = targetDegree;
    }

    public float getTargetDegree() {
        return this.mTargetDegree;
    }

    public void setStartDegree(float startDegree) {
        this.mStartDegree = startDegree;
    }

    public float getStartDegree() {
        return this.mStartDegree;
    }

    public void setTwistInterpolator(Interpolator twist) {
        this.mTwistInterpolator = twist;
    }

    public Interpolator getTwistInterpolator() {
        return this.mTwistInterpolator;
    }
}
