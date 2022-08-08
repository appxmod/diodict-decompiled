package com.nemustech.tiffany.world;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.nemustech.tiffany.world.TFView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFWorld {
    public static final int ATTACH_ORDER = 100;
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 2;
    static final int CAP_QUERY_MATRIX = 1;
    public static final int DEPTH_ORDER = 101;
    public static final float EPSILON = 1.0E-4f;
    public static final int FREEZE_ALL_VELOCITY = 255;
    public static final int FREEZE_MOVING_VELOCITY = 1;
    public static final int FREEZE_ROTATION_VELOCITY = 2;
    public static final int IMAGE_DIRECTION_BOTTOM = 3;
    public static final int IMAGE_DIRECTION_LEFT = 1;
    public static final int IMAGE_DIRECTION_RIGHT = 2;
    public static final int IMAGE_DIRECTION_TOP = 0;
    public static final int MOVE = 0;
    public static final int RENDER_AFFINITY_BALANCE = 0;
    public static final int RENDER_AFFINITY_INTERACTION = 2;
    public static final int RENDER_AFFINITY_PERFORMANCE = 1;
    public static final int REVERSE_ALL = 3;
    public static final int REVERSE_HORIZONTAL = 1;
    public static final int REVERSE_NONE = 0;
    public static final int REVERSE_VERTICAL = 2;
    public static final int ROTATE = 1;
    public static final int ROTATE_DECREASE = 1;
    public static final int ROTATE_FASTWAY = 2;
    public static final int ROTATE_INCREASE = 0;
    static final String TAG = "TFWorld";
    static final float TAP_ALLOW_RANGE = 25.0f;
    static final int TIFFANY_WORLD_VERSION_MAJOR = 3;
    static final int TIFFANY_WORLD_VERSION_MIDDLE = 0;
    static final int TIFFANY_WORLD_VERSION_MINOR = 32;
    private long mBackgroundFadeDuration;
    boolean mBlendingMode;
    boolean mBlendingSort;
    private TFCamera mCamera;
    private int mCapability;
    float mColorBackgroundA;
    float mColorBackgroundB;
    float mColorBackgroundG;
    float mColorBackgroundR;
    TFTextureInfo mDefaultDelayImageTextureInfo;
    private float mDepth;
    private boolean mDepthTestMode;
    private boolean mEconomicMode;
    OnEffectFinishListener mEffectFinishListener;
    private TFEventHandler mEventHandler;
    Runnable mFognizer;
    TFView mGLSurfaceView;
    private Handler mHandler;
    private float mHeight;
    private boolean mIsLocked;
    boolean mLoadTextureInAdvance;
    boolean mPaused;
    Runnable mPostDrawListener;
    float mReflectingFloor;
    float mReflectionOpacity;
    TFRenderer mRenderer;
    OnSelectListener mSelectListener;
    private BitmapDrawable mStartupDrawable;
    int mTextureFilter;
    boolean mTextureIsLoading;
    boolean mTouchDown;
    float mTouchedModelColorMaskB;
    float mTouchedModelColorMaskG;
    float mTouchedModelColorMaskR;
    boolean mTranslucentMode;
    View.OnTouchListener mUserTouchListener;
    OnViewSizeChangeListener mViewSizeChangeListener;
    private float mWidth;
    TFBackground mBackground = null;
    TFBackground mBackgroundOld = null;
    Bitmap mDefaultDelayImageBmp = null;
    boolean mTouchedModelColorMask = false;
    boolean mBanQueryingMatrix = false;
    private int mRenderAffinity = 1;
    int mTextureMagicKey = 0;
    private ArrayList<Runnable> mEventQueue = new ArrayList<>();
    private int mLongPressDuration = 1500;
    boolean mReflection = false;

    /* loaded from: classes.dex */
    public interface OnEffectFinishListener {
        void onEffectFinish(TFObject tFObject);
    }

    /* loaded from: classes.dex */
    public interface OnSelectListener {
        boolean onSelected(TFModel tFModel, int i);
    }

    /* loaded from: classes.dex */
    public interface OnViewSizeChangeListener {
        void onViewSizeChanged(int i, int i2);
    }

    public TFWorld(float width, float height, float depth) {
        create(width, height, depth);
        setBackgroundColor(0.0f, 0.0f, 0.0f, 1.0f);
        setTranslucentMode(false);
        banChiselFringe(false);
        setEconomicMode(true);
        setDepthTestMode(true);
    }

    public void lock() {
        this.mIsLocked = true;
    }

    public void unlock() {
        this.mIsLocked = false;
    }

    public boolean isLocked() {
        return this.mIsLocked;
    }

    public boolean banQueryingMatrix(boolean mode) {
        boolean pre = this.mBanQueryingMatrix;
        this.mBanQueryingMatrix = mode;
        return pre;
    }

    public void banChiselFringe(boolean mode) {
        if (mode) {
            this.mTextureFilter = 9728;
        } else {
            this.mTextureFilter = 9729;
        }
    }

    public void addReflectingFloor(float y, float opacity) {
        this.mReflection = true;
        this.mReflectingFloor = y;
        this.mReflectionOpacity = opacity;
    }

    public Layer addLayer() {
        return this.mRenderer.addLayer();
    }

    public int getLayerCount() {
        return this.mRenderer.getLayerCount();
    }

    public Layer getLayer(int index) {
        return this.mRenderer.getLayer(index);
    }

    public Layer setActiveLayer(Layer layer) {
        return this.mRenderer.setActiveLayer(layer);
    }

    public Layer getFrontLayer() {
        return this.mRenderer.getLayer(this.mRenderer.getLayerCount() - 1);
    }

    public Layer getActiveLayer() {
        return this.mRenderer.getActiveLayer();
    }

    public boolean setFrontLayer(Layer layer) {
        return this.mRenderer.setFrontLayer(layer);
    }

    public void spreadFog(float start, float end) {
        final float fogStart = -start;
        final float fogEnd = -end;
        this.mFognizer = new Runnable() { // from class: com.nemustech.tiffany.world.TFWorld.1
            @Override // java.lang.Runnable
            public void run() {
                GL10 gl = TFWorld.this.mRenderer.getGLContext();
                if (gl == null) {
                    throw new IllegalArgumentException("Void GL context.");
                }
                float[] fogColor = {0.0f, 0.0f, 0.0f, 1.0f};
                gl.glFogx(2917, 9729);
                gl.glFogfv(2918, fogColor, 0);
                gl.glHint(3156, 4352);
                gl.glFogf(2915, fogStart);
                gl.glFogf(2916, fogEnd);
                gl.glEnable(2912);
            }
        };
        queueEvent(this.mFognizer);
    }

    public void clearFog() {
        this.mFognizer = null;
        Runnable defognizer = new Runnable() { // from class: com.nemustech.tiffany.world.TFWorld.2
            @Override // java.lang.Runnable
            public void run() {
                GL10 gl = TFWorld.this.mRenderer.getGLContext();
                if (gl == null) {
                    throw new IllegalArgumentException("Void GL context.");
                }
                gl.glDisable(2912);
            }
        };
        queueEvent(defognizer);
    }

    public TFModel getHitModel(float x, float y) {
        if (this.mRenderer != null) {
            return this.mRenderer.getSelectedModel(x, y);
        }
        return null;
    }

    public void setObjectIndex(TFObject object, int newIndex) {
        if (object instanceof TFModel) {
            TFModel model = (TFModel) object;
            if (model.getHolder() != null) {
                return;
            }
        }
        LinkedList<TFObject> layer = object.getLayer();
        layer.remove(object);
        layer.add(newIndex, object);
    }

    public void removeReflectingFloor() {
        this.mReflection = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCapability(int capability) {
        this.mCapability |= capability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCapable(int capability) {
        return (this.mCapability & capability) != 0;
    }

    private void applyEconomicMode() {
        if (this.mGLSurfaceView != null) {
            if (this.mEconomicMode) {
                this.mGLSurfaceView.setRenderMode(0);
            } else {
                this.mGLSurfaceView.setRenderMode(1);
            }
            this.mGLSurfaceView.requestRender();
        }
    }

    public TFError show(View TiffanyView) {
        return show(TiffanyView, false);
    }

    public TFError show(View TiffanyView, boolean notDraw) {
        this.mGLSurfaceView = (TFView) TiffanyView;
        this.mGLSurfaceView.setRenderAffinity(this.mRenderAffinity);
        if (this.mStartupDrawable != null) {
            this.mGLSurfaceView.setBackgroundDrawable(this.mStartupDrawable);
        }
        this.mGLSurfaceView.setGLWrapper(new TFView.GLWrapper() { // from class: com.nemustech.tiffany.world.TFWorld.3
            @Override // com.nemustech.tiffany.world.TFView.GLWrapper
            public GL wrap(GL gl) {
                return new TFGL(gl, TFWorld.this);
            }
        });
        this.mRenderer.mSelectListener = this.mSelectListener;
        this.mRenderer.mNotDraw = notDraw;
        if (this.mTranslucentMode) {
            Log.i(TAG, "Translucent mode is activated");
            this.mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            this.mGLSurfaceView.getHolder().setFormat(-3);
        } else {
            Log.i(TAG, "Running in non-translucent mode");
        }
        this.mGLSurfaceView.setRenderer(this.mRenderer);
        applyEconomicMode();
        this.mEventHandler = new TFEventHandler(this, this.mRenderer);
        this.mGLSurfaceView.setOnTouchListener(this.mEventHandler.mTouchListener);
        int version = getVersion();
        Log.i(TAG, "Tiffany world(" + this + ") version : " + ((16711680 & version) >> 16) + "." + ((65280 & version) >> 8) + "." + (version & 255));
        Log.i(TAG, "Lincensed to Diotek.com");
        return TFError.ERROR_NONE;
    }

    public boolean isEconomicMode() {
        return this.mEconomicMode;
    }

    public void setRenderAffinity(int affinity) {
        Log.d(TAG, "Render affinity has been changed to " + affinity);
        this.mRenderAffinity = affinity;
        if (this.mGLSurfaceView != null) {
            this.mGLSurfaceView.setRenderAffinity(affinity);
        }
    }

    public void setEconomicMode(boolean economicMode) {
        this.mEconomicMode = economicMode;
        applyEconomicMode();
    }

    public void setTranslucentMode(boolean mode) {
        this.mTranslucentMode = mode;
    }

    public boolean isTranslucentMode() {
        return this.mTranslucentMode;
    }

    public void setTouchedModelColorMasking(float red, float green, float blue) {
        this.mTouchedModelColorMask = true;
        this.mTouchedModelColorMaskR = red;
        this.mTouchedModelColorMaskG = green;
        this.mTouchedModelColorMaskB = blue;
    }

    public void unsetTouchedModelColorMasking() {
        this.mTouchedModelColorMask = false;
    }

    public void setBackgroundColor(float red, float green, float blue, float alpha) {
        this.mColorBackgroundR = red;
        this.mColorBackgroundG = green;
        this.mColorBackgroundB = blue;
        this.mColorBackgroundA = alpha;
    }

    private void create(float width, float height, float depth) {
        TFUtils.loadLibrary();
        this.mWidth = width;
        this.mHeight = height;
        this.mDepth = depth;
        this.mCamera = TFCamera.getCamera();
        this.mCamera.attatch(this);
        this.mCamera.setLens(0.175f, 0.5f);
        this.mCamera.locate(0.0f, 0.0f, 4.0f);
        this.mRenderer = new TFRenderer(this, this.mCamera);
        Layer mainLayer = this.mRenderer.addLayer();
        this.mRenderer.setActiveLayer(mainLayer);
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    public void pause() {
        if (this.mGLSurfaceView != null) {
            this.mGLSurfaceView.onPause();
            this.mPaused = true;
            Log.i(TAG, "TiffanyWorld has been paused.");
            return;
        }
        Log.w(TAG, "Tried to pause TiffanyWorld, but no GL surface view found");
    }

    public void resume() {
        if (this.mPaused) {
            if (this.mGLSurfaceView != null) {
                this.mPaused = false;
                this.mGLSurfaceView.onResume();
                requestRender();
                Log.i(TAG, "TiffanyWorld has been resumed");
                return;
            }
            Log.w(TAG, "Tried to resume TiffanyWorld, but no GL surface view found");
            return;
        }
        Log.w(TAG, "Tried to resume TiffanyWorld which is already active.");
    }

    public void stop() {
    }

    public void setLongPressDuration(int milliseconds) {
        this.mLongPressDuration = milliseconds;
    }

    public int getLongPressDuration() {
        return this.mLongPressDuration;
    }

    public void setStartupImage(Bitmap startupImage) {
        Log.i(TAG, "SetStartupImage!!");
        this.mStartupDrawable = new BitmapDrawable(startupImage);
        this.mHandler = new Handler();
        setPostDrawListener(new Runnable() { // from class: com.nemustech.tiffany.world.TFWorld.4
            @Override // java.lang.Runnable
            public void run() {
                Log.i(TFWorld.TAG, "SetStartupImage works!!");
                TFWorld.this.mGLSurfaceView.setBackgroundDrawable(null);
                TFWorld.this.mStartupDrawable = null;
            }
        });
    }

    public void setPostDrawListener(Runnable postDrawListener) {
        if (this.mRenderer != null) {
            this.mRenderer.initDrawCount();
        }
        this.mPostDrawListener = postDrawListener;
    }

    public void setViewSizeChangeListener(OnViewSizeChangeListener viewSizeChangeListener) {
        this.mViewSizeChangeListener = viewSizeChangeListener;
    }

    public void setSelectListener(OnSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mUserTouchListener = onTouchListener;
    }

    public void setEffectFinishListener(OnEffectFinishListener effectFinishListener) {
        this.mEffectFinishListener = effectFinishListener;
    }

    public static int getVersion() {
        return 196640;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public float getDepth() {
        return this.mDepth;
    }

    public TFCamera getCamera() {
        return this.mCamera;
    }

    public void setDepthTestMode(boolean mode) {
        this.mDepthTestMode = mode;
    }

    public boolean isDepthTestMode() {
        return this.mDepthTestMode;
    }

    public void setBlendingMode(boolean mode) {
        setBlendingMode(mode, true);
    }

    public void setBlendingMode(boolean mode, boolean sortZ) {
        this.mBlendingMode = mode;
        if (!this.mBlendingMode) {
            sortZ = false;
        }
        this.mBlendingSort = sortZ;
    }

    public boolean isBlendingMode() {
        return this.mBlendingMode;
    }

    public void setBackgroundFadeEffect(long duration) {
        this.mBackgroundFadeDuration = duration;
    }

    private TFBackground getBackgroundInstance(TFWorld world) {
        if (this.mBackground == null) {
            this.mBackground = new TFBackground(this);
        } else if (this.mBackgroundFadeDuration > 0 && this.mBackground.mRealized) {
            if (this.mBackgroundOld != null) {
                this.mBackgroundOld.deleteImageResource(0);
            }
            this.mBackgroundOld = this.mBackground;
            this.mBackground = new TFBackground(this);
            this.mBackgroundOld.setNeedToBeUpdated(false);
        }
        this.mBackground.setNeedToBeUpdated(true);
        return this.mBackground;
    }

    public TFBackground getBackground() {
        return this.mBackground;
    }

    public boolean setBackgroundBitmap(Bitmap bitmap) {
        TFBackground background = getBackgroundInstance(this);
        return background.setImageResource(0, bitmap);
    }

    public TFError setBackgroundImageResource(Resources resources, int resource_id) {
        TFBackground background = getBackgroundInstance(this);
        return background.setImageResource(0, resources, resource_id);
    }

    public TFError setBackgroundImageResource(String fileName) {
        TFBackground background = getBackgroundInstance(this);
        return background.setImageResource(0, fileName);
    }

    /* loaded from: classes.dex */
    public class Layer extends LinkedList<TFObject> {
        public Layer() {
        }
    }

    /* loaded from: classes.dex */
    public class TFBackground extends TFModel {
        private static final String TAG = "TFBackground";
        protected boolean mChanged = false;
        protected float mZ = 0.0f;
        private boolean mRealized = false;

        public TFBackground(TFWorld world) {
            this.mWorld = world;
        }

        protected void updateVertex() {
            float[] v = {-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f};
            this.mVertexBuffer = TFUtils.newFloatBuffer(v.length);
            this.mVertexBuffer.put(v);
            this.mVertexBuffer.position(0);
        }

        protected void updateLocation() {
            int scrWidth = this.mWorld.mRenderer.mWidth;
            int scrHeight = this.mWorld.mRenderer.mHeight;
            float depth = this.mWorld.getDepth();
            float[] m = new float[16];
            this.mWorld.mRenderer.getHitTestLine(scrWidth / 2, scrHeight / 2, m, 0);
            this.mWorld.mRenderer.getHitTestLine(scrWidth, scrHeight, m, 8);
            this.mZ = m[6] + ((depth / 65536.0f) * 20.0f);
            this.mWidth = (m[12] - m[4]) * 2.0f;
            this.mHeight = (-(m[13] - m[5])) * 2.0f;
        }

        public void setNeedToBeUpdated(boolean bAppearing) {
            if (!bAppearing) {
                if (TFWorld.this.mBackgroundFadeDuration > 0) {
                    fade(0.0f, TFWorld.this.mBackgroundFadeDuration);
                    return;
                }
                return;
            }
            this.mChanged = true;
            if (TFWorld.this.mBackgroundFadeDuration > 0) {
                setOpacity(0.0f);
                fade(1.0f, TFWorld.this.mBackgroundFadeDuration);
            }
        }

        public void update(GL10 gl) {
            if (this.mChanged) {
                updateVertex();
                updateLocation();
                this.mChanged = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.nemustech.tiffany.world.TFModel
        public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
            float newRight = rectTexture.right / textureWidth;
            float newBottom = rectTexture.bottom / textureHeight;
            Log.i(TAG, "$$$ texWidth : " + newRight + " texHeight : " + newBottom);
            float[] t = {0.0f, newBottom, newRight, newBottom, 0.0f, 0.0f, newRight, 0.0f};
            this.mTextureBuffer = TFUtils.newFloatBuffer(t.length);
            this.mTextureBuffer.put(t);
            this.mTextureBuffer.position(0);
        }

        public void draw(GL10 gl, int tickPassed) {
            applyOpacity(gl, tickPassed);
            this.mTextures.setTextureByIndex(gl, 0);
            gl.glEnable(2884);
            gl.glCullFace(1029);
            gl.glEnableClientState(32884);
            gl.glEnableClientState(32888);
            gl.glDisableClientState(32886);
            gl.glDisableClientState(32885);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glTranslatef(0.0f, 0.0f, this.mZ);
            gl.glScalef(this.mWidth, this.mHeight, 1.0f);
            gl.glVertexPointer(2, 5126, 0, this.mVertexBuffer);
            gl.glTexCoordPointer(2, 5126, 0, this.mTextureBuffer);
            gl.glColor4f(1.0f, 1.0f, 1.0f, this.mOpacity);
            gl.glDrawArrays(5, 0, this.mVertexBuffer.capacity() / 2);
            gl.glMatrixMode(5888);
            gl.glPopMatrix();
            this.mRealized = true;
        }
    }

    public void setDefaultDelayImage(Context context, int resId) {
        this.mDefaultDelayImageBmp = TFUtils.decodeResource(context.getResources(), resId);
        this.mDefaultDelayImageTextureInfo = TFTextures.createTextureInfo(this.mDefaultDelayImageBmp, null);
    }

    public void deleteDefaultDelayImage() {
        this.mDefaultDelayImageBmp = null;
    }

    public void queueEvent(Runnable r) {
        synchronized (this) {
            this.mEventQueue.add(r);
            requestRender();
        }
    }

    public void clearEventQueue() {
        synchronized (this) {
            this.mEventQueue.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable getEvent() {
        Runnable remove;
        synchronized (this) {
            remove = this.mEventQueue.size() > 0 ? this.mEventQueue.remove(0) : null;
        }
        return remove;
    }

    public void requestRender() {
        if (!this.mEconomicMode || this.mGLSurfaceView == null) {
            return;
        }
        this.mGLSurfaceView.requestRender();
    }

    public int getViewWidth() {
        return this.mRenderer.mWidth;
    }

    public int getViewHeight() {
        return this.mRenderer.mHeight;
    }

    public void toWorldCoord(int[] uiCoord, float[] worldCoord, int offsetX, int offsetY) {
        int width = getViewWidth();
        int height = getViewHeight();
        int length = width > height ? width : height;
        float worldLength = width > height ? getWidth() : getHeight();
        int vx = uiCoord[0] - offsetX;
        int vy = uiCoord[1] - offsetY;
        int vw = uiCoord[2];
        int vh = uiCoord[3];
        worldCoord[0] = (((((-width) / 2) + vx) + (vw / 2)) * worldLength) / length;
        worldCoord[1] = ((((height / 2) - vy) - (vh / 2)) * worldLength) / length;
        worldCoord[2] = (vw * worldLength) / length;
        worldCoord[3] = (vh * worldLength) / length;
    }

    public void toWorldCoord(int[] uiCoord, float[] worldCoord) {
        toWorldCoord(uiCoord, worldCoord, 0, 0);
    }

    public void locateCameraAtFullScreen() {
        Log.i(TAG, "LocateCameraAtFullScreen");
        int width = getViewWidth();
        int height = getViewHeight();
        int length = width > height ? width : height;
        float worldLength = width > height ? getWidth() : getHeight();
        float z = TFUtils.calcZ(this, width, height, length, worldLength);
        getCamera().locate(2, -z, true);
    }

    public boolean dispatchEvent(MotionEvent e) {
        return this.mGLSurfaceView.dispatchTouchEvent(e);
    }

    public void printWorldState() {
        printWorldState(false);
    }

    public void printWorldState(boolean absolute) {
        int layerCount = getLayerCount();
        Log.d("HOLDER_STAT", "World---------------------------------------------- START\n");
        for (int i = 0; i < layerCount; i++) {
            LinkedList<TFObject> layer = getLayer(i);
            Log.d("HOLDER_STAT", "On layer : " + i + " (" + layer + ")");
            Iterator i$ = layer.iterator();
            while (i$.hasNext()) {
                TFObject o = i$.next();
                if (o instanceof TFHolder) {
                    TFHolder h = (TFHolder) o;
                    h.printHolderState(0, absolute);
                }
                if (o instanceof TFModel) {
                    TFModel m = (TFModel) o;
                    m.printModelState();
                }
            }
        }
        Log.d("HOLDER_STAT", "-------------------------- END --------------------------\n");
    }
}
