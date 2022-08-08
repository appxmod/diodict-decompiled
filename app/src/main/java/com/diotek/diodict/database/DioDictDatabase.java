package com.diotek.diodict.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Pair;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.mean.MarkerObject;
import com.diotek.diodict.tools.IgnoreCaseAscComparator;
import com.diotek.diodict.tools.IgnoreCaseDscComparator;
import com.diotek.diodict3.DioDict3R;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes.dex */
public class DioDictDatabase {
    public static boolean existWordbookFolder(Context context, String name) {
        boolean bRet = false;
        if (name.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0 || name.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            return true;
        }
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_NAME + "=?";
        String[] selectionArg = {name};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKFOLDER, selection, selectionArg, null);
        if (tCursor == null) {
            return false;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return false;
        }
        if (tCursor.getCount() != 0) {
            bRet = true;
        }
        tCursor.close();
        return bRet;
    }

    public static int addWordbookFolder(Context context, String name, int type) {
        if (existWordbookFolder(context, name)) {
            return 2;
        }
        ContentValues cv = new ContentValues();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_NAME, name);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE, Integer.valueOf(type));
        long time = System.currentTimeMillis();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_TIME, Long.valueOf(time));
        Uri wordbookfolderUri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        context.getContentResolver().insert(wordbookfolderUri, cv);
        return 1;
    }

    public static void deleteWordbookFolder(Context context, String name) {
        Uri wordbookitemUri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String folderId = String.valueOf(getWordbookFolderId(context, name));
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
        String[] selectionArg = {folderId};
        context.getContentResolver().delete(wordbookitemUri, selection, selectionArg);
        Uri wordbookfolderUri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        String selection2 = DioDictDatabaseInfo.DB_COLUMN_NAME + "=?";
        String[] selectionArg2 = {name};
        context.getContentResolver().delete(wordbookfolderUri, selection2, selectionArg2);
    }

    public static int editWordbookFolder(Context context, String name, String changeName, int cardType) {
        if (!name.equals(changeName) && existWordbookFolder(context, changeName)) {
            return 2;
        }
        ContentValues cv = new ContentValues();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_NAME, changeName);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE, Integer.valueOf(cardType));
        Uri wordbookfolderUri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_NAME + "=?";
        String[] selectionArg = {name};
        context.getContentResolver().update(wordbookfolderUri, cv, selection, selectionArg);
        return 1;
    }

    public static boolean existWordbookItem(Context context, int dbtype, String keyword, int suid, int folderid) {
        String selection;
        String[] selectionArg;
        boolean bRet = false;
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        if (folderid != -1) {
            selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=? And " + DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
            selectionArg = new String[]{Integer.toString(dbtype), keyword, Integer.toString(suid), Integer.toString(folderid)};
        } else {
            selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
            selectionArg = new String[]{Integer.toString(dbtype), keyword, Integer.toString(suid)};
        }
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection, selectionArg, null);
        if (tCursor == null) {
            return false;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return false;
        }
        if (tCursor.getCount() != 0) {
            bRet = true;
        }
        tCursor.close();
        return bRet;
    }

    public static int addWordbookItem(Context context, int dbtype, String keyword, int suid, int folderid) {
        if (existWordbookItem(context, dbtype, keyword, suid, folderid)) {
            return 2;
        }
        ContentValues cv = new ContentValues();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_DBTYPE, Integer.valueOf(dbtype));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_NAME, keyword);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_SUID, Integer.valueOf(suid));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_FOLDERID, Integer.valueOf(folderid));
        long time = System.currentTimeMillis();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_TIME, Long.valueOf(time));
        Uri wordbookitemUri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        context.getContentResolver().insert(wordbookitemUri, cv);
        return 1;
    }

    public static void sendCurFlashcardSortmode(Context context, int sort, int folderid) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
        String[] selectionArg = {Integer.toString(folderid)};
        Cursor cursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection, selectionArg, getSortMode(sort));
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void deleteWordbookItem(Context context, int dbtype, String keyword, int suid, int folderid) {
        Uri wordbookfolderUri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=? And " + DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid), Integer.toString(folderid)};
        context.getContentResolver().delete(wordbookfolderUri, selection, selectionArg);
    }

    public static int existHistoryItem(Context context, int dbtype, String keyword, int suid) {
        int nRet = -1;
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_HISTORYITEM, selection, selectionArg, null);
        if (tCursor == null) {
            return -1;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return -1;
        }
        if (tCursor.getCount() != 0) {
            nRet = tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_TIMES));
        }
        tCursor.close();
        return nRet;
    }

    public static int addHistory(Context context, int dbtype, String keyword, int suid) {
        if (DictDBManager.isIdiomDictionary(dbtype) || DictDBManager.isExampleDictionary(dbtype)) {
            return 0;
        }
        Uri historyitemUri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        ContentValues cv = new ContentValues();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_DBTYPE, Integer.valueOf(dbtype));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_NAME, keyword);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_SUID, Integer.valueOf(suid));
        long time = System.currentTimeMillis();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_TIME, Long.valueOf(time));
        int nHistoryItemTimes = existHistoryItem(context, dbtype, keyword, suid);
        if (nHistoryItemTimes != -1) {
            cv.put(DioDictDatabaseInfo.DB_COLUMN_TIMES, Integer.valueOf(nHistoryItemTimes + 1));
            context.getContentResolver().update(historyitemUri, cv, selection, selectionArg);
            return 2;
        }
        cv.put(DioDictDatabaseInfo.DB_COLUMN_TIMES, (Integer) 0);
        context.getContentResolver().insert(historyitemUri, cv);
        return 1;
    }

    public static void deleteHistory(Context context, int dbtype, String keyword, int suid) {
        Uri historyitemUri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        context.getContentResolver().delete(historyitemUri, selection, selectionArg);
    }

    public static Cursor getMarkerCursor(Context context, int nSort) {
        Uri uri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        Cursor cursor = context.getContentResolver().query(uri, DioDictDatabaseInfo.PROJECTION_MARKER, null, null, getSortMode(nSort));
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        } else if (cursor.getCount() != 0) {
            return cursor;
        } else {
            cursor.close();
            return null;
        }
    }

    public static int getMarkerCount(Context context, int nSort) {
        Uri uri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        Cursor cursor = context.getContentResolver().query(uri, DioDictDatabaseInfo.PROJECTION_MARKER, null, null, getSortMode(nSort));
        if (cursor == null) {
            return 0;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        int nCount = cursor.getCount();
        cursor.close();
        return nCount;
    }

    public static Object existMarker(Context context, int dbtype, int suid) {
        Object retObject = null;
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), Integer.toString(suid)};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MARKER, selection, selectionArg, null);
        if (tCursor == null) {
            return null;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return null;
        }
        if (tCursor.getCount() != 0) {
            retObject = getObjectByBytes(tCursor.getBlob(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_MARKEROBJ)));
        }
        tCursor.close();
        return retObject;
    }

    public static int addMarker(Context context, int dbtype, String keyword, int suid, ArrayList<MarkerObject> markerobj) {
        Uri markerUri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        ContentValues cv = new ContentValues();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_DBTYPE, Integer.valueOf(dbtype));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_NAME, keyword);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_SUID, Integer.valueOf(suid));
        long time = System.currentTimeMillis();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_TIME, Long.valueOf(time));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_MARKEROBJ, getBytesByObject(markerobj));
        if (existMarker(context, dbtype, suid) != null) {
            String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
            String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
            context.getContentResolver().update(markerUri, cv, selection, selectionArg);
            return 2;
        }
        context.getContentResolver().insert(markerUri, cv);
        return 1;
    }

    public static void deleteMarker(Context context, int dbtype, String keyword, int suid) {
        Uri markerUri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        context.getContentResolver().delete(markerUri, selection, selectionArg);
    }

    public static boolean existMemo(Context context, int dbtype, String keyword, int suid) {
        boolean bRet = false;
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MEMO, selection, selectionArg, null);
        if (tCursor == null) {
            return false;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return false;
        }
        if (tCursor.getCount() != 0) {
            bRet = true;
        }
        tCursor.close();
        return bRet;
    }

    public static int addMemo(Context context, int dbtype, String keyword, int suid, String memo, int skin) {
        Uri memoUri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        ContentValues cv = new ContentValues();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_DBTYPE, Integer.valueOf(dbtype));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_NAME, keyword);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_SUID, Integer.valueOf(suid));
        cv.put(DioDictDatabaseInfo.DB_COLUMN_MEMO, memo);
        cv.put(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE, Integer.valueOf(skin));
        long time = System.currentTimeMillis();
        cv.put(DioDictDatabaseInfo.DB_COLUMN_TIME, Long.valueOf(time));
        if (existMemo(context, dbtype, keyword, suid)) {
            String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
            String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
            context.getContentResolver().update(memoUri, cv, selection, selectionArg);
            return 2;
        }
        context.getContentResolver().insert(memoUri, cv);
        return 1;
    }

    public static void deleteMemo(Context context, int dbtype, String keyword, int suid) {
        Uri historyitemUri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        context.getContentResolver().delete(historyitemUri, selection, selectionArg);
    }

    public static int getWordbookFolderCount(Context context) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKFOLDER, null, null, null);
        if (tCursor == null) {
            return 0;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return 0;
        }
        int nWordbookFolderCount = tCursor.getCount();
        tCursor.close();
        return nWordbookFolderCount;
    }

    public static int getWordbookItemCount(Context context, int folderid) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
        String[] selectionArg = {Integer.toString(folderid)};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection, selectionArg, null);
        if (tCursor == null) {
            return 0;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return 0;
        }
        int nWordbookItemCount = tCursor.getCount();
        tCursor.close();
        return nWordbookItemCount;
    }

    public static int getHistoryItemCount(Context context) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_HISTORYITEM, null, null, null);
        if (tCursor == null) {
            return 0;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return 0;
        }
        int nHistoryItemCount = tCursor.getCount();
        tCursor.close();
        return nHistoryItemCount;
    }

    public static int getMemoCount(Context context) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MEMO, null, null, null);
        if (tCursor == null) {
            return 0;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return 0;
        }
        int nMemoCount = tCursor.getCount();
        tCursor.close();
        return nMemoCount;
    }

    public static Cursor getWordbookFolderCursor(Context context) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKFOLDER, null, null, null);
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() != 0) {
            return tmpCursor;
        } else {
            tmpCursor.close();
            return null;
        }
    }

    public static Cursor getWordbookItemCursor(Context context, int folderid, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
        String[] selectionArg = {Integer.toString(folderid)};
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getWordbookItemCursorByDictType(Context context, int nDictType, int folderid, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=? And " + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
        String[] selectionArg = {Integer.toString(folderid), Integer.toString(nDictType)};
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getWordbookItemCursorByDictType(Context context, Integer[] nDictType, int folderid, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=? And ";
        String selectionDicType = "(";
        String[] selectionArg = new String[nDictType.length + 1];
        selectionArg[0] = Integer.toString(folderid);
        for (int i = 0; i < nDictType.length; i++) {
            String selectionDicType2 = selectionDicType + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
            if (i + 1 >= nDictType.length) {
                selectionDicType = selectionDicType2 + ")";
            } else {
                selectionDicType = selectionDicType2 + " Or ";
            }
            selectionArg[i + 1] = Integer.toString(nDictType[i].intValue());
        }
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection + selectionDicType, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor queryDictTypeListCursor(Context context, String foldername, int folderid, int nSort) {
        Uri item_uri;
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_HISTORY) == 0) {
            item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        } else {
            item_uri = getUriString(getTableName(foldername));
        }
        DioDictDatabaseInfo.mGroupBy = DioDictDatabaseInfo.DB_COLUMN_DBTYPE;
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=?";
        String[] selectionArg = {Integer.toString(folderid)};
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0 || foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0 || foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_HISTORY) == 0) {
            selection = null;
            selectionArg = null;
        }
        Cursor tmpCursor = context.getContentResolver().query(item_uri, getProjectionStringArray(foldername), selection, selectionArg, getSortMode(nSort));
        DioDictDatabaseInfo.mGroupBy = null;
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getHistoryCursor(Context context, int nSort) {
        ContentResolver resolver;
        Cursor tmpCursor;
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        if (context != null && (resolver = context.getContentResolver()) != null && (tmpCursor = resolver.query(item_uri, DioDictDatabaseInfo.PROJECTION_HISTORYITEM, null, null, getSortMode(nSort))) != null) {
            if (!tmpCursor.moveToFirst()) {
                tmpCursor.close();
                return null;
            } else if (tmpCursor.getCount() == 0) {
                tmpCursor.close();
                return null;
            } else {
                return tmpCursor;
            }
        }
        return null;
    }

    public static Cursor getHistoryCursorByDictType(Context context, int nDictType, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
        String[] selectionArg = {Integer.toString(nDictType)};
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_HISTORYITEM, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getHistoryCursorByDictType(Context context, Integer[] nDictType, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
        String selection = "(";
        String[] selectionArg = new String[nDictType.length];
        for (int i = 0; i < nDictType.length; i++) {
            selection = selection + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
            selectionArg[i] = Integer.toString(nDictType[i].intValue());
            if (i < nDictType.length - 1) {
                selection = selection + " Or ";
            } else if (i == nDictType.length - 1) {
                selection = selection + ")";
            }
        }
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_HISTORYITEM, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getMarkerCursorByDictType(Context context, int nDictType, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
        String[] selectionArg = {Integer.toString(nDictType)};
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MARKER, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getMarkerCursorByDictType(Context context, Integer[] nDictType, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MARKER);
        String selection = "(";
        String[] selectionArg = new String[nDictType.length];
        for (int i = 0; i < nDictType.length; i++) {
            selection = selection + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
            selectionArg[i] = Integer.toString(nDictType[i].intValue());
            if (i < nDictType.length - 1) {
                selection = selection + " Or ";
            } else if (i == nDictType.length - 1) {
                selection = selection + ")";
            }
        }
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MARKER, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getMemoCursor(Context context, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MEMO, null, null, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getMemoCursorByDictType(Context context, int nDictType, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
        String[] selectionArg = {Integer.toString(nDictType)};
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MEMO, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getMemoCursorByDictType(Context context, Integer[] nDictType, int nSort) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        String selection = "(";
        String[] selectionArg = new String[nDictType.length];
        for (int i = 0; i < nDictType.length; i++) {
            selection = selection + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=?";
            selectionArg[i] = Integer.toString(nDictType[i].intValue());
            if (i < nDictType.length - 1) {
                selection = selection + " Or ";
            } else if (i == nDictType.length - 1) {
                selection = selection + ")";
            }
        }
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MEMO, selection, selectionArg, getSortMode(nSort));
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() == 0) {
            tmpCursor.close();
            return null;
        } else {
            return tmpCursor;
        }
    }

    public static Cursor getMemoCursorWith(Context context, int dbtype, String keyword, int suid) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_MEMO);
        String selection = DioDictDatabaseInfo.DB_COLUMN_DBTYPE + "=? And " + DioDictDatabaseInfo.DB_COLUMN_NAME + "=? And " + DioDictDatabaseInfo.DB_COLUMN_SUID + "=?";
        String[] selectionArg = {Integer.toString(dbtype), keyword, Integer.toString(suid)};
        Cursor tmpCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_MEMO, selection, selectionArg, null);
        if (tmpCursor == null) {
            return null;
        }
        if (!tmpCursor.moveToFirst()) {
            tmpCursor.close();
            return null;
        } else if (tmpCursor.getCount() != 0) {
            return tmpCursor;
        } else {
            tmpCursor.close();
            return null;
        }
    }

    public static Uri getUriString(String tablename) {
        Uri.Builder uribuilder = new Uri.Builder();
        uribuilder.scheme("content");
        uribuilder.authority(DioDict3R.AUTHORITY);
        uribuilder.path(tablename);
        return uribuilder.build();
    }

    public static int getWordbookFolderId(Context context, String name) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_NAME + "=?";
        String[] selectionArg = {name};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKFOLDER, selection, selectionArg, null);
        if (tCursor == null) {
            return 0;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return 0;
        }
        int nRet = tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
        tCursor.close();
        return nRet;
    }

    public static String getWordbookFolderName(Context context, int id) {
        Uri item_uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
        String selection = DioDictDatabaseInfo.DB_COLUMN_ID + "=?";
        String[] selectionArg = {String.valueOf(id)};
        Cursor tCursor = context.getContentResolver().query(item_uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKFOLDER, selection, selectionArg, null);
        if (tCursor == null) {
            return null;
        }
        if (!tCursor.moveToFirst()) {
            tCursor.close();
            return null;
        }
        String nRet = tCursor.getString(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME));
        tCursor.close();
        return nRet;
    }

    public static Cursor getWordbookItemCursorById(Context context, int folderId, int id) {
        Uri uri = getUriString(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
        String selection = DioDictDatabaseInfo.DB_COLUMN_FOLDERID + "=? And " + DioDictDatabaseInfo.DB_COLUMN_ID + "=?";
        String[] selectionArg = {String.valueOf(folderId), String.valueOf(id)};
        Cursor tCursor = context.getContentResolver().query(uri, DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM, selection, selectionArg, null);
        if (tCursor == null) {
            return null;
        }
        if (tCursor.moveToFirst()) {
            return tCursor;
        }
        tCursor.close();
        return null;
    }

    public static String getSortMode(int nMode) {
        switch (nMode) {
            case 1:
                return DioDictDatabaseInfo.SORT_ORDER_WORD_DSC_STR;
            case 2:
                return DioDictDatabaseInfo.SORT_ORDER_TIME_ASC_STR;
            case 3:
                return DioDictDatabaseInfo.SORT_ORDER_TIME_DSC_STR;
            case 4:
            case 5:
                return DioDictDatabaseInfo.SORT_ORDER_DICTYPE_STR;
            default:
                return DioDictDatabaseInfo.SORT_ORDER_WORD_ASC_STR;
        }
    }

    public static byte[] getBytesByObject(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = bos.toByteArray();
        return data;
    }

    public static Object getObjectByBytes(byte[] byteObject) {
        ObjectInputStream ois;
        Object object = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(byteObject);
        try {
            ois = new ObjectInputStream(bis);
        } catch (IOException e) {
            e = e;
        } catch (ClassNotFoundException e2) {
            e = e2;
        }
        try {
            bis.close();
            object = ois.readObject();
            ois.close();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return object;
        } catch (ClassNotFoundException e4) {
            e = e4;
            e.printStackTrace();
            return object;
        }
        return object;
    }

    public static Cursor getItemsCursor(Context context, String foldername, int nfolderId, int nSort) {
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            Cursor cursor = getMarkerCursor(context, nSort);
            return cursor;
        } else if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            Cursor cursor2 = getMemoCursor(context, nSort);
            return cursor2;
        } else {
            Cursor cursor3 = getWordbookItemCursor(context, nfolderId, nSort);
            return cursor3;
        }
    }

    public static Cursor getItemsCursorByDictType(Context context, Integer[] nDictType, String foldername, int nforderId, int nSort) {
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            Cursor cursor = getMarkerCursorByDictType(context, nDictType, nSort);
            return cursor;
        } else if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            Cursor cursor2 = getMemoCursorByDictType(context, nDictType, nSort);
            return cursor2;
        } else if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_HISTORY) == 0) {
            Cursor cursor3 = getHistoryCursorByDictType(context, nDictType, nSort);
            return cursor3;
        } else {
            Cursor cursor4 = getWordbookItemCursorByDictType(context, nDictType, nforderId, nSort);
            return cursor4;
        }
    }

    public static String getTableName(String foldername) {
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            return DioDictDatabaseInfo.TABLENAME_MARKER;
        }
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            return DioDictDatabaseInfo.TABLENAME_MEMO;
        }
        return DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM;
    }

    private static String[] getProjectionStringArray(String foldername) {
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            String[] result = DioDictDatabaseInfo.PROJECTION_MARKER;
            return result;
        } else if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            String[] result2 = DioDictDatabaseInfo.PROJECTION_MEMO;
            return result2;
        } else if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_HISTORY) == 0) {
            String[] result3 = DioDictDatabaseInfo.PROJECTION_HISTORYITEM;
            return result3;
        } else {
            String[] result4 = DioDictDatabaseInfo.PROJECTION_WORDBOOKITEM;
            return result4;
        }
    }

    public static int getItemsCount(Context context, String foldername, int nfolderId, int nSort) {
        if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            int nCount = getMarkerCount(context, nSort);
            return nCount;
        } else if (foldername.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            int nCount2 = getMemoCount(context);
            return nCount2;
        } else {
            int nCount3 = getWordbookItemCount(context, nfolderId);
            return nCount3;
        }
    }

    public static ArrayList<Integer> getDictTypeListfromCursor(Context context, String foldername, int filderId, int nSort, boolean bFullCheck, boolean checkTTS) {
        ArrayList<Integer> al = new ArrayList<>();
        boolean bInstalled = true;
        Cursor tmpCursor = queryDictTypeListCursor(context, foldername, filderId, nSort);
        if (tmpCursor != null) {
            do {
                int nDicType = tmpCursor.getInt(tmpCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE));
                if (checkTTS) {
                    bInstalled = true;
                }
                if (bInstalled) {
                    al.add(Integer.valueOf(nDicType));
                    if (!bFullCheck) {
                        break;
                    }
                }
            } while (tmpCursor.moveToNext());
            tmpCursor.close();
        }
        return al;
    }

    public static ArrayList<Integer> getDictTypeListfromCursorAvailTTS(Context context, String foldername, int folderId, int nSort, boolean bFullCheck) {
        return getDictTypeListfromCursor(context, foldername, folderId, nSort, bFullCheck, true);
    }

    public static ArrayList<Pair<String, Integer>> getDictListSortByDicName(Context context, String foldername, int folderid, boolean isAsc) {
        ArrayList<Integer> dicTypeList = getDictTypeListfromCursor(context, foldername, folderid, 0, true, false);
        ArrayList<Pair<String, Integer>> resultList = new ArrayList<>();
        for (int i = 0; i < dicTypeList.size(); i++) {
            int dicType = dicTypeList.get(i).intValue();
            String dicName = DictDBManager.getDictName(dicType);
            resultList.add(new Pair<>(dicName, Integer.valueOf(dicType)));
        }
        if (dicTypeList.size() > 0) {
            if (isAsc) {
                IgnoreCaseAscComparator icac = new IgnoreCaseAscComparator();
                Collections.sort(resultList, icac);
            } else {
                IgnoreCaseDscComparator icdc = new IgnoreCaseDscComparator();
                Collections.sort(resultList, icdc);
            }
        }
        return resultList;
    }
}
