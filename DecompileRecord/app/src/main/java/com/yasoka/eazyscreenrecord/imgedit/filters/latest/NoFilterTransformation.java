package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;

public class NoFilterTransformation implements Transformation<Bitmap> {
    public String getId() {
        return "NoFilterTransformation";
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        return resource;
    }
}
