package com.hb0730.code.generator.core.output.impl;

import com.hb0730.code.generator.core.engine.AbstractTemplateEngine;
import com.hb0730.code.generator.core.enums.OutputFile;
import com.hb0730.code.generator.core.output.AbstractOutput;

import java.io.File;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class ServiceImplOutput extends AbstractOutput {
    private final AbstractTemplateEngine engine;

    public ServiceImplOutput(AbstractTemplateEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected File getFile(String entityName) {
        String filename =
                this.engine
                        .getConfigBuilder()
                        .getStrategyConfig()
                        .getServiceStrategy()
                        .getConverterServiceImplFileName()
                        .converter(entityName);
        String pathInfo = getPathInfo(OutputFile.serviceImpl);
        String file = pathInfo + File.separator + filename + ".java";
        return this.engine.getConfigBuilder().getStrategyConfig().getOutputFile().createFile(file, OutputFile.serviceImpl);
    }

    @Override
    protected String getTemplatePath() {
        return engine.templateFilePath(
                engine.getConfigBuilder()
                        .getStrategyConfig()
                        .getServiceStrategy()
                        .getImplJavaTemplate()
        );
    }

    @Override
    protected boolean isFileOverride() {
        return engine.getConfigBuilder().getStrategyConfig().getServiceStrategy().isFileOverride();
    }

    @Override
    protected boolean isGenerate() {
        return engine.getConfigBuilder().getStrategyConfig().getServiceStrategy().isGenerateServiceImpl();
    }
}
