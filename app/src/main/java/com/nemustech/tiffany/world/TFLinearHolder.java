package com.nemustech.tiffany.world;

import android.util.SparseIntArray;

/* loaded from: classes.dex */
public class TFLinearHolder extends TFSimpleHolder {
    private static final String TAG = "TFLinearHolder";
    protected float mLength;

    public TFLinearHolder(float length) {
        this.mLength = length;
        getMoveAnimation().setInertia(1.5f);
        setPriorAxis(1);
    }

    public TFLinearHolder() {
        this(2.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getHeadSlotIndex(int slotCount) {
        return 0;
    }

    public TFLinearHolder(float length, boolean isTerminalHolder) {
        super(isTerminalHolder);
        this.mLength = length;
        getMoveAnimation().setInertia(1.5f);
        setPriorAxis(1);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected void onSetDrawOrder(SparseIntArray drawOrder, int modelIndex) {
        int slotCount = drawOrder.size();
        for (int i = 0; i < slotCount; i++) {
            drawOrder.put(i, (slotCount - i) - 1);
        }
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected void onAddSlots(int addedSlots) {
        setHeadSlotIndex(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onAddModel(TFObject object, int modelIndex) {
        object.setPriorAction(1);
        setHeadSlotIndex(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder
    public void onRemoveModel(TFObject object, int modelIndex) {
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected float getModelPosition(int modelIndex) {
        float position = (modelIndex - getHeadSlotIndex()) / getModelCount();
        return position;
    }

    @Override // com.nemustech.tiffany.world.TFSimpleHolder
    protected void locateObject(TFObject object, float modelPosition, int modelIndex) {
        float height = object.getHeight() / 2.0f;
        float z = 0.5f - modelPosition;
        object.locate(0.0f, 0.0f, z * this.mLength);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return -(forceVector[5] - forceVector[1]);
    }
}
