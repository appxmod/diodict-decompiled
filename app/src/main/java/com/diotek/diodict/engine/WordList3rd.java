package com.diotek.diodict.engine;

/* loaded from: classes.dex */
public class WordList3rd {
    public int m_DicType;
    public String m_dcKeyword;
    public int m_dcUID;

    public WordList3rd(String keyword, int dcUID, int nDicType) {
        this.m_dcKeyword = keyword;
        this.m_DicType = nDicType;
        this.m_dcUID = dcUID;
    }

    public WordList3rd(byte[] keyword, int dcUID, int nDicType) {
        this.m_dcKeyword = DictUtils.convertByteToString(keyword, nDicType, true);
        this.m_dcUID = dcUID;
        this.m_DicType = nDicType;
    }

    public String getKeyword() {
        return this.m_dcKeyword;
    }

    public int getSUID() {
        return this.m_dcUID;
    }

    public int getDicType() {
        return this.m_DicType;
    }
}
