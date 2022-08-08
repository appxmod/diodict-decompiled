package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public enum TTSQualityLevel {
    QUALITY_NONE,
    CORPORATE,
    PLUS,
    PRO,
    STANDART;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static TTSQualityLevel[] valuesCustom() {
        TTSQualityLevel[] valuesCustom = values();
        int length = valuesCustom.length;
        TTSQualityLevel[] tTSQualityLevelArr = new TTSQualityLevel[length];
        System.arraycopy(valuesCustom, 0, tTSQualityLevelArr, 0, length);
        return tTSQualityLevelArr;
    }
}
