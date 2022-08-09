package com.diotek.diodict.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;

import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict3.DioDict3R;

/* loaded from: classes.dex */
public class DioDictProvider extends ContentProvider {
    static diodictDBHelper mDioDictHelper = null;
    static SQLiteDatabase mDioDictDB = null;
    public static final UriMatcher sURIMatcher = new UriMatcher(-1);

    static {
        sURIMatcher.addURI(DioDict3R.AUTHORITY, DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER, 1);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, "wordbookfolder/#", 2);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM, 3);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, "wordbookitem/#", 4);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, DioDictDatabaseInfo.TABLENAME_HISTORYITEM, 5);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, "historyitem/#", 6);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, DioDictDatabaseInfo.TABLENAME_MARKER, 7);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, "marker/#", 8);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, DioDictDatabaseInfo.TABLENAME_MEMO, 9);
        sURIMatcher.addURI(DioDict3R.AUTHORITY, "memo/#", 10);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        mDioDictHelper = new diodictDBHelper(getContext());
        mDioDictDB = mDioDictHelper.getWritableDatabase();
        return mDioDictDB != null;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        switch (sURIMatcher.match(uri)) {
            case 1:
            case 2:
                qBuilder.setTables(DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER);
                break;
            case 3:
            case 4:
                qBuilder.setTables(DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM);
                break;
            case 5:
            case 6:
                qBuilder.setTables(DioDictDatabaseInfo.TABLENAME_HISTORYITEM);
                break;
            case 7:
            case 8:
                qBuilder.setTables(DioDictDatabaseInfo.TABLENAME_MARKER);
                break;
            case 9:
            case 10:
                qBuilder.setTables(DioDictDatabaseInfo.TABLENAME_MEMO);
                break;
            default:
                return null;
        }
        Cursor c = qBuilder.query(mDioDictDB, projection, selection, selectionArgs, DioDictDatabaseInfo.mGroupBy, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDioDictDB.insert(getTableNameByUri(uri), null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowID);
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int nRes = mDioDictDB.delete(getTableNameByUri(uri), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return nRes;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowID = mDioDictDB.update(getTableNameByUri(uri), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowID;
    }

    public String getTableNameByUri(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case 1:
            case 2:
                return DioDictDatabaseInfo.TABLENAME_WORDBOOKFOLDER;
            case 3:
            case 4:
                return DioDictDatabaseInfo.TABLENAME_WORDBOOKITEM;
            case 5:
            case 6:
                return DioDictDatabaseInfo.TABLENAME_HISTORYITEM;
            case 7:
            case 8:
                return DioDictDatabaseInfo.TABLENAME_MARKER;
            case 9:
            case 10:
                return DioDictDatabaseInfo.TABLENAME_MEMO;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    /* loaded from: classes.dex */
    private class diodictDBHelper extends SQLiteOpenHelper {
        static final int DIODICTDATAVERSION = 1;
        static final String QUERY_CHECK_END = "'";
        static final String QUERY_CHECK_START = "SELECT name FROM sqlite_master WHERE type='table' AND name='";

        public diodictDBHelper(Context context) {
            super(context, Environment.getExternalStorageDirectory().getPath()+"/DioDict3B/databases/"+DictInfo.DIODICTDATANAME, (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase db) {
            Cursor wordBookFolderCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='wordbookfolder'", null);
            Cursor wordBookItemCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='wordbookitem'", null);
            Cursor historyItemCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='historyitem'", null);
            Cursor markerCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='marker'", null);
            Cursor memoCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='memo'", null);
            try {
                if (wordBookFolderCursor.getCount() == 0) {
                    db.execSQL("CREATE TABLE wordbookfolder (" + DioDictDatabaseInfo.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DioDictDatabaseInfo.DB_COLUMN_NAME + " TEXT, " + DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_TIME + " LONG);");
                }
                if (wordBookItemCursor.getCount() == 0) {
                    db.execSQL("CREATE TABLE wordbookitem (" + DioDictDatabaseInfo.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_NAME + " TEXT, " + DioDictDatabaseInfo.DB_COLUMN_SUID + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_FOLDERID + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_TIME + " LONG);");
                }
                if (historyItemCursor.getCount() == 0) {
                    db.execSQL("CREATE TABLE historyitem (" + DioDictDatabaseInfo.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_NAME + " TEXT, " + DioDictDatabaseInfo.DB_COLUMN_SUID + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_TIMES + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_TIME + " LONG);");
                }
                if (markerCursor.getCount() == 0) {
                    db.execSQL("CREATE TABLE marker (" + DioDictDatabaseInfo.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_NAME + " TEXT, " + DioDictDatabaseInfo.DB_COLUMN_SUID + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_MARKEROBJ + " BLOB, " + DioDictDatabaseInfo.DB_COLUMN_TIME + " LONG);");
                }
                if (memoCursor.getCount() == 0) {
                    db.execSQL("CREATE TABLE memo (" + DioDictDatabaseInfo.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DioDictDatabaseInfo.DB_COLUMN_DBTYPE + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_NAME + " TEXT, " + DioDictDatabaseInfo.DB_COLUMN_SUID + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_MEMO + " TEXT, " + DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE + " INTEGER, " + DioDictDatabaseInfo.DB_COLUMN_TIME + " LONG);");
                }
            } finally {
                wordBookFolderCursor.close();
                wordBookItemCursor.close();
                historyItemCursor.close();
                markerCursor.close();
                memoCursor.close();
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS wordbookfolder");
            db.execSQL("DROP TABLE IF EXISTS wordbookitem");
            db.execSQL("DROP TABLE IF EXISTS historyitem");
            db.execSQL("DROP TABLE IF EXISTS marker");
            db.execSQL("DROP TABLE IF EXISTS memo");
            onCreate(db);
        }
    }
}
