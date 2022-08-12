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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.diotek.diodict.Preference;
import com.diotek.diodict.SearchListActivity;
import com.diotek.diodict.ViewUtils;
import com.diotek.diodict.dtestui.MeanToolbarWidgets;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.utils.CMN;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictUtils;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.utils.GlobalOptions;
import com.diodict.decompiled.R;
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
    private boolean bIsRemoving;
    private int mActiveGrip;
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
	
	/** is mark popup showing */
    private boolean bIsMarking;
	/** is drawing marks */
    private boolean bIsDrawingMarks;
	/** whether to select text and then mark selection */
    public boolean bIsOneShotMark;
	private int mMarkColor;
	
    private int mLeftGripMode;
    int[] mLeftGripPosition;
    private List<MarkerObject> mMarks;
    private boolean bMarkable;
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
	//FrameLayout mNoScrollView;
    private BaseActivity activity;
	public MeanToolbarWidgets toolbarWidgets;
    @NonNull private TextArea mSelectionArea = new TextArea();
	
	/** true=automatically shrink text selection on second click */
	private boolean bAutoRealm = true;
	/** 0=paragraph 1=semi-paragraph 2=word */
	public int mSelectRealmPrefer = 0;
	/** 0=paragraph 1=semi-paragraph 2=word */
	public int mSelectRealmPreferLong = 2;
	/** 0=paragraph 1=semi-paragraph 2=word */
    private int mSelectRealm = mSelectRealmPrefer;
	/** word break counter in a paragraph */
	private int breakCnt = 0;
	
    private CharSequence mSelectedText;
    private int mStoreCurrentTopOffset;
    private int mSwipeThreshold;
	
    private ExtendTextCallback mTTSCallback;
    private TagConverterCallback mTagConverterCallback;
	
    private boolean mTextLineSelect;
    private int mTextSelectColor;
    private int[] mTextSelectGripOffsetInWindow;
	
    private PopupWindow mTextSelectLeftGrip;
	private PopupWindow mTextSelectRightGrip;
    private PopupWindow mTextSelectPopupMenu;
	
    private View.OnClickListener mTextSelectPopupOnClickListener;
	
    private ExtendTextCallback mUpdateLayoutCallback;
	
    private final int mWeightMovingX;
	
    private final float mWeightMovingY;
	
    private ExtendTextCallback mWikiSearchCallback;
	
	private boolean bIgnoreNextUp;
	
    private Button wiki;
	private int downScrollY;
	private int orgX, orgY, lastX, lastY;
	
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
        this(context, attrs, 0);
    }

    public ExtendTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPopupBaseX = 0;
        this.mPopupBaseY = 0;
        this.mGripWidth = 38;
        this.mGripHeight = 72;
        this.mTextSelectColor = Color.argb(255, 255, 235, 170);
        this.mMarkColor = -16776961;
        this.mMarks = null;
        this.mContext = null;
        this.mMoving = false;
        this.mHandler = new Handler();
        this.mLeftGripPosition = new int[2];
        this.mRightGripPosition = new int[2];
        this.bMarkable = false;
        this.GRIP_LEFT = 1;
        this.GRIP_RIGHT = 2;
        this.GRIP_H_MASK = 15;
        this.GRIP_UP = 268435456;
        this.GRIP_DOWN = 0x20000000;
        this.GRIP_V_MASK = 0xF0000000;
        this.mLeftGripMode = 0;
        this.mRightGripMode = 0;
        this.TEXT_SELECT_NONE_GRIP = -1;
        this.TEXT_SELECT_LEFT_GRIP = 0;
        this.TEXT_SELECT_RIGHT_GRIP = 1;
        this.mWeightMovingX = 2;
        this.mWeightMovingY = 1.2f;
        this.bIsRemoving = false;
        this.mRemoveTextArea = new TextArea();
        this.mScrollView = null;
        this.mMeanLinearLayout = null;
        this.mDisplayMode = 15;
        this.SWIPE_THRESHOLD_PIXEL = 100;
        this.mTextLineSelect = false;
        this.mIsEnableInvalidate = true;
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
                // dismissTextSelectController();
                switch (v.getId()) {
                    case R.id.popup_copy /* 2131100016 */:
                        actionClipboardCopy();
                        return;
                    case R.id.popup_hyper /* 2131100017 */:
                        actionHyperText();
                        return;
                    case R.id.popup_google /* 2131100018 */:
                        actionGoogleSearch();
                        return;
                    case R.id.popup_wiki /* 2131100019 */:
                        actionWikiSearch();
                        return;
                    case R.id.popup_tts_us /* 2131100020 */:
                        actionTTS(1);
                        return;
                    case R.id.popup_tts_uk /* 2131100021 */:
                        actionTTS(2);
                        return;
                    case R.id.popup_tts /* 2131100022 */:
                        actionTTS(0);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mRunnableDismissTextSelectGrip = new Runnable() { // from class: com.diotek.diodict.mean.ExtendTextView.2
            @Override // java.lang.Runnable
            public void run() {
                dismissTextSelectGrip();
                hideTextSelectActionMenu();
                invalidate();
            }
        };
        initPrivateAttribute(context, attrs);
		if (context instanceof BaseActivity) {
			activity = (BaseActivity) context;
		}
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
        this.bMarkable = isEnable;
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
            this.bIsMarking = false;
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
        int fontSizeIndex = Preference.getFontSize();
        int markerColorIndex = Preference.markColor();
        int[] colorList = getResources().getIntArray(R.array.value_marker_color_adv);
        int[] fontSizeList = getResources().getIntArray(R.array.value_font_size);
        super.setTextSize(fontSizeList[fontSizeIndex]);
        setMarkerColor(colorList[markerColorIndex]);
        super.setTextColor(-16777216);
    }

    private void initPrivateAttribute(Context context, AttributeSet attrs) {
        if (this.mMarks != null) {
            this.mMarks.clear();
        }
        this.mMoving = false;
        this.bMarkable = getParsingMarkerable(attrs);
        setTypeface(DictUtils.createfont());
        this.mContext = context;
        this.bIsMarking = false;
        if (this.mSelectionArea == null) {
            this.mSelectionArea = new TextArea();
        }
        if (this.mRemoveTextArea == null) {
            this.mRemoveTextArea = new TextArea();
        }
        initializeSelectTextArea();
        this.flingDetector = new GestureDetector(context, this);
		this.flingDetector.setIsLongpressEnabled(false);
        this.mSwipeThreshold = (int) (100.0f * getResources().getDisplayMetrics().density);
        this.mTextLineSelect = false;
        this.mGripWidth = getResources().getDimensionPixelSize(R.dimen.mean_extendView_gripWidth);
        restoreSettings(this.mContext);
    }

    public void setMakerObject(ArrayList<MarkerObject> makerObj) {
        this.mMarks = makerObj;
    }

    public List<MarkerObject> getMakerObject() {
        return this.mMarks;
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
        int gripLeftVeticalMode = this.mLeftGripMode & (0xF0000000);
        int gripRightVeticalMode = this.mRightGripMode & (0xF0000000);
        if (gripLeftVeticalMode == 0x20000000) {
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
        if (gripRightVeticalMode == 0x20000000) {
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

    public boolean gripShowing() {
        return this.mTextSelectLeftGrip != null
				&& this.mTextSelectLeftGrip.isShowing()
				&& this.mTextSelectRightGrip != null
				&& this.mTextSelectRightGrip.isShowing();
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
        return this.mSelectionArea.isTextSelected() && this.mSelectionArea.contains(off);
    }

    public void setEnableTextSelect(boolean isEnableTextSelect) {
        this.mIsEnableTextSelect = isEnableTextSelect;
    }

    private boolean handleDragGrip(MotionEvent event, int actionMasked) {
		//CMN.Log("onTouchEventForGrip::", event.getActionMasked(), mIsEnableTextSelect, gripShowing());
        int y;
        int x;
        int y2;
        int x2;
        int gripVeticalMode;
        int moveToY;
        int x3 = lastX = (int) event.getX();
        int y3 = lastY = (int) event.getY();
        if (this.mIsIntercept > 0) {
            y3 += this.mIsIntercept;
        }
        switch (actionMasked) {
			case MotionEvent.ACTION_DOWN:
                int[] location = new int[2];
                getLocationInTextView(location, x3, y3);
                this.mPopupBaseX = 0;
                this.mPopupBaseY = location[1];
                if (gripShowing()) {
                    int[] locInParents = new int[2];
                    getLocationInParentsView(locInParents, x3, y3);
                    int fx = locInParents[0];
                    int fy = locInParents[1];
                    this.mActiveGrip = getSelectTextSelectGrip(fx, fy);
					//CMN.Log("mActivityGrip::", mActivityGrip);
					if (this.mActiveGrip != -1) {
                        this.mHandler.removeCallbacks(this.mRunnableDismissTextSelectGrip);
                        hideTextSelectActionMenu();
                        setFocuseTextSelectGrip(this.mActiveGrip, true);
                        return true;
                    }/* else if (isContainsOffset(x3, y3)) {
                        return true;
                    }*/
                }
                break;
			case MotionEvent.ACTION_UP:
				removeCallbacks(longPressTextRun);
				if(gripShowing() && !bIgnoreNextUp)
				if (this.mActiveGrip != -1) {
                    int oldSelectAreaStart = this.mSelectionArea.start;
                    int oldSelectAreaEnd = this.mSelectionArea.end;
                    Layout layout = super.getLayout();
                    int[] xy = new int[2];
                    int sy = 0;
                    if (this.mScrollView != null) {
                        sy = this.mScrollView.getScrollY();
                    }
                    int[] sxy = new int[2];
                    getLocationInTextView(sxy, 0, sy);
                    setFocuseTextSelectGrip(this.mActiveGrip, false);
                    if (this.mActiveGrip == 0) {
                        if ((this.mLeftGripMode & 0x20000000) == 0x20000000) {
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
                        if (this.mSelectionArea.end < foff) {
                            this.mSelectionArea.start = this.mSelectionArea.end;
                            this.mSelectionArea.end = foff;
                        } else {
                            this.mSelectionArea.start = foff;
                        }
                    } else {
                        if ((this.mRightGripMode & 0x20000000) == 0x20000000) {
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
                        if (this.mSelectionArea.start > off) {
                            this.mSelectionArea.end = this.mSelectionArea.start;
                            this.mSelectionArea.start = off;
                        } else {
                            this.mSelectionArea.end = off;
                        }
                    }
                    if (this.mSelectionArea.start == this.mSelectionArea.end) {
                        this.mSelectionArea.start = oldSelectAreaStart;
                        this.mSelectionArea.end = oldSelectAreaEnd;
                    }
                    if (this.mSelectionArea.isTextSelected()) {
                        getTextSelectGripPosition(this.mSelectionArea.start, this.mSelectionArea.end, this.mLeftGripPosition, this.mRightGripPosition);
                        showTextSelectGrip(this.mLeftGripPosition, this.mRightGripPosition);
                        showMenu();
                        invalidate();
                    }
                    return true;
                }
				else if (gripShowing() && isContainsOffset(x3, y3) && this.mTextSelectPopupMenu != null && !this.mTextSelectPopupMenu.isShowing()) {
                    showMenu();
                    //return true;
                }
                break;
			case MotionEvent.ACTION_MOVE:
                if (gripShowing() && this.mActiveGrip != -1) {
                    getLocationInParentsView(new int[2], x3, y3);
                    if (this.mActiveGrip == 0) {
                        gripVeticalMode = this.mLeftGripMode & (0xF0000000);
                    } else {
                        gripVeticalMode = this.mRightGripMode & (0xF0000000);
                    }
                    int moveToX = x3 - (this.mGripWidth / 2);
                    if (gripVeticalMode == 0x20000000) {
                        moveToY = (int) (y3 - (this.mGripHeight / 1.2f));
                    } else {
                        moveToY = y3 - (this.mGripHeight / 6);
                    }
                    moveToTextSelectGrip(this.mActiveGrip, moveToX, moveToY);
                    return true;
                }/* else if (isContainsOffset(x3, y3)) {
                    return true;
                }*/
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
		if (!this.bMarkable && !gripShowing()
				|| !this.bIsMarking && !this.mTextLineSelect && !gripShowing()) {
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
		//CMN.Log("onTouchEvent::", event.getActionMasked(), "gripShowing="+gripShowing());
		int action = event.getActionMasked();
		if (action!=MotionEvent.ACTION_POINTER_DOWN && action!=MotionEvent.ACTION_POINTER_UP) {
			try {
				boolean flingEvent = this.flingDetector.onTouchEvent(event);
				// CMN.Log("flingEvent::", flingEvent, event.getActionMasked());
				if (flingEvent) {
					removeCallbacks(longPressTextRun);
					//this.flingDetector = new GestureDetector(getContext(), this);
					return flingEvent;
				}
			} catch (Exception e) {
				CMN.Log(e);
			}
		}
		if (action==MotionEvent.ACTION_DOWN) {
			if (this.mScrollView != null)
				downScrollY = mScrollView.getScrollY();
			orgX = (int) event.getX();
			orgY = (int) event.getY();
			if (activity!=null && activity.clearTextViewSelection()) { // foca =
				bIgnoreNextUp = true;
			}
			else if(bIgnoreNextUp)
				bIgnoreNextUp = false;
		}
		else if (action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL) {
			removeCallbacks(longPressTextRun);
		}
		bMarkable = true;

//		mTextLineSelect = true;
//		mIsEnableTextSelect = true;
		if (this.bMarkable || mSelectionArea.isTextSelected()) {
			if (bIsMarking) {
				if (bIsRemoving) { //todo!!! 删除
					handleRemoveMarker(event);
					return true;
				}
				if (handleDrawMarkers(event, action)) {
					return true;
				}
			}
			if(mIsEnableTextSelect || gripShowing()){
				if (handleDragGrip(event, action)) {
					CMN.Log("onTouchEventForGrip!!!");
					return true;
				}
//				if (this.mTextLineSelect) {
//					return onTouchEventInLocalForLineSelect(event);
//				}
				if (handleTouchWord(event, action)) {
					return true;
				}
			}
		}
		if (!mIsEnableTextSelect && action==MotionEvent.ACTION_UP && !gripShowing() && !bIgnoreNextUp){
			startFlickLeft();
			return true;
		}
		if(!mIsEnableTextSelect && action==MotionEvent.ACTION_DOWN){
			// enable text selection for longPress even when the view is collapsed
			lastX = orgX;
			lastY = orgY;
			removeCallbacks(longPressTextRun);
			postDelayed(longPressTextRun, 700);
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
        switch (event.getActionMasked()) {
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
        if (this.mMarks != null) {
            for (int i = this.mMarks.size() - 1; i >= 0; i--) {
                MarkerObject obj = this.mMarks.get(i);
                if (obj.contain(StartOffset) && obj.contain(endOffset)) {
                    this.mMarks.remove(i);
                    return;
                }
            }
        }
    }

    public void removeAllMarker() {
        if (this.mMarks != null) {
            this.mMarks.clear();
            this.mMarks = null;
        }
    }

    private boolean handleDrawMarkers(MotionEvent event, int action) {
		int x = lastX = (int) event.getX();
		int y = lastY = (int) event.getY();
		if (bIsOneShotMark) {
			if ((action==MotionEvent.ACTION_UP||action==MotionEvent.ACTION_CANCEL)
					&& !bIgnoreNextUp && mActiveGrip==-1 && toolbarWidgets!=null) {
				// 选选择，再高亮。则允许点击关闭。
				toolbarWidgets.dismissMarkerPopup();
				return true;
			}
			return false;
		}
		else if (!bIsDrawingMarks && action != MotionEvent.ACTION_DOWN) {
			if ((action==MotionEvent.ACTION_UP)
					&& true // 通用允许关闭 todo settings
					&& mScrollView!=null && downScrollY == mScrollView.getScrollY()
			) {
				toolbarWidgets.dismissMarkerPopup();
			}
			return true; // allow scrolling
		}
        getLocationInTextView(location, x, y);
        int fx = location[0];
        int fy = location[1];
        Layout layout = super.getLayout();
        int line = layout.getLineForVertical(fy);
        int off = layout.getOffsetForHorizontal(line, fx);
        if (fx >= (super.getWidth() - super.getTotalPaddingRight()) - (super.getTextScaleX() * super.getTextSize())) {
            off = layout.getLineEnd(line);
        }
        int ty = layout.getLineBottom(line);
        if (y > ty) {
            off = -1;
        }
		int mSelectTextMode=1;
		final TextArea sela = this.mSelectionArea;
		switch (action) {
			case MotionEvent.ACTION_DOWN:{
				mMoving = false;
				int pad = (int) (2*GlobalOptions.density);
				if (x < layout.getLineLeft(line) - pad || x > layout.getLineRight(line) + pad
						|| y < layout.getLineTop(line) - pad || y > layout.getLineBaseline(line) + pad
				) {
					off = -1; // allow scrolling
				}
				if (off >= 0) {
					sela.start = off;
					sela.start_line = line;
					bIsDrawingMarks = true;
				} else {
					bIsDrawingMarks = false;
				}
				if (!bIsOneShotMark && mScrollView != null) {
					mScrollView.setScrollEnabled(!bIsDrawingMarks);
				}
				downScrollY = mScrollView.getScrollY();
			} return true;
			case MotionEvent.ACTION_UP:
				// CMN.debug("mSelectTextMode::ACTION_UP");
                if (mSelectTextMode == 0) {
                    if (sela.start_line == line) {
                        sela.end = off;
                    } else if (sela.start_line >= 0 && sela.start_line < layout.getLineCount()) {
                        sela.end = layout.getLineEnd(sela.start_line);
                    }
                } else {
                    sela.end = off;
                }
                if (sela.start > sela.end) {
                    int tempOffset = sela.end;
                    sela.end = sela.start;
                    sela.start = tempOffset;
                }
                // return true;
			case MotionEvent.ACTION_CANCEL:
				// CMN.debug("mSelectTextMode::ACTION_CANCEL");
				if (sela.start != -1 && sela.move != -1 && sela.start != sela.move) {
					sela.end = sela.move;
					performMark();
					invalidate();
					return true;
				}
				if (!bIsOneShotMark && mScrollView!=null) {
					mScrollView.setScrollEnabled(true);
				}
				mMoving = false;
				return true;
            case MotionEvent.ACTION_MOVE:
                this.mMoving = true;
                if (mSelectTextMode == 0) {
                    if (sela.start_line == line) {
                        sela.move = off;
                    }  else if (sela.start_line >= 0 && sela.start_line < layout.getLineCount()) {
                        sela.move = layout.getLineEnd(sela.start_line);
                    }
					//else off = sela.move;
                } else {
                    sela.move = off;
                }
                List<Rect> selectRect = getRectOfSelectedTextOffset(sela.start, sela.move);
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
            default:
                return true;
        }
    }
	
	int[] location = new int[2];
	// event nullable where null indicating longPress
    private boolean handleTouchWord(MotionEvent event, int action) {
		if (action==MotionEvent.ACTION_UP && (!bIgnoreNextUp || event==null)) { // 放手了
			removeCallbacks(longPressTextRun);
			getLocationInTextView(location, lastX, lastY);
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
			if (lastY > ty) {
				off = -1;
			}
			CMN.Log("handleTouchWord::off=", off);
			if (off != -1) {
				TagConverter.DictPos pos = this.mTagConverterCallback.isLinkOffset(off);
				if (pos != null) {
					saveMarkerObject();
					clearSelection();
					this.mSelectionArea.start = pos.start;
					this.mSelectionArea.end = pos.end;
					invalidate();
					return super.onTouchEvent(event);
				}
				CMN.Log("startClick::", mSelectionArea.isTextSelected(), mSelectionArea.contains(off));
				boolean reinitSel = true;
				if (mSelectionArea.isTextSelected()) {
					// 如果已经选中
					if (mSelectionArea.contains(off)) {
						// 如果点击选取内部，逐步较小选取范围
						if (bAutoRealm && mSelectRealm + 1 <= 2) {
							CMN.debug("缩小选区!", "breakCnt="+breakCnt);
							if (mSelectRealm == 0 && breakCnt < 3) {
								mSelectRealm = 2; // 一步到位
							} else {
								mSelectRealm++;
							}
							clearSelection();
						} else if(event!=null){
							reinitSel = false;
						}
					} else if (event != null) {
						mSelectRealm = mSelectRealmPrefer;
						CMN.debug("清空选区!", mSelectRealm);
						clearSelection();
						reinitSel = false;
					}
				}
				if (reinitSel) selectWordAt(off);
				// end startClick
				if (mSelectionArea.isTextSelected()) {
					getTextSelectGripPosition(this.mSelectionArea.start, this.mSelectionArea.end, this.mLeftGripPosition, this.mRightGripPosition);
					showTextSelectGrip(this.mLeftGripPosition, this.mRightGripPosition);
					showMenu();
					invalidate();
				}
			} else {
				this.mSelectionArea.init();
				mSelectRealm = mSelectRealmPrefer;
			}
			if (!this.mSelectionArea.isTextSelected()) {
				clearSelection();
				invalidate();
			}
			return true;
		}
		else if (action==MotionEvent.ACTION_DOWN) {
			// try to trigger longPress. todo dynamically assign active grip on move
			removeCallbacks(longPressTextRun);
			postDelayed(longPressTextRun, 700);
		}
		return false;
    }
	
	Runnable longPressTextRun = new Runnable() {
		@Override
		public void run() {
			if (mScrollView != null && mScrollView.getScrollY()==downScrollY) {
				boolean enable = mIsEnableTextSelect;
				if(!enable) setEnableTextSelect(true);
				// CMN.Log("longPressTextRun!!!");
				//MotionEvent evt = MotionEvent.obtain(0, 0, 0, orgX, orgY, 0);
				mSelectRealm = mSelectRealmPreferLong;
				handleTouchWord(null, MotionEvent.ACTION_UP);
				//evt.recycle();
				if(!enable) setEnableTextSelect(enable);
				bIgnoreNextUp = true;
			}
		}
	};

	/** select paragraph / sentence / word at offset. <br/>
	 * controlled by {@link #bAutoRealm} and {@link #mSelectRealm} and it preferences ( todo impl settings ui ) <br/>
	 * ( def behaviour : when expanded, use single tap to select paragraph -> then sentence -> then word. longPress to select word. )
* 				when collapsed, use longPress to select paragraph. then tap in the selection area to shrink it ) */
    private void selectWordAt(int off) {
        if (this.mSelectionArea == null) {
            this.mSelectionArea = new TextArea();
        }
		final boolean selWord = mSelectRealm==2;
        CharSequence text = super.getText();
        if (off >= text.length()) {
            this.mSelectionArea.init();
			return;
        }
		char charAt = text.charAt(off);
		if ((!selWord || charAt!=' ') && isWordBreak(charAt)) {
            this.mSelectionArea.init();
        }
		else {
            int charBlock = CodeBlock.getCodeBlock(charAt), newBlock;
			// CMN.Log("charBlock::", charBlock);
			boolean isNewBlock; // reeached text of new language
			char c;
			breakCnt = 0;
			// 左
            for (int i = off; i >= 0; i--) {
				c = text.charAt(i);
				 CMN.Log("charBlock::", c, CodeBlock.getCodeBlock(c));
				if (charBlock==0) {
					newBlock = CodeBlock.getCodeBlock(c);
					if (newBlock!=0) {
						charBlock = newBlock;
						charAt = c;
					}
					isNewBlock = false;
				} else if((selWord || c!=' ') && (newBlock=CodeBlock.getCodeBlock(c))!=0){
					isNewBlock =  newBlock != charBlock;
				} else {
					isNewBlock = false;
				}
				// isNewBlock 计算完毕
                if (isNewBlock || isWordBreak(c)) { // 截止
					breakCnt++;
					if (charAt!=' ' || !selWord) { // 选词时击中词后的空格也算
						this.mSelectionArea.start = i + 1;
						break;
					}
                }
                if (i == 0) {
                    this.mSelectionArea.start = i;
                }
            }
			// 右
            for (int i = off; i < text.length(); i++) {
				c = text.charAt(i);
				if (charBlock==0) {
					breakCnt++;
					newBlock = CodeBlock.getCodeBlock(c);
					if (newBlock!=0) {
						charBlock = newBlock;
					}
					isNewBlock = false;
				} else if((selWord || c!=' ') && (newBlock=CodeBlock.getCodeBlock(c))!=0){
					isNewBlock =  newBlock != charBlock;
				} else {
					isNewBlock = false;
				}
				// isNewBlock 计算完毕
                if (isNewBlock || isWordBreak(c)) { // 截止
                    this.mSelectionArea.end = i;
                    return;
                }
                if (i == text.length() - 1) {
                    this.mSelectionArea.end = text.length();
                }
            }
        }
    }
	
    private boolean isSentenceSeperator(char charAt) {
		return charAt==',' || charAt=='，' || charAt==';' || charAt=='；' || charAt=='.' || charAt=='。'
				|| charAt=='！' || charAt=='!' || charAt=='？' || charAt=='?';
	}
	
    private boolean isWordBreak(char charAt) {
		final boolean selWord = mSelectRealm==2;
		if(charAt==' ') {
			return selWord;
		}
		if (!selWord && charAt=='~') {
			return false;
		}
		if(mSelectRealm==0) { // 选中整个同语言的段落
			if(isSentenceSeperator(charAt)) {
				breakCnt++;
				return false;
			}
			if(charAt=='\r' || charAt=='\n') {
				return true;
			}
			int typ = Character.getType(charAt);
			if(typ != Character.PARAGRAPH_SEPARATOR && typ != Character.LINE_SEPARATOR) {
				return false;
			}
		}
        return !CodeBlock.isAlpabetCodeBlock(charAt)
				&& !CodeBlock.isHangulCodeBlock(charAt)
				&& !CodeBlock.isChineseCodeBlock(charAt)
				&& !CodeBlock.isLatin(charAt)
//				&& (typ!=Character.UPPERCASE_LETTER && typ!=Character.LOWERCASE_LETTER)
				&& !CodeBlock.isJapan(charAt)
				&& !DictUtils.isDioSymbolAlphabet(charAt);
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
        if (this.mMarks != null && this.mMarks.size() > 0) {
            Rect inRect = getInvalidateRect();
            int textLength = super.getText().length();
            for (int i = 0; i < this.mMarks.size(); i++) {
                MarkerObject obj = this.mMarks.get(i);
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
        int color = !bIsMarking || bIsOneShotMark?mTextSelectColor:mMarkColor;
        if (this.mMoving && !gripShowing()
				&& this.mSelectionArea.isTextSelecting()
				&& (tR = getRectOfSelectedTextOffset(this.mSelectionArea.start, this.mSelectionArea.move)) != null
				&& tR.size() > 0) {
            for (int j = 0; j < tR.size(); j++) {
                onDrawTextArea(canvas, tR.get(j), color);
            }
        }
    }

    private void drawSelectedTextArea(Canvas canvas) {
        List<Rect> tR;
		int color = !bIsMarking || bIsOneShotMark?mTextSelectColor:mMarkColor;
        if (!this.mMoving
				&& this.mSelectionArea.isTextSelected()
				&& (tR = getRectOfSelectedTextOffset(this.mSelectionArea.start, this.mSelectionArea.end)) != null
				&& tR.size() > 0) {
            for (int j = 0; j < tR.size(); j++) {
                onDrawTextArea(canvas, tR.get(j), color);
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
        this.mMarkColor = color;
		this.bIsRemoving = color == Integer.MAX_VALUE;
    }

    public void makeMenu() {
        if (this.mContext != null) {
            if (this.mTextSelectPopupMenu == null) {
                this.mTextSelectPopupMenu = new PopupWindow(this.mContext);
				this.mTextSelectPopupMenu.setTouchable(true);
                this.mTextSelectPopupMenu.setClippingEnabled(false);
                this.mTextSelectPopupMenu.setBackgroundDrawable(null);
                this.mTextSelectPopupMenu.setBackgroundDrawable(new ColorDrawable(0));
            }
            LayoutInflater inflate = (LayoutInflater) getContext().getSystemService("layout_inflater");
            this.mTextSelectPopupMenu.setFocusable(false);
			this.mTextSelectPopupMenu.setOutsideTouchable(true);
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
                this.wiki.setVisibility(View.GONE);
                this.bWikiVisible = false;
            }
            float density = GlobalOptions.density;
            String ttsWord = "";
			if (this.mSelectionArea.isTextSelected()) {
				try {
					ttsWord = super.getText().subSequence(this.mSelectionArea.start, this.mSelectionArea.end).toString();
				} catch (Exception e) {
					CMN.debug(this.mSelectionArea.start, this.mSelectionArea.end, e);
				}
			}
			if (ttsWord.length() == 0) {
                tts.setVisibility(View.GONE);
                tts_us.setVisibility(View.GONE);
                tts_uk.setVisibility(View.GONE);
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
                        tts.setVisibility(View.GONE);
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
                            tts_uk.setVisibility(View.GONE);
                        }
                    } else {
                        tts_us.setVisibility(View.GONE);
                        tts_uk.setVisibility(View.GONE);
                    }
                    if (this.bWikiVisible) {
                        this.wiki.setBackgroundResource(R.drawable.selectpop_btn);
                        this.wiki.setPadding((int) (15.0f * density), 0, (int) (15.0f * density), 0);
                    } else {
                        this.google.setBackgroundResource(R.drawable.selectpop_btn);
                        this.google.setPadding((int) (15.0f * density), 0, (int) (15.0f * density), 0);
                    }
                } else {
                    tts.setVisibility(View.GONE);
                    tts_us.setVisibility(View.GONE);
                    tts_uk.setVisibility(View.GONE);
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

    public void showMenu() {
        if (this.mScrollView == null) {
            setScrollView();
        }
        makeMenu();
		//if(false)
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
				final int pad = (int) (18 * GlobalOptions.density);
                if ((this.mLeftGripPosition[1] - popupHeight) - this.mScrollView.getScrollY() > 0) {
                    this.mPopupBaseY = (this.mLeftGripPosition[1] - popupHeight) - pad;
                } else if ((this.mRightGripPosition[1] - this.mScrollView.getScrollY()) + popupHeight + this.mGripHeight < this.mScrollView.getHeight()) {
                    this.mPopupBaseY = this.mRightGripPosition[1] + this.mGripHeight + pad;
                } else {
                    this.mPopupBaseY = (this.mLeftGripPosition[1] - popupHeight) - pad;
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
        return this.mSelectionArea.isTextSelected() && gripShowing();
    }

    public boolean isSelectedText() {
        if (this.mSelectionArea != null) {
            return this.mSelectionArea.isTextSelected();
        }
        return false;
    }

    public void initializeSelectTextArea() {
        if (this.mSelectionArea != null) {
            this.mSelectionArea.init();
        }
        if (this.mRemoveTextArea != null) {
            this.mRemoveTextArea.init();
        }
        this.mPrevMakerRect = null;
    }

    public void performMark() {
        if (this.mMarks == null) {
            this.mMarks = new ArrayList();
        }
        if (this.mMarks != null && this.mMarks.size() < 100) {
            if (this.mSelectionArea.isTextSelected()) {
                ArrayList<TagConverter.DictPos> mTotalOffset = this.mTagConverterCallback.convert_Total_Index(this.mDisplayMode, this.mSelectionArea.start, this.mSelectionArea.end);
                for (int k = 0; k < mTotalOffset.size(); k++) {
                    TagConverter.DictPos pos = mTotalOffset.get(k);
                    this.mMarks.add(new MarkerObject(pos.start, pos.end, this.mMarkColor));
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
        if (this.mSelectionArea.isTextSelected()) {
            this.mSelectedText = super.getText().subSequence(this.mSelectionArea.start, this.mSelectionArea.end);
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
        if (this.mSelectionArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            if (this.mGoogleSearchCallback != null) {
                this.mGoogleSearchCallback.run(this.mSelectedText.toString());
            }
//            initializeSelectTextArea();
//            dismissTextSelectGrip();
//            invalidate();
        }
    }

    public void actionWikiSearch() {
        if (this.mSelectionArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            if (this.mWikiSearchCallback != null) {
                this.mWikiSearchCallback.run(this.mSelectedText.toString());
            }
//            initializeSelectTextArea();
//            dismissTextSelectGrip();
//            invalidate();
        }
    }

    public void actionTTS(int mode) {
        if (this.mSelectionArea.isTextSelected()) {
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
//            initializeSelectTextArea();
//            dismissTextSelectGrip();
//            invalidate();
        }
    }

    public void actionClipboardCopy() {
        if (this.mSelectionArea.isTextSelected()) {
            this.mSelectedText = getSelectedString();
            ClipboardManager clipboard = (ClipboardManager) this.mContext.getSystemService("clipboard");
            if (clipboard != null) {
                clipboard.setText(this.mSelectedText.toString());
            }
			dismissTextSelectController();
//            initializeSelectTextArea();
//            dismissTextSelectGrip();
//            invalidate();
        }
    }

    public String getSelectedString() {
        if (this.mSelectionArea != null && this.mSelectionArea.isTextSelected()) {
            this.mSelectedText = super.getText().subSequence(this.mSelectionArea.start, this.mSelectionArea.end);
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

    public void initSelection() {
		if (this.gripShowing()) {
			clearSelection();
			invalidate();
		}
	}
	
    public void clearSelection() {
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
            this.mLeftGripMode |= 0x20000000;
        } else if (checkStartBottom > checkHeight) {
            this.mLeftGripMode |= 268435456;
        } else {
            this.mLeftGripMode |= 0x20000000;
        }
        if ((this.mLeftGripMode & 0x20000000) == 0x20000000) {
            leftXY[1] = startLineBottom - 20;
        } else {
            leftXY[1] = (startLineTop - this.mGripHeight) + 20;
        }
        int checkEndTop = (endLineTop - this.mGripHeight) - scollY;
        int checkEndBottom = ((endLineBottom - scollY) + this.mGripHeight) - 20;
        if (checkEndTop < 0) {
            this.mRightGripMode |= 0x20000000;
        } else if (checkEndBottom > checkHeight) {
            this.mRightGripMode |= 268435456;
        } else {
            this.mRightGripMode |= 0x20000000;
        }
        if ((this.mRightGripMode & 0x20000000) == 0x20000000) {
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

    private PopupWindow makeGrip(int gripKinds) {
        int contentViewId;
        if (gripKinds == 0) {
            if ((this.mLeftGripMode & 0x20000000) == 0x20000000) {
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
        } else if ((this.mRightGripMode & 0x20000000) == 0x20000000) {
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
		grip.setOutsideTouchable(false);
        grip.setClippingEnabled(false);
        grip.setBackgroundDrawable(null);
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
            this.mTextSelectLeftGrip = makeGrip(0);
        }
        if (this.mTextSelectRightGrip == null) {
            this.mTextSelectRightGrip = makeGrip(1);
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
        return this.bIsMarking;
    }

    public void setMarkerMode(boolean mark, boolean oneshot) {
		bIsMarking = mark;
		if (mark) {
			bIsOneShotMark = oneshot;
		} else if(!bIsOneShotMark && mScrollView!=null){
			mScrollView.setScrollEnabled(true);
		}
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
            ArrayList<MarkerObject> makerObj = (ArrayList) this.mMarks;
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
            ArrayList<MarkerObject> makerObj = (ArrayList) this.mMarks;
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
        List<Rect> tR = getRectOfSelectedTextOffset(this.mSelectionArea.start, this.mSelectionArea.end);
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
