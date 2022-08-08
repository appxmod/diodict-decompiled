package com.diotek.diodict.dependency.locale;

import com.diotek.diodict.dependency.Dependency;

/* loaded from: classes.dex */
public class LocaleInfo {
    public static Locale mLocale = null;

    public static Locale return_Locale() {
        if (mLocale == null) {
            try {
                Class<?> cs = Class.forName(Dependency.getLocaleName());
                mLocale = (Locale) cs.newInstance();
            } catch (ClassNotFoundException e) {
                mLocale = null;
            } catch (IllegalAccessException e2) {
                mLocale = null;
            } catch (InstantiationException e3) {
                mLocale = null;
            }
            if (mLocale == null) {
                mLocale = new Korean();
            }
        }
        return mLocale;
    }
}
