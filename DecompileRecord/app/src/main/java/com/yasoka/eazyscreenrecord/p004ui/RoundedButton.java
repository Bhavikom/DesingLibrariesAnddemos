package com.yasoka.eazyscreenrecord.p004ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.ezscreenrecorder.C0793R;

/* renamed from: com.ezscreenrecorder.ui.RoundedButton */
public class RoundedButton extends FrameLayout {
    private TextView textView;

    public RoundedButton(@NonNull Context context) {
        super(context, null);
    }

    public RoundedButton(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public RoundedButton(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.textView = new TextView(context, attributeSet, i);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        addView(this.textView, layoutParams);
        setClickable(true);
        setFocusable(true);
    }

    private void initView() {
        inflate(getContext(), C0793R.layout.custom_preview_dialog_native_ad_button, this);
        this.textView = (TextView) findViewById(C0793R.C0795id.id_custom_button_text);
        setClickable(true);
        setFocusable(true);
    }

    public void setText(CharSequence charSequence) {
        this.textView.setText(charSequence);
    }

    public void setText(int i) {
        this.textView.setText(i);
    }
}
