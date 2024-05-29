package com.hb0730.code.generator.core;

import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.GlobalConfig;
import com.hb0730.code.generator.core.config.PackageConfig;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.engine.FreemarkerTemplateEngine;
import com.hb0730.code.generator.core.handler.columntype.MysqlColumnTypeConvert;
import com.hb0730.code.generator.core.handler.keywords.MySqlKeyWordsHandler;
import com.hb0730.code.generator.core.po.TableField;
import com.hb0730.code.generator.core.po.TableInfo;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

class AutoGeneratorTest {

    @org.junit.jupiter.api.Test
    @DisplayName("测试")
    void execute() {

        GlobalConfig.Builder globalConfig = new GlobalConfig.Builder();
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addTablePrefix("t_")
                .entityStrategyBuilder()
                .enableFileOverride()
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .columnTypeConvertHandler(new MysqlColumnTypeConvert())
                .controllerStrategyBuilder()
                .enableFileOverride()
                .repositoryStrategyBuilder()
                .enableFileOverride()
                .serviceStrategyBuilder()
                .enableFileOverride()
                .disableService()
                .dtoStrategyBuilder()
                .enableFileOverride()
                .chainModel(false)
                .queryStrategyBuilder()
                .enableFileOverride()
                .build();
        PackageConfig.Builder packageConfigBuilder = new PackageConfig.Builder();

        ConfigBuilder configBuilder = new ConfigBuilder(strategyConfig, globalConfig.build(), packageConfigBuilder.build());
        AutoGenerator autoGenerator = new AutoGenerator().configBuilder(configBuilder);

        List<TableInfo> tableInfos = testData(configBuilder);
        FreemarkerTemplateEngine templateEngine = new FreemarkerTemplateEngine();

        autoGenerator.execute(tableInfos, templateEngine);

    }

    List<TableInfo> testData(ConfigBuilder config) {
        List<TableField> tableFields = new ArrayList<>();
        TableField tableField = new TableField(config, "id");
        tableField.setType("int(11)");
        tableField.setPk(true);
        tableField.setRequired(true);
        tableField.setComment("id");
        tableFields.add(tableField);
        TableField tableField1 = new TableField(config, "name");
        tableField1.setRequired(true);
        tableField1.setType("varchar(255)");
        tableField1.setComment("name");
        tableField1.setQuery(true);
        tableField1.setQueryType("like");
        tableFields.add(tableField1);
        TableField tableField2 = new TableField(config, "age");
        tableField2.setRequired(true);
        tableField2.setType("int(11)");
        tableField2.setComment("age");
        tableField2.setQuery(true);
        tableField2.setQueryType("eq");
        tableFields.add(tableField2);

        List<TableInfo> tableInfos = new ArrayList<>();
        TableInfo tableInfo = new TableInfo(config, "t_user");
        tableInfo.setComment("用户表");
        tableInfo.setHasPk(true);
        tableInfo.setPkType(tableField.getPropertyType());
        tableInfo.addFields(tableFields);
        tableInfos.add(tableInfo);
        //导入包
        tableInfo.processTable();
        return tableInfos;

    }
}