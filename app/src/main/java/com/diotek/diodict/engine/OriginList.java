package com.diotek.diodict.engine;

import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class OriginList {
    private ArrayList<HashMap<String, Object>> mResult;

    public OriginList() {
        this.mResult = null;
        this.mResult = new ArrayList<>();
    }

    public int size() {
        return this.mResult.size();
    }

    public String getKeyword(int index) {
        return (String) this.mResult.get(index).get(DictInfo.ListItem_Keyword);
    }

    public int getSuid(int index) {
        return ((Integer) this.mResult.get(index).get("suid")).intValue();
    }

    public void addWord(String word, int suid) {
        HashMap<String, Object> tItem = new HashMap<>();
        tItem.put(DictInfo.ListItem_Keyword, word);
        tItem.put("suid", Integer.valueOf(suid));
        this.mResult.add(tItem);
    }

    public ArrayList<HashMap<String, Object>> getResult() {
        return this.mResult;
    }
}
