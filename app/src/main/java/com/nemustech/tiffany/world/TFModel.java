package com.nemustech.tiffany.world;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.Matrix;
import android.os.Handler;
import android.util.Log;

import com.diotek.diodict.utils.CMN;
import com.nemustech.tiffany.world.TFTextureInfo;
import com.nemustech.tiffany.world.TFWorld;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public abstract class TFModel extends TFObject {
    private static final String TAG = "TFModel";
    private static final Handler mHandler = new Handler();
    private static float mMirrorY;
    private static float mObjectY;
    protected boolean mBeautyReflection;
    private float mColorMaskB;
    private float mColorMaskG;
    private float mColorMaskR;
    JitImageProvider mJitImageProvider;
    private boolean mLockImageCache;
    private float mLongDownX;
    private float mLongDownY;
    private boolean mLongPressValid;
    private OnTouchListener mOnTouchListener;
    private float mTapAbsX;
    private float mTapAbsY;
    protected FloatBuffer mTextureBuffer;
    int[] mTextureFilter;
    protected boolean mTranslucentMode;
    FloatBuffer mVertexBuffer;
    float[] mHitPoint = new float[8];
    int mHitFace = -1;
    float[] mHitTestLine = new float[8];
    float[] mUnMVBuffer = new float[16];
    protected int mTouchedIndex = -1;
    private Runnable mLongPressHandler = new Runnable() { // from class: com.nemustech.tiffany.world.TFModel.3
        @Override // java.lang.Runnable
        public void run() {
			CMN.debug("fatal mLongPressHandler!");
            if (TFModel.this.mLongPressValid) {
                TFModel.this.mOnTouchListener.onLongPressdown(TFModel.this, TFModel.this.mTouchedIndex, TFModel.this.mLongDownX, TFModel.this.mLongDownY);
            }
        }
    };
    private boolean mColorMask = false;
    private boolean mTouchable = true;
    boolean mFacingFront = false;
    protected TFTextures mTextures = new TFTextures(this, 2);
    protected boolean mShowBackFace = true;

    /* loaded from: classes.dex */
    public interface JitImageProvider {
        Bitmap getImage(int i);
    }

    /* loaded from: classes.dex */
    public interface OnTouchListener {
        boolean onLongPressdown(TFModel tFModel, int i, float f, float f2);

        boolean onSelected(TFModel tFModel, int i, float f, float f2);

        boolean onTouchDown(TFModel tFModel, int i, float f, float f2);

        boolean onTouchDrag(TFModel tFModel, int i, float f, float f2, float f3, float f4, int i2);

        boolean onTouchUp(TFModel tFModel, int i, float f, float f2);
    }

    public TFModel() {
        this.mVisible = true;
        this.mShouldDraw = true;
        this.mOpacity = 1.0f;
        this.mItemIndex = -1;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public Object clone() throws CloneNotSupportedException {
        TFModel clone = (TFModel) super.clone();
        clone.mCloneObject = true;
        clone.mHitPoint = new float[8];
        clone.mHitTestLine = new float[8];
        clone.mUnMVBuffer = new float[16];
        clone.mParentHolder = null;
        Log.d("CLONE", "Cloning model: " + this + " -> " + clone);
        return clone;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void attachTo(TFHolder parentHolder, int index) {
        if (this.mParentHolder != null) {
            throw new IllegalStateException("Already attached to a holder");
        }
        TFWorld.Layer layer = getLayer();
        if (layer != null) {
            layer.remove(this);
            setLayer(null);
        }
        parentHolder.addModel(this, index);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void attachTo(TFHolder parentHolder) {
        attachTo(parentHolder, parentHolder.getSlotCount());
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void attachTo(TFWorld world) {
        if (this.mParentHolder != null) {
            throw new IllegalStateException("Detach from its parent holder first");
        }
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }
        this.mWorld = world;
        this.mWorld.mRenderer.add(this);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void detachFrom(TFWorld world) {
        deleteAllImageResource();
        super.detachFrom(world);
    }

    public Bitmap getFaceImage(int faceIndex) {
        TFTextureInfo texInfo = this.mTextures.getTextureInfo(faceIndex);
        if (texInfo != null) {
            if (texInfo.mLayer[0].bmp != null) {
                return texInfo.mLayer[0].bmp;
            }
            if (this.mJitImageProvider != null) {
                return this.mJitImageProvider.getImage(faceIndex);
            }
            if (texInfo.mLayer[0].resource_id != 0) {
                return TFUtils.decodeResource(texInfo.mLayer[0].resources, texInfo.mLayer[0].resource_id);
            }
        }
        return null;
    }

    public TFTextureInfo getTextureInfo(int faceIndex) {
        return this.mTextures.getTextureInfo(faceIndex);
    }

    public boolean isSafeToRecycleFaceImage(int faceIndex) {
        TFTextureInfo texInfo = this.mTextures.getTextureInfo(faceIndex);
        return texInfo != null && texInfo.mLayer[0].bmp == null;
    }

    public int getIndex() {
        return 0;
    }

    public void setJitImageProvider(JitImageProvider jitImageProvider) {
        this.mJitImageProvider = jitImageProvider;
    }

    public JitImageProvider getJitImageProvider() {
        return this.mJitImageProvider;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    public void setColorMasking(float red, float green, float blue) {
        this.mColorMask = true;
        this.mColorMaskR = red;
        this.mColorMaskG = green;
        this.mColorMaskB = blue;
    }

    public void unsetColorMasking() {
        this.mColorMask = false;
    }

    public void setBackFaceVisibility(boolean visibility) {
        this.mShowBackFace = visibility;
    }

    public boolean isBackFaceVisible() {
        return this.mShowBackFace;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean isGoingToStop() {
        if (this.mParentHolder == null) {
            return super.isGoingToStop();
        }
        return super.isGoingToStop() && this.mParentHolder.isGoingToStop();
    }

    protected boolean onBeforeDraw(GL10 gl, int tickPassed) {
        return true;
    }

    protected boolean onDraw(GL10 gl, int tickPassed) {
        return true;
    }

    protected void onAfterDraw(GL10 gl, int tickPassed) {
    }

    protected int onCullFace(int index, boolean reflection) {
        return index == 0 ? reflection ? 1028 : 1029 : reflection ? 1029 : 1028;
    }

    protected void onCalcReflection(GL10 gl, float y) {
        gl.glRotatef(-this.mAngle[1], 1.0f, 0.0f, 0.0f);
        gl.glRotatef(-this.mAngle[2], 0.0f, 0.0f, 1.0f);
        gl.glTranslatef(0.0f, -((2.0f * y) + this.mHeight), 0.0f);
        gl.glScalef(1.0f, -1.0f, 1.0f);
        gl.glRotatef(this.mAngle[1], 1.0f, 0.0f, 0.0f);
        gl.glRotatef(this.mAngle[2], 0.0f, 0.0f, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSetOpacity(GL10 gl, float opacity, boolean reflection) {
        if (reflection) {
            float finalOpacity = opacity * this.mWorld.mReflectionOpacity;
            gl.glColor4f(finalOpacity, finalOpacity, finalOpacity, 1.0f);
        } else if (this.mWorld.isBlendingMode()) {
            gl.glColor4f(1.0f, 1.0f, 1.0f, opacity);
        } else {
            gl.glColor4f(opacity, opacity, opacity, 1.0f);
        }
    }

    protected void onDrawVertex(GL10 gl, int index, boolean reflection) {
        gl.glDrawArrays(5, index * 4, 4);
    }

    protected void onSetTexture(GL10 gl, int index, int layer) {
        this.mTextures.setTextureByIndex(gl, index, layer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void _draw(GL10 gl, int tickPassed) {
        int layerCount;
        if (this.mVisible && this.mShouldDraw) {
            if (this.mWorld == null) {
                throw new IllegalStateException("Tried to render a TFModel which is not attached to a world.");
            }
            if (onBeforeDraw(gl, tickPassed)) {
                if (updateObject(gl, tickPassed, true)) {
                    applyOpacity(gl, tickPassed);
                }
                if (onDraw(gl, tickPassed)) {
                    int maxTextureIndex = Math.max(0, this.mTextures.getMaxTextureIndex());
                    if (this.mShowBackFace && maxTextureIndex == 0) {
                        gl.glDisable(2884);
                    } else {
                        gl.glEnable(2884);
                    }
                    if (this.mFacingFront) {
                        float rotationX = -this.mAngle[0];
                        float rotationY = -this.mAngle[1];
                        boolean rotateYFirst = false;
                        if (this.mParentHolder != null) {
                            rotationX -= this.mParentHolder.mAngle[0];
                            rotationY -= this.mParentHolder.mAngle[1];
                            rotateYFirst = this.mParentHolder.mFacingFrontYFirst;
                        }
                        if (rotateYFirst) {
                            gl.glRotatef(rotationY, 1.0f, 0.0f, 0.0f);
                            gl.glRotatef(rotationX, 0.0f, 1.0f, 0.0f);
                        } else {
                            gl.glRotatef(rotationX, 0.0f, 1.0f, 0.0f);
                            gl.glRotatef(rotationY, 1.0f, 0.0f, 0.0f);
                        }
                    }
                    if (!this.mWorld.mBanQueryingMatrix) {
                        TFGL gl2 = (TFGL) gl;
                        gl2.getMatrix(this.mMatrix, 0);
                    }
                    gl.glEnable(3553);
                    gl.glEnableClientState(32884);
                    gl.glEnableClientState(32888);
                    gl.glVertexPointer(3, 5126, 0, this.mVertexBuffer);
                    gl.glTexCoordPointer(2, 5126, 0, this.mTextureBuffer);
                    for (int i = 0; i <= maxTextureIndex; i++) {
                        TFTextureInfo texInfo = this.mTextures.getTextureInfo(i);
                        if (texInfo != null) {
                            layerCount = texInfo.getLayerCount();
                        } else {
                            layerCount = 1;
                        }
                        for (int l = 0; l < layerCount; l++) {
                            onSetTexture(gl, i, l);
                            if (this.mWorld.mReflection) {
                                mObjectY = this.mLocation[1];
                                if (this.mParentHolder != null) {
                                    mObjectY += this.mParentHolder.mLocation[1];
                                }
                                mMirrorY = (mObjectY - (this.mHeight / 2.0f)) - this.mWorld.mReflectingFloor;
                                if (mMirrorY >= 0.0f && mMirrorY < this.mHeight) {
                                    onSetOpacity(gl, this.mOpacity, true);
                                    gl.glPushMatrix();
                                    gl.glCullFace(onCullFace(i, true));
                                    onCalcReflection(gl, mMirrorY);
                                    onDrawVertex(gl, i, true);
                                    gl.glPopMatrix();
                                }
                            }
                            if (this.mWorld.mTouchedModelColorMask && this.mTouchedIndex == i) {
                                gl.glColor4f(this.mWorld.mTouchedModelColorMaskR, this.mWorld.mTouchedModelColorMaskG, this.mWorld.mTouchedModelColorMaskB, this.mOpacity);
                            } else if (this.mColorMask) {
                                gl.glColor4f(this.mColorMaskR, this.mColorMaskG, this.mColorMaskB, this.mOpacity);
                            } else {
                                onSetOpacity(gl, this.mOpacity, false);
                            }
                            gl.glCullFace(onCullFace(i, false));
                            gl.glDisableClientState(32886);
                            onDrawVertex(gl, i, false);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void genTexture(GL10 gl) {
        this.mTextures.genTexture(gl);
    }

    public TFError setImageResource(int faceIndex, String fileName) {
        this.mTextures.setImageResource(faceIndex, fileName);
        return TFError.ERROR_NONE;
    }

    public TFError setImageResource(int faceIndex, Resources resources, int resource_id) {
        this.mTextures.setImageResource(faceIndex, resources, resource_id);
        return TFError.ERROR_NONE;
    }

    public void forceImageLoading(final TFWorld world) {
        final TFWorld originWorld = this.mWorld;
        Runnable texturizer = new Runnable() { // from class: com.nemustech.tiffany.world.TFModel.1
            @Override // java.lang.Runnable
            public void run() {
                TFModel.this.mWorld = world;
                GL10 gl = TFModel.this.mWorld.mRenderer.getGLContext();
                if (gl == null) {
                    throw new IllegalArgumentException("Void GL context.");
                }
                float prevOpacity = TFModel.this.getOpacity();
                boolean visibility = TFModel.this.isVisible();
                Log.i(TFModel.TAG, "Force draw to cache, " + TFModel.this);
                TFModel.this.setVisibility(true);
                TFModel.this.setOpacity(0.0f);
                TFModel.this._draw(gl, 0);
                TFModel.this.setOpacity(prevOpacity);
                TFModel.this.setVisibility(visibility);
                TFModel.this.mWorld = originWorld;
            }
        };
        world.queueEvent(texturizer);
    }

    public boolean setImageResource(int faceIndex, Bitmap bmp) {
        return setImageResource(faceIndex, bmp, (Rect) null);
    }

    public boolean setImageResource(int faceIndex, Bitmap bmp, Rect rectSet) {
        return this.mTextures.setImageResource(faceIndex, bmp, rectSet);
    }

    public void deleteImageResource(int faceIndex) {
        if (this.mCloneObject) {
            Log.w(TAG, "Tried to delete image resource with cloned model");
        } else {
            this.mTextures.deleteImageResource(faceIndex);
        }
    }

    public void lockImageCache() {
        this.mLockImageCache = true;
    }

    public void unlockImageCache() {
        this.mLockImageCache = false;
    }

    public boolean getImageCacheLockStatus() {
        return this.mLockImageCache;
    }

    public void deleteAllImageResource() {
        int maxTextureIndex = this.mTextures.getMaxTextureIndex();
        for (int i = 0; i <= maxTextureIndex; i++) {
            deleteImageResource(i);
        }
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public boolean getLockStatus() {
        if (this.mParentHolder == null || !this.mParentHolder.getLockStatus()) {
            return super.getLockStatus();
        }
        return true;
    }

    public void showEffect(int effectID, int faceIndex) {
        switch (effectID) {
            case 0:
                rotate(0.0f, 0.0f, 2000L);
                move(0.0f, 0.0f, 1.6f, 0.01f);
                return;
            case 1:
                rotate(720.0f, 720.0f, 2000L);
                move(this.mLocation[0], this.mLocation[1], -this.mWorld.getDepth(), 2000L);
                setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFModel.2
                    @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
                    public void onEffectFinish(TFObject object) {
                    }
                });
                return;
            default:
                return;
        }
    }

    protected void onTouchDown(float x, float y) {
    }

    protected void onTouchUp(float x, float y) {
    }

    protected void onTouchDrag(float start_x, float start_y, float end_x, float end_y, int tickPassed) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleTap(int selectedIndex, float x, float y) {
        this.mTapAbsX = x;
        this.mTapAbsY = y;
        if (this.mOnTouchListener == null || !this.mOnTouchListener.onSelected(this, selectedIndex, x, y)) {
            if (this.mSelectListener != null) {
                this.mSelectListener.onSelected(this, selectedIndex);
            } else if (this.mWorld != null && this.mWorld.mSelectListener != null) {
                this.mWorld.mSelectListener.onSelected(this, selectedIndex);
            } else {
                Log.w(TAG, "Got handleTap but locked or no listener registered or this model has been detached already.");
            }
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleDown(int faceIndex, float x, float y) {
		CMN.debug("fatal handleDown!");
        this.mTouchedIndex = faceIndex;
        if (this.mOnTouchListener != null) {
            if (this.mOnTouchListener.onTouchDown(this, faceIndex, x, y)) {
                return true;
            }
            this.mLongPressValid = true;
            mHandler.postDelayed(this.mLongPressHandler, this.mWorld.getLongPressDuration());
        }
        onTouchDown(x, y);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleUp(float x, float y) {
        if (this.mOnTouchListener != null) {
            this.mLongPressValid = false;
            mHandler.removeCallbacks(this.mLongPressHandler);
            if (this.mOnTouchListener.onTouchUp(this, this.mTouchedIndex, x, y)) {
                this.mTouchedIndex = -1;
                return true;
            }
        }
        onTouchUp(x, y);
        this.mTouchedIndex = -1;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleDrag(float start_x, float start_y, float end_x, float end_y, int tickPassed) {
        if (this.mOnTouchListener != null) {
            if (this.mLongPressValid) {
                this.mLongPressValid = false;
                mHandler.removeCallbacks(this.mLongPressHandler);
            }
            if (this.mOnTouchListener.onTouchDrag(this, this.mTouchedIndex, start_x, start_y, end_x, end_y, tickPassed)) {
                return true;
            }
        }
        onTouchDrag(start_x, start_y, end_x, end_y, tickPassed);
        return false;
    }

    public void setFaceFront(boolean mode) {
        this.mFacingFront = mode;
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    public void setBeautyReflection(boolean mode) {
        this.mBeautyReflection = mode;
    }

    public void setTouchable(boolean mode) {
        this.mTouchable = mode;
    }

    public boolean isTouchable() {
        return this.mTouchable;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public boolean isVisible() {
        if (this.mParentHolder == null || this.mParentHolder.isVisible()) {
            return this.mVisible;
        }
        return false;
    }

    public void banChiselFringe(boolean mode) {
        if (this.mTextureFilter == null) {
            this.mTextureFilter = new int[1];
        }
        if (mode) {
            this.mTextureFilter[0] = 9728;
        } else {
            this.mTextureFilter[0] = 9729;
        }
        if (this.mWorld != null) {
            GL10 gl = this.mWorld.mRenderer.getGLContext();
            TFTextureInfo texInfo = getTextureInfo(0);
            if (texInfo != null) {
                TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(0);
                if (layer.bTexturized) {
                    gl.glBindTexture(3553, texInfo.getLayer(0).texture_name);
                    gl.glTexParameterx(3553, 10241, this.mTextureFilter[0]);
                    gl.glTexParameterx(3553, 10240, this.mTextureFilter[0]);
                    gl.glTexParameterx(3553, 10242, 33071);
                    gl.glTexParameterx(3553, 10243, 33071);
                    this.mWorld.requestRender();
                }
            }
        }
    }

    public float getTapAbsX() {
        return this.mTapAbsX;
    }

    public float getTapAbsY() {
        return this.mTapAbsY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHitTestLine(float[] hitTestLine) {
        float[] m = this.mUnMVBuffer;
        Matrix.invertM(m, 0, this.mMatrix, 0);
        Matrix.multiplyMV(this.mHitTestLine, 0, m, 0, hitTestLine, 0);
        Matrix.multiplyMV(this.mHitTestLine, 4, m, 0, hitTestLine, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHitPoint() {
        for (int i = 0; i < this.mHitPoint.length; i++) {
            this.mHitPoint[i] = Float.NaN;
        }
        this.mHitFace = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHitFace(float[] near) {
        near[0] = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < this.mHitPoint.length; i++) {
            if (Float.isNaN(this.mHitPoint[i])) {
                return -1;
            }
        }
        near[0] = this.mHitPoint[6];
        return this.mHitFace;
    }

    public void getHitPoint(float[] result, int rOffset) {
        System.arraycopy(this.mHitPoint, 0, result, rOffset, this.mHitPoint.length);
    }

    public void getHitPointOnModel(float[] result, int rOffset) {
        System.arraycopy(this.mHitPoint, 0, result, rOffset, 4);
    }

    public void getHitPointOnWorld(float[] result, int rOffset) {
        System.arraycopy(this.mHitPoint, 4, result, rOffset, 4);
    }

    public void getHitTestLineOnModel(float[] result, int rOffset) {
        System.arraycopy(this.mHitTestLine, 0, result, rOffset, this.mHitTestLine.length);
    }

    public void convertModelCoord(float[] modelCoord, int modelCoordOffset, float[] worldCoord, int worldCoordOffset, float[] screenCoord, int screenCoordOffset) {
        float[] buf = {modelCoord[modelCoordOffset + 0], modelCoord[modelCoordOffset + 1], modelCoord[modelCoordOffset + 2], 1.0f};
        Matrix.multiplyMV(buf, 4, this.mMatrix, 0, buf, 0);
        if (worldCoord != null) {
            worldCoord[worldCoordOffset + 0] = buf[4];
            worldCoord[worldCoordOffset + 1] = buf[5];
            worldCoord[worldCoordOffset + 2] = buf[6];
        }
        if (screenCoord != null) {
            Matrix.multiplyMV(buf, 0, this.mWorld.getCamera().mMatrix, 0, buf, 4);
            float rw = 1.0f / buf[3];
            float left = this.mWorld.mRenderer.mLeft;
            float top = this.mWorld.mRenderer.mTop;
            float width = this.mWorld.mRenderer.mWidth;
            float height = this.mWorld.mRenderer.mHeight;
            float x = left + (((buf[0] * rw) + 1.0f) * width * 0.5f);
            float y = top + ((1.0f - (buf[1] * rw)) * height * 0.5f);
            float w = ((buf[2] * rw) + 1.0f) * 0.5f;
            screenCoord[screenCoordOffset + 0] = x;
            screenCoord[screenCoordOffset + 1] = y;
            screenCoord[screenCoordOffset + 2] = w;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int doHitTestVertexTrigangle(float[] vertex, int v0Offset, int v1Offset, int v2Offset, float[] hitTestLine, float[] hitPoint, int hitPointOffset, float[] uvt, int uvtOffset, float[] workBuffer, int workOffset) {
        int _v0 = workOffset + 0;
        int _v1_0 = workOffset + 4;
        int _v2_0 = workOffset + 8;
        int _p0 = workOffset + 12;
        int _p1_0 = workOffset + 16;
        int _pvec = workOffset + 20;
        int _qvec = workOffset + 24;
        int _tvec = workOffset + 28;
        TFVector3D.set(workBuffer, _v0, vertex, v0Offset);
        TFVector3D.set(workBuffer, _v1_0, vertex, v1Offset);
        TFVector3D.sub(workBuffer, _v1_0, workBuffer, _v0);
        TFVector3D.set(workBuffer, _v2_0, vertex, v2Offset);
        TFVector3D.sub(workBuffer, _v2_0, workBuffer, _v0);
        TFVector3D.set(workBuffer, _p0, hitTestLine, 0);
        TFVector3D.set(workBuffer, _p1_0, hitTestLine, 4);
        TFVector3D.sub(workBuffer, _p1_0, workBuffer, _p0);
        TFVector3D.cross(workBuffer, _pvec, workBuffer, _p1_0, workBuffer, _v2_0);
        float det = TFVector3D.dot(workBuffer, _v1_0, workBuffer, _pvec);
        if (det > -1.0E-6f && det < 1.0E-6f) {
            return -1;
        }
        float inv_det = 1.0f / det;
        TFVector3D.sub(workBuffer, _tvec, workBuffer, _p0, workBuffer, _v0);
        float u = TFVector3D.dot(workBuffer, _tvec, workBuffer, _pvec) * inv_det;
        if (u < 0.0f || u > 1.0f) {
            return -1;
        }
        TFVector3D.cross(workBuffer, _qvec, workBuffer, _tvec, workBuffer, _v1_0);
        float v = TFVector3D.dot(workBuffer, _p1_0, workBuffer, _qvec) * inv_det;
        if (v >= 0.0f && u + v <= 1.0f) {
            float t = TFVector3D.dot(workBuffer, _v2_0, workBuffer, _qvec) * inv_det;
            if (hitPoint != null) {
                TFVector3D.mul(hitPoint, hitPointOffset, workBuffer, _p1_0, t);
                TFVector3D.add(hitPoint, hitPointOffset, workBuffer, _p0);
            }
            if (uvt != null) {
                uvt[uvtOffset + 0] = u;
                uvt[uvtOffset + 1] = v;
                uvt[uvtOffset + 2] = t;
            }
            return det >= 0.0f ? 0 : 1;
        }
        return -1;
    }

    public void printModelState() {
        printModelState(0);
    }

    public void printModelState(int level) {
        StringBuilder sb = new StringBuilder();
        String space = "|  ";
        for (int i = 0; i < level; i++) {
            space = space.concat(" ");
        }
        sb.append(space + "Model -" + toString() + "\tDescription: " + this.mDescription + "\tItemIndex: " + this.mItemIndex + "\n");
        sb.append(space + "       should draw: " + this.mShouldDraw + "\tvisibility: " + this.mVisible + "\topacity: " + this.mOpacity + "\ttouchable: " + this.mTouchable + "\n");
        sb.append(space + "       location: " + this.mLocation[0] + ", " + this.mLocation[1] + ", " + this.mLocation[2] + "\n");
        sb.append(space + "       Texture: " + this.mTextures + "\n");
        sb.append(space + "       parentHolder: " + this.mParentHolder);
        Log.d("HOLDER_STAT", sb.toString());
    }

    public void swapFaces() {
        this.mTextures.swapFaces();
    }

    public int getTouchedFace() {
        return this.mTouchedIndex;
    }

    public void setTouchedFace(int faceIndex) {
        this.mTouchedIndex = faceIndex;
    }
}
