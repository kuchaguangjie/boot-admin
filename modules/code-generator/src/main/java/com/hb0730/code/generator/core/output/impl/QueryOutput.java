package com.hb0730.code.generator.core.output.impl;

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
    protected File getFile(String entityName) {
        String filename =
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getQueryStrategy()
                        .getConverterFileName()
                        .converter(entityName);
        String pathInfo = getPathInfo(OutputFile.query);
        String file = pathInfo + File.separator + filename + ".java";
        return this.engine.getConfigBuilder().getStrategyConfig().getOutputFile().createFile(file, OutputFile.query);
    }

    @Override
    protected String getTemplatePath() {
        return this.engine.templateFilePath(
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getQueryStrategy()
                        .getJavaTemplate()
        );
    }

    @Override
    protected boolean isFileOverride() {
        return this.engine.getConfigBuilder().getStrategyConfig().getQueryStrategy().isFileOverride();
    }

    @Override
    protected boolean isGenerate() {
        return true;
    }
}
