package com.diotek.diodict;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.diodict.decompiled.BuildConfig;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.utils.CMN;

import java.lang.ref.WeakReference;

public class MultiShareActivity extends Activity {
	public static WeakReference<BaseActivity> activity = ViewUtils.DummyRef;
	public static WeakReference dict_activity = ViewUtils.DummyRef;
	//public final static ArrayList<WeakReference<BaseActivity>> activities = new ArrayList<>();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String action = "plaindict.intent.action.GETTEXT";
		if (intent!=null) {
			action = intent.getAction();
		}
		if (Intent.ACTION_SEND.equals(action)
				|| "colordict.intent.action.SEARCH".equals(action)) {
			try {
				SearchListActivity a = (SearchListActivity) dict_activity.get();
				if (a == null) {
					intent.setClass(this, InitActivity.class);
					startActivity(intent);
				} else {
					a.processIntent(intent, false);
				}
			} catch (Exception e) {
				CMN.debug(e);
			}
		} else {
			Intent result = new Intent();
			BaseActivity a = activity.get();
			if (a!=null) {
				String text = a.getTextTarget();
				CMN.Log("dtest::share::", text);
				result.putExtra(Intent.EXTRA_TEXT, text);
				result.putExtra("ext_invoker", BuildConfig.APPLICATION_ID);
			}
			setResult(RESULT_OK, result);
		}
		finish();
	}
}
