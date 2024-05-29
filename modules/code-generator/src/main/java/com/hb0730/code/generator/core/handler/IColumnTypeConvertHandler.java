package com.hb0730.code.generator.core.handler;

import com.hb0730.code.generator.core.handler.columntype.IColumnType;
import jakarta.validation.constraints.NotNull;

/**
 * 字段类型
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
public interface IColumnTypeConvertHandler {

    /**
     * 执行类型转换
     *
     * @param fieldType 字段类型
     * @return ignore
     */
    IColumnType processTypeConvert(@NotNull String fieldType);

}
