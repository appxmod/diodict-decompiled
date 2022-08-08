package com.diotek.diodict3.dependency;

import com.diotek.diodict.dependency.locale.Locale;

/* loaded from: classes.dex */
public class China extends Locale {
    @Override // com.diotek.diodict.dependency.locale.Locale
    public String getGoogleAddress() {
        return "http://www.baidu.com/s?wd=";
    }

    @Override // com.diotek.diodict.dependency.locale.Locale
    public String getWikipediaAddress() {
        return "http://zh.m.wikipedia.org/wiki?search=";
    }
}
