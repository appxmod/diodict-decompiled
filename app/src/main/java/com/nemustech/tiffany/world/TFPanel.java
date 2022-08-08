package com.nemustech.tiffany.world;

import android.graphics.Rect;
import android.opengl.Matrix;
import android.util.Log;
import com.nemustech.tiffany.world.TFTextureInfo;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFPanel extends TFModel {
    static final String TAG = "TFPanel";
    protected FloatBuffer mColorBuffer;
    protected int[] mImageOrientation;
    private float mMorphAnchorX;
    private float mMorphAnchorY;
    private float mMorphTargetHeight;
    private float mMorphTargetWidth;
    private Morpher mMorpher;
    private OnPanelMorphListener mOnPanelMorphListener;
    protected int[] mReverseWay;

    /* loaded from: classes.dex */
    public interface OnPanelMorphListener {
        void onPanelMorph(float f, float f2);
    }

    public TFPanel() {
        create(1.0f, 1.0f);
    }

    public TFPanel(float width, float height) {
        create(width, height);
    }

    public TFPanel(TFWorld world, float width, float height) {
        create(width, height);
        super.attachTo(world);
    }

    public TFPanel(TFHolder holder, float width, float height) {
        create(width, height);
        super.attachTo(holder);
    }

    protected void applyMorph(int tickPassed) {
        if (this.mMorphRemainingTime > 0) {
            if (this.mMorphRemainingTime == this.mMorphInitialTime) {
                this.mMorphInitialTime = 0L;
            } else {
                if (this.mMorphRemainingTime - tickPassed < 0) {
                    tickPassed = (int) this.mMorphRemainingTime;
                }
                float ratio = tickPassed / ((float) this.mMorphRemainingTime);
                float widthDiff = (this.mMorphTargetWidth - getWidth()) * ratio;
                float heightDiff = (this.mMorphTargetHeight - getHeight()) * ratio;
                float[] newSize = {getWidth() + widthDiff, getHeight() + heightDiff};
                this.mMorphRemainingTime -= tickPassed;
                if (this.mOnPanelMorphListener != null) {
                    this.mMorpher.setParams(newSize[0], newSize[1], widthDiff, heightDiff);
                }
                this.mWorld.queueEvent(this.mMorpher);
                if (!isInEffectAnimation()) {
                    this.mEffectStatus = 2;
                }
            }
            this.mWorld.requestRender();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Morpher implements Runnable {
        private float mHeightDiff;
        private float mNewHeight;
        private float mNewWidth;
        private float mWidthDiff;

        Morpher() {
        }

        public void setParams(float newWidth, float newHeight, float widthDiff, float heightDiff) {
            this.mNewWidth = newWidth;
            this.mNewHeight = newHeight;
            this.mWidthDiff = widthDiff;
            this.mHeightDiff = heightDiff;
        }

        @Override // java.lang.Runnable
        public void run() {
            TFPanel.this.setSize(this.mNewWidth, this.mNewHeight);
            if (TFPanel.this.mMorphAnchorX != 0.0f) {
                TFPanel.this.locate(0, (-this.mWidthDiff) * TFPanel.this.mMorphAnchorX, true);
            }
            if (TFPanel.this.mMorphAnchorY != 0.0f) {
                TFPanel.this.locate(1, (-this.mHeightDiff) * TFPanel.this.mMorphAnchorY, true);
            }
            TFPanel.this.mOnPanelMorphListener.onPanelMorph(this.mWidthDiff, this.mHeightDiff);
        }
    }

    public void morph(float width, float height, long animationTime) {
        morph(width, height, 0.0f, 0.0f, animationTime);
    }

    public void morph(float width, float height, float anchorPositionX, float anchorPositionY, long animationTime) {
        if (anchorPositionX < -0.5f || 0.5f < anchorPositionX) {
            anchorPositionX = 0.0f;
        }
        if (anchorPositionY < -0.5f || 0.5f < anchorPositionY) {
            anchorPositionY = 0.0f;
        }
        this.mMorphInitialTime = animationTime;
        this.mMorphRemainingTime = animationTime;
        this.mMorphTargetWidth = width;
        this.mMorphTargetHeight = height;
        this.mMorphAnchorX = anchorPositionX;
        this.mMorphAnchorY = anchorPositionY;
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    public void setOnPanelMorphListener(OnPanelMorphListener listener) {
        if (this.mMorpher == null) {
            this.mMorpher = new Morpher();
        }
        this.mOnPanelMorphListener = listener;
    }

    private void create(float width, float height) {
        float[] texCoords = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        this.mTextureBuffer = tbb.asFloatBuffer();
        this.mTextureBuffer.put(texCoords);
        this.mTextureBuffer.position(0);
        setSize(width, height);
        this.mImageOrientation = new int[2];
        this.mReverseWay = new int[2];
        super.setBackFaceVisibility(true);
        float[] colorBuffer = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorBuffer.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        this.mColorBuffer = cbb.asFloatBuffer();
        this.mColorBuffer.put(colorBuffer);
        this.mColorBuffer.position(0);
    }

    public void setImageDirection(int index, int direction) {
        int offset = index * 8;
        this.mImageOrientation[index] = direction;
        FloatBuffer copy = FloatBuffer.allocate(8);
        for (int i = 0; i < 8; i++) {
            copy.put(i, this.mTextureBuffer.get(offset + i));
        }
        switch (direction) {
            case 1:
                this.mTextureBuffer.put(offset + 4, copy.get(0));
                this.mTextureBuffer.put(offset + 5, copy.get(1));
                this.mTextureBuffer.put(offset + 0, copy.get(2));
                this.mTextureBuffer.put(offset + 1, copy.get(3));
                this.mTextureBuffer.put(offset + 6, copy.get(4));
                this.mTextureBuffer.put(offset + 7, copy.get(5));
                this.mTextureBuffer.put(offset + 2, copy.get(6));
                this.mTextureBuffer.put(offset + 3, copy.get(7));
                return;
            case 2:
                this.mTextureBuffer.put(offset + 2, copy.get(0));
                this.mTextureBuffer.put(offset + 3, copy.get(1));
                this.mTextureBuffer.put(offset + 6, copy.get(2));
                this.mTextureBuffer.put(offset + 7, copy.get(3));
                this.mTextureBuffer.put(offset + 0, copy.get(4));
                this.mTextureBuffer.put(offset + 1, copy.get(5));
                this.mTextureBuffer.put(offset + 4, copy.get(6));
                this.mTextureBuffer.put(offset + 5, copy.get(7));
                return;
            case 3:
                this.mTextureBuffer.put(offset + 6, copy.get(0));
                this.mTextureBuffer.put(offset + 7, copy.get(1));
                this.mTextureBuffer.put(offset + 4, copy.get(2));
                this.mTextureBuffer.put(offset + 5, copy.get(3));
                this.mTextureBuffer.put(offset + 2, copy.get(4));
                this.mTextureBuffer.put(offset + 3, copy.get(5));
                this.mTextureBuffer.put(offset + 0, copy.get(6));
                this.mTextureBuffer.put(offset + 1, copy.get(7));
                return;
            default:
                return;
        }
    }

    public void reverseImage(int index, int reverseWay) {
        int offset = index * 8;
        this.mReverseWay[index] = reverseWay;
        if (reverseWay == 1 || reverseWay == 3) {
            float temp = this.mTextureBuffer.get(offset + 2);
            this.mTextureBuffer.put(offset + 2, this.mTextureBuffer.get(offset + 0));
            this.mTextureBuffer.put(offset + 0, temp);
            float temp2 = this.mTextureBuffer.get(offset + 6);
            this.mTextureBuffer.put(offset + 6, this.mTextureBuffer.get(offset + 4));
            this.mTextureBuffer.put(offset + 4, temp2);
        }
        if (reverseWay == 2 || reverseWay == 3) {
            float temp3 = this.mTextureBuffer.get(offset + 1);
            this.mTextureBuffer.put(offset + 1, this.mTextureBuffer.get(offset + 5));
            this.mTextureBuffer.put(offset + 3, this.mTextureBuffer.get(offset + 7));
            this.mTextureBuffer.put(offset + 5, temp3);
            this.mTextureBuffer.put(offset + 7, temp3);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFModel
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        int offset = index * 8;
        float newRight = rectTexture.right / textureWidth;
        float newBottom = rectTexture.bottom / textureHeight;
        float[] texCoords = {0.0f, newBottom, newRight, newBottom, 0.0f, 0.0f, newRight, 0.0f, newRight, newBottom, 0.0f, newBottom, newRight, 0.0f, 0.0f, 0.0f};
        this.mTextureBuffer.position(offset);
        this.mTextureBuffer.put(texCoords, offset, 8);
        this.mTextureBuffer.rewind();
        setImageDirection(index, this.mImageOrientation[index]);
        reverseImage(index, this.mReverseWay[index]);
    }

    public void setBackgroundImage(int faceIndex, TFTextureInfo textureInfo) {
        TFTextureInfo.TFTextureLayer layerInfo = textureInfo.getLayer(0);
        if (layerInfo.bTexturized) {
            getTextureInfo(faceIndex);
        }
    }

    public void setSize(float width, float height) {
        if (width <= 0.0f || height <= 0.0f) {
            Log.e(TAG, "Invalid panel size , width:" + width + " height:" + height);
        }
        this.mWidth = width;
        this.mHeight = height;
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        float[] vertices = {-halfWidth, -halfHeight, 0.0f, halfWidth, -halfHeight, 0.0f, -halfWidth, halfHeight, 0.0f, halfWidth, halfHeight, 0.0f, -halfWidth, -halfHeight, 0.0f, halfWidth, -halfHeight, 0.0f, -halfWidth, halfHeight, 0.0f, halfWidth, halfHeight, 0.0f};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        this.mVertexBuffer = vbb.asFloatBuffer();
        this.mVertexBuffer.put(vertices);
        this.mVertexBuffer.position(0);
        this.mWidth = width;
        this.mHeight = height;
        this.mDepth = 0.0f;
    }

    public void setColorBuffer(float[] colorBuffer) {
        for (int i = 0; i < colorBuffer.length; i++) {
            this.mColorBuffer.put(i, colorBuffer[i]);
        }
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected boolean onBeforeDraw(GL10 gl, int tickPassed) {
        applyMorph(tickPassed);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFModel
    public void onSetOpacity(GL10 gl, float opacity, boolean reflection) {
        if (reflection && this.mBeautyReflection) {
            float reflectionOpacity = opacity * this.mWorld.mReflectionOpacity;
            gl.glEnableClientState(32886);
            int j = 0;
            while (j < 8) {
                this.mColorBuffer.put(j, (j == 3 || j == 7) ? 1.0f : reflectionOpacity);
                j++;
            }
            gl.glColorPointer(4, 5126, 0, this.mColorBuffer);
            return;
        }
        super.onSetOpacity(gl, opacity, reflection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void updateHitPoint() {
        super.updateHitPoint();
        float[] m = this.mHitPoint;
        TFVector3D.getPointOnLine(m, 0, this.mHitTestLine, 0, this.mHitTestLine, 4, 0.0f, 2);
        float A = this.mWidth / 2.0f;
        float B = this.mHeight / 2.0f;
        float x = m[0];
        float y = m[1];
        if (x >= (-A) && x <= A && y >= (-B) && y <= B) {
            TFVector3D.setW(m, 0);
            Matrix.multiplyMV(m, 4, this.mMatrix, 0, m, 0);
            float dot = TFVector3D.dotWithAxis(this.mHitTestLine, 0, this.mHitTestLine, 4, 2);
            this.mHitFace = 0;
            if (dot > 0.0f) {
                this.mHitFace = 1;
            }
        }
    }
}
