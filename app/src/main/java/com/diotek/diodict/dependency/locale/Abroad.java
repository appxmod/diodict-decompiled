package com.diotek.diodict.dependency.locale;

/* loaded from: classes.dex */
public class Abroad extends Locale {
    @Override // com.diotek.diodict.dependency.locale.Locale
    public String getGoogleAddress() {
        return "http://www.google.com/search?hl=jp&newwindow=1&q=";
    }

    @Override // com.diotek.diodict.dependency.locale.Locale
    public String getWikipediaAddress() {
        return "http://m.wikipedia.org/wiki?search=";
    }
}
