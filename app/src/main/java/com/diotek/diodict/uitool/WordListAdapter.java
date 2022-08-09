package com.diotek.diodict.uitool;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class WordListAdapter extends ArrayAdapter<HashMap<String, Object>> {
    Context mContext;
    private EngineManager3rd mEngine;
    private View.OnClickListener mMemoClickListener = null;
    private int mTextViewResourceId;
    int mTextViewWidth;
    private ColorStateList mheaderTextColorState;

    public WordListAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, Object>> items) {
        super(context, textViewResourceId, items);
        this.mEngine = null;
        this.mTextViewResourceId = 0;
        this.mheaderTextColorState = null;
        this.mContext = context;
        this.mTextViewResourceId = textViewResourceId;
        this.mEngine = EngineManager3rd.getInstance(this.mContext);
        int focusColor = this.mContext.getResources().getColor(R.color.textColor_focus);
        int textColor = this.mContext.getResources().getColor(R.color.textColor_onBlackBackground);
        int[][] states = {new int[]{16842913}, new int[]{-16842913}};
        int[] colors = {focusColor, textColor};
        this.mheaderTextColorState = new ColorStateList(states, colors);
    }

    public void setMemoOnClickListener(View.OnClickListener listener) {
        this.mMemoClickListener = listener;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        HashMap<String, Object> itemData = getItem(position);
        String headerString = (String) itemData.get(DictInfo.ListItem_Header);
        if (headerString != null && headerString.length() > 0) {
            TextView headerView = new TextView(this.mContext.getApplicationContext());
            headerView.setGravity(16);
            headerView.setClickable(false);
            headerView.setBackgroundResource(R.drawable.sectionbar);
            headerView.setText(headerString);
            if (this.mheaderTextColorState != null) {
                headerView.setTextColor(this.mheaderTextColorState);
            }
            headerView.setPadding(((int) this.mContext.getResources().getDisplayMetrics().density) * 12, 0, 0, 0);
            headerView.setFocusable(false);
            headerView.setEnabled(false);
            return headerView;
        }
        int nDictType = Integer.parseInt(itemData.get(DictInfo.ListItem_DictType).toString());
        String word = itemData.get(DictInfo.ListItem_Keyword).toString();
        boolean IsTotalSearchList = IsTotalSearch(nDictType);
        if (DictDBManager.isOldKorDict(nDictType)) {
            word = word.substring(1);
        }
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService("layout_inflater");
        if (this.mTextViewResourceId == R.layout.flashcarditem_rowitem_copy_layout || this.mTextViewResourceId == R.layout.historyitem_rowitem_delete_layout) {
            v = vi.inflate(this.mTextViewResourceId, (ViewGroup) null);
        } else {
            View view = new RelativeLayout(getContext());
            v = vi.inflate(this.mTextViewResourceId, (ViewGroup) ((ViewGroup) view), true);
        }
        ImageView dictIcon = (ImageView) v.findViewById(R.id.dictIcon);
        if (dictIcon != null) {
            if (IsTotalSearchList || this.mTextViewResourceId != R.layout.search_rowitem_layout) {
                dictIcon.setVisibility(View.VISIBLE);
                dictIcon.setBackgroundDrawable(DictDBManager.getDictListIcon(nDictType));
            } else {
                dictIcon.setVisibility(View.GONE);
            }
        }
        ImageView memoIcon = (ImageView) v.findViewById(R.id.memo);
        if (memoIcon != null) {
            int suid = Integer.parseInt(itemData.get("suid").toString());
            if (DioDictDatabase.existMemo(this.mContext, nDictType, word, suid)) {
                Cursor c = DioDictDatabase.getMemoCursorWith(this.mContext, nDictType, word, suid);
                if (c != null) {
                    int nSkinIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE);
                    int skin = c.getInt(nSkinIdx);
                    c.close();
                    memoIcon.setBackgroundResource(getMemoSkinResourceId(skin));
                    memoIcon.setVisibility(View.VISIBLE);
                    memoIcon.setOnClickListener(this.mMemoClickListener);
                    memoIcon.setTag(Integer.valueOf(position));
                }
            } else {
                memoIcon.setVisibility(View.GONE);
            }
        }
        ChangeFontTextView wordtextView = (ChangeFontTextView) v.findViewById(R.id.word);
        if (word != null && wordtextView != null) {
            int nMinusWidth = 0;
            if (memoIcon != null && memoIcon.getVisibility() == 0) {
                nMinusWidth = 0 + memoIcon.getBackground().getIntrinsicWidth();
            }
            if (dictIcon != null && dictIcon.getVisibility() == 0) {
                nMinusWidth += dictIcon.getBackground().getIntrinsicWidth();
            }
            int wordTextViewWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.list_item_width);
            this.mTextViewWidth = wordTextViewWidth - nMinusWidth;
            if (dictIcon != null) {
                wordtextView.setTypeface(DictUtils.createfont());
            }
            wordtextView.setText(word);
        }
        return v;
    }

    private boolean IsTotalSearch(int nDictType) {
        return this.mEngine.getCurDict() == 65520;
    }

    private int getMemoSkinResourceId(int skin) {
        if (skin == 1) {
            return R.drawable.memo_skin_01;
        }
        if (skin == 2) {
            return R.drawable.memo_skin_02;
        }
        return R.drawable.memo_skin_03;
    }
}
