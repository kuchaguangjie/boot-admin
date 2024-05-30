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
 * Repository 生成策略
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class RepositoryStrategy implements ITemplate, IStrategy {
    /**
     * 自定义继承的Repository类全称，带包名
     */
    private String superClass = ConstVal.SUPER_REPOSITORY_CLASS;
    /**
     * Repository 模板
     */
    private String javaTemplate = ConstVal.REPOSITORY_TEMPLATE;

    /**
     * 转换输出文件名称
     */
    private ConverterFileName converterFileName = (entityName) -> (entityName + ConstVal.REPOSITORY);
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superRepositoryClassPackage", superClass);
        data.put("superRepositoryClass", ClassUtil.getSimpleName(superClass));
        return data;
    }

    public static class Builder extends BaseBuilder {
        private final RepositoryStrategy repositoryStrategy = new RepositoryStrategy();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * Repository 模板
         *
         * @param javaTemplate 模板
         * @return this
         */
        public Builder javaTemplate(String javaTemplate) {
            repositoryStrategy.javaTemplate = javaTemplate;
            return this;
        }

        /**
         * 自定义继承的Entity类全称，带包名
         *
         * @param superClass 全称
         * @return this
         */
        public Builder superClass(String superClass) {
            repositoryStrategy.superClass = superClass;
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
            repositoryStrategy.converterFileName = converterFileName;
            return this;
        }

        /**
         * 覆盖已有文件
         *
         * @return this
         */
        public Builder enableFileOverride() {
            repositoryStrategy.fileOverride = true;
            return this;
        }

        /**
         * 不覆盖已有文件
         *
         * @return this
         */
        public Builder disableFileOverride() {
            repositoryStrategy.fileOverride = false;
            return this;
        }

        public RepositoryStrategy get() {
            return repositoryStrategy;
        }
    }
}
