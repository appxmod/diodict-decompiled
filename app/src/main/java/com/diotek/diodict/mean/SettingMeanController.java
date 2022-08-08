package com.diotek.diodict.mean;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.uitool.TabView;

/* loaded from: classes.dex */
public class SettingMeanController extends BaseMeanController {
    public SettingMeanController(Context context, TextView titleTextView, ExtendTextView contentTextView, ImageView bookmark, TabView tabView, EngineManager3rd engine, boolean addHistory, BaseMeanController.ThemeModeCallback themeModeCallback, FileLinkTagViewManager fileLinkTagViewManager) {
        super(context, titleTextView, contentTextView, bookmark, tabView, engine, addHistory, themeModeCallback, fileLinkTagViewManager, null, 0);
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanView(String szTableName, String szFolderName, int nFolderId, int nSort, int nPos, boolean bRefreshView) {
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public void setMeanView(int nResultListType) {
        refreshSettingView();
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanViewByPos(int nPos, int nResultListType) {
        return 0;
    }

    @Override // com.diotek.diodict.mean.BaseMeanController
    public int setMeanViewKeywordInfo(int dicType, String keyword, int suid, int isDelay) {
        return 0;
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
