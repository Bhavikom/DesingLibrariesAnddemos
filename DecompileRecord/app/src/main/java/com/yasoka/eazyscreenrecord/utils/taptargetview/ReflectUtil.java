package com.yasoka.eazyscreenrecord.utils.taptargetview;

import java.lang.reflect.Field;

class ReflectUtil {
    ReflectUtil() {
    }

    static Object getPrivateField(Object obj, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }
}
