package com.diotek.diodict.engine;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.diotek.diodict.mean.MSG;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class ResInstall {
    public static final int FILE_COPY_ERR_NONE = 0;
    public static final int FILE_COPY_ERR_NO_MEMORY = 1;
    public static final int FILE_COPY_ERR_NO_RESOURCE = 2;

    public static int installDB(Context context, String[] dbNames, String[] dbResName) {
        if (dbNames == null || dbResName == null || dbNames == null || dbResName == null) {
            return 2;
        }
        for (int i = 0; i < dbNames.length; i++) {
            int DbResId = context.getResources().getIdentifier(dbResName[i], "raw", context.getPackageName());
            if (DbResId < 1) {
                return 2;
            }
            if (copyRes2File(context, DbResId, dbNames[i]) != 0) {
                return 1;
            }
        }
        return 0;
    }

    public static int installFont(Context context) {
        int fontResId = context.getResources().getIdentifier(DictInfo.DIODICT_FONT_NAME.toLowerCase().replaceAll(".ttf", ""), "raw", context.getPackageName());
        if (fontResId < 1) {
            return 2;
        }
        return copyRes2File(context, fontResId, DictInfo.DIODICT_FONT_NAME);
    }

    public static boolean IsAvailableSaveToInternalStorage(int size) {
        File fPath = Environment.getDataDirectory();
        StatFs stat = new StatFs(fPath.getPath());
        long lBlockSize = stat.getBlockSize();
        long lFreeBlocks = stat.getAvailableBlocks();
        MSG.l(2, "memory current: " + size + " / available : " + (lFreeBlocks * lBlockSize));
        return lFreeBlocks * lBlockSize > ((long) size);
    }

    private static int copyRes2File(Context context, int nResourceId, String szDBName) {
        if (nResourceId < 1) {
            return 2;
        }
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(nResourceId);
        InputStream file = null;
        FileInputStream fis = null;
        int FullSize = 0;
        try {
            try {
                fis = afd.createInputStream();
                FullSize = fis.available();
                InputStream file2 = context.openFileInput(szDBName);
                if (file2 != null && FullSize != file2.available()) {
                    file2.close();
                    File f = new File(szDBName);
                    f.delete();
                    file2 = context.openFileInput(szDBName);
                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (afd != null) {
                    afd.close();
                }
                if (file != null) {
                    file.close();
                }
            }
        } catch (FileNotFoundException e2) {
            if (afd != null) {
                try {
                    if (!IsAvailableSaveToInternalStorage(FullSize)) {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                return 1;
                            }
                        }
                        if (afd != null) {
                            afd.close();
                        }
                        if (file == null) {
                            return 1;
                        }
                        file.close();
                        return 1;
                    }
                    byte[] tempdata = new byte[1000];
                    FileOutputStream fos = context.openFileOutput(szDBName, 0);
                    while (FullSize > 0) {
                        if (FullSize < 1000) {
                            byte[] tempdata2 = new byte[FullSize];
                            fis.read(tempdata2);
                            fos.write(tempdata2);
                            continue;
                        } else {
                            fis.read(tempdata);
                            fos.write(tempdata);
                            continue;
                        }
                        FullSize -= 1000;
                    }
                } catch (IOException e4) {
                    e4.printStackTrace();
                    Log.e("copyRes2File ERR1", szDBName);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            if (afd != null) {
                afd.close();
            }
            if (file != null) {
                file.close();
            }
        } catch (IOException e6) {
            e6.printStackTrace();
            Log.e("copyRes2File ERR2", szDBName);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
            if (afd != null) {
                afd.close();
            }
            if (file != null) {
                file.close();
            }
        }
        return 0;
    }
}
