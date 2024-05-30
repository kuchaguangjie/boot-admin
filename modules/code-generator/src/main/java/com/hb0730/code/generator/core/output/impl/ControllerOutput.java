package com.hb0730.code.generator.core.output.impl;

import com.hb0730.code.generator.core.config.strategy.IStrategy;
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
    protected OutputFile getOutputFile() {
        return OutputFile.controller;
    }

    @Override
    protected String getFilePath(String entityName) {
        String filename = this.getStrategyConfig().getConverterFileName().converter(entityName);
        return getPathInfo(OutputFile.controller) + File.separator + filename + ".java";
    }

    @Override
    protected String getTemplatePath() {
        return this.engine.templateFilePath(
                getStrategyConfig().getJavaTemplate()
        );
    }

    @Override
    protected IStrategy getStrategyConfig() {
        return this.engine
                .getConfigBuilder()
                .getStrategyConfig()
                .getControllerStrategy();
    }
}
