package com.hb0730.code.generator.core.config;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

/**
 * 全局配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class GlobalConfig {
    /**
     * 生成文件的输出目录【 windows:D://  linux or mac:/tmp 】
     */
    private String outputDir = System.getProperty("os.name").toLowerCase().contains("windows") ? "D://" : "/tmp";
    ;
    /**
     * 作者
     */
    private String author = "<a href=\"mailto:huangbing0730@gmail\">hb0730</a>";
    /**
     * 日期
     */
    private Supplier<String> commentDate = () -> new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    /**
     * 是否生成springdoc
     */
    private boolean springdoc = true;

    public String getCommentDate() {
        return commentDate.get();
    }

    public static class Builder implements IBuilder<GlobalConfig> {
        private final GlobalConfig globalConfig = new GlobalConfig();

        public Builder(String author) {
            globalConfig.author = author;
        }

        public Builder() {
        }

        /**
         * 作者
         *
         * @param author 作者
         */
        public Builder author(Supplier<String> author) {
            globalConfig.author = author.get();
            return this;
        }

        /**
         * 作者
         *
         * @param author 作者
         */
        public Builder author(String author) {
            globalConfig.author = author;
            return this;
        }

        /**
         * 生成文件的输出目录
         *
         * @param outputDir 输出目录
         */
        public Builder outputDir(String outputDir) {
            globalConfig.outputDir = outputDir;
            return this;
        }

        /**
         * 日期
         *
         * @param commentDate 日期
         */
        public Builder commentDate(Supplier<String> commentDate) {
            globalConfig.commentDate = commentDate;
            return this;
        }

        /**
         * 日期
         *
         * @param commentDate 日期
         */
        public Builder commentDate(String commentDate) {
            globalConfig.commentDate = () -> commentDate;
            return this;
        }

        /**
         * 生成springdoc
         */
        public Builder springdoc() {
            globalConfig.springdoc = true;
            return this;
        }

        /**
         * 是否生成springdoc
         */
        public Builder springdoc(boolean springdoc) {
            globalConfig.springdoc = springdoc;
            return this;
        }


        @Override
        public GlobalConfig build() {
            return globalConfig;
        }
    }
}
