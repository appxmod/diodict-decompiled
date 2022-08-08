package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public final class ResultCode extends Exception {
    private int errorCode;
    private String errorDescription;
    public static final ResultCode OK = new ResultCode(0, "OK");
    public static final ResultCode ERROR_NOT_INITIALIZED = new ResultCode(1, "Not Initialized");
    public static final ResultCode ERROR_ALREADY_INITIALIZED = new ResultCode(2, "Already Initialized");
    public static final ResultCode ERROR_ALREADY_ACTIVE = new ResultCode(3, "Already Active");
    public static final ResultCode ERROR_NOT_ACTIVE = new ResultCode(4, "Not Active");
    public static final ResultCode ERROR_INVALID_PARAM = new ResultCode(5, "Invalid Param");
    public static final ResultCode ERROR_NO_LANGUAGE_LOADED = new ResultCode(6, "No Language Loaded");
    public static final ResultCode ERROR_BUSY = new ResultCode(7, "Busy");
    public static final ResultCode ERROR_NOT_IMPLEMENTED = new ResultCode(8, "Not Implemented");
    public static final ResultCode ERROR_INVALID_LICENSE = new ResultCode(9, "Invalid License");
    public static final ResultCode ERROR_OUT_OF_MEMORY = new ResultCode(10, "Out Of Memory");
    public static final ResultCode ERROR_RESOURCE = new ResultCode(11, "Resource");
    public static final ResultCode ERROR_INTERNAL = new ResultCode(12, "Internal Error");
    public static final ResultCode ERROR_FATAL = new ResultCode(13, "Fatal Error");
    public static final ResultCode ERROR_INVALID_LANGUAGE = new ResultCode(14, "Invalid Language");

    public ResultCode(int errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return String.format("Error : %d (%s)", Integer.valueOf(this.errorCode), this.errorDescription);
    }
}
