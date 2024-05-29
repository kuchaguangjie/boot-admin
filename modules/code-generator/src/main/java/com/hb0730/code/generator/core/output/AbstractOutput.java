package com.hb0730.code.generator.core.output;

import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.po.TableInfo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Slf4j
public abstract class AbstractOutput implements Output {
    private final AbstractTemplateEngine engine;

    public AbstractOutput(AbstractTemplateEngine engine) {
        this.engine = engine;
    }

    /**
     * 获取模板数据
     *
     * @param tableInfo 表信息
     * @return 模板数据
     */
    public Map<String, Object> getTemplateData(TableInfo tableInfo) {
        return engine.getTemplateData(tableInfo);
    }

    /**
     * 获取需要生成的文件
     *
     * @param entityName 实体名称
     * @return .
     */
    protected abstract File getFile(String entityName);

    /**
     * 获取模板路径
     *
     * @return 模板路径
     */
    protected abstract String getTemplatePath();

    /**
     * 是否覆盖文件
     *
     * @return 是否覆盖文件
     */
    protected abstract boolean isFileOverride();

    /**
     * 是否生成
     *
     * @return 是否生成
     */
    protected abstract boolean isGenerate();

    /**
     * 是否跳过生成
     *
     * @param tableInfo .
     */
    protected boolean isSkip(TableInfo tableInfo) {
        StrategyConfig strategyConfig = this.engine.getConfigBuilder().getStrategyConfig();
        Set<String> excludeTable = strategyConfig.getExcludeTable();
        return excludeTable.contains(tableInfo.getName());
    }

    @Override
    public void outputFile(@Nonnull TableInfo tableInfo, Map<String, Object> dataMap) {
        if (!isGenerate()) {
            log.debug("不生成[{}]的代码！！！", tableInfo.getName());
            return;
        }
        if (isSkip(tableInfo)) {
            log.debug("跳过表[{}]的生成！！！", tableInfo.getName());
            return;
        }
        Map<String, Object> templateData = dataMap;
        if (null == templateData) {
            templateData = getTemplateData(tableInfo);
        }
        File file = getFile(tableInfo.getEntityName());
        String templatePath = getTemplatePath();
        boolean fileOverride = isFileOverride();
        outputFile(file, templateData, templatePath, fileOverride);
    }

    @Override
    public String outputString(@Nonnull TableInfo tableInfo, @Nullable Map<String, Object> dataMap) {
        if (!isGenerate()) {
            log.debug("不生成[{}]的代码！！！", tableInfo.getName());
            return "";
        }
        if (isSkip(tableInfo)) {
            log.debug("跳过表[{}]的生成！！！", tableInfo.getName());
            return "";
        }
        Map<String, Object> templateData = dataMap;
        if (null == templateData) {
            templateData = getTemplateData(tableInfo);
        }
        String templatePath = getTemplatePath();
        try {
            return engine.render(templateData, templatePath);
        } catch (Exception e) {
            log.error("模板[{}]渲染失败！！！", templatePath, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取路径信息
     *
     * @param outputFile 输出文件
     * @return 路径信息
     */
    protected String getPathInfo(OutputFile outputFile) {
        return engine.getConfigBuilder().getPathInfo().get(outputFile);
    }

    /**
     * 输出文件
     *
     * @param file         文件
     * @param templateData 模板数据
     * @param templatePath 模板路径
     * @param fileOverride 是否覆盖
     */
    protected void outputFile(@NotNull File file,
                              Map<String, Object> templateData,
                              @NotNull String templatePath,
                              @NotNull boolean fileOverride) {
        if (isCreate(file, fileOverride)) {
            try {
                //文件目录是否存在
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                //渲染模板
                engine.render(templateData, templatePath, file);

            } catch (Exception e) {
                log.error("文件[{}]生成失败！！！", file.getName(), e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 检查文件是否创建文件
     *
     * @param file         文件
     * @param fileOverride 是否覆盖已有文件
     * @return 是否创建文件
     * @since 3.5.2
     */
    protected boolean isCreate(@NotNull File file, boolean fileOverride) {
        if (file.exists() && !fileOverride) {
            log.warn("文件[{}]已存在，且未开启文件覆盖配置，需要开启配置可到策略配置中设置！！！", file.getName());
        }
        return !file.exists() || fileOverride;
    }
}
