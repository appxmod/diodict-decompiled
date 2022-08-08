package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFError {
    private final int mErrorCode;
    public static final TFError ERROR_NONE = new TFError(0);
    public static final TFError INVALID_PARAM = new TFError(1);
    public static final TFError ALREADY_ATTACHED = new TFError(2);
    public static final TFError NOT_ATTACHED = new TFError(3);
    public static final TFError OUT_OF_INDEX = new TFError(4);

    private TFError(int error_code) {
        this.mErrorCode = error_code;
    }

    public int toInt() {
        return this.mErrorCode;
    }
}
