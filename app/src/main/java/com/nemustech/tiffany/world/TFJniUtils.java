package com.nemustech.tiffany.world;

import android.content.Context;

/* loaded from: classes.dex */
public class TFJniUtils {
    public static native int compress(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4);

    public static native void copy_buffer(byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i, int i2, boolean z);

    public static native void copy_raw_buffer(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2, int[] iArr3, int i, int i2);

    public static native void crop_buffer(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    public static native int decompress(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4);

    public static native void extract(int i, int i2, int[] iArr, int[] iArr2, boolean z);

    public static native int get_jpeg_data_size(byte[] bArr, int i);

    public static native int get_png_data_size(byte[] bArr);

    public static native int load_jpeg_mem(byte[] bArr, int i, int[] iArr, byte[] bArr2);

    public static native int load_png_mem(byte[] bArr, int[] iArr, byte[] bArr2);

    public static native void make_byte_to_int_buffer(int[] iArr, int i, byte[] bArr, int i2);

    public static native void verifyContext(Context context);

    static {
        System.loadLibrary("tfapps");
    }
}
