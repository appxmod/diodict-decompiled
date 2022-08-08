package com.diotek.diodict.mean;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.mean.TagConverter;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class MeaningSeparator extends TagConverter {
    public static final char LINE_FEED_CHAR = '\n';
    public static final int WORD_COUNT_PER_BLOCK = 2000;
    public static final boolean isUseSeparateBlock = true;
    private boolean mUseSeparateFunction = true;
    ArrayList<SpannableStringBuilder> mMeanBlockArray = new ArrayList<>();
    int mBlockIndex = 0;
    int mBlockTotalCount = 0;

    public MeaningSeparator(Context context, EngineManager3rd engine, int nTheme, TagConverter.LoadListener loadListener) {
        super(context, engine, nTheme, loadListener);
    }

    public int separateBlock() {
        int indexEndBlock;
        if (this.mMeanBlockArray == null) {
            return 0;
        }
        String str = this.mContentSpannableBuilder.toString();
        int indexStartBlock = 0;
        int indexLast = this.mContentSpannableBuilder.length();
        this.mBlockIndex = 0;
        this.mBlockTotalCount = 0;
        this.mMeanBlockArray.clear();
        if (this.mUseSeparateFunction) {
            do {
                int indexLineFeedCheck = (this.mBlockTotalCount + 1) * 2000;
                if (indexLineFeedCheck >= this.mContentSpannableBuilder.length()) {
                    indexEndBlock = indexLast;
                } else {
                    indexEndBlock = str.indexOf(10, indexLineFeedCheck);
                }
                if (indexEndBlock == -1 || indexEndBlock > this.mContentSpannableBuilder.length()) {
                    indexEndBlock = this.mContentSpannableBuilder.length();
                }
                if (indexStartBlock <= indexEndBlock) {
                    this.mMeanBlockArray.add(new SpannableStringBuilder(this.mContentSpannableBuilder.subSequence(indexStartBlock, indexEndBlock)));
                }
                indexStartBlock = indexEndBlock;
                this.mBlockTotalCount++;
            } while (indexEndBlock < indexLast);
        } else {
            this.mMeanBlockArray.add(this.mContentSpannableBuilder);
            this.mBlockTotalCount = 1;
        }
        return this.mBlockTotalCount;
    }

    @Override // com.diotek.diodict.mean.TagConverter
    public boolean loadMeaning(int dicType, String keyword, int suid, int nMode) {
        boolean bResult = super.loadMeaning(dicType, keyword, suid, nMode);
        separateBlock();
        return bResult;
    }

    @Override // com.diotek.diodict.mean.TagConverter
    public boolean loadMeaningWithMode(int dicType, String keyword, int suid, int dispMode) {
        boolean bResult = super.loadMeaningWithMode(dicType, keyword, suid, dispMode);
        separateBlock();
        return bResult;
    }

    @Override // com.diotek.diodict.mean.TagConverter
    public boolean loadMeaningBySource(int dicType, String source) {
        boolean bResult = super.loadMeaningBySource(dicType, source);
        separateBlock();
        return bResult;
    }

    @Override // com.diotek.diodict.mean.TagConverter
    public boolean updateDispalyMode(int displayMode) {
        boolean bResult = super.updateDispalyMode(displayMode);
        separateBlock();
        return bResult;
    }

    @Override // com.diotek.diodict.mean.TagConverter
    public Spanned getMeanFieldSpan() {
        if (this.mMeanBlockArray.size() <= 0) {
            this.mMeanBlockArray.add(this.mContentSpannableBuilder);
            this.mBlockTotalCount = 1;
        }
        return this.mMeanBlockArray.get(this.mBlockIndex);
    }

    public Spanned getNextMeanFieldSpan() {
        if (this.mBlockIndex >= this.mBlockTotalCount - 1) {
            return null;
        }
        this.mBlockIndex++;
        return this.mMeanBlockArray.get(this.mBlockIndex);
    }

    public Spanned getPrevMeanFieldSpan() {
        if (this.mBlockIndex == 0) {
            return null;
        }
        this.mBlockIndex--;
        return this.mMeanBlockArray.get(this.mBlockIndex);
    }

    public boolean isFirstBlock() {
        return this.mBlockIndex == 0;
    }

    public boolean isLastBlock() {
        return this.mBlockIndex >= this.mBlockTotalCount + (-1);
    }

    public void setUseSeparateFunction(boolean isSeparate) {
        this.mUseSeparateFunction = isSeparate;
    }

    public void destroy() {
        if (this.mMeanBlockArray != null) {
            this.mMeanBlockArray.clear();
            this.mMeanBlockArray = null;
        }
    }
}
