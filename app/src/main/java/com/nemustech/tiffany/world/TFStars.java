package com.nemustech.tiffany.world;

import android.os.SystemClock;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFStars extends TFModel {
    protected FloatBuffer mColors;
    protected int mCount;
    protected float[] mDefaultColor;
    protected float mDepth;
    protected FloatBuffer mPoints;
    protected Random mRandom;
    protected boolean mUseDefaultColor;

    public TFStars(float width, float height, float depth) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDepth = depth;
        this.mRandom = new Random();
        this.mPoints = null;
        this.mColors = null;
        this.mDefaultColor = new float[4];
        this.mDefaultColor[0] = 0.8f;
        this.mDefaultColor[1] = 0.8f;
        this.mDefaultColor[2] = 0.8f;
        this.mDefaultColor[3] = 1.0f;
    }

    public TFStars(float width, float height) {
        this(width, height, 1.0f);
    }

    public void setCount(int count, boolean useDefaultColor) {
        this.mCount = count;
        this.mUseDefaultColor = useDefaultColor;
        this.mPoints = allocBuffer(this.mCount * 3);
        deployStars();
        if (this.mUseDefaultColor) {
            this.mColors = null;
            return;
        }
        this.mColors = allocBuffer(this.mCount * 4);
        deployColors();
    }

    public void setDefaultColor(float red, float green, float blue, float alpha) {
        this.mDefaultColor[0] = red;
        this.mDefaultColor[1] = green;
        this.mDefaultColor[2] = blue;
        this.mDefaultColor[3] = alpha;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected boolean onDraw(GL10 gl, int tickPassed) {
        gl.glDisable(3553);
        gl.glEnableClientState(32886);
        if (this.mColors == null) {
            gl.glColor4f(this.mDefaultColor[0], this.mDefaultColor[1], this.mDefaultColor[2], this.mDefaultColor[3]);
        } else {
            gl.glColorPointer(4, 5126, 0, this.mColors);
        }
        gl.glPointSize(1.0f);
        gl.glEnableClientState(32884);
        gl.glVertexPointer(3, 5126, 0, this.mPoints);
        gl.glPointSize(2.5f);
        gl.glDrawArrays(0, 0, this.mCount);
        gl.glDisableClientState(32886);
        gl.glEnable(3553);
        return false;
    }

    protected void deployStars() {
        this.mRandom.setSeed(SystemClock.uptimeMillis());
        this.mPoints.position(0);
        for (int i = 0; i < this.mCount; i++) {
            float x = (this.mRandom.nextFloat() - 0.5f) * this.mWidth;
            float y = (this.mRandom.nextFloat() - 0.5f) * this.mHeight;
            float z = (this.mRandom.nextFloat() - 0.5f) * this.mDepth;
            this.mPoints.put(x);
            this.mPoints.put(y);
            this.mPoints.put(z);
        }
        this.mPoints.rewind();
    }

    protected void deployColors() {
        if (this.mColors != null) {
            this.mRandom.setSeed(SystemClock.uptimeMillis());
            this.mColors.position(0);
            for (int i = 0; i < (this.mCount * 8) / 10; i++) {
                float c = this.mRandom.nextFloat();
                this.mColors.put(c);
                this.mColors.put(c);
                this.mColors.put(c);
                this.mColors.put(1.0f);
            }
            for (int i2 = 0; i2 < (this.mCount * 1) / 10; i2++) {
                float r = this.mRandom.nextFloat();
                float a = this.mRandom.nextFloat();
                this.mColors.put(r);
                this.mColors.put(0.0f);
                this.mColors.put(0.0f);
                this.mColors.put((a * 0.2f) + 0.7f);
            }
            for (int i3 = 0; i3 < (this.mCount * 1) / 10; i3++) {
                float b = this.mRandom.nextFloat();
                float a2 = this.mRandom.nextFloat();
                this.mColors.put(0.0f);
                this.mColors.put(0.0f);
                this.mColors.put(b);
                this.mColors.put((a2 * 0.2f) + 0.7f);
            }
            this.mColors.rewind();
        }
    }

    private FloatBuffer allocBuffer(int size) {
        return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
}
