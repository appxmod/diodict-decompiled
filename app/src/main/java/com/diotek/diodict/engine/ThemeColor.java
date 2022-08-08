package com.diotek.diodict.engine;

import android.graphics.Color;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;

/* loaded from: classes.dex */
public class ThemeColor {
    public static final int HILIGHT_COLOR_INDEX = 2;
    public static final int MAX_ADV_COLOR = 13;
    public static final int MAX_BASE_COLOR = 9;
    public static final int[] baseTheme = {Color.rgb(0, 0, 0), Color.rgb(0, 0, 0), Color.rgb(255, 242, 0), Color.rgb(255, (int) DHWR.DLANG_THAI, 0), Color.rgb(0, 0, 0), Color.rgb(0, 0, 0), Color.rgb(140, 150, (int) DHWR.DLANG_BPMF), Color.rgb(163, 163, 163), Color.rgb(235, 85, 5)};
    public static final int[] advTheme = {Color.rgb(235, 85, 5), Color.rgb(235, 85, 5), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(235, 85, 5), Color.rgb(255, 255, 255), Color.rgb(255, 0, 255), Color.rgb(255, 242, 0), Color.rgb(90, 140, 35), Color.rgb(163, 163, 163), Color.rgb(90, 140, 35), Color.rgb(163, 163, 163)};
    public static final int[] baseTheme2 = {Color.rgb(0, 0, 0), Color.rgb(236, 236, 218), Color.rgb(183, 213, 48), Color.rgb(255, (int) DHWR.DLANG_THAI, 0), Color.rgb(60, 145, 165), Color.rgb(236, 236, 218), Color.rgb(100, 190, 215), Color.rgb(163, 163, 163), Color.rgb(240, 20, 100)};
    public static final int[] advTheme2 = {Color.rgb(240, 20, 100), Color.rgb(240, 20, 100), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(240, 20, 100), Color.rgb(255, 255, 255), Color.rgb(255, 0, 255), Color.rgb(183, 213, 48), Color.rgb(0, 0, 0), Color.rgb(163, 163, 163), Color.rgb(0, 0, 0), Color.rgb(163, 163, 163)};
    public static final int[] baseTheme3 = {Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(183, 213, 48), Color.rgb(255, (int) DHWR.DLANG_THAI, 0), Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(0, 0, 0), Color.rgb(163, 163, 163), Color.rgb(215, 0, 0)};
    public static final int[] advTheme3 = {Color.rgb(215, 0, 0), Color.rgb(215, 0, 0), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(215, 0, 0), Color.rgb(255, 255, 255), Color.rgb(255, 0, 255), Color.rgb(183, 213, 48), Color.rgb(0, 0, 0), Color.rgb(163, 163, 163), Color.rgb(0, 0, 0), Color.rgb(163, 163, 163)};
    public static final int[] basehypertheme = {Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(180, 255, 55), Color.rgb(163, 163, 163), Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(163, 163, 163), Color.rgb((int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI), Color.rgb((int) DHWR.DLANG_EQUALITY, 182, 49)};
    public static final int[] advhypertheme = {Color.rgb(252, (int) DHWR.DLANG_THAI, 0), Color.rgb((int) DHWR.DLANG_EQUALITY, 182, 49), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 255), Color.rgb(255, 255, 0), Color.rgb(170, 0, 255), Color.rgb((int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI), Color.rgb((int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI), Color.rgb((int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI), Color.rgb((int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI, (int) DHWR.DLANG_FARSI)};
    public static final int[] w3color = {Color.rgb(95, (int) DHWR.DLANG_HEBREW, 0)};

    public static void setHilightColorToTheme(int[] basecolorArray, int[] inputColorArray, int color) {
        for (int i = 0; i < basecolorArray.length; i++) {
            basecolorArray[i] = inputColorArray[i];
            if (i == 2) {
                basecolorArray[i] = color;
            }
        }
    }

    public static int getMeanBaseTextColor() {
        return baseTheme[4];
    }
}
