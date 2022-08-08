package com.nemustech.tiffany.world;

import android.graphics.Rect;
import android.opengl.Matrix;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFFlexiblePanel extends TFModel {
    public static final int N = 8;
    static final String TAG = "TFFlexiblePanel";
    protected float[] XYZ;
    private boolean mDrawReflection;
    private int[] mImageOrientation;
    protected PointBlender mNewPointBlender;
    protected PointBlender mPointBlender;
    protected boolean mPointBlenderChanged;
    private int[] mReverseWay;

    /* loaded from: classes.dex */
    public interface PointBlender {
        void calcXYZ(int i, int i2, float[] fArr);

        boolean isNextAvailable();

        void onEnd();

        boolean onFrame(int i);

        void onStart();
    }

    public TFFlexiblePanel() {
        this.mPointBlender = null;
        this.mNewPointBlender = null;
        this.mPointBlenderChanged = false;
        this.XYZ = new float[3];
        this.mDrawReflection = true;
        create(1.0f, 1.0f);
    }

    public TFFlexiblePanel(float width, float height) {
        this.mPointBlender = null;
        this.mNewPointBlender = null;
        this.mPointBlenderChanged = false;
        this.XYZ = new float[3];
        this.mDrawReflection = true;
        create(width, height);
    }

    public TFFlexiblePanel(TFWorld world, float width, float height) {
        this.mPointBlender = null;
        this.mNewPointBlender = null;
        this.mPointBlenderChanged = false;
        this.XYZ = new float[3];
        this.mDrawReflection = true;
        create(width, height);
        super.attachTo(world);
    }

    public TFFlexiblePanel(TFHolder holder, float width, float height) {
        this.mPointBlender = null;
        this.mNewPointBlender = null;
        this.mPointBlenderChanged = false;
        this.XYZ = new float[3];
        this.mDrawReflection = true;
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
            fillTexCoordFace(this.mTextureBuffer, index, 1.0f, 1.0f);
        }
    }

    public void reverseImage(int index, int reverseWay) {
        if (this.mReverseWay[index] != reverseWay) {
            this.mReverseWay[index] = reverseWay;
            fillTexCoordFace(this.mTextureBuffer, index, 1.0f, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean updateObject(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        if (!super.updateObject(gl, tickPassed, bDoGLCalc)) {
            return false;
        }
        if (this.mPointBlenderChanged) {
            this.mPointBlenderChanged = false;
            if (this.mPointBlender == null) {
                fillVertex(this.mVertexBuffer);
                this.mDrawReflection = checkPoints(this.mVertexBuffer);
            } else {
                this.mPointBlender.onEnd();
            }
            this.mPointBlender = this.mNewPointBlender;
            this.mNewPointBlender = null;
            if (this.mPointBlender != null) {
                this.mPointBlender.onStart();
            }
        }
        if (this.mPointBlender != null) {
            this.mEffectStatus = 1;
            this.mWorld.requestRender();
            if (this.mPointBlender.onFrame(tickPassed)) {
                fillVertex(this.mVertexBuffer);
                this.mDrawReflection = checkPoints(this.mVertexBuffer);
            }
            if (!this.mPointBlender.isNextAvailable()) {
                this.mPointBlender.onEnd();
                this.mPointBlender = null;
                this.mEffectStatus = 2;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        fillTexCoordFace(this.mTextureBuffer, index, rectTexture.right / textureWidth, rectTexture.bottom / textureHeight);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean isInEffectAnimation() {
        return super.isInEffectAnimation() || this.mPointBlender != null;
    }

    public void setSize(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        int i = 144 * 2;
        FloatBuffer vertex = ByteBuffer.allocateDirect(3456).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fillVertex(vertex);
        this.mDrawReflection = checkPoints(vertex);
        this.mVertexBuffer = vertex;
        FloatBuffer texCoord = ByteBuffer.allocateDirect(2304).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fillTexCoord(texCoord);
        this.mTextureBuffer = texCoord;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onDrawVertex(GL10 gl, int faceIndex, boolean reflection) {
        drawVertex(gl, faceIndex);
    }

    /* loaded from: classes.dex */
    public static class IdentityPointBlender implements PointBlender {
        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void calcXYZ(int s, int t, float[] xyz) {
            xyz[0] = s / 8.0f;
            xyz[1] = t / 8.0f;
            xyz[2] = 0.0f;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onStart() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean onFrame(int tick) {
            return true;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onEnd() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean isNextAvailable() {
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class Bezier3DPointBlender implements PointBlender {
        private static final float[][] mBernstein3D = initBernstein3D();
        public float[] mPoints = new float[48];

        public Bezier3DPointBlender() {
            setIdentityPoints(this.mPoints);
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void calcXYZ(int s, int t, float[] xyz) {
            float x = 0.0f;
            float y = 0.0f;
            float z = 0.0f;
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 4; i++) {
                    float m = mBernstein3D[j][t] * mBernstein3D[i][s];
                    int index = ((j * 4) + i) * 3;
                    x += this.mPoints[index + 0] * m;
                    y += this.mPoints[index + 1] * m;
                    z += this.mPoints[index + 2] * m;
                }
            }
            xyz[0] = x;
            xyz[1] = y;
            xyz[2] = z;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onStart() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean onFrame(int tick) {
            return false;
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public void onEnd() {
        }

        @Override // com.nemustech.tiffany.world.TFFlexiblePanel.PointBlender
        public boolean isNextAvailable() {
            return false;
        }

        public static void setIdentityPoints(float[] points) {
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 4; i++) {
                    points[(((j * 4) + i) * 3) + 0] = i / 3.0f;
                    points[(((j * 4) + i) * 3) + 1] = j / 3.0f;
                    points[(((j * 4) + i) * 3) + 2] = 0.0f;
                }
            }
        }

        private static final float[][] initBernstein3D() {
            float[][] table = (float[][]) Array.newInstance(Float.TYPE, 4, 9);
            for (int i = 0; i <= 8; i++) {
                float t = i / 8.0f;
                table[0][i] = (1.0f - t) * (1.0f - t) * (1.0f - t);
                table[1][i] = (1.0f - t) * 3.0f * (1.0f - t) * t;
                table[2][i] = (1.0f - t) * 3.0f * t * t;
                table[3][i] = t * t * t;
            }
            return table;
        }
    }

    public void setPointBlender(PointBlender pb) {
        this.mNewPointBlender = pb;
        this.mPointBlenderChanged = true;
        this.mWorld.requestRender();
    }

    protected void drawVertex(GL10 gl, int face) {
        int offset = face * 8 * 9 * 2;
        for (int i = 0; i < 8; i++) {
            gl.glDrawArrays(5, (i * 9 * 2) + offset, 18);
        }
    }

    private void fillVertexFace(FloatBuffer vertex, int face, float width, float height) {
        int offset = face * 8 * 9 * 2 * 3;
        float[] coord = this.XYZ;
        PointBlender pb = this.mPointBlender;
        vertex.position(offset);
        if (pb == null) {
            coord[2] = 0.0f;
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i <= 8; i++) {
                    coord[0] = i / 8.0f;
                    coord[1] = (j + 1) / 8.0f;
                    vertex.put((coord[0] - 0.5f) * width);
                    vertex.put((coord[1] - 0.5f) * height);
                    vertex.put(coord[2]);
                    coord[1] = (j + 0) / 8.0f;
                    vertex.put((coord[0] - 0.5f) * width);
                    vertex.put((coord[1] - 0.5f) * height);
                    vertex.put(coord[2]);
                }
            }
        } else {
            for (int j2 = 0; j2 < 8; j2++) {
                for (int i2 = 0; i2 <= 8; i2++) {
                    pb.calcXYZ(i2, j2 + 1, coord);
                    vertex.put((coord[0] - 0.5f) * width);
                    vertex.put((coord[1] - 0.5f) * height);
                    vertex.put(coord[2]);
                    pb.calcXYZ(i2, j2, coord);
                    vertex.put((coord[0] - 0.5f) * width);
                    vertex.put((coord[1] - 0.5f) * height);
                    vertex.put(coord[2]);
                }
            }
        }
        vertex.position(0);
    }

    private void fillTexCoordFace(FloatBuffer texCoord, int face, float width, float height) {
        int offset = face * 8 * 9 * 2 * 2;
        int reverse = this.mReverseWay[face];
        texCoord.position(offset);
        for (int j = 0; j < 8; j++) {
            float t1 = j / 8.0f;
            float t2 = (j + 1) / 8.0f;
            for (int i = 0; i <= 8; i++) {
                float s = i / 8.0f;
                switch (reverse) {
                    case 1:
                        texCoord.put((1.0f - s) * width);
                        texCoord.put((1.0f - t2) * height);
                        texCoord.put((1.0f - s) * width);
                        texCoord.put((1.0f - t1) * height);
                        break;
                    case 2:
                        texCoord.put(s * width);
                        texCoord.put(t2 * height);
                        texCoord.put(s * width);
                        texCoord.put(t1 * height);
                        break;
                    case 3:
                        texCoord.put((1.0f - s) * width);
                        texCoord.put(t2 * height);
                        texCoord.put((1.0f - s) * width);
                        texCoord.put(t1 * height);
                        break;
                    default:
                        texCoord.put(s * width);
                        texCoord.put((1.0f - t2) * height);
                        texCoord.put(s * width);
                        texCoord.put((1.0f - t1) * height);
                        break;
                }
            }
        }
        texCoord.position(0);
    }

    private void fillVertex(FloatBuffer vertex) {
        fillVertexFace(vertex, 0, this.mWidth, this.mHeight);
        fillVertexFace(vertex, 1, this.mWidth, this.mHeight);
        vertex.position(0);
    }

    private void fillTexCoord(FloatBuffer texCoord) {
        fillTexCoordFace(texCoord, 0, 1.0f, 1.0f);
        fillTexCoordFace(texCoord, 1, 1.0f, 1.0f);
        texCoord.position(0);
    }

    private boolean checkPoints(FloatBuffer vertex) {
        boolean ret = true;
        if (this.mWorld == null) {
            return true;
        }
        if (this.mWorld.mReflection) {
            float objectY = this.mLocation[1];
            if (this.mParentHolder != null) {
                objectY += this.mParentHolder.mLocation[1];
            }
            int i = 0;
            while (true) {
                if (i >= 144) {
                    break;
                }
                float y = vertex.get((i * 3) + 1) + objectY;
                if (y >= this.mWorld.mReflectingFloor) {
                    i++;
                } else {
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void updateHitPoint() {
        super.updateHitPoint();
        float[] work = new float[471];
        float tMin = Float.POSITIVE_INFINITY;
        this.mVertexBuffer.position(0);
        this.mVertexBuffer.get(work, 0, 432);
        this.mVertexBuffer.position(0);
        for (int i = 0; i < 8; i++) {
            int row = i * 9 * 2;
            for (int j = 0; j < 16; j++) {
                int a = (row + j + 0) * 3;
                int b = (row + j + 1) * 3;
                int c = (row + j + 2) * 3;
                if (j % 2 != 0) {
                    b = c;
                    c = b;
                }
                int hit = TFModel.doHitTestVertexTrigangle(work, a, b, c, this.mHitTestLine, work, 432, work, 436, work, 439);
                if (hit >= 0 && tMin >= work[438]) {
                    tMin = work[438];
                    this.mHitFace = hit;
                    System.arraycopy(work, 432, this.mHitPoint, 0, 4);
                }
            }
        }
        if (this.mHitFace >= 0) {
            TFVector3D.setW(this.mHitPoint, 0);
            Matrix.multiplyMV(this.mHitPoint, 4, this.mMatrix, 0, this.mHitPoint, 0);
        }
    }
}
