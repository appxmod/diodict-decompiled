package com.diotek.diospeech.tts;

/* loaded from: classes.dex */
public class DioSpeechTTS {
    public static native int native_PrompterInterface_abort(int i);

    public static native int native_PrompterInterface_getParam(int i, int i2, int[] iArr);

    public static native int native_PrompterInterface_getParamEx(int i, int i2, int[] iArr);

    public static native String native_PrompterInterface_getVoice(int i);

    public static native int native_PrompterInterface_getVoiceSub(int i);

    public static native int native_PrompterInterface_playString(int i, String str, String str2);

    public static native int native_PrompterInterface_setAudioOutput(int i, AudioOutputInterface audioOutputInterface);

    public static native int native_PrompterInterface_setEventAdapter(int i, PrompterEventAdapter prompterEventAdapter);

    public static native int native_PrompterInterface_setParam(int i, int i2, int i3);

    public static native int native_PrompterInterface_setParamEx(int i, int i2, String str);

    public static native int native_PrompterInterface_setVoice(int i, String str);

    public static native int native_PrompterInterface_setVoiceEx(int i, String str, int i2, int i3);

    public static native int native_PrompterInterface_wait(int i);

    public static native int native_PrompterManager_createPrompterInstance(int i, String str, int[] iArr);

    public static native void native_PrompterManager_destroyInstance();

    public static native int native_PrompterManager_destroyPrompterInstance(int i, int i2);

    public static native int native_PrompterManager_getInstance();

    public static native int native_PrompterManager_initialize(int i, String str);

    static {
        try {
            System.loadLibrary("DioSpeechTTS-jni");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public static ResultCode convertResultCode(int code) {
        switch (code) {
            case 0:
                return ResultCode.OK;
            case 1:
                return ResultCode.ERROR_NOT_INITIALIZED;
            case 2:
                return ResultCode.ERROR_ALREADY_INITIALIZED;
            case 3:
                return ResultCode.ERROR_ALREADY_ACTIVE;
            case 4:
                return ResultCode.ERROR_NOT_ACTIVE;
            case 5:
                return ResultCode.ERROR_INVALID_PARAM;
            case 6:
                return ResultCode.ERROR_NO_LANGUAGE_LOADED;
            case 7:
                return ResultCode.ERROR_BUSY;
            case 8:
                return ResultCode.ERROR_NOT_IMPLEMENTED;
            case 9:
                return ResultCode.ERROR_INVALID_LICENSE;
            case 10:
                return ResultCode.ERROR_OUT_OF_MEMORY;
            case 11:
                return ResultCode.ERROR_RESOURCE;
            case 12:
                return ResultCode.ERROR_INTERNAL;
            case 13:
                return ResultCode.ERROR_FATAL;
            case 14:
                return ResultCode.ERROR_INVALID_LANGUAGE;
            default:
                return ResultCode.ERROR_FATAL;
        }
    }
}
