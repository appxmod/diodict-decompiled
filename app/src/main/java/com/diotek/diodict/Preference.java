package com.diotek.diodict;

import android.content.Context;
import android.content.SharedPreferences;

import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.utils.GlobalOptions;

import org.knziha.metaline.Metaline;

public class Preference {
	static long firstFlag;
	SharedPreferences mPrefer;
	
	public Preference(Context context) {
		mPrefer = context.getSharedPreferences("pref", 0);
		if(firstFlag==0)
			firstFlag = mPrefer.getInt("FF", 0);
	}
	
	// IsFirstLoading
	@Metaline(flagPos=0) public static boolean firstLoading() { firstFlag=firstFlag; throw new RuntimeException(); }
	@Metaline(flagPos=0) public static boolean firstLoading(long SecondFlag) { firstFlag=firstFlag; throw new RuntimeException(); }
	
	@Metaline(flagPos=1, flagSize=4, max=5) public static int fontSize() { firstFlag=firstFlag; throw new RuntimeException();}
	@Metaline(flagPos=1, flagSize=4, max=5) public static void fontSize(int val) { firstFlag=firstFlag; throw new RuntimeException();}
	
	@Metaline(flagPos=5, flagSize=4, max=5) public static int markColor() { firstFlag=firstFlag; throw new RuntimeException();}
	@Metaline(flagPos=5, flagSize=4, max=5) public static void markColor(int val) { firstFlag=firstFlag; throw new RuntimeException();}
	
	public static int getFontSize() {
		int ret = fontSize();
		if (ret==0) {
			return GlobalOptions.ldpi?2:0;
		}
		return ret-1;
	}
	
	
	public void saveMarkerColor(int value) {
//		SharedPreferences pefSetLanguage = ;
//		SharedPreferences.Editor editor = pefSetLanguage.edit();
//		editor.putInt(DIODICT_SETTING_PREF_MARKER_PEN_COLOR_VALUE, value);
//		editor.commit();
	
	}
	
}
