package com.nemustech.tiffany.world;

import android.util.Log;
import com.nemustech.tiffany.world.TFHolder;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public abstract class TFSimpleHolder extends TFHolder {
    private static final String TAG = "TFSimpleHolder";
    protected boolean mFadingEffect = true;
    private OnMoveListener mOnMoveListener;

    /* loaded from: classes.dex */
    public interface OnMoveListener {
        void onMove(float f);
    }

    protected abstract float getModelPosition(int i);

    protected abstract void locateObject(TFObject tFObject, float f, int i);

    public TFSimpleHolder() {
        this.mMoveAni = new SimpleMoveAnimation(this);
    }

    public TFSimpleHolder(boolean isTerminalHolder) {
        super(isTerminalHolder);
        this.mMoveAni = new SimpleMoveAnimation(this);
    }

    @Override // com.nemustech.tiffany.world.TFHolder, com.nemustech.tiffany.world.TFObject
    public Object clone() throws CloneNotSupportedException {
        TFSimpleHolder clone = (TFSimpleHolder) super.clone();
        clone.mMoveAni = new SimpleMoveAnimation(clone);
        return clone;
    }

    public void setFadingEffect(boolean fadeEffect) {
        this.mFadingEffect = fadeEffect;
    }

    private void applyFadingEffect(TFObject object, int slotIdx) {
        if (slotIdx == 0 && this.mDeviation >= 0.0f) {
            object.mExternalFadingFactor = 1.0f - (this.mDeviation / 0.5f);
        } else if (slotIdx == getSlotCount() - 1 && this.mDeviation <= 0.0f) {
            object.mExternalFadingFactor = (this.mDeviation / 0.5f) + 1.0f;
        } else {
            object.mExternalFadingFactor = object.getOpacity();
        }
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public void setHeadItemIndex(int itemIndex, boolean showAnimation) {
        if (getHeadItemIndex() < 0 && getSlotCount() != 0) {
            if (this.mItemProvider == null) {
                throw new IllegalArgumentException("Shouldn't have reached here.");
            }
            this.mHeadItemIndex = itemIndex;
            int idx = 0;
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer oc = i$.next();
                setItemIndexOfObjectInSlot(oc, calculateItemIndexOfSlot(idx));
                idx++;
            }
            return;
        }
        int offset = getItemOffsetToHead(itemIndex);
        if (showAnimation) {
            this.mMoveAni.reset(false);
            this.mMoveAni.startMoveAnimation(offset);
            return;
        }
        scrollHeadItemIndex(offset, false);
        requestLayout();
    }

    public void setHeadItemIndex(int itemIndex, int duration) {
        int offset = getItemOffsetToHead(itemIndex);
        if (duration > 0) {
            this.mMoveAni.reset(false);
            this.mMoveAni.startMoveAnimation(offset, duration);
            return;
        }
        scrollHeadItemIndex(offset, false);
        requestLayout();
    }

    public void setHeadItemIndex(int itemIndex, float speed) {
        int offset = getItemOffsetToHead(itemIndex);
        if (speed > 0.0f) {
            this.mMoveAni.reset(false);
            this.mMoveAni.startMoveAnimation(offset, speed);
            return;
        }
        Log.d(TAG, "Animation speed is set to 0 in setHeadItemIndex");
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public TFObject getObjectInSlot(int idx) {
        if (idx < getSlotCount() && getSlotCount() != 0) {
            if (this.mImageProvider == null && this.mItemProvider == null && getHeadSlotIndex() >= 0) {
                int originalHeadItemIndex = this.mHeadItemIndex;
                shiftObjectsMultipleSlots(getHeadSlotIndex() - this.mHeadItemIndex, false);
                TFObject object = this.mObjectSlots.get(idx).getObject();
                shiftObjectsMultipleSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
                return object;
            }
            TFObject object2 = this.mObjectSlots.get(idx).getObject();
            return object2;
        }
        return null;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public int getSlotIndex(TFObject object) {
        if (this.mImageProvider == null && this.mItemProvider == null && getHeadSlotIndex() >= 0) {
            int originalHeadItemIndex = this.mHeadItemIndex;
            shiftObjectsMultipleSlots(getHeadSlotIndex() - this.mHeadItemIndex, false);
            int idx = this.mObjectSlots.indexOf(object.getWrapperObject());
            shiftObjectsMultipleSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
            return idx;
        }
        int idx2 = this.mObjectSlots.indexOf(object.getWrapperObject());
        return idx2;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    boolean addHolder(TFHolder holder) {
        return addHolder(holder, this.mObjectSlots.size());
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
                int originalHeadItemIndex = this.mHeadItemIndex;
                shiftObjectsMultipleSlots(getHeadSlotIndex() - this.mHeadItemIndex, false);
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
                shiftObjectsMultipleSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
                this.mHeadItemIndex = getHeadItemIndex();
            } else {
                this.mObjectSlots.add(oc);
                setItemIndexOfObjectInSlot(this.mObjectSlots.get(0), 0);
                setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                setDrawOrder(this.mObjectSlots.size() - 1);
                onAddHolder(holder, 0);
                this.mHeadItemIndex = 0;
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
        setHeadItemIndex(this.mHeadItemIndex, false);
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
    public TFHolder removeHolder(int slotIndex) {
        return removeHolder(slotIndex, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public TFHolder removeHolder(int slotIndex, boolean showAnimation) {
        TFHolder holder;
        if (isTerminalHolder()) {
            throw new IllegalArgumentException("This holder is a terminal holder which does not hold any holders but models");
        }
        if (slotIndex < 0 || this.mObjectSlots.size() <= slotIndex) {
            throw new IllegalArgumentException("Slot index out of bounds");
        }
        if (getHeadSlotIndex() < 0) {
            throw new IllegalArgumentException("Head Slot index has been set to an invalid index");
        }
        if (this.mImageProvider == null && this.mItemProvider == null) {
            int originalHeadItemIndex = this.mHeadItemIndex;
            shiftObjectsMultipleSlots(getHeadSlotIndex() - this.mHeadItemIndex, false);
            holder = getHolderInSlot(slotIndex);
            this.mObjectSlots.remove(slotIndex);
            for (int i = slotIndex; i < getSlotCount(); i++) {
                this.mObjectSlots.get(i).getObject().setItemIndex(i);
            }
            setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
            setDrawOrder(slotIndex);
            onRemoveHolder(holder, slotIndex);
            if (getSlotCount() > 0) {
                if (originalHeadItemIndex >= getHeadSlotIndex()) {
                    originalHeadItemIndex--;
                }
                shiftObjectsMultipleSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
            }
        } else {
            holder = getHolderInSlot(slotIndex);
            setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
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
        return holder;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    boolean addModel(TFModel model) {
        return addModel(model, this.mObjectSlots.size());
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
        if (this.mImageProvider == null && this.mItemProvider == null) {
            if (getSlotCount() != 0) {
                int originalHeadItemIndex = this.mHeadItemIndex;
                shiftObjectsMultipleSlots(getHeadSlotIndex() - this.mHeadItemIndex, false);
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
                shiftObjectsMultipleSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
                this.mHeadItemIndex = getHeadItemIndex();
            } else {
                this.mObjectSlots.add(oc);
                setItemIndexOfObjectInSlot(this.mObjectSlots.get(0), 0);
                setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                setDrawOrder(this.mObjectSlots.size() - 1);
                onAddModel(model, 0);
                this.mHeadItemIndex = 0;
            }
        } else {
            this.mObjectSlots.add(oc);
            this.mNeedToSetItem = true;
            this.mSetItemNow = true;
            setItemIndexOfObjectInSlot(oc, calculateItemIndexOfSlot(this.mObjectSlots.size() - 1));
            setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
            setDrawOrder(this.mObjectSlots.size() - 1);
            onAddModel(model, this.mObjectSlots.size() - 1);
        }
        setHeadItemIndex(this.mHeadItemIndex, false);
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
        refreshHolder();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addModel(TFModel model, int slotIndex, boolean showAnimation) {
        return addModel(model);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public TFModel removeModel(int slotIndex) {
        return removeModel(slotIndex, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFHolder
    public TFModel removeModel(int slotIndex, boolean showAnimation) {
        TFModel model = null;
        if (isTerminalHolder() && slotIndex < this.mObjectSlots.size()) {
            if (this.mImageProvider == null && this.mItemProvider == null && getHeadSlotIndex() >= 0) {
                if (getSlotCount() != 0) {
                    int originalHeadItemIndex = this.mHeadItemIndex;
                    shiftObjectsMultipleSlots(getHeadSlotIndex() - this.mHeadItemIndex, false);
                    model = getModelInSlot(slotIndex);
                    this.mObjectSlots.remove(slotIndex);
                    for (int i = slotIndex; i < getSlotCount(); i++) {
                        this.mObjectSlots.get(i).getObject().setItemIndex(i);
                    }
                    if (getSlotCount() > 0) {
                        setHeadSlotIndex(getHeadSlotIndex(getSlotCount()));
                    }
                    onRemoveModel(model, slotIndex);
                    setDrawOrder(slotIndex);
                    if (getSlotCount() > 0) {
                        if (originalHeadItemIndex >= getHeadSlotIndex()) {
                            originalHeadItemIndex--;
                        }
                        shiftObjectsMultipleSlots(originalHeadItemIndex - getHeadSlotIndex(), false);
                    }
                } else {
                    throw new IllegalArgumentException("Nothing to remove");
                }
            } else {
                model = getModel(slotIndex);
                this.mObjectSlots.remove(slotIndex);
                onRemoveModel(model, slotIndex);
                setDrawOrder(slotIndex);
            }
            if (this.mCloneList != null) {
                Iterator i$ = this.mCloneList.iterator();
                while (i$.hasNext()) {
                    TFObject o = i$.next();
                    ((TFHolder) o).removeModel(slotIndex);
                }
            }
            requestLayout();
        }
        return model;
    }

    public void setDelayedSetItemMode(boolean value) {
        this.mDelayedSetItemMode = value;
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
            this.mRefresh = false;
            this.mLayoutObjects = true;
            updated = true;
        }
        if (this.mDelayedSetItemMode && this.mSetItemNow) {
            this.mSetItemNow = false;
            this.mNeedToSetItem = false;
        }
        if ((this.mImageProvider != null || this.mItemProvider != null) && getItemCount() < getSlotCount() && this.mWraparound) {
            handlePartialEndlessLoop();
        }
        if (this.mLayoutObjects) {
            layoutObjects(this.mDeviation);
            this.mLayoutObjects = false;
            updated = true;
        }
        if (this.mOnUpdateHolderListener != null) {
            this.mOnUpdateHolderListener.onUpdateHolder(updated);
        }
        if (updated && this.mWorld != null) {
            this.mWorld.requestRender();
        }
        return ret;
    }

    private void handlePartialEndlessLoop() {
        if (getItemCount() < getSlotCount() && this.mWraparound) {
            int curSlotIndex = 0;
            int virtualHeadSlot = getHeadSlotIndex(getItemCount());
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer referenceWrapper = i$.next();
                if (curSlotIndex < getHeadSlotIndex() - virtualHeadSlot || (getHeadSlotIndex() + getItemCount()) - virtualHeadSlot <= curSlotIndex) {
                    setItemIndexOfObjectInSlot(referenceWrapper, -1);
                } else {
                    setItemIndexOfObjectInSlot(referenceWrapper, calculateItemIndexOfSlot(curSlotIndex - (getHeadSlotIndex() - virtualHeadSlot)));
                }
                curSlotIndex++;
            }
        }
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public boolean updateLayout(int tickPassed) {
        if (this.mMoveAni == null || !this.mMoveAni.update(tickPassed)) {
            return false;
        }
        return true;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public void layoutObjects(float headDeviation) {
        float p;
        int count = this.mObjectSlots.size();
        float t = headDeviation - ((int) headDeviation);
        float p1 = getModelPosition(0);
        float p2 = getModelPosition(-1);
        for (int i = 0; i < count; i++) {
            if (t < 0.0f) {
                p2 = getModelPosition(i + 1);
                p = ((1.0f - (-t)) * p1) + ((-t) * p2);
                p1 = p2;
            } else {
                p1 = getModelPosition(i);
                p = ((1.0f - t) * p1) + (t * p2);
                p2 = p1;
            }
            TFObject o = this.mObjectSlots.get(i).getObject();
            locateObject(o, p, i);
            if (this.mFadingEffect) {
                applyFadingEffect(o, i);
            }
        }
    }

    protected boolean shiftObjectsSingleSlot(int direction, boolean callListener) {
        int newItemIndex;
        boolean shiftSuccess = false;
        if (direction < 0) {
            int newItemIndex2 = adjustItemIndex(this.mHeadItemIndex - 1);
            if (this.mHeadItemIndex != newItemIndex2) {
                this.mHeadItemIndex = newItemIndex2;
                TFObjectContainer oc = this.mObjectSlots.removeLast();
                this.mObjectSlots.addFirst(oc);
                setItemIndexOfObjectInSlot(oc, calculateItemIndexOfSlot(0));
                shiftSuccess = true;
            }
        } else if (direction > 0 && this.mHeadItemIndex != (newItemIndex = adjustItemIndex(this.mHeadItemIndex + 1))) {
            this.mHeadItemIndex = newItemIndex;
            TFObjectContainer oc2 = this.mObjectSlots.removeFirst();
            this.mObjectSlots.addLast(oc2);
            setItemIndexOfObjectInSlot(oc2, calculateItemIndexOfSlot(getSlotCount() - 1));
            shiftSuccess = true;
        }
        if (direction != 0 && getItemCount() < getSlotCount() && this.mWraparound) {
            handlePartialEndlessLoop();
        }
        if (shiftSuccess) {
            if (this.mObjectShiftListener != null) {
                this.mWorld.queueEvent(this.mOnObjectShiftRunnable);
            }
            if (this.mDelayedSetItemMode) {
                this.mNeedToSetItem = true;
            }
        }
        return shiftSuccess;
    }

    protected int shiftObjectsMultipleSlots(int offset, boolean callListener) {
        if (offset > 0) {
            int i = 0;
            while (i < offset && shiftObjectsSingleSlot(1, callListener)) {
                i++;
            }
            return i;
        } else if (offset < 0) {
            int offset2 = offset * (-1);
            int i2 = 0;
            while (i2 < offset2 && shiftObjectsSingleSlot(-1, callListener)) {
                i2++;
            }
            return i2;
        } else {
            return 0;
        }
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.mOnMoveListener = onMoveListener;
    }

    public OnMoveListener getOnMoveListener() {
        return this.mOnMoveListener;
    }

    protected int scrollHeadItemIndex(int offset, boolean callListener) {
        int headIndex = this.mHeadItemIndex;
        int newIndex = headIndex + offset;
        if (offset != 0 && this.mDelayedSetItemMode) {
            this.mNeedToSetItem = true;
        }
        if (offset < 0) {
            if (!this.mWraparound && newIndex < 0) {
                offset = -headIndex;
            }
            for (int i = 0; i < (-offset); i++) {
                shiftObjectsSingleSlot(-1, callListener);
            }
        } else {
            if (!this.mWraparound && getItemCount() <= newIndex) {
                offset = (getItemCount() - 1) - this.mHeadItemIndex;
            }
            for (int i2 = 0; i2 < offset; i2++) {
                shiftObjectsSingleSlot(1, callListener);
            }
        }
        if (offset > 0) {
            this.mLayoutObjects = true;
            if (this.mWorld != null) {
                this.mWorld.requestRender();
            }
        }
        return offset;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    public boolean moveHeadModelStep(final float step, boolean bindingAction) {
        float nextStep = step + this.mDeviation;
        float prevDeviation = getDeviation();
        float oldHeadDeviation = this.mDeviation;
        int oldHeadItemIndex = this.mHeadItemIndex;
        float deviation = getDeviation(nextStep);
        int offset = getOffset(nextStep);
        int moved = scrollHeadItemIndex(offset, true);
        if (moved == offset) {
            this.mDeviation = deviation;
        } else {
            this.mDeviation = (0.45f * nextStep) / Math.abs(nextStep);
        }
        if (this.mDelayedSetItemMode && (this.mDeviation * oldHeadDeviation <= 0.0f || Math.abs(step) > 0.5f)) {
            boolean isShifted = false;
            if (oldHeadItemIndex != this.mHeadItemIndex) {
                isShifted = true;
            }
            if (this.mNeedToSetItem && (!isShifted || Math.abs(step) > 0.5f)) {
                this.mSetItemNow = true;
                refreshSlotIndex();
            }
        }
        if (prevDeviation != this.mDeviation) {
            this.mLayoutObjects = true;
            if (this.mWorld != null) {
                this.mWorld.requestRender();
            }
        }
        if (!this.mHolderBindings.isEmpty() && bindingAction) {
            if (this.mBindingBehaviorDispatcher != null) {
                this.mWorld.queueEvent(new Runnable() { // from class: com.nemustech.tiffany.world.TFSimpleHolder.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TFSimpleHolder.this.mBindingBehaviorDispatcher.bindingBehavior(step);
                    }
                });
            } else {
                this.mWorld.queueEvent(new Runnable() { // from class: com.nemustech.tiffany.world.TFSimpleHolder.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TFSimpleHolder.this.defaultBindingBehavior(step);
                    }
                });
            }
        }
        if (this.mOnMoveListener != null) {
            this.mOnMoveListener.onMove(step);
        }
        return offset == moved;
    }

    @Override // com.nemustech.tiffany.world.TFHolder
    /* renamed from: getMoveAnimation  reason: collision with other method in class */
    public SimpleMoveAnimation mo5getMoveAnimation() {
        return (SimpleMoveAnimation) this.mMoveAni;
    }

    /* loaded from: classes.dex */
    public class SimpleMoveAnimation extends TFHolder.MoveAnimation {
        public SimpleMoveAnimation(TFSimpleHolder holder) {
            super(holder);
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStart() {
            this.mInitialS = getHolder().getDeviation();
            this.mS = this.mInitialS;
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public boolean OnStop() {
            if (this.mAutoRepositionMode) {
                reset(false);
                boolean bKeepGoing = repositionObjects(this.mAutoRepositionDuration);
                return !bKeepGoing;
            }
            OnFrame();
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public void OnFrame() {
            float t = getTime();
            float S = calcS(this.mInitialS, this.mInitialV, this.mAcceleration, t);
            if (!getHolder().moveHeadModelStep(S - this.mS)) {
                if (this.mState != 2) {
                    stop();
                } else {
                    Log.d(TFSimpleHolder.TAG, "Attempt to call stop() on an animation wich stat is already STOP");
                }
            } else {
                this.mS = S;
            }
            getHolder().requestLayout();
        }
    }

    public void printSlots(String tag) {
        Log.d(tag, "-----------------------------------------------------------------------------------------");
        for (int i = 0; i < getSlotCount(); i++) {
            TFObject object = this.mObjectSlots.get(i).getObject();
            int itemIndex = object.getItemIndex();
            if (i == getHeadSlotIndex()) {
                Log.d(tag, "slot index: " + i + " item index is: " + itemIndex + " mDescription is: " + object.mDescription + " <-- head");
            } else {
                Log.d(tag, "slot index: " + i + " item index is: " + itemIndex + " mDescription is: " + object.mDescription);
            }
        }
    }
}
