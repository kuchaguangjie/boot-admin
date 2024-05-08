package com.hb0730.oss.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Configuration
@ConditionalOnProperty(prefix = "boot.admin.oss", name = "enabled", havingValue = "true")
@Import({AliyunOssConfiguration.class, S3OssConfiguration.class})
@EnableConfigurationProperties({OssProperties.class})
public class OssAutoConfiguration {

}
