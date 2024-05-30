# WIP: Code Generator

代码生成器是一个用于生成代码的工具，它可以根据模板生成代码，这样可以减少重复的工作，提高开发效率。

## 项目来源

本项目是从 [mybatis-plus-generator](https://github.com/baomidou/mybatis-plus) 中提取出来的代码生成器，对代码生成器进行了一些修改，使其可以符合项目的需求。

## 使用

### 生成文件

```java
import com.hb0730.code.generator.core.AutoGenerator;
import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.GlobalConfig;
import com.hb0730.code.generator.core.config.PackageConfig;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.engine.FreemarkerTemplateEngine;
import com.hb0730.code.generator.core.metadata.TableInfo;

GlobalConfig.Builder globalConfig = new GlobalConfig.Builder()
        .author("hb0730") // 作者
        .outputDir("D://") // 输出目录
        .commentDate("2024-05-30") // 日期 
        .springdoc(true); // 是否生成springdoc注解

StrategyConfig strategyConfig = new StrategyConfig.Builder()
        .addTablePrefix("t_") // 去除表前缀
        .entityStrategyBuilder() // 实体类策略
        .enableFileOverride() // 是否覆盖文件
        .keyWordsHandler(new MySqlKeyWordsHandler()) // 关键字处理
        .columnTypeConvertHandler(new MysqlColumnTypeConvert()) // 数据库类型转换
        .controllerStrategyBuilder() // controller策略
        .enableFileOverride() // 是否覆盖文件
        .repositoryStrategyBuilder() // repository策略
        .enableFileOverride() // 是否覆盖文件
        .serviceStrategyBuilder() // service策略
        .enableFileOverride() // 是否覆盖文件
        .disableGenerate() // 是否生成
        .serviceImplStrategyBuilder() // impl策略
        .enableFileOverride() // 是否覆盖文件
        .dtoStrategyBuilder() // dto策略
        .enableFileOverride() // 是否覆盖文件
        .chainModel(false) // 是否链式模型
        .queryStrategyBuilder() // query策略
        .enableFileOverride() // 是否覆盖文件
        .build(); // 构建

PackageConfig.Builder packageConfigBuilder = new PackageConfig.Builder();
ConfigBuilder configBuilder = new ConfigBuilder(globalConfig, strategyConfig, packageConfigBuilder);
FreemarkerTemplateEngine templateEngine = new FreemarkerTemplateEngine();
AutoGenerator autoGenerator = new AutoGenerator().configBuilder(configBuilder)
        .execute(tableInfos, templateEngine);

```

## 配置说明

### GlobalConfig 全局配置

| 属性          | 说明              | 默认值                               |
|-------------|-----------------|-----------------------------------|
| author      | 作者              | 默认:`hb0730`                       |
| outputDir   | 生成文件的输出目录       | Windows: `D://` Linux/Mac: `/tmp` |
| commentDate | 是否生成注释中的时间      | 当前时间: `yyyy-MM-dd`                |
| springdoc   | 是否生成springdoc注解 | 默认: `true`                        |

#### 示例

```java
import com.hb0730.code.generator.core.config.GlobalConfig;

GlobalConfig builder = new GlobalConfig.Builder()
        .author("hb0730") // 作者
        .outputDir("D://") // 输出目录
        .commentDate("2024-05-30") // 日期 
        .springdoc(true)
        .build(); // 是否生成springdoc注解
```

### PackageConfig 包配置

| 属性          | 说明           | 默认值                |
|-------------|--------------|--------------------|
| parent      | 父包名          | 默认:`com.hb0730`    |
| moduleName  | 模块名          |                    |
| entity      | 实体类包名        | 默认:`domain.entity` |
| dto         | dto包名        | 默认:`domain.dto`    |
| vo          | vo包名         | 默认:`domain.vo`     |
| query       | query包名      | 默认:`query`         |
| repository  | repository包名 | 默认:`repository`    |
| service     | service包名    | 默认:`service`       |
| serviceImpl | impl包名       | 默认:`service.impl`  |
| controller  | controller包名 | 默认:`controller`    |

#### 示例

```java
import com.hb0730.code.generator.core.config.PackageConfig;

PackageConfig packageConfig = new PackageConfig.Builder()
        .parent("com.hb0730") // 父包名
        .moduleName("test") // 模块名
        .entity("domain.entity") // 实体类包名
        .dto("domain.dto") // dto包名
        .vo("domain.vo") // vo包名
        .query("query") // query包名
        .repository("repository") // repository包名
        .service("service") // service包名
        .serviceImpl("service.impl") // impl包名
        .controller("controller") // controller包名
        .build();
```

### StrategyConfig 策略配置

| 属性                  | 说明           | 默认值 |
|---------------------|--------------|-----|
| tablePrefix         | 需要去除的表前缀     | []  |
| tableSuffix         | 需要去除的表后缀     | []  |
| fieldPrefix         | 需要去除的字段前缀    | []  |
| fieldSuffix         | 需要去除的字段后缀    | []  |
| includeTable        | 需要生成的表       | []  |
| excludeTable        | 需要排除的表       | []  |
| outputFile          | 内置模板输出文件处理   |     |
| entityStrategy      | 实体类策略        |     |
| dtoStrategy         | dto策略        |     |
| WIP: vo             | vo策略         |     |
| queryStrategy       | query策略      |     |
| repositoryStrategy  | repository策略 |     |
| serviceStrategy     | service策略    |     |
| serviceImplStrategy | impl策略       |     |
| controllerStrategy  | controller策略 |     |

#### 示例

```java
import com.hb0730.code.generator.core.config.StrategyConfig;

StrategyConfig strategyConfig = new StrategyConfig.Builder()
        .addTablePrefix("t_")// 去除表前缀
        .addTableSuffix("t") // 去除表后缀
        .addFieldPrefix("f_") // 去除字段前缀
        .addFieldSuffix("f") // 去除字段后缀
        .addIncludeTable("t_user") // 需要生成的表
        .controllerStrategyBuilder() // controller策略
        .repositoryStrategyBuilder() // repository策略
        .serviceStrategyBuilder() // service策略
        .serviceImplStrategyBuilder() // impl策略
        .entityStrategyBuilder() // 实体类策略
        .dtoStrategyBuilder() // dto策略
        .queryStrategyBuilder() // query策略
        .build();
```

#### Entity 策略

### Dto 策略

### Vo 策略

### Query 策略

### Repository 策略

### Service 策略

### ServiceImpl 策略

### Controller 策略

## 感谢

* [mybatis-plus](https://github.com/baomidou/mybatis-plus) 优秀的开源项目。