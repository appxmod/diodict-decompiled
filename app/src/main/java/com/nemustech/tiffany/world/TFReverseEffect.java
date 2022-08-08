package com.nemustech.tiffany.world;

import com.nemustech.tiffany.world.TFCustomPanel;
import java.util.Stack;

/* loaded from: classes.dex */
public class TFReverseEffect extends TFCustomPanel.Blender {
    public static final int DEFAULT_DURATION = 1500;
    public static final int DEFAULT_REVERSE_DURATION = 30;
    private static final String TAG = "TFReverseEffect";
    private final TFCustomPanel mCustomPanel;
    protected final float[] mIdentity;
    private final int mMeshH;
    private final int mMeshW;
    private float[] mVertex;
    private Stack<float[]> mVertexStack;
    private final TFCustomPanel.Time mTime = new TFCustomPanel.Time();
    protected int mReverseDuration = 30;
    protected int mDuration = 1500;

    public TFReverseEffect(TFCustomPanel customPanel, Stack<float[]> stack) {
        this.mCustomPanel = customPanel;
        this.mMeshW = this.mCustomPanel.getMeshWidth();
        this.mMeshH = this.mCustomPanel.getMeshHeight();
        this.mIdentity = new float[(this.mMeshH + 1) * (this.mMeshW + 1) * 3];
        this.mCustomPanel.loadIdentityVertex(this.mIdentity);
        this.mVertex = this.mCustomPanel.getVertex();
        this.mVertexStack = stack;
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public boolean hasEnded() {
        return this.mVertexStack.empty();
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onEnd() {
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onFrame(int tick) {
        if (this.mTime.update(tick) && !this.mVertexStack.empty()) {
            blend();
            this.mCustomPanel.requestUpdateVertex();
        }
    }

    private void blend() {
        float[] vertex = this.mVertexStack.pop();
        if (vertex != null) {
            System.arraycopy(vertex, 0, this.mVertex, 0, this.mVertex.length);
        }
    }

    @Override // com.nemustech.tiffany.world.TFCustomPanel.Blender
    public void onStart() {
        this.mTime.start(this.mDuration, this.mReverseDuration);
    }

    public void setReverseDuration(int duration, int reverseDuration) {
        if (reverseDuration <= 0 || reverseDuration >= duration) {
            reverseDuration = (duration * 3) / 4;
        }
        this.mDuration = duration;
        this.mReverseDuration = reverseDuration;
    }
}
