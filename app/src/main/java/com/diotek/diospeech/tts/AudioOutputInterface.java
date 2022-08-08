package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public interface AudioOutputInterface {
    void startPlaying(int i) throws ResultCode;

    void waitUntilProcessed() throws ResultCode;

    void writeAudioData(short[] sArr, int i) throws ResultCode;

    /* loaded from: classes.dex */
    public static final class ResultCode extends Exception {
        private int errorCode;
        private String errorDescription;
        public static final ResultCode AUDIO_OK = new ResultCode(0, "AUDIO_OK");
        public static final ResultCode AUDIO_ERROR = new ResultCode(1, "AUDIO_ERROR");

        public ResultCode(int errorCode, String errorDescription) {
            this.errorCode = errorCode;
            this.errorDescription = errorDescription;
        }

        @Override // java.lang.Throwable
        public String toString() {
            return String.format("Error : %d (%s)", Integer.valueOf(this.errorCode), this.errorDescription);
        }
    }
}
