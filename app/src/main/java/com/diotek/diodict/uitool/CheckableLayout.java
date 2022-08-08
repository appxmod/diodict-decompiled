package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class CheckableLayout extends RelativeLayout implements Checkable {
    Checkable checkable;
    int checkableId;
    final String NS = "http://schemas.android.com/apk/res/android";
    final String ATTR = "checkable";

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.checkableId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "checkable", 0);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        this.checkable = (Checkable) findViewById(this.checkableId);
        if (this.checkable == null) {
            return false;
        }
        return this.checkable.isChecked();
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        this.checkable = (Checkable) findViewById(this.checkableId);
        if (this.checkable != null) {
            this.checkable.setChecked(checked);
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        this.checkable = (Checkable) findViewById(this.checkableId);
        if (this.checkable != null) {
            this.checkable.toggle();
        }
    }
}
