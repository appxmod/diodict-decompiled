package com.diotek.diospeech.tts;

import java.util.HashMap;

/* loaded from: classes.dex */
public class PrompterManager {
    protected static int managerPt = 0;
    private static PrompterManager prompterManager;
    private HashMap<PrompterInterface, Integer> instanceMap = new HashMap<>();

    protected PrompterManager() {
    }

    public PrompterInterface createPrompterInstance(String instName) throws ResultCode {
        if (this.instanceMap == null) {
            throw ResultCode.ERROR_FATAL;
        }
        int[] interfacePt = new int[1];
        int idx = DioSpeechTTS.native_PrompterManager_createPrompterInstance(managerPt, instName, interfacePt);
        if (idx == 0 && interfacePt[0] != 0) {
            PrompterInterface instance = new PrompterInterface(interfacePt[0]);
            this.instanceMap.put(instance, Integer.valueOf(interfacePt[0]));
            return instance;
        }
        throw DioSpeechTTS.convertResultCode(idx);
    }

    public void destroyPrompterInstance(PrompterInterface promIfHandle) throws ResultCode {
        if (promIfHandle == null) {
            throw ResultCode.ERROR_INVALID_PARAM;
        }
        Integer interfacePt = this.instanceMap.remove(promIfHandle);
        if (interfacePt == null || interfacePt.intValue() == 0) {
            throw ResultCode.ERROR_INVALID_PARAM;
        }
        DioSpeechTTS.native_PrompterManager_destroyPrompterInstance(managerPt, interfacePt.intValue());
    }

    public void initialize(String resourcePath) throws ResultCode {
        int idx = DioSpeechTTS.native_PrompterManager_initialize(managerPt, resourcePath);
        if (idx != 0) {
            throw DioSpeechTTS.convertResultCode(idx);
        }
    }

    public static PrompterManager getInstance() {
        if (prompterManager == null) {
            prompterManager = new PrompterManager();
            managerPt = DioSpeechTTS.native_PrompterManager_getInstance();
        }
        return prompterManager;
    }

    public static void destroyInstance() {
        if (prompterManager != null) {
            prompterManager = null;
            DioSpeechTTS.native_PrompterManager_destroyInstance();
        }
    }
}
