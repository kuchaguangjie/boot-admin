package com.hb0730.code.generator.core.config.strategy;

import cn.hutool.core.util.ReflectUtil;
import com.hb0730.base.utils.ClassUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.code.generator.core.config.ITemplate;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.enums.ConstVal;
import com.hb0730.code.generator.core.handler.ConverterFileName;
import com.hb0730.code.generator.core.handler.IColumnTypeConvertHandler;
import com.hb0730.code.generator.core.handler.IKeyWordsHandler;
import com.hb0730.code.generator.core.handler.INameConvertHandler;
import com.hb0730.code.generator.core.handler.NamingStrategy;
import com.hb0730.code.generator.core.handler.nameconvert.DefaultNameConvert;
import com.hb0730.code.generator.core.po.TableInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Entity 生成策略
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class EntityStrategy implements ITemplate {
    /**
     * Entity 模板
     */
    private String javaTemplate = ConstVal.ENTITY_TEMPLATE;
    /**
     * 名称转换
     */
    private INameConvertHandler nameConvert;
    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superClass = ConstVal.SUPER_ENTITY_CLASS;
    /**
     * 自定义继承的Entity类，公共字段
     */
    private final Set<String> superEntityColumns = new HashSet<>();
    /**
     * 自定义忽略的字段
     */
    private final Set<String> ignoreColumns = new HashSet<>();
    /**
     * 是否生成 serialVersionUID
     */
    private boolean serialVersionUID = true;
    /**
     * 数据库表映射到实体的命名策略，默认下划线转驼峰
     */
    private NamingStrategy namingStrategy = NamingStrategy.underline_to_camel;
    /**
     * 数据库表字段映射到实体的命名策略,如果未指定采用 {@link #getNamingStrategy()}
     */
    private NamingStrategy columnNamingStrategy;
    /**
     * 关键字处理器
     */
    private IKeyWordsHandler keyWordsHandler;
    /**
     * 字段类型转换
     */
    private IColumnTypeConvertHandler columnTypeConvertHandler;

    /**
     * 转换输出文件名称
     */
    private ConverterFileName converterFileName = (entityName) -> entityName;
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride;

    /**
     * 主键类型,用于主键注解
     */
    private String idType = ConstVal.ID_TYPE;

    /**
     * 获取父类字段，通过反射获取
     *
     * @param clazz 类
     */
    public void convertSuperEntityColumns(Class<?> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        this.superEntityColumns.addAll(
                Arrays.stream(fields).map(Field::getName).toList()
        );
    }

    /**
     * 字段命名策略
     *
     * @return .
     */
    @NotNull
    public NamingStrategy columnNamingStrategy() {
        return columnNamingStrategy != null ? columnNamingStrategy : namingStrategy;
    }


    /**
     * 匹配父类字段（忽略大小写)
     *
     * @param fieldName 字段名称
     * @return .
     */
    public boolean matchSuperEntityColumns(String fieldName) {
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * 匹配忽略字段（忽略大小写)
     *
     * @param fieldName 字段名称
     * @return .
     */
    public boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> renderData = new HashMap<>();
        renderData.put("idType", this.idType);
        renderData.put("entitySerialVersionUID", this.serialVersionUID);
        renderData.put("superEntityClass", ClassUtil.getSimpleName(superClass));
        return renderData;

    }


    public static class Builder extends BaseBuilder {
        private final EntityStrategy entityStrategy = new EntityStrategy();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
            this.entityStrategy.nameConvert = new DefaultNameConvert();
        }

        /**
         * 设置Entity 模板
         *
         * @param javaTemplate 模板
         * @return this
         */
        public Builder javaTemplate(String javaTemplate) {
            this.entityStrategy.javaTemplate = javaTemplate;
            return this;
        }

        /**
         * 设置名称转换
         *
         * @param nameConvert 名称转换
         * @return this
         */
        public Builder nameConvert(INameConvertHandler nameConvert) {
            this.entityStrategy.nameConvert = nameConvert;
            return this;
        }

        /**
         * 设置自定义继承的Entity类
         *
         * @param superClass 类
         * @return this
         */
        public Builder superClass(Class<?> superClass) {
            return superClass(superClass.getName());
        }

        /**
         * 设置自定义继承的Entity类全称，带包名
         *
         * @param superClass 全称
         * @return this
         */
        public Builder superClass(String superClass) {
            this.entityStrategy.superClass = superClass;
            return this;
        }

        /**
         * 设置自定义继承的Entity类，公共字段
         *
         * @param superEntityColumns 公共字段
         * @return this
         */
        public Builder superEntityColumns(String... superEntityColumns) {
            this.entityStrategy.superEntityColumns.addAll(Arrays.asList(superEntityColumns));
            return this;
        }

        /**
         * 设置自定义忽略的字段
         *
         * @param ignoreColumns 忽略的字段
         * @return this
         */
        public Builder ignoreColumns(String... ignoreColumns) {
            this.entityStrategy.ignoreColumns.addAll(Arrays.asList(ignoreColumns));
            return this;
        }

        /**
         * 设置是否生成 serialVersionUID
         *
         * @param serialVersionUID 是否生成 serialVersionUID
         * @return this
         */
        public Builder serialVersionUID(boolean serialVersionUID) {
            this.entityStrategy.serialVersionUID = serialVersionUID;
            return this;
        }

        /**
         * 设置数据库表映射到实体的命名策略，默认下划线转驼峰
         *
         * @param namingStrategy 命名策略
         * @return this
         */
        public Builder namingStrategy(NamingStrategy namingStrategy) {
            this.entityStrategy.namingStrategy = namingStrategy;
            return this;
        }

        /**
         * 设置数据库表字段映射到实体的命名策略,如果未指定采用 {@link #namingStrategy}
         *
         * @param columnNamingStrategy 命名策略
         * @return this
         */
        public Builder columnNamingStrategy(NamingStrategy columnNamingStrategy) {
            this.entityStrategy.columnNamingStrategy = columnNamingStrategy;
            return this;
        }

        /**
         * 设置关键字处理器
         *
         * @param keyWordsHandler 关键字处理器
         * @return this
         */
        public Builder keyWordsHandler(IKeyWordsHandler keyWordsHandler) {
            this.entityStrategy.keyWordsHandler = keyWordsHandler;
            return this;
        }

        /**
         * 设置字段类型转换
         *
         * @param columnTypeConvertHandler 字段类型转换
         * @return this
         */
        public Builder columnTypeConvertHandler(IColumnTypeConvertHandler columnTypeConvertHandler) {
            this.entityStrategy.columnTypeConvertHandler = columnTypeConvertHandler;
            return this;
        }

        /**
         * 设置转换输出文件名称
         *
         * @param converterFileName 转换输出文件名称
         * @return this
         */
        public Builder converterFileName(ConverterFileName converterFileName) {
            this.entityStrategy.converterFileName = converterFileName;
            return this;
        }


        /**
         * 禁止覆盖已有文件
         *
         * @return this
         */
        public Builder disableFileOverride() {
            this.entityStrategy.fileOverride = false;
            return this;
        }

        /**
         * 允许覆盖已有文件
         *
         * @return this
         */
        public Builder enableFileOverride() {
            this.entityStrategy.fileOverride = true;
            return this;
        }


        /**
         * 设置主键类型,用于主键注解,类似 {@code com.hb0730.data.core.identifier.IdGenerator}
         *
         * @param idType 主键类型
         * @return this
         */
        public Builder idType(Class<?> idType) {
            return idType(idType.getName());
        }

        /**
         * 设置主键类型,用于主键注解
         *
         * @param idType 主键类型
         * @return this
         */
        public Builder idType(String idType) {
            this.entityStrategy.idType = idType;
            return this;
        }

        public EntityStrategy get() {
            String superClass = this.entityStrategy.getSuperClass();
            if (StrUtil.isNotBlank(superClass)) {
                try {
                    Class<?> clazz = ClassUtil.loadClass(superClass);
                    if (clazz != null) {
                        this.entityStrategy.convertSuperEntityColumns(clazz);
                    }
                } catch (Exception ignored) {
                }
            }
            return entityStrategy;
        }

    }
}
