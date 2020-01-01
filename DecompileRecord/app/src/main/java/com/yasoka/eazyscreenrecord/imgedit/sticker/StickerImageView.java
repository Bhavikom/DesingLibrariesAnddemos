package com.yasoka.eazyscreenrecord.imgedit.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class StickerImageView extends StickerView {
    private ImageView iv_main;
    private String owner_id;

    public StickerImageView(Context context) {
        super(context);
    }

    public StickerImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StickerImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setOwnerId(String str) {
        this.owner_id = str;
    }

    public String getOwnerId() {
        return this.owner_id;
    }

    public View getMainView() {
        if (this.iv_main == null) {
            this.iv_main = new ImageView(getContext());
            this.iv_main.setScaleType(ScaleType.FIT_XY);
        }
        return this.iv_main;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.iv_main.setImageBitmap(bitmap);
    }

    public void setImageResource(int i) {
        this.iv_main.setImageResource(i);
    }

    public void setImageDrawable(Drawable drawable) {
        this.iv_main.setImageDrawable(drawable);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) this.iv_main.getDrawable()).getBitmap();
    }
}
