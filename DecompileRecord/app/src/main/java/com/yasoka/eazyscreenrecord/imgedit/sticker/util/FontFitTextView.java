package com.yasoka.eazyscreenrecord.imgedit.sticker.util;

import android.content.Context;
import android.graphics.Paint;
import android.support.p003v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

public class FontFitTextView extends AppCompatTextView {
    private Paint mTestPaint;

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialise();
    }

    private void initialise() {
        this.mTestPaint = new Paint();
        this.mTestPaint.set(getPaint());
    }

    private void refitText(String str, int i) {
        if (i > 0) {
            int paddingLeft = (i - getPaddingLeft()) - getPaddingRight();
            float f = 240.0f;
            this.mTestPaint.set(getPaint());
            float f2 = 2.0f;
            while (f - f2 > 0.5f) {
                float f3 = (f + f2) / 2.0f;
                this.mTestPaint.setTextSize(f3);
                if (this.mTestPaint.measureText(str) >= ((float) paddingLeft)) {
                    f = f3;
                } else {
                    f2 = f3;
                }
            }
            setTextSize(0, f2);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = MeasureSpec.getSize(i);
        int measuredHeight = getMeasuredHeight();
        refitText(getText().toString(), size);
        setMeasuredDimension(size, measuredHeight);
    }

    /* access modifiers changed from: protected */
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        refitText(charSequence.toString(), getWidth());
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i != i3) {
            refitText(getText().toString(), i);
        }
    }
}
