package com.nemustech.tiffany.world;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class TFWindow extends Window {
	FrameLayout mMyDecorView;
	private int mWindowFeatures = 65;
	
	public TFWindow(Context context) {
		super(context);
		this.mMyDecorView = null;
		this.mMyDecorView = new FrameLayout(context);
		CMN.debug("fatal TFWindow!");
	}
	
	@Override
	public void takeSurface(SurfaceHolder.Callback2 callback2) {
	
	}
	
	@Override
	public void takeInputQueue(InputQueue.Callback callback) {
	
	}
	
	public int getWindowFeatures() {
		return this.mWindowFeatures;
	}
	
	public void setWindowFeatures(int features) {
		this.mWindowFeatures = features;
	}
	
	@Override // android.view.Window
	public void addContentView(View view, ViewGroup.LayoutParams params) {
	}
	
	@Override // android.view.Window
	public void closeAllPanels() {
	}
	
	@Override // android.view.Window
	public void closePanel(int featureId) {
	}
	
	@Override // android.view.Window
	public View getCurrentFocus() {
		return null;
	}
	
	@Override // android.view.Window
	public View getDecorView() {
		return this.mMyDecorView;
	}
	
	@Override // android.view.Window
	public LayoutInflater getLayoutInflater() {
		return null;
	}
	
	@Override // android.view.Window
	public int getVolumeControlStream() {
		return 0;
	}
	
	//@Override
	public int getStatusBarColor() {
		return 0;
	}
	
	//@Override
	public void setStatusBarColor(int i) {
	
	}
	
	//@Override
	public int getNavigationBarColor() {
		return 0;
	}
	
	//@Override
	public void setNavigationBarColor(int i) {
	
	}
	
	@Override
	public void setDecorCaptionShade(int decorCaptionShade) {
	
	}
	
	@Override
	public void setResizingCaptionDrawable(Drawable drawable) {
	
	}
	
	@Override // android.view.Window
	public boolean isFloating() {
		return false;
	}
	
	@Override // android.view.Window
	public boolean isShortcutKey(int keyCode, KeyEvent event) {
		return false;
	}
	
	@Override // android.view.Window
	protected void onActive() {
	}
	
	@Override // android.view.Window
	public void onConfigurationChanged(Configuration newConfig) {
	}
	
	@Override // android.view.Window
	public void openPanel(int featureId, KeyEvent event) {
	}
	
	@Override // android.view.Window
	public View peekDecorView() {
		return null;
	}
	
	@Override // android.view.Window
	public boolean performContextMenuIdentifierAction(int id, int flags) {
		return false;
	}
	
	@Override // android.view.Window
	public boolean performPanelIdentifierAction(int featureId, int id, int flags) {
		return false;
	}
	
	@Override // android.view.Window
	public boolean performPanelShortcut(int featureId, int keyCode, KeyEvent event, int flags) {
		return false;
	}
	
	@Override // android.view.Window
	public void restoreHierarchyState(Bundle savedInstanceState) {
	}
	
	@Override // android.view.Window
	public Bundle saveHierarchyState() {
		return null;
	}
	
	@Override // android.view.Window
	public void setBackgroundDrawable(Drawable drawable) {
	}
	
	@Override // android.view.Window
	public void setChildDrawable(int featureId, Drawable drawable) {
	}
	
	@Override // android.view.Window
	public void setChildInt(int featureId, int value) {
	}
	
	@Override // android.view.Window
	public void setContentView(int layoutResID) {
	}
	
	@Override // android.view.Window
	public void setContentView(View view) {
	}
	
	@Override // android.view.Window
	public void setContentView(View view, ViewGroup.LayoutParams params) {
	}
	
	@Override // android.view.Window
	public void setFeatureDrawable(int featureId, Drawable drawable) {
	}
	
	@Override // android.view.Window
	public void setFeatureDrawableAlpha(int featureId, int alpha) {
	}
	
	@Override // android.view.Window
	public void setFeatureDrawableResource(int featureId, int resId) {
	}
	
	@Override // android.view.Window
	public void setFeatureDrawableUri(int featureId, Uri uri) {
	}
	
	@Override // android.view.Window
	public void setFeatureInt(int featureId, int value) {
	}
	
	@Override // android.view.Window
	public void setTitle(CharSequence title) {
	}
	
	@Override // android.view.Window
	public void setTitleColor(int textColor) {
	}
	
	@Override // android.view.Window
	public void setVolumeControlStream(int streamType) {
	}
	
	@Override // android.view.Window
	public boolean superDispatchKeyEvent(KeyEvent event) {
		return false;
	}
	
	@Override
	public boolean superDispatchKeyShortcutEvent(KeyEvent keyEvent) {
		return false;
	}
	
	@Override // android.view.Window
	public boolean superDispatchTouchEvent(MotionEvent event) {
		return false;
	}
	
	@Override // android.view.Window
	public boolean superDispatchTrackballEvent(MotionEvent event) {
		return false;
	}
	
	@Override
	public boolean superDispatchGenericMotionEvent(MotionEvent motionEvent) {
		return false;
	}
	
	@Override // android.view.Window
	public void takeKeyEvents(boolean get) {
	}
	
	@Override // android.view.Window
	public void togglePanel(int featureId, KeyEvent event) {
	}
	
	@Override
	public void invalidatePanelMenu(int i) {
	
	}
}