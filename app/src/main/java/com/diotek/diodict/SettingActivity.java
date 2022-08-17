package com.diotek.diodict;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.ExtendScrollView;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.mean.SettingMeanController;
import com.diotek.diodict.uitool.BaseActivity;
import com.diodict.decompiled.R;

/* loaded from: classes.dex */
public class SettingActivity extends BaseActivity {
    private static final int RECOGNIZE_MIN_TIME = 0;
    private PopupWindow mPreviewPopup;
    private TextView mPreviewText;
    private EngineManager3rd mEngine = null;
    RadioButton mTheme1 = null;
    RadioButton mTheme2 = null;
    RadioButton mTheme3 = null;
    SeekBar mRecogTimeSeekBar = null;
    CheckBox mUseGestureCheckBox = null;
    ExtendScrollView mMeanScrollView = null;
    ScrollView mMeanOuterScrollView = null;
    int mThemeMode = 0;
    int mRecogTime = 0;
    SettingMeanController mSettingMeanController = null;
    TextView mSettingTitleTextView = null;
    ExtendTextView mSettingContentTextView = null;
    private LinearLayout mSettingContentLayout = null;
    LinearLayout mSettingMeanLayout = null;
    private int previewLayout = 0;
    private boolean mIsTracking = false;
    private boolean mUseGesture = true;
    private boolean mIsCreate = true;
    int mCheckUpdateIntervalPos = 0;
    int nVisibleLayoutCnt = 0;
    CompoundButton.OnCheckedChangeListener mUseGestureCheckBoxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.SettingActivity.2
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == SettingActivity.this.mUseGestureCheckBox) {
                SettingActivity.this.mUseGesture = isChecked;
                DictUtils.setGestureRecognitionToPreference(SettingActivity.this, SettingActivity.this.mUseGesture);
            }
        }
    };
    SeekBar.OnSeekBarChangeListener mRecogTimeSeekBarOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.diotek.diodict.SettingActivity.3
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String timeText;
            if ((progress / 100) % 10 > 0) {
                float time = ((progress / 100) / 10.0f) + 0.0f;
                timeText = "" + time;
            } else {
                int time2 = ((progress / 100) / 10) + 0;
                timeText = "" + time2;
            }
            if (SettingActivity.this.mPreviewText != null) {
                SettingActivity.this.mPreviewText.setText(timeText);
            }
            SettingActivity.this.mRecogTime = progress + 0;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            SettingActivity.this.mIsTracking = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            SettingActivity.this.mRecogTime = seekBar.getProgress() + 0;
            DictUtils.setRecogTimeToPreference(SettingActivity.this, SettingActivity.this.mRecogTime);
            SettingActivity.this.mIsTracking = false;
            SettingActivity.this.dismissTimePreview();
        }
    };
    private View.OnTouchListener mTimeDurationSeekBarOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SettingActivity.4
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 2 && SettingActivity.this.mIsTracking) {
                if (event.getX() <= v.getRight()) {
                    SettingActivity.this.showAtLocationTimePreview(event.getX());
                    return false;
                }
                SettingActivity.this.dismissTimePreview();
                return false;
            }
            return false;
        }
    };
    CompoundButton.OnCheckedChangeListener mThemeOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.SettingActivity.5
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView == SettingActivity.this.mTheme2) {
                    SettingActivity.this.mThemeMode = 1;
                } else if (buttonView == SettingActivity.this.mTheme3) {
                    SettingActivity.this.mThemeMode = 2;
                } else {
                    SettingActivity.this.mThemeMode = 0;
                }
                buttonView.requestFocus();
                if (SettingActivity.this.mSettingMeanController != null) {
                    SettingActivity.this.mSettingMeanController.refreshSettingView();
                }
            }
        }
    };
    BaseMeanController.ThemeModeCallback mThemeModeCallback = new BaseMeanController.ThemeModeCallback() { // from class: com.diotek.diodict.SettingActivity.6
        @Override // com.diotek.diodict.mean.BaseMeanController.ThemeModeCallback
        public int getThemeMode() {
            return SettingActivity.this.mThemeMode;
        }
    };
    View.OnTouchListener mMeanTextViewOnTouchListener = new View.OnTouchListener() { // from class: com.diotek.diodict.SettingActivity.7
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 1) {
                SettingActivity.this.mMeanOuterScrollView.requestDisallowInterceptTouchEvent(false);
            } else {
                SettingActivity.this.mMeanOuterScrollView.requestDisallowInterceptTouchEvent(true);
            }
            return true;
        }
    };
    View.OnFocusChangeListener mRecogTimeSeekBarOnFocusListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.SettingActivity.8
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            v.setPressed(v.isFocused());
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT < 11) {
			requestWindowFeature(7);
		}
		this.mCurrentMenuId = 1001;
		if (super.onCreateActivity(savedInstanceState)) {
			if (Dependency.isContainTTS()) {
				setVolumeControlStream(3);
			}
			this.mIsCreate = true;
			initActivity();
		}
	}

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mIsCreate = false;
        initActivity();
        if (this.mTheme1 != null && this.mTheme1.isChecked()) {
            this.mTheme1.requestFocus();
        } else if (this.mTheme2 != null && this.mTheme2.isChecked()) {
            this.mTheme2.requestFocus();
        } else if (this.mTheme3 != null && this.mTheme3.isChecked()) {
            this.mTheme3.requestFocus();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                Preference.fontTheme(this.mThemeMode);
                if (isVisiableView(this.mRecogTimeSeekBar) && this.mRecogTimeSeekBar.isFocused()) {
                    DictUtils.setRecogTimeToPreference(this, this.mRecogTime);
                }
                if (DictUtils.getRefreshViewStateFromPreference(getApplicationContext())) {
                    DictUtils.setRefreshViewStateToPreference(getApplicationContext(), false);
                    runSearchBtn(true);
                    break;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onDestroy() {
        super.onDestroy();
    }

    private void initActivity() {
        this.nVisibleLayoutCnt = 0;
        setContentView(R.layout.setting_layout);
        prepareTitleLayout(R.string.title_settings, this.mIsCreate);
        preparePreference();
        prepareContentsLayout();
        activeLayoutByDevice();
        Display display = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        int windowHeight = display.getHeight();
        int marginHeight = getResources().getDimensionPixelSize(R.dimen.themeTitle_height) + getResources().getDimensionPixelSize(R.dimen.appTitle_height);
        if (marginHeight < 0) {
            marginHeight = 0;
        }
        int maxThemeHeight = windowHeight - marginHeight;
        if (this.nVisibleLayoutCnt == 1 && maxThemeHeight > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, maxThemeHeight);
            ((LinearLayout) findViewById(R.id.themeSetting)).setLayoutParams(params);
            ((LinearLayout) findViewById(R.id.themeSetting)).invalidate();
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(-1, -1);
            this.mMeanScrollView.setLayoutParams(params2);
            this.mMeanScrollView.invalidate();
        }
        if (isVisiableView(this.mTheme1)) {
            this.mTheme1.requestFocus();
        }
    }

    private void prepareContentsLayout() {
        this.mSettingMeanLayout = (LinearLayout) findViewById(R.id.SettingMeanLayout);
        this.mSettingContentLayout = (LinearLayout) findViewById(R.id.recogTimeLayout);
        this.mMeanScrollView = (ExtendScrollView) findViewById(R.id.scrollview);
        this.mMeanScrollView.setFocusable(false);
        this.mMeanOuterScrollView = (ScrollView) findViewById(R.id.outerScrollView);
        prepareTimePreviewLayout();
    }

    private void prepareTimePreviewLayout() {
        LayoutInflater inflate = (LayoutInflater) getSystemService("layout_inflater");
        this.mPreviewPopup = new PopupWindow(this);
        this.previewLayout = R.layout.cradle_timepreview_popup;
        if (this.previewLayout != 0) {
            this.mPreviewText = (TextView) inflate.inflate(this.previewLayout, (ViewGroup) null);
            this.mPreviewText.setTextSize(20.0f);
            this.mPreviewPopup.setContentView(this.mPreviewText);
            this.mPreviewPopup.setBackgroundDrawable(null);
        }
        this.mPreviewPopup.setTouchable(false);
    }

    private void preparePreference() {
        prepareThemeLayout();
        if (this.mIsCreate) {
            this.mRecogTime = DictUtils.getRecogTimeFromPreference(this);
        }
        this.mRecogTimeSeekBar = (SeekBar) findViewById(R.id.recogTimeSeekBar);
        this.mRecogTimeSeekBar.setProgress(this.mRecogTime + 0);
        this.mRecogTimeSeekBar.setOnSeekBarChangeListener(this.mRecogTimeSeekBarOnSeekBarChangeListener);
        this.mRecogTimeSeekBar.setOnTouchListener(this.mTimeDurationSeekBarOnTouchListener);
        this.mRecogTimeSeekBar.setOnFocusChangeListener(this.mRecogTimeSeekBarOnFocusListener);
        if (this.mIsCreate) {
            this.mUseGesture = DictUtils.getGestureRecognitionFromPreference(this);
        }
        this.mRecogTimeSeekBar.setFocusable(true);
        this.mUseGestureCheckBox = (CheckBox) findViewById(R.id.GestureSettingCheckBox);
        this.mUseGestureCheckBox.setChecked(this.mUseGesture);
        this.mUseGestureCheckBox.setFocusable(true);
        this.mUseGestureCheckBox.setOnCheckedChangeListener(this.mUseGestureCheckBoxOnCheckedChangeListener);
    }

    private void prepareThemeLayout() {
        if (this.mIsCreate) {
            this.mThemeMode = DictUtils.getFontThemeFromPreference(this);
        }
        this.mTheme1 = (RadioButton) findViewById(R.id.theme1Btn);
        this.mTheme1.setOnCheckedChangeListener(this.mThemeOnCheckedChangeListener);
        this.mTheme2 = (RadioButton) findViewById(R.id.theme2Btn);
        this.mTheme2.setOnCheckedChangeListener(this.mThemeOnCheckedChangeListener);
        this.mTheme3 = (RadioButton) findViewById(R.id.theme3Btn);
        this.mTheme3.setOnCheckedChangeListener(this.mThemeOnCheckedChangeListener);
        switch (this.mThemeMode) {
            case 1:
                this.mTheme2.setChecked(true);
                break;
            case 2:
                this.mTheme3.setChecked(true);
                break;
            default:
                this.mTheme1.setChecked(true);
                break;
        }
        this.mSettingTitleTextView = (TextView) findViewById(R.id.MeanTitleTextView);
        this.mSettingContentTextView = (ExtendTextView) findViewById(R.id.MeanContentTextView);
        this.mSettingContentTextView.setOnTouchListener(this.mMeanTextViewOnTouchListener);
        this.mSettingContentTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.diotek.diodict.SettingActivity.1
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View v, boolean hasFocus) {
                if (SettingActivity.this.mMeanScrollView != null) {
                    if (hasFocus) {
                        SettingActivity.this.mMeanScrollView.setBackgroundColor(SettingActivity.this.getResources().getColor(R.color.setting_mean_focus_color));
                    } else {
                        SettingActivity.this.mMeanScrollView.setBackgroundColor(0);
                    }
                }
            }
        });
        this.mSettingTitleTextView.setPadding(5, 0, 5, 0);
        this.mSettingContentTextView.setPadding(5, 5, 5, 5);
        this.mEngine = EngineManager3rd.getInstance(this);
        this.mSettingMeanController = new SettingMeanController(this, this.mSettingTitleTextView, this.mSettingContentTextView, null, null, this.mEngine, true, this.mThemeModeCallback, null);
        this.mSettingMeanController.setMeanView(0);
        this.nVisibleLayoutCnt++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissTimePreview() {
        if (this.mPreviewPopup != null && this.mPreviewPopup.isShowing()) {
            this.mPreviewPopup.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAtLocationTimePreview(float x) {
        PopupWindow previewPopup = this.mPreviewPopup;
        int[] mOffsetInWindow = null;
        if (previewPopup != null) {
            if (0 == 0) {
                mOffsetInWindow = new int[2];
                this.mSettingContentLayout.getLocationInWindow(mOffsetInWindow);
            }
            int mPopupPreviewX = ((int) x) - 44;
            int mPopupPreviewY = this.mRecogTimeSeekBar.getTop() - 88;
            if (mPopupPreviewX < 0) {
                mPopupPreviewX = 0;
            }
            if (previewPopup.isShowing()) {
                previewPopup.update(mPopupPreviewX, mOffsetInWindow[1] + mPopupPreviewY, 88, 88);
                return;
            }
            previewPopup.setWidth(88);
            previewPopup.setHeight(88);
            previewPopup.showAtLocation(this.mSettingContentLayout, 0, mPopupPreviewX, mOffsetInWindow[1] + mPopupPreviewY);
        }
    }

    private void activeLayoutByDevice() {
        if (!Dependency.isContainHandWrightReocg()) {
            RelativeLayout recogTimeLayout = (RelativeLayout) findViewById(R.id.recogTime);
            recogTimeLayout.setVisibility(View.GONE);
            return;
        }
        this.nVisibleLayoutCnt++;
    }
}
