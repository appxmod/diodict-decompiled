package com.diotek.diodict.engine;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DBInfo implements Serializable {
    public static final int DEDT_MAX = 65282;
    public static final int DEL_ENGLISH = 2;
    public static final int DEL_KOREAN = 0;
    private static final long serialVersionUID = 9216371960999601623L;
    private int mCompanyLogoResId;
    private int mDbType;
    private int mEncode;
    private int mExtra1DBType;
    private int mExtra2DBType;
    private String mFileName;
    private int mIconResId;
    private int mListIconResId;
    private int mNameResId;
    private String mNotifyFilename;
    private int mPairDBType;
    private int mParentDBType;
    private int mProductLogoResId;
    private int mProductStringResId;
    private int mSearchMethod;
    private int mSourceLanguage;
    private int mTargetLanguage;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DBInfo(int dbType, int dicname, int nIconId, int nListIconId, int nCompanyLogoResId, int nProductLogoResId, int nProductStringResId, int searchmethod, String filename, int encoding, int pairDB, int parentDB, int extra1DB, int extra2DB, int sourceLang, int targetLang, String notifyName) {
        this.mDbType = dbType;
        this.mNameResId = dicname;
        this.mIconResId = nIconId;
        this.mListIconResId = nListIconId;
        this.mCompanyLogoResId = nCompanyLogoResId;
        this.mProductLogoResId = nProductLogoResId;
        this.mProductStringResId = nProductStringResId;
        this.mSearchMethod = searchmethod;
        this.mFileName = filename;
        this.mEncode = encoding;
        this.mPairDBType = pairDB;
        this.mParentDBType = parentDB;
        this.mExtra1DBType = extra1DB;
        this.mExtra2DBType = extra2DB;
        this.mSourceLanguage = sourceLang;
        this.mTargetLanguage = targetLang;
        this.mNotifyFilename = notifyName;
    }

    public int getDicNameResId() {
        return this.mNameResId;
    }

    public int getIconResID() {
        return this.mIconResId;
    }

    public int getListIconResID() {
        return this.mListIconResId;
    }

    public int getCompanyLogoResId() {
        return this.mCompanyLogoResId;
    }

    public int getProductLogoResId() {
        return this.mProductLogoResId;
    }

    public int getProductStringResId() {
        return this.mProductStringResId;
    }

    public int getSearchMethod() {
        return this.mSearchMethod;
    }

    public String getFilename() {
        return this.mFileName;
    }

    public int getEncode() {
        return this.mEncode;
    }

    public int getPairDBType() {
        return this.mPairDBType;
    }

    public int getDBType() {
        return this.mDbType;
    }

    public int getParentDBType() {
        return this.mParentDBType == 65282 ? this.mDbType : this.mParentDBType;
    }

    public int getExtra1DBType() {
        return this.mExtra1DBType == 65282 ? this.mDbType : this.mExtra1DBType;
    }

    public boolean isExtra1DBType() {
        return this.mExtra1DBType == this.mDbType;
    }

    public boolean isExtra2DBType() {
        return this.mExtra2DBType == this.mDbType;
    }

    public int getExtra2DBType() {
        return this.mExtra2DBType == 65282 ? this.mDbType : this.mExtra2DBType;
    }

    public int isIndependence() {
        if (this.mDbType == this.mExtra1DBType || this.mDbType == this.mExtra2DBType) {
            return 3;
        }
        if (this.mDbType == this.mPairDBType || this.mPairDBType == 65282) {
            return 0;
        }
        if (DictDBManager.languageWeight != null) {
            int sourceLanguageWeight = DictDBManager.languageWeight[this.mSourceLanguage];
            int targetLanguageWeight = DictDBManager.languageWeight[this.mTargetLanguage];
            if (this.mSourceLanguage == this.mTargetLanguage) {
                return 0;
            }
            if (sourceLanguageWeight < targetLanguageWeight) {
                return 1;
            }
        }
        return 2;
    }

    public int getSourceLanguage() {
        return this.mSourceLanguage;
    }

    public int getTargetLanguage() {
        return this.mTargetLanguage;
    }

    public String getNotifyFilename() {
        return this.mNotifyFilename;
    }
}
