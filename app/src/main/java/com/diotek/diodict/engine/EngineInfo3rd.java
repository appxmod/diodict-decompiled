package com.diotek.diodict.engine;

import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes.dex */
public class EngineInfo3rd {
    public static ArrayList<Integer> ALLTTS_TABLE = new ArrayList<Integer>() { // from class: com.diotek.diodict.engine.EngineInfo3rd.1
        private static final long serialVersionUID = 3;

        {
            add(65536);
            add(Integer.valueOf((int) EngineInfo3rd.TTS_CHINESE));
            add(Integer.valueOf((int) EngineInfo3rd.TTS_JAPANESE));
            add(Integer.valueOf((int) EngineInfo3rd.TTS_KOREAN));
        }
    };
    public static final byte FontTransparency = 64;
    public static final int TTS_CHINESE = 65537;
    public static final int TTS_ENGLISH = 65536;
    public static final int TTS_JAPANESE = 65538;
    public static final int TTS_KOREAN = 65539;

    public static int getOriginalDicTypeByNotIndependenceDicType(int nDicType, boolean bForSearch) {
        if (!bForSearch || !DictDBManager.isKanjiDictionary(nDicType)) {
            return (!bForSearch || !DictDBManager.isPinyinDictionary(nDicType)) ? DictDBManager.getOriginalDicTypeByNotIndependenceDicType(nDicType) : nDicType;
        }
        return nDicType;
    }

    public static boolean getCheckAuthTTS(int dicType) {
        if (DictDBManager.getCpENGDictionary(dicType)) {
            return IsInstalledTTS(65536);
        }
        if (DictDBManager.getCpKORDictionary(dicType)) {
            return IsInstalledTTS(TTS_KOREAN);
        }
        if (DictDBManager.getCpJPNDictionary(dicType)) {
            return IsInstalledTTS(TTS_JAPANESE);
        }
        if (DictDBManager.getCpCHNDictionary(dicType)) {
            return IsInstalledTTS(TTS_CHINESE);
        }
        return false;
    }

    public static boolean IsTTSAvailableCodePage(int codepage) {
        switch (codepage) {
            case 0:
            case DictInfo.CP_1250 /* 1250 */:
            case DictInfo.CP_LT1 /* 1252 */:
            case DictInfo.CP_TUR /* 1254 */:
            case DictInfo.CP_BAL /* 1257 */:
            case DictInfo.CP_CRL /* 21866 */:
                boolean result = IsInstalledTTS(65536);
                return result;
            case DictInfo.CP_JPN /* 932 */:
                boolean result2 = IsInstalledTTS(TTS_JAPANESE);
                return result2;
            case DictInfo.CP_CHN /* 936 */:
                boolean result3 = IsInstalledTTS(TTS_CHINESE);
                return result3;
            case DictInfo.CP_KOR /* 949 */:
                boolean result4 = IsInstalledTTS(TTS_KOREAN);
                return result4;
            default:
                return false;
        }
    }

    public static boolean IsTTSAvailableKeyword(String keyword) {
        boolean result = true;
        char[] charKeyword = new char[keyword.length()];
        keyword.getChars(0, keyword.length(), charKeyword, 0);
        for (int i = 0; i < charKeyword.length && i < 2; i++) {
            if ((charKeyword[i] >= '!' && charKeyword[i] <= '/') || ((charKeyword[i] >= ':' && charKeyword[i] <= '?') || ((charKeyword[i] >= '[' && charKeyword[i] <= '`') || ((charKeyword[i] >= '{' && charKeyword[i] <= '~') || ((charKeyword[i] >= 161 && charKeyword[i] <= 191) || (charKeyword[i] >= 8704 && charKeyword[i] <= 8942)))))) {
                if (i == 0 || !result) {
                    result = false;
                }
            } else {
                result = true;
            }
        }
        return result;
    }

    private static boolean IsInstalledTTS(int nLang) {
        boolean result = false;
        Integer[] ttslist = EngineManager3rd.getSupporTTS();
        if (ttslist == null) {
            return false;
        }
        int i = 0;
        while (true) {
            if (i >= ttslist.length) {
                break;
            } else if (ttslist[i].intValue() != nLang) {
                i++;
            } else {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean isUnicode(int nDicType) {
        switch (DictDBManager.getDictEncode(nDicType)) {
            case 0:
            case 1:
                return true;
            default:
                return false;
        }
    }

    public static String getInputLangaugeStr(int dictType, int currentSearchMethodId) {
        Locale.US.toString();
        String lanStr = DictDBManager.getDictVoiceLang(dictType);
        if (currentSearchMethodId == 8) {
            String lanStr2 = DictType.VOICE_MODE_KOR;
            return lanStr2;
        }
        return lanStr;
    }

    public static int getIMEMode(int dictType, int currentSearchMethodId) {
        int imemode = DictDBManager.getDictRecognizeLang(dictType);
        if (currentSearchMethodId == 8) {
            return 1;
        }
        if (currentSearchMethodId == 512) {
            return 0;
        }
        return imemode;
    }

    public static boolean codePageAlphabet(int codepage) {
        return codepage == 0 || DictUtils.ISLATIN_CP(codepage) || codepage == 21866;
    }

    public static boolean getIndependenceMain(int nDicType) {
        return DictDBManager.getDictIndependence(nDicType) == 0 || DictDBManager.getDictIndependence(nDicType) == 1;
    }

    public static boolean getAutoChange(int nDicType) {
        int tmpDicType = getOriginalDicTypeByNotIndependenceDicType(nDicType, true);
        return DictDBManager.getDictIndependence(tmpDicType) == 1 || DictDBManager.getDictIndependence(tmpDicType) == 2;
    }

    public static boolean IsIndependenceSub(int nDictype) {
        if (DictDBManager.getDictIndependence(nDictype) == 2) {
            int nParentDictype = DictDBManager.getPairDicTypeByCurDicType(nDictype);
            if (DictDBManager.getDictCompanyLogoResId(nDictype) != DictDBManager.getDictCompanyLogoResId(nParentDictype)) {
                return true;
            }
        }
        return false;
    }
}
