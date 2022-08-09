package com.diotek.diodict;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.mean.MSG;
import com.diotek.diodict3.phone.samsung.chn.R;

/* loaded from: classes.dex */
public class MemoActivity extends Activity {
    private int mDicType;
    private String mKeyWord;
    private TextView mMemoDateView;
    private TextView mMemoTitleView;
    private int mSUID;
    private final int MAX_REMAIN = 500;
    private EditText mMemoDataEditText = null;
    private TextView mMemoDataTextView = null;
    private RelativeLayout mMemoLayout = null;
    private TextView mMemoRemainView = null;
    private int mKindsOfSkin = 1;
    private Button mOkButton = null;
    private Button mCancelButton = null;
    private Button mEditButton = null;
    private Button mDeleteButton = null;
    private RelativeLayout mMemoEditLayout = null;
    private RelativeLayout mMemoNormalLayout = null;
    private RadioGroup mMemoSkinLayout = null;
    private ScrollView mScrollview = null;
    private LinearLayout mDataLayout = null;
    public Handler mHandler = new Handler();
    private int[] TEXT_COLOR = {Color.rgb(250, 255, 155), Color.rgb(230, 255, 155), Color.rgb(180, 250, 255)};
    private int[] LINE_COLOR = {Color.rgb(215, 140, 0), Color.rgb((int) DHWR.DLANG_THAI_DIGIT, 180, 5), Color.rgb(45, 145, 165)};
    private int MEMO_STATE_NONE = 0;
    private int MEMO_STATE_NORMAL = 1;
    private int MEMO_STATE_EDIT = 2;
    private AlertDialog mDeletePopup = null;
    DialogInterface.OnClickListener mMemoDeleteOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.MemoActivity.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if (MemoActivity.this.mDeletePopup != null) {
                MemoActivity.this.setActivityResult(true);
                MemoActivity.this.mDeletePopup.dismiss();
                MemoActivity.this.mDeletePopup = null;
            }
        }
    };
    DialogInterface.OnClickListener mMemoDeleteCancelOnClickListener = new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.MemoActivity.2
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if (MemoActivity.this.mDeletePopup != null) {
                MemoActivity.this.mDeletePopup.dismiss();
                MemoActivity.this.mDeletePopup = null;
            }
        }
    };
    private TextWatcher mMemoDataTextWatcher = new TextWatcher() { // from class: com.diotek.diodict.MemoActivity.3
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            MemoActivity.this.updateRemainCharacter(s.toString());
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    private View.OnClickListener mSkinViewOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.MemoActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            int resid = R.drawable.memo_skin_bg_01;
            int kinds = 1;
            switch (v.getId()) {
                case R.id.memo_skin_kinds_2 /* 2131100032 */:
                    resid = R.drawable.memo_skin_bg_02;
                    kinds = 2;
                    break;
                case R.id.memo_skin_kinds_3 /* 2131100033 */:
                    resid = R.drawable.memo_skin_bg_03;
                    kinds = 3;
                    break;
            }
            if (MemoActivity.this.mMemoLayout != null) {
                MemoActivity.this.mMemoLayout.setBackgroundResource(resid);
            }
            MemoActivity.this.mKindsOfSkin = kinds;
            MemoActivity.this.applySkinAttribute();
        }
    };
    private View.OnClickListener mButtonOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.MemoActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            boolean isHideKeyboard = true;
            switch (v.getId()) {
                case R.id.memo_ok /* 2131100044 */:
                    if (MemoActivity.this.mMemoDataEditText.getText().length() > 0) {
                        MemoActivity.this.setActivityResult(false);
                        break;
                    } else {
                        isHideKeyboard = false;
                        String message = MemoActivity.this.getResources().getString(R.string.inputtext_empty_message);
                        Toast.makeText(MemoActivity.this, message, 0).show();
                        break;
                    }
                case R.id.memo_cancel /* 2131100045 */:
                    MemoActivity.this.finish();
                    break;
                case R.id.memo_edit /* 2131100047 */:
                    MemoActivity.this.setMemoState(true);
                    break;
                case R.id.memo_delete /* 2131100048 */:
                    MemoActivity.this.showMemoDeleteDialog();
                    break;
            }
            if (isHideKeyboard) {
                MemoActivity.this.showHideSystemInputMethod(false);
            }
        }
    };
    private Runnable mHideOnlySoftInput = new Runnable() { // from class: com.diotek.diodict.MemoActivity.6
        @Override // java.lang.Runnable
        public void run() {
            InputMethodManager imm = (InputMethodManager) MemoActivity.this.getApplicationContext().getSystemService("input_method");
            if (imm != null && MemoActivity.this.mMemoDataEditText != null) {
                imm.hideSoftInputFromWindow(MemoActivity.this.mMemoDataEditText.getWindowToken(), 0);
            } else {
                MSG.l(2, "mhideOnlySoftInput():imm or mSearchInput is null");
            }
        }
    };
    private Runnable mShowOnlySoftInput = new Runnable() { // from class: com.diotek.diodict.MemoActivity.7
        @Override // java.lang.Runnable
        public void run() {
            InputMethodManager imm = (InputMethodManager) MemoActivity.this.getApplicationContext().getSystemService("input_method");
            if (imm != null && MemoActivity.this.mMemoDataEditText != null) {
                imm.showSoftInput(MemoActivity.this.mMemoDataEditText, 0);
            } else {
                MSG.l(2, "mshowOnlySoftInput():imm or mSearchInput is null");
            }
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(1);
        }
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = 2;
        lpWindow.dimAmount = 0.3f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.memo_layout);
        prepareLayout();
        applySkinAttribute();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        String text = this.mMemoDataEditText.getText().toString();
        setContentView(R.layout.memo_layout);
        prepareLayout();
        applySkinAttribute();
        if (this.mDeletePopup != null && this.mDeletePopup.isShowing()) {
            this.mDeletePopup.setTitle(getResources().getString(R.string.memo_delete_question));
            this.mDeletePopup.getButton(-1).setText(R.string.delete);
            this.mDeletePopup.getButton(-2).setText(R.string.dialog_cancel);
        }
        if (this.mMemoDataEditText != null && text != null) {
            this.mMemoDataEditText.setText(text);
            this.mMemoDataEditText.setSelection(text.length());
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                if (this.mOkButton.isFocused()) {
                    this.mButtonOnClickListener.onClick(this.mOkButton);
                    break;
                } else if (this.mCancelButton.isFocused()) {
                    this.mButtonOnClickListener.onClick(this.mCancelButton);
                    break;
                } else if (this.mEditButton.isFocused()) {
                    this.mButtonOnClickListener.onClick(this.mEditButton);
                    break;
                } else if (this.mDeleteButton.isFocused()) {
                    this.mButtonOnClickListener.onClick(this.mDeleteButton);
                    break;
                } else {
                    int i = 0;
                    while (true) {
                        if (i >= this.mMemoSkinLayout.getChildCount()) {
                            break;
                        } else {
                            RadioButton btn = (RadioButton) this.mMemoSkinLayout.getChildAt(i);
                            if (!btn.isFocused()) {
                                i++;
                            } else {
                                this.mSkinViewOnClickListener.onClick(btn);
                                break;
                            }
                        }
                    }
                }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void prepareLayout() {
        Intent intent = getIntent();
        String initDateString = intent.getStringExtra(DictInfo.INTENT_MEMO_INFO_TIME);
        String initMemoDataString = intent.getStringExtra(DictInfo.INTENT_MEMO_INFO_DATA);
        this.mDicType = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_DICT, 1);
        this.mKeyWord = intent.getStringExtra(DictInfo.INTENT_MEMO_INFO_WORD);
        this.mSUID = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_SUID, 1);
        this.mKindsOfSkin = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_SKIN, 1);
        this.mMemoLayout = (RelativeLayout) findViewById(R.id.memo_layout);
        this.mMemoRemainView = (TextView) findViewById(R.id.memo_remain);
        this.mMemoDateView = (TextView) findViewById(R.id.memo_timestamp);
        this.mMemoTitleView = (TextView) findViewById(R.id.memo_title);
        this.mMemoEditLayout = (RelativeLayout) findViewById(R.id.memo_normal_layout);
        this.mMemoDataTextView = (TextView) findViewById(R.id.memo_data_view);
        this.mDeleteButton = (Button) findViewById(R.id.memo_delete);
        this.mEditButton = (Button) findViewById(R.id.memo_edit);
        this.mScrollview = (ScrollView) findViewById(R.id.scrollview);
        this.mMemoNormalLayout = (RelativeLayout) findViewById(R.id.memo_edit_layout);
        this.mDataLayout = (LinearLayout) findViewById(R.id.DataLayout);
        this.mMemoSkinLayout = (RadioGroup) findViewById(R.id.skin_group);
        this.mMemoDataEditText = (EditText) findViewById(R.id.memo_data);
        this.mOkButton = (Button) findViewById(R.id.memo_ok);
        this.mCancelButton = (Button) findViewById(R.id.memo_cancel);
        this.mOkButton.setOnClickListener(this.mButtonOnClickListener);
        this.mCancelButton.setOnClickListener(this.mButtonOnClickListener);
        this.mDeleteButton.setOnClickListener(this.mButtonOnClickListener);
        this.mEditButton.setOnClickListener(this.mButtonOnClickListener);
        for (int i = 0; i < this.mMemoSkinLayout.getChildCount(); i++) {
            ((RadioButton) this.mMemoSkinLayout.getChildAt(i)).setOnClickListener(this.mSkinViewOnClickListener);
        }
        ((RadioButton) this.mMemoSkinLayout.getChildAt(this.mKindsOfSkin - 1)).setChecked(true);
        this.mMemoDataEditText.addTextChangedListener(this.mMemoDataTextWatcher);
        this.mMemoDataEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
        if (initDateString.length() > 0) {
            this.mMemoDateView.setText(initDateString);
        } else {
            long time = System.currentTimeMillis();
            this.mMemoDateView.setText(DictUtils.getDateString(time));
        }
        int editState = intent.getIntExtra(DictInfo.INTENT_MEMO_INFO_STATE, this.MEMO_STATE_NONE);
        if (initMemoDataString.length() > 0 && editState != this.MEMO_STATE_EDIT) {
            setMemoState(false);
            setMemoData(initMemoDataString);
        } else {
            String hint = getResources().getString(R.string.memo_hint);
            this.mMemoDataEditText.setHint(hint);
            setMemoState(true);
        }
        updateRemainCharacter(this.mMemoDataEditText.getText().toString());
        this.mMemoDataEditText.setSelection(0);
    }

    private void setMemoData(String initMemoDataString) {
        this.mMemoDataEditText.setText(initMemoDataString);
        this.mMemoDataTextView.setText(initMemoDataString);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMemoState(boolean isEditable) {
        int memoState;
        int i = this.MEMO_STATE_NONE;
        if (isEditable) {
            this.mMemoEditLayout.setVisibility(View.VISIBLE);
            this.mMemoSkinLayout.setVisibility(View.VISIBLE);
            this.mMemoNormalLayout.setVisibility(View.GONE);
            this.mDataLayout.setVisibility(View.VISIBLE);
            this.mScrollview.setVisibility(View.GONE);
            this.mMemoDataEditText.setSelection(this.mMemoDataEditText.length());
            this.mMemoDataEditText.requestFocus();
            showHideSystemInputMethod(true);
            memoState = this.MEMO_STATE_EDIT;
        } else {
            this.mMemoNormalLayout.setVisibility(View.VISIBLE);
            this.mScrollview.setVisibility(View.VISIBLE);
            this.mMemoEditLayout.setVisibility(View.GONE);
            this.mDataLayout.setVisibility(View.GONE);
            this.mMemoSkinLayout.setVisibility(View.INVISIBLE);
            memoState = this.MEMO_STATE_NORMAL;
        }
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_STATE, memoState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applySkinAttribute() {
        int resid = R.drawable.memo_skin_bg_01;
        switch (this.mKindsOfSkin) {
            case 2:
                resid = R.drawable.memo_skin_bg_02;
                break;
            case 3:
                resid = R.drawable.memo_skin_bg_03;
                break;
        }
        if (this.mMemoLayout != null) {
            this.mMemoLayout.setBackgroundResource(resid);
        }
        int textColor = this.TEXT_COLOR[this.mKindsOfSkin - 1];
        int lineColor = this.LINE_COLOR[this.mKindsOfSkin - 1];
        this.mMemoTitleView.setTextColor(textColor);
        this.mMemoDateView.setTextColor(textColor);
        this.mMemoRemainView.setTextColor(textColor);
        ((LinedEditText) this.mMemoDataEditText).setLineColor(lineColor);
        ((LinedTextView) this.mMemoDataTextView).setLineColor(lineColor);
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra(DictInfo.INTENT_MEMO_INFO_SKIN, this.mKindsOfSkin);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMemoDeleteDialog() {
        if (this.mDeletePopup == null || !this.mDeletePopup.isShowing()) {
            AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
            Dialog.setCancelable(true);
            Dialog.setPositiveButton(R.string.delete, this.mMemoDeleteOnClickListener);
            Dialog.setNegativeButton(R.string.dialog_cancel, this.mMemoDeleteCancelOnClickListener);
            Dialog.setTitle(getResources().getString(R.string.memo_delete_question));
            this.mDeletePopup = Dialog.create();
            Window win = this.mDeletePopup.getWindow();
            WindowManager.LayoutParams wlp = win.getAttributes();
            wlp.type = 1003;
            win.setAttributes(wlp);
            this.mDeletePopup.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActivityResult(boolean isDelete) {
        String data = "";
        if (!isDelete) {
            data = this.mMemoDataEditText.getText().toString();
        }
        Intent intent = new Intent();
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_DATA, data);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_SKIN, this.mKindsOfSkin);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_DICT, this.mDicType);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_WORD, this.mKeyWord);
        intent.putExtra(DictInfo.INTENT_MEMO_INFO_SUID, this.mSUID);
        setResult(-1, intent);
        finish();
    }

    protected void updateRemainCharacter(String string) {
        String remain = makeRemainCharacter(string.length());
        if (this.mMemoRemainView != null) {
            this.mMemoRemainView.setText(remain);
        }
    }

    protected String makeRemainCharacter(int curLength) {
        String remain = "" + Integer.toString(curLength);
        return (remain + "/") + Integer.toString(500);
    }

    /* loaded from: classes.dex */
    public static class LinedEditText extends EditText {
        private int max_line = 4;
        private Rect mRect = new Rect();
        private Paint mPaint = new Paint();

        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(-2147483393);
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(Canvas canvas) {
            int count = getLineCount();
            Rect r = this.mRect;
            Paint paint = this.mPaint;
            int startLine = getBaseline();
            getLineBounds(0, r);
            int repeat = Math.max(this.max_line, count);
            for (int i = 0; i < repeat; i++) {
                int baseline = startLine + (getLineHeight() * i);
                canvas.drawLine(r.left, baseline + 2, r.right, baseline + 2, paint);
            }
            super.onDraw(canvas);
        }

        public void setLineColor(int color) {
            this.mPaint.setColor(color);
        }
    }

    /* loaded from: classes.dex */
    public static class LinedTextView extends TextView {
        private int max_line = 5;
        private Rect mRect = new Rect();
        private Paint mPaint = new Paint();

        public LinedTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(-2147483393);
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(Canvas canvas) {
            int count = getLineCount();
            Rect r = this.mRect;
            Paint paint = this.mPaint;
            int startLine = getBaseline();
            getLineBounds(0, r);
            int repeat = Math.max(this.max_line, count);
            for (int i = 0; i < repeat; i++) {
                int baseline = startLine + (getLineHeight() * i);
                canvas.drawLine(r.left, baseline + 2, r.right, baseline + 2, paint);
            }
            super.onDraw(canvas);
        }

        public void setLineColor(int color) {
            this.mPaint.setColor(color);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showHideSystemInputMethod(boolean bShow) {
        if (bShow) {
            this.mHandler.postDelayed(this.mShowOnlySoftInput, 300L);
        } else {
            this.mHandler.post(this.mHideOnlySoftInput);
        }
    }
}
