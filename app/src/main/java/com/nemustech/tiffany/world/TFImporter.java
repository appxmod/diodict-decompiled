package com.nemustech.tiffany.world;

import com.diotek.diodict.utils.CMN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public abstract class TFImporter {
    public static final int BUFFER_SIZE = 4096;
    private static final String TAG = "TFImporter";
    public float[] mBoundBox = new float[6];
    public FloatBuffer mColorBuffer;
    public short[] mIndexArray;
    public ShortBuffer mIndexBuffer;
    public int mIndexCount;
    public FloatBuffer mNormalBuffer;
    public FloatBuffer mTextureBuffer;
    public FloatBuffer mVertexBuffer;

    public abstract void drawModel(GL10 gl10);

    public abstract int importStream(InputStream inputStream) throws IOException;

    public TFImporter(InputStream is) throws IOException {
        initialize();
        if (is != null) {
            importStream(is);
        }
    }

    public void initialize() {
        this.mVertexBuffer = null;
        this.mNormalBuffer = null;
        this.mColorBuffer = null;
        this.mTextureBuffer = null;
        this.mIndexBuffer = null;
        this.mIndexArray = null;
        this.mIndexCount = 0;
        this.mBoundBox[0] = Float.POSITIVE_INFINITY;
        this.mBoundBox[1] = Float.NEGATIVE_INFINITY;
        this.mBoundBox[2] = Float.POSITIVE_INFINITY;
        this.mBoundBox[3] = Float.NEGATIVE_INFINITY;
        this.mBoundBox[4] = Float.POSITIVE_INFINITY;
        this.mBoundBox[5] = Float.NEGATIVE_INFINITY;
    }

    public boolean canDraw() {
        return this.mVertexBuffer != null;
    }

    public void updateBoundBox(float x, float y, float z) {
        this.mBoundBox[0] = x < this.mBoundBox[0] ? x : this.mBoundBox[0];
        float[] fArr = this.mBoundBox;
        if (x <= this.mBoundBox[1]) {
            x = this.mBoundBox[1];
        }
        fArr[1] = x;
        this.mBoundBox[2] = y < this.mBoundBox[2] ? y : this.mBoundBox[2];
        float[] fArr2 = this.mBoundBox;
        if (y <= this.mBoundBox[3]) {
            y = this.mBoundBox[3];
        }
        fArr2[3] = y;
        this.mBoundBox[4] = z < this.mBoundBox[4] ? z : this.mBoundBox[4];
        float[] fArr3 = this.mBoundBox;
        if (z <= this.mBoundBox[5]) {
            z = this.mBoundBox[5];
        }
        fArr3[5] = z;
    }

    /* loaded from: classes.dex */
    public static class LineSplit {
        public static final int MAX_LINE_SIZE = 256;
        public static final int MAX_TOKEN_COUNT = 32;
        protected InputStream mReader;
        protected byte[] mLine = new byte[256];
        protected int mPosInLine = 0;
        protected int mUnread = -1;
        protected boolean mEOF = false;
        protected int mLineCount = 0;
        protected boolean mSkipEmptyLine = true;
        protected int[] mTokenStart = new int[32];
        protected int[] mTokenEnd = new int[32];
        protected int mCommentStart = -1;
        protected int mTokenCount = 0;

        public LineSplit(InputStream is) {
            this.mReader = is;
        }

        protected void setLineEmpty() {
            this.mPosInLine = 0;
        }

        protected int getLineLength() {
            return this.mPosInLine;
        }

        protected void appendToLine(int ch) {
            if (this.mPosInLine + 1 >= 256) {
                throw new IndexOutOfBoundsException("line overflow");
            }
            byte[] bArr = this.mLine;
            int i = this.mPosInLine;
            this.mPosInLine = i + 1;
            bArr[i] = (byte) ch;
        }

        protected byte charAt(int index) {
            if (index < 0 || index >= this.mPosInLine) {
                throw new IndexOutOfBoundsException();
            }
            return this.mLine[index];
        }

        protected int read() throws IOException {
            if (this.mUnread < 0) {
                return this.mReader.read();
            }
            int i = this.mUnread;
            this.mUnread = -1;
            return i;
        }

        protected void unread(int ch) {
            this.mUnread = ch;
        }

        protected void readLine() throws IOException {
            setLineEmpty();
            while (true) {
                int ch = read();
                if (ch < 0) {
                    if (getLineLength() > 0) {
                        break;
                    }
                    this.mEOF = true;
                    return;
                }
                appendToLine(ch);
                if (ch == 10) {
                    break;
                }
            }
            this.mLineCount++;
        }

        protected boolean isSpace(byte ch) {
            return ch == 32 || ch == 9 || ch == 13 || ch == 10;
        }

        protected boolean isCommentChar(byte ch) {
            return ch == 35;
        }

        protected void parseLine() {
            this.mCommentStart = -1;
            this.mTokenCount = 0;
            int len = getLineLength();
            int pos = 0;
            while (pos < len) {
                while (true) {
                    if (pos >= len) {
                        break;
                    }
                    byte ch = charAt(pos);
                    if (isCommentChar(ch)) {
                        this.mCommentStart = pos;
                        pos = len;
                        break;
                    } else if (!isSpace(ch)) {
                        break;
                    } else {
                        pos++;
                    }
                }
                if (pos < len) {
                    this.mTokenStart[this.mTokenCount] = pos;
                    while (pos < len && !isSpace(charAt(pos))) {
                        pos++;
                    }
                    this.mTokenEnd[this.mTokenCount] = pos;
                    this.mTokenCount++;
                } else {
                    return;
                }
            }
        }

        public boolean eof() {
            return this.mEOF;
        }

        public void nextLine() throws IOException {
            readLine();
            parseLine();
            if (this.mSkipEmptyLine) {
                while (!eof() && getTokenCount() == 0) {
                    readLine();
                    parseLine();
                }
            }
        }

        public int getLineCount() {
            return this.mLineCount;
        }

        public int getTokenCount() {
            return this.mTokenCount;
        }

        public String getTokenString(int tokenIndex) {
            if (tokenIndex < 0 || tokenIndex >= this.mTokenCount) {
                throw new IndexOutOfBoundsException();
            }
            int start = this.mTokenStart[tokenIndex];
            int len = this.mTokenEnd[tokenIndex] - start;
            return new String(this.mLine, start, len);
        }

        public int getTokenInt(int tokenIndex) {
            if (tokenIndex < 0 || tokenIndex >= this.mTokenCount) {
                throw new IndexOutOfBoundsException();
            }
            return parseInt(this.mLine, this.mTokenStart[tokenIndex], this.mTokenEnd[tokenIndex]);
        }

        public float getTokenFloat(int tokenIndex) {
            if (tokenIndex < 0 || tokenIndex >= this.mTokenCount) {
                throw new IndexOutOfBoundsException();
            }
            return parseFloat(this.mLine, this.mTokenStart[tokenIndex], this.mTokenEnd[tokenIndex]);
        }

        public boolean compareToken(int tokenIndex, String str) {
            if (tokenIndex < 0 || tokenIndex >= this.mTokenCount) {
                throw new IndexOutOfBoundsException();
            }
            int pos = this.mTokenStart[tokenIndex];
            int len = this.mTokenEnd[tokenIndex] - pos;
            if (len != str.length()) {
                return false;
            }
            for (int i = 0; i < len; i++) {
                if (((char) this.mLine[pos]) != str.charAt(i)) {
                    return false;
                }
                pos++;
            }
            return true;
        }

        protected static int parseInt(byte[] line, int pos, int end) {
            boolean minus = false;
            int ret = 0;
            if (line[pos] == 45) {
                minus = true;
                pos++;
            }
            while (pos < end) {
                int ch = line[pos];
                if (ch >= 48 && ch <= 57) {
                    ret = (ret * 10) + (ch - 48);
                    pos++;
                } else {
                    throw new NumberFormatException();
                }
            }
            if (minus) {
                return -ret;
            }
            return ret;
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x002c  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x004b  */
        /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected static float parseFloat(byte[] line, int pos, int end) {
			CMN.debug("fatal parseFloat!"+new String(line)+" "+pos);
			//throw new UnsupportedOperationException("Method not decompiled: com.nemustech.tiffany.world.TFImporter.LineSplit.parseFloat(byte[], int, int):float");
			boolean minus = false;
			float ret = 0.0f;
			if (line[pos] == 45) {
				minus = true;
				pos++;
			}
			while (pos < end) {
				int ch = line[pos];
				if (ch >= 48 && ch <= 57) {
					ret = (10.0f * ret) + (ch - 48);
					pos++;
				} else {
					if (ch != 46) {
						throw new NumberFormatException();
					}
					int div = 10;
					int pos2;
					for (pos2 = pos + 1; pos2 < end; pos2++) {
						int ch2 = line[pos2];
						if (ch2 >= 48 && ch2 <= 57) {
							ret += (ch2 - 48) / div;
							div *= 10;
						} else {
							throw new NumberFormatException();
						}
					}
					if (!minus) {
						return -ret;
					}
					return ret;
				}
			}
			return ret;
		}

        public void setSkipEmptyLine(boolean skip) {
            this.mSkipEmptyLine = skip;
        }

        public boolean getSkipEmptyLine() {
            return this.mSkipEmptyLine;
        }
    }

    /* loaded from: classes.dex */
    public static class ExpandFloatArray {
        public static final int ITEM_PER_LINE = 1024;
        public static final int LINE_INCREASE = 256;
        public float[][] mLines = null;
        public int mCount = 0;

        public ExpandFloatArray() {
            expandLines();
        }

        public void add(float value) {
            add(-1, value);
        }

        public void add(int index, float value) {
            if (index == -1) {
                index = this.mCount;
            }
            if (index < 0 || index > this.mCount) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int line = index / 1024;
            int indexInLine = index % 1024;
            if (line >= this.mLines.length) {
                expandLines();
            }
            if (this.mLines[line] == null) {
                this.mLines[line] = new float[1024];
            }
            this.mLines[line][indexInLine] = value;
            this.mCount = index + 1;
        }

        public int size() {
            return this.mCount;
        }

        public float[] toArray() {
            float[] ret = new float[this.mCount];
            int line = 0;
            int indexInLine = 0;
            for (int i = 0; i < this.mCount; i++) {
                int indexInLine2 = indexInLine + 1;
                ret[i] = this.mLines[line][indexInLine];
                if (indexInLine2 >= 1024) {
                    line++;
                    indexInLine = 0;
                } else {
                    indexInLine = indexInLine2;
                }
            }
            return ret;
        }

        protected void expandLines() {
			CMN.debug("expandLines::");
            float[][] old = this.mLines;
            int len = old != null ? old.length : 0;
            this.mLines = new float[len + 256][];
            if (old != null) {
                System.arraycopy(old, 0, this.mLines, 0, len);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ExpandIntArray {
        public static final int ITEM_PER_LINE = 1024;
        public static final int LINE_INCREASE = 256;
        public int[][] mLines = null;
        public int mCount = 0;

        public ExpandIntArray() {
            expandLines();
        }

        public void add(int value) {
            add(-1, value);
        }

        public void add(int index, int value) {
            if (index == -1) {
                index = this.mCount;
            }
            if (index < 0 || index > this.mCount) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int line = index / 1024;
            int indexInLine = index % 1024;
            if (line >= this.mLines.length) {
                expandLines();
            }
            if (this.mLines[line] == null) {
                this.mLines[line] = new int[1024];
            }
            this.mLines[line][indexInLine] = value;
            this.mCount = index + 1;
        }

        public int size() {
            return this.mCount;
        }

        public int[] toArray() {
            int[] ret = new int[this.mCount];
            int line = 0;
            int indexInLine = 0;
            for (int i = 0; i < this.mCount; i++) {
                int indexInLine2 = indexInLine + 1;
                ret[i] = this.mLines[line][indexInLine];
                if (indexInLine2 >= 1024) {
                    line++;
                    indexInLine = 0;
                } else {
                    indexInLine = indexInLine2;
                }
            }
            return ret;
        }

        protected void expandLines() {
			CMN.debug("expandLines::");
            int[][] old = this.mLines;
            int len = old != null ? old.length : 0;
            this.mLines = new int[len + 256][];
            if (old != null) {
                System.arraycopy(old, 0, this.mLines, 0, len);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ExpandShortArray {
        public static final int ITEM_PER_LINE = 1024;
        public static final int LINE_INCREASE = 256;
        public short[][] mLines = null;
        public int mCount = 0;

        public ExpandShortArray() {
            expandLines();
        }

        public void add(short value) {
            add(-1, value);
        }

        public void add(int index, short value) {
            if (index == -1) {
                index = this.mCount;
            }
            if (index < 0 || index > this.mCount) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int line = index / 1024;
            int indexInLine = index % 1024;
            if (line >= this.mLines.length) {
                expandLines();
            }
            if (this.mLines[line] == null) {
                this.mLines[line] = new short[1024];
            }
            this.mLines[line][indexInLine] = value;
            this.mCount = index + 1;
        }

        public int size() {
            return this.mCount;
        }

        public short[] toArray() {
            short[] ret = new short[this.mCount];
            int line = 0;
            int indexInLine = 0;
            for (int i = 0; i < this.mCount; i++) {
                int indexInLine2 = indexInLine + 1;
                ret[i] = this.mLines[line][indexInLine];
                if (indexInLine2 >= 1024) {
                    line++;
                    indexInLine = 0;
                } else {
                    indexInLine = indexInLine2;
                }
            }
            return ret;
        }

        protected void expandLines() {
			CMN.debug("expandLines::");
            short[][] old = this.mLines;
            int len = old != null ? old.length : 0;
            this.mLines = new short[len + 256][];
            if (old != null) {
                System.arraycopy(old, 0, this.mLines, 0, len);
            }
        }
    }

    protected BufferedReader newBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FloatBuffer newFloatBuffer(int size) {
        return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ShortBuffer newShortBuffer(int size) {
        return ByteBuffer.allocateDirect(size * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
    }
}
