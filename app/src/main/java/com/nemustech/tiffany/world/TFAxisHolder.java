package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public abstract class TFAxisHolder extends TFSimpleHolder {
    private static final String TAG = "TFAxisHolder";
    protected float mBorderAngle;
    protected boolean mClockwise;
    protected float mHeadAngle;
    protected float mOpenRangeAngle;
    protected PitchAndRollAnimation mPitchAndRollAni;
    protected float mRadius;
    protected RadiusAnimation mRadiusAni;

    public TFAxisHolder(float radius) {
        this.mRadius = radius;
        setPriorAxis(1);
    }

    public TFAxisHolder() {
        this(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onAddModel(TFObject object, int modelIndex) {
        object.setPriorAction(1);
        setHeadSlotIndex(getSlotCount() / 2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onRemoveModel(TFObject object, int modelIndex) {
        setHeadSlotIndex((getSlotCount() - 1) / 2);
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected float getModelPosition(int modelIndex) {
        float position = (modelIndex - getHeadSlotIndex()) / getModelCount();
        return position;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getTargetDegree(float modelPosition) {
        int modelCount = getSlotCount();
        float availDegree = 360.0f - this.mOpenRangeAngle;
        float unitDegree = availDegree / modelCount;
        float theoDegree = unitDegree * (modelCount + 1);
        float targetDegree = modelPosition * theoDegree;
        if (targetDegree < 0.0f && targetDegree > (-unitDegree)) {
            return this.mOpenRangeAngle * (targetDegree / unitDegree);
        }
        if (targetDegree <= (-unitDegree)) {
            return (-this.mOpenRangeAngle) + targetDegree + unitDegree;
        }
        return targetDegree;
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
        protected TFAxisHolder mHolder;
        protected float mSrcRadius = 0.0f;
        protected float mDestRadius = 0.0f;

        public RadiusAnimation(TFAxisHolder holder) {
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
        protected TFAxisHolder mHolder;
        protected int mPeriod = 0;
        protected boolean mRepeat;

        public PitchAndRollAnimation(TFAxisHolder holder) {
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

    public void setOpenRangeAngle(float angle) {
        this.mOpenRangeAngle = angle;
    }
}
