package com.nemustech.tiffany.world;

import android.util.Log;
import com.nemustech.tiffany.world.TFImporter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFTfmImporter extends TFImporter {
    private static final String TAG = "TFTfmImporter";

    public TFTfmImporter(InputStream is) throws IOException {
        super(is);
    }

    @Override // com.nemustech.tiffany.world.TFImporter
    public int importStream(InputStream is) throws IOException {
        Log.d(TAG, "START:" + is);
        BufferedInputStream r = new BufferedInputStream(is, TFImporter.BUFFER_SIZE);
        TFImporter.LineSplit l = new TFImporter.LineSplit(r);
        initialize();
        l.nextLine();
        if (!l.getTokenString(0).equals("TFM")) {
            throw new IOException("not .tfm file");
        }
        l.nextLine();
        if (l.eof() || l.getTokenCount() < 3) {
            throw new IOException("invalid count");
        }
        int vCount = l.getTokenInt(0);
        int fCount = l.getTokenInt(1);
        int fSize = l.getTokenInt(2);
        boolean useColor = false;
        boolean useNormal = false;
        boolean useTexCoord = false;
        for (int i = 3; i < l.getTokenCount(); i++) {
            if (l.getTokenString(i).equals("C")) {
                useColor = true;
            } else if (l.getTokenString(i).equals("N")) {
                useNormal = true;
            } else if (l.getTokenString(i).equals("T")) {
                useTexCoord = true;
            } else {
                throw new IOException("invalid type");
            }
        }
        l.nextLine();
        if (l.eof() || l.getTokenCount() != 6) {
            throw new IOException("invalid BoundBox");
        }
        for (int i2 = 0; i2 < 6; i2++) {
            this.mBoundBox[i2] = l.getTokenFloat(i2);
        }
        l.nextLine();
        this.mVertexBuffer = newFloatBuffer(vCount * 3);
        if (useColor) {
            this.mColorBuffer = newFloatBuffer(vCount * 4);
        }
        if (useNormal) {
            this.mNormalBuffer = newFloatBuffer(vCount * 3);
        }
        if (useTexCoord) {
            this.mTextureBuffer = newFloatBuffer(vCount * 2);
        }
        for (int i3 = 0; i3 < vCount; i3++) {
            int p = 0 + 1;
            this.mVertexBuffer.put(l.getTokenFloat(0));
            int p2 = p + 1;
            this.mVertexBuffer.put(l.getTokenFloat(p));
            int p3 = p2 + 1;
            this.mVertexBuffer.put(l.getTokenFloat(p2));
            if (useColor) {
                int p4 = p3 + 1;
                this.mColorBuffer.put(l.getTokenFloat(p3));
                int p5 = p4 + 1;
                this.mColorBuffer.put(l.getTokenFloat(p4));
                int p6 = p5 + 1;
                this.mColorBuffer.put(l.getTokenFloat(p5));
                p3 = p6 + 1;
                this.mColorBuffer.put(l.getTokenFloat(p6));
            }
            if (useNormal) {
                int p7 = p3 + 1;
                this.mNormalBuffer.put(l.getTokenFloat(p3));
                int p8 = p7 + 1;
                this.mNormalBuffer.put(l.getTokenFloat(p7));
                this.mNormalBuffer.put(l.getTokenFloat(p8));
                p3 = p8 + 1;
            }
            if (useTexCoord) {
                int p9 = p3 + 1;
                this.mTextureBuffer.put(l.getTokenFloat(p3));
                p3 = p9 + 1;
                this.mTextureBuffer.put(l.getTokenFloat(p9));
            }
            l.nextLine();
        }
        this.mVertexBuffer.position(0);
        if (useColor) {
            this.mColorBuffer.position(0);
        }
        if (useNormal) {
            this.mNormalBuffer.position(0);
        }
        if (useTexCoord) {
            this.mTextureBuffer.position(0);
        }
        this.mIndexArray = new short[fSize];
        this.mIndexCount = fCount;
        int pos = 0;
        for (int i4 = 0; i4 < fCount; i4++) {
            int count = l.getTokenInt(0);
            this.mIndexArray[pos] = (short) count;
            int j = 0;
            pos++;
            while (j < count) {
                this.mIndexArray[pos] = (short) l.getTokenInt(j + 1);
                j++;
                pos++;
            }
            l.nextLine();
        }
        this.mIndexBuffer = newShortBuffer(fSize);
        this.mIndexBuffer.put(this.mIndexArray);
        this.mIndexBuffer.position(0);
        Log.d(TAG, "END");
        return 0;
    }

    @Override // com.nemustech.tiffany.world.TFImporter
    public void drawModel(GL10 gl) {
        int i = 0;
        int len = this.mIndexArray.length;
        while (i < len) {
            gl.glDrawElements(6, this.mIndexArray[i], 5123, this.mIndexBuffer.position(i + 1));
            i += this.mIndexArray[i] + 1;
        }
    }
}
