package com.diotek.diodict.mean;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.uitool.TabView;

/* loaded from: classes.dex */
public class SearchMeanController extends BaseMeanController {
    Runnable runCreateDialog = new Runnable() { // from class: com.diotek.diodict.mean.SearchMeanController.1
        @Override // java.lang.Runnable
        public void run() {
            SearchMeanController.this.createLoadingDialog();
        }
    };
    Runnable runRefreshView = new Runnable() { // from class: com.diotek.diodict.mean.SearchMeanController.2
        @Override // java.lang.Runnable
        public void run() {
            if (SearchMeanController.this.mRunnableMode == 2) {
                SearchMeanController.this.mDicType = SearchMeanController.this.mRunnableDicType;
                SearchMeanController.this.mWord = SearchMeanController.this.mRunnableWord;
                SearchMeanController.this.mSUID = SearchMeanController.this.mRunnableSUID;
            } else if (SearchMeanController.this.mRunnableMode == 1) {
                SearchMeanController.this.mWordPos = SearchMeanController.this.mRunnablePos;
                int nDicType = SearchMeanController.this.mEngine.getResultDicTypeByPos(SearchMeanController.this.mWordPos, SearchMeanController.this.mRunnableHyper);
                if (nDicType == -1) {
                    SearchMeanController.this.refreshEmptyView();
                    return;
                }
                SearchMeanController.this.mDicType = nDicType;
                SearchMeanController.this.mEngine.setDicType(nDicType);
                SearchMeanController.this.mWord = SearchMeanController.this.mEngine.getResultListKeywordByPos(SearchMeanController.this.mWordPos, SearchMeanController.this.mRunnableHyper);
                if (SearchMeanController.this.mWord == null) {
                    SearchMeanController.this.refreshEmptyView();
                    return;
                } else {
                    SearchMeanController.this.mSUID = SearchMeanController.this.mEngine.getResultListSUIDByPos(SearchMeanController.this.mWordPos, SearchMeanController.this.mRunnableHyper);
                }
            } else {
                SearchMeanController.this.mWordPos = 0;
                SearchMeanController.this.mDicType = SearchMeanController.this.mEngine.getResultDicTypeByPos(SearchMeanController.this.mWordPos, SearchMeanController.this.mRunnableHyper);
                if (SearchMeanController.this.mDicType == -1) {
                    SearchMeanController.this.refreshEmptyView();
                    return;
                }
                SearchMeanController.this.mEngine.setDicType(SearchMeanController.this.mDicType);
                SearchMeanController.this.mWord = SearchMeanController.this.mEngine.getResultListKeywordByPos(SearchMeanController.this.mWordPos, SearchMeanController.this.mRunnableHyper);
                SearchMeanController.this.mSUID = SearchMeanController.this.mEngine.getResultListSUIDByPos(SearchMeanController.this.mWordPos, SearchMeanController.this.mRunnableHyper);
            }
            SearchMeanController.this.refreshView();
        }
    };

    public SearchMeanController(Context context, TextView titleTextView, ExtendTextView contentTextView, ImageView bookmark, TabView tabView, EngineManager3rd engine, boolean addHistory, BaseMeanController.ThemeModeCallback themeModeCallback, FileLinkTagViewManager fileLinkTagViewManager, BaseMeanController.MeanControllerCallback ttsLayoutCallback) {
        super(context, titleTextView, contentTextView, bookmark, tabView, engine, addHistory, themeModeCallback, fileLinkTagViewManager, ttsLayoutCallback, 0);
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanView(String szTableName, String szFolderName, int nFolderId, int nSort, int nPos, boolean bRefreshView) {
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public void setMeanView(int nResultListType) {
        if (!this.mNowLoading) {
            this.mNowLoading = true;
            this.mRunnableMode = 1;
            this.mRunnableHyper = nResultListType;
            removeRefreshMessage();
            this.mHandler.postDelayed(this.runRefreshView, 200L);
        }
    }

    private void removeRefreshMessage() {
        super.removeMessage();
        this.mHandler.removeCallbacks(this.runRefreshView);
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanViewByPos(int nPos, int nResultListType) {
        if (this.mNowLoading) {
            return 4;
        }
        this.mNowLoading = true;
        this.mRunnableMode = 1;
        this.mRunnablePos = nPos;
        this.mRunnableHyper = nResultListType;
        removeRefreshMessage();
        this.mHandler.postDelayed(this.runRefreshView, 200L);
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanViewKeywordInfo(int dicType, String keyword, int suid, int isDelay) {
        if (this.mNowLoading) {
            return 4;
        }
        this.mNowLoading = true;
        this.mRunnableMode = 2;
        this.mRunnableDicType = dicType;
        this.mRunnableWord = keyword;
        this.mRunnableSUID = suid;
        removeRefreshMessage();
        this.mHandler.postDelayed(this.runRefreshView, 200L);
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public void onDestory() {
        super.onDestory();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.runRefreshView);
        }
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setTitleView(String szTableName, int nFolderId, int nSort, int nPos) {
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setTitleViewByPos(int nPos, int nResultListType) {
        return 0;
    }
}
