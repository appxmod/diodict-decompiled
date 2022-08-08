package com.nemustech.tiffany.world;

import android.content.res.Resources;
import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class TFTextureInfo {
    static final int MAX_LAYER_COUNT = 2;
    private int mLayerCount = 0;
    public TFTextureLayer[] mLayer = new TFTextureLayer[2];

    public TFTextureInfo() {
        TFTextureLayer defaultLayer = new TFTextureLayer();
        addLayer(defaultLayer);
    }

    /* loaded from: classes.dex */
    public class TFTextureLayer {
        boolean bCalcCoord;
        boolean bTexturized;
        Bitmap bmp;
        private float coordRatioHeight;
        private float coordRatioWidth;
        String fileName;
        private int height;
        int magicKey;
        TFModel owner;
        private int quantitated_height;
        private int quantitated_width;
        int resource_id = -1;
        Resources resources;
        int texture_name;
        private int width;

        public TFTextureLayer() {
        }

        public int getWidth(boolean quantitated) {
            return quantitated ? this.quantitated_width : this.width;
        }

        public int getHeight(boolean quantitated) {
            return quantitated ? this.quantitated_height : this.height;
        }

        public void setWidth(int w) {
            this.width = w;
            this.quantitated_width = TFUtils.getTextureLength(w);
            this.coordRatioWidth = this.width / this.quantitated_width;
        }

        public void setHeight(int h) {
            this.height = h;
            this.quantitated_height = TFUtils.getTextureLength(h);
            this.coordRatioHeight = this.height / this.quantitated_height;
        }
    }

    public void setLayer(int index, TFTextureLayer layerInfo) {
        if (index >= this.mLayerCount) {
            throw new IllegalArgumentException("Layer index exceeded");
        }
        this.mLayer[index] = layerInfo;
    }

    public void addLayer(TFTextureLayer layerInfo) {
        this.mLayer[this.mLayerCount] = layerInfo;
        this.mLayerCount++;
    }

    public void deleteLayer(int index) {
        this.mLayer[index] = null;
        this.mLayerCount--;
    }

    public void insertLayer(int index, TFTextureLayer layerInfo) {
        if (getLayerCount() >= getMaxLayerCount()) {
            throw new IllegalArgumentException("Layer index exceeded");
        }
        for (int i = 0; i >= index; i--) {
            this.mLayer[i + 1] = this.mLayer[i];
        }
        this.mLayer[index] = layerInfo;
        this.mLayerCount++;
    }

    public TFTextureLayer getLayer(int index) {
        return this.mLayer[index];
    }

    public int getLayerCount() {
        return this.mLayerCount;
    }

    public int getMaxLayerCount() {
        return 2;
    }

    public Bitmap getBitmap() {
        return this.mLayer[0].bmp;
    }

    public float getCoordRatioWidth(int layerIndex) {
        return this.mLayer[layerIndex].coordRatioWidth;
    }

    public float getCoordRatioHeight(int layerIndex) {
        return this.mLayer[layerIndex].coordRatioHeight;
    }
}
