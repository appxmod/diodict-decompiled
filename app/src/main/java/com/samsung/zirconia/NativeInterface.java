package com.samsung.zirconia;

/* loaded from: classes.dex */
class NativeInterface {
    public static native boolean checkLicenseFile(String str, String str2, String str3);

    public static native boolean checkLicenseFile2(String str, String str2);

    public static native String doPassphraseTest(String str, String str2);

    public static native boolean storeLicenseKey(String str, byte[] bArr, String str2);

    NativeInterface() {
    }

    static {
        System.loadLibrary("nativeinterface");
    }
}
