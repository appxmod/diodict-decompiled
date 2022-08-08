package com.diotek.diodict;

import android.app.Activity;
import android.content.Context;
import java.util.HashMap;

/* loaded from: classes.dex */
public class NonConfigurationController {
    static final int HISTORYLISTMODE_DELETE = 1;
    static final int HISTORYLISTMODE_IDLE = 0;
    static final int HISTORYLISTMODE_NOITEM = 2;
    static final int SEARCHLIST_SEARCHMOTHOD_EXAMPLE = 4;
    static final int SEARCHLIST_SEARCHMOTHOD_HANGULRO = 8;
    static final int SEARCHLIST_SEARCHMOTHOD_IDIOM = 2;
    static final int SEARCHLIST_SEARCHMOTHOD_INITIAL = 32;
    static final int SEARCHLIST_SEARCHMOTHOD_SPELLCHECK = 16;
    static final int SEARCHLIST_SEARCHMOTHOD_TOTAL = 1024;
    static final int SEARCHLIST_SEARCHMOTHOD_WORD = 1;
    private static final String STATE_FLASHCARD_LASTPOS = "StateFlashcardLastPos";
    private static final String STATE_HISTORY_LASTPOS = "StateHistoryLastPos";
    private static final String STATE_HISTORY_LISTMODE = "StateHistoryListMode";
    private static final String STATE_HISTORY_SAVE_POPUP = "StateHistorySavePopup";
    private static final String STATE_HISTORY_SORT = "StateHistorySort";
    private static final String STATE_SEARCHLIST_FONT_POPUP = "StateSearchListFontPopup";
    private static final String STATE_SEARCHLIST_LASTPOS = "StateSearchListLastPos";
    private static final String STATE_SEARCHLIST_MARKER_POPUP = "StateSearchListMarkerPopup";
    private static final String STATE_SEARCHLIST_MEANDICT = "StateSearchListMeanDict";
    private static final String STATE_SEARCHLIST_MEANPOS = "StateSearchListMeanPos";
    private static final String STATE_SEARCHLIST_MEANSUID = "StateSearchListMeanSUID";
    private static final String STATE_SEARCHLIST_MEANWORD = "StateSearchListMeanWord";
    private static final String STATE_SEARCHLIST_SAVE_POPUP = "StateSearchListSavePopup";
    private static final String STATE_SEARCHLIST_SEARCHMETHOD = "StateSearchListSearchMethod";
    private static final String STATE_SEARCH_EDIT_TEXT = "StateSearchEditText";
    private static final String STATE_STUDY_PAGE = "StateStudyPage";
    private static final String STATE_STUDY_RESULT = "StateStudyResult";
    private static final String STATE_STUDY_RESULT_CORRECT_COUNT = "StateStudyResultCorrectCount";
    private static final String STATE_STUDY_RESULT_PAGE = "StateStudyResultPage";
    private static final String STATE_STUDY_RESULT_WRONG_COUNT = "StateStudyResultWrongCount";
    private Context mContext = null;
    private HashMap<String, Object> mState = null;
    private int mStateSearchListLastPos = 0;
    private boolean mStateSearchListSavePopup = false;
    private boolean mStateSearchListFontPopup = false;
    private boolean mStateSearchListMarkerPopup = false;
    private int mStateSearchListSearchMethod = 1;
    private String mStateSearchListMeanWord = "";
    private int mStateSearchListMeanSUID = 0;
    private int mStateSearchListMeanDict = 0;
    private int mStateSearchListMeanPos = 0;
    private int mStateHistoryLastPos = 0;
    private int mStateHistoryListMode = 0;
    private int mStateHistorySort = 3;
    private boolean mStateHistorySavePopup = false;
    private int mStateFlashcardLastPos = 0;
    private int mStateStudyPage = 0;
    private boolean mStateStudyResultPage = false;
    private int[] mStateStudyResult = {0};
    private int mStateStudyResultCorrectCount = 0;
    private int mStateStudyResultWrongCount = 0;
    private String mStateSearchEditTextString = null;

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Object onRetainNonConfigurationInstance() {
        this.mState = new HashMap<>();
        this.mState.put(STATE_SEARCHLIST_LASTPOS, Integer.valueOf(this.mStateSearchListLastPos));
        this.mState.put(STATE_SEARCHLIST_SAVE_POPUP, Boolean.valueOf(this.mStateSearchListSavePopup));
        this.mState.put(STATE_SEARCHLIST_FONT_POPUP, Boolean.valueOf(this.mStateSearchListFontPopup));
        this.mState.put(STATE_SEARCHLIST_MARKER_POPUP, Boolean.valueOf(this.mStateSearchListMarkerPopup));
        this.mState.put(STATE_SEARCHLIST_SEARCHMETHOD, Integer.valueOf(this.mStateSearchListSearchMethod));
        this.mState.put(STATE_SEARCHLIST_MEANPOS, Integer.valueOf(this.mStateSearchListMeanPos));
        this.mState.put(STATE_HISTORY_LASTPOS, Integer.valueOf(this.mStateHistoryLastPos));
        this.mState.put(STATE_HISTORY_LISTMODE, Integer.valueOf(this.mStateHistoryListMode));
        this.mState.put(STATE_HISTORY_SORT, Integer.valueOf(this.mStateHistorySort));
        this.mState.put(STATE_HISTORY_SAVE_POPUP, Boolean.valueOf(this.mStateHistorySavePopup));
        this.mState.put(STATE_FLASHCARD_LASTPOS, Integer.valueOf(this.mStateFlashcardLastPos));
        this.mState.put(STATE_STUDY_PAGE, Integer.valueOf(this.mStateStudyPage));
        this.mState.put(STATE_STUDY_RESULT_PAGE, Boolean.valueOf(this.mStateStudyResultPage));
        this.mState.put(STATE_STUDY_RESULT, this.mStateStudyResult);
        this.mState.put(STATE_STUDY_RESULT_CORRECT_COUNT, Integer.valueOf(this.mStateStudyResultCorrectCount));
        this.mState.put(STATE_STUDY_RESULT_WRONG_COUNT, Integer.valueOf(this.mStateStudyResultWrongCount));
        this.mState.put(STATE_SEARCH_EDIT_TEXT, this.mStateSearchEditTextString);
        return this.mState;
    }

    public void clearState(boolean clearSearchListLastPos) {
        this.mStateSearchListSavePopup = false;
        this.mStateSearchListFontPopup = false;
        this.mStateSearchListMarkerPopup = false;
        this.mStateSearchListSearchMethod = 1;
        this.mStateSearchListMeanPos = 0;
        this.mStateHistoryLastPos = 0;
        this.mStateHistoryListMode = 0;
        this.mStateHistorySort = 3;
        this.mStateHistorySavePopup = false;
        this.mStateFlashcardLastPos = 0;
        this.mStateStudyPage = 0;
        this.mStateStudyResultPage = false;
        this.mStateStudyResultCorrectCount = 0;
        this.mStateStudyResultWrongCount = 0;
        if (clearSearchListLastPos) {
            this.mStateSearchListLastPos = 0;
        }
        this.mStateSearchEditTextString = "";
    }

    public boolean getStateSaved() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        return this.mState != null;
    }

    public void setStateSearchEditTextString(String text) {
        this.mStateSearchEditTextString = text;
    }

    public String getStateSearchEditTextString() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchEditTextString = (String) this.mState.get(STATE_SEARCH_EDIT_TEXT);
        }
        return this.mStateSearchEditTextString;
    }

    public void setStateSearchListLastPos(int nPos) {
        this.mStateSearchListLastPos = nPos;
    }

    public int getStateSearchListLastPos() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListLastPos = ((Integer) this.mState.get(STATE_SEARCHLIST_LASTPOS)).intValue();
        }
        return this.mStateSearchListLastPos;
    }

    public void setStateSearchListFontPopup(boolean mode) {
        this.mStateSearchListFontPopup = mode;
    }

    public boolean getStateSearchListFontPopup() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListFontPopup = ((Boolean) this.mState.get(STATE_SEARCHLIST_FONT_POPUP)).booleanValue();
        }
        return this.mStateSearchListFontPopup;
    }

    public void setStateSearchListMarkerPopup(boolean mode) {
        this.mStateSearchListMarkerPopup = mode;
    }

    public boolean getStateSearchListMarkerPopup() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListMarkerPopup = ((Boolean) this.mState.get(STATE_SEARCHLIST_MARKER_POPUP)).booleanValue();
        }
        return this.mStateSearchListMarkerPopup;
    }

    public void setStateSearchListSavePopup(boolean mode) {
        this.mStateSearchListSavePopup = mode;
    }

    public boolean getStateSearchListSavePopup() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListSavePopup = ((Boolean) this.mState.get(STATE_SEARCHLIST_SAVE_POPUP)).booleanValue();
        }
        return this.mStateSearchListSavePopup;
    }

    public void setStateSearchListSearchMethod(int mode) {
        this.mStateSearchListSearchMethod = mode;
    }

    public int getStateSearchListSearchMethod() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListSearchMethod = ((Integer) this.mState.get(STATE_SEARCHLIST_SEARCHMETHOD)).intValue();
        }
        return this.mStateSearchListSearchMethod;
    }

    public void setStateSearchListMeanWord(String word) {
        this.mStateSearchListMeanWord = word;
    }

    public String getStateSearchListMeanWord() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListMeanWord = (String) this.mState.get(STATE_SEARCHLIST_MEANWORD);
        }
        return this.mStateSearchListMeanWord;
    }

    public void setStateSearchListMeanSUID(int suid) {
        this.mStateSearchListSearchMethod = suid;
    }

    public int getStateSearchListMeanSUID() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListMeanSUID = ((Integer) this.mState.get(STATE_SEARCHLIST_MEANSUID)).intValue();
        }
        return this.mStateSearchListMeanSUID;
    }

    public void setStateSearchListMeanDict(int dict) {
        this.mStateSearchListMeanDict = dict;
    }

    public int getStateSearchListMeanDict() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListMeanDict = ((Integer) this.mState.get(STATE_SEARCHLIST_MEANDICT)).intValue();
        }
        return this.mStateSearchListMeanDict;
    }

    public void setStateSearchListMeanPos(int nPos) {
        this.mStateSearchListMeanPos = nPos;
    }

    public int getStateSearchListMeanPos() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateSearchListMeanPos = ((Integer) this.mState.get(STATE_SEARCHLIST_MEANPOS)).intValue();
        }
        return this.mStateSearchListMeanPos;
    }

    public void setStateHistoryLastPos(int nPos) {
        this.mStateHistoryLastPos = nPos;
    }

    public int getStateHistoryLastPos() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateHistoryLastPos = ((Integer) this.mState.get(STATE_HISTORY_LASTPOS)).intValue();
        }
        return this.mStateHistoryLastPos;
    }

    public void setStateHistoryListMode(int mode) {
        this.mStateHistoryListMode = mode;
    }

    public int getStateHistoryListMode() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateHistoryListMode = ((Integer) this.mState.get(STATE_HISTORY_LISTMODE)).intValue();
        }
        return this.mStateHistoryListMode;
    }

    public void setStateHistorySort(int mode) {
        this.mStateHistorySort = mode;
    }

    public int getStateHistorySort() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateHistorySort = ((Integer) this.mState.get(STATE_HISTORY_SORT)).intValue();
        }
        return this.mStateHistorySort;
    }

    public void setStateHistorySavePopup(boolean mode) {
        this.mStateHistorySavePopup = mode;
    }

    public boolean getStateHistorySavePopup() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateHistorySavePopup = ((Boolean) this.mState.get(STATE_HISTORY_SAVE_POPUP)).booleanValue();
        }
        return this.mStateHistorySavePopup;
    }

    public void setStateFlashcardLastPos(int nPos) {
        this.mStateFlashcardLastPos = nPos;
    }

    public int getStateFlashcardLastPos() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateFlashcardLastPos = ((Integer) this.mState.get(STATE_FLASHCARD_LASTPOS)).intValue();
        }
        return this.mStateFlashcardLastPos;
    }

    public void setStateStudyPage(int mode) {
        this.mStateStudyPage = mode;
    }

    public int getStateStudyPage() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateStudyPage = ((Integer) this.mState.get(STATE_STUDY_PAGE)).intValue();
        }
        return this.mStateStudyPage;
    }

    public void setStateStudyResultPage(boolean mode) {
        this.mStateStudyResultPage = mode;
    }

    public boolean getStateStudyResultPage() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateStudyResultPage = ((Boolean) this.mState.get(STATE_STUDY_RESULT_PAGE)).booleanValue();
        }
        return this.mStateStudyResultPage;
    }

    public void setStateStudyResult(int[] mode) {
        this.mStateStudyResult = mode;
    }

    public int[] getStateStudyResult() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateStudyResult = (int[]) this.mState.get(STATE_STUDY_RESULT);
        }
        return this.mStateStudyResult;
    }

    public void setStateStudyResultCorrectCount(int mode) {
        this.mStateStudyResultCorrectCount = mode;
    }

    public int getStateStudyResultCorrectCount() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateStudyResultCorrectCount = ((Integer) this.mState.get(STATE_STUDY_RESULT_CORRECT_COUNT)).intValue();
        }
        return this.mStateStudyResultCorrectCount;
    }

    public void setStateStudyResultWrongCount(int mode) {
        this.mStateStudyResultWrongCount = mode;
    }

    public int getStateStudyResultWrongCount() {
        this.mState = (HashMap) ((Activity) this.mContext).getLastNonConfigurationInstance();
        if (this.mState != null) {
            this.mStateStudyResultWrongCount = ((Integer) this.mState.get(STATE_STUDY_RESULT_WRONG_COUNT)).intValue();
        }
        return this.mStateStudyResultWrongCount;
    }
}
