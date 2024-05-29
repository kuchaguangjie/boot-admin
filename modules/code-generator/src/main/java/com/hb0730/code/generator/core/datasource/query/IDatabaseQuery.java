package com.hb0730.code.generator.core.datasource.query;

import com.hb0730.code.generator.core.po.TableInfo;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/29
 */
public interface IDatabaseQuery {
    /**
     * 查询表信息
     *
     * @return 表信息
     */
    List<TableInfo> queryTables();
}
