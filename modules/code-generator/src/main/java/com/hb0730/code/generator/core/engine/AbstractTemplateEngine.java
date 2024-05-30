package com.hb0730.code.generator.core.engine;

import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.GlobalConfig;
import com.hb0730.code.generator.core.config.PackageConfig;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.output.impl.ControllerOutput;
import com.hb0730.code.generator.core.output.impl.DtoOutput;
import com.hb0730.code.generator.core.output.impl.EntityOutput;
import com.hb0730.code.generator.core.output.impl.QueryOutput;
import com.hb0730.code.generator.core.output.impl.RepositoryOutput;
import com.hb0730.code.generator.core.output.impl.ServiceImplOutput;
import com.hb0730.code.generator.core.output.impl.ServiceOutput;
import com.hb0730.code.generator.core.po.TableInfo;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public abstract class AbstractTemplateEngine {

    /**
     * 配置信息
     */
    private ConfigBuilder configBuilder;

    /**
     * 初始化配置
     *
     * @param config 配置
     */
    public abstract AbstractTemplateEngine init(ConfigBuilder config);

    /**
     * 渲染模板
     *
     * @param dataMap      数据
     * @param templatePath 模板路径
     * @return 渲染后的字符串
     */
    public abstract String render(Map<String, Object> dataMap, String templatePath) throws Exception;

    /**
     * 渲染模板
     *
     * @param dataMap      数据
     * @param templatePath 模板路径
     * @param outputFile   输出文件
     */
    public abstract void render(Map<String, Object> dataMap, String templatePath, File outputFile) throws Exception;

    /**
     * 模板文件路径
     *
     * @param templatePath 模板路径
     * @return 模板文件路径
     */
    public abstract String templateFilePath(String templatePath);

    /**
     * 获取模板数据
     *
     * @param tableInfo 表信息
     * @return 模板数据
     */
    public Map<String, Object> getTemplateData(TableInfo tableInfo) {
        GlobalConfig globalConfig = this.getConfigBuilder().getGlobalConfig();
        StrategyConfig strategyConfig = this.getConfigBuilder().getStrategyConfig();
        PackageConfig packageConfig = this.getConfigBuilder().getPackageConfig();

        Map<String, Object> data = new java.util.HashMap<>();
        data.putAll(strategyConfig.getEntityStrategy().renderData(tableInfo));
        data.putAll(strategyConfig.getControllerStrategy().renderData(tableInfo));
        data.putAll(strategyConfig.getServiceStrategy().renderData(tableInfo));
        data.putAll(strategyConfig.getServiceImplStrategy().renderData(tableInfo));
        data.putAll(strategyConfig.getRepositoryStrategy().renderData(tableInfo));
        data.putAll(strategyConfig.getDtoStrategy().renderData(tableInfo));
        data.putAll(strategyConfig.getQueryStrategy().renderData(tableInfo));


        data.put("author", globalConfig.getAuthor());
        data.put("date", globalConfig.getCommentDate());
        data.put("springdoc", globalConfig.isSpringdoc());
        data.put("package", packageConfig.getPackageInfo());
        data.put("table", tableInfo);
        return data;
    }

    /**
     * 批量输出
     *
     * @param tables 表信息
     */
    public void batchOutput(List<TableInfo> tables) {
        EntityOutput entityOutput = new EntityOutput(this);
        DtoOutput dtoOutput = new DtoOutput(this);
        QueryOutput queryOutput = new QueryOutput(this);
        RepositoryOutput repositoryOutput = new RepositoryOutput(this);
        ServiceOutput serviceOutput = new ServiceOutput(this);
        ServiceImplOutput serviceImplOutput = new ServiceImplOutput(this);
        ControllerOutput controllerOutput = new ControllerOutput(this);
        // 循环生成文件
        for (TableInfo tableInfo : tables) {
            Map<String, Object> templateData = getTemplateData(tableInfo);
            //entity
            entityOutput.outputFile(tableInfo, templateData);
            //dto
            dtoOutput.outputFile(tableInfo, templateData);
            //query
            queryOutput.outputFile(tableInfo, templateData);
            //repository
            repositoryOutput.outputFile(tableInfo, templateData);
            //service
            serviceOutput.outputFile(tableInfo, templateData);
            //serviceimpl
            serviceImplOutput.outputFile(tableInfo, templateData);
            //controller
            controllerOutput.outputFile(tableInfo, templateData);

        }
    }

    @NotNull
    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    @NotNull
    public AbstractTemplateEngine setConfigBuilder(@NotNull ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }
}
