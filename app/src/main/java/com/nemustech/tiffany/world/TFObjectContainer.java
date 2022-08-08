package com.nemustech.tiffany.world;

import java.util.Comparator;

/* loaded from: classes.dex */
public class TFObjectContainer implements Cloneable {
    static final Comparator<TFObjectContainer> compareAxisZ = new Comparator<TFObjectContainer>() { // from class: com.nemustech.tiffany.world.TFObjectContainer.1
        @Override // java.util.Comparator
        public int compare(TFObjectContainer obj1, TFObjectContainer obj2) {
            float distance = obj1.getObject().mLocation[2] - obj2.getObject().mLocation[2];
            if (distance > 0.0f) {
                return 1;
            }
            if (distance == 0.0f) {
                return 0;
            }
            return -1;
        }
    };
    private TFObject mObject;

    public TFObjectContainer() {
        this.mObject = null;
        this.mObject = null;
    }

    public TFObjectContainer(TFObject object) {
        this.mObject = null;
        setObject(object);
    }

    public Object clone() {
        try {
            TFObjectContainer oc = (TFObjectContainer) super.clone();
            if (this.mObject != null) {
                oc.mObject = (TFObject) this.mObject.clone();
            } else {
                oc.mObject = null;
            }
            return oc;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TFObject getObject() {
        return this.mObject;
    }

    public void setObject(TFObject object) {
        this.mObject = object;
        if (this.mObject != null) {
            this.mObject.setWrapperObject(this);
        }
    }

    public void clearObject() {
        if (this.mObject != null) {
            this.mObject.setWrapperObject(null);
            this.mObject = null;
        }
    }
}
