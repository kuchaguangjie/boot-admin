package com.hb0730.code.generator.core.output.impl;

import com.hb0730.code.generator.core.config.strategy.IStrategy;
import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.output.AbstractOutput;

import java.io.File;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/29
 */
public class QueryOutput extends AbstractOutput {
    private final AbstractTemplateEngine engine;

    public QueryOutput(AbstractTemplateEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected OutputFile getOutputFile() {
        return OutputFile.query;
    }

    @Override
    protected String getFilePath(String entityName) {
        String filename = this.getStrategyConfig().getConverterFileName().converter(entityName);
        return getPathInfo(OutputFile.query) + File.separator + filename + ".java";
    }

    @Override
    protected String getTemplatePath() {
        return this.engine.templateFilePath(getStrategyConfig().getJavaTemplate());
    }

    @Override
    protected IStrategy getStrategyConfig() {
        return this.engine
                .getConfigBuilder()
                .getStrategyConfig()
                .getQueryStrategy();
    }
}
