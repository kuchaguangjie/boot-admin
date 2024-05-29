package com.hb0730.code.generator.core.handler;

import com.hb0730.base.utils.StrUtil;
import com.hb0730.code.generator.core.config.ConfigBuilder;
import com.hb0730.code.generator.core.config.GlobalConfig;
import com.hb0730.code.generator.core.config.PackageConfig;
import com.hb0730.code.generator.core.config.StrategyConfig;
import com.hb0730.code.generator.core.config.strategy.EntityStrategy;
import com.hb0730.code.generator.core.config.strategy.RepositoryStrategy;
import com.hb0730.code.generator.core.config.strategy.ServiceStrategy;
import com.hb0730.code.generator.core.enums.ConstVal;
import com.hb0730.code.generator.core.enums.OutputFile;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 路径信息处理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class PathInfoHandler {
    /**
     * 输出目录
     */
    private final String outputDir;

    /**
     * 包配置信息
     */
    private final PackageConfig packageConfig;

    @Getter
    private final Map<OutputFile, String> pathInfo = new HashMap<>();


    public PathInfoHandler(ConfigBuilder configBuilder) {
        this.outputDir = configBuilder.getGlobalConfig().getOutputDir();
        this.packageConfig = configBuilder.getPackageConfig();
        setDefaultPathInfo(
                configBuilder.getGlobalConfig(),
                configBuilder.getStrategyConfig()
        );
    }

    private void setDefaultPathInfo(GlobalConfig globalConfig, StrategyConfig strategyConfig) {
        //entity
        EntityStrategy entityStrategy = strategyConfig.getEntityStrategy();
        putPathInfo(entityStrategy.getJavaTemplate(), OutputFile.entity, ConstVal.PACKAGE_ENTITY);

        //dto
        putPathInfo(strategyConfig.getDtoStrategy().getJavaTemplate(), OutputFile.dto, ConstVal.PACKAGE_DTO);

        // query
        putPathInfo(strategyConfig.getQueryStrategy().getJavaTemplate(), OutputFile.query, ConstVal.PACKAGE_QUERY);

        //repository
        RepositoryStrategy repositoryStrategy = strategyConfig.getRepositoryStrategy();
        putPathInfo(repositoryStrategy.getJavaTemplate(), OutputFile.repository, ConstVal.PACKAGE_REPOSITORY);

        //service
        ServiceStrategy serviceStrategy = strategyConfig.getServiceStrategy();
        putPathInfo(serviceStrategy.getJavaTemplate(), OutputFile.service, ConstVal.PACKAGE_SERVICE);
        putPathInfo(serviceStrategy.getImplJavaTemplate(), OutputFile.serviceImpl, ConstVal.PACKAGE_SERVICE_IMPL);

        //controller
        putPathInfo(strategyConfig.getControllerStrategy().getJavaTemplate(), OutputFile.controller, ConstVal.PACKAGE_CONTROLLER);
    }

    private void putPathInfo(String template, OutputFile outputFile, String module) {
        if (StringUtils.isNotBlank(template)) {
            putPathInfo(outputFile, module);
        }
    }

    private void putPathInfo(OutputFile outputFile, String module) {
        pathInfo.putIfAbsent(outputFile, joinPath(outputDir, packageConfig.getPackage(module)));
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StrUtil.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StrUtil.BACKSLASH + File.separator);
        return parentDir + packageName;
    }
}
