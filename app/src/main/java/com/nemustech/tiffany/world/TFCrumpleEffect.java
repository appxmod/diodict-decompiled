package com.nemustech.tiffany.world;

import android.util.Log;
import com.nemustech.tiffany.world.TFCustomPanel;
import java.util.Random;
import java.util.Stack;

/* loaded from: classes.dex */
public class TFCrumpleEffect extends TFCustomPanel.Blender {
    public static final int CRUMPLE_SHAPE_CIRCLE = 2;
    public static final int CRUMPLE_SHAPE_RECT = 1;
    public static final int DEFAULT_CRUMPLE_DURATION = 30;
    public static final float DEFAULT_CRUMPLE_SIZE = 0.0625f;
    public static final float DEFAULT_CRUMPLE_SIZE_PER_STEP = 0.0625f;
    public static final int DEFAULT_DURATION = 1500;
    public static final int EFFECT_MODE_CRUMPLE = 1;
    public static final int EFFECT_MODE_WRINKLE = 2;
    private static final String TAG = "TFCrumpleEffect";
    private final TFCustomPanel mCustomPanel;
    private float mEffectEndPosX;
    private float mEffectEndPosY;
    protected final float[] mIdentity;
    private int mMaxCrumpleVertexCount;
    private float mMaxVertexX;
    private float mMaxVertexY;
    private final int mMeshH;
    private final int mMeshW;
    private float mMinVertexX;
    private float mMinVertexY;
    private float mTouchPosX;
    private float mTouchPosY;
    private float[] mVertex;
    private final TFCustomPanel.Time mTime = new TFCustomPanel.Time();
    private int mCrumpleShape = 2;
    protected int mCrumpleDuration = 30;
    protected int mDuration = 1500;
    private float mCrumpleSize = 0.0625f;
    private float mCrumpleSizePerStep = 0.0625f;
    private float mCrumplePosX = 0.5f;
    private float mCrumplePosY = 0.5f;
    private int mCrumpleVertexCount = 0;
    private boolean mRemovable = false;
    private int mCrumpleBlendCount = 0;
    private int mMaxCrumpleBlendCount = 0;
    private int mEffectMode = 1;
    private Stack<float[]> mVertexStack = null;

    public TFCrumpleEffect(TFCustomPanel customPanel) {
        this.mCustomPanel = customPanel;
        this.mMeshW = this.mCustomPanel.getMeshWidth();
        this.mMeshH = this.mCustomPanel.getMeshHeight();
        this.mIdentity = new float[(this.mMeshH + 1) * (this.mMeshW + 1) * 3];
        this.mCustomPanel.loadIdentityVertex(this.mIdentity);
        this.mVertex = this.mCustomPanel.getVertex();
        this.mMaxCrumpleVertexCount = (this.mMeshH + 1) * (this.mMeshW + 1);
    }

    public void setVertexStack(Stack<float[]> vertexStack) {
        this.mVertexStack = vertexStack;
    }

    public void setMinMaxVertex() {
        this.mMinVertexX = 1.0f;
        this.mMaxVertexX = 0.0f;
        this.mMinVertexY = 1.0f;
        this.mMaxVertexY = 0.0f;
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            for (int j = 0; j <= this.mMeshW; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                if (this.mMinVertexX > this.mVertex[x]) {
                    this.mMinVertexX = this.mVertex[x];
                }
                if (this.mMaxVertexX < this.mVertex[x]) {
                    this.mMaxVertexX = this.mVertex[x];
                }
                if (this.mMinVertexY > this.mVertex[y]) {
                    this.mMinVertexY = this.mVertex[y];
                }
                if (this.mMaxVertexY < this.mVertex[y]) {
                    this.mMaxVertexY = this.mVertex[y];
                }
                rowIndex += 3;
            }
        }
    }

    public void dumpVertex() {
        Log.d(TAG, "[DUMP OF THE VERTEX]--------------------------------------------");
        int count = 0;
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            Log.d(TAG, "[i=" + i + "]---");
            for (int j = 0; j <= this.mMeshW; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                count++;
                Log.d(TAG, "mVertex(" + count + "): [" + j + ", " + i + "] => (" + x + "," + y + ")=(" + this.mVertex[x] + ", " + this.mVertex[y] + ")");
                rowIndex += 3;
            }
        }
    }

    public void dumpVertex2() {
        Log.d(TAG, "[DUMP OF THE VERTEX]--------------------------------------------");
        int count = 0;
        for (int i = 0; i <= this.mMeshW; i++) {
            int rowIndex = i * 3;
            Log.d(TAG, "[i=" + i + "]---");
            for (int j = 0; j <= this.mMeshH; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                count++;
                Log.d(TAG, "mVertex(" + count + "): [" + i + ", " + j + "] => (" + x + "," + y + ")=(" + this.mVertex[x] + ", " + this.mVertex[y] + ")");
                rowIndex += (this.mMeshW + 1) * 3;
            }
        }
    }

    public void dumpIdentityVertex() {
        Log.d(TAG, "[DUMP OF THE VERTEX]--------------------------------------------");
        int count = 0;
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            Log.d(TAG, "[i=" + i + "]---");
            for (int j = 0; j <= this.mMeshW; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                count++;
                Log.d(TAG, "mVertex(" + count + "): [" + j + ", " + i + "] => (" + x + "," + y + ")=(" + this.mIdentity[x] + ", " + this.mIdentity[y] + ")");
                rowIndex += 3;
            }
        }
    }

    public void dumpIdentityVertex2() {
        Log.d(TAG, "[DUMP OF THE VERTEX]--------------------------------------------");
        int count = 0;
        for (int i = 0; i <= this.mMeshW; i++) {
            int rowIndex = i * 3;
            Log.d(TAG, "[i=" + i + "]---");
            for (int j = 0; j <= this.mMeshH; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                count++;
                Log.d(TAG, "mIdentity(" + count + "): [" + i + ", " + j + "] => (" + x + "," + y + ")=(" + this.mIdentity[x] + ", " + this.mIdentity[y] + ")");
                rowIndex += (this.mMeshW + 1) * 3;
            }
        }
    }

    public void setEffectMode(int mode) {
        this.mEffectMode = mode;
    }

    public int getEffectMode() {
        return this.mEffectMode;
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onStart() {
        this.mTime.start(this.mDuration, this.mCrumpleDuration);
        switch (this.mEffectMode) {
            case 2:
                this.mEffectEndPosX = this.mTouchPosX;
                this.mEffectEndPosY = this.mTouchPosY;
                return;
            default:
                this.mEffectEndPosX = this.mCrumplePosX;
                this.mEffectEndPosY = this.mCrumplePosY;
                return;
        }
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public boolean hasEnded() {
        return this.mEffectMode == 2 ? this.mCrumpleBlendCount >= this.mMaxCrumpleBlendCount : this.mCrumpleVertexCount >= this.mMaxCrumpleVertexCount;
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onEnd() {
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onFrame(int tick) {
        if ((this.mEffectMode != 2 || this.mMaxCrumpleBlendCount > 0) && this.mTime.update(tick)) {
            blend();
            this.mCustomPanel.requestUpdateVertex();
        }
    }

    private void blend() {
        this.mCrumpleBlendCount++;
        updateObject();
    }

    private void updateObject() {
        this.mCrumpleVertexCount = 0;
        this.mMinVertexX = 1.0f;
        this.mMaxVertexX = 0.0f;
        this.mMinVertexY = 1.0f;
        this.mMaxVertexY = 0.0f;
        float[] pushedVertex = new float[(this.mMeshH + 1) * (this.mMeshW + 1) * 3];
        System.arraycopy(this.mVertex, 0, pushedVertex, 0, this.mVertex.length);
        if (this.mVertexStack != null) {
            this.mVertexStack.push(pushedVertex);
        }
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            for (int j = 0; j <= this.mMeshW; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                if (isInCrumpleArea(this.mVertex[x], this.mVertex[y])) {
                    if (this.mRemovable) {
                        this.mVertex[x] = this.mEffectEndPosX;
                        this.mVertex[y] = this.mEffectEndPosY;
                    }
                    this.mCrumpleVertexCount++;
                } else {
                    if (this.mVertex[x] < this.mEffectEndPosX) {
                        this.mVertex[x] = this.mVertex[x] + this.mCrumpleSizePerStep + getRandomOffset();
                    } else {
                        this.mVertex[x] = (this.mVertex[x] - this.mCrumpleSizePerStep) + getRandomOffset();
                    }
                    if (this.mVertex[y] < this.mEffectEndPosY) {
                        this.mVertex[y] = this.mVertex[y] + this.mCrumpleSizePerStep + getRandomOffset();
                    } else {
                        this.mVertex[y] = (this.mVertex[y] - this.mCrumpleSizePerStep) + getRandomOffset();
                    }
                }
                rowIndex += 3;
                if (this.mMinVertexX > this.mVertex[x]) {
                    this.mMinVertexX = this.mVertex[x];
                }
                if (this.mMaxVertexX < this.mVertex[x]) {
                    this.mMaxVertexX = this.mVertex[x];
                }
                if (this.mMinVertexY > this.mVertex[y]) {
                    this.mMinVertexY = this.mVertex[y];
                }
                if (this.mMaxVertexY < this.mVertex[y]) {
                    this.mMaxVertexY = this.mVertex[y];
                }
            }
        }
    }

    private boolean isInCrumpleArea(float vertexX, float vertexY) {
        float x = Math.abs(vertexX - this.mEffectEndPosX);
        float y = Math.abs(vertexY - this.mEffectEndPosY);
        switch (this.mCrumpleShape) {
            case 1:
                return x <= this.mCrumpleSize && y <= this.mCrumpleSize;
            default:
                float x2 = x * x;
                float y2 = y * y;
                float distance = (float) Math.sqrt(x2 + y2);
                return distance <= this.mCrumpleSize;
        }
    }

    private float getRandomOffset() {
        Random rnd = new Random();
        return rnd.nextBoolean() ? (float) (Math.random() % (this.mCrumpleSizePerStep / 3.0f)) : -((float) (Math.random() % (this.mCrumpleSizePerStep / 3.0f)));
    }

    public TFCustomPanel getPanel() {
        return this.mCustomPanel;
    }

    public void setCrumpleCoord(float crumplePosX, float crumplePosY) {
        this.mCrumplePosX = crumplePosX;
        this.mCrumplePosY = crumplePosY;
    }

    public void setTouchCoord(float touchPosX, float touchPosY) {
        this.mTouchPosX = touchPosX;
        this.mTouchPosY = touchPosY;
    }

    public void setCrumpleSize(float crumpleSize) {
        this.mCrumpleSize = crumpleSize;
    }

    public void setCrumpleSizePerStep(float crumpleSizePerStep) {
        this.mCrumpleSizePerStep = crumpleSizePerStep;
    }

    public void setCrumpleDuration(int duration, int crumpleDuration) {
        if (crumpleDuration <= 0 || crumpleDuration >= duration) {
            crumpleDuration = (duration * 3) / 4;
        }
        this.mDuration = duration;
        this.mCrumpleDuration = crumpleDuration;
    }

    public void setRemovable(boolean isRemove) {
        this.mRemovable = isRemove;
    }

    public void setCrumpleShape(int shape) {
        this.mCrumpleShape = shape;
    }

    public void setMaxCrumpleBlendCount(int count) {
        this.mMaxCrumpleBlendCount = count;
    }

    public int getMaxCrumpleBlendCount() {
        return this.mMaxCrumpleBlendCount;
    }

    public void setCrumpleCoordWithModelCoord(float xPos, float yPos) {
        float xPos2 = getCrumpleCoordXPosWithModelCoord(xPos);
        float yPos2 = getCrumpleCoordYPosWithModelCoord(yPos);
        switch (this.mEffectMode) {
            case 2:
                setTouchCoord(xPos2, yPos2);
                return;
            default:
                setCrumpleCoord(xPos2, yPos2);
                return;
        }
    }

    public float getCrumpleCoordXPosWithModelCoord(float xPos) {
        return (xPos / this.mCustomPanel.getWidth()) + 0.5f;
    }

    public float getCrumpleCoordYPosWithModelCoord(float yPos) {
        return (yPos / this.mCustomPanel.getHeight()) + 0.5f;
    }

    public Stack<float[]> getVertexStack() {
        return this.mVertexStack;
    }

    public float getMinVertexX() {
        return this.mMinVertexX;
    }

    public float getMaxVertexX() {
        return this.mMaxVertexX;
    }

    public float getMinVertexY() {
        return this.mMinVertexY;
    }

    public float getMaxVertexY() {
        return this.mMaxVertexY;
    }

    public void getPanelCoordPos(int xIndex, int yIndex, float[] xyz) {
        int vertexIndex = (xIndex * 3) + ((this.mMeshW + 1) * 3 * yIndex);
        int y = vertexIndex + 1;
        int z = vertexIndex + 2;
        xyz[0] = this.mVertex[vertexIndex];
        xyz[1] = this.mVertex[y];
        xyz[2] = this.mVertex[z];
    }

    public void resetCount() {
        this.mCrumpleVertexCount = 0;
        this.mCrumpleBlendCount = 0;
    }

    public void resetPanel() {
        System.arraycopy(this.mIdentity, 0, this.mVertex, 0, this.mIdentity.length);
        this.mCustomPanel.requestUpdateVertex();
    }
}
