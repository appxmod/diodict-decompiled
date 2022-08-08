package com.diotek.diodict.dependency.vendor;

import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.mean.MSG;

/* loaded from: classes.dex */
public class VendorInfo {
    public static Vendor mVendor = null;

    public static void initVendor() {
        if (mVendor != null) {
            MSG.l(2, "DIODICT3_for_Android VenderInfo need to be initialized");
        }
        mVendor = null;
    }

    public static Vendor return_Vendor() {
        if (mVendor == null) {
            try {
                Class<?> cs = Class.forName(Dependency.getVendorName());
                mVendor = (Vendor) cs.newInstance();
            } catch (ClassNotFoundException e) {
                mVendor = null;
            } catch (IllegalAccessException e2) {
                mVendor = null;
            } catch (InstantiationException e3) {
                mVendor = null;
            }
            if (mVendor == null) {
                MSG.l(2, "DIODICT3_for_Android Class instance not initialized and can't get vender name\nVenderName=" + Dependency.getVendorName());
                System.runFinalizersOnExit(true);
                System.exit(0);
            }
        }
        return mVendor;
    }
}
