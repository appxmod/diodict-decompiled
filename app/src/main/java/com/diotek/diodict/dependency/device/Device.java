package com.diotek.diodict.dependency.device;

import android.content.Context;
import android.os.Build;
import com.diotek.diodict.auth.TimeLimitAuth;
import com.diotek.diodict.engine.TTSEngine;

/* loaded from: classes.dex */
public abstract class Device {
    protected boolean mContainTTS = true;

    public abstract boolean checkDeviceVendor();

    public abstract boolean checkModelName();

    public abstract String[] getInstallDBFileNames();

    public abstract String[] getInstallDBResNames();

    public abstract int[] getSupportDBResList();

    public abstract boolean isContainHandWrightReocg();

    public abstract boolean isContainNPSE();

    public abstract boolean isContainTTS();

    public abstract boolean isDBinPackage();

    public abstract boolean needResInstall();

    public static Context getContextForAddOnDicts(int nDBType) {
        return null;
    }

    public boolean checkDRM() {
        if (isDioStoreVersion()) {
            return true;
        }
        return checkDeviceVendor() && (isSamsungAppsVersion() || checkModelName() || TimeLimitAuth.checkAuthTime());
    }

    public boolean isPreload() {
        return false;
    }

    public boolean isLangChangeWithSysime() {
        return false;
    }

    public String getDBpath() {
        return "";
    }

    public String getDBfolderName() {
        return "DioDict3B/";
    }

    public String getFontName() {
        return "DioDictFnt3.ttf";
    }

    public boolean isPinyinRealTime() {
        return false;
    }

    public String getPrefixEngUSTTS() {
        return TTSEngine.getPrefixSVOX();
    }

    public String getPrefixEngUKTTS() {
        return TTSEngine.getPrefixSVOX();
    }

    public String getPrefixKorTTS() {
        return TTSEngine.getPrefixSVOX();
    }

    public String getPrefixJpnTTS() {
        return TTSEngine.getPrefixSVOX();
    }

    public String getPrefixChnTTS() {
        return TTSEngine.getPrefixSVOX();
    }

    public boolean isUseJpnKeyboard() {
        return false;
    }

    public boolean isLiteVersion() {
        return false;
    }

    public boolean isSamsungAppsVersion() {
        return false;
    }

    public boolean isDioStoreVersion() {
        return false;
    }

    public void setContainTTS(boolean bUseTTS) {
        this.mContainTTS = bUseTTS;
    }

    public boolean checkFocusableModel() {
        return Build.MODEL.equals("GT-B9120") || Build.MODEL.equals("GT-B5330");
    }
	
	public boolean useTTSDownload() {
		return false;
	}
}
