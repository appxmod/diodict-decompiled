package com.diotek.diodict3.phone.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.Spanned;
import android.util.Log;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.EngineNative3rd;
import com.diotek.diodict.engine.ThemeColor;
import com.diotek.diodict.mean.TagConverter;
import com.diotek.diodict3.phone.service.IDioDictService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class DioDictService extends Service {
    public static final String KEY1_IsMatching = "match";
    public static final String KEY1_List = "List";
    public static final String KEY1_WordPosition = "position";
    public static final String KEY2_Suid = "suid";
    public static final String KEY2_Word = "word";
    public static final String KEY_MEAN_BODY = "meaning";
    public static final String KEY_MEAN_TITLE = "title";
    EngineManager3rd mEngine;
    boolean mIsExactlyMatching;
    int mKeywordPosition;
    TagConverter mTagConverter;
    HashMap<String, Object> mTotalInfoWordList;
    public final int THEME_DEFAULT = 0;
    IBinder m_binder = new IDioDictService.Stub() { // from class: com.diotek.diodict3.phone.service.DioDictService.1
        @Override // com.diotek.diodict3.phone.service.IDioDictService
        /* renamed from: getWordList */
        public HashMap<String, Object> mo3getWordList(int dicType, String inputWord, boolean isGetWordList) {
            int maxItems;
            if (inputWord == null || inputWord.equals("")) {
                return null;
            }
            int current_dictype = DioDictService.this.mEngine.getCurDict();
            EngineManager3rd.SearchMethodInfo preSearchMethod = DioDictService.this.mEngine.getSearchMethod();
            DioDictService.this.mEngine.setDicType(dicType);
            EngineManager3rd.SearchMethodInfo[] searchMethodInfo = DioDictService.this.mEngine.getSupportSearchMethodInfo();
            DioDictService.this.mEngine.setSearchMethod(searchMethodInfo[0]);
            if (!DioDictService.this.mEngine.searchByCheckWildChar(inputWord, inputWord, 2) || (maxItems = DioDictService.this.mEngine.getResultListCount(2)) <= 0) {
                return null;
            }
            DioDictService.this.mIsExactlyMatching = EngineNative3rd.LibGetResultExactMatch();
            DioDictService.this.mKeywordPosition = DioDictService.this.mEngine.getResultListKeywordPos(2);
            DioDictService.this.mTotalInfoWordList = new HashMap<>();
            List<HashMap<String, Object>> tWordList = new ArrayList<>();
            DioDictService.this.mTotalInfoWordList.put(DioDictService.KEY1_IsMatching, Boolean.valueOf(DioDictService.this.mIsExactlyMatching));
            if (!isGetWordList) {
                HashMap<String, Object> tItem = new HashMap<>();
                String keyword = DioDictService.this.mEngine.getResultListKeywordByPos(DioDictService.this.mKeywordPosition, 2);
                int suid = DioDictService.this.mEngine.getResultListSUIDByPos(DioDictService.this.mKeywordPosition, 2);
                tItem.put(DioDictService.KEY2_Word, keyword);
                tItem.put("suid", Integer.valueOf(suid));
                tWordList.add(tItem);
            } else {
                DioDictService.this.mTotalInfoWordList.put(DioDictService.KEY1_WordPosition, Integer.valueOf(DioDictService.this.mKeywordPosition));
                for (int idx = 0; idx < maxItems; idx++) {
                    HashMap<String, Object> tItem2 = new HashMap<>();
                    String keyword2 = DioDictService.this.mEngine.getResultListKeywordByPos(idx, 2);
                    int suid2 = DioDictService.this.mEngine.getResultListSUIDByPos(idx, 2);
                    tItem2.put(DioDictService.KEY2_Word, keyword2);
                    tItem2.put("suid", Integer.valueOf(suid2));
                    tWordList.add(tItem2);
                }
            }
            DioDictService.this.mTotalInfoWordList.put(DioDictService.KEY1_List, tWordList);
            if (current_dictype != -1) {
                DioDictService.this.mEngine.setDicType(current_dictype);
                DioDictService.this.mEngine.setSearchMethod(preSearchMethod);
            }
            return DioDictService.this.mTotalInfoWordList;
        }

        @Override // com.diotek.diodict3.phone.service.IDioDictService
        /* renamed from: getMeaning */
        public HashMap<String, Spanned> mo2getMeaning(int dicType, String keyword, int suid) {
            if (DioDictService.this.mTagConverter != null) {
                DioDictService.this.mTagConverter = null;
            }
            DioDictService.this.mTagConverter = new TagConverter(null, DioDictService.this.mEngine, 0, null);
            DioDictService.this.mTagConverter.loadMeaning(dicType, keyword, suid, 0);
            Spanned titleSpanned = DioDictService.this.mTagConverter.getKeyFieldSpan();
            Spanned meaningSpanned = DioDictService.this.mTagConverter.getMeanFieldSpan();
            HashMap<String, Spanned> meanigMap = new HashMap<>();
            meanigMap.put(DioDictService.KEY_MEAN_TITLE, titleSpanned);
            meanigMap.put(DioDictService.KEY_MEAN_BODY, meaningSpanned);
            return meanigMap;
        }

        @Override // com.diotek.diodict3.phone.service.IDioDictService
        public String getFontPath() {
            return DictUtils.getFontFullPath();
        }

        @Override // com.diotek.diodict3.phone.service.IDioDictService
        public int getMeanTextColor() {
            return ThemeColor.getMeanBaseTextColor();
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent arg0) {
        this.mEngine = EngineManager3rd.getInstance(getApplicationContext());
        if (!EngineNative3rd.LibIsValidDBHandle()) {
            this.mEngine.setSupportDictionary();
            if (!this.mEngine.initNativeEngine(getApplicationContext())) {
                return null;
            }
        }
        return this.m_binder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        this.mTotalInfoWordList = null;
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        this.mEngine = EngineManager3rd.getInstance(getApplicationContext());
        if (!EngineNative3rd.LibIsValidDBHandle()) {
            this.mEngine.setSupportDictionary();
            if (!this.mEngine.initNativeEngine(getApplicationContext())) {
                Log.e("DioDict", "onRebind null");
            }
        }
        super.onRebind(intent);
    }
}
