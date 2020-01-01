package com.yasoka.eazyscreenrecord.imgedit.fabric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class AmbilWarnaSquare extends View {
    final float[] color = {1.0f, 1.0f, 1.0f};
    Shader luar;
    Paint paint;

    public AmbilWarnaSquare(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AmbilWarnaSquare(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.paint == null) {
            this.paint = new Paint();
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, (float) getMeasuredHeight(), -1, ViewCompat.MEASURED_STATE_MASK, TileMode.CLAMP);
            this.luar = linearGradient;
        }
        LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, (float) getMeasuredWidth(), 0.0f, -1, Color.HSVToColor(this.color), TileMode.CLAMP);
        this.paint.setShader(new ComposeShader(this.luar, linearGradient2, Mode.MULTIPLY));
        canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), this.paint);
    }

    public void setHue(float f) {
        this.color[0] = f;
        invalidate();
    }
}
