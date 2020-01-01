package com.yasoka.eazyscreenrecord.utils.pieview;

public class RadialMenuHelperFunctions {
    public float distance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    public float angle(float f, float f2, float f3, float f4, boolean z, int i) {
        int i2;
        float atan2 = ((float) ((Math.atan2((double) (f4 - f2), (double) (f3 - f)) * 180.0d) / 3.141592653589793d)) + 90.0f + ((float) (z ? (360 / i) / 2 : 0));
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
            i2 = 360 / i;
        } else {
            i2 = 360 / i;
        }
        return atan2 / ((float) i2);
    }
}
