package com.diotek.diodict.uitool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class PinchImageView extends ImageView implements View.OnTouchListener {
    public static final int GROW = 0;
    public static final float MAX_SCALE = 4.0f;
    public static final float MIN_SCALE = 1.0f;
    public static final int SHRINK = 1;
    public static final int TOUCH_INTERVAL = 0;
    public static final float ZOOM = 1.5f;
    float distCur;
    float distDelta;
    float distPre;
    ImageView im;
    Matrix mBaseMatrix;
    Bitmap mBitmap;
    int mHeight;
    long mLastGestureTime;
    float mPredx;
    float mPredy;
    Matrix mResultMatrix;
    Matrix mSuppMatrix;
    int mTouchSlop;
    int mWidth;
    float xCur;
    float xPre;
    float xSec;
    float yCur;
    float yPre;
    float ySec;

    public PinchImageView(Context context, AttributeSet attr) {
        super(context, attr);
        this.im = null;
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mResultMatrix = new Matrix();
        this.mBitmap = null;
        this.distPre = -1.0f;
        this.mPredx = 0.0f;
        this.mPredy = 0.0f;
        _init();
    }

    public PinchImageView(Context context) {
        super(context);
        this.im = null;
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mResultMatrix = new Matrix();
        this.mBitmap = null;
        this.distPre = -1.0f;
        this.mPredx = 0.0f;
        this.mPredy = 0.0f;
        _init();
    }

    public PinchImageView(ImageView im) {
        super(im.getContext());
        this.im = null;
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mResultMatrix = new Matrix();
        this.mBitmap = null;
        this.distPre = -1.0f;
        this.mPredx = 0.0f;
        this.mPredy = 0.0f;
        _init();
        this.im = im;
        this.im.setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    public float getScale(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[0];
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }
        this.mBitmap = bm;
        center(true, true);
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mWidth = right - left;
        this.mHeight = bottom - top;
        if (this.mBitmap != null) {
            getProperBaseMatrix(this.mBaseMatrix);
            setImageMatrix(getImageViewMatrix());
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int mode;
        int action = event.getAction() & 255;
        int p_count = event.getPointerCount();
        switch (action) {
            case 0:
            case 1:
                this.xPre = event.getX(0);
                this.yPre = event.getY(0);
                this.distPre = -1.0f;
                this.mLastGestureTime = SystemClock.uptimeMillis();
                break;
            case 2:
                this.xCur = event.getX(0);
                this.yCur = event.getY(0);
                if (p_count > 1) {
                    this.xSec = event.getX(1);
                    this.ySec = event.getY(1);
                    this.distCur = (float) Math.sqrt(Math.pow(this.xSec - this.xCur, 2.0d) + Math.pow(this.ySec - this.yCur, 2.0d));
                    this.distDelta = this.distPre > -1.0f ? this.distCur - this.distPre : 0.0f;
                    float scale = getScale();
                    long now = SystemClock.uptimeMillis();
                    if (Math.abs(this.distDelta) > this.mTouchSlop) {
                        this.mLastGestureTime = 0L;
                        if (this.distDelta > 0.0f) {
                            mode = 0;
                        } else {
                            mode = this.distCur == this.distPre ? 2 : 1;
                        }
                        switch (mode) {
                            case 0:
                                zoomIn(scale);
                                break;
                            case 1:
                                zoomOut(scale);
                                break;
                        }
                        this.mLastGestureTime = now;
                        this.xPre = this.xCur;
                        this.yPre = this.yCur;
                        this.distPre = this.distCur;
                        return true;
                    }
                } else {
                    float nMoveX = this.xCur - this.xPre;
                    float nMoveY = this.yCur - this.yPre;
                    postTranslate(nMoveX, nMoveY);
                    setImageMatrix(getImageViewMatrix());
                }
                this.xPre = this.xCur;
                this.yPre = this.yCur;
                this.distPre = this.distCur;
                break;
        }
        return true;
    }

    private void _init() {
        this.im = this;
        this.mTouchSlop = ViewConfiguration.getTouchSlop();
        this.im.setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void zoomMax() {
        zoomTo(4.0f);
    }

    public void zoomMin() {
        zoomTo(1.0f);
    }

    public synchronized void postTranslate(float dx, float dy) {
        this.mSuppMatrix.postTranslate(dx, dy);
    }

    public void center(boolean horizontal, boolean vertical) {
        if (this.mBitmap != null) {
            Matrix m = getImageViewMatrix();
            RectF rect = new RectF(0.0f, 0.0f, this.mBitmap.getWidth(), this.mBitmap.getHeight());
            m.mapRect(rect);
            rect.left = 0.0f;
            rect.right = this.mBitmap.getWidth();
            float height = rect.height();
            float width = rect.width();
            float deltaX = 0.0f;
            float deltaY = 0.0f;
            if (vertical) {
                int viewHeight = getHeight();
                if (height < viewHeight) {
                    deltaY = ((viewHeight - height) / 2.0f) - rect.top;
                } else if (rect.top > 0.0f) {
                    deltaY = -rect.top;
                } else if (rect.bottom < viewHeight) {
                    deltaY = getHeight() - rect.bottom;
                }
            }
            if (horizontal) {
                int viewWidth = getWidth();
                if (width < viewWidth) {
                    deltaX = ((viewWidth - width) / 2.0f) - rect.left;
                } else if (rect.left > 0.0f) {
                    deltaX = -rect.left;
                } else if (rect.right < viewWidth) {
                    deltaX = viewWidth - rect.right;
                }
            }
            postTranslate(deltaX, deltaY);
            setImageMatrix(getImageViewMatrix());
        }
    }

    protected Matrix getImageViewMatrix() {
        this.mResultMatrix.set(this.mBaseMatrix);
        this.mResultMatrix.postConcat(this.mSuppMatrix);
        return this.mResultMatrix;
    }

    protected void zoomTo(float scale) {
        this.mSuppMatrix.setScale(scale, scale, getWidth() / 2.0f, getHeight() / 2.0f);
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    protected void zoomIn(float scale) {
        if (scale <= 4.0f) {
            this.mSuppMatrix.postScale(1.5f, 1.5f, getWidth() / 2.0f, getHeight() / 2.0f);
            setImageMatrix(getImageViewMatrix());
            center(true, true);
        }
    }

    protected void zoomOut(float scale) {
        if (scale >= 1.0f) {
            float cx = getWidth() / 2.0f;
            float cy = getHeight() / 2.0f;
            Matrix tmp = new Matrix(this.mSuppMatrix);
            tmp.postScale(0.6666667f, 0.6666667f, cx, cy);
            if (getScale(tmp) < 1.0f) {
                this.mSuppMatrix.setScale(1.0f, 1.0f, cx, cy);
            } else {
                this.mSuppMatrix.postScale(0.6666667f, 0.6666667f, cx, cy);
            }
            setImageMatrix(getImageViewMatrix());
            center(true, true);
        }
    }

    private void getProperBaseMatrix(Matrix matrix) {
        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float w = this.mBitmap.getWidth();
        float h = this.mBitmap.getHeight();
        matrix.reset();
        float widthScale = Math.min(viewWidth / w, 4.0f);
        float heightScale = Math.min(viewHeight / h, 4.0f);
        float scale = Math.min(widthScale, heightScale);
        Matrix bitmapMatrix = new Matrix();
        bitmapMatrix.preTranslate(-(this.mBitmap.getWidth() >> 1), -(this.mBitmap.getHeight() >> 1));
        bitmapMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
        matrix.postConcat(bitmapMatrix);
        matrix.postScale(scale, scale);
        matrix.postTranslate((viewWidth - (w * scale)) / 2.0f, (viewHeight - (h * scale)) / 2.0f);
    }
}
