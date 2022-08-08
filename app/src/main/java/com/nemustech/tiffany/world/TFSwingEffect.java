package com.nemustech.tiffany.world;

import android.util.Log;
import android.view.animation.Interpolator;
import com.nemustech.tiffany.world.TFCustomPanel;

/* loaded from: classes.dex */
public class TFSwingEffect extends TFCustomPanel.Blender {
    public static final int DEFAULT_SWING_COUNT = 8;
    private static final float DEFAULT_SWING_DISTANCE = 0.0f;
    public static final int DEFAULT_SWING_POSITION = 2;
    public static final int DEFAULT_SWING_START_DIRECTION = 1;
    public static final int SWING_POSITION_ALL = 1;
    private static final int SWING_POSITION_BODY = 4;
    public static final int SWING_POSITION_HEAD = 2;
    public static final int SWING_POSITION_TAIL = 3;
    public static final int SWING_START_DIRECTION_RIGHT = 1;
    public static final int SWINT_START_DIRECTION_LEFT = -1;
    private static final String TAG = "TFSwingEffect";
    private final TFCustomPanel mCustomPanel;
    protected int mDuration;
    private int mElapseWhenStopped;
    protected final float[] mIdentity;
    private final int mMeshH;
    private final int mMeshW;
    private float[] mSwingDistanceArr;
    protected int mSwingDuration;
    protected Interpolator mSwingInterpolator;
    private float[] mSwingRatio;
    private int mSwingRatioIdx;
    private float[] mVertex;
    private TFCustomPanel.Time mTime = new TFCustomPanel.Time();
    private int mSwingPosition = 2;
    private int mSwingCount = 8;
    private int mSwingStartDir = 1;
    private float mSwingDistance = DEFAULT_SWING_DISTANCE;
    private int mEffectStepCount = 0;
    private int mEffectStopCount = -1;
    private boolean mIsRunnig = true;

    public TFSwingEffect(TFCustomPanel customPanel) {
        this.mCustomPanel = customPanel;
        this.mMeshW = this.mCustomPanel.getMeshWidth();
        this.mMeshH = this.mCustomPanel.getMeshHeight();
        this.mSwingDistanceArr = new float[this.mMeshH + 1];
        this.mIdentity = new float[(this.mMeshH + 1) * (this.mMeshW + 1) * 3];
        this.mCustomPanel.loadIdentityVertex(this.mIdentity);
        this.mVertex = this.mCustomPanel.getVertex();
    }

    public void setSwingDuration(int duration, int swingDuration) {
        if (swingDuration <= 0 || swingDuration >= duration) {
            swingDuration = (duration * 3) / 4;
        }
        this.mDuration = duration;
        this.mSwingDuration = swingDuration;
    }

    public void setEffectStepCount(int count) {
        this.mEffectStepCount = count;
    }

    public void setEffectStopCount(int count) {
        this.mEffectStopCount = count;
    }

    public void setSwingCount(int swingCount) {
        this.mSwingCount = swingCount;
    }

    public void setSwingStartDir(int swingStartDir) {
        this.mSwingStartDir = swingStartDir;
    }

    public TFCustomPanel getPanel() {
        return this.mCustomPanel;
    }

    public void resetEffect() {
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            for (int j = 0; j <= this.mMeshW; j++) {
                this.mVertex[rowIndex] = this.mIdentity[rowIndex];
                rowIndex += 3;
            }
        }
        if (this.mCustomPanel.getWorld() != null) {
            this.mCustomPanel.requestUpdateVertex();
        }
    }

    public void setSwingDistance(float swingDistance) {
        this.mSwingDistance = swingDistance;
    }

    public void setSwingPosition(int pos) {
        this.mSwingPosition = pos;
    }

    private void initSwingXAxisDistance() {
        switch (this.mSwingPosition) {
            case 1:
                initAllSwingXAxisDistance();
                return;
            case 2:
                initHeadSwingXAxisDistance();
                return;
            case 3:
                initTailSwingXAxisDistance();
                return;
            case 4:
                initBodySwingXAxisDistance();
                return;
            default:
                return;
        }
    }

    private void initHeadSwingXAxisDistance() {
        int rowIndex = 0;
        for (int i = this.mMeshW; i >= 0; i--) {
            this.mSwingDistanceArr[i] = this.mVertex[rowIndex] * this.mVertex[rowIndex];
            rowIndex += 3;
        }
    }

    private void initTailSwingXAxisDistance() {
        int rowIndex = 0;
        for (int i = 0; i <= this.mMeshW; i++) {
            this.mSwingDistanceArr[i] = this.mVertex[rowIndex] * this.mVertex[rowIndex];
            rowIndex += 3;
        }
    }

    private void initBodySwingXAxisDistance() {
        this.mSwingDistanceArr[0] = 0.0f;
        int rowIndex = 0;
        for (int i = 1; i < this.mMeshW; i++) {
            this.mSwingDistanceArr[i] = (float) Math.sqrt(1.0f - this.mVertex[rowIndex]);
            rowIndex += 3;
        }
        this.mSwingDistanceArr[this.mMeshW] = 0.0f;
    }

    private void initAllSwingXAxisDistance() {
        for (int i = 0; i <= this.mMeshH; i++) {
            this.mSwingDistanceArr[i] = this.mVertex[(this.mMeshW * 3) / 2];
        }
    }

    private void initSwingDistance() {
        switch (this.mSwingPosition) {
            case 1:
                initAllSwingDistance();
                return;
            case 2:
                initHeadSwingDistance();
                return;
            case 3:
                initTailSwingDistance();
                return;
            case 4:
                initBodySwingDistance();
                return;
            default:
                return;
        }
    }

    private void initAllSwingDistance() {
        float meshWidth = this.mSwingDistance / this.mMeshW;
        for (int i = 0; i <= this.mMeshH; i++) {
            this.mSwingDistanceArr[i] = (this.mMeshW / 2) * meshWidth;
        }
    }

    private void initHeadSwingDistance() {
        float meshWidth = this.mSwingDistance / this.mMeshW;
        int j = 0;
        for (int i = this.mMeshW; i >= 0; i--) {
            this.mSwingDistanceArr[i] = j * meshWidth * j * meshWidth;
            j++;
        }
    }

    private void initTailSwingDistance() {
        float meshWidth = this.mSwingDistance / this.mMeshW;
        int j = 0;
        for (int i = 0; i <= this.mMeshW; i++) {
            this.mSwingDistanceArr[i] = j * meshWidth * j * meshWidth;
            j++;
        }
    }

    private void initBodySwingDistance() {
        float meshWidth = this.mSwingDistance / this.mMeshW;
        this.mSwingDistanceArr[0] = 0.0f;
        for (int i = 1; i < this.mMeshW; i++) {
            this.mSwingDistanceArr[i] = (float) Math.sqrt(1.0f - (i * meshWidth));
        }
        this.mSwingDistanceArr[this.mMeshW] = 0.0f;
    }

    private void initSwingRatio() {
        if (this.mEffectStepCount > 0) {
            this.mSwingRatio = new float[this.mSwingCount * (this.mEffectStepCount + 1) * 4];
            float stepUnit = this.mSwingStartDir / (this.mEffectStepCount + 1);
            this.mSwingRatio[0] = stepUnit;
            for (int i = 1; i < this.mSwingCount * (this.mEffectStepCount + 1) * 4; i++) {
                if (i % ((this.mEffectStepCount + 1) * 4) == 0) {
                    stepUnit /= 2.0f;
                }
                if (i % (this.mEffectStepCount + 1) == 0 && i % ((this.mEffectStepCount + 1) * 2) != 0) {
                    stepUnit *= -1.0f;
                }
                this.mSwingRatio[i] = this.mSwingRatio[i - 1] + stepUnit;
            }
        } else {
            this.mSwingRatio = new float[(this.mSwingCount * 2) + 1];
            this.mSwingRatio[0] = this.mSwingStartDir;
            for (int i2 = 1; i2 < this.mSwingCount * 2; i2++) {
                if (i2 % 2 == 0) {
                    this.mSwingRatio[i2] = (this.mSwingRatio[i2 - 1] / 2.0f) * (-1.0f);
                } else {
                    this.mSwingRatio[i2] = this.mSwingRatio[i2 - 1] * (-1.0f);
                }
            }
        }
        this.mSwingRatio[this.mSwingRatio.length - 1] = 0.0f;
    }

    private void setSwingInterpolator(Interpolator interpolator) {
        if (interpolator != null) {
            this.mSwingInterpolator = interpolator;
        }
    }

    private void dumpSwingFactor() {
        Log.d(TAG, "[DUMP OF THE SWING RATIO]--------------------------------------------");
        for (int i = 0; i < this.mSwingRatio.length; i++) {
            Log.d(TAG, "[" + i + "]=" + this.mSwingRatio[i]);
        }
    }

    private void dumpSwingDistance() {
        Log.d(TAG, "[DUMP OF THE SWING DISTANCE]--------------------------------------------");
        for (int i = 0; i < this.mSwingDistanceArr.length; i++) {
            Log.d(TAG, "[" + i + "]=" + this.mSwingDistanceArr[i]);
        }
    }

    private void dumpVertex() {
        Log.d(TAG, "[DUMP OF THE VERTEX]--------------------------------------------");
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            Log.d(TAG, "[i=" + i + "]---");
            for (int j = 0; j <= this.mMeshW; j++) {
                int x = rowIndex;
                int y = rowIndex + 1;
                int i2 = rowIndex + 2;
                Log.d(TAG, "mVertex: [" + i + ", " + j + "] => (" + x + "," + y + ")=(" + this.mVertex[x] + ", " + this.mVertex[y] + ")");
                rowIndex += 3;
            }
        }
    }

    public void resume() {
        this.mTime = null;
        this.mTime = new TFCustomPanel.Time();
        this.mTime.start(this.mDuration - this.mElapseWhenStopped, this.mSwingDuration);
        this.mIsRunnig = true;
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onStart() {
        this.mTime.start(this.mDuration, this.mSwingDuration);
        this.mSwingRatioIdx = 0;
        if (this.mSwingDistance == DEFAULT_SWING_DISTANCE) {
            initSwingXAxisDistance();
        } else {
            initSwingDistance();
        }
        initSwingRatio();
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public boolean hasEnded() {
        if (this.mEffectStopCount > 0 && this.mSwingRatioIdx >= this.mEffectStopCount) {
            this.mEffectStopCount = this.mSwingRatio.length;
            this.mIsRunnig = false;
            this.mElapseWhenStopped = this.mTime.getElapse();
        }
        return this.mSwingRatio.length <= this.mSwingRatioIdx;
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onEnd() {
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onFrame(int tick) {
        if (this.mTime == null || (this.mTime.update(tick) && this.mIsRunnig)) {
            blend();
            this.mSwingRatioIdx++;
            this.mCustomPanel.requestUpdateVertex();
        }
    }

    private void blend() {
        updateObject();
    }

    private void updateObject() {
        for (int i = 0; i <= this.mMeshH; i++) {
            int rowIndex = (this.mMeshW + 1) * i * 3;
            float swingLength = this.mSwingDistanceArr[this.mMeshH - i];
            for (int j = 0; j <= this.mMeshW; j++) {
                if (this.mSwingRatioIdx == this.mSwingRatio.length - 1) {
                    this.mVertex[rowIndex] = this.mIdentity[rowIndex];
                } else {
                    this.mVertex[rowIndex] = this.mIdentity[rowIndex] + (this.mSwingRatio[this.mSwingRatioIdx] * swingLength);
                }
                rowIndex += 3;
            }
        }
    }
}
