package com.diotek.diodict.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Spanned;
import android.util.Log;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.EngineNative3rd;
import com.diotek.diodict.engine.WordList3rd;
import com.diotek.diodict.mean.TagConverter;
import com.diotek.diodict.service.IServiceForReadersHub;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ServiceForReadersHub extends Service {
    public static final int CREATE_FAIL = 0;
    public static final int CREATE_SUCCESS = 1;
    private static final int DEFAULT_WORD_POS = 50;
    private static final String INTENT_EXTRA_DICTIONARY_NAME = "dic_type";
    private static final String INTENT_EXTRA_WORD_NAME = "search_word";
    private static final String INTENT_EXTRA_WORD_SUID = "search_suid";
    private static final String INTENT_MODE_NAME = "display_mode";
    private static final String INTENT_MODE_VALUE = "display_mode_view";
    int mKeywordPosition;
    String mResultMean;
    WordList3rd mResultWord;
    int[] mSuidList;
    TagConverter mTagConverter;
    ArrayList<String> mWordList;
    int mWordListCount;
    public final int THEME_DEFAULT = 0;
    Bitmap mBitmap = null;
    EngineManager3rd mEngine = null;
    boolean mIsEngineInit = false;
    int mDicType = -1;
    IBinder mBinder = new IServiceForReadersHub.Stub() { // from class: com.diotek.diodict.service.ServiceForReadersHub.1
        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public String getKeyword() throws RemoteException {
            if (ServiceForReadersHub.this.mResultWord != null) {
                return ServiceForReadersHub.this.mResultWord.getKeyword();
            }
            return null;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public int getSuid() throws RemoteException {
            if (ServiceForReadersHub.this.mResultWord != null) {
                return ServiceForReadersHub.this.mResultWord.getSUID();
            }
            return 0;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public boolean changeDicType(int dictype) {
            ServiceForReadersHub.this.mEngine.setCurDict(dictype);
            return true;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public boolean isDBExisting() throws RemoteException {
            Integer[] list = null;
            if (ServiceForReadersHub.this.mEngine != null) {
                list = ServiceForReadersHub.this.mEngine.getSupportDictionary();
            }
            return list != null && list.length > 0;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public boolean SetSearchList(String word, int dictype) throws RemoteException {
            if (!ServiceForReadersHub.this.mIsEngineInit) {
                Log.e("DioDict", "SetSearchList Engine Fail");
                return false;
            }
            if (word == null) {
                word = "";
            }
            ServiceForReadersHub.this.mEngine.setCurDict(dictype);
            EngineManager3rd.SearchMethodInfo[] searchMethodInfo = ServiceForReadersHub.this.mEngine.getSupportSearchMethodInfo();
            ServiceForReadersHub.this.mEngine.setSearchMethod(searchMethodInfo[0]);
            if (!ServiceForReadersHub.this.mEngine.searchByCheckWildChar(word, word, 2)) {
                return false;
            }
            ServiceForReadersHub.this.mWordListCount = ServiceForReadersHub.this.mEngine.getResultListCount(2);
            if (ServiceForReadersHub.this.mWordListCount <= 0) {
                return false;
            }
            ServiceForReadersHub.this.mKeywordPosition = ServiceForReadersHub.this.mEngine.getResultListKeywordPos(2);
            ServiceForReadersHub.this.mWordList = new ArrayList<>();
            ServiceForReadersHub.this.mSuidList = new int[ServiceForReadersHub.this.mWordListCount + 1];
            for (int i = 0; i < ServiceForReadersHub.this.mWordListCount; i++) {
                ServiceForReadersHub.this.mWordList.add(ServiceForReadersHub.this.mEngine.getResultListKeywordByPos(i, 2));
                ServiceForReadersHub.this.mSuidList[i] = ServiceForReadersHub.this.mEngine.getResultListSUIDByPos(i, 2);
            }
            ServiceForReadersHub.this.mResultWord = new WordList3rd(ServiceForReadersHub.this.mWordList.get(ServiceForReadersHub.this.mKeywordPosition), ServiceForReadersHub.this.mSuidList[ServiceForReadersHub.this.mKeywordPosition], dictype);
            return true;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public int[] getSuidList() throws RemoteException {
            return ServiceForReadersHub.this.mSuidList;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public List<String> getWordList() throws RemoteException {
            return ServiceForReadersHub.this.mWordList;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public int getKeywordPosition() throws RemoteException {
            return ServiceForReadersHub.this.mKeywordPosition;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public String getWordMeanWithSUID(int suid, int dicType) throws RemoteException {
            String keyword = null;
            int i = 0;
            while (true) {
                if (i >= ServiceForReadersHub.this.mSuidList.length) {
                    break;
                } else if (ServiceForReadersHub.this.mSuidList[i] != suid) {
                    i++;
                } else {
                    String keyword2 = ServiceForReadersHub.this.mWordList.get(i);
                    keyword = keyword2;
                    break;
                }
            }
            return getMean(dicType, keyword, suid);
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public String getFontPath() throws RemoteException {
            String sdPath = DictInfo.DIODICT_EXTERNAL_PATH + Dependency.getDevice().getDBfolderName();
            String curFontPath = DictUtils.getFontFullPath();
            if (curFontPath.contains("/data/data/")) {
                if (ServiceForReadersHub.copyFontResToSdcard(curFontPath, sdPath, DictInfo.DIODICT_FONT_NAME)) {
                    return sdPath + DictInfo.DIODICT_FONT_NAME;
                }
                return null;
            }
            return curFontPath;
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public String getWordMean(String word, int dicType) throws RemoteException {
            SetSearchList(word, dicType);
            return getMean(dicType, ServiceForReadersHub.this.mResultWord.m_dcKeyword, ServiceForReadersHub.this.mResultWord.m_dcUID);
        }

        private String getMean(int dictType, String keyword, int suid) {
            if (ServiceForReadersHub.this.mTagConverter != null) {
                ServiceForReadersHub.this.mTagConverter = null;
            }
            if (keyword == null || suid == 0) {
                return null;
            }
            ServiceForReadersHub.this.mTagConverter = new TagConverter(null, ServiceForReadersHub.this.mEngine, 0, null);
            ServiceForReadersHub.this.mTagConverter.loadMeaning(dictType, keyword, suid, 0);
            Spanned meaningSpanned = ServiceForReadersHub.this.mTagConverter.getMeanFieldSpan();
            return meaningSpanned.toString();
        }

        @Override // com.diotek.diodict.service.IServiceForReadersHub
        public boolean isDBExactMatching() throws RemoteException {
            return ServiceForReadersHub.this.mEngine.getResultList(2).isBExactmatch();
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (!Dependency.isInit()) {
            Dependency.Init(this);
        }
        if (!DictDBManager.isInitDBManager()) {
            DictDBManager.InitDBManager(this);
            DictDBManager.setUseDBbyResID(Dependency.getDevice().getSupportDBResList());
        }
        if (this.mEngine == null) {
            this.mEngine = EngineManager3rd.getInstance(getApplicationContext());
        }
        if (!EngineNative3rd.LibIsValidDBHandle()) {
            this.mEngine.setSupportDictionary();
            if (!this.mEngine.initNativeEngine(getApplicationContext())) {
                return null;
            }
        }
        this.mIsEngineInit = true;
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        this.mIsEngineInit = false;
        this.mDicType = -1;
        return super.onUnbind(intent);
    }

    public static void setOpenWord(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String display_mode = bundle.getString(INTENT_MODE_NAME);
        if (display_mode != null && display_mode.equals(INTENT_MODE_VALUE)) {
            String searchWord = bundle.getString(INTENT_EXTRA_WORD_NAME);
            int Suid = bundle.getInt(INTENT_EXTRA_WORD_SUID, 0);
            int dicType = bundle.getInt(INTENT_EXTRA_DICTIONARY_NAME, -1);
            if (searchWord != null && Suid > 0) {
                DictUtils.setSearchLastSearchInfoToPreference(context, dicType, 1, searchWord, 50);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0033 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean copyFontResToSdcard(java.lang.String r12, java.lang.String r13, java.lang.String r14) {
        /*
            r11 = 1000(0x3e8, float:1.401E-42)
            r9 = 0
            r4 = 0
            r0 = 0
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch: java.io.IOException -> La9 java.lang.Throwable -> Lbf
            r5.<init>(r12)     // Catch: java.io.IOException -> La9 java.lang.Throwable -> Lbf
            if (r5 == 0) goto Ld1
            int r10 = r5.available()     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
            if (r10 >= 0) goto Ld1
            r5.close()     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
            r3.<init>(r12)     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
            r3.delete()     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
            r4.<init>(r12)     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lce
        L22:
            int r0 = r4.available()     // Catch: java.io.IOException -> La9 java.lang.Throwable -> Lbf
            if (r0 >= 0) goto L33
            if (r4 == 0) goto L2d
            r4.close()     // Catch: java.io.IOException -> L2e
        L2d:
            return r9
        L2e:
            r2 = move-exception
            r2.printStackTrace()
            goto L2d
        L33:
            boolean r10 = com.diotek.diodict.engine.ResInstall.IsAvailableSaveToInternalStorage(r0)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            if (r10 != 0) goto L44
            if (r4 == 0) goto L2d
            r4.close()     // Catch: java.io.IOException -> L3f
            goto L2d
        L3f:
            r2 = move-exception
            r2.printStackTrace()
            goto L2d
        L44:
            r10 = 1000(0x3e8, float:1.401E-42)
            byte[] r7 = new byte[r10]     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            java.io.File r1 = new java.io.File     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            r1.<init>(r13)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            if (r1 == 0) goto L58
            boolean r10 = r1.isDirectory()     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            if (r10 != 0) goto L58
            r1.mkdirs()     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
        L58:
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            r10.<init>()     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            java.lang.StringBuilder r10 = r10.append(r14)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            java.lang.String r10 = r10.toString()     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            r6.<init>(r10)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
        L6e:
            if (r0 <= 0) goto L98
            if (r0 >= r11) goto L7d
            byte[] r8 = new byte[r0]     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            r4.read(r8)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            r6.write(r8)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
        L7a:
            int r0 = r0 + (-1000)
            goto L6e
        L7d:
            r4.read(r7)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            r6.write(r7)     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
            goto L7a
        L84:
            r2 = move-exception
            r2.printStackTrace()     // Catch: java.io.IOException -> La9 java.lang.Throwable -> Lbf
            java.lang.String r10 = "copyFontResToSdcard ERR"
            android.util.Log.e(r10, r13)     // Catch: java.io.IOException -> La9 java.lang.Throwable -> Lbf
            if (r4 == 0) goto L2d
            r4.close()     // Catch: java.io.IOException -> L93
            goto L2d
        L93:
            r2 = move-exception
            r2.printStackTrace()
            goto L2d
        L98:
            if (r6 == 0) goto L9d
            r6.close()     // Catch: java.io.IOException -> L84 java.lang.Throwable -> Lbf
        L9d:
            if (r4 == 0) goto La2
            r4.close()     // Catch: java.io.IOException -> La4
        La2:
            r9 = 1
            goto L2d
        La4:
            r2 = move-exception
            r2.printStackTrace()
            goto La2
        La9:
            r2 = move-exception
        Laa:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> Lbf
            java.lang.String r10 = "copyFontResToSdcard ERR"
            android.util.Log.e(r10, r12)     // Catch: java.lang.Throwable -> Lbf
            if (r4 == 0) goto L2d
            r4.close()     // Catch: java.io.IOException -> Lb9
            goto L2d
        Lb9:
            r2 = move-exception
            r2.printStackTrace()
            goto L2d
        Lbf:
            r9 = move-exception
        Lc0:
            if (r4 == 0) goto Lc5
            r4.close()     // Catch: java.io.IOException -> Lc6
        Lc5:
            throw r9
        Lc6:
            r2 = move-exception
            r2.printStackTrace()
            goto Lc5
        Lcb:
            r9 = move-exception
            r4 = r5
            goto Lc0
        Lce:
            r2 = move-exception
            r4 = r5
            goto Laa
        Ld1:
            r4 = r5
            goto L22
        */
        throw new UnsupportedOperationException("Method not decompiled: com.diotek.diodict.service.ServiceForReadersHub.copyFontResToSdcard(java.lang.String, java.lang.String, java.lang.String):boolean");
    }
}
