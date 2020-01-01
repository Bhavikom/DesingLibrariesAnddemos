package com.yasoka.eazyscreenrecord.imgedit.filters.internal;

public class RSBlur {
    /* JADX WARNING: Removed duplicated region for block: B:11:0x003c  */
    @android.annotation.TargetApi(18)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap blur(android.content.Context r3, android.graphics.Bitmap r4, int r5) throws android.renderscript.RSRuntimeException {
        /*
            android.renderscript.RenderScript r3 = android.renderscript.RenderScript.create(r3)     // Catch:{ all -> 0x0038 }
            android.renderscript.RenderScript$RSMessageHandler r0 = new android.renderscript.RenderScript$RSMessageHandler     // Catch:{ all -> 0x0036 }
            r0.<init>()     // Catch:{ all -> 0x0036 }
            r3.setMessageHandler(r0)     // Catch:{ all -> 0x0036 }
            android.renderscript.Allocation$MipmapControl r0 = android.renderscript.Allocation.MipmapControl.MIPMAP_NONE     // Catch:{ all -> 0x0036 }
            r1 = 1
            android.renderscript.Allocation r0 = android.renderscript.Allocation.createFromBitmap(r3, r4, r0, r1)     // Catch:{ all -> 0x0036 }
            android.renderscript.Type r1 = r0.getType()     // Catch:{ all -> 0x0036 }
            android.renderscript.Allocation r1 = android.renderscript.Allocation.createTyped(r3, r1)     // Catch:{ all -> 0x0036 }
            android.renderscript.Element r2 = android.renderscript.Element.U8_4(r3)     // Catch:{ all -> 0x0036 }
            android.renderscript.ScriptIntrinsicBlur r2 = android.renderscript.ScriptIntrinsicBlur.create(r3, r2)     // Catch:{ all -> 0x0036 }
            r2.setInput(r0)     // Catch:{ all -> 0x0036 }
            float r5 = (float) r5     // Catch:{ all -> 0x0036 }
            r2.setRadius(r5)     // Catch:{ all -> 0x0036 }
            r2.forEach(r1)     // Catch:{ all -> 0x0036 }
            r1.copyTo(r4)     // Catch:{ all -> 0x0036 }
            if (r3 == 0) goto L_0x0035
            r3.destroy()
        L_0x0035:
            return r4
        L_0x0036:
            r4 = move-exception
            goto L_0x003a
        L_0x0038:
            r4 = move-exception
            r3 = 0
        L_0x003a:
            if (r3 == 0) goto L_0x003f
            r3.destroy()
        L_0x003f:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.imgedit.filters.internal.RSBlur.blur(android.content.Context, android.graphics.Bitmap, int):android.graphics.Bitmap");
    }
}
