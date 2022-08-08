package com.nemustech.tiffany.world;

import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import com.nemustech.tiffany.world.TFTextureInfo;
import com.nemustech.tiffany.world.TFView;
import com.nemustech.tiffany.world.TFWorld;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TFRenderer implements TFView.Renderer {
    private static final int FRAME_RATE_EVAL_PERIOD = 3000;
    private static final String TAG = "TFRenderer";
    private static final long TICK_HOLD_LIMIT = 30;
    private TFWorld.Layer mActiveLayer;
    private TFCamera mCamera;
    private boolean mCancelSelection;
    private long mCurrTick;
    private long mDownTick;
    private long mDragInterval;
    private long mFPSTick;
    private GL10 mGL;
    int mHeight;
    private float mHoldStartX;
    private float mHoldStartY;
    int mLeft;
    boolean mNotDraw;
    private long mPrevDragTick;
    private long mPrevTick;
    TFWorld.OnSelectListener mSelectListener;
    int mTop;
    private boolean mValidDown;
    int mWidth;
    private TFWorld mWorld;
    private static float[] mScratch = new float[8];
    private static long accumulatedDrawingCount = 0;
    private float mDownX = 0.0f;
    private float mDownY = 0.0f;
    private float mUpX = 0.0f;
    private float mUpY = 0.0f;
    private float mStartX = 0.0f;
    private float mStartY = 0.0f;
    private float mEndX = 0.0f;
    private float mEndY = 0.0f;
    private float mTickPassed = 0.0f;
    private float[] mHitTestLine = new float[8];
    private float[] mUnViewportBuffer = new float[32];
    private int mDrawCount = 0;
    private final LinkedList<TFWorld.Layer> mLayer = new LinkedList<>();
    private TFModel mRecentSelectedModel = null;
    private int mRecentSelectedFaceIndex = -1;
    private float[] mMVP = new float[16];
    private float[] mV = new float[4];

    public TFRenderer(TFWorld world, TFCamera camera) {
        this.mWorld = world;
        this.mCamera = camera;
    }

    @Override // com.nemustech.tiffany.world.TFView.Renderer
    public void onDrawFrame(GL10 gl) {
        int tickPassed;
        LinkedList<TFObject> filteredLayer;
        if (!this.mNotDraw) {
            gl.glClear(16640);
            if (this.mWorld.isBlendingMode()) {
                gl.glEnable(3042);
            } else {
                gl.glDisable(3042);
            }
            if (this.mWorld.isDepthTestMode()) {
                gl.glEnable(2929);
            } else {
                gl.glDisable(2929);
            }
            this.mCurrTick = SystemClock.uptimeMillis();
            if (this.mPrevTick == 0) {
                tickPassed = 0;
            } else {
                tickPassed = Math.min(30, (int) (this.mCurrTick - this.mPrevTick));
            }
            this.mPrevTick = this.mCurrTick;
            if (this.mCamera.mChangeStatus) {
                this.mCamera.updateObject(gl, tickPassed, true);
                this.mCamera.updateProjection((TFGL) gl, tickPassed);
            }
            gl.glMatrixMode(5888);
            gl.glEnable(3553);
            gl.glEnable(2832);
            gl.glLoadIdentity();
            if (0 != 0) {
                TFUtils.drawLine(gl, 0.0f, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f);
                TFUtils.drawLine(gl, 0.0f, 0.0f, 0.0f, 0.0f, 5.0f, 0.0f);
                TFUtils.drawLine(gl, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 5.0f);
                TFUtils.drawLine(gl, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f);
                TFUtils.drawLine(gl, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f);
                TFUtils.drawLine(gl, -1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f);
                TFUtils.drawLine(gl, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f);
            }
            if (this.mWorld.mBackground != null) {
                if (this.mWorld.mBackgroundOld != null) {
                    this.mWorld.mBackgroundOld.draw(gl, tickPassed);
                }
                this.mWorld.mBackground.update(gl);
                this.mWorld.mBackground.draw(gl, tickPassed);
            }
            this.mWorld.mTouchDown = this.mDownTick > 0 && this.mCurrTick - this.mDownTick > TICK_HOLD_LIMIT;
            while (true) {
                Runnable r = this.mWorld.getEvent();
                if (r == null) {
                    break;
                }
                r.run();
            }
            Iterator<TFWorld.Layer> it = this.mLayer.iterator();
            while (it.hasNext()) {
                LinkedList<TFObject> layer = (TFWorld.Layer) it.next();
                if (this.mWorld.mBlendingSort) {
                    filteredLayer = new LinkedList<>(layer);
                    Collections.sort(filteredLayer, TFObject.compareAxisZ);
                } else {
                    filteredLayer = layer;
                }
                Iterator i$ = filteredLayer.iterator();
                while (i$.hasNext()) {
                    TFObject o = (TFObject) i$.next();
                    if (o instanceof TFHolder) {
                        TFHolder h = (TFHolder) o;
                        gl.glLoadIdentity();
                        h.draw(gl, tickPassed);
                        h.checkEffectFinish();
                    } else {
                        TFModel m = (TFModel) o;
                        gl.glLoadIdentity();
                        m._draw(gl, tickPassed);
                        m.checkEffectFinish();
                    }
                }
            }
            this.mWorld.getCamera().checkEffectFinish();
            if (this.mDrawCount < 2) {
                this.mPrevTick = 0L;
                this.mDrawCount++;
                if (this.mDrawCount == 2 && this.mWorld.mPostDrawListener != null) {
                    Log.d(TAG, "PostDrawListener status run");
                    this.mWorld.mPostDrawListener.run();
                    this.mWorld.mPostDrawListener = null;
                }
            }
            accumulatedDrawingCount++;
            long elapsedTick = this.mCurrTick - this.mFPSTick;
            if (elapsedTick > 3000) {
                Log.v(TAG, ((accumulatedDrawingCount * 1000) / (this.mCurrTick - this.mFPSTick)) + " fps.");
                accumulatedDrawingCount = 0L;
                this.mFPSTick = this.mCurrTick;
                return;
            }
            return;
        }
        while (true) {
            Runnable r2 = this.mWorld.getEvent();
            if (r2 != null) {
                r2.run();
            } else {
                return;
            }
        }
    }

    public TFWorld getWorld() {
        return this.mWorld;
    }

    @Override // com.nemustech.tiffany.world.TFView.Renderer
    public int[] getConfigSpec() {
        if (this.mWorld.isTranslucentMode()) {
            int[] configSpec = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 16, 12344};
            return configSpec;
        }
        int[] configSpec2 = {12325, 16, 12344};
        return configSpec2;
    }

    @Override // com.nemustech.tiffany.world.TFView.Renderer
    public void onSurfaceChanged(GL10 gl, final int width, final int height) {
        TFGL gl2 = (TFGL) gl;
        this.mLeft = 0;
        this.mTop = 0;
        this.mWidth = width;
        this.mHeight = height;
        if (this.mWorld.mViewSizeChangeListener != null) {
            this.mWorld.queueEvent(new Runnable() { // from class: com.nemustech.tiffany.world.TFRenderer.1
                @Override // java.lang.Runnable
                public void run() {
                    TFRenderer.this.mWorld.mViewSizeChangeListener.onViewSizeChanged(width, height);
                    TFRenderer.this.mNotDraw = false;
                }
            });
        }
        Log.d(TAG, "TFView size changed, width:" + this.mWidth + ",height:" + this.mHeight);
        gl2.glViewport(this.mLeft, this.mTop, this.mWidth, this.mHeight);
        TFCamera camera = this.mWorld.getCamera();
        camera.updateProjection(gl2, 0.0f);
    }

    @Override // com.nemustech.tiffany.world.TFView.Renderer
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String strExtension = gl.glGetString(7939);
        Log.v(TAG, "OpenGL extension list : " + strExtension);
        if (strExtension.indexOf("GL_OES_query_matrix") != -1) {
            this.mWorld.setCapability(1);
            Log.i(TAG, "Query matrix enabled. Using hardware matrix calculation");
        } else {
            Log.i(TAG, "Hardware query matrix is not supported. Using Tiffany engine's matrix.");
        }
        gl.glDisable(3024);
        gl.glHint(3152, 4354);
        gl.glClearColor(this.mWorld.mColorBackgroundR, this.mWorld.mColorBackgroundG, this.mWorld.mColorBackgroundB, this.mWorld.mColorBackgroundA);
        gl.glEnable(2884);
        gl.glShadeModel(7425);
        gl.glBlendFunc(770, 771);
        this.mGL = gl;
        if (this.mWorld.mDefaultDelayImageBmp == null) {
            this.mWorld.mDefaultDelayImageBmp = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
            this.mWorld.mDefaultDelayImageBmp.eraseColor(-1);
            this.mWorld.mDefaultDelayImageTextureInfo = TFTextures.createTextureInfo(this.mWorld.mDefaultDelayImageBmp, null);
        }
        TFTextureInfo.TFTextureLayer layer = this.mWorld.mDefaultDelayImageTextureInfo.getLayer(0);
        layer.bTexturized = false;
        TFTextures.setTextureInfo(gl, this.mWorld.mDefaultDelayImageTextureInfo, 9729, 0, this.mWorld.mTextureMagicKey);
        layer.setWidth(this.mWorld.mDefaultDelayImageBmp.getWidth());
        layer.setHeight(this.mWorld.mDefaultDelayImageBmp.getHeight());
        if (this.mWorld.mBackground != null) {
            this.mWorld.mBackground.genTexture(gl);
        }
        if (this.mWorld.mFognizer != null) {
            this.mWorld.queueEvent(this.mWorld.mFognizer);
        }
    }

    @Override // com.nemustech.tiffany.world.TFView.Renderer
    public void onSurfaceDestroyed() {
        initDrawCount();
        this.mWorld.mPaused = true;
    }

    @Override // com.nemustech.tiffany.world.TFView.Renderer
    public void onPause(GL10 gl) {
        this.mPrevTick = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initDrawCount() {
        this.mDrawCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GL10 getGLContext() {
        return this.mGL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFWorld.Layer addLayer() {
        TFWorld tFWorld = this.mWorld;
        tFWorld.getClass();
        TFWorld.Layer layer = new TFWorld.Layer();
        this.mLayer.add(layer);
        return layer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFWorld.Layer getActiveLayer() {
        return this.mActiveLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFWorld.Layer setActiveLayer(TFWorld.Layer layer) {
        TFWorld.Layer previousActiveLayer = this.mActiveLayer;
        this.mActiveLayer = layer;
        return previousActiveLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLayerCount() {
        return this.mLayer.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFWorld.Layer getLayer(int index) {
        return this.mLayer.get(index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setFrontLayer(TFWorld.Layer layer) {
        boolean result = this.mLayer.remove(layer);
        if (result) {
            this.mLayer.add(layer);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(TFModel model) {
        this.mActiveLayer.add(model);
        model.setLayer(this.mActiveLayer);
        this.mWorld.requestRender();
    }

    void remove(TFModel model) {
        if (model.getLayer().remove(model)) {
            model.deleteAllImageResource();
            model.setLayer(null);
            model.mWorld = null;
        }
        this.mWorld.requestRender();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(TFHolder holder) {
        this.mActiveLayer.add(holder);
        holder.setLayer(this.mActiveLayer);
        this.mWorld.requestRender();
    }

    void remove(TFHolder holder) {
        if (!holder.isCloneObject()) {
            removeHelper(holder);
        }
        holder.getLayer().remove(holder);
        holder.setLayer(null);
        this.mWorld.requestRender();
    }

    private void removeHelper(TFHolder holder) {
        if (holder.isTerminalHolder()) {
            for (int i = 0; i < holder.getSlotCount(); i++) {
                holder.getModelInSlot(i).deleteAllImageResource();
            }
            return;
        }
        for (int i2 = 0; i2 < holder.getSlotCount(); i2++) {
            removeHelper(holder.getHolderInSlot(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(TFObject object) {
        if (object instanceof TFModel) {
            remove((TFModel) object);
        } else if (object instanceof TFHolder) {
            remove((TFHolder) object);
        }
    }

    private float getSquare(float[] vertex, int offset, int blobSize, float x, float y) {
        float sumPositive = 0.0f;
        float sumNegative = 0.0f;
        int i = offset;
        while (i < (offset + blobSize) - 2) {
            sumPositive += vertex[i] * vertex[i + 3];
            sumNegative += vertex[i + 1] * vertex[i + 2];
            i += 2;
        }
        float S = Math.abs((sumPositive + ((vertex[i] * y) + (vertex[offset + 1] * x))) - (sumNegative + ((vertex[i + 1] * x) + (vertex[offset] * y)))) / 2.0f;
        return S;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFModel getSelectedModel(float x, float y) {
        TFModel selectedModel = null;
        if (!this.mWorld.mBanQueryingMatrix) {
            this.mRecentSelectedFaceIndex = -1;
            this.mRecentSelectedModel = null;
            float[] near = {0.0f};
            selectedModel = null;
            Log.d(TAG, "++getSelectedModel");
            getHitTestLine(x, y, this.mHitTestLine);
            int layerCount = this.mLayer.size();
            int j = layerCount - 1;
            while (true) {
                if (j < 0) {
                    break;
                }
                LinkedList<TFObject> attachOrder = this.mLayer.get(j);
                int size = attachOrder.size();
                for (int i = size - 1; i >= 0; i--) {
                    TFObject o = attachOrder.get(i);
                    selectedModel = findSelectedModel(o, x, y, -10000.0f, near);
                    if (selectedModel != null) {
                        break;
                    }
                }
                if (selectedModel != null) {
                    this.mRecentSelectedFaceIndex = selectedModel.getHitFace(near);
                    this.mRecentSelectedModel = selectedModel;
                    break;
                }
                j--;
            }
            Log.d(TAG, "--getSelectedModel with " + selectedModel);
        }
        return selectedModel;
    }

    private TFModel findSelectedModel(TFObject object, float x, float y, float nearest, float[] near) {
        TFModel selectedModel = null;
        if (object instanceof TFHolder) {
            TFHolder holder = (TFHolder) object;
            if (holder.isVisible() && holder.isDrawable()) {
                int objectCount = holder.getSlotCount();
                int i = objectCount - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    TFModel temp = findSelectedModel(holder.getObjectInSlot(holder.mDrawOrder == null ? i : holder.mDrawOrder.get(i)), x, y, nearest, near);
                    if (temp == null) {
                        i--;
                    } else {
                        selectedModel = temp;
                        break;
                    }
                }
            }
        } else if (object instanceof TFModel) {
            TFModel model = (TFModel) object;
            if (model.isVisible() && model.isDrawable() && model.isTouchable()) {
                int faceIndex = getSelectedFaceIndex(model, x, y, near);
                if (faceIndex < 0) {
                    return null;
                }
                if (near[0] > nearest) {
                    float nearest2 = near[0];
                    model.mHitFace = faceIndex;
                    selectedModel = model;
                }
            }
        }
        return selectedModel;
    }

    void getRasterizedVertices(float[] matrixModelView, float[] glVertices, float[] rasterizedVertices) {
        computeMVP(matrixModelView, 0);
        int numVertices = glVertices.length / 3;
        for (int i = 0; i < numVertices; i++) {
            mScratch[0] = glVertices[i * 3];
            mScratch[1] = glVertices[(i * 3) + 1];
            mScratch[2] = glVertices[(i * 3) + 2];
            mScratch[3] = 1.0f;
            project(mScratch, 0, mScratch, 4);
            System.arraycopy(mScratch, 4, rasterizedVertices, i * 2, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getFaceVertices(TFModel model, int faceIndex, float[] vertices) {
        computeMVP(model.mMatrix, 0);
        if (vertices.length == 8) {
            float[] rasterVertices = new float[8];
            for (int j = 0; j < 4; j++) {
                int offset = (faceIndex * 12) + (j * 3);
                mScratch[0] = model.mVertexBuffer.get(offset);
                mScratch[1] = model.mVertexBuffer.get(offset + 1);
                mScratch[2] = model.mVertexBuffer.get(offset + 2);
                mScratch[3] = 1.0f;
                project(mScratch, 0, mScratch, 4);
                if (j > 1) {
                    rasterVertices[((4 - j) + 1) * 2] = mScratch[4];
                    rasterVertices[(((4 - j) + 1) * 2) + 1] = mScratch[5];
                } else {
                    rasterVertices[j * 2] = mScratch[4];
                    rasterVertices[(j * 2) + 1] = mScratch[5];
                }
            }
            System.arraycopy(rasterVertices, 0, vertices, 0, 8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSelectedFaceIndex(TFModel model, float x, float y, float[] near) {
        model.updateHitTestLine(this.mHitTestLine);
        model.updateHitPoint();
        int ret = model.getHitFace(near);
        return ret;
    }

    public int handleTap(float x, float y) {
        if (this.mRecentSelectedModel != null && this.mRecentSelectedFaceIndex != -1) {
            Log.d(TAG, "Touch select");
            this.mRecentSelectedModel.handleTap(this.mRecentSelectedFaceIndex, x, y);
            return 0;
        }
        return 0;
    }

    private void prvDrag(TFModel selectedModel, float sx, float sy, float ex, float ey, int tickPassed) {
        if (selectedModel != null) {
            if (!selectedModel.getLockStatus()) {
                if (selectedModel.mParentHolder != null) {
                    selectedModel.mParentHolder.handleDrag(selectedModel, sx, sy, ex, ey, tickPassed);
                } else {
                    selectedModel.handleDrag(sx, sy, ex, ey, tickPassed);
                }
            } else {
                Log.v(TAG, "Drag on locked model.");
            }
        }
        this.mWorld.requestRender();
    }

    private void prvDown(TFModel selectedModel, float x, float y) {
        if (selectedModel != null) {
            if (!selectedModel.getLockStatus()) {
                this.mValidDown = selectedModel.isGoingToStop();
                Log.d(TAG, "Touch Down");
                if (selectedModel.mParentHolder != null) {
                    selectedModel.mParentHolder.handleDown(selectedModel, this.mRecentSelectedFaceIndex, x, y);
                } else {
                    selectedModel.handleDown(this.mRecentSelectedFaceIndex, x, y);
                }
            } else {
                Log.d(TAG, "Down detected, but the selectedModel is locked!!");
            }
        }
        this.mWorld.requestRender();
    }

    private void prvUp(TFModel selectedModel, float x, float y) {
        if (selectedModel != null) {
            if (!selectedModel.getLockStatus()) {
                Log.d(TAG, "Touch Up on " + selectedModel);
                if (selectedModel.mParentHolder != null) {
                    selectedModel.mParentHolder.handleUp(selectedModel, x, y);
                } else {
                    selectedModel.handleUp(x, y);
                }
            } else {
                Log.d(TAG, "Locked!!");
            }
        }
        this.mWorld.requestRender();
    }

    public void handleDown(float x, float y, long currentTick) {
        this.mDownX = x;
        this.mDownY = y;
        this.mHoldStartX = x;
        this.mHoldStartY = y;
        this.mDownTick = currentTick;
        this.mCancelSelection = false;
        float tx = this.mDownX;
        float ty = this.mDownY;
        this.mRecentSelectedModel = getSelectedModel(tx, ty);
        prvDown(this.mRecentSelectedModel, tx, ty);
    }

    public void handleUp(float x, float y) {
        this.mDownTick = 0L;
        this.mUpX = x;
        this.mUpY = y;
        float tx = this.mUpX;
        float ty = this.mUpY;
        prvUp(this.mRecentSelectedModel, tx, ty);
        Log.d(TAG, "mValidDown : " + this.mValidDown);
        if (Math.abs(tx - this.mDownX) < 25.0f && Math.abs(ty - this.mDownY) < 25.0f && this.mValidDown && !this.mCancelSelection) {
            handleTap(tx, ty);
        }
    }

    public void handleDrag(float start_x, float start_y, float end_x, float end_y, long currentTick) {
        this.mStartX = start_x;
        this.mStartY = start_y;
        this.mEndX = end_x;
        this.mEndY = end_y;
        if (this.mPrevDragTick == 0) {
            this.mDragInterval = 0L;
        } else {
            this.mDragInterval = currentTick - this.mPrevDragTick;
        }
        this.mPrevDragTick = currentTick;
        if (!this.mCancelSelection && (Math.abs(end_x - this.mHoldStartX) >= 25.0f || Math.abs(end_y - this.mHoldStartY) >= 25.0f)) {
            this.mDownTick = currentTick;
            this.mHoldStartX = end_x;
            this.mHoldStartY = end_y;
            if (start_x != this.mDownX || start_y != this.mDownY) {
                this.mStartX = this.mDownX;
                this.mStartY = this.mDownY;
            }
            this.mCancelSelection = true;
        }
        if (this.mCancelSelection) {
            float tStartX = this.mStartX;
            float tStartY = this.mStartY;
            float tEndX = this.mEndX;
            float tEndY = this.mEndY;
            int tTickPassed = (int) this.mDragInterval;
            if (this.mRecentSelectedModel == null) {
                this.mRecentSelectedModel = getSelectedModel(tEndX, tEndY);
            }
            prvDrag(this.mRecentSelectedModel, tStartX, tStartY, tEndX, tEndY, tTickPassed);
        }
    }

    private void getMatrix(GL10 gl, int mode, float[] mat) {
        TFGL gl2 = (TFGL) gl;
        gl2.glMatrixMode(mode);
        gl2.getMatrix(mat, 0);
    }

    private void computeMVP(float[] modelView, int modelViewOffset) {
        TFCamera camera = this.mWorld.getCamera();
        Matrix.multiplyMM(this.mMVP, 0, camera.mMatrix, 0, modelView, 0);
    }

    private void project(float[] obj, int objOffset, float[] win, int winOffset) {
        Matrix.multiplyMV(this.mV, 0, this.mMVP, 0, obj, objOffset);
        float rw = 1.0f / this.mV[3];
        win[winOffset] = this.mLeft + (((this.mV[0] * rw) + 1.0f) * this.mWidth * 0.5f);
        win[winOffset + 1] = this.mTop + (this.mHeight * ((this.mV[1] * rw) + 1.0f) * 0.5f);
        win[winOffset + 2] = ((this.mV[2] * rw) + 1.0f) * 0.5f;
    }

    private boolean unProject(float winx, float winy, float winz, float[] model, int offsetM, float[] proj, int offsetP, int[] viewport, int offsetV, float[] xyz, int offset) {
        float[] _tempGluUnProjectData = new float[40];
        _tempGluUnProjectData[32] = (((winx - viewport[offsetV]) * 2.0f) / viewport[offsetV + 2]) - 1.0f;
        _tempGluUnProjectData[33] = (((winy - viewport[offsetV + 1]) * 2.0f) / viewport[offsetV + 3]) - 1.0f;
        _tempGluUnProjectData[34] = (2.0f * winz) - 1.0f;
        _tempGluUnProjectData[35] = 1.0f;
        Matrix.multiplyMM(_tempGluUnProjectData, 16, proj, offsetP, model, offsetM);
        Matrix.invertM(_tempGluUnProjectData, 0, _tempGluUnProjectData, 16);
        Matrix.multiplyMV(_tempGluUnProjectData, 36, _tempGluUnProjectData, 0, _tempGluUnProjectData, 32);
        if (_tempGluUnProjectData[39] == 0.0d) {
            return false;
        }
        xyz[offset] = _tempGluUnProjectData[36] / _tempGluUnProjectData[39];
        xyz[offset + 1] = _tempGluUnProjectData[37] / _tempGluUnProjectData[39];
        xyz[offset + 2] = _tempGluUnProjectData[38] / _tempGluUnProjectData[39];
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getHitTestLine(float x, float y, float[] hitLine) {
        getHitTestLine(x, y, hitLine, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getHitTestLine(float x, float y, float[] hitLine, int hitLineOffset) {
        float[] m = this.mUnViewportBuffer;
        float f = (((x - this.mLeft) * 2.0f) / this.mWidth) - 1.0f;
        m[28] = f;
        m[24] = f;
        float f2 = 1.0f - (((y - this.mTop) * 2.0f) / this.mHeight);
        m[29] = f2;
        m[25] = f2;
        m[26] = 0.0f;
        m[30] = 1.0f;
        m[31] = 1.0f;
        m[27] = 1.0f;
        float[] cameraMatrix = this.mWorld.getCamera().mMatrix;
        if (!Matrix.invertM(m, 8, cameraMatrix, 0)) {
        }
        Matrix.multiplyMV(m, 0, m, 8, m, 24);
        Matrix.multiplyMV(m, 4, m, 8, m, 28);
        hitLine[hitLineOffset + 0] = m[0] / m[3];
        hitLine[hitLineOffset + 1] = m[1] / m[3];
        hitLine[hitLineOffset + 2] = m[2] / m[3];
        hitLine[hitLineOffset + 3] = m[3] / m[3];
        hitLine[hitLineOffset + 4] = m[4] / m[7];
        hitLine[hitLineOffset + 5] = m[5] / m[7];
        hitLine[hitLineOffset + 6] = m[6] / m[7];
        hitLine[hitLineOffset + 7] = m[7] / m[7];
    }
}
