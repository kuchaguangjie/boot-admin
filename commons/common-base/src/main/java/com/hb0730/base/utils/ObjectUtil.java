package com.hb0730.base.utils;

import cn.hutool.core.util.ReflectUtil;

/**
 * 对象工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/11
 */
public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    /**
     * 判断字段是否不为空，任意一个不为空返回true
     *
     * @param obj    对象
     * @param fields 字段
     * @return .
     */
    public static boolean fieldNonNull(Object obj, String... fields) {
        if (obj == null) {
            return false;
        }
        for (String field : fields) {
            try {
                Object value = ReflectUtil.getFieldValue(obj, field);
                if (value != null) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        return false;
    }
}
