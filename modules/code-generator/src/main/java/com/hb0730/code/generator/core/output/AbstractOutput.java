package com.hb0730.code.generator.core.output;

import com.hb0730.code.generator.core.config.strategy.IStrategy;
import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.po.TableInfo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

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
     * 获取输出文件类型
     *
     * @return 输出文件类型
     */
    protected abstract OutputFile getOutputFile();

    /**
     * 获取文件路径
     *
     * @param entityName 实体名称
     * @return 文件路径
     */
    protected abstract String getFilePath(String entityName);

    /**
     * 获取模板路径
     *
     * @return 模板路径
     */
    protected abstract String getTemplatePath();

    /**
     * 获取策略配置
     *
     * @return 策略配置
     */
    protected abstract IStrategy getStrategyConfig();

    /**
     * 是否跳过生成
     *
     * @param tableInfo .
     */
    protected boolean isSkip(TableInfo tableInfo) {

        //是否排除表
        boolean excludeTable = this.engine
                .getConfigBuilder()
                .getStrategyConfig()
                .matchExcludeTable(tableInfo.getName());
        //是否包含表
        boolean includeTable = this.engine
                .getConfigBuilder()
                .getStrategyConfig()
                .matchTable(
                        tableInfo.getName(),
                        this.engine
                                .getConfigBuilder().
                                getStrategyConfig()
                                .getIncludeTable()
                );
        return excludeTable || (!includeTable && !this.engine
                .getConfigBuilder()
                .getStrategyConfig()
                .getIncludeTable().isEmpty());
    }

    @Override
    public void outputFile(@Nonnull TableInfo tableInfo, Map<String, Object> dataMap) {
        boolean generate = isGenerate(tableInfo);
        if (!generate) {
            return;
        }
        Map<String, Object> templateData = dataMap;
        if (null == templateData) {
            templateData = getTemplateData(tableInfo);
        }
        String filePath = getFilePath(tableInfo.getEntityName());
        File file = getFile(filePath);
        String templatePath = getTemplatePath();
        boolean fileOverride = getStrategyConfig().isFileOverride();
        outputFile(file, templateData, templatePath, fileOverride);
    }

    @Override
    public String outputString(@Nonnull TableInfo tableInfo, @Nullable Map<String, Object> dataMap) {
        boolean generate = isGenerate(tableInfo);
        if (!generate) {
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
     * 获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    protected File getFile(String filePath) {
        return this.engine
                .getConfigBuilder()
                .getStrategyConfig()
                .getOutputFile()
                .createFile(filePath, getOutputFile());
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

    /**
     * 是否生成
     *
     * @param tableInfo 表信息
     * @return 是否生成
     */
    protected boolean isGenerate(TableInfo tableInfo) {
        if (!getStrategyConfig().isGenerate()) {
            log.debug("不生成[{}]的代码！！！", tableInfo.getName());
            return false;
        }
        if (isSkip(tableInfo)) {
            log.debug("跳过表[{}]的生成！！！", tableInfo.getName());
            return false;
        }
        return true;
    }
}
