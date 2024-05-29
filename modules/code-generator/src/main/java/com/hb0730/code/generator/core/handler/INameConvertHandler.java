package com.hb0730.code.generator.core.handler;

import com.hb0730.code.generator.core.po.TableField;
import com.hb0730.code.generator.core.po.TableInfo;
import jakarta.validation.constraints.NotNull;

/**
 * 名称转换接口类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
public interface INameConvertHandler {
    /**
     * 执行实体名称转换
     *
     * @param tableInfo 表信息对象
     */
    @NotNull
    String entityNameConvert(@NotNull TableInfo tableInfo);

    /**
     * 执行属性名称转换
     *
     * @param field 表字段对象，如果属性表字段命名不一致注意 convert 属性的设置
     */
    @NotNull
    String propertyNameConvert(@NotNull TableField field);

}
