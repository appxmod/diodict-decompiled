package com.diotek.diodict.mean;

import java.lang.Character;

/* loaded from: classes.dex */
public class CodeBlock {
    public static final int CB_ALPHABET = 2;
    public static final int CB_CHINESE = 1;
    public static final int CB_HANGUL = 3;
    public static final int CB_JAPANESE = 4;
    public static final int UG_CJKCF_END = 65103;
    public static final int UG_CJKCF_START = 65072;
    public static final int UG_CJKCI_END = 64255;
    public static final int UG_CJKCI_START = 63744;
    public static final int UG_CJKEA_END = 19903;
    public static final int UG_CJKEA_START = 13312;
    public static final int UG_CJKRS_END = 12031;
    public static final int UG_CJKRS_START = 11904;
    public static final int UG_CJKSP_END = 12351;
    public static final int UG_CJKSP_START = 12288;
    public static final int UG_CJK_C_END = 13311;
    public static final int UG_CJK_C_START = 13056;
    public static final int UG_CJK_I_END = 40879;
    public static final int UG_CJK_I_START = 19968;
    public static final int UG_E_CJK_END = 13055;
    public static final int UG_E_CJK_START = 12800;

    public static int getCodeBlock(char c) {
		int typ = Character.getType(c);
        //if (isAlpabetCodeBlock(c) || isLatin(c)) {
        if (typ==Character.UPPERCASE_LETTER || typ==Character.LOWERCASE_LETTER) {
            return 2;
        }
        if (isHangulCodeBlock(c)) {
            return 3;
        }
        if (isChineseCodeBlock(c)) {
            return 1;
        }
        return isJapan(c) ? 4 : 0;
    }

    public static boolean isChineseCodeBlock(char a) {
        return isChinese(a) || isYbmChinese(a);
    }

    public static boolean isAlpabetCodeBlock(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isHangulCodeBlock(char c) {
        return isHangulSyllable(c) || isHangulCompJamo(c) || isHangulJamo(c);
    }

    private static boolean isYbmChinese(char a) {
        return (a >= 42873 && a <= 42994) || (a >= 61440 && a <= 61471) || !((a < 63494 || a > 63544 || a == 63499 || a == 63500 || a == 63502) && (a < 63639 || a > 63743 || a == 63682 || a == 63683 || a == 63687 || a == 63697 || a == 63726 || a == 63727 || a == 63730 || a == 63731));
    }

    private static boolean isChinese(char a) {
        return (11904 <= a && a <= 12031) || (6144 <= a && a <= 7254) || ((12800 <= a && a <= 13055) || ((13056 <= a && a <= 13168) || ((13179 <= a && a <= 13183) || ((13280 <= a && a <= 13310) || ((13312 <= a && a <= 19903) || ((19968 <= a && a <= 40879) || ((55204 <= a && a <= 55216) || ((63744 <= a && a <= 64255 && ((41991 > a || a > 42054) && ((42057 > a || a > 42068) && a != 42071))) || ((41216 <= a && a <= 41312) || ((41339 <= a && a <= 41432 && a != 41390 && a != 41391) || a == 41756 || ((41901 <= a && a <= 41918) || ((65477 <= a && a <= 65503) || (65072 <= a && a <= 65103)))))))))))));
    }

    private static boolean isHangulJamo(char a) {
        return a >= 4352 && a < 4607;
    }

    private static boolean isHangulCompJamo(char a) {
        return a >= 12592 && a <= 12687;
    }

    private static boolean isHangulSyllable(char a) {
        return a >= 44032 && a <= 55215;
    }

    public static boolean isLatin(char a) {
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(a);
        return ('a' <= a && a <= 'z') || ('A' <= a && a <= 'Z') || Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL.equals(unicodeBlock) || ((192 <= a && a <= 214) || ((216 <= a && a <= 246) || ((248 <= a && a <= 255) || Character.UnicodeBlock.LATIN_EXTENDED_A.equals(unicodeBlock) || Character.UnicodeBlock.LATIN_EXTENDED_B.equals(unicodeBlock))));
    }

    public static boolean isJapan(char a) {
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(a);
        return Character.UnicodeBlock.HIRAGANA.equals(unicodeBlock) || Character.UnicodeBlock.KATAKANA.equals(unicodeBlock) || Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(unicodeBlock);
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
