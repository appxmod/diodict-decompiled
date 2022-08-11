/*
 *  Copyright © 2016, Turing Technologies, an unincorporated organisation of Wynne Plaga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.diotek.diodict;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.utils.GlobalOptions;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.utils.IU;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

public class ViewUtils {
	public static float density;
	
	public static Paint mRectPaint;
	public static Paint mRectPaintAlpha;
	
	public static int FloatTextBG = 0xffffff00;
	public static int FloatTextBGAlpha = 0x7fffff00;
	
	public static Rect rect = new Rect();
	
	/** 刷机后检测rom而不是检测生厂商。 set manually。  */
	public static boolean checkRom;
	
	public final static Cursor EmptyCursor=new AbstractWindowedCursor() {
		@Override
		public int getCount() {
			return 0;
		}
		public String[] getColumnNames() {
			return new String[0];
		}
	};
	public static final boolean littleCat = Build.VERSION.SDK_INT<=Build.VERSION_CODES.KITKAT;
	public static final boolean littleCake = Build.VERSION.SDK_INT<=21;
	public static final boolean bigMountain = Build.VERSION.SDK_INT>22;
	public static final boolean hugeHimalaya = Build.VERSION.SDK_INT>=Build.VERSION_CODES.P;
	
	public static ColorDrawable GrayBG = new ColorDrawable(0xff8f8f8f);
	
	static int getDP(int dp, View v){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, v.getResources().getDisplayMetrics());
    }

    static int getDP(float dp, Context c){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }
	
	
	public static boolean isWindowDetached(Window window) {
		return window==null || window.getDecorView().getParent()==null || window.getDecorView().getVisibility()!=View.VISIBLE;
	}
	
	
	public static void logAllViews(){
		List<View> views = getWindowManagerViews();
		CMN.debug("\n\n\n\n\n");
		CMN.debug("logAllViews::", views);
		for(View vI:views){
			CMN.Log("\n\n\n\n\nlogAllViews::  "+vI);
			CMN.recurseLog(vI);
		}
	}
	
	/* get the list from WindowManagerGlobal.mViews */
	public static List<View> getWindowManagerViews() {
		try {
			//  Class.forName("android.view.WindowManagerGlobal")
			//  ------>mViews
			//  ------>getInstance
			Object views = execSimple("{android.view.WindowManagerGlobal}.getInstance().mViews", reflectionPool);
			CMN.debug("logAllViews::views::", views);
			if (views instanceof List) {
				return (List<View>) views;
			} else if (views instanceof View[]) {
				return Arrays.asList((View[])views);
			}
		} catch (Exception e) {
			CMN.debug("logAllViews::", e);
			//instance_WindowManagerGlobal = new Exception();
		}
		return new ArrayList<>();
	}
	
	/* get the list from WindowManagerGlobal. the result array list is cached */
	public static List<View> getWindowManagerViews(BaseActivity a) {
		if (a.wViews!=null) {
			return a.wViews;
		}
		try {
			//  Class.forName("android.view.WindowManagerGlobal")
			//  ------>mViews
			//  ------>getInstance
			Object views = execSimple("{android.view.WindowManagerGlobal}.getInstance().mViews", reflectionPool);
			//CMN.debug("logAllViews::views::", views);
			if (views instanceof List) {
				return a.wViews = (List<View>) views;
			} else if (views instanceof View[]) {
				return Arrays.asList((View[])views);
			}
		} catch (Exception e) {
			CMN.debug("logAllViews::", e);
			//instance_WindowManagerGlobal = new Exception();
		}
		return new ArrayList<>();
	}
	
	/* 对话框是否置顶 */
	public static boolean isTopmost(Dialog dialog, BaseActivity a) {
		if (dialog!=null) {
			List<View> views = getWindowManagerViews(a);
			final int size = views.size();
			//CMN.debug("isTopmost::", views.indexOf(dialog.getWindow().getDecorView()), size -1);
			//CMN.debug(views);
			Window win = dialog.getWindow();
			if (win!=null && size >1) {
				View dv = win.getDecorView();
				if (views.get(size - 1)==dv) {
					return true;
				}
				Class<? extends View> clazz = dv.getClass();
				for (int i = size - 1; i >= 0; i--) {
					View view = views.get(i);
					if (view.getClass()==clazz) {
						return view==dv;
					}
//					if (a.isPanelDecorView(view)) {
//						return false;
//					}
				}
			}
		}
		return false;
	}
	
	/* 将对话框置顶 */
	public static void ensureTopmost(Dialog dialog, BaseActivity a, Dialog.OnDismissListener disLis) {
		if (dialog!=null) {
			if (!isTopmost(dialog, a)) {
				dialog.setOnDismissListener(null);
				dialog.dismiss();
				dialog.show();
				dialog.setOnDismissListener(disLis);
				CMN.debug("ensureTopmost::reshow!!!");
			}
			else CMN.debug("ensureTopmost::same!!!");
		}
	}
	
	public static int indexOf(CharSequence text, char cc, int now) {
		for (int i = now; i < text.length(); i++) {
			if(text.charAt(i)==cc){
				return i;
			}
		}
		return -1;
	}
	
	public static View getViewItemByPath(View obj, int...path) {
		int cc=0;
		while(cc<path.length) {
			//CMN.Log(cc, obj);
			if(obj instanceof ViewGroup) {
				obj = ((ViewGroup)obj).getChildAt(path[cc]);
			} else {
				obj = null;
				break;
			}
			cc++;
		}
		return (View)obj;
	}
	
	/* 📕📕📕 微空间内爆术 📕📕📕 */
	public static View getViewItemByClass(View donkeySteed, int dynamicFrom, Class<?>...classes) {
		if(classes[0].isInstance(donkeySteed)) {
			ViewGroup vg;
			for (int i = 1;i < classes.length; i++) {
				vg=(ViewGroup) donkeySteed;
				donkeySteed = vg.getChildAt(0);
				if(i>=dynamicFrom) {
					int j=0;
					int cc=vg.getChildCount();
					while(!classes[i].isInstance(donkeySteed)&&++j<cc) {
						donkeySteed = vg.getChildAt(j);
					}
				}
				if(!classes[i].isInstance(donkeySteed)) {
					return null;
				}
			}
			return donkeySteed;
		}
		return null;
	}
	
	public static void setOnClickListenersOneDepth(ViewGroup vg, View.OnClickListener clicker, int depth, Object[] viewFetcher) {
		int cc = vg.getChildCount();
		View ca;
		boolean longClickable = clicker instanceof View.OnLongClickListener;
		boolean touhable = clicker instanceof View.OnTouchListener;
		if(vg.isClickable()) {
			click(vg, clicker, longClickable, touhable);
		}
		for (int i = 0; i < cc; i++) {
			ca = vg.getChildAt(i);
			//CMN.Log("setOnClickListenersOneDepth", ca, (i+1)+"/"+(cc));
			if(ca instanceof ViewGroup) {
				if(--depth>0) {
					if(ca.isClickable()) {
						click(ca, clicker, longClickable, touhable);
					} else {
						setOnClickListenersOneDepth((ViewGroup) ca, clicker, depth, viewFetcher);
					}
				}
			} else {
				int id = ca.getId();
				if(ca.getId()!=View.NO_ID){
					if(!(ca instanceof EditText) && ca.isEnabled()) {
						click(ca, clicker, longClickable, touhable);
					}
					if(viewFetcher!=null) {
						for (int j = 0; j < viewFetcher.length; j++) {
							if(viewFetcher[j] instanceof Integer && (int)viewFetcher[j]==id) {
								viewFetcher[j]=ca;
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public static void setOnClickListenersOneDepth(ViewGroup vg, View.OnClickListener clicker, SparseArray<View> viewFetcher, int depth) {
		int cc = vg.getChildCount();
		View ca;
		boolean longClickable = clicker instanceof View.OnLongClickListener;
		boolean touhable = clicker instanceof View.OnTouchListener;
		if(vg.isClickable()) {
			click(vg, clicker, longClickable, touhable);
		}
		for (int i = 0; i < cc; i++) {
			ca = vg.getChildAt(i);
			//CMN.Log("setOnClickListenersOneDepth", ca, (i+1)+"/"+(cc));
			if(ca instanceof ViewGroup) {
				if(--depth>0) {
					if(ca.isClickable()) {
						click(ca, clicker, longClickable, touhable);
					} else {
						setOnClickListenersOneDepth((ViewGroup) ca, clicker, viewFetcher, depth);
					}
				}
			} else {
				int id = ca.getId();
				if(ca.getId()!=View.NO_ID){
					if(!(ca instanceof EditText) && ca.isEnabled()) {
						click(ca, clicker, longClickable, touhable);
					}
					if(viewFetcher!=null) {
						viewFetcher.put(ca.getId(), ca);
					}
				}
			}
		}
	}
	
	private static void click(View ca, View.OnClickListener clicker, boolean longClickable, boolean touhable) {
		ca.setOnClickListener(clicker);
		if(longClickable&&ca.isLongClickable()) {
			ca.setOnLongClickListener((View.OnLongClickListener) clicker);
		}
		if(touhable) {
			ca.setOnTouchListener((View.OnTouchListener) clicker);
		}
	}
	
	public static boolean removeView(View viewToRemove) {
		return removeIfParentBeOrNotBe(viewToRemove, null, false);
	}
	
	public static boolean removeIfParentBeOrNotBe(View view, ViewGroup parent, boolean tobe) {
		if(view!=null) {
			ViewParent svp = view.getParent();
			if((parent!=svp) ^ tobe) {
				if(svp!=null) {
					((ViewGroup)svp).removeView(view);
					//CMN.Log("removing from...", svp, view.getParent(), view);
					return view.getParent()==null;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean addViewToParent(View view2Add, ViewGroup parent, int index) {
		if(removeIfParentBeOrNotBe(view2Add, parent, false)) {
			int cc=parent.getChildCount();
			if(index<0) {
				index = cc+index;
				if(index<0) {
					index = 0;
				}
			} else if(index>cc) {
				index = cc;
			}
			parent.addView(view2Add, index);
			return true;
		}
		return false;
	}
	
	public static boolean addViewToParent(View view2Add, ViewGroup parent, View index) {
		return addViewToParent(view2Add, parent, parent.indexOfChild(index)+1);
	}
	
	public static boolean addViewToParent(View view2Add, ViewGroup parent) {
		if(parent!=null && removeIfParentBeOrNotBe(view2Add, parent, false)) {
			parent.addView(view2Add);
			return true;
		}
		return false;
	}
	
	public static void postInvalidateLayout(View view) {
		view.post(view::requestLayout);
	}
	
	static int resourceId=-1;
	public static int getStatusBarHeight(Resources resources) {
		if(resourceId==-1)
			try {
				resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
			} catch (Exception ignored) { }
		if (resourceId != -1) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}
	
	public static void removeIfChildIsNot(View someView, ViewGroup parent) {
		int cc=parent.getChildCount();
		if(cc>1) {
			for(int i=cc-1;i>=0;i--)
				if(parent.getChildAt(i)!=someView)
					parent.removeViewAt(i);
		}
	}
	
	public static void removeAllViews(ViewGroup parent) {
		if(parent.getChildCount()>0) {
			parent.removeAllViews();
		}
	}
	
	public static void addViewToParentUnique(View view2Add, ViewGroup parent) {
		addViewToParent(view2Add, parent);
		removeIfChildIsNot(view2Add, parent);
	}
	
	public static boolean ViewIsId(View view, int id) {
		return view!=null && view.getId()==id;
	}
	
	public static boolean ViewIsChildOf(View view, Object parent) {
		if (view!=null) {
			ViewParent vp = view.getParent();
			while (vp!=null) {
				if (vp==parent)
					return true;
				vp = vp.getParent();
			}
		}
		return false;
	}
	
	public static CharSequence decorateSuffixTick(CharSequence title, boolean hasTick) {
		int len=title.length();
		boolean b1 = title.charAt(len-1)=='√';
		if(b1 ^ hasTick) {
			return hasTick?title+" √":title.subSequence(0, len-2);
		}
		return title;
	}

	public static boolean checkSetVersion(int[] versions, int i, int version) {
		if(versions[i]!=version) {
			versions[i]=version;
			return true;
		}
		return false;
	}
	
	public static <T> T getLast(@NonNull ArrayList<T> array) {
		if(array.size()>0) return array.get(array.size()-1);
		return null;
	}
	
//	public static List<MenuItemImpl> MapNumberToMenu(MenuBuilder menu, int...numbers) {
//		MenuItemImpl[] items = new MenuItemImpl[numbers.length];
//		for (int i = 0; i < numbers.length; i++) {
//			items[i] = (MenuItemImpl) menu.getItem(numbers[i]);
//		}
//		return Arrays.asList(items);
//	}
	
	/**  */
	public static Object getWeakRefObj(Object tag) {
		return tag==null?null:((WeakReference)tag).get();
	}
	
	public static boolean isVisible(View v) {
		return v.getVisibility()==View.VISIBLE;
	}
	
	public static boolean isVisibleV2(View v) {
		return v!=null && v.getVisibility()==View.VISIBLE && v.getParent()!=null;
	}
	
	public static void setVisible(View v, boolean visible) {
		v.setVisibility(visible?View.VISIBLE:View.GONE);
	}
	
	public static void setVisibility(View v, boolean visible) {
		v.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
	}
	
	public static void setVisibleV3(View v, boolean visible) {
		int vis = visible?View.VISIBLE:View.INVISIBLE;
		if(v.getVisibility()!=vis) v.setVisibility(vis);
	}
	
	public static void setVisibleV2(View v, boolean visible) {
		if(v!=null)v.setVisibility(visible?View.VISIBLE:View.GONE);
	}
	
//	public static MenuItem findInMenu(List<MenuItemImpl> mainMenu, int id) {
//		for (int i = 0; i < mainMenu.size(); i++) {
//			if(mainMenu.get(i).getItemId()==id) {
//				return mainMenu.get(i);
//			}
//		}
//		return null;
//	}
	
	public static View findViewById(ViewGroup vg, int id) {
		for (int i = 0,len=vg.getChildCount(); i < len; i++) {
			View c = vg.getChildAt(i);
			if(c.getId()==id) return c;
		}
		return vg.findViewById(id);
	}
	
	private static int max(int r, int y, int b) {
		return Math.max(r, Math.max(y, b));
	}
	
	private static int min(int r, int y, int b) {
		return Math.min(r, Math.min(y, b));
	}
	
	public static WeakReference DummyRef = new WeakReference<>(null);
	
	public static View getViewItemByPath(Object obj, int...path) {
		int cc=0;
		while(cc<path.length) {
			//CMN.Log(cc, obj);
			if(obj instanceof ViewGroup) {
				obj = ((ViewGroup)obj).getChildAt(path[cc]);
			} else {
				obj = null;
				break;
			}
			cc++;
		}
		return Objects.requireNonNull((View)obj);
	}
	
	
	public static void setOnClickListenersOneDepth(ViewGroup vg, View.OnClickListener clicker, int depth, int idxStart, Object[] viewFetcher) {
		int cc = vg.getChildCount();
		View ca;
		for (int i = idxStart; i < cc; i++) {
			ca = vg.getChildAt(i);
			//CMN.Log("setOnClickListenersOneDepth", ca, (i+1)+"/"+(cc), ca.isEnabled());
			if(ca instanceof ViewGroup) {
				if(--depth>0) {
					setOnClickListenersOneDepth((ViewGroup) ca, clicker, depth, 0, viewFetcher);
				}
			} else {
				int id = ca.getId();
				if(ca.getId()!=View.NO_ID){
					if(!(ca instanceof EditText) && ca.isEnabled()) {
						ca.setOnClickListener(clicker);
						if(clicker instanceof View.OnLongClickListener && ca.isLongClickable()) {
							ca.setOnLongClickListener((View.OnLongClickListener) clicker);
						}
					}
					if(viewFetcher!=null) {
						for (int j = 0; j < viewFetcher.length; j++) {
							if(viewFetcher[j] instanceof Integer && (int)viewFetcher[j]==id) {
								viewFetcher[j]=ca;
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean actualLandscapeMode(Context c) {
		int angle = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
		return angle== Surface.ROTATION_90||angle==Surface.ROTATION_270;
	}
	
	public static String getTextInView(View view) {
		CharSequence ret = ((TextView)view).getText();
		return ret==null?"":ret.toString();
	}
	
	public static String getFieldInView(View view) {
		return ((TextView)view).getText().toString().trim().replaceAll("[\r\n]", "");
	}
	
	public static String getTextInView(View view, int id) {
		return ((TextView)view.findViewById(id)).getText().toString();
	}
	
	public static void setTextInView(View view, CharSequence cs) {
		((TextView)view).setText(cs);
	}
	
	public static View replaceView(View viewToAdd, View viewToRemove) {
		return replaceView(viewToAdd, viewToRemove, true);
	}
	
	public static View replaceView(View viewToAdd, View viewToRemove, boolean layoutParams) {
		ViewGroup.LayoutParams lp = viewToRemove.getLayoutParams();
		ViewGroup vg = (ViewGroup) viewToRemove.getParent();
		if(vg!=null) {
			int idx = vg.indexOfChild(viewToRemove);
			removeView(viewToAdd);
			if (layoutParams) {
				vg.addView(viewToAdd, idx, lp);
			} else {
				vg.addView(viewToAdd, idx);
			}
			removeView(viewToRemove);
		}
		return viewToAdd;
	}
	
	public static Drawable getThemeDrawable(Context context, int attrId) {
		int[] attrs = new int[] { attrId };
		TypedArray ta = context.obtainStyledAttributes(attrs);
		Drawable drawableFromTheme = ta.getDrawable(0);
		ta.recycle();
		return drawableFromTheme;
	}
	
	public static int getViewIndex(View sv) {
		ViewGroup svp = (ViewGroup) sv.getParent();
		if (svp!=null) {
			return svp.indexOfChild(sv);
		}
		return -1;
	}
	
	public static void blinkView(View blinkView, boolean post) {
		Animation anim = new AlphaAnimation(0.1f, 1.0f);
		anim.setDuration(50);
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(2);
		if (post) {
			blinkView.post(() -> blinkView.startAnimation(anim));
		} else {
			blinkView.startAnimation(anim);
		}
	}
	
	public static String getTextSelection(TextView tv) {
		int st = tv.getSelectionStart();
		int ed = tv.getSelectionEnd();
		if (st>ed) {
			int tmp = st; st=ed; ed=tmp;
		}
		return tv.getText().subSequence(st, ed).toString();
	}
	
	public static void clearTextSelection(TextView view) {
		MotionEvent evt = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, -1, -1, 0);
		if (view!=null) {
			view.dispatchTouchEvent(evt);
			evt.setAction(MotionEvent.ACTION_UP);
			view.dispatchTouchEvent(evt);
		}
		evt.setSource(100);
		if(view.hasSelection()) view.clearFocus();
		evt.recycle();
	}
	
	public static void preventDefaultTouchEvent(View view, int x, int y) {
		MotionEvent evt = MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, x, y, 0);
		if (view!=null) view.dispatchTouchEvent(evt);
		evt.setSource(100);
		evt.recycle();
	}
	
	public static void performClick(View view, float x, float y) {
		MotionEvent evt = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0);
		view.dispatchTouchEvent(evt);
		evt.setAction(MotionEvent.ACTION_UP);
		view.dispatchTouchEvent(evt);
		evt.recycle();
	}
	
	
	public static class BaseAnimationListener implements Animation.AnimationListener {
		@Override public void onAnimationStart(Animation animation) { }
		@Override public void onAnimationEnd(Animation animation) {  }
		@Override public void onAnimationRepeat(Animation animation) {  }
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class BaseAnimatorListener implements Animator.AnimatorListener {
		@Override public void onAnimationStart(Animator animation) { }
		@Override public void onAnimationEnd(Animator animation) {  }
		@Override public void onAnimationCancel(Animator animation) { }
		@Override public void onAnimationRepeat(Animator animation) { }
	}
	
	public static HashMap<String, Object> reflectionPool = new HashMap<>();
	
	//View.class.getDeclaredField("mScrollCache");
	//Class.forName("android.view.View$ScrollabilityCache").getDeclaredField("scrollBar")
	
	public static void setListViewScrollbarColor(View mListView, boolean red) {
		try {
			Drawable ScrollbarDrawable = (Drawable) execSimple("$.mScrollCache.scrollBar", reflectionPool, mListView);
			//CMN.debug("setListViewScrollbarColor::", ScrollbarDrawable, mListView);
			ScrollbarDrawable.setColorFilter(red?RED:GREY);
		} catch (Exception e) {
			CMN.debug("setListViewScrollbarColor::", e);
		}
	}
	
	static ColorFilter RED = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
	static ColorFilter GREY = new PorterDuffColorFilter(0x8a666666, PorterDuff.Mode.SRC_IN);
	
	
	//Class.forName("android.widget.FastScroller");
	//--->getDeclaredField("mTrackDrawable");
	//--->getDeclaredField("mThumbImage");
	//AbsListView.class.getDeclaredField("mFastScroll");
	
	public static void setListViewFastColor(View...mListViews) {
		try {
			for(View mListView:mListViews) {
				// https://github1s.com/aosp-mirror/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/AbsListView.java#L584
				String eval = "$.mFastScroll.mThumbImage";
				if (Build.VERSION.SDK_INT<21) {
					eval = eval.substring(0,13)+"er"+eval.substring(13);
				}
				ImageView ThumbImage = (ImageView) execSimple(eval, reflectionPool, mListView);
				//CMN.debug("setListViewFastColor::", ThumbImage);
				if (ThumbImage != null) ThumbImage.setColorFilter(GREY);
			}
		} catch (Exception e) {
			CMN.debug("setListViewFastColor::", e);
		}
	}
	
	public static void addOnLayoutChangeListener(View view, View.OnLayoutChangeListener layoutChangeListener) {
		if (layoutChangeListener!=null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			view.addOnLayoutChangeListener(layoutChangeListener);
	}
	
	public static Field getField(Class<?> aClass, String name) throws Exception {
//		CMN.debug("getField::"+aClass+"->"+name);
		if (aClass==ObjectUtils.NULL.getClass()) {
			return null;
		}
		Field ret=null;
		try {
			ret = aClass.getDeclaredField(name);
			ret.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				ret = aClass.getField(name);
			} catch (NoSuchFieldException ex) {
				while((aClass=aClass.getSuperclass())!=null) {
					try {
						ret = aClass.getDeclaredField(name);
						break;
					} catch (NoSuchFieldException ignored) { }
				}
			}
		}
		try {
			Objects.requireNonNull(ret);
		} catch (Exception e) {
			CMN.debug("getField::notFount::"+aClass+"->"+name);
			throw e;
		}
		ret.setAccessible(true);
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(ret, ret.getModifiers() & ~Modifier.FINAL);
		} catch (Exception ignored) { }
		return ret;
	}
	
	public static Method getMethod(Class<?> aClass, String name, Class<?>[] types) throws Exception {
		if (aClass==ObjectUtils.NULL.getClass()) {
			return null;
		}
		Method ret;
		try {
			ret = aClass.getMethod(name, types);
		} catch (NoSuchMethodException e) {
			ret = aClass.getDeclaredMethod(name, types);
			ret.setAccessible(true);
		}
		return Objects.requireNonNull(ret);
	}
	
	public static Object execSimple(String simplet, HashMap<String, Object> reflectionPool, Object...vars) throws Exception {
		StringTokenizer array = new StringTokenizer(simplet, ";\r\n");
		HashMap<String, Object> variables = new HashMap<>();
		int st=0;
		for (int i = st; i < vars.length-0; i++) {
			if(i==st && vars[i] instanceof String && vars[i].toString().endsWith("::")) {
				st++; continue;
			}
			//if(vars[i]==simplet) break;
			variables.put("$"+(i==st?"":i), vars[i+0]);
		}
		Object ret = null;
		while(array.hasMoreTokens()){
			String ln = array.nextToken();
//			CMN.debug("ln::"+ln);
			int eqIdx = ln.indexOf('=');
			if (eqIdx>0) {
				Class sClazz = null;
				Class zClazz = null;
				Object object = null;
				Object newObj = null;
				String varName=null;
				String valName=null;
				Field fieldToSet=null;
				varName = ln.substring(0, eqIdx);
				// 左值
				if (varName.startsWith("var")) {
					varName = varName.substring(4);
				} else {
					if (varName.contains(".")) {
						if (varName.startsWith("{")) {
							int idx=varName.lastIndexOf("}");
							sClazz = Class.forName(varName.substring(1, idx));
							varName = varName.substring(idx+1);
							String[] expsLeft = ("ex"+varName).trim().split("\\.(?![0-9])");
							fieldToSet = (Field) evalFieldMethod(sClazz, object, expsLeft, variables, reflectionPool);
						} else {
							int idx=varName.lastIndexOf(".");
							String[] expsLeft = varName.substring(0, idx).split("\\.(?![0-9])");
							object = evalFieldMethod(sClazz, object, expsLeft, variables, reflectionPool);
							expsLeft = ("ex"+varName.substring(idx)).split("\\.(?![0-9])");
							fieldToSet = (Field) evalFieldMethod(null, object, expsLeft, variables, reflectionPool);
						}
						varName = null;
					} else { // 取出局部变量缓存值，作为左值
						object = variables.get(varName);
					}
				}
				// 左值
				// eq[0] = eq[1]
				//         ...exps
				// 右值
				valName = ln.substring(eqIdx+1);
//				CMN.debug("右值::"+valName);
				String[] expsRight=null;
				if (valName.startsWith("{")) {
					int idx=valName.lastIndexOf("}");
					zClazz = Class.forName(valName.substring(1, idx));
					if (idx+1>=valName.length()) {
						newObj = zClazz; // 直接变成类
					} else {
						expsRight = valName.substring(idx).split("\\.(?![0-9])");
					}
				} else if (valName.startsWith("'")) {
					newObj = valName.substring(1, valName.length()-1);
				}  else {
					expsRight = valName.split("\\.(?![0-9])");
					valName = expsRight[0];
					newObj = variables.get(valName);
				}
				if (newObj==null || expsRight!=null && expsRight.length>1) {
					newObj = evalFieldMethod(zClazz, newObj, expsRight, variables, reflectionPool);
				}
				ret = newObj;
				// 右值
				// 赋值
				if (fieldToSet!=null) {
//					CMN.debug("field赋值::", object, fieldToSet, newObj);
					fieldToSet.set(object, newObj);
				}
				if (varName!=null) {
//					CMN.debug("var赋值::", varName, newObj);
					variables.put(varName, newObj);
				}
			}
			else if(ln.length()>2){
				if (ln.startsWith("{")) {
					int idx=ln.indexOf("}");
					Class<?> sClazz = Class.forName(ln.substring(1, idx));
					ln = ln.substring(idx+1);
					String[] expsLeft = (ln).trim().split("\\.(?![0-9])");
					ret = evalFieldMethod(sClazz, null, expsLeft, variables, reflectionPool);
				} else {
					String[] exps = ln.split("\\.(?![0-9])");
					ret = evalFieldMethod(null, null, exps, variables, reflectionPool);
				}
			}
		}
		return ret;
	}
	
	final static HashMap<String, Class> typeHash = new HashMap<>();
	
	//	public static Object evalSimple(Object object, String exps) {
//		try {
//			return evalFieldMethod(null, object, exps.split("\\."));
//		} catch (Exception e) {
//			return e;
//		}
//	}
	public static Object evalFieldMethod(Class zClazz, Object object
			, String[] exps, HashMap<String, Object> variables, HashMap<String, Object> reflectionPool) throws Exception {
//		CMN.debug("evalFieldMethod::", zClazz, object, "exp::"+Arrays.toString(exps), variables);
		boolean extract=exps.length>1&& "ex".equals(exps[0]);
		Object fMd = null;
		if (typeHash.size()==0) {
			// byte、short、int、long、float、double
			//typeHash.put("byte", byte.class);  typeHash.put("Byte", Byte.class);
			//typeHash.put("double", double.class);  typeHash.put("Double", Double.class);
			//typeHash.put("float", float.class);  typeHash.put("Float", Float.class);
			//typeHash.put("long", long.class); typeHash.put("Long", Long.class);
			typeHash.put("int", int.class); typeHash.put("Int", Integer.class);
			//typeHash.put("short", short.class);  typeHash.put("Short", Short.class);
			typeHash.put("String", String.class);
		}
		if (zClazz==null && object!=null) {
			zClazz = object instanceof Class?(Class) object :object.getClass();
		}
		Object[] parameters; Class[] methodTypes;
		for (int i = (object==null&&zClazz==null)?0:1; i < exps.length; i++) {
			String fdMd=exps[i];
			parameters = ArrayUtils.EMPTY_OBJECT_ARRAY;
			methodTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
			if (i==0 && variables!=null) {
				//CMN.debug("init extraction from variables::"+fdMd, zClazz);
				object = variables.get(fdMd);
				zClazz = object==null? ObjectUtils.Null.class:
						object instanceof Class?(Class) object :object.getClass();
				continue;
			}
			int zh = fdMd.indexOf("[");
			int xi = fdMd.indexOf("(");
			int which=-1;
			if(xi>0) {
				String[] arr;
				int where=xi;
				if(zh>0) {
					if (zh<xi) {
						where = zh;
					} else {
						which = IU.parsint(fdMd.substring(zh+1, fdMd.indexOf("]", zh)), 0);
					}
				}
				String methodName = fdMd.substring(0, where);
				fMd = null;
				String storeKey=null;
				if (zClazz!=null && reflectionPool!=null) {
					storeKey = zClazz.hashCode()+methodName;
					fMd = reflectionPool.get(storeKey);
				}
				if (fMd==null) {
					if (where!=xi) {
						String types = fdMd.substring(zh+1, fdMd.indexOf("]", zh));
						arr=null;
						if (types.contains(",")) {
							arr = types.split(",");
						} else if(types.length()>0) {
							arr = new String[]{types};
						}
						if (arr!=null) {
							methodTypes = new Class[arr.length];
							for (int j = 0; j < arr.length; j++) { // ..分析参数类型
								String typeName = arr[j].trim();
								//CMN.debug("typeName::", typeName);
								Class type = typeHash.get(typeName);
								if (type==null) {
									if (!typeName.contains(".")) {
									
									} else {
										type = Class.forName(typeName);
									}
								}
								methodTypes[j] = type;
							}
						}
					}
				} //else CMN.Log("复用反射::"+storeKey);
				//if (methodTypes.length>0) {
				String params = fdMd.substring(xi+1, fdMd.indexOf(")", xi));
				arr=null;
				if (params.contains(",")) {
					arr = params.split(",");
				} else if(params.length()>0) {
					arr = new String[]{params};
				}
				if (arr!=null) {
					parameters = new Object[arr.length];
					if(methodTypes.length==0) methodTypes = new Class[arr.length];
					for (int j = 0; j < arr.length; j++) { // ..分析参数
						String paramName = arr[j].trim();
//						CMN.debug("paramName::", paramName);
						try {
							//CMN.debug("paramName::", paramName, methodTypes[j]==null?null:methodTypes[j].getSimpleName());
							if (paramName.startsWith("'")) {
								parameters[j] = paramName.substring(1, paramName.length()-1);
							} else if (paramName.startsWith("0x")) {
								parameters[j] = IU.parsint(paramName);
							} else {
								Integer.parseInt(paramName.substring(0, 1));
								if (methodTypes[j]!=null) {
									char c =  methodTypes[j].getSimpleName().toUpperCase().charAt(0);
									if(c=='B') parameters[j] = Boolean.valueOf(paramName);
									else if(c=='D') parameters[j] = Double.valueOf(paramName);
									else if(c=='F') parameters[j] = Float.valueOf(paramName);
									else if(c=='L') parameters[j] = Long.valueOf(paramName);
									else if(c=='I') parameters[j] = IU.parsint(paramName);
									else if(c=='S') parameters[j] = Short.parseShort(paramName);
								} else {
									parameters[j] = IU.parsint(paramName);
								}
							}
						} catch (NumberFormatException e) { // ...objHash
							//e.printStackTrace();
							if (variables!=null) {
								parameters[j] = variables.get(paramName);
							}
						}
						if (parameters[j]!=null && methodTypes[j]==null) {
							methodTypes[j] = parameters[j].getClass();
						}
					}
				}
				//}
//				CMN.debug("methodTypes::", methodTypes.length, Arrays.toString(methodTypes));
//				CMN.debug("parameters::", parameters.length, Arrays.toString(parameters));
//				CMN.debug("fdMd::", zClazz, methodName);
				if (fMd==null) {
					fMd = getMethod(zClazz, methodName, methodTypes);
					if (reflectionPool!=null && fMd!=null) reflectionPool.put(storeKey, fMd);
				}
				if(fMd==null) return null;
				//Objects.requireNonNull(fMd);
				object = ((Method)fMd).invoke(object, parameters);
//				CMN.debug("result::", object);
			}
			else {
				if(zh>0) {
					which = IU.parsint(fdMd.substring(zh+1, fdMd.indexOf("]", zh)), 0);
					fdMd = fdMd.substring(0, zh);
				}
//				CMN.debug("getField::", zClazz, object, fdMd);
				fMd = null;
				if (zClazz.isArray()) {
					object = Array.getLength(object);
				} else {
					String storeKey=null;
					if (reflectionPool!=null) {
						storeKey = zClazz.hashCode()+fdMd;
						fMd = reflectionPool.get(storeKey);
					}
					if (fMd==null) {
						fMd = getField(zClazz, fdMd);
						if (reflectionPool!=null && fMd!=null) reflectionPool.put(storeKey, fMd);
					} //else CMN.Log("复用反射::"+storeKey);
					if(fMd==null) return null;
					Objects.requireNonNull(fMd);
					object = ((Field)fMd).get(object);
				}
			}
			if (which>=0) { // 是数组
				try {
					object = ((Object[])object)[which];
				} catch (Exception e) {
					object = null;
				}
			}
			zClazz = object==null?ObjectUtils.Null.class:object.getClass();
		}
		return extract?fMd:object;
	}
	
	public static boolean isKeyboardShown(View rootView) {
		final int softKeyboardHeight = 100;
		rootView.getWindowVisibleDisplayFrame(rect);
		int heightDiff = rootView.getBottom() - rect.bottom;
		return heightDiff > softKeyboardHeight * GlobalOptions.density;
	}
	
	
	/** Retrieve the invoker application (the intent sender) package name for onNewIntent or onCreate
	 * @param timeRange Seconds of time. Querying app usage events from now-timeRange to now.
	 * 			maybe 1 for onNewIntent and 3 for onCreate.
	 * @return the ThirdParty package name or null if not found*/
	@Nullable
	public static String topThirdParty(Context context, float timeRange, long timeUntil) {
		String thisPak, tmp, top = null;
		try {
			thisPak = context.getPackageName();
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
				UsageStatsManager man = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
				if(timeRange<0)
					timeRange=1;
				int range = (int) (1000*timeRange);
				if (timeUntil<range) {
					timeUntil = System.currentTimeMillis();
				}
				UsageEvents uEvts = man.queryEvents(timeUntil - range,timeUntil); // query in 1~3 sec
				UsageEvents.Event e = new UsageEvents.Event();
				ArrayList<String> packages = new ArrayList<>(64);
				int cc = 64;
				while (uEvts.getNextEvent(e) && --cc>0){
					packages.add(e.getPackageName());
					 CMN.debug("topThirdParty::", e.getPackageName()/*, e.getTimeStamp()*/);
				}
				for (int i = packages.size()-1; i >= 0; i--) {
					tmp = packages.get(i);
					if (!thisPak.equals(tmp) && !(
							tmp.endsWith(".updater")
							|| tmp.endsWith(".notification")
							|| tmp.contains("webview")
							)) {
						if (tmp.endsWith(".launcher")||tmp.endsWith(".home")) {
							if ("android".equals(top)) {
								top = null;
							}
							break;
						}
						if (!"android".equals(tmp) || top==null) {
							top = tmp;
							if (!"android".equals(tmp)) {
								break;
							}
						}
					}
				}
			} else {
				ActivityManager man = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				List<ActivityManager.RecentTaskInfo> tasks = man.getRecentTasks(3, 0);
				for(ActivityManager.RecentTaskInfo info:tasks) {
					tmp = info.baseIntent.getComponent().getPackageName();
					//CMN.Log("topThirdParty::", tmp);
					if (!thisPak.equals(tmp) && !"android".equals(tmp)) {
						top = tmp;
						break;
					}
				}
			}
		} catch (Exception e) {
			//CMN.Log(e);
		}
		return top;
	}
	
	public static void TrimWindowWidth(Window win, DisplayMetrics dm) {
		if(win!=null) {
			int maxWidth = (int) (GlobalOptions.density*480);
			WindowManager.LayoutParams attr = win.getAttributes();
			int targetW=dm.widthPixels>maxWidth?maxWidth: ViewGroup.LayoutParams.MATCH_PARENT;
			if(targetW!=attr.width){
				attr.width = targetW;
				win.setAttributes(attr);
			}
		}
	}
	
	public static boolean canDrawOverlays(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return Settings.canDrawOverlays(context);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			final int OP_SYSTEM_ALERT_WINDOW = 24;
			return checkOp(context, OP_SYSTEM_ALERT_WINDOW);
		} else {
			return true;
		}
	}
	
	
	private static boolean checkOp(Context context, int op) {
		AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
		try {
			Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
			return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
		} catch (Exception e) {
			CMN.Log(e);
		}
		return false;
	}
	
}
