package com.nemustech.tiffany.world;

import android.graphics.Rect;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFCustomPanel extends TFModel {
    public static final int MESH_HEIGHT = 8;
    public static final int MESH_WIDTH = 8;
    private static final String TAG = "TFCustomPanel";
    protected Blender mBlender;
    protected boolean mBlenderChanged;
    boolean mDrawReflection;
    protected int mFaceOffset;
    protected int mMeshHeight;
    protected float[] mMeshTexCoord;
    protected float[] mMeshVertex;
    protected int mMeshWidth;
    private float mMorphAnchorX;
    private float mMorphAnchorY;
    private float mMorphTargetHeight;
    private float mMorphTargetWidth;
    private Morpher mMorpher;
    protected Blender mNewBlender;
    private OnPanelMorphListener mOnPanelMorphListener;
    protected int mRequestUpdateTexCoordCount;
    protected int mRequestUpdateVertexCount;
    protected float[] mTempFloat6;
    protected float[] mTempFloat8;
    protected float[] mTextureHeight;
    protected float[] mTextureWidth;

    /* loaded from: classes.dex */
    public static abstract class Blender {
        public abstract boolean hasEnded();

        public abstract void onEnd();

        public abstract void onFrame(int i);

        public abstract void onStart();
    }

    /* loaded from: classes.dex */
    public interface OnPanelMorphListener {
        void onPanelMorph(float f, float f2);
    }

    public TFCustomPanel(float width, float height) {
        this(width, height, 8, 8);
    }

    public TFCustomPanel(float width, float height, int meshWidth, int meshHeight) {
        this.mTempFloat6 = new float[6];
        this.mTempFloat8 = new float[8];
        this.mDrawReflection = true;
        create();
        setSize(width, height, meshWidth, meshHeight);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    void adjustTextureCoordination(Rect rectTexture, int index, int textureWidth, int textureHeight) {
        setTextureSize(index, rectTexture.right / textureWidth, rectTexture.bottom / textureHeight);
        buildTexCoordArray(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean updateObject(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        boolean updated = false;
        if (!super.updateObject(gl, tickPassed, bDoGLCalc)) {
            return false;
        }
        if (this.mBlenderChanged) {
            if (this.mBlender != null) {
                this.mBlender.onEnd();
            }
            this.mBlender = this.mNewBlender;
            this.mNewBlender = null;
            this.mBlenderChanged = false;
            if (this.mBlender != null) {
                this.mBlender.onStart();
            }
        }
        if (this.mBlender != null) {
            this.mBlender.onFrame(tickPassed);
            if (this.mBlender.hasEnded()) {
                this.mBlender.onEnd();
                this.mBlender = null;
            }
        }
        if (this.mRequestUpdateVertexCount > 0) {
            buildVertexArray(0);
            buildVertexArray(1);
            this.mRequestUpdateVertexCount = 0;
            updated = true;
        }
        if (this.mRequestUpdateTexCoordCount > 0) {
            buildTexCoordArray(0);
            buildTexCoordArray(1);
            this.mRequestUpdateTexCoordCount = 0;
            updated = true;
        }
        if (updated) {
            this.mWorld.requestRender();
        }
        if (isInEffectAnimation()) {
            this.mEffectStatus = 1;
            this.mWorld.requestRender();
        } else if (this.mEffectStatus == 1) {
            this.mEffectStatus = 2;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nemustech.tiffany.world.TFObject
    public boolean isInEffectAnimation() {
        return super.isInEffectAnimation() || this.mBlender != null;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected void onDrawVertex(GL10 gl, int index, boolean reflection) {
        drawVertex(gl, index);
    }

    @Override // com.nemustech.tiffany.world.TFModel
    protected boolean onBeforeDraw(GL10 gl, int tickPassed) {
        applyMorph(tickPassed);
        return true;
    }

    @Override // com.nemustech.tiffany.world.TFModel
    void updateHitPoint() {
        super.updateHitPoint();
        int hitPointOffset = this.mMeshHeight * (this.mMeshWidth + 1) * 2 * 3;
        int uvtOffset = hitPointOffset + 4;
        int workOffset = uvtOffset + 3;
        float[] work = new float[workOffset + 32];
        float tMin = Float.POSITIVE_INFINITY;
        this.mVertexBuffer.position(0);
        this.mVertexBuffer.get(work, 0, hitPointOffset);
        this.mVertexBuffer.position(0);
        for (int i = 0; i < this.mMeshHeight; i++) {
            int row = (this.mMeshWidth + 1) * i * 2;
            for (int j = 0; j < ((this.mMeshWidth + 1) * 2) - 2; j++) {
                int a = (row + j + 0) * 3;
                int b = (row + j + 1) * 3;
                int c = (row + j + 2) * 3;
                if (j % 2 != 0) {
                    b = c;
                    c = b;
                }
                int hit = TFModel.doHitTestVertexTrigangle(work, a, b, c, this.mHitTestLine, work, hitPointOffset, work, uvtOffset, work, workOffset);
                if (hit >= 0 && tMin >= work[uvtOffset + 2]) {
                    tMin = work[uvtOffset + 2];
                    this.mHitFace = hit;
                    System.arraycopy(work, hitPointOffset, this.mHitPoint, 0, 4);
                }
            }
        }
        if (this.mHitFace >= 0) {
            TFVector3D.setW(this.mHitPoint, 0);
            Matrix.multiplyMV(this.mHitPoint, 4, this.mMatrix, 0, this.mHitPoint, 0);
        }
    }

    public void setSize(float width, float height, int meshWidth, int meshHeight) {
        this.mWidth = width;
        this.mHeight = height;
        this.mMeshWidth = meshWidth;
        this.mMeshHeight = meshHeight;
        this.mFaceOffset = this.mMeshHeight * (this.mMeshWidth + 1) * 2;
        int vertexCount = (this.mMeshHeight + 1) * (this.mMeshWidth + 1);
        this.mMeshVertex = new float[vertexCount * 3];
        loadIdentityVertex(this.mMeshVertex);
        this.mMeshTexCoord = new float[vertexCount * 2];
        loadIdentityTexCoord(this.mMeshTexCoord);
        int arraySize = this.mFaceOffset * 2;
        this.mVertexBuffer = newFloatBuffer(arraySize * 3);
        buildVertexArray(0);
        buildVertexArray(1);
        this.mTextureBuffer = newFloatBuffer(arraySize * 2);
        buildTexCoordArray(0);
        buildTexCoordArray(1);
    }

    public int getMeshWidth() {
        return this.mMeshWidth;
    }

    public int getMeshHeight() {
        return this.mMeshHeight;
    }

    public float[] getVertex() {
        return this.mMeshVertex;
    }

    public float[] getTexCoord() {
        return this.mMeshTexCoord;
    }

    public void requestUpdateVertex() {
        this.mRequestUpdateVertexCount++;
        this.mWorld.requestRender();
    }

    public void requestUpdateTexCoord() {
        this.mRequestUpdateTexCoordCount++;
        this.mWorld.requestRender();
    }

    protected void create() {
        setBackFaceVisibility(true);
        this.mTextureWidth = new float[2];
        this.mTextureHeight = new float[2];
        setTextureSize(0, 1.0f, 1.0f);
        setTextureSize(1, 1.0f, 1.0f);
        this.mRequestUpdateVertexCount = 0;
        this.mRequestUpdateTexCoordCount = 0;
        this.mBlender = null;
        this.mNewBlender = null;
        this.mBlenderChanged = false;
    }

    protected void setTextureSize(int face, float width, float height) {
        this.mTextureWidth[face] = width;
        this.mTextureHeight[face] = height;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void drawVertex(GL10 gl, int face) {
        int offset = face * this.mFaceOffset;
        int row = (this.mMeshWidth + 1) * 2;
        int height = this.mMeshHeight;
        for (int i = 0; i < height; i++) {
            gl.glDrawArrays(5, offset, row);
            offset += row;
        }
    }

    public void loadIdentityVertex(float[] vertex) {
        int meshW = this.mMeshWidth;
        int meshH = this.mMeshHeight;
        for (int j = 0; j <= meshH; j++) {
            int meshIndex = (meshW + 1) * j * 3;
            float y = j / meshH;
            for (int i = 0; i <= meshW; i++) {
                vertex[meshIndex + 0] = i / meshW;
                vertex[meshIndex + 1] = y;
                vertex[meshIndex + 2] = 0.0f;
                meshIndex += 3;
            }
        }
    }

    public void loadIdentityTexCoord(float[] texCoord) {
        int meshW = this.mMeshWidth;
        int meshH = this.mMeshHeight;
        for (int j = 0; j <= meshH; j++) {
            int meshIndex = (meshW + 1) * j * 2;
            float y = j / meshH;
            for (int i = 0; i <= meshW; i++) {
                texCoord[meshIndex + 0] = i / meshW;
                texCoord[meshIndex + 1] = y;
                meshIndex += 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildVertexArray(int face) {
        int meshW = this.mMeshWidth;
        int meshH = this.mMeshHeight;
        int vertexW = this.mMeshWidth + 1;
        int offset = this.mFaceOffset * 3;
        float w = this.mWidth;
        float h = this.mHeight;
        float[] xyz = this.mTempFloat6;
        this.mVertexBuffer.position(face * offset);
        for (int j = 0; j < meshH; j++) {
            int index1 = (j + 1) * vertexW * 3;
            int index2 = j * vertexW * 3;
            for (int i = 0; i <= meshW; i++) {
                xyz[0] = (this.mMeshVertex[index1 + 0] - 0.5f) * w;
                xyz[1] = (this.mMeshVertex[index1 + 1] - 0.5f) * h;
                xyz[2] = this.mMeshVertex[index1 + 2];
                xyz[3] = (this.mMeshVertex[index2 + 0] - 0.5f) * w;
                xyz[4] = (this.mMeshVertex[index2 + 1] - 0.5f) * h;
                xyz[5] = this.mMeshVertex[index2 + 2];
                this.mVertexBuffer.put(xyz);
                index1 += 3;
                index2 += 3;
            }
        }
        this.mVertexBuffer.position(0);
    }

    protected void buildTexCoordArray(int face) {
        int meshW = this.mMeshWidth;
        int meshH = this.mMeshHeight;
        int vertexW = this.mMeshWidth + 1;
        int offset = this.mFaceOffset * 2;
        float w = this.mTextureWidth[face];
        float h = this.mTextureHeight[face];
        float[] st = this.mTempFloat6;
        this.mTextureBuffer.position(face * offset);
        for (int j = 0; j < meshH; j++) {
            int index1 = (j + 1) * vertexW * 2;
            int index2 = j * vertexW * 2;
            for (int i = 0; i <= meshW; i++) {
                st[0] = this.mMeshTexCoord[index1 + 0] * w;
                st[1] = (1.0f - this.mMeshTexCoord[index1 + 1]) * h;
                st[2] = this.mMeshTexCoord[index2 + 0] * w;
                st[3] = (1.0f - this.mMeshTexCoord[index2 + 1]) * h;
                if (face == 1) {
                    st[0] = w - st[0];
                    st[2] = w - st[2];
                }
                this.mTextureBuffer.put(st, 0, 4);
                index1 += 2;
                index2 += 2;
            }
        }
        this.mTextureBuffer.position(0);
    }

    /* loaded from: classes.dex */
    public static class Time {
        public static final int ELAPSE = 50;
        protected int mDuration;
        protected int mElapse;
        protected int mTickAccum;
        protected int mTicks;

        public Time() {
            start(0);
        }

        public void start(int duration) {
            start(duration, 50);
        }

        public void start(int duration, int elapse) {
            this.mTickAccum = 0;
            this.mElapse = elapse;
            this.mDuration = duration;
            this.mTicks = 0;
        }

        public boolean update(int tickPassed) {
            this.mTickAccum += tickPassed;
            if (this.mTickAccum < this.mElapse) {
                return false;
            }
            this.mTickAccum = 0;
            this.mTicks += this.mElapse;
            return true;
        }

        public boolean hasEnded() {
            return this.mTicks > this.mDuration;
        }

        public int getTicks() {
            return this.mTicks;
        }

        public int getElapse() {
            return this.mElapse;
        }

        public int getDuration() {
            return this.mDuration;
        }
    }

    public void startBlender(Blender blender) {
        this.mNewBlender = blender;
        this.mBlenderChanged = true;
        this.mWorld.requestRender();
    }

    public void stopBlender() {
        startBlender(null);
    }

    public Blender getBlender() {
        return this.mBlender;
    }

    /* loaded from: classes.dex */
    public static class IdentityEffect extends Blender {
        protected final TFCustomPanel mCustomPanel;

        public IdentityEffect(TFCustomPanel customPanel) {
            this.mCustomPanel = customPanel;
        }

        @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
        public void onStart() {
            this.mCustomPanel.getVertex();
            this.mCustomPanel.loadIdentityVertex(this.mCustomPanel.getVertex());
            this.mCustomPanel.requestUpdateVertex();
            this.mCustomPanel.loadIdentityTexCoord(this.mCustomPanel.getTexCoord());
            this.mCustomPanel.requestUpdateTexCoord();
        }

        @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
        public void onFrame(int ticks) {
        }

        @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
        public void onEnd() {
        }

        @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
        public boolean hasEnded() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static FloatBuffer newFloatBuffer(int count) {
        return ByteBuffer.allocateDirect(count * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void setOnPanelMorphListener(OnPanelMorphListener listener) {
        if (this.mMorpher == null) {
            this.mMorpher = new Morpher();
        }
        this.mOnPanelMorphListener = listener;
    }

    protected void applyMorph(int tickPassed) {
        if (this.mMorphRemainingTime > 0) {
            if (this.mMorphRemainingTime == this.mMorphInitialTime) {
                this.mMorphInitialTime = 0L;
            } else {
                if (this.mMorphRemainingTime - tickPassed < 0) {
                    tickPassed = (int) this.mMorphRemainingTime;
                }
                float ratio = tickPassed / ((float) this.mMorphRemainingTime);
                float widthDiff = (this.mMorphTargetWidth - getWidth()) * ratio;
                float heightDiff = (this.mMorphTargetHeight - getHeight()) * ratio;
                float[] newSize = {getWidth() + widthDiff, getHeight() + heightDiff};
                this.mMorphRemainingTime -= tickPassed;
                if (this.mOnPanelMorphListener != null) {
                    this.mMorpher.setParams(newSize[0], newSize[1], widthDiff, heightDiff);
                }
                this.mWorld.queueEvent(this.mMorpher);
                if (!isInEffectAnimation()) {
                    this.mEffectStatus = 2;
                }
            }
            this.mWorld.requestRender();
        }
    }

    public void morph(float width, float height, long animationTime) {
        morph(width, height, 0.0f, 0.0f, animationTime);
    }

    public void morph(float width, float height, float anchorPositionX, float anchorPositionY, long animationTime) {
        if (anchorPositionX < -0.5f || 0.5f < anchorPositionX) {
            anchorPositionX = 0.0f;
        }
        if (anchorPositionY < -0.5f || 0.5f < anchorPositionY) {
            anchorPositionY = 0.0f;
        }
        this.mMorphInitialTime = animationTime;
        this.mMorphRemainingTime = animationTime;
        this.mMorphTargetWidth = width;
        this.mMorphTargetHeight = height;
        this.mMorphAnchorX = anchorPositionX;
        this.mMorphAnchorY = anchorPositionY;
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Morpher implements Runnable {
        private float mHeightDiff;
        private float mNewHeight;
        private float mNewWidth;
        private float mWidthDiff;

        Morpher() {
        }

        public void setParams(float newWidth, float newHeight, float widthDiff, float heightDiff) {
            this.mNewWidth = newWidth;
            this.mNewHeight = newHeight;
            this.mWidthDiff = widthDiff;
            this.mHeightDiff = heightDiff;
        }

        @Override // java.lang.Runnable
        public void run() {
            TFCustomPanel.this.setSize(this.mNewWidth, this.mNewHeight, 8, 8);
            if (TFCustomPanel.this.mMorphAnchorX != 0.0f) {
                TFCustomPanel.this.locate(0, (-this.mWidthDiff) * TFCustomPanel.this.mMorphAnchorX, true);
            }
            if (TFCustomPanel.this.mMorphAnchorY != 0.0f) {
                TFCustomPanel.this.locate(1, (-this.mHeightDiff) * TFCustomPanel.this.mMorphAnchorY, true);
            }
            TFCustomPanel.this.mOnPanelMorphListener.onPanelMorph(this.mWidthDiff, this.mHeightDiff);
        }
    }
}
