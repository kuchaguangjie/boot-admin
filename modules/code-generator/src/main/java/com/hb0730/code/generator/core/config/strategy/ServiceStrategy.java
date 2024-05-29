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
 * service 策略
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class ServiceStrategy implements ITemplate {
    /**
     * 模板路径
     */
    private String javaTemplate = ConstVal.SERVICE_TEMPLATE;
    /**
     * 实现类模板路径
     */
    private String implJavaTemplate = ConstVal.SERVICE_IMPL_TEMPLATE;

    /**
     * 父类class,带包名
     */
    private String superClass;
    /**
     * 父类class,不带包名
     */
    private String superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;

    /**
     * 是否生成service
     */
    private boolean generateService = true;
    /**
     * 是否生成serviceImpl
     */
    private boolean generateServiceImpl = true;

    /**
     * 转换输出Service文件名称
     */
    private ConverterFileName converterServiceFileName = (entityName -> "I" + entityName + ConstVal.SERVICE);
    /**
     * 转换输出ServiceImpl文件名称
     */
    private ConverterFileName converterServiceImplFileName = (entityName -> entityName + ConstVal.SERVICE_IMPL);
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superServiceClassPackage", superClass);
        data.put("superServiceImplClassPackage", superServiceImplClass);
        data.put("superServiceClass", ClassUtil.getSimpleName(superClass));
        data.put("superServiceImplClass", ClassUtil.getSimpleName(superServiceImplClass));
        data.put("generateService", generateService);
        return data;
    }

    public static class Builder extends BaseBuilder {
        private final ServiceStrategy serviceStrategy = new ServiceStrategy();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * 设置模板路径
         *
         * @param javaTemplate 模板路径
         * @return this
         */
        public Builder javaTemplate(String javaTemplate) {
            this.serviceStrategy.javaTemplate = javaTemplate;
            return this;
        }

        /**
         * 设置实现类模板路径
         *
         * @param implJavaTemplate 实现类模板路径
         * @return this
         */
        public Builder implJavaTemplate(String implJavaTemplate) {
            this.serviceStrategy.implJavaTemplate = implJavaTemplate;
            return this;
        }


        /**
         * 设置父类
         *
         * @param superClass 父类
         * @return this
         */
        public Builder superClass(Class<?> superClass) {
            return superClass(superClass.getName());
        }

        /**
         * 设置父类
         *
         * @param superClass 父类
         * @return this
         */
        public Builder superClass(String superClass) {
            this.serviceStrategy.superClass = superClass;
            return this;
        }

        /**
         * 设置父类
         *
         * @param superServiceImplClass 父类
         * @return this
         */
        public Builder superServiceImplClass(Class<?> superServiceImplClass) {
            return superServiceImplClass(superServiceImplClass.getName());
        }

        /**
         * 设置父类
         *
         * @param superServiceImplClass 父类
         * @return this
         */
        public Builder superServiceImplClass(String superServiceImplClass) {
            this.serviceStrategy.superServiceImplClass = superServiceImplClass;
            return this;
        }

        /**
         * 设置转换输出Service文件名称
         *
         * @param converterServiceFileName 转换输出Service文件名称
         * @return this
         */
        public Builder converterServiceFileName(ConverterFileName converterServiceFileName) {
            this.serviceStrategy.converterServiceFileName = converterServiceFileName;
            return this;
        }

        /**
         * 设置转换输出ServiceImpl文件名称
         *
         * @param converterServiceImplFileName 转换输出ServiceImpl文件名称
         * @return this
         */
        public Builder converterServiceImplFileName(ConverterFileName converterServiceImplFileName) {
            this.serviceStrategy.converterServiceImplFileName = converterServiceImplFileName;
            return this;
        }

        /**
         * 允许覆盖文件
         *
         * @return this
         */
        public Builder enableFileOverride() {
            this.serviceStrategy.fileOverride = true;
            return this;
        }

        /**
         * 禁止覆盖文件
         *
         * @return this
         */
        public Builder disableFileOverride() {
            this.serviceStrategy.fileOverride = false;
            return this;
        }


        /**
         * 允许生成service
         *
         * @return this
         */
        public Builder enableService() {
            this.serviceStrategy.generateService = true;
            return this;
        }

        /**
         * 禁止生成service
         *
         * @return this
         */
        public Builder disableService() {
            this.serviceStrategy.generateService = false;
            return this;
        }

        /**
         * 允许生成serviceImpl
         *
         * @return this
         */
        public Builder enableServiceImpl() {
            this.serviceStrategy.generateServiceImpl = true;
            return this;
        }

        /**
         * 禁止生成serviceImpl
         *
         * @return this
         */
        public Builder disableServiceImpl() {
            this.serviceStrategy.generateServiceImpl = false;
            return this;
        }


        public ServiceStrategy get() {
            return this.serviceStrategy;
        }
    }
}
