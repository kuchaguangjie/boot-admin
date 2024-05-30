package com.hb0730.code.generator.core.config;

import com.hb0730.code.generator.core.enums.OutputFile;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 汇总配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class ConfigBuilder {
    /**
     * 策略配置信息
     */
    private StrategyConfig strategyConfig;

    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 包配置信息
     */
    private final PackageConfig packageConfig;

    /**
     * 路径配置信息
     */
    private final Map<OutputFile, String> pathInfo = new HashMap<>();


    public ConfigBuilder(@Nullable StrategyConfig strategyConfig,
                         @Nullable GlobalConfig globalConfig,
                         @Nullable PackageConfig packageConfig) {
        this.strategyConfig = Optional.ofNullable(strategyConfig).orElse(new StrategyConfig.Builder().build());
        this.globalConfig = Optional.ofNullable(globalConfig).orElse(new GlobalConfig.Builder().build());
        this.packageConfig = Optional.ofNullable(packageConfig).orElse(new PackageConfig.Builder().build());


        this.pathInfo.putAll(new PathInfoHandler(this).getPathInfo());
    }

    /**
     * 设置全局配置
     *
     * @param globalConfig 全局配置
     * @return this
     */
    public ConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    /**
     * 设置策略配置
     *
     * @param strategyConfig 策略配置
     * @return this
     */
    public ConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }
}
