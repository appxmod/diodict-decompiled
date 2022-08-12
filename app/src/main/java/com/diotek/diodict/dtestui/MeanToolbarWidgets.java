package com.diotek.diodict.dtestui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.diodict.decompiled.R;
import com.diotek.diodict.HyperCommonActivity;
import com.diotek.diodict.MemoActivity;
import com.diotek.diodict.Preference;
import com.diotek.diodict.SearchListActivity;
import com.diotek.diodict.ViewUtils;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.CustomPopupRelativeLayout;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.utils.GlobalOptions;
import com.diotek.diodict.utils.IU;

@SuppressLint("ResourceType")
public class MeanToolbarWidgets {
	public final BaseActivity a;
	public SearchListActivity schLstAct;
	public HyperCommonActivity hyperAct;
	
	public final Preference preference;
	public PopupWindow mFontSizeChangePopup = null;
	public PopupWindow mMarkerPopup = null;
	public RadioGroup markerGroup = null;
	public ImageView mMarkerCloseBtn = null;
	public boolean isVisible;
	public View.OnClickListener btnOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isVisible)
				return;
			isVisible = true;
			switch (v.getId()) {
				case R.id.MarkerBtn:
					initMarkerMode();
					break;
				case R.id.FontBtn:
					initFontMode();
					break;
				case R.id.MemoBtn:
					initMemoMode();
					break;
				case R.id.SaveBtn:
					initFavMode();
					break;
			}
			if (schLstAct!=null) {
				schLstAct.showSoftInputMethod(false);
			}
		}
	};
	
	public void initFavMode() {
		if (a.mTextView!=null) {
			a.mTextView.initSelection();
		}
		if (schLstAct != null) {
			schLstAct.showFlashcardListPop(false);
		}
		else if(hyperAct!=null){
			hyperAct.showFlashcardListPop(false);
		}
	}
	
	public MeanToolbarWidgets(BaseActivity a) {
		this.a = a;
		preference = a.preference;
		if (a instanceof SearchListActivity) {
			schLstAct = (SearchListActivity) a;
		}
		else if (a instanceof HyperCommonActivity) {
			hyperAct = (HyperCommonActivity) a;
		}
	}
	
	public void initFontMode() {
		if (a.mTextView != null) {
			a.mTextView.clearSelection();
		}
		//isVisible = true;
		showFontSizePopup();
	}
	
	public View.OnClickListener mFontSizeOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.28
		@Override // android.view.View.OnClickListener
		public void onClick(View v) {
			int[] fontSizeList = a.mResources.getIntArray(R.array.value_font_size);
			int fontSizeIndex = v.getId();
			if (a.mTextView != null && fontSizeIndex<5) {
				a.mTextView.setTextSize(fontSizeList[fontSizeIndex]);
				preference.fontSize(fontSizeIndex+1);
			}
			dismissFontSizePopup();
		}
	};
	
	public boolean dismissFontSizePopup() {
		if (this.mFontSizeChangePopup == null || !this.mFontSizeChangePopup.isShowing()) {
			return false;
		}
		this.mFontSizeChangePopup.dismiss();
		return true;
	}
	
	PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.SearchListActivity.68
		@Override // android.widget.PopupWindow.OnDismissListener
		public void onDismiss() {
			// a.enableMeanToolbar(true);
			isVisible = false;
		}
	};
	
	public void showFontSizePopup() {
		//isVisible = true;
		int[] windowLocation = new int[2];
		RelativeLayout parents = a.findViewById(R.id.SearchRightLayout);
		LayoutInflater inflate = a.getLayoutInflater();
		RelativeLayout PopupContent = (RelativeLayout) inflate.inflate(R.layout.fontsize_select_popup, (ViewGroup) null);
		RadioGroup group = PopupContent.findViewById(R.id.font_group);
		parents.getLocationInWindow(windowLocation);
		float density = GlobalOptions.density;
		int popupWidth = a.mResources.getConfiguration().orientation == 1 ? (int) ((313.3f * density) + 0.5f) : (int) ((337.3f * density) + 0.5f);
		View view = PopupContent.findViewById(R.id.font_popup_bg_view);
		int popupHeight = view.getBackground().getIntrinsicHeight();
		int popupX = parents.getWidth() - popupWidth;
		if (this.mFontSizeChangePopup == null) {
			this.mFontSizeChangePopup = CommonUtils.makeWindowWithPopupWindow(a, 0, PopupContent, a.mResources.getDrawable(R.drawable.popup_back), this.mOnDismissListener);
			for (int i = 0; i < group.getChildCount(); i++) {
				view = group.getChildAt(i);
				view.setId(i);
				view.setOnClickListener(this.mFontSizeOnClickListener);
			}
			view = ViewUtils.findViewById((ViewGroup) group.getParent(), R.id.font_close);
			view.setId(5);
			view.setOnClickListener(this.mFontSizeOnClickListener);
		}
		int fontSizeIndex = Preference.getFontSize();//a.setFontSizeFromPreference(true);
		((RadioButton) group.getChildAt(fontSizeIndex)).setChecked(true);
		if (this.mFontSizeChangePopup != null) {
			if (this.mFontSizeChangePopup.isShowing()) {
				this.mFontSizeChangePopup.update(windowLocation[0] + popupX, windowLocation[1] + 0, popupWidth, popupHeight);
				return;
			}
			this.mFontSizeChangePopup.setWidth(popupWidth);
			this.mFontSizeChangePopup.setHeight(popupHeight);
			this.mFontSizeChangePopup.showAtLocation(parents, 0, windowLocation[0] + popupX, windowLocation[1] + 0);
		}
	}
	
	public View.OnClickListener mMarkerColorOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.SearchListActivity.30
		@Override // android.view.View.OnClickListener
		public void onClick(View v) {
			int[] colorList = a.mResources.getIntArray(R.array.value_marker_color_adv);
			int value = v.getId();
			CMN.Log("onClick::", value);
			if (value <= 5) {
				ExtendTextView mTextView = a.mTextView;
				mTextView.setMarkerColor(colorList[value]);
				if(mTextView.bIsOneShotMark) { // hasSelection
					// 直接高亮选中文本！
					mTextView.performMark();
					mTextView.showMenu();
					dismissMarkerPopup();
				}
				else if (value < 5) {
					preference.markColor(value);
				}
			} else {
				dismissMarkerPopup();
			}
		}
	};
	
	public boolean dismissMarkerPopup() {
		CMN.debug("dismissMarkerPopup::");
		if (mMarkerPopup == null || !mMarkerPopup.isShowing()) {
			return false;
		}
		isVisible = false;
		mMarkerPopup.dismiss();
		mMarkerPopup = null;
		a.mTextView.setMarkerMode(false, false);
		a.setFocusableActivity(true);
		return true;
	}
	
	// enable click == nothing visible
	public void enable(boolean enable) {
		isVisible = !enable;
	}
	
	public class PopupTouchInterceptor implements View.OnTouchListener {
		private PopupTouchInterceptor() {
		}
		@Override // android.view.View.OnTouchListener
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			if ((action != 0 || mMarkerPopup == null || !mMarkerPopup.isShowing()) && action == 1) {
			}
			if (action == 4 && mMarkerPopup != null && mMarkerPopup.isShowing()) {
				//mMarkerPopup.dismiss();
				return true;
			}
			return false;
		}
	}
	
	View.OnTouchListener mMarkerPopupTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.HyperCommonActivity.8
		@Override // android.view.View.OnTouchListener
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == 4 || event.getAction() == 3) {
				if (mMarkerPopup != null) {
					mMarkerPopup.dismiss();
					mMarkerPopup = null;
				}
				return true;
			}
			return false;
		}
	};
	
	public void initMarkerMode() {
		if (a.mTextView != null && !a.mTextView.isMarkerMode()) {
			showMarkerPopup();
			a.mTextView.setMarkerMode(true, a.mTextView.gripShowing());
		}
	}
	
	public void showMarkerPopup() {
		//isVisible = true;
		int[] colorList = a.mResources.getIntArray(R.array.value_marker_color_adv);
		int[] windowLocation = new int[2];
		RelativeLayout parents = a.findViewById(R.id.SearchRightLayout);
		LayoutInflater inflate = a.getLayoutInflater();
		// CustomPopupRelativeLayout
		RelativeLayout PopupContent = (RelativeLayout) inflate.inflate(R.layout.marker_color_select_popup, null);
		markerGroup = PopupContent.findViewById(R.id.marker_group);
		parents.getLocationInWindow(windowLocation);
		parents.setOnTouchListener(mMarkerPopupTouchListener);
		float density = GlobalOptions.density;
		int popupWidth = a.mResources.getConfiguration().orientation == 1 ? (int) ((313.3f * density) + 0.5f) : (int) ((337.3f * density) + 0.5f);
		ImageView view = PopupContent.findViewById(R.id.marker_popup_bg_view);
		int popupHeight = view.getBackground().getIntrinsicHeight();
		int popupX = parents.getWidth() - popupWidth;
		if (mMarkerPopup == null) {
			mMarkerPopup = CommonUtils.makeWindowWithPopupWindow(a, 0, PopupContent, null, this.mOnDismissListener, false); // true
			for (int i = 0, len=markerGroup.getChildCount(); true; i++) {
				View btn;
				if (i < len) {
					btn = markerGroup.getChildAt(i);
				} else {
					if(i > len)  break;
					btn = mMarkerCloseBtn = (ImageView) ViewUtils
							.findViewById((ViewGroup) markerGroup.getParent(), R.id.marker_close);
				}
				//btn.setTag(i);
				btn.setId(i);
				btn.setOnClickListener(this.mMarkerColorOnClickListener);
				btn.setFocusable(true);
			}
			mMarkerPopup.setOutsideTouchable(false); // true
			mMarkerPopup.setTouchInterceptor(new PopupTouchInterceptor());
		} else {
			markerGroup.clearCheck();
		}
		int markerColorIndex = preference.markColor();
		RadioButton childAt = (RadioButton) markerGroup.getChildAt(markerColorIndex);
		childAt.setChecked(true);
		childAt.requestFocusFromTouch();
		a.mSearchMeanController.setMeanContentTextViewMarkerColor(colorList[markerColorIndex]);
		if (mMarkerPopup != null) {
			if (mMarkerPopup.isShowing()) {
				mMarkerPopup.update(windowLocation[0] + popupX, windowLocation[1] + 0, popupWidth, popupHeight);
			} else {
				mMarkerPopup.setWidth(popupWidth);
				mMarkerPopup.setHeight(popupHeight);
				mMarkerPopup.showAtLocation(parents, 0, windowLocation[0] + popupX, windowLocation[1] + 0);
			}
		}
		a.setFocusableActivity(false);
	}
	
	public boolean onKeyUp_ENTER() {
		if (isMarkerShowing()) {
			if (mMarkerCloseBtn.isFocused()) {
				dismissMarkerPopup();
				if (schLstAct!=null) {
					schLstAct.mMarkerBtn.requestFocus();
				}
				else if (hyperAct!=null) {
					hyperAct.mMarkerBtn.requestFocus();
				}
				return true;
			}
			RadioButton view = (RadioButton) markerGroup.findFocus();
			int[] colorList = a.mResources.getIntArray(R.array.value_marker_color_adv);
			if (a.mTextView != null && view != null && !view.isChecked()) {
				int viewIdx = IU.parsint(view.getTag());
				a.mTextView.setMarkerColor(colorList[viewIdx]);
				view.setChecked(true);
				if (viewIdx < 5) {
					preference.markColor(viewIdx);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean destroyData() {
		boolean ret = false;
		if (mMarkerPopup != null) {
			mMarkerPopup.dismiss();
			mMarkerPopup = null;
			ret = true;
		}
		if (this.mFontSizeChangePopup != null) {
			this.mFontSizeChangePopup.dismiss();
			this.mFontSizeChangePopup = null;
			ret = true;
		}
		return ret;
	}
	
	public boolean isMarkerShowing() {
		return mMarkerPopup != null && mMarkerPopup.isShowing() && markerGroup != null;
	}
	
	public void setFocusMarker(boolean bRight) {
		if (!isMarkerShowing()) {
			return;
		}
		View view = markerGroup.findFocus();
		if (view == null) {
			if (this.mMarkerCloseBtn.isFocused()) {
				View view2 = this.mMarkerCloseBtn;
				if (!bRight) {
					markerGroup.getChildAt(markerGroup.getChildCount() - 1).requestFocus();
					return;
				}
				return;
			}
			markerGroup.getChildAt(0).requestFocusFromTouch();
			markerGroup.getChildAt(0).setFocusableInTouchMode(false);
			return;
		}
		int idx = IU.parsint(view.getTag());
		if (bRight) {
			if (idx >= 0 && idx < markerGroup.getChildCount() - 1) {
				markerGroup.getChildAt(idx + 1).requestFocus();
			} else if (idx == markerGroup.getChildCount() - 1) {
				int i = idx + 1;
				this.mMarkerCloseBtn.requestFocus();
			}
		} else if (idx <= markerGroup.getChildCount() && idx > 0) {
			markerGroup.getChildAt(idx - 1).requestFocus();
		}
	}
	
	public void initMemoMode() {
		//isVisible = true;
		Intent intent = new Intent();
		intent.setClass(a, MemoActivity.class);
		intent.setFlags(603979776); // hyper meiyou
		String time_string = "";
		String data = "";
		int skin = 1;
		ExtendTextView mTextView = a.mTextView;
		if (mTextView != null) {
			int dbtype = mTextView.getDbtype();
			String keyword = mTextView.getKeyword();
			int suid = mTextView.getSuid();
			if (DioDictDatabase.existMemo(a, dbtype, keyword, suid)) {
				Cursor c = DioDictDatabase.getMemoCursorWith(a, dbtype, keyword, suid);
				if (c != null) {
					int nMemoIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_MEMO);
					int nTimeIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_TIME);
					int nSkinIdx = c.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE);
					data = c.getString(nMemoIdx);
					long time = c.getLong(nTimeIdx);
					time_string = DictUtils.getDateString(time);
					skin = c.getInt(nSkinIdx);
					c.close();
				} else {
					return;
				}
			}
			intent.putExtra(DictInfo.INTENT_MEMO_INFO_TIME, time_string);
			intent.putExtra(DictInfo.INTENT_MEMO_INFO_DATA, data);
			intent.putExtra(DictInfo.INTENT_MEMO_INFO_SKIN, skin);
			intent.putExtra(DictInfo.INTENT_MEMO_INFO_DICT, dbtype);
			intent.putExtra(DictInfo.INTENT_MEMO_INFO_WORD, keyword);
			intent.putExtra(DictInfo.INTENT_MEMO_INFO_SUID, suid);
			a.startActivityForResult(intent, 8);
			return;
		}
		MSG.l(2, "runMemoBtn(): error ");
	}
}
