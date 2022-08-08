package com.nemustech.tiffany.util;

import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class TFCache {
    private static final String TAG = "TFCache";
    public static final int VICTIM_STYLE_NEW = 1;
    public static final int VICTIM_STYLE_OLD = 0;
    private LinkedHashMap<Integer, Bitmap> mArrayBitmap;
    private LinkedList<Integer> mArrayIntKey = new LinkedList<>();
    private LinkedHashMap<String, Integer> mArrayStrKey = new LinkedHashMap<>();
    private int mCapacity;
    private int mPseudoId;
    private boolean mVictimShouldBeOldestOne;

    public TFCache(int maxCacheCount) {
        this.mCapacity = maxCacheCount;
        this.mArrayBitmap = new LinkedHashMap<>(this.mCapacity);
    }

    public Integer put(int id, Bitmap bitmap) {
        if (getCachedCount() >= this.mCapacity) {
            removeVictim();
        }
        Integer key = new Integer(id);
        if (this.mArrayBitmap.containsKey(key)) {
            throw new IllegalArgumentException("Cache manager has already that key! : " + id);
        }
        if (this.mVictimShouldBeOldestOne) {
            this.mArrayIntKey.addLast(key);
        } else {
            this.mArrayIntKey.addFirst(key);
        }
        this.mArrayBitmap.put(key, bitmap);
        return key;
    }

    public void put(String string, Bitmap bitmap) {
        if (this.mArrayStrKey.containsKey(string)) {
            throw new IllegalArgumentException("Cache manager has already that string key! : " + string);
        }
        int i = this.mPseudoId;
        this.mPseudoId = i + 1;
        Integer key = put(i, bitmap);
        this.mArrayStrKey.put(string, key);
    }

    public void add(int location, String string, Bitmap bitmap) {
        if (getCachedCount() >= this.mCapacity) {
            Integer key = this.mArrayIntKey.removeLast();
            this.mArrayStrKey.values().remove(key);
            Bitmap victim = this.mArrayBitmap.get(key);
            victim.recycle();
            this.mArrayBitmap.remove(key);
        }
        if (this.mArrayStrKey.containsKey(string)) {
            throw new IllegalArgumentException("Cache manager has already that string key! : " + string);
        }
        int i = this.mPseudoId;
        this.mPseudoId = i + 1;
        Integer key2 = new Integer(i);
        this.mArrayIntKey.add(location, key2);
        this.mArrayBitmap.put(key2, bitmap);
        this.mArrayStrKey.put(string, key2);
    }

    public void remove(String string) {
        Integer key = this.mArrayStrKey.get(string);
        if (key != null) {
            remove(key);
        }
    }

    public void remove(Integer value) {
        int location = this.mArrayIntKey.indexOf(value);
        if (location != -1 && location < this.mArrayIntKey.size()) {
            Integer removedValue = this.mArrayIntKey.remove(location);
            this.mArrayStrKey.values().remove(removedValue);
            this.mArrayBitmap.remove(removedValue);
        }
    }

    public int indexOf(String string) {
        Integer key = this.mArrayStrKey.get(string);
        if (key != null) {
            return this.mArrayIntKey.indexOf(key);
        }
        return -1;
    }

    public Bitmap get(int id) {
        Integer key = new Integer(id);
        this.mArrayBitmap.get(key);
        return this.mArrayBitmap.get(key);
    }

    public Bitmap get(String string) {
        Integer key = this.mArrayStrKey.get(string);
        if (key == null) {
            return null;
        }
        return get(key.intValue());
    }

    public int getCachedCount() {
        return this.mArrayBitmap.size();
    }

    public void setVictimStyle(int victimStyle) {
        this.mVictimShouldBeOldestOne = victimStyle == 0;
    }

    private void removeVictim() {
        Integer key = this.mVictimShouldBeOldestOne ? this.mArrayIntKey.removeFirst() : this.mArrayIntKey.removeLast();
        this.mArrayStrKey.values().remove(key);
        Bitmap victim = this.mArrayBitmap.get(key);
        victim.recycle();
        this.mArrayBitmap.remove(key);
    }

    public void clear() {
        Iterator i$ = this.mArrayIntKey.iterator();
        while (i$.hasNext()) {
            Integer integer = (Integer) i$.next();
            Bitmap bitmap = this.mArrayBitmap.get(integer);
            bitmap.recycle();
        }
        this.mPseudoId = 0;
        this.mArrayBitmap.clear();
        this.mArrayIntKey.clear();
        this.mArrayStrKey.clear();
    }
}
