package com.diotek.diodict.dhwr.b2c.kor;

import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.mean.MSG;

/* loaded from: classes.dex */
public class DHWR {
    public static final int ADDITIVE_DEGRADECAND = 31;
    public static final int ADDITIVE_INIT = 28;
    public static final int ADDITIVE_REMOVECAND = 30;
    public static final int ADDITIVE_REMOVELAST = 29;
    public static final int DHWR_LOG_BUFFER = 10;
    public static final int DHWR_LOG_CALLBACK = 9;
    public static final int DHWR_LOG_LEVEL = 8;
    public static final int DHWR_PARAM_BOUND = 6;
    public static final int DHWR_PARAM_ENG_GAP_THRESHOLD = 4;
    public static final int DHWR_PARAM_LANG_MAIN = 0;
    public static final int DHWR_PARAM_LANG_SUB = 1;
    public static final int DHWR_PARAM_LANG_TYPE = 2;
    public static final int DHWR_PARAM_MODEL = 12;
    public static final int DHWR_PARAM_RAM_BUFFER = 11;
    public static final int DHWR_PARAM_ROTATE = 7;
    public static final int DHWR_PARAM_SENSITIVITY = 3;
    public static final int DHWR_PARAM_SHIFT_INFO = 13;
    public static final int DHWR_PARAM_SHIFT_INFO_DIRECTION = 15;
    public static final int DHWR_PARAM_SHIFT_INFO_PERCENT = 16;
    public static final int DHWR_PARAM_SHIFT_INFO_STATE = 14;
    public static final int DHWR_PARAM_SHIFT_NUM_0 = 17;
    public static final int DHWR_PARAM_SHIFT_NUM_1 = 18;
    public static final int DHWR_PARAM_SHIFT_NUM_2 = 19;
    public static final int DHWR_PARAM_SHIFT_NUM_3 = 20;
    public static final int DHWR_PARAM_SHIFT_NUM_4 = 21;
    public static final int DHWR_PARAM_SHIFT_NUM_5 = 22;
    public static final int DHWR_PARAM_SHIFT_NUM_6 = 23;
    public static final int DHWR_PARAM_SHIFT_NUM_7 = 24;
    public static final int DHWR_PARAM_SHIFT_NUM_8 = 25;
    public static final int DHWR_PARAM_SHIFT_NUM_9 = 26;
    public static final int DHWR_PARAM_UNISTROKE_STATE = 27;
    public static final int DHWR_PARAM_USER_CHARSET = 5;
    public static final byte DHWR_SHIFT_ALL_OFF = 0;
    public static final byte DHWR_SHIFT_ALL_ON = 7;
    public static final byte DHWR_SHIFT_DIRECT_BOTTOM = 8;
    public static final byte DHWR_SHIFT_DIRECT_LEFT = 1;
    public static final byte DHWR_SHIFT_DIRECT_NONE = 0;
    public static final byte DHWR_SHIFT_DIRECT_RIGHT = 2;
    public static final byte DHWR_SHIFT_DIRECT_TOP = 4;
    public static final byte DHWR_SHIFT_ENG_ON = 1;
    public static final byte DHWR_SHIFT_HAN_ON = 4;
    public static final byte DHWR_SHIFT_INIT = 0;
    public static final byte DHWR_SHIFT_NUM_ON = 2;
    public static final byte DHWR_UNISTROKE_CONTEXT_INIT = 1;
    public static final byte DHWR_UNISTROKE_CONTEXT_NOTUSE = 0;
    public static final byte DHWR_UNISTROKE_STATE_FAIL = 0;
    public static final byte DHWR_UNISTROKE_STATE_NEW = 1;
    public static final byte DHWR_UNISTROKE_STATE_UPDATE = 2;
    public static final int DLANG_ALBANIAN = 1;
    public static final int DLANG_ARABIC = 116;
    public static final int DLANG_ARABIC_SYM = 119;
    public static final int DLANG_AUSTRIA = 2;
    public static final int DLANG_BASQUE = 3;
    public static final int DLANG_BELARUSIAN = 32;
    public static final int DLANG_BENGALI = 121;
    public static final int DLANG_BPMF = 115;
    public static final int DLANG_BULGARIAN = 33;
    public static final int DLANG_CALCULATOR = 136;
    public static final int DLANG_CATALAN = 4;
    public static final int DLANG_CROATIAN = 5;
    public static final int DLANG_CZECH = 6;
    public static final int DLANG_DANISH = 7;
    public static final int DLANG_DIALDIGIT = 137;
    public static final int DLANG_DIALDIGIT_PW = 138;
    public static final int DLANG_DUTCH = 8;
    public static final int DLANG_EMOTICON = 134;
    public static final int DLANG_ENGLISH = 0;
    public static final int DLANG_EQUALITY = 133;
    public static final int DLANG_ESTONIAN = 9;
    public static final int DLANG_EXTRA_CHN = 110;
    public static final int DLANG_FARSI = 117;
    public static final int DLANG_FINNISH = 10;
    public static final int DLANG_FLICK = 129;
    public static final int DLANG_FRENCH = 11;
    public static final int DLANG_GAELIC = 12;
    public static final int DLANG_GALICIAN = 13;
    public static final int DLANG_GERMANY = 2;
    public static final int DLANG_GESTURE = 126;
    public static final int DLANG_GESTURE_EDITING = 127;
    public static final int DLANG_GREEK = 34;
    public static final int DLANG_HANJA = 102;
    public static final int DLANG_HEBREW = 122;
    public static final int DLANG_HINDI = 120;
    public static final int DLANG_HIRAGANA = 112;
    public static final int DLANG_HONGKONG = 111;
    public static final int DLANG_HUNGARIAN = 14;
    public static final int DLANG_ICELANDIC = 15;
    public static final int DLANG_IRISH = 12;
    public static final int DLANG_ITALIAN = 16;
    public static final int DLANG_KANJI = 114;
    public static final int DLANG_KATAKANA = 113;
    public static final int DLANG_KAZAKH = 35;
    public static final int DLANG_KOREAN = 101;
    public static final int DLANG_LATVIAN = 17;
    public static final int DLANG_LITHUANIAN = 18;
    public static final int DLANG_MACEDONIAN = 36;
    public static final int DLANG_MAORI = 43;
    public static final int DLANG_MONGOLIAN = 37;
    public static final int DLANG_NORWEGIAN = 19;
    public static final int DLANG_NUMERIC = 130;
    public static final int DLANG_OROMO = 47;
    public static final int DLANG_POLISH = 20;
    public static final int DLANG_PORTUGUESE = 21;
    public static final int DLANG_PORTUGUESEB = 22;
    public static final int DLANG_PUNC = 131;
    public static final int DLANG_REGION1_ALL = 31;
    public static final int DLANG_REGION2_ALL = 54;
    public static final int DLANG_ROMANIAN = 23;
    public static final int DLANG_RUSSIAN = 38;
    public static final int DLANG_SAMOAN = 44;
    public static final int DLANG_SERBIAN_C = 39;
    public static final int DLANG_SERBIAN_L = 24;
    public static final int DLANG_SIMP_CHN = 103;
    public static final int DLANG_SIMP_CHN_COMMON = 104;
    public static final int DLANG_SIMP_CHN_RARE = 105;
    public static final int DLANG_SIMP_RADICAL = 106;
    public static final int DLANG_SLOVAK = 25;
    public static final int DLANG_SLOVENIAN = 26;
    public static final int DLANG_SOTHO_N = 49;
    public static final int DLANG_SOTHO_S = 48;
    public static final int DLANG_SPANISH = 13;
    public static final int DLANG_SWAHILI = 50;
    public static final int DLANG_SWATI = 51;
    public static final int DLANG_SWEDISH = 27;
    public static final int DLANG_SYMBOL = 132;
    public static final int DLANG_TAITIAN = 45;
    public static final int DLANG_TATAR_C = 41;
    public static final int DLANG_TATAR_L = 30;
    public static final int DLANG_THAI = 123;
    public static final int DLANG_THAI_DIGIT = 125;
    public static final int DLANG_THAI_SIGN = 124;
    public static final int DLANG_TONGAN = 46;
    public static final int DLANG_TRAD_CHN = 107;
    public static final int DLANG_TRAD_CHN_COMMON = 108;
    public static final int DLANG_TRAD_CHN_RARE = 109;
    public static final int DLANG_TURKISH = 28;
    public static final int DLANG_UKRAINIAN = 40;
    public static final int DLANG_URDU = 118;
    public static final int DLANG_USER_SET = 135;
    public static final int DLANG_USER_SHAPE = 128;
    public static final int DLANG_VIETNAMESE = 29;
    public static final int DLANG_XHOSA = 52;
    public static final int DLANG_ZULU = 53;
    public static final int DTYPE_AUTO_SPACE;
    public static final int DTYPE_BOTH_SIMP_TRAD;
    public static final int DTYPE_CONSONANT;
    public static final int DTYPE_CONS_RECOMMEND;
    public static final int DTYPE_CURRENCY;
    public static final int DTYPE_CURSIVE;
    public static final int DTYPE_ENDPUNC;
    public static final int DTYPE_JOHAP;
    public static final int DTYPE_LOWERCASE;
    public static final int DTYPE_MULTI_CHARS;
    public static final int DTYPE_MULTI_LINE;
    public static final int DTYPE_NONE;
    public static final int DTYPE_NUMERIC;
    public static final int DTYPE_ONLY_SIMP;
    public static final int DTYPE_ONLY_TRAD;
    public static final int DTYPE_SIGN;
    public static final int DTYPE_SIMP_TO_TRAD;
    public static final int DTYPE_TONE;
    public static final int DTYPE_TRAD_TO_SIMP;
    public static final int DTYPE_UNISTROKE;
    public static final int DTYPE_UPPERCASE;
    public static final int DTYPE_VOWEL;
    public static final int DTYPE_VOWEL_DEPENDENT;
    public static final int DTYPE_WANSUNG;
    public static final int DTYPE_WANSUNG_EX;
    public static final int ERR_EMPTY_INK = 5;
    public static final int ERR_EXPIRE_DEMO = 10;
    public static final int ERR_INVALID_ARGUMENTS = 6;
    public static final int ERR_INVALID_DICTIONARY = 8;
    public static final int ERR_INVALID_INSTANCE = 9;
    public static final int ERR_INVALID_MODEL = 7;
    public static final int ERR_NORESULT = 1;
    public static final int ERR_NULL_POINTER = 2;
    public static final int ERR_OUTOFMEMORY = 3;
    public static final int ERR_OUTOFRANGE = 4;
    public static final int ERR_SUCCESS = 0;
    public static final int ERR_UNSUPPORTED_MODE = 11;
    public static final byte GESTURE_BACKSPACE = 8;
    public static final byte GESTURE_DELETE = 12;
    public static final byte GESTURE_GRETURN = 3;
    public static final byte GESTURE_MERGE = 2;
    public static final byte GESTURE_RETURN = 13;
    public static final byte GESTURE_SHIFT = 16;
    public static final byte GESTURE_SPACE = 32;
    public static final int LANG_FIJIAN = 42;
    public static final int LEVEL_DEBUG = 4;
    public static final int LEVEL_ERROR = 1;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_NONE = 0;
    public static final int LEVEL_WARN = 2;
    public static final byte MAX_CANDIDATES = 10;
    public static final byte MAX_CHARACTERS = 20;
    public static final int MULTICHAR = 1;
    public static final int SINGLECHAR = 0;
    public static final int maxmode = 1;
    public int[][] arMode;
    public int type = 0;
    public int nMode = 0;
    public int nCands = 10;

    public static final native int AddPoint(short s, short s2);

    public static final native int AddWordToUserDict(char[] cArr);

    public static final native int CheckWordInDict(char[] cArr);

    public static final native int ClearUAInfo();

    public static final native int Close();

    public static final native int ConfirmCand(int i);

    public static final native int Create();

    public static final native int DHWRClearUserModel();

    public static final native int DHWRGetUserModelInkBuff(int i, int i2, char[] cArr, int[] iArr);

    public static final native int DHWRGetUserModelInkPointSize(int i, int[] iArr);

    public static final native int DHWRGetUserModelSize(int[] iArr);

    public static final native int DHWRLoadUserModel(byte[] bArr, int i);

    public static final native int DHWRRegisterUserModel(char c);

    public static final native int DHWRStoreUserModel(byte[] bArr, int[] iArr);

    public static final native int DHWRUnregisterUserModel(int i);

    public static final native int EndStroke();

    public static final native int FreeUserCharSet();

    public static final native int FreeUserVowelCharSet();

    public static final native int GetInkCount(int[] iArr);

    public static final native int GetInkPoint(int[] iArr, int[] iArr2, int i);

    public static final native int GetParam(int i, byte[] bArr);

    public static final native int GetRevision(char[] cArr);

    public static final native int InkClear();

    public static final native int LoadDict(byte[] bArr, byte[] bArr2);

    public static final native int LoadUADB(byte[] bArr);

    public static final native int Recognize(char[] cArr);

    public static final native int SaveUADB(byte[] bArr, int[] iArr);

    public static final native int SetAttribute(int i, int[][] iArr, int i2, int[] iArr2);

    public static final native int SetParam(int i, byte[] bArr);

    public static final native int SetUserCharSet(char[] cArr, int i);

    public static final native int SetUserVowelCharSet(char[] cArr, int i);

    public static final native int SetWritingArea(int i, int i2, int i3, int i4, int i5);

    static {
        if (Dependency.getDevice().isContainHandWrightReocg()) {
            try {
                MSG.l("Try LoadLibrary.");
                System.loadLibrary(DictInfo.HWR_LIB_NAME);
            } catch (UnsatisfiedLinkError e) {
                MSG.l(2, "LoadLibrary failed.");
            }
            MSG.l(1, "LoadLibrary success.");
        }
        DTYPE_NONE = BIT_FLAG(0);
        DTYPE_MULTI_CHARS = BIT_FLAG(1);
        DTYPE_UPPERCASE = BIT_FLAG(2);
        DTYPE_LOWERCASE = BIT_FLAG(3);
        DTYPE_AUTO_SPACE = BIT_FLAG(4);
        DTYPE_MULTI_LINE = BIT_FLAG(5);
        DTYPE_UNISTROKE = BIT_FLAG(6);
        DTYPE_CURSIVE = BIT_FLAG(7);
        DTYPE_WANSUNG = BIT_FLAG(8);
        DTYPE_JOHAP = BIT_FLAG(9);
        DTYPE_CONSONANT = BIT_FLAG(10);
        DTYPE_CONS_RECOMMEND = BIT_FLAG(11);
        DTYPE_VOWEL = BIT_FLAG(12);
        DTYPE_VOWEL_DEPENDENT = BIT_FLAG(13);
        DTYPE_TONE = BIT_FLAG(14);
        DTYPE_CURRENCY = BIT_FLAG(15);
        DTYPE_SIMP_TO_TRAD = BIT_FLAG(16);
        DTYPE_TRAD_TO_SIMP = BIT_FLAG(17);
        DTYPE_ONLY_TRAD = BIT_FLAG(2);
        DTYPE_ONLY_SIMP = BIT_FLAG(3);
        DTYPE_BOTH_SIMP_TRAD = BIT_FLAG(8);
        DTYPE_ENDPUNC = BIT_FLAG(18);
        DTYPE_WANSUNG_EX = BIT_FLAG(19);
        DTYPE_NUMERIC = BIT_FLAG(20);
        DTYPE_SIGN = BIT_FLAG(21);
    }

    private static final int BIT_FLAG(int n) {
        return 1 << n;
    }
}
