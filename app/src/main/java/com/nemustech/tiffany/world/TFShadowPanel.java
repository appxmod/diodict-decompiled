package com.nemustech.tiffany.world;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFShadowPanel extends TFCustomPanel {
    static final String TAG = "TFShadowPanel";
    protected FloatBuffer mColorBuffer;
    protected float[] mMeshColor;

    public TFShadowPanel(float width, float height) {
        super(width, height, 8, 8);
        this.mRequestUpdateTexCoordCount = 0;
    }

    public TFShadowPanel(float width, float height, int meshWidth, int meshHeight) {
        super(width, height, meshWidth, meshHeight);
        this.mRequestUpdateTexCoordCount = 0;
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel
    public void setSize(float width, float height, int meshWidth, int meshHeight) {
        this.mWidth = width;
        this.mHeight = height;
        this.mMeshWidth = meshWidth;
        this.mMeshHeight = meshHeight;
        this.mFaceOffset = this.mMeshHeight * (this.mMeshWidth + 1) * 2;
        int vertexCount = (this.mMeshHeight + 1) * (this.mMeshWidth + 1);
        this.mMeshVertex = new float[vertexCount * 3];
        loadIdentityVertex(this.mMeshVertex);
        this.mMeshColor = new float[vertexCount * 4];
        loadIdentityColor(this.mMeshColor);
        this.mMeshTexCoord = new float[vertexCount * 2];
        loadIdentityTexCoord(this.mMeshTexCoord);
        this.mVertexBuffer = newFloatBuffer(this.mFaceOffset * 3);
        this.mColorBuffer = newFloatBuffer(this.mFaceOffset * 4);
        this.mTextureBuffer = newFloatBuffer(this.mFaceOffset * 2);
    }

    protected void loadIdentityColor(float[] color) {
        int meshW = this.mMeshWidth;
        int meshH = this.mMeshHeight;
        for (int j = 0; j <= meshH; j++) {
            int meshIndex = (meshW + 1) * j * 4;
            for (int i = 0; i <= meshW; i++) {
                color[meshIndex + 0] = 0.0f;
                color[meshIndex + 1] = 0.0f;
                color[meshIndex + 2] = 0.0f;
                color[meshIndex + 3] = 0.0f;
                meshIndex += 4;
            }
        }
    }

    protected void buildColorArray() {
        int meshW = this.mMeshWidth;
        int meshH = this.mMeshHeight;
        int vertexW = this.mMeshWidth + 1;
        float[] rgba = this.mTempFloat8;
        this.mColorBuffer.position(0);
        for (int j = 0; j < meshH; j++) {
            int index1 = (j + 1) * vertexW * 4;
            int index2 = j * vertexW * 4;
            for (int i = 0; i <= meshW; i++) {
                rgba[0] = this.mMeshColor[index1 + 0];
                rgba[1] = this.mMeshColor[index1 + 1];
                rgba[2] = this.mMeshColor[index1 + 2];
                rgba[3] = this.mMeshColor[index1 + 3];
                rgba[4] = this.mMeshColor[index2 + 0];
                rgba[5] = this.mMeshColor[index2 + 1];
                rgba[6] = this.mMeshColor[index2 + 2];
                rgba[7] = this.mMeshColor[index2 + 3];
                this.mColorBuffer.put(rgba);
                index1 += 4;
                index2 += 4;
            }
        }
        this.mColorBuffer.position(0);
    }

    public void setMeshVertex(float[] vertexBuffer) {
        for (int i = 0; i < this.mMeshVertex.length; i++) {
            this.mMeshVertex[i] = vertexBuffer[i];
        }
        buildVertexArray(0);
    }

    public void setMeshColor(float[] colorBuffer) {
        for (int i = 0; i < this.mMeshColor.length; i++) {
            this.mMeshColor[i] = colorBuffer[i];
        }
        buildColorArray();
    }

    public float[] getMeshColor() {
        return this.mMeshColor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFCustomPanel
    public void drawVertex(GL10 gl, int face) {
        if (this.mTextures.getTextureCount() == 0) {
            gl.glDisable(3553);
        }
        gl.glEnableClientState(32886);
        gl.glColorPointer(4, 5126, 0, this.mColorBuffer);
        super.drawVertex(gl, face);
        gl.glDisableClientState(32886);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onSetTexture(GL10 gl, int index, int layer) {
        if (this.mTextures.getTextureCount() > 0) {
            this.mTextures.setTextureByIndex(gl, index, layer);
        }
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void setWidth(float width) {
        this.mWidth = width;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void setHeight(float height) {
        this.mHeight = height;
    }
}
