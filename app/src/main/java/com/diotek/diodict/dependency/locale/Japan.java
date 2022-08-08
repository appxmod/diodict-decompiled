package com.diotek.diodict.dependency.locale;

/* loaded from: classes.dex */
public class Japan extends Locale {
    @Override // com.diotek.diodict.dependency.locale.Locale
    public String getGoogleAddress() {
        return "http://www.google.co.jp/search?hl=jp&newwindow=1&q=";
    }

    @Override // com.diotek.diodict.dependency.locale.Locale
    public String getWikipediaAddress() {
        return "http://jp.m.wikipedia.org/wiki?search=";
    }
}
