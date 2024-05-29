package com.hb0730.code.generator.core.engine;

import com.hb0730.base.utils.StrUtil;
import com.hb0730.code.generator.core.config.ConfigBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * freemarker模板引擎
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Slf4j
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {
    private Configuration configuration;


    @Override
    public FreemarkerTemplateEngine init(ConfigBuilder config) {
        this.configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.configuration.setDefaultEncoding("UTF-8");
        this.configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class,
                StrUtil.SLASH);
        return this;
    }

    /**
     * 渲染模板
     *
     * @param object       对象
     * @param templatePath 模板路径
     * @return 渲染后的字符串
     */
    @Override
    public String render(Map<String, Object> object, String templatePath) throws Exception {
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate(templatePath);
            template.process(object, writer);
            return writer.toString();
        }
    }

    /**
     * 渲染模板
     *
     * @param data         数据
     * @param templatePath 模板路径
     * @param outputFile   输出文件
     */
    @Override
    public void render(Map<String, Object> data, String templatePath, File outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            template.process(data, writer);
        }
    }

    /**
     * 模板文件路径
     *
     * @param filePath 文件路径
     * @return 模板文件路径
     */
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }
}
