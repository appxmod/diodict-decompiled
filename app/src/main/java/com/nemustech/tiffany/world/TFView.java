package com.nemustech.tiffany.world;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLDebugHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.diotek.diodict.utils.CMN;

import java.io.Writer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFView extends SurfaceView implements SurfaceHolder.Callback {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    private static String TAG = "TFView";
    private int mDebugFlags;
    private EGLConfigChooser mEGLConfigChooser;
    private EglHelper mEglHelper;
    private boolean mEglStarted;
    private GL10 mGLToDraw;
    private GLWrapper mGLWrapper;
    private boolean mHasSurface;
    private boolean mPaused;
    private int mRenderAffinity;
    private int mRenderOrder;
    private Renderer mRenderer;
    private boolean mRequestRender;
    private boolean mSurfaceChanged;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private boolean mTellRendererSurfaceCreated;
    protected Runnable mRunToDrawFrame = new Runnable() { // from class: com.nemustech.tiffany.world.TFView.1
        @Override // java.lang.Runnable
        public void run() {
            TFView.this.drawFrame();
        }
    };
    private int mRenderMode = 0;
    private boolean mRenderPaused = true;

    /* loaded from: classes.dex */
    public interface EGLConfigChooser {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    /* loaded from: classes.dex */
    public interface GLWrapper {
        GL wrap(GL gl);
    }

    /* loaded from: classes.dex */
    public interface Renderer {
        int[] getConfigSpec();

        void onDrawFrame(GL10 gl10);

        void onPause(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);

        void onSurfaceDestroyed();
    }

    public TFView(Context context) {
        super(context);
        init();
        TFJniUtils.verifyContext(context);
    }

    public TFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TFJniUtils.verifyContext(context);
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(2);
        this.mEglHelper = new EglHelper();
    }

    /* loaded from: classes.dex */
    private static abstract class BaseConfigChooser implements EGLConfigChooser {
        protected int[] mConfigSpec;

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] configSpec) {
            this.mConfigSpec = configSpec;
        }

        @Override // com.nemustech.tiffany.world.TFView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            egl.eglChooseConfig(display, this.mConfigSpec, null, 0, num_config);
            int numConfigs = num_config[0];
            if (numConfigs <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] configs = new EGLConfig[numConfigs];
            egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config);
            EGLConfig config = chooseConfig(egl, display, configs);
            if (config == null) {
                throw new IllegalArgumentException("No config chosen");
            }
            return config;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344});
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        @Override // com.nemustech.tiffany.world.TFView.BaseConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            EGLConfig closestConfig = null;
            int closestDistance = 1000;
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, 12325, 0);
                int s = findConfigAttrib(egl, display, config, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int r = findConfigAttrib(egl, display, config, 12324, 0);
                    int g = findConfigAttrib(egl, display, config, 12323, 0);
                    int b = findConfigAttrib(egl, display, config, 12322, 0);
                    int a = findConfigAttrib(egl, display, config, 12321, 0);
                    int distance = Math.abs(r - this.mRedSize) + Math.abs(g - this.mGreenSize) + Math.abs(b - this.mBlueSize) + Math.abs(a - this.mAlphaSize);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestConfig = config;
                    }
                }
            }
            return closestConfig;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                int defaultValue2 = this.mValue[0];
                return defaultValue2;
            }
            return defaultValue;
        }
    }

    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        this.mEGLConfigChooser = configChooser;
    }

    public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
        setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize));
    }

    public void setGLWrapper(GLWrapper glWrapper) {
        this.mGLWrapper = glWrapper;
    }

    public void setDebugFlags(int debugFlags) {
        this.mDebugFlags = debugFlags;
    }

    public int getDebugFlags() {
        return this.mDebugFlags;
    }

    public void setRenderer(Renderer renderer) {
        this.mRenderer = renderer;
        if (this.mTellRendererSurfaceCreated) {
            Log.d(TAG, "surfaceCreated by setting renderer.");
            this.mTellRendererSurfaceCreated = false;
            this.mHasSurface = true;
            if (!this.mEglStarted) {
                startEGL();
            }
        }
        requestRender();
    }

    public void setRenderMode(int renderMode) {
        this.mRenderMode = renderMode;
        if (renderMode == 1) {
            requestRender();
        }
    }

    public int getRenderMode() {
        return this.mRenderMode;
    }

    public void requestRender() {
        if (getVisibility() == 0 && !this.mRequestRender) {
            if (this.mRenderPaused) {
                this.mRenderPaused = false;
                prvRequestRender();
                return;
            }
            this.mRequestRender = true;
        }
    }

    private void prvRequestRender() {
        switch (this.mRenderAffinity) {
            case 0:
                if (this.mRenderOrder == 0) {
                    this.mRenderOrder = 1;
                    post(this.mRunToDrawFrame);
                    return;
                }
                this.mRenderOrder = 0;
                invalidate();
                return;
            case 1:
                post(this.mRunToDrawFrame);
                return;
            case 2:
                postInvalidate();
                return;
            default:
                return;
        }
    }

    public void renderNow() {
        drawFrame();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        if (this.mRenderer == null) {
            Log.w(TAG, "surfaceCreated but no renderer found!");
            this.mTellRendererSurfaceCreated = true;
            return;
        }
        Log.d(TAG, "surfaceCreated successfully");
        this.mHasSurface = true;
        if (!this.mEglStarted) {
            startEGL();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");
        this.mHasSurface = false;
        if (this.mRenderer != null) {
            this.mRenderer.onSurfaceDestroyed();
        }
        finishEGL();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, "surfaceChanged(" + w + ", " + h + ")");
        this.mSurfaceChanged = true;
        this.mSurfaceWidth = w;
        this.mSurfaceHeight = h;
        invalidate();
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
        this.mPaused = true;
        this.mRenderPaused = true;
        this.mRequestRender = false;
        removeCallbacks(this.mRunToDrawFrame);
        finishEGL();
    }

    private void startEGL() {
        if (!this.mEglStarted) {
            Log.d(TAG, "EGL start");
            this.mEglHelper.start(this.mRenderer.getConfigSpec());
            this.mEglStarted = true;
            return;
        }
        Log.w(TAG, "EGL already started");
    }

    private void finishEGL() {
        if (this.mEglStarted) {
            Log.d(TAG, "EGL finish");
            this.mEglHelper.finish();
            this.mEglStarted = false;
            this.mGLToDraw = null;
            ((TFRenderer) this.mRenderer).getWorld().mTextureMagicKey++;
            return;
        }
        Log.w(TAG, "EGL already finished");
    }

    public void onResume() {
        Log.d(TAG, "onResume()");
        this.mPaused = false;
        if (this.mRenderer != null) {
            startEGL();
        }
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow()");
        super.onDetachedFromWindow();
        finishEGL();
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawFrame();
    }

    protected void drawFrame() {
		CMN.debug("drawFrame!!!");
        if (!this.mPaused && this.mHasSurface && this.mEglStarted) {
            if (this.mGLToDraw == null) {
                this.mGLToDraw = (GL10) this.mEglHelper.createSurface(getHolder());
                this.mRenderer.onSurfaceCreated(this.mGLToDraw, this.mEglHelper.mEglConfig);
                this.mSurfaceChanged = true;
            }
            if (this.mSurfaceChanged) {
                this.mRenderer.onSurfaceChanged(this.mGLToDraw, this.mSurfaceWidth, this.mSurfaceHeight);
                this.mSurfaceChanged = false;
            }
            if (getVisibility() == 0 && this.mRenderer != null && this.mEglHelper.mEglContext != null) {
                this.mRequestRender = false;
                this.mRenderer.onDrawFrame(this.mGLToDraw);
                if (!this.mPaused) {
                    this.mEglHelper.swap();
                }
                if (this.mRenderMode == 1 || this.mRequestRender) {
                    prvRequestRender();
                    return;
                }
                this.mRenderPaused = true;
                if (!this.mHasSurface) {
                    Log.d(TAG, "TFView stopped, has surface : " + this.mHasSurface);
                }
                this.mRenderer.onPause(this.mGLToDraw);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class EglHelper {
        EGL10 mEgl;
        EGLConfig mEglConfig;
        EGLContext mEglContext;
        EGLDisplay mEglDisplay;
        EGLSurface mEglSurface;

        public EglHelper() {
        }

        public void start(int[] configSpec) {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            int[] version = new int[2];
            this.mEgl.eglInitialize(this.mEglDisplay, version);
            EGLConfig[] configs = new EGLConfig[1];
            int[] num_config = new int[1];
            this.mEgl.eglChooseConfig(this.mEglDisplay, configSpec, configs, 1, num_config);
            this.mEglConfig = configs[0];
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL10.EGL_NO_CONTEXT, null);
            this.mEglSurface = null;
        }

        public GL createSurface(SurfaceHolder holder) {
            if (this.mEglSurface != null) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
            }
            this.mEglSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, holder, null);
            this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext);
            GL gl = this.mEglContext.getGL();
            if (TFView.this.mGLWrapper != null) {
                gl = TFView.this.mGLWrapper.wrap(gl);
            }
            if ((TFView.this.mDebugFlags & 3) != 0) {
                int configFlags = 0;
                Writer log = null;
                if ((TFView.this.mDebugFlags & 1) != 0) {
                    configFlags = 0 | 1;
                }
                if ((TFView.this.mDebugFlags & 2) != 0) {
                    log = new LogWriter();
                }
                return GLDebugHelper.wrap(gl, configFlags, log);
            }
            return gl;
        }

        public boolean swap() {
            this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
            return this.mEgl.eglGetError() != 12302;
        }

        public void finish() {
            if (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
                this.mEglSurface = null;
            }
            if (this.mEglContext != null) {
                this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
                this.mEglContext = null;
            }
            if (this.mEglDisplay != null) {
                this.mEgl.eglTerminate(this.mEglDisplay);
                this.mEglDisplay = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LogWriter extends Writer {
        private StringBuilder mBuilder = new StringBuilder();

        LogWriter() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            flushBuilder();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            flushBuilder();
        }

        @Override // java.io.Writer
        public void write(char[] buf, int offset, int count) {
            for (int i = 0; i < count; i++) {
                char c = buf[offset + i];
                if (c == '\n') {
                    flushBuilder();
                } else {
                    this.mBuilder.append(c);
                }
            }
        }

        private void flushBuilder() {
            if (this.mBuilder.length() > 0) {
                Log.v("TFView", this.mBuilder.toString());
                this.mBuilder.delete(0, this.mBuilder.length());
            }
        }
    }

    public void setRenderAffinity(int affinity) {
        this.mRenderAffinity = affinity;
    }
}
