package com.nemustech.tiffany.world;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class TFOverlayWindow {
    protected FrameLayout mEffectDecor;
    protected TFWindow mEffectWindow;
    protected int mHeight;
    protected int mWidth;
    protected WindowManager mWindowManager;
    protected int mX;
    protected int mY;

    public TFOverlayWindow(Context context) {
        init(context);
        setPosition(0, 0, -1, -1);
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public Window getEffectWindow() {
        return this.mEffectWindow;
    }

    public FrameLayout getEffectDecor() {
        return this.mEffectDecor;
    }

    public void setPosition(int x, int y) {
        setPosition(x, y, -1, -1);
    }

    public void setPosition(int x, int y, int width, int height) {
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getX() {
        return this.mX;
    }

    public int getY() {
        return this.mY;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void show() {
        show(null);
    }

    public void show(WindowManager.LayoutParams wl) {
        if (wl == null) {
            wl = this.mEffectWindow.getAttributes();
            if (this.mX != 0 || this.mY != 0 || this.mWidth >= 0 || this.mHeight >= 0) {
                Display d = this.mWindowManager.getDefaultDisplay();
                wl.x = this.mX;
                wl.y = this.mY;
                wl.width = this.mWidth < 0 ? d.getWidth() - this.mX : this.mWidth;
                wl.height = this.mHeight < 0 ? d.getHeight() - this.mY : this.mHeight;
            }
        }
        this.mWindowManager.addView(this.mEffectDecor, wl);
    }

    public void hide() {
        this.mEffectDecor.removeAllViews();
        this.mWindowManager.removeView(this.mEffectDecor);
    }

    public void setFocusable(boolean focusable) {
        if (focusable) {
            this.mEffectWindow.clearFlags(8);
        } else {
            this.mEffectWindow.addFlags(8);
        }
    }

    public void setTouchable(boolean touchable) {
        if (touchable) {
            this.mEffectWindow.clearFlags(16);
        } else {
            this.mEffectWindow.addFlags(16);
        }
    }

    public void stop() {
        hide();
    }

    protected void init(Context context) {
        Window w = ((Activity) context).getWindow();
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mEffectWindow = new TFWindow(context);
        this.mEffectWindow.setAttributes(w.getAttributes());
        this.mEffectDecor = (FrameLayout) this.mEffectWindow.getDecorView();
        setFocusable(false);
        setTouchable(false);
    }
}
