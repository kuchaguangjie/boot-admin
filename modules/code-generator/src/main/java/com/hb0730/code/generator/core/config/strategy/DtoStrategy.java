package com.hb0730.code.generator.core.config.strategy;

import com.hb0730.base.utils.ClassUtil;
import com.hb0730.code.generator.core.config.ITemplate;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.enums.ConstVal;
import com.hb0730.code.generator.core.handler.ConverterFileName;
import com.hb0730.code.generator.core.po.TableInfo;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * dto策略
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/29
 */
@Getter
public class DtoStrategy implements ITemplate {
    /**
     * 模板路径
     */
    private String javaTemplate = ConstVal.DTO_TEMPLATE;
    /**
     * 父类,带包名
     */
    private String superClass = ConstVal.SUPER_DTO_CLASS;

    /**
     * 转换输出文件名称
     */
    private ConverterFileName converterFileName = (entityName) -> (entityName + ConstVal.DTO);
    /**
     * 是否生成 serialVersionUID
     */
    private boolean serialVersionUID = true;
    /**
     * 日期格式化
     */
    private String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";
    /**
     * 是否链式模型
     */
    private boolean chainModel = false;

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superDtoClassPackage", superClass);
        data.put("superDtoClass", ClassUtil.getSimpleName(superClass));
        data.put("dtoChainModel", chainModel);
        data.put("dtoSerialVersionUID", serialVersionUID);
        data.put("dtoDateFormatPattern", dateFormatPattern);
        return data;
    }

    public static class Builder extends BaseBuilder {
        private final DtoStrategy dtoStrategy = new DtoStrategy();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * 模板路径
         *
         * @param javaTemplate 模板
         * @return this
         */
        public Builder javaTemplate(String javaTemplate) {
            dtoStrategy.javaTemplate = javaTemplate;
            return this;
        }

        /**
         * 父类
         *
         * @param superClass 父类
         * @return this
         */
        public Builder superClass(Class<?> superClass) {
            return superClass(superClass.getName());
        }

        /**
         * 父类,带包名
         *
         * @param superClass 全称
         * @return this
         */
        public Builder superClass(String superClass) {
            dtoStrategy.superClass = superClass;
            return this;
        }

        /**
         * 转换输出文件名称
         *
         * @param converterFileName 转换
         * @return this
         * @see ConverterFileName
         */
        public Builder converterFileName(ConverterFileName converterFileName) {
            dtoStrategy.converterFileName = converterFileName;
            return this;
        }

        /**
         * 生成 serialVersionUID
         *
         * @return this
         */
        public Builder serialVersionUID() {
            dtoStrategy.serialVersionUID = true;
            return this;
        }

        /**
         * 生成 serialVersionUID
         *
         * @param serialVersionUID 是否生成
         * @return this
         */
        public Builder serialVersionUID(boolean serialVersionUID) {
            dtoStrategy.serialVersionUID = serialVersionUID;
            return this;
        }

        /**
         * 日期格式化
         *
         * @param dateFormatPattern 日期格式化
         * @return this
         */
        public Builder dateFormatPattern(String dateFormatPattern) {
            dtoStrategy.dateFormatPattern = dateFormatPattern;
            return this;
        }

        /**
         * 链式模型
         *
         * @return this
         */
        public Builder chainModel() {
            dtoStrategy.chainModel = true;
            return this;
        }

        /**
         * 链式模型
         *
         * @return this
         */
        public Builder chainModel(boolean chainModel) {
            dtoStrategy.chainModel = chainModel;
            return this;
        }

        /**
         * 覆盖已有文件
         *
         * @return this
         */
        public Builder enableFileOverride() {
            dtoStrategy.fileOverride = true;
            return this;
        }

        /**
         * 不覆盖已有文件
         *
         * @return this
         */
        public Builder disableFileOverride() {
            dtoStrategy.fileOverride = false;
            return this;
        }

        public DtoStrategy get() {
            return dtoStrategy;
        }
    }
}
