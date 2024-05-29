package com.hb0730.code.generator.core.config;

import com.hb0730.code.generator.core.po.TableInfo;

import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public interface ITemplate {

    /**
     * 渲染数据
     *
     * @param tableInfo 表信息
     * @return 渲染数据
     */
    Map<String, Object> renderData(TableInfo tableInfo);
}
