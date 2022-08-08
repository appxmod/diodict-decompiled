package com.diotek.diodict.auth;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class Settings {
    public static final String DIODICT_SETTING_PREF_ENABLED = "end";

    public static void setDictAuthStateToPreference(Context ctx, boolean value) {
        SharedPreferences prefSettings = ctx.getSharedPreferences(DIODICT_SETTING_PREF_ENABLED, 0);
        SharedPreferences.Editor editor = prefSettings.edit();
        editor.putBoolean(DIODICT_SETTING_PREF_ENABLED, value);
        editor.commit();
    }

    public static boolean getDictAuthStateFromPreference(Context ctx) {
        SharedPreferences prefSettings = ctx.getSharedPreferences(DIODICT_SETTING_PREF_ENABLED, 0);
        boolean result = prefSettings.getBoolean(DIODICT_SETTING_PREF_ENABLED, false);
        return result;
    }
}
