package com.nemustech.tiffany.world;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.Matrix;
import com.nemustech.tiffany.world.TFWorld;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFCube extends TFModel {
    static final String TAG = "TFCube";
    private static float mMirrorY;
    private static float mObjectY;

    public TFCube(TFWorld world, float width, float height) {
        this(world, width, height, width);
    }

    public TFCube(float width, float height, float depth) {
        this.mTextures.setNumFaces(6);
        setBackFaceVisibility(false);
        setSize(width, height, depth);
    }

    public TFCube(float width, float height) {
        this(width, height, width);
    }

    public TFCube(TFWorld world, float width, float height, float depth) {
        this(width, height, depth);
        attachTo(world);
    }

    public TFError setImageResource(Resources resources, int[] resource_id) {
        if (resource_id.length > 6) {
            return TFError.INVALID_PARAM;
        }
        for (int i = 0; i < resource_id.length; i++) {
            super.setImageResource(i, resources, resource_id[i]);
        }
        return TFError.ERROR_NONE;
    }

    public TFError setImageResource(Bitmap[] bmp, Rect rectSet) {
        if (bmp.length > 6) {
            return TFError.INVALID_PARAM;
        }
        for (int i = 0; i < bmp.length; i++) {
            this.mTextures.setImageResource(i, bmp[i], rectSet);
        }
        return TFError.ERROR_NONE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        float newRight = rectTexture.right / textureWidth;
        float newBottom = rectTexture.bottom / textureHeight;
        float[] texCoords = new float[8];
        switch (index) {
            case 0:
                texCoords[0] = 0.0f;
                texCoords[1] = newBottom;
                texCoords[2] = newRight;
                texCoords[3] = newBottom;
                texCoords[4] = 0.0f;
                texCoords[5] = 0.0f;
                texCoords[6] = newRight;
                texCoords[7] = 0.0f;
                break;
            case 1:
            case 2:
            case 3:
                texCoords[0] = newRight;
                texCoords[1] = newBottom;
                texCoords[2] = newRight;
                texCoords[3] = 0.0f;
                texCoords[4] = 0.0f;
                texCoords[5] = newBottom;
                texCoords[6] = 0.0f;
                texCoords[7] = 0.0f;
                break;
            case 4:
                texCoords[0] = newRight;
                texCoords[1] = 0.0f;
                texCoords[2] = 0.0f;
                texCoords[3] = 0.0f;
                texCoords[4] = newRight;
                texCoords[5] = newBottom;
                texCoords[6] = 0.0f;
                texCoords[7] = newBottom;
                break;
            case 5:
                texCoords[0] = 0.0f;
                texCoords[1] = 0.0f;
                texCoords[2] = 0.0f;
                texCoords[3] = newBottom;
                texCoords[4] = newRight;
                texCoords[5] = 0.0f;
                texCoords[6] = newRight;
                texCoords[7] = newBottom;
                break;
        }
        this.mTextureBuffer.position(index * 8);
        this.mTextureBuffer.put(texCoords);
        this.mTextureBuffer.rewind();
    }

    public void setSize(float width, float height, float depth) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDepth = depth;
        float w = width / 2.0f;
        float h = height / 2.0f;
        float d = depth / 2.0f;
        float[] vertices = {-w, -h, d, w, -h, d, -w, h, d, w, h, d, -w, -h, -d, -w, h, -d, w, -h, -d, w, h, -d, -w, -h, d, -w, h, d, -w, -h, -d, -w, h, -d, w, -h, -d, w, h, -d, w, -h, d, w, h, d, -w, h, d, w, h, d, -w, h, -d, w, h, -d, -w, -h, d, -w, -h, -d, w, -h, d, w, -h, -d};
        float[] texCoords = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        this.mVertexBuffer = vbb.asFloatBuffer();
        this.mVertexBuffer.put(vertices);
        this.mVertexBuffer.position(0);
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        this.mTextureBuffer = tbb.asFloatBuffer();
        this.mTextureBuffer.put(texCoords);
        this.mTextureBuffer.position(0);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onTouchDrag(float start_x, float start_y, float end_x, float end_y, int tickPassed) {
        calcForce(start_x, start_y, end_x, end_y, tickPassed);
        float[] vertices = new float[8];
        float[] near = new float[1];
        int face_index = this.mWorld.mRenderer.getSelectedFaceIndex(this, end_x, end_y, near);
        if (face_index >= 0) {
            this.mWorld.mRenderer.getFaceVertices(this, face_index, vertices);
        }
        float max = Math.max(vertices[0], vertices[2]);
        float max2 = Math.max(Math.max(max, vertices[4]), vertices[6]);
        float min = Math.min(Math.min(Math.min(vertices[0], vertices[2]), vertices[4]), vertices[6]);
        float base = min + ((max2 - min) / 2.0f);
        boolean bSpinUpward = false;
        if (face_index == 2 || face_index == 3) {
            if (this.mAngle[0] >= 180.0f && this.mAngle[0] < 360.0f) {
                if (start_x <= base) {
                    bSpinUpward = true;
                }
            } else if (start_x >= base) {
                bSpinUpward = true;
            }
        } else if (this.mAngle[0] < 90.0f || this.mAngle[0] > 270.0f) {
            bSpinUpward = true;
        }
        float forceX = this.mForceFromSide * 5.0f;
        float forceY = this.mForceFromHead * 5.0f;
        if (!bSpinUpward) {
            forceY = -forceY;
        }
        spin(forceX, forceY, 0.0f, false);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void setWeight(float weight) {
        super.setWeight(weight / 2.0f);
        this.mRotationResistency *= 4.0f;
    }

    private void prvEffectLookFront(int selectedIndex) {
        float[] targetAngle = new float[3];
        switch (selectedIndex) {
            case 1:
                targetAngle[0] = 180.0f;
                targetAngle[1] = 0.0f;
                break;
            case 2:
                targetAngle[0] = 90.0f;
                targetAngle[1] = 0.0f;
                break;
            case 3:
                targetAngle[0] = 270.0f;
                targetAngle[1] = 0.0f;
                break;
            case 4:
                targetAngle[0] = 180.0f;
                targetAngle[1] = 270.0f;
                break;
            case 5:
                targetAngle[0] = 0.0f;
                targetAngle[1] = 270.0f;
                break;
            default:
                targetAngle[0] = 0.0f;
                targetAngle[1] = 0.0f;
                targetAngle[2] = 0.0f;
                break;
        }
        super.rotate(targetAngle[0], targetAngle[1], 0.281f, 2);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    public void showEffect(int effectID, int targetIndex) {
        switch (effectID) {
            case 0:
                prvEffectLookFront(targetIndex);
                setEffectFinishListener(new TFWorld.OnEffectFinishListener() { // from class: com.nemustech.tiffany.world.TFCube.1
                    @Override // com.nemustech.tiffany.world.TFWorld.OnEffectFinishListener
                    public void onEffectFinish(TFObject object) {
                        TFCube.this.setEffectFinishListener(null);
                        TFCube.this.move(TFCube.this.mLocation[0], 0.0f, 1.6f, 0.01f);
                    }
                });
                return;
            case 1:
                super.showEffect(effectID, targetIndex);
                return;
            default:
                return;
        }
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected int onCullFace(int index, boolean reflection) {
        return reflection ? 1028 : 1029;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onCalcReflection(GL10 gl, float y) {
        gl.glLoadIdentity();
        gl.glTranslatef(this.mLocation[0], -((2.0f * y) + this.mHeight), this.mLocation[2]);
        gl.glRotatef(this.mAngle[0], 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-this.mAngle[1], 1.0f, 0.0f, 0.0f);
        gl.glScalef(1.0f, -1.0f, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public void updateHitPoint() {
        super.updateHitPoint();
        float xCoord = this.mWidth / 2.0f;
        float yCoord = this.mHeight / 2.0f;
        float zCoord = this.mDepth / 2.0f;
        float[] m = this.mHitPoint;
        float[] hitTestCoord = {zCoord, -zCoord, -xCoord, xCoord, yCoord, -yCoord};
        int[] hitTestPlane = {2, 2, 0, 0, 1, 1};
        float tMin = 100.0f;
        int face = -1;
        for (int i = 0; i < hitTestCoord.length; i++) {
            float t = TFVector3D.getPointOnLine(m, 0, this.mHitTestLine, 0, this.mHitTestLine, 4, hitTestCoord[i], hitTestPlane[i]);
            boolean xRange = hitTestPlane[i] == 0 || (m[0] >= (-xCoord) && m[0] <= xCoord);
            boolean yRange = hitTestPlane[i] == 1 || (m[1] >= (-yCoord) && m[1] <= yCoord);
            boolean zRange = hitTestPlane[i] == 2 || (m[2] >= (-zCoord) && m[2] <= zCoord);
            if (xRange && yRange && zRange && t < tMin) {
                tMin = t;
                face = i;
            }
        }
        if (face >= 0) {
            TFVector3D.getPointOnLine(m, 0, this.mHitTestLine, 0, this.mHitTestLine, 4, hitTestCoord[face], hitTestPlane[face]);
            TFVector3D.setW(m, 0);
            Matrix.multiplyMV(m, 4, this.mMatrix, 0, m, 0);
            this.mHitFace = face;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.nemustech.tiffany.world.TFModel
    public int getHitFace(float[] near) {
        int face = super.getHitFace(near);
        return face < 0 ? face : this.mHitFace;
    }
}
