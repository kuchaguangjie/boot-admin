package com.hb0730.code.generator.core.po;

import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.GlobalConfig;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.config.strategy.EntityStrategy;
import com.hb0730.code.generator.core.handler.columntype.IColumnType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 表信息
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
@Getter
public class TableInfo {
    /**
     * 包导入信息
     */
    private final Set<String> importPackages = new TreeSet<>();
    private final Set<String> dtoImportPackages = new TreeSet<>();
    private final Set<String> queryImportPackages = new TreeSet<>();
    /**
     * 实体策略
     */
    private final EntityStrategy entityStrategy;
    private final StrategyConfig strategyConfig;
    private final GlobalConfig globalConfig;
    /**
     * 表名
     */
    private final String name;
    /**
     * 实体名称
     */
    private final String entityName;
    /**
     * 表注释
     */
    private String comment;
    /**
     * 主键
     */
    private boolean hasPk;
    /**
     * 主键类型
     */
    private String pkType;
    /**
     * 字段
     */
    private final List<TableField> fields = new ArrayList<>();
    /**
     * 公共字段
     */
    private final List<TableField> commonFields = new ArrayList<>();

    /**
     * repository 名称
     */
    private String repositoryName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private String dtoName;
    private String voName;
    private String queryName;


    public TableInfo(ConfigBuilder configBuilder, String name) {
        this.strategyConfig = configBuilder.getStrategyConfig();
        this.entityStrategy = configBuilder.getStrategyConfig().getEntityStrategy();
        this.globalConfig = configBuilder.getGlobalConfig();
        this.name = name;
        this.entityName = this.entityStrategy.getNameConvert().entityNameConvert(this);
    }


    public void addField(TableField field) {
        if (this.entityStrategy.matchIgnoreColumns(field.getName())) {
            return;
        }
        if (this.entityStrategy.matchSuperEntityColumns(field.getName())) {
            commonFields.add(field);
        } else {
            fields.add(field);
        }
    }

    /**
     * 添加字段
     *
     * @param fields 字段
     * @return this
     */
    public TableInfo addFields(TableField... fields) {
        for (TableField field : fields) {
            addField(field);
        }
        return this;
    }

    /**
     * 添加字段
     *
     * @param fields 字段
     * @return this
     */
    public TableInfo addFields(List<TableField> fields) {
        fields.forEach(this::addField);
        return this;
    }

    /**
     * 设置主键
     *
     * @param hasPk 是否有主键
     * @return this
     */
    public TableInfo setHasPk(boolean hasPk) {
        this.hasPk = hasPk;
        return this;
    }

    /**
     * 设置主键类型
     *
     * @param pkType 主键类型
     * @return this
     */
    public TableInfo setPkType(String pkType) {
        this.pkType = pkType;
        return this;
    }

    /**
     * 设置表注释
     *
     * @param comment 表注释
     * @return this
     */
    public TableInfo setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * 处理表信息(文件名与导包)
     */
    public void processTable() {
        this.repositoryName = strategyConfig.getRepositoryStrategy().getConverterFileName().converter(this.entityName);
        this.serviceName = strategyConfig.getServiceStrategy().getConverterServiceFileName().converter(this.entityName);
        this.serviceImplName = strategyConfig.getServiceStrategy().getConverterServiceImplFileName().converter(this.entityName);
        this.controllerName = strategyConfig.getControllerStrategy().getConverterFileName().converter(this.entityName);
        this.dtoName = strategyConfig.getDtoStrategy().getConverterFileName().converter(this.entityName);
        this.queryName = strategyConfig.getQueryStrategy().getConverterFileName().converter(this.entityName);

        importPackage();
        dtoImportPackage();
        queryImportPackage();
    }

    /**
     * 导入包
     */
    public void importPackage() {
        String superEntity = this.entityStrategy.getSuperClass();
        if (StrUtil.isNotBlank(superEntity)) {
            importPackages.add(superEntity);
        }
        this.fields.forEach(field -> {
            boolean hasPk = field.isHasPk();
            if (hasPk) {
                importPackages.add("jakarta.persistence.Id");
            }
            boolean autoIncrement = field.isAutoIncrement();
            if (autoIncrement) {
                importPackages.add("jakarta.persistence.GeneratedValue");
            }
            boolean keywords = field.isKeywords();
            if (keywords) {
                importPackages.add("jakarta.persistence.Column");
            }
            IColumnType columnType = field.getColumnType();
            if (null != columnType && StrUtil.isNotBlank(columnType.getPkg())) {
                importPackages.add(columnType.getPkg());
            }
            String propertyType = field.getPropertyType();
            boolean required = field.isRequired();
            if (required && "String".equals(propertyType)) {
                importPackages.add("jakarta.validation.constraints.NotBlank");
            } else if (required) {
                importPackages.add("jakarta.validation.constraints.NotNull");
            }

        });

    }

    /**
     * 导入包
     */
    public void dtoImportPackage() {
        this.fields.forEach(field -> {
            IColumnType columnType = field.getColumnType();
            if (null != columnType && StrUtil.isNotBlank(columnType.getPkg())) {
                dtoImportPackages.add(columnType.getPkg());
            }

            if (field.isQuery()) {
                if (StrUtil.equalsIgnoreCase("String", field.getPropertyType())) {
                    dtoImportPackages.add("jakarta.validation.constraints.NotBlank");
                } else {
                    dtoImportPackages.add("jakarta.validation.constraints.NotNull");
                }
            }
            if (StrUtil.equalsIgnoreCase("Date", field.getPropertyType())) {
                String dateFormatPattern = this.strategyConfig.getDtoStrategy().getDateFormatPattern();
                if (StrUtil.isNotBlank(dateFormatPattern)) {
                    dtoImportPackages.add("com.fasterxml.jackson.annotation.JsonFormat");
                }
            }
        });

        if (CollectionUtil.isNotEmpty(dtoImportPackages)) {
            if (this.globalConfig.isSpringdoc()) {
                dtoImportPackages.add("io.swagger.v3.oas.annotations.media.Schema");
            }
        }
    }

    /**
     * 导入包
     */
    public void queryImportPackage() {
        this.fields.forEach(field -> {
            if (!field.isQuery()) {
                return;
            }
            IColumnType columnType = field.getColumnType();
            if (null != columnType && StrUtil.isNotBlank(columnType.getPkg())) {
                queryImportPackages.add(columnType.getPkg());
            }
            String annotationPackage = field.getQueryTypeAnnotationPackage();
            if (StrUtil.isNotBlank(annotationPackage)) {
                queryImportPackages.add(annotationPackage);
            }
        });

        if (CollectionUtil.isNotEmpty(queryImportPackages)) {
            if (this.globalConfig.isSpringdoc()) {
                queryImportPackages.add("io.swagger.v3.oas.annotations.media.Schema");
            }
        }
    }

}
