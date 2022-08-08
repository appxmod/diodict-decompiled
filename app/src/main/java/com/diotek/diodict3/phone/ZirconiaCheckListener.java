package com.diotek.diodict3.phone;

import com.samsung.zirconia.LicenseCheckListener;

/* loaded from: classes.dex */
public class ZirconiaCheckListener implements LicenseCheckListener {
    ZirconiaCallback mFailedCallback;
    ZirconiaCallback mSuccessCallback;

    /* loaded from: classes.dex */
    public interface ZirconiaCallback {
        void run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZirconiaCheckListener(ZirconiaCallback successCallback, ZirconiaCallback failedCallback) {
        this.mSuccessCallback = successCallback;
        this.mFailedCallback = failedCallback;
    }

    @Override // com.samsung.zirconia.LicenseCheckListener
    public void licenseCheckedAsInvalid() {
        this.mFailedCallback.run();
    }

    @Override // com.samsung.zirconia.LicenseCheckListener
    public void licenseCheckedAsValid() {
        this.mSuccessCallback.run();
    }
}
