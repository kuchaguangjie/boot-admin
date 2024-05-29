package com.hb0730.code.generator.core.po;

import com.hb0730.base.utils.ClassUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.config.strategy.EntityStrategy;
import com.hb0730.code.generator.core.handler.IColumnTypeConvertHandler;
import com.hb0730.code.generator.core.handler.IKeyWordsHandler;
import com.hb0730.code.generator.core.handler.columntype.IColumnType;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;

import java.sql.JDBCType;

/**
 * 表信息
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
@Getter
public class TableField {
    private final EntityStrategy entityStrategy;
    private final StrategyConfig strategyConfig;
    /**
     * 字段名
     */
    private String name;
    /**
     * 处理后的列名，如`desc`等关键字
     */
    private String columnName;
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段属性名称，驼峰命名，如`user_name` => `userName`，去除前缀和指定的字符，如`tb_user` => `user`
     */
    private String propertyName;

    /**
     * 字段类型
     */
    private IColumnType columnType;
    /**
     * 是否主键
     */
    private boolean hasPk;
    /**
     * 是否自增
     */
    private boolean autoIncrement;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 是否关键字
     */
    private boolean keywords;
    /**
     * 字段元数据信息
     *
     * @since 3.5.0
     */
    private MetaInfo metaInfo;
    /**
     * 是否查询字段
     */
    private boolean query;
    /**
     * 查询类型,如: eq, ne, gt, ge, lt, le, like, notLike, likeLeft, likeRight, in, notIn, isNull, isNotNull, between, notBetween
     */
    private String queryType;


    public TableField(ConfigBuilder configBuilder, String name) {
        this.strategyConfig = configBuilder.getStrategyConfig();
        this.entityStrategy = configBuilder.getStrategyConfig().getEntityStrategy();
        this.name = name;
        this.propertyName = entityStrategy.getNameConvert().propertyNameConvert(this);
        this.keywords = entityStrategy.getKeyWordsHandler().isKeyWords(name);
        if (this.keywords) {
            this.columnName = entityStrategy.getKeyWordsHandler().formatColumn(name);
        }
    }

    /**
     * 设置列名
     *
     * @param columnName 列名
     * @return this
     */
    public TableField setColumnName(String columnName) {
        this.columnName = columnName;
        IKeyWordsHandler keyWordsHandler = entityStrategy.getKeyWordsHandler();
        if (keyWordsHandler != null) {
            this.keywords = keyWordsHandler.isKeyWords(columnName);
        }
        return this;
    }


    /**
     * 设置属性名
     *
     * @param propertyName 属性名
     * @return this
     */
    public TableField setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    /**
     * 获取属性类型
     *
     * @return .
     */
    public String getPropertyType() {
        return columnType.getType();
    }


    /**
     * 设置注释
     *
     * @param comment .
     * @return .
     */
    public TableField setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * 设置主键
     *
     * @param autoIncrement 是否自增
     * @return .
     */
    public TableField setPk(boolean autoIncrement) {
        this.hasPk = true;
        this.autoIncrement = autoIncrement;
        return this;
    }

    /**
     * 设置字段类型
     *
     * @param type 字段类型
     * @return .
     */
    public TableField setType(String type) {
        this.type = type;
        IColumnTypeConvertHandler columnTypeConvertHandler = entityStrategy.getColumnTypeConvertHandler();
        if (columnTypeConvertHandler != null) {
            this.columnType = columnTypeConvertHandler.processTypeConvert(type);
        }
        return this;
    }

    /**
     * 设置字段类型
     *
     * @param columnType 字段类型
     * @return .
     */
    public TableField setColumnType(IColumnType columnType) {
        this.columnType = columnType;
        return this;
    }

    /**
     * 设置是否必填
     *
     * @param required 是否必填
     * @return .
     */
    public TableField setRequired(boolean required) {
        this.required = required;
        return this;
    }


    /**
     * 设置字段元数据信息
     *
     * @param metaInfo 字段元数据信息
     * @return .
     */
    public TableField setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
        return this;
    }

    /**
     * 设置是否查询字段
     *
     * @param query 是否查询字段
     * @return .
     */
    public TableField setQuery(boolean query) {
        this.query = query;
        return this;
    }

    /**
     * 设置查询类型
     *
     * @param queryType 查询类型
     * @return .
     */
    public TableField setQueryType(String queryType) {
        this.queryType = queryType;
        return this;
    }

    /**
     * 获取查询类型注解包
     *
     * @return .
     */
    @Nullable
    public String getQueryTypeAnnotationPackage() {
        QueryTypeEnums queryTypeEnums = QueryTypeEnums.getQueryTypeEnums(queryType);
        if (queryTypeEnums != null) {
            return queryTypeEnums.getAnnotation();
        }
        return null;
    }

    /**
     * 获取查询类型注解
     *
     * @return .
     */
    public String getQueryTypeClass() {
        String annotationPackage = getQueryTypeAnnotationPackage();
        if (StrUtil.isNotBlank(annotationPackage)) {
            return ClassUtil.getSimpleName(annotationPackage);
        }
        return null;
    }


    /**
     * 元数据信息
     *
     * @author nieqiurong 2021/2/8
     */
    @Data
    public static class MetaInfo {

        /**
         * 表名称
         */
        private String tableName;

        /**
         * 字段名称
         */
        private String columnName;

        /**
         * 字段长度
         */
        private int length;

        /**
         * 是否非空
         */
        private boolean nullable;

        /**
         * 字段注释
         */
        private String remarks;

        /**
         * 字段默认值
         */
        private String defaultValue;

        /**
         * 字段精度
         */
        private int scale;

        /**
         * JDBC类型
         */
        private JDBCType jdbcType;
    }


    @Getter
    public static enum QueryTypeEnums {
        EQ("eq", "com.hb0730.query.annotation.Equals", "等值条件"),
        NE("ne", "com.hb0730.query.annotation.NotEquals", "不等值条件"),
        GT("gt", "com.hb0730.query.annotation.GreaterThan", "大于条件"),
        GE("ge", "com.hb0730.query.annotation.GreaterThanOrEquals", "大于等于条件"),
        LT("lt", "com.hb0730.query.annotation.LessThan", "小于条件"),
        LE("le", "com.hb0730.query.annotation.LessThanOrEquals", "小于等于条件"),
        LIKE("like", "com.hb0730.query.annotation.Like", "模糊查询"),
        ;
        private final String type;
        private final String annotation;
        private final String desc;

        QueryTypeEnums(String type, String annotation, String desc) {
            this.type = type;
            this.annotation = annotation;
            this.desc = desc;
        }

        @Nullable
        public static QueryTypeEnums getQueryTypeEnums(String type) {
            for (QueryTypeEnums value : values()) {
                if (value.getType().equals(type)) {
                    return value;
                }
            }
            return null;
        }
    }
}
