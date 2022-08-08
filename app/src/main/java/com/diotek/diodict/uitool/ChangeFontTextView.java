package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.diotek.diodict.engine.DictUtils;

/* loaded from: classes.dex */
public class ChangeFontTextView extends TextView {
    public ChangeFontTextView(Context context) {
        super(context);
    }

    public ChangeFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(DictUtils.createfont());
    }

    public ChangeFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
