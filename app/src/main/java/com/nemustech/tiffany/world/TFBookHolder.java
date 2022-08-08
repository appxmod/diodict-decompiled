package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFBookHolder extends TFSimpleHolder {
    public static final int FLIP_BACKWARD = -1;
    public static final int FLIP_FORWARD = 1;
    public static final int FLIP_HORIZONTAL = 0;
    public static final int FLIP_VERTICAL = 1;
    private static final String TAG = "GFBookHolder";
    protected int mFlipDirection;
    protected int mFlipOrientation;
    protected float mInitialAngle;
    protected float mModelPositionGap;
    protected float mPageLiftAngle;
    protected float mSpreadAngle;
    protected float mModelWidth = -1.0f;
    protected float mModelHeight = -1.0f;
    protected float mMargin = 0.2f;
    protected PageFlipFactorProvider mPageFlipFactorProvider = null;

    /* loaded from: classes.dex */
    public interface PageFlipFactorProvider {
        float pageFlipFactorProvider(float f);
    }

    public TFBookHolder() {
        this.mWraparound = false;
    }

    public TFBookHolder(int flipOrientation, int flipDirection, float initialAngle, float spreadAngle, float pageLiftAngle) {
        this.mFlipOrientation = flipOrientation;
        this.mFlipDirection = flipDirection;
        this.mInitialAngle = initialAngle;
        this.mSpreadAngle = spreadAngle;
        this.mPageLiftAngle = pageLiftAngle;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        return (slotCount - 1) / 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onAddModel(TFObject object, int modelIndex) {
        object.mShouldRotateFirst = true;
        if (getSlotCount() < 3) {
            this.mModelPositionGap = 1.0f / getSlotCount();
        } else {
            this.mModelPositionGap = 1 / (getSlotCount() - 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onRemoveModel(TFObject object, int modelIndex) {
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected float getModelPosition(int modelIndex) {
        float position = (modelIndex - getHeadSlotIndex()) * this.mModelPositionGap;
        return position;
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected void locateObject(TFObject model, float modelPosition, int modelIndex) {
        float x;
        float y;
        float rotateAngle;
        if (this.mFlipOrientation == 0) {
            x = ((model.getWidth() * 0.5f) + ((Math.abs(modelIndex - getHeadSlotIndex()) - 1) * 0.0035f)) * this.mFlipDirection;
            y = 0.0f;
        } else {
            x = 0.0f;
            y = (-((model.getHeight() * 0.5f) - ((Math.abs(modelIndex - getHeadSlotIndex()) - 1) * 0.0035f))) * this.mFlipDirection;
        }
        float z = (-modelIndex) * 0.0035f;
        model.locate(x, y, z);
        if (modelIndex < getHeadSlotIndex()) {
            rotateAngle = (-(this.mInitialAngle + this.mSpreadAngle)) * this.mFlipDirection;
        } else if (modelIndex > getHeadSlotIndex()) {
            rotateAngle = (-this.mInitialAngle) * this.mFlipDirection;
        } else if (this.mDeviation < 0.0f) {
            float determinant = 1.0f - (Math.abs(this.mDeviation) / 0.5f);
            float factor = getPageFlipFactor(determinant);
            rotateAngle = (-(this.mInitialAngle + (this.mPageLiftAngle * factor))) * this.mFlipDirection;
        } else if (this.mDeviation > 0.0f) {
            float determinant2 = this.mDeviation / 0.5f;
            float factor2 = getPageFlipFactor(determinant2);
            rotateAngle = (-(this.mInitialAngle + this.mPageLiftAngle + ((this.mSpreadAngle - this.mPageLiftAngle) * factor2))) * this.mFlipDirection;
        } else {
            rotateAngle = (-(this.mInitialAngle + this.mPageLiftAngle)) * this.mFlipDirection;
        }
        if (rotateAngle >= 360.0f) {
            rotateAngle -= 360.0f;
        } else if (rotateAngle < 0.0f) {
            rotateAngle += 360.0f;
        }
        if (this.mFlipOrientation == 0) {
            model.look(rotateAngle, 0.0f);
        } else {
            model.look(0.0f, rotateAngle);
        }
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return this.mFlipOrientation == 0 ? ((-(forceVector[4] - forceVector[0])) / 1.5f) * this.mFlipDirection : ((forceVector[5] - forceVector[1]) / 1.5f) * this.mFlipDirection;
    }

    protected void modelSelectedBehavior(TFModel selectedModel) {
        this.mMoveAni.reset(false);
        if (getModelIndex(selectedModel) == getHeadModelIndex()) {
            setHeadItemIndex(getItemIndexOfSlot(getModelIndex(selectedModel)) + 1, true);
            return;
        }
        this.mMoveAni.reset(false);
        setHeadItemIndex(getItemIndexOfSlot(getModelIndex(selectedModel)), true);
    }

    protected float getPageFlipFactor(float determinant) {
        return this.mPageFlipFactorProvider == null ? defaultFlipFactorProvider(determinant) : this.mPageFlipFactorProvider.pageFlipFactorProvider(determinant);
    }

    protected float defaultFlipFactorProvider(float determinant) {
        return determinant * determinant;
    }

    public void setPageFlipFactorProvider(PageFlipFactorProvider factorProvider) {
        this.mPageFlipFactorProvider = factorProvider;
    }
}
