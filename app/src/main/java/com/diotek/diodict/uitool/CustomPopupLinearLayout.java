package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import com.diotek.diodict.engine.DictUtils;

/* loaded from: classes.dex */
public class CustomPopupLinearLayout extends LinearLayout {
    Context mContext;
    CustomPopupLinearLayoutOnKeyListenerCallback mCustomPopupLinearLayoutOnKeyListenerCallback = null;

    /* loaded from: classes.dex */
    public interface CustomPopupLinearLayoutOnKeyListenerCallback {
        void runOnKeyListener(KeyEvent keyEvent);
    }

    public CustomPopupLinearLayout(Context context) {
        super(context);
        this.mContext = null;
        this.mContext = context;
    }

    public CustomPopupLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = null;
        this.mContext = context;
    }

    public void setOnKeyListenerCallback(CustomPopupLinearLayoutOnKeyListenerCallback customPopupLinearLayoutOnKeyListenerCallback) {
        this.mCustomPopupLinearLayoutOnKeyListenerCallback = customPopupLinearLayoutOnKeyListenerCallback;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 1) {
            if (event.getKeyCode() == 4 && this.mCustomPopupLinearLayoutOnKeyListenerCallback != null) {
                this.mCustomPopupLinearLayoutOnKeyListenerCallback.runOnKeyListener(event);
            }
        } else if (event.getAction() == 0 && DictUtils.setVolumeByKey(this.mContext, event.getKeyCode())) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
