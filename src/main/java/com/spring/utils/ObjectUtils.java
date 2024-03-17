package com.spring.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @Author: ly
 * @Package: com.spring.utils
 * @Project: mySpring
 * @name: ObjectUtils
 * @Date:2024/1/27 22:19
 */
public class ObjectUtils {
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }
}
