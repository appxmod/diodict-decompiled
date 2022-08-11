package com.diotek.diodict.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.diotek.diodict.auth.DioDictCheckDBModule;
import com.diodict.decompiled.R;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.StringTokenizer;

/* loaded from: classes.dex */
public class DioStoreAuth {
    public static final int AUTHCODE_ERROR_DEVICE_ID = 2;
    public static final int AUTHCODE_ERROR_PRODUCT_NUMBER = 3;
    public static final int AUTHCODE_NOT_REGISTED = 1;
    public static final int AUTHCODE_SUCCESS = 0;
    public static final String KEY_RES_COUNT = "res_count";
    public static final String KEY_RES_DBURL = "down";
    public static final String KEY_RES_MSG = "res_msg";
    public static final String KEY_RES_NO = "res_no";
    public static final String KEY_TOKEN = "token";
    private static final int KIND_PART_DIGIT = 7;
    private static final int N1ST_CHECK_DENO = 26;
    private static final int N2ND_CHECK_DENO = 25;
    private static final int N3RD_CHECK_DENO = 36;
    private static final int NUM_PART_DIGIT = 4;
    private static final int PASSWD_LEN = 4;
    public static final int RETURNCODE_EXIST_UPDATE = 7;
    public static final int RETURNCODE_FAIL_ACTIVATION = 5;
    public static final int RETURNCODE_HTTP_ERROR = 65536;
    public static final int RETURNCODE_INCORRECT_CODE = 3;
    public static final int RETURNCODE_MALFORMED_URL = 6;
    public static final int RETURNCODE_NO_AUTHCODE = 2;
    public static final int RETURNCODE_SUCCESSFUL_ACTIVATION = 1;
    public static final int RETURNCODE_THE_LATEST = 8;
    public static final int RETURNCODE_UNKNOWN_ERROR = 0;
    public static final int RUTURNCODE_ONLY_THREE_DEVICE = 4;
    private static final int ZERO_VALUE_10 = 48;
    private static final int ZERO_VALUE_26 = 65;
    DioDictCheckDBModule.AuthInitCallback mAndroidMarketAuthInitCallback;
    boolean mCallFromService;
    private Context mContext;
    private String mAuthUrl = null;
    private final String PRODUCT_KINDCODE = "DP";
    private final String mServerUrl = "http://auth.diotek.co.kr/product/DioDict3_Phone_Android_Auth.asp?";
    private final String mProductName = "DIODICT-300-DP-20110718";
    String m_kindPart_Add = "AAAAAAAAAAAAAAAAAAAAA";
    Hashtable<String, String> mResponseTable = new Hashtable<>();
    String mBuyNumber = null;
    EditText mBuyNumberEditText = null;
    DialogInterface.OnClickListener mDialogOkOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.auth.DioStoreAuth.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int whichButton) {
            DioStoreAuth.this.mBuyNumber = DioStoreAuth.this.mBuyNumberEditText.getText().toString();
            InputMethodManager imm = (InputMethodManager) DioStoreAuth.this.mContext.getSystemService("input_method");
            imm.hideSoftInputFromWindow(DioStoreAuth.this.mBuyNumberEditText.getWindowToken(), 0);
            DioStoreAuth.this.registration(DioStoreAuth.this.mContext, DioStoreAuth.this.mBuyNumber);
        }
    };
    DialogInterface.OnClickListener mDialogCancelOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.auth.DioStoreAuth.2
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int whichButton) {
            ((Activity) DioStoreAuth.this.mContext).finish();
        }
    };
    DialogInterface.OnCancelListener mDialogOnCancelListener = new DialogInterface.OnCancelListener() { // from class: com.diotek.diodict.auth.DioStoreAuth.3
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface arg0) {
            ((Activity) DioStoreAuth.this.mContext).finish();
        }
    };
    DialogInterface.OnClickListener mAuthResultOKOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.auth.DioStoreAuth.4
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            DioStoreAuth.this.mAndroidMarketAuthInitCallback.afterAuth(1, DioStoreAuth.this.mCallFromService);
        }
    };
    DialogInterface.OnClickListener mAuthResultCancelOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.auth.DioStoreAuth.5
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            DioStoreAuth.this.mAndroidMarketAuthInitCallback.afterAuth(0, DioStoreAuth.this.mCallFromService);
        }
    };
    DialogInterface.OnCancelListener mAuthResultCancelOnCancelListener = new DialogInterface.OnCancelListener() { // from class: com.diotek.diodict.auth.DioStoreAuth.6
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialog) {
            DioStoreAuth.this.mAndroidMarketAuthInitCallback.afterAuth(0, DioStoreAuth.this.mCallFromService);
        }
    };

    public DioStoreAuth(Context context, DioDictCheckDBModule.AuthInitCallback authInitCallback, boolean callFromService) {
        this.mContext = null;
        this.mCallFromService = false;
        this.mAndroidMarketAuthInitCallback = null;
        this.mContext = context;
        this.mAndroidMarketAuthInitCallback = authInitCallback;
        this.mCallFromService = callFromService;
        this.mResponseTable.clear();
    }

    public String getDeviceId() {
        String DeviceID;
        TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
        if (tm != null && (DeviceID = tm.getDeviceId()) != null) {
            return DioSerial.getEigenValue(DeviceID);
        }
        WifiManager manager = (WifiManager) this.mContext.getSystemService("wifi");
        if (manager != null) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            String MACAddress = wifiInfo.getMacAddress();
            if (MACAddress != null) {
                String macString = "";
                StringTokenizer st1 = new StringTokenizer(MACAddress, ":");
                while (st1.hasMoreTokens()) {
                    String token = st1.nextToken();
                    macString = macString + token;
                }
                return DioSerial.getEigenValue(macString);
            }
        }
        return null;
    }

    public boolean buildAuthURL(String userInputSerial, String szSyncId) {
        String userInputSerial2 = userInputSerial.trim();
        if (userInputSerial2 == null || szSyncId == null) {
            return false;
        }
        String deviceName = Build.MODEL;
        String osType = "Android" + Build.VERSION.RELEASE;
        String tmpAuthUrl = "http://auth.diotek.co.kr/product/DioDict3_Phone_Android_Auth.asp?syncid=" + szSyncId + "&devicename=" + deviceName + "&buyserial=" + userInputSerial2 + "&target=DIODICT-300-DP-20110718&OS=" + osType + "&kindcode=" + AddOnInfo.getszDicID(this.mContext) + "&kindcode1=" + AddOnInfo.getszDicID(this.mContext);
        this.mAuthUrl = tmpAuthUrl.replaceAll(" ", "%20");
        return true;
    }

    public int getNewAuthorize() {
        int responseCode = 0;
        try {
            URL url = new URL(this.mAuthUrl);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
                int size = 0;
                int count = 5;
                while (count != 0) {
                    size = httpConnection.getContentLength();
                    if (size != -1) {
                        break;
                    }
                    count--;
                }
                if (count == 0) {
                    return 0;
                }
                byte[] buf = new byte[size];
                in.read(buf);
                String returnCode = new String(buf, "US_ASCII");
                setKeyElements(returnCode);
                String authCode = this.mResponseTable.get(KEY_RES_NO.toLowerCase());
                if (authCode == null) {
                    return 3;
                }
                if (authCode.equals("0xFF")) {
                    return 1;
                }
                if (authCode.equals("0x01") || authCode.equals("03")) {
                    return 3;
                }
                if (authCode.equals("0x02")) {
                    return 4;
                }
                return 0;
            }
            return 65536 | responseCode;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 6;
        } catch (IOException e2) {
            e2.printStackTrace();
            if (responseCode != 0) {
                return 65536 | responseCode;
            }
            return 5;
        }
    }

    private boolean setKeyElements(String returnCode) {
        String key = returnCode.substring(0, "Res_No".length());
        String szSubCode = returnCode.substring("Res_No".length() + ":".length());
        String value = szSubCode.substring(0, szSubCode.indexOf("<br>"));
        String szSubCode2 = szSubCode.substring(value.length() + "<br>".length());
        if (key != null) {
            this.mResponseTable.put(key.toLowerCase().trim(), value.trim());
        }
        if (!value.equals("0xFF")) {
            return false;
        }
        String key2 = szSubCode2.substring(0, "Res_Msg".length());
        String szSubCode3 = szSubCode2.substring("Res_Msg".length() + ":".length());
        String value2 = szSubCode3.substring(0, szSubCode3.indexOf("<br>"));
        String szSubCode4 = szSubCode3.substring(value2.length() + "<br>".length());
        String value3 = value2.replaceAll("-", "");
        if (key2 != null) {
            this.mResponseTable.put(key2.toLowerCase().trim(), value3.trim());
        }
        String key3 = szSubCode4.substring(0, "Res_Count".length());
        String szSubCode5 = szSubCode4.substring("Res_Count".length() + ":".length());
        String value4 = szSubCode5.substring(0, szSubCode5.indexOf("<br>"));
        String szSubCode6 = szSubCode5.substring(value4.length() + "<br>".length());
        if (key3 != null) {
            this.mResponseTable.put(key3.toLowerCase().trim(), value4.trim());
        }
        String key4 = szSubCode6.substring(0, "DOWN".length());
        String szSubCode7 = szSubCode6.substring("DOWN".length() + ":".length());
        String value5 = szSubCode7.substring(0, szSubCode7.indexOf("<br>"));
        String szSubCode8 = szSubCode7.substring(value5.length() + "<br>".length());
        if (key4 != null) {
            this.mResponseTable.put(key4.toLowerCase().trim(), value5.trim());
        }
        String key5 = szSubCode8.substring(0, "TOKEN".length());
        String szSubCode9 = szSubCode8.substring("TOKEN".length() + ":".length());
        if (key5 != null && szSubCode9 != null) {
            this.mResponseTable.put(key5.toLowerCase().trim(), szSubCode9.trim());
        }
        return true;
    }

    public int VerifyAuth(int partNum, long dicID) {
        String szProductNum = getAuthorizeCode();
        if (szProductNum == null) {
            return 1;
        }
        String DeviceID = getDeviceId();
        if (DeviceID == null || DeviceID.length() == 0) {
            return 2;
        }
        if (szProductNum.substring(8, 10).equals("DP") && szProductNum.length() == 32) {
            this.m_kindPart_Add = szProductNum.substring(0, 1) + this.m_kindPart_Add.substring(1, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 1) + szProductNum.substring(3, 4) + this.m_kindPart_Add.substring(2, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 2) + szProductNum.substring(10, 13) + this.m_kindPart_Add.substring(3, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 5) + szProductNum.substring(17, 18) + this.m_kindPart_Add.substring(5, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 6) + szProductNum.substring(19, 22) + this.m_kindPart_Add.substring(6, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 9) + szProductNum.substring(25, 26) + this.m_kindPart_Add.substring(10, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 10) + szProductNum.substring(27, 30) + this.m_kindPart_Add.substring(11, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 13) + szProductNum.substring(13, 14) + this.m_kindPart_Add.substring(14, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 14) + szProductNum.substring(6, 8) + this.m_kindPart_Add.substring(15, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 16) + szProductNum.substring(14, 16) + this.m_kindPart_Add.substring(17, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 18) + szProductNum.substring(22, 24) + this.m_kindPart_Add.substring(19, this.m_kindPart_Add.length());
            this.m_kindPart_Add = this.m_kindPart_Add.substring(0, 20) + szProductNum.substring(30, 31) + this.m_kindPart_Add.substring(21, this.m_kindPart_Add.length());
            String _thirdPart = szProductNum.substring(1, 2) + szProductNum.substring(4, 5) + szProductNum.substring(16, 17) + szProductNum.substring(18, 19);
            String _fourthPart = szProductNum.substring(2, 3) + szProductNum.substring(5, 6) + szProductNum.substring(24, 25) + szProductNum.substring(26, 27);
            String _firstPart = this.m_kindPart_Add.substring(0, 4);
            String _secondPart = "DP" + this.m_kindPart_Add.substring(4, 6);
            return (!OKDioDict2Android(_firstPart, _secondPart, _thirdPart, _fourthPart, DeviceID) || !VerifyContent(partNum, dicID)) ? 3 : 0;
        }
        return 3;
    }

    public boolean OKDioDict2Android(String first, String second, String third, String last, String szHotsyncID) {
        int nLen = szHotsyncID.length();
        if (nLen == 0) {
            return false;
        }
        int nSumHotsync = 0;
        char[] szwFourthPart = new char[4];
        int nSum1 = DioSerial.getWeightedTotalSum(first.toCharArray(), 4) + szHotsyncID.codePointAt(1 % nLen);
        int nSum2 = DioSerial.getWeightedTotalSum(second.toCharArray(), 4) + szHotsyncID.codePointAt(0);
        int nSum3 = DioSerial.getWeightedTotalSum(third.toCharArray(), 4) + szHotsyncID.codePointAt(2 % nLen);
        for (int i = 0; i < nLen; i++) {
            nSumHotsync += szHotsyncID.codePointAt(i);
        }
        int nTmpNum = (((((nSum1 + nSum2) + nSumHotsync) + 2) % 36) * (nSum1 * nSum3)) % 36;
        if (nTmpNum < 26) {
            szwFourthPart[0] = (char) (nTmpNum + 65);
        } else {
            szwFourthPart[0] = (char) ((nTmpNum - 26) + 48);
        }
        int nTmpNum2 = ((((nSum1 + 4) % 36) * ((nSum2 + nSum3) % 36)) + 3) % 36;
        if (nTmpNum2 < 26) {
            szwFourthPart[1] = (char) (nTmpNum2 + 65);
        } else {
            szwFourthPart[1] = (char) ((nTmpNum2 - 26) + 48);
        }
        szwFourthPart[2] = (char) (((((((nSum1 % 25) * (nSum2 % 25)) * (nSum3 % 25)) + (nSumHotsync % 25)) + 6) % 25) + 65);
        szwFourthPart[3] = (char) ((nSumHotsync / 3) % 36);
        int nSum4 = DioSerial.getTotalSum(szwFourthPart, 4);
        int nTmpNum3 = (nSum4 + 8) % 36;
        if (nTmpNum3 < 26) {
            szwFourthPart[3] = (char) (nTmpNum3 + 65);
        } else {
            szwFourthPart[3] = (char) ((nTmpNum3 - 26) + 48);
        }
        String sztmpFourthPart = new String(szwFourthPart);
        if (last.equals(sztmpFourthPart)) {
            return true;
        }
        return false;
    }

    public String getStatusCode() {
        String statusCode = this.mResponseTable.get(KEY_RES_NO.toLowerCase());
        return statusCode;
    }

    public String getTokenKey() {
        String tokenKey = this.mResponseTable.get(KEY_TOKEN.toLowerCase());
        return tokenKey;
    }

    public String getResCount() {
        String resCount = this.mResponseTable.get(KEY_RES_COUNT.toLowerCase());
        return resCount;
    }

    public String getAuthCode() {
        String authCode = this.mResponseTable.get(KEY_RES_MSG.toLowerCase());
        return authCode;
    }

    public void close() {
        this.mResponseTable.clear();
        this.mResponseTable = null;
    }

    public void saveAuthorizeCode(String code) {
        DioSerial.saveAuthorizeCode(this.mContext, code);
    }

    public String getAuthorizeCode() {
        return DioSerial.getAuthorizeCode(this.mContext);
    }

    public void removeRegistration() {
        DioSerial.removeRegistration(this.mContext);
    }

    public void saveDBPathtoPreference(String szPath) {
        DioSerial.saveDBPathtoPreference(this.mContext, szPath);
    }

    public String getDBPathfromPreference() {
        return DioSerial.getDBPathfromPreference(this.mContext);
    }

    public void removeDBPathfromPreference() {
        DioSerial.removeDBPathfromPreference(this.mContext);
    }

    public void saveDBSizetoPreference(String szDBName, int nDBSize) {
        DioSerial.saveDBSizetoPreference(this.mContext, szDBName, nDBSize);
    }

    public int getDBSizefromPreference(String szDBName) {
        return DioSerial.getDBSizefromPreference(this.mContext, szDBName);
    }

    public void removeDBSizefromPreference(String szDBName) {
        DioSerial.removeDBSizefromPreference(this.mContext, szDBName);
    }

    public boolean getContainDict(long lAuthCode, long lDictCode) {
        long n = lAuthCode & lDictCode;
        return n != 0;
    }

    public String getKindPart() {
        return this.m_kindPart_Add;
    }

    public boolean VerifyContent(int partNum, long dicID) {
        String szTemp = this.m_kindPart_Add.substring((2 - partNum) * 7, ((2 - partNum) + 1) * 7);
        long lDict = Convert36NumToDecimal(szTemp.toCharArray());
        return (lDict & dicID) != 0;
    }

    public long Convert36NumToDecimal(char[] psz36Num) {
        long lDec = 0;
        long lMultiplier = 1;
        for (int i = psz36Num.length - 1; i >= 0; i--) {
            long curValue = psz36Num[i] >= 'A' ? psz36Num[i] - 'A' : (psz36Num[i] - '0') + 26;
            lDec += curValue * lMultiplier;
            lMultiplier *= 36;
        }
        return lDec;
    }

    public Dialog onCreateDialog(Context context, int id) {
        LayoutInflater factory = LayoutInflater.from(context);
        View textEntryView = factory.inflate(R.layout.alert_dialog_code_entry, (ViewGroup) null);
        this.mBuyNumberEditText = (EditText) textEntryView.findViewById(R.id.productnum_edit);
        return new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(context.getString(R.string.authentication_title)).setView(textEntryView).setPositiveButton(context.getString(R.string.ok), this.mDialogOkOnClickListener).setNegativeButton(context.getString(R.string.cancel), this.mDialogCancelOnClickListener).setOnCancelListener(this.mDialogOnCancelListener).create();
    }

    public void registration(Context context, String buyNumber) {
        String errorMsg;
        if (buildAuthURL(buyNumber, getDeviceId())) {
            int resultCode = getNewAuthorize();
            switch (resultCode) {
                case 1:
                    String authCode = getAuthCode();
                    saveAuthorizeCode(authCode);
                    int ret = VerifyAuth(AddOnInfo.getnPartNum(context), AddOnInfo.getlDicID(context));
                    switch (ret) {
                        case 0:
                            String szSuccess = context.getString(R.string.http_error);
                            new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(context.getString(R.string.Warning)).setMessage(szSuccess.replace("%s", getResCount())).setPositiveButton(R.string.ok, this.mAuthResultOKOnClickListener).create().show();
                            return;
                        case 1:
                        default:
                            errorMsg = context.getString(R.string.unknown_error);
                            break;
                        case 2:
                            errorMsg = context.getString(R.string.msg_empty_deviceid);
                            break;
                        case 3:
                            String tmperrorMsg = context.getString(R.string.auth_product_code_fail);
                            errorMsg = tmperrorMsg.replace("%s", new Integer(65535 & resultCode).toString());
                            break;
                    }
                case 2:
                case 3:
                case 6:
                    String tmperrorMsg2 = context.getString(R.string.auth_product_code_fail);
                    errorMsg = tmperrorMsg2.replace("%s", new Integer(65535 & resultCode).toString());
                    break;
                case 4:
                    errorMsg = context.getString(R.string.regist_only_three_device);
                    break;
                case 5:
                    errorMsg = context.getString(R.string.regist_connect_fail);
                    break;
                default:
                    if ((65536 & resultCode) != 0) {
                        errorMsg = context.getString(R.string.http_error) + new Integer(65535 & resultCode).toString();
                        break;
                    } else {
                        errorMsg = context.getString(R.string.unknown_error);
                        break;
                    }
            }
        } else {
            errorMsg = context.getString(R.string.msg_empty_deviceid);
        }
        AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
        aDialog.setIcon(R.drawable.alert_dialog_icon);
        aDialog.setTitle(context.getString(R.string.Warning));
        aDialog.setMessage(errorMsg);
        aDialog.setPositiveButton(R.string.ok, this.mAuthResultCancelOnClickListener);
        aDialog.setOnCancelListener(this.mAuthResultCancelOnCancelListener);
        aDialog.create();
        aDialog.show();
    }
}
