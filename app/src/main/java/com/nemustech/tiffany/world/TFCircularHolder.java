package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFCircularHolder extends TFSimpleHolder {
    private static final String TAG = "TFCircularHolder";
    protected float mHeadRadius;
    protected PitchAndRollAnimation mPitchAndRollAni;
    protected float mRadius;
    protected RadiusAnimation mRadiusAni;

    public TFCircularHolder(float radius) {
        this.mRadius = radius;
    }

    public TFCircularHolder() {
        this(1.0f);
    }

    public TFCircularHolder(float radius, boolean isTerminalHolder) {
        super(isTerminalHolder);
        this.mRadius = radius;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        return (slotCount - 1) / 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onAddModel(TFObject object, int modelIndex) {
        object.mShouldRotateFirst = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onRemoveModel(TFObject object, int modelIndex) {
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected float getModelPosition(int modelIndex) {
        float position = (modelIndex - getHeadSlotIndex()) / getSlotCount();
        return position;
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected void locateObject(TFObject model, float modelPosition, int modelIndex) {
        float y = model.mHeight / 2.0f;
        float z = this.mRadius;
        if (this.mPitchAndRollAni != null && !this.mPitchAndRollAni.hasEnded()) {
            y += 0.1f * this.mPitchAndRollAni.getOffset(modelPosition);
        }
        if (this.mHeadRadius > this.mRadius && modelIndex == getHeadModelIndex()) {
            float t = getDeviation() * 2.0f;
            if (t < 0.0f) {
                t = -t;
            }
            z = ((1.0f - t) * this.mHeadRadius) + (this.mRadius * t);
        }
        model.locate(0.0f, y, z);
        float x = modelPosition * 360.0f;
        model.look(x, 0.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return -(forceVector[4] - forceVector[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean isInEffectAnimation() {
        if (super.isInEffectAnimation()) {
            return true;
        }
        if (this.mRadiusAni != null && !this.mRadiusAni.hasEnded()) {
            return true;
        }
        return this.mPitchAndRollAni != null && !this.mPitchAndRollAni.hasEnded();
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder, com.nemustech.tiffany.world.TFHolder
    public boolean updateLayout(int tickPassed) {
        boolean updated = super.updateLayout(tickPassed);
        if (this.mRadiusAni != null && this.mRadiusAni.update(tickPassed)) {
            updated = true;
        }
        if (this.mPitchAndRollAni != null && this.mPitchAndRollAni.update(tickPassed)) {
            return true;
        }
        return updated;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        requestLayout();
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void startRadiusAni(float destRadius) {
        startRadiusAni(destRadius, 1000);
    }

    public void startRadiusAni(float destRadius, int durationInMSec) {
        if (this.mRadiusAni == null) {
            this.mRadiusAni = new RadiusAnimation(this);
        }
        this.mRadiusAni.reset(true);
        this.mRadiusAni.setDestRadius(destRadius);
        this.mRadiusAni.setDuration(durationInMSec);
        this.mRadiusAni.start();
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RadiusAnimation extends TFAnimation {
        protected TFCircularHolder mHolder;
        protected float mSrcRadius = 0.0f;
        protected float mDestRadius = 0.0f;

        public RadiusAnimation(TFCircularHolder holder) {
            this.mHolder = holder;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStart() {
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStop() {
            OnFrame();
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public void OnFrame() {
            this.mHolder.setRadius(getRadius());
            this.mHolder.requestLayout();
        }

        public void setDestRadius(float destRadius) {
            this.mSrcRadius = this.mHolder.getRadius();
            this.mDestRadius = destRadius;
        }

        public float getRadius() {
            float t = getT();
            return ((1.0f - t) * this.mSrcRadius) + (this.mDestRadius * t);
        }
    }

    public void startPitchAndRoll(int periodInMSec) {
        if (this.mPitchAndRollAni == null) {
            this.mPitchAndRollAni = new PitchAndRollAnimation(this);
        }
        this.mPitchAndRollAni.reset(true);
        this.mPitchAndRollAni.setPeriod(periodInMSec);
        this.mPitchAndRollAni.setDuration(periodInMSec);
        this.mPitchAndRollAni.start();
        requestLayout();
    }

    public void stopPitchAndRoll() {
        this.mPitchAndRollAni.mRepeat = false;
        this.mPitchAndRollAni.stop();
        requestLayout();
    }

    /* loaded from: classes.dex */
    static class PitchAndRollAnimation extends TFAnimation {
        protected TFCircularHolder mHolder;
        protected int mPeriod = 0;
        protected boolean mRepeat;

        public PitchAndRollAnimation(TFCircularHolder holder) {
            this.mHolder = holder;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStart() {
            this.mRepeat = true;
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStop() {
            OnFrame();
            if (this.mRepeat) {
                start();
                return true;
            }
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public void OnFrame() {
            this.mHolder.requestLayout();
        }

        public void setPeriod(int periodInMSec) {
            this.mPeriod = periodInMSec;
        }

        public float getOffset(float modelPosition) {
            float rad = (float) ((getT() + modelPosition) * 2.0f * 3.141592653589793d);
            return (float) Math.sin(rad);
        }
    }

    public void setHeadRadius(float headRadius) {
        this.mHeadRadius = headRadius;
        requestLayout();
    }
}
