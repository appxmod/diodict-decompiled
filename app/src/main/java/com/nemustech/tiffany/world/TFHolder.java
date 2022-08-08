package com.nemustech.tiffany.world;

import android.opengl.Matrix;
import android.util.Log;
import android.util.SparseIntArray;
import com.nemustech.tiffany.world.TFAnimation;
import com.nemustech.tiffany.world.TFWorld;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public abstract class TFHolder extends TFObject {
    private static final String TAG = "TFHolder";
    BindingBehaviorDispatcher mBindingBehaviorDispatcher;
    TFHolder mCurrentActingHolder;
    float mDeceleration;
    boolean mDelayedSetItemMode;
    float mDeviation;
    protected boolean mDragged;
    SparseIntArray mDrawOrder;
    TFObject mDummy;
    boolean mFacingFrontYFirst;
    int mHeadItemIndex;
    private int mHeadSlotIndex;
    LinkedList<TFHolder> mHolderBindings;
    ImageProvider mImageProvider;
    TFItemProvider<?> mItemProvider;
    boolean mLayoutObjects;
    protected float mMinorDrag;
    protected MoveAnimation mMoveAni;
    boolean mNeedToSetItem;
    OnObjectShiftListener mObjectShiftListener;
    protected LinkedList<TFObjectContainer> mObjectSlots;
    Runnable mOnObjectShiftRunnable;
    protected OnUpdateHolderListener mOnUpdateHolderListener;
    boolean mRefresh;
    float mSensitivity;
    OnSetItemListener mSetItemListener;
    boolean mSetItemNow;
    protected float mTOfHitPoint;
    private boolean mTerminalHolder;
    protected boolean mTouchDown;
    protected float mVelocity;
    protected boolean mWraparound;

    /* loaded from: classes.dex */
    public interface BindingBehaviorDispatcher {
        void bindingBehavior(float f);
    }

    /* loaded from: classes.dex */
    public interface ImageProvider {
        void cancelImage(int i);

        int getTotalImageCount();

        void setImage(TFModel tFModel, int i);
    }

    /* loaded from: classes.dex */
    public interface OnObjectShiftListener {
        void onObjectShift(int i);
    }

    /* loaded from: classes.dex */
    public interface OnSetItemListener {
        void onSetItemEnd(TFObjectContainer tFObjectContainer, int i);
    }

    /* loaded from: classes.dex */
    public interface OnUpdateHolderListener {
        void onUpdateHolder(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean addHolder(TFHolder tFHolder, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean addModel(TFModel tFModel, int i);

    public abstract int getHeadSlotIndex(int i);

    public abstract void layoutObjects(float f);

    public abstract boolean moveHeadModelStep(float f, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract TFHolder removeHolder(int i, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract TFModel removeModel(int i, boolean z);

    public abstract void setHeadItemIndex(int i, boolean z);

    public abstract boolean updateLayout(int i);

    public TFHolder() {
        this.mOnObjectShiftRunnable = new Runnable() { // from class: com.nemustech.tiffany.world.TFHolder.2
            @Override // java.lang.Runnable
            public void run() {
                TFHolder.this.mObjectShiftListener.onObjectShift(TFHolder.this.mHeadItemIndex);
            }
        };
        this.mSensitivity = 1.0f;
        this.mDeceleration = 5.0E-5f;
        this.mTerminalHolder = true;
        this.mVisible = true;
        this.mShouldDraw = true;
        this.mWraparound = false;
        this.mObjectSlots = new LinkedList<>();
        this.mObjectSlots.clear();
        this.mHolderBindings = new LinkedList<>();
        this.mHolderBindings.clear();
        this.mHeadItemIndex = 0;
        this.mHeadSlotIndex = -1;
        this.mDragged = false;
        this.mTouchDown = false;
        this.mMinorDrag = 0.0f;
        this.mDeviation = 0.0f;
        this.mDelayedSetItemMode = false;
        this.mNeedToSetItem = false;
        this.mSetItemNow = false;
        if (!this.mTerminalHolder) {
            this.mDummy = new TFPlaceHolder();
            this.mDummy.mDescription = "dummy";
        }
    }

    public TFHolder(boolean isTerminalHolder) {
        this();
        setTerminalHolder(isTerminalHolder);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public Object clone() throws CloneNotSupportedException {
        TFHolder clone = (TFHolder) super.clone();
        Log.d("CLONE", "cloning holder: " + this + " -> " + clone);
        clone.mCloneList = null;
        clone.mBindingBehaviorDispatcher = null;
        clone.mObjectShiftListener = null;
        clone.mHolderBindings = new LinkedList<>();
        clone.mCurrentActingHolder = null;
        clone.mTouchDown = false;
        clone.mMinorDrag = 0.0f;
        clone.mDragged = false;
        clone.mTOfHitPoint = 0.0f;
        clone.mVelocity = 0.0f;
        clone.mMoveAni = null;
        clone.mDeviation = 0.0f;
        clone.mDrawOrder = new SparseIntArray();
        if (this.mItemProvider == null) {
            clone.mObjectSlots = new LinkedList<>();
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer oc = (TFObjectContainer) i$.next();
                if (this.mTerminalHolder) {
                    clone.addModel((TFModel) oc.getObject().clone(), clone.mObjectSlots.size());
                } else {
                    clone.addHolder((TFHolder) oc.getObject().clone(), clone.mObjectSlots.size());
                }
            }
            if (this.mImageProvider != null) {
                clone.mImageProvider = this.mImageProvider;
            }
        } else {
            clone.mObjectSlots = new LinkedList<>();
            clone.mItemProvider = this.mItemProvider.getClone(clone);
            if (clone.mItemProvider instanceof TFObjectProvider) {
                clone.addSlots(getSlotCount());
            } else {
                Iterator i$2 = this.mObjectSlots.iterator();
                while (i$2.hasNext()) {
                    TFObjectContainer oc2 = (TFObjectContainer) i$2.next();
                    if (this.mTerminalHolder) {
                        clone.addModel((TFModel) oc2.getObject().clone());
                    } else {
                        clone.addHolder((TFHolder) oc2.getObject().clone());
                    }
                }
            }
        }
        clone.mHeadItemIndex = this.mHeadItemIndex;
        clone.refreshHolder();
        bindHolder(clone);
        clone.bindHolder(this);
        Log.d("BINDING", "Holder binding between " + this + " and " + clone);
        Log.d("BINDING", "Worlds " + this.mWorld + " and " + clone.mWorld);
        Log.d("world", "Cloning... This is: " + this + " clone is: " + clone);
        Log.d("world", "TFWorld clone status - this.mWorld: " + this.mWorld + " clone.mWorld: " + clone.mWorld);
        return clone;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void attachTo(TFHolder parentHolder, int index) {
        if (this.mParentHolder != null) {
            throw new IllegalStateException("Has already parent holder");
        }
        TFWorld.Layer layer = getLayer();
        if (layer != null) {
            layer.remove(this);
            setLayer(null);
        }
        parentHolder.addHolder(this, index);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void attachTo(TFHolder parentHolder) {
        attachTo(parentHolder, parentHolder.getSlotCount());
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void attachTo(TFWorld world) {
        if (this.mParentHolder != null) {
            throw new IllegalStateException("Detach from its parent holder first");
        }
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }
        this.mWorld = world;
        Iterator i$ = this.mObjectSlots.iterator();
        while (i$.hasNext()) {
            TFObjectContainer o = (TFObjectContainer) i$.next();
            if (o.getObject() != null) {
                if (o.getObject() instanceof TFModel) {
                    o.getObject().mWorld = this.mWorld;
                } else {
                    ((TFHolder) o.getObject()).associateToWorld(this.mWorld);
                }
            }
        }
        world.mRenderer.add(this);
        if (this.mCloneList != null) {
            Iterator i$2 = this.mCloneList.iterator();
            while (i$2.hasNext()) {
                ((TFHolder) i$2.next()).attachTo(world);
            }
        }
        Log.i(TAG, "Holder(" + this + ") has been attatched");
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void detachFrom(TFWorld world) {
        deleteAllImageResource();
        super.detachFrom(world);
    }

    private void deleteAllImageResource() {
        if (isTerminalHolder()) {
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer referenceWrapper = (TFObjectContainer) i$.next();
                ((TFModel) referenceWrapper.getObject()).deleteAllImageResource();
            }
            return;
        }
        Iterator i$2 = this.mObjectSlots.iterator();
        while (i$2.hasNext()) {
            TFObjectContainer referenceWrapper2 = (TFObjectContainer) i$2.next();
            ((TFHolder) referenceWrapper2.getObject()).deleteAllImageResource();
        }
    }

    public int getItemCount() {
        if (this.mImageProvider != null) {
            return this.mImageProvider.getTotalImageCount();
        }
        if (this.mItemProvider != null) {
            return this.mItemProvider.getItemCount();
        }
        return this.mObjectSlots.size();
    }

    public void setEndlessMode(boolean endless) {
        this.mWraparound = endless;
        refreshHolder();
        requestLayout();
    }

    public boolean isEndlessMode() {
        return this.mWraparound;
    }

    public void setSetItemFinishListener(OnSetItemListener setItemFinishListener) {
        this.mSetItemListener = setItemFinishListener;
    }

    public void setObjectShiftListener(OnObjectShiftListener modelChangeListener) {
        this.mObjectShiftListener = modelChangeListener;
    }

    public void setUpdateHolderListener(OnUpdateHolderListener updateHolderListener) {
        this.mOnUpdateHolderListener = updateHolderListener;
    }

    public OnUpdateHolderListener getUpdateHolderListener() {
        return this.mOnUpdateHolderListener;
    }

    public void setImageProvider(ImageProvider iProvider) {
        this.mImageProvider = iProvider;
    }

    public void setItemProvider(TFItemProvider<?> iProvider) {
        this.mItemProvider = iProvider;
        if (this.mItemProvider != null) {
            this.mItemProvider.setClientHolder(this);
        }
        this.mItemProvider.associateTo(this);
        if (this.mHeadItemIndex < 0) {
            setHeadItemIndex(0, false);
        }
        refreshHolder();
    }

    public TFItemProvider getItemProvider() {
        return this.mItemProvider;
    }

    public int getHeadItemIndex() {
        if (getHeadSlotIndex() >= 0 && getHeadSlotIndex() < getSlotCount()) {
            TFObject object = this.mObjectSlots.get(this.mHeadSlotIndex).getObject();
            if (object != null) {
                return object.getItemIndex();
            }
            throw new IllegalArgumentException("No object in Head Slot Index of Holder.");
        }
        throw new IllegalArgumentException("Head Slot index out of bounds. Slot Count: " + getSlotCount() + " Head Slot Index: " + getHeadSlotIndex());
    }

    public TFPlaceHolder createPlaceHolder() {
        TFPlaceHolder newHolder = new TFPlaceHolder();
        newHolder.setAutoGeneratedFlag(true);
        for (int i = 0; i < 3; i++) {
            newHolder.mLocation[i] = this.mLocation[i];
            newHolder.mAngle[i] = this.mAngle[i];
        }
        newHolder.mShouldRotateFirst = this.mShouldRotateFirst;
        newHolder.mRotateYFirst = this.mRotateYFirst;
        newHolder.mFacingFrontYFirst = this.mFacingFrontYFirst;
        return newHolder;
    }

    public void setBindingBehaviorDispatcher(BindingBehaviorDispatcher dispatcher) {
        this.mBindingBehaviorDispatcher = dispatcher;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void defaultBindingBehavior(float step) {
        Iterator i$ = this.mHolderBindings.iterator();
        while (i$.hasNext()) {
            TFHolder holder = (TFHolder) i$.next();
            holder.moveHeadModelStep(step, false);
        }
    }

    protected void setCurrentActingHolder(TFHolder activeHolder) {
        this.mCurrentActingHolder = activeHolder;
    }

    void onBindingAnimationStart(TFAnimation animation) {
        TFHolder holder = (TFHolder) animation.getSubject();
        LinkedList<TFHolder> holderBindings = holder.getHolderBindings();
        this.mCurrentActingHolder = holder;
        Iterator i$ = holderBindings.iterator();
        while (i$.hasNext()) {
            TFHolder m = (TFHolder) i$.next();
            m.setCurrentActingHolder(holder);
            m.getMoveAnimation().setTempAutoRepositionMode(false);
            m.getMoveAnimation().stop();
        }
    }

    void onBindingAnimationEnd(TFAnimation animation) {
        LinkedList<TFHolder> holderBindings = ((TFHolder) animation.getSubject()).getHolderBindings();
        Iterator i$ = holderBindings.iterator();
        while (i$.hasNext()) {
            TFHolder m = (TFHolder) i$.next();
            if (!m.equals(this.mCurrentActingHolder)) {
                m.getMoveAnimation().setOriginalAutoRepositionMode();
            }
        }
    }

    public void bindHolder(TFHolder holder) {
        if (this.mHolderBindings == null) {
            this.mHolderBindings = new LinkedList<>();
        }
        this.mHolderBindings.add(holder);
    }

    public LinkedList<TFHolder> getHolderBindings() {
        return this.mHolderBindings;
    }

    public boolean unbindHolder(TFHolder holder) {
        if (this.mHolderBindings.contains(holder)) {
            return this.mHolderBindings.remove(holder);
        }
        return false;
    }

    public void clearHolderBindings() {
        this.mHolderBindings.clear();
    }

    protected void onTouchDown(TFModel selectedModel, float x, float y) {
    }

    protected void onTouchUp(TFModel selectedModel, float x, float y) {
    }

    protected void onTouchDrag(TFModel selectedModel, float start_x, float start_y, float end_x, float end_y, int tickPassed) {
        float[] p = new float[32];
        float[] hitLine = new float[8];
        this.mWorld.mRenderer.getHitTestLine(start_x, start_y, hitLine);
        TFVector3D.getPointOnLine(p, 8, hitLine, 0, hitLine, 4, this.mTOfHitPoint);
        this.mWorld.mRenderer.getHitTestLine(end_x, end_y, hitLine);
        TFVector3D.getPointOnLine(p, 12, hitLine, 0, hitLine, 4, this.mTOfHitPoint);
        Matrix.invertM(p, 16, this.mMatrix, 0);
        TFVector3D.setW(p, 8);
        Matrix.multiplyMV(p, 0, p, 16, p, 8);
        TFVector3D.setW(p, 12);
        Matrix.multiplyMV(p, 4, p, 16, p, 12);
        for (int i = 0; i < 8; i++) {
            if (Float.isNaN(p[i])) {
                Log.d(TAG, String.format("(%f,%f,%f,%f)->(%f,%f,%f,%f), %d", Float.valueOf(p[0]), Float.valueOf(p[1]), Float.valueOf(p[2]), Float.valueOf(p[3]), Float.valueOf(p[4]), Float.valueOf(p[5]), Float.valueOf(p[6]), Float.valueOf(p[7]), Integer.valueOf(tickPassed)));
                Log.d(TAG, String.format("(%f,%f,%f,%f)->(%f,%f,%f,%f), %d", Float.valueOf(p[8]), Float.valueOf(p[9]), Float.valueOf(p[10]), Float.valueOf(p[11]), Float.valueOf(p[12]), Float.valueOf(p[13]), Float.valueOf(p[14]), Float.valueOf(p[15]), Integer.valueOf(tickPassed)));
                Log.d(TAG, "TOUCH CANCELLED");
                return;
            }
        }
        applyTouchEvent(selectedModel, p, tickPassed);
    }

    protected void doBindingAction(float start_x, float start_y, float end_x, float end_y, int tickPassed) {
    }

    protected void cancelImage(int itemIndex) {
        if (this.mImageProvider != null) {
            this.mImageProvider.cancelImage(itemIndex);
        }
    }

    protected int calcIndex(int baseIndex, int gap, boolean bEndless) {
        int itemCount = getItemCount();
        int newIndex = baseIndex + (gap % itemCount);
        if (bEndless) {
            return (newIndex + itemCount) % itemCount;
        }
        if (newIndex < 0) {
            return 0;
        }
        return newIndex >= itemCount ? itemCount - 1 : newIndex;
    }

    protected int diffIndex(int indexA, int indexB, boolean bEndless) {
        int gap = indexA - indexB;
        int itemCount = getItemCount();
        if (bEndless) {
            int gap2 = Math.abs(gap) % itemCount;
            if (gap2 > itemCount / 2) {
                gap2 = itemCount - gap2;
            }
            return indexA < indexB ? -gap2 : gap2;
        }
        return gap;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int adjustItemIndex(int itemIndex) {
        int itemCount = getItemCount();
        if (itemCount > 0) {
            if (itemIndex >= 0 && itemIndex < itemCount) {
                return itemIndex;
            }
            if (!this.mWraparound) {
                return -1;
            }
            while (itemIndex < 0) {
                itemIndex += itemCount;
            }
            while (itemIndex >= itemCount) {
                itemIndex -= itemCount;
            }
            return itemIndex;
        }
        return -1;
    }

    protected int getItemOffset(int itemIndex1, int itemIndex2) {
        int off;
        int itemCount = getItemCount();
        int offset = itemIndex2 - itemIndex1;
        if (this.mWraparound) {
            if (offset > 0) {
                off = offset - itemCount;
            } else {
                off = offset + itemCount;
            }
            if (Math.abs(off) < Math.abs(offset)) {
                return off;
            }
            return offset;
        }
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getItemOffsetToHead(int itemIndex) {
        return getItemOffset(getHeadItemIndex(), itemIndex);
    }

    public void setTerminalHolder(boolean terminalHolder) {
        this.mTerminalHolder = terminalHolder;
        if (this.mDummy == null) {
            this.mDummy = new TFPlaceHolder();
            this.mDummy.mDescription = "dummy";
        }
    }

    public boolean isTerminalHolder() {
        return this.mTerminalHolder;
    }

    public void addSlots(int numSlots) {
        for (int i = 0; i < numSlots; i++) {
            TFObjectContainer oc = new TFObjectContainer();
            if (this.mDummy == null) {
                this.mDummy = new TFObject();
            }
            oc.setObject(this.mDummy);
            this.mObjectSlots.add(oc);
            setDrawOrder(i);
        }
        setHeadSlotIndex(getHeadSlotIndex(numSlots));
        onAddSlots(numSlots);
    }

    public void removeSlots(int numSlots) {
        if (this.mObjectSlots.size() >= numSlots) {
            this.mObjectSlots.removeLast();
        }
    }

    boolean addHolder(TFHolder holder) {
        return addHolder(holder, getSlotCount() - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFHolder removeHolder(int index) {
        return removeHolder(index, false);
    }

    boolean addModel(TFModel model) {
        return addModel(model, getSlotCount() - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFModel removeModel(int index) {
        return removeModel(index, false);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void associateToWorld(TFWorld world) {
        this.mWorld = world;
        if (this.mTerminalHolder) {
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer o = (TFObjectContainer) i$.next();
                TFModel model = (TFModel) o.getObject();
                model.mWorld = world;
            }
            return;
        }
        Iterator i$2 = this.mObjectSlots.iterator();
        while (i$2.hasNext()) {
            TFObjectContainer o2 = (TFObjectContainer) i$2.next();
            TFHolder holder = (TFHolder) o2.getObject();
            holder.associateToWorld(world);
        }
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void associateToHolder(TFHolder holder) {
        this.mParentHolder = holder;
        associateToWorld(holder.mWorld);
    }

    public boolean setHolder(TFHolder holder, int slotIndex) {
        if (!this.mTerminalHolder && slotIndex < this.mObjectSlots.size()) {
            this.mObjectSlots.get(slotIndex).setObject(holder);
            if (this.mCloneList != null) {
                Iterator i$ = this.mCloneList.iterator();
                while (i$.hasNext()) {
                    TFObject o = (TFObject) i$.next();
                    try {
                        ((TFHolder) o).setHolder((TFHolder) holder.clone(), slotIndex);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean setModel(TFModel model, int slotIndex) {
        if (this.mTerminalHolder && slotIndex <= this.mObjectSlots.size()) {
            this.mObjectSlots.get(slotIndex).setObject(model);
            if (this.mCloneList != null) {
                Iterator i$ = this.mCloneList.iterator();
                while (i$.hasNext()) {
                    TFObject o = (TFObject) i$.next();
                    try {
                        ((TFHolder) o).setModel((TFModel) model.clone(), slotIndex);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Deprecated
    public int getModelCount() {
        return this.mObjectSlots.size();
    }

    public int getSlotCount() {
        return this.mObjectSlots.size();
    }

    public TFObject getObjectInSlot(int idx) {
        if (idx >= getSlotCount() || getSlotCount() == 0) {
            return null;
        }
        return this.mObjectSlots.get(idx).getObject();
    }

    @Deprecated
    public TFModel getModel(int index) {
        return getModelInSlot(index);
    }

    public TFModel getModelInSlot(int index) {
        if (this.mTerminalHolder) {
            return (TFModel) getObjectInSlot(index);
        }
        throw new IllegalArgumentException("The holder which getModel() was called on is not a terminal holder, and does note contain any model");
    }

    @Deprecated
    public TFHolder getHolder(int index) {
        if (!this.mTerminalHolder) {
            return (TFHolder) getObjectInSlot(index);
        }
        throw new IllegalArgumentException("The holder which getModel() was called on is a terminal holder, and does not contain any holder.");
    }

    public TFHolder getHolderInSlot(int index) {
        if (!this.mTerminalHolder) {
            return (TFHolder) getObjectInSlot(index);
        }
        throw new IllegalArgumentException("The holder which getModel() was called on is a terminal holder, and does not contain any holder.");
    }

    public TFObject getObjectInSlotByItemIndex(int itemIdx) {
        Iterator i$ = this.mObjectSlots.iterator();
        while (i$.hasNext()) {
            TFObjectContainer m = (TFObjectContainer) i$.next();
            if (m.getObject().getItemIndex() == itemIdx) {
                return m.getObject();
            }
        }
        return null;
    }

    @Deprecated
    public TFModel getModelByItemIndex(int itemIdx) {
        if (this.mTerminalHolder) {
            return (TFModel) getObjectInSlotByItemIndex(itemIdx);
        }
        return null;
    }

    public TFModel getModelInSlotByItemIndex(int itemIdx) {
        if (this.mTerminalHolder) {
            return (TFModel) getObjectInSlotByItemIndex(itemIdx);
        }
        return null;
    }

    public TFHolder getHolderByItemIndex(int itemIdx) {
        if (!this.mTerminalHolder) {
            return (TFHolder) getObjectInSlotByItemIndex(itemIdx);
        }
        return null;
    }

    @Deprecated
    public int getModelIndex(TFModel model) {
        return this.mObjectSlots.indexOf(model.getWrapperObject());
    }

    public int getSlotIndex(TFObject object) {
        return this.mObjectSlots.indexOf(object.getWrapperObject());
    }

    public TFModel getHeadModel() {
        if (this.mTerminalHolder) {
            return (TFModel) getHeadObject();
        }
        return null;
    }

    public TFHolder getHeadHolder() {
        if (!this.mTerminalHolder) {
            return (TFHolder) getHeadObject();
        }
        return null;
    }

    public TFObject getHeadObject() {
        return this.mObjectSlots.get(this.mHeadSlotIndex).getObject();
    }

    @Deprecated
    public int getHeadModelIndex() {
        return this.mHeadSlotIndex;
    }

    public int getHeadSlotIndex() {
        return this.mHeadSlotIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setHeadSlotIndex(int index) {
        if (getSlotCount() > 0) {
            if (index >= 0 && index < getSlotCount()) {
                this.mHeadSlotIndex = index;
                this.mHeadItemIndex = getHeadItemIndex();
                return;
            }
            this.mHeadSlotIndex = -1;
            this.mHeadItemIndex = -1;
            throw new IllegalArgumentException("New head slot index value is out of bounds");
        } else if (index == -1) {
            this.mHeadSlotIndex = -1;
            this.mHeadItemIndex = -1;
        } else {
            throw new IllegalArgumentException("New head slot index value must be -1 when there are no slots in holder - index is: " + index);
        }
    }

    public void arrangeModels() {
        Log.w(TAG, "This holder did not override 'arrangeModels()' method!");
    }

    void genTexture(GL10 gl) {
        if (this.mTerminalHolder) {
            for (int i = 0; i < this.mObjectSlots.size(); i++) {
                TFModel model = (TFModel) this.mObjectSlots.get(i).getObject();
                if (model != null) {
                    model.genTexture(gl);
                }
            }
            return;
        }
        Iterator i$ = this.mObjectSlots.iterator();
        while (i$.hasNext()) {
            TFObjectContainer oc = (TFObjectContainer) i$.next();
            TFHolder holder = (TFHolder) oc.getObject();
            if (holder != null) {
                holder.genTexture(gl);
            }
        }
    }

    public void refreshHolder() {
        this.mRefresh = true;
        refreshSlotIndex();
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setItemIndexOfObjectInSlot(final TFObjectContainer referenceWrapper, final int itemIndex) {
        int oldItemIndex;
        TFObject object = referenceWrapper.getObject();
        if (object != null) {
            oldItemIndex = object.getItemIndex();
        } else {
            referenceWrapper.setObject(this.mDummy);
            object = referenceWrapper.getObject();
            oldItemIndex = -1;
        }
        if (itemIndex < 0) {
            object.setItemIndex(-1);
            object.mShouldDraw = false;
            return;
        }
        boolean setFlag = this.mDelayedSetItemMode ? (oldItemIndex != itemIndex && this.mNeedToSetItem && this.mSetItemNow) || this.mRefresh : oldItemIndex != itemIndex || this.mRefresh;
        if (setFlag) {
            if (this.mTerminalHolder) {
                if (this.mImageProvider != null) {
                    TFModel model = (TFModel) object;
                    model.deleteAllImageResource();
                    this.mImageProvider.setImage(model, itemIndex);
                } else if (this.mItemProvider != null) {
                    this.mItemProvider.setItem(referenceWrapper, itemIndex);
                    object = referenceWrapper.getObject();
                }
                object.mShouldDraw = true;
                object.setItemIndex(itemIndex);
            } else if (this.mItemProvider == null) {
                object.mShouldDraw = true;
                object.setItemIndex(itemIndex);
            } else {
                referenceWrapper.clearObject();
                this.mItemProvider.setItem(referenceWrapper, itemIndex);
                TFObject object2 = referenceWrapper.getObject();
                if (object2 == null) {
                    referenceWrapper.setObject(this.mDummy);
                    TFObject object3 = referenceWrapper.getObject();
                    object3.mShouldDraw = false;
                    object3.setItemIndex(-1);
                } else {
                    object2.mShouldDraw = true;
                    object2.setItemIndex(itemIndex);
                }
            }
            if (this.mSetItemListener != null) {
                this.mWorld.queueEvent(new Runnable() { // from class: com.nemustech.tiffany.world.TFHolder.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TFHolder.this.mSetItemListener.onSetItemEnd(referenceWrapper, itemIndex);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void refreshSlotIndex() {
        int slotIndex = 0;
        Iterator i$ = this.mObjectSlots.iterator();
        while (i$.hasNext()) {
            TFObjectContainer slot = (TFObjectContainer) i$.next();
            setItemIndexOfObjectInSlot(slot, calculateItemIndexOfSlot(slotIndex));
            slotIndex++;
        }
    }

    @Deprecated
    public int getItemIndexOfModel(int modelIdx) {
        return getItemIndexOfSlot(modelIdx);
    }

    public int getItemIndexOfSlot(int slotIdx) {
        return this.mObjectSlots.get(slotIdx).getObject().getItemIndex();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int calculateItemIndexOfSlot(int slotIdx) {
        if (this.mHeadSlotIndex >= 0) {
            int itemIdx = this.mHeadItemIndex + (slotIdx - this.mHeadSlotIndex);
            return adjustItemIndex(itemIdx);
        }
        return slotIdx;
    }

    public TFHolder searchForChildObject(TFObject object) {
        TFHolder toReturn = null;
        Iterator i$ = this.mObjectSlots.iterator();
        while (i$.hasNext()) {
            TFObjectContainer m = (TFObjectContainer) i$.next();
            if (m.getObject() != null) {
                if (m.getObject() instanceof TFModel) {
                    if (m.getObject() == object) {
                        TFHolder toReturn2 = m.getObject().getParentHolder();
                        return toReturn2;
                    }
                } else {
                    toReturn = ((TFHolder) m.getObject()).searchForChildObject(object);
                    if (toReturn != null) {
                        return toReturn;
                    }
                }
            }
        }
        return toReturn;
    }

    @Deprecated
    public boolean setHeadDeviation(float deviation) {
        if (deviation > 0.5f || deviation < -0.5f) {
            return false;
        }
        this.mDeviation = deviation;
        return true;
    }

    public boolean setDeviation(float deviation) {
        if (deviation > 0.5f || deviation < -0.5f) {
            return false;
        }
        this.mDeviation = deviation;
        return true;
    }

    @Deprecated
    public float getHeadDeviation() {
        return this.mDeviation;
    }

    public float getDeviation() {
        return this.mDeviation;
    }

    public boolean moveHeadModelStep(float step) {
        return moveHeadModelStep(step, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getDeviation(float step) {
        float deviation = step - ((int) step);
        if (deviation > 0.5f) {
            return deviation - 1.0f;
        }
        if (deviation < -0.5f) {
            return deviation + 1.0f;
        }
        return deviation;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getOffset(float step) {
        float deviation = step - ((int) step);
        if (deviation > 0.5f) {
            step += 0.5f;
        } else if (deviation < -0.5f) {
            step -= 0.5f;
        }
        return (int) step;
    }

    public void requestLayout() {
        this.mLayoutObjects = true;
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    protected void dumpObjectInSlots() {
        int i = 0;
        Log.d(TAG, String.format("-------------------------------------", new Object[0]));
        Iterator i$ = this.mObjectSlots.iterator();
        while (i$.hasNext()) {
            TFObjectContainer o = (TFObjectContainer) i$.next();
            Log.d(TAG, String.format("dumpModelList - slotIndex: %d, itemIndex: %d", Integer.valueOf(i), Integer.valueOf(o.getObject().getItemIndex())));
            i++;
        }
    }

    private void renderChild(GL10 gl, int tickPassed, TFObjectContainer o) {
        gl.glPushMatrix();
        if (o.getObject() instanceof TFHolder) {
            TFHolder holder = (TFHolder) o.getObject();
            holder.draw(gl, tickPassed);
            holder.checkEffectFinish();
        } else if (o.getObject() instanceof TFModel) {
            TFModel model = (TFModel) o.getObject();
            model._draw(gl, tickPassed);
            model.checkEffectFinish();
        }
        gl.glPopMatrix();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void draw(GL10 gl, int tickPassed) {
        if (this.mWorld != null && this.mVisible && this.mShouldDraw) {
            if (updateObject(gl, tickPassed, true)) {
            }
            applyOpacity(gl, tickPassed);
            if (!this.mWorld.mBanQueryingMatrix) {
                ((TFGL) gl).getMatrix(this.mMatrix, 0);
            }
            if (this.mWorld.mBlendingSort) {
                LinkedList<TFObjectContainer> objects = new LinkedList<>(this.mObjectSlots);
                Collections.sort(objects, TFObjectContainer.compareAxisZ);
                Iterator i$ = objects.iterator();
                while (i$.hasNext()) {
                    TFObjectContainer o = (TFObjectContainer) i$.next();
                    renderChild(gl, tickPassed, o);
                }
                return;
            }
            LinkedList<TFObjectContainer> objects2 = this.mObjectSlots;
            int objectCount = this.mObjectSlots.size();
            if (this.mDrawOrder != null && this.mDrawOrder.size() != objectCount) {
                throw new IllegalStateException("DrawingCount does not match with ObjectSlot count.");
            }
            for (int i = 0; i < objectCount; i++) {
                TFObjectContainer o2 = objects2.get(this.mDrawOrder == null ? i : this.mDrawOrder.get(i));
                renderChild(gl, tickPassed, o2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleDown(TFModel selectedModel, int faceIndex, float x, float y) {
        if (!selectedModel.handleDown(faceIndex, x, y)) {
            if (!this.mMoveAni.hasEnded()) {
                this.mMoveAni.reset(false);
            }
            this.mTouchDown = true;
            this.mTOfHitPoint = TFVector3D.getTOfPointOnLine(selectedModel.mHitPoint, 0, selectedModel.mHitTestLine, 0, selectedModel.mHitTestLine, 4);
            if (Float.isNaN(this.mTOfHitPoint)) {
                Log.d(TAG, String.format("INVALID POINT (%f,%f,%f)", Float.valueOf(selectedModel.mHitPoint[4]), Float.valueOf(selectedModel.mHitPoint[5]), Float.valueOf(selectedModel.mHitPoint[6])));
                this.mTouchDown = false;
                return;
            }
            onTouchDown(selectedModel, x, y);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleUp(TFModel selectedModel, float x, float y) {
        if (!selectedModel.handleUp(x, y)) {
            this.mTouchDown = false;
            if (this.mVelocity != 0.0f) {
                this.mMoveAni.startMoveAnimation(this.mVelocity, this.mDeceleration);
            } else {
                if (this.mDragged) {
                    this.mMoveAni.reset(false);
                }
                if (this.mMoveAni.mAutoRepositionMode) {
                    this.mMoveAni.repositionObjects(this.mMoveAni.mAutoRepositionDuration);
                }
            }
            this.mMinorDrag = 0.0f;
            this.mDragged = false;
            this.mVelocity = 0.0f;
            if (this.mParentHolder != null) {
                this.mParentHolder.handleUp(selectedModel, x, y);
            } else {
                onTouchUp(selectedModel, x, y);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleDrag(TFModel selectedModel, float startX, float startY, float endX, float endY, int tickPassed) {
        if (!selectedModel.handleDrag(startX, startY, endX, endY, tickPassed) && this.mTouchDown && tickPassed > 0) {
            onTouchDrag(selectedModel, startX, startY, endX, endY, tickPassed);
        }
    }

    protected float calcTouchVectorMagnitude(float[] forceVector, int tickPassed) {
        return (float) Math.sqrt(Math.pow(forceVector[4] - forceVector[0], 2.0d) + Math.pow(forceVector[5] - forceVector[1], 2.0d) + Math.pow(forceVector[6] - forceVector[0], 2.2d));
    }

    public void applyTouchEvent(TFObject object, float[] forceVector, int tickPassed) {
        float dx = calcTouchVectorMagnitude(forceVector, tickPassed);
        if (Float.isNaN(dx)) {
            Log.d(TAG, String.format("tickPassed: %d", Integer.valueOf(tickPassed)));
        }
        float dx2 = dx * this.mSensitivity;
        if (Math.abs(this.mMinorDrag + dx2) > 0.1f) {
            this.mDragged = true;
            this.mVelocity = (this.mMinorDrag + dx2) / tickPassed;
            moveHeadModelStep(this.mMinorDrag + dx2);
        } else {
            this.mMinorDrag += dx2;
            this.mVelocity = 0.0f;
        }
        if (this.mParentHolder != null) {
            this.mParentHolder.applyTouchEvent(object, forceVector, tickPassed);
        }
    }

    protected void onSetDrawOrder(SparseIntArray drawOrder, int modelIndex) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDrawOrder(int modelIndex) {
        if (this.mDrawOrder == null) {
            this.mDrawOrder = new SparseIntArray();
        }
        int size = this.mDrawOrder.size();
        if (modelIndex < size) {
            int removedOrder = this.mDrawOrder.get(size - 1);
            this.mDrawOrder.removeAt(size - 1);
            int newSize = this.mDrawOrder.size();
            for (int i = 0; i < newSize; i++) {
                if (this.mDrawOrder.get(i) > removedOrder) {
                    this.mDrawOrder.put(i, this.mDrawOrder.get(i) - 1);
                }
            }
        } else if (modelIndex == size) {
            this.mDrawOrder.append(modelIndex, modelIndex);
        } else {
            throw new IllegalArgumentException("Invalid model Index onSetDrawOrder");
        }
        onSetDrawOrder(this.mDrawOrder, modelIndex);
        if (this.mObjectSlots.size() != this.mDrawOrder.size()) {
            throw new IllegalStateException("DrawingCount does not match with ObjectSlot count.");
        }
    }

    protected void onAddSlots(int addedSlots) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAddHolder(TFObject object, int modelIndex) {
        onAddModel(object, modelIndex);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRemoveHolder(TFObject object, int modelIndex) {
        onRemoveModel(object, modelIndex);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAddModel(TFObject object, int modelIndex) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRemoveModel(TFObject object, int modelIndex) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean updateObject(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        return super.updateObject(gl, tickPassed, bDoGLCalc);
    }

    public boolean isTouchDown() {
        return this.mTouchDown;
    }

    public float getDeceleration() {
        return this.mDeceleration;
    }

    public void setDeceleration(float deceleration) {
        this.mDeceleration = deceleration;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public float getSensitivity() {
        return this.mSensitivity;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public boolean getLockStatus() {
        if (this.mParentHolder == null) {
            return super.getLockStatus();
        }
        if (!super.getLockStatus()) {
            return this.mParentHolder.getLockStatus();
        }
        return true;
    }

    public void printHolderState() {
        printHolderState(0);
    }

    public void printHolderState(int level, boolean absolute) {
        float[] location = null;
        if (absolute) {
            location = new float[3];
            getAbsLocation(location);
        }
        int itemCount = getItemCount();
        StringBuilder sb = new StringBuilder();
        String space = level == 0 ? "".concat("+") : "".concat("|");
        for (int i = 0; i < level; i++) {
            space = space.concat(" ");
        }
        sb.append(space + "+Holder -" + this + "\tDescription: " + this.mDescription + "\tItemIndex: " + this.mItemIndex + "\n");
        String space2 = space.concat(" ");
        sb.append(space2 + "        Item Count: " + itemCount + "\tSlot Count: " + this.mObjectSlots.size() + "\n");
        sb.append(space2 + "        should draw: " + this.mShouldDraw + "\tvisibility: " + this.mVisible + "\topacity: " + this.mOpacity + "\n");
        if (absolute) {
            sb.append(space2 + "        location: " + location[0] + ", " + location[1] + ", " + location[2] + "\n");
        } else {
            sb.append(space2 + "        location: " + this.mLocation[0] + ", " + this.mLocation[1] + ", " + this.mLocation[2] + "\n");
        }
        sb.append(space2 + "        parentHolder: " + this.mParentHolder + "\n");
        if (this.mItemProvider != null) {
            sb.append(space2 + "        ItemProvider: " + this.mItemProvider + " itemProvider's client: " + this.mItemProvider.getClientHolder());
        } else {
            sb.append(space2 + "        ItemProvider: " + this.mItemProvider);
        }
        Log.d("HOLDER_STAT", sb.toString());
        if (!this.mTerminalHolder) {
            int level2 = level + 1;
            Iterator i$ = this.mObjectSlots.iterator();
            while (i$.hasNext()) {
                TFObjectContainer oc = (TFObjectContainer) i$.next();
                if (oc.getObject() != null) {
                    ((TFHolder) oc.getObject()).printHolderState(level2);
                }
            }
            int i2 = level2 - 1;
            return;
        }
        Iterator i$2 = this.mObjectSlots.iterator();
        while (i$2.hasNext()) {
			TFObjectContainer oc = (TFObjectContainer) i$2.next();
            ((TFModel) oc.getObject()).printModelState(level);
        }
    }

    public void printHolderState(int level) {
        printHolderState(level, false);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void setSensitivity(float sensitivityValue) {
        this.mSensitivity = sensitivityValue;
    }

    /* loaded from: classes.dex */
    public abstract class MoveAnimation extends TFAnimation {
        protected float mAcceleration;
        protected float mInitialS;
        protected float mInitialV;
        protected float mS;
        protected float mV;
        protected float mObjectInertia = 1.0f;
        protected boolean mAutoRepositionMode = true;
        protected boolean mOriginalAutoRepositionValue = true;
        protected int mAutoRepositionDuration = 200;
        protected float mINITIAL_V = 0.004f;

        public MoveAnimation(TFHolder holder) {
            this.mSubject = holder;
        }

        public void startMoveAnimation(int offset) {
            setMoveOffset(offset);
            start();
            getHolder().requestLayout();
        }

        public void startMoveAnimation(int offset, int duration) {
            setMoveOffset(offset, duration);
            start();
            getHolder().requestLayout();
        }

        public void startMoveAnimation(int offset, float speed) {
            setMoveOffset(offset, speed);
            start();
            getHolder().requestLayout();
        }

        public void startMoveAnimation(float velocity, float a) {
            setMove(velocity, a);
            start();
            getHolder().requestLayout();
        }

        public void startMoveAnimation(float velocity, int duration) {
            setMove(velocity, duration);
            start();
            getHolder().requestLayout();
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public void start() {
            super.start();
            ((TFHolder) this.mSubject).onBindingAnimationStart(this);
            if (TFHolder.this.mWorld != null) {
                TFHolder.this.mWorld.requestRender();
            }
        }

        @Override // com.nemustech.tiffany.world.TFAnimation
        public void stop() {
            super.stop();
            ((TFHolder) this.mSubject).onBindingAnimationEnd(this);
        }

        public boolean repositionObjects(int duration) {
            float headDeviation = getHolder().getDeviation();
            if (Math.abs(headDeviation) > 0.001f && duration != 0) {
                float distance = (-headDeviation) * this.mObjectInertia;
                float velocity = (2.0f * distance) / duration;
                float acceleration = velocity / duration;
                reset(false);
                startMoveAnimation(velocity, acceleration);
                return true;
            }
            if (TFHolder.this.mDelayedSetItemMode && TFHolder.this.mNeedToSetItem) {
                TFHolder.this.mSetItemNow = true;
                TFHolder.this.refreshSlotIndex();
            }
            getHolder().setDeviation(0.0f);
            getHolder().requestLayout();
            TFAnimation.AnimationEventListener eventListener = getAnimationEventListener();
            if (eventListener != null) {
                this.mState = 1;
                eventListener.onAnimationStart(this);
                this.mState = 2;
                eventListener.onAnimationEnd(this);
            }
            return false;
        }

        public boolean repositionHeadModel() {
            return repositionObjects(0);
        }

        public void setInertia(float inertia) {
            this.mObjectInertia = inertia;
        }

        public void setAutoRepositionMode(boolean mode) {
            this.mAutoRepositionMode = mode;
            this.mOriginalAutoRepositionValue = this.mAutoRepositionMode;
        }

        protected void setTempAutoRepositionMode(boolean mode) {
            this.mAutoRepositionMode = mode;
        }

        protected void setOriginalAutoRepositionMode() {
            this.mAutoRepositionMode = this.mOriginalAutoRepositionValue;
        }

        public void setAutoRepositionMode(boolean mode, int duration) {
            setAutoRepositionMode(mode);
            this.mAutoRepositionDuration = duration;
        }

        public void setMove(float initialV, int duration) {
            reset(false);
            this.mInitialV = initialV;
            this.mAcceleration = 0.0f;
            setDuration(duration);
        }

        public void setMove(float initialV, float accel) {
            reset(false);
            this.mInitialV = initialV;
            this.mAcceleration = accel;
            if ((this.mInitialV > 0.0f && this.mAcceleration > 0.0f) || (this.mInitialV < 0.0f && this.mAcceleration < 0.0f)) {
                this.mAcceleration = -this.mAcceleration;
            }
            int t = (int) (calcDuration(initialV, 0.0f, this.mAcceleration) + 0.5f);
            setDuration(t);
        }

        public void setInitialVelocity(float v) {
            this.mINITIAL_V = v;
        }

        public void setMoveOffset(int offset) {
            setMoveOffset(offset, this.mINITIAL_V);
        }

        public void setMoveOffset(int offset, int duration) {
            setMoveOffset(offset, Math.abs(offset / duration));
        }

        public void setMoveOffset(int offset, float speed) {
            setMoveStep(offset, speed);
        }

        public void setMoveStep(float step) {
            setMoveStep(step, this.mINITIAL_V);
        }

        public void setMoveStep(float step, int duration) {
            setMoveStep(step, Math.abs(step / duration));
        }

        public void setMoveStep(float step, float speed) {
            int remainTime = 0;
            if (!hasEnded()) {
                remainTime = getDuration() - getTime();
            }
            reset(false);
            this.mInitialV = speed;
            this.mAcceleration = 0.0f;
            if (step < 0.0f) {
                this.mInitialV = -this.mInitialV;
            }
            int t = (int) ((step / this.mInitialV) + 0.5f);
            setDuration(t + remainTime);
        }

        protected float calcDuration(float initialV, float finalV, float accel) {
            return (finalV - initialV) / accel;
        }

        protected float calcV(float initialV, float accel, float t) {
            return (accel * t) + initialV;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public float calcS(float initialS, float initialV, float accel, float t) {
            return (initialV * t) + initialS + (((accel * t) * t) / 2.0f);
        }

        public TFHolder getHolder() {
            return (TFHolder) this.mSubject;
        }
    }

    /* renamed from: getMoveAnimation */
    public MoveAnimation getMoveAnimation() {
        return this.mMoveAni;
    }

    public void setMoveAnimation(MoveAnimation moveAnimation) {
        this.mMoveAni = moveAnimation;
    }
}
