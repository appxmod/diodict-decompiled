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
import com.diotek.diodict.engine.ResInstall;
import com.diotek.diodict.engine.WordList3rd;
import com.diotek.diodict.mean.TagConverter;
import com.diotek.diodict.service.IServiceForReadersHub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static boolean copyFontResToSdcard(String dataFontPath, String sdPath, String fontName) {
        //throw new UnsupportedOperationException("Method not decompiled: com.diotek.diodict.service.ServiceForReadersHub.copyFontResToSdcard(java.lang.String, java.lang.String, java.lang.String):boolean");
		CMN.debug("fatal copyFontResToSdcard!");
		int FullSize;
		FileInputStream fis = null;
		try {
			try {
				FileInputStream fis2 = new FileInputStream(dataFontPath);
				if (fis2 != null) {
					try {
						if (fis2.available() < 0) {
							fis2.close();
							File f = new File(dataFontPath);
							f.delete();
							fis = new FileInputStream(dataFontPath);
							FullSize = fis.available();
							if (FullSize >= 0) {
								if (fis == null) {
									return false;
								}
								try {
									fis.close();
									return false;
								} catch (IOException e) {
									e.printStackTrace();
									return false;
								}
							}
							try {
								if (!ResInstall.IsAvailableSaveToInternalStorage(FullSize)) {
									if (fis == null) {
										return false;
									}
									try {
										fis.close();
										return false;
									} catch (IOException e2) {
										e2.printStackTrace();
										return false;
									}
								}
								byte[] tempdata = new byte[1000];
								File dbPath = new File(sdPath);
								if (dbPath != null && !dbPath.isDirectory()) {
									dbPath.mkdirs();
								}
								FileOutputStream fos = new FileOutputStream(sdPath + fontName);
								while (FullSize > 0) {
									if (FullSize < 1000) {
										byte[] tempdata2 = new byte[FullSize];
										fis.read(tempdata2);
										fos.write(tempdata2);
									} else {
										fis.read(tempdata);
										fos.write(tempdata);
									}
									FullSize -= 1000;
								}
								if (fos != null) {
									fos.close();
								}
								if (fis != null) {
									try {
										fis.close();
									} catch (IOException e3) {
										e3.printStackTrace();
									}
								}
								return true;
							} catch (IOException e4) {
								e4.printStackTrace();
								Log.e("copyFontResToSdcard ERR", sdPath);
								if (fis == null) {
									return false;
								}
								try {
									fis.close();
									return false;
								} catch (IOException e5) {
									e5.printStackTrace();
									return false;
								}
							}
						}
					} catch (IOException e) {
						fis = fis2;
						e.printStackTrace();
						Log.e("copyFontResToSdcard ERR", dataFontPath);
						if (fis == null) {
							return false;
						}
						try {
							fis.close();
							return false;
						} catch (IOException e7) {
							e7.printStackTrace();
							return false;
						}
					} catch (Throwable th) {
						th = th;
						fis = fis2;
						if (fis != null) {
							try {
								fis.close();
							} catch (IOException e8) {
								e8.printStackTrace();
							}
						}
						throw th;
					}
				}
				fis = fis2;
				FullSize = fis.available();
				if (FullSize >= 0) {
				}
			} catch (IOException e9) {
				e9.printStackTrace();
			}
		} catch (Throwable th2) {
			th2.printStackTrace();
		}
		return false;
	}
}
