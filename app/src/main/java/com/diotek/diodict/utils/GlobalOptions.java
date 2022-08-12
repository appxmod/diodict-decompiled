package com.diotek.diodict.utils;

import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;

public class GlobalOptions {
	public static boolean isLarge;
	public static boolean isSmall;
	public static boolean isDark;
	private static final float[] NEGATIVEMATRIX = {
			-1.0f,     0,     0,    0, 255, // red
			0, -1.0f,     0,    0, 255, // green
			0,     0, -1.0f,    0, 255, // blue
			0,     0,     0, 1.0f,   0  // alpha
	};
	public static final ColorMatrixColorFilter NEGATIVE = new ColorMatrixColorFilter(NEGATIVEMATRIX);
	public static final PorterDuffColorFilter WHITE = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
	public static final PorterDuffColorFilter BLACK = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


	public static boolean debug;
	
	public final static boolean chromium=Build.MANUFACTURER.equals("chromium");
	
	public static float density;
	
	public static boolean ldpi;
	
	public static int realWidth;
	
	public static int realHeight;
	
	public static int width;
	
	public static int height;
	
	public static int softInputHeight;
	
	public static int btnMaxWidth;
	
	public static boolean hideTextRecDlg;
}
