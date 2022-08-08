package com.nemustech.tiffany.world;

import android.util.Log;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TFGL implements GL, GL10, GL10Ext, GL11, GL11Ext {
    private static final boolean _check = false;
    ByteBuffer mByteBuffer;
    float[] mCheckA;
    float[] mCheckB;
    private MatrixStack mCurrent;
    FloatBuffer mFloatBuffer;
    private int mMatrixMode;
    private MatrixStack mModelView;
    private MatrixStack mProjection;
    private MatrixStack mTexture;
    private TFWorld mWorld;
    private GL10 mgl;
    private GL10Ext mgl10Ext;
    private GL11 mgl11;
    private GL11Ext mgl11Ext;

    public TFGL(GL gl, TFWorld world) {
        this.mWorld = world;
        this.mgl = (GL10) gl;
        if (gl instanceof GL10Ext) {
            this.mgl10Ext = (GL10Ext) gl;
        }
        if (gl instanceof GL11) {
            this.mgl11 = (GL11) gl;
        }
        if (gl instanceof GL11Ext) {
            this.mgl11Ext = (GL11Ext) gl;
        }
        this.mModelView = new MatrixStack();
        this.mProjection = new MatrixStack();
        this.mTexture = new MatrixStack();
        this.mCurrent = this.mModelView;
        this.mMatrixMode = 5888;
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glActiveTexture(int texture) {
        this.mgl.glActiveTexture(texture);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glAlphaFunc(int func, float ref) {
        this.mgl.glAlphaFunc(func, ref);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glAlphaFuncx(int func, int ref) {
        this.mgl.glAlphaFuncx(func, ref);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glBindTexture(int target, int texture) {
        this.mgl.glBindTexture(target, texture);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glBlendFunc(int sfactor, int dfactor) {
        this.mgl.glBlendFunc(sfactor, dfactor);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClear(int mask) {
        this.mgl.glClear(mask);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClearColor(float red, float green, float blue, float alpha) {
        this.mgl.glClearColor(red, green, blue, alpha);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClearColorx(int red, int green, int blue, int alpha) {
        this.mgl.glClearColorx(red, green, blue, alpha);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClearDepthf(float depth) {
        this.mgl.glClearDepthf(depth);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClearDepthx(int depth) {
        this.mgl.glClearDepthx(depth);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClearStencil(int s) {
        this.mgl.glClearStencil(s);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glClientActiveTexture(int texture) {
        this.mgl.glClientActiveTexture(texture);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glColor4f(float red, float green, float blue, float alpha) {
        this.mgl.glColor4f(red, green, blue, alpha);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glColor4x(int red, int green, int blue, int alpha) {
        this.mgl.glColor4x(red, green, blue, alpha);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        this.mgl.glColorMask(red, green, blue, alpha);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glColorPointer(int size, int type, int stride, Buffer pointer) {
        this.mgl.glColorPointer(size, type, stride, pointer);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
        this.mgl.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
        this.mgl.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
        this.mgl.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
        this.mgl.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glCullFace(int mode) {
        this.mgl.glCullFace(mode);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDeleteTextures(int n, int[] textures, int offset) {
        this.mgl.glDeleteTextures(n, textures, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDeleteTextures(int n, IntBuffer textures) {
        this.mgl.glDeleteTextures(n, textures);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDepthFunc(int func) {
        this.mgl.glDepthFunc(func);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDepthMask(boolean flag) {
        this.mgl.glDepthMask(flag);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDepthRangef(float near, float far) {
        this.mgl.glDepthRangef(near, far);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDepthRangex(int near, int far) {
        this.mgl.glDepthRangex(near, far);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDisable(int cap) {
        this.mgl.glDisable(cap);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDisableClientState(int array) {
        this.mgl.glDisableClientState(array);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDrawArrays(int mode, int first, int count) {
        this.mgl.glDrawArrays(mode, first, count);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glDrawElements(int mode, int count, int type, Buffer indices) {
        this.mgl.glDrawElements(mode, count, type, indices);
    }

    @Override // javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.opengles.GL11Ext
    public void glEnable(int cap) {
        this.mgl.glEnable(cap);
    }

    @Override // javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.opengles.GL11Ext
    public void glEnableClientState(int array) {
        this.mgl.glEnableClientState(array);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFinish() {
        this.mgl.glFinish();
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFlush() {
        this.mgl.glFlush();
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFogf(int pname, float param) {
        this.mgl.glFogf(pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFogfv(int pname, float[] params, int offset) {
        this.mgl.glFogfv(pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFogfv(int pname, FloatBuffer params) {
        this.mgl.glFogfv(pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFogx(int pname, int param) {
        this.mgl.glFogx(pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFogxv(int pname, int[] params, int offset) {
        this.mgl.glFogxv(pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFogxv(int pname, IntBuffer params) {
        this.mgl.glFogxv(pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFrontFace(int mode) {
        this.mgl.glFrontFace(mode);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFrustumf(float left, float right, float bottom, float top, float near, float far) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glFrustumf(left, right, bottom, top, near, far);
        }
        this.mgl.glFrustumf(left, right, bottom, top, near, far);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glFrustumx(int left, int right, int bottom, int top, int near, int far) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glFrustumx(left, right, bottom, top, near, far);
        }
        this.mgl.glFrustumx(left, right, bottom, top, near, far);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glGenTextures(int n, int[] textures, int offset) {
        this.mgl.glGenTextures(n, textures, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glGenTextures(int n, IntBuffer textures) {
        this.mgl.glGenTextures(n, textures);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public int glGetError() {
        int result = this.mgl.glGetError();
        return result;
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glGetIntegerv(int pname, int[] params, int offset) {
        this.mgl.glGetIntegerv(pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glGetIntegerv(int pname, IntBuffer params) {
        this.mgl.glGetIntegerv(pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public String glGetString(int name) {
        String result = this.mgl.glGetString(name);
        return result;
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glHint(int target, int mode) {
        this.mgl.glHint(target, mode);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightModelf(int pname, float param) {
        this.mgl.glLightModelf(pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightModelfv(int pname, float[] params, int offset) {
        this.mgl.glLightModelfv(pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightModelfv(int pname, FloatBuffer params) {
        this.mgl.glLightModelfv(pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightModelx(int pname, int param) {
        this.mgl.glLightModelx(pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightModelxv(int pname, int[] params, int offset) {
        this.mgl.glLightModelxv(pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightModelxv(int pname, IntBuffer params) {
        this.mgl.glLightModelxv(pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightf(int light, int pname, float param) {
        this.mgl.glLightf(light, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightfv(int light, int pname, float[] params, int offset) {
        this.mgl.glLightfv(light, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightfv(int light, int pname, FloatBuffer params) {
        this.mgl.glLightfv(light, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightx(int light, int pname, int param) {
        this.mgl.glLightx(light, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightxv(int light, int pname, int[] params, int offset) {
        this.mgl.glLightxv(light, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLightxv(int light, int pname, IntBuffer params) {
        this.mgl.glLightxv(light, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLineWidth(float width) {
        this.mgl.glLineWidth(width);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLineWidthx(int width) {
        this.mgl.glLineWidthx(width);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLoadIdentity() {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glLoadIdentity();
        }
        this.mgl.glLoadIdentity();
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLoadMatrixf(float[] m, int offset) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glLoadMatrixf(m, offset);
        }
        this.mgl.glLoadMatrixf(m, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLoadMatrixf(FloatBuffer m) {
        int position = m.position();
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glLoadMatrixf(m);
        }
        m.position(position);
        this.mgl.glLoadMatrixf(m);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLoadMatrixx(int[] m, int offset) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glLoadMatrixx(m, offset);
        }
        this.mgl.glLoadMatrixx(m, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLoadMatrixx(IntBuffer m) {
        int position = m.position();
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glLoadMatrixx(m);
        }
        m.position(position);
        this.mgl.glLoadMatrixx(m);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glLogicOp(int opcode) {
        this.mgl.glLogicOp(opcode);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMaterialf(int face, int pname, float param) {
        this.mgl.glMaterialf(face, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMaterialfv(int face, int pname, float[] params, int offset) {
        this.mgl.glMaterialfv(face, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMaterialfv(int face, int pname, FloatBuffer params) {
        this.mgl.glMaterialfv(face, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMaterialx(int face, int pname, int param) {
        this.mgl.glMaterialx(face, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMaterialxv(int face, int pname, int[] params, int offset) {
        this.mgl.glMaterialxv(face, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMaterialxv(int face, int pname, IntBuffer params) {
        this.mgl.glMaterialxv(face, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMatrixMode(int mode) {
        switch (mode) {
            case 5888:
                this.mCurrent = this.mModelView;
                break;
            case 5889:
                this.mCurrent = this.mProjection;
                break;
            case 5890:
                this.mCurrent = this.mTexture;
                break;
            default:
                throw new IllegalArgumentException("Unknown matrix mode: " + mode);
        }
        this.mgl.glMatrixMode(mode);
        this.mMatrixMode = mode;
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMultMatrixf(float[] m, int offset) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glMultMatrixf(m, offset);
        }
        this.mgl.glMultMatrixf(m, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMultMatrixf(FloatBuffer m) {
        int position = m.position();
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glMultMatrixf(m);
        }
        m.position(position);
        this.mgl.glMultMatrixf(m);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMultMatrixx(int[] m, int offset) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glMultMatrixx(m, offset);
        }
        this.mgl.glMultMatrixx(m, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMultMatrixx(IntBuffer m) {
        int position = m.position();
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glMultMatrixx(m);
        }
        m.position(position);
        this.mgl.glMultMatrixx(m);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
        this.mgl.glMultiTexCoord4f(target, s, t, r, q);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glMultiTexCoord4x(int target, int s, int t, int r, int q) {
        this.mgl.glMultiTexCoord4x(target, s, t, r, q);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glNormal3f(float nx, float ny, float nz) {
        this.mgl.glNormal3f(nx, ny, nz);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glNormal3x(int nx, int ny, int nz) {
        this.mgl.glNormal3x(nx, ny, nz);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glNormalPointer(int type, int stride, Buffer pointer) {
        this.mgl.glNormalPointer(type, stride, pointer);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glOrthof(float left, float right, float bottom, float top, float near, float far) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glOrthof(left, right, bottom, top, near, far);
        }
        this.mgl.glOrthof(left, right, bottom, top, near, far);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glOrthox(int left, int right, int bottom, int top, int near, int far) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glOrthox(left, right, bottom, top, near, far);
        }
        this.mgl.glOrthox(left, right, bottom, top, near, far);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPixelStorei(int pname, int param) {
        this.mgl.glPixelStorei(pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPointSize(float size) {
        this.mgl.glPointSize(size);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPointSizex(int size) {
        this.mgl.glPointSizex(size);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPolygonOffset(float factor, float units) {
        this.mgl.glPolygonOffset(factor, units);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPolygonOffsetx(int factor, int units) {
        this.mgl.glPolygonOffsetx(factor, units);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPopMatrix() {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glPopMatrix();
        }
        this.mgl.glPopMatrix();
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glPushMatrix() {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glPushMatrix();
        }
        this.mgl.glPushMatrix();
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
        this.mgl.glReadPixels(x, y, width, height, format, type, pixels);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glRotatef(float angle, float x, float y, float z) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glRotatef(angle, x, y, z);
        }
        this.mgl.glRotatef(angle, x, y, z);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glRotatex(int angle, int x, int y, int z) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glRotatex(angle, x, y, z);
        }
        this.mgl.glRotatex(angle, x, y, z);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glSampleCoverage(float value, boolean invert) {
        this.mgl.glSampleCoverage(value, invert);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glSampleCoveragex(int value, boolean invert) {
        this.mgl.glSampleCoveragex(value, invert);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glScalef(float x, float y, float z) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glScalef(x, y, z);
        }
        this.mgl.glScalef(x, y, z);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glScalex(int x, int y, int z) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glScalex(x, y, z);
        }
        this.mgl.glScalex(x, y, z);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glScissor(int x, int y, int width, int height) {
        this.mgl.glScissor(x, y, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glShadeModel(int mode) {
        this.mgl.glShadeModel(mode);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glStencilFunc(int func, int ref, int mask) {
        this.mgl.glStencilFunc(func, ref, mask);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glStencilMask(int mask) {
        this.mgl.glStencilMask(mask);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glStencilOp(int fail, int zfail, int zpass) {
        this.mgl.glStencilOp(fail, zfail, zpass);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
        this.mgl.glTexCoordPointer(size, type, stride, pointer);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexEnvf(int target, int pname, float param) {
        this.mgl.glTexEnvf(target, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexEnvfv(int target, int pname, float[] params, int offset) {
        this.mgl.glTexEnvfv(target, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexEnvfv(int target, int pname, FloatBuffer params) {
        this.mgl.glTexEnvfv(target, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexEnvx(int target, int pname, int param) {
        this.mgl.glTexEnvx(target, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexEnvxv(int target, int pname, int[] params, int offset) {
        this.mgl.glTexEnvxv(target, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexEnvxv(int target, int pname, IntBuffer params) {
        this.mgl.glTexEnvxv(target, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
        this.mgl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexParameterf(int target, int pname, float param) {
        this.mgl.glTexParameterf(target, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexParameterx(int target, int pname, int param) {
        this.mgl.glTexParameterx(target, pname, param);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexParameteriv(int target, int pname, int[] params, int offset) {
        this.mgl11.glTexParameteriv(target, pname, params, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexParameteriv(int target, int pname, IntBuffer params) {
        this.mgl11.glTexParameteriv(target, pname, params);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
        this.mgl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTranslatef(float x, float y, float z) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glTranslatef(x, y, z);
        }
        this.mgl.glTranslatef(x, y, z);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glTranslatex(int x, int y, int z) {
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.glTranslatex(x, y, z);
        }
        this.mgl.glTranslatex(x, y, z);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glVertexPointer(int size, int type, int stride, Buffer pointer) {
        this.mgl.glVertexPointer(size, type, stride, pointer);
    }

    @Override // javax.microedition.khronos.opengles.GL10
    public void glViewport(int x, int y, int width, int height) {
        this.mgl.glViewport(x, y, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glClipPlanef(int plane, float[] equation, int offset) {
        this.mgl11.glClipPlanef(plane, equation, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glClipPlanef(int plane, FloatBuffer equation) {
        this.mgl11.glClipPlanef(plane, equation);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glClipPlanex(int plane, int[] equation, int offset) {
        this.mgl11.glClipPlanex(plane, equation, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glClipPlanex(int plane, IntBuffer equation) {
        this.mgl11.glClipPlanex(plane, equation);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexfOES(float x, float y, float z, float width, float height) {
        this.mgl11Ext.glDrawTexfOES(x, y, z, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexfvOES(float[] coords, int offset) {
        this.mgl11Ext.glDrawTexfvOES(coords, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexfvOES(FloatBuffer coords) {
        this.mgl11Ext.glDrawTexfvOES(coords);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexiOES(int x, int y, int z, int width, int height) {
        this.mgl11Ext.glDrawTexiOES(x, y, z, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexivOES(int[] coords, int offset) {
        this.mgl11Ext.glDrawTexivOES(coords, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexivOES(IntBuffer coords) {
        this.mgl11Ext.glDrawTexivOES(coords);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexsOES(short x, short y, short z, short width, short height) {
        this.mgl11Ext.glDrawTexsOES(x, y, z, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexsvOES(short[] coords, int offset) {
        this.mgl11Ext.glDrawTexsvOES(coords, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexsvOES(ShortBuffer coords) {
        this.mgl11Ext.glDrawTexsvOES(coords);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexxOES(int x, int y, int z, int width, int height) {
        this.mgl11Ext.glDrawTexxOES(x, y, z, width, height);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexxvOES(int[] coords, int offset) {
        this.mgl11Ext.glDrawTexxvOES(coords, offset);
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glDrawTexxvOES(IntBuffer coords) {
        this.mgl11Ext.glDrawTexxvOES(coords);
    }

    @Override // javax.microedition.khronos.opengles.GL10Ext
    public int glQueryMatrixxOES(int[] mantissa, int mantissaOffset, int[] exponent, int exponentOffset) {
        return this.mgl10Ext.glQueryMatrixxOES(mantissa, mantissaOffset, exponent, exponentOffset);
    }

    @Override // javax.microedition.khronos.opengles.GL10Ext
    public int glQueryMatrixxOES(IntBuffer mantissa, IntBuffer exponent) {
        return this.mgl10Ext.glQueryMatrixxOES(mantissa, exponent);
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glBindBuffer(int target, int buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glBufferData(int target, int size, Buffer data, int usage) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glBufferSubData(int target, int offset, int size, Buffer data) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glDeleteBuffers(int n, int[] buffers, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glDeleteBuffers(int n, IntBuffer buffers) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGenBuffers(int n, int[] buffers, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGenBuffers(int n, IntBuffer buffers) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetBooleanv(int pname, boolean[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetBooleanv(int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetClipPlanef(int pname, float[] eqn, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetClipPlanef(int pname, FloatBuffer eqn) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetClipPlanex(int pname, int[] eqn, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetClipPlanex(int pname, IntBuffer eqn) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetFixedv(int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetFixedv(int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetFloatv(int pname, float[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetFloatv(int pname, FloatBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetLightfv(int light, int pname, float[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetLightfv(int light, int pname, FloatBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetLightxv(int light, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetLightxv(int light, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetMaterialfv(int face, int pname, float[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetMaterialxv(int face, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetMaterialxv(int face, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexEnviv(int env, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexEnvxv(int env, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexEnvxv(int env, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexParameterxv(int target, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetTexParameterxv(int target, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public boolean glIsBuffer(int buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public boolean glIsEnabled(int cap) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public boolean glIsTexture(int texture) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointParameterf(int pname, float param) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointParameterfv(int pname, float[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointParameterfv(int pname, FloatBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointParameterx(int pname, int param) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointParameterxv(int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointParameterxv(int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexEnvi(int target, int pname, int param) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexEnviv(int target, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexEnviv(int target, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11, javax.microedition.khronos.opengles.GL11Ext
    public void glTexParameterfv(int target, int pname, float[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexParameterfv(int target, int pname, FloatBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexParameteri(int target, int pname, int param) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexParameterxv(int target, int pname, int[] params, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexParameterxv(int target, int pname, IntBuffer params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glColorPointer(int size, int type, int stride, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glDrawElements(int mode, int count, int type, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glGetPointerv(int pname, Buffer[] params) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glNormalPointer(int type, int stride, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glTexCoordPointer(int size, int type, int stride, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11
    public void glVertexPointer(int size, int type, int stride, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glCurrentPaletteMatrixOES(int matrixpaletteindex) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glLoadPaletteFromModelViewMatrixOES() {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glMatrixIndexPointerOES(int size, int type, int stride, Buffer pointer) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glMatrixIndexPointerOES(int size, int type, int stride, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glWeightPointerOES(int size, int type, int stride, Buffer pointer) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.microedition.khronos.opengles.GL11Ext
    public void glWeightPointerOES(int size, int type, int stride, int offset) {
        throw new UnsupportedOperationException();
    }

    public void getMatrix(float[] m, int offset) {
        int oesMode;
        if (isSoftMatrixCalcNeeded()) {
            this.mCurrent.getMatrix(m, offset);
            return;
        }
        switch (this.mMatrixMode) {
            case 5888:
                oesMode = 35213;
                break;
            case 5889:
                oesMode = 35214;
                break;
            case 5890:
                oesMode = 35215;
                break;
            default:
                throw new IllegalArgumentException("Unknown matrix mode");
        }
        if (this.mByteBuffer == null) {
            this.mByteBuffer = ByteBuffer.allocateDirect(64);
            this.mByteBuffer.order(ByteOrder.nativeOrder());
            this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
        }
        this.mgl.glGetIntegerv(oesMode, this.mByteBuffer.asIntBuffer());
        for (int i = 0; i < 16; i++) {
            m[i + offset] = this.mFloatBuffer.get(i);
        }
    }

    private boolean isSoftMatrixCalcNeeded() {
        return !this.mWorld.isCapable(1) && !this.mWorld.mBanQueryingMatrix;
    }

    public int getMatrixMode() {
        return this.mMatrixMode;
    }

    private void check() {
        int oesMode;
        switch (this.mMatrixMode) {
            case 5888:
                oesMode = 35213;
                break;
            case 5889:
                oesMode = 35214;
                break;
            case 5890:
                oesMode = 35215;
                break;
            default:
                throw new IllegalArgumentException("Unknown matrix mode");
        }
        if (this.mByteBuffer == null) {
            this.mCheckA = new float[16];
            this.mCheckB = new float[16];
            this.mByteBuffer = ByteBuffer.allocateDirect(64);
            this.mByteBuffer.order(ByteOrder.nativeOrder());
            this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
        }
        this.mgl.glGetIntegerv(oesMode, this.mByteBuffer.asIntBuffer());
        for (int i = 0; i < 16; i++) {
            this.mCheckB[i] = this.mFloatBuffer.get(i);
        }
        this.mCurrent.getMatrix(this.mCheckA, 0);
        boolean fail = false;
        for (int i2 = 0; i2 < 16; i2++) {
            if (this.mCheckA[i2] != this.mCheckB[i2]) {
                Log.d("GLMatWrap", "i:" + i2 + " a:" + this.mCheckA[i2] + " a:" + this.mCheckB[i2]);
                fail = true;
            }
        }
        if (fail) {
            throw new IllegalArgumentException("Matrix math difference.");
        }
    }
}
