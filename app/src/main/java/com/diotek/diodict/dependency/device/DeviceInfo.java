package com.diotek.diodict.dependency.device;

import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.mean.MSG;

/* loaded from: classes.dex */
public class DeviceInfo {
    public static Device mDevice = null;

    public static void initDevice() {
        if (mDevice != null) {
            MSG.l(2, "DIODICT3_for_Android DeviceInfor need to be initialized");
        }
        mDevice = null;
    }

    public static Device return_Device() {
        if (mDevice == null) {
            try {
                Class<?> cs = Class.forName(Dependency.getDeviceName());
                mDevice = (Device) cs.newInstance();
            } catch (ClassNotFoundException e) {
                mDevice = null;
            } catch (IllegalAccessException e2) {
                mDevice = null;
            } catch (InstantiationException e3) {
                mDevice = null;
            }
            if (mDevice == null) {
                MSG.l(2, "DIODICT3_for_Android Class instance not initialized and can't get device name\nDeviceName=" + Dependency.getDeviceName());
                System.runFinalizersOnExit(true);
                System.exit(0);
            }
        }
        return mDevice;
    }
}
