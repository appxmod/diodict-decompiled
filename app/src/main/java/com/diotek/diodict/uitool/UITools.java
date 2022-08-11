package com.diotek.diodict.uitool;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diodict.decompiled.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class UITools {
    public static void savePicture(Bitmap bmp, String szPath, String szName, Bitmap.CompressFormat nFormat) {
        FileOutputStream foutputstream = null;
        FileOutputStream foutputstream2 = null;
        if (bmp != null) {
            try {
                File foutput = new File(szPath);
                foutput.mkdir();
                File foutput2 = new File(szPath + "/" + szName);
                foutputstream = new FileOutputStream(foutput2);
            } catch (IOException e) {
                e = e;
            }
            try {
                bmp.compress(nFormat, 100, foutputstream);
                foutputstream.flush();
                foutputstream.close();
            } catch (Exception e) {
                foutputstream2 = foutputstream;
                try {
                    foutputstream2.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public static void recycleDrawable(Drawable bg, boolean isAniDrawable, boolean isconfigChange) {
        if (bg != null) {
            bg.setCallback(null);
        }
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    public static void prepareMemoSkin(Context context, int dbtype, String keyword, int suid, ImageButton memoBtn) {
        if (dbtype > -1 && keyword != null && suid > -1) {
            if (DioDictDatabase.existMemo(context, dbtype, keyword, suid)) {
                Cursor c = DioDictDatabase.getMemoCursorWith(context, dbtype, keyword, suid);
                if (c != null) {
                    int nSkinIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE);
                    int skin = c.getInt(nSkinIdx);
                    c.close();
                    if (skin > 0) {
                        int[] skinId = {R.drawable.memo_view_01, R.drawable.memo_view_02, R.drawable.memo_view_03};
                        memoBtn.setBackgroundResource(skinId[skin - 1]);
                        return;
                    }
                    return;
                }
                return;
            }
            memoBtn.setBackgroundResource(R.drawable.memo);
        }
    }
}
