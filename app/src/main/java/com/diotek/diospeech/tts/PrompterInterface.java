package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public class PrompterInterface {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$diotek$diospeech$tts$TTSGenderType;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$diotek$diospeech$tts$TTSParamId;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$diotek$diospeech$tts$TTSQualityLevel;
    protected int interfacePt;
    protected int audioPt = 0;
    protected int adapterPt = 0;

    static /* synthetic */ int[] $SWITCH_TABLE$com$diotek$diospeech$tts$TTSGenderType() {
        int[] iArr = $SWITCH_TABLE$com$diotek$diospeech$tts$TTSGenderType;
        if (iArr == null) {
            iArr = new int[TTSGenderType.valuesCustom().length];
            try {
                iArr[TTSGenderType.FEMALE.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[TTSGenderType.GENDER_NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[TTSGenderType.MALE.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$diotek$diospeech$tts$TTSGenderType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$diotek$diospeech$tts$TTSParamId() {
        int[] iArr = $SWITCH_TABLE$com$diotek$diospeech$tts$TTSParamId;
        if (iArr == null) {
            iArr = new int[TTSParamId.valuesCustom().length];
            try {
                iArr[TTSParamId.PITCH.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[TTSParamId.POLYREAD.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[TTSParamId.SPEED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[TTSParamId.VOLUME.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$diotek$diospeech$tts$TTSParamId = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$diotek$diospeech$tts$TTSQualityLevel() {
        int[] iArr = $SWITCH_TABLE$com$diotek$diospeech$tts$TTSQualityLevel;
        if (iArr == null) {
            iArr = new int[TTSQualityLevel.valuesCustom().length];
            try {
                iArr[TTSQualityLevel.CORPORATE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[TTSQualityLevel.PLUS.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[TTSQualityLevel.PRO.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[TTSQualityLevel.QUALITY_NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[TTSQualityLevel.STANDART.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$diotek$diospeech$tts$TTSQualityLevel = iArr;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PrompterInterface(int cPtr) {
        this.interfacePt = 0;
        this.interfacePt = cPtr;
    }

    public void setVoice(String voiceName) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_setVoice(this.interfacePt, voiceName);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void setVoice(String voiceName, TTSGenderType genderType) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_setVoiceEx(this.interfacePt, voiceName, convertTTSGenderType(genderType), convertTTSQualityLevel(TTSQualityLevel.QUALITY_NONE));
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void setVoice(String voiceName, TTSQualityLevel qualityLevel) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_setVoiceEx(this.interfacePt, voiceName, convertTTSGenderType(TTSGenderType.GENDER_NONE), convertTTSQualityLevel(qualityLevel));
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void setVoice(String voiceName, TTSGenderType genderType, TTSQualityLevel qualityLevel) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_setVoiceEx(this.interfacePt, voiceName, convertTTSGenderType(genderType), convertTTSQualityLevel(qualityLevel));
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public String getVoice() throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_getVoiceSub(this.interfacePt);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
        return DioSpeechTTS.native_PrompterInterface_getVoice(this.interfacePt);
    }

    public void setParam(TTSParamId ttsParamId, int culValue) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_setParam(this.interfacePt, convertTTSParamId(ttsParamId), culValue);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void setParamEx(TTSParamId ttsParamId, String culValue) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_setParamEx(this.interfacePt, convertTTSParamId(ttsParamId), culValue);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void getParam(TTSParamId ttsParamId, int[] value) throws ResultCode {
        if (value.length != 1) {
            throw ResultCode.ERROR_INVALID_PARAM;
        }
        int idx = DioSpeechTTS.native_PrompterInterface_getParam(this.interfacePt, convertTTSParamId(ttsParamId), value);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void getParamEx(TTSParamId ttsParamId, int[] values) throws ResultCode {
        if (values.length != 4) {
            throw ResultCode.ERROR_INVALID_PARAM;
        }
        int idx = DioSpeechTTS.native_PrompterInterface_getParamEx(this.interfacePt, convertTTSParamId(ttsParamId), values);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void playString(String message) throws ResultCode {
        playString(message, "DEFAULT");
    }

    public void playString(String message, String context) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_playString(this.interfacePt, message, context);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void abort() throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_abort(this.interfacePt);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void waitForPlaying() throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterInterface_wait(this.interfacePt);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void setAudioOutput(AudioOutputInterface audioInterface) throws ResultCode {
        int idx = -1;
        if (this.interfacePt != 0 && audioInterface != null) {
            idx = DioSpeechTTS.native_PrompterInterface_setAudioOutput(this.interfacePt, audioInterface);
        }
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public void setEventAdapter(PrompterEventAdapter eventAdapter) throws ResultCode {
        int idx = -1;
        if (this.interfacePt != 0 && eventAdapter != null) {
            idx = DioSpeechTTS.native_PrompterInterface_setEventAdapter(this.interfacePt, eventAdapter);
        }
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    private int convertTTSParamId(TTSParamId param) {
        switch ($SWITCH_TABLE$com$diotek$diospeech$tts$TTSParamId()[param.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            default:
                return -1;
        }
    }

    private int convertTTSGenderType(TTSGenderType genderType) {
        switch ($SWITCH_TABLE$com$diotek$diospeech$tts$TTSGenderType()[genderType.ordinal()]) {
            case 1:
            default:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
        }
    }

    private int convertTTSQualityLevel(TTSQualityLevel qualityLevel) {
        switch ($SWITCH_TABLE$com$diotek$diospeech$tts$TTSQualityLevel()[qualityLevel.ordinal()]) {
            case 1:
            default:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
        }
    }
}
