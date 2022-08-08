package com.nemustech.tiffany.world;

import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFCircle extends TFModel {
    protected static final int N = 32;
    static final String TAG = "TFCircle";
    private int[] mImageOrientation;
    private int[] mReverseWay;
    private RectF mTexRect;

    public TFCircle() {
        create(1.0f, 1.0f);
    }

    public TFCircle(float width, float height) {
        create(width, height);
    }

    public TFCircle(TFWorld world, float width, float height) {
        create(width, height);
        super.attachTo(world);
    }

    public TFCircle(TFHolder holder, float width, float height) {
        create(width, height);
        super.attachTo(holder);
    }

    private void create(float width, float height) {
        this.mImageOrientation = new int[2];
        this.mReverseWay = new int[2];
        setSize(width, height);
        super.setBackFaceVisibility(true);
    }

    public void setImageDirection(int index, int direction) {
        if (this.mImageOrientation[index] != direction) {
            this.mImageOrientation[index] = direction;
            fillTexCoord(this.mTextureBuffer, direction);
        }
    }

    public void reverseImage(int index, int reverseWay) {
        this.mReverseWay[index] = reverseWay;
        float centerX = this.mTexRect.centerX();
        float centerY = this.mTexRect.centerY();
        int len = (this.mTextureBuffer.capacity() / 2) / 2;
        int offset = index * (this.mTextureBuffer.capacity() / 2);
        switch (reverseWay) {
            case 0:
                return;
            case 1:
                for (int i = 0; i < len; i++) {
                    this.mTextureBuffer.put((i * 2) + offset + 0, centerX - this.mTextureBuffer.get(((i * 2) + offset) + 0));
                }
                return;
            case 2:
                for (int i2 = 0; i2 < len; i2++) {
                    this.mTextureBuffer.put((i2 * 2) + offset + 1, centerX - this.mTextureBuffer.get(((i2 * 2) + offset) + 1));
                }
                return;
            case 3:
                for (int i3 = 0; i3 < len; i3++) {
                    this.mTextureBuffer.put((i3 * 2) + offset + 0, centerX - this.mTextureBuffer.get(((i3 * 2) + offset) + 0));
                    this.mTextureBuffer.put((i3 * 2) + offset + 1, centerY - this.mTextureBuffer.get(((i3 * 2) + offset) + 1));
                }
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        this.mTexRect.left = 0.0f;
        this.mTexRect.right = rectTexture.right / textureWidth;
        this.mTexRect.top = 0.0f;
        this.mTexRect.bottom = rectTexture.bottom / textureHeight;
        fillTexCoord(this.mTextureBuffer);
        setImageDirection(index, this.mImageOrientation[index]);
        reverseImage(index, this.mReverseWay[index]);
    }

    public void setSize(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDepth = 0.0f;
        FloatBuffer vertex = ByteBuffer.allocateDirect(816).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fillVertex(vertex);
        this.mVertexBuffer = vertex;
        FloatBuffer texCoord = ByteBuffer.allocateDirect(544).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fillTexCoord(texCoord);
        this.mTextureBuffer = texCoord;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onDrawVertex(GL10 gl, int index, boolean reflection) {
        gl.glDrawArrays(6, index * 34, 34);
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
        if (x >= (-A) && x <= A && y >= (-B) && y <= B && ((x * x) / (A * A)) + ((y * y) / (B * B)) <= 1.0f) {
            TFVector3D.setW(m, 0);
            Matrix.multiplyMV(m, 4, this.mMatrix, 0, m, 0);
            float dot = TFVector3D.dotWithAxis(this.mHitTestLine, 0, this.mHitTestLine, 4, 2);
            this.mHitFace = 0;
            if (dot > 0.0f) {
                this.mHitFace = 1;
            }
        }
    }

    private void fillVertex(FloatBuffer vertex) {
        float radiusX = this.mWidth / 2.0f;
        float radiusY = this.mHeight / 2.0f;
        vertex.position(0);
        vertex.put(0.0f);
        vertex.put(0.0f);
        vertex.put(0.0f);
        for (int i = 0; i <= 32; i++) {
            float rad = ((i * 2) * 3.1415927f) / 32.0f;
            float x = radiusX * ((float) Math.cos(rad));
            float y = radiusY * ((float) Math.sin(rad));
            vertex.put(x);
            vertex.put(y);
            vertex.put(0.0f);
        }
        for (int i2 = 0; i2 <= 33; i2++) {
            vertex.put(vertex.get((i2 * 3) + 0));
            vertex.put(vertex.get((i2 * 3) + 1));
            vertex.put(vertex.get((i2 * 3) + 2));
        }
        vertex.position(0);
    }

    private void fillTexCoord(FloatBuffer texCoord) {
        fillTexCoord(texCoord, 0);
    }

    private void fillTexCoord(FloatBuffer texCoord, int direction) {
        float rotate;
        if (this.mTexRect == null) {
            this.mTexRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        float centerX = this.mTexRect.centerX();
        float radiusX = this.mTexRect.width() / 2.0f;
        float centerY = this.mTexRect.centerY();
        float radiusY = this.mTexRect.height() / 2.0f;
        switch (direction) {
            case 1:
                rotate = 1.5707964f;
                break;
            case 2:
                rotate = -1.5707964f;
                break;
            case 3:
                rotate = 3.1415927f;
                break;
            default:
                rotate = 0.0f;
                break;
        }
        texCoord.position(0);
        texCoord.put(centerX);
        texCoord.put((2.0f * centerY) - centerY);
        for (int i = 0; i <= 32; i++) {
            float rad = (((i * 2) * 3.1415927f) / 32.0f) + rotate;
            float x = radiusX * ((float) Math.cos(rad));
            float y = radiusY * ((float) Math.sin(rad));
            texCoord.put(x + centerX);
            texCoord.put((2.0f * centerY) - (y + centerY));
        }
        for (int i2 = 0; i2 <= 33; i2++) {
            texCoord.put((2.0f * centerX) - texCoord.get((i2 * 2) + 0));
            texCoord.put(texCoord.get((i2 * 2) + 1));
        }
        texCoord.position(0);
    }
}
