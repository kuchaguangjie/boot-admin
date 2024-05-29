package com.hb0730.base.utils;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {

    /**
     * 获取类名
     *
     * @param className className 全类名
     * @return ignore
     */
    public static String getSimpleName(String className) {
        return StrUtil.isBlank(className) ? null : className.substring(className.lastIndexOf(StrUtil.DOT) + 1);
    }
}
