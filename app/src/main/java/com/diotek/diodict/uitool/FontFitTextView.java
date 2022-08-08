package com.diotek.diodict.uitool;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;
import com.diotek.diodict.engine.DictUtils;

/* loaded from: classes.dex */
public class FontFitTextView extends TextView {
    private float maxTextSize;
    private float minTextSize;
    private TextPaint testPaint;

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        this.testPaint = new TextPaint();
        this.testPaint.set(getPaint());
        this.testPaint.setTypeface(DictUtils.createfont());
        this.maxTextSize = getTextSize();
        if (this.maxTextSize < 40.0f) {
            this.maxTextSize = 40.0f;
        }
        this.minTextSize = 10.0f;
    }

    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0 && !text.equals("")) {
            int availableWidth = (textWidth - getPaddingLeft()) - getPaddingRight();
            int availableHeight = (textHeight - getPaddingBottom()) - getPaddingTop();
            float trySize = this.maxTextSize;
            this.testPaint.setTextSize(this.testPaint.density * trySize);
            while (true) {
                if (trySize <= this.minTextSize || getAllHeight(text, availableWidth) <= availableHeight) {
                    break;
                }
                trySize -= 1.0f;
                if (trySize <= this.minTextSize) {
                    trySize = this.minTextSize;
                    break;
                }
                this.testPaint.setTextSize(this.testPaint.density * trySize);
            }
            setTextSize(trySize);
        }
    }

    private int getAllHeight(String text, int availableWidth) {
        Rect tempR = new Rect();
        this.testPaint.getTextBounds(text, 0, text.length(), tempR);
        int n = (tempR.width() / availableWidth) + 2;
        float h = (float) (tempR.height() * 1.6d);
        return (int) (n * h);
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        refitText(text.toString(), getWidth(), getHeight());
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            refitText(getText().toString(), w, h);
        }
    }

    public float getMinTextSize() {
        return this.minTextSize;
    }

    public void setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
    }

    public float getMaxTextSize() {
        return this.maxTextSize;
    }

    public void setMaxTextSize(int manTextSize) {
        this.maxTextSize = manTextSize;
    }
}
