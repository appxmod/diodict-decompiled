package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.diotek.diodict.engine.DictUtils;

/* loaded from: classes.dex */
public class CustomPopupRelativeLayout extends RelativeLayout {
    Context mContext;
    CustomPopupRelativeLayoutOnKeyListenerCallback mCustomPopupRelativeLayoutOnKeyListenerCallback = null;

    /* loaded from: classes.dex */
    public interface CustomPopupRelativeLayoutOnKeyListenerCallback {
        void runOnKeyListener(KeyEvent keyEvent);
    }

    public CustomPopupRelativeLayout(Context context) {
        super(context);
        this.mContext = null;
        this.mContext = context;
    }

    public CustomPopupRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = null;
        this.mContext = context;
    }

    public void setOnKeyListenerCallback(CustomPopupRelativeLayoutOnKeyListenerCallback customPopupRelativeLayoutOnKeyListenerCallback) {
        this.mCustomPopupRelativeLayoutOnKeyListenerCallback = customPopupRelativeLayoutOnKeyListenerCallback;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 1) {
            if (event.getKeyCode() == 4 && this.mCustomPopupRelativeLayoutOnKeyListenerCallback != null) {
                this.mCustomPopupRelativeLayoutOnKeyListenerCallback.runOnKeyListener(event);
            }
        } else if (event.getAction() == 0 && DictUtils.setVolumeByKey(this.mContext, event.getKeyCode())) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
