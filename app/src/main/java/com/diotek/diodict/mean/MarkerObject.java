package com.diotek.diodict.mean;

import android.graphics.Rect;
import android.text.Layout;
import java.io.Serializable;

/* loaded from: classes.dex */
public class MarkerObject implements Serializable {
    private static final long serialVersionUID = 1;
    private int mColor;
    public int mEndOffset;
    public int mStartOffset;

    public MarkerObject(int start, int end, int color) {
        if (start > end) {
            this.mStartOffset = end;
            this.mEndOffset = start;
        } else {
            this.mStartOffset = start;
            this.mEndOffset = end;
        }
        this.mColor = color;
    }

    public boolean contain(int offset) {
        return this.mStartOffset <= offset && offset <= this.mEndOffset;
    }

    public Rect getRect(Layout layout) {
        Rect r = new Rect();
        int startLine = layout.getLineForOffset(this.mStartOffset);
        r.left = (int) layout.getPrimaryHorizontal(this.mStartOffset);
        r.top = layout.getLineTop(startLine);
        r.bottom = layout.getLineBottom(startLine);
        r.right = (int) layout.getPrimaryHorizontal(this.mEndOffset);
        return r;
    }

    public int getColor() {
        return this.mColor;
    }
}
