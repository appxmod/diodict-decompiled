package com.diotek.diodict.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;
import android.widget.Toast;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.mean.CodeBlock;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.utils.CMN;
import com.diodict.decompiled.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class DictUtils {
    static final long DASH = 300;
    private static final String DIODICT_FIRSTLOADING_INFO_PREF = "Variable";
    private static final String DIODICT_FIRSTLOADING_INFO_PREF_VALUE = "IsFirstLoading";
    private static final String DIODICT_SEARCH_INFO_PREF = "pref_search_info";
    private static final String DIODICT_SEARCH_INFO_PREF_CURSOR_SORT = "pref_searchinfo_cursor_sort";
    public static final int DIODICT_SEARCH_INFO_PREF_CURSOR_SORT_DEFAULT_VALUE = 3;
    private static final String DIODICT_SEARCH_INFO_PREF_CURSOR_WORD_POS = "pref_searchinfo_cursor_word_pos";
    public static final int DIODICT_SEARCH_INFO_PREF_CURSOR_WORD_POS_DEFAULT_VALUE = 0;
    public static final int DIODICT_SEARCH_INFO_PREF_LAST_DICT_DEFAULT_VALUE = -1;
    private static final String DIODICT_SEARCH_INFO_PREF_LAST_DICT_VALUE = "pref_searchinfo_dict";
    public static final int DIODICT_SEARCH_INFO_PREF_LAST_POS_DEFAULT_VALUE = 0;
    private static final String DIODICT_SEARCH_INFO_PREF_LAST_POS_VALUE = "pref_searchinfo_pos";
    public static final int DIODICT_SEARCH_INFO_PREF_LAST_TYPE_DEFAULT_VALUE = 1;
    private static final String DIODICT_SEARCH_INFO_PREF_LAST_TYPE_VALUE = "pref_searchinfo_type";
    public static final String DIODICT_SEARCH_INFO_PREF_LAST_WORD_DEFAULT_VALUE = "";
    private static final String DIODICT_SEARCH_INFO_PREF_LAST_WORD_VALUE = "pref_searchinfo_word";
    public static final String DIODICT_SETTING_PREF_CHINESE_INPUT = "PrefJapaneseInput";
    public static final boolean DIODICT_SETTING_PREF_CHINESE_INPUT_DEFAULT_VALUE = false;
    public static final String DIODICT_SETTING_PREF_CHINESE_INPUT_VALUE = "JapaneseInput";
    public static final String DIODICT_SETTING_PREF_DATE_FORMAT = "PrefDataFormat";
    public static final String DIODICT_SETTING_PREF_DATE_FORMAT_VALUE = "DataFormat";
    public static final int DIODICT_SETTING_PREF_DURATION_DATE_FORMAT_VALUE = 1;
    private static final int DIODICT_SETTING_PREF_DURATION_FLASHCARD_SORT_VALUE = 0;
    public static final String DIODICT_SETTING_PREF_DURATION_TIME = "PrefDurationTime";
    public static final int DIODICT_SETTING_PREF_DURATION_TIME_DEFAULT_VALUE = 3000;
    public static final boolean DIODICT_SETTING_PREF_DURATION_TIME_FORMAT_VALUE = false;
    public static final String DIODICT_SETTING_PREF_DURATION_TIME_VALUE = "DurationTime";
    private static final String DIODICT_SETTING_PREF_FEEDBACK_MODE = "PrefFeedbackMode";
    private static final int DIODICT_SETTING_PREF_FEEDBACK_MODE_DEFAULT_VALUE = 0;
    private static final String DIODICT_SETTING_PREF_FEEDBACK_MODE_VALUE = "Feedback";
    private static final String DIODICT_SETTING_PREF_FLASHCARD_SORT = "pref_FlashcardSort";
    public static final String DIODICT_SETTING_PREF_FONT_THEME = "PrefFontTheme";
    public static final int DIODICT_SETTING_PREF_FONT_THEME_DEFAULT_VALUE = 0;
    public static final String DIODICT_SETTING_PREF_FONT_THEME_VALUE = "FontTheme";
    public static final String DIODICT_SETTING_PREF_GESTURE = "PrefGesture";
    public static final boolean DIODICT_SETTING_PREF_GESTURE_DEFAULT_VALUE = false;
    public static final String DIODICT_SETTING_PREF_GESTURE_VALUE = "PrefGestureUsed";
    public static final String DIODICT_SETTING_PREF_JAPANESE_INPUT = "PrefJapaneseInput";
    public static final boolean DIODICT_SETTING_PREF_JAPANESE_INPUT_DEFAULT_VALUE = false;
    public static final String DIODICT_SETTING_PREF_JAPANESE_INPUT_VALUE = "JapaneseInput";
    private static final String DIODICT_SETTING_PREF_LAST_DICT = "PrefLastDict";
    private static final int DIODICT_SETTING_PREF_LAST_DICT_DEFAULT_VALUE = -1;
    private static final String DIODICT_SETTING_PREF_LAST_DICT_VALUE = "LastDict";
    public static final String DIODICT_SETTING_PREF_MARKER_PEN_COLOR = "PrefMarkerPenColor";
    public static final int DIODICT_SETTING_PREF_MARKER_PEN_COLOR_DEFAULT_VALUE = 0;
    public static final String DIODICT_SETTING_PREF_MARKER_PEN_COLOR_VALUE = "MarkerColor";
    public static final String DIODICT_SETTING_PREF_MEAN_FONT_SIZE = "PrefMeanFontSize";
    public static final int DIODICT_SETTING_PREF_MEAN_FONT_SIZE_DEFAULT_VALUE_LDPI = 2;
    public static final String DIODICT_SETTING_PREF_MEAN_FONT_SIZE_VALUE = "MeanFontSize";
    public static final String DIODICT_SETTING_PREF_RECOG_TIME = "PrefRecognizeTime";
    public static final int DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE = 300;
    public static final String DIODICT_SETTING_PREF_RECOG_TIME_VALUE = "RecognizeTime";
    private static final String DIODICT_SETTING_PREF_REFRESH_VIEW = "PrefRefreshView";
    private static final String DIODICT_SETTING_PREF_REFRESH_VIEW_VALUE = "RefreshViewState";
    private static final String DIODICT_SETTING_PREF_START_DICT = "PrefStartDict";
    private static final String DIODICT_SETTING_PREF_START_DICT_VALUE = "StartDict";
    public static final String DIODICT_SETTING_PREF_TIME_FORMAT = "PrefTimeFormat";
    public static final String DIODICT_SETTING_PREF_TIME_FORMAT_VALUE = "TimeFormat";
    public static final String DIODICT_SETTING_PREF_UPDATE = "PrefUpdate";
    public static final int DIODICT_SETTING_PREF_UPDATE_DEFAULT_VALUE = 3;
    public static final String DIODICT_SETTING_PREF_UPDATE_VALUE = "PrefUpdateUsed";
    static final long DOT = 100;
    public static final int EFFECT_NONE = 2;
    public static final int EFFECT_SOUND = 0;
    public static final int EFFECT_VIBE = 1;
    static final long GAP = 100;
    static final long LETTER_GAP = 300;
    public static final int SOUND_CORRECT = 0;
    public static final int SOUND_EXCELLENT = 2;
    public static final int SOUND_GOOD = 3;
    public static final int SOUND_OOPS = 4;
    public static final int SOUND_QUESTION = 5;
    public static final int SOUND_WRONG = 1;
    private static final long SPEED_BASE = 100;
    static final long WORD_GAP = 700;
    private static final String[] EXCEPTION_CORRECTWORD = {"2,4,5-T", "2, 4, 5-T", "2,4-D", "2, 4-D"};
    public static Typeface mDioDictFnt3 = null;
    public static int DIODICT_SETTING_PREF_MEAN_FONT_SIZE_DEFAULT_VALUE = 0;
    public static SoundPool mSoundPool = null;
    public static AudioManager mAudioManager = null;
    public static HashMap<Integer, Integer> mSoundPoolMap = null;
    private static final long WHITE_GAP = 0;
    public static final long[][] sVibratePattern = {new long[]{WHITE_GAP, 100}, new long[]{WHITE_GAP, 100, 100, 100, 100, 100}, new long[]{WHITE_GAP, 100, 100, 300, 100, 300, 100, 100, 300, 100}, new long[]{WHITE_GAP, 100, 100, 100, 100, 100, 100, 100, 100, 100}, new long[]{WHITE_GAP, 300, 100, 100}, new long[]{WHITE_GAP, 100, 100, 100, 100, 100, 100, 100, 100, 100}};

    public static boolean checkInstallDB(int dicType) {
        Integer[] dictList = DictDBManager.getDictList();
        for (Integer num : dictList) {
            if (num.intValue() == dicType) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkInstallTTS(int nLang) {
        for (int i = 0; i < DictInfo.TTSList.length; i++) {
            if (DictInfo.TTSList[i] == nLang) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkExistTTSFile(int nLang) {
        if (Dependency.getDeviceName().equals("com.diotek.diodict.dependency.device.LgtH13")) {
            return 65539 != nLang;
        }
        String[] fileList = null;
        switch (nLang) {
            case 65536:
                if (Dependency.getDevice().getPrefixEngUSTTS().equals(TTSEngine.getPrefixSVOX())) {
                    fileList = DictInfo.SvoxEngTTSFileList;
                    break;
                } else {
                    fileList = DictInfo.HCIEngTTSFileList;
                    break;
                }
            case EngineInfo3rd.TTS_CHINESE /* 65537 */:
                if (Dependency.getDevice().getPrefixChnTTS().equals(TTSEngine.getPrefixSVOX())) {
                    fileList = DictInfo.SvoxChnTTSFileList;
                    break;
                } else {
                    fileList = DictInfo.HCIChnTTSFileList;
                    break;
                }
            case EngineInfo3rd.TTS_JAPANESE /* 65538 */:
                if (Dependency.getDevice().getPrefixJpnTTS().equals(TTSEngine.getPrefixSVOX())) {
                    fileList = DictInfo.SvoxJpnTTSFileList;
                    break;
                } else {
                    fileList = DictInfo.HCIJpnTTSFileList;
                    break;
                }
            case EngineInfo3rd.TTS_KOREAN /* 65539 */:
                if (Dependency.getDevice().getPrefixKorTTS().equals(TTSEngine.getPrefixSVOX())) {
                    fileList = DictInfo.SvoxKorTTSFileList;
                    break;
                } else {
                    fileList = DictInfo.HCIKorTTSFileList;
                    break;
                }
        }
        if (fileList == null) {
            return false;
        }
        for (int i = 0; i < fileList.length; i++) {
            String szIdiomDBName = DictInfo.TTSPATH + fileList[i];
            if (!checkExistFile(szIdiomDBName)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkExistSecoundTTSFile(int nLang) {
        String[] fileList = null;
        switch (nLang) {
            case 65536:
                fileList = DictInfo.SvoxEngUKTTSFileList;
                break;
            case EngineInfo3rd.TTS_CHINESE /* 65537 */:
                fileList = DictInfo.SvoxChnYueTTSFileList;
                break;
        }
        if (fileList == null) {
            return false;
        }
        for (int i = 0; i < fileList.length; i++) {
            String szIdiomDBName = DictInfo.TTSPATH + fileList[i];
            if (!checkExistFile(szIdiomDBName)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkExistFile(String filePath) {
        File f = new File(filePath);
        return f.exists();
    }

    public static void setDBPath(String path) {
        DictInfo.DIODICT_EXTERNAL_PATH = path;
        String appName = Dependency.getDBpathName();
        if (appName.equals("")) {
            appName = DictInfo.DIODICT_EXTERNAL_APPPATH;
        }
        DictInfo.EXTERNALPATH = DictInfo.DIODICT_EXTERNAL_PATH;
        DictInfo.DBPATH = DictInfo.EXTERNALPATH + appName;
        DictInfo.FONTPATH = DictInfo.EXTERNALPATH + appName;
        DictInfo.TTSPATH = DictInfo.EXTERNALPATH + appName + DictInfo.TTS_CONFIG_PATH + "/";
        DictInfo.NPSEPATH = DictInfo.EXTERNALPATH + appName + "npse/";
        MSG.l(0, "DIODICT3_for_Android (DicUtil.setDBPath() ) DBPATH :" + DictInfo.DBPATH);
    }

    public static String getCurrentDBFontName(int dicType) {
        return DictInfo.DIODICT_FONT_NAME;
    }

    public static void setDBPath(Context context, boolean bChangeExternalSD) {
        DictInfo.DIODICT_FONT_NAME = Dependency.getDevice().getFontName();
        if (Dependency.getDevice().isDBinPackage()) {
            DictInfo.DBPATH = "/data/data/" + context.getPackageName() + "/files/";
            DictInfo.FONTPATH = "/data/data/" + context.getPackageName() + "/files/";
            DictInfo.TTSPATH = "/data/data/" + context.getPackageName() + "/files/";
        } else if (Dependency.getDBpath().equals("")) {
            DictInfo.DIODICT_EXTERNAL_PATH = "/sdcard/";
            String appName = Dependency.getDBpathName();
            if (appName.equals("")) {
                appName = DictInfo.DIODICT_EXTERNAL_APPPATH;
            }
            DictInfo.EXTERNALPATH = DictInfo.DIODICT_EXTERNAL_PATH;
            DictInfo.DBPATH = DictInfo.EXTERNALPATH + appName;
            DictInfo.FONTPATH = DictInfo.EXTERNALPATH + appName;
            DictInfo.TTSPATH = DictInfo.EXTERNALPATH + appName + DictInfo.TTS_CONFIG_PATH + "/";
            DictInfo.NPSEPATH = DictInfo.EXTERNALPATH + appName + "npse/";
        } else {
            DictInfo.EXTERNALPATH = Dependency.getDBpath();
            String appName2 = Dependency.getDBpathName();
            if (appName2.equals("")) {
                appName2 = DictInfo.DIODICT_EXTERNAL_APPPATH;
            }
            DictInfo.DBPATH = DictInfo.EXTERNALPATH + appName2;
            DictInfo.FONTPATH = DictInfo.EXTERNALPATH + appName2;
            DictInfo.TTSPATH = DictInfo.EXTERNALPATH + appName2 + DictInfo.TTS_CONFIG_PATH + "/";
            DictInfo.NPSEPATH = DictInfo.EXTERNALPATH + appName2 + "npse/";
        }
        MSG.l(0, "DIODICT3_for_Android (DicUtil.setDBPath() ) DBPATH :" + DictInfo.DBPATH);
    }

    public static String getExternalDBPath() {
        return DictInfo.DIODICT_EXTERNAL_DBPATH;
    }

    public static String getExternalPath() {
        return DictInfo.EXTERNALPATH;
    }

    public static String getDBPath() {
        return DictInfo.DBPATH;
    }

    public static String getTTSPath() {
        return DictInfo.TTSPATH;
    }

    public static String getFontFullPath() {
        return DictInfo.FONTPATH + DictInfo.DIODICT_FONT_NAME;
    }

    public static void installLib(Context context) {
    }

    public static void copyDB(Context context, int nResourceId, String szDBName) throws IOException {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(nResourceId);
        InputStream file = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            file = context.openFileInput(szDBName);
        } catch (FileNotFoundException e) {
            if (afd != null) {
                try {
                    try {
                        fis = afd.createInputStream();
                        byte[] tempdata = new byte[1000];
                        fos = context.openFileOutput(szDBName, 0);
                        for (int size = fis.available(); size > 0; size -= 1000) {
                            if (size < 1000) {
                                byte[] tempdata2 = new byte[size];
                                fis.read(tempdata2);
                                fos.write(tempdata2);
                                continue;
                            } else {
                                fis.read(tempdata);
                                fos.write(tempdata);
                                continue;
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (fis != null) {
                            fis.close();
                        }
                    }
                } catch (Throwable th) {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                            throw th;
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            }
        }
        if (file != null) {
            try {
                file.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        if (afd != null) {
            try {
                afd.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        }
    }

    public static String convertByteToString(byte[] wordbyte, int nDicType, boolean convertSymbol) {
        String charset;
        int encode = DictDBManager.getDictEncode(nDicType);
        switch (encode) {
            case 0:
                charset = new String("UTF-16LE");
                break;
            case 4:
                charset = new String("KSC5601");
                break;
            default:
                charset = new String("UTF-16LE");
                break;
        }
        String convered = convertByteToString(wordbyte, charset, convertSymbol);
        return convered;
    }

    public static String convertByteToString(byte[] wordbyte, String charset, boolean convertSymbol) {
        String convered = null;
        if (charset.equals("UTF-16LE")) {
            try {
                int size = getWordByteLength(wordbyte);
                convered = new String(getRealWordByte(size, wordbyte), charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (charset.equals("KSC5601")) {
            try {
                convered = new String(wordbyte, charset);
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        if (convertSymbol) {
            return getSuffixString(convered);
        }
        return convered;
    }

    public static byte[] getRealWordByte(int size, byte[] wordbyte) {
        byte[] convered = new byte[size];
        for (int i = 0; i < size; i++) {
            convered[i] = wordbyte[i];
        }
        return convered;
    }

    public static int getWordByteLength(byte[] wordbyte) {
        int total = wordbyte.length;
        int size = 0;
        if (total > 1) {
            for (int i = 0; i < total; i += 2) {
                if (wordbyte[i] != 0 || wordbyte[i + 1] != 0) {
                    size += 2;
                } else {
                    return size;
                }
            }
            return size;
        }
        return total;
    }

    public static String getSuffixString(String word) {
        String suffixword;
        if (word == null) {
            return null;
        }
        int size = word.length();
        boolean isFound = false;
        if (word.contains("%U")) {
            if (size < 4) {
                return null;
            }
            char[] wordcharbuf = new char[size - 3];
            int i = 0;
            int j = 0;
            while (i < size) {
                if (word.charAt(i) == '%' && word.charAt(i + 1) == 'U') {
                    isFound = true;
                    int start = word.indexOf("%U", i) + 2;
                    int end = word.indexOf("?", i);
                    short code = getSymboleCode(word, start, end);
                    wordcharbuf[j] = (char) code;
                    j++;
                    i += (end - start) + 1;
                } else if (!isFound || word.charAt(i) != '?') {
                    wordcharbuf[j] = word.charAt(i);
                    j++;
                }
                i++;
            }
            suffixword = new String(wordcharbuf, 0, size - 3);
        } else {
            suffixword = word.substring(0, size);
        }
        return suffixword;
    }

    private static short getSymboleCode(String word, int start, int end) {
        String symbol = word.substring(start, end);
        short temp = (short) Integer.parseInt(symbol);
        short code = (short) EngineNative3rd.LibGetUnicodeFromDioSymbol(temp);
        return code;
    }

    public static byte[] makeSearchWord(byte[] searchbyte, int encode) {
        byte[] resultbyte;
        if (searchbyte == null) {
            return null;
        }
        int searchwordsize = searchbyte.length;
        switch (encode) {
            case 0:
                resultbyte = new byte[searchwordsize + 2];
                break;
            case 4:
                resultbyte = new byte[searchwordsize + 1];
                break;
            default:
                return null;
        }
        for (int i = 0; i < searchwordsize; i++) {
            resultbyte[i] = searchbyte[i];
        }
        return resultbyte;
    }

    public static void logMessage(String str) {
        MSG.l(1, "NativeLog:" + str);
    }

    public static Typeface createfont() {
        if (mDioDictFnt3 == null) {
            String filePath = DictInfo.FONTPATH + DictInfo.DIODICT_FONT_NAME;
            File file = new File(filePath);
            if (file.exists()) {
                mDioDictFnt3 = Typeface.createFromFile(filePath);
            }
        }
        return mDioDictFnt3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:132:0x0237, code lost:
        r1 = com.diotek.diodict.engine.DictInfo.CP_JPN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int getCodePage(java.lang.String word) {
		if (patternKor.matcher(word).find()) {
			return DictInfo.CP_KOR;
		}
		Matcher m = patternHan.matcher(word);
		if (m.find()) {
			int st = m.start();
			if (patternEn.matcher(word.substring(0, st)).find()) {
				return DictInfo.CP_LT1;
			}
			return DictInfo.CP_CHN;
		}
		return DictInfo.CP_LT1;
	}
	
	static Pattern patternKor;
	static Pattern patternHan;
	final static Pattern patternEn = Pattern.compile("[a-zA-Z]");
	
	static {
		try {
			patternHan = Pattern.compile("\\p{IsHan}");
			patternKor = Pattern.compile("\\p{IsHangul}");
		} catch (Exception e) {
			// CMN.debug(e);
			patternHan = Pattern.compile("[\\u4e00-\\u9fa5]");
			patternKor = Pattern.compile("[\\u3130-\\u318F\\uAC00-\\uD7A3]");
		}
	}
	
	public static boolean isLatinCP(int codepage) {
		if (!isLatin1CP(codepage) && codepage != 1257 && codepage != 1254) {
			return false;
		}
		return true;
	}
	
	public static boolean isLatin1CP(int codepage) {
		if (codepage != 1252 && codepage != 1250) {
			return false;
		}
		return true;
	}
	
	public static boolean isZhuyinCharacter(String word) {
		if (word == null || 1 > word.length()) {
			return false;
		}
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (12544 <= c && 12592 >= c) {
				return true;
			}
			if (12687 <= c && 12591 >= c) {
				return true;
			}
		}
		return false;
	}
	
	
	public static int getCodePage(char ch) {
		CMN.debug("getCodePage!!!"+ch);
        if (ch == '[' || ch == '(' || ch == 173 || ch == 175 || ch == 183 || ch == 168 || ch == 183 || ch == 176 || ch == 180 || ch == 184) {
            return -1;
        }
        if (ch >= 192 && ch <= 255) {
            return 0;
        }
        if (ch >= 338 && ch <= 339) {
            return 0;
        }
        if ((ch >= 381 && ch <= 382) || ch == 376 || ch == 402 || ch == 710 || ch == 732 || ch == 603) {
            return 0;
        }
        if ((ch >= 7680 && ch <= 7935) || ch == 8491 || ch == 257 || ch == 299 || ch == 338 || ch == 339 || ch == 363) {
            return 0;
        }
        if (ch >= 352 && ch <= 353) {
            return 0;
        }
        if (ch >= 8211 && ch <= 8212) {
            return 0;
        }
        if (ch >= 8216 && ch <= 8218) {
            return 0;
        }
        if (ch >= 8220 && ch <= 8222) {
            return 0;
        }
        if ((ch >= 8224 && ch <= 8226) || ch == 8230 || ch == 8240) {
            return 0;
        }
        if ((ch >= 8249 && ch <= 8250) || ch == 8364 || ch == 8482) {
            return 0;
        }
        if (ch >= 256 && ch <= 577) {
            return 0;
        }
        if ((ch >= 4352 && ch <= 4607) || ((ch >= 12592 && ch <= 12687) || (ch >= 44032 && ch <= 55215))) {
            return DictInfo.CP_KOR;
        }
        if ((ch >= 11904 && ch <= 12031) || ((ch >= 12544 && ch <= 12591) || ((ch >= 12704 && ch <= 12735) || ((ch >= 13312 && ch <= 19903) || ((ch >= 19968 && ch <= 40879) || ((ch >= 63744 && ch <= 64050) || ch == 64132 || ch == 64133 || ch == 64147 || ch == 64148 || ch == 64150 || ((ch >= 64177 && ch <= 64255) || ((ch >= 63639 && ch <= 63686) || ((ch >= 63688 && ch <= 63725) || ((ch >= 63728 && ch <= 63729) || ((ch >= 63732 && ch <= 63743) || ((ch >= 6144 && ch <= 7254) || ((ch >= 57554 && ch <= 57555) || ch == 63537 || ch == 9758))))))))))))) {
            return DictInfo.CP_CHN;
        }
        if ((ch >= 12352 && ch <= 12447) || (ch >= 12448 && ch <= 12543)) {
            return DictInfo.CP_JPN;
        }
        if (ch == 1025 || ch == 1028 || ((ch >= 1030 && ch <= 1031) || ((ch >= 1040 && ch <= 1103) || ch == 1105 || ch == 1108 || ((ch >= 1110 && ch <= 1111) || ((ch >= 1168 && ch <= 1169) || ch == 8729 || ch == 8730 || ch == 8776 || ((ch >= 8804 && ch <= 8805) || ((ch >= 8992 && ch <= 8225) || ch == 9472 || ch == 9474 || ch == 9484 || ch == 9488 || ch == 9492 || ch == 9496 || ch == 9500 || ch == 9508 || ch == 9516 || ch == 9516 || ch == 9524 || ch == 9532 || ((ch >= 9552 && ch <= 9554) || ch == 9556 || ((ch >= 9559 && ch <= 9563) || ((ch >= 9565 && ch <= 9569) || ch == 9571 || ((ch >= 9574 && ch <= 9578) || ch == 9580 || ch == 9600 || ch == 9604 || ch == 9608 || ch == 9612 || ((ch >= 9616 && ch <= 9619) || ch == 9632)))))))))))) {
            return DictInfo.CP_CRL;
        }
        if (ch >= 'A' && ch <= 'Z') {
            return 0;
        }
        if (ch >= 'a' && ch <= 'z') {
            return 0;
        }
        if (ch >= 192 && ch <= 246) {
            return 0;
        }
        if (ch >= 248 && ch <= 255) {
            return 0;
        }
        if ((ch >= '0' && ch <= '9') || ch == 41746) {
            return 0;
        }
        return (ch < '!' || ch > '~') ? -1 : 255;
    }

    public static boolean ISLATIN_CP(int codepage) {
        if (!ISLATIN1_CP(codepage) && codepage != 1257 && codepage != 1254) {
            return false;
        }
        return true;
    }

    public static boolean ISLATIN1_CP(int codepage) {
        if (codepage != 1252 && codepage != 1250) {
            return false;
        }
        return true;
    }

    public static boolean isDioSymbolAlphabet(char charAt) {
        char[] symbolCode = {7424, 665, 7428, 7429, 7431, 57534, 610, 668, 618, 7434, 7435, 671, 7437, 628, 7439, 7448, 57535, 640, 57536, 7451, 7452, 7456, 7457, 57537, 655, 7458};
        for (char c : symbolCode) {
            if (charAt == c) {
                return true;
            }
        }
        return false;
    }

    public static String convertDioSymbolAlphabetString(String word) {
        char[] convertedCharArray = new char[word.length()];
        char[] symbolCode = {7424, 665, 7428, 7429, 7431, 57534, 610, 668, 618, 7434, 7435, 671, 7437, 628, 7439, 7448, 57535, 640, 57536, 7451, 7452, 7456, 7457, 57537, 655, 7458};
        for (int i = 0; i < word.length(); i++) {
            int j = 0;
            while (true) {
                if (j < symbolCode.length) {
                    if (word.charAt(i) != symbolCode[j]) {
                        j++;
                    } else {
                        convertedCharArray[i] = (char) (j + 65);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (convertedCharArray[i] == 0) {
                convertedCharArray[i] = word.charAt(i);
            }
        }
        return new String(convertedCharArray);
    }

    public static boolean isWildcardSearch(String searchWord) {
        int size = searchWord.length();
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (searchWord.charAt(i) == '*' || searchWord.charAt(i) == '?') {
                return true;
            }
        }
        return false;
    }

    public static boolean isWildcardCharacter(char c) {
        return c == '*' || c == '?';
    }

    public static boolean isValidWildCondition(String searchWord) {
        boolean z = true;
        if (searchWord == null) {
            return false;
        }
        int nPositiveKeyPos = -1;
        int size = searchWord.length();
        if (size == 0) {
            return false;
        }
        int i = 0;
        while (i < size) {
            if (searchWord.charAt(i) == '*' || searchWord.charAt(i) == '?') {
                if (nPositiveKeyPos > -1) {
                    return false;
                }
                nPositiveKeyPos = i;
                while (true) {
                    i++;
                    if (i == size) {
                        return true;
                    }
                    if (searchWord.charAt(i) == '*' || searchWord.charAt(i) == '?') {
                    }
                }
            }
            i++;
        }
        if (nPositiveKeyPos <= -1) {
            z = false;
        }
        return z;
    }

    public static String[] makeNearestList(Context context) {
        EngineManager3rd engine = EngineManager3rd.getInstance(context);
        String[] list = new String[4];
        int pos = getNearestWordFirstPos(engine.getResultListCount(1));
        for (int i = 0; i < 4; i++) {
            list[i] = engine.getResultListKeywordByPos(pos + i, 1);
        }
        return list;
    }

    public static Integer[] makeHyperDicList(String word, Context context) {
        int madeSize;
        if (word == null) {
            return null;
        }
        Vector<Integer> hyperDictList = null;
        int codepage = getCodePage(word);
        if (codepage == -1) {
            return null;
        }
        switch (codepage) {
            case 0:
            case DictInfo.CP_1250 /* 1250 */:
            case DictInfo.CP_LT1 /* 1252 */:
            case DictInfo.CP_TUR /* 1254 */:
            case DictInfo.CP_BAL /* 1257 */:
            case DictInfo.CP_CRL /* 21866 */:
                hyperDictList = getHyperTextSupportDictionaryOfEnglish(context);
                break;
            case DictInfo.CP_JPN /* 932 */:
                hyperDictList = getHyperTextSupportDictionaryOfJapanese(context);
                break;
            case DictInfo.CP_CHN /* 936 */:
                hyperDictList = getHyperTextSupportDictionaryOfChinese(context);
                break;
            case DictInfo.CP_KOR /* 949 */:
                hyperDictList = getHyperTextSupportDictionaryOfKorean(context);
                break;
        }
        if (hyperDictList == null || (madeSize = hyperDictList.size()) == 0) {
            return null;
        }
        Integer[] retHyperDictList = new Integer[madeSize];
        hyperDictList.toArray(retHyperDictList);
        hyperDictList.clear();
        return retHyperDictList;
    }

    public static Vector<Integer> getHyperTextSupportDictionaryOfEnglish(Context context) {
        Integer[] dictList = EngineManager3rd.getInstance(context).getSupportDictionary();
        int dictListSize = dictList.length;
        Vector<Integer> hyperDictList = new Vector<>();
        for (int i = 0; i < dictListSize; i++) {
            if (DictDBManager.getDictHyperTextLang(dictList[i].intValue()) == 0) {
                hyperDictList.add(dictList[i]);
            }
        }
        return hyperDictList;
    }

    public static Vector<Integer> getHyperTextSupportDictionaryOfKorean(Context context) {
        Integer[] dictList = EngineManager3rd.getInstance(context).getSupportDictionary();
        int dictListSize = dictList.length;
        Vector<Integer> hyperDictList = new Vector<>();
        for (int i = 0; i < dictListSize; i++) {
            if (DictDBManager.getDictHyperTextLang(dictList[i].intValue()) == 1) {
                hyperDictList.add(dictList[i]);
            }
        }
        return hyperDictList;
    }

    public static Vector<Integer> getHyperTextSupportDictionaryOfChinese(Context context) {
        Integer[] dictList = EngineManager3rd.getInstance(context).getSupportDictionary();
        int dictListSize = dictList.length;
        Vector<Integer> hyperDictList = new Vector<>();
        for (int i = 0; i < dictListSize; i++) {
            if (DictDBManager.getDictHyperTextLang(dictList[i].intValue()) == 2) {
                hyperDictList.add(dictList[i]);
            }
            int nKanjiDicType = DictDBManager.getKanjiDicTypeByCurDicType(dictList[i].intValue());
            if (nKanjiDicType != dictList[i].intValue() && nKanjiDicType != 65282) {
                hyperDictList.add(Integer.valueOf(nKanjiDicType));
            }
        }
        return hyperDictList;
    }

    public static Vector<Integer> getHyperTextSupportDictionaryOfJapanese(Context context) {
        Integer[] dictList = EngineManager3rd.getInstance(context).getSupportDictionary();
        int dictListSize = dictList.length;
        Vector<Integer> hyperDictList = new Vector<>();
        for (int i = 0; i < dictListSize; i++) {
            if (DictDBManager.getDictHyperTextLang(dictList[i].intValue()) == 3 && DictDBManager.getDictIndependence(dictList[i].intValue()) != 3) {
                hyperDictList.add(dictList[i]);
            }
        }
        return hyperDictList;
    }

    public static int getNearestWordFirstPos(int keyPos) {
        return (keyPos / 2) - 2;
    }

    public static String getDateString(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = dateFormat.format(Long.valueOf(time));
        return dateString;
    }

    public static boolean isEnglish(String word) {
        int size = word.length();
        for (int i = 0; i < size; i++) {
            if (('a' <= word.charAt(i) && word.charAt(i) <= 'z') || (('A' <= word.charAt(i) && word.charAt(i) <= 'Z') || ((192 <= word.charAt(i) && word.charAt(i) <= 246) || (248 <= word.charAt(i) && word.charAt(i) <= 255)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLatin(String word) {
        int size = word.length();
        for (int i = 0; i < size; i++) {
            if (('a' <= word.charAt(i) && word.charAt(i) <= 'z') || ('A' <= word.charAt(i) && word.charAt(i) <= 'Z')) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstNumber(String word) {
        int size = word.length();
        return size > 0 && word.charAt(0) >= '0' && word.charAt(0) <= '9';
    }

    public static void replaceText(EditText input, CharSequence text, int newCursorPosition, boolean composing) {
        int newCursorPosition2;
        Editable content = input.getText();
        if (content != null) {
            input.beginBatchEdit();
            int a = getComposingSpanStart(input);
            int b = getComposingSpanEnd(input);
            if (b < a) {
                a = b;
                b = a;
            }
            if (a == -1 || b == -1) {
                a = Selection.getSelectionStart(content);
                b = Selection.getSelectionEnd(content);
                if (a < 0) {
                    a = 0;
                }
                if (b < 0) {
                    b = 0;
                }
                if (b < a) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
            }
            if (newCursorPosition > 0) {
                newCursorPosition2 = newCursorPosition + (b - 1);
            } else {
                newCursorPosition2 = newCursorPosition + a;
            }
            if (newCursorPosition2 < 0) {
                newCursorPosition2 = 0;
            }
            if (newCursorPosition2 > content.length()) {
                newCursorPosition2 = content.length();
            }
            Selection.setSelection(content, newCursorPosition2);
            content.replace(a, b, text);
            int end = input.getSelectionEnd();
            if (composing) {
                Selection.setSelection(content, a, end);
            }
            input.endBatchEdit();
        }
    }

    private static int getComposingSpanStart(EditText input) {
        return input.getSelectionStart();
    }

    private static int getComposingSpanEnd(EditText input) {
        return input.getSelectionEnd();
    }

    public static String makeCorrectWord(String keyword) {
        char[] charKeyword = new char[keyword.length()];
        keyword.getChars(0, keyword.length(), charKeyword, 0);
        int txtLen = charKeyword.length;
        char[] byteAnswerKeyword = new char[txtLen];
        int j = 0;
        boolean bCommaChk = true;
        boolean bParenthesis = false;
        char pairSymbol = 0;
        int idx = 0;
        while (true) {
            if (idx < EXCEPTION_CORRECTWORD.length) {
                if (!EXCEPTION_CORRECTWORD[idx].equals(keyword)) {
                    idx++;
                } else {
                    bCommaChk = false;
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < txtLen && charKeyword[i] != '%'; i++) {
            if (!bParenthesis) {
                if (isSubscript(charKeyword[i])) {
                    continue;
                } else if (isParenthesis(charKeyword[i])) {
                    bParenthesis = true;
                    pairSymbol = getPairParenthesis(charKeyword[i]);
                } else if (bCommaChk && charKeyword[i] == ',') {
                    break;
                } else {
                    byteAnswerKeyword[j] = charKeyword[i];
                    j++;
                }
            } else if (charKeyword[i] == pairSymbol) {
                bParenthesis = false;
            }
        }
        String strAnswerKeyword = new String(byteAnswerKeyword);
        String strAnswerKeyword1 = strAnswerKeyword.substring(0, j);
        return strAnswerKeyword1;
    }

    static boolean isSubscript(char c) {
        return (c >= 8304 && c <= 8313) || c == 185 || c == 178 || c == 179 || (c >= 9332 && c <= 9337);
    }

    static char getPairParenthesis(char c) {
        switch (c) {
            case '(':
                return ')';
            case '[':
                return ']';
            case 12300:
                return (char) 12301;
            case 65288:
                return (char) 65289;
            case 65339:
                return (char) 65341;
            default:
                return (char) 0;
        }
    }

    static boolean isParenthesis(char c) {
        switch (c) {
            case '(':
            case '[':
            case 12300:
            case 65288:
            case 65339:
                return true;
            default:
                return false;
        }
    }

    public static boolean isWordSeperator(char charAt) {
        return CodeBlock.isAlpabetCodeBlock(charAt) || CodeBlock.isHangulCodeBlock(charAt) || CodeBlock.isChineseCodeBlock(charAt) || CodeBlock.isLatin(charAt) || CodeBlock.isJapan(charAt) || CodeBlock.isNumber(charAt);
    }

    public static String getMakeSearchKeyword(String szKeyword) {
        StringBuilder szReturnKeyword = new StringBuilder();
        for (int i = 0; i < szKeyword.length(); i++) {
            szReturnKeyword.append(getKeyword(szKeyword.charAt(i)));
        }
        return szReturnKeyword.toString();
    }

    public static char getKeyword(char cChar) {
        if (cChar == 161) {
            return '!';
        }
        if (cChar == 176 || cChar == 8304) {
            return 'O';
        }
        if (cChar == 185 || cChar == 8305) {
            return '1';
        }
        if (cChar == 178 || cChar == 8306) {
            return '2';
        }
        if (cChar == 179 || cChar == 8307) {
            return '3';
        }
        if (cChar == 191) {
            return '?';
        }
        if ((cChar <= 197 && cChar >= 192) || ((cChar <= 229 && cChar >= 224) || cChar == 170 || ((cChar <= 57349 && cChar >= 57344) || ((cChar <= 57364 && cChar >= 57353) || ((cChar >= 7840 && cChar <= 7841) || cChar == 57517))))) {
            return 'a';
        }
        if (cChar == 7263 || cChar == 7264) {
            return (char) 228;
        }
        if (cChar == 7853) {
            return (char) 226;
        }
        if (cChar == 162 || cChar == 169 || cChar == 199 || cChar == 231 || cChar == 262 || cChar == 57379 || (cChar <= 57378 && cChar >= 57376)) {
            return 'c';
        }
        if (cChar == 208 || cChar == 240 || cChar == 57380 || cChar == 7693) {
            return 'd';
        }
        if ((cChar <= 203 && cChar >= 200) || ((cChar <= 235 && cChar >= 232) || ((cChar <= 57385 && cChar >= 57381) || ((cChar <= 57390 && cChar >= 57386) || cChar == 7864 || cChar == 7865 || cChar == 7869 || cChar == 7877)))) {
            return 'e';
        }
        if (cChar == 500 || (cChar <= 57399 && cChar >= 57397)) {
            return 'g';
        }
        if ((cChar <= 57401 && cChar >= 57400) || cChar == 7716 || cChar == 7717 || cChar == 7723) {
            return 'h';
        }
        if ((cChar <= 207 && cChar >= 204) || ((cChar <= 239 && cChar >= 236) || ((cChar == 57403 && cChar <= 57408 && cChar >= 57404) || ((cChar <= 57415 && cChar >= 57409) || cChar == 7882 || cChar == 7883 || cChar == 7725 || cChar == 7735)))) {
            return 'i';
        }
        if (cChar == 57416) {
            return 'j';
        }
        if (cChar == 7728) {
            return 'k';
        }
        if (cChar == 163 || cChar == 313 || cChar == 65505 || cChar == 57417) {
            return 'l';
        }
        if (cChar == 57418 || cChar == 7742 || cChar == 7743 || cChar == 7747) {
            return 'm';
        }
        if (cChar == 209 || cChar == 241 || cChar == 323 || cChar == 57402 || cChar == 324 || cChar == 57419 || ((cChar <= 57421 && cChar >= 57420) || cChar == 57447 || cChar == 7749 || cChar == 7751 || cChar == 7753)) {
            return 'n';
        }
        if (cChar == 186 || ((cChar <= 214 && cChar >= 210) || cChar == 216 || ((cChar <= 246 && cChar >= 242) || cChar == 248 || ((cChar <= 57427 && cChar >= 57422) || ((cChar <= 57435 && cChar >= 57428) || cChar == 7884 || cChar == 7885 || cChar == 336))))) {
            return 'o';
        }
        if (cChar == 7265 || cChar == 7268) {
            return (char) 246;
        }
        if (cChar == 7764 || cChar == 7765) {
            return 'p';
        }
        if (cChar == 174 || cChar == 340 || cChar == 341 || cChar == 57448 || cChar == 7770 || cChar == 7771) {
            return 'r';
        }
        if (cChar == '$' || cChar == 346 || cChar == 351 || cChar == 7776 || cChar == 7778 || cChar == 7779) {
            return 's';
        }
        if (cChar == 7788 || cChar == 7789) {
            return 't';
        }
        if ((cChar <= 220 && cChar >= 217) || ((cChar <= 252 && cChar >= 249) || cChar == 57463 || cChar == 57464 || ((cChar <= 57453 && cChar >= 57449) || ((cChar <= 57459 && cChar >= 57454) || cChar == 7908 || cChar == 7909 || cChar == 7915 || cChar == 7919)))) {
            return 'u';
        }
        if (cChar == 7266 || cChar == 7267) {
            return (char) 252;
        }
        if (cChar == 57460) {
            return 'v';
        }
        if (cChar == 7810 || cChar == 65510 || cChar == 57462 || cChar == 7811) {
            return 'w';
        }
        if (cChar == 215) {
            return 'x';
        }
        if (cChar == 165 || cChar == 221 || cChar == 253 || cChar == 255 || cChar == 65509 || cChar == 7923 || cChar == 7922 || cChar == 7925) {
            return 'y';
        }
        if (cChar == 377 || cChar == 7826 || cChar == 7827 || cChar == 7828) {
            return 'z';
        }
        return cChar;
    }

    public static void setJapaneseInputToPreference(Context ctx, boolean value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences("PrefJapaneseInput", 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putBoolean("JapaneseInput", value);
        editor.commit();
    }

    public static boolean getJapaneseInputFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences("PrefJapaneseInput", 0);
        boolean value = pefSetting.getBoolean("JapaneseInput", false);
        return value;
    }

    public static void setChineseInputToPreference(Context ctx, boolean value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences("PrefJapaneseInput", 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putBoolean("JapaneseInput", value);
        editor.commit();
    }

    public static boolean getChineseInputFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences("PrefJapaneseInput", 0);
        boolean value = pefSetting.getBoolean("JapaneseInput", false);
        return value;
    }

    public static void setFontThemeToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_FONT_THEME, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_FONT_THEME_VALUE, value);
        editor.commit();
    }

    public static int getFontThemeFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_FONT_THEME, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_FONT_THEME_VALUE, 0);
        return value;
    }

    public static void setRecogTimeToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_RECOG_TIME, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_RECOG_TIME_VALUE, value);
        editor.commit();
    }

    public static int getRecogTimeFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_RECOG_TIME, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_RECOG_TIME_VALUE, DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE);
        return value;
    }

    public static void setCheckUpdateIntervalToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_UPDATE, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_UPDATE_VALUE, value);
        editor.commit();
    }

    public static int getCheckUpdateIntervalFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_UPDATE, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_UPDATE_VALUE, 3);
        return value;
    }

    public static void setLastDictToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_LAST_DICT, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_LAST_DICT_VALUE, value);
        editor.commit();
    }

    public static int getLastDictFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_LAST_DICT, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_LAST_DICT_VALUE, -1);
        return value;
    }

    public static void setDictationFeedbackModeToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_FEEDBACK_MODE, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_FEEDBACK_MODE_VALUE, value);
        editor.commit();
    }

    public static int getDictationFeedbackModeFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_FEEDBACK_MODE, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_FEEDBACK_MODE_VALUE, 0);
        return value;
    }

    public static void setGestureRecognitionToPreference(Context ctx, boolean value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_GESTURE, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putBoolean(DIODICT_SETTING_PREF_GESTURE_VALUE, value);
        editor.commit();
    }

    public static boolean getGestureRecognitionFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_GESTURE, 0);
        boolean value = pefSetting.getBoolean(DIODICT_SETTING_PREF_GESTURE_VALUE, false);
        return value;
    }

    public static String getStartDictFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_START_DICT, 0);
        String value = pefSetting.getString(DIODICT_SETTING_PREF_START_DICT_VALUE, "");
        return value;
    }

    public static void setRefreshViewStateToPreference(Context ctx, boolean value) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_REFRESH_VIEW, 0);
        SharedPreferences.Editor editor = pefSetting.edit();
        editor.putBoolean(DIODICT_SETTING_PREF_REFRESH_VIEW_VALUE, value);
        editor.commit();
    }

    public static boolean getRefreshViewStateFromPreference(Context ctx) {
        SharedPreferences prefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_REFRESH_VIEW, 0);
        boolean value = prefSetting.getBoolean(DIODICT_SETTING_PREF_REFRESH_VIEW_VALUE, false);
        return value;
    }

    public static boolean checkSoundMode(Context ctx) {
        AudioManager am = (AudioManager) ctx.getSystemService("audio");
        if (Dependency.isContainTTS() && am.getStreamVolume(3) == 0) {
            Toast.makeText(ctx, (int) R.string.not_playing_streamVolum_0, 0).show();
            return false;
        }
        return true;
    }

    public static void setSearchLastDictToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_LAST_DICT_VALUE, value);
        editor.commit();
    }

    public static int getSearchLastDictFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        int value = pefSetting.getInt(DIODICT_SEARCH_INFO_PREF_LAST_DICT_VALUE, -1);
        return value;
    }

    public static void setSearchLastTypeToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_LAST_TYPE_VALUE, value);
        editor.commit();
    }

    public static int getSearchLastTypeFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        int value = pefSetting.getInt(DIODICT_SEARCH_INFO_PREF_LAST_TYPE_VALUE, 1);
        return value;
    }

    public static void setSearchLastPosToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_LAST_POS_VALUE, value);
        editor.commit();
    }

    public static int getSearchLastPosFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        int value = pefSetting.getInt(DIODICT_SEARCH_INFO_PREF_LAST_POS_VALUE, 0);
        return value;
    }

    public static void setSearchLastWordToPreference(Context ctx, String value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putString(DIODICT_SEARCH_INFO_PREF_LAST_WORD_VALUE, value);
        editor.commit();
    }

    public static String getSearchLastWordFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        String value = pefSetting.getString(DIODICT_SEARCH_INFO_PREF_LAST_WORD_VALUE, "");
        return value;
    }

    public static void setSearchLastSearchInfoToPreference(Context ctx, int dict, int type, String word, int pos) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_LAST_DICT_VALUE, dict);
        editor.putInt(DIODICT_SEARCH_INFO_PREF_LAST_TYPE_VALUE, type);
        editor.putInt(DIODICT_SEARCH_INFO_PREF_LAST_POS_VALUE, pos);
        editor.putString(DIODICT_SEARCH_INFO_PREF_LAST_WORD_VALUE, word);
        editor.commit();
    }

    public static void initSearchLastSearchInfoToPreference(Context ctx) {
        setSearchLastSearchInfoToPreference(ctx, -1, 1, "", 0);
    }

    public static void setDurationTimeToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_DURATION_TIME, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_DURATION_TIME_VALUE, value);
        editor.commit();
    }

    public static int getDurationTimeFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_DURATION_TIME, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_DURATION_TIME_VALUE, DIODICT_SETTING_PREF_DURATION_TIME_DEFAULT_VALUE);
        return value;
    }

    public static void setCradleDateFormatToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_DATE_FORMAT, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SETTING_PREF_DATE_FORMAT_VALUE, value);
        editor.commit();
    }

    public static int getCradleDateFormatFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_DATE_FORMAT, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_DATE_FORMAT_VALUE, 1);
        return value;
    }

    public static void setCradleTimeFormatToPreference(Context ctx, boolean value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SETTING_PREF_TIME_FORMAT, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putBoolean(DIODICT_SETTING_PREF_TIME_FORMAT_VALUE, value);
        editor.commit();
    }

    public static boolean getCradleTimeFormatFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SETTING_PREF_TIME_FORMAT, 0);
        boolean value = pefSetting.getBoolean(DIODICT_SETTING_PREF_TIME_FORMAT_VALUE, false);
        return value;
    }

    public static void setSearchCursorInfoSortToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_CURSOR_SORT, value);
        editor.commit();
    }

    public static int getSearchCursorInfoSortFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        int value = pefSetting.getInt(DIODICT_SEARCH_INFO_PREF_CURSOR_SORT, 3);
        return value;
    }

    public static void setSearchCursorInfoWordPosToPreference(Context ctx, int value) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_CURSOR_WORD_POS, value);
        editor.commit();
    }

    public static int getSearchCursorInfoWordPosFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        int value = pefSetting.getInt(DIODICT_SEARCH_INFO_PREF_CURSOR_WORD_POS, 0);
        return value;
    }

    public static void setSearchCursorInfoToPreference(Context ctx, int sort, int pos) {
        SharedPreferences pefSetLanguage = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetLanguage.edit();
        editor.putInt(DIODICT_SEARCH_INFO_PREF_CURSOR_SORT, sort);
        editor.putInt(DIODICT_SEARCH_INFO_PREF_CURSOR_WORD_POS, pos);
        editor.commit();
    }

    public static void initSearchCursorInfoToPreference(Context ctx) {
        setSearchCursorInfoToPreference(ctx, 3, 0);
    }

    public static void setFlashcardSortToPreference(Context ctx, int value) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        SharedPreferences.Editor editor = pefSetting.edit();
        editor.putInt(DIODICT_SETTING_PREF_FLASHCARD_SORT, value);
        editor.commit();
    }

    public static int getFlashcardSortFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_SEARCH_INFO_PREF, 0);
        int value = pefSetting.getInt(DIODICT_SETTING_PREF_FLASHCARD_SORT, 0);
        return value;
    }

    public static boolean isFirstLoadingFromPreference(Context ctx) {
        SharedPreferences pefSetting = ctx.getSharedPreferences(DIODICT_FIRSTLOADING_INFO_PREF, 0);
        if (!pefSetting.getString(DIODICT_FIRSTLOADING_INFO_PREF_VALUE, "yes").equals("no")) {
            SharedPreferences.Editor editor = pefSetting.edit();
            editor.putString(DIODICT_FIRSTLOADING_INFO_PREF_VALUE, "no");
            editor.commit();
            return true;
        }
        return false;
    }

    public static void setVolume(Context context, int keyCode) {
        AudioManager am = (AudioManager) context.getSystemService("audio");
        if (am != null) {
            am.adjustSuggestedStreamVolume(keyCode == 24 ? 1 : -1, TTSEngine.isPlaying() ? 3 : 2, 17);
        }
    }

    public static boolean setVolumeByKey(Context context, int keyCode) {
        if (keyCode == 24 || keyCode == 25) {
            setVolume(context, keyCode);
            return true;
        }
        return false;
    }

    public static int ICU_CompareString(byte[] a, byte[] b) {
        String strA = convertByteToString(a, new String("UTF-16LE"), false);
        String strB = convertByteToString(b, new String("UTF-16LE"), false);
        return ICUCollator.getInstance().compareTo(strA, strB);
    }
}
