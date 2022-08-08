package com.samsung.zirconia;

import android.app.Activity;
import android.util.Log;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import java.io.File;

/* loaded from: classes.dex */
public class Zirconia {
    public static final int EZIRCONIA_APPLICATION_MODIFIED = 82;
    public static final int EZIRCONIA_CANNOT_CHECK = 31;
    public static final int EZIRCONIA_CLIENT_MISMATCH = 21;
    public static final int EZIRCONIA_INVALID_VALUE = 23;
    public static final int EZIRCONIA_KEY_CREATION_FAILED = 81;
    public static final int EZIRCONIA_LICENSE_MISMATCH = 50;
    public static final int EZIRCONIA_NOT_PURCHASED = 11;
    public static final int EZIRCONIA_RECEIVE_FAILED = 61;
    public static final int EZIRCONIA_SEND_FAILED = 62;
    public static final int EZIRCONIA_SERVER_MISMATCH = 71;
    public static final int EZIRCONIA_SUCCESS = 0;
    public static final int EZIRCONIA_VERSION_MISMATCH = 22;
    private String applicationID;
    private Activity currentActivity;
    private String deviceIMEI;
    private String deviceIMSI;
    private String deviceMIN;
    private String deviceModel;
    private boolean isEmulator;
    private String licenseFilePath;
    private LicenseCheckListener licenseCheckListener = null;
    private boolean isApplicationHacked = false;
    private int threadPriority = 5;
    private int zirconiaError = 0;
    private boolean checkLocalOnly = false;
    private boolean isWorking = false;

    public Zirconia(Activity activity) {
        this.currentActivity = activity;
        DevInfoRetriever devInfoRetriever = new DevInfoRetriever(this.currentActivity);
        this.isEmulator = devInfoRetriever.isEmulator();
        this.applicationID = activity.getPackageName();
        this.deviceIMEI = devInfoRetriever.getIMEI();
        this.deviceIMSI = devInfoRetriever.getIMSI();
        this.deviceModel = devInfoRetriever.getModel();
        this.deviceMIN = devInfoRetriever.getMIN();
        File fileNewDir = activity.getDir("zirconia", 0);
        String newDir = fileNewDir.getAbsolutePath();
        this.licenseFilePath = String.valueOf(newDir) + "/zirconia.dat";
    }

    public void setLicenseCheckListener(LicenseCheckListener listener) {
        this.licenseCheckListener = listener;
    }

    public void setThreadPriority(int newPriority) {
        if (newPriority < 1) {
            newPriority = 1;
        }
        if (newPriority < 10) {
            newPriority = 1;
        }
        this.threadPriority = newPriority;
    }

    public void setBogusIMEI(String bogusIMEI) {
        if (this.isEmulator) {
            this.deviceIMEI = bogusIMEI;
        }
    }

    public void checkLicense(boolean checkLocalOnly, boolean dontUseThread) {
        if (!isWorking()) {
            this.isWorking = true;
            this.checkLocalOnly = checkLocalOnly;
            CheckerRunnable checkerRunnable = new CheckerRunnable(this, null);
            if (dontUseThread) {
                checkerRunnable.run();
                return;
            }
            Thread checkerThread = new Thread(checkerRunnable);
            checkerThread.setPriority(this.threadPriority);
            checkerThread.start();
        }
    }

    public boolean deleteLicense() {
        File file = new File(this.licenseFilePath);
        if (!file.exists()) {
            return false;
        }
        boolean isSuccess = file.delete();
        return isSuccess;
    }

    public boolean isWorking() {
        return this.isWorking;
    }

    public int getError() {
        return this.zirconiaError;
    }

    public ZirconiaVersion version() {
        return new ZirconiaVersion(1, DHWR.DLANG_HINDI, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CheckerRunnable implements Runnable {
        private CheckerRunnable() {
        }

        /* synthetic */ CheckerRunnable(Zirconia zirconia, CheckerRunnable checkerRunnable) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            checkerThreadWorker();
        }

        void checkerThreadWorker() {
            boolean unlocked = false;
            Zirconia.this.zirconiaError = 11;
            if (!checkLicenseFile()) {
                if (!Zirconia.this.checkLocalOnly) {
                    String packageCode = Zirconia.this.currentActivity.getPackageCodePath();
                    LicenseRetriever licenseRetriever = new LicenseRetriever(Zirconia.this.deviceIMEI, Zirconia.this.applicationID, Zirconia.this.deviceIMSI, Zirconia.this.deviceModel, Zirconia.this.deviceMIN, Zirconia.this.licenseFilePath, packageCode);
                    Zirconia.this.zirconiaError = licenseRetriever.retrieveLicense();
                    if (Zirconia.this.zirconiaError == 50 && checkLicenseFile()) {
                        Zirconia.this.zirconiaError = 0;
                        unlocked = true;
                    }
                }
            } else if (checkLicenseFilePhase2()) {
                Zirconia.this.zirconiaError = 0;
                unlocked = true;
            } else {
                Zirconia.this.zirconiaError = 82;
                Zirconia.this.isApplicationHacked = true;
            }
            if (unlocked) {
                if (Zirconia.this.licenseCheckListener != null) {
                    Zirconia.this.licenseCheckListener.licenseCheckedAsValid();
                }
            } else if (Zirconia.this.licenseCheckListener != null) {
                Zirconia.this.licenseCheckListener.licenseCheckedAsInvalid();
            }
            Zirconia.this.isWorking = false;
        }

        boolean checkLicenseFile() {
            return NativeInterface.checkLicenseFile(Zirconia.this.licenseFilePath, Zirconia.this.deviceIMEI, Zirconia.this.applicationID);
        }

        boolean checkLicenseFilePhase2() {
            String packageCode = Zirconia.this.currentActivity.getPackageCodePath();
            return NativeInterface.checkLicenseFile2(Zirconia.this.licenseFilePath, packageCode);
        }
    }

    public void doVariablesTest() {
        Log.d("Zirconia", "isEmulator: " + this.isEmulator);
        Log.d("Zirconia", "isApplicationHacked: " + this.isApplicationHacked);
        Log.d("Zirconia", "threadPriority :" + this.threadPriority);
        Log.d("Zirconia", "zirconiaError :" + this.zirconiaError);
        Log.d("Zirconia", "checkLocalOnly :" + this.checkLocalOnly);
        Log.d("Zirconia", "applicationID :" + this.applicationID);
        Log.d("Zirconia", "deviceIMEI :" + this.deviceIMEI);
        Log.d("Zirconia", "deviceIMSI :" + this.deviceIMSI);
        Log.d("Zirconia", "deviceModel :" + this.deviceModel);
        Log.d("Zirconia", "deviceMIN :" + this.deviceMIN);
        Log.d("Zirconia", "licenseFilePath :" + this.licenseFilePath);
    }
}
