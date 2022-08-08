package com.nemustech.tiffany.world;

import java.nio.FloatBuffer;

/* loaded from: classes.dex */
public class TFVector3D {
    public static final float EPSILON = 1.0E-6f;
    public static final int W = 3;
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;

    public static void set(float[] v, int vOffset, float x, float y, float z) {
        v[vOffset + 0] = x;
        v[vOffset + 1] = y;
        v[vOffset + 2] = z;
    }

    public static void set(float[] v, int vOffset, float[] src, int srcOffset) {
        System.arraycopy(src, srcOffset, v, vOffset, 3);
    }

    public static void set(float[] v, int vOffset, FloatBuffer src, int srcOffset) {
        set(v, vOffset, src.get(srcOffset + 0), src.get(srcOffset + 1), src.get(srcOffset + 2));
    }

    public static void setW(float[] v, int vOffset, float w) {
        v[vOffset + 3] = w;
    }

    public static void setW(float[] v, int vOffset) {
        setW(v, vOffset, 1.0f);
    }

    public static float lengthSquare(float[] v, int vOffset) {
        return dot(v, vOffset, v, vOffset);
    }

    public static float length(float[] v, int vOffset) {
        return (float) Math.sqrt(lengthSquare(v, vOffset));
    }

    public static void add(float[] result, int rOffset, float[] v1, int v1Offset, float[] v2, int v2Offset) {
        result[rOffset + 0] = v1[v1Offset + 0] + v2[v2Offset + 0];
        result[rOffset + 1] = v1[v1Offset + 1] + v2[v2Offset + 1];
        result[rOffset + 2] = v1[v1Offset + 2] + v2[v2Offset + 2];
    }

    public static void sub(float[] result, int rOffset, float[] v1, int v1Offset, float[] v2, int v2Offset) {
        result[rOffset + 0] = v1[v1Offset + 0] - v2[v2Offset + 0];
        result[rOffset + 1] = v1[v1Offset + 1] - v2[v2Offset + 1];
        result[rOffset + 2] = v1[v1Offset + 2] - v2[v2Offset + 2];
    }

    public static void mul(float[] result, int rOffset, float[] v, int vOffset, float scalar) {
        result[rOffset + 0] = v[vOffset + 0] * scalar;
        result[rOffset + 1] = v[vOffset + 1] * scalar;
        result[rOffset + 2] = v[vOffset + 2] * scalar;
    }

    public static float dot(float[] v1, int v1Offset, float[] v2, int v2Offset) {
        float x = v1[v1Offset + 0] * v2[v2Offset + 0];
        float y = v1[v1Offset + 1] * v2[v2Offset + 1];
        float z = v1[v1Offset + 2] * v2[v2Offset + 2];
        return x + y + z;
    }

    public static void cross(float[] result, int rOffset, float[] v1, int v1Offset, float[] v2, int v2Offset) {
        float i = (v1[v1Offset + 1] * v2[v2Offset + 2]) - (v1[v1Offset + 2] * v2[v2Offset + 1]);
        float j = (v1[v1Offset + 2] * v2[v2Offset + 0]) - (v1[v1Offset + 0] * v2[v2Offset + 2]);
        float k = (v1[v1Offset + 0] * v2[v2Offset + 1]) - (v1[v1Offset + 1] * v2[v2Offset + 0]);
        result[rOffset + 0] = i;
        result[rOffset + 1] = j;
        result[rOffset + 2] = k;
    }

    public static void add(float[] v1, int v1Offset, float[] v2, int v2Offset) {
        add(v1, v1Offset, v1, v1Offset, v2, v2Offset);
    }

    public static void sub(float[] v1, int v1Offset, float[] v2, int v2Offset) {
        sub(v1, v1Offset, v1, v1Offset, v2, v2Offset);
    }

    public static void mul(float[] v, int vOffset, float scalar) {
        mul(v, vOffset, v, vOffset, scalar);
    }

    public static float getPointOnLine(float[] point, int pointOffset, float[] p1, int p1Offset, float[] p2, int p2Offset, float value, int whichAxis) {
        sub(point, pointOffset, p2, p2Offset, p1, p1Offset);
        if (whichAxis < 0 || whichAxis > 2) {
            whichAxis = 2;
        }
        float t = (value - p1[p1Offset + whichAxis]) / point[whichAxis];
        mul(point, pointOffset, t);
        add(point, pointOffset, p1, p1Offset);
        return t;
    }

    public static void getPointOnLine(float[] point, int pointOffset, float[] p1, int p1Offset, float[] p2, int p2Offset, float t) {
        sub(point, pointOffset, p2, p2Offset, p1, p1Offset);
        mul(point, pointOffset, t);
        add(point, pointOffset, p1, p1Offset);
    }

    public static float getTOfPointOnLine(float[] point, int pointOffset, float[] p1, int p1Offset, float[] p2, int p2Offset) {
        float dir_x = p2[p2Offset + 0] - p1[p1Offset + 0];
        float dir_y = p2[p2Offset + 1] - p1[p1Offset + 1];
        float dir_z = p2[p2Offset + 2] - p1[p1Offset + 2];
        float p_x = point[pointOffset + 0] - p1[p1Offset + 0];
        float p_y = point[pointOffset + 1] - p1[p1Offset + 1];
        float p_z = point[pointOffset + 2] - p1[p1Offset + 2];
        float t_x = isEqual(dir_x, 0.0f) ? Float.NaN : p_x / dir_x;
        float t_y = isEqual(dir_y, 0.0f) ? Float.NaN : p_y / dir_y;
        float t_z = isEqual(dir_z, 0.0f) ? Float.NaN : p_z / dir_z;
        if (!isEqual(t_x, t_y) || !isEqual(t_y, t_z) || !isEqual(t_z, t_x)) {
            return Float.NaN;
        }
        if (Float.isNaN(t_x)) {
            if (!Float.isNaN(t_y)) {
                return t_y;
            }
            return t_z;
        }
        return t_x;
    }

    public static float dotWithAxis(float[] p1, int p1Offset, float[] p2, int p2Offset, int whichAxis) {
        switch (whichAxis) {
            case 0:
                return p2[p2Offset + 0] - p1[p1Offset + 0];
            case 1:
                return p2[p2Offset + 1] - p1[p1Offset + 1];
            default:
                return p2[p2Offset + 2] - p1[p1Offset + 2];
        }
    }

    public static boolean isEqual(float a, float b) {
        float diff = b - a;
        return diff < 1.0E-6f && diff > -1.0E-6f;
    }
}
