package com.diotek.diodict;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.utils.GlobalOptions;

import org.knziha.metaline.Metaline;

import java.util.HashMap;

public final class Preference {
	private static Preference Instance;
	static long firstFlag;
	static long firstFlagStamp;
	SharedPreferences mPrefer;
	HashMap<String, Object> valueCache = new HashMap<>();
	SharedPreferences.Editor editor;
	
	public static Preference getInstance(Context context) {
		if (Instance==null) {
			Instance = new Preference(context);
		}
		return Instance;
	}
	
	private Preference(Context context) {
		mPrefer = context.getSharedPreferences("settings", 0);
		firstFlagStamp = firstFlag = mPrefer.getLong("FF", 0);
	}
	
	// IsFirstLoading
	@Metaline(flagPos=0) public static boolean firstLoading() { firstFlag=firstFlag; throw new RuntimeException(); }
	@Metaline(flagPos=0) public static void firstLoading(boolean val) { firstFlag=firstFlag; throw new RuntimeException(); }
	
	@Metaline(flagPos=1, flagSize=4, max=5) public static int fontSize() { firstFlag=firstFlag; throw new RuntimeException();}
	@Metaline(flagPos=1, flagSize=4, max=5) public static void fontSize(int val) { firstFlag=firstFlag; throw new RuntimeException();}
	
	@Metaline(flagPos=5, flagSize=4, max=5) public static int markColor() { firstFlag=firstFlag; throw new RuntimeException();}
	@Metaline(flagPos=5, flagSize=4, max=5) public static void markColor(int val) { firstFlag=firstFlag; throw new RuntimeException();}
	
	@Metaline(flagPos=9, flagSize=4, max=5) public static int fontTheme() { firstFlag=firstFlag; throw new RuntimeException();}
	@Metaline(flagPos=9, flagSize=4, max=5) public static void fontTheme(int val) { firstFlag=firstFlag; throw new RuntimeException();}
	
	@Metaline(flagPos=13, flagSize=4, max=5) public static int feedBackMode() { firstFlag=firstFlag; throw new RuntimeException();}
	@Metaline(flagPos=13, flagSize=4, max=5) public static void feedBackMode(int val) { firstFlag=firstFlag; throw new RuntimeException();}
	
	@Metaline(flagPos=14) public static boolean gestureRecog() { firstFlag=firstFlag; throw new RuntimeException(); }
	@Metaline(flagPos=14) public static void gestureRecog(boolean val) { firstFlag=firstFlag; throw new RuntimeException(); }
	@Metaline(flagPos=15) public static boolean refreshViewState() { firstFlag=firstFlag; throw new RuntimeException(); }
	@Metaline(flagPos=15) public static void refreshViewState(boolean val) { firstFlag=firstFlag; throw new RuntimeException(); }
	
	
	public static int getFontSize() {
		int ret = fontSize();
		if (ret==0) {
			return GlobalOptions.ldpi?2:0;
		}
		return ret-1;
	}
	
	public SharedPreferences getSharedPreferences() {
		return mPrefer;
	}
	
	public void check(boolean exitVM) {
		if (editor!=null || firstFlag!=firstFlagStamp) {
			edit().putLong("FF", firstFlagStamp = firstFlag);
			if (!exitVM && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				editor.apply();
			} else {
				editor.commit();
			}
			editor = null;
		}
	}
	
	public void putInt(String key, int value) {
		edit().putInt(key, value);
		valueCache.put(key, value);
	}
	
	public int getInt(String key, int value) {
		if (editor!=null) {
			Object ret = valueCache.get(key);
			if (ret!=null && ret.getClass()==Integer.class) {
				return (Integer) ret;
			}
		}
		return mPrefer.getInt(key, value);
	}
	
//	public void putBoolean(String key, boolean value) {
//		edit().putBoolean(key, value);
//		valueCache.put(key, value);
//	}
//
//	public boolean getBoolean(String key, boolean value) {
//		if (editor!=null) {
//			Object ret = valueCache.get(key);
//			if (ret!=null && ret.getClass()==Boolean.class) {
//				return (Boolean) ret;
//			}
//		}
//		return mPrefer.getBoolean(key, value);
//	}
	
	public void putString(String key, String value) {
		edit().putString(key, value);
		valueCache.put(key, value);
	}
	
	
	private SharedPreferences.Editor edit() {
		if (editor==null) {
			editor = mPrefer.edit();
		}
		return editor;
	}
	
	public String getString(String key, String value) {
		if (editor!=null) {
			Object ret = valueCache.get(key);
			if (ret!=null && ret.getClass()==String.class) {
				return (String) ret;
			}
		}
		return mPrefer.getString(key, value);
	}
}
