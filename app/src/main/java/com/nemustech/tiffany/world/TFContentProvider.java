package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public abstract class TFContentProvider<T> extends TFItemProvider<T> {
    private String TAG = "TFImageProvider";

    protected abstract void applyContent(TFObject tFObject, int i);

    @Override // com.nemustech.tiffany.world.TFItemProvider
    public void setItem(TFObjectContainer objectContainer, int itemIndex) {
        if (objectContainer.getObject() != null && getItemCount() != 0) {
            TFModel model = (TFModel) objectContainer.getObject();
            model.deleteAllImageResource();
            int index = itemIndex % getItemCount();
            applyContent(objectContainer.getObject(), index);
        }
    }
}
