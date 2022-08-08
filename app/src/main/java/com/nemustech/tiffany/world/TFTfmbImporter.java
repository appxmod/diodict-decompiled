package com.nemustech.tiffany.world;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFTfmbImporter extends TFImporter {
    private static final String TAG = "TFTfmbImporter";

    public TFTfmbImporter(InputStream is) throws IOException {
        super(is);
    }

    private FloatBuffer loadFloats(DataInputStream r, int count) throws IOException {
        FloatBuffer buf = newFloatBuffer(count);
        for (int i = 0; i < count; i++) {
            buf.put(r.readFloat());
        }
        buf.position(0);
        return buf;
    }

    @Override // com.nemustech.tiffany.world.TFImporter
    public int importStream(InputStream is) throws IOException {
        Log.d(TAG, "START:" + is);
        DataInputStream r = new DataInputStream(new BufferedInputStream(is, TFImporter.BUFFER_SIZE));
        initialize();
        byte[] header = new byte[4];
        r.read(header);
        int vCount = r.readInt();
        int fCount = r.readInt();
        int fSize = r.readInt();
        int info = r.readInt();
        for (int i = 0; i < 6; i++) {
            this.mBoundBox[i] = r.readFloat();
        }
        this.mVertexBuffer = loadFloats(r, vCount * 3);
        if ((info & 1) == 1) {
            this.mColorBuffer = loadFloats(r, vCount * 4);
        }
        if ((info & 2) == 2) {
            this.mNormalBuffer = loadFloats(r, vCount * 3);
        }
        if ((info & 4) == 4) {
            this.mTextureBuffer = loadFloats(r, vCount * 2);
        }
        this.mIndexArray = new short[fSize];
        this.mIndexCount = fCount;
        for (int i2 = 0; i2 < fSize; i2++) {
            this.mIndexArray[i2] = r.readShort();
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
