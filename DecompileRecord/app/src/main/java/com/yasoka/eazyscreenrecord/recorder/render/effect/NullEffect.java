package com.yasoka.eazyscreenrecord.recorder.render.effect;

import android.content.Context;

import com.yasoka.eazyscreenrecord.recorder.render.GLSLFileUtils;
//import com.appsmartz.recorder.render.GLSLFileUtils;

public class NullEffect extends Effect {
    private static final String NULL_EFFECT_FRAGMENT = "null/fragmentshader.glsl";
    private static final String NULL_EFFECT_VERTEX = "null/vertexshader.glsl";

    public NullEffect(Context context) {
        super.setShader(GLSLFileUtils.getFileContextFromAssets(context, NULL_EFFECT_VERTEX), GLSLFileUtils.getFileContextFromAssets(context, NULL_EFFECT_FRAGMENT));
    }
}
