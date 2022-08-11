package com.diotek.diodict.engine;

import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.utils.CMN;
import com.diotek.diospeech.DictionaryFilter;
import com.diotek.diospeech.tts.PrompterEventAdapter;
import com.diotek.diospeech.tts.PrompterInterface;
import com.diotek.diospeech.tts.PrompterManager;
import com.diotek.diospeech.tts.ResultCode;

/* loaded from: classes.dex */
public class TTSEngine {
    public static final int ERR_NONE = 0;
    public static final int ERR_NOT_FOUND_SO = -1;
    private static final int INIT_STATE_DESTROYED = 2;
    private static final int INIT_STATE_INITIALIZED = 3;
    private static final int PLAY_STATE_BUSY = 2;
    private static final int PLAY_STATE_IDLE = 1;
    private static final int PLAY_STATE_INVALID = -1;
    private static final int TTS_CHN = 3;
    public static final int TTS_ENG_NONE = 0;
    private static final int TTS_ENG_S = 0;
    public static final int TTS_ENG_UK = 1;
    public static final int TTS_ENG_US = 0;
    private static final int TTS_ERR_CREATE = 2;
    private static final int TTS_ERR_DESTROY = 3;
    private static final int TTS_ERR_INIT = 1;
    private static final int TTS_ERR_NONE = 0;
    private static final int TTS_ERR_PLAY = 5;
    private static final int TTS_ERR_PLAY_LENGTH_0 = 4;
    private static final int TTS_ERR_SETVOICE = 8;
    private static final int TTS_ERR_SETVOICE_CODEPAGE = 7;
    private static final int TTS_ERR_STOP = 6;
    private static final int TTS_JPN = 5;
    private static final int TTS_KOR = 2;
    private static final int TTS_NOT_INIT = 0;
    static boolean isStartTTS;
    private static boolean isUseTilde;
    private static String keywordString;
    private static String mPlayString;
    public static OnTTSPlayed nextWordCallback;
    private static PrompterInterface prompter;
    static PrompterEventAdapter promptereventadapter;
    static boolean usepronun;
    private static final String[] TTSVOICE_LANG = {"en-US", "en-GB", "ko-KR", "zh-CN", "yue-CN", "ja-JP"};
    private static String SVOX_TTS = "SVOX_";
    private static String HCILAB_TTS = "HCILAB_";
    private static String languageCode = "";
    private static boolean isUsePreZHCNTTS = true;
    private static final String[] EXCEPTION_TTSWORD = {"Ā", "Á", "Ǎ", "À", "ā", "á", "ǎ", "à", "Ē", "É", "Ě", "È", "ē", "é", "ě", "è", "Ī", "Í", "Ǐ", "Ì", "ī", "í", "ǐ", "ì", "Ō", "Ó", "Ǒ", "Ò", "ō", "ó", "ǒ", "ò", "Ū", "Ú", "Ǔ", "Ù", "ū", "ú", "ǔ", "ù", "Ǚ", "Ǖ", "Ǘ", "Ǚ", "Ǜ", "ü", "ǖ", "ǘ", "ǚ", "ǜ", "ɑ", "ɡ", "β", "ǹ", "ḿ", "\ue7c7", "—", "’", "︈", "?", "r", "y"};
    private static final String[] PINYINWORD = {"a1", "a2", "a3", "a4", "a1", "a2", "a3", "a4", "e1", "e2", "e3", "e4", "e1", "e2", "e3", "e4", "i1", "i2", "i3", "i4", "i1", "i2", "i3", "i4", "o1", "o2", "o3", "o4", "o1", "o2", "o3", "o4", "u1", "u2", "u3", "u4", "u1", "u2", "u3", "u4", "ü", "ü1", "ü2", "ü3", "ü4", "ü", "ü1", "ü2", "ü3", "ü4", "a", "g", "b", "n", "m", "m", " ", " ", " ", " ", " r", " y"};
    private static final String[] CHN_VOWEL = {"a1", "a2", "a3", "a4", "a1", "a2", "a3", "a4", "e1", "e2", "e3", "e4", "e1", "e2", "e3", "e4", "i1", "i2", "i3", "i4", "i1", "i2", "i3", "i4", "o1", "o2", "o3", "o4", "o1", "o2", "o3", "o4", "u1", "u2", "u3", "u4", "u1", "u2", "u3", "u4", "ü1", "ü2", "ü3", "ü4", "ü1", "ü2", "ü3", "ü4"};
    private static int mPlayCount = 0;
    private static PrompterManager prompterMgr = null;
    private static int mPlayState = 1;
    private static int mInitialState = 2;

    /* loaded from: classes.dex */
    public interface OnTTSPlayed {
        void setNextWordAfterTTsPlayed();
    }

    static /* synthetic */ int access$306() {
        int i = mPlayCount - 1;
        mPlayCount = i;
        return i;
    }

    static {
        try {
            MSG.l(1, "JNI:Trying to load " + DictInfo.TTS_LIB_NAME);
            if (Dependency.isContainTTS()) {
                System.loadLibrary(DictInfo.TTS_LIB_NAME);
            }
        } catch (UnsatisfiedLinkError e) {
            MSG.l(2, "JNI:Fail : Could not load " + DictInfo.TTS_LIB_NAME);
            Dependency.setContainTTS(false);
        }
        nextWordCallback = null;
        isStartTTS = false;
        usepronun = false;
        promptereventadapter = new PrompterEventAdapter() { // from class: com.diotek.diodict.engine.TTSEngine.1
            @Override // com.diotek.diospeech.tts.PrompterEventAdapter
            public String eventTextEntered(String enteredMessage) {
                if (TTSEngine.keywordString == null) {
                    String unused = TTSEngine.keywordString = "";
                }
                if (enteredMessage == null) {
                    enteredMessage = "";
                }
                if (!TTSEngine.usepronun) {
					CMN.debug("fatal runFilter! ak");
                    //return DictionaryFilter.runFilter(DictInfo.TTSPATH + DictInfo.TTSPATH_LUA, TTSEngine.languageCode, "", false, enteredMessage);
                }
                return enteredMessage;
            }

            @Override // com.diotek.diospeech.tts.PrompterEventAdapter
            public void eventStateChanged(int changedState) {
                if (changedState == 2) {
                    int unused = TTSEngine.mPlayState = 1;
                    TTSEngine.isStartTTS = false;
                } else {
                    int unused2 = TTSEngine.mPlayState = -1;
                }
                switch (changedState) {
                    case 3:
                        TTSEngine.isStartTTS = true;
                        MSG.l("eventStateChanged PLAYING");
                        break;
                    case 5:
                        TTSEngine.isStartTTS = false;
                        if (TTSEngine.mPlayCount == -1) {
                            TTSEngine.PlayTTS();
                        } else if (TTSEngine.access$306() <= 0) {
                            String unused3 = TTSEngine.mPlayString = null;
                            MSG.l("eventStateChanged STOP");
                        } else {
                            TTSEngine.PlayTTS();
                            MSG.l("eventStateChanged REPLAY");
                        }
                        if (TTSEngine.nextWordCallback != null) {
                            TTSEngine.nextWordCallback.setNextWordAfterTTsPlayed();
                            break;
                        }
                        break;
                }
                MSG.l("eventStateChanged " + changedState);
                MSG.l("mPlayState" + TTSEngine.mPlayState);
            }
        };
    }

    public static void setNextWordCallback(OnTTSPlayed callback) {
        nextWordCallback = callback;
    }

    public static int InitTTS() {
        if (!Dependency.isContainTTS()) {
            return 0;
        }
        languageCode = "en-US";
        if (mInitialState != 2) {
            return 0;
        }
        try {
            prompterMgr = PrompterManager.getInstance();
            prompterMgr.initialize(DictInfo.TTSPATH);
            try {
                prompter = prompterMgr.createPrompterInstance("Default");
                prompter.setVoice(languageCode);
                prompter.setEventAdapter(promptereventadapter);
                mPlayState = 1;
                mInitialState = 3;
                return 0;
            } catch (ResultCode e) {
                MSG.l(2, "Fail : InitTTS() createPrompterInstance");
                e.printStackTrace();
                return 2;
            }
        } catch (ResultCode e1) {
            MSG.l(2, "Fail : InitTTS() initialize");
            e1.printStackTrace();
            return 1;
        }
    }

    public static int DestroyTTS() {
        if (mInitialState == 3 && prompter != null) {
            try {
                if (prompter != null) {
                    prompter.setEventAdapter(null);
                    promptereventadapter = null;
                    prompterMgr.destroyPrompterInstance(prompter);
                    prompter = null;
                }
                if (prompterMgr != null) {
                    PrompterManager.destroyInstance();
                    prompterMgr = null;
                    mInitialState = 2;
                }
                keywordString = null;
                mPlayString = null;
                languageCode = null;
                return 0;
            } catch (ResultCode e) {
                e.printStackTrace();
                MSG.l(2, "Fail : DestroyTTS() destroyPrompterInstance : " + e);
                return 3;
            }
        }
        return 0;
    }

    public static int PlayTTS(int isUsUk, String keyword, String playstr, int dbType, int count) {
        boolean isKeyword = false;
        if (keyword == null) {
            isKeyword = true;
        } else if (keyword.equals(playstr)) {
            isKeyword = true;
        }
        if (isKeyword && CommonUtils.isUselessTTSWord(playstr, dbType)) {
            return 0;
        }
        if (DictDBManager.getCpCHNDictionary(dbType) && isKeyword && playstr.contains("[") && isUsUk == 0) {
            usepronun = true;
            mPlayString = playstr;
            mPlayCount = count;
            if (mPlayString.length() <= 0) {
                return 4;
            }
            if (mPlayString.contains("二人转")) {
                usepronun = false;
                int idx = mPlayString.indexOf("[");
                mPlayString = mPlayString.substring(0, idx);
            } else {
                int firstIdx = mPlayString.indexOf("[");
                int lastIdx = mPlayString.indexOf("]");
                mPlayString = mPlayString.substring(firstIdx + 1, lastIdx);
                mPlayString = convertPinyinString(mPlayString);
                mPlayString = mPlayString.replace('\'', ' ');
                mPlayString = mPlayString.replace(',', ' ');
                mPlayString = mPlayString.replace((char) 8226, ' ');
                mPlayString = mPlayString.replace('~', ' ');
                mPlayString = mPlayString.replace((char) 65292, ' ');
                mPlayString = mPlayString.toLowerCase();
                mPlayString = "<phoneme alphabet='x-SVOX-pinyin_zh-CN' ph='" + mPlayString + "'>1</phoneme>";
            }
            if (mPlayState != 1) {
                return 5;
            }
            if (SetTTSVoiceLanguageInPronun(isUsUk, dbType) != 0) {
                return 0;
            }
            try {
                mPlayState = 2;
                if (prompter != null) {
                    prompter.playString(mPlayString);
                }
                return 0;
            } catch (ResultCode e) {
                MSG.l(2, "Fail : prompter.playString : " + e);
                return 5;
            }
        }
        usepronun = false;
        if (playstr.contains("[")) {
            int firstIdx2 = playstr.indexOf("[");
            playstr = playstr.substring(0, firstIdx2);
        }
        if (isStartTTS || mPlayState != 1) {
            MSG.l(2, "mPlayState : " + mPlayState);
            return 0;
        }
        keywordString = keyword;
        mPlayString = playstr;
        mPlayCount = count;
        if (mPlayString.length() <= 0) {
            return 4;
        }
        mPlayString = mPlayString.replace(',', ' ');
        if (mPlayString.contains("馬鈴薯")) {
            mPlayString = mPlayString.replace("馬鈴薯", "馬鈴薯");
        }
        String tilde = String.format("%c", 8764);
        if (keywordString != null) {
            if (mPlayString.contains(tilde)) {
                isUseTilde = true;
            } else if (mPlayString.contains("~")) {
                isUseTilde = true;
                tilde = "~";
            } else {
                isUseTilde = false;
            }
            if (isUseTilde) {
                mPlayString = mPlayString.replaceAll(tilde, keywordString);
            }
        }
        int err = SetTTSVoiceLanguage(isUsUk, mPlayString, dbType);
        if (err != 0) {
            return err;
        }
        try {
            mPlayState = 2;
            if (prompter != null) {
                prompter.playString(mPlayString);
            }
            return 0;
        } catch (ResultCode e2) {
            MSG.l(2, "Fail : prompter.playString : " + e2);
            return 5;
        }
    }

    public static int PlayTTS() {
        if (prompter == null) {
            MSG.l(2, "PlayTTS() prompter is null: mustbe check!!");
            return 1;
        } else if (mPlayString.length() <= 0) {
            return 4;
        } else {
            try {
                mPlayState = 2;
                if (prompter != null) {
                    prompter.playString(mPlayString);
                }
                return 0;
            } catch (ResultCode e) {
                MSG.l(2, "Fail : PlayTTS()");
                e.printStackTrace();
                return 5;
            }
        }
    }

    public static int StopTTS() {
        MSG.l(1, "StopTTS");
        if (prompter == null) {
            MSG.l(2, "StopTTS() prompter is null: mustbe check!!");
            return 1;
        }
        try {
            if (prompter != null && isPlaying()) {
                prompter.abort();
            }
            mPlayCount = 0;
            return 0;
        } catch (ResultCode e) {
            MSG.l(2, "Warning: Fail : StopTTS");
            e.printStackTrace();
            return 6;
        }
    }

    public static int SetTTSVoiceLanguageInPronun(int isUsUk, int DicType) {
        int lan = isUsUk + 3;
        return requestSetParameter(lan);
    }

    public static int SetTTSVoiceLanguage(int isUsUk, String str, int DbType) {
        int lan = 0;
        int codepage = DictUtils.getCodePage(str);
        switch (codepage) {
            case 0:
                lan = isUsUk + 0;
                break;
            case DictInfo.CP_JPN /* 932 */:
                lan = 5;
                break;
            case DictInfo.CP_CHN /* 936 */:
                if (Dependency.isChina()) {
                    lan = isUsUk + 3;
                    break;
                } else if (DictDBManager.getCpJPNDictionary(DbType)) {
                    lan = 5;
                    break;
                } else if (DictDBManager.getCpKORDictionary(DbType)) {
                    lan = 2;
                    break;
                } else if (DictDBManager.getCpCHNDictionary(DbType)) {
                    lan = isUsUk + 3;
                    break;
                } else {
                    int pairDbType = DictDBManager.getPairDicTypeByCurDicType(DbType);
                    if (DictDBManager.getCpJPNDictionary(pairDbType)) {
                        lan = 5;
                        break;
                    } else if (DictDBManager.getCpKORDictionary(pairDbType)) {
                        lan = 2;
                        break;
                    } else if (DictDBManager.getCpCHNDictionary(pairDbType)) {
                        lan = isUsUk + 3;
                        break;
                    }
                }
                break;
            case DictInfo.CP_KOR /* 949 */:
                lan = 2;
                break;
            default:
                if (DictUtils.ISLATIN_CP(codepage)) {
                    lan = isUsUk + 0;
                    break;
                } else {
                    MSG.l("SetTTSVoiceLanguage : default");
                    return 7;
                }
        }
        if (lan < 0 || lan > TTSVOICE_LANG.length) {
            MSG.l("TTS_ERR_PLAY_LENGTH_0");
            return 4;
        }
        languageCode = TTSVOICE_LANG[lan];
        return requestSetParameter(lan);
    }

    private static int requestSetParameter(int lan) {
        try {
            MSG.l(1, "requestSetParameter(int lan)");
            if (prompter != null) {
                MSG.l(1, "prompter.setVoice(TTSVOICE_LANG[lan]);");
                prompter.setVoice(TTSVOICE_LANG[lan]);
            }
            languageCode = TTSVOICE_LANG[lan];
            return 0;
        } catch (ResultCode e) {
            MSG.l(2, " requestPlayTTS(): prompter.setVoice is fail : " + e);
            e.printStackTrace();
            if (mPlayState == -1) {
                try {
                    MSG.l(1, "mPlayState == PLAY_STATE_INVALID");
                    prompter = prompterMgr.createPrompterInstance("Default");
                    prompter.setVoice(TTSVOICE_LANG[lan]);
                    mPlayState = 1;
                    isStartTTS = false;
                } catch (ResultCode e1) {
                    MSG.l(2, "Fail : InitTTS() createPrompterInstance : " + e1);
                    e1.printStackTrace();
                    return 2;
                }
            }
            return 8;
        }
    }

    public static boolean isPlayIdleState() {
        return mPlayState == 1;
    }

    public static String getPrefixHCILAB() {
        return HCILAB_TTS;
    }

    public static String getPrefixSVOX() {
        return SVOX_TTS;
    }

    public static String convertPinyinString(String word) {
        if (word == null) {
            return "";
        }
        if (isUsePreZHCNTTS) {
            for (int i = 0; i < EXCEPTION_TTSWORD.length; i++) {
                if (word.contains(EXCEPTION_TTSWORD[i])) {
                    word = word.replace(EXCEPTION_TTSWORD[i], PINYINWORD[i]);
                }
            }
            if (word.contains(" y") && word.indexOf(" y") == 0) {
                word = word.substring(word.indexOf(" y") + 1);
            }
            if (word.contains(" r")) {
                int indexOfLastR = word.lastIndexOf(" r");
                if (indexOfLastR == 0) {
                    word = word.substring(indexOfLastR + 1);
                } else {
                    if (indexOfLastR == word.length() - 2 || word.charAt(indexOfLastR + 2) == ' ') {
                        if (word.charAt(indexOfLastR - 1) == 'e' || (word.charAt(indexOfLastR - 1) >= '1' && word.charAt(indexOfLastR - 1) <= '4' && word.charAt(indexOfLastR - 2) == 'e')) {
                            if (word.length() > indexOfLastR + 2) {
                                word = word.substring(0, indexOfLastR) + "r" + word.substring(indexOfLastR + 2);
                            } else {
                                word = word.substring(0, indexOfLastR) + "r";
                            }
                        } else {
                            word = word.substring(0, indexOfLastR) + "r";
                            if (word.contains("yangr") || word.contains("ya1ngr") || word.contains("ya2ngr") || word.contains("ya3ngr") || word.contains("ya4ngr") || word.contains("yi3ngr") || word.contains("yongr") || word.contains("yo1ngr") || word.contains("yo2ngr") || word.contains("yo3ngr") || word.contains("yo4ngr")) {
                                if (word.length() > indexOfLastR + 3) {
                                    word = word.substring(0, word.lastIndexOf("r")) + "er" + word.substring(indexOfLastR + 3);
                                } else {
                                    word = word.substring(0, word.lastIndexOf("r")) + "er";
                                }
                            }
                        }
                    } else if ((word.charAt(indexOfLastR - 1) == 'e' || (word.charAt(indexOfLastR - 1) >= '1' && word.charAt(indexOfLastR - 1) <= '4' && word.charAt(indexOfLastR - 2) == 'e')) && word.length() > indexOfLastR + 2 && !isChnVowel(word.charAt(indexOfLastR + 2))) {
                        word = word.substring(0, indexOfLastR) + "r " + word.substring(indexOfLastR + 2);
                    }
                    if (word.lastIndexOf(" r ") != -1) {
                        word = word.replace(" r ", "r ");
                    }
                }
            }
            if (word.contains("qia1ngu")) {
                word = word.replace("qia1ngu", "qia1n gu");
            }
            if (word.contains("yi1na")) {
                word = word.replace("yi1na", "yi1 na");
            }
            if (word.contains("yi2na")) {
                word = word.replace("yi2na", "yi2 na");
            }
            if (word.contains("yi3na")) {
                word = word.replace("yi3na", "yi3 na");
            }
            if (word.contains("mu3nia2ng")) {
                word = word.replace("mu3nia2ng", " mu3 nia2ng");
            }
            if (word.contains("munia2ng")) {
                word = word.replace("munia2ng", " mu nia2ng");
            }
            if (word.contains("sa1nga")) {
                word = word.replace("sa1nga", "sa1n ga");
            }
            if (word.contains("sa1nge")) {
                word = word.replace("sa1nge", "sa1n ge");
            }
            if (word.contains("sa1ngu")) {
                word = word.replace("sa1ngu", "sa1n gu");
            }
            if (word.contains("zha4ngo1ng")) {
                word = word.replace("zha4ngo1ng", "zha4n go1ng");
            }
            if (word.contains("/")) {
                word = word.substring(0, word.indexOf(47));
            }
        } else {
            for (int i2 = 0; i2 < EXCEPTION_TTSWORD.length; i2++) {
                if (word.contains(EXCEPTION_TTSWORD[i2])) {
                    word = word.replace(EXCEPTION_TTSWORD[i2], PINYINWORD[i2]);
                }
            }
            if (word.contains(" r")) {
                word = word.replace(" r", "r");
            }
            int[] vowelList = new int[20];
            int listIndex = 0;
            StringBuffer tmpWord = new StringBuffer();
            tmpWord.append(word);
            for (int i3 = 0; i3 < CHN_VOWEL.length; i3++) {
                while (true) {
                    int VowelIndex = tmpWord.lastIndexOf(CHN_VOWEL[i3]);
                    if (VowelIndex != -1) {
                        vowelList[listIndex] = VowelIndex;
                        tmpWord.replace(VowelIndex, VowelIndex + 1, " ");
                        listIndex++;
                    }
                }
            }
            for (int i4 = listIndex; i4 < vowelList.length; i4++) {
                vowelList[i4] = -1;
            }
            for (int i5 = 0; i5 < vowelList.length && vowelList[i5] != -1; i5++) {
                int VowelNumIndex = vowelList[i5] + 1;
                int ConsonantIndex = VowelNumIndex + 1;
                if (ConsonantIndex < word.length() && word.charAt(ConsonantIndex) != ' ') {
                    char num = word.charAt(VowelNumIndex);
                    StringBuffer final_consonant = new StringBuffer();
                    final_consonant.append("");
                    while (isChnVowel(word.charAt(ConsonantIndex)) && ConsonantIndex + 1 < word.length()) {
                        ConsonantIndex++;
                    }
                    if (word.charAt(ConsonantIndex) == 'n') {
                        final_consonant.append(word.charAt(ConsonantIndex));
                        if (ConsonantIndex + 1 < word.length() && word.charAt(ConsonantIndex) != ' ') {
                            if (word.charAt(ConsonantIndex + 1) == 'g') {
                                ConsonantIndex++;
                                final_consonant.append(word.charAt(ConsonantIndex));
                            }
                            if (ConsonantIndex + 1 < word.length() && word.charAt(ConsonantIndex) != ' ' && word.charAt(ConsonantIndex + 1) == 'r') {
                                ConsonantIndex++;
                                final_consonant.append(word.charAt(ConsonantIndex));
                            }
                        }
                        if (ConsonantIndex + 1 < word.length()) {
                            if (isChnVowel(word.charAt(ConsonantIndex + 1))) {
                                word = word.substring(0, VowelNumIndex) + final_consonant.substring(0, final_consonant.length() - 1) + num + final_consonant.substring(final_consonant.length() - 1, final_consonant.length()) + word.substring(ConsonantIndex + 1, word.length());
                            } else {
                                word = word.substring(0, VowelNumIndex) + ((Object) final_consonant) + num + word.substring(ConsonantIndex + 1, word.length());
                            }
                        } else {
                            word = word.substring(0, VowelNumIndex) + ((Object) final_consonant) + num + word.substring(ConsonantIndex + 1, word.length());
                        }
                    } else if (word.charAt(ConsonantIndex) == 'r') {
                        final_consonant.append("r");
                        if (ConsonantIndex + 1 < word.length()) {
                            if (isChnVowel(word.charAt(ConsonantIndex + 1))) {
                                word = word.substring(0, VowelNumIndex) + word.substring(VowelNumIndex + 1, ConsonantIndex) + final_consonant.substring(0, final_consonant.length() - 1) + num + final_consonant.substring(final_consonant.length() - 1, final_consonant.length()) + word.substring(ConsonantIndex + 1, word.length());
                            } else {
                                word = word.substring(0, VowelNumIndex) + ((Object) final_consonant) + num + word.substring(ConsonantIndex + 1, word.length());
                            }
                        } else {
                            word = word.substring(0, VowelNumIndex) + ((Object) final_consonant) + num + word.substring(ConsonantIndex + 1, word.length());
                        }
                    } else if (isChnVowel(word.charAt(ConsonantIndex))) {
                        word = word.substring(0, VowelNumIndex) + word.substring(VowelNumIndex + 1, ConsonantIndex + 1) + num + word.substring(ConsonantIndex + 1, word.length());
                    } else {
                        word = word.substring(0, VowelNumIndex) + word.substring(VowelNumIndex + 1, ConsonantIndex) + num + word.substring(ConsonantIndex, word.length());
                    }
                }
            }
        }
        if (word.contains("wo3 gua3n bao3 ni3 chi1 le zhe4 yao4 jiu4 hao3")) {
            return "wo3 guan3 bao3 ni3 chi1 le zhe4 yao4 jiu4 hao3";
        }
        if (word.contains("na3li")) {
            return "na2li3";
        }
        if (word.contains("ni3 suan4 lao3 ji3")) {
            return "ni3 suan4 lao2ji3";
        }
        if (word.contains("bu4shan4")) {
            return "bu2shan4";
        }
        return word;
    }

    private static boolean isChnVowel(char oneWord) {
        return oneWord == 'a' || oneWord == 'e' || oneWord == 'i' || oneWord == 'o' || oneWord == 'u' || oneWord == 252;
    }

    public static void setPlayModeIdle() {
        mPlayState = 1;
    }

    public static boolean isPlaying() {
        return isStartTTS;
    }
}
