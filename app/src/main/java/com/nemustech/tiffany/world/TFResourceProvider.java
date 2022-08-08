package com.nemustech.tiffany.world;

import android.content.res.Resources;

/* loaded from: classes.dex */
public class TFResourceProvider extends TFContentProvider<Integer> {
    private String TAG;
    private Resources mResources;

    public TFResourceProvider(Resources resources) {
        this.TAG = "ResourceProvider";
        this.mResources = resources;
    }

    public TFResourceProvider(Resources resources, int[] list) {
        this(resources);
        setResourceList(list);
    }

    public void setResourceList(int[] list) {
        for (int i : list) {
            addItem(Integer.valueOf(i));
        }
    }

    @Override // com.nemustech.tiffany.world.TFContentProvider
    protected void applyContent(TFObject object, int itemIndex) {
        ((TFModel) object).setImageResource(0, this.mResources, getItem(itemIndex).intValue());
    }
}
