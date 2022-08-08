package com.nemustech.tiffany.world;

import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public abstract class TFItemProvider<T> implements Cloneable {
    private TFHolder mClientHolder;
    private JitImageProvider mJitImageProvider;
    private String TAG = "TFItemProvider";
    private LinkedList<T> mItemList = new LinkedList<>();

    /* loaded from: classes.dex */
    public interface ItemProvider {
        void setItem(TFObjectContainer tFObjectContainer, int i);
    }

    /* loaded from: classes.dex */
    public interface JitImageProvider {
        Bitmap getImage(int i, int i2);
    }

    public abstract void setItem(TFObjectContainer tFObjectContainer, int i);

    public void setJitImageProvider(JitImageProvider jitImage) {
        this.mJitImageProvider = jitImage;
    }

    public JitImageProvider getJitImageProvider() {
        return this.mJitImageProvider;
    }

    public T getItem(int idx) {
        return this.mItemList.get(idx);
    }

    public void setClientHolder(TFHolder holder) {
        this.mClientHolder = holder;
    }

    public TFHolder getClientHolder() {
        return this.mClientHolder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFItemProvider<T> getClone(TFHolder holder) {
        TFItemProvider<T> clone = (TFItemProvider) clone();
        clone.setClientHolder(holder);
        return clone;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Object clone() {
        try {
            TFItemProvider clone = (TFItemProvider) super.clone();
            clone.mItemList = new LinkedList<>();
            clone.setClientHolder(null);
            Iterator i$ = this.mItemList.iterator();
            while (i$.hasNext()) {
                T o = (T) i$.next();
                if (o instanceof TFObject) {
                    clone.addItem(((TFObject) o).clone());
                } else {
                    clone.addItem(o);
                }
            }
            return clone;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setItemList(LinkedList<T> list) {
        this.mItemList = list;
    }

    public LinkedList<T> getItemList() {
        return this.mItemList;
    }

    public int getItemCount() {
        return this.mItemList.size();
    }

    public boolean addItem(T item) {
        if (this.mClientHolder != null && (item instanceof TFObject)) {
            ((TFObject) item).associateToHolder(this.mClientHolder);
        }
        if (this.mItemList.add(item)) {
            if (this.mClientHolder != null && this.mClientHolder.getCloneList() != null) {
                LinkedList<TFObject> cloneList = this.mClientHolder.getCloneList();
                Iterator i$ = cloneList.iterator();
                while (i$.hasNext()) {
                    TFObject o = (TFObject) i$.next();
                    ((TFHolder) o).getItemProvider().addItem(item);
                }
            }
            return true;
        }
        return false;
    }

    public void addItemAt(int idx, T item) {
        this.mItemList.add(idx, item);
        if (this.mClientHolder != null && this.mClientHolder.getCloneList() != null) {
            Iterator i$ = this.mClientHolder.getCloneList().iterator();
            while (i$.hasNext()) {
                TFObject o = (TFObject) i$.next();
                ((TFHolder) o).getItemProvider().addItemAt(idx, item);
            }
        }
    }

    public T removeItemAt(int idx) {
        T removed = this.mItemList.remove(idx);
        if (removed != null && this.mClientHolder != null && this.mClientHolder.getCloneList() != null) {
            Iterator i$ = this.mClientHolder.getCloneList().iterator();
            while (i$.hasNext()) {
                TFObject o = (TFObject) i$.next();
                ((TFHolder) o).getItemProvider().removeItemAt(idx);
            }
        }
        return removed;
    }

    public int removeItem(T item) {
        int index = this.mItemList.indexOf(item);
        if (index != -1) {
            this.mItemList.remove(index);
            if (this.mClientHolder != null && this.mClientHolder.getCloneList() != null) {
                Iterator i$ = this.mClientHolder.getCloneList().iterator();
                while (i$.hasNext()) {
                    TFObject o = (TFObject) i$.next();
                    ((TFHolder) o).getItemProvider().removeItem(item);
                }
            }
        }
        return index;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void associateTo(TFHolder holder) {
        Iterator i$ = this.mItemList.iterator();
        while (i$.hasNext()) {
            T o = (T) i$.next();
            if (o instanceof TFObject) {
                ((TFObject) o).associateToHolder(holder);
            } else {
                return;
            }
        }
    }
}
