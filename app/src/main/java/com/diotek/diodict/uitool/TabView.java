package com.diotek.diodict.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.diodict.decompiled.R;
import com.diotek.diodict.utils.GlobalOptions;

import java.util.EventListener;
import java.util.Locale;

/* loaded from: classes.dex */
public class TabView extends FrameLayout {
    final String TextImageButton_textpositiony_ATTR;
    final String id_ATTR;
    final String layout_height_ATTR;
    final String layout_width_ATTR;
    private TabViewOnClickListener mBtnClickCallBack;
    public View.OnClickListener mBtnOnClickListener;
    ViewGroup.MarginLayoutParams mButtonGapParam;
    private Context mContext;
    private int mHeight;
    private boolean mIsCenterVertical;
    private LinearLayout[] mLinear;
    private int mPaddingTop;
    private int mTextAlignDegree;
    private boolean mTextAlignVertical;
    private boolean mTextBold;
    private String mTextColor;
    private int mTextPositionY;
    private int mTextSize;
    private int mTotalCount;
    private int mWidth;
    final String tabview_gapBottom_ATTR;
    final String tabview_gapLeft_ATTR;
    final String tabview_gapRight_ATTR;
    final String tabview_gapTop_ATTR;
    final String tabview_textAlignDegree_ATTR;
    final String tabview_textAlign_ATTR;
    final String tabview_textBold_ATTR;
    final String tabview_textColor_ATTR;
    final String tabview_textSize_ATTR;
    final String tabview_total_ATTR;

    /* loaded from: classes.dex */
    public interface TabViewOnClickListener extends EventListener {
        void onClick(View view, int i);
    }

    public TabView(Context context) {
        super(context);
        this.mContext = null;
        this.mTotalCount = 0;
        this.mLinear = null;
        this.id_ATTR = "id";
        this.tabview_total_ATTR = "tabview_total";
        this.layout_width_ATTR = "layout_width";
        this.layout_height_ATTR = "layout_height";
        this.tabview_gapLeft_ATTR = "tabview_gapleft";
        this.tabview_gapTop_ATTR = "tabview_gaptop";
        this.tabview_gapRight_ATTR = "tabview_gapright";
        this.tabview_gapBottom_ATTR = "tabview_gapbottom";
        this.tabview_textAlign_ATTR = "tabview_textAlign";
        this.tabview_textAlignDegree_ATTR = "tabview_textAlignDegree";
        this.tabview_textColor_ATTR = "tabview_textColor";
        this.tabview_textSize_ATTR = "tabview_textSize";
        this.tabview_textBold_ATTR = "tabview_textBold";
        this.TextImageButton_textpositiony_ATTR = "text_positiony";
        this.mWidth = 0;
        this.mHeight = 0;
        this.mTextPositionY = 0;
        this.mTextAlignVertical = false;
        this.mTextAlignDegree = 0;
        this.mTextColor = "";
        this.mTextSize = 0;
        this.mTextBold = true;
        this.mIsCenterVertical = false;
        this.mPaddingTop = 0;
        this.mButtonGapParam = null;
        this.mBtnClickCallBack = null;
        this.mBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.TabView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int nSelectBtnPos = TabView.this.changeTabState(v);
                if (TabView.this.mBtnClickCallBack != null) {
                    TabView.this.mBtnClickCallBack.onClick(v, nSelectBtnPos);
                }
            }
        };
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            setTabViewPosition(bottom - top);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override // android.view.View
    public void setFocusable(boolean focusable) {
        for (int i = 0; i < this.mTotalCount; i++) {
            this.mLinear[i].getChildAt(0).setFocusable(focusable);
        }
        super.setFocusable(focusable);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = null;
        this.mTotalCount = 0;
        this.mLinear = null;
        this.id_ATTR = "id";
        this.tabview_total_ATTR = "tabview_total";
        this.layout_width_ATTR = "layout_width";
        this.layout_height_ATTR = "layout_height";
        this.tabview_gapLeft_ATTR = "tabview_gapleft";
        this.tabview_gapTop_ATTR = "tabview_gaptop";
        this.tabview_gapRight_ATTR = "tabview_gapright";
        this.tabview_gapBottom_ATTR = "tabview_gapbottom";
        this.tabview_textAlign_ATTR = "tabview_textAlign";
        this.tabview_textAlignDegree_ATTR = "tabview_textAlignDegree";
        this.tabview_textColor_ATTR = "tabview_textColor";
        this.tabview_textSize_ATTR = "tabview_textSize";
        this.tabview_textBold_ATTR = "tabview_textBold";
        this.TextImageButton_textpositiony_ATTR = "text_positiony";
        this.mWidth = 0;
        this.mHeight = 0;
        this.mTextPositionY = 0;
        this.mTextAlignVertical = false;
        this.mTextAlignDegree = 0;
        this.mTextColor = "";
        this.mTextSize = 0;
        this.mTextBold = true;
        this.mIsCenterVertical = false;
        this.mPaddingTop = 0;
        this.mButtonGapParam = null;
        this.mBtnClickCallBack = null;
        this.mBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.TabView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int nSelectBtnPos = TabView.this.changeTabState(v);
                if (TabView.this.mBtnClickCallBack != null) {
                    TabView.this.mBtnClickCallBack.onClick(v, nSelectBtnPos);
                }
            }
        };
        this.mContext = context;
        if (this.mContext != null) {
            this.mTotalCount = getParsingTotal(attrs);
            this.mWidth = getParsingWidth(attrs);
            this.mHeight = getParsingHeight(attrs);
            this.mTextPositionY = getParsingTextPositionY(attrs);
            this.mTextAlignVertical = getParsingTextAlignVertical(attrs);
            this.mTextAlignDegree = getParsingTextAlignDegree(attrs);
            this.mTextColor = getParsingTextColor(attrs);
            this.mTextSize = getParsingTextSize(attrs);
            this.mTextBold = getParsingTextBold(attrs);
            this.mButtonGapParam = getParsingMargin(this.mWidth, this.mHeight, attrs);
            ViewGroup.MarginLayoutParams buttonParam = new ViewGroup.MarginLayoutParams(this.mWidth, this.mHeight);
            this.mTextSize = (int) (this.mTextSize * this.mContext.getResources().getDisplayMetrics().density);
            if (CommonUtils.isLowResolutionDevice(this.mContext) || CommonUtils.isQVGADevice(this.mContext)) {
                this.mTextSize = (int) (this.mTextSize * 1.33f);
                String systemLanguage = getResources().getConfiguration().locale.getDisplayLanguage(Locale.ENGLISH);
                if (systemLanguage.equalsIgnoreCase("Chinese")) {
                    this.mTextSize += 2;
                }
            }
            this.mLinear = new LinearLayout[this.mTotalCount];
            for (int i = 0; i < this.mTotalCount; i++) {
                this.mLinear[i] = new LinearLayout(context);
                this.mLinear[i].setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                TextImageButton imageButton = new TextImageButton(context);
                imageButton.setLayoutParams(new LinearLayout.LayoutParams(buttonParam));
                imageButton.setTextAlignVertical(this.mTextAlignVertical);
                imageButton.setTextAlignDegree(this.mTextAlignDegree);
                imageButton.setTextColor(this.mTextColor);
                imageButton.setTextSize(this.mTextSize);
                imageButton.setTextBold(this.mTextBold);
                imageButton.setOnClickListener(this.mBtnOnClickListener);
                imageButton.setPositionY(this.mTextPositionY);
                this.mLinear[i].addView(imageButton);
                addView(this.mLinear[i]);
            }
            setTabViewPosition(this.mHeight * this.mTotalCount);
        }
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = null;
        this.mTotalCount = 0;
        this.mLinear = null;
        this.id_ATTR = "id";
        this.tabview_total_ATTR = "tabview_total";
        this.layout_width_ATTR = "layout_width";
        this.layout_height_ATTR = "layout_height";
        this.tabview_gapLeft_ATTR = "tabview_gapleft";
        this.tabview_gapTop_ATTR = "tabview_gaptop";
        this.tabview_gapRight_ATTR = "tabview_gapright";
        this.tabview_gapBottom_ATTR = "tabview_gapbottom";
        this.tabview_textAlign_ATTR = "tabview_textAlign";
        this.tabview_textAlignDegree_ATTR = "tabview_textAlignDegree";
        this.tabview_textColor_ATTR = "tabview_textColor";
        this.tabview_textSize_ATTR = "tabview_textSize";
        this.tabview_textBold_ATTR = "tabview_textBold";
        this.TextImageButton_textpositiony_ATTR = "text_positiony";
        this.mWidth = 0;
        this.mHeight = 0;
        this.mTextPositionY = 0;
        this.mTextAlignVertical = false;
        this.mTextAlignDegree = 0;
        this.mTextColor = "";
        this.mTextSize = 0;
        this.mTextBold = true;
        this.mIsCenterVertical = false;
        this.mPaddingTop = 0;
        this.mButtonGapParam = null;
        this.mBtnClickCallBack = null;
        this.mBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.TabView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int nSelectBtnPos = TabView.this.changeTabState(v);
                if (TabView.this.mBtnClickCallBack != null) {
                    TabView.this.mBtnClickCallBack.onClick(v, nSelectBtnPos);
                }
            }
        };
    }

    public void setOnClickListener(TabViewOnClickListener listener) {
        this.mBtnClickCallBack = listener;
    }

    public int changeTabState(View v) {
        int nSelectBtnPos = 0;
        int nBtnTotalCount = this.mLinear.length;
        for (int i = 0; i < nBtnTotalCount; i++) {
            this.mLinear[i].getChildAt(0).setSelected(false);
        }
        int i2 = 0;
        while (true) {
            if (i2 >= nBtnTotalCount) {
                break;
            } else if (this.mLinear[i2].getChildAt(0) == ((ImageButton) v)) {
                nSelectBtnPos = i2;
                break;
            } else {
                bringChildToFront(this.mLinear[i2]);
                this.mLinear[i2].invalidate();
                i2++;
            }
        }
        if (nSelectBtnPos + 1 < nBtnTotalCount) {
            for (int i3 = nBtnTotalCount - 1; i3 > nSelectBtnPos; i3--) {
                bringChildToFront(this.mLinear[i3]);
                this.mLinear[i3].invalidate();
            }
        }
        bringChildToFront(this.mLinear[nSelectBtnPos]);
        return nSelectBtnPos;
    }

    public int getParsingTotal(AttributeSet attrs) {
        String tmpTotalStr = attrs.getAttributeValue(null, "tabview_total");
        if (tmpTotalStr == null) {
            return 0;
        }
        int nTotal = Integer.parseInt(tmpTotalStr);
        return nTotal;
    }

    public int getParsingWidth(AttributeSet attrs) {
        String tmpWidthStr = attrs.getAttributeValue(null, "layout_width");
        if (tmpWidthStr == null) {
            return 0;
        }
        if (tmpWidthStr.equals("fill_parent")) {
            return -1;
        }
        if (tmpWidthStr.equals("wrap_content")) {
            return -2;
        }
        if (tmpWidthStr.contains("dp")) {
            int nWidth = (int) (Integer.parseInt(tmpWidthStr.substring(0, tmpWidthStr.indexOf("dp"))) * getResources().getDisplayMetrics().density);
            return nWidth;
        }
        int nWidth2 = Integer.parseInt(tmpWidthStr);
        return nWidth2;
    }

    public int getParsingHeight(AttributeSet attrs) {
        String tmpHeightStr = attrs.getAttributeValue(null, "layout_height");
        if (tmpHeightStr == null) {
            return 0;
        }
        if (tmpHeightStr.equals("fill_parent")) {
            return -1;
        }
        if (tmpHeightStr.equals("wrap_content")) {
            return -2;
        }
        if (tmpHeightStr.contains("dp")) {
            int nHeight = (int) (Integer.parseInt(tmpHeightStr.substring(0, tmpHeightStr.indexOf("dp"))) * getResources().getDisplayMetrics().density);
            return nHeight;
        } else if (tmpHeightStr.contains("cradletab_height")) {
            this.mIsCenterVertical = true;
            int nHeight2 = this.mContext.getResources().getInteger(R.integer.cradletab_height);
            return nHeight2;
        } else {
            int nHeight3 = Integer.parseInt(tmpHeightStr);
            return nHeight3;
        }
    }

    public boolean getParsingTextAlignVertical(AttributeSet attrs) {
        String tmpTextAlignVerticalStr = attrs.getAttributeValue(null, "tabview_textAlign");
        if (tmpTextAlignVerticalStr != null && tmpTextAlignVerticalStr.equals("vertical")) {
            return true;
        }
        return false;
    }

    public int getParsingTextAlignDegree(AttributeSet attrs) {
        String tmpTextAlignDegreeStr = attrs.getAttributeValue(null, "tabview_textAlignDegree");
        if (tmpTextAlignDegreeStr == null) {
            return 90;
        }
        int alignDegree = Integer.parseInt(tmpTextAlignDegreeStr);
        return alignDegree;
    }

    public String getParsingTextColor(AttributeSet attrs) {
        String tmpTextColorStr = attrs.getAttributeValue(null, "tabview_textColor");
        if (tmpTextColorStr == null) {
            return "#ffffffd2";
        }
        return tmpTextColorStr;
    }

    public int getParsingTextSize(AttributeSet attrs) {
        String tmpTextSize = attrs.getAttributeValue(null, "tabview_textSize");
        if (tmpTextSize == null) {
            return 10;
        }
        if (tmpTextSize.equals("cradletab_textSize")) {
            int textSize = this.mContext.getResources().getInteger(R.integer.cradletab_textSize);
            return textSize;
        }
        int textSize2 = Integer.parseInt(tmpTextSize);
        return textSize2;
    }

    public boolean getParsingTextBold(AttributeSet attrs) {
        String tmpTextBold = attrs.getAttributeValue(null, "tabview_textBold");
        if (tmpTextBold == null) {
            return true;
        }
        boolean textBold = Boolean.parseBoolean(tmpTextBold);
        return textBold;
    }

    public int getParsingTextPositionY(AttributeSet attrs) {
        String tmpTextPositionYStr = attrs.getAttributeValue(null, "text_positiony");
        if (tmpTextPositionYStr == null) {
            return 0;
        }
        int bTextPositionY = (int) (Float.parseFloat(tmpTextPositionYStr) * GlobalOptions.density);
        return bTextPositionY;
    }

    public ViewGroup.MarginLayoutParams getParsingMargin(int nWidth, int nHeight, AttributeSet attrs) {
        int nTop;
        int nLeft;
        int nBottom;
        ViewGroup.MarginLayoutParams marginLayoutParam = new ViewGroup.MarginLayoutParams(nWidth, nHeight);
        String tmpMarginStr = attrs.getAttributeValue(null, "tabview_gapleft");
        if (tmpMarginStr != null) {
            if (tmpMarginStr.contains("dp")) {
                int nLeft2 = (int) (Integer.parseInt(tmpMarginStr.substring(0, tmpMarginStr.indexOf("dp"))) * getResources().getDisplayMetrics().density);
            } else {
                Integer.parseInt(tmpMarginStr);
            }
        }
        String tmpMarginStr2 = attrs.getAttributeValue(null, "tabview_gaptop");
        if (tmpMarginStr2 == null) {
            nTop = (int) getResources().getDimension(R.dimen.tabview_gaptop);
        } else if (tmpMarginStr2.contains("dp")) {
            nTop = (int) (Integer.parseInt(tmpMarginStr2.substring(0, tmpMarginStr2.indexOf("dp"))) * getResources().getDisplayMetrics().density);
        } else {
            nTop = Integer.parseInt(tmpMarginStr2);
        }
        String tmpMarginStr3 = attrs.getAttributeValue(null, "tabview_gapleft");
        if (tmpMarginStr3 == null) {
            nLeft = 0;
        } else if (tmpMarginStr3.contains("dp")) {
            nLeft = (int) (Integer.parseInt(tmpMarginStr3.substring(0, tmpMarginStr3.indexOf("dp"))) * getResources().getDisplayMetrics().density);
        } else {
            nLeft = Integer.parseInt(tmpMarginStr3);
        }
        String tmpMarginStr4 = attrs.getAttributeValue(null, "tabview_gapbottom");
        if (tmpMarginStr4 == null) {
            nBottom = 0;
        } else if (tmpMarginStr4.contains("dp")) {
            nBottom = (int) (Integer.parseInt(tmpMarginStr4.substring(0, tmpMarginStr4.indexOf("dp"))) * getResources().getDisplayMetrics().density);
        } else {
            nBottom = Integer.parseInt(tmpMarginStr4);
        }
        marginLayoutParam.setMargins(nLeft, nTop, 0, nBottom);
        return marginLayoutParam;
    }

    public TextImageButton getButton(int nPos) {
        LinearLayout layout = this.mLinear[nPos];
        if (layout == null) {
            return null;
        }
        return (TextImageButton) layout.getChildAt(0);
    }

    public LinearLayout getButtonLayout(int nPos) {
        return this.mLinear[nPos];
    }

    public int getTotalCount() {
        return this.mTotalCount;
    }

    public void setTabViewPosition(int nLayoutHeight) {
        int nOneTabViewHeight = this.mLinear[0].getMeasuredHeight();
        if (nOneTabViewHeight == 0) {
            nOneTabViewHeight = this.mHeight;
        }
        int nTotalTabViewHeight = nOneTabViewHeight * this.mTotalCount;
        int nShortageHeight = nTotalTabViewHeight - nLayoutHeight;
        this.mButtonGapParam.topMargin = nOneTabViewHeight;
        if (nLayoutHeight < nTotalTabViewHeight) {
            this.mButtonGapParam.topMargin = nOneTabViewHeight - (nShortageHeight / (this.mTotalCount - 1));
        } else {
            this.mButtonGapParam.topMargin = nOneTabViewHeight;
        }
        if (this.mIsCenterVertical) {
            this.mPaddingTop = (nLayoutHeight - nTotalTabViewHeight) / 2;
        }
        refreshTabViewPosition();
    }

    public void refreshTabViewPosition() {
        int nMinusPos = 0;
        for (int i = 0; i < this.mTotalCount; i++) {
            if (this.mLinear[i].getVisibility() == 0) {
                TextImageButton imageButton = (TextImageButton) this.mLinear[i].getChildAt(0);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageButton.getLayoutParams();
                params.setMargins((i - nMinusPos) * this.mButtonGapParam.leftMargin, ((i - nMinusPos) * this.mButtonGapParam.topMargin) + this.mPaddingTop, (i - nMinusPos) * this.mButtonGapParam.rightMargin, ((i - nMinusPos) * this.mButtonGapParam.bottomMargin) + this.mPaddingTop);
            } else {
                nMinusPos++;
            }
        }
    }

    public void setBtnSelected(int nMode) {
        for (int i = 0; i < this.mTotalCount; i++) {
            if (i == nMode) {
                getButton(i).setSelected(true);
            } else {
                getButton(i).setSelected(false);
            }
        }
    }

    public void setBtnEnable(boolean isEnable) {
        for (int i = 0; i < this.mTotalCount; i++) {
            getButton(i).setEnabled(isEnable);
        }
    }
}
