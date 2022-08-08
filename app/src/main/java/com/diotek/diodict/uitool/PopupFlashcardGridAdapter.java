package com.diotek.diodict.uitool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class PopupFlashcardGridAdapter extends SimpleAdapter {
    private Context mContext;
    private String mFolderName = "";

    public PopupFlashcardGridAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.mContext = null;
        this.mContext = context;
    }

    @Override // android.widget.SimpleAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ViewGroup view = new RelativeLayout(this.mContext);
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.flashcard_rowitem_s_copy_layout, view, true);
        }
        HashMap<String, Object> itemData = (HashMap) getItem(position);
        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.checkableFlashcard);
        if (checkbox != null && itemData != null && layout != null) {
            boolean bDisabled = false;
            if (this.mFolderName.compareTo(itemData.get(DictInfo.ListItem_WordbookName).toString()) == 0) {
                bDisabled = true;
            }
            setFlashcardBg(layout, Integer.parseInt(itemData.get(DictInfo.ListItem_WordbookFolderType).toString()), bDisabled);
            checkbox.setChecked(Boolean.parseBoolean(itemData.get(DictInfo.ListItem_WordbookFolderChecked).toString()));
            if (bDisabled) {
                checkbox.setEnabled(false);
                convertView.setClickable(true);
            } else {
                checkbox.setEnabled(true);
                convertView.setClickable(false);
            }
            TextView titleText = (TextView) convertView.findViewById(R.id.wordbooktitle);
            if (titleText != null) {
                titleText.setText(itemData.get(DictInfo.ListItem_WordbookName).toString());
            }
            TextView countText = (TextView) convertView.findViewById(R.id.numword);
            if (countText != null) {
                countText.setText(itemData.get(DictInfo.ListItem_WordCount).toString());
            }
        }
        return convertView;
    }

    public void setCurrentFolder(String folderName) {
        this.mFolderName = folderName;
    }

    public void setFlashcardBg(View v, int type, boolean bDisabled) {
        switch (type) {
            case 2:
                if (bDisabled) {
                    v.setBackgroundResource(R.drawable.card02_s_dim);
                    return;
                } else {
                    v.setBackgroundResource(R.drawable.card02_s);
                    return;
                }
            case 3:
                if (bDisabled) {
                    v.setBackgroundResource(R.drawable.card03_s_dim);
                    return;
                } else {
                    v.setBackgroundResource(R.drawable.card03_s);
                    return;
                }
            default:
                if (bDisabled) {
                    v.setBackgroundResource(R.drawable.card01_s_dim);
                    return;
                } else {
                    v.setBackgroundResource(R.drawable.card01_s);
                    return;
                }
        }
    }
}
