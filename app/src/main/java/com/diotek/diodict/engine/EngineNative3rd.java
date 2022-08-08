package com.diotek.diodict.engine;

import android.util.Log;
import com.diotek.diodict.dependency.Dependency;

/* loaded from: classes.dex */
public class EngineNative3rd {
    public static final int ERR_NONE = 0;
    public static final int ERR_NOT_FOUND_SO = -1;
    private static int m_lastErr = 0;

    public static native void LibChangeBuf(boolean z);

    public static native int LibDBMove(byte[] bArr, byte[] bArr2);

    public static native int LibDraCallBackTest();

    public static native void LibDrawMeanView(boolean z, boolean z2);

    public static native int LibGetCurDB();

    public static native int LibGetCurMeanSuid();

    public static native byte[] LibGetDBVersion(int i);

    public static native byte[] LibGetFirstWordList(int i);

    public static native byte[] LibGetGurMeanWord();

    public static native byte[] LibGetLastWordList(int i);

    public static native byte[] LibGetMeaning(int i, int i2, byte[] bArr, int i3, int i4, boolean z, boolean z2);

    public static native byte[] LibGetMeaningData(int i, byte[] bArr, int i2, int i3, boolean z);

    public static native byte[] LibGetMeaningFromOCR(byte[] bArr, int i, boolean z);

    public static native byte[] LibGetMeaningStr(byte[] bArr, int i, int i2);

    public static native OriginList LibGetOriginList(int i, int i2, byte[] bArr);

    public static native int LibGetPTNativePronouce(int i, int i2);

    public static native ResultWordList3rd LibGetResult();

    public static native boolean LibGetResultExactMatch();

    public static native int LibGetResultKeyPos();

    public static native int LibGetResultSize();

    public static native int LibGetSourceLanguage(int i);

    public static native int LibGetSpellCheckLangType(int i);

    public static native int LibGetSpellCheckResultNum();

    public static native byte[] LibGetSpellCheckWordByIndex(int i);

    public static native byte[] LibGetTalkerNotation(int i, int i2);

    public static native long LibGetTalkerSUID(int i, int i2);

    public static native byte[] LibGetTalkerSentence(int i, int i2);

    public static native int LibGetTargetLanguage(int i);

    public static native int LibGetUnicodeFromDioSymbol(int i);

    public static native byte[] LibGetWordList(int i, int i2);

    public static native int LibGetWordSuid(int i);

    public static native int LibHangulroSearch(int[] iArr, boolean z, int i);

    public static native int LibIdiomExampleSearch(byte[] bArr);

    public static native int LibIdiomExampleSearchByWL(byte[] bArr, int i);

    public static native int LibInit(byte[] bArr, boolean z, boolean z2, boolean z3);

    public static native int LibInitialSearch(byte[] bArr);

    public static native int LibInitialSearchByWL(byte[] bArr, int i);

    public static native int[] LibIntegrationDBArray();

    public static native boolean LibIsValidDBHandle();

    public static native int LibOpenDB(byte[] bArr, boolean z);

    public static native int LibPageGetCurLine();

    public static native int LibPageGetKeyFieldLine();

    public static native int LibPageGetLinePerPage();

    public static native int LibPageGetMeanFieldLine(boolean z);

    public static native long LibPageGetSelectedLine();

    public static native byte[] LibPageGetSelectedText(boolean z);

    public static native int LibPageGetTotalLine(boolean z);

    public static native boolean LibPageHasSelectedText();

    public static native boolean LibPageHyperAt(int i, int i2);

    public static native boolean LibPageHyperDown();

    public static native boolean LibPageHyperLeft();

    public static native boolean LibPageHyperRight();

    public static native boolean LibPageHyperUp();

    public static native boolean LibPageIsEnabledHyper();

    public static native boolean LibPageScroll(int i);

    public static native void LibPageSetDisplayMode(int i);

    public static native void LibPageSetDisplayModeByCode(int i);

    public static native void LibPageSetEnableHyper(boolean z, boolean z2);

    public static native void LibPageSetHyperType(boolean z, boolean z2);

    public static native boolean LibPageTouchDown(int i, int i2);

    public static native void LibPageTouchMove(int i, int i2);

    public static native boolean LibPageTouchUp(int i, int i2);

    public static native int LibReSearchMean();

    public static native int LibReleaseCBInterface();

    public static native int LibSearchByWL(byte[] bArr, int i, boolean z);

    public static native int LibSearchMean(byte[] bArr, int i);

    public static native int LibSearchMeanByPos(int i);

    public static native int LibSearchMeanBySUID(int i, byte[] bArr, int i2);

    public static native int LibSearchWord(byte[] bArr, boolean z);

    public static native void LibSetDicType(int i);

    public static native void LibSetHighlightPenColor(int[] iArr);

    public static native void LibSetLineGap(int i);

    public static native void LibSetMargin(int i, int i2, int i3, int i4);

    public static native void LibSetMyDic(byte[] bArr, int i);

    public static native void LibSetResultSize(int i);

    public static native void LibSetScreen(int i, int i2, int i3, int i4, boolean z);

    public static native void LibSetSepGap(int i);

    public static native void LibSetTalkerCurLang(int i);

    public static native void LibSetTalkerTransLang(int i);

    public static native void LibSetTheme(int[] iArr, int[] iArr2);

    public static native int LibSpellCheckInit(byte[] bArr, int i);

    public static native int LibSpellCheckSearch(byte[] bArr);

    public static native int LibSpellCheckTerm();

    public static native int LibTalkerInit(byte[] bArr);

    public static native int LibTalkerSearchResult(int i, int i2);

    public static native void LibTalkerSetDBPath(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7);

    public static native int LibTalkerTerm();

    public static native int LibTerminate();

    public static native int LibUnifiySearch(byte[] bArr, int i, int i2, int i3);

    public static native int LibWildCardSearch(byte[] bArr);

    public static native int LibWildCardSearchByWL(byte[] bArr, int i);

    public static native boolean LibisUnicodeDB(int i);

    static {
        try {
            if (Dependency.getDevice().isPreload()) {
                DictInfo.DIODICT_LIB_NAME = "DioDict3EngineNative";
            }
            Log.i("JNI", "Trying to load " + DictInfo.DIODICT_LIB_NAME);
            System.loadLibrary(DictInfo.DIODICT_LIB_NAME);
            setLastErr(0);
        } catch (UnsatisfiedLinkError e) {
            Log.e("JNI", "Warning: Could not load " + DictInfo.DIODICT_LIB_NAME);
            setLastErr(-1);
        }
    }

    public static void setLastErr(int m_lastErr2) {
        m_lastErr = m_lastErr2;
    }

    public static int getLastErr() {
        return m_lastErr;
    }
}
