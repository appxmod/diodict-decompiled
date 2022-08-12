package com.diotek.diodict.uitool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.diotek.diodict.utils.GlobalOptions;

/* loaded from: classes.dex */
public class TextImageButton extends ImageButton {
    final String ATTR;
    final String BaseAndroidNamespace;
    final String TextAlignVertical;
    final String TextImageButton_textbold_ATTR;
    final String TextImageButton_textcolor_ATTR;
    final String TextImageButton_textpositiony_ATTR;
    final String TextImageButton_textsize_ATTR;
    Context mContext;
    private Path mPath;
    int m_textAlignDegree;
    boolean m_textAlignVertical;
    boolean m_textBold;
    String m_textBtn;
    int m_textColor;
    int m_textPositionY;
    int m_textSize;
    int stringId;

    public TextImageButton(Context context) {
        super(context);
        this.ATTR = "text_button";
        this.TextImageButton_textsize_ATTR = "text_size";
        this.TextImageButton_textpositiony_ATTR = "text_positiony";
        this.TextImageButton_textcolor_ATTR = "text_textcolor";
        this.TextImageButton_textbold_ATTR = "text_bold";
        this.TextAlignVertical = "text_align_vertical";
        this.BaseAndroidNamespace = "http://schemas.android.com/apk/res/android";
        this.mContext = null;
        this.stringId = 0;
        this.m_textBtn = "";
        this.m_textSize = 20;
        this.m_textPositionY = 0;
        this.m_textColor = -16777216;
        this.m_textBold = false;
        this.m_textAlignVertical = false;
        this.m_textAlignDegree = 90;
        this.mPath = new Path();
        this.mContext = context;
        initAttribute();
    }

    private void initAttribute() {
        this.m_textColor = -16777216;
        this.m_textSize = 20;
        this.m_textBold = true;
    }

    public TextImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ATTR = "text_button";
        this.TextImageButton_textsize_ATTR = "text_size";
        this.TextImageButton_textpositiony_ATTR = "text_positiony";
        this.TextImageButton_textcolor_ATTR = "text_textcolor";
        this.TextImageButton_textbold_ATTR = "text_bold";
        this.TextAlignVertical = "text_align_vertical";
        this.BaseAndroidNamespace = "http://schemas.android.com/apk/res/android";
        this.mContext = null;
        this.stringId = 0;
        this.m_textBtn = "";
        this.m_textSize = 20;
        this.m_textPositionY = 0;
        this.m_textColor = -16777216;
        this.m_textBold = false;
        this.m_textAlignVertical = false;
        this.m_textAlignDegree = 90;
        this.mContext = context;
        float nDensity = GlobalOptions.density;
        if (attrs.getAttributeValue(null, "text_button") == null) {
            String tmpstr = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
            if (tmpstr == null) {
                this.m_textBtn = "";
            } else if (tmpstr.contains("@")) {
                this.stringId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "text", 0);
                this.m_textBtn = getResources().getString(this.stringId);
            } else {
                this.m_textBtn = tmpstr;
            }
        }
        String tmpstr2 = attrs.getAttributeValue(null, "text_size");
        if (tmpstr2 != null) {
            if (tmpstr2.contains("@")) {
                int dimenId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textSize", 0);
                this.m_textSize = getResources().getDimensionPixelSize(dimenId);
            } else if (tmpstr2.contains("sp")) {
                this.m_textSize = (int) (Float.parseFloat(tmpstr2.replace("sp", "")) * nDensity);
            }
        } else {
            String tmpstr3 = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textSize");
            if (tmpstr3 != null) {
                if (tmpstr3.contains("@")) {
                    int dimenId2 = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textSize", 0);
                    this.m_textSize = getResources().getDimensionPixelSize(dimenId2);
                } else if (tmpstr3.contains("sp")) {
                    this.m_textSize = (int) (Float.parseFloat(tmpstr3.replace("sp", "")) * nDensity);
                }
            }
        }
        String tmpstr4 = attrs.getAttributeValue(null, "text_positiony");
        if (tmpstr4 != null) {
            this.m_textPositionY = (int) (Float.parseFloat(tmpstr4) * nDensity);
        }
        String curNamespace = null;
        String curAttrName = "text_textcolor";
        String tmpstr5 = attrs.getAttributeValue(null, "text_textcolor");
        if (tmpstr5 == null) {
            curNamespace = "http://schemas.android.com/apk/res/android";
            curAttrName = "textColor";
            tmpstr5 = attrs.getAttributeValue(curNamespace, curAttrName);
        }
        if (tmpstr5 != null) {
            if (tmpstr5.contains("#")) {
                this.m_textColor = getColor(tmpstr5);
            } else if (tmpstr5.contains("@")) {
                int colorId = attrs.getAttributeResourceValue(curNamespace, curAttrName, 0);
                this.m_textColor = getResources().getColor(colorId);
            } else {
                this.m_textColor = Integer.parseInt(tmpstr5, 10);
            }
            this.m_textBold = attrs.getAttributeBooleanValue(null, "text_bold", false);
            this.m_textAlignVertical = attrs.getAttributeBooleanValue(null, "text_align_vertical", false);
        }
        this.mPath = new Path();
    }

    public TextImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ATTR = "text_button";
        this.TextImageButton_textsize_ATTR = "text_size";
        this.TextImageButton_textpositiony_ATTR = "text_positiony";
        this.TextImageButton_textcolor_ATTR = "text_textcolor";
        this.TextImageButton_textbold_ATTR = "text_bold";
        this.TextAlignVertical = "text_align_vertical";
        this.BaseAndroidNamespace = "http://schemas.android.com/apk/res/android";
        this.mContext = null;
        this.stringId = 0;
        this.m_textBtn = "";
        this.m_textSize = 20;
        this.m_textPositionY = 0;
        this.m_textColor = -16777216;
        this.m_textBold = false;
        this.m_textAlignVertical = false;
        this.m_textAlignDegree = 90;
        this.mContext = context;
    }

    public void setText(String text) {
        this.m_textBtn = text;
    }

    public void setText(int textId) {
        this.m_textBtn = this.mContext.getResources().getString(textId);
    }

    public void setTextSize(int nSize) {
        this.m_textSize = nSize;
    }

    public void setTextColor(String color) {
        this.m_textColor = getColor(color);
    }

    public void setTextBold(boolean bold) {
        this.m_textBold = bold;
    }

    public void setPositionY(int nPosY) {
        this.m_textPositionY = nPosY;
    }

    public void setTextColor(int nColor) {
        this.m_textColor = nColor;
    }

    public void setTextAlignVertical(boolean isSet) {
        this.m_textAlignVertical = isSet;
    }

    public void setTextAlignDegree(int value) {
        this.m_textAlignDegree = value;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        Paint textPaint = new Paint(1);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(this.m_textColor);
        textPaint.setTextSize(this.m_textSize);
        textPaint.setFakeBoldText(this.m_textBold);
        Rect rtCanvas = new Rect();
        getDrawingRect(rtCanvas);
        int nCenterX = rtCanvas.right / 2;
        if (this.m_textAlignVertical && this.m_textBtn != null) {
            makePath(this.mPath, textPaint);
            canvas.drawTextOnPath(this.m_textBtn, this.mPath, 0.0f, 0.0f, textPaint);
        } else if (this.m_textBtn != null && !this.m_textBtn.equals("")) {
            Rect textRect = new Rect();
            textPaint.getTextBounds(this.m_textBtn, 0, this.m_textBtn.length() - 1, textRect);
            if (this.m_textPositionY == 0) {
                this.m_textPositionY = (rtCanvas.bottom * 2) / 3;
            }
            canvas.drawText(this.m_textBtn, nCenterX, this.m_textPositionY, textPaint);
            invalidate();
        }
        super.onDraw(canvas);
    }

    private void makePath(Path p, Paint paint) {
        int height = super.getHeight();
        int width = super.getWidth();
        float move_y = height;
        if (this.m_textAlignDegree == 90) {
            float move_x = (width / 2) - getRightPaddingOffset();
            p.moveTo(move_x, move_y);
            p.lineTo(move_x, 0.0f);
        } else if (this.m_textAlignDegree == 270) {
            float move_x2 = width / 5;
            p.moveTo(move_x2, 0.0f);
            p.lineTo(move_x2, move_y);
        }
    }

    private int getColor(String color) {
        if (color == null || !color.contains("#")) {
            return -46;
        }
        if (color.length() == 7) {
            String tmpstr2 = color.substring(1, 3);
            int color_r = Integer.parseInt(tmpstr2, 16);
            String tmpstr22 = color.substring(3, 5);
            int color_g = Integer.parseInt(tmpstr22, 16);
            String tmpstr23 = color.substring(5, 7);
            int color_b = Integer.parseInt(tmpstr23, 16);
            int textColor = (-16777216) + (65536 * color_r) + (color_g * 256) + color_b;
            return textColor;
        } else if (color.length() != 9) {
            return -46;
        } else {
            String tmpstr24 = color.substring(1, 3);
            int color_a = Integer.parseInt(tmpstr24, 16);
            String tmpstr25 = color.substring(3, 5);
            int color_r2 = Integer.parseInt(tmpstr25, 16);
            String tmpstr26 = color.substring(5, 7);
            int color_g2 = Integer.parseInt(tmpstr26, 16);
            String tmpstr27 = color.substring(7, 9);
            int color_b2 = Integer.parseInt(tmpstr27, 16);
            int textColor2 = (16777216 * color_a) + (65536 * color_r2) + (color_g2 * 256) + color_b2;
            return textColor2;
        }
    }
}
