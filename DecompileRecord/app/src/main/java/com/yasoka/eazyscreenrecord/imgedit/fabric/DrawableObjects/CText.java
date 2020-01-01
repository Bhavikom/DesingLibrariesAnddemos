package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class CText extends CDrawable {
    private String mText;

    public CText(String str, int i, int i2, Paint paint) {
        setText(str);
        setYcoords(i2);
        setXcoords(i);
        setPaint(paint);
        calculateTextSizes();
    }

    private void calculateTextSizes() {
        Rect rect = new Rect();
        getPaint().getTextBounds(getText(), 0, getText().length(), rect);
        setHeight(rect.height());
        setWidth(rect.width());
    }

    public void setText(String str) {
        this.mText = str;
        calculateTextSizes();
    }

    public String getText() {
        return this.mText;
    }

    public void setPaint(Paint paint) {
        super.setPaint(paint);
        calculateTextSizes();
    }

    public void draw(Canvas canvas) {
        canvas.drawText(getText(), (float) getXcoords(), (float) getYcoords(), getPaint());
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof CPath)) {
            return false;
        }
        CText cText = (CText) obj;
        if (cText.mText == null && this.mText == null) {
            return true;
        }
        return cText.mText.equals(this.mText);
    }
}
