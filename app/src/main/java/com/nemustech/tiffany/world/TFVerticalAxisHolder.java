package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFVerticalAxisHolder extends TFAxisHolder {
    private static final String TAG = "TFVerticalAxisHolder";

    public TFVerticalAxisHolder(float radius, float headAngle) {
        super(radius);
        if (headAngle > 180.0f) {
            this.mHeadAngle = TFUtils.filterAngle(headAngle + 90.0f);
            this.mBorderAngle = TFUtils.filterAngle(this.mHeadAngle - 180.0f);
            this.mClockwise = true;
            return;
        }
        this.mHeadAngle = TFUtils.filterAngle(headAngle - 90.0f);
        this.mBorderAngle = TFUtils.filterAngle(this.mHeadAngle + 180.0f);
        this.mClockwise = false;
    }

    public TFVerticalAxisHolder() {
        this(0.0f, 0.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        return 0;
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected void locateObject(TFObject model, float modelPosition, int modelIndex) {
        int i = -1;
        float x = ((this.mClockwise ? -1 : 1) * (model.mWidth + this.mRadius)) / 2.0f;
        float y = model.mHeight / 2.0f;
        if (this.mPitchAndRollAni != null && !this.mPitchAndRollAni.hasEnded()) {
            y += 0.1f * this.mPitchAndRollAni.getOffset(modelPosition);
        }
        model.locate(x, y, 0.0f);
        if (!this.mClockwise) {
            i = 1;
        }
        float x2 = (i * getTargetDegree(modelPosition)) + this.mHeadAngle;
        model.look(x2, 0.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return (this.mClockwise ? 1 : -1) * (forceVector[4] - forceVector[0]);
    }
}
