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
 * query 模板
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/29
 */
@Getter
public class QueryStrategy implements ITemplate, IStrategy {
    /**
     * 模板路径
     */
    private String javaTemplate = ConstVal.QUERY_TEMPLATE;
    /**
     * 父类,带包名
     */
    private String superClass = ConstVal.SUPER_QUERY_CLASS;

    /**
     * 转换输出文件名称
     */
    private ConverterFileName converterFileName = (entityName) -> (entityName + ConstVal.QUERY);
    /**
     * 是否生成 serialVersionUID
     */
    private boolean serialVersionUID = true;
    /**
     * 是否链式模型
     */
    private boolean chainModel = false;
    /**
     * 是否覆盖文件
     */
    private boolean fileOverride;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superQueryClassPackage", superClass);
        data.put("superQueryClass", ClassUtil.getSimpleName(superClass));
        data.put("queryChainModel", chainModel);
        data.put("querySerialVersionUID", serialVersionUID);
        return data;
    }


    public static class Builder extends BaseBuilder {
        private final QueryStrategy queryStrategy = new QueryStrategy();

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
            queryStrategy.javaTemplate = javaTemplate;
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
         * 父类
         *
         * @param superClass 父类
         * @return this
         */
        public Builder superClass(String superClass) {
            queryStrategy.superClass = superClass;
            return this;
        }

        /**
         * 转换输出文件名称
         *
         * @param converterFileName 转换输出文件名称
         * @return this
         */
        public Builder converterFileName(ConverterFileName converterFileName) {
            queryStrategy.converterFileName = converterFileName;
            return this;
        }

        /**
         * 是否生成 serialVersionUID
         *
         * @return this
         */
        public Builder serialVersionUID() {
            queryStrategy.serialVersionUID = true;
            return this;
        }

        /**
         * 是否生成 serialVersionUID
         *
         * @param serialVersionUID 是否生成
         * @return this
         */
        public Builder serialVersionUID(boolean serialVersionUID) {
            queryStrategy.serialVersionUID = serialVersionUID;
            return this;
        }

        /**
         * 链式模型
         *
         * @return this
         */
        public Builder chainModel() {
            queryStrategy.chainModel = true;
            return this;
        }

        /**
         * 链式模型
         *
         * @param chainModel 是否链式模型
         * @return this
         */
        public Builder chainModel(boolean chainModel) {
            queryStrategy.chainModel = chainModel;
            return this;
        }

        /**
         * 覆盖文件
         *
         * @return this
         */
        public Builder enableFileOverride() {
            queryStrategy.fileOverride = true;
            return this;
        }

        /**
         * 禁止覆盖文件
         *
         * @return this
         */
        public Builder disabledFileOverride() {
            queryStrategy.fileOverride = false;
            return this;
        }

        /**
         * 覆盖文件
         *
         * @param fileOverride 是否覆盖
         * @return this
         */
        public Builder fileOverride(boolean fileOverride) {
            queryStrategy.fileOverride = fileOverride;
            return this;
        }

        public QueryStrategy get() {
            return queryStrategy;
        }


    }


}
