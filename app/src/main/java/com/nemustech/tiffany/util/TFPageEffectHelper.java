package com.nemustech.tiffany.util;

/* loaded from: classes.dex */
public class TFPageEffectHelper {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final boolean USE_JNI = false;
    private static TFPageEffectHelper stInstance;

    static {
        $assertionsDisabled = !TFPageEffectHelper.class.desiredAssertionStatus();
        stInstance = null;
    }

    public static TFPageEffectHelper getInstance() {
        if (stInstance == null) {
            stInstance = new TFPageEffectHelper();
        }
        return stInstance;
    }

    private TFPageEffectHelper() {
    }

    public void calcCurlMeshAll(int xDim, float xUnit, int yDim, float yUnit, float rollRadius, float rollDirection, float[] progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || progress != null) {
            if (!$assertionsDisabled && xDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && rollRadius <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            for (int i = 0; i < progress.length; i++) {
                float p = progress[i];
                calcCurlMeshInt(xDim, xUnit, yDim, yUnit, rollRadius, rollDirection, p, xyzf, startOffset + (i * xDim * yDim * 4));
            }
            return;
        }
        throw new AssertionError();
    }

    public void calcCurlMesh(int xDim, float xUnit, int yDim, float yUnit, float rollRadius, float rollDirection, float progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || xDim > 1) {
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && rollRadius <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            calcCurlMeshInt(xDim, xUnit, yDim, yUnit, rollRadius, rollDirection, progress, xyzf, startOffset);
            return;
        }
        throw new AssertionError();
    }

    public void calcRDoorOutMeshAll(int xDim, float xUnit, int yDim, float yUnit, float[] progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || progress != null) {
            if (!$assertionsDisabled && xDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            for (int i = 0; i < progress.length; i++) {
                float p = progress[i];
                calcRDoorOutMeshInt(xDim, xUnit, yDim, yUnit, p, xyzf, startOffset + (i * xDim * yDim * 4));
            }
            return;
        }
        throw new AssertionError();
    }

    public void calcRDoorOutMesh(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || xDim > 1) {
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            calcRDoorOutMeshInt(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
            return;
        }
        throw new AssertionError();
    }

    public void calcRDoorInMeshAll(int xDim, float xUnit, int yDim, float yUnit, float[] progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || progress != null) {
            if (!$assertionsDisabled && xDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            for (int i = 0; i < progress.length; i++) {
                float p = progress[i];
                calcRDoorInMeshInt(xDim, xUnit, yDim, yUnit, p, xyzf, startOffset + (i * xDim * yDim * 4));
            }
            return;
        }
        throw new AssertionError();
    }

    public void calcRDoorInMesh(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || xDim > 1) {
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            calcRDoorInMeshInt(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
            return;
        }
        throw new AssertionError();
    }

    private void calcRDoorInMeshInt(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        calcRDoorInMesh_Java(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
    }

    private void calcRDoorOutMeshInt(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        calcRDoorOutMesh_Java(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
    }

    private void calcCurlMeshInt(int xDim, float xUnit, int yDim, float yUnit, float rollRadius, float rollDirection, float progress, float[] xyzf, int startOffset) {
        calcCurlMesh_Java(xDim, xUnit, yDim, yUnit, rollRadius, rollDirection, progress, xyzf, startOffset);
    }

    private void calcRDoorInMesh_Java(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        float theta = (float) Math.toRadians(45.0f * (1.0f - progress));
        float cosvalue = (float) Math.cos(theta);
        float sinvalue = (float) Math.sin(theta);
        for (int iy = 0; iy < yDim; iy++) {
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + startOffset;
                float x = ix * xUnit;
                float rotatedX = (cosvalue * x) + (sinvalue * 0.0f);
                float rotatedZ = ((-sinvalue) * x) + (cosvalue * 0.0f);
                xyzf[i] = rotatedX;
                xyzf[i + 1] = iy * yUnit;
                xyzf[i + 2] = rotatedZ;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + startOffset;
                float x2 = xyzf[i2];
                float y = xyzf[i2 + 1];
                float z = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x2;
                float a2 = xyzf[iNextCol + 1] - y;
                float a3 = xyzf[iNextCol + 2] - z;
                float b1 = xyzf[iNextRow] - x2;
                float b2 = xyzf[iNextRow + 1] - y;
                float b3 = xyzf[iNextRow + 2] - z;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }

    private void calcRDoorOutMesh_Java(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        float theta = (float) Math.toRadians((-90.0f) * progress);
        float cosvalue = (float) Math.cos(theta);
        float sinvalue = (float) Math.sin(theta);
        for (int iy = 0; iy < yDim; iy++) {
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + startOffset;
                float x = ix * xUnit;
                float rotatedX = (cosvalue * x) + (sinvalue * 0.0f);
                float rotatedZ = ((-sinvalue) * x) + (cosvalue * 0.0f);
                xyzf[i] = rotatedX;
                xyzf[i + 1] = iy * yUnit;
                xyzf[i + 2] = rotatedZ;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + startOffset;
                float x2 = xyzf[i2];
                float y = xyzf[i2 + 1];
                float z = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x2;
                float a2 = xyzf[iNextCol + 1] - y;
                float a3 = xyzf[iNextCol + 2] - z;
                float b1 = xyzf[iNextRow] - x2;
                float b2 = xyzf[iNextRow + 1] - y;
                float b3 = xyzf[iNextRow + 2] - z;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }

    private void calcCurlMesh_Java(int xDim, float xUnit, int yDim, float yUnit, float rollRadius, float rollDirection, float progress, float[] xyzf, int startOffset) {
        float xShift = (xDim * xUnit) / 2.0f;
        float yShift = (yDim * yUnit) / 2.0f;
        float cosvalue = (float) Math.cos(rollDirection);
        float sinvalue = (float) Math.sin(rollDirection);
        float rcosvalue = (float) Math.cos(-rollDirection);
        float rsinvalue = (float) Math.sin(-rollDirection);
        for (int iy = 0; iy < yDim; iy++) {
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + startOffset;
                float z = 0.0f;
                float x = (ix * xUnit) - xShift;
                float y = (iy * yUnit) - yShift;
                float rotatedX = (cosvalue * x) + (sinvalue * y);
                float rotatedY = ((-sinvalue) * x) + (cosvalue * y);
                float x2 = rotatedX + xShift;
                float y2 = rotatedY + yShift;
                float theta = (float) ((((progress * 3.141592653589793d) * rollRadius) - x2) / rollRadius);
                if (theta > 0.0f) {
                    x2 += calcCycloidX(rollRadius, theta);
                    z = calcCycloidY(rollRadius, theta);
                }
                float x3 = x2 - xShift;
                float y3 = y2 - yShift;
                float rotatedX2 = (rcosvalue * x3) + (rsinvalue * y3);
                float rotatedY2 = ((-rsinvalue) * x3) + (rcosvalue * y3);
                xyzf[i] = rotatedX2 + xShift;
                xyzf[i + 1] = rotatedY2 + yShift;
                xyzf[i + 2] = z;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + startOffset;
                float x4 = xyzf[i2];
                float y4 = xyzf[i2 + 1];
                float z2 = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x4;
                float a2 = xyzf[iNextCol + 1] - y4;
                float a3 = xyzf[iNextCol + 2] - z2;
                float b1 = xyzf[iNextRow] - x4;
                float b2 = xyzf[iNextRow + 1] - y4;
                float b3 = xyzf[iNextRow + 2] - z2;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }

    private float calcCycloidX(float radius, float theta) {
        return (float) (radius * (theta - Math.sin(theta)));
    }

    private float calcCycloidY(float radius, float theta) {
        return (float) (radius * (1.0d - Math.cos(theta)));
    }

    private float bezier(float p1, float p2, float p3, float p4, float t) {
        return ((1.0f - t) * p1 * (1.0f - t) * (1.0f - t)) + (p2 * 3.0f * t * (1.0f - t) * (1.0f - t)) + (p3 * 3.0f * t * t * (1.0f - t)) + (p4 * t * t * t);
    }

    public void calcGenieMeshAll(int xDim, float xUnit, int yDim, float yUnit, float[] xyzf, int bottleNeckX, int bottleNeckThickness, int inoutCount, int shrinkCount, int bgHeight) {
        if ($assertionsDisabled || xDim > 1) {
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            float x1 = bottleNeckX;
            float y3 = ((yDim - 1) * yUnit) - 120.0f;
            float y4 = (yDim - 1) * yUnit;
            float[] curveXPosition = new float[1000];
            float[] curveX2Position = new float[1000];
            for (int i = 0; i < 1000; i++) {
                curveXPosition[i] = -1.0f;
                curveX2Position[i] = -1.0f;
            }
            for (int i2 = 0; i2 < 10000; i2++) {
                float x = bezier(x1, x1, 0.0f, 0.0f, (i2 / 1000.0f) / 10.0f);
                float y = bezier(0.0f, 400.0f, y3, y4, (i2 / 1000.0f) / 10.0f);
                int index = (int) ((1000.0f * y) / ((yDim - 1) * yUnit));
                if (curveXPosition[index] < 0.0f) {
                    curveXPosition[index] = x;
                }
            }
            float x12 = bottleNeckX + bottleNeckThickness;
            float x4 = (xDim - 1) * xUnit;
            for (int i3 = 0; i3 < 10000; i3++) {
                float x2 = bezier(x12, x12, x4, x4, (i3 / 1000.0f) / 10.0f);
                float y2 = bezier(0.0f, 400.0f, y3, y4, (i3 / 1000.0f) / 10.0f);
                int index2 = (int) ((1000.0f * y2) / ((yDim - 1) * yUnit));
                if (curveX2Position[index2] < 0.0f) {
                    curveX2Position[index2] = x2;
                }
            }
            for (int i4 = 0; i4 < shrinkCount; i4++) {
                calcGenieShrinkMeshInt(xDim, xUnit, yDim, yUnit, i4, xyzf, curveXPosition, curveX2Position, shrinkCount);
            }
            for (int i5 = 0; i5 < inoutCount; i5++) {
                calcGenieInOutMeshInt(xDim, xUnit, yDim, yUnit, shrinkCount + i5, xyzf, (shrinkCount - 1) * xDim * yDim * 4, curveXPosition, curveX2Position, inoutCount, shrinkCount, bgHeight);
            }
            return;
        }
        throw new AssertionError();
    }

    private void calcGenieShrinkMeshInt(int xDim, float xUnit, int yDim, float yUnit, int curFrame, float[] xyzf, float[] matrixX, float[] matrixX2, int shrinkCount) {
        calcGenieShrinkMesh_Java(xDim, xUnit, yDim, yUnit, curFrame, xyzf, matrixX, matrixX2, shrinkCount);
    }

    private void calcGenieInOutMeshInt(int xDim, float xUnit, int yDim, float yUnit, int curFrame, float[] xyzf, int baseOffset, float[] matrixX, float[] matrixX2, int inoutCount, int shrinkCount, int bgHeight) {
        calcGenieInOutMesh_Java(xDim, xUnit, yDim, yUnit, curFrame, xyzf, baseOffset, matrixX, matrixX2, inoutCount, shrinkCount, bgHeight);
    }

    private void calcGenieInOutMesh_Java(int xDim, float xUnit, int yDim, float yUnit, int curFrame, float[] xyzf, int baseOffset, float[] matrixX, float[] matrixX2, int inoutCount, int shrinkCount, int bgHeight) {
        int pageOffset = xDim * 4 * yDim * curFrame;
        int flowDownCount = curFrame - shrinkCount;
        float totalY = (yDim - 1) * yUnit;
        int matrixSize = matrixX.length - 1;
        float offsetY = flowDownCount / (inoutCount - 1);
        float startX = (-offsetY) * matrixSize;
        for (int iy = 0; iy < yDim; iy++) {
            int matrix = (int) (((iy / (yDim - 1)) * matrixSize) + startX);
            if (matrix < 0) {
                matrix = 0;
            }
            float deltaX = matrixX[matrix];
            float deltaX2 = matrixX2[matrix];
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + pageOffset;
                float y = (iy * yUnit) - (offsetY * totalY);
                if (y < bgHeight) {
                    y = bgHeight;
                }
                xyzf[i] = deltaX + ((ix / (xDim - 1)) * (deltaX2 - deltaX));
                xyzf[i + 1] = y;
                xyzf[i + 2] = 0.0f;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + pageOffset;
                float x = xyzf[i2];
                float y2 = xyzf[i2 + 1];
                float z = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x;
                float a2 = xyzf[iNextCol + 1] - y2;
                float a3 = xyzf[iNextCol + 2] - z;
                float b1 = xyzf[iNextRow] - x;
                float b2 = xyzf[iNextRow + 1] - y2;
                float b3 = xyzf[iNextRow + 2] - z;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }

    private void calcGenieShrinkMesh_Java(int xDim, float xUnit, int yDim, float yUnit, int curFrame, float[] xyzf, float[] matrixX, float[] matrixX2, int shrinkCount) {
        int pageOffset = xDim * 4 * yDim * curFrame;
        int matrixSize = matrixX.length - 1;
        for (int iy = 0; iy < yDim; iy++) {
            float origDeltaX = matrixX[(int) ((iy / (yDim - 1)) * matrixSize)];
            float origDeltaX2 = matrixX2[(int) ((iy / (yDim - 1)) * matrixSize)];
            float deltaX = origDeltaX * (curFrame / (shrinkCount - 1));
            float deltaX2 = ((xDim - 1) * xUnit) - ((((xDim - 1) * xUnit) - origDeltaX2) * (curFrame / (shrinkCount - 1)));
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + pageOffset;
                xyzf[i] = deltaX + ((ix / (xDim - 1)) * (deltaX2 - deltaX));
                xyzf[i + 1] = iy * yUnit;
                xyzf[i + 2] = 0.0f;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + pageOffset;
                float x = xyzf[i2];
                float y = xyzf[i2 + 1];
                float z = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x;
                float a2 = xyzf[iNextCol + 1] - y;
                float a3 = xyzf[iNextCol + 2] - z;
                float b1 = xyzf[iNextRow] - x;
                float b2 = xyzf[iNextRow + 1] - y;
                float b3 = xyzf[iNextRow + 2] - z;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }

    public void calcThreeWallsLeftMeshAll(int xDim, float xUnit, int yDim, float yUnit, float[] progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || progress != null) {
            if (!$assertionsDisabled && xDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            for (int i = 0; i < progress.length; i++) {
                float p = progress[i];
                calcThreeWallsLeftMeshInt(xDim, xUnit, yDim, yUnit, p, xyzf, startOffset + (i * xDim * yDim * 4));
            }
            return;
        }
        throw new AssertionError();
    }

    public void calcThreeWallsLeftMesh(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || xDim > 1) {
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            calcThreeWallsLeftMeshInt(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
            return;
        }
        throw new AssertionError();
    }

    public void calcThreeWallsRightMeshAll(int xDim, float xUnit, int yDim, float yUnit, float[] progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || progress != null) {
            if (!$assertionsDisabled && xDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            for (int i = 0; i < progress.length; i++) {
                float p = progress[i];
                calcThreeWallsRightMeshInt(xDim, xUnit, yDim, yUnit, p, xyzf, startOffset + (i * xDim * yDim * 4));
            }
            return;
        }
        throw new AssertionError();
    }

    public void calcThreeWallsRightMesh(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        if ($assertionsDisabled || xDim > 1) {
            if (!$assertionsDisabled && xUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yDim <= 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && yUnit <= 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && xyzf.length < (xDim * yDim * 4) + startOffset) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && startOffset <= 0) {
                throw new AssertionError();
            }
            calcThreeWallsRightMeshInt(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
            return;
        }
        throw new AssertionError();
    }

    private void calcThreeWallsLeftMeshInt(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        calcThreeWallsLeftMesh_Java(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
    }

    private void calcThreeWallsRightMeshInt(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        calcThreeWallsRightMesh_Java(xDim, xUnit, yDim, yUnit, progress, xyzf, startOffset);
    }

    private void calcThreeWallsLeftMesh_Java(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        float xTotalLength = (xDim - 1) * xUnit;
        float angle = (float) Math.acos(1.0f - progress);
        if (progress <= 0.0f) {
            angle = 0.0f;
        }
        if (progress >= 1.0f) {
            angle = 1.5707964f;
        }
        float cosvalue = (float) Math.cos(angle);
        float sinvalue = (float) Math.sin(angle);
        float progressParabola = progress;
        if (progressParabola > 0.5d) {
            progressParabola = 1.0f - progressParabola;
        }
        float angleParabola = (float) Math.acos(1.0f - progressParabola);
        if (progressParabola <= 0.0f) {
            angleParabola = 0.0f;
        }
        float kappa = (float) Math.atan(angleParabola);
        for (int iy = 0; iy < yDim; iy++) {
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + startOffset;
                float x = ix * xUnit;
                float y = iy * yUnit;
                float z = 0.0f;
                float normalizedX = x / xTotalLength;
                if (progressParabola > 0.0f && angleParabola > 0.0f) {
                    z = (xDim - 1) * xUnit * ((((normalizedX - 0.5f) * kappa) * (normalizedX - 0.5f)) - (kappa / 4.0f));
                }
                float x2 = x - xTotalLength;
                float rotatedX = (cosvalue * x2) + (sinvalue * z);
                float rotatedZ = ((-sinvalue) * x2) + (cosvalue * z);
                float z2 = rotatedZ;
                float x3 = rotatedX + xTotalLength;
                if (progress <= 1.0f) {
                    x3 -= progress * xTotalLength;
                } else if (progress > 1.0f) {
                    x3 -= xTotalLength;
                    z2 += (progress - 1.0f) * xTotalLength;
                }
                xyzf[i] = x3;
                xyzf[i + 1] = y;
                xyzf[i + 2] = z2;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + startOffset;
                float x4 = xyzf[i2];
                float y2 = xyzf[i2 + 1];
                float z3 = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x4;
                float a2 = xyzf[iNextCol + 1] - y2;
                float a3 = xyzf[iNextCol + 2] - z3;
                float b1 = xyzf[iNextRow] - x4;
                float b2 = xyzf[iNextRow + 1] - y2;
                float b3 = xyzf[iNextRow + 2] - z3;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }

    private void calcThreeWallsRightMesh_Java(int xDim, float xUnit, int yDim, float yUnit, float progress, float[] xyzf, int startOffset) {
        float xTotalLength = (xDim - 1) * xUnit;
        float angle = (float) Math.acos(1.0f - progress);
        if (progress <= 0.0f) {
            angle = 0.0f;
        }
        if (progress >= 1.0f) {
            angle = 1.5707964f;
        }
        float cosvalue = (float) Math.cos(-angle);
        float sinvalue = (float) Math.sin(-angle);
        float progressParabola = progress;
        if (progressParabola > 0.5d) {
            progressParabola = 1.0f - progressParabola;
        }
        float angleParabola = (float) Math.acos(1.0f - progressParabola);
        if (progressParabola <= 0.0f) {
            angleParabola = 0.0f;
        }
        float kappa = (float) Math.atan(angleParabola);
        for (int iy = 0; iy < yDim; iy++) {
            for (int ix = 0; ix < xDim; ix++) {
                int i = (((iy * xDim) + ix) * 4) + startOffset;
                float x = ix * xUnit;
                float y = iy * yUnit;
                float z = 0.0f;
                float normalizedX = x / xTotalLength;
                if (progressParabola > 0.0f && angleParabola > 0.0f) {
                    z = (xDim - 1) * xUnit * ((((normalizedX - 0.5f) * kappa) * (normalizedX - 0.5f)) - (kappa / 4.0f));
                }
                float rotatedX = (cosvalue * x) + (sinvalue * z);
                float rotatedZ = ((-sinvalue) * x) + (cosvalue * z);
                float x2 = rotatedX;
                float z2 = rotatedZ;
                if (progress <= 1.0f) {
                    x2 += progress * xTotalLength;
                } else if (progress > 1.0f) {
                    x2 += xTotalLength;
                    z2 += (progress - 1.0f) * xTotalLength;
                }
                xyzf[i] = x2;
                xyzf[i + 1] = y;
                xyzf[i + 2] = z2;
                xyzf[i + 3] = 0.0f;
            }
        }
        for (int iy2 = 0; iy2 < yDim - 1; iy2++) {
            for (int ix2 = 0; ix2 < xDim - 1; ix2++) {
                int i2 = (((iy2 * xDim) + ix2) * 4) + startOffset;
                float x3 = xyzf[i2];
                float y2 = xyzf[i2 + 1];
                float z3 = xyzf[i2 + 2];
                int iNextCol = i2 + 4;
                int iNextRow = i2 + (xDim * 4);
                float a1 = xyzf[iNextCol] - x3;
                float a2 = xyzf[iNextCol + 1] - y2;
                float a3 = xyzf[iNextCol + 2] - z3;
                float b1 = xyzf[iNextRow] - x3;
                float b2 = xyzf[iNextRow + 1] - y2;
                float b3 = xyzf[iNextRow + 2] - z3;
                float c1 = (a2 * b3) - (a3 * b2);
                float c2 = (a3 * b1) - (a1 * b3);
                float c3 = (a1 * b2) - (a2 * b1);
                float f = (c1 * 0.0f) + (c2 * 0.0f) + (c3 * (-1.0f));
                xyzf[i2 + 3] = f;
            }
        }
    }
}
