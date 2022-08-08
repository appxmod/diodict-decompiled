package com.diotek.diodict.uitool;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.diotek.diodict.engine.DictUtils;

/* loaded from: classes.dex */
public class ChangeFontButton extends Button {
    public ChangeFontButton(Context context) {
        super(context);
    }

    public ChangeFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(DictUtils.createfont());
    }

    public ChangeFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(DictUtils.createfont());
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int widhMeasureSpec, int heightMeasureSpec) {
        String string = (String) getText();
        setText((String) TextUtils.ellipsize(string, getPaint(), View.MeasureSpec.getSize(widhMeasureSpec), TextUtils.TruncateAt.END));
        super.onMeasure(widhMeasureSpec, heightMeasureSpec);
    }
}
