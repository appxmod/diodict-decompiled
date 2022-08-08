package com.nemustech.tiffany.world;

import android.opengl.Matrix;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/* loaded from: classes.dex */
public class MatrixStack {
    private static final int DEFAULT_MAX_DEPTH = 32;
    private static final int MATRIX_SIZE = 16;
    private float[] mMatrix;
    private float[] mTemp;
    private int mTop;

    public MatrixStack() {
        commonInit(32);
    }

    public MatrixStack(int maxDepth) {
        commonInit(maxDepth);
    }

    private void commonInit(int maxDepth) {
        this.mMatrix = new float[maxDepth * 16];
        this.mTemp = new float[32];
        glLoadIdentity();
    }

    public void glFrustumf(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(this.mMatrix, this.mTop, left, right, bottom, top, near, far);
    }

    public void glFrustumx(int left, int right, int bottom, int top, int near, int far) {
        glFrustumf(fixedToFloat(left), fixedToFloat(right), fixedToFloat(bottom), fixedToFloat(top), fixedToFloat(near), fixedToFloat(far));
    }

    public void glLoadIdentity() {
        Matrix.setIdentityM(this.mMatrix, this.mTop);
    }

    public void glLoadMatrixf(float[] m, int offset) {
        System.arraycopy(m, offset, this.mMatrix, this.mTop, 16);
    }

    public void glLoadMatrixf(FloatBuffer m) {
        m.get(this.mMatrix, this.mTop, 16);
    }

    public void glLoadMatrixx(int[] m, int offset) {
        for (int i = 0; i < 16; i++) {
            this.mMatrix[this.mTop + i] = fixedToFloat(m[offset + i]);
        }
    }

    public void glLoadMatrixx(IntBuffer m) {
        for (int i = 0; i < 16; i++) {
            this.mMatrix[this.mTop + i] = fixedToFloat(m.get());
        }
    }

    public void glMultMatrixf(float[] m, int offset) {
        System.arraycopy(this.mMatrix, this.mTop, this.mTemp, 0, 16);
        Matrix.multiplyMM(this.mMatrix, this.mTop, this.mTemp, 0, m, offset);
    }

    public void glMultMatrixf(FloatBuffer m) {
        m.get(this.mTemp, 16, 16);
        glMultMatrixf(this.mTemp, 16);
    }

    public void glMultMatrixx(int[] m, int offset) {
        for (int i = 0; i < 16; i++) {
            this.mTemp[i + 16] = fixedToFloat(m[offset + i]);
        }
        glMultMatrixf(this.mTemp, 16);
    }

    public void glMultMatrixx(IntBuffer m) {
        for (int i = 0; i < 16; i++) {
            this.mTemp[i + 16] = fixedToFloat(m.get());
        }
        glMultMatrixf(this.mTemp, 16);
    }

    public void glOrthof(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(this.mMatrix, this.mTop, left, right, bottom, top, near, far);
    }

    public void glOrthox(int left, int right, int bottom, int top, int near, int far) {
        glOrthof(fixedToFloat(left), fixedToFloat(right), fixedToFloat(bottom), fixedToFloat(top), fixedToFloat(near), fixedToFloat(far));
    }

    public void glPopMatrix() {
        preflight_adjust(-1);
        adjust(-1);
    }

    public void glPushMatrix() {
        preflight_adjust(1);
        System.arraycopy(this.mMatrix, this.mTop, this.mMatrix, this.mTop + 16, 16);
        adjust(1);
    }

    public void glRotatef(float angle, float x, float y, float z) {
        Matrix.setRotateM(this.mTemp, 0, angle, x, y, z);
        System.arraycopy(this.mMatrix, this.mTop, this.mTemp, 16, 16);
        Matrix.multiplyMM(this.mMatrix, this.mTop, this.mTemp, 16, this.mTemp, 0);
    }

    public void glRotatex(int angle, int x, int y, int z) {
        glRotatef(angle, fixedToFloat(x), fixedToFloat(y), fixedToFloat(z));
    }

    public void glScalef(float x, float y, float z) {
        Matrix.scaleM(this.mMatrix, this.mTop, x, y, z);
    }

    public void glScalex(int x, int y, int z) {
        glScalef(fixedToFloat(x), fixedToFloat(y), fixedToFloat(z));
    }

    public void glTranslatef(float x, float y, float z) {
        Matrix.translateM(this.mMatrix, this.mTop, x, y, z);
    }

    public void glTranslatex(int x, int y, int z) {
        glTranslatef(fixedToFloat(x), fixedToFloat(y), fixedToFloat(z));
    }

    public void getMatrix(float[] dest, int offset) {
        System.arraycopy(this.mMatrix, this.mTop, dest, offset, 16);
    }

    private float fixedToFloat(int x) {
        return x * 1.5258789E-5f;
    }

    private void preflight_adjust(int dir) {
        int newTop = this.mTop + (dir * 16);
        if (newTop < 0) {
            throw new IllegalArgumentException("stack underflow");
        }
        if (newTop + 16 > this.mMatrix.length) {
            throw new IllegalArgumentException("stack overflow");
        }
    }

    private void adjust(int dir) {
        this.mTop += dir * 16;
    }
}
