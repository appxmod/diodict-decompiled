package com.nemustech.tiffany.world;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.nemustech.tiffany.world.TFAnimation;
import com.nemustech.tiffany.world.TFFlexiblePanel;
import com.nemustech.tiffany.world.TFPagePanel;
import com.nemustech.tiffany.world.TFWorld;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class TFEffect {
    private static final int STATUS_BAR_BOTTOM = 2;
    private static final int STATUS_BAR_NONE = 0;
    private static final int STATUS_BAR_TOP = 1;
    static final String TAG = "TFEffect";
    private float cameraZ;
    private boolean mActive;
    private Window mActivityWindow;
    private AnimationEventListener mAnimationEventListener;
    private Drawable mBD;
    private boolean mBlending;
    private boolean mChildViewUseFull;
    private Context mContext;
    private Bitmap mCoverImage;
    private FrameLayout mDecorLayout;
    private boolean mDepthTest;
    OnDialogBitmapListener mDialogBitmapListener;
    private boolean mDragMode;
    private boolean mEconomic;
    private int mEffectFinishCnt;
    private TFWorld.OnEffectFinishListener mEffectFinishListener;
    private int mEffectKind;
    private TFWindow mEffectWindow;
    private int mEffectWindowCleaningCnt;
    private int mEndIndex;
    private boolean mOneWayAnimation;
    private Drawable mPFBD;
    private TFPagePanel mPageCurlPage;
    private int mParam1;
    private int mParam2;
    TFObject mProcessModel;
    private boolean mReflectingFloorExist;
    private boolean mReverse;
    private int mStartIndex;
    private long mStartTick;
    private int mStatusBarHeight;
    private int mStatusBarStatus;
    private TFView mSurfaceView;
    private int mTitleHeight;
    private boolean mTranslucent;
    private ViewGroup mUIViewParent;
    private float mUnitPanelHeight;
    private float mUnitPanelWidth;
    private int mViewHeight;
    private int mViewOffsetX;
    private int mViewOffsetY;
    private int mViewWidth;
    private WindowManager mWindowMgr;
    private TFWorld mWorld;
    private Runnable mEffect2DAnimationEnd = new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.17
        @Override // java.lang.Runnable
        public void run() {
            TFEffect.this.mDecorLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
            if (TFEffect.this.mChildView[TFEffect.this.mStartIndex] != null && TFEffect.this.mChildView[TFEffect.this.mEndIndex] != null) {
                TFEffect.this.mChildView[TFEffect.this.mEndIndex].bringToFront();
                TFEffect.this.mChildView[TFEffect.this.mEndIndex].setVisibility(View.VISIBLE);
                TFEffect.this.mChildView[TFEffect.this.mStartIndex].setVisibility(View.INVISIBLE);
            }
            TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.17.1
                @Override // java.lang.Runnable
                public void run() {
                    TFEffect.this.mDecorLayout.removeView(TFEffect.this.mChild2DView[TFEffect.this.mStartIndex]);
                    TFEffect.this.mDecorLayout.removeView(TFEffect.this.mChild2DView[TFEffect.this.mEndIndex]);
                    TFEffect.this.mWindowMgr.removeView(TFEffect.this.mDecorLayout);
                }
            });
        }
    };
    private View.OnKeyListener mSurfaceViewKeyListener = new View.OnKeyListener() { // from class: com.nemustech.tiffany.world.TFEffect.32
        @Override // android.view.View.OnKeyListener
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            float[] position = new float[3];
            float[] angle = new float[3];
            TFEffect.this.mProcessModel.getLocation(position);
            TFEffect.this.mProcessModel.getAngle(angle);
            switch (keyCode) {
                case 19:
                    TFObject tFObject = TFEffect.this.mProcessModel;
                    float f = angle[0];
                    float f2 = angle[1] - 1.0f;
                    angle[1] = f2;
                    tFObject.rotate(f, f2, 1.0f, 2);
                    break;
                case 20:
                    TFObject tFObject2 = TFEffect.this.mProcessModel;
                    float f3 = angle[0];
                    float f4 = angle[1] + 1.0f;
                    angle[1] = f4;
                    tFObject2.rotate(f3, f4, 1.0f, 2);
                    break;
                case 21:
                    TFObject tFObject3 = TFEffect.this.mProcessModel;
                    float f5 = angle[0] + 1.0f;
                    angle[0] = f5;
                    tFObject3.rotate(f5, angle[1], 1.0f, 2);
                    break;
                case 22:
                    TFObject tFObject4 = TFEffect.this.mProcessModel;
                    float f6 = angle[0] - 1.0f;
                    angle[0] = f6;
                    tFObject4.rotate(f6, angle[1], 1.0f, 2);
                    break;
                case 29:
                    TFObject tFObject5 = TFEffect.this.mProcessModel;
                    float f7 = (float) (position[0] - 0.001d);
                    position[0] = f7;
                    tFObject5.locate(f7, position[1], position[2]);
                    break;
                case 32:
                    TFObject tFObject6 = TFEffect.this.mProcessModel;
                    float f8 = (float) (position[0] + 0.001d);
                    position[0] = f8;
                    tFObject6.locate(f8, position[1], position[2]);
                    break;
                case 34:
                    TFObject tFObject7 = TFEffect.this.mProcessModel;
                    float f9 = (float) (position[0] - 0.01d);
                    position[0] = f9;
                    tFObject7.locate(f9, position[1], position[2]);
                    break;
                case 35:
                    TFObject tFObject8 = TFEffect.this.mProcessModel;
                    float f10 = position[0];
                    float f11 = (float) (position[1] - 0.01d);
                    position[1] = f11;
                    tFObject8.locate(f10, f11, position[2]);
                    break;
                case 36:
                    TFObject tFObject9 = TFEffect.this.mProcessModel;
                    float f12 = (float) (position[0] + 0.01d);
                    position[0] = f12;
                    tFObject9.locate(f12, position[1], position[2]);
                    break;
                case 39:
                    TFObject tFObject10 = TFEffect.this.mProcessModel;
                    float f13 = position[0];
                    float f14 = position[1];
                    float f15 = (float) (position[2] - 0.01d);
                    position[2] = f15;
                    tFObject10.locate(f13, f14, f15);
                    break;
                case 40:
                    TFObject tFObject11 = TFEffect.this.mProcessModel;
                    float f16 = position[0];
                    float f17 = position[1];
                    float f18 = (float) (position[2] + 0.01d);
                    position[2] = f18;
                    tFObject11.locate(f16, f17, f18);
                    break;
                case 41:
                    TFObject tFObject12 = TFEffect.this.mProcessModel;
                    float f19 = position[0];
                    float f20 = position[1];
                    float f21 = (float) (position[2] + 0.1d);
                    position[2] = f21;
                    tFObject12.locate(f19, f20, f21);
                    break;
                case 42:
                    TFObject tFObject13 = TFEffect.this.mProcessModel;
                    float f22 = position[0];
                    float f23 = position[1];
                    float f24 = (float) (position[2] - 0.1d);
                    position[2] = f24;
                    tFObject13.locate(f22, f23, f24);
                    break;
                case 43:
                    TFObject tFObject14 = TFEffect.this.mProcessModel;
                    float f25 = position[0];
                    float f26 = position[1];
                    float f27 = (float) (position[2] - 0.001d);
                    position[2] = f27;
                    tFObject14.locate(f25, f26, f27);
                    break;
                case 44:
                    TFObject tFObject15 = TFEffect.this.mProcessModel;
                    float f28 = position[0];
                    float f29 = position[1];
                    float f30 = (float) (position[2] + 0.001d);
                    position[2] = f30;
                    tFObject15.locate(f28, f29, f30);
                    break;
                case DHWR.DLANG_OROMO /* 47 */:
                    TFObject tFObject16 = TFEffect.this.mProcessModel;
                    float f31 = position[0];
                    float f32 = (float) (position[1] - 0.001d);
                    position[1] = f32;
                    tFObject16.locate(f31, f32, position[2]);
                    break;
                case DHWR.DLANG_SOTHO_S /* 48 */:
                    TFObject tFObject17 = TFEffect.this.mProcessModel;
                    float f33 = position[0];
                    float f34 = (float) (position[1] + 0.01d);
                    position[1] = f34;
                    tFObject17.locate(f33, f34, position[2]);
                    break;
                case DHWR.DLANG_SWATI /* 51 */:
                    TFObject tFObject18 = TFEffect.this.mProcessModel;
                    float f35 = position[0];
                    float f36 = (float) (position[1] + 0.001d);
                    position[1] = f36;
                    tFObject18.locate(f35, f36, position[2]);
                    break;
            }
            Log.d(TFEffect.TAG, "x=" + position[0] + " y=" + position[1] + " z=" + position[2] + " degreeX=" + angle[0] + " degreeY=" + angle[1]);
            return false;
        }
    };
    public Interpolator mEffectInterpolator = null;
    private Canvas mCanvas = null;
    private boolean mStopping = false;
    private boolean mTouchableMode = true;
    protected TwistParam mTwistParam = null;
    private View[] mChildView = new View[2];
    private ImageView[] mChild2DView = new ImageView[2];
    private Bitmap[] mBitmap = new Bitmap[2];
    private Bitmap[] mBackBitmap = new Bitmap[2];
    private Handler mHandler = new Handler();
    private boolean[] mRecycleSafe = new boolean[2];

    /* loaded from: classes.dex */
    public interface AnimationEventListener {
        void onAnimationEnd(int i, boolean z);

        void onAnimationStart(int i, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnDialogBitmapListener {
        void onDialogBitmap(Bitmap bitmap);
    }

    static /* synthetic */ int access$1908(TFEffect x0) {
        int i = x0.mEffectFinishCnt;
        x0.mEffectFinishCnt = i + 1;
        return i;
    }

    /* loaded from: classes.dex */
    class Status {
        public static final int ACTIVE = 1;
        public static final int DONE = 2;
        public static final int NONE = 0;

        Status() {
        }
    }

    /* loaded from: classes.dex */
    public class Model {
        public static final int DISAPPEAR_ROLLING_UP = 1;
        public static final int ZOOM_IN_FACING_FRONT = 0;

        public Model() {
        }
    }

    /* loaded from: classes.dex */
    public class Transition {
        public static final int ASKEW = 5;
        public static final int CENTER_ZOOM = 18;
        public static final int COLUMN = 6;
        public static final int CUBE = 9;
        public static final int DIAGONAL_FLING = 14;
        public static final int DIAGONAL_SCALE = 15;
        public static final int EFFECT_MAX = 23;
        public static final int ENTRANCE = 4;
        public static final int FLIP = 0;
        public static final int GENIE = 20;
        public static final int LINE_ZOOM = 19;
        public static final int MOSAIC = 1;
        public static final int PAGEOVER_BY_ANGLE = 11;
        public static final int PAGE_CURL = 21;
        public static final int PAGE_CURL_TRANS = 22;
        public static final int PROJECTOR_BOARD_DOWN = 17;
        public static final int PROJECTOR_BOARD_UP = 16;
        public static final int REPLACE = 2;
        public static final int REVOLVING = 3;
        public static final int ROW = 7;
        public static final int SINK = 12;
        public static final int STICKER = 13;
        public static final int TWIST = 8;
        public static final int VPAGEOVER = 10;

        public Transition() {
        }
    }

    public void setReflectingFloor(boolean bExist) {
        this.mReflectingFloorExist = bExist;
    }

    public TFEffect(Context context) {
        long startTick = System.currentTimeMillis();
        Log.d(TAG, "TFEffect creation start on " + startTick);
        this.mWindowMgr = (WindowManager) context.getSystemService("window");
        this.mActivityWindow = ((Activity) context).getWindow();
        this.mEffectWindow = new TFWindow(context);
        this.mEffectWindow.setAttributes(this.mActivityWindow.getAttributes());
        this.mDecorLayout = (FrameLayout) this.mEffectWindow.getDecorView();
        this.mChild2DView[0] = new ImageView(context);
        this.mChild2DView[1] = new ImageView(context);
        this.mContext = context;
        init();
        TFUtils.loadLibrary();
        System.gc();
        TFJniUtils.verifyContext(context);
        long duration = System.currentTimeMillis() - startTick;
        Log.d(TAG, "TFEffect created taking " + duration + " !!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void addView(View child, int index) {
        addView(child, null, index);
    }

    public void addView(View child, Bitmap bmp, int index) {
        if (index == 0 || index == 1) {
            this.mChildView[index] = child;
            if (child != null && index == 0) {
                this.mUIViewParent = (ViewGroup) child.getRootView();
            }
            if (bmp == null) {
                if (child != null) {
                    child.setDrawingCacheEnabled(true);
                    Bitmap cachedBitmap = child.getDrawingCache();
                    this.mBitmap[index] = Bitmap.createBitmap(cachedBitmap);
                    this.mRecycleSafe[index] = cachedBitmap != this.mBitmap[index];
                } else {
                    return;
                }
            } else {
                this.mRecycleSafe[index] = false;
                this.mBitmap[index] = bmp;
            }
            syncBitmaps();
        }
    }

    private void syncBitmaps() {
        if (this.mBitmap[0] != null && this.mBitmap[1] != null) {
            int lowerHeight = this.mBitmap[0].getHeight() <= this.mBitmap[1].getHeight() ? this.mBitmap[0].getHeight() : this.mBitmap[1].getHeight();
            for (int i = 0; i < 2; i++) {
                if (this.mBitmap[i].getHeight() > lowerHeight) {
                    Bitmap newBitmap = Bitmap.createBitmap(this.mBitmap[i], 0, this.mBitmap[i].getHeight() - lowerHeight, this.mBitmap[i].getWidth(), lowerHeight);
                    this.mBitmap[i] = newBitmap;
                    this.mRecycleSafe[i] = true;
                }
            }
        }
    }

    public void init() {
        this.mCoverImage = null;
        for (int i = 0; i < 2; i++) {
            if (this.mBitmap[i] != null) {
                if (this.mRecycleSafe[i] && !this.mBitmap[i].isRecycled()) {
                    this.mBitmap[i].recycle();
                }
                this.mBitmap[i] = null;
            }
            this.mChildView[i] = null;
            this.mBackBitmap[i] = null;
            this.mRecycleSafe[i] = false;
        }
        this.mParam1 = 0;
        this.mParam2 = 0;
        this.mEffectInterpolator = null;
        setReflectingFloor(true);
        setTranslucentMode(false);
        this.mDepthTest = true;
        this.mDragMode = false;
        this.mEconomic = false;
        this.mBlending = false;
        this.mEffectFinishListener = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prvOnFinishEffect(TFModel[] modelsToDetach) {
        Log.v(TAG, "Transition effect ends.");
        this.mEffectFinishCnt = 0;
        if (this.mAnimationEventListener != null) {
            this.mAnimationEventListener.onAnimationEnd(this.mEffectKind, this.mReverse);
        }
        this.mWorld.setRenderAffinity(2);
        if (this.mChildView[this.mStartIndex] != null && this.mChildView[this.mEndIndex] != null && !this.mTranslucent) {
            this.mChildView[this.mEndIndex].bringToFront();
            this.mChildView[this.mEndIndex].setVisibility(View.VISIBLE);
            this.mChildView[this.mStartIndex].setVisibility(View.INVISIBLE);
        }
        this.mHandler.post(new AnonymousClass1(modelsToDetach));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.nemustech.tiffany.world.TFEffect$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ TFModel[] val$modelsToDetach;

        AnonymousClass1(TFModel[] tFModelArr) {
            this.val$modelsToDetach = tFModelArr;
        }

        @Override // java.lang.Runnable
        public void run() {
            TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.1.1
                @Override // java.lang.Runnable
                public void run() {
                    for (int i = 0; i < 2; i++) {
                        if (TFEffect.this.mBitmap[i] != null && TFEffect.this.mRecycleSafe[i]) {
                            TFEffect.this.mBitmap[i].recycle();
                        }
                    }
                    if (AnonymousClass1.this.val$modelsToDetach != null) {
                        for (int i2 = 0; i2 < AnonymousClass1.this.val$modelsToDetach.length; i2++) {
                            AnonymousClass1.this.val$modelsToDetach[i2].deleteAllImageResource();
                        }
                    }
                    TFEffect.this.mDecorLayout.removeView(TFEffect.this.mSurfaceView);
                    if (TFEffect.this.mDecorLayout.getParent() != null) {
                        TFEffect.this.mWindowMgr.removeView(TFEffect.this.mDecorLayout);
                    }
                    TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TFEffect.this.mActive = false;
                            TFEffect.this.mWorld = null;
                            TFEffect.this.mStopping = false;
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prvOnFinishEffect(TFModel modelToDetach) {
        TFModel[] modelsToDetach = {modelToDetach};
        prvOnFinishEffect(modelsToDetach);
    }

    public void setCoverImage(Bitmap coverImage) {
        this.mCoverImage = coverImage;
    }

    public void setViewOffset(int offsetX, int offsetY) {
        this.mViewOffsetX = offsetX;
        this.mViewOffsetY = offsetY;
    }

    public void setBackImage(Bitmap backImage, int index) {
        this.mBackBitmap[index] = backImage;
    }

    public void setTranslucentMode(boolean mode) {
        this.mTranslucent = mode;
    }

    public void setEffectParam(int param1, int param2) {
        setEffectParam(param1, param2, null);
    }

    public void setEffectParam(int param1, int param2, Interpolator interpolator) {
        this.mParam1 = param1;
        this.mParam2 = param2;
        this.mEffectInterpolator = interpolator;
    }

    private boolean is2DEffect(int effect) {
        return effect >= 14 && effect <= 19;
    }

    public void stop() {
        if (this.mStopping) {
            Log.w(TAG, "TFEffect.stop() called but previous action is working");
            return;
        }
        this.mStopping = true;
        if (this.mWorld == null) {
            Log.w(TAG, "TFEffect.stop() called but there was no tiffany world.");
            return;
        }
        int count = 0;
        int numLayer = this.mWorld.getLayerCount();
        for (int l = 0; l < numLayer; l++) {
            LinkedList<TFObject> layer = this.mWorld.getLayer(l);
            Iterator i$ = layer.iterator();
            while (i$.hasNext()) {
                TFObject o = (TFObject) i$.next();
                if (o instanceof TFHolder) {
                    count += ((TFHolder) o).getSlotCount();
                } else {
                    count++;
                }
            }
        }
        int index = 0;
        TFModel[] modelsToBeDetached = new TFModel[count];
        for (int l2 = 0; l2 < numLayer; l2++) {
            LinkedList<TFObject> layer2 = this.mWorld.getLayer(l2);
            Iterator i$2 = layer2.iterator();
            while (i$2.hasNext()) {
                TFObject o2 = (TFObject) i$2.next();
                if (o2 instanceof TFHolder) {
                    TFHolder h = (TFHolder) o2;
                    int count2 = h.getSlotCount();
                    for (int j = 0; j < count2; j++) {
                        modelsToBeDetached[index] = h.getModel(j);
                        index++;
                    }
                } else {
                    modelsToBeDetached[index] = (TFModel) o2;
                    index++;
                }
            }
        }
        prvOnFinishEffect(modelsToBeDetached);
    }

    public void showEffect(int effect, boolean bReversed) {
        Rect visibleFrame = new Rect();
        Window activityWindow = ((Activity) this.mContext).getWindow();
        activityWindow.getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
        int deviceHeight = this.mContext.getResources().getDisplayMetrics().heightPixels;
        if ((this.mEffectWindow.getAttributes().flags & 1024) != 0) {
            visibleFrame.top = 0;
        }
        this.mTitleHeight = activityWindow.findViewById(16908290).getTop() - visibleFrame.top;
        if (visibleFrame.height() == deviceHeight) {
            this.mStatusBarHeight = 0;
            this.mStatusBarStatus = 0;
        } else if (visibleFrame.top == 0) {
            this.mStatusBarHeight = deviceHeight - visibleFrame.height();
            this.mStatusBarStatus = 2;
        } else {
            this.mStatusBarHeight = visibleFrame.top;
            this.mStatusBarStatus = 1;
        }
        Log.i(TAG, "Status bar : " + this.mStatusBarHeight + " title:" + this.mTitleHeight);
        this.mViewWidth = visibleFrame.width();
        this.mViewHeight = visibleFrame.height() - this.mTitleHeight;
        if (is2DEffect(effect)) {
            showEffect2D(effect, bReversed);
        } else {
            showEffect3D(effect, bReversed);
        }
    }

    public void showEffect2D(int effect, boolean bReversed) {
        this.mEffectWindowCleaningCnt = 0;
        if (bReversed) {
            this.mStartIndex = 1;
            this.mEndIndex = 0;
        } else {
            this.mStartIndex = 0;
            this.mEndIndex = 1;
        }
        this.mChild2DView[0].setImageBitmap(this.mBitmap[this.mStartIndex]);
        this.mChild2DView[1].setImageBitmap(this.mBitmap[this.mEndIndex]);
        this.mDecorLayout.setBackgroundColor(Color.argb(255, 0, 0, 0));
        this.mDecorLayout.addView(this.mChild2DView[0]);
        this.mDecorLayout.addView(this.mChild2DView[1]);
        switch (effect) {
            case 14:
                showEffectDiagonalFling(bReversed);
                break;
            case 15:
                showEffectDiagonalScale(bReversed);
                break;
            case 16:
                showEffectProjectorBoardUp(bReversed);
                break;
            case 17:
                showEffectProjectorBoardDown(bReversed);
                break;
            case 18:
                showEffectCenterZoom(bReversed);
                break;
            case 19:
                showEffectLineZoom(bReversed);
                break;
        }
        setAnimationWindow();
    }

    public void showEffect3D(int effect, boolean bReversed) {
        if (!this.mActive) {
            this.mEffectKind = effect;
            this.mReverse = bReversed;
            this.mOneWayAnimation = false;
            if (this.mReverse) {
                this.mStartIndex = 1;
                this.mEndIndex = 0;
            } else {
                this.mStartIndex = 0;
                this.mEndIndex = 1;
            }
            this.mActive = true;
            this.mStartTick = System.currentTimeMillis();
            Log.d(TAG, "showEffect start on " + this.mStartTick);
            if (this.mChildView[0].getWidth() == this.mViewWidth && this.mChildView[0].getHeight() == this.mViewHeight) {
                this.mChildViewUseFull = true;
            } else {
                this.mChildViewUseFull = false;
            }
            if (this.mCoverImage == null) {
                if (!this.mChildViewUseFull) {
                    this.mActivityWindow.getDecorView().setDrawingCacheEnabled(true);
                    Bitmap decorImage = null;
                    switch (this.mStatusBarStatus) {
                        case 0:
                        case 1:
                            decorImage = Bitmap.createBitmap(this.mActivityWindow.getDecorView().getDrawingCache(), 0, this.mStatusBarHeight + this.mTitleHeight, this.mViewWidth, this.mViewHeight);
                            break;
                        case 2:
                            decorImage = Bitmap.createBitmap(this.mActivityWindow.getDecorView().getDrawingCache(), 0, this.mTitleHeight, this.mViewWidth, this.mViewHeight);
                            break;
                    }
                    this.mBD = new BitmapDrawable(this.mContext.getResources(), decorImage);
                } else {
                    this.mBD = new BitmapDrawable(this.mContext.getResources(), this.mBitmap[this.mStartIndex]);
                }
            } else {
                this.mBD = new BitmapDrawable(this.mContext.getResources(), this.mCoverImage);
            }
            this.mSurfaceView = new TFView(this.mContext);
            this.mSurfaceView.setBackgroundDrawable(this.mBD);
            this.mDecorLayout.addView(this.mSurfaceView);
            this.mWorld = new TFWorld(1.5f, 1.5f, 10.0f);
            this.mWorld.lock();
            Log.i(TAG, "view width:" + this.mViewWidth + " view height:" + this.mViewHeight);
            this.mUnitPanelWidth = 1.0f;
            this.mUnitPanelHeight = this.mViewHeight / this.mViewWidth;
            this.mWorld.setViewSizeChangeListener(new TFWorld.OnViewSizeChangeListener() { // from class: com.nemustech.tiffany.world.TFEffect.2
                @Override // com.nemustech.tiffany.world.TFWorld.OnViewSizeChangeListener
                public void onViewSizeChanged(int width, int height) {
                    Log.d(TFEffect.TAG, "View size changed to width:" + width + " height:" + height);
                    TFCamera camera = TFEffect.this.mWorld.getCamera();
                    float z = TFUtils.calcZ(TFEffect.this.mWorld, width, height, TFEffect.this.mViewWidth, TFEffect.this.mUnitPanelWidth);
                    camera.locate(2, -z, true);
                }
            });
            this.mWorld.setPostDrawListener(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.3
                @Override // java.lang.Runnable
                public void run() {
                    long duration = System.currentTimeMillis() - TFEffect.this.mStartTick;
                    Log.d(TFEffect.TAG, "showEffect preparation finished taking " + duration + " !!!!!!!!!!!!!!!!!!!!");
                    if (TFEffect.this.mAnimationEventListener != null) {
                        TFEffect.this.mAnimationEventListener.onAnimationStart(TFEffect.this.mEffectKind, TFEffect.this.mReverse);
                    }
                    if (TFEffect.this.mChildView[TFEffect.this.mStartIndex] != null) {
                        TFEffect.this.mChildView[TFEffect.this.mStartIndex].setVisibility(View.INVISIBLE);
                    }
                    if (TFEffect.this.mOneWayAnimation && TFEffect.this.mChildView[1] != null) {
                        TFEffect.this.mChildView[1].bringToFront();
                        TFEffect.this.mChildView[1].setVisibility(View.VISIBLE);
                    }
                    TFEffect.this.mSurfaceView.setBackgroundDrawable(null);
                }
            });
            switch (effect) {
                case 0:
                    showEffectFlip(bReversed);
                    break;
                case 1:
                    showEffectMosaic(bReversed);
                    break;
                case 2:
                    showEffectReplace();
                    break;
                case 3:
                    showEffectRevolvingDoor();
                    break;
                case 4:
                    showEffectEntranceDoor();
                    break;
                case 5:
                    showEffectAskew();
                    break;
                case 6:
                    showEffectVerticalColumn(bReversed);
                    break;
                case 7:
                    showEffectHorizontalRow(bReversed);
                    break;
                case 8:
                    showEffectTwist(bReversed);
                    break;
                case 9:
                    showEffectHorizontalCube(bReversed);
                    break;
                case 10:
                    showEffectVerticalPageOver(bReversed);
                    break;
                case 11:
                    showEffectPageOver_PagePanel(bReversed, this.mParam1, this.mParam2);
                    break;
                case 12:
                    showEffectSink_FlexiblePanel(bReversed);
                    break;
                case 13:
                    showEffectPageOver_Sticker(bReversed, this.mParam1, this.mParam2);
                    break;
                case 20:
                    showEffectGenie_FlexiblePanel(bReversed);
                    break;
                case 21:
                    showEffectPageCurl(bReversed, this.mParam1, this.mParam2, this.mEffectInterpolator);
                    break;
                case 22:
                    showEffectPageCurlTrans(bReversed, this.mParam1, this.mParam2, this.mEffectInterpolator);
                    break;
            }
            setAnimationWindow();
            this.mWorld.banQueryingMatrix(true);
            this.mWorld.setTranslucentMode(this.mTranslucent);
            if (this.mTranslucent) {
                this.mWorld.setBackgroundColor(0.0f, 0.0f, 0.0f, 0.0f);
            }
            if (this.mReflectingFloorExist) {
                this.mWorld.addReflectingFloor((-this.mUnitPanelHeight) / 2.0f, 0.5f);
            } else {
                this.mWorld.removeReflectingFloor();
            }
            this.mWorld.setDepthTestMode(this.mDepthTest);
            this.mWorld.setEconomicMode(this.mEconomic);
            this.mWorld.setBlendingMode(this.mBlending);
            this.mWorld.show(this.mSurfaceView);
        }
    }

    private void setAnimationWindow() {
        if (this.mTouchableMode) {
            this.mEffectWindow.clearFlags(16);
        } else {
            this.mEffectWindow.addFlags(16);
        }
        WindowManager.LayoutParams wl = this.mEffectWindow.getAttributes();
        Log.i(TAG, "Window flags:" + wl.flags);
        switch (this.mStatusBarStatus) {
            case 0:
            case 1:
                wl.y = this.mStatusBarHeight + this.mTitleHeight;
                break;
            case 2:
                wl.y = this.mTitleHeight;
                break;
        }
        wl.height = this.mViewHeight;
        this.mWindowMgr.addView(this.mDecorLayout, wl);
    }

    public boolean isActive() {
        return this.mActive;
    }

    private void showEffectFlip(final boolean bReversed) {
        TFPanel panel = new TFPanel(this.mWorld, this.mUnitPanelWidth, this.mUnitPanelHeight);
        panel.setImageResource(0, this.mBitmap[this.mStartIndex], (Rect) null);
        panel.setImageResource(1, this.mBitmap[this.mEndIndex], (Rect) null);
        panel.move(0.0f, 0.0f, -1.5f, 300L);
        if (bReversed) {
            panel.rotate(-90.0f, 0.0f, 300L, 1, 1);
        } else {
            panel.rotate(90.0f, 0.0f, 300L, 0, 0);
        }
        panel.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.4
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                object.move(0.0f, 0.0f, 0.0f, 300L);
                if (bReversed) {
                    object.rotate(180.0f, 0.0f, 300L, 1, 1);
                } else {
                    object.rotate(180.0f, 0.0f, 300L, 0, 0);
                }
                object.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.4.1
                    @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
                    public void onEffectFinish(TFObject object2) {
                        TFEffect.this.prvOnFinishEffect((TFModel) object2);
                    }
                });
            }
        });
    }

    private void showEffectMosaic(boolean bReversed) {
        int MOSAIC_HORZ_NUM = this.mUnitPanelHeight >= this.mUnitPanelWidth ? 3 : 4;
        int MOSAIC_VERT_NUM = this.mUnitPanelHeight >= this.mUnitPanelWidth ? 4 : 3;
        final int TOTAL_COUNT = MOSAIC_HORZ_NUM * MOSAIC_VERT_NUM;
        int MOSAIC_WIDTH = this.mBitmap[0].getWidth() / MOSAIC_HORZ_NUM;
        int MOSAIC_HEIGHT = this.mBitmap[0].getHeight() / MOSAIC_VERT_NUM;
        float[] locX = new float[TOTAL_COUNT];
        float[] locY = new float[TOTAL_COUNT];
        final TFPanel[] panel = new TFPanel[TOTAL_COUNT];
        Rect rectMosaic = new Rect();
        this.mEffectFinishCnt = 0;
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.5
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.access$1908(TFEffect.this);
                if (TFEffect.this.mEffectFinishCnt == TOTAL_COUNT) {
                    TFEffect.this.prvOnFinishEffect(panel);
                }
            }
        });
        float panelWidth = this.mUnitPanelWidth / MOSAIC_HORZ_NUM;
        float panelHeight = this.mUnitPanelHeight / MOSAIC_VERT_NUM;
        for (int y = 0; y < MOSAIC_VERT_NUM; y++) {
            for (int x = 0; x < MOSAIC_HORZ_NUM; x++) {
                int index = (y * MOSAIC_HORZ_NUM) + x;
                panel[index] = new TFPanel(this.mWorld, panelWidth, panelHeight);
                rectMosaic.set(x * MOSAIC_WIDTH, y * MOSAIC_HEIGHT, (x * MOSAIC_WIDTH) + MOSAIC_WIDTH, (y * MOSAIC_HEIGHT) + MOSAIC_HEIGHT);
                panel[index].setImageResource(0, this.mBitmap[this.mStartIndex], rectMosaic);
                panel[index].setImageResource(1, this.mBitmap[this.mEndIndex], rectMosaic);
                locX[index] = (-(this.mUnitPanelWidth / 2.0f)) + (panelWidth / 2.0f) + (x * panelWidth);
                locY[index] = ((this.mUnitPanelHeight / 2.0f) - (panelHeight / 2.0f)) - (y * panelHeight);
                panel[index].locate(locX[index], locY[index], 0.0f);
                long waitTime = (long) (800.0d * Math.random());
                panel[index].setWait(waitTime);
                int direction = bReversed ? 1 : 0;
                panel[index].rotate(180.0f, 0.0f, 300L, direction, direction);
            }
        }
    }

    private void showEffectReplace() {
        TFCircularHolder circularRail = new TFCircularHolder(0.5f);
        circularRail.attachTo(this.mWorld);
        circularRail.setFadingEffect(false);
        circularRail.locate(0.0f, (-this.mUnitPanelHeight) / 2.0f, -0.5f);
        circularRail.setEndlessMode(true);
        final TFPanel[] panel = new TFPanel[2];
        for (int i = 0; i < 2; i++) {
            panel[i] = new TFPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
            panel[i].setFaceFront(true);
            circularRail.addModel(panel[i], i, false);
        }
        panel[0].setImageResource(0, this.mBitmap[this.mStartIndex], (Rect) null);
        panel[1].setImageResource(0, this.mBitmap[this.mEndIndex], (Rect) null);
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.6
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.this.prvOnFinishEffect(panel);
            }
        });
        circularRail.getMoveAnimation().setAnimationEventListener(new TFAnimation.AnimationEventListener() { // from class: com.nemustech.tiffany.world.TFEffect.7
            @Override // com.nemustech.tiffany.world.TFAnimation.AnimationEventListener
            public void onAnimationStart(TFAnimation animation) {
            }

            @Override // com.nemustech.tiffany.world.TFAnimation.AnimationEventListener
            public void onAnimationEnd(TFAnimation animation) {
                TFEffect.this.prvOnFinishEffect(panel);
            }
        });
        circularRail.setHeadItemIndex(circularRail.getHeadItemIndex() + 1, 500);
    }

    private void showEffectRevolvingDoor() {
        TFVerticalAxisHolder verticalAxis = new TFVerticalAxisHolder(0.0f, 90.0f);
        verticalAxis.attachTo(this.mWorld);
        verticalAxis.locate(-(this.mUnitPanelWidth / 2.0f), -(this.mUnitPanelHeight / 2.0f), 0.0f);
        verticalAxis.setOpenRangeAngle(240.0f);
        verticalAxis.setEndlessMode(true);
        final TFPanel[] p = new TFPanel[2];
        for (int i = 0; i < 2; i++) {
            p[i] = new TFPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
            verticalAxis.addModel(p[i], i, false);
        }
        p[0].setImageResource(0, this.mBitmap[this.mStartIndex], (Rect) null);
        p[1].setImageResource(0, this.mBitmap[this.mEndIndex], (Rect) null);
        verticalAxis.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.8
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.this.prvOnFinishEffect(p);
            }
        });
        verticalAxis.rotate(-90.0f, 0.0f, 1000L);
    }

    private void showEffectEntranceDoor() {
        this.mBlending = true;
        TFVerticalAxisHolder verticalAxisL = new TFVerticalAxisHolder(0.0f, 90.0f);
        TFVerticalAxisHolder verticalAxisR = new TFVerticalAxisHolder(0.0f, 270.0f);
        verticalAxisL.setFadingEffect(false);
        verticalAxisR.setFadingEffect(false);
        final TFPanel panelBack = new TFPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        final TFPanel panelFrontL = new TFPanel(this.mUnitPanelWidth / 2.0f, this.mUnitPanelHeight);
        final TFPanel panelFrontR = new TFPanel(this.mUnitPanelWidth / 2.0f, this.mUnitPanelHeight);
        panelBack.attachTo(this.mWorld);
        verticalAxisL.attachTo(this.mWorld);
        verticalAxisR.attachTo(this.mWorld);
        verticalAxisL.addModel(panelFrontL, 0, false);
        verticalAxisR.addModel(panelFrontR, 0, false);
        verticalAxisL.locate((-this.mUnitPanelWidth) / 2.0f, (-this.mUnitPanelHeight) / 2.0f, 0.0f);
        verticalAxisL.look(0.0f, 0.0f);
        verticalAxisR.locate(this.mUnitPanelWidth / 2.0f, (-this.mUnitPanelHeight) / 2.0f, 0.0f);
        verticalAxisR.look(0.0f, 0.0f);
        Rect rectBitmap = new Rect();
        int bmpWidth = this.mBitmap[this.mStartIndex].getWidth() / 2;
        int bmpHeight = this.mBitmap[this.mStartIndex].getHeight();
        panelBack.setImageResource(0, this.mBitmap[this.mEndIndex], (Rect) null);
        rectBitmap.set(0, 0, bmpWidth, bmpHeight);
        panelFrontL.setImageResource(0, this.mBitmap[this.mStartIndex], rectBitmap);
        rectBitmap.set(bmpWidth, 0, bmpWidth * 2, bmpHeight);
        panelFrontR.setImageResource(0, this.mBitmap[this.mStartIndex], rectBitmap);
        this.mEffectFinishCnt = 0;
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.9
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                if (TFEffect.this.mEffectFinishCnt >= 2) {
                    TFModel[] modelsToDelete = {panelBack, panelFrontL, panelFrontR};
                    TFEffect.this.prvOnFinishEffect(modelsToDelete);
                    return;
                }
                TFEffect.access$1908(TFEffect.this);
            }
        });
        panelFrontL.fade(0.0f, 1000L);
        panelFrontR.fade(0.0f, 1000L);
        panelBack.locate(0.0f, 0.0f, -2.28f);
        panelBack.move(0.0f, 0.0f, 0.0f, 1000L);
        verticalAxisL.move(-this.mUnitPanelWidth, -this.mUnitPanelHeight, 0.5f, 1000L);
        verticalAxisL.rotate(90.0f, 0.0f, 1000L, 0, 2);
        verticalAxisR.move(this.mUnitPanelWidth, -this.mUnitPanelHeight, 0.5f, 1000L);
        verticalAxisR.rotate(-90.0f, 0.0f, 1000L, 1, 2);
    }

    private void showEffectAskew() {
        TFPlaceHolder centerWall = new TFPlaceHolder(true);
        TFPlaceHolder sideWall = new TFPlaceHolder(true);
        TFPanel panelFront = new TFPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        TFPanel panelBackL = new TFPanel(this.mUnitPanelWidth / 2.0f, this.mUnitPanelHeight);
        TFPanel panelBackR = new TFPanel(this.mUnitPanelWidth / 2.0f, this.mUnitPanelHeight);
        TFPanel panelBackMain = new TFPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        centerWall.attachTo(this.mWorld);
        sideWall.attachTo(this.mWorld);
        centerWall.addModel(panelFront, 0);
        sideWall.addModel(panelBackL, 0);
        sideWall.addModel(panelBackR, 1);
        panelBackL.setVisibility(false);
        panelBackR.setVisibility(false);
        Rect rectBitmap = new Rect();
        int bmpWidth = this.mBitmap[this.mStartIndex].getWidth() / 2;
        int bmpHeight = this.mBitmap[this.mStartIndex].getHeight();
        panelFront.setImageResource(0, this.mBitmap[this.mStartIndex], (Rect) null);
        rectBitmap.set(0, 0, bmpWidth, bmpHeight);
        panelBackL.setImageResource(0, this.mBitmap[this.mEndIndex], rectBitmap);
        rectBitmap.set(bmpWidth, 0, bmpWidth * 2, bmpHeight);
        panelBackR.setImageResource(0, this.mBitmap[this.mEndIndex], rectBitmap);
        panelBackMain.setImageResource(0, this.mBitmap[this.mEndIndex], (Rect) null);
        centerWall.setEffectFinishListener(new AnonymousClass10(panelBackL, panelBackR, panelFront, centerWall, sideWall, panelBackMain));
        centerWall.locate(0.0f, 0.0f, 0.0f);
        centerWall.move(-0.014f, 0.0f, -1.1f, 500L);
        centerWall.rotate(38.0f, 335.0f, 500L, 2, 2);
        sideWall.move(-0.014f, 0.0f, -1.1f, 500L);
        sideWall.rotate(38.0f, 335.0f, 500L, 2, 2);
        sideWall.locate(0.0f, 0.0f, -0.01f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.nemustech.tiffany.world.TFEffect$10  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass10 implements TFWorld.OnEffectFinishListener {
        final /* synthetic */ TFPlaceHolder val$centerWall;
        final /* synthetic */ TFPanel val$panelBackL;
        final /* synthetic */ TFPanel val$panelBackMain;
        final /* synthetic */ TFPanel val$panelBackR;
        final /* synthetic */ TFPanel val$panelFront;
        final /* synthetic */ TFPlaceHolder val$sideWall;

        AnonymousClass10(TFPanel tFPanel, TFPanel tFPanel2, TFPanel tFPanel3, TFPlaceHolder tFPlaceHolder, TFPlaceHolder tFPlaceHolder2, TFPanel tFPanel4) {
            this.val$panelBackL = tFPanel;
            this.val$panelBackR = tFPanel2;
            this.val$panelFront = tFPanel3;
            this.val$centerWall = tFPlaceHolder;
            this.val$sideWall = tFPlaceHolder2;
            this.val$panelBackMain = tFPanel4;
        }

        @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
        public void onEffectFinish(TFObject object) {
            this.val$panelBackL.setVisibility(true);
            this.val$panelBackR.setVisibility(true);
            this.val$panelBackL.locate(-2.75f, 0.0f, 0.0f);
            this.val$panelBackR.locate(2.75f, 0.0f, 0.0f);
            this.val$panelFront.move(0.0f, 2.5f, 0.0f, 500L);
            this.val$panelBackL.move(-0.25f, 0.0f, 0.0f, 500L);
            this.val$panelBackR.move(0.25f, 0.0f, 0.0f, 500L);
            this.val$panelFront.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.10.1
                @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
                public void onEffectFinish(TFObject object2) {
                    AnonymousClass10.this.val$panelFront.detachFrom(AnonymousClass10.this.val$centerWall);
                    AnonymousClass10.this.val$panelBackL.detachFrom(AnonymousClass10.this.val$sideWall);
                    AnonymousClass10.this.val$panelBackR.detachFrom(AnonymousClass10.this.val$sideWall);
                    AnonymousClass10.this.val$panelBackL.setVisibility(false);
                    AnonymousClass10.this.val$panelBackR.setVisibility(false);
                    AnonymousClass10.this.val$panelBackMain.attachTo(AnonymousClass10.this.val$centerWall);
                    AnonymousClass10.this.val$centerWall.move(0.0f, 0.0f, 0.0f, 500L);
                    AnonymousClass10.this.val$centerWall.rotate(0.0f, 0.0f, 500L, 2, 2);
                    AnonymousClass10.this.val$centerWall.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.10.1.1
                        @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
                        public void onEffectFinish(TFObject object3) {
                            AnonymousClass10.this.val$panelBackMain.detachFrom(AnonymousClass10.this.val$centerWall);
                            TFModel[] modelsToDelete = {AnonymousClass10.this.val$panelFront, AnonymousClass10.this.val$panelBackL, AnonymousClass10.this.val$panelBackR, AnonymousClass10.this.val$panelBackMain};
                            TFEffect.this.prvOnFinishEffect(modelsToDelete);
                        }
                    });
                }
            });
        }
    }

    private void showEffectVerticalColumn(boolean bReversed) {
        int MOSAIC_WIDTH = this.mBitmap[this.mStartIndex].getWidth() / 4;
        int MOSAIC_HEIGHT = this.mBitmap[this.mStartIndex].getHeight() / 1;
        float[] locX = new float[4];
        float[] locY = new float[4];
        float panelWidth = this.mUnitPanelWidth / 4.0f;
        float panelHeight = this.mUnitPanelHeight;
        final TFPanel[] panel = new TFPanel[4];
        Rect rectMosaic = new Rect();
        this.mEffectFinishCnt = 0;
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.11
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.access$1908(TFEffect.this);
                if (TFEffect.this.mEffectFinishCnt == 4) {
                    TFEffect.this.prvOnFinishEffect(panel);
                }
            }
        });
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 4; x++) {
                int index = (y * 4) + x;
                panel[index] = new TFPanel(this.mWorld, panelWidth, panelHeight);
                rectMosaic.set(x * MOSAIC_WIDTH, y * MOSAIC_HEIGHT, (x * MOSAIC_WIDTH) + MOSAIC_WIDTH, (y * MOSAIC_HEIGHT) + MOSAIC_HEIGHT);
                panel[index].setImageResource(0, this.mBitmap[this.mStartIndex], rectMosaic);
                panel[index].setImageResource(1, this.mBitmap[this.mEndIndex], rectMosaic);
                locX[index] = (-0.5f) + (index * panelWidth) + (panelWidth / 2.0f);
                locY[index] = 0.0f;
                panel[index].locate(locX[index], locY[index], 0.0f);
                long waitTime = (bReversed ? (4 - index) - 1 : index) * 200;
                panel[index].setWait(waitTime);
                int direction = bReversed ? 1 : 0;
                panel[index].rotate(180.0f, 0.0f, 300L, direction, direction);
            }
        }
    }

    private void showEffectHorizontalRow(boolean bReversed) {
        int MOSAIC_WIDTH = this.mBitmap[this.mStartIndex].getWidth() / 1;
        int MOSAIC_HEIGHT = this.mBitmap[this.mStartIndex].getHeight() / 4;
        float[] locX = new float[4];
        float[] locY = new float[4];
        float normalizeHeight = this.mUnitPanelHeight / 4.0f;
        final TFPanel[] panel = new TFPanel[4];
        Rect rectMosaic = new Rect();
        this.mEffectFinishCnt = 0;
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.12
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.access$1908(TFEffect.this);
                if (TFEffect.this.mEffectFinishCnt == 4) {
                    TFEffect.this.prvOnFinishEffect(panel);
                }
            }
        });
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 1; x++) {
                int index = (y * 1) + x;
                panel[index] = new TFPanel(this.mWorld, this.mUnitPanelWidth, normalizeHeight);
                rectMosaic.set(x * MOSAIC_WIDTH, y * MOSAIC_HEIGHT, (x * MOSAIC_WIDTH) + MOSAIC_WIDTH, (y * MOSAIC_HEIGHT) + MOSAIC_HEIGHT);
                panel[index].setImageResource(0, this.mBitmap[this.mStartIndex], rectMosaic);
                panel[index].setImageResource(1, this.mBitmap[this.mEndIndex], rectMosaic);
                panel[index].reverseImage(1, 3);
                locX[index] = 0.0f;
                locY[index] = (2.0f * normalizeHeight) - ((index * normalizeHeight) + (normalizeHeight / 2.0f));
                panel[index].locate(locX[index], locY[index], 0.0f);
                long waitTime = index * 200;
                panel[index].setWait(waitTime);
                int direction = bReversed ? 1 : 0;
                panel[index].rotate(0.0f, 180.0f, 300L, direction, direction);
            }
        }
    }

    private void showEffectHorizontalCube(boolean bReversed) {
        setReflectingFloor(false);
        final TFCube cube = new TFCube(this.mWorld, this.mUnitPanelWidth, this.mUnitPanelHeight);
        cube.setImageResource(0, this.mBitmap[0]);
        cube.setImageResource(3, this.mBitmap[1]);
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.13
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.this.prvOnFinishEffect(cube);
            }
        });
        cube.locate(0.0f, 0.0f, -0.5f);
        if (bReversed) {
            cube.look(-90.0f, 0.0f);
            cube.rotate(0.0f, 0.0f, 500L, 0, 0);
            return;
        }
        cube.rotate(-90.0f, 0.0f, 500L, 1, 1);
    }

    private void showEffectVerticalPageOver(boolean bReversed) {
        float panelWidth;
        float panelHeight;
        Rect upRect = new Rect();
        Rect downRect = new Rect();
        setReflectingFloor(false);
        if (!this.mChildViewUseFull) {
            setTranslucentMode(true);
            panelWidth = this.mChildView[1].getWidth() / this.mViewWidth;
            panelHeight = this.mChildView[1].getHeight() / this.mViewWidth;
        } else {
            panelWidth = this.mUnitPanelWidth;
            panelHeight = this.mUnitPanelHeight;
        }
        final TFHorizontalAxisHolder horizontalAxis = new TFHorizontalAxisHolder(0.0f, bReversed ? 180.0f : 0.0f);
        horizontalAxis.attachTo(this.mWorld);
        float[] loc = getViewLocation(this.mChildView[this.mStartIndex], 0.5f);
        horizontalAxis.locate(loc[0], loc[1], 0.0f);
        final TFPanel rotate_panel = new TFPanel(panelWidth, panelHeight / 2.0f);
        final TFPanel current_up_panel = new TFPanel(this.mWorld, panelWidth, panelHeight / 2.0f);
        final TFPanel next_down_panel = new TFPanel(this.mWorld, panelWidth, panelHeight / 2.0f);
        horizontalAxis.addModel(rotate_panel, 0, false);
        int bmpWidth = this.mBitmap[this.mStartIndex].getWidth();
        int bmpHeight = this.mBitmap[this.mStartIndex].getHeight() / 2;
        upRect.set(0, 0, bmpWidth, bmpHeight);
        downRect.set(0, bmpHeight, bmpWidth, bmpHeight * 2);
        if (bReversed) {
            rotate_panel.setImageResource(0, this.mBitmap[this.mStartIndex], downRect);
            rotate_panel.setImageResource(1, this.mBitmap[this.mEndIndex], upRect);
            rotate_panel.reverseImage(1, 3);
            current_up_panel.setImageResource(0, this.mBitmap[this.mStartIndex], upRect);
            if (!this.mChildViewUseFull) {
                float[] loc2 = getViewLocation(this.mChildView[this.mStartIndex], 0.25f);
                current_up_panel.locate(loc2[0], loc2[1], 0.0f);
            } else {
                current_up_panel.locate(0.0f, panelHeight / 4.0f, 0.0f);
            }
            TFUtils.saveBitmapToFile("sdcard/check.png", this.mBitmap[this.mEndIndex], Bitmap.CompressFormat.PNG, 1);
            next_down_panel.setImageResource(0, this.mBitmap[this.mEndIndex], downRect);
            if (!this.mChildViewUseFull) {
                float[] loc3 = getViewLocation(this.mChildView[this.mStartIndex], 0.75f);
                next_down_panel.locate(loc3[0], loc3[1], 0.0f);
            } else {
                next_down_panel.locate(0.0f, -(panelHeight / 4.0f), 0.0f);
            }
        } else {
            rotate_panel.setImageResource(0, this.mBitmap[this.mStartIndex], upRect);
            rotate_panel.setImageResource(1, this.mBitmap[this.mEndIndex], downRect);
            rotate_panel.reverseImage(1, 3);
            current_up_panel.setImageResource(0, this.mBitmap[this.mEndIndex], upRect);
            if (!this.mChildViewUseFull) {
                float[] loc4 = getViewLocation(this.mChildView[this.mStartIndex], 0.25f);
                current_up_panel.locate(loc4[0], loc4[1], 0.0f);
            } else {
                current_up_panel.locate(0.0f, panelHeight / 4.0f, 0.0f);
            }
            next_down_panel.setImageResource(0, this.mBitmap[this.mStartIndex], downRect);
            if (!this.mChildViewUseFull) {
                float[] loc5 = getViewLocation(this.mChildView[this.mStartIndex], 0.75f);
                next_down_panel.locate(loc5[0], loc5[1], 0.0f);
            } else {
                next_down_panel.locate(0.0f, -(panelHeight / 4.0f), 0.0f);
            }
        }
        this.mEffectFinishCnt = 0;
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.14
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFModel[] modelsToDelete = {rotate_panel, current_up_panel, next_down_panel};
                if (TFEffect.this.mReverse) {
                    if (!TFEffect.this.mChildViewUseFull) {
                        TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.14.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TFEffect.this.mChildView[TFEffect.this.mEndIndex].bringToFront();
                                TFEffect.this.mChildView[TFEffect.this.mEndIndex].setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    TFEffect.this.prvOnFinishEffect(modelsToDelete);
                } else if (TFEffect.this.mEffectFinishCnt != 0) {
                    if (TFEffect.this.mEffectFinishCnt != 1) {
                        if (TFEffect.this.mEffectFinishCnt != 2) {
                            if (TFEffect.this.mEffectFinishCnt != 3) {
                                if (TFEffect.this.mEffectFinishCnt != 4) {
                                    if (TFEffect.this.mEffectFinishCnt != 5) {
                                        if (!TFEffect.this.mChildViewUseFull) {
                                            TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.14.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    TFEffect.this.mChildView[TFEffect.this.mEndIndex].bringToFront();
                                                    TFEffect.this.mChildView[TFEffect.this.mEndIndex].setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                        TFEffect.this.prvOnFinishEffect(modelsToDelete);
                                        return;
                                    }
                                    float reboundAngle = 20.0f - 15.0f;
                                    long reboundDuration = 80 - 40;
                                    horizontalAxis.rotate(0.0f, reboundAngle, reboundDuration);
                                    TFEffect.access$1908(TFEffect.this);
                                    return;
                                }
                                float reboundAngle2 = 20.0f - 15.0f;
                                long reboundDuration2 = 80 - 40;
                                horizontalAxis.rotate(0.0f, -reboundAngle2, reboundDuration2);
                                TFEffect.access$1908(TFEffect.this);
                                return;
                            }
                            float reboundAngle3 = 20.0f - 10.0f;
                            long reboundDuration3 = 80 - 20;
                            horizontalAxis.rotate(0.0f, reboundAngle3, reboundDuration3);
                            TFEffect.access$1908(TFEffect.this);
                            return;
                        }
                        float reboundAngle4 = 20.0f - 10.0f;
                        long reboundDuration4 = 80 - 20;
                        horizontalAxis.rotate(0.0f, -reboundAngle4, reboundDuration4);
                        TFEffect.access$1908(TFEffect.this);
                        return;
                    }
                    horizontalAxis.rotate(0.0f, 20.0f, 80L);
                    TFEffect.access$1908(TFEffect.this);
                } else {
                    horizontalAxis.rotate(0.0f, -20.0f, 80L);
                    TFEffect.access$1908(TFEffect.this);
                }
            }
        });
        if (bReversed) {
            horizontalAxis.rotate(0.0f, -180.0f, 327L, 1, 1);
        } else {
            horizontalAxis.rotate(0.0f, 180.0f, 327L, 0, 0);
        }
    }

    private float[] getViewLocation(View targetView, float heightCenterRatio) {
		CMN.debug("getViewLocation::");
		int[] tmp = {0,0};
		targetView.getLocationOnScreen(tmp);
        int[] viewLocation = {0, tmp[1] - (this.mStatusBarHeight + this.mTitleHeight)};
        DisplayMetrics dm = this.mContext.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = (dm.heightPixels - this.mStatusBarHeight) - this.mTitleHeight;
        Log.d(TAG, "View position x:" + viewLocation[0] + " y:" + viewLocation[1]);
        float[] loc = {((viewLocation[0] + (targetView.getWidth() / 2.0f)) - (screenWidth / 2.0f)) / screenWidth, ((screenHeight / 2.0f) - (viewLocation[1] + (targetView.getHeight() * heightCenterRatio))) / screenWidth};
        return loc;
    }

    private void showEffectSink_FlexiblePanel(boolean bReversed) {
        int startIndex = this.mStartIndex;
        int endIndex = this.mEndIndex;
        TFPanel nextPage = new TFPanel(this.mWorld, this.mUnitPanelWidth, this.mUnitPanelHeight);
        nextPage.setImageResource(0, this.mBitmap[endIndex], (Rect) null);
        nextPage.locate(0.0f, 0.0f, -0.001f);
        final TFFlexiblePanel currentPage = new TFFlexiblePanel(this.mWorld, this.mUnitPanelWidth, this.mUnitPanelHeight);
        currentPage.setImageResource(0, this.mBitmap[startIndex], (Rect) null);
        currentPage.locate(0.0f, 0.0f, 0.0f);
        if (0 != 0) {
            currentPage.reverseImage(0, 0);
        }
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.15
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.this.prvOnFinishEffect(currentPage);
            }
        });
        Sink sink = new Sink();
        currentPage.setPointBlender(sink);
    }

    private void showEffectPageOver_PagePanel(boolean bReversed, int degree, int duration) {
        setTranslucentMode(true);
        setReflectingFloor(false);
        this.mOneWayAnimation = true;
        int startIndex = this.mStartIndex;
        float timeLineStart = 0.0f;
        float timeLineEnd = 1.0f;
        if (bReversed) {
            startIndex = this.mEndIndex;
            timeLineStart = 1.0f;
            timeLineEnd = 0.0f;
        }
        float x = 0.0f;
        float y = 0.0f;
        if (this.mChildView[startIndex] != null && this.mCoverImage != null) {
            x = ((this.mChildView[startIndex].getLeft() + (this.mViewWidth / 2.0f)) - (this.mCoverImage.getWidth() / 2.0f)) / this.mViewWidth;
            y = ((this.mCoverImage.getHeight() / 2.0f) - (this.mChildView[startIndex].getTop() + (this.mViewHeight / 2.0f))) / this.mViewWidth;
            Log.d(TAG, "ScreenWidth:" + this.mCoverImage.getWidth() + " viewLeft:" + this.mChildView[startIndex].getLeft());
            Log.d(TAG, "ScreenHeight:" + this.mCoverImage.getHeight() + " viewTop:" + this.mChildView[startIndex].getTop());
            Log.d(TAG, "top:" + this.mChildView[startIndex].getTop() + " x:" + x + " y:" + y + " mViewWidth:" + this.mViewWidth);
        }
        final TFPagePanel currentPage = new TFPagePanel(this.mWorld, this.mUnitPanelWidth, this.mUnitPanelHeight);
        currentPage.locate(x, y, 0.0f);
        currentPage.setImageResource(0, this.mBitmap[startIndex], (Rect) null);
        if (this.mBackBitmap[startIndex] != null) {
            currentPage.setImageResource(1, this.mBackBitmap[startIndex], (Rect) null);
        }
        currentPage.setDirectionDegree(degree);
        currentPage.setTimeLine(timeLineStart);
        if (0 != 0) {
            currentPage.reverseImage(0, 0);
        }
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.16
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                if (TFEffect.this.mReverse && TFEffect.this.mChildView[0] != null && TFEffect.this.mChildView[1] != null) {
                    TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.16.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TFEffect.this.mChildView[1].setVisibility(View.INVISIBLE);
                            TFEffect.this.mChildView[0].bringToFront();
                            TFEffect.this.mChildView[0].setVisibility(View.VISIBLE);
                        }
                    });
                }
                TFEffect.this.prvOnFinishEffect(currentPage);
            }
        });
        currentPage.setTimeLine(timeLineStart, timeLineEnd, duration, this.mEffectInterpolator);
    }

    private void showEffectDiagonalFling(boolean bReversed) {
        TFAnimationSet set = new TFAnimationSet();
        Animation in = set.getAnimation(1, true);
        Animation out = set.getAnimation(1, false);
        in.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.18
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TFEffect.this.mHandler.post(TFEffect.this.mEffect2DAnimationEnd);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.mChild2DView[0].startAnimation(out);
        this.mChild2DView[1].startAnimation(in);
    }

    private void showEffectDiagonalScale(boolean bReversed) {
        TFAnimationSet set = new TFAnimationSet();
        Animation in = set.getAnimation(2, true);
        Animation out = set.getAnimation(2, false);
        in.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.19
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TFEffect.this.mHandler.post(TFEffect.this.mEffect2DAnimationEnd);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.mChild2DView[0].startAnimation(out);
        this.mChild2DView[1].startAnimation(in);
    }

    private void showEffectProjectorBoardUp(boolean bReversed) {
        TFAnimationSet set = new TFAnimationSet();
        Animation out = set.getAnimation(3, false);
        this.mChild2DView[0].bringToFront();
        out.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.20
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TFEffect.this.mHandler.post(TFEffect.this.mEffect2DAnimationEnd);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.mChild2DView[0].startAnimation(out);
    }

    private void showEffectProjectorBoardDown(boolean bReversed) {
        TFAnimationSet set = new TFAnimationSet();
        Animation out = set.getAnimation(4, false);
        this.mChild2DView[1].bringToFront();
        out.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.21
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TFEffect.this.mHandler.post(TFEffect.this.mEffect2DAnimationEnd);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.mChild2DView[1].startAnimation(out);
    }

    private void showEffectCenterZoom(boolean bReversed) {
        TFAnimationSet set = new TFAnimationSet();
        Animation in = set.getAnimation(5, true);
        this.mChild2DView[1].bringToFront();
        in.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.22
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TFEffect.this.mHandler.post(TFEffect.this.mEffect2DAnimationEnd);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.mChild2DView[1].startAnimation(in);
    }

    private void showEffectLineZoom(boolean bReversed) {
        final TFAnimationSet set = new TFAnimationSet();
        Animation in = set.getAnimation(6, true);
        this.mChild2DView[1].bringToFront();
        in.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.23
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                Animation secondAni = set.getAnimation(7, true);
                secondAni.setAnimationListener(new Animation.AnimationListener() { // from class: com.nemustech.tiffany.world.TFEffect.23.1
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation2) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation2) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation2) {
                        TFEffect.this.mHandler.post(TFEffect.this.mEffect2DAnimationEnd);
                    }
                });
                TFEffect.this.mChild2DView[1].startAnimation(secondAni);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.mChild2DView[1].startAnimation(in);
    }

    private void showEffectPageOver_Sticker(boolean bReversed, int degree, int duration) {
        setTranslucentMode(true);
        setReflectingFloor(false);
        int startIndex = this.mStartIndex;
        float timeLineStart = 0.0f;
        float timeLineEnd = 1.0f;
        if (bReversed) {
            startIndex = this.mEndIndex;
            timeLineStart = 1.0f;
            timeLineEnd = 0.0f;
        }
        View targetView = this.mChildView[startIndex];
        int[] viewLocation = new int[2];
        targetView.getLocationOnScreen(viewLocation);
        switch (this.mStatusBarStatus) {
            case 0:
            case 1:
                viewLocation[1] = viewLocation[1] - (this.mStatusBarHeight + this.mTitleHeight);
                break;
            case 2:
                viewLocation[1] = viewLocation[1] - this.mTitleHeight;
                break;
        }
        DisplayMetrics dm = this.mContext.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = (dm.heightPixels - this.mStatusBarHeight) - this.mTitleHeight;
        Log.d(TAG, "View position x:" + viewLocation[0] + " y:" + viewLocation[1]);
        float x = ((viewLocation[0] + (targetView.getWidth() / 2.0f)) - (screenWidth / 2.0f)) / screenWidth;
        float y = ((screenHeight / 2.0f) - (viewLocation[1] + (targetView.getHeight() / 2.0f))) / screenWidth;
        final TFPagePanel currentPage = new TFPagePanel(this.mWorld, targetView.getWidth() / screenWidth, targetView.getHeight() / screenWidth);
        currentPage.locate(x, y, 0.0f);
        currentPage.setImageResource(0, this.mBitmap[startIndex], (Rect) null);
        if (this.mBackBitmap[startIndex] != null) {
            currentPage.setImageResource(1, this.mBackBitmap[startIndex], (Rect) null);
        }
        currentPage.setDirectionDegree(degree);
        currentPage.setTimeLine(timeLineStart);
        if (0 != 0) {
            currentPage.reverseImage(0, 0);
        }
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.24
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                if (TFEffect.this.mReverse && TFEffect.this.mChildView[0] != null) {
                    TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.24.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TFEffect.this.mChildView[0].bringToFront();
                            TFEffect.this.mChildView[0].setVisibility(View.VISIBLE);
                        }
                    });
                }
                TFEffect.this.prvOnFinishEffect(currentPage);
            }
        });
        currentPage.setTimeLine(timeLineStart, timeLineEnd, duration);
    }

    private void showEffectGenie_FlexiblePanel(boolean bReversed) {
        int startIndex;
        setTranslucentMode(true);
        this.mOneWayAnimation = true;
        if (!bReversed) {
            startIndex = this.mStartIndex;
        } else {
            startIndex = this.mEndIndex;
        }
        final TFFlexiblePanel currentPage = new TFFlexiblePanel(this.mWorld, this.mUnitPanelWidth, this.mUnitPanelHeight);
        currentPage.setImageResource(0, this.mBitmap[startIndex], (Rect) null);
        currentPage.locate(0.0f, 0.0f, 0.0f);
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.25
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                if (TFEffect.this.mReverse && TFEffect.this.mChildView[0] != null && TFEffect.this.mChildView[1] != null) {
                    TFEffect.this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.25.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TFEffect.this.mChildView[1].setVisibility(View.INVISIBLE);
                            TFEffect.this.mChildView[0].bringToFront();
                            TFEffect.this.mChildView[0].setVisibility(View.VISIBLE);
                        }
                    });
                }
                TFEffect.this.prvOnFinishEffect(currentPage);
            }
        });
        Genie genie = new Genie(bReversed);
        currentPage.setPointBlender(genie);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getMaxDegree(TFPagePanel p, float timeline) {
        float w = p.getWidth();
        float h = p.getHeight();
        TFPagePanel.CircularPageOverInterpolator inter = (TFPagePanel.CircularPageOverInterpolator) p.getPageOverInterpolator();
        float factor = 1.0f + ((3.1415927f * inter.mR) / 2.0f);
        float t = timeline * factor;
        float theta = (float) Math.atan2(t * h, (1.0f - t) * w);
        int degree = 90 - ((int) (Math.toDegrees(theta) + 0.5d));
        return degree;
    }

    private void showEffectPageCurlTrans(boolean bReversed, int degree, int duration, Interpolator interpolator) {
        this.mDepthTest = false;
        this.mEconomic = true;
        setTranslucentMode(true);
        this.mOneWayAnimation = true;
        this.mPageCurlPage = new TFPagePanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        this.mPageCurlPage.setImageResource(0, this.mBitmap[0]);
        this.mPageCurlPage.reverseImage(1, 2);
        if (this.mBackBitmap[0] != null) {
            this.mPageCurlPage.setImageResource(1, this.mBackBitmap[0]);
        }
        this.mPageCurlPage.attachTo(this.mWorld);
        this.mPageCurlPage.setPageShadow(0.25f, 0.9f);
        this.mPageCurlPage.setDirectionDegree(degree);
        this.mPageCurlPage.setTimeLine(bReversed ? 1.0f : 0.0f);
        this.mPageCurlPage.setTimeLine(this.mPageCurlPage.getTimeLine(), this.mDragMode ? bReversed ? 1 : 0 : bReversed ? 0 : 1, duration, interpolator);
        if (this.mDragMode) {
            this.mPageCurlPage.setOnUpdateVertexListener(new TFPagePanel.OnUpdateVertexListener() { // from class: com.nemustech.tiffany.world.TFEffect.26
                @Override // com.nemustech.tiffany.world.TFPagePanel.OnUpdateVertexListener
                public void beforeUpdate(TFPagePanel pagePanel) {
                    int degree2 = pagePanel.getDirectionDegree();
                    float t = pagePanel.getTimeLine();
                    int maxDegree = TFEffect.getMaxDegree(pagePanel, t);
                    if (degree2 < 90) {
                        degree2 = Math.max(Math.min(degree2, maxDegree), 0);
                    } else if (degree2 < 180) {
                        degree2 = Math.min(Math.max(degree2, 180 - maxDegree), 180);
                    } else if (degree2 < 270) {
                        degree2 = Math.max(Math.min(degree2, maxDegree + 180), 180);
                    } else if (degree2 < 360) {
                        degree2 = Math.min(Math.max(degree2, 360 - maxDegree), 360);
                    }
                    pagePanel.setDirectionDegree(degree2);
                }

                @Override // com.nemustech.tiffany.world.TFPagePanel.OnUpdateVertexListener
                public void afterUpdate(TFPagePanel pagePanel) {
                }
            });
        }
        this.mEffectFinishListener = new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.27
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.this.prvOnFinishEffect(TFEffect.this.mPageCurlPage);
            }
        };
        if (this.mDragMode) {
            this.mPageCurlPage.setEffectFinishListener(null);
        } else {
            this.mPageCurlPage.setEffectFinishListener(this.mEffectFinishListener);
        }
    }

    private void showEffectPageCurl(boolean bReversed, int degree, int duration, Interpolator interpolator) {
        float f = 1.0f;
        this.mDepthTest = false;
        this.mEconomic = true;
        this.mBlending = true;
        this.mPageCurlPage = new TFPagePanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        this.mPageCurlPage.setBorderShadow(0.03f, 1.0f);
        this.mPageCurlPage.setBackMaskOpacity(0.5f);
        this.mPageCurlPage.setImageResource(0, this.mBitmap[0]);
        this.mPageCurlPage.reverseImage(1, 2);
        if (this.mBackBitmap[0] != null) {
            this.mPageCurlPage.setImageResource(1, this.mBackBitmap[0]);
        }
        final TFPanel nextPage = new TFPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        nextPage.setImageResource(0, this.mBitmap[1]);
        nextPage.attachTo(this.mWorld);
        this.mPageCurlPage.attachTo(this.mWorld);
        this.mPageCurlPage.setPageShadow(0.25f, 0.9f);
        this.mPageCurlPage.setDirectionDegree(degree);
        TFPagePanel tFPagePanel = this.mPageCurlPage;
        if (!bReversed) {
            f = 0.0f;
        }
        tFPagePanel.setTimeLine(f);
        this.mPageCurlPage.setTimeLine(this.mPageCurlPage.getTimeLine(), this.mDragMode ? bReversed ? 1 : 0 : bReversed ? 0 : 1, duration, interpolator);
        if (this.mDragMode) {
            this.mPageCurlPage.setOnUpdateVertexListener(new TFPagePanel.OnUpdateVertexListener() { // from class: com.nemustech.tiffany.world.TFEffect.28
                @Override // com.nemustech.tiffany.world.TFPagePanel.OnUpdateVertexListener
                public void beforeUpdate(TFPagePanel pagePanel) {
                    int degree2 = pagePanel.getDirectionDegree();
                    float t = pagePanel.getTimeLine();
                    int maxDegree = TFEffect.getMaxDegree(pagePanel, t);
                    if (degree2 < 90) {
                        degree2 = Math.max(Math.min(degree2, maxDegree), 0);
                    } else if (degree2 < 180) {
                        degree2 = Math.min(Math.max(degree2, 180 - maxDegree), 180);
                    } else if (degree2 < 270) {
                        degree2 = Math.max(Math.min(degree2, maxDegree + 180), 180);
                    } else if (degree2 < 360) {
                        degree2 = Math.min(Math.max(degree2, 360 - maxDegree), 360);
                    }
                    pagePanel.setDirectionDegree(degree2);
                }

                @Override // com.nemustech.tiffany.world.TFPagePanel.OnUpdateVertexListener
                public void afterUpdate(TFPagePanel pagePanel) {
                }
            });
        }
        this.mEffectFinishListener = new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.29
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFModel[] modelsToDelete = {TFEffect.this.mPageCurlPage, nextPage};
                TFEffect.this.prvOnFinishEffect(modelsToDelete);
            }
        };
        if (this.mDragMode) {
            this.mPageCurlPage.setEffectFinishListener(null);
        } else {
            this.mPageCurlPage.setEffectFinishListener(this.mEffectFinishListener);
        }
    }

    private void showEffectTwist(boolean bReversed) {
        this.mDepthTest = false;
        this.mEconomic = true;
        final TFCustomPanel cusPanel = new TFCustomPanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        cusPanel.attachTo(this.mWorld);
        int startIndex = this.mStartIndex;
        int endIndex = this.mEndIndex;
        cusPanel.setImageResource(0, this.mBitmap[startIndex], (Rect) null);
        cusPanel.setImageResource(1, this.mBitmap[endIndex], (Rect) null);
        cusPanel.locate(0.0f, 0.0f, 0.0f);
        this.mWorld.setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFEffect.30
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                TFEffect.this.prvOnFinishEffect(cusPanel);
            }
        });
        TFTwistEffect tw = new TFTwistEffect(cusPanel);
        if (this.mTwistParam != null) {
            this.mTwistParam.setParam(tw);
        }
        cusPanel.startBlender(tw);
    }

    /* loaded from: classes.dex */
    public static class PageOver extends TFFlexiblePanel.IdentityPointBlender {
        public static final int BOTTOM_TO_TOP = 3;
        public static final int LEFT_TO_RIGHT = 1;
        private static final int N = 8;
        public static final int RIGHT_TO_LEFT = 0;
        public static final int TOP_TO_BOTTOM = 2;
        private static final int circleN = 2;
        private static final int mEllapse = 0;
        private int mFrame;
        private int mTicks;
        private float[] points = new float[18];
        public int mDirection = 0;
        public boolean mReverse = false;

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.IdentityPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void calcXYZ(int s, int t, float[] xyz) {
            switch (this.mDirection) {
                case 1:
                    xyz[0] = 1.0f - this.points[((8 - s) * 2) + 0];
                    xyz[1] = t / 8.0f;
                    xyz[2] = this.points[((8 - s) * 2) + 1];
                    return;
                case 2:
                    xyz[0] = s / 8.0f;
                    xyz[1] = this.points[(t * 2) + 0];
                    xyz[2] = this.points[(t * 2) + 1];
                    return;
                case 3:
                    xyz[0] = s / 8.0f;
                    xyz[1] = 1.0f - this.points[((8 - t) * 2) + 0];
                    xyz[2] = this.points[((8 - t) * 2) + 1];
                    return;
                default:
                    xyz[0] = this.points[(s * 2) + 0];
                    xyz[1] = t / 8.0f;
                    xyz[2] = this.points[(s * 2) + 1];
                    return;
            }
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.IdentityPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onStart() {
            int i = 0;
            this.mTicks = 0;
            if (this.mReverse) {
                i = 10;
            }
            this.mFrame = i;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.IdentityPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean onFrame(int tick) {
            this.mTicks += tick;
            if (this.mTicks < 0) {
                return false;
            }
            this.mTicks += 0;
            this.points[0] = 0.0f;
            this.points[1] = 0.0f;
            if (this.mFrame <= 8) {
                int c = 8 - this.mFrame;
                float cx = (8 - this.mFrame) / 8.0f;
                int i = 1;
                while (i <= c) {
                    this.points[(i * 2) + 0] = this.points[((i - 1) * 2) + 0] + 0.125f;
                    this.points[(i * 2) + 1] = this.points[((i - 1) * 2) + 1];
                    i++;
                }
                while (i <= 8 && i <= c + 2) {
                    float rad = (((-90.0f) + ((i - c) * 90.0f)) * 3.1415927f) / 180.0f;
                    this.points[(i * 2) + 0] = (0.07957747f * ((float) Math.cos(rad))) + cx;
                    this.points[(i * 2) + 1] = (0.07957747f * ((float) Math.sin(rad))) + 0.07957747f;
                    i++;
                }
                while (i <= 8) {
                    this.points[(i * 2) + 0] = this.points[((i - 1) * 2) + 0] - 0.125f;
                    this.points[(i * 2) + 1] = this.points[((i - 1) * 2) + 1];
                    i++;
                }
            } else {
                int c2 = this.mFrame - 8;
                float center_rad = ((90.0f + (90.0f * c2)) * 3.1415927f) / 180.0f;
                float cx2 = 0.07957747f * ((float) Math.cos(center_rad));
                float cy = 0.07957747f * ((float) Math.sin(center_rad));
                int i2 = 1;
                while (i2 <= 2 - c2) {
                    float rad2 = (((-90.0f) + ((c2 + i2) * 90.0f)) * 3.1415927f) / 180.0f;
                    this.points[(i2 * 2) + 0] = (0.07957747f * ((float) Math.cos(rad2))) + cx2;
                    this.points[(i2 * 2) + 1] = (0.07957747f * ((float) Math.sin(rad2))) + cy;
                    i2++;
                }
                while (i2 <= 8) {
                    this.points[(i2 * 2) + 0] = this.points[((i2 - 1) * 2) + 0] - 0.125f;
                    this.points[(i2 * 2) + 1] = this.points[((i2 - 1) * 2) + 1];
                    i2++;
                }
            }
            this.mFrame = (this.mReverse ? -1 : 1) + this.mFrame;
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.IdentityPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onEnd() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.IdentityPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean isNextAvailable() {
            return this.mReverse ? this.mFrame >= 0 : this.mFrame <= 10;
        }
    }

    /* loaded from: classes.dex */
    public static class Sink extends TFFlexiblePanel.Bezier3DPointBlender {
        private static final int mRotateDegree = 15;
        private static final float mScalar = 1.5f;
        private boolean mNext;
        private int mTicks = 0;
        private float[] XYZ = {0.0f, 0.0f, 0.0f};
        private int[] mAngles = {0, 0, 0};

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onStart() {
            this.mTicks = 0;
            setIdentityPoints(this.mPoints);
            this.mAngles[0] = -30;
            this.mAngles[1] = -90;
            this.mAngles[2] = -135;
            this.mNext = true;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean onFrame(int tick) {
            this.mTicks += tick;
            if (this.mTicks < 100) {
                return false;
            }
            this.mTicks -= 100;
            for (int i = 0; i <= 2; i++) {
                int[] iArr = this.mAngles;
                iArr[i] = iArr[i] + 15;
                if (this.mAngles[i] >= 0) {
                    switch (i) {
                        case 0:
                            newPoint(this.mPoints, 1, 1, this.XYZ);
                            newPoint(this.mPoints, 1, 2, this.XYZ);
                            newPoint(this.mPoints, 2, 1, this.XYZ);
                            newPoint(this.mPoints, 2, 2, this.XYZ);
                            continue;
                        case 1:
                            newPoint(this.mPoints, 0, 1, this.XYZ);
                            newPoint(this.mPoints, 0, 2, this.XYZ);
                            newPoint(this.mPoints, 1, 0, this.XYZ);
                            newPoint(this.mPoints, 1, 3, this.XYZ);
                            newPoint(this.mPoints, 2, 0, this.XYZ);
                            newPoint(this.mPoints, 2, 3, this.XYZ);
                            newPoint(this.mPoints, 3, 1, this.XYZ);
                            newPoint(this.mPoints, 3, 2, this.XYZ);
                            continue;
                        case 2:
                            newPoint(this.mPoints, 0, 0, this.XYZ);
                            newPoint(this.mPoints, 0, 3, this.XYZ);
                            newPoint(this.mPoints, 3, 0, this.XYZ);
                            newPoint(this.mPoints, 3, 3, this.XYZ);
                            if ((this.XYZ[0] * this.XYZ[0]) + (this.XYZ[1] * this.XYZ[1]) < 1.0E-4d) {
                                this.mNext = false;
                                break;
                            } else {
                                continue;
                            }
                    }
                }
            }
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onEnd() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean isNextAvailable() {
            return this.mNext;
        }

        private void newPoint(float[] points, int j, int i, float[] coord) {
            coord[0] = points[(((j * 4) + i) * 3) + 0] - 0.5f;
            coord[1] = points[(((j * 4) + i) * 3) + 1] - 0.5f;
            float x = coord[0] / 1.5f;
            float y = coord[1] / 1.5f;
            coord[0] = (((float) Math.cos(0.2617994f)) * x) - (((float) Math.sin(0.2617994f)) * y);
            coord[1] = (((float) Math.sin(0.2617994f)) * x) + (((float) Math.cos(0.2617994f)) * y);
            points[(((j * 4) + i) * 3) + 0] = coord[0] + 0.5f;
            points[(((j * 4) + i) * 3) + 1] = coord[1] + 0.5f;
        }
    }

    /* loaded from: classes.dex */
    public static class Genie extends TFFlexiblePanel.Bezier3DPointBlender {
        private static final int DIET_STEP = 5;
        private static final int SCALE_STEP = 8;
        private static final int TOT_STEP = 13;
        private float mBasePointX;
        private float mBasePointXMargin;
        private boolean mNext;
        private boolean mReversed;
        private static int TOT_POINTS = 48;
        private static float[] mEffectDataForReverse = new float[TOT_POINTS * 13];
        private int mTicks = 0;
        private int mStep = 0;
        private float[] XYZ = {0.0f, 0.0f, 0.0f};
        private float[] mDietPercent = {1.0f, 1.0f, 0.4f, 0.0f};
        private float[] mOriginPoints = new float[TOT_POINTS];
        private float[] mDumpPoints = new float[TOT_POINTS];

        public Genie(boolean bReversed) {
            this.mReversed = false;
            this.mReversed = bReversed;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onStart() {
            this.mTicks = 0;
            this.mBasePointX = 0.8f;
            this.mBasePointXMargin = 0.02f;
            setIdentityPoints(this.mPoints);
            dumpPoints(this.mPoints, this.mOriginPoints);
            this.mNext = true;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean onFrame(int tick) {
            this.mTicks += tick;
            if (this.mTicks < 50) {
                return false;
            }
            this.mTicks -= 50;
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 4; i++) {
                    if (!this.mReversed) {
                        if (this.mStep < 5) {
                            dietPoints(this.mPoints, j, i, this.XYZ, this.mReversed);
                        } else {
                            if (this.mStep == 5 && j == 0 && i == 0) {
                                dumpPoints(this.mPoints, this.mDumpPoints);
                            }
                            scalePoints(this.mPoints, j, i, this.XYZ, this.mReversed);
                        }
                        savePoints(this.mPoints, j, i);
                    } else {
                        reversePoints(this.mPoints, j, i);
                    }
                }
            }
            if (this.mStep >= 12) {
                this.mNext = false;
            } else {
                this.mStep++;
            }
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onEnd() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.Bezier3DPointBlender, com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean isNextAvailable() {
            return this.mNext;
        }

        private void dietPoints(float[] points, int j, int i, float[] coord, boolean isFromBase) {
            int ix = (((j * 4) + i) * 3) + 0;
            int iy = (((j * 4) + i) * 3) + 1;
            float totLenX = Math.abs(this.mBasePointX - this.mOriginPoints[ix]) - this.mBasePointXMargin;
            float useLenX = totLenX * this.mDietPercent[j];
            float incUnitX = useLenX / 5.0f;
            coord[0] = points[ix];
            coord[1] = points[iy];
            if (!isFromBase) {
                if (points[ix] < this.mBasePointX) {
                    points[ix] = coord[0] + incUnitX;
                } else if (points[ix] > this.mBasePointX) {
                    points[ix] = coord[0] - incUnitX;
                }
            } else if (this.mOriginPoints[ix] < this.mBasePointX) {
                points[ix] = coord[0] - incUnitX;
            } else if (this.mOriginPoints[ix] > this.mBasePointX) {
                points[ix] = coord[0] + incUnitX;
            }
        }

        private void scalePoints(float[] points, int j, int i, float[] coord, boolean isFromBase) {
            int ix = (((j * 4) + i) * 3) + 0;
            int iy = (((j * 4) + i) * 3) + 1;
            float totLenX = Math.abs(this.mBasePointX - this.mDumpPoints[ix]) - this.mBasePointXMargin;
            float incUnitX = totLenX / 8.0f;
            float totLenY = this.mDumpPoints[iy] - 0.0f;
            float incUnitY = totLenY / 8.0f;
            coord[0] = points[ix];
            coord[1] = points[iy];
            if (!isFromBase) {
                if (j != 0) {
                    if (points[ix] < this.mBasePointX) {
                        points[ix] = coord[0] + incUnitX;
                    } else if (points[ix] > this.mBasePointX) {
                        points[ix] = coord[0] - incUnitX;
                    }
                    points[iy] = coord[1] - incUnitY;
                }
            } else if (j != 0) {
                if (this.mDumpPoints[ix] < this.mBasePointX) {
                    points[ix] = coord[0] - incUnitX;
                } else if (this.mDumpPoints[ix] > this.mBasePointX) {
                    points[ix] = coord[0] + incUnitX;
                }
                points[iy] = coord[1] + incUnitY;
            }
        }

        private void dumpPoints(float[] srcPoints, float[] dstPoints) {
            if (srcPoints == null || dstPoints == null) {
                Log.e(TFEffect.TAG, "Error in dumpPoints() points is null");
            }
            for (int i = 0; i < srcPoints.length; i++) {
                dstPoints[i] = srcPoints[i];
            }
        }

        private void savePoints(float[] points, int j, int i) {
            int indexBaseSrc = ((j * 4) + i) * 3;
            int indexBaseDst = (this.mStep * TOT_POINTS) + indexBaseSrc;
            mEffectDataForReverse[indexBaseDst + 0] = points[indexBaseSrc + 0];
            mEffectDataForReverse[indexBaseDst + 1] = points[indexBaseSrc + 1];
            mEffectDataForReverse[indexBaseDst + 2] = points[indexBaseSrc + 2];
        }

        private void reversePoints(float[] points, int j, int i) {
            int indexBaseDst = ((j * 4) + i) * 3;
            int indexBaseSrc = ((12 - this.mStep) * TOT_POINTS) + indexBaseDst;
            points[indexBaseDst + 0] = mEffectDataForReverse[indexBaseSrc + 0];
            points[indexBaseDst + 1] = mEffectDataForReverse[indexBaseSrc + 1];
            points[indexBaseDst + 2] = mEffectDataForReverse[indexBaseSrc + 2];
        }
    }

    /* loaded from: classes.dex */
    public static class TwistParam {
        public final int mDuration;
        public final float mStartDegree;
        public final float mTargetDegree;
        public final int mTwistDuration;
        public final Interpolator mTwistInterpolator;

        public TwistParam(int duration, int twistDuration, float startDegree, float targetDegree, Interpolator twistInterpolator) {
            this.mDuration = duration;
            this.mTwistDuration = twistDuration;
            this.mStartDegree = startDegree;
            this.mTargetDegree = targetDegree;
            this.mTwistInterpolator = twistInterpolator;
        }

        public void setParam(TFTwistEffect twist) {
            twist.setDuration(this.mDuration, this.mTwistDuration);
            twist.setStartDegree(this.mStartDegree);
            twist.setTargetDegree(this.mTargetDegree);
            twist.setTwistInterpolator(this.mTwistInterpolator);
        }
    }

    public void setTwistParam(TwistParam param) {
        this.mTwistParam = param;
    }

    public TwistParam getTwistParam() {
        return this.mTwistParam;
    }

    public void captureDialog(final Dialog dlg) {
        dlg.show();
        Window popupWindow = dlg.getWindow();
        final View decorView = popupWindow.getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.setVisibility(View.INVISIBLE);
        this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffect.31
            @Override // java.lang.Runnable
            public void run() {
                Bitmap dlgBmp = decorView.getDrawingCache();
                if (TFEffect.this.mDialogBitmapListener != null) {
                    TFEffect.this.mDialogBitmapListener.onDialogBitmap(dlgBmp);
                    dlg.dismiss();
                }
            }
        });
    }

    public void setDialogBitmapListener(OnDialogBitmapListener dialogBitmapListener) {
        this.mDialogBitmapListener = dialogBitmapListener;
    }

    public void setAnimationEventListener(AnimationEventListener animationEventListener) {
        this.mAnimationEventListener = animationEventListener;
    }

    public TFPagePanel getPagePanel() {
        return this.mPageCurlPage;
    }

    public void setTouchableMode(boolean mode) {
        this.mTouchableMode = mode;
    }

    public void setDragMode(boolean mode) {
        this.mDragMode = mode;
        Log.i(TAG, "TFEffect setDragMode to " + this.mDragMode);
    }

    private float calcTimeline(float x, float y, float degree) {
        if (Float.isNaN(degree)) {
            return 0.0f;
        }
        float halfW = this.mChildView[0].getWidth() / 2.0f;
        float halfH = this.mChildView[0].getHeight() / 2.0f;
        float[] p = new float[10];
        p[0] = -halfW;
        p[1] = -halfH;
        p[2] = -halfW;
        p[3] = halfH;
        p[4] = halfW;
        p[5] = -halfH;
        p[6] = halfW;
        p[7] = halfH;
        p[8] = x - halfW;
        p[9] = -(y - halfH);
        float xMin = Float.MAX_VALUE;
        float xMax = Float.MIN_VALUE;
        float rad = (float) Math.toRadians(-degree);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        for (int i = 0; i < p.length / 2; i++) {
            float px = p[(i * 2) + 0];
            float py = p[(i * 2) + 1];
            p[(i * 2) + 0] = (px * cos) - (py * sin);
            p[(i * 2) + 1] = (px * sin) + (py * cos);
            if (p[(i * 2) + 0] < xMin) {
                xMin = p[(i * 2) + 0];
            }
            if (p[(i * 2) + 0] > xMax) {
                xMax = p[(i * 2) + 0];
            }
        }
        TFPagePanel.CircularPageOverInterpolator inter = (TFPagePanel.CircularPageOverInterpolator) this.mPageCurlPage.getPageOverInterpolator();
        float r = inter.mR;
        float factor = 1.0f + (3.1415927f * r);
        float dl = (xMax - p[8]) / (xMax - xMin);
        if (dl < 3.1415927f * r) {
            return ((float) Math.sqrt((3.1415927f * dl) * r)) / factor;
        }
        return (((3.1415927f * r) + dl) / 2.0f) / factor;
    }

    private float calcDegree(float x, float y, float startX, float startY) {
        float dx = startX - x;
        float dy = -(startY - y);
        float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
        float dot_x = dx / distance;
        float x_degree = (float) Math.toDegrees(Math.acos(dot_x));
        if (dy < 0.0f) {
            float degree = 360.0f - x_degree;
            return degree;
        }
        return x_degree;
    }

    public void setDragLocation(float x, float y, float startX, float startY) {
        float degree = calcDegree(x, y, startX, startY);
        float t = calcTimeline(x, y, degree);
        setDragAmount(t, (int) degree);
    }

    public void setDragAmount(float t, int degree) {
        if (this.mDragMode && isActive()) {
            switch (this.mEffectKind) {
                case 11:
                case 13:
                case 21:
                case 22:
                    this.mPageCurlPage.setEffectFinishListener(null);
                    this.mPageCurlPage.setDirectionDegree(degree);
                    this.mPageCurlPage.setTimeLine(t);
                    return;
                default:
                    return;
            }
        }
    }

    public void finishDrag(int duration, boolean canceled) {
        if (isActive()) {
            switch (this.mEffectKind) {
                case 11:
                case 13:
                case 21:
                case 22:
                    this.mPageCurlPage.setEffectFinishListener(this.mEffectFinishListener);
                    this.mPageCurlPage.setTimeLine(this.mPageCurlPage.getTimeLine(), canceled ? 0 : 1, duration, null);
                    if (canceled) {
                        View swap = this.mChildView[0];
                        this.mChildView[0] = this.mChildView[1];
                        this.mChildView[1] = swap;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
