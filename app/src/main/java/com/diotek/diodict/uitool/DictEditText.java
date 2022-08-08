package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictUtils;

/* loaded from: classes.dex */
public class DictEditText extends EditText {
    final int HINT_TEXT_SIZE = 15;
    final int NORMAL_TEXT_SIZE = 20;

    public DictEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        int imeOptions = 3;
        imeOptions = CommonUtils.isUseKeypadNoExtractUI() ? 3 | 268435456 : imeOptions;
        setInputType(0);
        setTextColor(-16777216);
        super.setImeOptions(imeOptions);
        if (!Dependency.isJapan()) {
            setTypeface(DictUtils.createfont());
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == 1) {
            setInputType(0);
        }
        boolean ret = super.onTouchEvent(event);
        setInputType(1);
        if (action == 1) {
            setCursorVisible(true);
        }
        return ret;
    }

    public void enableInputType(boolean isEnable) {
        if (isEnable) {
            setInputType(1);
        } else {
            setInputType(0);
        }
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        if (text.length() == 0) {
            setTextSize(2, 15.0f);
        } else {
            setTextSize(2, 20.0f);
        }
        super.onTextChanged(text, start, before, after);
    }
}
