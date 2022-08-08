package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFWallHolder extends TFGridHolder {
    private static final String TAG = "TFWallHolder";

    public TFWallHolder(int direction, int groupSize) {
        this.mDirection = direction;
        this.mGroupSize = groupSize;
        this.mScrollStartIndexOffset = -1;
        this.mScrollEndIndexOffset = 1;
    }

    public TFWallHolder() {
        this(1, 2);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        return (calcGroupCount(slotCount) / 2) * this.mGroupSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onAddModel(TFObject object, int modelIndex) {
        object.setPriorAction(1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onRemoveModel(TFObject object, int modelIndex) {
    }

    public void setItemSize(float itemWidth, float itemHeight) {
        if (this.mDirection == 1) {
            this.mItemWidth = itemWidth;
            this.mGroupHeight = itemHeight;
            return;
        }
        this.mItemWidth = itemHeight;
        this.mGroupHeight = itemWidth;
    }

    public void setSpaces(float horizontalSpace, float verticalSpace) {
        if (this.mDirection == 1) {
            this.mGroupSpace = verticalSpace;
            this.mItemSpace = horizontalSpace;
        } else {
            this.mGroupSpace = horizontalSpace;
            this.mItemSpace = verticalSpace;
        }
        this.mViewWidth = getViewWidth(this.mGroupSize);
        this.mViewHeight = getViewHeight(getGroupCount());
    }

    public void setDirection(int direction) {
        this.mDirection = direction;
        requestLayout();
    }

    public int getDirection() {
        return this.mDirection;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return this.mDirection == 1 ? (forceVector[5] - forceVector[1]) * 3.0f : (-(forceVector[4] - forceVector[0])) * 3.0f;
    }

    @Override // com.nemustech.tiffany.world.TFGridHolder
    protected void locateModel(TFModel model, int modelIndex, float x, float y, float z) {
        if (this.mDirection == 1) {
            model.locate(x - (this.mViewWidth / 2.0f), -y, z);
        } else {
            model.locate(y, (this.mViewWidth / 2.0f) - x, z);
        }
    }
}
