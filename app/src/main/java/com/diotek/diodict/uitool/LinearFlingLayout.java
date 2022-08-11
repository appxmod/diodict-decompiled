package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class LinearFlingLayout extends LinearLayout {
	OnTouchListener touchListener;
	public LinearFlingLayout(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void setOnTouchListener(OnTouchListener l) {
		touchListener = l;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (touchListener!=null) touchListener.onTouch(this, ev);
		return super.dispatchTouchEvent(ev);
	}
}
