package com.diotek.diodict.uitool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RadioButton;

/* loaded from: classes.dex */
public class TextRadioButton extends RadioButton {
    final String ATTR;
    final String TextAlignVertical;
    final String TextRadioButton_textbold_ATTR;
    final String TextRadioButton_textcolor_ATTR;
    final String TextRadioButton_textpositiony_ATTR;
    final String TextRadioButton_textsize_ATTR;
    String m_textBtn;
    int m_textColor;
    int m_textPositionY;
    int m_textSize;
    int stringId;

    public TextRadioButton(Context context) {
        super(context);
        this.ATTR = "text_button";
        this.TextRadioButton_textsize_ATTR = "text_size";
        this.TextRadioButton_textpositiony_ATTR = "text_positiony";
        this.TextRadioButton_textcolor_ATTR = "text_textcolor";
        this.TextRadioButton_textbold_ATTR = "text_bold";
        this.TextAlignVertical = "text_align_vertical";
        this.stringId = 0;
        this.m_textBtn = "";
        this.m_textSize = 0;
        this.m_textPositionY = 0;
        this.m_textColor = 0;
    }

    public TextRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ATTR = "text_button";
        this.TextRadioButton_textsize_ATTR = "text_size";
        this.TextRadioButton_textpositiony_ATTR = "text_positiony";
        this.TextRadioButton_textcolor_ATTR = "text_textcolor";
        this.TextRadioButton_textbold_ATTR = "text_bold";
        this.TextAlignVertical = "text_align_vertical";
        this.stringId = 0;
        this.m_textBtn = "";
        this.m_textSize = 0;
        this.m_textPositionY = 0;
        this.m_textColor = 0;
        float nDensity = CommonUtils.getDeviceDensity(context);
        String tmpstr = attrs.getAttributeValue(null, "text_button");
        if (tmpstr == null) {
            this.m_textBtn = "";
        } else if (tmpstr.contains("@")) {
            this.stringId = attrs.getAttributeResourceValue(null, "text_button", 0);
            this.m_textBtn = getResources().getString(this.stringId);
        } else {
            this.m_textBtn = tmpstr;
        }
        String tmpstr2 = attrs.getAttributeValue(null, "text_size");
        if (tmpstr2 != null) {
            this.m_textSize = (int) (Float.parseFloat(tmpstr2) * nDensity);
        }
        String tmpstr3 = attrs.getAttributeValue(null, "text_positiony");
        if (tmpstr3 != null) {
            this.m_textPositionY = (int) (Float.parseFloat(tmpstr3) * nDensity);
        }
        String tmpstr4 = attrs.getAttributeValue(null, "text_textcolor");
        if (tmpstr4 != null) {
            if (tmpstr4.contains("#")) {
                if (tmpstr4.length() == 7) {
                    String tmpstr22 = tmpstr4.substring(1, 3);
                    int color_r = Integer.parseInt(tmpstr22, 16);
                    String tmpstr23 = tmpstr4.substring(3, 5);
                    int color_g = Integer.parseInt(tmpstr23, 16);
                    String tmpstr24 = tmpstr4.substring(5, 7);
                    int color_b = Integer.parseInt(tmpstr24, 16);
                    this.m_textColor = (-16777216) + (65536 * color_r) + (color_g * 256) + color_b;
                    return;
                } else if (tmpstr4.length() == 9) {
                    String tmpstr25 = tmpstr4.substring(1, 3);
                    int color_a = Integer.parseInt(tmpstr25, 16);
                    String tmpstr26 = tmpstr4.substring(3, 5);
                    int color_r2 = Integer.parseInt(tmpstr26, 16);
                    String tmpstr27 = tmpstr4.substring(5, 7);
                    int color_g2 = Integer.parseInt(tmpstr27, 16);
                    String tmpstr28 = tmpstr4.substring(7, 9);
                    int color_b2 = Integer.parseInt(tmpstr28, 16);
                    this.m_textColor = (16777216 * color_a) + (65536 * color_r2) + (color_g2 * 256) + color_b2;
                    return;
                } else {
                    return;
                }
            }
            this.m_textColor = Integer.parseInt(tmpstr4, 10);
        }
    }

    public TextRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ATTR = "text_button";
        this.TextRadioButton_textsize_ATTR = "text_size";
        this.TextRadioButton_textpositiony_ATTR = "text_positiony";
        this.TextRadioButton_textcolor_ATTR = "text_textcolor";
        this.TextRadioButton_textbold_ATTR = "text_bold";
        this.TextAlignVertical = "text_align_vertical";
        this.stringId = 0;
        this.m_textBtn = "";
        this.m_textSize = 0;
        this.m_textPositionY = 0;
        this.m_textColor = 0;
    }

    public void setText(String text) {
        this.m_textBtn = text;
    }

    public void setTextSize(int nSize) {
        this.m_textSize = nSize;
    }

    public void setPositionY(int nPosY) {
        this.m_textPositionY = nPosY;
    }

    @Override // android.widget.TextView
    public void setTextColor(int nColor) {
        this.m_textColor = nColor;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint(1);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(this.m_textColor);
        textPaint.setTextSize(this.m_textSize);
        Rect rtCanvas = new Rect();
        getDrawingRect(rtCanvas);
        int nCenterX = rtCanvas.right / 2;
        canvas.drawText(this.m_textBtn, nCenterX, this.m_textPositionY, textPaint);
    }
}
