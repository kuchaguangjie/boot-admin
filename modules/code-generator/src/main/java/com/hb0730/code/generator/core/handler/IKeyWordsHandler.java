package com.hb0730.code.generator.core.handler;

import jakarta.validation.constraints.NotNull;

import java.util.Collection;

/**
 * 关键字处理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
public interface IKeyWordsHandler {

    /**
     * 获取关键字
     *
     * @return 关键字集合
     */
    @NotNull
    Collection<String> getKeyWords();

    /**
     * 格式化关键字格式
     *
     * @return 格式
     */
    @NotNull
    String formatStyle();

    /**
     * 是否为关键字
     *
     * @param columnName 字段名称
     * @return 是否为关键字
     */
    boolean isKeyWords(@NotNull String columnName);

    /**
     * 格式化字段
     *
     * @param columnName 字段名称
     * @return 格式化字段
     */
    @NotNull
    default String formatColumn(@NotNull String columnName) {
        return String.format(formatStyle(), columnName);
    }

}
