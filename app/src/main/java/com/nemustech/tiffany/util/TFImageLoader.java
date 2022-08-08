package com.nemustech.tiffany.util;

import android.util.Log;
import com.nemustech.tiffany.world.TFModel;
import java.util.HashMap;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class TFImageLoader extends Thread {
    private static final String TAG = "TFImageLoader";
    private boolean mIdle;
    private OnItemUpdateListener mItemUpdateListener;
    private boolean mPaused;
    private boolean mThreadLive;
    private LinkedList<Integer> mIndexArray = new LinkedList<>();
    private HashMap<Integer, Cell> mSlotArray = new HashMap<>();

    /* loaded from: classes.dex */
    public interface OnItemUpdateListener {
        void onItemUpdate(TFModel tFModel, int i);
    }

    /* loaded from: classes.dex */
    public class Cell {
        boolean mIsUrgent;
        int mKey;
        TFModel mModel;

        public Cell(TFModel model, int key, boolean isUrgent) {
            this.mKey = key;
            this.mModel = model;
            this.mIsUrgent = isUrgent;
        }
    }

    public void clearQueue() {
        synchronized (this) {
            this.mSlotArray.clear();
            this.mIndexArray.clear();
            Log.i(TAG, "Image loader queue cleared");
        }
    }

    public void removeQueue(Cell cell) {
        synchronized (this) {
            Integer key = Integer.valueOf(cell.mKey);
            Cell slot = this.mSlotArray.remove(key);
            if (slot != null) {
                this.mIndexArray.remove(key);
                Log.w(TAG, "Queue index:" + key + " has been removed from queue.");
            }
        }
    }

    public void addQueue(Cell cell) {
        synchronized (this) {
            Integer key = Integer.valueOf(cell.mKey);
            do {
                Cell prevSlot = this.mSlotArray.get(key);
                if (prevSlot == null) {
                    break;
                } else if (prevSlot.mModel != cell.mModel) {
                    this.mSlotArray.remove(key);
                    this.mIndexArray.remove(key);
                } else {
                    return;
                }
            } while (1 != 0);
            this.mSlotArray.put(key, cell);
            if (cell.mIsUrgent) {
                this.mIndexArray.addFirst(key);
            } else {
                this.mIndexArray.add(key);
            }
            if (this.mIdle) {
                this.mIdle = false;
                notify();
            }
            if (this.mSlotArray.size() != this.mIndexArray.size()) {
                Log.e(TAG, "Array mismatch !!!!!");
            }
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.mThreadLive = true;
        while (this.mThreadLive) {
            synchronized (this) {
                if (this.mPaused || this.mIdle) {
                    try {
                        Log.d(TAG, "Nothing to set or forced to be paused");
                        wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            Integer key = this.mIndexArray.peek();
            if (key != null) {
                Cell cell = this.mSlotArray.get(key);
                this.mItemUpdateListener.onItemUpdate(cell.mModel, cell.mKey);
                synchronized (this) {
                    this.mSlotArray.remove(key);
                    this.mIndexArray.remove(key);
                    if (this.mSlotArray.size() != this.mIndexArray.size()) {
                        Log.e(TAG, "Array mismatch !!!!!");
                    }
                }
            } else {
                this.mIdle = true;
            }
        }
        Log.d(TAG, "Thread Paused.");
    }

    public void onPause() {
        synchronized (this) {
            this.mPaused = true;
        }
    }

    public void onResume() {
        synchronized (this) {
            this.mPaused = false;
            notify();
        }
    }

    public void quit() {
        synchronized (this) {
            this.mThreadLive = false;
            notify();
        }
        try {
            join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setItemUpdateListener(OnItemUpdateListener updateItemListener) {
        this.mItemUpdateListener = updateItemListener;
    }
}
