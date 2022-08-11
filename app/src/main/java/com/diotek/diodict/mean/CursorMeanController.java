package com.diotek.diodict.mean;

import android.content.Context;
import android.database.Cursor;
import android.widget.ImageView;
import android.widget.TextView;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.uitool.TabView;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class CursorMeanController extends BaseMeanController {
    private int mTotalWordCount = 0;
    private String mWordbookFolderName = "";
    private Integer[] mDicTypes = null;
    Runnable runRefreshView = new Runnable() { // from class: com.diotek.diodict.mean.CursorMeanController.1
        @Override // java.lang.Runnable
        public void run() {
            Cursor savedWordCursor;
            if (CursorMeanController.this.mRunnableMode == 2) {
                CursorMeanController.this.mDicType = CursorMeanController.this.mRunnableDicType;
                CursorMeanController.this.mEngine.setDicType(CursorMeanController.this.mDicType);
                CursorMeanController.this.mWord = CursorMeanController.this.mRunnableWord;
                CursorMeanController.this.mSUID = CursorMeanController.this.mRunnableSUID;
            } else {
                CursorMeanController.this.mWordPos = CursorMeanController.this.mRunnablePos;
                if (CursorMeanController.this.mTableName == DioDictDatabaseInfo.TABLENAME_HISTORYITEM) {
                    savedWordCursor = DioDictDatabase.getHistoryCursor(CursorMeanController.this.mContext, CursorMeanController.this.mSort);
                } else if (CursorMeanController.this.mTableName == DioDictDatabaseInfo.TABLENAME_MEMO) {
                    savedWordCursor = CursorMeanController.this.mDicTypes != null ? DioDictDatabase.getMemoCursorByDictType(CursorMeanController.this.mContext, CursorMeanController.this.mDicTypes, CursorMeanController.this.mSort) : DioDictDatabase.getMemoCursor(CursorMeanController.this.mContext, CursorMeanController.this.mSort);
                } else if (CursorMeanController.this.mTableName != DioDictDatabaseInfo.TABLENAME_MARKER) {
                    String[] folderId = {String.valueOf(CursorMeanController.this.mFolderId)};
                    if (CursorMeanController.this.mDicTypes != null) {
                        savedWordCursor = DioDictDatabase.getWordbookItemCursorByDictType(CursorMeanController.this.mContext, CursorMeanController.this.mDicTypes, CursorMeanController.this.mFolderId, CursorMeanController.this.mSort);
                    } else {
                        savedWordCursor = DioDictDatabase.getWordbookItemCursor(CursorMeanController.this.mContext, CursorMeanController.this.mFolderId, CursorMeanController.this.mSort);
                    }
                } else {
                    savedWordCursor = CursorMeanController.this.mDicTypes != null ? DioDictDatabase.getMarkerCursorByDictType(CursorMeanController.this.mContext, CursorMeanController.this.mDicTypes, CursorMeanController.this.mSort) : DioDictDatabase.getMarkerCursor(CursorMeanController.this.mContext, CursorMeanController.this.mSort);
                }
                if (savedWordCursor == null) {
                    CursorMeanController.this.refreshEmptyView();
                    CursorMeanController.this.mNowLoading = false;
                    return;
                } else if (savedWordCursor.moveToPosition(CursorMeanController.this.mWordPos)) {
                    CursorMeanController.this.mTotalWordCount = savedWordCursor.getCount();
                    CursorMeanController.this.mDicType = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE));
                    CursorMeanController.this.mEngine.setDicType(CursorMeanController.this.mDicType);
                    CursorMeanController.this.mWord = savedWordCursor.getString(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME));
                    CursorMeanController.this.mSUID = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID));
                    savedWordCursor.close();
                } else {
                    savedWordCursor.close();
                    CursorMeanController.this.refreshEmptyView();
                    CursorMeanController.this.mNowLoading = false;
                    return;
                }
            }
            if (CursorMeanController.this.mIsDelay) {
                CursorMeanController.this.refreshView();
            } else if (!CursorMeanController.this.mIsDelay) {
                CursorMeanController.this.refreshViewNoDelay();
            }
        }
    };

    public CursorMeanController(Context context, TextView titleTextView, ExtendTextView contentTextView, ImageView bookmark, TabView tabView, EngineManager3rd engine, BaseMeanController.ThemeModeCallback themeModeCallback, FileLinkTagViewManager fileLinkTagViewManager, BaseMeanController.MeanControllerCallback ttsLayoutCallback) {
        super(context, titleTextView, contentTextView, bookmark, tabView, engine, false, themeModeCallback, fileLinkTagViewManager, ttsLayoutCallback, 0);
    }

    public CursorMeanController(Context context, TextView titleTextView, ExtendTextView contentTextView, ImageView bookmark, TabView tabView, EngineManager3rd engine, BaseMeanController.ThemeModeCallback themeModeCallback, FileLinkTagViewManager fileLinkTagViewManager, BaseMeanController.MeanControllerCallback ttsLayoutCallback, int loadingMode) {
        super(context, titleTextView, contentTextView, bookmark, tabView, engine, false, themeModeCallback, fileLinkTagViewManager, ttsLayoutCallback, loadingMode);
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public void setMeanView(int nResultListType) {
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanView(String szTableName, String szFolderName, int nFolderId, int nSort, int nPos, boolean bRefreshView) {
        if (this.mNowLoading) {
            return 4;
        }
        this.mRunnableMode = 1;
        this.mTableName = szTableName;
        this.mWordbookFolderName = szFolderName;
        this.mFolderId = nFolderId;
        this.mSort = nSort;
        this.mRunnablePos = nPos;
        this.mIgnoreMeanTab = !bRefreshView;
        removeRefreshMessage();
        if (!bRefreshView) {
            return 0;
        }
        this.mNowLoading = true;
        this.mHandler.postDelayed(this.runRefreshView, 200L);
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanViewByPos(int nPos, int isDelay) {
        if (this.mNowLoading) {
            return 4;
        }
        this.mRunnablePos = nPos;
        this.mWordPos = nPos;
        this.mNowLoading = true;
        this.mRunnableMode = 1;
        removeRefreshMessage();
        if (isDelay == 1) {
            this.mIsDelay = true;
            this.mHandler.postDelayed(this.runRefreshView, 200L);
            return 0;
        } else if (isDelay != 0) {
            return 0;
        } else {
            this.mIsDelay = false;
            this.mHandler.post(this.runRefreshView);
            return 0;
        }
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanViewKeywordInfo(int dicType, String keyword, int suid, int isDelay) {
        if (this.mNowLoading) {
            return 4;
        }
        removeRefreshMessage();
        this.mNowLoading = true;
        this.mRunnableMode = 2;
        this.mRunnableDicType = dicType;
        this.mRunnableWord = keyword;
        this.mRunnableSUID = suid;
        if (isDelay == 1) {
            this.mIsDelay = true;
            this.mHandler.postDelayed(this.runRefreshView, 200L);
            return 0;
        } else if (isDelay != 0) {
            return 0;
        } else {
            this.mIsDelay = false;
            this.mHandler.post(this.runRefreshView);
            return 0;
        }
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setTitleView(String szTableName, int nFolderId, int nSort, int nPos) {
        this.mTableName = szTableName;
        this.mFolderId = nFolderId;
        this.mSort = nSort;
        this.mRunnablePos = nPos;
        this.mWordPos = nPos;
        Cursor savedWordCursor = DioDictDatabase.getItemsCursor(this.mContext, this.mWordbookFolderName, this.mFolderId, this.mSort);
        if (savedWordCursor == null) {
            refreshEmptyView();
            return 1;
        } else if (!savedWordCursor.moveToPosition(this.mWordPos)) {
            savedWordCursor.close();
            refreshEmptyView();
            return 1;
        } else {
            this.mTotalWordCount = savedWordCursor.getCount();
            this.mDicType = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE));
            this.mEngine.setDicType(this.mDicType);
            this.mWord = savedWordCursor.getString(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME));
            this.mSUID = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID));
            this.mExistContentMean = loadTitle(false);
            setTitleView();
            savedWordCursor.close();
            return 0;
        }
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setTitleViewByPos(int nPos, int nResultListType) {
        this.mRunnablePos = nPos;
        this.mWordPos = nPos;
        Cursor savedWordCursor = DioDictDatabase.getItemsCursor(this.mContext, this.mWordbookFolderName, this.mFolderId, this.mSort);
        if (savedWordCursor == null) {
            refreshEmptyView();
            return 1;
        } else if (!savedWordCursor.moveToPosition(this.mWordPos)) {
            savedWordCursor.close();
            refreshEmptyView();
            return 1;
        } else {
            this.mTotalWordCount = savedWordCursor.getCount();
            this.mDicType = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE));
            this.mEngine.setDicType(this.mDicType);
            this.mWord = savedWordCursor.getString(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME));
            this.mSUID = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID));
            this.mExistContentMean = loadTitle(false);
            setTitleView();
            savedWordCursor.close();
            return 0;
        }
    }

    public void setDicTypeList(Integer[] nDicTypes) {
        this.mDicTypes = nDicTypes;
    }

    public int setPrevTitleView() {
        if (this.mWordPos == 0) {
            return 3;
        }
        int nPos = this.mWordPos - 1;
        return setTitleViewByPos(nPos, 0);
    }

    public int setNextTitleView() {
        if (this.mTotalWordCount == this.mWordPos + 1) {
            return 3;
        }
        int nPos = this.mWordPos + 1;
        return setTitleViewByPos(nPos, 0);
    }

    private void removeRefreshMessage() {
        super.removeMessage();
        super.dismissDialog();
        this.mNowLoading = false;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.runRefreshView);
        }
    }

    public int setPrevMeanView() {
        if (this.mWordPos == 0) {
            return 3;
        }
        int nPos = this.mWordPos - 1;
        return setMeanViewByPos(nPos, 0);
    }

    public int setNextMeanView() {
        if (this.mTotalWordCount == this.mWordPos + 1) {
            return 3;
        }
        int nPos = this.mWordPos + 1;
        return setMeanViewByPos(nPos, 0);
    }

    public int getTotalWordCount() {
        return this.mTotalWordCount;
    }

    public int setSort(int nSort) {
        Cursor savedWordCursor;
        this.mSort = nSort;
        if (this.mTableName == DioDictDatabaseInfo.TABLENAME_HISTORYITEM) {
            savedWordCursor = DioDictDatabase.getHistoryCursor(this.mContext, this.mSort);
            if (savedWordCursor == null) {
                refreshEmptyView();
                return 1;
            }
        } else {
            String[] folderId = {String.valueOf(this.mFolderId)};
            savedWordCursor = DioDictDatabase.getItemsCursor(this.mContext, this.mWordbookFolderName, this.mFolderId, this.mSort);
        }
        if (savedWordCursor != null) {
            if (!savedWordCursor.moveToPosition(this.mWordPos)) {
                savedWordCursor.close();
                refreshEmptyView();
                return 1;
            }
            this.mTotalWordCount = savedWordCursor.getCount();
            this.mDicType = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_DBTYPE));
            this.mEngine.setDicType(this.mDicType);
            this.mWord = savedWordCursor.getString(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME));
            this.mSUID = savedWordCursor.getInt(savedWordCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_SUID));
            savedWordCursor.close();
            return 0;
        }
        return 1;
    }

    public void setMeanTabMode() {
        this.mTabViewMeanOnly = true;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public void onDestory() {
        super.onDestory();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.runRefreshView);
        }
    }

    public void setMeanTitleTextSizeUpdateCallback(BaseMeanController.MeanTitleTextSizeUpdateCallback callback) {
        this.mTitleTextFontSizeUpdateCallback = callback;
    }

    public int getTitleFontSize(boolean bTitleOnly) {
        int nSize = (int) this.mContext.getResources().getDimension(R.dimen.cradle_titleOnly_textSize);
        int nFontGap = 6;
        int nMinFontSize = 17;
        int nCharNum = 13;
        if (!bTitleOnly) {
            nSize = (int) this.mContext.getResources().getDimension(R.dimen.cradle_title_textSize);
            nFontGap = 2;
            nMinFontSize = 15;
        }
        if (this.mContext.getResources().getConfiguration().orientation == 2) {
            nSize -= 2;
            nFontGap--;
            nCharNum = 9;
        }
        if (!DictDBManager.getCpENGDictionary(this.mDicType)) {
            nCharNum /= 2;
        }
        String titlestr = this.mTitleTextView.getText().toString();
        int nLineNum = titlestr.length() / nCharNum;
        if (nLineNum > 1) {
            nSize -= (nLineNum - 1) * nFontGap;
        }
        if (nSize < nMinFontSize) {
            int nSize2 = nMinFontSize;
            return nSize2;
        }
        return nSize;
    }
}
