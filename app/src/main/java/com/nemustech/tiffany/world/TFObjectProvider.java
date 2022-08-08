package com.nemustech.tiffany.world;

/* loaded from: classes.dex */
public class TFObjectProvider extends TFItemProvider<TFObject> {
    private String TAG = "TFObjectProvider";

    @Override // com.nemustech.tiffany.world.TFItemProvider
    public void setItem(TFObjectContainer objectContainer, int itemIndex) {
        if (getItemCount() > 0) {
            int index = itemIndex % getItemCount();
            objectContainer.setObject(getItem(index));
            return;
        }
        objectContainer.setObject(null);
    }
}
