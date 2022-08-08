package com.nemustech.tiffany.world;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.nemustech.tiffany.world.TFPagePanel;
import com.nemustech.tiffany.world.TFWorld;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class TFPageFlipper {
    private static final String TAG = "TFPageFlipper";
    private AnimationEventListener mAnimationEventListener;
    private BitmapDrawable mBackBitmapDrawable;
    private boolean mBlending;
    private Context mContext;
    private FrameLayout mDecorLayout;
    private boolean mDepthTest;
    private boolean mEconomic;
    private TFWorld.OnEffectFinishListener mEffectFinishListener;
    private TFWindow mEffectWindow;
    private ImageProvider mImageProvider;
    private int mParam1;
    private int mParam2;
    private TFView mSurfaceView;
    private int mTotalPages;
    private float mUnitPanelHeight;
    private float mUnitPanelWidth;
    private WindowManager mWindowMgr;
    private TFWorld mWorld;
    private boolean mPageRightToLeft = true;
    private Interpolator mEffectInterpolator = null;
    private boolean mActive = false;
    private boolean mEffecting = false;
    private int mOrientation = 1;
    private LinkedList<View> mViewList = new LinkedList<>();
    private LinkedList<TFPagePanel> mPagePanelList = new LinkedList<>();
    private Handler mHandler = new Handler();
    private boolean mDragMode = true;
    private int mPageIndex = 0;
    private float mPageWidth = 0.0f;
    private float mPageHeight = 0.0f;

    /* loaded from: classes.dex */
    public interface AnimationEventListener {
        void onAnimationEnd(boolean z);

        void onAnimationStart(boolean z);
    }

    /* loaded from: classes.dex */
    public interface ImageProvider {
        void setImage(TFModel tFModel, View view, int i);
    }

    public TFPageFlipper(Context context) {
        this.mContext = context;
        this.mWindowMgr = (WindowManager) context.getSystemService("window");
        this.mEffectWindow = new TFWindow(context);
        this.mEffectWindow.setAttributes(((Activity) context).getWindow().getAttributes());
        this.mEffectWindow.addFlags(8);
        this.mEffectWindow.addFlags(16);
        this.mDecorLayout = (FrameLayout) this.mEffectWindow.getDecorView();
        TFUtils.loadLibrary();
        System.gc();
    }

    public void setPageSize(float width, float height) {
        this.mPageWidth = width;
        this.mPageHeight = height;
    }

    public void addView(View v, int index) {
        addView(v, null, index);
    }

    public void addView(View v, Bitmap bmp, int index) {
        if (bmp == null) {
            if (v != null) {
                v.setDrawingCacheEnabled(true);
                Bitmap cacheBmp = v.getDrawingCache();
                if (cacheBmp != null) {
                    makeImagePanel(Bitmap.createBitmap(cacheBmp));
                }
            } else {
                return;
            }
        } else {
            if (index == 0) {
                this.mBackBitmapDrawable = new BitmapDrawable(this.mContext.getResources(), Bitmap.createBitmap(bmp));
            }
            makeImagePanel(bmp);
        }
        this.mViewList.add(v);
    }

    public void setEffectParam(int param1, int param2, Interpolator interpolator) {
        this.mParam1 = param1;
        this.mParam2 = param2;
        this.mEffectInterpolator = interpolator;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public void setTotalPages(int totPages) {
        this.mTotalPages = totPages;
    }

    public boolean show() {
        this.mSurfaceView = new TFView(this.mContext);
        this.mSurfaceView.setBackgroundDrawable(this.mBackBitmapDrawable);
        this.mDecorLayout.addView(this.mSurfaceView);
        this.mWorld = new TFWorld(2.0f, 2.0f, 7.0f);
        this.mWorld.lock();
        int no = this.mPagePanelList.size();
        for (int j = no - 1; j >= 0; j--) {
            TFPagePanel panel = this.mPagePanelList.get(j);
            panel.attachTo(this.mWorld);
            panel.setPageShadow(0.25f, 0.9f);
        }
        this.mWorld.setViewSizeChangeListener(new TFWorld.OnViewSizeChangeListener() { // from class: com.nemustech.tiffany.world.TFPageFlipper.1
            @Override // com.nemustech.tiffany.world.TFWorld.OnViewSizeChangeListener
            public void onViewSizeChanged(int width, int height) {
                Log.d(TFPageFlipper.TAG, "View size changed to width:" + width + " height:" + height);
                TFCamera camera = TFPageFlipper.this.mWorld.getCamera();
                float z = TFUtils.calcZ(TFPageFlipper.this.mWorld, width, height, (int) TFPageFlipper.this.mPageWidth, TFPageFlipper.this.mUnitPanelWidth);
                camera.locate(2, -z, true);
            }
        });
        this.mWorld.setPostDrawListener(new Runnable() { // from class: com.nemustech.tiffany.world.TFPageFlipper.2
            @Override // java.lang.Runnable
            public void run() {
                if (TFPageFlipper.this.mAnimationEventListener != null) {
                    TFPageFlipper.this.mAnimationEventListener.onAnimationStart(false);
                }
                TFPageFlipper.this.mSurfaceView.setBackgroundDrawable(null);
                TFPageFlipper.this.mBackBitmapDrawable = null;
            }
        });
        this.mDepthTest = false;
        this.mEconomic = true;
        this.mBlending = true;
        setAnimationWindow();
        this.mWorld.banQueryingMatrix(true);
        this.mWorld.removeReflectingFloor();
        this.mWorld.setDepthTestMode(this.mDepthTest);
        this.mWorld.setEconomicMode(this.mEconomic);
        this.mWorld.setBlendingMode(this.mBlending, false);
        this.mWorld.show(this.mSurfaceView);
        this.mActive = true;
        return true;
    }

    public boolean showEffect(boolean bReversed) {
        boolean result = false;
        if (!this.mEffecting) {
            showEffectPageCurl(bReversed, this.mParam1, this.mParam2, this.mEffectInterpolator);
            result = true;
        }
        Log.d(TAG, "### showEffect() mEffecting=" + this.mEffecting);
        return result;
    }

    public View getCurrentView() {
        return this.mViewList.getFirst();
    }

    public void pause() {
        if (this.mWorld != null) {
            this.mWorld.pause();
        }
    }

    public void resume() {
        if (this.mWorld != null) {
            this.mWorld.resume();
        }
    }

    public void stop() {
        if (this.mWorld != null) {
            this.mWorld.stop();
        }
    }

    public void destroy() {
        if (this.mActive) {
            int len = this.mPagePanelList.size();
            for (int i = 0; i < len; i++) {
                TFPagePanel panel = this.mPagePanelList.get(i);
                panel.deleteAllImageResource();
            }
            for (int i2 = 0; i2 < len; i2++) {
                this.mPagePanelList.removeLast();
            }
            this.mDecorLayout.removeView(this.mSurfaceView);
            this.mWindowMgr.removeView(this.mDecorLayout);
            this.mActive = false;
            this.mWorld = null;
        }
    }

    public void enterPageFlipper() {
        if (!this.mActive && this.mWorld == null && show()) {
            prepareReadyPage(this.mPageRightToLeft);
        }
    }

    public void exitPageFlipper() {
        if (this.mActive && this.mWorld != null) {
            int len = this.mPagePanelList.size();
            for (int i = 0; i < len; i++) {
                TFPagePanel panel = this.mPagePanelList.get(i);
                panel.deleteAllImageResource();
            }
            for (int i2 = 0; i2 < len; i2++) {
                this.mPagePanelList.removeLast();
            }
            this.mHandler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFPageFlipper.3
                @Override // java.lang.Runnable
                public void run() {
                    TFPageFlipper.this.mDecorLayout.removeView(TFPageFlipper.this.mSurfaceView);
                    TFPageFlipper.this.mWindowMgr.removeView(TFPageFlipper.this.mDecorLayout);
                    TFPageFlipper.this.mActive = false;
                    TFPageFlipper.this.mWorld = null;
                }
            });
        }
    }

    public boolean isActive() {
        return this.mActive;
    }

    public boolean isEffecting() {
        return this.mEffecting;
    }

    public void finishDrag(int duration, boolean canceled, boolean isRightToLeft) {
        int i = 0;
        int i2 = 1;
        this.mPageRightToLeft = isRightToLeft;
        this.mEffecting = true;
        TFPagePanel curPagePanel = this.mPagePanelList.getFirst();
        if (canceled) {
            curPagePanel.setEffectFinishListener(null);
            Log.d(TAG, "### finishDrag f_canceled=" + canceled);
            float timeLine = curPagePanel.getTimeLine();
            if (canceled) {
                i2 = 0;
            }
            curPagePanel.setTimeLine(timeLine, i2, duration, null);
            this.mEffecting = false;
            return;
        }
        curPagePanel.setEffectFinishListener(this.mEffectFinishListener);
        float timeLine2 = curPagePanel.getTimeLine();
        if (!canceled) {
            i = 1;
        }
        curPagePanel.setTimeLine(timeLine2, i, duration, null);
    }

    public void setDragAmount(float t, int degree) {
        if (this.mDragMode) {
            TFPagePanel curPagePanel = this.mPagePanelList.getFirst();
            curPagePanel.setEffectFinishListener(null);
            curPagePanel.setDirectionDegree(degree);
            curPagePanel.setTimeLine(t);
        }
    }

    public void setImageProvider(ImageProvider iProvider) {
        this.mImageProvider = iProvider;
    }

    public TFWorld getWorld() {
        return this.mWorld;
    }

    public void setEffectingDone() {
        this.mEffecting = false;
        Log.d(TAG, "### updatePage() mEffecting=" + this.mEffecting);
    }

    private void makeImagePanel(Bitmap bmp) {
        this.mUnitPanelWidth = 1.0f;
        this.mUnitPanelHeight = this.mPageHeight / this.mPageWidth;
        TFPagePanel panel = new TFPagePanel(this.mUnitPanelWidth, this.mUnitPanelHeight);
        panel.setBorderShadow(0.03f, 0.5f);
        panel.setBackMaskOpacity(0.5f);
        panel.reverseImage(1, 2);
        panel.setImageResource(0, bmp);
        this.mPagePanelList.add(panel);
    }

    private void showEffectPageCurl(boolean bReversed, int degree, int duration, Interpolator interpolator) {
        TFPagePanel curPagePanel = this.mPagePanelList.getFirst();
        curPagePanel.lock();
        curPagePanel.setDirectionDegree(degree);
        curPagePanel.setOnUpdateVertexListener(new TFPagePanel.OnUpdateVertexListener() { // from class: com.nemustech.tiffany.world.TFPageFlipper.4
            @Override // com.nemustech.tiffany.world.TFPagePanel.OnUpdateVertexListener
            public void beforeUpdate(TFPagePanel pagePanel) {
                int degree2 = pagePanel.getDirectionDegree();
                float t = pagePanel.getTimeLine();
                int maxDegree = TFPageFlipper.getMaxDegree(pagePanel, t);
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
        curPagePanel.setTimeLine(bReversed ? 1.0f : 0.0f);
        curPagePanel.setTimeLine(curPagePanel.getTimeLine(), this.mDragMode ? bReversed ? 1 : 0 : bReversed ? 0 : 1, duration, interpolator);
        this.mEffectFinishListener = new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFPageFlipper.5
            @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
            public void onEffectFinish(TFObject object) {
                Log.d(TFPageFlipper.TAG, "### onEffectFinish() mEffecting=" + TFPageFlipper.this.mEffecting);
                TFPagePanel curPagePanel2 = (TFPagePanel) TFPageFlipper.this.mPagePanelList.getFirst();
                curPagePanel2.setEffectFinishListener(null);
                curPagePanel2.unlock();
                if (TFPageFlipper.this.mImageProvider != null) {
                    TFPageFlipper.this.shiftList(TFPageFlipper.this.mPageRightToLeft);
                    TFPageFlipper.this.mPageIndex = TFPageFlipper.this.getNextIndex(TFPageFlipper.this.mPageIndex, TFPageFlipper.this.mTotalPages, TFPageFlipper.this.mPageRightToLeft);
                    TFPageFlipper.this.prepareReadyPage(TFPageFlipper.this.mPageRightToLeft);
                }
            }
        };
        curPagePanel.setEffectFinishListener(this.mEffectFinishListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareReadyPage(boolean isRightToLeft) {
        if (this.mImageProvider != null) {
            TFPagePanel readyPage = getNextPage(this.mPageRightToLeft);
            int nextPageIndex = getNextIndex(this.mPageIndex, this.mTotalPages, this.mPageRightToLeft);
            View nextView = getNextView(this.mPageRightToLeft);
            this.mImageProvider.setImage(readyPage, nextView, nextPageIndex);
        }
    }

    private TFPagePanel getNextPage(boolean isRightToLeft) {
        int size = this.mPagePanelList.size();
        if (isRightToLeft) {
            TFPagePanel resultPage = this.mPagePanelList.get(1);
            return resultPage;
        }
        TFPagePanel resultPage2 = this.mPagePanelList.get(size - 1);
        return resultPage2;
    }

    private View getNextView(boolean isRightToLeft) {
        int size = this.mViewList.size();
        if (isRightToLeft) {
            View resultView = this.mViewList.get(1);
            return resultView;
        }
        View resultView2 = this.mViewList.get(size - 1);
        return resultView2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNextIndex(int curIndex, int totNo, boolean isRightToLeft) {
        if (isRightToLeft) {
            int resultIndex = curIndex + 1;
            if (resultIndex >= totNo) {
                return 0;
            }
            return resultIndex;
        }
        int resultIndex2 = curIndex - 1;
        if (resultIndex2 < 0) {
            return totNo - 1;
        }
        return resultIndex2;
    }

    private void adjustModelIndex() {
        int size = this.mPagePanelList.size();
        int i = 0;
        int j = size - 1;
        while (i < size) {
            TFPagePanel panel = this.mPagePanelList.get(i);
            this.mWorld.setObjectIndex(panel, j);
            i++;
            j--;
        }
    }

    private void revertPagePanel(TFPagePanel panel) {
        TFPagePanel panel2 = this.mPagePanelList.getLast();
        TFPagePanel panel3 = panel2;
        panel3.setTimeLine(0.0f);
        panel3.look(0.0f, 0.0f);
        panel3.locate(0.0f, 0.0f, 0.0f);
        this.mWorld.requestRender();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shiftList(boolean isRightToLeft) {
        shiftViewList(isRightToLeft);
        shiftPagePanelList(isRightToLeft);
    }

    private void shiftViewList(boolean isRightToLeft) {
        if (isRightToLeft) {
            View movingView = this.mViewList.removeFirst();
            this.mViewList.addLast(movingView);
            return;
        }
        View movingView2 = this.mViewList.removeLast();
        this.mViewList.addFirst(movingView2);
    }

    private void shiftPagePanelList(boolean isRightToLeft) {
        if (isRightToLeft) {
            TFPagePanel movingPanel = this.mPagePanelList.removeFirst();
            this.mPagePanelList.addLast(movingPanel);
        } else {
            TFPagePanel movingPanel2 = this.mPagePanelList.removeLast();
            this.mPagePanelList.addFirst(movingPanel2);
        }
        adjustModelIndex();
        revertPagePanel(this.mPagePanelList.getLast());
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

    private void setAnimationWindow() {
        WindowManager.LayoutParams wl = this.mEffectWindow.getAttributes();
        wl.height = (int) this.mPageHeight;
        wl.y = TFUtils.getStatusBarHeight(this.mContext);
        Log.d(TAG, "Couldn't determine root view. Use in activity transition");
        this.mWindowMgr.addView(this.mDecorLayout, wl);
    }

    public void setAnimationEventListener(AnimationEventListener animationEventListener) {
        this.mAnimationEventListener = animationEventListener;
    }
}
