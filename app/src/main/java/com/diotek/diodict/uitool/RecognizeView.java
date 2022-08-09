package com.diotek.diodict.uitool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.diotek.diodict.engine.DictUtils;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: classes.dex */
public class RecognizeView extends View {
    public static final int IM_CHINESE = 1;
    public static final int IM_ENGLISH = 0;
    public static final int IM_HANJA = 5;
    public static final int IM_JAPANESE_HIRA = 2;
    public static final int IM_JAPANESE_KATA = 3;
    public static final int IM_KOREAN = 4;
    public static final int IM_NUMERIC = 6;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Context mContext;
    private final Paint mLinePaint;
    private int mPenColor;
    private final int mPenThickness;
    RecognizeResultCallback mRecognizeResultCallback;
    private final Rect mRect;
    private LinearLayout m_CandidateLayout;
    private Handler m_Handler;
    int m_Language;
    int m_LanguageMode;
    Point m_PrePoint;
    private HandlerRunnable m_RunnableHandle;
    int[][] m_arMode;
    char[] m_arResult;
    int m_arSize;
    int m_errorcode;
    int m_nMode;
    int m_nNumOfMode;
    int[] m_pCands;
    int[] m_pInkCount;
    private Typeface m_tf;

    /* loaded from: classes.dex */
    public interface RecognizeResultCallback {
        void setResult(String str);
    }

    public RecognizeView(Context context) {
        super(context);
        this.m_arResult = null;
        this.m_arSize = 0;
        this.m_Language = 0;
        this.m_PrePoint = null;
        this.m_nMode = 0;
        this.m_nNumOfMode = 0;
        this.mRect = new Rect();
        this.m_arMode = (int[][]) Array.newInstance(Integer.TYPE, 2, 2);
        this.m_pInkCount = new int[1];
        this.m_errorcode = 0;
        this.m_pCands = new int[1];
        this.m_LanguageMode = 0;
        this.m_Handler = new Handler();
        this.m_RunnableHandle = null;
        this.m_CandidateLayout = null;
        this.mContext = null;
        this.m_tf = null;
        this.mPenThickness = 10;
        this.mPenColor = Color.argb(255, 255, 255, 255);
        this.mRecognizeResultCallback = null;
        this.mContext = context;
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setColor(this.mPenColor);
        this.mLinePaint.setStrokeWidth(10.0f);
        HWLibInit();
        if (this.m_tf == null) {
            this.m_tf = DictUtils.createfont();
        }
    }

    public RecognizeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.m_arResult = null;
        this.m_arSize = 0;
        this.m_Language = 0;
        this.m_PrePoint = null;
        this.m_nMode = 0;
        this.m_nNumOfMode = 0;
        this.mRect = new Rect();
        this.m_arMode = (int[][]) Array.newInstance(Integer.TYPE, 2, 2);
        this.m_pInkCount = new int[1];
        this.m_errorcode = 0;
        this.m_pCands = new int[1];
        this.m_LanguageMode = 0;
        this.m_Handler = new Handler();
        this.m_RunnableHandle = null;
        this.m_CandidateLayout = null;
        this.mContext = null;
        this.m_tf = null;
        this.mPenThickness = 10;
        this.mPenColor = Color.argb(255, 255, 255, 255);
        this.mRecognizeResultCallback = null;
        this.mContext = context;
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setColor(this.mPenColor);
        this.mLinePaint.setStrokeWidth(10.0f);
        HWLibInit();
        if (this.m_tf == null) {
            this.m_tf = DictUtils.createfont();
        }
    }

    public RecognizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.m_arResult = null;
        this.m_arSize = 0;
        this.m_Language = 0;
        this.m_PrePoint = null;
        this.m_nMode = 0;
        this.m_nNumOfMode = 0;
        this.mRect = new Rect();
        this.m_arMode = (int[][]) Array.newInstance(Integer.TYPE, 2, 2);
        this.m_pInkCount = new int[1];
        this.m_errorcode = 0;
        this.m_pCands = new int[1];
        this.m_LanguageMode = 0;
        this.m_Handler = new Handler();
        this.m_RunnableHandle = null;
        this.m_CandidateLayout = null;
        this.mContext = null;
        this.m_tf = null;
        this.mPenThickness = 10;
        this.mPenColor = Color.argb(255, 255, 255, 255);
        this.mRecognizeResultCallback = null;
        this.mContext = context;
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setColor(this.mPenColor);
        HWLibInit();
        if (this.m_tf == null) {
            this.m_tf = DictUtils.createfont();
        }
    }

    public void Clear() {
        clearCanvas();
        DHWR.InkClear();
    }

    public void HWLibSetWritingArea(int left, int top, int right, int bottom, int sizeRate) {
        this.mRect.left = left;
        this.mRect.top = top;
        this.mRect.bottom = bottom;
        this.mRect.right = right;
        DHWR.SetWritingArea(left, top, right, bottom, sizeRate);
    }

    public void HWLibInit() {
        DHWR.Create();
        Clear();
        SetRecogMode(0);
        this.m_arResult = new char[210];
        this.m_arSize = 0;
        this.m_PrePoint = new Point(0, 0);
    }

    public char getarResult(int nPos) {
        if (nPos >= this.m_arSize) {
            return (char) 0;
        }
        return this.m_arResult[nPos];
    }

    public char[] getszarResult() {
        return this.m_arResult;
    }

    public int getarSize() {
        return this.m_arSize;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            onTouchEventDown(event);
        } else if (action == 2) {
            onTouchEventMove(event);
        } else if (action == 1) {
            onTouchEventUp(event);
        }
        return true;
    }

    public boolean onTouchEventDown(MotionEvent event) {
        endRecognize();
        this.m_PrePoint.x = (int) event.getX();
        this.m_PrePoint.y = (int) event.getY();
        AddPoints((short) this.m_PrePoint.x, (short) this.m_PrePoint.y);
        return true;
    }

    public boolean onTouchEventMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getHistorySize() > 0) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                float historyX = event.getHistoricalX(i);
                float historyY = event.getHistoricalY(i);
                onTouchEventMove(historyX, historyY);
            }
        }
        AddPoints((short) x, (short) y);
        drawLine(x, y, this.m_PrePoint.x, this.m_PrePoint.y);
        this.m_PrePoint.x = (int) x;
        this.m_PrePoint.y = (int) y;
        return true;
    }

    private void onTouchEventMove(float historyX, float historyY) {
        AddPoints((short) historyX, (short) historyY);
        drawLine(historyX, historyY, this.m_PrePoint.x, this.m_PrePoint.y);
        this.m_PrePoint.x = (int) historyX;
        this.m_PrePoint.y = (int) historyY;
    }

    public boolean onTouchEventUp(MotionEvent event) {
        this.m_PrePoint.x = 0;
        this.m_PrePoint.y = 0;
        DHWR.EndStroke();
        startRecognize();
        return true;
    }

    public boolean SetRecogMode(int nMode) {
        this.m_nMode = nMode;
        this.m_nNumOfMode = 0;
        switch (nMode) {
            case 0:
                this.m_arMode[this.m_nNumOfMode][0] = 0;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_UPPERCASE | DHWR.DTYPE_LOWERCASE;
                this.m_nNumOfMode++;
                break;
            case 1:
                this.m_arMode[this.m_nNumOfMode][0] = 103;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_NONE;
                this.m_nNumOfMode++;
                break;
            case 2:
                this.m_arMode[this.m_nNumOfMode][0] = 112;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_NONE;
                this.m_nNumOfMode++;
                break;
            case 3:
                this.m_arMode[this.m_nNumOfMode][0] = 113;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_NONE;
                this.m_nNumOfMode++;
                break;
            case 4:
                this.m_arMode[this.m_nNumOfMode][0] = 101;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_WANSUNG | DHWR.DTYPE_JOHAP | DHWR.DTYPE_CONSONANT;
                this.m_nNumOfMode++;
                break;
            case 5:
                this.m_arMode[this.m_nNumOfMode][0] = 102;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_NONE;
                this.m_nNumOfMode++;
                break;
            case 6:
                this.m_arMode[this.m_nNumOfMode][0] = 130;
                this.m_arMode[this.m_nNumOfMode][1] = DHWR.DTYPE_NONE;
                this.m_nNumOfMode++;
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean processRecognize() {
        this.m_pCands[0] = 10;
        DHWR.GetInkCount(this.m_pInkCount);
        this.m_errorcode = DHWR.SetAttribute(0, this.m_arMode, this.m_nNumOfMode, this.m_pCands);
        Arrays.fill(this.m_arResult, ' ');
        this.m_errorcode = DHWR.Recognize(this.m_arResult);
        makeResultString(this.m_arResult);
        Clear();
        return this.m_errorcode == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String makeResultString(char[] mArResult) {
        String strResult = new String(mArResult);
        for (int nPos = 0; nPos < strResult.length(); nPos++) {
            if (strResult.charAt(nPos) == 0) {
                String retString = strResult.substring(0, nPos);
                return retString;
            }
        }
        return null;
    }

    private void AddPoints(short x, short y) {
        DHWR.AddPoint(x, y);
    }

    private void clearCanvas() {
        if (this.mCanvas != null) {
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    public int getRecogMode() {
        return this.m_nMode;
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int curW = this.mBitmap != null ? this.mBitmap.getWidth() : 0;
        int curH = this.mBitmap != null ? this.mBitmap.getHeight() : 0;
        if (curW < w || curH < h) {
            if (curW < w) {
                curW = w;
            }
            if (curH < h) {
                curH = h;
            }
            Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (this.mBitmap != null) {
                newCanvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
            }
            this.mBitmap = newBitmap;
            this.mCanvas = newCanvas;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
        }
        if (this.mRect.left != getLeft() || this.mRect.top != getTop() || this.mRect.right != getRight() || this.mRect.bottom != getBottom()) {
            HWLibSetWritingArea(getLeft(), getTop(), getRight(), getBottom(), 50);
        }
    }

    private void Invalidate(float sx, float sy, float ex, float ey, int penThickness) {
        float dimetion = penThickness / 2.0f;
        Rect invalidRect = new Rect();
        int rectR = getRight();
        int rectL = getLeft();
        int rectT = getTop();
        int rectB = getBottom();
        if (sx == ex && sy == ey) {
            invalidRect.set((int) (sx - dimetion), (int) (sy - dimetion), (int) (sx + dimetion), (int) (sy + dimetion));
        } else {
            invalidRect.left = sx < ex ? (int) (sx - dimetion) : (int) (ex - dimetion);
            invalidRect.right = sx < ex ? (int) (ex + dimetion) : (int) (sx + dimetion);
            invalidRect.top = sy < ey ? (int) (sy - dimetion) : (int) (ey - dimetion);
            invalidRect.bottom = sy < ey ? (int) (ey + dimetion) : (int) (sy + dimetion);
        }
        if (invalidRect.left < 0) {
            invalidRect.left = rectL;
        }
        if (invalidRect.right > rectR) {
            invalidRect.right = rectR;
        }
        if (invalidRect.top < 0) {
            invalidRect.top = rectT;
        }
        if (invalidRect.bottom > rectB) {
            invalidRect.bottom = rectB;
        }
        invalidate(invalidRect);
    }

    private void onDrawLineImprove(float sx, float sy, float x, float y) {
        Paint paint = this.mLinePaint;
        Canvas canvas = this.mCanvas;
        if (paint != null && canvas != null) {
            paint.setAntiAlias(true);
            paint.setStrokeWidth(10.0f);
            if (x == sx && y == sy) {
                canvas.drawCircle(x, y, 5.0f, paint);
            } else {
                canvas.drawCircle(x, y, 5.0f, paint);
                paint.setStrokeWidth(10.0f);
                canvas.drawLine(sx, sy, x, y, paint);
            }
            Invalidate(sx, sy, x, y, 10);
        }
    }

    private void drawLine(float x, float y, float x2, float y2) {
        onDrawLineImprove(x, y, x2, y2);
    }

    public void setCandidateTextView(LinearLayout layout, TextView[] candidateTV) {
        this.m_CandidateLayout = layout;
    }

    public void setResultCabllback(RecognizeResultCallback callback) {
        this.mRecognizeResultCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class HandlerRunnable implements Runnable {
        HandlerRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (RecognizeView.this.processRecognize()) {
                switch (RecognizeView.this.m_nMode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        String result = RecognizeView.this.makeResultString(RecognizeView.this.getszarResult());
                        if (result != null && RecognizeView.this.mRecognizeResultCallback != null) {
                            RecognizeView.this.mRecognizeResultCallback.setResult(result);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void startRecognize() {
        int RecogTime = DictUtils.getRecogTimeFromPreference(this.mContext);
        if (RecogTime < 300) {
            RecogTime = DictUtils.DIODICT_SETTING_PREF_RECOG_TIME_DEFAULT_VALUE;
        }
        this.m_RunnableHandle = new HandlerRunnable();
        this.m_Handler.postDelayed(this.m_RunnableHandle, RecogTime);
    }

    public void endRecognize() {
        this.m_Handler.removeCallbacks(this.m_RunnableHandle);
        this.m_RunnableHandle = null;
    }

    public void showHideCandidateWindow(boolean bShow) {
        if (bShow) {
            this.m_CandidateLayout.setVisibility(View.VISIBLE);
        } else {
            this.m_CandidateLayout.setVisibility(View.GONE);
        }
    }

    public void setCandidateClickListener(View.OnClickListener cl) {
    }

    public void onDestroy() {
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        if (this.mCanvas != null) {
            this.mCanvas = null;
        }
        if (this.mRecognizeResultCallback != null) {
            this.mRecognizeResultCallback = null;
        }
        Clear();
        System.gc();
    }
}
