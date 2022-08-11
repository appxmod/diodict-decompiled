package com.diotek.diodict.engine;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.diotek.diodict.dependency.Dependency;
import com.diodict.decompiled.R;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public class DictDBManager {
    public static final int DBINFO_INDEX_ID_COMPANYLOGORES = 4;
    public static final int DBINFO_INDEX_ID_DBTYPE = 0;
    public static final int DBINFO_INDEX_ID_DICTNAME = 1;
    public static final int DBINFO_INDEX_ID_ENCODING = 9;
    public static final int DBINFO_INDEX_ID_EXAMPLEDB = 13;
    public static final int DBINFO_INDEX_ID_FILENAME = 8;
    public static final int DBINFO_INDEX_ID_ICON = 2;
    public static final int DBINFO_INDEX_ID_IDIOMDB = 12;
    public static final int DBINFO_INDEX_ID_LISTICON = 3;
    public static final int DBINFO_INDEX_ID_NOTIFYID = 16;
    public static final int DBINFO_INDEX_ID_PAIRDB = 10;
    public static final int DBINFO_INDEX_ID_PARENTDB = 11;
    public static final int DBINFO_INDEX_ID_PRODUCTLOGORES = 5;
    public static final int DBINFO_INDEX_ID_PRODUCTSTRINGRES = 6;
    public static final int DBINFO_INDEX_ID_SEARCHMETHOD = 7;
    public static final int DBINFO_INDEX_ID_SOURCELANGUAGE = 14;
    public static final int DBINFO_INDEX_ID_TARGETLANGUAGE = 15;
    public static final int DBINFO_INDEX_ID_TTS_ALLOW = 17;
    public static final int DEDT_MAX = 65282;
    public static final int DEDT_TOTAL_SEARCH = 65520;
    public static final int DEL_ARABIC = 25;
    public static final int DEL_BENGALI = 32;
    public static final int DEL_CHINESE = 4;
    public static final int DEL_CHINESE_PINYIN = 8;
    public static final int DEL_CHINESE_PINYIN_INITIAL = 9;
    public static final int DEL_CHINESE_SIMP = 5;
    public static final int DEL_CHINESE_SIMPTRAD = 7;
    public static final int DEL_CHINESE_TRAD = 6;
    public static final int DEL_DANISH = 20;
    public static final int DEL_DUTCH = 21;
    public static final int DEL_EASTERN = 40;
    public static final int DEL_ENGCHNJPNKOR = 42;
    public static final int DEL_ENGLISH = 2;
    public static final int DEL_ENGLISH_UK = 3;
    public static final int DEL_ESPANOL = 14;
    public static final int DEL_FINNISH = 24;
    public static final int DEL_FRENCH = 12;
    public static final int DEL_GAEILGE = 38;
    public static final int DEL_GERMAN = 13;
    public static final int DEL_GREEK = 36;
    public static final int DEL_HINDI = 31;
    public static final int DEL_INDONESIAN = 26;
    public static final int DEL_ITALIAN = 16;
    public static final int DEL_JAPANESE = 10;
    public static final int DEL_JAPANESE_KANJI = 11;
    public static final int DEL_KOREAN = 0;
    public static final int DEL_KOREAN_OLD = 1;
    public static final int DEL_MALAYSIAN = 30;
    public static final int DEL_MAX = 44;
    public static final int DEL_NORWEGIAN = 22;
    public static final int DEL_PERSIAN = 34;
    public static final int DEL_POLISH = 37;
    public static final int DEL_PORTUGUESE = 15;
    public static final int DEL_RUSSIAN = 18;
    public static final int DEL_SWEDISH = 23;
    public static final int DEL_TAGALOG = 35;
    public static final int DEL_THAI = 28;
    public static final int DEL_THAI_PHONETIC = 29;
    public static final int DEL_TURKISH = 17;
    public static final int DEL_UKRAINIAN = 19;
    public static final int DEL_URUD = 33;
    public static final int DEL_URUD_ARABIC = 43;
    public static final int DEL_VIETNAMESE = 27;
    public static final int DEL_WESTERN = 39;
    public static final int DEL_WORLD = 41;
    public static final int ERROR = -1;
    public static final byte FontTransparency = 64;
    public static final int HANGULRO_CHINESE = 1;
    public static final int HANGULRO_ENGLISH = 0;
    public static final int HANGULRO_JAPANESE = 2;
    public static final int HANGULRO_NOTSUPPORT = -1;
    public static final int HYPERTEXT_CHINESE = 2;
    public static final int HYPERTEXT_ENGLISH = 0;
    public static final int HYPERTEXT_JAPANESE = 3;
    public static final int HYPERTEXT_KOREAN = 1;
    public static final int HYPERTEXT_NONE = -1;
    public static final int RECOG_MODE_CHN = 5;
    public static final int RECOG_MODE_CHN_SIMP = 6;
    public static final int RECOG_MODE_CHN_TRAD = 7;
    public static final int RECOG_MODE_ENG = 0;
    public static final int RECOG_MODE_FRA = 2;
    public static final int RECOG_MODE_GER = 3;
    public static final int RECOG_MODE_JPN = 4;
    public static final int RECOG_MODE_KOR = 1;
    public static final int RECOG_MODE_VET = 8;
    public static final int SPELLCHECK_ENG = 0;
    public static final int TTS_DOWNLOAD_HCI_CHN_ALLOW = 8;
    public static final int TTS_DOWNLOAD_HCI_ENG_ALLOW = 5;
    public static final int TTS_DOWNLOAD_HCI_JPN_ALLOW = 7;
    public static final int TTS_DOWNLOAD_HCI_KOR_ALLOW = 6;
    public static final int TTS_DOWNLOAD_NOT_ALLOW = 0;
    public static final int TTS_DOWNLOAD_SVOX_CHN_ALLOW = 4;
    public static final int TTS_DOWNLOAD_SVOX_ENG_ALLOW = 1;
    public static final int TTS_DOWNLOAD_SVOX_JPN_ALLOW = 3;
    public static final int TTS_DOWNLOAD_SVOX_KOR_ALLOW = 2;
    static Context mContext = null;
    static Resources mDBInfoRes = null;
    static boolean mIsUseDBManager = false;
    public static final String VOICE_MODE_ENG = Locale.US.toString();
    public static final String VOICE_MODE_KOR = Locale.KOREA.toString();
    public static final String VOICE_MODE_JPN = Locale.JAPAN.toString();
    public static final String VOICE_MODE_CHN = Locale.CHINA.toString();
    public static final HashMap<Integer, DBInfo> DICTINFO_TABLE = new HashMap<>();
    public static final HashMap<Integer, Integer> LANGUAGEINFO_TABLE = new HashMap<Integer, Integer>() { // from class: com.diotek.diodict.engine.DictDBManager.1
        private static final long serialVersionUID = 2;

        {
            put(0, Integer.valueOf((int) R.drawable.ic_list_lang_kor));
            put(1, Integer.valueOf((int) R.drawable.ic_list_lang_kor));
            put(2, Integer.valueOf((int) R.drawable.ic_list_lang_eng));
            put(3, Integer.valueOf((int) R.drawable.ic_list_lang_eng));
            put(4, Integer.valueOf((int) R.drawable.ic_list_lang_chn));
            put(5, Integer.valueOf((int) R.drawable.ic_list_lang_chn));
            put(6, Integer.valueOf((int) R.drawable.ic_list_lang_chn));
            put(7, Integer.valueOf((int) R.drawable.ic_list_lang_chn));
            put(8, Integer.valueOf((int) R.drawable.ic_list_lang_chn));
            put(9, Integer.valueOf((int) R.drawable.ic_list_lang_chn));
            put(10, Integer.valueOf((int) R.drawable.ic_list_lang_jpn));
            put(11, Integer.valueOf((int) R.drawable.ic_list_lang_jpn));
            put(12, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(13, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(14, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(15, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(16, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(17, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(18, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(19, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(20, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(21, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(22, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(23, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(24, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(25, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(26, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(27, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(28, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(29, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(30, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(31, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(32, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(33, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(34, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(35, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(36, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(37, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(38, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(39, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(40, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(41, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(42, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(43, Integer.valueOf((int) R.drawable.dic_intro_none));
            put(44, Integer.valueOf((int) R.drawable.dic_intro_none));
        }
    };
    public static ArrayList<Integer> mUseDBList = new ArrayList<>();
    public static int[] languageWeight = null;

    public static void InitDBManager(Context context) {
        mContext = context;
        mDBInfoRes = mContext.getResources();
        Resources resources = mDBInfoRes;
        mIsUseDBManager = true;
        if (mIsUseDBManager) {
            DICTINFO_TABLE.clear();
            mUseDBList.clear();
            DictUtils.setDBPath(context, false);
            if (Dependency.getDevice().needResInstall()) {
                String[] dbFileNames = Dependency.getDevice().getInstallDBFileNames();
                String fontFileName = Dependency.getDevice().getFontName();
                int len$ = dbFileNames.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    String dbname = dbFileNames[i$];
                    if (DictUtils.checkExistFile(DictUtils.getDBPath() + dbname)) {
                        i$++;
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.initialize_message), 0).show();
                        switch (ResInstall.installDB(mContext, dbFileNames, Dependency.getDevice().getInstallDBResNames())) {
                        }
                    }
                }
                if (!DictUtils.checkExistFile(DictUtils.getDBPath() + fontFileName)) {
                    ResInstall.installFont(mContext);
                }
            }
            languageWeight = mContext.getResources().getIntArray(R.array.languageweight);
        }
    }

    public static boolean isInitDBManager() {
        return (mContext != null || !isUsingDBManager()) && mUseDBList.size() > 1;
    }

    public static boolean isUsingDBManager() {
        return mIsUseDBManager;
    }

    public static void setUseDBbyResID(int[] dbResIDList) {
        if (dbResIDList != null) {
            for (int i : dbResIDList) {
                AddDictType(getDictType(i));
            }
        }
    }

    public static void AddDictType(DBInfo dbInfo) {
        if (dbInfo.getDBType() >= 0 && dbInfo.getDBType() != 65282) {
            DICTINFO_TABLE.put(Integer.valueOf(dbInfo.getDBType()), dbInfo);
            mUseDBList.add(Integer.valueOf(dbInfo.getDBType()));
        }
    }

    public static DBInfo getDictType(Context context, int dbInfoResID) {
        TypedArray dbInfo = context.getResources().obtainTypedArray(dbInfoResID);
        int dbType = dbInfo.getInt(0, -1);
        int DictNameResID = dbInfo.getResourceId(1, R.string.none);
        int IconResID = dbInfo.getResourceId(2, R.string.none);
        int ListIconResID = dbInfo.getResourceId(3, R.drawable.dic_intro_none);
        int CompanyLogoResID = dbInfo.getResourceId(4, R.drawable.dic_intro_none);
        int ProductLogoResID = dbInfo.getResourceId(5, R.drawable.dic_intro_none);
        int ProductStringResID = dbInfo.getResourceId(6, R.string.none);
        int searchMethod = dbInfo.getInt(7, 0);
        String dbFname = dbInfo.getString(8);
        int encoding = dbInfo.getInt(9, 0);
        int pairDB = dbInfo.getInt(10, 0);
        int parentDB = dbInfo.getInt(11, 0);
        int idiomDB = dbInfo.getInt(12, 0);
        int exampleDB = dbInfo.getInt(13, 0);
        int SourceLanguage = dbInfo.getInt(14, 0);
        int TargetLanguage = dbInfo.getInt(15, 0);
        String NotifyFileName = dbInfo.getString(16);
        dbInfo.recycle();
        return new DBInfo(dbType, DictNameResID, IconResID, ListIconResID, CompanyLogoResID, ProductLogoResID, ProductStringResID, searchMethod, dbFname, encoding, pairDB, parentDB, idiomDB, exampleDB, SourceLanguage, TargetLanguage, NotifyFileName);
    }

    public static DBInfo getDictType(int dbInfoResID) {
        return getDictType(mContext, dbInfoResID);
    }

    public static int getAutoDicType(int nDicType, boolean bCompulsion, String word) {
        int retDictType = nDicType;
        if (bCompulsion || (word.length() != 0 && !DictUtils.isWildcardCharacter(word.charAt(0)))) {
            int codepage = DictUtils.getCodePage(word);
            DBInfo dbInfo = DICTINFO_TABLE.get(Integer.valueOf(nDicType));
            int codepageToLanguage = codePageToLanguageCode(codepage);
            int sourceLanguage = dbInfo.getSourceLanguage();
            int targetLanguage = dbInfo.getTargetLanguage();
            if (bCompulsion) {
                retDictType = dbInfo.getPairDBType();
            } else if (isLatinEngLanguageDict(nDicType)) {
                retDictType = nDicType;
            } else if (isMatchLanguage(codepageToLanguage, sourceLanguage)) {
                retDictType = nDicType;
            } else if (codepage == 0 && isMatchLanguage(sourceLanguage, 4)) {
                retDictType = dbInfo.getExtra1DBType();
            } else if (codepage == 0 && isMatchLanguage(sourceLanguage, 10)) {
                retDictType = nDicType;
            } else if (isMatchLanguage(codepageToLanguage, targetLanguage)) {
                retDictType = dbInfo.getPairDBType();
            } else if (codepage == 936 && isMatchLanguage(sourceLanguage, 10)) {
                retDictType = dbInfo.getExtra1DBType();
            }
            return retDictType != 65282 ? retDictType : nDicType;
        }
        return nDicType;
    }

    private static boolean isMatchLanguage(int leftLanguage, int rightLanguage) {
        if (leftLanguage == rightLanguage) {
            return true;
        }
        int leftPrimeLanguageCode = getPrimeLanguageCode(leftLanguage);
        int rightPrimeLanguageCode = getPrimeLanguageCode(rightLanguage);
        return leftPrimeLanguageCode == rightPrimeLanguageCode;
    }

    private static int getPrimeLanguageCode(int language) {
        switch (language) {
            case 0:
            case 1:
                return 0;
            case 2:
            case 3:
                return 2;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return 4;
            case 10:
            case 11:
                return 10;
            default:
                return language;
        }
    }

    private static boolean containsDBInfo(int nDicType) {
        return DICTINFO_TABLE.containsKey(Integer.valueOf(nDicType));
    }

    public static int getIdiomDicTypeByCurDicType(int nDicType) {
        if (getCpENGDictionary(nDicType)) {
            return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getExtra1DBType();
        }
        return 65282;
    }

    public static int getExampleDicTypeByCurDicType(int nDicType) {
        if (getCpENGDictionary(nDicType)) {
            return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getExtra2DBType();
        }
        return 65282;
    }

    public static int getOriginalDicTypeByIdiomExampleDicType(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return 65282;
        }
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getParentDBType();
    }

    public static boolean isIdiomDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).isExtra1DBType() && !isLanguageChinese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage()) && !isLanguageJapanese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean isExampleDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).isExtra2DBType() && !isLanguageChinese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage()) && !isLanguageJapanese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static int getPairDicTypeByCurDicType(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? nDicType : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getPairDBType();
    }

    public static int getOriginalDicTypeByNotIndependenceDicType(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? nDicType : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getParentDBType();
    }

    public static int getKanjiDicTypeByCurDicType(int nDicType) {
        if (getCpJPNDictionary(nDicType)) {
            return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getExtra1DBType();
        }
        return 65282;
    }

    public static int getPinyinDicTypeByCurDicType(int nDicType) {
        if (getCpCHNDictionary(nDicType)) {
            return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getExtra1DBType();
        }
        return 65282;
    }

    public static int getOldKorDicTypeByCurDicType(int nDicType) {
        if (getCpKORDictionary(nDicType)) {
            return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getExtra1DBType();
        }
        return 65282;
    }

    public static boolean getCpENGDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageEnglish(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean isEngDict(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageEnglish(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage()) || isLanguageEnglish(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getTargetLanguage());
    }

    public static boolean isChnInitialDict(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageChineseInitial(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean getCpKORDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageKorean(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean isKorDict(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageKorean(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage()) || isLanguageKorean(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getTargetLanguage());
    }

    public static boolean getCpCHNDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageChinese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean isChnDict(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageChinese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage()) || isLanguageChinese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getTargetLanguage());
    }

    public static boolean getCpJPNDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageJapanese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean isJpnDict(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageJapanese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage()) || isLanguageJapanese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getTargetLanguage());
    }

    public static boolean isKanjiDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        boolean isJapanLanguage = isLanguageJapanese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
        boolean isExtraDB = DICTINFO_TABLE.get(Integer.valueOf(nDicType)).isExtra1DBType();
        return isJapanLanguage && isExtraDB;
    }

    public static boolean isPinyinDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        boolean isChineseLanguage = isLanguageChinese(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
        boolean isExtraDB = DICTINFO_TABLE.get(Integer.valueOf(nDicType)).isExtra1DBType();
        return isChineseLanguage && isExtraDB;
    }

    public static boolean isLanguageLatin(int language) {
        return language == 27 || language == 26 || language == 26 || language == 20 || language == 21 || language == 14 || language == 24 || language == 12 || language == 13 || language == 23 || language == 22 || language == 37;
    }

    private static boolean isLatinEngLanguageDict(int nDicType) {
        DBInfo dbInfo = DICTINFO_TABLE.get(Integer.valueOf(nDicType));
        int sourceLanguage = dbInfo.getSourceLanguage();
        int targetLanguage = dbInfo.getTargetLanguage();
        return (isLanguageLatin(sourceLanguage) || isLanguageEnglish(sourceLanguage)) && (isLanguageLatin(targetLanguage) || isLanguageEnglish(targetLanguage));
    }

    public static boolean getCpKORTOJPNDictionary(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return isLanguageKorean(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static boolean isOldKorDict(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage() == mContext.getResources().getInteger(R.integer.DEL_KOREAN_OLD);
    }

    public static boolean isTotalSearch(int nDicType) {
        return containsDBInfo(nDicType) && nDicType >= 0 && nDicType == mContext.getResources().getInteger(R.integer.DEDT_TOTAL_SEARCH);
    }

    private static Context getContextByDBType(int nDictType) {
        return mContext;
    }

    private static int getDictNameResId(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? R.string.none : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getDicNameResId();
    }

    public static String getDictName(int nDicType) {
        return getContextByDBType(nDicType).getResources().getString(getDictNameResId(nDicType));
    }

    private static int getDictIconResID(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? R.drawable.dic_intro_none : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getIconResID();
    }

    public static Drawable getDictIcon(int nDicType) {
        return getContextByDBType(nDicType).getResources().getDrawable(getDictIconResID(nDicType));
    }

    public static Drawable getDictListIcon(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return mContext.getResources().getDrawable(R.drawable.dic_intro_none);
        }
        int resID = LANGUAGEINFO_TABLE.get(Integer.valueOf(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage())).intValue();
        return mContext.getResources().getDrawable(resID);
    }

    public static int getDictCompanyLogoResId(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? R.drawable.dic_intro_none : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getCompanyLogoResId();
    }

    public static Drawable getDictCompanyLogo(int nDicType) {
        return getContextByDBType(nDicType).getResources().getDrawable(getDictCompanyLogoResId(nDicType));
    }

    private static int getDictProductLogoResId(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? R.drawable.dic_intro_none : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getProductLogoResId();
    }

    public static Drawable getDictProductLogo(int nDicType) {
        return getContextByDBType(nDicType).getResources().getDrawable(getDictProductLogoResId(nDicType));
    }

    private static int getDictProductStringResId(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? R.drawable.dic_intro_none : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getProductStringResId();
    }

    public static String getDictProductString(int nDicType) {
        return getContextByDBType(nDicType).getResources().getString(getDictProductStringResId(nDicType));
    }

    public static int getDictSearchMethod(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? R.drawable.dic_intro_none : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSearchMethod();
    }

    public static String getDictFilename(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? mDBInfoRes.getString(R.string.none) : DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getFilename();
    }

    public static int getDictIndependence(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return 0;
        }
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).isIndependence();
    }

    public static int getDictEncode(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return -1;
        }
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getEncode();
    }

    public static boolean getDictAutoChange(int nDicType) {
        return containsDBInfo(nDicType) && nDicType >= 0 && getDictIndependence(nDicType) != 3;
    }

    public static int getDictHangulroLang(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return -1;
        }
        int sourceLanguage = DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage();
        return languageCodeToHangulroMode(sourceLanguage);
    }

    public static String getDictSpellCheckFileName(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? mDBInfoRes.getString(R.string.none) : getSpellChekDBFileName(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static int getDictHyperTextLang(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return -1;
        }
        return languageCodeToHyperMode(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static int getDictRecognizeLang(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return 0;
        }
        return getRecogMode(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static String getDictVoiceLang(int nDicType) {
        return (!containsDBInfo(nDicType) || nDicType < 0) ? VOICE_MODE_ENG : getVoiceMode(DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage());
    }

    public static String getDictNotifyFileName(int nDicType) {
        return DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getNotifyFilename();
    }

    public static boolean isUnicode(int nDicType) {
        if (!containsDBInfo(nDicType) || nDicType < 0) {
            return false;
        }
        switch (getDictEncode(nDicType)) {
            case 0:
            case 1:
                return true;
            default:
                return false;
        }
    }

    public static String getInputLangaugeStr(int dictType, int currentSearchMethodId) {
        if (!containsDBInfo(dictType)) {
            return Locale.US.toString();
        }
        String dictVoiceLang = getDictVoiceLang(dictType);
        if (currentSearchMethodId == 8) {
            String lanStr = DictType.VOICE_MODE_KOR;
            return lanStr;
        }
        return dictVoiceLang;
    }

    public static int getIMEMode(int dictType, int currentSearchMethodId) {
        if (!containsDBInfo(dictType)) {
            return 0;
        }
        int dictRecognizeLang = getDictRecognizeLang(dictType);
        if (currentSearchMethodId == mDBInfoRes.getInteger(R.integer.SEARCHTYPE_HANGULRO)) {
            return 1;
        }
        return dictRecognizeLang;
    }

    public static boolean codePageAlphabet(int codepage) {
        return codepage == 0 || DictUtils.ISLATIN_CP(codepage) || codepage == 21866;
    }

    public static Integer[] getDictList() {
        int size = mUseDBList.size();
        Integer[] result = new Integer[size];
        for (int i = 0; i < size; i++) {
            result[i] = mUseDBList.get(i);
        }
        return result;
    }

    public static boolean isTargetLanguageJpn(int dictType) {
        return sourceLanguageCompare(dictType, R.integer.DEL_JAPANESE);
    }

    public static boolean sourceLanguageCompare(int dictType, int resLanguageID) {
        if (!containsDBInfo(dictType)) {
            return false;
        }
        int language = mDBInfoRes.getInteger(resLanguageID);
        return language == DICTINFO_TABLE.get(Integer.valueOf(dictType)).getTargetLanguage();
    }

    public static boolean isExtraDBType(int nDictType) {
        if (!containsDBInfo(nDictType)) {
            return false;
        }
        boolean ext1db = DICTINFO_TABLE.get(Integer.valueOf(nDictType)).isExtra1DBType();
        boolean ext2db = DICTINFO_TABLE.get(Integer.valueOf(nDictType)).isExtra2DBType();
        return ext1db || ext2db;
    }

    private static int codePageToLanguageCode(int codepage) {
        int resID = R.integer.DEL_MAX;
        switch (codepage) {
            case -1:
                resID = R.integer.DEL_MAX;
                break;
            case 0:
                resID = R.integer.DEL_ENGLISH;
                break;
            case DictInfo.CP_JPN /* 932 */:
                resID = R.integer.DEL_JAPANESE;
                break;
            case DictInfo.CP_CHN /* 936 */:
                resID = R.integer.DEL_CHINESE_SIMP;
                break;
            case DictInfo.CP_KOR /* 949 */:
                resID = R.integer.DEL_KOREAN;
                break;
            case DictInfo.CP_TWN /* 950 */:
                resID = R.integer.DEL_CHINESE_TRAD;
                break;
            case DictInfo.CP_1250 /* 1250 */:
                resID = R.integer.DEL_WORLD;
                break;
            case DictInfo.CP_LT1 /* 1252 */:
                resID = R.integer.DEL_WORLD;
                break;
            case DictInfo.CP_TUR /* 1254 */:
                resID = R.integer.DEL_TURKISH;
                break;
            case DictInfo.CP_BAL /* 1257 */:
                resID = R.integer.DEL_WORLD;
                break;
            case DictInfo.CP_HIN /* 1331 */:
                resID = R.integer.DEL_HINDI;
                break;
            case DictInfo.CP_CRL /* 21866 */:
                resID = R.integer.DEL_WORLD;
                break;
        }
        return mContext.getResources().getInteger(resID);
    }

    public static Integer[] getDictListByCodepage(Integer[] supportDictList, int codepage) {
        ArrayList<Integer> languageList = codePageToLanguageList(codepage);
        ArrayList<Integer> dictList = new ArrayList<>();
        for (Integer dbType : supportDictList) {
            Iterator i$ = languageList.iterator();
            while (true) {
                if (i$.hasNext()) {
                    Integer language = (Integer) i$.next();
                    if (DICTINFO_TABLE.get(dbType).getSourceLanguage() == language.intValue()) {
                        dictList.add(dbType);
                        break;
                    }
                }
            }
        }
        return (Integer[]) dictList.toArray(new Integer[dictList.size()]);
    }

    private static ArrayList<Integer> codePageToLanguageList(int codepage) {
        ArrayList<Integer> resIDList = new ArrayList<>();
        switch (codepage) {
            case -1:
                resIDList.add(44);
                break;
            case DictInfo.CP_JPN /* 932 */:
                resIDList.add(10);
                break;
            case DictInfo.CP_CHN /* 936 */:
            case DictInfo.CP_TWN /* 950 */:
                resIDList.add(4);
                resIDList.add(8);
                resIDList.add(9);
                resIDList.add(5);
                resIDList.add(7);
                resIDList.add(6);
                resIDList.add(11);
                break;
            case DictInfo.CP_KOR /* 949 */:
                resIDList.add(0);
                resIDList.add(1);
                break;
            case DictInfo.CP_TUR /* 1254 */:
                resIDList.add(17);
                break;
            case DictInfo.CP_HIN /* 1331 */:
                resIDList.add(31);
                break;
            default:
                resIDList.add(25);
                resIDList.add(32);
                resIDList.add(20);
                resIDList.add(21);
                resIDList.add(2);
                resIDList.add(3);
                resIDList.add(14);
                resIDList.add(24);
                resIDList.add(12);
                resIDList.add(38);
                resIDList.add(13);
                resIDList.add(36);
                resIDList.add(26);
                resIDList.add(16);
                resIDList.add(30);
                resIDList.add(22);
                resIDList.add(34);
                resIDList.add(37);
                resIDList.add(15);
                resIDList.add(18);
                resIDList.add(23);
                resIDList.add(35);
                resIDList.add(28);
                resIDList.add(29);
                resIDList.add(19);
                resIDList.add(33);
                resIDList.add(43);
                resIDList.add(27);
                resIDList.add(39);
                resIDList.add(40);
                resIDList.add(41);
                break;
        }
        return resIDList;
    }

    private static boolean isLanguageKorean(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_KOREAN) == language || mDBInfoRes.getInteger(R.integer.DEL_KOREAN_OLD) == language;
    }

    private static boolean isLanguageFrench(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_FRENCH) == language;
    }

    private static boolean isLanguageGerman(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_GERMAN) == language;
    }

    private static boolean isLanguageChinese_SIMP(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_CHINESE_SIMP) == language;
    }

    private static boolean isLanguageChinese_TRAD(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_CHINESE_TRAD) == language;
    }

    private static boolean isLanguageChinese(int language) {
        return language == mContext.getResources().getInteger(R.integer.DEL_CHINESE) || language == mContext.getResources().getInteger(R.integer.DEL_CHINESE_PINYIN) || language == mContext.getResources().getInteger(R.integer.DEL_CHINESE_SIMP) || language == mContext.getResources().getInteger(R.integer.DEL_CHINESE_SIMPTRAD) || language == mContext.getResources().getInteger(R.integer.DEL_CHINESE_TRAD);
    }

    private static boolean isLanguageEnglish(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_ENGLISH) == language;
    }

    private static boolean isLanguageJapanese(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_JAPANESE) == language || mDBInfoRes.getInteger(R.integer.DEL_JAPANESE_KANJI) == language;
    }

    private static boolean isLanguageVetnamese(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_VIETNAMESE) == language;
    }

    private static boolean isLanguageChineseInitial(int language) {
        return mDBInfoRes.getInteger(R.integer.DEL_CHINESE_PINYIN_INITIAL) == language;
    }

    private static int languageCodeToHangulroMode(int language) {
        if (isLanguageEnglish(language)) {
            return 0;
        }
        if (isLanguageJapanese(language)) {
            return 2;
        }
        if (isLanguageChinese(language)) {
            return 1;
        }
        return -1;
    }

    private static int languageCodeToHyperMode(int language) {
        if (isLanguageKorean(language)) {
            return 1;
        }
        if (isLanguageEnglish(language)) {
            return 0;
        }
        if (isLanguageJapanese(language)) {
            return 3;
        }
        if (isLanguageChinese(language)) {
            return 2;
        }
        return -1;
    }

    private static String getSpellChekDBFileName(int language) {
        if (isLanguageEnglish(language)) {
            return mDBInfoRes.getString(R.string.SPELLCHECK_ENGDBNAME);
        }
        if (isLanguageKorean(language)) {
            return mDBInfoRes.getString(R.string.SPELLCHECK_NONE);
        }
        if (isLanguageJapanese(language)) {
            return mDBInfoRes.getString(R.string.SPELLCHECK_NONE);
        }
        if (isLanguageChinese(language)) {
            return mDBInfoRes.getString(R.string.SPELLCHECK_NONE);
        }
        return mDBInfoRes.getString(R.string.SPELLCHECK_NONE);
    }

    private static int getRecogMode(int language) {
        if (isLanguageEnglish(language)) {
            return 0;
        }
        if (isLanguageFrench(language)) {
            return 2;
        }
        if (isLanguageGerman(language)) {
            return 3;
        }
        if (isLanguageKorean(language)) {
            return 1;
        }
        if (isLanguageJapanese(language)) {
            return 4;
        }
        if (isLanguageChinese_SIMP(language)) {
            return 6;
        }
        if (isLanguageChinese_TRAD(language)) {
            return 7;
        }
        if (isLanguageChinese(language)) {
            return 5;
        }
        return isLanguageVetnamese(language) ? 8 : 0;
    }

    private static String getVoiceMode(int language) {
        if (isLanguageEnglish(language)) {
            return VOICE_MODE_ENG;
        }
        if (isLanguageKorean(language)) {
            return VOICE_MODE_KOR;
        }
        if (isLanguageJapanese(language)) {
            return VOICE_MODE_JPN;
        }
        if (isLanguageChinese(language)) {
            return VOICE_MODE_CHN;
        }
        return VOICE_MODE_ENG;
    }

    public static String getCurDictHint(int nDicType) {
        if (nDicType == 65520) {
            return " ";
        }
        int srcLang = DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getSourceLanguage();
        int tgtLang = DICTINFO_TABLE.get(Integer.valueOf(nDicType)).getTargetLanguage();
        String str = getLangName(srcLang) + "-" + getLangName(tgtLang);
        return str;
    }

    public static String getDBVersion(int nDicType) {
        if (!EngineManager3rd.openDB(nDicType)) {
            return "";
        }
        byte[] byteRes = EngineNative3rd.LibGetDBVersion(nDicType);
        try {
            String resDBVersion = new String(byteRes, "UTF-8");
            return resDBVersion;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getLangName(int cp) {
        if (cp == mDBInfoRes.getInteger(R.integer.DEL_ENGLISH) || cp == mDBInfoRes.getInteger(R.integer.DEL_ENGLISH_UK)) {
            String str = mContext.getResources().getString(R.string.edit_hit_eng);
            return str;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_KOREAN) || cp == mDBInfoRes.getInteger(R.integer.DEL_KOREAN_OLD)) {
            String str2 = mContext.getResources().getString(R.string.edit_hit_kor);
            return str2;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_JAPANESE) || cp == mDBInfoRes.getInteger(R.integer.DEL_JAPANESE_KANJI)) {
            String str3 = mContext.getResources().getString(R.string.edit_hit_jpn);
            return str3;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_CHINESE) || cp == mDBInfoRes.getInteger(R.integer.DEL_CHINESE_SIMP) || cp == mDBInfoRes.getInteger(R.integer.DEL_CHINESE_PINYIN) || cp == mDBInfoRes.getInteger(R.integer.DEL_CHINESE_PINYIN_INITIAL) || cp == mDBInfoRes.getInteger(R.integer.DEL_CHINESE_SIMPTRAD) || cp == mDBInfoRes.getInteger(R.integer.DEL_CHINESE_TRAD)) {
            String str4 = mContext.getResources().getString(R.string.edit_hit_chn);
            return str4;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_ESPANOL)) {
            String str5 = mContext.getResources().getString(R.string.edit_hit_spa);
            return str5;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_PORTUGUESE)) {
            String str6 = mContext.getResources().getString(R.string.edit_hit_por);
            return str6;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_GERMAN)) {
            String str7 = mContext.getResources().getString(R.string.edit_hit_ger);
            return str7;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_GERMAN)) {
            String str8 = mContext.getResources().getString(R.string.edit_hit_fra);
            return str8;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_GERMAN)) {
            String str9 = mContext.getResources().getString(R.string.edit_hit_ita);
            return str9;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_MALAYSIAN)) {
            String str10 = mContext.getResources().getString(R.string.edit_hit_mal);
            return str10;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_RUSSIAN)) {
            String str11 = mContext.getResources().getString(R.string.edit_hit_rus);
            return str11;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_UKRAINIAN)) {
            String str12 = mContext.getResources().getString(R.string.edit_hit_ukr);
            return str12;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_DANISH)) {
            String str13 = mContext.getResources().getString(R.string.edit_hit_dan);
            return str13;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_FINNISH)) {
            String str14 = mContext.getResources().getString(R.string.edit_hit_fin);
            return str14;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_DUTCH)) {
            String str15 = mContext.getResources().getString(R.string.edit_hit_dut);
            return str15;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_NORWEGIAN)) {
            String str16 = mContext.getResources().getString(R.string.edit_hit_nor);
            return str16;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_SWEDISH)) {
            String str17 = mContext.getResources().getString(R.string.edit_hit_swe);
            return str17;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_INDONESIAN)) {
            String str18 = mContext.getResources().getString(R.string.edit_hit_ind);
            return str18;
        } else if (cp == mDBInfoRes.getInteger(R.integer.DEL_VIETNAMESE)) {
            String str19 = mContext.getResources().getString(R.string.edit_hit_vie);
            return str19;
        } else if (cp != mDBInfoRes.getInteger(R.integer.DEL_THAI) && cp != mDBInfoRes.getInteger(R.integer.DEL_THAI_PHONETIC)) {
            return "";
        } else {
            String str20 = mContext.getResources().getString(R.string.edit_hit_tha);
            return str20;
        }
    }

    public static boolean checkInstalledOxfordChineseEnglish(Context context) {
        if (mContext == null || mDBInfoRes == null) {
            if (context == null) {
                return false;
            }
            InitDBManager(context);
        }
        int size = mUseDBList.size();
        int dicType_chneng = mDBInfoRes.getInteger(R.integer.DEDT_OXFORD_ENG_CHN);
        int dicType_chneng_fltrp = mDBInfoRes.getInteger(R.integer.DEDT_OXFORD_FLTRP_CHN_ENG);
        for (int i = 0; i < size; i++) {
            if (dicType_chneng == mUseDBList.get(i).intValue() || dicType_chneng_fltrp == mUseDBList.get(i).intValue()) {
                return true;
            }
        }
        return false;
    }
	
	public static boolean isOxfordChineseTrad(int nDicType) {
		return false;//nDicType == mContext.getResources().getInteger(R.dbtype.DEDT_OXFORD_ENGTOTRAD) || nDicType == mContext.getResources().getInteger(R.dbtype.DEDT_OXFORD_TRADTOENG) || nDicType == mContext.getResources().getInteger(R.dbtype.DEDT_OXFORD_PINYIN_TRADTOENG) || nDicType == mContext.getResources().getInteger(R.dbtype.DEDT_OXFORD_ZHUYIN_TRADTOENG);
	}
	
}
