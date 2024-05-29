package com.hb0730.code.generator.core.handler;

/**
 * 转换输出文件名称
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@FunctionalInterface
public interface ConverterFileName {
    /**
     * 转换文件名称
     *
     * @param entityName 实体名称
     * @return 文件名称
     */
    String converter(String entityName);
}
