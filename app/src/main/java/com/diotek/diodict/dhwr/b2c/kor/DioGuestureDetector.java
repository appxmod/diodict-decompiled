package com.diotek.diodict.dhwr.b2c.kor;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class DioGuestureDetector {
    private static final boolean HWR_DEBUG = false;
    private static final String TAG = "DioGuestureDetector";
    private int mHWInputMode;
    private int mHWInputModeType;
    Context mIMEService;
    private int mLanguageMode;
    private boolean mOnLangChinese;
    private boolean mOnLangEnglish;
    private boolean mOnLangKorean;
    private final boolean DEBUG = true;
    private final int CHINESE_GESTURE_SPACE = 31;
    private final String BUILD_MODEL = "sdk";
    int[] mCands = new int[1];
    private boolean mIsSingleCharMode = false;
    private boolean mIsKorInitialSearch = false;
    private String[] mCandList = null;

    public void setContext(Context context) {
        this.mIMEService = context;
    }

    public int Create() {
        DHWR.SetParam(12, "sdk".getBytes());
        return DHWR.Create();
    }

    public void Close() {
        DHWR.Close();
    }

    public void setLanguage(int Language) {
        this.mOnLangChinese = true;
        this.mOnLangKorean = true;
        this.mOnLangEnglish = true;
    }

    private void showHwrErrStateToast() {
    }

    private void setAttribute(int type, int Language, int LanguageType, int nMode) {
        int[][] arMode = (int[][]) Array.newInstance(Integer.TYPE, 1, 2);
        arMode[0][0] = Language;
        arMode[0][1] = LanguageType;
        this.mCands[0] = 10;
        DHWR.SetAttribute(type, arMode, nMode, this.mCands);
    }

    public boolean recognizeGestureBackSpace() {
        Log.d(TAG, "recognizeGestureBackSpace");
        char[] aResult = new char[210];
        setAttribute(0, 127, DHWR.DTYPE_NONE, 1);
        DHWR.Recognize(aResult);
        if (aResult[0] == '\b') {
            clearInk();
            return true;
        }
        return false;
    }

    private boolean recognizeGesture() {
        Log.d(TAG, "recognizeGesture");
        char[] aResult = new char[210];
        setAttribute(0, 127, DHWR.DTYPE_NONE, 1);
        int result = DHWR.Recognize(aResult);
        if (((short) result) != 0) {
            return false;
        }
        switch (aResult[0]) {
            case '\r':
            case 31:
                clearInk();
                return true;
            default:
                return false;
        }
    }

    private boolean recognizeGestureSpace() {
        Log.d(TAG, "recognizeGesture");
        char[] aResult = new char[210];
        setAttribute(0, 127, DHWR.DTYPE_NONE, 1);
        int result = DHWR.Recognize(aResult);
        if (((short) result) != 0) {
            return false;
        }
        switch (aResult[0]) {
            case ' ':
                clearInk();
                return true;
            default:
                return false;
        }
    }

    public void recognizeSentence() {
        showHwrErrStateToast();
        if (!isRecognizeTextMode() || !recognizeGesture()) {
            char[] aResult = new char[210];
            for (int i = 0; i < aResult.length; i++) {
                aResult[i] = 0;
            }
            setAttribute(1, getHwrInputMode(), getHwrInputModeType(), 1);
            Log.d(TAG, "getHwrInputMode(): " + getHwrInputMode());
            Log.d(TAG, "getHwrInputModeType(): " + getHwrInputModeType());
            int result = DHWR.Recognize(aResult);
            for (int j = 0; j < aResult.length; j++) {
                Log.d(TAG, "aResult : " + ((int) aResult[j]));
            }
            Log.d(TAG, "result: " + result);
            clearInk();
        }
    }

    private void setAttributeChineseAll() {
        int[][] arMode = (int[][]) Array.newInstance(Integer.TYPE, 5, 2);
        int nMode = 0;
        if (this.mOnLangChinese) {
            arMode[0][0] = 103;
            arMode[0][1] = DHWR.DTYPE_NONE;
            nMode = 0 + 1;
        }
        if (this.mOnLangKorean) {
            arMode[nMode][0] = 101;
            arMode[nMode][1] = DHWR.DTYPE_JOHAP | DHWR.DTYPE_CONSONANT;
            nMode++;
        }
        if (this.mOnLangEnglish) {
            arMode[nMode][0] = 0;
            arMode[nMode][1] = DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE;
            nMode++;
        }
        arMode[nMode][0] = 130;
        arMode[nMode][1] = DHWR.DTYPE_NONE;
        int nMode2 = nMode + 1;
        arMode[nMode2][0] = 132;
        arMode[nMode2][1] = DHWR.DTYPE_NONE;
        int nMode3 = nMode2 + 1;
        this.mCands[0] = 10;
        if (nMode3 > 0) {
            Log.i(TAG, " nMode = " + nMode3);
            DHWR.SetAttribute(0, arMode, nMode3, this.mCands);
        }
    }

    private void setAttributeSentenceGesture() {
        int[][] arMode = (int[][]) Array.newInstance(Integer.TYPE, 5, 2);
        int nMode = 0;
        int nType = 0;
        if (this.mLanguageMode == 0) {
            arMode[0][0] = 0;
            arMode[0][1] = DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE;
            int nMode2 = 0 + 1;
            arMode[nMode2][0] = 130;
            arMode[nMode2][1] = DHWR.DTYPE_NONE;
            nMode = nMode2 + 1;
            nType = 1;
        } else if (this.mLanguageMode == 1) {
            arMode[0][0] = 101;
            if (this.mIsKorInitialSearch) {
                arMode[0][1] = DHWR.DTYPE_CONSONANT;
            } else {
                arMode[0][1] = DHWR.DTYPE_JOHAP;
            }
            nMode = 0 + 1;
            nType = 1;
        } else if (this.mLanguageMode == 2) {
            arMode[0][0] = 11;
            arMode[0][1] = DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE;
            int nMode3 = 0 + 1;
            arMode[nMode3][0] = 130;
            arMode[nMode3][1] = DHWR.DTYPE_NONE;
            nMode = nMode3 + 1;
            nType = 1;
        } else if (this.mLanguageMode == 3) {
            arMode[0][0] = 2;
            arMode[0][1] = DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE;
            int nMode4 = 0 + 1;
            arMode[nMode4][0] = 130;
            arMode[nMode4][1] = DHWR.DTYPE_NONE;
            nMode = nMode4 + 1;
            nType = 1;
        } else if (this.mLanguageMode == 4) {
            arMode[0][0] = 112;
            arMode[0][1] = DHWR.DTYPE_NONE;
            int nMode5 = 0 + 1;
            arMode[nMode5][0] = 113;
            arMode[nMode5][1] = DHWR.DTYPE_NONE;
            int nMode6 = nMode5 + 1;
            arMode[nMode6][0] = 114;
            arMode[nMode6][1] = DHWR.DTYPE_NONE;
            int nMode7 = nMode6 + 1;
            arMode[nMode7][0] = 132;
            arMode[nMode7][1] = DHWR.DTYPE_NONE;
            nMode = nMode7 + 1;
        } else if (this.mLanguageMode == 5) {
            arMode[0][0] = 103;
            arMode[0][1] = DHWR.DTYPE_NONE;
            int nMode8 = 0 + 1;
            arMode[nMode8][0] = 107;
            arMode[nMode8][1] = DHWR.DTYPE_NONE;
            nMode = nMode8 + 1;
        } else if (this.mLanguageMode == 6) {
            arMode[0][0] = 103;
            arMode[0][1] = DHWR.DTYPE_NONE;
            nMode = 0 + 1;
        } else if (this.mLanguageMode == 7) {
            arMode[0][0] = 107;
            arMode[0][1] = DHWR.DTYPE_NONE;
            nMode = 0 + 1;
        } else if (this.mLanguageMode == 8) {
            arMode[0][0] = 29;
            arMode[0][1] = DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE;
            nMode = 0 + 1;
        }
        this.mCands[0] = 10;
        if (nMode > 0) {
            DHWR.SetAttribute(nType, arMode, nMode, this.mCands);
        }
        if (nType == 0) {
            this.mIsSingleCharMode = true;
        } else {
            this.mIsSingleCharMode = false;
        }
    }

    public char recognizeGesetureDetect() {
        char cResult;
        char[] aResult = new char[210];
        if (recognizeGestureSpace()) {
            return ' ';
        }
        setAttributeChineseAll();
        int result = DHWR.Recognize(aResult);
        clearInk();
        if (result >= 0) {
            cResult = aResult[0];
        } else {
            cResult = 0;
        }
        Log.d(TAG, "recognizeGesetureDetect: cResult = " + cResult + " result = " + result);
        return cResult;
    }

    public String[] getCandidate() {
        return this.mCandList;
    }

    public boolean isSingleCharMode() {
        return this.mIsSingleCharMode;
    }

    public String recognizeSentenceGesetureDetect() {
        char cResult;
        char[] aResult = new char[210];
        String strResult = null;
        StringBuilder sb = new StringBuilder();
        if (this.mLanguageMode != 4 && this.mLanguageMode != 6 && (cResult = recognizeSymbolGesture()) != 0) {
            clearInk();
            sb.append(cResult);
            return sb.toString();
        }
        setAttributeSentenceGesture();
        int result = DHWR.Recognize(aResult);
        clearInk();
        if (result >= 0) {
            strResult = new String(aResult);
        }
        if (strResult != null) {
            this.mCandList = strResult.split("\u0000");
        }
        if (strResult != null) {
            boolean isFinal = true;
            for (int nPos = 0; nPos < strResult.length() && (strResult.charAt(nPos) != 0 || !isFinal); nPos++) {
                if (this.mLanguageMode == 4) {
                    if (isJapaneseCharacter(strResult.charAt(nPos))) {
                        sb.append(strResult.charAt(nPos));
                        isFinal = true;
                    } else {
                        isFinal = false;
                    }
                } else {
                    sb.append(strResult.charAt(nPos));
                }
            }
        }
        Log.d(TAG, "recognizeSentenceGesetureDetect2: cResult = " + sb.toString() + " result = " + result);
        return sb.toString();
    }

    private boolean isJapaneseCharacter(char charAt) {
        return (charAt >= 12352 && charAt <= 65535) || charAt == '*' || charAt == '?';
    }

    private char recognizeSymbolGesture() {
        char[] aResult = new char[210];
        setAttribute(0, DHWR.DLANG_SYMBOL, DHWR.DTYPE_NONE, 1);
        int result = DHWR.Recognize(aResult);
        if (((short) result) != 0) {
            return (char) 0;
        }
        if (aResult[0] != '*' && aResult[0] != '?') {
            return (char) 0;
        }
        return aResult[0];
    }

    public void recognizeBoxMode(boolean isComplexMode) {
        showHwrErrStateToast();
        if (!recognizeGesture()) {
            char[] aResult = new char[210];
            if (!isComplexMode) {
                setAttribute(0, getHwrInputMode(), getHwrInputModeType(), 1);
            }
            int result = DHWR.Recognize(aResult);
            Log.d(TAG, "getHwrInputMode(): " + getHwrInputMode());
            Log.d(TAG, "result box: " + result);
            clearInk();
        }
    }

    public void setWritingArea(Rect rect) {
        DHWR.SetWritingArea(rect.left, rect.top, rect.right, rect.bottom, 40);
    }

    public void addPoint(short x, short y) {
        DHWR.AddPoint(x, y);
    }

    public int getInkCounter() {
        int[] count = new int[1];
        DHWR.GetInkCount(count);
        return count[0];
    }

    public void endStroke() {
        DHWR.EndStroke();
    }

    private void setHwrInputMode(int mode) {
        this.mHWInputMode = mode;
    }

    private void setHwrInputModeType(int modeType) {
        this.mHWInputModeType = modeType;
    }

    private int getHwrInputMode() {
        return this.mHWInputMode;
    }

    private int getHwrInputModeType() {
        return this.mHWInputModeType;
    }

    public void clearInk() {
        DHWR.InkClear();
    }

    public void setHwrInputLanguageForGuesture() {
        setHwrInputMode(0);
        setHwrInputModeType(DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE);
    }

    private boolean isRecognizeTextMode() {
        return getHwrInputMode() != 132;
    }

    public void setSuggestionActive(boolean active) {
    }

    public void destroy() {
        Close();
    }

    public void setLanguage(boolean mOnRecogLangEng, boolean mOnRecogLangKor, boolean mOnRecogLangChn) {
        this.mOnLangChinese = mOnRecogLangChn;
        this.mOnLangKorean = mOnRecogLangKor;
        this.mOnLangEnglish = true;
    }

    public void setLanguageMode(int imeMode, boolean isKorInitialSearch) {
        this.mLanguageMode = imeMode;
        this.mIsKorInitialSearch = isKorInitialSearch;
    }
}
