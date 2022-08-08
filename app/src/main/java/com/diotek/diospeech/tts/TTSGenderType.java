package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public enum TTSGenderType {
    GENDER_NONE,
    MALE,
    FEMALE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static TTSGenderType[] valuesCustom() {
        TTSGenderType[] valuesCustom = values();
        int length = valuesCustom.length;
        TTSGenderType[] tTSGenderTypeArr = new TTSGenderType[length];
        System.arraycopy(valuesCustom, 0, tTSGenderTypeArr, 0, length);
        return tTSGenderTypeArr;
    }
}
