package com.nemustech.tiffany.world;

import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFSphere extends TFModel {
    protected static final int N = 32;
    private static final String TAG = "TFSphere";
    protected FloatBuffer mLatitude;
    protected int mLatitudeSplit;
    protected int mLatitudeStep;
    protected FloatBuffer mLongitude;
    protected int mLongitudeSplit;
    protected int mLongitudeStep;
    private float[] mHitTestBuffer = new float[12];
    protected float[] mLatitudeEquatorColor = {0.5f, 0.5f, 0.5f, 0.0f};
    protected float[] mLatitudeColor = {0.5f, 0.5f, 0.5f, 0.0f};
    protected float mLatitudeWidth = 0.5f;
    protected float[] mLongitudePrimeColor = {0.5f, 0.5f, 0.5f, 0.0f};
    protected float[] mLongitudeColor = {0.5f, 0.5f, 0.5f, 0.0f};
    protected float mLongitudeWidth = 0.5f;

    public TFSphere(TFWorld world, float width, float height) {
        this.mRotateYFirst = true;
        setSize(width, height);
        super.attachTo(world);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        fillTexCoordFace(this.mTextureBuffer, index, rectTexture.right / textureWidth, rectTexture.bottom / textureHeight);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected int onCullFace(int index, boolean reflection) {
        return reflection ? 1028 : 1029;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected boolean onDraw(GL10 gl, int tickPassed) {
        gl.glScalef(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mWidth / 2.0f);
        drawLatitudeAndLongitude(gl);
        return true;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onCalcReflection(GL10 gl, float y) {
        gl.glLoadIdentity();
        gl.glTranslatef(this.mLocation[0], -((y * 2.0f) + this.mHeight), this.mLocation[2]);
        gl.glRotatef(this.mAngle[0], 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-this.mAngle[1], 1.0f, 0.0f, 0.0f);
        gl.glScalef(this.mWidth / 2.0f, (-this.mHeight) / 2.0f, this.mWidth / 2.0f);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onDrawVertex(GL10 gl, int index, boolean reflection) {
        drawVertex(gl);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onTouchDrag(float startX, float startY, float endX, float endY, int tickPassed) {
        calcForce(startX, startY, endX, endY, tickPassed);
        float forceX = this.mForceFromSide * 5.0f;
        float forceY = this.mForceFromHead * 5.0f;
        if (Math.abs(forceX) > Math.abs(forceY)) {
            forceY = 0.0f;
        } else {
            forceX = 0.0f;
        }
        float yAngle = this.mAngle[1];
        if (yAngle >= 90.0f && yAngle < 270.0f) {
            forceX = -forceX;
        }
        spin(forceX, forceY, 0.0f, false);
    }

    public static void convertModelToGeographic(float[] model, int mOffset, float[] geographic, int gOffset) {
        float length = TFVector3D.length(model, mOffset);
        float x = model[mOffset + 0] / length;
        float y = model[mOffset + 1] / length;
        float z = model[mOffset + 2] / length;
        float theta = (float) ((Math.asin(y) * 180.0d) / 3.141592653589793d);
        float phi = 90.0f - ((float) ((Math.atan2(z, x) * 180.0d) / 3.141592653589793d));
        geographic[gOffset + 0] = theta;
        geographic[gOffset + 1] = phi;
    }

    public static void convertGeographicToModel(float[] geographic, int gOffset, float[] model, int mOffset) {
        float theta = (float) ((geographic[gOffset + 0] * 3.141592653589793d) / 180.0d);
        float phi = (float) ((geographic[gOffset + 1] * 3.141592653589793d) / 180.0d);
        float x = (float) (Math.cos(theta) * Math.sin(phi));
        float y = (float) Math.sin(theta);
        float z = (float) (Math.cos(theta) * Math.cos(phi));
        model[mOffset + 0] = x;
        model[mOffset + 1] = y;
        model[mOffset + 2] = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void updateHitPoint() {
        super.updateHitPoint();
        float[] m = this.mHitTestBuffer;
        float[] line = this.mHitTestLine;
        TFVector3D.set(m, 0, line, 0);
        TFVector3D.set(m, 4, line, 4);
        TFVector3D.sub(m, 4, m, 0);
        TFVector3D.set(m, 8, 0.0f, 0.0f, 0.0f);
        TFVector3D.sub(m, 8, m, 0);
        float a = TFVector3D.lengthSquare(m, 4);
        float b = (-2.0f) * TFVector3D.dot(m, 8, m, 4);
        float c = TFVector3D.lengthSquare(m, 8) - (1.0f * 1.0f);
        float det = (b * b) - ((4.0f * a) * c);
        if (det >= 0.0f) {
            float u = (2.0f * TFVector3D.dot(m, 8, m, 4)) - ((float) Math.sqrt(det));
            float u2 = u / (2.0f * TFVector3D.lengthSquare(m, 4));
            TFVector3D.set(this.mHitPoint, 0, m, 4);
            TFVector3D.mul(this.mHitPoint, 0, u2);
            TFVector3D.add(this.mHitPoint, 0, m, 0);
            TFVector3D.setW(this.mHitPoint, 0);
            Matrix.multiplyMV(this.mHitPoint, 4, this.mMatrix, 0, this.mHitPoint, 0);
            this.mHitFace = 0;
        }
    }

    public void setSize(float width, float height) {
        Log.d(TAG, "setSize: START");
        this.mWidth = width;
        this.mHeight = height;
        FloatBuffer vertex = ByteBuffer.allocateDirect(12672).order(ByteOrder.nativeOrder()).asFloatBuffer();
        FloatBuffer texCoord = ByteBuffer.allocateDirect(8448).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fillVertex(vertex);
        vertex.position(0);
        fillTexCoord(texCoord);
        texCoord.position(0);
        this.mVertexBuffer = vertex;
        this.mTextureBuffer = texCoord;
        Log.d(TAG, "setSize: FINISH");
    }

    private void drawVertex(GL10 gl) {
        int offset = 0;
        for (int i = 16; i > 0; i--) {
            gl.glDrawArrays(5, offset, 66);
            offset += 66;
        }
    }

    private void fillVertex(FloatBuffer vertex) {
        vertex.position(0);
        for (int j = 0; j < 16; j++) {
            float latitude1 = ((90.0f - (11.25f * (j + 0))) * 3.1415927f) / 180.0f;
            float latitude2 = ((90.0f - (11.25f * (j + 1))) * 3.1415927f) / 180.0f;
            float latitude1Sin = (float) Math.sin(latitude1);
            float latitude1Cos = (float) Math.cos(latitude1);
            float latitude2Sin = (float) Math.sin(latitude2);
            float latitude2Cos = (float) Math.cos(latitude2);
            for (int i = 0; i <= 32; i++) {
                float longitude = (((-180.0f) + (11.25f * i)) * 3.1415927f) / 180.0f;
                float longitudeSin = (float) Math.sin(longitude);
                float longitudeCos = (float) Math.cos(longitude);
                float x = latitude1Cos * longitudeSin;
                float z = latitude1Cos * longitudeCos;
                vertex.put(x);
                vertex.put(latitude1Sin);
                vertex.put(z);
                float x2 = latitude2Cos * longitudeSin;
                float z2 = latitude2Cos * longitudeCos;
                vertex.put(x2);
                vertex.put(latitude2Sin);
                vertex.put(z2);
            }
        }
        vertex.position(0);
    }

    private void fillTexCoord(FloatBuffer texCoord) {
        fillTexCoordFace(texCoord, 0, 1.0f, 1.0f);
    }

    private void fillTexCoordFace(FloatBuffer texCoord, int face, float width, float height) {
        texCoord.position(0);
        for (int j = 0; j < 16; j++) {
            for (int i = 0; i <= 32; i++) {
                float u = i / 32.0f;
                float v = (j + 0) / 16.0f;
                texCoord.put(u * width);
                texCoord.put(v * height);
                float u2 = i / 32.0f;
                float v2 = (j + 1) / 16.0f;
                texCoord.put(u2 * width);
                texCoord.put(v2 * height);
            }
        }
        texCoord.position(0);
    }

    private static FloatBuffer allocBuffer(int size) {
        return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void showLatitude(boolean show, int step) {
        showLatitude(show, step, 64);
    }

    public void showLongitude(boolean show, int step) {
        showLongitude(show, step, 64);
    }

    public void showLatitude(boolean show, int step, int split) {
        if (show) {
            this.mLatitude = buildLatitude(step, split);
            this.mLatitudeStep = step;
            this.mLatitudeSplit = split;
            return;
        }
        this.mLatitude = null;
    }

    public void showLongitude(boolean show, int step, int split) {
        if (show) {
            this.mLongitude = buildLongitude(step, split);
            this.mLongitudeStep = step;
            this.mLongitudeSplit = split;
            return;
        }
        this.mLatitude = null;
    }

    public void setLatitudeColor(int equatorColor, int color) {
        this.mLatitudeEquatorColor[0] = Color.red(equatorColor) / 255.0f;
        this.mLatitudeEquatorColor[1] = Color.green(equatorColor) / 255.0f;
        this.mLatitudeEquatorColor[2] = Color.blue(equatorColor) / 255.0f;
        this.mLatitudeEquatorColor[3] = Color.alpha(equatorColor) / 255.0f;
        this.mLatitudeColor[0] = Color.red(color) / 255.0f;
        this.mLatitudeColor[1] = Color.green(color) / 255.0f;
        this.mLatitudeColor[2] = Color.blue(color) / 255.0f;
        this.mLatitudeColor[3] = Color.alpha(color) / 255.0f;
    }

    public void setLongitudeColor(int primeColor, int color) {
        this.mLongitudePrimeColor[0] = Color.red(primeColor) / 255.0f;
        this.mLongitudePrimeColor[1] = Color.green(primeColor) / 255.0f;
        this.mLongitudePrimeColor[2] = Color.blue(primeColor) / 255.0f;
        this.mLongitudePrimeColor[3] = Color.alpha(primeColor) / 255.0f;
        this.mLongitudeColor[0] = Color.red(color) / 255.0f;
        this.mLongitudeColor[1] = Color.green(color) / 255.0f;
        this.mLongitudeColor[2] = Color.blue(color) / 255.0f;
        this.mLongitudeColor[3] = Color.alpha(color) / 255.0f;
    }

    public void setLatitudeWidth(float width) {
        this.mLatitudeWidth = width;
    }

    public void setLongitudeWidth(float width) {
        this.mLongitudeWidth = width;
    }

    protected static FloatBuffer buildLatitude(int stepLat, int split) {
        int stepCount = (90 / stepLat) - 1;
        FloatBuffer b = allocBuffer(((stepCount * 2) + 1) * split * 3);
        b.position(0);
        for (int i = 0; i < split; i++) {
            float rad = (i / split) * 6.2831855f;
            float x = (float) Math.sin(rad);
            float z = (float) Math.cos(rad);
            b.put(x);
            b.put(0.0f);
            b.put(z);
        }
        int j = stepLat;
        while (j < 90) {
            float latRad = (j / 90.0f) * 1.5707964f;
            float latCos = (float) Math.cos(latRad);
            float latSin = (float) Math.sin(latRad);
            for (int i2 = 0; i2 < split; i2++) {
                float rad2 = (i2 / split) * 6.2831855f;
                float x2 = ((float) Math.sin(rad2)) * latCos;
                float z2 = ((float) Math.cos(rad2)) * latCos;
                b.put(x2);
                b.put(latSin);
                b.put(z2);
            }
            float y = -latSin;
            for (int i3 = 0; i3 < split; i3++) {
                float rad3 = (i3 / split) * 6.2831855f;
                float x3 = ((float) Math.sin(rad3)) * latCos;
                float z3 = ((float) Math.cos(rad3)) * latCos;
                b.put(x3);
                b.put(y);
                b.put(z3);
            }
            j += stepLat;
        }
        b.position(0);
        return b;
    }

    protected static FloatBuffer buildLongitude(int step, int split) {
        int stepCount = (180 / step) - 1;
        FloatBuffer b = allocBuffer((stepCount + 1) * split * 3);
        b.position(0);
        for (int i = 0; i < split; i++) {
            float rad = (i / split) * 6.2831855f;
            float y = (float) Math.sin(rad);
            float z = (float) Math.cos(rad);
            b.put(0.0f);
            b.put(y);
            b.put(z);
        }
        int j = step;
        while (j < 180) {
            float meriRad = (j / 180.0f) * 3.1415927f;
            float meriCos = (float) Math.cos(meriRad);
            float meriSin = (float) Math.sin(meriRad);
            for (int i2 = 0; i2 < split; i2++) {
                float latRad = (i2 / split) * 6.2831855f;
                float latCos = (float) Math.cos(latRad);
                float latSin = (float) Math.sin(latRad);
                float x = latCos * meriSin;
                float z2 = latCos * meriCos;
                b.put(x);
                b.put(latSin);
                b.put(z2);
            }
            j += step;
        }
        b.position(0);
        return b;
    }

    protected void drawLatitudeAndLongitude(GL10 gl) {
        if (this.mLatitude != null || this.mLongitude != null) {
            gl.glPushMatrix();
            gl.glScalef(1.0f, 1.0f, 1.0f);
            gl.glDisable(3553);
            gl.glEnable(2848);
            if (this.mLatitude != null) {
                int c = this.mLatitudeSplit;
                int stepCount = (90 / this.mLatitudeStep) - 1;
                gl.glLineWidth(this.mLatitudeWidth);
                gl.glVertexPointer(3, 5126, 0, this.mLatitude);
                gl.glColor4f(this.mLatitudeEquatorColor[0], this.mLatitudeEquatorColor[1], this.mLatitudeEquatorColor[2], this.mOpacity);
                gl.glDrawArrays(2, 0, c);
                int offset = 0 + c;
                gl.glColor4f(this.mLatitudeColor[0], this.mLatitudeColor[1], this.mLatitudeColor[2], this.mOpacity);
                for (int i = stepCount; i > 0; i--) {
                    gl.glDrawArrays(2, offset, c);
                    int offset2 = offset + c;
                    gl.glDrawArrays(2, offset2, c);
                    offset = offset2 + c;
                }
            }
            if (this.mLongitude != null) {
                int c2 = this.mLongitudeSplit;
                int stepCount2 = (180 / this.mLongitudeStep) - 1;
                gl.glLineWidth(this.mLongitudeWidth);
                gl.glVertexPointer(3, 5126, 0, this.mLongitude);
                gl.glColor4f(this.mLongitudePrimeColor[0], this.mLongitudePrimeColor[1], this.mLongitudePrimeColor[2], this.mOpacity);
                gl.glDrawArrays(2, 0, c2);
                int offset3 = 0 + c2;
                gl.glColor4f(this.mLongitudeColor[0], this.mLongitudeColor[1], this.mLongitudeColor[2], this.mOpacity);
                for (int i2 = stepCount2; i2 > 0; i2--) {
                    gl.glDrawArrays(2, offset3, c2);
                    offset3 += c2;
                }
            }
            gl.glDisable(2848);
            gl.glEnable(3553);
            gl.glPopMatrix();
        }
    }
}
