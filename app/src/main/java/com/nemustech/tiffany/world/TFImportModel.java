package com.nemustech.tiffany.world;

import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFImportModel extends TFModel {
    private static final String TAG = "TFImportModel";
    protected TFImporter mImportModel;
    protected float[] mModelColor = new float[4];

    public TFImportModel(TFImporter importModel) {
        setImportModel(importModel);
        setModelColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public float getModelWidth() {
        if (this.mImportModel == null) {
            return 0.0f;
        }
        return this.mImportModel.mBoundBox[1] - this.mImportModel.mBoundBox[0];
    }

    public float getModelHeight() {
        if (this.mImportModel == null) {
            return 0.0f;
        }
        return this.mImportModel.mBoundBox[3] - this.mImportModel.mBoundBox[2];
    }

    public float getModelDepth() {
        if (this.mImportModel == null) {
            return 0.0f;
        }
        return this.mImportModel.mBoundBox[5] - this.mImportModel.mBoundBox[4];
    }

    public void setImportModel(TFImporter importModel) {
        this.mImportModel = importModel;
        this.mWidth = 1.0f;
        this.mHeight = 1.0f;
        this.mDepth = 1.0f;
        this.mVertexBuffer = null;
        if (importModel != null) {
            this.mWidth = this.mImportModel.mBoundBox[1] - this.mImportModel.mBoundBox[0];
            this.mHeight = this.mImportModel.mBoundBox[3] - this.mImportModel.mBoundBox[2];
            this.mDepth = this.mImportModel.mBoundBox[5] - this.mImportModel.mBoundBox[4];
            this.mVertexBuffer = importModel.mVertexBuffer;
        }
    }

    public TFImporter getImportModel() {
        return this.mImportModel;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.mWidth = getModelWidth() * scaleX;
        this.mHeight = getModelHeight() * scaleY;
        this.mDepth = getModelDepth() * scaleZ;
    }

    public void setSize(float width, float height, float depth) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDepth = depth;
    }

    public void setModelColor(float r, float g, float b, float a) {
        this.mModelColor[0] = r;
        this.mModelColor[1] = g;
        this.mModelColor[2] = b;
        this.mModelColor[3] = a;
    }

    void draw(GL10 gl, int tickPassed) {
        if (this.mVisible && this.mShouldDraw && this.mImportModel != null && this.mImportModel.canDraw()) {
            gl.glEnableClientState(32884);
            gl.glVertexPointer(3, 5126, 0, this.mImportModel.mVertexBuffer);
            if (this.mImportModel.mColorBuffer != null) {
                gl.glEnableClientState(32886);
                gl.glColorPointer(4, 5126, 0, this.mImportModel.mColorBuffer);
            } else {
                gl.glDisableClientState(32886);
                gl.glColor4f(this.mModelColor[0], this.mModelColor[1], this.mModelColor[2], this.mOpacity);
            }
            if (this.mImportModel.mNormalBuffer != null) {
                gl.glEnableClientState(32885);
                gl.glEnable(32826);
                gl.glNormalPointer(5126, 0, this.mImportModel.mNormalBuffer);
            } else {
                gl.glDisableClientState(32885);
            }
            if (this.mImportModel.mTextureBuffer != null) {
                gl.glEnableClientState(32888);
                gl.glTexCoordPointer(2, 5126, 0, this.mImportModel.mTextureBuffer);
                if (this.mTextures.getMaxTextureIndex() > 0) {
                    this.mTextures.setTextureByIndex(gl, 0);
                }
            } else {
                gl.glDisableClientState(32888);
            }
            gl.glDisable(2884);
            gl.glScalef(this.mWidth / getModelWidth(), this.mHeight / getModelHeight(), this.mDepth / getModelDepth());
            this.mImportModel.drawModel(gl);
            gl.glDisableClientState(32886);
            gl.glDisableClientState(32885);
            gl.glEnableClientState(32888);
            gl.glEnable(2884);
        }
    }
}
