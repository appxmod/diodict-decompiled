package com.nemustech.tiffany.world;

import android.util.Log;
import com.nemustech.tiffany.world.TFCustomPanel;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class TFRippleEffect extends TFCustomPanel.Blender {
    static final float RIPPLE_AMPLITUDE = 0.125f;
    static final int RIPPLE_COUNT = 8;
    static final int RIPPLE_CYCLES = 24;
    static final int RIPPLE_LENGTH = 2048;
    static final int RIPPLE_SCALE = 1024;
    static final int RIPPLE_STEP = 16;
    private static final String TAG = "TFRippleEffect";
    float[] mAmplitude;
    int[] mCX;
    int[] mCY;
    final TFCustomPanel mCustomPanel;
    float[] mIdentityTexCoord;
    int[] mMax;
    final int mMeshH;
    final int mMeshW;
    int[] mT;
    final TFCustomPanel.Time mTime = new TFCustomPanel.Time();
    int[][] mVectorL;
    float[][] mVectorX;
    float[][] mVectorY;

    public TFRippleEffect(TFCustomPanel customPanel) {
        this.mCustomPanel = customPanel;
        this.mMeshW = this.mCustomPanel.getMeshWidth();
        this.mMeshH = this.mCustomPanel.getMeshHeight();
        this.mIdentityTexCoord = new float[(this.mMeshH + 1) * (this.mMeshW + 1) * 2];
        this.mCustomPanel.loadIdentityTexCoord(this.mIdentityTexCoord);
        initializeRipple();
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onStart() {
        this.mTime.start(0, 30);
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onFrame(int tick) {
        if (this.mTime.update(tick)) {
            updateRipple();
            this.mCustomPanel.requestUpdateTexCoord();
        }
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onEnd() {
        float[] texCoord = this.mCustomPanel.getTexCoord();
        this.mCustomPanel.loadIdentityTexCoord(texCoord);
        this.mCustomPanel.requestUpdateTexCoord();
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public boolean hasEnded() {
        for (int k = 0; k < 8; k++) {
            if (!hasRippleEnded(k)) {
                return false;
            }
        }
        return true;
    }

    void initializeRipple() {
        precalcAmplitude();
        precalcVector();
        this.mCX = new int[8];
        this.mCY = new int[8];
        this.mT = new int[8];
        this.mMax = new int[8];
        for (int k = 0; k < 8; k++) {
            this.mCX[k] = 0;
            this.mCY[k] = 0;
            this.mT[k] = RIPPLE_LENGTH;
            this.mMax[k] = 0;
        }
    }

    void precalcAmplitude() {
        this.mAmplitude = new float[RIPPLE_LENGTH];
        for (int i = 0; i < RIPPLE_LENGTH; i++) {
            float a = 0.0f;
            if (i > 0) {
                float t = 1.0f - (i / 2047.0f);
                float theta = 2.0f * t * 3.1415927f * 24.0f;
                float t8 = t * t;
                float t82 = t8 * t8;
                a = (((-((float) Math.cos(theta))) * 0.5f) + 0.5f) * RIPPLE_AMPLITUDE * t82 * t82;
            }
            this.mAmplitude[i] = a;
        }
    }

    void precalcVector() {
        this.mVectorX = (float[][]) Array.newInstance(Float.TYPE, this.mMeshH + 1, this.mMeshW + 1);
        this.mVectorY = (float[][]) Array.newInstance(Float.TYPE, this.mMeshH + 1, this.mMeshW + 1);
        this.mVectorL = (int[][]) Array.newInstance(Integer.TYPE, this.mMeshH + 1, this.mMeshW + 1);
        for (int j = 0; j <= this.mMeshH; j++) {
            for (int i = 0; i <= this.mMeshW; i++) {
                float dx = i / this.mMeshW;
                float dy = j / this.mMeshH;
                float l = (float) Math.sqrt((dx * dx) + (dy * dy));
                if (l > 0.0f) {
                    dx /= l;
                    dy /= l;
                }
                this.mVectorX[j][i] = dx;
                this.mVectorY[j][i] = dy;
                this.mVectorL[j][i] = (int) (1024.0f * l);
            }
        }
    }

    boolean hasRippleEnded(int index) {
        return this.mT[index] >= this.mMax[index];
    }

    int findRippleEnded() {
        for (int i = 0; i < 8; i++) {
            if (hasRippleEnded(i)) {
                return i;
            }
        }
        return -1;
    }

    void updateRipple() {
        float[] texCoord = this.mCustomPanel.getTexCoord();
        System.arraycopy(this.mIdentityTexCoord, 0, texCoord, 0, this.mIdentityTexCoord.length);
        for (int k = 0; k < 8; k++) {
            if (!hasRippleEnded(k)) {
                int[] iArr = this.mT;
                iArr[k] = iArr[k] + 16;
                int cx = this.mCX[k];
                int cy = this.mCY[k];
                int t = this.mT[k];
                int j = 0;
                while (j <= this.mMeshH) {
                    int index = (this.mMeshW + 1) * j * 2;
                    int i = 0;
                    while (i <= this.mMeshW) {
                        int vx = i > cx ? i - cx : cx - i;
                        int vy = j > cy ? j - cy : cy - j;
                        float dx = i > cx ? this.mVectorX[vy][vx] : -this.mVectorX[vy][vx];
                        float dy = j > cy ? this.mVectorY[vy][vx] : -this.mVectorY[vy][vx];
                        int l = this.mVectorL[vy][vx];
                        int r = t - l;
                        if (r < 0) {
                            r = 0;
                        }
                        if (r >= RIPPLE_LENGTH) {
                            r = 2047;
                        }
                        float amp = 1.0f - (t / 2048.0f);
                        if (amp < 0.0f) {
                            amp = 0.0f;
                        }
                        float amp2 = amp * amp * this.mAmplitude[r];
                        int i2 = index + 0;
                        texCoord[i2] = texCoord[i2] + (dx * amp2);
                        int i3 = index + 1;
                        texCoord[i3] = texCoord[i3] + (dy * amp2);
                        index += 2;
                        i++;
                    }
                    j++;
                }
            }
        }
    }

    public void addRippleWithModelCoord(float centerX, float centerY) {
        addRippleWithTexCoord((centerX / this.mCustomPanel.getWidth()) + 0.5f, (centerY / this.mCustomPanel.getHeight()) + 0.5f);
    }

    public void addRippleWithTexCoord(float cx, float cy) {
        int k = findRippleEnded();
        if (k < 0) {
            Log.d(TAG, "NO FREE RIPPLE SLOT");
            return;
        }
        this.mCX[k] = (int) ((this.mMeshW * cx) + 0.5f);
        this.mCY[k] = (int) ((this.mMeshH * cy) + 0.5f);
        this.mT[k] = 0;
        this.mMax[k] = ((int) (getMaxDistance(cx, cy) * 1024.0f)) + 160;
    }

    static float getDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    static float getMaxDistance(float cx, float cy) {
        float y = 0.0f;
        float x = cx > 0.5f ? 0.0f : 1.0f;
        if (cy <= 0.5f) {
            y = 1.0f;
        }
        return getDistance(x, y, cx, cy);
    }
}
