package com.diotek.diodict.engine;

import java.text.CollationKey;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ICUCollator.java */
/* loaded from: classes.dex */
public class StringKey implements Comparable<StringKey> {
    CollationKey key;
    String s;

    public StringKey(String s, CollationKey key) {
        this.s = s;
        this.key = key;
    }

    @Override // java.lang.Comparable
    public int compareTo(StringKey obj) {
        return this.key.compareTo(obj.key);
    }

    public String toString() {
        return this.s;
    }
}
