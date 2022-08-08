package com.diotek.diodict.auth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;
import com.diotek.diodict.KindOfMarket;
import com.diotek.diodict.auth.service.IDioDictCheckDBCallback;
import com.diotek.diodict3.phone.samsung.chn.R;

/* loaded from: classes.dex */
public class DioDictCheckDBModule {
    public static RemoteCallbackList<IDioDictCheckDBCallback> mCallback = new RemoteCallbackList<>();
    static IDioDictCheckDBCallback mCheckDBCallback = null;
    static DioStoreAuth mDioStoreAuth = null;
    public static AuthInitCallback mAuthInitCallback = new AuthInitCallback() { // from class: com.diotek.diodict.auth.DioDictCheckDBModule.1
        @Override // com.diotek.diodict.auth.DioDictCheckDBModule.AuthInitCallback
        public boolean afterAuth(int nRes, boolean callFromService) {
            if (callFromService) {
                int callbacks = DioDictCheckDBModule.mCallback.beginBroadcast();
                for (int i = 0; i < callbacks; i++) {
                    try {
                        DioDictCheckDBModule.mCallback.getBroadcastItem(i).afterAuthInit(nRes);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                DioDictCheckDBModule.mCallback.finishBroadcast();
                return false;
            }
            try {
                DioDictCheckDBModule.mCheckDBCallback.afterAuthInit(nRes);
                return false;
            } catch (RemoteException e2) {
                e2.printStackTrace();
                return false;
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface AuthInitCallback {
        boolean afterAuth(int i, boolean z);
    }

    public static void setCheckDBCallback(IDioDictCheckDBCallback checkDBCallback) {
        mCheckDBCallback = checkDBCallback;
    }

    public static boolean innerStartBuyDB(Context context, boolean callFromService) {
        return startDioStoreAuth(context, callFromService);
    }

    public static boolean startDioStoreAuth(Context context, boolean callFromService) {
        mDioStoreAuth = new DioStoreAuth(context, mAuthInitCallback, callFromService);
        int ret = mDioStoreAuth.VerifyAuth(AddOnInfo.getnPartNum(context), AddOnInfo.getlDicID(context));
        switch (ret) {
            case 0:
                mAuthInitCallback.afterAuth(1, callFromService);
                return true;
            case 1:
            case 3:
                if (callFromService) {
                    mAuthInitCallback.afterAuth(0, callFromService);
                    break;
                } else {
                    ((Activity) context).showDialog(0);
                    break;
                }
            case 2:
                if (!callFromService) {
                    Toast.makeText(context, context.getString(R.string.msg_empty_deviceid), 1);
                }
                mAuthInitCallback.afterAuth(0, callFromService);
                break;
            default:
                mAuthInitCallback.afterAuth(0, callFromService);
                break;
        }
        return false;
    }

    public static Dialog onCreateDialog(Context context, int id) {
        switch (KindOfMarket.mKindOfMarket) {
            case 2:
                return mDioStoreAuth.onCreateDialog(context, id);
            default:
                return null;
        }
    }
}
