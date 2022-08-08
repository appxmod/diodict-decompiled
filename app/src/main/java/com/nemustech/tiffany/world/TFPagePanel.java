package com.nemustech.tiffany.world;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.Matrix;
import android.util.Log;
import android.view.animation.Interpolator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFPagePanel extends TFModel {
    protected static final float DEFAULT_BOTTOM_SHADOW_ALPHA = 0.9f;
    protected static final float DEFAULT_BOTTOM_SHADOW_RATIO = 0.25f;
    public static final int ELAPSE = 30;
    public static final int PAGE_MODE_LEFT = 1;
    public static final int PAGE_MODE_RIGHT = 2;
    public static final int PAGE_MODE_SOLO = 0;
    private static final String TAG = "TFPagePanel";
    protected FloatBuffer mBackMaskColorBuffer;
    protected float mBackMaskOpacity;
    protected float mBorderShadowDarkAlpha;
    protected boolean mBorderShadowInflated;
    protected float mBorderShadowRatio;
    protected float mBottomShadowAlpha;
    protected float mBottomShadowHeight;
    protected TFShadowPanel mBottomShadowPanel;
    protected float mBottomShadowRatio;
    protected float mBottomShadowWidth;
    protected int mDirectionDegree;
    protected int mDuration;
    protected boolean mFlipToBackface;
    protected int[] mImageOrientation;
    protected float[] mInflection;
    protected PageOverInterpolator mInterpolator;
    protected float mMinY;
    protected OnUpdateVertexListener mOnUpdateVertex;
    protected int mPageMode;
    protected float[] mRectVertex;
    protected int[] mReverseWay;
    protected float[] mT;
    protected float[] mTextureScale;
    protected int mTickAccum;
    protected int mTickElapse;
    protected int mTicks;
    protected Interpolator mTimeInterpolator;
    protected float mTimeLineEnd;
    protected float mTimeLineStart;
    protected float[] mV;
    protected int mVertexCount;
    protected float[] mXYL;
    protected int mXYLCount;

    /* loaded from: classes.dex */
    public interface OnUpdateVertexListener {
        void afterUpdate(TFPagePanel tFPagePanel);

        void beforeUpdate(TFPagePanel tFPagePanel);
    }

    /* loaded from: classes.dex */
    public static abstract class PageOverInterpolator {
        public abstract int getBufferCount();

        public abstract int getElemCount();

        public abstract float getMaxHeight();

        public abstract int interpolate(float f, float[] fArr);
    }

    public TFPagePanel() {
        this(1.0f, 1.0f, (PageOverInterpolator) null);
    }

    public TFPagePanel(float width, float height) {
        this(width, height, (PageOverInterpolator) null);
    }

    public TFPagePanel(TFWorld world, float width, float height) {
        this(width, height, (PageOverInterpolator) null);
        super.attachTo(world);
    }

    public TFPagePanel(TFHolder holder, float width, float height) {
        this(width, height, (PageOverInterpolator) null);
        super.attachTo(holder);
    }

    public TFPagePanel(float width, float height, PageOverInterpolator i) {
        this.mDirectionDegree = 0;
        this.mVertexCount = 0;
        this.mV = new float[6];
        this.mT = new float[4];
        this.mRectVertex = new float[8];
        this.mInflection = new float[4];
        this.mImageOrientation = new int[2];
        this.mReverseWay = new int[2];
        this.mTextureScale = new float[4];
        this.mFlipToBackface = false;
        create(width, height, i);
    }

    public void setSize(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        int count = (this.mInterpolator.getElemCount() + 2) * 2;
        this.mVertexBuffer = newBuffer(count * 3 * 2);
        this.mTextureBuffer = newBuffer(count * 2 * 2);
        this.mBackMaskColorBuffer = newBuffer(count * 4 * 2);
        buildVertexAndTexCoord();
    }

    public boolean setDirectionDegree(int degree) {
        while (degree < 0) {
            degree += 360;
        }
        while (degree >= 360) {
            degree -= 360;
        }
        this.mDirectionDegree = degree;
        return true;
    }

    public int getDirectionDegree() {
        return this.mDirectionDegree;
    }

    public boolean setTimeLine(float t) {
        return setTimeLine(t, t, 0, null);
    }

    public boolean setTimeLine(float start, float end, int duration) {
        return setTimeLine(start, end, duration, null);
    }

    public boolean setTimeLine(float start, float end, int duration, Interpolator interpolator) {
        if (start < 0.0f || start > 1.0f || end < 0.0f || end > 1.0f) {
            return false;
        }
        this.mTimeLineStart = start;
        this.mTimeLineEnd = end;
        this.mDuration = duration;
        this.mTimeInterpolator = interpolator;
        this.mTicks = 0;
        this.mTickAccum = 0;
        buildVertexAndTexCoord();
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
        return true;
    }

    public float getTimeLine() {
        if (this.mDuration == 0) {
            return this.mTimeLineStart;
        }
        float t = this.mTicks / this.mDuration;
        if (t < 0.0f) {
            return this.mTimeLineStart;
        }
        if (t > 1.0f) {
            return this.mTimeLineEnd;
        }
        if (this.mTimeInterpolator != null) {
            t = this.mTimeInterpolator.getInterpolation(t);
        }
        return ((1.0f - t) * this.mTimeLineStart) + (this.mTimeLineEnd * t);
    }

    boolean isPlaying() {
        return this.mTicks < this.mDuration;
    }

    public void setBackMaskOpacity(float opacity) {
        this.mBackMaskOpacity = opacity;
    }

    public void setElapse(int msec) {
        this.mTickElapse = msec;
    }

    public int getElapse() {
        return this.mTickElapse;
    }

    public void reverseImage(int index, int reverseWay) {
        if (this.mReverseWay[index] != reverseWay) {
            this.mReverseWay[index] = reverseWay;
            buildVertexAndTexCoord();
            if (this.mWorld != null) {
                this.mWorld.requestRender();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean updateObject(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        if (!super.updateObject(gl, tickPassed, bDoGLCalc)) {
            return false;
        }
        if (isPlaying()) {
            this.mTickAccum += tickPassed;
            if (this.mTickAccum >= this.mTickElapse) {
                this.mTickAccum -= this.mTickElapse;
                this.mTicks += this.mTickElapse;
                buildVertexAndTexCoord();
            }
            this.mEffectStatus = 1;
            this.mWorld.requestRender();
            return true;
        } else if (this.mEffectStatus != 1 || isInEffectAnimation()) {
            return true;
        } else {
            this.mEffectStatus = 2;
            this.mWorld.requestRender();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        super.adjustTextureCoordination(rectTexture, index, textureWidth, textureHeight);
        this.mTextureScale[(index * 2) + 0] = rectTexture.right / textureWidth;
        this.mTextureScale[(index * 2) + 1] = rectTexture.bottom / textureHeight;
        buildVertexAndTexCoord();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean isInEffectAnimation() {
        return super.isInEffectAnimation() || isPlaying();
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected boolean onDraw(GL10 gl, int tickPassed) {
        if (this.mBottomShadowPanel != null) {
            gl.glPushMatrix();
            this.mBottomShadowPanel._draw(gl, tickPassed);
            gl.glPopMatrix();
            return true;
        }
        return true;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onDrawVertex(GL10 gl, int index, boolean reflection) {
        if (index == 1) {
            gl.glEnableClientState(32886);
            gl.glColorPointer(4, 5126, 0, this.mBackMaskColorBuffer);
            if (this.mBackMaskOpacity != 0.0f) {
                this.mTextures.setTextureByIndex(gl, 0);
                drawVertex(gl, 0);
                gl.glDisableClientState(32886);
                gl.glColor4f(1.0f, 1.0f, 1.0f, this.mBackMaskOpacity);
                this.mTextures.setTextureByIndex(gl, 1);
                drawVertex(gl, index);
                return;
            }
            drawVertex(gl, index);
            gl.glDisableClientState(32886);
            return;
        }
        drawVertex(gl, index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void updateHitPoint() {
        super.updateHitPoint();
        int hitPointOffset = this.mVertexCount * 3;
        int uvtOffset = hitPointOffset + 4;
        int workOffset = uvtOffset + 3;
        float[] work = new float[workOffset + 32];
        float tMin = Float.POSITIVE_INFINITY;
        this.mVertexBuffer.position(0);
        this.mVertexBuffer.get(work, 0, hitPointOffset);
        this.mVertexBuffer.position(0);
        for (int i = 0; i < this.mVertexCount - 2; i++) {
            int a = (i + 0) * 3;
            int b = (i + 1) * 3;
            int c = (i + 2) * 3;
            if (i % 2 != 0) {
                b = c;
                c = b;
            }
            int hit = TFModel.doHitTestVertexTrigangle(work, a, b, c, this.mHitTestLine, work, hitPointOffset, work, uvtOffset, work, workOffset);
            if (hit >= 0 && tMin >= work[uvtOffset + 2]) {
                tMin = work[uvtOffset + 2];
                this.mHitFace = hit;
                System.arraycopy(work, hitPointOffset, this.mHitPoint, 0, 4);
            }
        }
        if (this.mHitFace >= 0) {
            TFVector3D.setW(this.mHitPoint, 0);
            Matrix.multiplyMV(this.mHitPoint, 4, this.mMatrix, 0, this.mHitPoint, 0);
        }
    }

    protected void create(float width, float height, PageOverInterpolator i) {
        this.mTimeLineStart = 0.0f;
        this.mTimeLineEnd = 0.0f;
        this.mTicks = 0;
        this.mTickAccum = 0;
        this.mTickElapse = 30;
        this.mDuration = 0;
        this.mTimeInterpolator = null;
        initTextureParam(0);
        initTextureParam(1);
        if (i == null) {
            i = new CircularPageOverInterpolator();
        }
        setPageOverInterpolator(i);
        setSize(width, height);
    }

    protected void drawVertex(GL10 gl, int face) {
        int offset = face * this.mVertexCount;
        gl.glDrawArrays(5, offset, this.mVertexCount);
    }

    void buildVertexAndTexCoord() {
        if (this.mOnUpdateVertex != null) {
            this.mOnUpdateVertex.beforeUpdate(this);
        }
        if (this.mDirectionDegree % 90 == 0) {
            buildOrthogonal();
        } else {
            buildDiagonal();
        }
        buildBackFace();
        adjustTexCoord(0);
        adjustTexCoord(1);
        this.mVertexBuffer.position(0);
        this.mTextureBuffer.position(0);
        configureBottomShadow();
        if (this.mOnUpdateVertex != null) {
            this.mOnUpdateVertex.afterUpdate(this);
        }
    }

    void buildOrthogonal() {
        this.mXYLCount = this.mInterpolator.interpolate(getTimeLine(), this.mXYL);
        int count = this.mXYLCount;
        int degree = this.mDirectionDegree;
        this.mMinY = Float.POSITIVE_INFINITY;
        this.mVertexCount = 0;
        for (int i = 0; i < count; i++) {
            float ix = this.mXYL[(i * 3) + 0];
            float iy = this.mFlipToBackface ? -this.mXYL[(i * 3) + 1] : this.mXYL[(i * 3) + 1];
            float il = this.mXYL[(i * 3) + 2];
            if (degree == 0) {
                this.mV[0] = ix - 0.5f;
                this.mV[1] = 0.5f;
                this.mV[2] = iy;
                this.mV[3] = ix - 0.5f;
                this.mV[4] = -0.5f;
                this.mV[5] = iy;
                this.mT[0] = il;
                this.mT[1] = 0.0f;
                this.mT[2] = il;
                this.mT[3] = 1.0f;
            } else if (degree == 90) {
                this.mV[0] = -0.5f;
                this.mV[1] = ix - 0.5f;
                this.mV[2] = iy;
                this.mV[3] = 0.5f;
                this.mV[4] = ix - 0.5f;
                this.mV[5] = iy;
                this.mT[0] = 0.0f;
                this.mT[1] = 1.0f - il;
                this.mT[2] = 1.0f;
                this.mT[3] = 1.0f - il;
            } else if (degree == 180) {
                this.mV[0] = 0.5f - ix;
                this.mV[1] = -0.5f;
                this.mV[2] = iy;
                this.mV[3] = 0.5f - ix;
                this.mV[4] = 0.5f;
                this.mV[5] = iy;
                this.mT[0] = 1.0f - il;
                this.mT[1] = 1.0f;
                this.mT[2] = 1.0f - il;
                this.mT[3] = 0.0f;
            } else if (degree == 270) {
                this.mV[0] = 0.5f;
                this.mV[1] = 0.5f - ix;
                this.mV[2] = iy;
                this.mV[3] = -0.5f;
                this.mV[4] = 0.5f - ix;
                this.mV[5] = iy;
                this.mT[0] = 1.0f;
                this.mT[1] = il;
                this.mT[2] = 0.0f;
                this.mT[3] = il;
            }
            scale(this.mV, 0, this.mWidth, this.mHeight, this.mWidth);
            scale(this.mV, 3, this.mWidth, this.mHeight, this.mWidth);
            this.mVertexBuffer.put(this.mV);
            this.mVertexCount += 2;
            this.mTextureBuffer.put(this.mT);
            this.mMinY = Math.min(this.mMinY, this.mV[1]);
            this.mMinY = Math.min(this.mMinY, this.mV[4]);
        }
        this.mVertexBuffer.position(0);
        this.mTextureBuffer.position(0);
    }

    void buildDiagonal() {
        float y1;
        float y2;
        float rectScale = buildRectVertex(this.mRectVertex, -this.mDirectionDegree, this.mWidth, this.mHeight);
        this.mXYLCount = this.mInterpolator.interpolate(getTimeLine(), this.mXYL);
        int count = this.mXYLCount;
        for (int i = 0; i < count; i++) {
            this.mXYL[(i * 3) + 0] = this.mXYL[(i * 3) + 0] - 0.5f;
            this.mXYL[(i * 3) + 2] = this.mXYL[(i * 3) + 2] - 0.5f;
        }
        float rad = (this.mDirectionDegree * 3.1415927f) / 180.0f;
        float cos = rectScale * ((float) Math.cos(rad));
        float sin = rectScale * ((float) Math.sin(rad));
        float[] p = this.mInflection;
        getInterpolatorOfL(this.mXYL, this.mRectVertex[2], p, 0);
        getInterpolatorOfL(this.mXYL, this.mRectVertex[4], p, 2);
        this.mMinY = Float.POSITIVE_INFINITY;
        this.mVertexCount = 0;
        boolean passed1 = false;
        boolean passed2 = false;
        int i2 = 0;
        while (i2 < count) {
            float ix = this.mXYL[(i2 * 3) + 0];
            float iy = this.mXYL[(i2 * 3) + 1];
            float il = this.mXYL[(i2 * 3) + 2];
            boolean retry = false;
            if (!passed1 && il > this.mRectVertex[2]) {
                ix = p[0];
                iy = p[1];
                il = this.mRectVertex[2];
                passed1 = true;
                retry = true;
            } else if (!passed2 && il > this.mRectVertex[4]) {
                ix = p[2];
                iy = p[3];
                il = this.mRectVertex[4];
                passed2 = true;
                retry = true;
            }
            if (il < this.mRectVertex[2]) {
                y1 = yOnLine2D(this.mRectVertex, 0, this.mRectVertex, 2, il);
            } else {
                y1 = yOnLine2D(this.mRectVertex, 2, this.mRectVertex, 6, il);
            }
            if (il < this.mRectVertex[4]) {
                y2 = yOnLine2D(this.mRectVertex, 0, this.mRectVertex, 4, il);
            } else {
                y2 = yOnLine2D(this.mRectVertex, 4, this.mRectVertex, 6, il);
            }
            this.mV[0] = ix;
            this.mV[1] = y1;
            this.mV[2] = this.mFlipToBackface ? -iy : iy;
            this.mV[3] = ix;
            this.mV[4] = y2;
            float[] fArr = this.mV;
            if (this.mFlipToBackface) {
                iy = -iy;
            }
            fArr[5] = iy;
            this.mT[0] = il;
            this.mT[1] = y1;
            this.mT[2] = il;
            this.mT[3] = y2;
            if (this.mV[1] > this.mV[4]) {
                swapN(this.mV, 0, 1, 3);
                swapN(this.mT, 0, 1, 2);
            }
            calcRotateZ(this.mV, 0, cos, sin);
            float[] fArr2 = this.mV;
            fArr2[2] = fArr2[2] * rectScale;
            calcRotateZ(this.mV, 3, cos, sin);
            float[] fArr3 = this.mV;
            fArr3[5] = fArr3[5] * rectScale;
            calcRotateZ(this.mT, 0, cos, sin);
            calcRotateZ(this.mT, 2, cos, sin);
            this.mT[0] = (this.mT[0] + (this.mWidth / 2.0f)) / this.mWidth;
            this.mT[1] = ((this.mHeight / 2.0f) - this.mT[1]) / this.mHeight;
            this.mT[2] = (this.mT[2] + (this.mWidth / 2.0f)) / this.mWidth;
            this.mT[3] = ((this.mHeight / 2.0f) - this.mT[3]) / this.mHeight;
            if (isSamePointN(this.mV, 0, this.mV, 3, 3)) {
                this.mVertexBuffer.put(this.mV, 0, 3);
                this.mTextureBuffer.put(this.mT, 0, 2);
                this.mVertexCount++;
            } else {
                this.mVertexBuffer.put(this.mV);
                this.mTextureBuffer.put(this.mT);
                this.mVertexCount += 2;
            }
            this.mMinY = Math.min(this.mMinY, this.mV[1]);
            this.mMinY = Math.min(this.mMinY, this.mV[4]);
            if (!retry) {
                i2++;
            }
        }
        this.mVertexBuffer.position(0);
        this.mTextureBuffer.position(0);
    }

    void buildBackFace() {
        this.mVertexBuffer.position(this.mVertexCount * 3);
        this.mTextureBuffer.position(this.mVertexCount * 2);
        this.mBackMaskColorBuffer.position(0);
        for (int i = 0; i < this.mVertexCount; i++) {
            this.mVertexBuffer.put(this.mVertexBuffer.get((i * 3) + 0));
            this.mVertexBuffer.put(this.mVertexBuffer.get((i * 3) + 1));
            this.mVertexBuffer.put(this.mVertexBuffer.get((i * 3) + 2));
            this.mTextureBuffer.put(1.0f - this.mTextureBuffer.get((i * 2) + 0));
            this.mTextureBuffer.put(this.mTextureBuffer.get((i * 2) + 1));
            for (int j = 0; j < 8; j++) {
                this.mBackMaskColorBuffer.put(1.0f);
            }
        }
        this.mVertexBuffer.position(0);
        this.mTextureBuffer.position(0);
        this.mBackMaskColorBuffer.position(0);
    }

    void adjustTexCoord(int face) {
        float sx = this.mTextureScale[(face * 2) + 0];
        float sy = this.mTextureScale[(face * 2) + 1];
        int offset = this.mVertexCount * face * 2;
        this.mTextureBuffer.position(offset);
        for (int i = 0; i < this.mVertexCount; i++) {
            float s = this.mTextureBuffer.get((i * 2) + offset + 0) * sx;
            float t = this.mTextureBuffer.get((i * 2) + offset + 1) * sy;
            switch (this.mReverseWay[face]) {
                case 1:
                    t = sy - t;
                    break;
                case 2:
                    s = sx - s;
                    break;
                case 3:
                    s = sx - s;
                    t = sy - t;
                    break;
            }
            this.mTextureBuffer.put(s);
            this.mTextureBuffer.put(t);
        }
        this.mTextureBuffer.position(0);
    }

    void calcRotateZ(float[] xyz, int offset, float cos, float sin) {
        float x = xyz[offset + 0];
        float y = xyz[offset + 1];
        xyz[offset + 0] = (cos * x) - (sin * y);
        xyz[offset + 1] = (sin * x) + (cos * y);
    }

    void scale(float[] xyz, int offset, float sx, float sy, float sz) {
        int i = offset + 0;
        xyz[i] = xyz[i] * sx;
        int i2 = offset + 1;
        xyz[i2] = xyz[i2] * sy;
        int i3 = offset + 2;
        xyz[i3] = xyz[i3] * sz;
    }

    float getInterpolatorOfL(float[] iXYL, float l, float[] xy, int offset) {
        float ixPrev = iXYL[0];
        float iyPrev = iXYL[1];
        float ilPrev = iXYL[2];
        int i = 1;
        while (true) {
            float ix = iXYL[(i * 3) + 0];
            float iy = iXYL[(i * 3) + 1];
            float il = iXYL[(i * 3) + 2];
            if (il >= l) {
                if (iyPrev < iy) {
                    float c = (il - l) / (il - ilPrev);
                    xy[offset + 0] = ix - ((ix - ixPrev) * c);
                    xy[offset + 1] = iy - ((iy - iyPrev) * c);
                    return il;
                } else if (ix - ixPrev < 0.0f) {
                    xy[offset + 0] = (il - l) + ix;
                    xy[offset + 1] = iy;
                    return il;
                } else {
                    xy[offset + 0] = l;
                    xy[offset + 1] = iy;
                    return il;
                }
            } else if (il < 0.5f) {
                ixPrev = ix;
                iyPrev = iy;
                ilPrev = il;
                i++;
            } else {
                xy[offset + 0] = ix;
                xy[offset + 1] = iy;
                return 0.5f;
            }
        }
    }

    float yOnLine2D(float[] p1, int p1Offset, float[] p2, int p2Offset, float x) {
        float t = (x - p1[p1Offset + 0]) / (p2[p2Offset + 0] - p1[p1Offset + 0]);
        float y = p1[p1Offset + 1] + ((p2[p2Offset + 1] - p1[p1Offset + 1]) * t);
        return y;
    }

    float buildRectVertex(float[] vertex, float degree, float width, float height) {
        float w = width / 2.0f;
        float h = height / 2.0f;
        float rad = (3.1415927f * degree) / 180.0f;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        vertex[0] = -w;
        vertex[1] = -h;
        vertex[2] = w;
        vertex[3] = -h;
        vertex[4] = -w;
        vertex[5] = h;
        vertex[6] = w;
        vertex[7] = h;
        calcRotateZ(vertex, 0, cos, sin);
        calcRotateZ(vertex, 2, cos, sin);
        calcRotateZ(vertex, 4, cos, sin);
        calcRotateZ(vertex, 6, cos, sin);
        int c = vertex.length / 2;
        for (int i = 0; i < c; i++) {
            for (int j = i + 1; j < c; j++) {
                boolean swap = vertex[(i * 2) + 0] > vertex[(j * 2) + 0] || (vertex[(i * 2) + 0] == vertex[(j * 2) + 0] && vertex[(i * 2) + 1] < vertex[(j * 2) + 1]);
                if (swap) {
                    swapN(vertex, i, j, 2);
                }
            }
        }
        float scale = vertex[6] - vertex[0];
        for (int i2 = 0; i2 < c; i2++) {
            int i3 = (i2 * 2) + 0;
            vertex[i3] = vertex[i3] / scale;
            int i4 = (i2 * 2) + 1;
            vertex[i4] = vertex[i4] / scale;
        }
        return scale;
    }

    void swapN(float[] m, int a, int b, int n) {
        for (int i = 0; i < n; i++) {
            float temp = m[(a * n) + i];
            m[(a * n) + i] = m[(b * n) + i];
            m[(b * n) + i] = temp;
        }
    }

    boolean isSamePointN(float[] p1, int p1Offset, float[] p2, int p2Offset, int n) {
        for (int i = 0; i < n; i++) {
            if (p1[p1Offset + i] != p2[p2Offset + i]) {
                return false;
            }
        }
        return true;
    }

    protected void configureBottomShadow() {
        float verticalDeviationDirection;
        int offset;
        if (this.mBottomShadowPanel != null) {
            this.mBottomShadowPanel.setPriorAction(1);
            float[] vertexArray = null;
            if (0 == 0) {
                int m = getMaxLengthOfVertexArray();
                vertexArray = new float[m];
            }
            int vertexCount = getVertexArray(vertexArray, 0) / 3;
            int foldingInflectionPoint = 0;
            float foldingStartHeight = vertexArray[(vertexCount * 3) - 1];
            float foldingEndHeight = vertexArray[2];
            if (foldingEndHeight < foldingStartHeight) {
                int i = vertexCount - 2;
                while (true) {
                    if (i >= 1) {
                        if (vertexArray[(i * 3) + 2] <= 0.5f * foldingStartHeight) {
                            foldingInflectionPoint = i;
                            break;
                        } else {
                            i--;
                        }
                    } else {
                        break;
                    }
                }
            } else if (foldingEndHeight > foldingStartHeight) {
                int i2 = 1;
                while (true) {
                    if (i2 <= vertexCount - 2) {
                        if (vertexArray[(i2 * 3) + 2] > 0.5f * foldingEndHeight) {
                            i2++;
                        } else {
                            foldingInflectionPoint = i2;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            float t = getTimeLine();
            if (foldingInflectionPoint > 1 && t > 0.1f && t < DEFAULT_BOTTOM_SHADOW_ALPHA) {
                this.mBottomShadowPanel.setVisibility(true);
                resizeShadowPanel(this.mBottomShadowWidth, this.mHeight * 2.0f, true, true);
                float x2 = vertexArray[foldingInflectionPoint * 3];
                float y2 = vertexArray[(foldingInflectionPoint * 3) + 1];
                float x1 = vertexArray[(foldingInflectionPoint - 1) * 3];
                float y1 = vertexArray[((foldingInflectionPoint - 1) * 3) + 1];
                float midPointX = (x1 + x2) * 0.5f;
                float midPointY = (y1 + y2) * 0.5f;
                float z = vertexArray[(foldingInflectionPoint * 3) + 2];
                float temp = (float) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
                float u = (((-x1) * (x2 - x1)) - ((y2 - y1) * y1)) / (temp * temp);
                float xOffset = x1 + ((x2 - x1) * u);
                float yOffset = y1 + ((y2 - y1) * u);
                float verticalDeviation = pointDistance(xOffset, yOffset, midPointX, midPointY);
                float horizontalDeviationDirection = xOffset > 0.0f ? 1.0f : -1.0f;
                if (90 < getDirectionDegree() && getDirectionDegree() < 270) {
                    horizontalDeviationDirection *= -1.0f;
                }
                if (getDirectionDegree() >= 0 && getDirectionDegree() < 180) {
                    if (xOffset - midPointX < 0.0f) {
                        verticalDeviationDirection = -1.0f;
                    } else {
                        verticalDeviationDirection = 1.0f;
                    }
                } else if (xOffset - midPointX < 0.0f) {
                    verticalDeviationDirection = 1.0f;
                } else {
                    verticalDeviationDirection = -1.0f;
                }
                int degree = getDirectionDegree();
                if (degree > 180) {
                    degree -= 360;
                }
                this.mBottomShadowPanel.setHeadDegree(degree);
                float inflectionPointDistance = (float) Math.sqrt((xOffset * xOffset) + (yOffset * yOffset));
                float cameraPositionZ = this.mWorld.getCamera().getLocation(2);
                float horizontalDeviation = (((cameraPositionZ + z) * inflectionPointDistance) / cameraPositionZ) - (0.05f * horizontalDeviationDirection);
                this.mBottomShadowPanel.locate((horizontalDeviation * horizontalDeviationDirection) + (this.mBottomShadowWidth * 0.5f), verticalDeviation * verticalDeviationDirection, 0.0f);
                if (t > 0.2f && t < 0.8f) {
                    float[] GRADATION_LEVEL = {DEFAULT_BOTTOM_SHADOW_ALPHA, 0.8f, 0.7f, 0.6f, 0.5f, 0.6f, 0.7f, 0.8f, DEFAULT_BOTTOM_SHADOW_ALPHA};
                    FloatBuffer colorBuffer = getBackgroundColorArray();
                    int offsetBase = this.mBackMaskOpacity > 0.0f ? 0 : this.mVertexCount * 4;
                    for (int i3 = 0; i3 < GRADATION_LEVEL.length; i3++) {
                        if (foldingEndHeight < foldingStartHeight) {
                            offset = (((i3 * 2) + foldingInflectionPoint) + 1) - (GRADATION_LEVEL.length / 2);
                        } else {
                            offset = (foldingInflectionPoint - (i3 * 2)) + 1 + (GRADATION_LEVEL.length / 2);
                        }
                        if (offset >= 1 && offset < vertexCount) {
                            for (int j = offsetBase; j < offsetBase + 3; j++) {
                                colorBuffer.put(((offset - 1) * 4) + j, GRADATION_LEVEL[i3]);
                                colorBuffer.put((offset * 4) + j, GRADATION_LEVEL[i3]);
                            }
                        }
                    }
                    return;
                }
                return;
            }
            this.mBottomShadowPanel.setVisibility(false);
        }
    }

    private float pointDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }

    /* loaded from: classes.dex */
    public static class CircularPageOverInterpolator extends PageOverInterpolator {
        public static final int CIRCLE_N = 16;
        public static final int NELEM = 3;
        public static final float RADIUS = 0.1f;
        public static final float pi = 3.1415927f;
        public static final float pi3_2 = 4.712389f;
        public static final float pi_2 = 1.5707964f;
        public final int mCircleN;
        public final float mR;

        public CircularPageOverInterpolator() {
            this(16, 0.1f);
        }

        public CircularPageOverInterpolator(int circleN, float radius) {
            this.mCircleN = circleN;
            this.mR = radius;
        }

        @Override // com.nemustech.tiffany.world.TFPagePanel.PageOverInterpolator
        public float getMaxHeight() {
            return this.mR * 2.0f;
        }

        @Override // com.nemustech.tiffany.world.TFPagePanel.PageOverInterpolator
        public int getElemCount() {
            return this.mCircleN + 2 + 1;
        }

        @Override // com.nemustech.tiffany.world.TFPagePanel.PageOverInterpolator
        public int getBufferCount() {
            return getElemCount() * 3;
        }

        @Override // com.nemustech.tiffany.world.TFPagePanel.PageOverInterpolator
        public int interpolate(float t, float[] xyl) {
            int pos;
            if (t < 0.0f) {
                t = 0.0f;
            }
            if (t > 1.0f) {
                t = 1.0f;
            }
            float pi_r = 3.1415927f * this.mR;
            float ll = t * (1.0f + pi_r);
            int pos2 = setPoint(0, xyl, 0.0f, 0.0f, 0.0f);
            if (ll <= 1.0f) {
                int pos3 = setPoint(pos2, xyl, 1.0f - ll, 0.0f, 1.0f - ll);
                float theta = ll > pi_r ? 3.1415927f : ll / this.mR;
                pos = roll(pos3, xyl, theta);
                float remain = 1.0f - xyl[(pos - 3) + 2];
                if (remain > 0.0f) {
                    float endX = xyl[(pos - 3) + 0] - remain;
                    float endY = xyl[(pos - 3) + 1];
                    pos = setPoint(pos, xyl, endX, endY, 1.0f);
                }
            } else {
                float theta2 = 3.1415927f - ((ll - 1.0f) / this.mR);
                int pos4 = unroll(pos2, xyl, theta2);
                float endX2 = xyl[(pos4 - 3) + 0] - (1.0f - xyl[(pos4 - 3) + 2]);
                float endY2 = xyl[(pos4 - 3) + 1];
                pos = setPoint(pos4, xyl, endX2, endY2, 1.0f);
            }
            return pos / 3;
        }

        protected int setPoint(int pos, float[] xyl, float x, float y, float l) {
            xyl[pos + 0] = x;
            xyl[pos + 1] = y;
            xyl[pos + 2] = l;
            return pos + 3;
        }

        protected int roll(int pos, float[] xyl, float theta) {
            int count = intCeil((theta / 3.1415927f) * this.mCircleN);
            if (count <= 0) {
                return pos;
            }
            int prevPos = pos - 3;
            float centerX = xyl[prevPos + 0];
            float centerY = this.mR;
            float prevL = xyl[prevPos + 2];
            for (int i = 1; i <= count; i++) {
                float rad = (i * theta) / count;
                pos = setPoint(pos, xyl, centerX + (this.mR * fcos(rad - 1.5707964f)), centerY + (this.mR * fsin(rad - 1.5707964f)), prevL + (this.mR * rad));
            }
            return pos;
        }

        protected int unroll(int pos, float[] xyl, float theta) {
            int count = intCeil((theta / 3.1415927f) * this.mCircleN);
            if (count <= 0) {
                return pos;
            }
            int prevPos = pos - 3;
            float centerX = xyl[prevPos + 0] + (this.mR * fcos(4.712389f - theta));
            float centerY = xyl[prevPos + 1] + (this.mR * fsin(4.712389f - theta));
            float prevL = xyl[prevPos + 2];
            for (int i = 1; i <= count; i++) {
                float rad = (i * theta) / count;
                float startRad = 1.5707964f - theta;
                pos = setPoint(pos, xyl, centerX + (this.mR * fcos(startRad + rad)), centerY + (this.mR * fsin(startRad + rad)), prevL + (this.mR * rad));
            }
            return pos;
        }

        protected static float fsin(float rad) {
            return (float) Math.sin(rad);
        }

        protected static float fcos(float rad) {
            return (float) Math.cos(rad);
        }

        protected static int intCeil(float f) {
            int i = (int) f;
            if (f > i) {
                return i + 1;
            }
            return i;
        }
    }

    public PageOverInterpolator getPageOverInterpolator() {
        return this.mInterpolator;
    }

    public void setPageOverInterpolator(PageOverInterpolator i) {
        this.mInterpolator = i;
        this.mXYL = new float[this.mInterpolator.getBufferCount()];
    }

    static FloatBuffer newBuffer(int count) {
        return ByteBuffer.allocateDirect(count * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    void initTextureParam(int face) {
        this.mImageOrientation[face] = 0;
        this.mReverseWay[face] = 0;
        this.mTextureScale[(face * 2) + 0] = 1.0f;
        this.mTextureScale[(face * 2) + 1] = 1.0f;
    }

    FloatBuffer getBackgroundColorArray() {
        return this.mBackMaskColorBuffer;
    }

    FloatBuffer getVertexBuffer() {
        return this.mVertexBuffer.asReadOnlyBuffer();
    }

    int getVertexCount() {
        return this.mVertexCount;
    }

    int getVertexArray(float[] a, int offset) {
        int count = this.mVertexCount * 3;
        if (a != null) {
            if (a.length - offset < count) {
                return -1;
            }
            this.mVertexBuffer.get(a, offset, count);
            this.mVertexBuffer.position(0);
            return count;
        }
        return count;
    }

    int getMaxLengthOfVertexArray() {
        int count = (this.mInterpolator.getElemCount() + 2) * 2;
        return count * 3;
    }

    public void setOnUpdateVertexListener(OnUpdateVertexListener l) {
        this.mOnUpdateVertex = l;
    }

    public void setPageShadow(float ratio, float darkAlpha) {
        this.mBottomShadowAlpha = darkAlpha;
        this.mBottomShadowRatio = ratio;
        this.mBottomShadowWidth = this.mWidth * this.mBottomShadowRatio;
        this.mBottomShadowHeight = ((float) Math.sqrt(Math.pow(this.mWidth, 2.0d) + Math.pow(this.mHeight, 2.0d))) * 1.2f;
        this.mBottomShadowPanel = new TFShadowPanel(this.mBottomShadowWidth, this.mBottomShadowHeight, 3, 1);
        this.mBottomShadowPanel.locate((this.mWidth * 0.5f) + (this.mBottomShadowWidth * 0.5f), 0.0f, 0.0f);
        this.mBottomShadowPanel.mWorld = this.mWorld;
        resizeShadowPanel(this.mBottomShadowWidth, this.mBottomShadowHeight, true, true);
    }

    public void setBorderShadow(float ratio, float darkAlpha) {
        this.mBorderShadowRatio = ratio;
        this.mBorderShadowDarkAlpha = darkAlpha;
    }

    protected void fillBorderShadow(Canvas canvas, int faceIndex, int borderThickness, int borderWidth, int borderHeight, float darkAlpha, Bitmap image) {
        int shadowId;
        int endAlpha = (int) (255.0f * darkAlpha);
        Paint paint = new Paint();
        int left = 0;
        int right = 0;
        if (this.mPageMode == 0) {
            shadowId = 2;
        } else if (this.mPageMode == 1) {
            if (faceIndex == 0) {
                shadowId = 1;
            } else {
                shadowId = 0;
            }
        } else if (faceIndex == 0) {
            shadowId = 0;
        } else {
            shadowId = 1;
        }
        for (int i = 0; i <= borderThickness; i++) {
            switch (shadowId) {
                case 0:
                    left = 0;
                    right = borderWidth - i;
                    break;
                case 1:
                    left = i;
                    right = borderWidth;
                    break;
                case 2:
                    left = i;
                    right = borderWidth - i;
                    break;
            }
            if (i == borderThickness) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(-1);
                canvas.drawRect(left, i, right, borderHeight - i, paint);
            } else {
                paint.setStyle(Paint.Style.STROKE);
                int alpha = (((endAlpha + 0) * i) / borderThickness) + 0;
                paint.setARGB(alpha, 0, 0, 0);
                canvas.drawRect(left, i, right - 1, (borderHeight - i) - 1, paint);
            }
        }
        if (image != null) {
            canvas.drawBitmap(image, shadowId != 0 ? borderThickness : 0, borderThickness, paint);
            if (shadowId == 0 || shadowId == 1) {
                int shadowWidth = borderThickness * 2;
                paint.setStyle(Paint.Style.STROKE);
                for (int i2 = 0; i2 <= shadowWidth; i2++) {
                    int alpha2 = (((endAlpha + 0) * i2) / shadowWidth) + 0;
                    paint.setARGB(alpha2, 0, 0, 0);
                    float x = shadowId == 0 ? shadowWidth - i2 : (borderWidth - shadowWidth) + i2;
                    canvas.drawLine(x, borderThickness, x, borderHeight - borderThickness, paint);
                }
            }
        }
    }

    @Override // com.nemustech.tiffany.world.TFModel
    public boolean setImageResource(int faceIndex, Bitmap bmp, Rect rectSet) {
        int shadowBorderWidth;
        int shadowBorderHeight;
        if (this.mBorderShadowRatio > 0.0f) {
            int shadowBorderThickness = (int) (Math.max(bmp.getWidth(), bmp.getHeight()) * this.mBorderShadowRatio);
            int width = bmp.getWidth() + shadowBorderThickness + (this.mPageMode == 0 ? shadowBorderThickness : 0);
            int height = bmp.getHeight() + (shadowBorderThickness * 2);
            if (!this.mBorderShadowInflated) {
                Log.d(TAG, "Panel inflated!");
                setSize((this.mWidth * width) / bmp.getWidth(), (this.mHeight * height) / bmp.getHeight());
                this.mBorderShadowInflated = true;
            }
            Bitmap inflatedFront = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            inflatedFront.setDensity(0);
            inflatedFront.eraseColor(0);
            Canvas cvs = new Canvas(inflatedFront);
            if (this.mBackMaskOpacity > 0.0f && faceIndex == 0) {
                cvs.drawBitmap(bmp, shadowBorderThickness, shadowBorderThickness, new Paint());
                if (super.setImageResource(faceIndex, inflatedFront, (Rect) null)) {
                    inflatedFront.recycle();
                }
                int shadowBorderThickness2 = (int) (256.0f * this.mBorderShadowRatio);
                if (bmp.getWidth() >= bmp.getHeight()) {
                    shadowBorderWidth = 256;
                    shadowBorderHeight = (bmp.getHeight() * 256) / bmp.getWidth();
                } else {
                    shadowBorderWidth = (bmp.getWidth() * 256) / bmp.getHeight();
                    shadowBorderHeight = 256;
                }
                Log.d(TAG, "Shadow border width : " + shadowBorderWidth);
                Log.d(TAG, "Shadow border height : " + shadowBorderHeight);
                Log.d(TAG, "Shadow Length : " + shadowBorderThickness2);
                Bitmap backMask = Bitmap.createBitmap(shadowBorderWidth, shadowBorderHeight, Bitmap.Config.ARGB_8888);
                backMask.eraseColor(0);
                fillBorderShadow(new Canvas(backMask), 1, shadowBorderThickness2, shadowBorderWidth, shadowBorderHeight, this.mBorderShadowDarkAlpha, null);
                if (super.setImageResource(1, backMask, (Rect) null)) {
                    backMask.recycle();
                }
            } else {
                fillBorderShadow(cvs, faceIndex, shadowBorderThickness, width, height, this.mBorderShadowDarkAlpha, bmp);
                if (super.setImageResource(faceIndex, inflatedFront, (Rect) null)) {
                    inflatedFront.recycle();
                }
            }
            return true;
        }
        return super.setImageResource(faceIndex, bmp, rectSet);
    }

    protected void resizeShadowPanel(float width, float height, boolean updateColorBuffer, boolean updateVertexBuffer) {
        this.mBottomShadowWidth = width;
        this.mBottomShadowHeight = height;
        if (updateColorBuffer) {
            float[] colorBuffer = {0.0f, 0.0f, 0.0f, this.mBottomShadowAlpha, 0.0f, 0.0f, 0.0f, this.mBottomShadowAlpha / 3.0f, 0.0f, 0.0f, 0.0f, this.mBottomShadowAlpha / 9.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, this.mBottomShadowAlpha, 0.0f, 0.0f, 0.0f, this.mBottomShadowAlpha / 3.0f, 0.0f, 0.0f, 0.0f, this.mBottomShadowAlpha / 9.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            this.mBottomShadowPanel.setMeshColor(colorBuffer);
        }
        if (updateVertexBuffer) {
            float[] vertexBuffer = {0.0f, 0.0f, 0.0f, 0.35f, 0.0f, 0.0f, 0.55f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.35f, 1.0f, 0.0f, 0.55f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
            this.mBottomShadowPanel.setMeshVertex(vertexBuffer);
        }
    }

    public boolean getFlipToBackface() {
        return this.mFlipToBackface;
    }

    public void setFlipToBackface(boolean flipToBackface) {
        this.mFlipToBackface = flipToBackface;
    }

    public void setPageMode(int pageMode) {
        this.mPageMode = pageMode;
    }

    public int getPageMode() {
        return this.mPageMode;
    }
}
