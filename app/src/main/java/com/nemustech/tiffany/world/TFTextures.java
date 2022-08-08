package com.nemustech.tiffany.world;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLUtils;
import android.util.Log;
import com.nemustech.tiffany.world.TFTextureInfo;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class TFTextures {
    static final String TAG = "TFTextures";
    private static Rect mRect = new Rect();
    private int mMaxTextureIndex = -1;
    private TFModel mModel;
    private int mTextureCount;
    private TFTextureInfo[] mTextureInfoArray;

    public TFTextures(TFModel model, int numFaces) {
        this.mModel = model;
        setNumFaces(numFaces);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNumFaces(int numFaces) {
        this.mTextureInfoArray = new TFTextureInfo[numFaces];
    }

    private void removeTexture(TFTextureInfo texInfo, boolean clearBitmapCache) {
        int layerCount = texInfo.getLayerCount();
        for (int i = 0; i < layerCount; i++) {
            removeTexture(texInfo, clearBitmapCache, i);
        }
    }

    private void removeTexture(TFTextureInfo texInfo, boolean clearBitmapCache, int layerIndex) {
        TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(layerIndex);
        if (layer == null) {
            throw new IllegalArgumentException("Invalid texture layer index");
        }
        if (layer.bTexturized && layer.owner == this.mModel) {
            if (layer.magicKey == this.mModel.mWorld.mTextureMagicKey) {
                final int texture_name = layer.texture_name;
                final GL10 gl = this.mModel.mWorld.mRenderer.getGLContext();
                Runnable textureEraser = new Runnable() { // from class: com.nemustech.tiffany.world.TFTextures.1
                    @Override // java.lang.Runnable
                    public void run() {
                        int[] texName = {texture_name};
                        gl.glDeleteTextures(1, texName, 0);
                    }
                };
                this.mModel.getWorld().queueEvent(textureEraser);
            } else {
                Log.w(TAG, "removeTexture was ignored since magic key didn't match");
            }
            layer.bTexturized = false;
        }
        if (clearBitmapCache && layer.bmp != null) {
            layer.bmp.recycle();
            layer.bmp = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMaxTextureIndex() {
        return this.mMaxTextureIndex;
    }

    private void loadTexture(TFTextureInfo.TFTextureLayer layer, int faceIndex) {
        if (layer.bmp == null) {
            if (layer.resource_id != -1) {
                layer.bmp = TFUtils.decodeResource(layer.resources, layer.resource_id);
            } else if (layer.fileName != null) {
                layer.bmp = TFUtils.decodeFile(layer.fileName);
            } else if (layer.owner.mJitImageProvider != null) {
                layer.bmp = layer.owner.mJitImageProvider.getImage(faceIndex);
            } else if (layer.owner.getParentHolder() != null && layer.owner.getParentHolder().getItemProvider() != null && layer.owner.getParentHolder().getItemProvider().getJitImageProvider() != null) {
                layer.bmp = layer.owner.getParentHolder().getItemProvider().getJitImageProvider().getImage(layer.owner.mItemIndex, 0);
                Log.i(TAG, "load texture through jit provider");
            } else {
                Log.e(TAG, "Loading texture failed due to corrupted texture info");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void genTexture(GL10 gl) {
        int size = this.mTextureInfoArray.length;
        for (int i = 0; i < size; i++) {
            TFTextureInfo texInfo = this.mTextureInfoArray[i];
            if (texInfo != null) {
                int layerCount = texInfo.getLayerCount();
                for (int j = 0; j < layerCount; j++) {
                    setTextureByIndex(gl, i, j);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setTextureInfo(GL10 gl, TFTextureInfo texInfo, int textureFilter, int layerIndex, int magicKey) {
        TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(layerIndex);
        if (layer.bmp != null) {
            if (!layer.bTexturized) {
                int[] textureNameVessel = new int[1];
                gl.glGenTextures(1, textureNameVessel, 0);
                layer.texture_name = textureNameVessel[0];
                layer.bTexturized = true;
                layer.magicKey = magicKey;
                gl.glBindTexture(3553, layer.texture_name);
                if (texInfo.getCoordRatioWidth(layerIndex) != 1.0f || texInfo.getCoordRatioHeight(layerIndex) != 1.0f) {
                    int format = GLUtils.getInternalFormat(layer.bmp);
                    int width = layer.getWidth(true);
                    int height = layer.getHeight(true);
                    if (format == 35734 || (format == 6407 && layer.getWidth(false) % 2 != 0)) {
                        Bitmap bmp = layer.bmp.copy(Bitmap.Config.ARGB_8888, false);
                        int format2 = GLUtils.getInternalFormat(bmp);
                        int type = GLUtils.getType(bmp);
                        Log.d(TAG, "format: " + format2 + ", type: " + type);
                        gl.glTexImage2D(3553, 0, format2, width, height, 0, format2, type, null);
                        GLUtils.texSubImage2D(3553, 0, 0, 0, bmp, format2, type);
                        bmp.recycle();
                    } else {
                        int type2 = GLUtils.getType(layer.bmp);
                        gl.glTexImage2D(3553, 0, format, width, height, 0, format, type2, null);
                        GLUtils.texSubImage2D(3553, 0, 0, 0, layer.bmp, format, type2);
                    }
                } else {
                    GLUtils.texImage2D(3553, 0, layer.bmp, 0);
                }
                gl.glTexParameterx(3553, 10241, textureFilter);
                gl.glTexParameterx(3553, 10240, textureFilter);
                gl.glTexParameterx(3553, 10242, 33071);
                gl.glTexParameterx(3553, 10243, 33071);
                return;
            }
            throw new IllegalArgumentException("setTextureInfo with already texturized info");
        }
        throw new IllegalArgumentException("setTextureInfo with null bmp");
    }

    private void showLoadingIcon(GL10 gl, int index) {
        if (this.mModel != null && this.mModel.mWorld != null) {
            TFTextureInfo.TFTextureLayer layer = this.mModel.mWorld.mDefaultDelayImageTextureInfo.getLayer(0);
            Rect rect = mRect;
            mRect.top = 0;
            rect.left = 0;
            mRect.right = layer.getWidth(false);
            mRect.bottom = layer.getHeight(false);
            this.mModel.adjustTextureCoordination(mRect, index, layer.getWidth(true), layer.getHeight(true));
            gl.glBindTexture(3553, layer.texture_name);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTextureByIndex(GL10 gl, int index, int layerIndex) {
        int textureFilter;
        TFTextureInfo texInfo = this.mTextureInfoArray[index];
        if (texInfo != null) {
            TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(layerIndex);
            if (layer.bTexturized) {
                if (layer.magicKey == this.mModel.mWorld.mTextureMagicKey) {
                    gl.glBindTexture(3553, layer.texture_name);
                    return;
                }
                layer.bTexturized = false;
                loadTexture(layer, index);
                setTextureByIndex(gl, index, layerIndex);
                return;
            } else if (layer.bmp != null) {
                if (this.mModel.mTextureFilter != null) {
                    textureFilter = this.mModel.mTextureFilter[0];
                } else {
                    textureFilter = this.mModel.mWorld.mTextureFilter;
                }
                if (!layer.bCalcCoord) {
                    layer.bCalcCoord = true;
                    layer.setWidth(layer.bmp.getWidth());
                    layer.setHeight(layer.bmp.getHeight());
                    setTextureInfo(gl, texInfo, textureFilter, layerIndex, this.mModel.mWorld.mTextureMagicKey);
                    Rect rect = mRect;
                    mRect.top = 0;
                    rect.left = 0;
                    mRect.right = layer.getWidth(false);
                    mRect.bottom = layer.getHeight(false);
                    this.mModel.adjustTextureCoordination(mRect, index, layer.getWidth(true), layer.getHeight(true));
                } else {
                    setTextureInfo(gl, texInfo, textureFilter, layerIndex, this.mModel.mWorld.mTextureMagicKey);
                }
                TFHolder holder = layer.owner.getParentHolder();
                TFItemProvider itemProvider = null;
                if (holder != null) {
                    itemProvider = holder.getItemProvider();
                }
                if ((layer.resource_id != -1 || layer.fileName != null || layer.owner.mJitImageProvider != null || (itemProvider != null && itemProvider.getJitImageProvider() != null)) && layer.bmp != null && !this.mModel.getImageCacheLockStatus()) {
                    layer.bmp.recycle();
                    layer.bmp = null;
                    return;
                }
                return;
            } else {
                loadTexture(layer, index);
                if (layer.bmp == null) {
                    Log.e(TAG, "Load texture fail on " + this.mModel + " description:" + this.mModel.mDescription);
                    return;
                } else {
                    setTextureByIndex(gl, index, layerIndex);
                    return;
                }
            }
        }
        if (layerIndex == 0) {
            showLoadingIcon(gl, index);
        }
        Log.w(TAG, "setTextureByIndex with null texInfo");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTextureByIndex(GL10 gl, int index) {
        setTextureByIndex(gl, index, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTextureCount() {
        return this.mTextureCount;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFTextureInfo getTextureInfo(int index) {
        return this.mTextureInfoArray[index];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deleteImageResource(int index) {
        TFTextureInfo texInfo = this.mTextureInfoArray[index];
        if (texInfo != null) {
            removeTexture(texInfo, !this.mModel.getImageCacheLockStatus());
            this.mTextureInfoArray[index] = null;
            this.mTextureCount--;
            if (index == this.mMaxTextureIndex) {
                int i = this.mMaxTextureIndex - 1;
                while (i >= 0 && this.mTextureInfoArray[i] == null) {
                    i--;
                }
                this.mMaxTextureIndex = i;
            }
        }
    }

    private void putTextureInfo(int index, TFTextureInfo info) {
        if (index > this.mMaxTextureIndex) {
            this.mMaxTextureIndex = index;
            this.mTextureCount++;
        } else {
            TFTextureInfo texInfo = this.mTextureInfoArray[index];
            if (texInfo != null) {
                int layerCountOld = texInfo.getLayerCount();
                int layerCountNew = info.getLayerCount();
                for (int i = 0; i < layerCountOld; i++) {
                    boolean bClearImageCache = !this.mModel.getImageCacheLockStatus();
                    if (bClearImageCache) {
                        int j = 0;
                        while (true) {
                            if (j >= layerCountNew) {
                                break;
                            } else if (info.mLayer[j].bmp == null || info.mLayer[j].bmp != texInfo.mLayer[i].bmp) {
                                j++;
                            } else {
                                bClearImageCache = false;
                                break;
                            }
                        }
                    }
                    removeTexture(texInfo, bClearImageCache, i);
                }
            } else {
                this.mTextureCount++;
            }
        }
        this.mTextureInfoArray[index] = info;
        if (this.mModel.mWorld != null) {
            this.mModel.mWorld.requestRender();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImageResource(int index, String fileName) {
        TFTextureInfo texInfo = new TFTextureInfo();
        TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(0);
        layer.fileName = fileName;
        layer.owner = this.mModel;
        putTextureInfo(index, texInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImageResource(int index, Resources resources, int resource_id) {
        TFTextureInfo texInfo = new TFTextureInfo();
        TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(0);
        layer.resource_id = resource_id;
        layer.resources = resources;
        layer.owner = this.mModel;
        putTextureInfo(index, texInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setImageResource(int index, Bitmap bmp, Rect rectSet) {
        TFTextureInfo texInfo = createTextureInfo(bmp, rectSet);
        TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(0);
        layer.owner = this.mModel;
        putTextureInfo(index, texInfo);
        return layer.bmp != bmp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TFTextureInfo createTextureInfo(Bitmap bmp, Rect rectSet) {
        TFTextureInfo texInfo = new TFTextureInfo();
        TFTextureInfo.TFTextureLayer layer = texInfo.getLayer(0);
        if (rectSet == null) {
            layer.bmp = bmp;
        } else {
            layer.bmp = Bitmap.createBitmap(bmp, rectSet.left, rectSet.top, rectSet.width(), rectSet.height());
        }
        return texInfo;
    }

    public void swapFaces() {
        TFTextureInfo tmp = this.mTextureInfoArray[0];
        this.mTextureInfoArray[0] = this.mTextureInfoArray[1];
        this.mTextureInfoArray[1] = tmp;
    }
}
