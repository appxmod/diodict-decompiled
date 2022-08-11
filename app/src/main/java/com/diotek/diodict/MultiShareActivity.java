package com.diotek.diodict;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.utils.CMN;
import com.diodict.decompiled.BuildConfig;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;

public class MultiShareActivity extends Activity {
	public static WeakReference<BaseActivity> activity = ViewUtils.DummyRef;
	//public final static ArrayList<WeakReference<BaseActivity>> activities = new ArrayList<>();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Intent result = new Intent();
		BaseActivity a = activity.get();
		if (a!=null) {
			String text = a.getTextTarget();
			CMN.Log("dtest::share::", text);
			result.putExtra(Intent.EXTRA_TEXT, text);
			result.putExtra("ext_invoker", BuildConfig.APPLICATION_ID);
		}
		setResult(RESULT_OK, result);
		
		finish();
	}
}
