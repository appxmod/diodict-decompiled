package com.nemustech.tiffany.world;

import android.util.Log;
import com.nemustech.tiffany.world.TFHolder;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public abstract class TFGridHolder extends TFHolder {
    public static final int HORIZONTAL = 0;
    private static String TAG = "TFGridHolder";
    public static final int VERTICAL = 1;
    protected int mDirection;
    protected int mScrollEndIndexOffset;
    protected int mScrollStartIndexOffset;
    protected float mViewHeight;
    protected float mViewWidth;
    protected int mGroupSize = 1;
    protected float mMarginLeft = 0.0f;
    protected float mMarginRight = 0.0f;
    protected float mMarginTop = 0.1f;
    protected float mMarginBottom = 0.1f;
    protected float mGroupHeight = 1.0f;
    protected float mGroupSpace = 0.1f;
    protected float mItemWidth = 1.0f;
    protected float mItemSpace = 0.2f;

    protected abstract void locateModel(TFModel tFModel, int i, float f, float f2, float f3);

    public TFGridHolder() {
        this.mMoveAni = new GridMoveAnimation(this);
    }

    public void setGroupSize(int groupSize) {
        if (groupSize >= 1) {
            this.mGroupSize = groupSize;
        }
    }

    public int getGroupSize() {
        return this.mGroupSize;
    }

    public int getGroupCount() {
        return calcGroupCount(getItemCount());
    }

    public int calcGroupCount(int itemCount) {
        return this.mGroupSize == 1 ? itemCount : ((this.mGroupSize + itemCount) - 1) / this.mGroupSize;
    }

    protected int indexToGroup(int index) {
        return this.mGroupSize == 1 ? index : index / this.mGroupSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getViewHeight(int groupCount) {
        float p = 0.0f + this.mMarginTop;
        return p + (((this.mGroupHeight + this.mGroupSpace) * groupCount) - this.mGroupSpace) + this.mMarginBottom;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getViewWidth(int groupSize) {
        float p = 0.0f + this.mMarginLeft;
        return p + (((this.mItemWidth + this.mItemSpace) * groupSize) - this.mItemSpace) + this.mMarginRight;
    }

    protected float getGroupSpace(int groupCount) {
        float p = this.mViewHeight;
        return (p - ((this.mMarginTop + this.mMarginBottom) + (this.mGroupHeight * groupCount))) / (groupCount - 1);
    }

    protected float getItemSpace(int groupSize) {
        float p = this.mViewWidth;
        return (p - ((this.mMarginLeft + this.mMarginRight) + (this.mItemWidth * groupSize))) / (groupSize - 1);
    }

    protected float getGroupPosition(int group) {
        float p = 0.0f + this.mMarginTop;
        return p + ((this.mGroupHeight + this.mGroupSpace) * group) + (this.mGroupHeight / 2.0f);
    }

    protected float getItemPosition(int itemInGroup) {
        float p = 0.0f + this.mMarginLeft;
        return p + ((this.mItemWidth + this.mItemSpace) * itemInGroup) + (this.mItemWidth / 2.0f);
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    /* renamed from: getMoveAnimation */
    public GridMoveAnimation mo5getMoveAnimation() {
        return (GridMoveAnimation) this.mMoveAni;
    }

    public void setHeadGroupIndex(int groupIndex, boolean showAnimation) {
        int groupOffset = groupIndex - indexToGroup(this.mHeadItemIndex);
        if (showAnimation) {
            this.mMoveAni.startMoveAnimation(groupOffset);
            return;
        }
        scrollHeadItemIndex(groupOffset, false);
        requestLayout();
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public void setHeadItemIndex(int itemIndex, boolean showAnimation) {
        int groupOffset = indexToGroup(itemIndex) - indexToGroup(this.mHeadItemIndex);
        Log.d(TAG, "head groupOffset is: " + groupOffset + "( " + indexToGroup(this.mHeadItemIndex) + "->" + indexToGroup(itemIndex));
        if (showAnimation) {
            this.mMoveAni.startMoveAnimation(groupOffset);
            return;
        }
        scrollHeadItemIndex(groupOffset, false);
        requestLayout();
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public void setEndlessMode(boolean endless) {
        Log.w(TAG, "Endless mode is not supported");
        super.setEndlessMode(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFHolder, com.nemustech.tiffany.world.TFObject
    public boolean updateObject(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        boolean ret = super.updateObject(gl, tickPassed, bDoGLCalc);
        boolean updated = false;
        if (updateLayout(tickPassed)) {
            updated = true;
        }
        if (this.mRefresh) {
            refreshSlotIndex();
            this.mRefresh = false;
            this.mLayoutObjects = true;
            updated = true;
        }
        if (this.mDelayedSetItemMode && this.mSetItemNow) {
            int slotIndex = 0;
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer slot = i$.next();
                setItemIndexOfObjectInSlot(slot, calculateItemIndexOfSlot(slotIndex));
                slotIndex++;
            }
            this.mSetItemNow = false;
            this.mNeedToSetItem = false;
        }
        if (this.mLayoutObjects) {
            layoutObjects(this.mDeviation);
            this.mLayoutObjects = false;
            updated = true;
        }
        if (updated && this.mWorld != null) {
            this.mWorld.requestRender();
        }
        return ret;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public boolean updateLayout(int tickPassed) {
        if (indexToGroup(this.mHeadItemIndex) < (-this.mScrollStartIndexOffset)) {
            setHeadGroupIndex(-this.mScrollStartIndexOffset, false);
        } else if (indexToGroup(this.mHeadItemIndex) > (getGroupCount() - 1) - this.mScrollEndIndexOffset) {
            setHeadGroupIndex((getGroupCount() - 1) - this.mScrollEndIndexOffset, false);
        }
        if (this.mMoveAni == null || !this.mMoveAni.update(tickPassed)) {
            return false;
        }
        return true;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public void layoutObjects(float headDeviation) {
        float y;
        int itemCount = getItemCount();
        int itemGroupCount = indexToGroup(itemCount);
        int modelCount = getModelCount();
        int modelGroupCount = calcGroupCount(modelCount);
        float t = headDeviation - ((int) headDeviation);
        this.mViewHeight = getViewHeight(itemGroupCount);
        this.mViewWidth = getViewWidth(this.mGroupSize);
        float headY = getGroupPosition(indexToGroup(this.mHeadItemIndex));
        for (int j = 0; j < modelGroupCount; j++) {
            int modelIndex = j * this.mGroupSize;
            int itemIndex = this.mHeadItemIndex + (modelIndex - getHeadSlotIndex());
            int group = indexToGroup(itemIndex);
            if (headDeviation < 0.0f) {
                float p1 = getGroupPosition(group);
                float p2 = getGroupPosition(group + 1);
                y = ((1.0f - (-t)) * p1) + ((-t) * p2);
            } else {
                float p12 = getGroupPosition(group);
                float p22 = getGroupPosition(group - 1);
                y = ((1.0f - t) * p12) + (t * p22);
            }
            float y2 = y - headY;
            for (int i = 0; i < this.mGroupSize && modelIndex < modelCount; i++) {
                TFModel model = getModel(modelIndex);
                float x = getItemPosition(i);
                locateModel(model, modelIndex, x, y2, 0.0f);
                modelIndex++;
            }
        }
    }

    public int scrollHeadItemIndex(int offset, boolean callListener) {
        int itemCount = getItemCount();
        int headIndex = this.mHeadItemIndex;
        if (offset < 0) {
            int itemIndexLimit = (-this.mScrollStartIndexOffset) * this.mGroupSize;
            for (int i = 0; i < (-offset); i++) {
                headIndex--;
                int newIndex = adjustItemIndex(headIndex);
                if (newIndex < itemIndexLimit) {
                    return -i;
                }
                this.mHeadItemIndex = newIndex;
                shiftObjectsInSlots(-1, true);
            }
            return offset;
        }
        int itemIndexLimit2 = itemCount - ((this.mScrollEndIndexOffset + 1) * this.mGroupSize);
        Log.d(TAG, "item index limit is: " + itemIndexLimit2 + "/" + itemCount);
        for (int i2 = 0; i2 < offset; i2++) {
            headIndex++;
            int newIndex2 = adjustItemIndex(headIndex);
            if (newIndex2 < 0 || newIndex2 > itemIndexLimit2) {
                return i2;
            }
            this.mHeadItemIndex = newIndex2;
            shiftObjectsInSlots(1, true);
        }
        return offset;
    }

    public int scrollHeadModelIndex(int offset) {
        return 0;
    }

    protected void shiftObjectsInSlots(int offset, boolean callListener) {
        if (this.mDelayedSetItemMode) {
            this.mNeedToSetItem = true;
        }
        if (offset < 0) {
            offset = -offset;
            for (int i = 0; i < getSlotCount(); i++) {
                if (i < offset) {
                    TFObjectContainer oc = this.mObjectSlots.removeLast();
                    TFObjectContainer oc2 = oc;
                    this.mObjectSlots.addFirst(oc2);
                    setItemIndexOfObjectInSlot(oc2, calculateItemIndexOfSlot((offset - 1) - i));
                } else {
                    TFObjectContainer oc3 = this.mObjectSlots.get(i);
                    setItemIndexOfObjectInSlot(oc3, calculateItemIndexOfSlot(i));
                }
            }
        } else if (offset > 0) {
            for (int i2 = 0; i2 < getSlotCount(); i2++) {
                if (i2 < offset) {
                    TFObjectContainer oc4 = this.mObjectSlots.removeFirst();
                    TFObjectContainer oc5 = oc4;
                    this.mObjectSlots.addLast(oc5);
                    setItemIndexOfObjectInSlot(oc5, calculateItemIndexOfSlot(getSlotCount() - 1) - 1);
                } else {
                    TFObjectContainer oc6 = this.mObjectSlots.get(i2 - offset);
                    setItemIndexOfObjectInSlot(oc6, calculateItemIndexOfSlot(i2 - offset));
                }
            }
        } else {
            int slotIndex = 0;
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer slot = i$.next();
                setItemIndexOfObjectInSlot(slot, calculateItemIndexOfSlot(slotIndex));
                slotIndex++;
            }
        }
        if (offset != 0 && this.mObjectShiftListener != null) {
            this.mWorld.queueEvent(this.mOnObjectShiftRunnable);
        }
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public boolean moveHeadModelStep(float step, boolean bindingAction) {
        boolean z = true;
        float step2 = step + this.mDeviation;
        float deviation = getDeviation(step2);
        int offset = getOffset(step2);
        int head = indexToGroup(getHeadItemIndex());
        int count = getGroupCount();
        if (this.mWraparound || (head + offset < count && head + offset >= 0)) {
            int moved = scrollHeadItemIndex(this.mGroupSize * offset, true);
            if (this.mGroupSize * offset == moved) {
                this.mDeviation = deviation;
            } else {
                this.mDeviation = (0.45f * step2) / Math.abs(step2);
            }
            if (!this.mHolderBindings.isEmpty() && bindingAction) {
                if (this.mBindingBehaviorDispatcher != null) {
                    this.mBindingBehaviorDispatcher.bindingBehavior(step2);
                } else {
                    defaultBindingBehavior(step2);
                }
            }
            requestLayout();
            if (this.mGroupSize * offset != moved) {
                z = false;
            }
            return z;
        }
        return false;
    }

    public void setScrollLimitOffset(int start, int end) {
        this.mScrollStartIndexOffset = start;
        this.mScrollEndIndexOffset = end;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public boolean addHolder(TFHolder holder, int slotIndex) {
        if (isTerminalHolder()) {
            throw new IllegalArgumentException("Tried to attach a holder to terminal holder");
        }
        if (slotIndex > this.mObjectSlots.size()) {
            throw new IllegalArgumentException("The given slot index exceeds slot count");
        }
        holder.mParentHolder = this;
        holder.associateToWorld(this.mWorld);
        TFObjectContainer oc = new TFObjectContainer(holder);
        if (this.mItemProvider == null) {
            if (getSlotCount() != 0) {
                int originalHeadItemIndex = getHeadItemIndex();
                shiftObjectsInSlots(getHeadSlotIndex() - getHeadItemIndex(), false);
                this.mObjectSlots.add(slotIndex, oc);
                int idx = 0;
                Iterator i$ = this.mObjectSlots.iterator();
                while (i$.hasNext()) {
                    TFObjectContainer container = i$.next();
                    container.getObject().setItemIndex(idx);
                    idx++;
                }
                if (originalHeadItemIndex >= slotIndex) {
                    originalHeadItemIndex++;
                }
                setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                setDrawOrder(this.mObjectSlots.size() - 1);
                onAddHolder(holder, this.mObjectSlots.indexOf(oc));
                shiftObjectsInSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
            } else {
                this.mObjectSlots.add(oc);
                setItemIndexOfObjectInSlot(this.mObjectSlots.get(0), 0);
                setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                setDrawOrder(this.mObjectSlots.size() - 1);
                onAddHolder(holder, 0);
            }
        } else {
            this.mObjectSlots.add(oc);
            this.mNeedToSetItem = true;
            this.mSetItemNow = true;
            setItemIndexOfObjectInSlot(oc, this.mObjectSlots.size() - 1);
            setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
            setDrawOrder(this.mObjectSlots.size() - 1);
            onAddHolder(holder, this.mObjectSlots.size() - 1);
        }
        if (this.mCloneList != null) {
            Iterator i$2 = this.mCloneList.iterator();
            while (i$2.hasNext()) {
                TFObject o = i$2.next();
                try {
                    ((TFHolder) o).addHolder((TFHolder) holder.clone(), slotIndex);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public TFHolder removeHolder(int slotIndex, boolean showAnimation) {
        TFHolder holder = null;
        if (!isTerminalHolder() && slotIndex < this.mObjectSlots.size()) {
            if (this.mImageProvider == null && this.mItemProvider == null && getHeadSlotIndex() >= 0) {
                if (getSlotCount() != 0) {
                    int originalHeadItemIndex = getHeadItemIndex();
                    shiftObjectsInSlots(getHeadSlotIndex() - getHeadItemIndex(), false);
                    holder = getHolder(slotIndex);
                    this.mObjectSlots.remove(slotIndex);
                    for (int i = slotIndex; i < getSlotCount(); i++) {
                        this.mObjectSlots.get(i).getObject().setItemIndex(i);
                    }
                    setDrawOrder(slotIndex);
                    onRemoveHolder(holder, slotIndex);
                    shiftObjectsInSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
                } else {
                    throw new IllegalArgumentException("Nothing to remove");
                }
            } else {
                holder = getHolderInSlot(slotIndex);
                onRemoveHolder(holder, slotIndex);
                this.mObjectSlots.remove(slotIndex);
                setDrawOrder(slotIndex);
            }
            if (this.mCloneList != null) {
                Iterator i$ = this.mCloneList.iterator();
                while (i$.hasNext()) {
                    TFObject o = i$.next();
                    ((TFHolder) o).removeHolder(slotIndex);
                }
            }
            requestLayout();
        }
        return holder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public boolean addModel(TFModel model, int slotIndex) {
        if (!isTerminalHolder()) {
            throw new IllegalArgumentException("Cannot attach a model to non terminal holder");
        }
        if (slotIndex > this.mObjectSlots.size()) {
            throw new IllegalArgumentException("Invalid slotIndex");
        }
        model.mParentHolder = this;
        model.mWorld = this.mWorld;
        TFObjectContainer oc = new TFObjectContainer(model);
        if (this.mImageProvider == null && this.mItemProvider == null && getHeadSlotIndex() >= 0) {
            if (getSlotCount() != 0) {
                int originalHeadItemIndex = getHeadItemIndex();
                shiftObjectsInSlots(getHeadSlotIndex() - getHeadItemIndex(), false);
                this.mObjectSlots.add(slotIndex, oc);
                int idx = 0;
                Iterator i$ = this.mObjectSlots.iterator();
                while (i$.hasNext()) {
                    TFObjectContainer container = i$.next();
                    container.getObject().setItemIndex(idx);
                    idx++;
                }
                if (originalHeadItemIndex >= slotIndex) {
                    originalHeadItemIndex++;
                }
                setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                setDrawOrder(this.mObjectSlots.size() - 1);
                onAddModel(model, slotIndex);
                shiftObjectsInSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
            } else {
                this.mObjectSlots.add(oc);
                setItemIndexOfObjectInSlot(this.mObjectSlots.get(0), 0);
                setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                setDrawOrder(this.mObjectSlots.size() - 1);
                onAddModel(model, 0);
            }
        } else {
            this.mObjectSlots.add(oc);
            this.mNeedToSetItem = true;
            this.mSetItemNow = true;
            setItemIndexOfObjectInSlot(oc, this.mObjectSlots.size() - 1);
            setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
            setDrawOrder(this.mObjectSlots.size() - 1);
            onAddModel(model, this.mObjectSlots.size() - 1);
        }
        if (this.mCloneList != null) {
            Iterator i$2 = this.mCloneList.iterator();
            while (i$2.hasNext()) {
                TFObject o = i$2.next();
                try {
                    TFHolder h = (TFHolder) o;
                    h.addModel((TFModel) model.clone(), slotIndex);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        requestLayout();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public TFModel removeModel(int index, boolean showAnimation) {
        return null;
    }

    /* loaded from: classes.dex */
    public class GridMoveAnimation extends TFHolder.MoveAnimation {
        public static final float ANGLE_DEC = 1.0f;
        public static final float ANGLE_Y_LIMIT = 40.0f;
        protected float mAngle;
        protected boolean mIsLeaning;
        protected boolean mIsScrolling;
        protected boolean mLeaningMode;

        public GridMoveAnimation(TFGridHolder holder) {
            super(holder);
        }

        public void setLeaningMode(boolean mode) {
            this.mLeaningMode = mode;
        }

        public boolean isLeaningMode() {
            return this.mLeaningMode;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStart() {
            this.mInitialS = getHolder().getDeviation();
            this.mS = this.mInitialS;
            this.mIsLeaning = true;
            this.mIsScrolling = true;
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStop() {
            boolean bKeepGoing = true;
            if (this.mAutoRepositionMode) {
                reset(false);
                bKeepGoing = repositionObjects(this.mAutoRepositionDuration);
            }
            OnFrame();
            return !bKeepGoing;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public void OnFrame() {
            float t = getTime();
            float S = calcS(this.mInitialS, this.mInitialV, this.mAcceleration, t);
            float step = S - this.mS;
            if (!getHolder().moveHeadModelStep(step)) {
                if (this.mIsLeaning) {
                    if (this.mAngle == 0.0f) {
                        Log.d(TFGridHolder.TAG, "Angle is 0 at stop");
                        getHolder().look(0.0f, this.mAngle);
                        this.mIsLeaning = false;
                        stop();
                    } else {
                        this.mAngle -= this.mAngle % 1.0f;
                        if (this.mAngle >= 0.0f) {
                            this.mAngle -= 1.0f;
                        } else {
                            this.mAngle += 1.0f;
                        }
                        if (((TFGridHolder) getHolder()).mDirection == 1) {
                            getHolder().look(0.0f, this.mAngle);
                        } else {
                            getHolder().look(this.mAngle, 0.0f);
                        }
                    }
                }
            } else {
                this.mS = S;
                if (isLeaningMode()) {
                    this.mAngle = step * 40.0f;
                    if (this.mAngle > 40.0f) {
                        this.mAngle = 40.0f;
                    } else if (this.mAngle < -40.0f) {
                        this.mAngle = -40.0f;
                    }
                    if (((TFGridHolder) getHolder()).mDirection == 1) {
                        getHolder().look(0.0f, this.mAngle);
                    } else {
                        getHolder().look(this.mAngle, 0.0f);
                    }
                    if (t == 0.0f) {
                        getHolder().look(0.0f, 0.0f);
                    }
                }
            }
            getHolder().requestLayout();
        }
    }
}
