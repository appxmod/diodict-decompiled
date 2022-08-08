package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public enum TTSParamId {
    SPEED,
    VOLUME,
    PITCH,
    POLYREAD;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static TTSParamId[] valuesCustom() {
        TTSParamId[] valuesCustom = values();
        int length = valuesCustom.length;
        TTSParamId[] tTSParamIdArr = new TTSParamId[length];
        System.arraycopy(valuesCustom, 0, tTSParamIdArr, 0, length);
        return tTSParamIdArr;
    }
}
