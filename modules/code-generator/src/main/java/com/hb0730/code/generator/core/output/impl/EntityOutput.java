package com.hb0730.code.generator.core.output.impl;

import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.output.AbstractOutput;

import java.io.File;

/**
 * Entity 输出
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class EntityOutput extends AbstractOutput {
    private final AbstractTemplateEngine engine;

    public EntityOutput(AbstractTemplateEngine engine) {
        super(engine);
        this.engine = engine;
    }


    @Override
    protected File getFile(String entityName) {
        String filename =
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getEntityStrategy()
                        .getConverterFileName()
                        .converter(entityName);
        String pathInfo = getPathInfo(OutputFile.entity);
        String file = pathInfo + File.separator + filename + ".java";
        return this.engine.getConfigBuilder().getStrategyConfig().getOutputFile().createFile(file, OutputFile.entity);
    }

    @Override
    protected String getTemplatePath() {
        return engine.templateFilePath(
                engine.getConfigBuilder()
                        .getStrategyConfig()
                        .getEntityStrategy()
                        .getJavaTemplate()
        );
    }

    @Override
    protected boolean isFileOverride() {
        return this.engine.getConfigBuilder().getStrategyConfig().getEntityStrategy().isFileOverride();
    }

    @Override
    protected boolean isGenerate() {
        return true;
    }
}
