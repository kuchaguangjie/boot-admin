package com.hb0730.code.generator.core.output.impl;

import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.output.AbstractOutput;

import java.io.File;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class ControllerOutput extends AbstractOutput {
    private final AbstractTemplateEngine engine;

    public ControllerOutput(AbstractTemplateEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected File getFile(String entityName) {
        String filename =
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getControllerStrategy()
                        .getConverterFileName()
                        .converter(entityName);
        String pathInfo = getPathInfo(OutputFile.controller);
        String file = pathInfo + File.separator + filename + ".java";
        return this.engine.getConfigBuilder().getStrategyConfig().getOutputFile().createFile(file, OutputFile.controller);

    }

    @Override
    protected String getTemplatePath() {
        return this.engine.templateFilePath(
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getControllerStrategy()
                        .getJavaTemplate()
        );
    }

    @Override
    protected boolean isFileOverride() {
        return this.engine.getConfigBuilder().getStrategyConfig().getControllerStrategy().isFileOverride();
    }

    @Override
    protected boolean isGenerate() {
        return true;
    }
}
