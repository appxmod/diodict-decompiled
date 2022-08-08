package com.nemustech.tiffany.world;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.Debug;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFUtils {
    private static final int ACTIVITY_TITLE_SIZE = 25;
    static final int DPI_120 = 120;
    static final int DPI_160 = 160;
    static final int DPI_240 = 240;
    private static final int HEADER_SIZE = 12;
    public static final String PACKAGE_NAME = "com.nemustech.tiffany.world";
    static final int PANEL_SCREEN_SIZE_128 = 128;
    static final int PANEL_SCREEN_SIZE_256 = 256;
    static final int PANEL_SCREEN_SIZE_512 = 512;
    static final int PANEL_SCREEN_SIZE_64 = 64;
    static final int PANEL_SCREEN_SIZE_MAX = 512;
    private static final int STATUS_BAR_HEIGHT = 25;
    private static final String TAG = "TFUtils";
    private static final int TITLE_BAR_HEIGHT = 25;
    public static FloatBuffer mPixel;
    private static int[] rDst = new int[4];
    private static int[] rSrc = new int[4];
    private static int[] rSrcCopy = new int[4];
    static int[] gInfoBuf = new int[6];
    static MatrixStack matrixSimulator = new MatrixStack();

    private TFUtils() {
    }

    private static int makeUnsignedShort(byte signedByte) {
        return signedByte & 255;
    }

    public static float filterAngle(float angle) {
        float filteredAngle = angle;
        if (filteredAngle < 0.0f) {
            filteredAngle = (filteredAngle % 360.0f) + 360.0f;
        }
        return filteredAngle % 360.0f;
    }

    public static int calcOrthoDeviationAngle(float cameraDistanceOnLevel, float level) {
        int angle = (int) ((Math.atan(level / cameraDistanceOnLevel) * 180.0d) / 3.141592653589793d);
        return angle;
    }

    public static boolean isBetweenAngle(float rangeDegreeA, float rangeDegreeB, float targetAngle, boolean bIncludeBoundary) {
        float range = diffAngle(rangeDegreeA, rangeDegreeB);
        float diffA = diffAngle(rangeDegreeA, targetAngle);
        float diffB = diffAngle(rangeDegreeB, targetAngle);
        if (!bIncludeBoundary || !(diffA == 0.0f || diffB == 0.0f)) {
            return diffA < range && diffB < range;
        }
        return true;
    }

    public static float calcMovingSpeed(TFObject object, float destinationX, float destinationY, float destinationZ, float speed, int axisOfSpeed) {
        float maxDistance = 0.0f;
        float[] distance = {Math.abs(object.getLocation(0) - destinationX), Math.abs(object.getLocation(1) - destinationY), Math.abs(object.getLocation(2) - destinationZ)};
        for (int i = 0; i < 3; i++) {
            if (distance[i] > maxDistance) {
                maxDistance = distance[i];
            }
        }
        float finalSpeed = (speed * maxDistance) / distance[axisOfSpeed];
        return finalSpeed;
    }

    public static float diffAngle(float degreeA, float degreeB) {
        float gap = Math.abs(degreeA - degreeB) % 360.0f;
        return gap <= 180.0f ? gap : 360.0f - gap;
    }

    public static void loadLibrary() {
        System.loadLibrary("tfapps");
    }

    public static void cropyBuffer(int[] srcBuf, int[] dstBuf, Rect rectSrc, Rect rectSrcCopy) {
        int[] rSrc2 = {rectSrc.left, rectSrc.top, rectSrc.right, rectSrc.bottom};
        int[] rSrcCopy2 = {rectSrcCopy.left, rectSrcCopy.top, rectSrcCopy.right, rectSrcCopy.bottom};
        TFJniUtils.crop_buffer(dstBuf, srcBuf, rSrc2, rSrcCopy2);
    }

    public static int[] getBitmapPixels(Bitmap bmp, Rect rectResult) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[height * width];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        rectResult.set(0, 0, width, height);
        return pixels;
    }

    static ByteBuffer extractJni(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] arr = new int[height * width];
        int[] pixels = new int[height * width];
        ByteBuffer bb = ByteBuffer.allocateDirect(height * width * 4);
        bb.order(ByteOrder.BIG_ENDIAN);
        IntBuffer ib = bb.asIntBuffer();
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        TFJniUtils.extract(width, height, arr, pixels, true);
        ib.put(arr);
        bb.position(0);
        return bb;
    }

    private static ByteBuffer decodeTex(InputStream stream, int[] info) {
        ByteBuffer ret;
        try {
            ByteBuffer header = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
            stream.read(header.array(), 0, 12);
            info[0] = header.getInt();
            info[1] = header.getInt();
            int dataSize = header.getInt();
            ByteBuffer data = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN);
            int readSize = stream.read(data.array());
            ByteBuffer buf = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN);
            int uncompSize = TFJniUtils.decompress(data.array(), 0, readSize, buf.array(), 0, -1);
            if (uncompSize < 0) {
                throw new IOException("Uncompressing failed");
            }
            int tex_width = getTextureLength(info[0]);
            int tex_height = getTextureLength(info[1]);
            if (tex_width != info[0] || tex_height != info[1]) {
                ByteBuffer expand = ByteBuffer.allocate(tex_width * 4 * tex_height).order(ByteOrder.LITTLE_ENDIAN);
                int[] rectSrc = {0, 0, info[0] * 4, info[1]};
                int[] rectDst = {0, 0, tex_width * 4, tex_height};
                TFJniUtils.copy_raw_buffer(expand.array(), rectDst, buf.array(), rectSrc, rectSrc, 0, 0);
                ret = expand;
            } else {
                ret = buf;
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ByteBuffer decodeStream(InputStream iStream, String fileName, int[] info) {
        String strExt = fileName.substring(fileName.lastIndexOf(46));
        if (strExt.compareToIgnoreCase(".tex") == 0) {
            ByteBuffer bb = decodeTex(iStream, info);
            return bb;
        } else if (strExt.compareToIgnoreCase(".jpg") == 0 || strExt.compareToIgnoreCase(".jpeg") == 0) {
            byte[] jpgbuf = null;
            int jpgbuf_size = 0;
            try {
                jpgbuf_size = iStream.available();
                jpgbuf = new byte[jpgbuf_size];
                iStream.read(jpgbuf, 0, jpgbuf_size);
                iStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int size = TFJniUtils.get_jpeg_data_size(jpgbuf, jpgbuf_size);
            byte[] bmpbuf = new byte[size];
            TFJniUtils.load_jpeg_mem(jpgbuf, jpgbuf_size, info, bmpbuf);
            ByteBuffer bb2 = ByteBuffer.wrap(bmpbuf);
            return bb2;
        } else if (strExt.compareToIgnoreCase(".png") != 0) {
            return null;
        } else {
            byte[] pngbuf = null;
            try {
                int pngbuf_size = iStream.available();
                pngbuf = new byte[pngbuf_size];
                iStream.read(pngbuf, 0, pngbuf_size);
            } catch (IOException e12) {
                e12.printStackTrace();
            }
            int size2 = TFJniUtils.get_png_data_size(pngbuf);
            byte[] bmpbuf2 = new byte[size2];
            TFJniUtils.load_png_mem(pngbuf, info, bmpbuf2);
            ByteBuffer bb3 = ByteBuffer.wrap(bmpbuf2);
            return bb3;
        }
    }

    static ByteBuffer decodeFile(String fileName, int[] info) {
        try {
            FileInputStream iStream = new FileInputStream(fileName);
            return decodeStream(iStream, fileName, info);
        } catch (Exception e) {
            Log.e(TAG, "File open error : " + fileName);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap decodeFile(String fileName) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        return BitmapFactory.decodeFile(fileName, opt);
    }

    static ByteBuffer decodeResource(Resources resources, int resId, int[] info) {
        String strPath = resources.getString(resId);
        InputStream iStream = resources.openRawResource(resId);
        return decodeStream(iStream, strPath, info);
    }

    public static Bitmap decodeResource(Resources resources, int resId) {
        if (resources == null) {
            throw new IllegalArgumentException("decodeResource with 'null' resources");
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(resources, resId, opt);
        return bmp;
    }

    public static int[] convertRGBAtoARGB(int width, int height, int[] bufRGBA, Rect rectResult) {
        int[] bufARGB = new int[width * height];
        TFJniUtils.extract(width, height, bufARGB, bufRGBA, false);
        rectResult.set(0, 0, width, height);
        return bufARGB;
    }

    public static void copyBuffer(int[] bufSrc, byte[] bufDst, Rect rectSrc, Rect rectSrcCopy, Rect rectResult) {
        int copy_width = rectSrcCopy.width();
        int copy_height = rectSrcCopy.height();
        int[] rDst2 = {0, 0, copy_width, copy_height};
        int[] rSrc2 = {rectSrc.left, rectSrc.top, rectSrc.right, rectSrc.bottom};
        int[] rSrcCopy2 = {rectSrcCopy.left, rectSrcCopy.top, rectSrcCopy.right, rectSrcCopy.bottom};
        TFJniUtils.copy_buffer(bufDst, rDst2, bufSrc, rSrc2, rSrcCopy2, 0, 0, false);
        rectResult.set(0, 0, copy_width, copy_height);
    }

    /* renamed from: com.nemustech.tiffany.world.TFUtils$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    public static void saveBitmapToTextureFile(Bitmap bitmap, String fileName) {
        try {
            Bitmap.Config conf = bitmap.getConfig();
            switch (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[conf.ordinal()]) {
                case 1:
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int size = width * 4 * height;
                    ByteBuffer header = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer buf = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer comp = ByteBuffer.allocate(size);
                    header.putInt(width);
                    header.putInt(height);
                    header.putInt(size);
                    bitmap.copyPixelsToBuffer(buf);
                    int comp_size = TFJniUtils.compress(buf.array(), 0, -1, comp.array(), 0, -1);
                    if (comp_size < 0) {
                        throw new IOException("Compressing failed");
                    }
                    FileOutputStream out = new FileOutputStream(fileName + ".tex");
                    out.write(header.array());
                    out.write(comp.array(), 0, comp_size);
                    out.flush();
                    out.close();
                    return;
                default:
                    throw new IOException("Invalid Bitmap config");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getTextureLength(int length) {
        for (int i = 0; i <= 11; i++) {
            int tl = 1 << i;
            if (tl >= length) {
                return tl;
            }
        }
        Log.e(TAG, "Too big texture size");
        return 0;
    }

    public static void copyBufferToCenter(int[] bufSrc, byte[] bufDst, Rect rectSrc, Rect rectSrcCopy, Rect rectResult, int bufferWidth, int bufferHeight) {
        rDst[0] = 0;
        rDst[1] = 0;
        rDst[2] = bufferWidth;
        rDst[3] = bufferHeight;
        rSrc[0] = rectSrc.left;
        rSrc[1] = rectSrc.top;
        rSrc[2] = rectSrc.right;
        rSrc[3] = rectSrc.bottom;
        rSrcCopy[0] = rectSrcCopy.left;
        rSrcCopy[1] = rectSrcCopy.top;
        rSrcCopy[2] = rectSrcCopy.right;
        rSrcCopy[3] = rectSrcCopy.bottom;
        if (bufferWidth < rectSrcCopy.width() || bufferHeight < rectSrcCopy.height()) {
            Log.e(TAG, "ERROR !!! The panel size must greater than rectSrcCopy width or height");
            return;
        }
        TFJniUtils.copy_buffer(bufDst, rDst, bufSrc, rSrc, rSrcCopy, ((bufferWidth + 0) - rectSrcCopy.width()) / 2, ((bufferHeight + 0) - rectSrcCopy.height()) / 2, false);
        rectResult.set(0, 0, bufferWidth, bufferHeight);
    }

    static void defragSparseModelArray(SparseArray<TFModel> arrayModel) {
        int count = arrayModel.size();
        for (int i = 0; i < count; i++) {
            int id = arrayModel.keyAt(i);
            if (id != i) {
                TFModel model = arrayModel.valueAt(i);
                arrayModel.remove(id);
                arrayModel.append(i, model);
            }
        }
    }

    static void defragSparseHolderArray(SparseArray<TFHolder> arrayModel) {
        int count = arrayModel.size();
        for (int i = 0; i < count; i++) {
            int id = arrayModel.keyAt(i);
            if (id != i) {
                TFHolder model = arrayModel.valueAt(i);
                arrayModel.remove(id);
                arrayModel.append(i, model);
            }
        }
    }

    public static void overlap(TFModel fromModel, TFModel toModel, long animationTime) {
        TFWorld world = fromModel.mWorld;
        toModel.setOpacity(0.0f);
        toModel.attachTo(world);
        fromModel.fade(0.0f, animationTime);
        toModel.fade(1.0f, animationTime);
    }

    public static void copyBufferToTopLeft(int[] bufSrc, byte[] bufDst, Rect rectSrc, Rect rectSrcCopy, Rect rectResult, int bufferWidth, int bufferHeight) {
        rDst[0] = 0;
        rDst[1] = 0;
        rDst[2] = bufferWidth;
        rDst[3] = bufferHeight;
        rSrc[0] = rectSrc.left;
        rSrc[1] = rectSrc.top;
        rSrc[2] = rectSrc.right;
        rSrc[3] = rectSrc.bottom;
        rSrcCopy[0] = rectSrcCopy.left;
        rSrcCopy[1] = rectSrcCopy.top;
        rSrcCopy[2] = rectSrcCopy.right;
        rSrcCopy[3] = rectSrcCopy.bottom;
        if (bufferWidth < rectSrcCopy.width() || bufferHeight < rectSrcCopy.height()) {
            Log.e(TAG, "ERROR !!! The panel size must greater than rectSrcCopy width or height");
            return;
        }
        TFJniUtils.copy_buffer(bufDst, rDst, bufSrc, rSrc, rSrcCopy, 0, 0, false);
        rectResult.set(0, 0, bufferWidth, bufferHeight);
    }

    static void gluLookAt(MatrixStack gl, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;
        float rlf = 1.0f / Matrix.length(fx, fy, fz);
        float fx2 = fx * rlf;
        float fy2 = fy * rlf;
        float fz2 = fz * rlf;
        float rlup = 1.0f / Matrix.length(upX, upY, upZ);
        float upX2 = upX * rlup;
        float upY2 = upY * rlup;
        float upZ2 = upZ * rlup;
        float sx = (fy2 * upZ2) - (fz2 * upY2);
        float sy = (fz2 * upX2) - (fx2 * upZ2);
        float sz = (fx2 * upY2) - (fy2 * upX2);
        float ux = (sy * fz2) - (sz * fy2);
        float uy = (sz * fx2) - (sx * fz2);
        float uz = (sx * fy2) - (sy * fx2);
        float[] m = {sx, ux, -fx2, 0.0f, sy, uy, -fy2, 0.0f, sz, uz, -fz2, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        gl.glMultMatrixf(m, 0);
        gl.glTranslatef(-eyeX, -eyeY, -eyeZ);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void gluLookAt(GL10 gl, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;
        float rlf = 1.0f / Matrix.length(fx, fy, fz);
        float fx2 = fx * rlf;
        float fy2 = fy * rlf;
        float fz2 = fz * rlf;
        float rlup = 1.0f / Matrix.length(upX, upY, upZ);
        float upX2 = upX * rlup;
        float upY2 = upY * rlup;
        float upZ2 = upZ * rlup;
        float sx = (fy2 * upZ2) - (fz2 * upY2);
        float sy = (fz2 * upX2) - (fx2 * upZ2);
        float sz = (fx2 * upY2) - (fy2 * upX2);
        float ux = (sy * fz2) - (sz * fy2);
        float uy = (sz * fx2) - (sx * fz2);
        float uz = (sx * fy2) - (sy * fx2);
        float[] m = {sx, ux, -fx2, 0.0f, sy, uy, -fy2, 0.0f, sz, uz, -fz2, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        gl.glMultMatrixf(m, 0);
        gl.glTranslatef(-eyeX, -eyeY, -eyeZ);
    }

    public static float calcZ(TFWorld world, int screenWidth, int screenHeight, int rasterizedLength, float length) {
        float[] win = new float[2];
        float[] v = new float[8];
        float[] vertex = {0.0f, length, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        float[] modelView = new float[16];
        matrixSimulator.glLoadIdentity();
        matrixSimulator.getMatrix(modelView, 0);
        TFCamera camera = world.getCamera();
        matrixSimulator.glLoadIdentity();
        float ratio = screenWidth / screenHeight;
        float h = world.getHeight() / 2.0f;
        float w = world.getWidth() / 2.0f;
        float[] cameraPosition = new float[3];
        float[] projectView = new float[16];
        matrixSimulator.glFrustumf((-w) * camera.mWideScale * ratio, camera.mWideScale * w * ratio, (-h) * camera.mWideScale, camera.mWideScale * h, camera.mNear, camera.mNear + world.getDepth());
        camera.getLocation(cameraPosition);
        gluLookAt(matrixSimulator, cameraPosition[0], cameraPosition[1], cameraPosition[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        matrixSimulator.getMatrix(projectView, 0);
        float[] mvp = new float[16];
        Matrix.multiplyMM(mvp, 0, projectView, 0, modelView, 0);
        for (int i = 0; i < 2; i++) {
            Matrix.multiplyMV(v, i * 4, mvp, 0, vertex, i * 4);
            float rw = 1.0f / v[(i * 4) + 3];
            win[i] = screenHeight * ((v[(i * 4) + 1] * rw) + 1.0f) * 0.5f;
        }
        float d = (cameraPosition[2] * (win[0] - win[1])) / length;
        float z = cameraPosition[2] - ((length * d) / rasterizedLength);
        Log.d(TAG, "Calculated d:" + d + " Z:" + z);
        return z;
    }

    public static float calcRealWorldFactor(TFWorld world, int screenWidth, int screenHeight, float distance) {
        float[] win = new float[2];
        float[] v = new float[8];
        float[] vertex = {0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        float[] modelView = new float[16];
        matrixSimulator.glLoadIdentity();
        matrixSimulator.getMatrix(modelView, 0);
        TFCamera camera = world.getCamera();
        matrixSimulator.glLoadIdentity();
        float ratio = screenWidth / screenHeight;
        float h = world.getHeight() / 2.0f;
        float w = world.getWidth() / 2.0f;
        float[] cameraPosition = new float[3];
        float[] projectView = new float[16];
        matrixSimulator.glFrustumf((-w) * camera.mWideScale * ratio, camera.mWideScale * w * ratio, (-h) * camera.mWideScale, camera.mWideScale * h, camera.mNear, camera.mNear + world.getDepth());
        camera.getLocation(cameraPosition);
        gluLookAt(matrixSimulator, cameraPosition[0], cameraPosition[1], cameraPosition[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        matrixSimulator.getMatrix(projectView, 0);
        float[] mvp = new float[16];
        Matrix.multiplyMM(mvp, 0, projectView, 0, modelView, 0);
        for (int i = 0; i < 2; i++) {
            Matrix.multiplyMV(v, i * 4, mvp, 0, vertex, i * 4);
            float rw = 1.0f / v[(i * 4) + 3];
            win[i] = screenHeight * ((v[(i * 4) + 1] * rw) + 1.0f) * 0.5f;
        }
        float d = cameraPosition[2] * (win[0] - win[1]);
        float rasterizedLength = d / distance;
        return 1.0f / rasterizedLength;
    }

    public static void drawPixel(GL10 gl, float x, float y, float z, float size) {
        if (mPixel == null) {
            mPixel = ByteBuffer.allocateDirect(24).order(ByteOrder.nativeOrder()).asFloatBuffer();
        }
        mPixel.position(0);
        mPixel.put(x);
        mPixel.put(y);
        mPixel.put(z);
        mPixel.position(0);
        gl.glEnableClientState(32884);
        gl.glDisableClientState(32888);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glPointSize(size);
        gl.glVertexPointer(3, 5126, 0, mPixel);
        gl.glDrawArrays(0, 0, 1);
    }

    public static void drawLine(GL10 gl, float x, float y, float z, float x2, float y2, float z2) {
        if (mPixel == null) {
            mPixel = ByteBuffer.allocateDirect(24).order(ByteOrder.nativeOrder()).asFloatBuffer();
        }
        mPixel.position(0);
        mPixel.put(x);
        mPixel.put(y);
        mPixel.put(z);
        mPixel.put(x2);
        mPixel.put(y2);
        mPixel.put(z2);
        mPixel.position(0);
        gl.glEnableClientState(32884);
        gl.glDisableClientState(32888);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glVertexPointer(3, 5126, 0, mPixel);
        gl.glDrawArrays(1, 0, 2);
    }

    public static int convertValueByDPI(int value, int srcDpi, int dstDpi) {
        float a = value * dstDpi;
        float b = a / srcDpi;
        float c = b + 0.5f;
        int result = (int) c;
        return result;
    }

    public static int getTitleBarHeight(int currentDPI) {
        return convertValueByDPI(25, DPI_160, currentDPI);
    }

    public static int getStatusBarHeight(Context context) {
        return TypedValue.complexToDimensionPixelSize(6401, context.getResources().getDisplayMetrics());
    }

    public static Context getTiffanyContext(Context context) {
        try {
            Context c = context.createPackageContext(PACKAGE_NAME, 2);
            return c;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveBitmapToFile(Context context, String filename, Bitmap bitmap, Bitmap.CompressFormat saveFormat, int mode) {
        boolean result = false;
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(filename, mode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap.compress(saveFormat, 100, out)) {
            result = true;
        }
        try {
            out.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public static boolean saveBitmapToFile(String path, Bitmap bitmap, Bitmap.CompressFormat saveFormat, int mode) {
        boolean result = false;
        File f = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(f);
            if (bitmap.compress(saveFormat, 100, out)) {
                result = true;
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static Bitmap loadBitmapFromFile(String packageName, String filename) {
        String path = new String("/data/data/" + packageName + "/files/" + filename);
        return loadBitmapFromFile(path);
    }

    public static Bitmap loadBitmapFromFile(String path) {
        FileInputStream fs = null;
        File f = new File(path);
        try {
            FileInputStream fs2 = new FileInputStream(f);
            fs = fs2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(fs);
            return bitmap;
        } finally {
            try {
                fs.close();
            } catch (IOException e2) {
            }
        }
    }

    public static void getModelWorldLocation(TFModel model, float[] location) {
        float[] modelLocation = new float[3];
        float[] holderLocation = new float[3];
        TFHolder holder = model.getHolder();
        if (holder != null) {
            holder.getLocation(holderLocation);
        }
        model.getLocation(modelLocation);
        location[0] = holderLocation[0] + modelLocation[0];
        location[1] = holderLocation[1] + modelLocation[1];
        location[2] = holderLocation[2] + modelLocation[2];
    }

    public static FloatBuffer newFloatBuffer(int count) {
        return ByteBuffer.allocateDirect(count * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static void checkGLError(GL10 gl) {
        int error = gl.glGetError();
        if (error != 0) {
            Log.e("checkGLError", "GL ERROR: " + GLU.gluErrorString(error));
        }
    }

    public static boolean panelIInternalHitTest(TFPanel panel, float left, float top, float width, float height) {
        float targetLeft = left * panel.getWidth();
        float targetWidth = width * panel.getWidth();
        float targetTop = top * panel.getHeight();
        float targetHeight = height * panel.getHeight();
        float targetLeft2 = targetLeft - (panel.getWidth() / 2.0f);
        float targetTop2 = (panel.getHeight() - targetTop) - (panel.getHeight() / 2.0f);
        float[] hitPoint = new float[4];
        panel.getHitPointOnModel(hitPoint, 0);
        float hitX = hitPoint[0];
        float hitY = hitPoint[1];
        return targetLeft2 <= hitX && hitX < targetLeft2 + targetWidth && targetTop2 - targetHeight <= hitY && hitY < targetTop2;
    }

    public static void dumpFreeMemory(String comment) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        int totalPrivate = memoryInfo.getTotalPrivateDirty();
        int totalPSS = memoryInfo.getTotalPss();
        int totalShared = memoryInfo.getTotalSharedDirty();
        Log.i(TAG, "### Memory Usage on '" + comment + "' - private : " + totalPrivate + ", PSS:" + totalPSS + ", shared:" + totalShared);
    }
}
