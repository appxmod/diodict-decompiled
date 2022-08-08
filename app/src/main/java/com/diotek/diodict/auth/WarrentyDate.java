package com.diotek.diodict.auth;

import com.diotek.diodict.KindOfMarket;

/* loaded from: classes.dex */
public class WarrentyDate {
    public static final int WARRENTY_DAY = 31;
    public static final int WARRENTY_HOURS = 24;
    public static final int WARRENTY_MINUTES = 0;
    public static final int WARRENTY_MONTH = 12;
    public static final int WARRENTY_SECONDS = 0;
    public static final int WARRENTY_YEAR = 112;

    public int isWarrentyCorrect(int market) {
        if (market != KindOfMarket.mKindOfMarket) {
            return 334455;
        }
        int warrenty = 0;
        switch (warrenty) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 100:
            case 101:
                return market - 0;
            default:
                return 334456;
        }
    }
}
