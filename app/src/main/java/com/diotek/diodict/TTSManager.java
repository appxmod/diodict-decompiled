package com.diotek.diodict;

import android.app.Activity;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictDBManager;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.engine.EngineInfo3rd;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.BaseMeanController;
import com.diotek.diodict.mean.ExtendTextView;
import com.diotek.diodict.uitool.CommonUtils;
import com.diodict.decompiled.R;
import java.util.Locale;

/* loaded from: classes.dex */
public class TTSManager {
    private View.OnKeyListener mContentOnKeyListener;
    Activity mContext;
    EngineManager3rd mEngine;
    private ExtendTextView mMeanTextView;
    View mParentView;
    View mRootView;
    Runnable mRunnablePlayTTS;
    private View mRunnableTTSBtn;
    public BaseMeanController.MeanControllerCallback mTTSLayoutCallback;
    View.OnClickListener mTTSOnClickListner;
    protected View.OnClickListener mTTSRepeatStopOnClickListener;
    protected PopupWindow.OnDismissListener mTTSRepeatStopOnDismissListener;
    public PopupWindow mTTSStopPopup;
    private TextView mTitleTextView;
    private Button mUKOnceBtn;
    private ImageButton mUKRepeatBtn;
    private Button mUSOnceBtn;
    private ImageButton mUSRepeatBtn;

    public TTSManager(Activity context) {
        this.mUSOnceBtn = null;
        this.mUSRepeatBtn = null;
        this.mUKOnceBtn = null;
        this.mUKRepeatBtn = null;
        this.mMeanTextView = null;
        this.mTitleTextView = null;
        this.mRunnableTTSBtn = null;
        this.mTTSOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.TTSManager.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TTSManager.this.mRunnableTTSBtn = v;
                if (TTSManager.this.mTitleTextView != null) {
                    TTSManager.this.mMeanTextView.setKeyword(TTSManager.this.mTitleTextView.getText().toString());
                }
                if (!CommonUtils.isPlaying()) {
                    switch (TTSManager.this.mRunnableTTSBtn.getId()) {
                        case R.id.USRepeatBtn /* 2131099973 */:
                        case R.id.UKRepeatBtn /* 2131099975 */:
                            TTSManager.this.showTTSStopPopup(TTSManager.this.mRunnableTTSBtn);
                            break;
                    }
                    new Thread(TTSManager.this.mRunnablePlayTTS).start();
                }
            }
        };
        this.mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.TTSManager.2
            @Override // java.lang.Runnable
            public void run() {
                int count = 1;
                switch (TTSManager.this.mRunnableTTSBtn.getId()) {
                    case R.id.USRepeatBtn /* 2131099973 */:
                    case R.id.UKRepeatBtn /* 2131099975 */:
                        count = -1;
                        break;
                }
                if (TTSManager.this.mRunnableTTSBtn.getId() == R.id.USOnceBtn || TTSManager.this.mRunnableTTSBtn.getId() == R.id.USRepeatBtn) {
                    CommonUtils.playTTS(0, TTSManager.this.getPlayTTSWord(), TTSManager.this.getPlayTTSDbType(), count);
                } else {
                    CommonUtils.playTTS(1, TTSManager.this.getPlayTTSWord(), TTSManager.this.getPlayTTSDbType(), count);
                }
            }
        };
        this.mTTSLayoutCallback = new BaseMeanController.MeanControllerCallback() { // from class: com.diotek.diodict.TTSManager.3
            @Override // com.diotek.diodict.mean.BaseMeanController.MeanControllerCallback
            public boolean run() {
                TTSManager.this.showHideTTSLayout(true);
                return false;
            }
        };
        this.mTTSStopPopup = null;
        this.mContentOnKeyListener = new View.OnKeyListener() { // from class: com.diotek.diodict.TTSManager.4
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == 1) {
                    if (keyCode == 4 && TTSManager.this.isTTSRepeat()) {
                        TTSManager.this.onTerminateTTS();
                        return true;
                    }
                } else if (event.getAction() == 0 && DictUtils.setVolumeByKey(TTSManager.this.mContext, keyCode)) {
                    return true;
                }
                return false;
            }
        };
        this.mTTSRepeatStopOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.TTSManager.5
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                CommonUtils.stopTTS();
                TTSManager.this.mTTSStopPopup = null;
            }
        };
        this.mTTSRepeatStopOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.TTSManager.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TTSManager.this.onTerminateTTS();
            }
        };
        this.mContext = context;
        this.mParentView = null;
        this.mEngine = EngineManager3rd.getInstance(context);
    }

    public TTSManager(Activity context, View view) {
        this.mUSOnceBtn = null;
        this.mUSRepeatBtn = null;
        this.mUKOnceBtn = null;
        this.mUKRepeatBtn = null;
        this.mMeanTextView = null;
        this.mTitleTextView = null;
        this.mRunnableTTSBtn = null;
        this.mTTSOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.TTSManager.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TTSManager.this.mRunnableTTSBtn = v;
                if (TTSManager.this.mTitleTextView != null) {
                    TTSManager.this.mMeanTextView.setKeyword(TTSManager.this.mTitleTextView.getText().toString());
                }
                if (!CommonUtils.isPlaying()) {
                    switch (TTSManager.this.mRunnableTTSBtn.getId()) {
                        case R.id.USRepeatBtn /* 2131099973 */:
                        case R.id.UKRepeatBtn /* 2131099975 */:
                            TTSManager.this.showTTSStopPopup(TTSManager.this.mRunnableTTSBtn);
                            break;
                    }
                    new Thread(TTSManager.this.mRunnablePlayTTS).start();
                }
            }
        };
        this.mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.TTSManager.2
            @Override // java.lang.Runnable
            public void run() {
                int count = 1;
                switch (TTSManager.this.mRunnableTTSBtn.getId()) {
                    case R.id.USRepeatBtn /* 2131099973 */:
                    case R.id.UKRepeatBtn /* 2131099975 */:
                        count = -1;
                        break;
                }
                if (TTSManager.this.mRunnableTTSBtn.getId() == R.id.USOnceBtn || TTSManager.this.mRunnableTTSBtn.getId() == R.id.USRepeatBtn) {
                    CommonUtils.playTTS(0, TTSManager.this.getPlayTTSWord(), TTSManager.this.getPlayTTSDbType(), count);
                } else {
                    CommonUtils.playTTS(1, TTSManager.this.getPlayTTSWord(), TTSManager.this.getPlayTTSDbType(), count);
                }
            }
        };
        this.mTTSLayoutCallback = new BaseMeanController.MeanControllerCallback() { // from class: com.diotek.diodict.TTSManager.3
            @Override // com.diotek.diodict.mean.BaseMeanController.MeanControllerCallback
            public boolean run() {
                TTSManager.this.showHideTTSLayout(true);
                return false;
            }
        };
        this.mTTSStopPopup = null;
        this.mContentOnKeyListener = new View.OnKeyListener() { // from class: com.diotek.diodict.TTSManager.4
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == 1) {
                    if (keyCode == 4 && TTSManager.this.isTTSRepeat()) {
                        TTSManager.this.onTerminateTTS();
                        return true;
                    }
                } else if (event.getAction() == 0 && DictUtils.setVolumeByKey(TTSManager.this.mContext, keyCode)) {
                    return true;
                }
                return false;
            }
        };
        this.mTTSRepeatStopOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.TTSManager.5
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                CommonUtils.stopTTS();
                TTSManager.this.mTTSStopPopup = null;
            }
        };
        this.mTTSRepeatStopOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.TTSManager.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TTSManager.this.onTerminateTTS();
            }
        };
        this.mContext = context;
        this.mParentView = view;
        this.mEngine = EngineManager3rd.getInstance(context);
    }

    public TTSManager(Activity context, View parentView, View rootView) {
        this.mUSOnceBtn = null;
        this.mUSRepeatBtn = null;
        this.mUKOnceBtn = null;
        this.mUKRepeatBtn = null;
        this.mMeanTextView = null;
        this.mTitleTextView = null;
        this.mRunnableTTSBtn = null;
        this.mTTSOnClickListner = new View.OnClickListener() { // from class: com.diotek.diodict.TTSManager.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TTSManager.this.mRunnableTTSBtn = v;
                if (TTSManager.this.mTitleTextView != null) {
                    TTSManager.this.mMeanTextView.setKeyword(TTSManager.this.mTitleTextView.getText().toString());
                }
                if (!CommonUtils.isPlaying()) {
                    switch (TTSManager.this.mRunnableTTSBtn.getId()) {
                        case R.id.USRepeatBtn /* 2131099973 */:
                        case R.id.UKRepeatBtn /* 2131099975 */:
                            TTSManager.this.showTTSStopPopup(TTSManager.this.mRunnableTTSBtn);
                            break;
                    }
                    new Thread(TTSManager.this.mRunnablePlayTTS).start();
                }
            }
        };
        this.mRunnablePlayTTS = new Runnable() { // from class: com.diotek.diodict.TTSManager.2
            @Override // java.lang.Runnable
            public void run() {
                int count = 1;
                switch (TTSManager.this.mRunnableTTSBtn.getId()) {
                    case R.id.USRepeatBtn /* 2131099973 */:
                    case R.id.UKRepeatBtn /* 2131099975 */:
                        count = -1;
                        break;
                }
                if (TTSManager.this.mRunnableTTSBtn.getId() == R.id.USOnceBtn || TTSManager.this.mRunnableTTSBtn.getId() == R.id.USRepeatBtn) {
                    CommonUtils.playTTS(0, TTSManager.this.getPlayTTSWord(), TTSManager.this.getPlayTTSDbType(), count);
                } else {
                    CommonUtils.playTTS(1, TTSManager.this.getPlayTTSWord(), TTSManager.this.getPlayTTSDbType(), count);
                }
            }
        };
        this.mTTSLayoutCallback = new BaseMeanController.MeanControllerCallback() { // from class: com.diotek.diodict.TTSManager.3
            @Override // com.diotek.diodict.mean.BaseMeanController.MeanControllerCallback
            public boolean run() {
                TTSManager.this.showHideTTSLayout(true);
                return false;
            }
        };
        this.mTTSStopPopup = null;
        this.mContentOnKeyListener = new View.OnKeyListener() { // from class: com.diotek.diodict.TTSManager.4
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == 1) {
                    if (keyCode == 4 && TTSManager.this.isTTSRepeat()) {
                        TTSManager.this.onTerminateTTS();
                        return true;
                    }
                } else if (event.getAction() == 0 && DictUtils.setVolumeByKey(TTSManager.this.mContext, keyCode)) {
                    return true;
                }
                return false;
            }
        };
        this.mTTSRepeatStopOnDismissListener = new PopupWindow.OnDismissListener() { // from class: com.diotek.diodict.TTSManager.5
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                CommonUtils.stopTTS();
                TTSManager.this.mTTSStopPopup = null;
            }
        };
        this.mTTSRepeatStopOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.TTSManager.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TTSManager.this.onTerminateTTS();
            }
        };
        this.mContext = context;
        this.mParentView = parentView;
        this.mRootView = rootView;
        this.mEngine = EngineManager3rd.getInstance(context);
    }

    public void setExtendTextView(ExtendTextView meanView) {
        this.mMeanTextView = meanView;
    }

    public void setTitleTextView(TextView titleView) {
        this.mTitleTextView = titleView;
    }

    public void prepareMeanTTSLayout() {
        if (this.mParentView == null) {
            this.mUSOnceBtn = (Button) this.mContext.findViewById(R.id.USOnceBtn);
            this.mUSRepeatBtn = (ImageButton) this.mContext.findViewById(R.id.USRepeatBtn);
            this.mUKOnceBtn = (Button) this.mContext.findViewById(R.id.UKOnceBtn);
            this.mUKRepeatBtn = (ImageButton) this.mContext.findViewById(R.id.UKRepeatBtn);
        } else {
            this.mUSOnceBtn = (Button) this.mParentView.findViewById(R.id.USOnceBtn);
            this.mUSRepeatBtn = (ImageButton) this.mParentView.findViewById(R.id.USRepeatBtn);
            this.mUKOnceBtn = (Button) this.mParentView.findViewById(R.id.UKOnceBtn);
            this.mUKRepeatBtn = (ImageButton) this.mParentView.findViewById(R.id.UKRepeatBtn);
        }
        this.mUSOnceBtn.setOnClickListener(this.mTTSOnClickListner);
        this.mUSRepeatBtn.setOnClickListener(this.mTTSOnClickListner);
        this.mUKOnceBtn.setOnClickListener(this.mTTSOnClickListner);
        this.mUKRepeatBtn.setOnClickListener(this.mTTSOnClickListner);
    }

    public void prepareMeanTTSLayout(View.OnClickListener listener) {
        this.mUSOnceBtn = (Button) this.mContext.findViewById(R.id.USOnceBtn);
        this.mUSRepeatBtn = (ImageButton) this.mContext.findViewById(R.id.USRepeatBtn);
        this.mUKOnceBtn = (Button) this.mContext.findViewById(R.id.UKOnceBtn);
        this.mUKRepeatBtn = (ImageButton) this.mContext.findViewById(R.id.UKRepeatBtn);
        setOnClickListener(listener);
    }

    private void setOnClickListener(View.OnClickListener listener) {
        if (listener != null) {
            this.mUSOnceBtn.setOnClickListener(listener);
            this.mUSRepeatBtn.setOnClickListener(listener);
            this.mUKOnceBtn.setOnClickListener(listener);
            this.mUKRepeatBtn.setOnClickListener(listener);
        }
    }

    public void showHideTTSLayout(boolean bShow) {
        int nTTSFisrtBtnResId;
        int nTTSSecondBtnResId;
        Integer[] supportTTS;
        int nLang = 0;
        int nDicType = this.mEngine.getCurDict();
        if (this.mMeanTextView != null) {
            nDicType = this.mMeanTextView.getDbtype();
        }
        if (DictDBManager.getCpENGDictionary(nDicType)) {
            nLang = 65536;
        } else if (DictDBManager.getCpCHNDictionary(nDicType)) {
            nLang = EngineInfo3rd.TTS_CHINESE;
        } else if (DictDBManager.getCpJPNDictionary(nDicType)) {
            nLang = EngineInfo3rd.TTS_JAPANESE;
        } else if (DictDBManager.getCpKORDictionary(nDicType)) {
            nLang = EngineInfo3rd.TTS_KOREAN;
        }
        boolean bExist = false;
        int nVisibility = 8;
        int nTTSFisrtRepeatBtnResId = R.drawable.repeat;
        int nTTSSecondRepeatBtnResId = R.drawable.repeat;
        String nTTSFirstStr = "";
        String nTTSSecondStr = "";
        float nDensity = CommonUtils.getDeviceDensity(this.mContext);
        float nTTSTextSize = 10.0f;
        int nTTSLeftPadding = this.mContext.getResources().getDimensionPixelSize(R.dimen.mean_tts_btn_paddingLeft);
        if (nTTSLeftPadding <= 0) {
            nTTSLeftPadding = (int) (8.0f * nDensity);
        }
        int nTTSRightPadding = this.mContext.getResources().getDimensionPixelSize(R.dimen.mean_tts_btn_paddingRight);
        if (nTTSRightPadding <= 0) {
            nTTSLeftPadding = (int) (25.0f * nDensity);
        }
        int nUSBtnLeftPadding = nTTSLeftPadding;
        int nUKBtnLeftPadding = nTTSLeftPadding;
        if (Dependency.isContainTTS() && (supportTTS = EngineManager3rd.getSupporTTS()) != null) {
            int i = 0;
            while (true) {
                if (i >= supportTTS.length) {
                    break;
                } else if (nLang != supportTTS[i].intValue()) {
                    i++;
                } else {
                    bExist = true;
                    break;
                }
            }
        }
        if (bExist && bShow) {
            nVisibility = 0;
        }
        this.mUSOnceBtn.setVisibility(nVisibility);
        this.mUSRepeatBtn.setVisibility(nVisibility);
        if (nLang == 65536 || nLang == 65537) {
            if (DictUtils.checkExistSecoundTTSFile(nLang)) {
                if (nLang == 65536) {
                    nTTSFirstStr = this.mContext.getResources().getString(R.string.tts_us);
                    nTTSSecondStr = this.mContext.getResources().getString(R.string.tts_uk);
                    nTTSFisrtBtnResId = R.drawable.tts;
                    nTTSSecondBtnResId = R.drawable.tts;
                    this.mUSOnceBtn.setEnabled(true);
                    this.mUSRepeatBtn.setEnabled(true);
                } else {
                    nTTSFirstStr = this.mContext.getResources().getString(R.string.tts_zh_cn);
                    nTTSSecondStr = this.mContext.getResources().getString(R.string.tts_yue_cn);
                    nTTSTextSize = 8.4f;
                    String locale = Locale.getDefault().getISO3Language();
                    if (locale.contains("zho")) {
                        nUSBtnLeftPadding -= (int) (3.0f * nDensity);
                        nUKBtnLeftPadding += (int) (3.0f * nDensity);
                    }
                    if (CommonUtils.isUselessTTSWord(getPlayTTSWord(), nDicType)) {
                        nTTSFisrtBtnResId = R.drawable.tts_lite;
                        nTTSSecondBtnResId = R.drawable.tts_lite;
                        nTTSFisrtRepeatBtnResId = R.drawable.repeat_lite;
                        nTTSSecondRepeatBtnResId = R.drawable.repeat_lite;
                        this.mUSOnceBtn.setEnabled(false);
                        this.mUSRepeatBtn.setEnabled(false);
                    } else {
                        nTTSFisrtBtnResId = R.drawable.tts;
                        nTTSSecondBtnResId = R.drawable.tts;
                        this.mUSOnceBtn.setEnabled(true);
                        this.mUSRepeatBtn.setEnabled(true);
                    }
                }
            } else {
                nVisibility = 8;
                nTTSFisrtBtnResId = R.drawable.tts_etc;
                nTTSSecondBtnResId = R.drawable.tts_etc;
            }
        } else {
            nVisibility = 8;
            nTTSFisrtBtnResId = R.drawable.tts_etc;
            nTTSSecondBtnResId = R.drawable.tts_etc;
        }
        this.mUKOnceBtn.setVisibility(nVisibility);
        this.mUKRepeatBtn.setVisibility(nVisibility);
        this.mUSOnceBtn.setText(nTTSFirstStr);
        this.mUSOnceBtn.setTextSize(nTTSTextSize);
        this.mUSOnceBtn.setPadding(nUSBtnLeftPadding, 0, nTTSRightPadding, 0);
        this.mUSOnceBtn.setBackgroundResource(nTTSFisrtBtnResId);
        this.mUKOnceBtn.setBackgroundResource(nTTSSecondBtnResId);
        this.mUKOnceBtn.setText(nTTSSecondStr);
        this.mUKOnceBtn.setTextSize(nTTSTextSize);
        this.mUKOnceBtn.setPadding(nUKBtnLeftPadding, 0, nTTSRightPadding, 0);
        this.mUSRepeatBtn.setBackgroundResource(nTTSFisrtRepeatBtnResId);
        this.mUKRepeatBtn.setBackgroundResource(nTTSSecondRepeatBtnResId);
    }

    public void setEnableTTSButton(boolean isEnable) {
        if (this.mUSOnceBtn != null) {
            this.mUSOnceBtn.setEnabled(isEnable);
        }
        if (this.mUSRepeatBtn != null) {
            this.mUSRepeatBtn.setEnabled(isEnable);
        }
        if (this.mUKOnceBtn != null) {
            this.mUKOnceBtn.setEnabled(isEnable);
        }
        if (this.mUKRepeatBtn != null) {
            this.mUKRepeatBtn.setEnabled(isEnable);
        }
    }

    protected void showTTSStopPopup(View parents) {
        IBinder windowToken;
        if (this.mParentView != null) {
            int[] windowLocation = new int[2];
            LayoutInflater inflate = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            ImageView PopupContent = (ImageView) inflate.inflate(R.layout.tts_repeat_stop_popup, (ViewGroup) null);
            parents.getLocationInWindow(windowLocation);
            PopupContent.measure(0, 0);
            int popupWidth = PopupContent.getMeasuredWidth();
            int popupHeight = PopupContent.getMeasuredHeight();
            int popupY = this.mContext.getWindowManager().getDefaultDisplay().getHeight() - this.mParentView.getHeight();
            if (this.mTTSStopPopup == null) {
                this.mTTSStopPopup = CommonUtils.makeWindowWithPopupWindow(parents.getContext(), 0, PopupContent, this.mContext.getResources().getDrawable(R.drawable.trans), null, Dependency.getDevice().checkFocusableModel());
                if (this.mTTSStopPopup != null) {
                    ((ImageView) PopupContent.findViewById(R.id.tts_repeat_stop)).setOnClickListener(this.mTTSRepeatStopOnClickListener);
                    PopupContent.setOnKeyListener(this.mContentOnKeyListener);
                    this.mTTSStopPopup.setOnDismissListener(this.mTTSRepeatStopOnDismissListener);
                } else {
                    return;
                }
            }
            if (this.mTTSStopPopup.isShowing()) {
                this.mTTSStopPopup.update(windowLocation[0] + 5, windowLocation[1] + popupY, popupWidth, popupHeight);
                return;
            }
            this.mTTSStopPopup.setWidth(popupWidth);
            this.mTTSStopPopup.setHeight(popupHeight);
            View view = this.mContext.getCurrentFocus();
            if (view == null) {
                view = this.mRootView;
            }
            if (view != null && (windowToken = view.getWindowToken()) != null && windowToken.isBinderAlive()) {
                this.mTTSStopPopup.showAtLocation(view, 0, windowLocation[0] + 5, windowLocation[1] + popupY);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTTSRepeat() {
        return this.mTTSStopPopup != null && this.mTTSStopPopup.isShowing();
    }

    protected String getPlayTTSWord() {
        if (this.mMeanTextView == null) {
            return "";
        }
        if (this.mMeanTextView.isSelectedText()) {
            String word = this.mMeanTextView.getSelectedString();
            return word;
        }
        String word2 = this.mMeanTextView.getKeyword();
        return word2;
    }

    protected int getPlayTTSDbType() {
        return this.mMeanTextView.getDbtype();
    }

    public void onTerminateTTS() {
        CommonUtils.stopTTS();
        if (this.mTTSStopPopup != null) {
            this.mTTSStopPopup.dismiss();
            this.mTTSStopPopup = null;
        }
    }

    public void requestFocusTTSBtn() {
        if (this.mUSOnceBtn != null && this.mUSOnceBtn.getVisibility() == 0) {
            this.mUSOnceBtn.setFocusable(true);
            this.mUSOnceBtn.requestFocus();
        } else if (this.mUKOnceBtn != null && this.mUKOnceBtn.getVisibility() == 0) {
            this.mUKOnceBtn.setFocusable(true);
            this.mUKOnceBtn.requestFocus();
        }
    }

    public void setFocusTTSBtn(boolean bFocus) {
        if (this.mUSOnceBtn != null && this.mUSOnceBtn.getVisibility() == 0) {
            this.mUSOnceBtn.setFocusable(bFocus);
        }
        if (this.mUSRepeatBtn != null && this.mUSRepeatBtn.getVisibility() == 0) {
            this.mUSRepeatBtn.setFocusable(bFocus);
        }
        if (this.mUKOnceBtn != null && this.mUKOnceBtn.getVisibility() == 0) {
            this.mUKOnceBtn.setFocusable(bFocus);
        }
        if (this.mUKRepeatBtn != null && this.mUKRepeatBtn.getVisibility() == 0) {
            this.mUKRepeatBtn.setFocusable(bFocus);
        }
    }
}
