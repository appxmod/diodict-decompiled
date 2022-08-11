package com.diotek.diodict3.dependency;

import com.diotek.diodict.dependency.device.Device;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class SamsungChn extends Device {
    private int[] supportDBResList = {R.array.DEDT_OXFORD_FLTRP_ENG_CHN, R.array.DEDT_OXFORD_FLTRP_CHN_ENG, R.array.DEDT_OXFORD_FLTRP_PINYIN_CHN_ENG, R.array.DEDT_MANTOU_KORTOSIMP, R.array.DEDT_MANTOU_PINYIN_SIMPTOKOR, R.array.DEDT_MANTOU_SIMPTOKOR, R.array.DEDT_MANTOU_INITIAL_SIMPTOKOR, R.array.DEDT_TOTAL_SEARCH};
    private final String[] DIODICT_DB_NAME = {"OxfordEngToChnDictionaryUCS2LEFLTRPSUIDDBwH.dat", "OxfordChnToEngDictionaryUCS2LEFLTRPSUIDDBwH.dat", "MantouKorToSimpDictionaryUCS2LESUIDDBwH.dat", "MantouSimpToKorDictionaryUCS2LESUIDDBwH.dat"};
    private final String[] DIODICT_DB_RES_NAME = {"oxfordengtochndictionaryucs2lefltrpsuiddbwh", "oxfordchntoengdictionaryucs2lefltrpsuiddbwh", "mantoukortosimpdictionaryucs2lesuiddbwh", "mantousimptokordictionaryucs2lesuiddbwh"};

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean checkDeviceVendor() {
        return true;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean checkModelName() {
        return true;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public String[] getInstallDBFileNames() {
        return this.DIODICT_DB_NAME;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public int[] getSupportDBResList() {
        return this.supportDBResList;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public String[] getInstallDBResNames() {
        return this.DIODICT_DB_RES_NAME;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public String getFontName() {
        return "DioDictFntS3JC.ttf";
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean needResInstall() {
        return isDBinPackage();
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean isDBinPackage() {
        return false;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean isContainHandWrightReocg() {
        return true;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean isContainTTS() {
        return true;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean isContainNPSE() {
        return false;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean isPreload() {
        return true;
    }
}
