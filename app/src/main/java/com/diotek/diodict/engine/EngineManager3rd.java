package com.diotek.diodict.engine;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.diotek.diodict.CMN;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

/* loaded from: classes.dex */
public class EngineManager3rd {
    private static volatile EngineManager3rd engineInstance = null;
    private static Integer[] mSupporTTS = null;
    private Context mContext;
    private boolean mEngineNoErr;
    private int mHighlightPenColorIndex;
    private ResultWordList3rd mHyperResultList;
    public ResultWordList3rd mResultList;
    private ResultWordList3rd mServiceResultList;
    private Integer[] mSupportDictionary = null;
    private Integer[] mSupportMainDictionary = null;
    private int mCurDicType = -1;
    private SearchMethodInfo[] mSupportSearchMethodInfo = null;
    private SearchMethodInfo mCurSearchMethod = null;
    public final int NO_ERROR = 0;
    public final int ERROR_NOT_FOUND_SO = 1;
    public final int ERROR_NO_EXIST_DB_FILE = 2;
    public final int ERROR_NOT_FOUND_STARTDICT = 3;
    public final int ERROR_SEARCH_FAIL = 4;

    /* loaded from: classes.dex */
    public static class SearchMethodInfo implements Parcelable {
        public static final Parcelable.Creator<SearchMethodInfo> CREATOR = new Parcelable.Creator<SearchMethodInfo>() { // from class: com.diotek.diodict.engine.EngineManager3rd.SearchMethodInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SearchMethodInfo createFromParcel(Parcel source) {
                SearchMethodInfo sm = new SearchMethodInfo(source.readInt(), source.readInt());
                return sm;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SearchMethodInfo[] newArray(int size) {
                return new SearchMethodInfo[size];
            }
        };
        int id;
        int nameID;

        public SearchMethodInfo(int n, int nid) {
            this.nameID = 0;
            this.id = 0;
            this.nameID = n;
            this.id = nid;
        }

        public int getNameID() {
            return this.nameID;
        }

        public int getId() {
            return this.id;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.nameID);
            dest.writeInt(this.id);
        }
    }

    private EngineManager3rd(Context context) {
        this.mEngineNoErr = false;
        this.mContext = null;
        this.mEngineNoErr = true;
        this.mContext = context;
    }

    public static EngineManager3rd getInstance(Context context) {
        if (engineInstance == null) {
            synchronized (EngineManager3rd.class) {
                if (engineInstance == null && context != null) {
                    engineInstance = new EngineManager3rd(context);
                }
            }
        }
        return engineInstance;
    }

    public boolean initDBManager() {
        Dependency.Init(this.mContext);
        if (!Dependency.getDevice().checkDRM()) {
            Toast.makeText(this.mContext, (int) R.string.model_check, 0).show();
            return false;
        }
        Dependency.getVendor().startInit(this.mContext);
        return true;
    }

    public boolean initDicEngine(int dicType, String word, int suid) {
        if (initDicEngineWithResult(dicType, word, suid) == 0) {
            return true;
        }
        this.mEngineNoErr = false;
        return false;
    }

    public int initDicEngineWithResult(int dicType, String word, int suid) {
        DictUtils.installLib(this.mContext);
        if (DictDBManager.checkInstalledOxfordChineseEnglish(this.mContext) && ICUCollator.isUsableICU()) {
            ICUCollator.getInstance().initialize();
        }
        int nativeInitRes = initNativeEngineWithResult(this.mContext);
        if (nativeInitRes == 0) {
            int nDicType = getStartDict(dicType);
            if (nDicType == -1) {
                return 3;
            }
            if (!changeDictionary(nDicType, word, suid)) {
                return 4;
            }
            DictUtils.initSearchLastSearchInfoToPreference(this.mContext);
            DictUtils.initSearchCursorInfoToPreference(this.mContext);
            DictInfo.mCurrentDBName = DictDBManager.getDictName(nDicType);
            if (Dependency.isContainTTS()) {
                TTSEngine.InitTTS();
            }
            return 0;
        }
        return nativeInitRes;
    }

    public boolean initNativeEngine(Context context) {
        if (initNativeEngineWithResult(context) == 0) {
            return true;
        }
        this.mEngineNoErr = false;
        return false;
    }

    public int initNativeEngineWithResult(Context context) {
        if (EngineNative3rd.getLastErr() == -1) {
            this.mEngineNoErr = false;
            return 1;
        }
        byte[] dbUHCPFullPath = null;
        if (DictInfo.DIODICT_DB_UHCPTABLE_7Z != null) {
            dbUHCPFullPath = (DictUtils.getDBPath() + DictInfo.DIODICT_DB_UHCPTABLE_7Z).getBytes();
        }
        int LibInit = EngineNative3rd.LibInit(dbUHCPFullPath, false, true, false);
        if (!setSupportDictionary()) {
            MSG.l(2, "not exist db file");
            this.mEngineNoErr = false;
            return 2;
        } else if (Dependency.isContainTTS()) {
            setSupportTTS();
            return LibInit;
        } else {
            return LibInit;
        }
    }

    public void setDicType(int nDicType) {
		if (mCurDicType!=nDicType)
		{
			setCurDict(nDicType);
			if (nDicType != 65520) {
				setNativeDicType(nDicType);
			}
		}
    }

    public void setNativeDicType(int nDicType) {
        EngineNative3rd.LibSetDicType(nDicType);
        openDB(nDicType);
    }

    public Vector<Integer> getExistDBList() {
        Vector<Integer> templist = new Vector<>();
        Integer[] dicts = DictDBManager.getDictList();
        int size = dicts.length;
        for (int i = 0; i < size; i++) {
            String dbfullpath = DictUtils.getDBPath() + DictDBManager.getDictFilename(dicts[i].intValue()) + DictInfo.DBEXTENSION;
            File file = new File(dbfullpath);
            if (file.exists() && DictDBManager.getDictIndependence(dicts[i].intValue()) != 3) {
                if (DictUtils.checkInstallDB(dicts[i].intValue())) {
                    templist.add(dicts[i]);
                }
            } else if (!file.exists() && dicts[i].intValue() != 65520 && !dbfullpath.contains("TOTAL_SEARCH") && !dbfullpath.contains("/sdcard/")) {
                MSG.l(2, "DioDict_3_for_Android (EngineManger3rd.getExistDBList() No Exist DB : " + dbfullpath);
            }
            if (dicts[i].intValue() == 65520 && templist.size() > 2) {
                templist.add((int) DictDBManager.DEDT_TOTAL_SEARCH);
            }
        }
        return templist;
    }
	
	public int getStartDict(int lastDicType) { // pasted from jdui
		int nDicTypePos = -1;
		Integer[] supportDicts = getSupportDictionary();
		if (supportDicts.length == 0) {
			return nDicTypePos;
		}
		if (lastDicType == -1) {
			DictUtils.setLastDictToPreference(this.mContext, supportDicts[0]);
			return supportDicts[0];
		}
		nDicTypePos = 0;
		while (true) {
			int b = -1;
			if (nDicTypePos < supportDicts.length)
				if (lastDicType == supportDicts[nDicTypePos]) {
					b = nDicTypePos;
				} else {
					nDicTypePos++;
					continue;
				}
			if (b == -1 || supportDicts[b] == 65520) {
				DictUtils.setLastDictToPreference(this.mContext, supportDicts[0]);
				return supportDicts[0];
			}
			return supportDicts[b];
		}
	}

    public boolean setSupportDictionary() {
        ArrayList<Integer> tempMainList = new ArrayList<>();
        DictUtils.setDBPath(this.mContext, false);
        Vector<Integer> templist = getExistDBList();
        if (templist.size() == 0 || templist.get(0).intValue() == 65520) {
            if (Dependency.isChina()) {
                DictUtils.setDBPath("/preload/");
            } else if (!Dependency.isJapan()) {
                return false;
            } else {
                DictUtils.setDBPath(DictInfo.DIODICT_JPN_SYSTEM_PATH);
            }
            templist = getExistDBList();
            if (templist.size() == 0 || templist.get(0).intValue() == 65520) {
                return false;
            }
        }
        this.mSupportDictionary = new Integer[templist.size()];
        this.mSupportDictionary = (Integer[]) templist.toArray(this.mSupportDictionary);
        for (int i = 0; i < this.mSupportDictionary.length; i++) {
            if (EngineInfo3rd.getIndependenceMain(this.mSupportDictionary[i].intValue())) {
                tempMainList.add(this.mSupportDictionary[i]);
            }
        }
        if (tempMainList.size() == 0 || tempMainList.get(0).intValue() == 65520) {
            return false;
        }
        this.mSupportMainDictionary = new Integer[tempMainList.size()];
        this.mSupportMainDictionary = (Integer[]) tempMainList.toArray(this.mSupportMainDictionary);
        Arrays.sort(this.mSupportMainDictionary, new Comparator<Integer>() { // from class: com.diotek.diodict.engine.EngineManager3rd.1
            @Override // java.util.Comparator
            public int compare(Integer arg0, Integer arg1) {
                return arg0.intValue() - arg1.intValue();
            }
        });
        Arrays.sort(this.mSupportDictionary, new Comparator<Integer>() { // from class: com.diotek.diodict.engine.EngineManager3rd.2
            @Override // java.util.Comparator
            public int compare(Integer arg0, Integer arg1) {
                return arg0.intValue() - arg1.intValue();
            }
        });
        return true;
    }

    public Integer[] getSupportDictionary() {
        return this.mSupportDictionary;
    }

    public Integer[] getSupportMainDictionary() {
        return this.mSupportMainDictionary;
    }

    public Vector<Integer> getExistTTSList() {
        Vector<Integer> templist = new Vector<>();
        Iterator i$ = EngineInfo3rd.ALLTTS_TABLE.iterator();
        while (i$.hasNext()) {
            int ttsID = ((Integer)i$.next()).intValue();
            if (DictUtils.checkExistTTSFile(ttsID) && DictUtils.checkInstallTTS(ttsID)) {
                templist.add(Integer.valueOf(ttsID));
            }
        }
        return templist;
    }

    public boolean setSupportTTS() {
        Vector<Integer> templist = getExistTTSList();
        if (templist.size() == 0) {
            return false;
        }
        mSupporTTS = new Integer[templist.size()];
        mSupporTTS = (Integer[]) templist.toArray(mSupporTTS);
        return true;
    }

    public static Integer[] getSupporTTS() {
        return mSupporTTS;
    }

    public boolean changeDictionary(int nDicType, String word, int suid) {
        if (nDicType == -1) {
            return false;
        }
        setDicType(nDicType);
        if (nDicType != 65520) {
            DictUtils.setLastDictToPreference(this.mContext, nDicType);
        }
        this.mSupportSearchMethodInfo = createSearchMethodList(nDicType, this.mContext);
        if (this.mSupportSearchMethodInfo == null || !setSearchMethod(this.mSupportSearchMethodInfo[0])) {
            return false;
        }
        if (suid == -1) {
            return search(word, false, 0);
        }
        return search(word, suid, false, 0);
    }

    public SearchMethodInfo[] createSearchMethodList(int nDicType, Context context) {
        return getSearchMethodTitle(DictDBManager.getDictSearchMethod(nDicType), context);
    }

    public SearchMethodInfo[] getSupportSearchMethodInfo() {
        if (this.mSupportSearchMethodInfo == null) {
            this.mSupportSearchMethodInfo = createSearchMethodList(getCurDict(), this.mContext);
        }
        return this.mSupportSearchMethodInfo;
    }

    public SearchMethodInfo getSearchMethod() {
        return this.mCurSearchMethod;
    }

    public boolean setSearchMethod(SearchMethodInfo searchMethod) {
        if (searchMethod == null) {
            return false;
        }
        this.mCurSearchMethod = searchMethod;
        return true;
    }

    public boolean setSearchMethodById(int searchMethodId) {
        SearchMethodInfo[] methodInfo = getSupportSearchMethodInfo();
        for (SearchMethodInfo info : methodInfo) {
            if (info.id == searchMethodId) {
                setSearchMethod(info);
                return true;
            }
        }
        MSG.l(2, "해당 method id는 현재 지원되지 않음. searchMethodId = " + searchMethodId + " getCurDict() = " + getCurDict());
        setSearchMethod(methodInfo[0]);
        return false;
    }

    public SearchMethodInfo createSearchMethodInfo(int name, int nDictType) {
        return new SearchMethodInfo(name, nDictType);
    }

    private SearchMethodInfo[] getSearchMethodTitle(int searchmethods, Context context) {
        Vector<SearchMethodInfo> title = new Vector<>();
        if ((searchmethods & 1) == 1) {
            title.add(createSearchMethodInfo(R.string.word_search, 1));
        }
        if ((searchmethods & 2) == 2) {
            title.add(createSearchMethodInfo(R.string.phrases_search, 2));
        }
        if ((searchmethods & 4) == 4) {
            title.add(createSearchMethodInfo(R.string.example_search, 4));
        }
        if ((searchmethods & 8) == 8) {
            title.add(createSearchMethodInfo(R.string.hangulro_search, 8));
        }
        if ((searchmethods & 16) == 16) {
            title.add(createSearchMethodInfo(R.string.spellcheck_search, 16));
        }
        if ((searchmethods & 32) == 32) {
            title.add(createSearchMethodInfo(R.string.initial_search, 32));
        }
        if ((searchmethods & 64) == 64) {
            title.add(createSearchMethodInfo(R.string.oldkor_search, 64));
        }
        if ((searchmethods & DictType.SEARCHTYPE_PINYIN) == 512) {
            title.add(createSearchMethodInfo(R.string.pinyin_search, DictType.SEARCHTYPE_PINYIN));
        }
        if ((searchmethods & 1024) == 1024) {
            title.add(createSearchMethodInfo(R.string.total_search, 1024));
        }
        if (this.mSupportSearchMethodInfo != null) {
            this.mSupportSearchMethodInfo = null;
            System.gc();
        }
        this.mSupportSearchMethodInfo = new SearchMethodInfo[title.size()];
        title.toArray(this.mSupportSearchMethodInfo);
        return this.mSupportSearchMethodInfo;
    }

    private boolean isSupportWildcard() {
        return this.mCurSearchMethod.id == 1 || this.mCurSearchMethod.id == 1024 || this.mCurSearchMethod.id == 128;
    }

    public boolean searchByCheckWildChar(String keyword, String oriKeyword, int nResultListType) {
        boolean bWildcard = false;
        if (isSupportWildcard()) {
            if (oriKeyword != null) {
                bWildcard = DictUtils.isWildcardSearch(oriKeyword);
            }
            if (bWildcard && getCurDict() == 65520) {
                clearResultList(nResultListType);
                return false;
            }
        }
        return search(keyword, bWildcard, nResultListType);
    }

    public boolean searchByCheckWildChar(String keyword, int nSUID, String oriKeyword, int nResultListType) {
        boolean bWildcard = false;
        if (isSupportWildcard()) {
            if (oriKeyword != null) {
                bWildcard = DictUtils.isWildcardSearch(oriKeyword);
            }
            if (bWildcard && getCurDict() == 65520) {
                clearResultList(nResultListType);
                return false;
            }
        }
        return search(keyword, nSUID, bWildcard, nResultListType);
    }

    private boolean search(String word, boolean bWildcard, int nResultListType) {
        int nSearchMethod = this.mCurSearchMethod.getId();
        if (bWildcard) {
            nSearchMethod = 128;
        }
        this.mEngineNoErr = search(word, nSearchMethod, nResultListType);
        return this.mEngineNoErr;
    }

    private boolean search(String word, int nSearchMethod, int nResultListType) {
        int nTmpDicType = getCurDict();
        String word2 = word.trim();
        byte[] wordbyte = convertToEngineSearchCharSet(word2.toLowerCase(), nTmpDicType);
        int err = -1;
        switch (nSearchMethod) {
            case 2:
                setDicType(DictDBManager.getIdiomDicTypeByCurDicType(nTmpDicType));
                err = EngineNative3rd.LibIdiomExampleSearch(wordbyte);
                break;
            case 4:
                setDicType(DictDBManager.getExampleDicTypeByCurDicType(nTmpDicType));
                err = EngineNative3rd.LibIdiomExampleSearch(wordbyte);
                break;
            case 8:
                if (DictDBManager.getCpCHNDictionary(nTmpDicType)) {
                    setDicType(DictDBManager.getPinyinDicTypeByCurDicType(nTmpDicType));
                } else {
                    setDicType(EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(nTmpDicType, true));
                }
                int nHangulroLang = DictDBManager.getDictHangulroLang(nTmpDicType);
                if (nHangulroLang != -1 && !word2.equals("")) {
                    char[] csearchWord = new char[word2.length()];
                    word2.getChars(0, word2.length(), csearchWord, 0);
                    int[] wordArray = new int[word2.length()];
                    for (int i = 0; i < word2.length(); i++) {
                        wordArray[i] = csearchWord[i];
                    }
                    err = EngineNative3rd.LibHangulroSearch(wordArray, EngineInfo3rd.isUnicode(getCurDict()), nHangulroLang);
                    break;
                }
                break;
            case 16:
                setDicType(EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(nTmpDicType, true));
                String szSpellCheckDBName = DictDBManager.getDictSpellCheckFileName(getCurDict());
                if (szSpellCheckDBName != null && !szSpellCheckDBName.equals("")) {
                    String szSpellCheckDBPath = DictInfo.DBPATH + szSpellCheckDBName + DictInfo.DBEXTENSION;
                    err = EngineNative3rd.LibSpellCheckInit(szSpellCheckDBPath.getBytes(), getCurDict());
                    try {
                        byte[] tmpbyte = word2.getBytes("ISO-8859-1");
                        err = EngineNative3rd.LibSpellCheckSearch(tmpbyte);
                        break;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                break;
            case 32:
                setDicType(EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(nTmpDicType, true));
                err = EngineNative3rd.LibInitialSearch(wordbyte);
                if (err == 0 && word2.length() == 0) {
                    err = -1;
                    break;
                }
                break;
            case 64:
                setDicType(DictDBManager.getOldKorDicTypeByCurDicType(nTmpDicType));
                err = EngineNative3rd.LibSearchWord(wordbyte, false);
                break;
            case 128:
                setDicType(EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(nTmpDicType, true));
                err = EngineNative3rd.LibWildCardSearch(wordbyte);
                break;
            case DictType.SEARCHTYPE_PINYIN /* 512 */:
                setDicType(DictDBManager.getPinyinDicTypeByCurDicType(nTmpDicType));
                err = EngineNative3rd.LibSearchWord(wordbyte, false);
                break;
            case 1024:
                if (isUnifiedCodeset(word2)) {
                    int codepage = DictUtils.getCodePage(word2);
                    err = searchTotal(wordbyte, codepage);
                    break;
                }
                break;
            default:
                setDicType(EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(nTmpDicType, true));
                err = EngineNative3rd.LibSearchWord(wordbyte, false);
                break;
        }
        if (err != 0) {
            clearResultList(nResultListType);
            return false;
        }
        return setResultList(nResultListType);
    }

    private boolean search(String word, int suid, boolean bWildcard, int nResultListType) {
        int nSearchMethod = this.mCurSearchMethod.getId();
        if (bWildcard) {
            nSearchMethod = 128;
        }
        this.mEngineNoErr = search(word, suid, nSearchMethod, nResultListType);
        return this.mEngineNoErr;
    }

    private boolean search(String word, int suid, int nSearchMethod, int nResultListType) {
        int err;
        byte[] wordbyte = convertToEngineSearchCharSet(word, getCurDict());
        switch (nSearchMethod) {
            case 2:
                setDicType(DictDBManager.getIdiomDicTypeByCurDicType(getCurDict()));
                err = EngineNative3rd.LibIdiomExampleSearchByWL(wordbyte, suid);
                break;
            case 4:
                setDicType(DictDBManager.getExampleDicTypeByCurDicType(getCurDict()));
                err = EngineNative3rd.LibIdiomExampleSearchByWL(wordbyte, suid);
                break;
            case 8:
                char[] csearchWord = new char[word.length()];
                word.getChars(0, word.length(), csearchWord, 0);
                int[] wordArray = new int[word.length()];
                for (int i = 0; i < word.length(); i++) {
                    wordArray[i] = csearchWord[i];
                }
                err = EngineNative3rd.LibHangulroSearch(wordArray, EngineInfo3rd.isUnicode(getCurDict()), DictDBManager.getDictHangulroLang(getCurDict()));
                break;
            case 16:
                String szSpellCheckDBName = DictInfo.DBPATH + DictDBManager.getDictSpellCheckFileName(getCurDict()) + DictInfo.DBEXTENSION;
                err = EngineNative3rd.LibSpellCheckInit(szSpellCheckDBName.getBytes(), getCurDict());
                try {
                    byte[] tmpbyte = word.getBytes("ISO-8859-1");
                    err = EngineNative3rd.LibSpellCheckSearch(tmpbyte);
                    break;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    break;
                }
            case 32:
                err = EngineNative3rd.LibInitialSearchByWL(wordbyte, suid);
                break;
            case 64:
                setDicType(DictDBManager.getOldKorDicTypeByCurDicType(getCurDict()));
                err = EngineNative3rd.LibSearchWord(wordbyte, false);
                break;
            case 128:
                err = EngineNative3rd.LibWildCardSearchByWL(wordbyte, suid);
                break;
            case DictType.SEARCHTYPE_PINYIN /* 512 */:
                setDicType(DictDBManager.getPinyinDicTypeByCurDicType(getCurDict()));
                err = EngineNative3rd.LibSearchWord(wordbyte, false);
                break;
            default:
                err = EngineNative3rd.LibSearchWord(wordbyte, false);
                break;
        }
        if (err != 0) {
            return false;
        }
        return setResultList(nResultListType);
    }

    public boolean setResultList(int nResultListType) {
        int[] nTotalSearchDictType = null;
        int nCurDictType = getCurDict();
        int nItemDictType = nCurDictType;
        switch (nResultListType) {
            case 1:
                this.mHyperResultList = EngineNative3rd.LibGetResult();
                break;
            case 2:
                this.mServiceResultList = EngineNative3rd.LibGetResult();
                break;
            default:
                this.mResultList = EngineNative3rd.LibGetResult();
                break;
        }
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList.getSize() <= 0) {
            return false;
        }
        if (nCurDictType == 65520 && (nTotalSearchDictType = EngineNative3rd.LibIntegrationDBArray()) == null) {
            return false;
        }
        for (int i = 0; i < tmpResultList.getSize(); i++) {
            if (nCurDictType == 65520) {
                if (nTotalSearchDictType == null) {
                    return false;
                }
                nItemDictType = nTotalSearchDictType[i];
            }
            tmpResultList.setAstWordList(EngineNative3rd.LibGetWordList(i, nItemDictType), EngineNative3rd.LibGetWordSuid(i), i, nItemDictType);
        }
        return true;
    }

    public void clearResultList(int nResultListType) {
        switch (nResultListType) {
            case 1:
                this.mHyperResultList = null;
                return;
            case 2:
                this.mServiceResultList = null;
                return;
            default:
                this.mResultList = null;
                return;
        }
    }

    public ResultWordList3rd getResultList(int nResultListType) {
        switch (nResultListType) {
            case 1:
                return this.mHyperResultList;
            case 2:
                return this.mServiceResultList;
            default:
                return this.mResultList;
        }
    }

    public String getMeaningData(String ketyword, int suid, int dicType, int bufsize, boolean isKeyword) {
        byte[] searchwordbyte = convertToEngineSearchCharSet(ketyword, dicType);
        byte[] array = EngineNative3rd.LibGetMeaningData(dicType, searchwordbyte, suid, bufsize, isKeyword);
        String str = DictUtils.convertByteToString(array, dicType, false);
        return str;
    }
	
	//synchronized
    public String getMeaning(String ketyword, int suid, int dicType, int enDispMode, int bufsize, boolean isConvertSym, boolean isKeepTag, int nMode) {
        byte[] searchwordbyte = convertToEngineSearchCharSet(ketyword, dicType);
//		CMN.Log("LibGetMeaning::", Thread.currentThread().getId(), ketyword, searchwordbyte==null, dicType, enDispMode, suid, bufsize, isConvertSym, isKeepTag);
//		try {
//			throw new RuntimeException();
//		} catch (RuntimeException e) {
//			CMN.Log(e);
//		}
		byte[] array = EngineNative3rd.LibGetMeaning(dicType, enDispMode, searchwordbyte, suid, bufsize, isConvertSym, isKeepTag);
        String str = DictUtils.convertByteToString(array, dicType, false);
        if (nMode == 1) {
            String correctWord = DictUtils.makeCorrectWord(ketyword);
            String mCorrectWord = DictUtils.getMakeSearchKeyword(correctWord);
            String asterisk = "";
            for (int i = 0; i < mCorrectWord.length(); i++) {
                asterisk = asterisk + "*";
            }
            int codepage = DictUtils.getCodePage(mCorrectWord);
            if (codepage == 936 || codepage == 949 || codepage == 932 || codepage == 950) {
                str = str.replaceAll(mCorrectWord, asterisk);
            } else {
                str = str.replaceAll(" " + mCorrectWord + " ", " " + asterisk + " ");
            }
        }
        if (DictDBManager.isIdiomDictionary(dicType)) {
			return str.replace("%M", "%M%D") + "%d";
        }
        if (DictDBManager.isExampleDictionary(dicType)) {
			return str.replace("%M", "%M%E") + "%e";
        }
		//CMN.Log("LibGetMeaning="+str);
        return str;
    }

    public static boolean openDB(int nDicType) {
        String dbFileName = DictUtils.getDBPath() + DictDBManager.getDictFilename(nDicType) + DictInfo.DBEXTENSION;
        if (dbFileName == null || dbFileName.length() == 0) {
            return false;
        }
        int err = EngineNative3rd.LibOpenDB(dbFileName.getBytes(), false);
        return err == 0;
    }

    public boolean isRealTimeSearchSupport() {
        int searchMethodId = this.mCurSearchMethod.getId();
        return searchMethodId == 1 || searchMethodId == 512;
    }

    public String getEngineLastWord() {
        byte[] lastWordArray = EngineNative3rd.LibGetLastWordList(getCurDict());
        if (lastWordArray == null) {
            return null;
        }
        String lastStr = DictUtils.convertByteToString(lastWordArray, getCurDict(), false);
        return lastStr;
    }

    public String getEngineFirstWord() {
        byte[] firstWordArray = EngineNative3rd.LibGetFirstWordList(getCurDict());
        if (firstWordArray == null) {
            return null;
        }
        String firstStr = DictUtils.convertByteToString(firstWordArray, getCurDict(), false);
        return firstStr;
    }

    public int getCurDict() {
        return this.mCurDicType;
    }

    public void setCurDict(int nDicType) {
        this.mCurDicType = nDicType;
    }

    public boolean getEngineNoError() {
        return this.mEngineNoErr;
    }

    public static byte[] convertToEngineSearchCharSet(String word, int nDicType) {
        byte[] converted = null;
        switch (DictDBManager.getDictEncode(nDicType)) {
            case 0:
                try {
                    converted = word.getBytes("UTF-16LE");
                    break;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    break;
                }
            case 4:
                try {
                    converted = word.getBytes("KSC5601");
                    break;
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    break;
                }
        }
        if (converted == null) {
            return null;
        }
        byte[] makeSearchword = DictUtils.makeSearchWord(converted, DictDBManager.getDictEncode(nDicType));
        return makeSearchword;
    }

    public int getResultListSUIDByPos(int nPos, int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return 0;
        }
        return tmpResultList.getSUIDByPos(nPos);
    }

    public String getResultListKeywordByPos(int nPos, int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return null;
        }
        return tmpResultList.getKeywordByPos(nPos);
    }

    public int getResultListDictByPos(int nPos, int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return -1;
        }
        return tmpResultList.getDicTypeByPos(nPos);
    }

    public int getResultListKeywordPos(int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return 0;
        }
        return tmpResultList.getKeywordPos();
    }

    public boolean getHyperTextExactMatch(int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return false;
        }
        return tmpResultList.isBExactmatch();
    }

    public int getResultDicTypeByPos(int nPos, int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return -1;
        }
        return tmpResultList.getDicTypeByPos(nPos);
    }

    public int getResultListCount(int nResultListType) {
        ResultWordList3rd tmpResultList = getResultList(nResultListType);
        if (tmpResultList == null) {
            return 0;
        }
        return tmpResultList.getSize();
    }

    public int getCurrentSearchMethodId() {
        return this.mCurSearchMethod.getId();
    }

    private boolean isUnifiedCodeset(String word) {
        if (word == null || word.length() == 0) {
            return false;
        }
        if (word.length() == 1) {
            return true;
        }
        int codePageFirst = 255;
        String word2 = word.trim();
        int i = 0;
        while (i < word2.length() && codePageFirst == 255) {
            codePageFirst = DictUtils.getCodePage(word2.charAt(i));
            i++;
        }
        while (i < word2.length()) {
            char ch = word2.charAt(i);
            if (ch != ' ') {
                int codePageNext = DictUtils.getCodePage(ch);
                if (codePageNext != codePageFirst) {
                    return false;
                }
            }
            i++;
        }
        return true;
    }

    private int searchTotal(byte[] wordbyte, int codepage) {
        boolean isChnCodePage = codepage == 936;
        Integer[] supportDict = DictDBManager.getDictListByCodepage(DictDBManager.getDictList(), codepage);
        int searchednum = 0;
        int nTmpDicType = getCurDict();
        ArrayList<Integer> dictList = new ArrayList<>();
        for (Integer num : supportDict) {
            int dictype = num.intValue();
            if (dictype != 65520 && !dictList.contains(dictList)) {
                if (isChnCodePage) {
                    if (!DictDBManager.isChnInitialDict(dictype)) {
                        int tmpdictype = DictDBManager.getKanjiDicTypeByCurDicType(dictype);
                        if (tmpdictype != 65282) {
                            dictype = tmpdictype;
                        }
                    }
                }
                setDicType(dictype);
                dictList.add(Integer.valueOf(dictype));
                searchednum = EngineNative3rd.LibUnifiySearch(wordbyte, getCurDict(), 50, searchednum);
            }
        }
        setDicType(nTmpDicType);
        return searchednum == 0 ? 1 : 0;
    }

    public int getPageTotalLine(boolean bEstimateWhole) {
        return EngineNative3rd.LibPageGetTotalLine(bEstimateWhole);
    }

    public int getPageCurLine() {
        return EngineNative3rd.LibPageGetCurLine();
    }

    public boolean pageScroll(int nStep) {
        return EngineNative3rd.LibPageScroll(nStep);
    }

    public boolean pageTouchDown(int x, int y) {
        return EngineNative3rd.LibPageTouchDown(x, y);
    }

    public void pageTouchMove(int x, int y) {
        EngineNative3rd.LibPageTouchMove(x, y);
    }

    public boolean pageTouchUp(int x, int y) {
        return EngineNative3rd.LibPageTouchUp(x, y);
    }

    public boolean pageHyperAt(int x, int y) {
        return EngineNative3rd.LibPageHyperAt(x, y);
    }

    public boolean pageHyperUp() {
        return EngineNative3rd.LibPageHyperUp();
    }

    public boolean pageHyperDown() {
        return EngineNative3rd.LibPageHyperDown();
    }

    public void setPageEnableHyper(boolean bHyperOn, boolean bSelection) {
        EngineNative3rd.LibPageSetEnableHyper(bHyperOn, bSelection);
    }

    public boolean hasPageSelectedText() {
        return EngineNative3rd.LibPageHasSelectedText();
    }

    public int[] getPageSelectedLine() {
        int[] pos = {0, 0};
        long XYPos = EngineNative3rd.LibPageGetSelectedLine();
        pos[0] = ((int) ((-65536) & XYPos)) >> 16;
        pos[1] = (int) (65535 & XYPos);
        return pos;
    }

    public int searchMean(String word, int suid) {
        byte[] searchwordbyte = convertToEngineSearchCharSet(word, getCurDict());
        int err = EngineNative3rd.LibSearchMean(searchwordbyte, suid);
        return err;
    }

    public void drawMeanView(boolean bSearchBefore, boolean bChangeOrientation) {
        EngineNative3rd.LibDrawMeanView(bSearchBefore, bChangeOrientation);
    }

    public void setHighlightPenColorIdx(int nIdx, int[] inputColorThemeArray) {
        this.mHighlightPenColorIndex = nIdx;
        setHighlightPenColor(Color.rgb(20, 190, 240), inputColorThemeArray);
    }

    public int getHighlightPenColorIdx() {
        return this.mHighlightPenColorIndex;
    }

    public void setHighlightPenColor(int nColor, int[] inputColorThemeArray) {
        int[] baseColor = new int[9];
        ThemeColor.setHilightColorToTheme(baseColor, inputColorThemeArray, nColor);
        EngineNative3rd.LibSetHighlightPenColor(baseColor);
    }

    public void setTheme(int[] baseColor, int[] adbColor) {
        EngineNative3rd.LibSetTheme(baseColor, adbColor);
    }

    public byte[] getPageSelectedText(boolean isUnicode) {
        return EngineNative3rd.LibPageGetSelectedText(isUnicode);
    }

    public void setPageMargin(int t, int b, int l, int r) {
        EngineNative3rd.LibSetMargin(t, b, l, r);
    }

    public void setSepGap(int gap) {
        EngineNative3rd.LibSetSepGap(gap);
    }

    public boolean getIsValidDBHandle() {
        if (EngineNative3rd.getLastErr() == -1) {
            MSG.l(2, "not found so file");
            return false;
        }
        return EngineNative3rd.LibIsValidDBHandle();
    }

    public void terminateEngine() {
        if (EngineNative3rd.getLastErr() == -1) {
            MSG.l(2, "not found so file");
        } else {
            EngineNative3rd.LibTerminate();
        }
    }

    public OriginList getOriginList(int dicType, int langType, String word) {
        byte[] wordbyte = convertToEngineSearchCharSet(word, dicType);
        return EngineNative3rd.LibGetOriginList(dicType, langType, wordbyte);
    }
}
