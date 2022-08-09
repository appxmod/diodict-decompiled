package com.diotek.diodict.mean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.ClipboardManager;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.diotek.diodict.CMN;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ExtendTextView extends TextView implements GestureDetector.OnGestureListener {
    public static final String GESTURE_SWIPE_LEFT = "swipe_left";
    public static final String GESTURE_SWIPE_RIGHT = "swipe_right";
    private static final int GRIP_X_WEIGHT = 2;
    private static final int GRIP_Y_WEIGHT = 20;
    private static final int MAX_MARKER = 100;
    private static final int TTS_NORMAL = 0;
    private static final int TTS_UK = 2;
    private static final int TTS_US = 1;
    private final int GRIP_DOWN;
    private final int GRIP_H_MASK;
    private final int GRIP_LEFT;
    private final int GRIP_RIGHT;
    private final int GRIP_UP;
    private final int GRIP_V_MASK;
    private final int SWIPE_THRESHOLD_PIXEL;
    private final int TEXT_SELECT_LEFT_GRIP;
    private final int TEXT_SELECT_NONE_GRIP;
    private final int TEXT_SELECT_RIGHT_GRIP;
    private final int VERTICAL_GAP;
    private boolean bWikiVisible;
    private Button google;
    private boolean isRemoveMode;
    private int mActivityGrip;
    private ExtendTextCallback mConfigChangeCallback;
    private Context mContext;
    private int mCurDbType;
    private String mCurKeyword;
    private int mCurSuid;
    private int mDisplayMode;
    protected AfterSetMeanViewCallback mFinishDrawMeanCallback;
    private GestureDetector flingDetector;
    private ExtendTextCallback mGoogleSearchCallback;
    private int mGripHeight;
    private int mGripWidth;
    Handler mHandler;
    private ExtendTextCallback mHyperTextCallback;
    private boolean mIsEnableInvalidate;
    private boolean mIsEnableTextSelect;
    private int mIsIntercept;
    private boolean mIsMarkerMode;
    private int mLeftGripMode;
    int[] mLeftGripPosition;
    private int mMarkerColor;
    private List<MarkerObject> mMarkerObj;
    private boolean mMarkerable;
    private ViewGroup mMeanLinearLayout;
    private boolean mMoving;
    private int mPopupBaseX;
    private int mPopupBaseY;
    private LinearLayout mPopupContent;
    private int[] mPopupMenuOffsetInWindow;
    private List<Rect> mPrevMakerRect;
    private TextArea mRemoveTextArea;
    private int mRightGripMode;
    int[] mRightGripPosition;
    private Runnable mRunnableDismissTextSelectGrip;
    private ExtendScrollView mScrollView;
    private TextArea mSelectTextArea;
    private int mSelectTextMode;
    private CharSequence mSelectedText;
    private int mStoreCurrentTopOffset;
    private int mSwipeThreshold;
    private ExtendTextCallback mTTSCallback;
    private TagConverterCallback mTagConverterCallback;
    private boolean mTextLineSelect;
    private int mTextSelectColor;
    private int[] mTextSelectGripOffsetInWindow;
    private PopupWindow mTextSelectLeftGrip;
    private PopupWindow mTextSelectPopupMenu;
    private View.OnClickListener mTextSelectPopupOnClickListener;
    private PopupWindow mTextSelectRightGrip;
    private ExtendTextCallback mUpdateLayoutCallback;
    private final int mWeightMovingX;
    private final float mWeightMovingY;
    private ExtendTextCallback mWikiSearchCallback;
    final String markerEnable;
    private Button wiki;

    /* loaded from: classes.dex */
    public interface AfterSetMeanViewCallback {
        int afterSetMean();
    }

    /* loaded from: classes.dex */
    public interface ExtendTextCallback {
        boolean run();

        boolean run(String str);
    }

    /* loaded from: classes.dex */
    public interface TagConverterCallback {
        ArrayList<TagConverter.DictPos> convert_Partial_Index(int i, int i2, int i3);

        ArrayList<TagConverter.DictPos> convert_Total_Index(int i, int i2, int i3);

        boolean isFirstBlock();

        TagConverter.DictPos isLinkOffset(int i);
    }

    public void setTagConverter(TagConverterCallback callback) {
        this.mTagConverterCallback = callback;
    }

    /* loaded from: classes.dex */
    public class TextArea {
        public int end;
        public int move;
        public int start;
        public int start_line;

        public TextArea(int s, int e, int m) {
            this.start = s;
            this.end = e;
            this.move = m;
        }

        public TextArea() {
            this.start = -1;
            this.move = -1;
            this.end = -1;
        }

        public void init() {
            this.start = -1;
            this.move = -1;
            this.end = -1;
        }

        public boolean isTextSelecting() {
            return (this.start == -1 || this.move == -1 || this.start == this.move) ? false : true;
        }

        public boolean isTextSelected() {
            return (this.start == -1 || this.end == -1 || this.start == this.end) ? false : true;
        }

        public boolean contains(int off) {
            return this.start <= off && off <= this.end;
        }
    }

    public ExtendTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.markerEnable = "marker_enable";
        this.mPopupBaseX = 0;
        this.mPopupBaseY = 0;
        this.mGripWidth = 38;
        this.mGripHeight = 72;
        this.mTextSelectColor = Color.argb(255, 255, 235, 170);
        this.mMarkerColor = -16776961;
        this.mMarkerObj = null;
        this.mContext = null;
        this.mMoving = false;
        this.mHandler = new Handler();
        this.mLeftGripPosition = new int[2];
        this.mRightGripPosition = new int[2];
        this.mMarkerable = false;
        this.GRIP_LEFT = 1;
        this.GRIP_RIGHT = 2;
        this.GRIP_H_MASK = 15;
        this.GRIP_UP = 268435456;
        this.GRIP_DOWN = 536870912;
        this.GRIP_V_MASK = -268435456;
        this.mLeftGripMode = 0;
        this.mRightGripMode = 0;
        this.TEXT_SELECT_NONE_GRIP = -1;
        this.TEXT_SELECT_LEFT_GRIP = 0;
        this.TEXT_SELECT_RIGHT_GRIP = 1;
        this.mSelectTextArea = new TextArea();
        this.mWeightMovingX = 2;
        this.mWeightMovingY = 1.2f;
        this.isRemoveMode = false;
        this.mRemoveTextArea = new TextArea();
        this.mScrollView = null;
        this.mMeanLinearLayout = null;
        this.mDisplayMode = 15;
        this.SWIPE_THRESHOLD_PIXEL = 100;
        this.mTextLineSelect = false;
        this.mIsEnableInvalidate = true;
        this.mIsEnableTextSelect = false;
        this.wiki = null;
        this.google = null;
        this.bWikiVisible = true;
        this.mTagConverterCallback = null;
        this.mIsIntercept = 0;
        this.mCurDbType = -1;
        this.mCurKeyword = null;
        this.mCurSuid = -1;
        this.VERTICAL_GAP = 10;
        this.mTextSelectPopupOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.mean.ExtendTextView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ExtendTextView.this.dismissTextSelectController();
                switch (v.getId()) {
                    case R.id.popup_copy /* 2131100016 */:
                        ExtendTextView.this.actionClipboardCopy();
                        return;
                    case R.id.popup_hyper /* 2131100017 */:
                        ExtendTextView.this.actionHyperText();
                        return;
                    case R.id.popup_google /* 2131100018 */:
                        ExtendTextView.this.actionGoogleSearch();
                        return;
                    case R.id.popup_wiki /* 2131100019 */:
                        ExtendTextView.this.actionWikiSearch();
                        return;
                    case R.id.popup_tts_us /* 2131100020 */:
                        ExtendTextView.this.actionTTS(1);
                        return;
                    case R.id.popup_tts_uk /* 2131100021 */:
                        ExtendTextView.this.actionTTS(2);
                        return;
                    case R.id.popup_tts /* 2131100022 */:
                        ExtendTextView.this.actionTTS(0);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mRunnableDismissTextSelectGrip = new Runnable() { // from class: com.diotek.diodict.mean.ExtendTextView.2
            @Override // java.lang.Runnable
            public void run() {
                ExtendTextView.this.dismissTextSelectGrip();
                ExtendTextView.this.hideTextSelectActionMenu();
                ExtendTextView.this.invalidate();
            }
        };
        initPrivateAttribute(context, attrs);
    }

    public ExtendTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.markerEnable = "marker_enable";
        this.mPopupBaseX = 0;
        this.mPopupBaseY = 0;
        this.mGripWidth = 38;
        this.mGripHeight = 72;
        this.mTextSelectColor = Color.argb(255, 255, 235, 170);
        this.mMarkerColor = -16776961;
        this.mMarkerObj = null;
        this.mContext = null;
        this.mMoving = false;
        this.mHandler = new Handler();
        this.mLeftGripPosition = new int[2];
        this.mRightGripPosition = new int[2];
        this.mMarkerable = false;
        this.GRIP_LEFT = 1;
        this.GRIP_RIGHT = 2;
        this.GRIP_H_MASK = 15;
        this.GRIP_UP = 268435456;
        this.GRIP_DOWN = 536870912;
        this.GRIP_V_MASK = -268435456;
        this.mLeftGripMode = 0;
        this.mRightGripMode = 0;
        this.TEXT_SELECT_NONE_GRIP = -1;
        this.TEXT_SELECT_LEFT_GRIP = 0;
        this.TEXT_SELECT_RIGHT_GRIP = 1;
        this.mSelectTextArea = new TextArea();
        this.mWeightMovingX = 2;
        this.mWeightMovingY = 1.2f;
        this.isRemoveMode = false;
        this.mRemoveTextArea = new TextArea();
        this.mScrollView = null;
        this.mMeanLinearLayout = null;
        this.mDisplayMode = 15;
        this.SWIPE_THRESHOLD_PIXEL = 100;
        this.mTextLineSelect = false;
        this.mIsEnableInvalidate = true;
        this.mIsEnableTextSelect = false;
        this.wiki = null;
        this.google = null;
        this.bWikiVisible = true;
        this.mTagConverterCallback = null;
        this.mIsIntercept = 0;
        this.mCurDbType = -1;
        this.mCurKeyword = null;
        this.mCurSuid = -1;
        this.VERTICAL_GAP = 10;
        this.mTextSelectPopupOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.mean.ExtendTextView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ExtendTextView.this.dismissTextSelectController();
                switch (v.getId()) {
                    case R.id.popup_copy /* 2131100016 */:
                        ExtendTextView.this.actionClipboardCopy();
                        return;
                    case R.id.popup_hyper /* 2131100017 */:
                        ExtendTextView.this.actionHyperText();
                        return;
                    case R.id.popup_google /* 2131100018 */:
                        ExtendTextView.this.actionGoogleSearch();
                        return;
                    case R.id.popup_wiki /* 2131100019 */:
                        ExtendTextView.this.actionWikiSearch();
                        return;
                    case R.id.popup_tts_us /* 2131100020 */:
                        ExtendTextView.this.actionTTS(1);
                        return;
                    case R.id.popup_tts_uk /* 2131100021 */:
                        ExtendTextView.this.actionTTS(2);
                        return;
                    case R.id.popup_tts /* 2131100022 */:
                        ExtendTextView.this.actionTTS(0);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mRunnableDismissTextSelectGrip = new Runnable() { // from class: com.diotek.diodict.mean.ExtendTextView.2
            @Override // java.lang.Runnable
            public void run() {
                ExtendTextView.this.dismissTextSelectGrip();
                ExtendTextView.this.hideTextSelectActionMenu();
                ExtendTextView.this.invalidate();
            }
        };
        initPrivateAttribute(context, attrs);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    public boolean getParsingMarkerable(AttributeSet attrs) {
        String enable = attrs.getAttributeValue(null, "marker_enable");
        if (enable != null && enable.equals("true")) {
            return true;
        }
        return false;
    }

    public void setMarkerable(boolean isEnable) {
        this.mMarkerable = isEnable;
    }

    @Override // android.widget.TextView
    public void setText(CharSequence text, TextView.BufferType type) {
        if (this.mTagConverterCallback == null || this.mTagConverterCallback.isFirstBlock()) {
            if (text != null && text.length() == 0) {
                this.mCurKeyword = "";
            }
            if (this.mContext == null) {
                this.mContext = getContext();
            }
            if (this.mContext != null) {
                setTypeface(DictUtils.createfont());
            }
            initializeSelectTextArea();
            dismissTextSelectController();
            dismissTextSelectGrip();
            this.mIsMarkerMode = false;
            this.mIsEnableInvalidate = true;
            super.setText(text, type);
            if (this.mFinishDrawMeanCallback != null) {
                this.mFinishDrawMeanCallback.afterSetMean();
                return;
            }
            return;
        }
        super.setText(text, type);
    }

    public void forceScrollStop() {
        if (this.mScrollView != null) {
            MotionEvent me = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, 0.0f, 0.0f, 0);
            this.mScrollView.dispatchTouchEvent(me);
        }
    }

    public void setCallback(ExtendTextCallback hyper, ExtendTextCallback google, ExtendTextCallback wiki, ExtendTextCallback TTS, ExtendTextCallback configChange, ExtendTextCallback updateLayoutCallback, AfterSetMeanViewCallback finishDrawMeanCallback) {
        this.mHyperTextCallback = hyper;
        this.mGoogleSearchCallback = google;
        this.mWikiSearchCallback = wiki;
        this.mTTSCallback = TTS;
        this.mConfigChangeCallback = configChange;
        this.mUpdateLayoutCallback = updateLayoutCallback;
        this.mFinishDrawMeanCallback = finishDrawMeanCallback;
    }

    @Override // android.widget.TextView
    public void setTextSize(float size) {
        setTextSize(size, true);
    }

    public void setTextSize(float size, boolean isStoreCurrentTopOffset) {
        if (isStoreCurrentTopOffset) {
            storeCurrentTopOffset();
        }
        super.setTextSize(size);
        if (this.mConfigChangeCallback != null) {
            this.mConfigChangeCallback.run();
        }
    }

    private Rect getInvalidateRect() {
        Rect invalidateRect = new Rect();
        int y = 0;
        if (this.mScrollView == null) {
            setScrollView();
        }
        if (this.mScrollView != null) {
            y = this.mScrollView.getScrollY();
        }
        int[] xy = new int[2];
        getLocationInTextView(xy, 0, y);
        Display display = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        int displayHeight = display.getHeight();
        int topLine = super.getLayout().getLineForVertical(xy[1]);
        invalidateRect.left = 0;
        if (topLine != 0) {
            topLine--;
        }
        int top = super.getLayout().getLineBaseline(topLine);
        int[] fxy = new int[2];
        getLocationInParentsView(fxy, 0, 0);
        if (topLine == 0) {
            invalidateRect.top = 0;
        } else {
            invalidateRect.top = top;
        }
        invalidateRect.right = super.getLayout().getWidth();
        invalidateRect.bottom = invalidateRect.top + displayHeight;
        if (this.mScrollView == null) {
            setScrollView();
        }
        return invalidateRect;
    }

    private void storeCurrentTopOffset() {
        if (this.mScrollView != null) {
            int y = this.mScrollView.getScrollY();
            int[] xy = new int[2];
            getLocationInTextView(xy, 0, y);
            int RestoreLine = super.getLayout().getLineForVertical(xy[1]);
            this.mStoreCurrentTopOffset = super.getLayout().getLineStart(RestoreLine);
            return;
        }
        setScrollView();
    }

    public int getRestoreScrollY() {
        if (this.mStoreCurrentTopOffset == 0) {
            return this.mStoreCurrentTopOffset;
        }
        if (super.getLayout() != null) {
            int line = super.getLayout().getLineForOffset(this.mStoreCurrentTopOffset);
            return super.getLayout().getLineBaseline(line);
        }
        return 0;
    }

    public void getLinePositionY(int off) {
        int y = 0;
        if (off > 0) {
            int line = super.getLayout().getLineForOffset(off);
            y = super.getLayout().getLineTop(line);
        }
        if (this.mScrollView == null) {
            setScrollView();
        }
        if (this.mScrollView != null) {
            this.mScrollView.scrollTo(0, y);
        }
    }

    private void restoreSettings(Context ctx) {
        int fontSizeIndex = DictUtils.getFontSizeFromPreference(ctx);
        int markerColorIndex = DictUtils.getMarkerColorFromPreference(ctx);
        int[] colorList = getResources().getIntArray(R.array.value_marker_color_adv);
        int[] fontSizeList = getResources().getIntArray(R.array.value_font_size);
        super.setTextSize(fontSizeList[fontSizeIndex]);
        setMarkerColor(colorList[markerColorIndex]);
        super.setTextColor(-16777216);
    }

    private void initPrivateAttribute(Context context, AttributeSet attrs) {
        if (this.mMarkerObj != null) {
            this.mMarkerObj.clear();
        }
        this.mMoving = false;
        this.mMarkerable = getParsingMarkerable(attrs);
        setTypeface(DictUtils.createfont());
        this.mContext = context;
        this.mIsMarkerMode = false;
        if (this.mSelectTextArea == null) {
            this.mSelectTextArea = new TextArea();
        }
        if (this.mRemoveTextArea == null) {
            this.mRemoveTextArea = new TextArea();
        }
        initializeSelectTextArea();
        this.mSelectTextMode = 0;
        this.flingDetector = new GestureDetector(context, this);
		this.flingDetector.setIsLongpressEnabled(false);
        this.mSwipeThreshold = (int) (100.0f * getResources().getDisplayMetrics().density);
        this.mTextLineSelect = false;
        this.mGripWidth = getResources().getDimensionPixelSize(R.dimen.mean_extendView_gripWidth);
        restoreSettings(this.mContext);
    }

    public void setMakerObject(ArrayList<MarkerObject> makerObj) {
        this.mMarkerObj = makerObj;
    }

    public List<MarkerObject> getMakerObject() {
        return this.mMarkerObj;
    }

    private void getLocationInTextView(int[] location, int x, int y) {
        int localY = y - super.getTotalPaddingTop();
        int localX = x - super.getTotalPaddingLeft();
        if (localX < 0) {
            localX = 0;
        } else if (localX >= super.getWidth() - super.getTotalPaddingRight()) {
            localX = super.getWidth() - super.getTotalPaddingRight();
        }
        if (localY < 0) {
            localY = 0;
        } else if (localY >= super.getHeight() - super.getTotalPaddingBottom()) {
            localY = super.getHeight() - super.getTotalPaddingBottom();
        }
        location[0] = localX;
        location[1] = localY + super.getScrollY();
    }

    private void getLocationInParentsView(int[] location, int x, int y) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        int top = loc[1];
        int left = loc[0];
        location[0] = x + left;
        location[1] = y + top;
    }

    private int getSelectTextSelectGrip(int x, int y) {
        if (this.mTextSelectLeftGrip == null || this.mTextSelectRightGrip == null || !this.mTextSelectLeftGrip.isShowing() || !this.mTextSelectRightGrip.isShowing()) {
            return -1;
        }
        Rect Rect = new Rect();
        int[] xy = new int[2];
        this.mTextSelectLeftGrip.getContentView().getLocationOnScreen(xy);
        int viewWidth = this.mTextSelectLeftGrip.getContentView().getWidth();
        int viewHeight = this.mTextSelectLeftGrip.getContentView().getHeight();
        int gripLeftVeticalMode = this.mLeftGripMode & (-268435456);
        int gripRightVeticalMode = this.mRightGripMode & (-268435456);
        if (gripLeftVeticalMode == 536870912) {
            Rect.left = xy[0];
            Rect.top = xy[1] + 20;
            Rect.right = Rect.left + viewWidth;
            Rect.bottom = (Rect.top + viewHeight) - 20;
        } else {
            Rect.left = xy[0];
            Rect.top = xy[1];
            Rect.right = Rect.left + viewWidth;
            Rect.bottom = (Rect.top + viewHeight) - 20;
        }
        if (Rect.contains(x, y)) {
            return 0;
        }
        this.mTextSelectRightGrip.getContentView().getLocationOnScreen(xy);
        int viewWidth2 = this.mTextSelectRightGrip.getContentView().getWidth();
        int viewHeight2 = this.mTextSelectRightGrip.getContentView().getHeight();
        if (gripRightVeticalMode == 536870912) {
            Rect.left = xy[0];
            Rect.top = xy[1] + 20;
            Rect.right = Rect.left + viewWidth2;
            Rect.bottom = (Rect.top + viewHeight2) - 20;
        } else {
            Rect.left = xy[0];
            Rect.top = xy[1];
            Rect.right = Rect.left + viewWidth2;
            Rect.bottom = (Rect.top + viewHeight2) - 20;
        }
        return Rect.contains(x, y) ? 1 : -1;
    }

    public boolean isActiveTextSelectGrip() {
        return this.mTextSelectLeftGrip != null && this.mTextSelectLeftGrip.isShowing() && this.mTextSelectRightGrip != null && this.mTextSelectRightGrip.isShowing();
    }

    private boolean isContainsOffset(int x, int y) {
        int[] location2 = new int[2];
        getLocationInTextView(location2, x, y);
        int fx = location2[0];
        int fy = location2[1];
        Layout layout = super.getLayout();
        int line = layout.getLineForVertical(fy);
        int off = layout.getOffsetForHorizontal(line, fx);
        int ty = layout.getLineBottom(line);
        if (y > ty) {
            off = -1;
        }
        return this.mSelectTextArea.isTextSelected() && this.mSelectTextArea.contains(off);
    }

    public void setEnableTextSelect(boolean isEnableTextSelect) {
        this.mIsEnableTextSelect = isEnableTextSelect;
    }

    private boolean onTouchEventForGrip(MotionEvent event) {
        int y;
        int x;
        int y2;
        int x2;
        int gripVeticalMode;
        int moveToY;
        int x3 = (int) event.getX();
        int y3 = (int) event.getY();
        if (this.mIsIntercept > 0) {
            y3 += this.mIsIntercept;
        }
        switch (event.getAction()) {
            case 0:
                if (!this.mIsEnableTextSelect) {
                    return true;
                }
                int[] location = new int[2];
                getLocationInTextView(location, x3, y3);
                this.mPopupBaseX = 0;
                this.mPopupBaseY = location[1];
                if (isActiveTextSelectGrip()) {
                    int[] locInParents = new int[2];
                    getLocationInParentsView(locInParents, x3, y3);
                    int fx = locInParents[0];
                    int fy = locInParents[1];
                    this.mActivityGrip = getSelectTextSelectGrip(fx, fy);
                    if (this.mActivityGrip != -1) {
                        this.mHandler.removeCallbacks(this.mRunnableDismissTextSelectGrip);
                        hideTextSelectActionMenu();
                        setFocuseTextSelectGrip(this.mActivityGrip, true);
                        return true;
                    } else if (isContainsOffset(x3, y3)) {
                        return true;
                    }
                }
                break;
            case 1:
                if (!this.mIsEnableTextSelect) {
                    startFlickLeft();
                    return true;
                } else if (isActiveTextSelectGrip() && this.mActivityGrip != -1) {
                    int oldSelectAreaStart = this.mSelectTextArea.start;
                    int oldSelectAreaEnd = this.mSelectTextArea.end;
                    Layout layout = super.getLayout();
                    int[] xy = new int[2];
                    int sy = 0;
                    if (this.mScrollView != null) {
                        sy = this.mScrollView.getScrollY();
                    }
                    int[] sxy = new int[2];
                    getLocationInTextView(sxy, 0, sy);
                    setFocuseTextSelectGrip(this.mActivityGrip, false);
                    if (this.mActivityGrip == 0) {
                        if ((this.mLeftGripMode & 536870912) == 536870912) {
                            y2 = (int) (y3 - (this.mGripHeight / 1.2f));
                        } else {
                            y2 = (int) (y3 + (this.mGripHeight / 1.2f));
                        }
                        if ((this.mLeftGripMode & 1) == 1) {
                            x2 = x3 - (this.mGripWidth / 2);
                        } else {
                            x2 = x3 + (this.mGripWidth / 2);
                        }
                        if (y2 < sxy[1]) {
                            y2 = sxy[1];
                        }
                        getLocationInTextView(xy, x2, y2);
                        int fline = getLineAdjust(layout.getLineForVertical(xy[1]), layout);
                        int foff = layout.getOffsetForHorizontal(fline, xy[0]);
                        int lastoff = layout.getLineEnd(fline);
                        float lineRight = layout.getLineRight(fline);
                        if (xy[0] >= lineRight) {
                            foff = lastoff;
                        }
                        if (this.mSelectTextArea.end < foff) {
                            this.mSelectTextArea.start = this.mSelectTextArea.end;
                            this.mSelectTextArea.end = foff;
                        } else {
                            this.mSelectTextArea.start = foff;
                        }
                    } else {
                        if ((this.mRightGripMode & 536870912) == 536870912) {
                            y = (int) (y3 - (this.mGripHeight / 1.2f));
                        } else {
                            y = (int) (y3 + (this.mGripHeight / 1.2f));
                        }
                        if ((this.mRightGripMode & 1) == 1) {
                            x = x3 - (this.mGripWidth / 2);
                        } else {
                            x = x3 + (this.mGripWidth / 2);
                        }
                        if (y < sxy[1]) {
                            y = sxy[1];
                        }
                        getLocationInTextView(xy, x, y);
                        int line = getLineAdjust(layout.getLineForVertical(xy[1]), layout);
                        int off = layout.getOffsetForHorizontal(line, xy[0]);
                        int lastoff2 = layout.getLineEnd(line);
                        float lineRight2 = layout.getLineRight(line);
                        if (xy[0] >= lineRight2) {
                            off = lastoff2;
                        }
                        if (this.mSelectTextArea.start > off) {
                            this.mSelectTextArea.end = this.mSelectTextArea.start;
                            this.mSelectTextArea.start = off;
                        } else {
                            this.mSelectTextArea.end = off;
                        }
                    }
                    if (this.mSelectTextArea.start == this.mSelectTextArea.end) {
                        this.mSelectTextArea.start = oldSelectAreaStart;
                        this.mSelectTextArea.end = oldSelectAreaEnd;
                    }
                    if (this.mSelectTextArea.isTextSelected()) {
                        getTextSelectGripPosition(this.mSelectTextArea.start, this.mSelectTextArea.end, this.mLeftGripPosition, this.mRightGripPosition);
                        showTextSelectGrip(this.mLeftGripPosition, this.mRightGripPosition);
                        showTextSelectPopupMenu();
                        invalidate();
                    }
                    return true;
                } else if (isActiveTextSelectGrip() && isContainsOffset(x3, y3) && this.mTextSelectPopupMenu != null && !this.mTextSelectPopupMenu.isShowing()) {
                    showTextSelectPopupMenu();
                    return true;
                }
                break;
            case 2:
                if (!this.mIsEnableTextSelect) {
                    return true;
                }
                if (isActiveTextSelectGrip() && this.mActivityGrip != -1) {
                    getLocationInParentsView(new int[2], x3, y3);
                    if (this.mActivityGrip == 0) {
                        gripVeticalMode = this.mLeftGripMode & (-268435456);
                    } else {
                        gripVeticalMode = this.mRightGripMode & (-268435456);
                    }
                    int moveToX = x3 - (this.mGripWidth / 2);
                    if (gripVeticalMode == 536870912) {
                        moveToY = (int) (y3 - (this.mGripHeight / 1.2f));
                    } else {
                        moveToY = y3 - (this.mGripHeight / 6);
                    }
                    moveToTextSelectGrip(this.mActivityGrip, moveToX, moveToY);
                    return true;
                } else if (isContainsOffset(x3, y3)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private int getLineAdjust(int fline, Layout layout) {
        int nLine = fline;
        if (this.mScrollView == null) {
            return nLine;
        }
        while (layout.getLineBottom(nLine) - this.mScrollView.getScrollY() > this.mScrollView.getHeight() && nLine > 0) {
            nLine--;
        }
        return nLine;
    }
	
	@Override // android.view.GestureDetector.OnGestureListener
	public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
		if (!this.mMarkerable && !isActiveTextSelectGrip()
				|| !this.mIsMarkerMode && !this.mTextLineSelect && !isActiveTextSelectGrip()) {
			float absX = Math.abs(velocityX);
			float absY = Math.abs(velocityY);
			float deltaX = me2.getX() - me1.getX();
			int travelX = getWidth() / 4;
			if (velocityX > this.mSwipeThreshold && absY < absX && deltaX > travelX) {
				startFlickRight();
				return true;
			} else if (velocityX < (-this.mSwipeThreshold) && absY < absX && deltaX < (-travelX)) {
				startFlickLeft();
				return true;
			}
		}
		return false;
	}
	
    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		if (action!=MotionEvent.ACTION_POINTER_DOWN && action!=MotionEvent.ACTION_POINTER_UP) {
			try {
				boolean flingEvent = this.flingDetector.onTouchEvent(event);
				// CMN.Log("flingEvent::", flingEvent, event.getActionMasked());
				if (flingEvent) {
					//this.flingDetector = new GestureDetector(getContext(), this);
					return flingEvent;
				}
			} catch (Exception e) {
				CMN.Log(e);
			}
		}
		if (this.mMarkerable || isActiveTextSelectGrip()) {
			if (this.isRemoveMode && this.mIsMarkerMode) {
				handleRemoveMarker(event);
				return true;
			} else if (this.mIsMarkerMode) {
				return onTouchEventInLocalForLineSelect(event);
			} else {
				if (onTouchEventForGrip(event)) {
					return true;
				}
				if (this.mTextLineSelect) {
					return onTouchEventInLocalForLineSelect(event);
				}
				return onTouchEventInLocalForWordSelect(event);
			}
		}
		return super.onTouchEvent(event);
    }

    private void handleRemoveMarker(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int[] location = new int[2];
        getLocationInTextView(location, x, y);
        int fx = location[0];
        int fy = location[1];
        Layout layout = super.getLayout();
        int line = layout.getLineForVertical(fy);
        int off = layout.getOffsetForHorizontal(line, fx);
        switch (event.getAction()) {
            case 0:
                this.mRemoveTextArea.start = off;
                this.mRemoveTextArea.start_line = line;
                return;
            case 1:
                if (this.mRemoveTextArea.start_line == line) {
                    this.mRemoveTextArea.end = off;
                } else {
                    this.mRemoveTextArea.end = layout.getLineEnd(this.mRemoveTextArea.start_line) - 1;
                }
                if (this.mRemoveTextArea.start > off) {
                    this.mRemoveTextArea.end = this.mRemoveTextArea.start;
                    this.mRemoveTextArea.start = off;
                }
                ArrayList<TagConverter.DictPos> mTotalOffset = this.mTagConverterCallback.convert_Total_Index(this.mDisplayMode, this.mRemoveTextArea.start, this.mRemoveTextArea.end);
                for (int k = 0; k < mTotalOffset.size(); k++) {
                    TagConverter.DictPos pos = mTotalOffset.get(k);
                    removeMaker(pos.start, pos.end);
                }
                invalidate();
                return;
            case 2:
            default:
                return;
        }
    }

    private void removeMaker(int StartOffset, int endOffset) {
        if (this.mMarkerObj != null) {
            for (int i = this.mMarkerObj.size() - 1; i >= 0; i--) {
                MarkerObject obj = this.mMarkerObj.get(i);
                if (obj.contain(StartOffset) && obj.contain(endOffset)) {
                    this.mMarkerObj.remove(i);
                    return;
                }
            }
        }
    }

    public void removeAllMarker() {
        if (this.mMarkerObj != null) {
            this.mMarkerObj.clear();
            this.mMarkerObj = null;
        }
    }

    private boolean onTouchEventInLocalForLineSelect(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int[] location = new int[2];
        getLocationInTextView(location, x, y);
        int fx = location[0];
        int fy = location[1];
        Layout layout = super.getLayout();
        int line = layout.getLineForVertical(fy);
        int off = layout.getOffsetForHorizontal(line, fx);
        this.mMoving = false;
        if (fx >= (super.getWidth() - super.getTotalPaddingRight()) - (super.getTextScaleX() * super.getTextSize())) {
            off = layout.getLineEnd(line);
        }
        int ty = layout.getLineBottom(line);
        if (y > ty) {
            off = -1;
        }
        switch (event.getAction()) {
            case 0:
                this.mSelectTextArea.start = off;
                this.mSelectTextArea.start_line = line;
                return true;
            case 1:
                if (this.mSelectTextMode == 0) {
                    if (this.mSelectTextArea.start_line == line) {
                        this.mSelectTextArea.end = off;
                    } else if (this.mSelectTextArea.start_line >= 0 && this.mSelectTextArea.start_line < layout.getLineCount()) {
                        this.mSelectTextArea.end = layout.getLineEnd(this.mSelectTextArea.start_line);
                    }
                } else {
                    this.mSelectTextArea.end = off;
                }
                if (this.mSelectTextArea.start > this.mSelectTextArea.end) {
                    int tempOffset = this.mSelectTextArea.end;
                    this.mSelectTextArea.end = this.mSelectTextArea.start;
                    this.mSelectTextArea.start = tempOffset;
                }
                if (this.mSelectTextArea.isTextSelected()) {
                    if (this.mTextLineSelect) {
                        if (this.mIsMarkerMode) {
                            actionMarker();
                        } else {
                            getTextSelectGripPosition(this.mSelectTextArea.start, this.mSelectTextArea.end, this.mLeftGripPosition, this.mRightGripPosition);
                            showTextSelectGrip(this.mLeftGripPosition, this.mRightGripPosition);
                        }
                    } else {
                        actionMarker();
                    }
                    invalidate();
                    return true;
                }
                return true;
            case 2:
                this.mMoving = true;
                if (this.mSelectTextMode == 0) {
                    if (this.mSelectTextArea.start_line == line) {
                        this.mSelectTextArea.move = off;
                    } else if (this.mSelectTextArea.start_line >= 0 && this.mSelectTextArea.start_line < layout.getLineCount()) {
                        this.mSelectTextArea.move = layout.getLineEnd(this.mSelectTextArea.start_line);
                    }
                } else {
                    this.mSelectTextArea.move = off;
                }
                List<Rect> selectRect = getRectOfSelectedTextOffset(this.mSelectTextArea.start, this.mSelectTextArea.move);
                if (this.mPrevMakerRect != null) {
                    for (int j = 0; j < this.mPrevMakerRect.size(); j++) {
                        Rect r = this.mPrevMakerRect.get(j);
                        invalidate(r);
                    }
                }
                for (int j2 = 0; j2 < selectRect.size(); j2++) {
                    Rect r2 = selectRect.get(j2);
                    invalidate(r2);
                }
                this.mPrevMakerRect = selectRect;
                return true;
            case 3:
                if (this.mSelectTextArea.start != -1 && this.mSelectTextArea.move != -1 && this.mSelectTextArea.start != this.mSelectTextArea.move) {
                    this.mSelectTextArea.end = this.mSelectTextArea.move;
                    actionMarker();
                    invalidate();
                    return true;
                }
                return true;
            default:
                return true;
        }
    }

    private boolean onTouchEventInLocalForWordSelect(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int[] location = new int[2];
        getLocationInTextView(location, x, y);
        int fx = location[0];
        int fy = location[1];
        Layout layout = super.getLayout();
        if (layout == null) {
            return false;
        }
        int line = getLineAdjust(layout.getLineForVertical(fy), layout);
        int off = layout.getOffsetForHorizontal(line, fx);
        float lineWidth = layout.getLineWidth(line);
        this.mMoving = false;
        if (fx <= lineWidth) {
            off--;
        }
        int ty = layout.getLineBottom(line);
        if (y > ty) {
            off = -1;
        }
        TagConverter.DictPos pos = this.mTagConverterCallback.isLinkOffset(off);
        if (pos != null) {
            if (event.getAction() == 1) {
                saveMarkerObject();
                initTextSelect();
                this.mSelectTextArea.start = pos.start;
                this.mSelectTextArea.end = pos.end;
                invalidate();
            }
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case 1:
                if (off != -1 && startClick(off)) {
                    getTextSelectGripPosition(this.mSelectTextArea.start, this.mSelectTextArea.end, this.mLeftGripPosition, this.mRightGripPosition);
                    showTextSelectGrip(this.mLeftGripPosition, this.mRightGripPosition);
                    showTextSelectPopupMenu();
                    invalidate();
                } else if (off == -1) {
                    this.mSelectTextArea.init();
                }
                if (!this.mSelectTextArea.isTextSelected()) {
                    initTextSelect();
                    invalidate();
                    break;
                }
                break;
        }
        return true;
    }

    private boolean startClick(int off) {
        if (this.mSelectTextArea.isTextSelected()) {
            if (this.mSelectTextArea.contains(off)) {
                initTextSelect();
                return false;
            }
            setSelectText(off);
            return this.mSelectTextArea.isTextSelected();
        }
        setSelectText(off);
        return this.mSelectTextArea.isTextSelected();
    }

    private void setSelectText(int off) {
        if (this.mSelectTextArea == null) {
            this.mSelectTextArea = new TextArea();
        }
        CharSequence text = super.getText();
        if (off >= text.length()) {
            this.mSelectTextArea.init();
        } else if (isWordSeperator(super.getText().charAt(off))) {
            this.mSelectTextArea.init();
        } else {
            int codeBlock = CodeBlock.getCodeBlock(text.charAt(off));
            for (int i = off; i >= 0; i--) {
                boolean isCodeBlockDiff = CodeBlock.getCodeBlock(text.charAt(i)) != codeBlock;
                if (isWordSeperator(text.charAt(i)) || isCodeBlockDiff) {
                    this.mSelectTextArea.start = i + 1;
                    break;
                }
                if (i == 0) {
                    this.mSelectTextArea.start = i;
                }
            }
            for (int k = off; k < text.length(); k++) {
                boolean isCodeBlockDiff2 = CodeBlock.getCodeBlock(text.charAt(k)) != codeBlock;
                if (isWordSeperator(text.charAt(k)) || isCodeBlockDiff2) {
                    this.mSelectTextArea.end = k;
                    return;
                }
                if (k == text.length() - 1) {
                    this.mSelectTextArea.end = text.length();
                }
            }
        }
    }

    private boolean isWordSeperator(char charAt) {
        return !CodeBlock.isAlpabetCodeBlock(charAt) && !CodeBlock.isHangulCodeBlock(charAt) && !CodeBlock.isChineseCodeBlock(charAt) && !CodeBlock.isLatin(charAt) && !CodeBlock.isJapan(charAt) && !DictUtils.isDioSymbolAlphabet(charAt);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideTextSelectActionMenu() {
        if (this.mTextSelectPopupMenu != null) {
            this.mTextSelectPopupMenu.dismiss();
        }
    }

    private List<Rect> getRectOfSelectedTextOffset(int startOffset, int endOffset) {
        Layout layout = super.getLayout();
        List<Rect> r = new ArrayList<>();
        int paddingTop = super.getPaddingTop() + 2;
        if (startOffset > endOffset) {
            startOffset = endOffset;
            endOffset = startOffset;
        }
        if (startOffset < 0) {
            startOffset = 0;
        }
        if (endOffset > super.getText().length()) {
            endOffset = super.getText().length();
        }
        if (endOffset < 0) {
            endOffset = 0;
        }
        int startLine = layout.getLineForOffset(startOffset);
        int endLine = layout.getLineForOffset(endOffset);
        if (startLine != endLine) {
            Rect startRect = new Rect();
            Rect endRect = new Rect();
            startRect.top = layout.getLineTop(startLine) + paddingTop + 1;
            startRect.bottom = layout.getLineBottom(startLine) + paddingTop;
            startRect.left = (int) layout.getPrimaryHorizontal(startOffset);
            startRect.right = (int) layout.getLineRight(startLine);
            r.add(startRect);
            endRect.top = layout.getLineTop(endLine) + paddingTop + 1;
            endRect.bottom = layout.getLineBottom(endLine) + paddingTop;
            endRect.left = (int) layout.getPrimaryHorizontal(layout.getLineStart(endLine));
            endRect.right = (int) layout.getPrimaryHorizontal(endOffset);
            for (int i = startLine + 1; i < endLine; i++) {
                Rect tempRect = new Rect();
                tempRect.top = layout.getLineTop(i) + paddingTop + 1;
                tempRect.bottom = layout.getLineBottom(i) + paddingTop;
                int sOff = layout.getLineStart(i);
                tempRect.left = (int) layout.getPrimaryHorizontal(sOff);
                tempRect.right = (int) layout.getLineRight(i);
                r.add(tempRect);
            }
            r.add(endRect);
        } else {
            Rect startRect2 = new Rect();
            startRect2.top = layout.getLineTop(startLine) + paddingTop + 1;
            startRect2.bottom = layout.getLineBottom(startLine) + paddingTop;
            startRect2.left = (int) layout.getPrimaryHorizontal(startOffset);
            startRect2.right = (int) layout.getPrimaryHorizontal(endOffset);
            r.add(startRect2);
        }
        return r;
    }

    private void drawSavedMarker(Canvas canvas) {
        if (this.mMarkerObj != null && this.mMarkerObj.size() > 0) {
            Rect inRect = getInvalidateRect();
            int textLength = super.getText().length();
            for (int i = 0; i < this.mMarkerObj.size(); i++) {
                MarkerObject obj = this.mMarkerObj.get(i);
                ArrayList<TagConverter.DictPos> mPartialOffset = this.mTagConverterCallback.convert_Partial_Index(this.mDisplayMode, obj.mStartOffset, obj.mEndOffset);
                for (int k = 0; k < mPartialOffset.size(); k++) {
                    TagConverter.DictPos pos = mPartialOffset.get(k);
                    if (pos.start <= textLength) {
                        List<Rect> tR = getRectOfSelectedTextOffset(pos.start, pos.end);
                        for (int j = 0; j < tR.size(); j++) {
                            Rect r = tR.get(j);
                            if (inRect.contains(r)) {
                                onDrawTextArea(canvas, r, obj.getColor());
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawSelectingTextArea(Canvas canvas) {
        List<Rect> tR;
        int color = this.mTextSelectColor;
        if (this.mMoving && !isActiveTextSelectGrip() && this.mSelectTextArea.isTextSelecting() && (tR = getRectOfSelectedTextOffset(this.mSelectTextArea.start, this.mSelectTextArea.move)) != null && tR.size() > 0) {
            for (int j = 0; j < tR.size(); j++) {
                Rect r = tR.get(j);
                if (this.mIsMarkerMode) {
                    color = this.mMarkerColor;
                }
                onDrawTextArea(canvas, r, color);
            }
        }
    }

    private void drawSelectedTextArea(Canvas canvas) {
        List<Rect> tR;
        int color = this.mTextSelectColor;
        if (!this.mMoving && this.mSelectTextArea.isTextSelected() && (tR = getRectOfSelectedTextOffset(this.mSelectTextArea.start, this.mSelectTextArea.end)) != null && tR.size() > 0) {
            for (int j = 0; j < tR.size(); j++) {
                Rect r = tR.get(j);
                if (this.mIsMarkerMode) {
                    color = this.mMarkerColor;
                }
                onDrawTextArea(canvas, r, color);
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mIsEnableInvalidate) {
            drawSavedMarker(canvas);
            drawSelectingTextArea(canvas);
            drawSelectedTextArea(canvas);
        }
        super.onDraw(canvas);
    }

    private void onDrawTextArea(Canvas canvas, Rect r, int color) {
        Paint paint = super.getPaint();
        paint.setColor(color);
        if (canvas != null) {
            canvas.drawRect(r, paint);
        }
    }

    public void setTextSelectColor(int color) {
        this.mTextSelectColor = color;
    }

    public void setMarkerColor(int color) {
        this.mMarkerColor = color;
        if (color == Integer.MAX_VALUE) {
            this.isRemoveMode = true;
        } else {
            this.isRemoveMode = false;
        }
    }

    public void makeTextSelectPopupMenu() {
        if (this.mContext != null) {
            if (this.mTextSelectPopupMenu == null) {
                this.mTextSelectPopupMenu = new PopupWindow(this.mContext);
                this.mTextSelectPopupMenu.setTouchable(true);
                this.mTextSelectPopupMenu.setClippingEnabled(false);
                this.mTextSelectPopupMenu.setBackgroundDrawable(null);
                this.mTextSelectPopupMenu.setOutsideTouchable(true);
                this.mTextSelectPopupMenu.setBackgroundDrawable(new ColorDrawable(0));
            }
            LayoutInflater inflate = (LayoutInflater) getContext().getSystemService("layout_inflater");
            this.mTextSelectPopupMenu.setFocusable(true);
            this.mPopupContent = (LinearLayout) inflate.inflate(R.layout.mean_menu_control_popup, (ViewGroup) null);
            this.mTextSelectPopupMenu.setContentView(this.mPopupContent);
            ((Button) this.mPopupContent.findViewById(R.id.popup_copy)).setOnClickListener(this.mTextSelectPopupOnClickListener);
            ((Button) this.mPopupContent.findViewById(R.id.popup_hyper)).setOnClickListener(this.mTextSelectPopupOnClickListener);
            this.google = (Button) this.mPopupContent.findViewById(R.id.popup_google);
            this.google.setOnClickListener(this.mTextSelectPopupOnClickListener);
            this.wiki = (Button) this.mPopupContent.findViewById(R.id.popup_wiki);
            ImageButton tts = (ImageButton) this.mPopupContent.findViewById(R.id.popup_tts);
            ImageButton tts_us = (ImageButton) this.mPopupContent.findViewById(R.id.popup_tts_us);
            ImageButton tts_uk = (ImageButton) this.mPopupContent.findViewById(R.id.popup_tts_uk);
            this.wiki.setOnClickListener(this.mTextSelectPopupOnClickListener);
            tts.setOnClickListener(this.mTextSelectPopupOnClickListener);
            tts_us.setOnClickListener(this.mTextSelectPopupOnClickListener);
            tts_uk.setOnClickListener(this.mTextSelectPopupOnClickListener);
            if (Dependency.getLocaleName().contains("China")) {
                this.wiki.setVisibility(8);
                this.bWikiVisible = false;
            }
            float density = CommonUtils.getDeviceDensity(this.mContext);
            String ttsWord = "";
            if (this.mSelectTextArea.isTextSelected()) {
                ttsWord = super.getText().subSequence(this.mSelectTextArea.start, this.mSelectTextArea.end).toString();
            }
            if (ttsWord.length() == 0) {
                tts.setVisibility(8);
                tts_us.setVisibility(8);
                tts_uk.setVisibility(8);
                if (this.bWikiVisible) {
                    this.wiki.setBackgroundResource(R.drawable.selectpop_right);
                    this.wiki.setPadding((int) (15.0f * density), 0, (int) (23.0f * density), 0);
                } else {
                    this.google.setBackgroundResource(R.drawable.selectpop_right);
                    this.google.setPadding((int) (15.0f * density), 0, (int) (23.0f * density), 0);
                }
            } else {
                boolean isEnglish = false;
                boolean isChinese = false;
                int nLang = CommonUtils.getTTSLanguage(ttsWord, this.mCurDbType);
                if (nLang == 65536) {
                    isEnglish = true;
                } else if (nLang == 65537) {
                    isChinese = true;
                }
                boolean bExist = CommonUtils.getSupportTTSLanguage(nLang);
                if (!Dependency.isContainTTS()) {
                    bExist = false;
                }
                if (bExist) {
                    if (isEnglish || isChinese) {
                        tts.setVisibility(8);
                        if (isEnglish) {
                            tts_us.setImageResource(R.drawable.selectpop_us);
                            tts_uk.setImageResource(R.drawable.selectpop_uk);
                        } else if (isChinese) {
                            tts_us.setImageResource(R.drawable.selectpop_zh_cn);
                            tts_uk.setImageResource(R.drawable.selectpop_yue_cn);
                        }
                        if (!DictUtils.checkExistSecoundTTSFile(nLang)) {
                            tts_us.setImageResource(R.drawable.selectpop_tts);
                            tts_us.setBackgroundResource(R.drawable.selectpop_right);
                            tts_us.setPadding((int) (15.0f * density), 0, (int) (23.0f * density), 0);
                            tts_uk.setVisibility(8);
                        }
                    } else {
                        tts_us.setVisibility(8);
                        tts_uk.setVisibility(8);
                    }
                    if (this.bWikiVisible) {
                        this.wiki.setBackgroundResource(R.drawable.selectpop_btn);
                        this.wiki.setPadding((int) (15.0f * density), 0, (int) (15.0f * density), 0);
                    } else {
                        this.google.setBackgroundResource(R.drawable.selectpop_btn);
                        this.google.setPadding((int) (15.0f * density), 0, (int) (15.0f * density), 0);
                    }
                } else {
                    tts.setVisibility(8);
                    tts_us.setVisibility(8);
                    tts_uk.setVisibility(8);
                    if (this.bWikiVisible) {
                        this.wiki.setBackgroundResource(R.drawable.selectpop_right);
                        this.wiki.setPadding((int) (15.0f * density), 0, (int) (23.0f * density), 0);
                    } else {
                        this.google.setBackgroundResource(R.drawable.selectpop_right);
                        this.google.setPadding((int) (15.0f * density), 0, (int) (23.0f * density), 0);
                    }
                }
            }
            this.mPopupContent.measure(0, 0);
            ((Button) this.mPopupContent.findViewById(R.id.popup_copy)).requestFocus();
        }
    }

    public void showTextSelectPopupMenu() {
        if (this.mScrollView == null) {
            setScrollView();
        }
        makeTextSelectPopupMenu();
        if (this.mScrollView != null && this.mTextSelectPopupMenu != null) {
            if (this.mPopupMenuOffsetInWindow == null) {
                this.mPopupMenuOffsetInWindow = new int[2];
            }
            getLocationInWindow(this.mPopupMenuOffsetInWindow);
            int popupWidth = 439;
            int popupHeight = 60;
            if (this.mPopupContent != null) {
                popupWidth = this.mPopupContent.getMeasuredWidth();
                popupHeight = this.mPopupContent.getMeasuredHeight();
            }
            if (super.getWidth() > popupWidth) {
                this.mPopupBaseX = (super.getWidth() - popupWidth) / 2;
            } else {
                this.mPopupBaseX = super.getWidth() - popupWidth;
            }
            if (this.mTextSelectRightGrip != null && this.mTextSelectRightGrip.isShowing()) {
                if ((this.mLeftGripPosition[1] - popupHeight) - this.mScrollView.getScrollY() > 0) {
                    this.mPopupBaseY = (this.mLeftGripPosition[1] - popupHeight) - 10;
                } else if ((this.mRightGripPosition[1] - this.mScrollView.getScrollY()) + popupHeight + this.mGripHeight < this.mScrollView.getHeight()) {
                    this.mPopupBaseY = this.mRightGripPosition[1] + this.mGripHeight + 10;
                } else {
                    this.mPopupBaseY = (this.mLeftGripPosition[1] - popupHeight) - 10;
                }
            }
            if (this.mTextSelectPopupMenu != null) {
                if (this.mTextSelectPopupMenu.isShowing()) {
                    this.mTextSelectPopupMenu.update(this.mPopupBaseX + this.mPopupMenuOffsetInWindow[0], this.mPopupBaseY + this.mPopupMenuOffsetInWindow[1], popupWidth, popupHeight);
                    return;
                }
                this.mTextSelectPopupMenu.setWidth(popupWidth);
                this.mTextSelectPopupMenu.setHeight(popupHeight);
                this.mTextSelectPopupMenu.showAtLocation(this, 0, this.mPopupBaseX + this.mPopupMenuOffsetInWindow[0], this.mPopupBaseY + this.mPopupMenuOffsetInWindow[1]);
            }
        }
    }

    private void showToastMessage(String msg) {
        if (this.mContext != null) {
            Toast.makeText(this.mContext, msg, 1).show();
        }
    }

    public boolean isIntercepteScrollTouchEvent() {
        return this.mSelectTextArea.isTextSelected() && isActiveTextSelectGrip();
    }

    public boolean isSelectedText() {
        if (this.mSelectTextArea != null) {
            return this.mSelectTextArea.isTextSelected();
        }
        return false;
    }

    public void initializeSelectTextArea() {
        if (this.mSelectTextArea != null) {
            this.mSelectTextArea.init();
        }
        if (this.mRemoveTextArea != null) {
            this.mRemoveTextArea.init();
        }
        this.mPrevMakerRect = null;
    }

    public void actionMarker() {
        if (this.mMarkerObj == null) {
            this.mMarkerObj = new ArrayList();
        }
        if (this.mMarkerObj != null && this.mMarkerObj.size() < 100) {
            if (this.mSelectTextArea.isTextSelected()) {
                ArrayList<TagConverter.DictPos> mTotalOffset = this.mTagConverterCallback.convert_Total_Index(this.mDisplayMode, this.mSelectTextArea.start, this.mSelectTextArea.end);
                for (int k = 0; k < mTotalOffset.size(); k++) {
                    TagConverter.DictPos pos = mTotalOffset.get(k);
                    this.mMarkerObj.add(new MarkerObject(pos.start, pos.end, this.mMarkerColor));
                }
                initializeSelectTextArea();
                invalidate();
                return;
            }
            return;
        }
        showToastMessage(this.mContext.getResources().getString(R.string.marker_maximum));
    }

    public void actionHyperText() {
        if (this.mSelectTextArea.isTextSelected()) {
            this.mSelectedText = super.getText().subSequence(this.mSelectTextArea.start, this.mSelectTextArea.end);
            if (this.mHyperTextCallback != null) {
                boolean ret = this.mHyperTextCallback.run(this.mSelectedText.toString());
                if (!ret) {
                    initializeSelectTextArea();
                    Toast.makeText(this.mContext, "hyperlink is not supported word !!", 0);
                }
            }
            dismissTextSelectGrip();
            invalidate();
        }
    }

    public void actionGoogleSearch() {
        if (this.mSelectTextArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            if (this.mGoogleSearchCallback != null) {
                this.mGoogleSearchCallback.run(this.mSelectedText.toString());
            }
            initializeSelectTextArea();
            dismissTextSelectGrip();
            invalidate();
        }
    }

    public void actionWikiSearch() {
        if (this.mSelectTextArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            if (this.mWikiSearchCallback != null) {
                this.mWikiSearchCallback.run(this.mSelectedText.toString());
            }
            initializeSelectTextArea();
            dismissTextSelectGrip();
            invalidate();
        }
    }

    public void actionTTS(int mode) {
        if (this.mSelectTextArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            if (this.mTTSCallback != null) {
                this.mTTSCallback.run(this.mSelectedText.toString());
            } else if (mode == 2) {
                CommonUtils.playTTS(1, this.mCurKeyword, this.mSelectedText.toString(), this.mCurDbType, 1);
            } else if (mode == 1) {
                CommonUtils.playTTS(0, this.mCurKeyword, this.mSelectedText.toString(), this.mCurDbType, 1);
            } else {
                CommonUtils.playTTS(0, this.mCurKeyword, this.mSelectedText.toString(), this.mCurDbType, 1);
            }
            initializeSelectTextArea();
            dismissTextSelectGrip();
            invalidate();
        }
    }

    public void actionClipboardCopy() {
        if (this.mSelectTextArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            ClipboardManager clipboard = (ClipboardManager) this.mContext.getSystemService("clipboard");
            if (clipboard != null) {
                clipboard.setText(this.mSelectedText.toString());
            }
            initializeSelectTextArea();
            dismissTextSelectGrip();
            invalidate();
        }
    }

    public String getSelectedString() {
        if (this.mSelectTextArea != null && this.mSelectTextArea.isTextSelected()) {
            this.mSelectedText = super.getText().subSequence(this.mSelectTextArea.start, this.mSelectTextArea.end);
        }
        return this.mSelectedText != null ? DictUtils.convertDioSymbolAlphabetString(this.mSelectedText.toString()) : "";
    }

    public void dismissTextSelectController() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mRunnableDismissTextSelectGrip);
        }
        if (this.mTextSelectPopupMenu != null && this.mTextSelectPopupMenu.isShowing()) {
            this.mTextSelectPopupMenu.dismiss();
        }
    }

    public void initTextSelect() {
        dismissTextSelectController();
        dismissTextSelectGrip();
        initializeSelectTextArea();
    }

    public void forceInvalidate() {
        invalidate();
    }

    @Override // android.widget.TextView, android.view.View
    public boolean performLongClick() {
        return super.performLongClick();
    }

    public void onDestroy() {
        this.mHandler.removeCallbacks(this.mRunnableDismissTextSelectGrip);
        if (this.mTextSelectPopupMenu != null) {
            this.mTextSelectPopupMenu.dismiss();
            this.mTextSelectPopupMenu = null;
        }
        if (this.mTextSelectLeftGrip != null) {
            this.mTextSelectLeftGrip.dismiss();
            this.mTextSelectLeftGrip = null;
        }
        if (this.mTextSelectRightGrip != null) {
            this.mTextSelectRightGrip.dismiss();
            this.mTextSelectRightGrip = null;
        }
        initializeSelectTextArea();
        this.mLeftGripPosition = null;
        this.mRightGripPosition = null;
        this.mHyperTextCallback = null;
        this.mGoogleSearchCallback = null;
        this.mWikiSearchCallback = null;
        this.mConfigChangeCallback = null;
        this.mUpdateLayoutCallback = null;
        this.mFinishDrawMeanCallback = null;
    }

    private int getGripHorizonMode(boolean isLeftGrip, int startX) {
        int prevHorizonMode = isLeftGrip ? this.mLeftGripMode & 15 : this.mRightGripMode & 15;
        int defaultHorizonMode = isLeftGrip ? 2 : 1;
        int returnHorizonMode = defaultHorizonMode;
        if (prevHorizonMode == 1) {
            if (this.mGripWidth + startX > super.getWidth()) {
                returnHorizonMode = 2;
            }
        } else if (prevHorizonMode == 2) {
            if (startX - this.mGripWidth < 0) {
                returnHorizonMode = 1;
            }
        } else if (startX - this.mGripWidth < 0) {
            returnHorizonMode = 1;
        } else if (this.mGripWidth + startX > super.getWidth()) {
            returnHorizonMode = 2;
        }
        if (returnHorizonMode == 1 && this.mGripWidth + startX > super.getWidth()) {
            returnHorizonMode = 2;
        }
        if (returnHorizonMode == 2 && startX - this.mGripWidth < 0) {
            return 1;
        }
        return returnHorizonMode;
    }

    public void getTextSelectGripPosition(int startOffset, int endOffset, int[] leftXY, int[] rightXY) {
        int prevLeftMode = this.mLeftGripMode;
        int prevRightMode = this.mRightGripMode;
        Layout layout = super.getLayout();
        int startLine = layout.getLineForOffset(startOffset);
        int endLine = layout.getLineForOffset(endOffset);
        int startLineTop = layout.getLineTop(startLine);
        int endLineTop = layout.getLineTop(endLine);
        int startLineBottom = layout.getLineBottom(startLine);
        int endLineBottom = layout.getLineBottom(endLine);
        leftXY[0] = (int) layout.getPrimaryHorizontal(startOffset);
        rightXY[0] = (int) layout.getPrimaryHorizontal(endOffset);
        this.mLeftGripMode = getGripHorizonMode(true, leftXY[0]);
        if ((this.mLeftGripMode & 15) == 2) {
            leftXY[0] = leftXY[0] - (this.mGripWidth - 2);
        } else {
            leftXY[0] = leftXY[0] - 2;
        }
        this.mRightGripMode = getGripHorizonMode(false, rightXY[0]);
        if ((this.mRightGripMode & 15) == 2) {
            rightXY[0] = rightXY[0] - (this.mGripWidth - 2);
        } else {
            rightXY[0] = rightXY[0] - 2;
        }
        int[] windowOffset = new int[2];
        getLocationInWindow(windowOffset);
        int checkHeight = 0;
        int scollY = 0;
        if (this.mScrollView == null) {
            setScrollView();
        }
        if (this.mMeanLinearLayout == null) {
            this.mMeanLinearLayout = (ViewGroup) this.mScrollView.getParent();
        }
        if (this.mScrollView != null) {
            checkHeight = this.mMeanLinearLayout.getHeight();
            scollY = this.mScrollView.getScrollY();
        }
        int checkStartTop = (startLineTop - this.mGripHeight) - scollY;
        int checkStartBottom = ((startLineBottom - scollY) + this.mGripHeight) - 20;
        if (checkStartTop < 0) {
            this.mLeftGripMode |= 536870912;
        } else if (checkStartBottom > checkHeight) {
            this.mLeftGripMode |= 268435456;
        } else {
            this.mLeftGripMode |= 536870912;
        }
        if ((this.mLeftGripMode & 536870912) == 536870912) {
            leftXY[1] = startLineBottom - 20;
        } else {
            leftXY[1] = (startLineTop - this.mGripHeight) + 20;
        }
        int checkEndTop = (endLineTop - this.mGripHeight) - scollY;
        int checkEndBottom = ((endLineBottom - scollY) + this.mGripHeight) - 20;
        if (checkEndTop < 0) {
            this.mRightGripMode |= 536870912;
        } else if (checkEndBottom > checkHeight) {
            this.mRightGripMode |= 268435456;
        } else {
            this.mRightGripMode |= 536870912;
        }
        if ((this.mRightGripMode & 536870912) == 536870912) {
            rightXY[1] = endLineBottom - 20;
        } else {
            rightXY[1] = (endLineTop - this.mGripHeight) + 20;
        }
        if (prevLeftMode != this.mLeftGripMode && this.mTextSelectLeftGrip != null) {
            this.mTextSelectLeftGrip.dismiss();
            this.mTextSelectLeftGrip = null;
        }
        if (prevRightMode != this.mRightGripMode && this.mTextSelectRightGrip != null) {
            this.mTextSelectRightGrip.dismiss();
            this.mTextSelectRightGrip = null;
        }
    }

    private PopupWindow makeTextSelectGrip(int gripKinds) {
        int contentViewId;
        if (gripKinds == 0) {
            if ((this.mLeftGripMode & 536870912) == 536870912) {
                if ((this.mLeftGripMode & 1) == 1) {
                    contentViewId = R.layout.leftgrip_down;
                } else {
                    contentViewId = R.layout.rightgrip_down;
                }
            } else if ((this.mLeftGripMode & 1) == 1) {
                contentViewId = R.layout.leftgrip_up;
            } else {
                contentViewId = R.layout.rightgrip_up;
            }
        } else if ((this.mRightGripMode & 536870912) == 536870912) {
            if ((this.mRightGripMode & 1) == 1) {
                contentViewId = R.layout.leftgrip_down;
            } else {
                contentViewId = R.layout.rightgrip_down;
            }
        } else if ((this.mRightGripMode & 1) == 1) {
            contentViewId = R.layout.leftgrip_up;
        } else {
            contentViewId = R.layout.rightgrip_up;
        }
        PopupWindow grip = new PopupWindow(this.mContext);
        grip.setTouchable(false);
        grip.setFocusable(false);
        grip.setClippingEnabled(false);
        grip.setBackgroundDrawable(null);
        grip.setOutsideTouchable(true);
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService("layout_inflater");
        ImageView imv = (ImageView) inflate.inflate(contentViewId, (ViewGroup) null);
        grip.setContentView(imv);
        imv.measure(0, 0);
        int width = imv.getMeasuredWidth();
        int height = imv.getMeasuredHeight();
        if (width != 0 && width != this.mGripWidth) {
            this.mGripWidth = width;
        }
        if (height != 0 && height != this.mGripWidth) {
            this.mGripHeight = height;
        }
        return grip;
    }

    private void moveToTextSelectGrip(int kinds, int x, int y) {
        PopupWindow grip;
        this.mHandler.removeCallbacks(this.mRunnableDismissTextSelectGrip);
        if (kinds == 0) {
            grip = this.mTextSelectLeftGrip;
        } else {
            grip = this.mTextSelectRightGrip;
        }
        if (grip != null) {
            if (this.mTextSelectGripOffsetInWindow == null) {
                this.mTextSelectGripOffsetInWindow = new int[2];
            }
            getLocationInWindow(this.mTextSelectGripOffsetInWindow);
            int popupWidth = this.mGripWidth;
            int popupHeight = this.mGripHeight;
            if (grip != null && grip.isShowing()) {
                grip.update(this.mTextSelectGripOffsetInWindow[0] + x, this.mTextSelectGripOffsetInWindow[1] + y, popupWidth, popupHeight);
            }
            int[] wxy = new int[2];
            int[] sxy = new int[2];
            grip.getContentView().getLocationInWindow(wxy);
            grip.getContentView().getLocationOnScreen(sxy);
        }
    }

    private void setFocuseTextSelectGrip(int kinds, boolean isFocuse) {
        PopupWindow grip;
        if (kinds == 0) {
            grip = this.mTextSelectLeftGrip;
        } else {
            grip = this.mTextSelectRightGrip;
        }
        ImageView iv = (ImageView) grip.getContentView();
        iv.setSelected(isFocuse);
    }

    private void showTextSelectGrip(int[] left, int[] right) {
        this.mHandler.removeCallbacks(this.mRunnableDismissTextSelectGrip);
        if (this.mTextSelectLeftGrip == null) {
            this.mTextSelectLeftGrip = makeTextSelectGrip(0);
        }
        if (this.mTextSelectRightGrip == null) {
            this.mTextSelectRightGrip = makeTextSelectGrip(1);
        }
        if (this.mTextSelectGripOffsetInWindow == null) {
            this.mTextSelectGripOffsetInWindow = new int[2];
        }
        getLocationInWindow(this.mTextSelectGripOffsetInWindow);
        int popupWidth = this.mGripWidth;
        int popupHeight = this.mGripHeight;
        int mLeftGripViewX = left[0];
        int mLeftGripViewY = left[1];
        int mRightGripViewX = right[0];
        int mRightGripViewY = right[1];
        if (this.mTextSelectLeftGrip != null) {
            if (this.mTextSelectLeftGrip.isShowing()) {
                this.mTextSelectLeftGrip.update(this.mTextSelectGripOffsetInWindow[0] + mLeftGripViewX, this.mTextSelectGripOffsetInWindow[1] + mLeftGripViewY, popupWidth, popupHeight);
            } else {
                this.mTextSelectLeftGrip.setWidth(popupWidth);
                this.mTextSelectLeftGrip.setHeight(popupHeight);
                this.mTextSelectLeftGrip.showAtLocation(this, 0, this.mTextSelectGripOffsetInWindow[0] + mLeftGripViewX, this.mTextSelectGripOffsetInWindow[1] + mLeftGripViewY);
            }
        }
        if (this.mTextSelectRightGrip != null) {
            if (this.mTextSelectRightGrip.isShowing()) {
                this.mTextSelectRightGrip.update(this.mTextSelectGripOffsetInWindow[0] + mRightGripViewX, this.mTextSelectGripOffsetInWindow[1] + mRightGripViewY, popupWidth, popupHeight);
                return;
            }
            this.mTextSelectRightGrip.setWidth(popupWidth);
            this.mTextSelectRightGrip.setHeight(popupHeight);
            this.mTextSelectRightGrip.showAtLocation(this, 0, this.mTextSelectGripOffsetInWindow[0] + mRightGripViewX, this.mTextSelectGripOffsetInWindow[1] + mRightGripViewY);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dismissTextSelectGrip() {
        if (this.mTextSelectLeftGrip != null && this.mTextSelectLeftGrip.isShowing()) {
            this.mTextSelectLeftGrip.dismiss();
        }
        if (this.mTextSelectRightGrip != null && this.mTextSelectRightGrip.isShowing()) {
            this.mTextSelectRightGrip.dismiss();
        }
    }

    public boolean onTouchEvent(MotionEvent event, int scrollY) {
        if (scrollY >= 0) {
            this.mIsIntercept = scrollY;
            onTouchEvent(event);
            this.mIsIntercept = 0;
            return true;
        }
        return false;
    }

    public int getDbtype() {
        return this.mCurDbType;
    }

    public String getKeyword() {
        return this.mCurKeyword;
    }

    public int getSuid() {
        return this.mCurSuid;
    }

    public void setKeyword(String keyword) {
        this.mCurKeyword = keyword;
    }

    public void setCurWordInfo(int dbtype, String keyword, int suid) {
        this.mCurDbType = dbtype;
        this.mCurKeyword = keyword;
        this.mCurSuid = suid;
    }

    public void setDisplayMode(int displayMode) {
        this.mDisplayMode = displayMode;
    }

    public boolean isMarkerMode() {
        return this.mIsMarkerMode;
    }

    public void setMakerMode(boolean b) {
        this.mIsMarkerMode = b;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    private void startFlickLeft() {
        if (this.mCurKeyword != null && !this.mCurKeyword.equals("") && this.mUpdateLayoutCallback != null) {
            this.mUpdateLayoutCallback.run(GESTURE_SWIPE_LEFT);
        }
    }

    private void startFlickRight() {
        if (this.mUpdateLayoutCallback != null) {
            this.mUpdateLayoutCallback.run(GESTURE_SWIPE_RIGHT);
        }
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent e) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent e) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public void saveMarkerObject() {
        if (this.mCurKeyword != null && this.mCurKeyword.length() != 0) {
            ArrayList<MarkerObject> makerObj = (ArrayList) this.mMarkerObj;
            int dbtyp = this.mCurDbType;
            String keyword = this.mCurKeyword;
            int suid = this.mCurSuid;
            if (makerObj != null && makerObj.size() > 0) {
                DioDictDatabase.addMarker(this.mContext, dbtyp, keyword, suid, makerObj);
            }
            if (DioDictDatabase.existMarker(this.mContext, dbtyp, suid) == null) {
                return;
            }
            if (makerObj == null || (makerObj != null && makerObj.size() == 0)) {
                DioDictDatabase.deleteMarker(this.mContext, dbtyp, keyword, suid);
            }
        }
    }

    public void refreshMarkerObject() {
        if (this.mCurKeyword != null) {
            ArrayList<MarkerObject> makerObj = (ArrayList) this.mMarkerObj;
            int dbtyp = this.mCurDbType;
            int suid = this.mCurSuid;
            if (DioDictDatabase.existMarker(this.mContext, dbtyp, suid) == null && makerObj != null) {
                removeAllMarker();
            }
        }
    }

    public int getSuperHeight() {
        return super.getLayout().getHeight();
    }

    public void getSelectTextTopLineBound(int[] top, int[] bottom) {
        if (this.mScrollView == null) {
            setScrollView();
        }
        List<Rect> tR = getRectOfSelectedTextOffset(this.mSelectTextArea.start, this.mSelectTextArea.end);
        int[] locInParents = new int[2];
        getLocationInParentsView(locInParents, 0, tR.get(0).top);
        top[0] = locInParents[1];
        getLocationInParentsView(locInParents, 0, tR.get(0).bottom);
        bottom[0] = locInParents[1];
    }

    public void setScrollView() {
        this.mScrollView = (ExtendScrollView) getParent();
    }

    public void setParentScrollTo(int x, int y) {
        if (this.mScrollView != null) {
            this.mScrollView.scrollTo(x, y);
        }
    }

    public void stopInvilidate() {
        this.mIsEnableInvalidate = false;
    }

    public ExtendScrollView getScrollView() {
        if (this.mScrollView == null) {
            this.mScrollView = (ExtendScrollView) getParent();
        }
        return this.mScrollView;
    }
}
