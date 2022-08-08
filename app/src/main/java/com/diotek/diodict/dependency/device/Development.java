package com.diotek.diodict.dependency.device;

/* loaded from: classes.dex */
public class Development extends Device {
    private int[] supportDBResList = new int[0];

    @Override // com.diotek.diodict.dependency.device.Device
    public int[] getSupportDBResList() {
        return this.supportDBResList;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean checkDeviceVendor() {
        return true;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean checkModelName() {
        return true;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean needResInstall() {
        return false;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public String[] getInstallDBFileNames() {
        return null;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public String[] getInstallDBResNames() {
        return null;
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
    public boolean isContainNPSE() {
        return false;
    }

    @Override // com.diotek.diodict.dependency.device.Device
    public boolean isContainTTS() {
        return true;
    }
}
