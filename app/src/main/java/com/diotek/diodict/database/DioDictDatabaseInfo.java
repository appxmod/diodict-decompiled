package com.diotek.diodict.database;

/* loaded from: classes.dex */
public class DioDictDatabaseInfo {
    public static final int DBStatus_AlreadyExist = 2;
    public static final int DBStatus_Fail = 0;
    public static final int DBStatus_Success = 1;
    public static final int DBTYPE_HISTORYITEM = 5;
    public static final int DBTYPE_HISTORYITEMID = 6;
    public static final int DBTYPE_MARKER = 7;
    public static final int DBTYPE_MARKERID = 8;
    public static final int DBTYPE_MEMO = 9;
    public static final int DBTYPE_MEMOID = 10;
    public static final int DBTYPE_WORDBOOKFOLDER = 1;
    public static final int DBTYPE_WORDBOOKFOLDERID = 2;
    public static final int DBTYPE_WORDBOOKITEM = 3;
    public static final int DBTYPE_WORDBOOKITEMID = 4;
    public static final String FOLDERNAME_HISTORY = "History";
    public static final String FOLDERNAME_MARKER = "Marker";
    public static final String FOLDERNAME_MEMO = "Memo";
    public static final int SORT_ORDER_DICTYPE_ASC = 4;
    public static final int SORT_ORDER_DICTYPE_DSC = 5;
    public static final int SORT_ORDER_TIME_ASC = 2;
    public static final int SORT_ORDER_TIME_DSC = 3;
    public static final int SORT_ORDER_WORD_ASC = 0;
    public static final int SORT_ORDER_WORD_DSC = 1;
    public static final String TABLENAME_HISTORYITEM = "historyitem";
    public static final String TABLENAME_MARKER = "marker";
    public static final String TABLENAME_WORDBOOKFOLDER = "wordbookfolder";
    public static final String TABLENAME_WORDBOOKITEM = "wordbookitem";
    public static String DB_COLUMN_ID = "_id";
    public static String DB_COLUMN_NAME = "name";
    public static String DB_COLUMN_TIME = "time";
    public static String DB_COLUMN_DBTYPE = "dbtype";
    public static String DB_COLUMN_SUID = "suid";
    public static String DB_COLUMN_FOLDERID = "folderid";
    public static String DB_COLUMN_TIMES = "times";
    public static String DB_COLUMN_MARKEROBJ = "markerobj";
    public static final String TABLENAME_MEMO = "memo";
    public static String DB_COLUMN_MEMO = TABLENAME_MEMO;
    public static String DB_COLUMN_FOLDERTYPE = "foldertype";
    public static final String[] PROJECTION_WORDBOOKFOLDER = {DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_FOLDERTYPE, DB_COLUMN_TIME};
    public static final String[] PROJECTION_WORDBOOKITEM = {DB_COLUMN_ID, DB_COLUMN_DBTYPE, DB_COLUMN_NAME, DB_COLUMN_SUID, DB_COLUMN_FOLDERID, DB_COLUMN_TIME};
    public static final String[] PROJECTION_HISTORYITEM = {DB_COLUMN_ID, DB_COLUMN_DBTYPE, DB_COLUMN_NAME, DB_COLUMN_SUID, DB_COLUMN_TIMES, DB_COLUMN_TIME};
    public static final String[] PROJECTION_MARKER = {DB_COLUMN_ID, DB_COLUMN_DBTYPE, DB_COLUMN_NAME, DB_COLUMN_SUID, DB_COLUMN_MARKEROBJ, DB_COLUMN_TIME};
    public static final String[] PROJECTION_MEMO = {DB_COLUMN_ID, DB_COLUMN_DBTYPE, DB_COLUMN_NAME, DB_COLUMN_SUID, DB_COLUMN_MEMO, DB_COLUMN_FOLDERTYPE, DB_COLUMN_TIME};
    public static final String SORT_ORDER_WORD_ASC_STR = DB_COLUMN_NAME + " COLLATE NOCASE ASC";
    public static final String SORT_ORDER_WORD_DSC_STR = DB_COLUMN_NAME + " COLLATE NOCASE DESC";
    public static final String SORT_ORDER_TIME_ASC_STR = DB_COLUMN_TIME + " ASC";
    public static final String SORT_ORDER_TIME_DSC_STR = DB_COLUMN_TIME + " DESC";
    public static final String SORT_ORDER_DICTYPE_STR = SORT_ORDER_WORD_ASC_STR;
    public static final String SORT_ORDER_TIMES_ASC_STR = DB_COLUMN_TIMES + " ASC";
    public static final String SORT_ORDER_TIMES_DSC_STR = DB_COLUMN_TIMES + " DESC";
    public static String mGroupBy = null;
}