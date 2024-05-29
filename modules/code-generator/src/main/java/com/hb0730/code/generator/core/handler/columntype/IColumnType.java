package com.hb0730.code.generator.core.handler.columntype;

/**
 * 获取实体类字段属性类信息接口
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
public interface IColumnType {

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    String getType();

    /**
     * 获取字段类型完整名
     *
     * @return 字段类型完整名
     */
    String getPkg();
}
