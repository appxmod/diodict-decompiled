package com.diotek.diodict.uitool;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;

/* loaded from: classes.dex */
public class StorageState {
    static final int nERROR = -1;

    public static boolean getExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getInternalStorageFreeSize() {
        File fPath = Environment.getDataDirectory();
        StatFs stat = new StatFs(fPath.getPath());
        long lBlockSize = stat.getBlockSize();
        long lFreeBlocks = stat.getAvailableBlocks();
        return lFreeBlocks * lBlockSize;
    }

    public static long getInternalStorageTotalSize() {
        File fPath = Environment.getDataDirectory();
        StatFs stat = new StatFs(fPath.getPath());
        long lBlockSize = stat.getBlockSize();
        long lTotalBlocks = stat.getBlockCount();
        return lTotalBlocks * lBlockSize;
    }

    public static long getExternalStorageFreeSize() {
        if (getExternalStorageAvailable()) {
            File fPath = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(fPath.getPath());
            long lBlockSize = stat.getBlockSize();
            long lFreeBlocks = stat.getAvailableBlocks();
            return lFreeBlocks * lBlockSize;
        }
        return -1L;
    }

    public static long getExternalStorageTotalSize() {
        if (getExternalStorageAvailable()) {
            File fPath = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(fPath.getPath());
            long lBlockSize = stat.getBlockSize();
            long lTotalBlocks = stat.getBlockCount();
            return lTotalBlocks * lBlockSize;
        }
        return -1L;
    }
}
