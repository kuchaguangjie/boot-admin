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
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/30
 */
@Getter
public class ServiceImplStrategy implements ITemplate, IStrategy {
    /**
     * 模板路径
     */
    private String javaTemplate = ConstVal.SERVICE_IMPL_TEMPLATE;

    /**
     * 父类class,不带包名
     */
    private String superClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
    /**
     * 转换输出ServiceImpl文件名称
     */
    private ConverterFileName converterFileName = (entityName -> entityName + ConstVal.SERVICE_IMPL);

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superServiceImplClassPackage", superClass);
        data.put("superServiceImplClass", ClassUtil.getSimpleName(superClass));
        return data;
    }

    public static class Builder extends BaseBuilder {
        private final ServiceImplStrategy serviceImplStrategy = new ServiceImplStrategy();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * 模板路径
         *
         * @param javaTemplate 模板路径
         * @return this
         */
        public Builder javaTemplate(String javaTemplate) {
            serviceImplStrategy.javaTemplate = javaTemplate;
            return this;
        }

        /**
         * 父类class
         *
         * @param superClass 父类class
         * @return this
         */
        public Builder superClass(Class<?> superClass) {
            return superClass(superClass.getName());
        }

        /**
         * 父类class
         *
         * @param superClass 父类class
         * @return this
         */
        public Builder superClass(String superClass) {
            serviceImplStrategy.superClass = superClass;
            return this;
        }

        /**
         * 转换输出ServiceImpl文件名称
         *
         * @param converterFileName 转换输出ServiceImpl文件名称
         * @return this
         */
        public Builder converterFileName(ConverterFileName converterFileName) {
            serviceImplStrategy.converterFileName = converterFileName;
            return this;
        }

        /**
         * 覆盖已有文件
         *
         * @return this
         */
        public Builder enableFileOverride() {
            return fileOverride(true);
        }

        /**
         * 不覆盖已有文件
         *
         * @return this
         */
        public Builder disableFileOverride() {
            return fileOverride(false);
        }

        /**
         * 是否覆盖已有文件
         *
         * @param fileOverride 是否覆盖已有文件
         * @return this
         */
        public Builder fileOverride(boolean fileOverride) {
            serviceImplStrategy.fileOverride = fileOverride;
            return this;
        }

        public ServiceImplStrategy get() {
            return serviceImplStrategy;
        }
    }
}
