package com.hb0730.code.generator.core;

import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.GlobalConfig;
import com.hb0730.code.generator.core.config.PackageConfig;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.po.TableInfo;

import java.util.List;
import java.util.function.Consumer;

/**
 * 代码生成器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class AutoGenerator {
    private StrategyConfig.Builder strategyConfigBuilder;
    private GlobalConfig.Builder globalConfigBuilder;
    private PackageConfig.Builder packageConfigBuilder;
    private ConfigBuilder configBuilder;

    public AutoGenerator() {
        this.strategyConfigBuilder = new StrategyConfig.Builder();
        this.globalConfigBuilder = new GlobalConfig.Builder();
        this.packageConfigBuilder = new PackageConfig.Builder();
    }

    /**
     * 全局配置
     *
     * @param consumer 全局配置
     * @return this
     */
    public AutoGenerator globalConfig(Consumer<GlobalConfig.Builder> consumer) {
        consumer.accept(this.globalConfigBuilder);
        return this;
    }

    /**
     * 全局配置
     *
     * @param builder 全局配置
     * @return this
     */
    public AutoGenerator globalConfig(GlobalConfig.Builder builder) {
        this.globalConfigBuilder = builder;
        return this;
    }


    /**
     * 策略配置
     *
     * @param consumer 策略配置
     * @return this
     */
    public AutoGenerator strategyConfig(Consumer<StrategyConfig.Builder> consumer) {
        consumer.accept(this.strategyConfigBuilder);
        return this;
    }

    /**
     * 策略配置
     *
     * @param builder 策略配置
     * @return this
     */
    public AutoGenerator strategyConfig(StrategyConfig.Builder builder) {
        this.strategyConfigBuilder = builder;
        return this;
    }


    /**
     * 包配置
     *
     * @param consumer 包配置
     * @return this
     */
    public AutoGenerator packageConfig(Consumer<PackageConfig.Builder> consumer) {
        consumer.accept(this.packageConfigBuilder);
        return this;
    }

    /**
     * 包配置
     *
     * @param builder 包配置
     * @return this
     */
    public AutoGenerator packageConfig(PackageConfig.Builder builder) {
        this.packageConfigBuilder = builder;
        return this;
    }

    /**
     * 配置构建
     *
     * @param configBuilder 配置构建
     * @return this
     */
    public AutoGenerator configBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }

    /**
     * 执行
     *
     * @param tableInfo      表信息
     * @param templateEngine 模板引擎
     */
    public void execute(List<TableInfo> tableInfo, AbstractTemplateEngine templateEngine) {
        if (this.configBuilder == null) {
            this.configBuilder = new ConfigBuilder(strategyConfigBuilder.build(), globalConfigBuilder.build(), packageConfigBuilder.build());
        }
        templateEngine.setConfigBuilder(this.configBuilder).init(this.configBuilder).batchOutput(tableInfo);
    }
}
