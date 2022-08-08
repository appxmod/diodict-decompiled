package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFHorizontalAxisHolder extends TFAxisHolder {
    private static final String TAG = "TFHorizontalAxisHolder";

    public TFHorizontalAxisHolder(float radius, float headAngle) {
        super(radius);
        if (headAngle < 90.0f || headAngle > 270.0f) {
            this.mHeadAngle = TFUtils.filterAngle(headAngle);
            this.mBorderAngle = TFUtils.filterAngle(this.mHeadAngle - 180.0f);
            this.mClockwise = true;
            return;
        }
        this.mHeadAngle = TFUtils.filterAngle(headAngle + 180.0f);
        this.mBorderAngle = TFUtils.filterAngle(this.mHeadAngle + 180.0f);
        this.mClockwise = false;
    }

    public TFHorizontalAxisHolder() {
        this(0.0f, 0.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        return 0;
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected void locateObject(TFObject model, float modelPosition, int modelIndex) {
        int i = -1;
        float y = ((this.mClockwise ? 1 : -1) * (model.mHeight + this.mRadius)) / 2.0f;
        model.locate(0.0f, y, 0.0f);
        if (!this.mClockwise) {
            i = 1;
        }
        float y2 = (i * getTargetDegree(modelPosition)) + this.mHeadAngle;
        model.look(0.0f, y2);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return (this.mClockwise ? -1 : 1) * (forceVector[5] - forceVector[1]);
    }
}
