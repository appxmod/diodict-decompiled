package com.diotek.diodict.engine;

/* loaded from: classes.dex */
public class ResultWordList3rd {
    private boolean m_Exactmatch;
    WordList3rd[] m_astWordList;
    private int m_nSize;
    private int m_nkeywordPos;

    public ResultWordList3rd(int vSize, int vPos, boolean exactmatch) {
        this.m_nSize = 0;
        this.m_nkeywordPos = 0;
        this.m_Exactmatch = false;
        this.m_nSize = vSize;
        this.m_nkeywordPos = vPos;
        this.m_Exactmatch = exactmatch;
        this.m_astWordList = new WordList3rd[this.m_nSize];
    }

    public WordList3rd[] getAstWordList() {
        return this.m_astWordList;
    }

    public WordList3rd getWordList(int index) {
        if (index >= this.m_astWordList.length) {
            return null;
        }
        return this.m_astWordList[index];
    }

    public String getKeywordByPos(int index) {
        if (index >= this.m_astWordList.length) {
            return null;
        }
        return this.m_astWordList[index].m_dcKeyword;
    }

    public int getSUIDByPos(int index) {
        if (index >= this.m_astWordList.length) {
            return 0;
        }
        return this.m_astWordList[index].m_dcUID;
    }

    public int getDicTypeByPos(int index) {
        if (index >= this.m_astWordList.length) {
            return 0;
        }
        return this.m_astWordList[index].m_DicType;
    }

    public int getSize() {
        return this.m_nSize;
    }

    public int getKeywordPos() {
        return this.m_nkeywordPos;
    }

    public boolean isBExactmatch() {
        return this.m_Exactmatch;
    }

    public void setAstWordList(byte[] vwordList, int suid, int index, int nDicType) {
        this.m_astWordList[index] = new WordList3rd(vwordList, suid, nDicType);
    }

    public void setNSize(int size) {
        this.m_nSize = size;
    }

    public void setNkeywordPos(int nkeywordPos) {
        this.m_nkeywordPos = nkeywordPos;
    }

    public void setBExtractmatch(boolean extractmatch) {
        this.m_Exactmatch = extractmatch;
    }
}
