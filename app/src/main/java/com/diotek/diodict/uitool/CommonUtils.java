package com.diotek.diodict.uitool;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.TTSEngine;
import com.diotek.diodict.mean.MSG;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/* loaded from: classes.dex */
public class CommonUtils {
    public static final int WEB_SEARCH_TYPE_GOOGLE = 0;
    public static final int WEB_SEARCH_TYPE_WIKI = 1;
    private static final String[] EXCEPTION_TTSWORD = {"2,4,5-T", "2, 4, 5-T", "2,4-D", "2, 4-D", "24/7", "3-D[thrée-ꌓ] TV", ".22", "30-'06, 30/06", "1471", "0800 nùmber", "0898 nùmber", "1800 nùmber", "401K", "404", "747", "773H", "911", "9.11", "9/11", "999", "@", "A2 (level)", "AAA", "A", "A & E", "A & R", "A & M", "A. & M.", "A. & N.", "A & P", "a.a.O.", "A₂", "000", "6 is to 2 as 12 is to 4. 6:2=12:4", "ah", "ah¹", "ah²", "ahh", "Å·bo", "n/a, NA", "NAACP, N.A.A.C.P.", "NAAQS", "naart·je, -jie", "〇[líng]"};
    private static final String[] CHANGE_TTSWORD = {"2 4 5T", "2 4 5T", "2 4D", "2 4D", "24 7", "3 D TV", "22", "30 o6", "1 4 7 1", "o800 number", "o8 98 number", "1,800 number", "4o1K", "4o4", "7 4 7", "7 7 3H", "9 1 1", "9 11", "9 11", "9 9 9", "at", "A2 level", "A A A", "A.", "A. N E.", "A. N R.", "A. N M.", "A. N M.", "A. N N.", "A. N P.", "A. A. O.", "A2", "triple o", "6 is to 2 as 12 is to 4 6 2 12 4", "aah", "aah", "aah", "aah", "oubu", "N A.", "N double A. C P", "N double A. Q S", "na rjie", "零[ling]"};
    static final String[] VOWEL = {"a", "A", "e", "E", "i", "I", "O", "o", "u", "U", "y", "Y", "@"};
    private static boolean mUseKeypadNoExtractUI = true;
    private static float mDensity = -1.0f;

    public static PopupWindow makeWindowWithPopupWindow(Context context, int contentId, View contentView, Drawable popupBackground, PopupWindow.OnDismissListener onDismissListener) {
        return makeWindowWithPopupWindow(context, contentId, contentView, popupBackground, onDismissListener, true);
    }

    public static PopupWindow makeWindowWithPopupWindow(Context context, int contentId, View contentView, Drawable popupBackground, PopupWindow.OnDismissListener onDismissListener, boolean bFocus) {
        if (context != null) {
            PopupWindow win = new PopupWindow(context);
            win.setTouchable(true);
            win.setOutsideTouchable(true);
            win.setFocusable(bFocus);
            win.setClippingEnabled(false);
            win.setBackgroundDrawable(popupBackground);
            if (onDismissListener != null) {
                win.setOnDismissListener(onDismissListener);
            }
            if (contentId != 0) {
                LayoutInflater inflate = (LayoutInflater) context.getSystemService("layout_inflater");
                CustomPopupLinearLayout mPopupContent = (CustomPopupLinearLayout) inflate.inflate(contentId, (ViewGroup) null);
                win.setContentView(mPopupContent);
            } else if (contentView != null) {
                win.setContentView(contentView);
            }
            return win;
        }
        MSG.l(2, "makeWindowWithPopupWindow(): context is null");
        return null;
    }

    public static PopupWindow makeWindowWithPopupWindowTTS(Context context, int contentId, View contentView, Drawable popupBackground) {
        return makeWindowWithPopupWindowTTS(context, contentId, contentView, popupBackground, true);
    }

    public static PopupWindow makeWindowWithPopupWindowTTS(Context context, int contentId, View contentView, Drawable popupBackground, boolean bFocus) {
        PopupWindow win = null;
        if (context == null) {
            MSG.l(2, "makeWindowWithPopupWindow(): context is null");
            return null;
        }
        if (0 == 0) {
            win = new PopupWindow(context);
            win.setTouchable(true);
            win.setOutsideTouchable(true);
            win.setFocusable(bFocus);
            win.setClippingEnabled(false);
            win.setBackgroundDrawable(popupBackground);
        }
        if (contentId != 0) {
            LayoutInflater inflate = (LayoutInflater) context.getSystemService("layout_inflater");
            LinearLayout mPopupContent = (LinearLayout) inflate.inflate(contentId, (ViewGroup) null);
            win.setContentView(mPopupContent);
        } else if (contentView != null) {
            win.setContentView(contentView);
        }
        return win;
    }

    public static void startWebSearch(Context ctx, String word, int type) {
        String url;
        if (type == 0) {
            url = Dependency.getLocale().getGoogleAddress();
        } else {
            url = Dependency.getLocale().getWikipediaAddress();
        }
        try {
            url = url + URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        if (type == 0) {
            url = url + "&aq=f&aqi=&aql=&oq=";
        }
        Intent i = new Intent();
        i.setComponent(null);
        i.setAction("android.intent.action.VIEW");
        i.addCategory("android.intent.category.BROWSABLE");
        i.setData(Uri.parse(url));
        ctx.startActivity(i);
    }

    public static void playTTS(int isUsUk, String keyword, String word, int dbType, int count) {
        TTSEngine.StopTTS();
        if (word != null && word.length() > 0) {
            String convertTTSkeyword = convertTTSWord(keyword, true, dbType);
            String convertTTSWord = convertTTSWord(word, false, dbType);
            TTSEngine.PlayTTS(isUsUk, convertTTSkeyword, convertTTSWord, dbType, count);
        }
    }

    public static void playTTS(int isUsUk, String word, int dbType, int count) {
        if (word != null && word.length() > 0) {
            String convertTTSWord = convertTTSWord(word, true, dbType);
            TTSEngine.PlayTTS(isUsUk, null, convertTTSWord, dbType, count);
        }
    }

    public static void stopTTS() {
        TTSEngine.StopTTS();
    }

    public static boolean isPlaying() {
        return TTSEngine.isPlaying();
    }

    public static int getTTSLanguage(String word, int dbType) {
        int Language = -1;
        if (word == null || word.length() == 0) {
            return -1;
        }
        int CodePage = DictUtils.getCodePage(word);
        switch (CodePage) {
            case 0:
            case DictInfo.CP_1250 /* 1250 */:
            case DictInfo.CP_LT1 /* 1252 */:
            case DictInfo.CP_TUR /* 1254 */:
            case DictInfo.CP_BAL /* 1257 */:
            case DictInfo.CP_CRL /* 21866 */:
                Language = 65536;
                break;
            case DictInfo.CP_JPN /* 932 */:
                Language = EngineInfo3rd.TTS_JAPANESE;
                break;
            case DictInfo.CP_CHN /* 936 */:
                if (DictDBManager.getCpJPNDictionary(dbType)) {
                    MSG.l("SetTTSVoiceLanguage : CP_JPN");
                    Language = EngineInfo3rd.TTS_JAPANESE;
                    break;
                } else if (DictDBManager.getCpKORDictionary(dbType)) {
                    MSG.l("SetTTSVoiceLanguage : CP_KOR");
                    Language = EngineInfo3rd.TTS_KOREAN;
                    break;
                } else if (DictDBManager.getCpCHNDictionary(dbType)) {
                    MSG.l("SetTTSVoiceLanguage : CP_CHN");
                    Language = EngineInfo3rd.TTS_CHINESE;
                    break;
                } else {
                    int pairDbType = DictDBManager.getPairDicTypeByCurDicType(dbType);
                    if (DictDBManager.getCpJPNDictionary(pairDbType)) {
                        MSG.l("SetTTSVoiceLanguage : CP_JPN");
                        Language = EngineInfo3rd.TTS_JAPANESE;
                        break;
                    } else if (DictDBManager.getCpKORDictionary(pairDbType)) {
                        MSG.l("SetTTSVoiceLanguage : CP_KOR");
                        Language = EngineInfo3rd.TTS_KOREAN;
                        break;
                    } else if (DictDBManager.getCpCHNDictionary(pairDbType)) {
                        MSG.l("SetTTSVoiceLanguage : CP_CHN");
                        Language = EngineInfo3rd.TTS_CHINESE;
                        break;
                    }
                }
                break;
            case DictInfo.CP_KOR /* 949 */:
                Language = EngineInfo3rd.TTS_KOREAN;
                break;
        }
        return Language;
    }

    public static int getTTSLanguage(int dictType) {
        if (DictDBManager.getCpENGDictionary(dictType)) {
            return 65536;
        }
        if (DictDBManager.getCpCHNDictionary(dictType)) {
            return EngineInfo3rd.TTS_CHINESE;
        }
        if (DictDBManager.isJpnDict(dictType)) {
            return EngineInfo3rd.TTS_JAPANESE;
        }
        if (!DictDBManager.getCpKORDictionary(dictType)) {
            return -1;
        }
        return EngineInfo3rd.TTS_KOREAN;
    }

    public static boolean getSupportTTSLanguage(int language) {
        Integer[] supportTTS;
        if (language == -1 || !Dependency.isContainTTS() || (supportTTS = EngineManager3rd.getSupporTTS()) == null) {
            return false;
        }
        for (Integer num : supportTTS) {
            if (language == num.intValue()) {
                return true;
            }
        }
        return false;
    }

    public static String GetKeyword(String pKeyWord, boolean bTTSMode, boolean bKeyword) {
        boolean isSeperateStart = false;
        String sResult = "";
        int txtLen = pKeyWord.length();
        for (int i = 0; i < txtLen; i++) {
            char c = pKeyWord.charAt(i);
            if (bKeyword) {
                if (isSeperateStart && c == ' ') {
                    break;
                }
                isSeperateStart = false;
                if (c == ',') {
                    isSeperateStart = true;
                }
            }
            if (c == 161) {
                sResult = sResult + '!';
            } else if (c == 191) {
                sResult = sResult + '?';
            } else if ((c <= 197 && c >= 192) || ((c <= 229 && c >= 224) || c == 170 || ((c <= 57349 && c >= 57344) || ((c <= 57364 && c >= 57353) || ((c >= 7840 && c <= 7841) || c == 57517 || c == 193))))) {
                if (!bTTSMode || c > 255 || c == 170) {
                    sResult = sResult + 'a';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 7263 || c == 7264) {
                sResult = sResult + 228;
            } else if (c == 41734 || c == 41754) {
                sResult = sResult + 'b';
            } else if (c == 7853) {
                sResult = sResult + 226;
            } else if (c == 162 || c == 169 || c == 199 || c == 231 || c == 262 || c == 57379 || (c <= 57378 && c >= 57376)) {
                if (bTTSMode || c > 255) {
                    sResult = sResult + 'c';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 208 || c == 240 || c == 57380 || c == 7693 || c == 41747 || c == 41736) {
                if (!bTTSMode || c > 255 || c == 208 || c == 240) {
                    sResult = sResult + 'd';
                } else {
                    sResult = sResult + c;
                }
            } else if ((c <= 203 && c >= 200) || ((c <= 235 && c >= 232) || ((c <= 57385 && c >= 57381) || ((c <= 57390 && c >= 57386) || c == 7864 || c == 7865 || c == 7869 || c == 7877)))) {
                if (!bTTSMode || c > 255) {
                    sResult = sResult + 'e';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 41737) {
                sResult = sResult + 'f';
            } else if (c == 500 || (c <= 57399 && c >= 57397)) {
                sResult = sResult + 'g';
            } else if ((c <= 57401 && c >= 57400) || c == 7716 || c == 7717 || c == 7723) {
                sResult = sResult + 'h';
            } else if ((c <= 207 && c >= 204) || ((c <= 239 && c >= 236) || ((c == 57403 && c <= 57408 && c >= 57404) || ((c <= 57415 && c >= 57409) || c == 7882 || c == 7883 || c == 7725 || c == 7735 || c == 237)))) {
                if (!bTTSMode || c > 255) {
                    sResult = sResult + 'i';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 57416) {
                sResult = sResult + 'j';
            } else if (c == 7728) {
                sResult = sResult + 'k';
            } else if (c == 163 || c == 313 || c == 65505 || c == 57417) {
                if (!bTTSMode || c > 255) {
                    sResult = sResult + 'l';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 57418 || c == 7742 || c == 7743 || c == 7747) {
                sResult = sResult + 'm';
            } else if (c == 209 || c == 241 || c == 323 || c == 57402 || c == 324 || c == 57419 || ((c <= 57421 && c >= 57420) || c == 57447 || c == 7749 || c == 7751 || c == 7753)) {
                if (!bTTSMode || c > 255) {
                    sResult = sResult + 'n';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 186 || ((c <= 214 && c >= 210) || c == 216 || ((c <= 246 && c >= 242) || c == 248 || ((c <= 57427 && c >= 57422) || ((c <= 57435 && c >= 57428) || c == 7884 || c == 7885 || c == 336))))) {
                if (!bTTSMode || c > 255 || c == 186) {
                    sResult = sResult + 'o';
                } else {
                    sResult = sResult + c;
                }
            } else if (c == 7265 || c == 7268) {
                sResult = sResult + 246;
            } else if (c == 7764 || c == 7765) {
                sResult = sResult + 'p';
            } else if (c == 41752) {
                sResult = sResult + 'q';
            } else if (c == 174 || c == 340 || c == 341 || c == 57448 || c == 7770 || c == 7771) {
                if (!bTTSMode || c > 255) {
                    sResult = sResult + 'r';
                } else {
                    sResult = sResult + c;
                }
            } else if (c != '$' && c != 346 && c != 351 && c != 7776 && c != 7778 && c != 7779) {
                if (c == 7788 || c == 7789) {
                    sResult = sResult + 't';
                } else if (c == 41746) {
                    sResult = sResult + 'T';
                } else if ((c <= 220 && c >= 217) || ((c <= 252 && c >= 249) || c == 57463 || c == 57464 || ((c <= 57453 && c >= 57449) || ((c <= 57459 && c >= 57454) || c == 7908 || c == 7909 || c == 7915 || c == 7919)))) {
                    if (!bTTSMode || c > 255) {
                        sResult = sResult + 'u';
                    } else {
                        sResult = sResult + c;
                    }
                } else if (c == 7266 || c == 7267) {
                    sResult = sResult + 252;
                } else if (c == 57460) {
                    sResult = sResult + 'v';
                } else if (c == 7810 || c == 65510 || c == 57462 || c == 7811) {
                    sResult = sResult + 'w';
                } else if (c == 215 || c == 41750) {
                    sResult = sResult + 'x';
                } else if (c == 165 || c == 221 || c == 253 || c == 255 || c == 65509 || c == 7923 || c == 7922 || c == 7925) {
                    if (!bTTSMode || c > 255) {
                        sResult = sResult + 'y';
                    } else {
                        sResult = sResult + c;
                    }
                } else if (c == 377 || c == 7826 || c == 7827 || c == 7828) {
                    sResult = sResult + 'z';
                } else if (c != 183) {
                    sResult = sResult + c;
                }
            }
        }
        return sResult;
    }

    public static String convertTTSWord(String word, boolean isKeyword, int dbType) {
        String convword;
        for (int idx = 0; idx < EXCEPTION_TTSWORD.length; idx++) {
            if (EXCEPTION_TTSWORD[idx].equals(word)) {
                return CHANGE_TTSWORD[idx];
            }
        }
        int codepage = DictUtils.getCodePage(word);
        int nLang = getTTSLanguage(word, dbType);
        EngineManager3rd mEngine = EngineManager3rd.getInstance(null);
        if (DictDBManager.isIdiomDictionary(mEngine.getCurDict()) || DictDBManager.isExampleDictionary(mEngine.getCurDict())) {
            isKeyword = false;
        }
        if (nLang == 65536) {
            convword = GetKeyword(word, false, isKeyword);
        } else {
            convword = word;
        }
        String resultword = convword;
        if (!isKeyword) {
            int lastindex = resultword.length() - 1;
            if (resultword.charAt(lastindex) == '.') {
                resultword = resultword.substring(0, lastindex);
            }
        }
        if (resultword.contains("-")) {
            int charIdx = resultword.indexOf(45) - 1;
            if (isKeyword && charIdx == 0) {
                char onechar = resultword.charAt(charIdx);
                if (isAlphabet(Character.valueOf(onechar))) {
                    resultword = resultword.replace('-', '.');
                }
            } else {
                resultword = resultword.replace('-', ' ');
            }
        }
        String resultword2 = resultword.replaceAll("─", "").replaceAll("－", "").replaceAll("·", "").replaceAll("ː", "").replaceAll("¹", "").replaceAll("³", "").replaceAll("²", "").replaceAll("…", "").replaceAll("ⁱ", "").replaceAll("\u2072", "").replaceAll("\u2073", "").replaceAll("⁴", "").replaceAll("⁵", "").replaceAll("⑴", "").replaceAll("⑵", "").replaceAll("⑶", "").replaceAll("⑷", "").replaceAll("⑸", "").replaceAll("⑹", "");
        if (resultword2.contains("(") && resultword2.contains(")")) {
            if (!isKeyword) {
                resultword2 = resultword2.replace('(', ' ').replace(')', ' ');
            } else {
                int startidx = resultword2.indexOf("(");
                int endidx = resultword2.lastIndexOf(")");
                resultword2 = resultword2.substring(0, startidx) + resultword2.substring(endidx + 1, resultword2.length());
            }
        }
        if (resultword2.contains("「") && resultword2.contains("」")) {
            int startidx2 = resultword2.indexOf("「");
            int endidx2 = resultword2.lastIndexOf("」");
            if (isKeyword) {
                resultword2 = resultword2.substring(0, startidx2);
            } else {
                resultword2 = resultword2.substring(0, startidx2) + resultword2.substring(endidx2 + 1, resultword2.length());
            }
        }
        if (resultword2.contains("（") && resultword2.contains("）")) {
            int startidx3 = resultword2.indexOf("（");
            int endidx3 = resultword2.lastIndexOf("）");
            if (isKeyword) {
                resultword2 = resultword2.substring(0, startidx3);
            } else {
                resultword2 = resultword2.substring(0, startidx3) + resultword2.substring(endidx3 + 1, resultword2.length());
            }
        }
        if (!DictDBManager.getCpCHNDictionary(dbType) || !isKeyword) {
            String resultword3 = makeCorrectTTSNumber(resultword2);
            if (codepage == 0) {
                if (isConsistOfConsonant(resultword3)) {
                    return seperateWord(resultword3).toUpperCase();
                }
                if (resultword3.toUpperCase().equals(resultword3)) {
                    boolean isEndDot = false;
                    if (resultword3.contains(".")) {
                        if (resultword3.lastIndexOf(46) == resultword3.length() - 1) {
                            isEndDot = true;
                        }
                        resultword3 = resultword3.replace('.', ' ');
                        if (isEndDot) {
                            resultword3 = resultword3 + ".";
                        }
                    }
                    return resultword3.toUpperCase();
                } else if (resultword3.toLowerCase().equals("abend") || resultword3.toLowerCase().equals("abba")) {
                    return resultword3.toLowerCase();
                } else {
                    if (resultword3.toLowerCase().equals("tpd")) {
                        return resultword3.toUpperCase();
                    }
                    return resultword3;
                }
            }
            return resultword3;
        }
        return resultword2;
    }

    public static String makeCorrectTTSNumber(String word) {
        int cnt = 0;
        int i = 0;
        while (i < word.length()) {
            if (isNumber(word.charAt(i))) {
                cnt = 1;
                while (true) {
                    i++;
                    if (i < word.length() && isNumber(word.charAt(i))) {
                        cnt++;
                    }
                }
            }
            i++;
        }
        if (cnt == 4 || cnt == 3) {
            int i2 = 0;
            while (i2 < word.length()) {
                if (isNumber(word.charAt(i2)) && cnt - 1 == 2) {
                    if (word.charAt(i2 + 1) != '0' || word.charAt(i2 + 2) != '0') {
                        word = word.replaceFirst(word.charAt(i2) + "", word.charAt(i2) + " ");
                    }
                    i2 = word.length();
                }
                i2++;
            }
        }
        return word;
    }

    public static boolean isSymbol(char c) {
        return c == '(' || c == ')' || c == 8764 || c == '~' || c == ' ' || c == '-' || c == '.' || c == ',' || c == '/';
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isAlphabet(Character ch) {
        return (ch.charValue() >= 'a' && ch.charValue() <= 'z') || (ch.charValue() >= 'A' && ch.charValue() <= 'Z');
    }

    public static String seperateWord(String word) {
        int size = word.length();
        char[] arr = new char[255];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (isAlphabet(Character.valueOf(word.charAt(i)))) {
                arr[j] = word.charAt(i);
                int j2 = j + 1;
                arr[j2] = ',';
                j = j2 + 1;
            }
        }
        return new String(arr, 0, j);
    }

    public static boolean isConsistOfConsonant(String word) {
        int size = VOWEL.length;
        for (int i = 0; i < size; i++) {
            if (word.contains(VOWEL[i])) {
                return false;
            }
        }
        int size2 = word.length();
        for (int i2 = 0; i2 < size2; i2++) {
            if (isNumber(word.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUselessTTSWord(String keyword, int dbType) {
        if (keyword != null && keyword.length() >= 1) {
            char character = keyword.charAt(0);
            if (DictDBManager.getCpCHNDictionary(dbType)) {
                if (DictUtils.getCodePage(character) != 936) {
                    return true;
                }
                if (character >= 6166 && character <= 19967) {
                    return true;
                }
                if (character >= 43003 && character <= 43024) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String checkSpaceInWBName(String name) {
        if (name == null) {
            return null;
        }
        String name2 = name.replace((char) 160, ' ');
        for (char c = 8194; c <= 8203; c = (char) (c + 1)) {
            name2 = name2.replace(c, ' ');
        }
        return name2.trim();
    }

    public static boolean isLowResolutionDevice(Context context) {
        if (context == null) {
            return false;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if ((metrics.heightPixels != 480 || metrics.widthPixels != 320) && ((metrics.heightPixels != 320 || metrics.widthPixels != 480) && ((metrics.heightPixels != 320 && metrics.heightPixels != 400) || metrics.widthPixels != 240))) {
            if (metrics.heightPixels != 240) {
                return false;
            }
            if (metrics.widthPixels != 320 && metrics.widthPixels != 400) {
                return false;
            }
        }
        return true;
    }

    public static boolean isQVGADevice(Context context) {
        if (context == null) {
            return false;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (metrics.heightPixels == 320 && metrics.widthPixels == 240) || (metrics.heightPixels == 240 && metrics.widthPixels == 320);
    }

    public static boolean isWVGADevice(Context context) {
        if (context == null) {
            return false;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (metrics.heightPixels == 480 && metrics.widthPixels == 800) || (metrics.heightPixels == 800 && metrics.widthPixels == 480);
    }

    public static boolean is1280H_xhdpiDevice(Context context) {
        if (context == null) {
            return false;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Configuration config = context.getResources().getConfiguration();
        return (metrics.heightPixels == 1280 && config.orientation == 1) || (metrics.widthPixels == 1280 && config.orientation == 2);
    }

    public static boolean isUseKeypadNoExtractUI() {
        return mUseKeypadNoExtractUI;
    }

    public static void setKeypadNoExtractUI(Context context) {
        if (isWVGADevice(context)) {
            mUseKeypadNoExtractUI = false;
        }
    }

    public static float getDeviceDensity(Context ctx) {
        if (mDensity <= 0.0f) {
            mDensity = ctx.getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }
}
