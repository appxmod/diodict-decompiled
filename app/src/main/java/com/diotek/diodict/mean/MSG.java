package com.diotek.diodict.mean;

import android.os.Build;
import android.os.Debug;
import android.util.Log;
import com.diotek.diodict.engine.DictUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/* loaded from: classes.dex */
public class MSG {
    private static final String TAG = "DioDict3";
    private static final boolean WRITE_TO_FILE = false;
    private static final boolean DEBUG = Build.TYPE.equals("eng");
    private static String mLogFile = DictUtils.getDBPath() + "DioDict3Log.txt";

    public static void l(int i, String msg) {
        if (DEBUG) {
            switch (i) {
                case 0:
                    Log.d(TAG, msg);
                    return;
                case 1:
                    Log.i(TAG, msg);
                    return;
                case 2:
                    Log.e(TAG, msg);
                    return;
                default:
                    return;
            }
        }
    }

    private static void writeLog(String msg) {
        if (msg != null && msg.length() >= 1) {
            File file = new File(mLogFile);
            FileOutputStream fos = null;
            try {
                try {
                    FileOutputStream fos2 = new FileOutputStream(file, true);
                    if (fos2 != null) {
                        try {
                            Date date = new Date();
                            String toWrite = String.valueOf(date.getDate()) + "_" + String.valueOf(date.getHours()) + "_" + String.valueOf(date.getMinutes()) + "_" + String.valueOf(date.getSeconds()) + " : " + msg + "\n";
                            fos2.write(toWrite.getBytes());
                        } catch (Exception e) {
                            e = e;
                            fos = fos2;
                            e.printStackTrace();
                            if (fos != null) {
                                try {
                                    fos.close();
                                    return;
                                } catch (Exception e2) {
                                    return;
                                }
                            }
                            return;
                        } catch (Throwable th) {
                            th = th;
                            fos = fos2;
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (Exception e3) {
                                }
                            }
                            throw th;
                        }
                    }
                    if (fos2 != null) {
                        try {
                            fos2.close();
                        } catch (Exception e4) {
                        }
                    }
                } catch (Exception e5) {
                    e = e5;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public static void l(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void memoryPrint(String functionName) {
        Log.i(TAG, "-------------- : " + functionName);
        Log.i(TAG, "Runtime.getRuntime().freeMemory() = " + (Runtime.getRuntime().freeMemory() / 1024));
        Log.i(TAG, "Runtime.getRuntime().maxMemory() = " + (Runtime.getRuntime().maxMemory() / 1024));
        Log.i(TAG, "Runtime.getRuntime().totalMemory() = " + (Runtime.getRuntime().totalMemory() / 1024));
        Log.i(TAG, "Debug.getNativeHeapFreeSize() = " + (Debug.getNativeHeapFreeSize() / 1024));
        Log.i(TAG, "Debug.getNativeHeapAllocatedSize() = " + (Debug.getNativeHeapAllocatedSize() / 1024));
        Log.i(TAG, "Debug.getNativeHeapSize() = " + (Debug.getNativeHeapSize() / 1024));
    }
}
