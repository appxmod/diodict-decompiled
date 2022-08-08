package com.diotek.diodict3.phone.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict3.phone.DioAuthActivity;
import com.diotek.diodict3.phone.service.IDioDictServiceMain;

/* loaded from: classes.dex */
public class DioDictServiceMain extends Service {
    public static final int CREATE_FAIL = 0;
    public static final int CREATE_SUCCESS = 1;
    public static final int DICTYPE_ENGTOKOR = 0;
    public static final int DICTYPE_KORTOENG = 1;
    public static final int MAX_KEYWORD_LENGTH = 70;
    static Context mContext;
    EngineManager3rd mEngine = null;
    boolean mIsEngineInit = false;
    IBinder mBinder = new IDioDictServiceMain.Stub() { // from class: com.diotek.diodict3.phone.service.DioDictServiceMain.1
        @Override // com.diotek.diodict3.phone.service.IDioDictServiceMain
        public int[] reqAvailableDBTypeList() throws RemoteException {
            if (Dependency.getDevice().checkDRM() && DioAuthActivity.checkAuth(DioDictServiceMain.this.getApplicationContext())) {
                int[] dbTypeList = Dependency.getDevice().getSupportDBResList();
                if (dbTypeList == null) {
                    return dbTypeList;
                }
                for (int i = 0; i < dbTypeList.length; i++) {
                    dbTypeList[i] = DictDBManager.getDictType(dbTypeList[i]).getDBType();
                }
                return dbTypeList;
            }
            return null;
        }

        @Override // com.diotek.diodict3.phone.service.IDioDictServiceMain
        public String reqDBPath() throws RemoteException {
            return DictUtils.getDBPath();
        }

        @Override // com.diotek.diodict3.phone.service.IDioDictServiceMain
        public String reqFontFileName(int dicType) throws RemoteException {
            return DictUtils.getCurrentDBFontName(dicType);
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mIsEngineInit = false;
        mContext = getApplicationContext();
        this.mEngine = EngineManager3rd.getInstance(getApplicationContext());
        Dependency.Init(mContext);
        DictDBManager.InitDBManager(mContext);
        Dependency.getVendor().initializeForService(mContext);
        DictUtils.setDBPath(mContext, false);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mIsEngineInit = false;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
