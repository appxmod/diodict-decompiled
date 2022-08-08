package com.diotek.diodict;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class GridViewAdapter extends SimpleAdapter {
    public static final int GRIDVIEW_MODE_CHANGEDICT = 1000;
    public static final int GRIDVIEW_MODE_DEFAULT = 0;
    public static final int GRIDVIEW_MODE_FLASHCARD = 1003;
    public static final int GRIDVIEW_MODE_FLASHCARD_DEL = 1005;
    public static final int GRIDVIEW_MODE_FLASHCARD_EDIT = 1004;
    public static final int GRIDVIEW_MODE_HYPERLIST = 1001;
    public static final int GRIDVIEW_MODE_HYPERLIST_ORIGIN = 1006;
    public static final int GRIDVIEW_MODE_HYPERLIST_SELECT_DICT = 1002;
    Context mContext;
    LayoutInflater mLayoutInflater;
    int mMode;
    int mOrientation;
    int mTotalItemCount;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GridViewAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        int i = 0;
        this.mContext = context;
        this.mMode = 0;
        this.mTotalItemCount = data != null ? data.size() : i;
        this.mOrientation = context.getResources().getConfiguration().orientation;
        this.mLayoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
    }

    public GridViewAdapter(Context context, List<HashMap<String, Object>> data, int resource, String[] from, int[] to, int mode) {
        super(context, data, resource, from, to);
        this.mContext = context;
        this.mMode = mode;
        this.mTotalItemCount = data != null ? data.size() : 0;
        this.mOrientation = context.getResources().getConfiguration().orientation;
        this.mLayoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
    }

    @Override // android.widget.SimpleAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.mMode == 1000) {
            return getChangeDictConvertView(convertView, position);
        }
        if (this.mMode == 1001) {
            return getHyperListConvertView(convertView, position);
        }
        if (this.mMode == 1006) {
            return getHyperOriginListConvertView(convertView, position);
        }
        if (this.mMode == 1002) {
            return getHyperListSelectDictConvertView(convertView, position);
        }
        if (this.mMode == 1003 || this.mMode == 1004 || this.mMode == 1005) {
            return getFlashcardConvertView(convertView, position, this.mMode);
        }
        return convertView;
    }

    private View getChangeDictConvertView(View v, int position) {
        HashMap<String, Object> item = (HashMap) getItem(position);
        int curDicType = EngineInfo3rd.getOriginalDicTypeByNotIndependenceDicType(EngineManager3rd.getInstance(this.mContext).getCurDict(), false);
        int posDicType = ((Integer) item.get(DictInfo.ListItem_DictType)).intValue();
        if (DictDBManager.getDictIndependence(curDicType) == 2) {
            curDicType = DictDBManager.getAutoDicType(curDicType, true, "");
        }
        if (v == null) {
            v = this.mLayoutInflater.inflate(R.layout.changedict_rowitem_layout, (ViewGroup) null);
        }
        ((ImageView) v.findViewById(R.id.changedicttype)).setImageDrawable((Drawable) item.get(DictInfo.ListItem_DictIcon));
        ImageView iv = (ImageView) v.findViewById(R.id.currentdicttypecheck);
        if (curDicType == posDicType) {
            iv.setVisibility(0);
        } else {
            iv.setVisibility(8);
        }
        return v;
    }

    private View getHyperListConvertView(View v, int position) {
        HashMap<String, Object> item = (HashMap) getItem(position);
        String str = (String) item.get(DictInfo.ListItem_Keyword);
        if (v == null) {
            v = this.mLayoutInflater.inflate(R.layout.hyper_popup_rowitem_layout, (ViewGroup) null);
        }
        TextView tv = (TextView) v.findViewById(R.id.word);
        tv.setText(str);
        ImageView view_vLine = (ImageView) v.findViewById(R.id.vertical_line);
        if (view_vLine != null) {
            if (position % 2 != 0) {
                view_vLine.setVisibility(4);
            } else {
                view_vLine.setVisibility(0);
            }
        }
        ImageView view_hLine = (ImageView) v.findViewById(R.id.hyper_list_top_line);
        if (view_hLine != null) {
            if (position > 1) {
                view_hLine.setVisibility(8);
            } else {
                view_hLine.setVisibility(0);
            }
        }
        ImageView view_hLine_bottom = (ImageView) v.findViewById(R.id.hyper_list_bottom_line);
        if (view_hLine_bottom != null) {
            int bottomStartIndex = this.mTotalItemCount - 1;
            if (this.mOrientation == 2 && this.mTotalItemCount % 2 == 0) {
                bottomStartIndex--;
            }
            if (position >= bottomStartIndex) {
                view_hLine_bottom.setVisibility(8);
            } else {
                view_hLine_bottom.setVisibility(0);
            }
        }
        return v;
    }

    private View getHyperOriginListConvertView(View v, int position) {
        HashMap<String, Object> item = (HashMap) getItem(position);
        String str = (String) item.get(DictInfo.ListItem_Keyword);
        if (v == null) {
            v = this.mLayoutInflater.inflate(R.layout.hyper_popup_rowitem_layout, (ViewGroup) null);
        }
        TextView tv = (TextView) v.findViewById(R.id.word);
        tv.setText(str);
        ImageView view_vLine = (ImageView) v.findViewById(R.id.vertical_line);
        if (view_vLine != null) {
            if (position % 2 != 0) {
                view_vLine.setVisibility(4);
            } else {
                view_vLine.setVisibility(0);
            }
        }
        ImageView view_hLine = (ImageView) v.findViewById(R.id.hyper_list_top_line);
        if (view_hLine != null) {
            if (this.mOrientation == 1) {
                if (position > 1) {
                    view_hLine.setVisibility(8);
                } else {
                    view_hLine.setVisibility(0);
                }
            } else {
                view_hLine.setVisibility(8);
            }
        }
        ImageView view_hLine_bottom = (ImageView) v.findViewById(R.id.hyper_list_bottom_line);
        if (view_hLine_bottom != null) {
            int bottomStartIndex = this.mTotalItemCount - 1;
            if (this.mOrientation == 2 && this.mTotalItemCount % 2 == 0) {
                bottomStartIndex--;
            }
            if (position >= bottomStartIndex) {
                view_hLine_bottom.setVisibility(8);
            } else {
                view_hLine_bottom.setVisibility(0);
            }
        }
        return v;
    }

    private View getHyperListSelectDictConvertView(View v, int position) {
        HashMap<String, Object> item = (HashMap) getItem(position);
        String str = (String) item.get(DictInfo.ListItem_Keyword);
        if (v == null) {
            v = this.mLayoutInflater.inflate(R.layout.hyper_popup_rowitem_layout, (ViewGroup) null);
        }
        TextView tv = (TextView) v.findViewById(R.id.word);
        tv.setText(str);
        if (this.mOrientation == 1) {
            Drawable icon = (Drawable) item.get(DictInfo.ListItem_DictIcon);
            float density = CommonUtils.getDeviceDensity(this.mContext);
            tv.setCompoundDrawablesWithIntrinsicBounds(icon, (Drawable) null, (Drawable) null, (Drawable) null);
            tv.setCompoundDrawablePadding((int) (12.0f * density));
        } else {
            ImageView view_vLine = (ImageView) v.findViewById(R.id.vertical_line);
            if (view_vLine != null) {
                if (position % 2 != 0) {
                    view_vLine.setVisibility(4);
                } else {
                    view_vLine.setVisibility(0);
                }
            }
            ImageView view_hLine = (ImageView) v.findViewById(R.id.hyper_list_top_line);
            if (view_hLine != null) {
                if (position > 1) {
                    view_hLine.setVisibility(8);
                } else {
                    view_hLine.setVisibility(0);
                }
            }
        }
        return v;
    }

    private View getFlashcardConvertView(View v, int position, int nMode) {
        HashMap<String, Object> item = (HashMap) getItem(position);
        boolean bDisabled = false;
        int nRowLayoutId = R.layout.flashcard_rowitem_layout;
        String name = item.get(DictInfo.ListItem_WordbookName).toString();
        int wordCount = ((Integer) item.get(DictInfo.ListItem_WordCount)).intValue();
        if (name.compareTo(DioDictDatabaseInfo.FOLDERNAME_MARKER) == 0) {
            bDisabled = true;
            nRowLayoutId = R.layout.flashcard_rowitem_marker_layout;
        } else if (name.compareTo(DioDictDatabaseInfo.FOLDERNAME_MEMO) == 0) {
            bDisabled = true;
            nRowLayoutId = R.layout.flashcard_rowitem_memo_layout;
        } else {
            switch (nMode) {
                case 1004:
                    nRowLayoutId = R.layout.flashcard_rowitem_edit_layout;
                    break;
                case GRIDVIEW_MODE_FLASHCARD_DEL /* 1005 */:
                    nRowLayoutId = R.layout.flashcard_rowitem_delete_layout;
                    break;
            }
        }
        View v2 = this.mLayoutInflater.inflate(nRowLayoutId, (ViewGroup) null, true);
        TextView tvTitle = (TextView) v2.findViewById(R.id.wordbooktitle);
        TextView tvWordCount = (TextView) v2.findViewById(R.id.numword);
        tvTitle.setText(name);
        tvWordCount.setText(Integer.toString(wordCount));
        ViewGroup bg = (ViewGroup) v2.findViewById(R.id.flashcard_bg);
        if (bg != null) {
            setFlashcardBg(bg, Integer.parseInt(item.get(DictInfo.ListItem_WordbookFolderType).toString()));
        }
        if (bDisabled && nMode != 1003) {
            v2.setEnabled(false);
            v2.setClickable(true);
        } else {
            v2.setEnabled(true);
            v2.setClickable(false);
        }
        return v2;
    }

    private void setFlashcardBg(View view, int type) {
        switch (type) {
            case 1:
                view.setBackgroundResource(R.drawable.card01);
                return;
            case 2:
                view.setBackgroundResource(R.drawable.card02);
                return;
            case 3:
                view.setBackgroundResource(R.drawable.card03);
                return;
            default:
                return;
        }
    }
}
