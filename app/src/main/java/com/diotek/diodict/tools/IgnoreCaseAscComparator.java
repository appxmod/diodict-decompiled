package com.diotek.diodict.tools;

import android.util.Pair;
import java.util.Comparator;

/* loaded from: classes.dex */
public class IgnoreCaseAscComparator implements Comparator<Pair<String, Integer>> {
    @Override // java.util.Comparator
    public int compare(Pair<String, Integer> object1, Pair<String, Integer> object2) {
        return ((String) object1.first).compareToIgnoreCase((String) object2.first);
    }
}
