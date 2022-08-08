package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/* loaded from: classes.dex */
public class FixedViewFlipper extends ViewFlipper {
    public FixedViewFlipper(Context context) {
        super(context);
    }

    public FixedViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override // android.widget.ViewFlipper, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception e) {
            stopFlipping();
        }
    }
}
