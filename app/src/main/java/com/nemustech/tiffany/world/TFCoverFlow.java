package com.nemustech.tiffany.world;

import android.util.Log;
import android.util.SparseIntArray;

/* loaded from: classes.dex */
public class TFCoverFlow extends TFSimpleHolder {
    public static final float PI_HALF = 1.5707964f;
    static final String TAG = "TFCoverFlow";
    private float mHeadPositionLength;
    private float mLeaningAngle;
    private float mLength;
    private float mMargin;
    private float mModelPositionGap;
    private float mSlotWidth;

    public TFCoverFlow(float length) {
        this.mLeaningAngle = 65.0f;
        this.mLength = length;
        this.mSlotWidth = -1.0f;
        this.mModelPositionGap = -1.0f;
        this.mHeadPositionLength = -1.0f;
        this.mVelocity = 0.0f;
        getMoveAnimation().setInertia(1.25f);
        getMoveAnimation().setAutoRepositionMode(true, 150);
        this.mWraparound = false;
    }

    public TFCoverFlow() {
        this(2.0f);
    }

    public void setLeaningAngle(float angle) {
        this.mLeaningAngle = angle;
    }

    public float getLeaningAngle() {
        return this.mLeaningAngle;
    }

    public void setLength(float length) {
        this.mLength = length;
    }

    public float getLength() {
        return this.mLength;
    }

    public void setSlotWidth(float slotWidth) {
        this.mSlotWidth = slotWidth;
        this.mMargin = ((float) (((Math.cos((this.mLeaningAngle * 3.141592653589793d) / 180.0d) * this.mSlotWidth) * 0.5d) + (this.mSlotWidth * 0.5f))) / this.mLength;
        this.mHeadPositionLength = this.mMargin * 0.333f;
    }

    public float getSlotWidth() {
        return this.mSlotWidth;
    }

    public float getMargin() {
        return this.mMargin;
    }

    public void setHeadPositionLength(float headPositionLength) {
        this.mHeadPositionLength = headPositionLength;
    }

    public float getHeadPositionLength() {
        return this.mHeadPositionLength;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected void onSetDrawOrder(SparseIntArray drawOrder, int modelIndex) {
        int drawIndex = 0;
        int slotCount = getSlotCount();
        for (int i = 0; i < getHeadSlotIndex(); i++) {
            drawOrder.put(i, drawIndex);
            drawIndex++;
        }
        for (int i2 = slotCount - 1; i2 > getHeadSlotIndex(); i2--) {
            drawOrder.put(i2, drawIndex);
            drawIndex++;
        }
        if (slotCount > 0) {
            drawOrder.put(getHeadSlotIndex(), drawIndex);
        }
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected float getModelPosition(int modelIndex) {
        float position;
        if (modelIndex == getHeadSlotIndex()) {
            return 0.0f;
        }
        if (getHeadSlotIndex() > modelIndex) {
            position = (-this.mMargin) - (this.mModelPositionGap * ((getHeadSlotIndex() - modelIndex) - 1));
        } else {
            position = this.mMargin + (this.mModelPositionGap * ((modelIndex - getHeadSlotIndex()) - 1));
        }
        return position;
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected void locateObject(TFObject model, float modelPosition, int modelIndex) {
        float y = model.getHeight() * 0.5f;
        if (Math.abs(modelPosition) < this.mHeadPositionLength * 0.5f) {
            model.locate(this.mLength * modelPosition, y, (float) (this.mSlotWidth * Math.sin((this.mLeaningAngle * 3.141592653589793d) / 180.0d) * 0.30000001192092896d));
            model.look(0.0f, 0.0f);
            return;
        }
        float rotateDirection = modelPosition < 0.0f ? 1.0f : -1.0f;
        float determinant = (Math.abs(modelPosition) - (this.mHeadPositionLength * 0.5f)) / (this.mMargin - (this.mHeadPositionLength * 0.5f));
        if (determinant > 1.0f) {
            determinant = 1.0f;
        }
        float determinant2 = determinant * 1.5707964f;
        float angle = (float) (this.mLeaningAngle * (Math.pow(determinant2 - 1.0f, 3.0d) + 1.0d));
        float distnace = (float) (Math.sin((this.mLeaningAngle * 3.141592653589793d) / 180.0d) * 0.30000001192092896d * Math.sin((1.0f + determinant2) * 1.5707964f));
        model.locate(this.mLength * modelPosition, y, distnace);
        model.look(rotateDirection * angle, 0.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return -(forceVector[4] - forceVector[0]);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        if (slotCount == 0) {
            return -1;
        }
        return (slotCount - 1) / 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onAddModel(TFObject object, int modelIndex) {
        if (this.mSlotWidth != -1.0f) {
            if (object.getWidth() != this.mSlotWidth) {
                Log.e(TAG, "Variable model width error: all the models added to TFCoverFlow must be the same");
                return;
            }
        } else if (this.mLength == -1.0f) {
            Log.e(TAG, "Unspecified Information error: visiable Length of the cover flow is not specified");
            return;
        } else if (this.mLength < object.getWidth() * 1.5f) {
            Log.e(TAG, "Invalid Information error: visiable Length of the cover flow too short compared to the model width");
            return;
        } else {
            this.mSlotWidth = object.getWidth();
            this.mMargin = ((float) (((Math.cos((this.mLeaningAngle * 3.141592653589793d) / 180.0d) * this.mSlotWidth) * 0.5d) + (this.mSlotWidth * 0.5f))) / this.mLength;
            this.mHeadPositionLength = this.mMargin * 0.333f;
        }
        this.mModelPositionGap = (1.0f - (this.mMargin * 2.0f)) / (getSlotCount() - 1);
    }

    protected void onAddHolder(TFHolder holder, int modelIndex) {
        if (this.mSlotWidth != -1.0f) {
            if (holder.getWidth() != this.mSlotWidth) {
                Log.e(TAG, "Variable model width error: all the models added to TFCoverFlow must be the same");
                return;
            }
        } else if (this.mLength == -1.0f) {
            Log.e(TAG, "Unspecified Information error: visiable Length of the cover flow is not specified");
            return;
        } else if (this.mLength < holder.getWidth() * 1.5f) {
            Log.e(TAG, "Invalid Information error: visiable Length of the cover flow too short compared to the model width");
            return;
        } else {
            this.mSlotWidth = holder.getWidth();
            this.mMargin = ((float) (((Math.cos((this.mLeaningAngle * 3.141592653589793d) / 180.0d) * this.mSlotWidth) * 0.5d) + (this.mSlotWidth * 0.5f))) / this.mLength;
            this.mHeadPositionLength = this.mMargin * 0.333f;
        }
        this.mModelPositionGap = (1.0f - (this.mMargin * 2.0f)) / (getSlotCount() - 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onRemoveModel(TFObject object, int modelIndex) {
    }
}
