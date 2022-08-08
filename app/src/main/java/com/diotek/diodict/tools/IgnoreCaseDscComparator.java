package com.diotek.diodict.tools;

import android.util.Pair;
import java.util.Comparator;

/* loaded from: classes.dex */
public class IgnoreCaseDscComparator implements Comparator<Pair<String, Integer>> {
    @Override // java.util.Comparator
    public int compare(Pair<String, Integer> object1, Pair<String, Integer> object2) {
        int result = ((String) object1.first).compareToIgnoreCase((String) object2.first);
        if (result > 0) {
            return -1;
        }
        if (result < 0) {
            return 1;
        }
        return 0;
    }
}
