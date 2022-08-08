package com.nemustech.tiffany.world;

import android.util.Log;

/* loaded from: classes.dex */
public class TFCamera extends TFObject {
    private static final String TAG = "TFCamera";
    static final TFCamera tiffanyCamera = new TFCamera();
    boolean mChangeStatus;
    private long mLensMorphDurationTime;
    float mNear;
    private float mTargetNear;
    private float mTargetWideScale;
    float mWideScale;

    private TFCamera() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TFCamera getCamera() {
        return tiffanyCamera;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attatch(TFWorld world) {
        this.mWorld = world;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void locate(int axis, float location, boolean bRelative) {
        super.locate(axis, location, bRelative);
        this.mChangeStatus = true;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void locate(float locationX, float locationY, float locationZ) {
        super.locate(locationX, locationY, locationZ);
        this.mChangeStatus = true;
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void move(float locationX, float locationY, float locationZ, float startingSpeed, float middleSpeed, float inflectionPoint, float endingSpeed) {
        Log.d(TAG, "Camera move!");
        this.mChangeStatus = true;
        super.move(locationX, locationY, locationZ, startingSpeed, middleSpeed, inflectionPoint, endingSpeed);
    }

    @Override // com.nemustech.tiffany.world.TFObject
    public void rotate(float angleX, float angleY, float speed, int directionX, int directionY) {
        Log.d(TAG, "Camera rotate!");
        this.mChangeStatus = true;
        super.rotate(angleX, angleY, speed, directionX, directionY);
    }

    public void setLens(float wideScale, float perspective) {
        this.mWideScale = wideScale;
        this.mNear = perspective;
        this.mChangeStatus = true;
    }

    public void morphLens(float targetWideScale, float targetPerspective, long duration) {
        this.mTargetWideScale = targetWideScale;
        this.mTargetNear = targetPerspective;
        this.mLensMorphDurationTime = duration;
        if (duration > 0) {
            this.mChangeStatus = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateProjection(TFGL gl, float tickPassed) {
        boolean bShouldUpdate = isInEffectAnimation();
        Log.d(TAG, "updateProjection ! next time update : " + bShouldUpdate);
        if (this.mLensMorphDurationTime > 0) {
            float multiplier = tickPassed / ((float) this.mLensMorphDurationTime);
            float newWideScale = this.mWideScale + ((this.mTargetWideScale - this.mWideScale) * multiplier);
            float newNear = this.mNear + ((this.mTargetNear - this.mNear) * multiplier);
            this.mLensMorphDurationTime -= tickPassed;
            if (newWideScale > this.mWideScale) {
                if (newWideScale > this.mTargetWideScale) {
                    newWideScale = this.mTargetWideScale;
                }
            } else if (newWideScale < this.mTargetWideScale) {
                newWideScale = this.mTargetWideScale;
            }
            if (newNear > this.mNear) {
                if (newNear > this.mTargetNear) {
                    newNear = this.mTargetNear;
                }
            } else if (newNear < this.mTargetNear) {
                newNear = this.mTargetNear;
            }
            this.mWideScale = newWideScale;
            this.mNear = newNear;
            if (!bShouldUpdate && this.mLensMorphDurationTime <= 0) {
                this.mEffectStatus = 2;
            } else {
                bShouldUpdate = true;
            }
            Log.v(TAG, "LensMorphDurationTime : " + this.mLensMorphDurationTime);
        }
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        float ratio = this.mWorld.mRenderer.mWidth / this.mWorld.mRenderer.mHeight;
        float h = this.mWorld.getHeight() / 2.0f;
        float w = this.mWorld.getWidth() / 2.0f;
        gl.glFrustumf((-w) * this.mWideScale * ratio, this.mWideScale * w * ratio, this.mWideScale * (-h), h * this.mWideScale, this.mNear, this.mWorld.getDepth() + this.mNear);
        TFUtils.gluLookAt(gl, this.mLocation[0], this.mLocation[1], this.mLocation[2], this.mAngle[0], this.mAngle[1], this.mAngle[2], 0.0f, 1.0f, 0.0f);
        gl.getMatrix(this.mMatrix, 0);
        this.mChangeStatus = bShouldUpdate;
        return bShouldUpdate;
    }
}
