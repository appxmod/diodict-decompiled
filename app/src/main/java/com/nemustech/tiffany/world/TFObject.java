package com.nemustech.tiffany.world;

import android.util.Log;
import com.nemustech.tiffany.world.TFWorld;
import java.util.Comparator;
import java.util.LinkedList;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFObject implements Cloneable {
    private static final float STOP_RESISTENCY_FACTOR = 0.8f;
    private static final String TAG = "TFObject";
    static final Comparator<TFObject> compareAxisZ = new Comparator<TFObject>() { // from class: com.nemustech.tiffany.world.TFObject.2
        @Override // java.util.Comparator
        public int compare(TFObject obj1, TFObject obj2) {
            float distance = obj1.mMatrix[14] - obj2.mMatrix[14];
            if (distance > 0.0f) {
                return 1;
            }
            if (distance == 0.0f) {
                return 0;
            }
            return -1;
        }
    };
    private static float gRotationResistency;
    private static float gRotationVelocity;
    protected boolean mActOnDrag;
    protected LinkedList<TFObject> mCloneList;
    float mDepth;
    public String mDescription;
    private TFWorld.OnEffectFinishListener mEffectFinishListener;
    int mEffectStatus;
    protected long mFadeInitialTime;
    protected long mFadeRemainingTime;
    protected float mFadeTargetOpacity;
    protected float mForceFromHead;
    protected float mForceFromSide;
    float mHeight;
    private boolean mIsCorrectAccelerator;
    protected boolean mIsMovingTargetExist;
    protected boolean mIsRotatingTargetExist;
    private TFWorld.Layer mLayer;
    protected long mMorphInitialTime;
    protected long mMorphRemainingTime;
    protected float mOpacity;
    TFObject mOriginalTwin;
    protected TFHolder mParentHolder;
    private int[] mReservedParam;
    protected float mRotationResistency;
    protected TFWorld.OnSelectListener mSelectListener;
    private float mSensitivity;
    boolean mShouldDraw;
    protected float mTranslateResistency;
    boolean mVisible;
    protected long mWaitInitialTime;
    protected long mWaitRemainingTime;
    protected float mWeight;
    float mWidth;
    protected TFWorld mWorld;
    protected TFObjectContainer mWrapper;
    final float EPSILON = 1.0E-6f;
    protected float[] mLocation = new float[3];
    protected float[] mPosMovingTarget = new float[3];
    protected float[] mMovingVelocity = new float[3];
    protected float[] mMovingAccelerator = new float[6];
    protected float[] mMovingMiddlePoint = new float[3];
    protected float[] mAngle = new float[3];
    protected float[] mAngleRotateAmount = new float[3];
    protected float[] mAngleRotateTarget = new float[3];
    protected float[] mRotationVelocity = new float[3];
    protected float[] mRotationAccelerator = new float[3];
    protected int[] mRotateDirection = new int[3];
    boolean mCloneObject = false;
    private boolean mIsLocked = false;
    protected boolean mIsIgnoringResistency = false;
    boolean mShouldRotateFirst = false;
    boolean mRotateYFirst = false;
    float[] mMatrix = new float[16];
    protected float mIntendedOpacity = 1.0f;
    protected float mExternalFadingFactor = 1.0f;
    private float mEndingSpeed = 0.0f;
    private float mMovingDuration = 0.0f;
    int mItemIndex = -1;

    public TFObject() {
        this.mIsCorrectAccelerator = false;
        setSensitivity(1.0f);
        setWeight(1.0f);
        setActOnDrag(true);
        this.mIsCorrectAccelerator = false;
    }

    public Object clone() throws CloneNotSupportedException {
        TFObject clone = (TFObject) super.clone();
        clone.mLocation = (float[]) this.mLocation.clone();
        clone.mPosMovingTarget = new float[3];
        clone.mMovingVelocity = new float[3];
        clone.mMovingAccelerator = new float[6];
        clone.mMovingMiddlePoint = new float[3];
        clone.mAngle = (float[]) this.mAngle.clone();
        clone.mAngleRotateTarget = new float[3];
        clone.mRotationVelocity = new float[3];
        clone.mRotationAccelerator = new float[3];
        clone.mRotateDirection = new int[3];
        clone.mMatrix = new float[16];
        clone.mOriginalTwin = this;
        clone.mCloneObject = true;
        clone.mCloneList = null;
        if (this.mCloneList == null) {
            this.mCloneList = new LinkedList<>();
        }
        this.mCloneList.add(clone);
        return clone;
    }

    public TFWorld getWorld() {
        return this.mWorld;
    }

    public boolean isCloneObject() {
        return this.mCloneObject;
    }

    public void setActOnDrag(boolean mode) {
        this.mActOnDrag = mode;
    }

    public void locate(float locationX, float locationY, float locationZ) {
        boolean bRequest = false;
        if (this.mLocation[0] != locationX || this.mLocation[1] != locationY || this.mLocation[2] != locationZ) {
            bRequest = true;
        }
        this.mLocation[0] = locationX;
        this.mLocation[1] = locationY;
        this.mLocation[2] = locationZ;
        if (!bRequest || this.mWorld == null) {
            return;
        }
        this.mWorld.requestRender();
    }

    public void locate(int axis, float location, boolean bRelative) {
        boolean bRequest = false;
        if (bRelative) {
            float[] fArr = this.mLocation;
            fArr[axis] = fArr[axis] + location;
            if (location != 0.0f) {
                bRequest = true;
            }
        } else {
            if (this.mLocation[axis] != location) {
                bRequest = true;
            }
            this.mLocation[axis] = location;
        }
        if (!bRequest || this.mWorld == null) {
            return;
        }
        this.mWorld.requestRender();
    }

    public void freeze(int velocityMask) {
        this.mWaitInitialTime = 0L;
        this.mWaitRemainingTime = 0L;
        for (int i = 0; i < 3; i++) {
            if ((velocityMask & 1) != 0) {
                this.mMovingVelocity[i] = 0.0f;
                this.mMovingAccelerator[i] = 0.0f;
                this.mMovingAccelerator[i + 3] = 0.0f;
                this.mPosMovingTarget[i] = 0.0f;
                this.mIsMovingTargetExist = false;
            }
            if ((velocityMask & 2) != 0) {
                this.mRotationVelocity[i] = 0.0f;
                this.mAngleRotateTarget[i] = 0.0f;
                this.mIsRotatingTargetExist = false;
            }
        }
    }

    public void move(int axis, float newLocation, float speed, boolean bRelative) {
        float[] targetLocation = new float[3];
        for (int i = 0; i < 3; i++) {
            if (i == axis) {
                if (bRelative) {
                    targetLocation[i] = this.mLocation[i] + newLocation;
                } else {
                    targetLocation[i] = newLocation;
                }
            } else {
                targetLocation[i] = this.mLocation[i];
            }
        }
        move(targetLocation[0], targetLocation[1], targetLocation[2], speed);
    }

    public void move(float locationX, float locationY, float locationZ, float speed) {
        move(locationX, locationY, locationZ, speed, speed, 0.5f, speed);
    }

    public void move(float locationX, float locationY, float locationZ, float startingSpeed, float middleSpeed, float inflectionPoint, float endingSpeed) {
        this.mPosMovingTarget[0] = locationX;
        this.mPosMovingTarget[1] = locationY;
        this.mPosMovingTarget[2] = locationZ;
        float[] deviation = new float[3];
        for (int i = 0; i < 3; i++) {
            deviation[i] = Math.abs(this.mPosMovingTarget[i] - this.mLocation[i]);
        }
        float wholeDistance = (float) Math.sqrt((deviation[0] * deviation[0]) + (deviation[1] * deviation[1]) + (deviation[2] * deviation[2]));
        float vs = Math.abs(startingSpeed);
        float vm = Math.abs(middleSpeed);
        float ve = Math.abs(endingSpeed);
        float ip = inflectionPoint;
        float[] fArr = new float[3];
        if (inflectionPoint == 0.0f || inflectionPoint == 1.0f) {
            if (inflectionPoint == 0.0f) {
                vs = middleSpeed;
                vm = endingSpeed;
            } else {
                vm = startingSpeed;
                ve = middleSpeed;
            }
            ip = 0.5f;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            this.mMovingVelocity[i2] = (deviation[i2] * startingSpeed) / wholeDistance;
            this.mMovingMiddlePoint[i2] = ((this.mPosMovingTarget[i2] - this.mLocation[i2]) * ip) + this.mLocation[i2];
        }
        float[] mp = this.mMovingMiddlePoint;
        if (inflectionPoint == 0.0f || inflectionPoint == 1.0f) {
            if (inflectionPoint == 0.0f) {
                mp = this.mPosMovingTarget;
            } else {
                mp = this.mLocation;
            }
        }
        float[] dist = new float[6];
        for (int i3 = 0; i3 < 3; i3++) {
            dist[i3] = mp[i3] - this.mLocation[i3];
            dist[i3 + 3] = this.mPosMovingTarget[i3] - mp[i3];
        }
        float d1 = (float) Math.sqrt((dist[0] * dist[0]) + (dist[1] * dist[1]) + (dist[2] * dist[2]));
        float d2 = (float) Math.sqrt((dist[3] * dist[3]) + (dist[4] * dist[4]) + (dist[5] * dist[5]));
        float t1 = Math.abs((2.0f * d1) / (vs + vm));
        float t2 = Math.abs((2.0f * d2) / (vm + ve));
        for (int i4 = 0; i4 < deviation.length; i4++) {
            this.mMovingAccelerator[i4] = (((vm - vs) / t1) * deviation[i4]) / wholeDistance;
            this.mMovingAccelerator[i4 + 3] = (((ve - vm) / t2) * deviation[i4]) / wholeDistance;
        }
        if (inflectionPoint == 0.0f || inflectionPoint == 1.0f) {
            if (inflectionPoint == 0.0f) {
                this.mMovingAccelerator[0] = this.mMovingAccelerator[3];
                this.mMovingAccelerator[1] = this.mMovingAccelerator[4];
                this.mMovingAccelerator[2] = this.mMovingAccelerator[5];
            } else {
                this.mMovingAccelerator[3] = this.mMovingAccelerator[0];
                this.mMovingAccelerator[4] = this.mMovingAccelerator[1];
                this.mMovingAccelerator[5] = this.mMovingAccelerator[2];
            }
        }
        this.mIsCorrectAccelerator = false;
        this.mEndingSpeed = endingSpeed;
        this.mMovingDuration = t2;
        this.mIsMovingTargetExist = true;
        this.mIsIgnoringResistency = true;
        this.mEffectStatus = 1;
        this.mWorld.requestRender();
    }

    public void move(float locationX, float locationY, float locationZ, long duration) {
        float[] dist = {Math.abs(locationX - this.mLocation[0]), Math.abs(locationY - this.mLocation[1]), Math.abs(locationZ - this.mLocation[2])};
        float distance = (float) Math.sqrt((dist[0] * dist[0]) + (dist[1] * dist[1]) + (dist[2] * dist[2]));
        float speed = distance / ((float) duration);
        move(locationX, locationY, locationZ, speed, speed, 0.5f, speed);
    }

    protected void onFinishMove() {
        freeze(1);
        if (!isInEffectAnimation()) {
            this.mEffectStatus = 2;
        }
    }

    public void shoot(float forceX, float forceY, float forceZ, boolean bIgnoreResistency) {
        float k = this.mWeight * 800.0f;
        float[] fArr = this.mMovingVelocity;
        fArr[0] = fArr[0] + (forceX / k);
        float[] fArr2 = this.mMovingVelocity;
        fArr2[1] = fArr2[1] + (forceY / k);
        float[] fArr3 = this.mMovingVelocity;
        fArr3[2] = fArr3[2] + (forceZ / k);
        this.mIsMovingTargetExist = false;
        this.mIsIgnoringResistency = bIgnoreResistency;
        this.mWorld.requestRender();
    }

    public void spin(float forceX, float forceY, float forceZ, boolean bIgnoreResistency) {
        float k = this.mWeight * 100.0f;
        float[] fArr = this.mRotationVelocity;
        fArr[0] = fArr[0] + (forceX / k);
        float[] fArr2 = this.mRotationVelocity;
        fArr2[1] = fArr2[1] + (forceY / k);
        float[] fArr3 = this.mRotationVelocity;
        fArr3[2] = fArr3[2] + (forceZ / k);
        this.mIsMovingTargetExist = false;
        this.mIsIgnoringResistency = bIgnoreResistency;
        this.mWorld.requestRender();
    }

    public boolean isDrawable() {
        return this.mShouldDraw;
    }

    protected void onFinishEffect() {
        if (this.mEffectFinishListener != null) {
            this.mEffectFinishListener.onEffectFinish(this);
        } else if (this.mWorld != null && this.mWorld.mEffectFinishListener != null) {
            this.mWorld.mEffectFinishListener.onEffectFinish(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isInEffectAnimation() {
        return this.mIsMovingTargetExist || this.mIsRotatingTargetExist || this.mFadeRemainingTime > 0 || this.mMorphRemainingTime > 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkEffectFinish() {
        if (this.mEffectStatus == 2) {
            this.mEffectStatus = 0;
            this.mWorld.queueEvent(new Runnable() { // from class: com.nemustech.tiffany.world.TFObject.1
                @Override // java.lang.Runnable
                public void run() {
                    TFObject.this.onFinishEffect();
                }
            });
        }
    }

    private float calcRotationVelocity(int tickPassed, int axis) {
        return tickPassed * this.mRotationVelocity[axis];
    }

    protected void prvRotate(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        if (this.mWaitRemainingTime <= 0) {
            for (int i = 0; i < 3; i++) {
                if (this.mRotationVelocity[i] != 0.0f) {
                    this.mWorld.requestRender();
                    gRotationVelocity = calcRotationVelocity(tickPassed, i);
                    if (this.mIsRotatingTargetExist) {
                        switch (this.mRotateDirection[i]) {
                            case 0:
                                if (this.mAngle[i] > this.mAngleRotateTarget[i]) {
                                    float[] fArr = this.mAngle;
                                    fArr[i] = fArr[i] + gRotationVelocity;
                                    if (this.mAngle[i] >= 360.0f) {
                                        float[] fArr2 = this.mAngle;
                                        fArr2[i] = fArr2[i] - 360.0f;
                                        if (this.mAngle[i] >= this.mAngleRotateTarget[i]) {
                                            this.mAngle[i] = this.mAngleRotateTarget[i];
                                            this.mRotationVelocity[i] = 0.0f;
                                            break;
                                        }
                                    }
                                } else if (this.mAngle[i] < this.mAngleRotateTarget[i]) {
                                    float[] fArr3 = this.mAngle;
                                    fArr3[i] = fArr3[i] + gRotationVelocity;
                                    if (this.mAngleRotateTarget[i] >= 360.0f && this.mAngle[i] >= 360.0f) {
                                        if (Math.abs(this.mAngleRotateTarget[i]) - 360.0f < 1.0E-4f) {
                                            float[] fArr4 = this.mAngle;
                                            this.mAngleRotateTarget[i] = 0.0f;
                                            fArr4[i] = 0.0f;
                                            this.mRotationVelocity[i] = 0.0f;
                                        } else {
                                            float[] fArr5 = this.mAngle;
                                            fArr5[i] = fArr5[i] - 360.0f;
                                            float[] fArr6 = this.mAngleRotateTarget;
                                            fArr6[i] = fArr6[i] - 360.0f;
                                        }
                                    }
                                    if (this.mAngle[i] >= this.mAngleRotateTarget[i]) {
                                        this.mAngle[i] = this.mAngleRotateTarget[i];
                                        this.mRotationVelocity[i] = 0.0f;
                                        break;
                                    }
                                }
                                break;
                            case 1:
                                if (this.mAngle[i] > this.mAngleRotateTarget[i]) {
                                    float[] fArr7 = this.mAngle;
                                    fArr7[i] = fArr7[i] + gRotationVelocity;
                                    if (this.mAngle[i] < 0.0f) {
                                        if (Math.abs(this.mAngleRotateTarget[i]) < 1.0E-4f) {
                                            this.mAngle[i] = this.mAngleRotateTarget[i];
                                            this.mRotationVelocity[i] = 0.0f;
                                        } else {
                                            float[] fArr8 = this.mAngle;
                                            fArr8[i] = fArr8[i] + 360.0f;
                                            float[] fArr9 = this.mAngleRotateTarget;
                                            fArr9[i] = fArr9[i] + 360.0f;
                                        }
                                    }
                                    if (this.mAngle[i] <= this.mAngleRotateTarget[i]) {
                                        this.mAngle[i] = this.mAngleRotateTarget[i];
                                        this.mRotationVelocity[i] = 0.0f;
                                        break;
                                    }
                                } else if (this.mAngle[i] < this.mAngleRotateTarget[i]) {
                                    float[] fArr10 = this.mAngle;
                                    fArr10[i] = fArr10[i] + gRotationVelocity;
                                    if (this.mAngle[i] < 0.0f) {
                                        float[] fArr11 = this.mAngle;
                                        fArr11[i] = fArr11[i] + 360.0f;
                                        if (this.mAngle[i] <= this.mAngleRotateTarget[i]) {
                                            this.mAngle[i] = this.mAngleRotateTarget[i];
                                            this.mRotationVelocity[i] = 0.0f;
                                            break;
                                        }
                                    }
                                }
                                break;
                            case 2:
                                if (this.mAngle[i] > this.mAngleRotateTarget[i]) {
                                    if (this.mAngle[i] - this.mAngleRotateTarget[i] > 180.0f) {
                                        float[] fArr12 = this.mAngle;
                                        fArr12[i] = fArr12[i] + gRotationVelocity;
                                        if (this.mAngle[i] >= 360.0f) {
                                            float[] fArr13 = this.mAngle;
                                            fArr13[i] = fArr13[i] - 360.0f;
                                            if (this.mAngle[i] >= this.mAngleRotateTarget[i]) {
                                                this.mAngle[i] = this.mAngleRotateTarget[i];
                                                this.mRotationVelocity[i] = 0.0f;
                                                break;
                                            }
                                        }
                                    } else {
                                        float[] fArr14 = this.mAngle;
                                        fArr14[i] = fArr14[i] - gRotationVelocity;
                                        if (this.mAngle[i] <= this.mAngleRotateTarget[i]) {
                                            this.mAngle[i] = this.mAngleRotateTarget[i];
                                            this.mRotationVelocity[i] = 0.0f;
                                            break;
                                        }
                                    }
                                } else if (this.mAngle[i] < this.mAngleRotateTarget[i]) {
                                    if (this.mAngleRotateTarget[i] - this.mAngle[i] > 180.0f) {
                                        float[] fArr15 = this.mAngle;
                                        fArr15[i] = fArr15[i] - gRotationVelocity;
                                        if (this.mAngle[i] < 0.0f) {
                                            float[] fArr16 = this.mAngle;
                                            fArr16[i] = fArr16[i] + 360.0f;
                                            if (this.mAngle[i] <= this.mAngleRotateTarget[i]) {
                                                this.mAngle[i] = this.mAngleRotateTarget[i];
                                                this.mRotationVelocity[i] = 0.0f;
                                                break;
                                            }
                                        }
                                    } else {
                                        float[] fArr17 = this.mAngle;
                                        fArr17[i] = fArr17[i] + gRotationVelocity;
                                        if (this.mAngle[i] >= this.mAngleRotateTarget[i]) {
                                            this.mAngle[i] = this.mAngleRotateTarget[i];
                                            this.mRotationVelocity[i] = 0.0f;
                                            break;
                                        }
                                    }
                                }
                                break;
                        }
                    } else {
                        float[] fArr18 = this.mAngle;
                        fArr18[i] = fArr18[i] + gRotationVelocity;
                    }
                    if (!this.mIsIgnoringResistency) {
                        if (this.mWorld.mTouchDown) {
                            gRotationResistency = Math.abs(this.mRotationVelocity[i]) * STOP_RESISTENCY_FACTOR;
                        } else {
                            gRotationResistency = this.mRotationResistency;
                        }
                        if (this.mRotationVelocity[i] > 0.0f) {
                            float[] fArr19 = this.mRotationVelocity;
                            fArr19[i] = fArr19[i] - gRotationResistency;
                            if (this.mRotationVelocity[i] < 0.0f) {
                                this.mRotationVelocity[i] = 0.0f;
                            }
                        } else if (this.mRotationVelocity[i] < 0.0f) {
                            float[] fArr20 = this.mRotationVelocity;
                            fArr20[i] = fArr20[i] + gRotationResistency;
                            if (this.mRotationVelocity[i] > 0.0f) {
                                this.mRotationVelocity[i] = 0.0f;
                            }
                        }
                    }
                    this.mAngle[i] = TFUtils.filterAngle(this.mAngle[i]);
                }
            }
            if (this.mIsRotatingTargetExist && this.mAngle[0] == this.mAngleRotateTarget[0] && this.mAngle[1] == this.mAngleRotateTarget[1] && this.mAngle[2] == this.mAngleRotateTarget[2]) {
                onFinishRotate();
            }
        } else {
            this.mWorld.requestRender();
        }
        if (bDoGLCalc) {
            if (this.mRotateYFirst) {
                gl.glRotatef(this.mAngle[1], 1.0f, 0.0f, 0.0f);
                gl.glRotatef(this.mAngle[0], 0.0f, 1.0f, 0.0f);
            } else {
                gl.glRotatef(this.mAngle[0], 0.0f, 1.0f, 0.0f);
                gl.glRotatef(this.mAngle[1], 1.0f, 0.0f, 0.0f);
            }
            gl.glRotatef(this.mAngle[2], 0.0f, 0.0f, 1.0f);
        }
    }

    protected void prvTranslate(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        float translateResistency;
        if (this.mWaitRemainingTime <= 0) {
            for (int i = 0; i < 3; i++) {
                if (this.mMovingVelocity[i] != 0.0f || this.mMovingAccelerator[i] != 0.0f) {
                    this.mWorld.requestRender();
                    float distance = tickPassed * this.mMovingVelocity[i];
                    if (this.mIsMovingTargetExist) {
                        if (this.mMovingAccelerator[i] != 0.0f || this.mMovingAccelerator[i + 3] != 0.0f) {
                            if ((this.mLocation[i] > this.mPosMovingTarget[i] && this.mLocation[i] > this.mMovingMiddlePoint[i]) || (this.mLocation[i] < this.mPosMovingTarget[i] && this.mLocation[i] < this.mMovingMiddlePoint[i])) {
                                float[] fArr = this.mMovingVelocity;
                                fArr[i] = fArr[i] + this.mMovingAccelerator[i];
                            } else {
                                if (!this.mIsCorrectAccelerator) {
                                    float realMiddleSpeed = (float) Math.sqrt((this.mMovingVelocity[0] * this.mMovingVelocity[0]) + (this.mMovingVelocity[1] * this.mMovingVelocity[1]) + (this.mMovingVelocity[2] * this.mMovingVelocity[2]));
                                    float[] deviation = new float[3];
                                    for (int j = 0; j < 3; j++) {
                                        deviation[j] = Math.abs(this.mPosMovingTarget[j] - this.mLocation[j]);
                                    }
                                    float wholeDistance = Math.abs((deviation[0] * deviation[0]) + (deviation[1] * deviation[1]) + (deviation[2] * deviation[2]));
                                    for (int j2 = 0; j2 < 3; j2++) {
                                        this.mMovingAccelerator[j2 + 3] = ((this.mEndingSpeed - realMiddleSpeed) / this.mMovingDuration) * (deviation[j2] / wholeDistance);
                                        this.mMovingMiddlePoint[j2] = this.mLocation[j2];
                                    }
                                    this.mIsCorrectAccelerator = true;
                                }
                                float[] fArr2 = this.mMovingVelocity;
                                fArr2[i] = fArr2[i] + this.mMovingAccelerator[i + 3];
                            }
                            if (this.mMovingVelocity[i] <= 0.0f) {
                                Log.w(TAG, "Alas!! You hit the road. Please report this to joshua@nemustech.com");
                                float[] fArr3 = this.mMovingVelocity;
                                fArr3[i] = fArr3[i] - this.mMovingAccelerator[i + 3];
                            }
                        }
                        if (this.mLocation[i] > this.mPosMovingTarget[i]) {
                            float[] fArr4 = this.mLocation;
                            fArr4[i] = fArr4[i] - distance;
                            if (this.mLocation[i] < this.mPosMovingTarget[i] || Math.abs(this.mLocation[i] - this.mPosMovingTarget[i]) < 1.0E-6f) {
                                this.mLocation[i] = this.mPosMovingTarget[i];
                            }
                        }
                        if (this.mLocation[i] < this.mPosMovingTarget[i]) {
                            float[] fArr5 = this.mLocation;
                            fArr5[i] = fArr5[i] + distance;
                            if (this.mLocation[i] > this.mPosMovingTarget[i]) {
                                this.mLocation[i] = this.mPosMovingTarget[i];
                            }
                        }
                        float gap = Math.abs(this.mPosMovingTarget[i] - this.mLocation[i]);
                        if (gap < 1.0E-4f) {
                            this.mLocation[i] = this.mPosMovingTarget[i];
                        }
                    } else {
                        Log.d(TAG, "no target");
                        float[] fArr6 = this.mLocation;
                        fArr6[i] = fArr6[i] + distance;
                    }
                    if (!this.mIsIgnoringResistency) {
                        if (this.mWorld.mTouchDown) {
                            translateResistency = Math.abs(this.mMovingVelocity[i]) * STOP_RESISTENCY_FACTOR;
                        } else {
                            translateResistency = this.mTranslateResistency;
                        }
                        if (this.mMovingVelocity[i] > 0.0f) {
                            float[] fArr7 = this.mMovingVelocity;
                            fArr7[i] = fArr7[i] - translateResistency;
                            if (this.mMovingVelocity[i] < 0.0f) {
                                this.mMovingVelocity[i] = 0.0f;
                            }
                        }
                        if (this.mMovingVelocity[i] < 0.0f) {
                            float[] fArr8 = this.mMovingVelocity;
                            fArr8[i] = fArr8[i] + translateResistency;
                            if (this.mMovingVelocity[i] > 0.0f) {
                                this.mMovingVelocity[i] = 0.0f;
                            }
                        }
                    }
                }
            }
            if (this.mIsMovingTargetExist && this.mLocation[0] == this.mPosMovingTarget[0] && this.mLocation[1] == this.mPosMovingTarget[1] && this.mLocation[2] == this.mPosMovingTarget[2]) {
                onFinishMove();
            }
        } else {
            this.mWorld.requestRender();
        }
        if (bDoGLCalc) {
            gl.glTranslatef(this.mLocation[0], this.mLocation[1], this.mLocation[2]);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void calcForce(float start_x, float start_y, float end_x, float end_y, int tickPassed) {
        float forceHorizontal = end_x - start_x;
        float forceVertical = end_y - start_y;
        if (tickPassed < 5) {
            tickPassed = 5;
        }
        this.mForceFromSide = ((5.0f * forceHorizontal) * this.mSensitivity) / tickPassed;
        this.mForceFromHead = ((5.0f * forceVertical) * this.mSensitivity) / tickPassed;
    }

    public void look(float angleX, float angleY) {
        boolean bRequest = false;
        float newAngleX = TFUtils.filterAngle(angleX);
        float newAngleY = TFUtils.filterAngle(angleY);
        if (this.mAngle[0] != newAngleX || this.mAngle[1] != newAngleY) {
            bRequest = true;
        }
        this.mAngle[0] = newAngleX;
        this.mAngle[1] = newAngleY;
        if (!bRequest || this.mWorld == null) {
            return;
        }
        this.mWorld.requestRender();
    }

    public void rotate(float distanceX, float distanceY, float speed) {
        float[] targetDegree = new float[2];
        int[] directions = new int[2];
        float[] distance = {distanceX, distanceY};
        for (int i = 0; i < 2; i++) {
            directions[i] = distance[i] > 0.0f ? 0 : 1;
            targetDegree[i] = this.mAngle[i] + distance[i];
        }
        rotate(targetDegree[0], targetDegree[1], speed, directions[0], directions[1]);
    }

    public void rotate(float distanceX, float distanceY, long duration) {
        float[] targetDegree = new float[2];
        int[] directions = new int[2];
        float[] distance = {distanceX, distanceY};
        for (int i = 0; i < 2; i++) {
            directions[i] = distance[i] > 0.0f ? 0 : 1;
            targetDegree[i] = this.mAngle[i] + distance[i];
        }
        rotate(targetDegree[0], targetDegree[1], duration, directions[0], directions[1]);
    }

    public void rotate(float angleX, float angleY, float speed, int directionX, int directionY) {
        float maxAngle = 0.0f;
        this.mAngleRotateTarget[0] = angleX;
        this.mAngleRotateTarget[1] = angleY;
        this.mRotateDirection[0] = directionX;
        this.mRotateDirection[1] = directionY;
        for (int i = 0; i < 2; i++) {
            switch (this.mRotateDirection[i]) {
                case 0:
                    if (this.mAngleRotateTarget[i] < this.mAngle[i]) {
                        this.mAngleRotateTarget[i] = TFUtils.filterAngle(this.mAngleRotateTarget[i]);
                        this.mAngleRotateAmount[i] = (360.0f - this.mAngleRotateTarget[i]) + this.mAngle[i];
                        break;
                    } else {
                        this.mAngleRotateAmount[i] = this.mAngleRotateTarget[i] - this.mAngle[i];
                        break;
                    }
                case 1:
                    if (this.mAngleRotateTarget[i] > this.mAngle[i]) {
                        this.mAngleRotateTarget[i] = TFUtils.filterAngle(this.mAngleRotateTarget[i]);
                        this.mAngleRotateAmount[i] = this.mAngle[i] + (360.0f - this.mAngleRotateTarget[i]);
                        break;
                    } else {
                        this.mAngleRotateAmount[i] = this.mAngle[i] - this.mAngleRotateTarget[i];
                        break;
                    }
                case 2:
                    this.mAngleRotateTarget[i] = TFUtils.filterAngle(this.mAngleRotateTarget[i]);
                    this.mAngleRotateAmount[i] = Math.abs(this.mAngleRotateTarget[i] - this.mAngle[i]);
                    if (Math.abs(this.mAngleRotateAmount[i]) > 180.0f) {
                        this.mAngleRotateAmount[i] = 360.0f - this.mAngleRotateAmount[i];
                        break;
                    }
                    break;
            }
            if (maxAngle < this.mAngleRotateAmount[i]) {
                maxAngle = this.mAngleRotateAmount[i];
            }
        }
        float estimatedTime = maxAngle / Math.abs(speed);
        for (int i2 = 0; i2 < 2; i2++) {
            this.mRotationVelocity[i2] = (this.mRotateDirection[i2] == 1 ? -1 : 1) * Math.abs(this.mAngleRotateAmount[i2] / estimatedTime);
        }
        this.mIsIgnoringResistency = true;
        this.mIsRotatingTargetExist = true;
        this.mEffectStatus = 1;
        this.mWorld.requestRender();
    }

    public void rotate(float angleX, float angleY, float speed, int direction) {
        rotate(angleX, angleY, speed, direction, direction);
    }

    public void rotate(float angleX, float angleY, long duration, int directionX, int directionY) {
        this.mAngleRotateTarget[0] = angleX;
        this.mAngleRotateTarget[1] = angleY;
        this.mRotateDirection[0] = directionX;
        this.mRotateDirection[1] = directionY;
        for (int i = 0; i < 2; i++) {
            switch (this.mRotateDirection[i]) {
                case 0:
                    if (this.mAngleRotateTarget[i] < this.mAngle[i]) {
                        this.mAngleRotateTarget[i] = TFUtils.filterAngle(this.mAngleRotateTarget[i]);
                        this.mAngleRotateAmount[i] = (this.mAngleRotateTarget[i] + 360.0f) - this.mAngle[i];
                    } else {
                        this.mAngleRotateAmount[i] = this.mAngleRotateTarget[i] - this.mAngle[i];
                    }
                    this.mRotationVelocity[i] = this.mAngleRotateAmount[i] / ((float) duration);
                    break;
                case 1:
                    if (this.mAngleRotateTarget[i] > this.mAngle[i]) {
                        this.mAngleRotateTarget[i] = TFUtils.filterAngle(this.mAngleRotateTarget[i]);
                        this.mAngleRotateAmount[i] = this.mAngle[i] + (360.0f - this.mAngleRotateTarget[i]);
                    } else {
                        this.mAngleRotateAmount[i] = this.mAngle[i] - this.mAngleRotateTarget[i];
                    }
                    this.mRotationVelocity[i] = (-this.mAngleRotateAmount[i]) / ((float) duration);
                    break;
                case 2:
                    this.mAngleRotateTarget[i] = TFUtils.filterAngle(this.mAngleRotateTarget[i]);
                    this.mAngleRotateAmount[i] = Math.abs(this.mAngleRotateTarget[i] - this.mAngle[i]);
                    if (Math.abs(this.mAngleRotateAmount[i]) > 180.0f) {
                        this.mAngleRotateAmount[i] = 360.0f - this.mAngleRotateAmount[i];
                    }
                    this.mRotationVelocity[i] = this.mAngleRotateAmount[i] / ((float) duration);
                    break;
            }
        }
        this.mIsIgnoringResistency = true;
        this.mIsRotatingTargetExist = true;
        this.mEffectStatus = 1;
        this.mWorld.requestRender();
    }

    public void setPriorAxis(int axis) {
        if (axis == 1) {
            this.mRotateYFirst = true;
        } else {
            this.mRotateYFirst = false;
        }
    }

    public void setPriorAction(int action) {
        if (action == 1) {
            this.mShouldRotateFirst = true;
        } else {
            this.mShouldRotateFirst = false;
        }
    }

    public void setSensitivity(float sensitivity) {
        this.mSensitivity = sensitivity;
    }

    public float getSensitivity() {
        return this.mSensitivity;
    }

    public void setWait(long milliseconds) {
        this.mWaitInitialTime = milliseconds;
        this.mWaitRemainingTime = milliseconds;
    }

    protected void onFinishRotate() {
        freeze(2);
        this.mIsRotatingTargetExist = false;
        if (!isInEffectAnimation()) {
            this.mEffectStatus = 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean updateObject(GL10 gl, int tickPassed, boolean bDoGLCalc) {
        if (this.mWaitRemainingTime > 0) {
            if (this.mWaitRemainingTime == this.mWaitInitialTime) {
                this.mWaitInitialTime = 0L;
            } else {
                this.mWaitRemainingTime -= tickPassed;
            }
            this.mWorld.requestRender();
        }
        if (this.mShouldRotateFirst) {
            prvRotate(gl, tickPassed, bDoGLCalc);
            prvTranslate(gl, tickPassed, bDoGLCalc);
        } else {
            prvTranslate(gl, tickPassed, bDoGLCalc);
            prvRotate(gl, tickPassed, bDoGLCalc);
        }
        return this.mWaitRemainingTime <= 0;
    }

    public void getAngle(float[] angle) {
        System.arraycopy(this.mAngle, 0, angle, 0, 3);
    }

    public float getAngle(int axis) {
        return this.mAngle[axis];
    }

    public float getNextAngle(int tickPassed, int axis) {
        float rotationVelocity = calcRotationVelocity(tickPassed, axis);
        float angle = TFUtils.filterAngle(this.mAngle[axis] + rotationVelocity);
        return angle;
    }

    public void setHeadDegree(float angle) {
        boolean bRequest = false;
        float newAngle = TFUtils.filterAngle(angle);
        if (this.mAngle[2] != newAngle) {
            bRequest = true;
        }
        this.mAngle[2] = newAngle;
        if (!bRequest || this.mWorld == null) {
            return;
        }
        this.mWorld.requestRender();
    }

    public void rotateHeadDegree(float angle, float speed, int direction) {
        this.mAngleRotateTarget[2] = angle;
        this.mRotateDirection[2] = direction;
        switch (this.mRotateDirection[2]) {
            case 0:
                if (this.mAngleRotateTarget[2] < this.mAngle[2]) {
                    this.mAngleRotateTarget[2] = TFUtils.filterAngle(this.mAngleRotateTarget[2]);
                }
                this.mRotationVelocity[2] = Math.abs(speed);
                break;
            case 1:
                if (this.mAngleRotateTarget[2] > this.mAngle[2]) {
                    this.mAngleRotateTarget[2] = TFUtils.filterAngle(this.mAngleRotateTarget[2]);
                }
                this.mRotationVelocity[2] = -Math.abs(speed);
                break;
            case 2:
                this.mAngleRotateTarget[2] = TFUtils.filterAngle(this.mAngleRotateTarget[2]);
                this.mRotationVelocity[2] = Math.abs(speed);
                break;
        }
        this.mIsIgnoringResistency = true;
        this.mIsRotatingTargetExist = true;
        this.mEffectStatus = 1;
        this.mWorld.requestRender();
    }

    public float getWidth() {
        return this.mWidth;
    }

    public void setWidth(float width) {
        this.mWidth = width;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public void setHeight(float height) {
        this.mHeight = height;
    }

    public void getLocation(float[] location) {
        System.arraycopy(this.mLocation, 0, location, 0, 3);
    }

    public void getAbsLocation(float[] location) {
        location[0] = this.mMatrix[12];
        location[1] = this.mMatrix[13];
        location[2] = this.mMatrix[14];
    }

    public float getLocation(int axis) {
        return this.mLocation[axis];
    }

    public float getMovingVelocity(int axis) {
        return this.mMovingVelocity[axis];
    }

    public float getRotatingVelocity(int axis) {
        return this.mRotationVelocity[axis];
    }

    public void lock() {
        this.mIsLocked = true;
    }

    public void unlock() {
        this.mIsLocked = false;
    }

    public boolean getLockStatus() {
        return this.mIsLocked;
    }

    public float getWeight() {
        return this.mWeight;
    }

    public void setWeight(float weight) {
        this.mWeight = weight;
        this.mTranslateResistency = this.mWeight * 5.0E-5f;
        this.mRotationResistency = this.mWeight * 5.0E-4f;
    }

    public void setEffectFinishListener(TFWorld.OnEffectFinishListener effectFinishListener) {
        this.mEffectFinishListener = effectFinishListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isGoingToStop() {
        float sumMovingVelocity = Math.abs(this.mMovingVelocity[0]) + Math.abs(this.mMovingVelocity[1]) + Math.abs(this.mMovingVelocity[2]);
        float sumRotationVelocity = Math.abs(this.mRotationVelocity[0]) + Math.abs(this.mRotationVelocity[1]) + Math.abs(this.mRotationVelocity[2]);
        Log.d(TAG, "sumMovingVelocity:" + sumMovingVelocity + "  sumRotationVelocity:" + sumRotationVelocity);
        return sumMovingVelocity <= 0.001f && sumRotationVelocity <= 0.01f;
    }

    public void setItemIndex(int itemIndex) {
        this.mItemIndex = itemIndex;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public void setVisibility(boolean visibility) {
        if (this.mWorld != null && this.mVisible != visibility) {
            this.mWorld.requestRender();
        }
        this.mVisible = visibility;
    }

    public float getOpacity() {
        return this.mIntendedOpacity;
    }

    public void setOpacity(float opacity) {
        this.mOpacity = opacity;
        this.mIntendedOpacity = opacity;
        this.mFadeRemainingTime = 0L;
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    public float getActualOpacity() {
        return this.mOpacity;
    }

    void setActualOpacity(float opacity) {
        this.mOpacity = opacity;
    }

    public void fade(float targetOpacity, long durationTime) {
        this.mFadeInitialTime = durationTime;
        this.mFadeRemainingTime = durationTime;
        this.mFadeTargetOpacity = targetOpacity;
        if (this.mWorld != null) {
            this.mWorld.requestRender();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void applyOpacity(GL10 gl, int tickPassed) {
        if (this.mFadeRemainingTime > 0) {
            if (this.mFadeRemainingTime == this.mFadeInitialTime) {
                this.mFadeInitialTime = 0L;
            } else {
                float newOpacity = this.mIntendedOpacity + ((this.mFadeTargetOpacity - this.mIntendedOpacity) * (tickPassed / ((float) this.mFadeRemainingTime)));
                this.mFadeRemainingTime -= tickPassed;
                if (newOpacity > 1.0f) {
                    newOpacity = 1.0f;
                    this.mFadeRemainingTime = 0L;
                } else if (newOpacity < 0.0f) {
                    newOpacity = 0.0f;
                    this.mFadeRemainingTime = 0L;
                }
                this.mIntendedOpacity = newOpacity;
                if (this.mParentHolder != null) {
                    this.mOpacity = this.mParentHolder.mOpacity * this.mIntendedOpacity;
                } else {
                    this.mOpacity = this.mIntendedOpacity;
                }
                if (!isInEffectAnimation()) {
                    this.mEffectStatus = 2;
                }
            }
            this.mWorld.requestRender();
        } else if (this.mParentHolder != null) {
            this.mOpacity = this.mParentHolder.mOpacity * this.mIntendedOpacity * this.mExternalFadingFactor;
        } else {
            this.mOpacity = this.mIntendedOpacity * this.mExternalFadingFactor;
        }
    }

    public void setWrapperObject(TFObjectContainer wrapper) {
        this.mWrapper = wrapper;
    }

    public TFObjectContainer getWrapperObject() {
        return this.mWrapper;
    }

    public int getItemIndex() {
        return this.mItemIndex;
    }

    @Deprecated
    public TFHolder getHolder() {
        return this.mParentHolder;
    }

    public TFHolder getParentHolder() {
        return this.mParentHolder;
    }

    public void attachTo(TFHolder parentHolder, int index) {
    }

    public void attachTo(TFHolder parentHolder) {
    }

    public void attachTo(TFWorld world) {
    }

    public void detachFrom(TFHolder parentHolder, boolean bClonePlaceHolder) {
        TFPlaceHolder placeHolder = null;
        if (parentHolder == this.mParentHolder && parentHolder != null) {
            if (this instanceof TFModel) {
                this.mParentHolder.removeModel(this.mParentHolder.getSlotIndex(this), false);
            } else if (this instanceof TFHolder) {
                this.mParentHolder.removeHolder(this.mParentHolder.getSlotIndex(this), false);
            }
            if ((parentHolder instanceof TFPlaceHolder) && ((TFPlaceHolder) parentHolder).isAutoGenerated()) {
                this.mParentHolder.detachFrom(this.mParentHolder.mWorld);
            }
            if (bClonePlaceHolder) {
                placeHolder = this.mParentHolder.createPlaceHolder();
                if (this instanceof TFHolder) {
                    placeHolder.setTerminalHolder(false);
                }
            }
            this.mParentHolder = null;
            if (placeHolder != null) {
                placeHolder.attachTo(this.mWorld);
                attachTo(placeHolder);
                return;
            }
            attachTo(this.mWorld);
            return;
        }
        throw new IllegalArgumentException("Invalid or void parent holder");
    }

    public void detachFrom(TFHolder parentHolder) {
        detachFrom(parentHolder, true);
    }

    public void detachFrom(TFWorld world) {
        if (!this.mCloneObject) {
            if (this.mCloneList != null) {
                int cloneListSize = this.mCloneList.size();
                for (int i = 0; i < cloneListSize; i++) {
                    TFObject o = this.mCloneList.removeFirst();
                    o.detachFrom(this.mWorld);
                }
            }
        } else {
            this.mOriginalTwin.mCloneList.remove(this);
        }
        if (this.mParentHolder != null) {
            if (this.mParentHolder.isTerminalHolder()) {
                this.mParentHolder.removeModel(this.mParentHolder.getSlotIndex(this), false);
            } else {
                this.mParentHolder.removeHolder(this.mParentHolder.getSlotIndex(this), false);
            }
            this.mParentHolder = null;
            this.mWorld = null;
        } else if (this.mWorld != null) {
            this.mWorld.mRenderer.remove(this);
            this.mWorld = null;
        } else {
            throw new IllegalStateException("Tried to detach a model which is already detached from TFWorld.");
        }
    }

    public LinkedList<TFObject> getCloneList() {
        return this.mCloneList;
    }

    public void associateToWorld(TFWorld world) {
        this.mWorld = world;
    }

    public void associateToHolder(TFHolder holder) {
        if (holder != null) {
            this.mParentHolder = holder;
            associateToWorld(holder.mWorld);
            return;
        }
        this.mParentHolder = null;
        associateToWorld(null);
    }

    public TFWorld.Layer getLayer() {
        return this.mLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLayer(TFWorld.Layer layer) {
        this.mLayer = layer;
    }

    public void setSelectListener(TFWorld.OnSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public void prepareReservedParam(int count) {
        this.mReservedParam = new int[count];
    }

    public int getSizeOfReservedParam() {
        if (this.mReservedParam != null) {
            return this.mReservedParam.length;
        }
        return 0;
    }

    public int getReservedParam(int index) {
        return this.mReservedParam[index];
    }

    public void setReservedParam(int index, int value) {
        this.mReservedParam[index] = value;
    }
}
