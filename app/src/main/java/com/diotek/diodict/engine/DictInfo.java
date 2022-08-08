package com.diotek.diodict.engine;

import android.app.AlertDialog;

/* loaded from: classes.dex */
public class DictInfo {
    public static final int CP_1250 = 1250;
    public static final int CP_BAL = 1257;
    public static final int CP_CHN = 936;
    public static final int CP_CRL = 21866;
    public static final int CP_ENG = 0;
    public static final int CP_ERR = -1;
    public static final int CP_HIN = 1331;
    public static final int CP_JPN = 932;
    public static final int CP_KOR = 949;
    public static final int CP_LT1 = 1252;
    public static final int CP_SPECIAL = 255;
    public static final int CP_TUR = 1254;
    public static final int CP_TWN = 950;
    public static final int DICTATION_CORRECT = 1;
    public static final int DICTATION_DEFAULT = 0;
    public static final int DICTATION_WRONG = 2;
    public static final String DIODICT_EXTERNAL2_DBPATH = "/sdcard/external_sd/DioDict3P/";
    public static final String DIODICT_EXTERNAL2_FONTPATH = "/sdcard/external_sd/DioDict3P/";
    public static final String DIODICT_EXTERNAL2_NOTIFYPATH = "/sdcard/external_sd/DioDict3P/notify/";
    public static final String DIODICT_EXTERNAL2_NPSEPATH = "/sdcard/external_sd/DioDict3P/npse/";
    public static final String DIODICT_EXTERNAL2_PATH = "/sdcard/external_sd/";
    public static final String DIODICT_EXTERNAL2_TTSPATH = "/sdcard/external_sd/DioDict3P/tts/Config/DioDict3";
    public static final String DIODICT_JPN_SYSTEM_PATH = "/system/etc/";
    public static final int ERR_FINISH = 3;
    public static final int ERR_NONE = 0;
    public static final int ERR_NOT_EXIST = 1;
    public static final int ERR_NOT_SUPPORT_DICT = 2;
    public static final int ERR_NOWLOADING = 4;
    public static final int FLASHCARD_MODE_DELETE = 2;
    public static final int FLASHCARD_MODE_EDIT = 1;
    public static final int FLASHCARD_MODE_EDIT_DISABLED = 3;
    public static final int FLASHCARD_MODE_NORMAL = 0;
    public static final int HANDLER_STUDY_CHECKTIME = 7;
    public static final int HANDLER_STUDY_CORRECT = 0;
    public static final int HANDLER_STUDY_FINISH = 2;
    public static final int HANDLER_STUDY_RESTUDY = 6;
    public static final int HANDLER_STUDY_SHOWMEAN = 5;
    public static final int HANDLER_STUDY_SLIDESHOW = 4;
    public static final int HANDLER_STUDY_START = 3;
    public static final int HANDLER_STUDY_WRONG = 1;
    public static final String INTENT_FLASHCARDITEM_MODE = "flashcarditem_mode";
    public static final String INTENT_FLASHCARD_ENTERING_MODE = "flashcard_entering";
    public static final String INTENT_FLASHCARD_FOLER_ID = "flashcard_folder_id";
    public static final String INTENT_HYPER_DICT = "hyper_dict";
    public static final String INTENT_HYPER_DICTNAME = "hyper_dictname";
    public static final String INTENT_HYPER_ISCHECKLIST = "hyper_ischeckList";
    public static final String INTENT_HYPER_MEANPOS = "hyper_meanpos";
    public static final String INTENT_HYPER_SHOW_CHANGEDICT = "hyper_changebutton";
    public static final String INTENT_HYPER_SHOW_SEARCH = "hyper_searchbutton";
    public static final String INTENT_HYPER_SUID = "hyper_suid";
    public static final String INTENT_HYPER_WORD = "hyper_word";
    public static final String INTENT_MEMO_INFO_DATA = "memo_data";
    public static final String INTENT_MEMO_INFO_DICT = "memo_dict";
    public static final String INTENT_MEMO_INFO_SKIN = "memo_skin";
    public static final String INTENT_MEMO_INFO_STATE = "memo_state";
    public static final String INTENT_MEMO_INFO_SUID = "memo_suid";
    public static final String INTENT_MEMO_INFO_TIME = "time_stamp";
    public static final String INTENT_MEMO_INFO_WORD = "memo_word";
    public static final String INTENT_RUN_BY_WIDGET = "run_by_widget";
    public static final String INTENT_SAVE_CURRENTDICT = "save_currentdict";
    public static final String INTENT_SAVE_INPUTWORD = "save_inputword";
    public static final String INTENT_SAVE_MEANPOS = "save_meanpos";
    public static final String INTENT_SAVE_SEARCHMETHOD = "save_searchmethod";
    public static final String INTENT_WORDBOOKFOLDERID = "wordbookfolder_id";
    public static final String INTENT_WORDBOOKNAME = "wordbookforder_name";
    public static final String INTENT_WORDCOUNT = "word_count";
    public static final String INTENT_WORDINFO_DICTTYPE = "wordinfo_dict_type";
    public static final String INTENT_WORDINFO_KEYWORD = "wordinfo_keyword";
    public static final String INTENT_WORDINFO_SUID = "wordinfo_suid";
    public static final String INTENT_WORDSEARCH = "wordsearch_bywidget";
    public static final int LISTITEM_HEADER_FRIDAY = 5;
    public static final int LISTITEM_HEADER_MONDAY = 1;
    public static final int LISTITEM_HEADER_NONE = 100;
    public static final int LISTITEM_HEADER_NONE_DELETEMODE = 101;
    public static final int LISTITEM_HEADER_OLDITEMS = 9;
    public static final int LISTITEM_HEADER_SATURDAY = 6;
    public static final int LISTITEM_HEADER_SUNDAY = 0;
    public static final int LISTITEM_HEADER_THISMONTH = 8;
    public static final int LISTITEM_HEADER_THURSDAY = 4;
    public static final int LISTITEM_HEADER_TODAY = 7;
    public static final int LISTITEM_HEADER_TUESDAY = 2;
    public static final int LISTITEM_HEADER_WEDNESDAY = 3;
    public static final int LISTITEM_HEADER_YESTERDAY = 10;
    public static final String ListItem_DictIcon = "DictIcon";
    public static final String ListItem_DictType = "DictType";
    public static final String ListItem_Header = "header";
    public static final String ListItem_Keyword = "keyword";
    public static final String ListItem_WordCount = "KeyWordCount";
    public static final String ListItem_WordbookFolderChecked = "KeyWordbookFolderChecked";
    public static final String ListItem_WordbookFolderId = "KeyWordbookFolderId";
    public static final String ListItem_WordbookFolderType = "KeyWordbookFolderType";
    public static final String ListItem_WordbookName = "KeyWordbookName";
    public static final String ListItem_cursorPos = "cursorPos";
    public static final String ListItem_suid = "suid";
    public static final int MAX_WORDBOOKFOLDER = 40;
    public static final int MEANVIEW_DELAY = 1;
    public static final int MEANVIEW_NODELAY = 0;
    public static final int RESULTLIST_TYPE_HYPER = 1;
    public static final int RESULTLIST_TYPE_NORMAL = 0;
    public static final int RESULTLIST_TYPE_SERVICE = 2;
    public static final int RESULT_HYPERTEXT_CANCEL = 10;
    public static final int RESULT_HYPERTEXT_OK = 9;
    public static final int RESULT_MEMO_EDIT_OK = 8;
    public static final int RUNNABLEMODE_BY_POS = 1;
    public static final int RUNNABLEMODE_DEFAULT = 1;
    public static final int RUNNABLEMODE_KEYWORD_INFO = 2;
    public static final int STUDY_CORRECT = 1;
    public static final int STUDY_DEFAULT = 0;
    public static final int STUDY_SHOW_MARK_DURATION = 2000;
    public static final int STUDY_WRONG = 2;
    public static final int TABVIEW_ABOUT = 4;
    public static final int TABVIEW_ALL = 0;
    public static final int TABVIEW_EXAMPLE = 3;
    public static final int TABVIEW_IDIOM = 2;
    public static final int TABVIEW_MEAN = 1;
    public static final String TTSPATH_LUA = "/filter.lua";
    public static int[] TTSList = {65536, EngineInfo3rd.TTS_CHINESE, EngineInfo3rd.TTS_JAPANESE, EngineInfo3rd.TTS_KOREAN};
    public static String[] EngTTSFileList = {"config.lua", "filter.lua", "svoxconfig.txt", "SVOXKEYS.txt", "en-US/dd_eng_to_kor.lua", "en-US/lh0lt2en-US22_sg.bin", "en-US/lh0lt2en-US22_sg_0.pil", "en-US/lh0lt2en-US22_sg_1.pil", "en-US/lh0lt2en-US22en-US_ta.bin", "en-US/svox-lh0lt2en-US22.bin", "en-US/ulx_en-US0.txt", "en-US/10_diotek_ptts30.key", "en-US/config/config.4", "en-US/eng_db/TTSDBAA.dat", "en-US/eng_db/TTSDBJJ.dat", "en-US/eng_dict/eng2pacc.bin", "en-US/eng_dict/eng2ptree.bin", "en-US/eng_dict/EngAnalDict.bin", "en-US/eng_dict/EngAnalDict.FST", "en-US/eng_dict/EngBreakWD.FST", "en-US/eng_dict/EngProb_bi.bin", "en-US/eng_dict/EngProb_lex.bin", "en-US/eng_dict/EngProb_lex.FST", "en-US/eng_dict/EngPron.bin", "en-US/eng_dict/EngPron.FST", "en-US/eng_dict/EngUserDict.txt", "en-US/eng_pros/engprd.bin4", "en-US/eng_table/eff.db", "en-US/eng_table/xd.4"};
    public static String[] ChnTTSFileList = {"config.lua", "filter.lua", "svoxconfig.txt", "SVOXKEYS.txt", "zh-CN/dd_chn_to_kor.lua", "zh-CN/jw0lt0zh-CN22_sg.pil", "zh-CN/jw0lt0zh-CN22_sg_0.pil", "zh-CN/jw0lt0zh-CN22zh-CN_ta.pil", "zh-CN/svox-jw0lt0zh-CN22.pil", "zh-CN/10_diotek_ptts30.key", "zh-CN/chi_db/TTSDBAA.dat", "zh-CN/chi_db/TTSDBJJ.dat", "zh-CN/chi_dict/ChiDict.bin", "zh-CN/chi_dict/ChiDict.FST", "zh-CN/chi_dict/ChiHZP.bin", "zh-CN/chi_dict/ChiHZP.FST", "zh-CN/chi_dict/ChiSeg.bin", "zh-CN/chi_dict/ChiSeg.FST", "zh-CN/chi_dict/ChiT2S.bin", "zh-CN/chi_dict/ChiTagR.bin", "zh-CN/chi_dict/ChiTagR.FST", "zh-CN/chi_dict/ChiTagS.bin", "zh-CN/chi_dict/ChiUserDict.txt", "zh-CN/chi_dict/chunking_rules.txt", "zh-CN/chi_pros/chiprd.bin0", "zh-CN/chi_table/cdb1.0", "zh-CN/chi_table/cdb2.0", "zh-CN/chi_table/xd.0", "zh-CN/config/config.0"};
    public static String[] JpnTTSFileList = {"config.lua", "filter.lua", "svoxconfig.txt", "SVOXKEYS.txt", "ja-JP/10_diotek_ptts30.key", "ja-JP/dd_jpn_to_kor.lua", "ja-JP/so0lt1ja-JP22_sg.bin", "ja-JP/so0lt1ja-JP22_sg_0.pil", "ja-JP/so0lt1ja-JP22_sg_1.pil", "ja-JP/so0lt1ja-JP22ja-JP_ta.bin", "ja-JP/svox-so0lt1ja-JP22.bin", "ja-JP/config/config.0", "ja-JP/jpn_db/TTSDBAA.dat", "ja-JP/jpn_db/TTSDBJJ.dat", "ja-JP/jpn_dict/JapConn.bin", "ja-JP/jpn_dict/JapDict.bin", "ja-JP/jpn_dict/JapDict.FST", "ja-JP/jpn_dict/JapEng.bin", "ja-JP/jpn_dict/JapEng.FST", "ja-JP/jpn_dict/JapKJT.bin", "ja-JP/jpn_dict/JapKJT.FST", "ja-JP/jpn_dict/JapPron.bin", "ja-JP/jpn_dict/JapRengo.bin", "ja-JP/jpn_dict/JapRengo.FST", "ja-JP/jpn_dict/JapRule.bin", "ja-JP/jpn_dict/JapUserDict.txt", "ja-JP/jpn_pros/jpnprd.bin0", "ja-JP/jpn_table/cdb1.0", "ja-JP/jpn_table/cdb2.0", "ja-JP/jpn_table/eff.db", "ja-JP/jpn_table/xd.0"};
    public static String[] KorTTSFileList = {"config.lua", "filter.lua", "svoxconfig.txt", "SVOXKEYS.txt", "/ko-KR/10_diotek_ptts30.key", "/ko-KR/dd_kor_to_kor.lua", "/ko-KR/sl1lt1ko-KR22_sg.pil", "/ko-KR/sl1lt1ko-KR22_sg_0.pil", "/ko-KR/sl1lt1ko-KR22_sg_1.pil", "/ko-KR/sl1lt1ko-KR22ko-KR_ta.pil", "/ko-KR/svox-sl1lt1ko-KR22.pil", "/ko-KR/config/config.0", "/ko-KR/db/TTSDBAA.dat", "/ko-KR/db/TTSDBJJ.dat", "/ko-KR/dict/0pronB.dat.new", "/ko-KR/dict/pron_new.dat.new", "/ko-KR/dict/pron_new.FST", "/ko-KR/dict/pronsd0.dat.new", "/ko-KR/dict/pronsd0.FST", "/ko-KR/dict/pronsd1.dat.new", "/ko-KR/dict/pronsd1.FST", "/ko-KR/dict/user.dic", "/ko-KR/kmorphdic/CnxtUniProb.dat.new", "/ko-KR/kmorphdic/CnxtUniProb.idx", "/ko-KR/kmorphdic/Connect.dat.new", "/ko-KR/kmorphdic/Connect.FST", "/ko-KR/kmorphdic/Connect.idx", "/ko-KR/kmorphdic/name.dat.new", "/ko-KR/kmorphdic/name.FST", "/ko-KR/kmorphdic/post.dat", "/ko-KR/kmorphdic/pre.dat.new", "/ko-KR/kmorphdic/pre.FST", "/ko-KR/kmorphdic/sait.dat.new", "/ko-KR/kmorphdic/sait.FST", "/ko-KR/kor_pros/korprd.bin0", "/ko-KR/prsc/bigram.idx", "/ko-KR/prsc/casef.dat.new", "/ko-KR/prsc/casef.FST", "/ko-KR/prsc/grammar.idx", "/ko-KR/prsc/sm_dic.dat.new", "/ko-KR/prsc/sm_dic.FST", "/ko-KR/table/0.cdb1", "/ko-KR/table/0.cdb2", "/ko-KR/table/cdp.dat.new", "/ko-KR/table/eff.db", "/ko-KR/table/english.bg", "/ko-KR/table/VH.tbl0", "/ko-KR/table/VV.tbl0", "/ko-KR/table/xd.0"};
    public static String[] SvoxChnYueTTSFileList = {"../../Plus/yue-CN/lp0lt1yue-CN22_sg.bin", "../../Plus/yue-CN/lp0lt1yue-CN22_sg_0.pil", "../../Plus/yue-CN/lp0lt1yue-CN22_sg_1.pil", "../../Plus/yue-CN/lp0lt1yue-CN22yue-CN_ta.bin", "../../Plus/yue-CN/svox-lp0lt1yue-CN22.bin"};
    public static String[] SvoxEngTTSFileList = {"../../Plus/en-US/lh0lt2en-US22_sg.bin", "../../Plus/en-US/lh0lt2en-US22_sg_0.pil", "../../Plus/en-US/lh0lt2en-US22_sg_1.pil", "../../Plus/en-US/lh0lt2en-US22en-US_ta.bin", "../../Plus/en-US/svox-lh0lt2en-US22.bin"};
    public static String[] SvoxEngUKTTSFileList = {"../../Plus/en-GB/kh0lt2en-GB22_sg.bin", "../../Plus/en-GB/kh0lt2en-GB22_sg_0.pil", "../../Plus/en-GB/kh0lt2en-GB22_sg_1.pil", "../../Plus/en-GB/kh0lt2en-GB22en-GB_ta.bin", "../../Plus/en-GB/svox-kh0lt2en-GB22.bin"};
    public static String[] SvoxJpnTTSFileList = {"../../Plus/ja-JP/so0lt1ja-JP22_sg.bin", "../../Plus/ja-JP/so0lt1ja-JP22_sg_0.pil", "../../Plus/ja-JP/so0lt1ja-JP22_sg_1.pil", "../../Plus/ja-JP/so0lt1ja-JP22ja-JP_ta.bin", "../../Plus/ja-JP/svox-so0lt1ja-JP22.bin"};
    public static String[] SvoxChnTTSFileList = {"../../Plus/zh-CN/jw0lt1zh-CN22_sg.bin", "../../Plus/zh-CN/jw0lt1zh-CN22_sg_0.pil", "../../Plus/zh-CN/jw0lt1zh-CN22_sg_1.pil", "../../Plus/zh-CN/jw0lt1zh-CN22zh-CN_ta.bin", "../../Plus/zh-CN/svox-jw0lt1zh-CN22.bin"};
    public static String[] SvoxKorTTSFileList = {"../../Plus/ko-KR/sl1lt1ko-KR22_sg.bin", "../../Plus/ko-KR/sl1lt1ko-KR22_sg_0.pil", "../../Plus/ko-KR/sl1lt1ko-KR22_sg_1.pil", "../../Plus/ko-KR/sl1lt1ko-KR22ko-KR_ta.bin", "../../Plus/ko-KR/svox-sl1lt1ko-KR22.bin"};
    public static String[] HCIEngTTSFileList = {"../../CJK_Pro/en-US/config/config.4", "../../CJK_Pro/en-US/eng_db/TTSDBAA.dat", "../../CJK_Pro/en-US/eng_db/TTSDBJJ.dat", "../../CJK_Pro/en-US/eng_dict/eng2pacc.bin", "../../CJK_Pro/en-US/eng_dict/eng2ptree.bin", "../../CJK_Pro/en-US/eng_dict/EngAnalDict.bin", "../../CJK_Pro/en-US/eng_dict/EngAnalDict.FST", "../../CJK_Pro/en-US/eng_dict/EngBreakWD.FST", "../../CJK_Pro/en-US/eng_dict/EngProb_bi.bin", "../../CJK_Pro/en-US/eng_dict/EngProb_lex.bin", "../../CJK_Pro/en-US/eng_dict/EngProb_lex.FST", "../../CJK_Pro/en-US/eng_dict/EngPron.bin", "../../CJK_Pro/en-US/eng_dict/EngPron.FST", "../../CJK_Pro/en-US/eng_dict/EngUserDict.txt", "../../CJK_Pro/en-US/eng_pros/engprd.bin4", "../../CJK_Pro/en-US/eng_table/eff.db", "../../CJK_Pro/en-US/eng_table/xd.4"};
    public static String[] HCIChnTTSFileList = {"../../CJK_Pro/zh-CN/chi_db/TTSDBAA.dat", "../../CJK_Pro/zh-CN/chi_db/TTSDBJJ.dat", "../../CJK_Pro/zh-CN/chi_dict/ChiDict.bin", "../../CJK_Pro/zh-CN/chi_dict/ChiDict.FST", "../../CJK_Pro/zh-CN/chi_dict/ChiHZP.bin", "../../CJK_Pro/zh-CN/chi_dict/ChiHZP.FST", "../../CJK_Pro/zh-CN/chi_dict/ChiSeg.bin", "../../CJK_Pro/zh-CN/chi_dict/ChiSeg.FST", "../../CJK_Pro/zh-CN/chi_dict/ChiT2S.bin", "../../CJK_Pro/zh-CN/chi_dict/ChiTagR.bin", "../../CJK_Pro/zh-CN/chi_dict/ChiTagR.FST", "../../CJK_Pro/zh-CN/chi_dict/ChiTagS.bin", "../../CJK_Pro/zh-CN/chi_dict/ChiUserDict.txt", "../../CJK_Pro/zh-CN/chi_dict/chunking_rules.txt", "../../CJK_Pro/zh-CN/chi_pros/chiprd.bin0", "../../CJK_Pro/zh-CN/chi_table/cdb1.0", "../../CJK_Pro/zh-CN/chi_table/cdb2.0", "../../CJK_Pro/zh-CN/chi_table/xd.0", "../../CJK_Pro/zh-CN/config/config.0"};
    public static String[] HCIJpnTTSFileList = {"../../CJK_Pro/ja-JP/config/config.0", "../../CJK_Pro/ja-JP/jpn_db/TTSDBAA.dat", "../../CJK_Pro/ja-JP/jpn_db/TTSDBJJ.dat", "../../CJK_Pro/ja-JP/jpn_dict/JapConn.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapDict.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapDict.FST", "../../CJK_Pro/ja-JP/jpn_dict/JapEng.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapEng.FST", "../../CJK_Pro/ja-JP/jpn_dict/JapKJT.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapKJT.FST", "../../CJK_Pro/ja-JP/jpn_dict/JapPron.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapRengo.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapRengo.FST", "../../CJK_Pro/ja-JP/jpn_dict/JapRule.bin", "../../CJK_Pro/ja-JP/jpn_dict/JapUserDict.txt", "../../CJK_Pro/ja-JP/jpn_pros/jpnprd.bin0", "../../CJK_Pro/ja-JP/jpn_table/cdb1.0", "../../CJK_Pro/ja-JP/jpn_table/cdb2.0", "../../CJK_Pro/ja-JP/jpn_table/eff.db", "../../CJK_Pro/ja-JP/jpn_table/xd.0"};
    public static String[] HCIKorTTSFileList = {"../../CJK_Pro/ko-KR/config/config.0", "../../CJK_Pro/ko-KR/db/TTSDBAA.dat", "../../CJK_Pro/ko-KR/db/TTSDBJJ.dat", "../../CJK_Pro/ko-KR/dict/0pronB.dat.new", "../../CJK_Pro/ko-KR/dict/pron_new.dat.new", "../../CJK_Pro/ko-KR/dict/pron_new.FST", "../../CJK_Pro/ko-KR/dict/pronsd0.dat.new", "../../CJK_Pro/ko-KR/dict/pronsd0.FST", "../../CJK_Pro/ko-KR/dict/pronsd1.dat.new", "../../CJK_Pro/ko-KR/dict/pronsd1.FST", "../../CJK_Pro/ko-KR/kmorphdic/CnxtUniProb.dat.new", "../../CJK_Pro/ko-KR/kmorphdic/CnxtUniProb.idx", "../../CJK_Pro/ko-KR/kmorphdic/Connect.dat.new", "../../CJK_Pro/ko-KR/kmorphdic/Connect.FST", "../../CJK_Pro/ko-KR/kmorphdic/Connect.idx", "../../CJK_Pro/ko-KR/kmorphdic/name.dat.new", "../../CJK_Pro/ko-KR/kmorphdic/name.FST", "../../CJK_Pro/ko-KR/kmorphdic/post.dat", "../../CJK_Pro/ko-KR/kmorphdic/pre.dat.new", "../../CJK_Pro/ko-KR/kmorphdic/pre.FST", "../../CJK_Pro/ko-KR/kmorphdic/sait.dat.new", "../../CJK_Pro/ko-KR/kmorphdic/sait.FST", "../../CJK_Pro/ko-KR/kor_pros/korprd.bin0", "../../CJK_Pro/ko-KR/prsc/bigram.idx", "../../CJK_Pro/ko-KR/prsc/casef.dat.new", "../../CJK_Pro/ko-KR/prsc/casef.FST", "../../CJK_Pro/ko-KR/prsc/grammar.idx", "../../CJK_Pro/ko-KR/prsc/sm_dic.dat.new", "../../CJK_Pro/ko-KR/prsc/sm_dic.FST", "../../CJK_Pro/ko-KR/table/0.cdb1", "../../CJK_Pro/ko-KR/table/0.cdb2", "../../CJK_Pro/ko-KR/table/cdp.dat.new", "../../CJK_Pro/ko-KR/table/eff.db", "../../CJK_Pro/ko-KR/table/english.bg", "../../CJK_Pro/ko-KR/table/VH.tbl0", "../../CJK_Pro/ko-KR/table/VV.tbl0", "../../CJK_Pro/ko-KR/table/xd.0"};
    public static int LIMITEDYEAR = 2011;
    public static int LIMITEDMONTH = 5;
    public static int LIMITEDDAY = 31;
    public static String INTENT_STARTSTATE = "startState";
    public static int INTENT_NEWSTART = 0;
    public static int INTENT_ALREADYSTART1ST = 1;
    public static int INTENT_ALREADYSTART2ND = 2;
    public static String DIODICT_LIB_NAME = "DioDict3EngineNativeB2C";
    public static String DIODICT_HANGUL_LIB_NAME = "DioDict3HangulCoreB2C";
    public static String TTS_LIB_NAME = "DioSpeechTTS-jni";
    public static String DIODICT_FONT_NAME = "DioDictFnt3.ttf";
    public static String DIODICT_DB_UHCPTABLE_7Z = null;
    public static String DBEXTENSION = ".dat";
    public static String DIODICTDATANAME = "diodict.db";
    public static String DIODICT_EXTERNAL_PATH = "/sdcard/";
    public static final String DIODICT_EXTERNAL_APPPATH = "DioDict3P/";
    public static final String DIODICT_EXTERNAL_DBPATH = DIODICT_EXTERNAL_PATH + DIODICT_EXTERNAL_APPPATH;
    public static final String DIODICT_EXTERNAL_FONTPATH = DIODICT_EXTERNAL_DBPATH;
    public static final String TTS_CONFIG_PATH = "tts/Config/DioDict3";
    public static final String DIODICT_EXTERNAL_TTSPATH = DIODICT_EXTERNAL_DBPATH + TTS_CONFIG_PATH;
    public static final String DIODICT_EXTERNAL_NOTIFYPATH = DIODICT_EXTERNAL_DBPATH + "notify/";
    public static final String DIODICT_EXTERNAL_NPSEPATH = DIODICT_EXTERNAL_DBPATH + "npse/";
    public static String EXTERNALPATH = DIODICT_EXTERNAL_PATH;
    public static String DBPATH = DIODICT_EXTERNAL_DBPATH;
    public static String FONTPATH = DIODICT_EXTERNAL_FONTPATH;
    public static String TTSPATH = DIODICT_EXTERNAL_TTSPATH;
    public static String FLASHCARDPATH = "";
    public static final String DIODICT_FLASHCARD_FOLDERNAME = "flashcard/";
    public static String FLASHCARDBACKUPPATH = DBPATH + DIODICT_FLASHCARD_FOLDERNAME;
    public static String NOTIFYPATH = "android_asset/notify/";
    public static String NPSEPATH = DIODICT_EXTERNAL_NPSEPATH;
    public static String NPSE_PLAYPATH = "/mnt/sdcard/";
    public static AlertDialog m_DBCheckDialog = null;
    public static String HWR_LIB_NAME = "gdhwr2";
    public static String mCurrentDBName = null;
    public static int mDictationResId = -1895825408;
    public static int MAX_HYPERTEXTWORD_LENGTH = 50;
}