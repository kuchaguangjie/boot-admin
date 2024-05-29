package com.hb0730.code.generator.core.output.impl;

import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.output.AbstractOutput;

import java.io.File;

/**
 * Repository 输出
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class RepositoryOutput extends AbstractOutput {
    private final AbstractTemplateEngine engine;

    public RepositoryOutput(AbstractTemplateEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected File getFile(String entityName) {
        String filename =
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getRepositoryStrategy()
                        .getConverterFileName()
                        .converter(entityName);
        String pathInfo = getPathInfo(OutputFile.repository);
        String file = pathInfo + File.separator + filename + ".java";
        return this.engine.getConfigBuilder().getStrategyConfig().getOutputFile().createFile(file, OutputFile.repository);
    }

    @Override
    protected String getTemplatePath() {
        return engine.templateFilePath(
                engine.getConfigBuilder()
                        .getStrategyConfig()
                        .getRepositoryStrategy()
                        .getJavaTemplate()
        );
    }

    @Override
    protected boolean isFileOverride() {
        return this.engine.getConfigBuilder().getStrategyConfig().getRepositoryStrategy().isFileOverride();
    }

    @Override
    protected boolean isGenerate() {
        return true;
    }
}
