package com.hb0730.oss.configuration;

import com.hb0730.base.exception.ServiceException;
import com.hb0730.oss.core.OssStorage;
import com.hb0730.oss.core.s3.S3OssProperties;
import com.hb0730.oss.core.s3.S3OssStorage;
import com.hb0730.oss.factory.OssFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Configuration
public class S3OssConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "boot.admin.oss", name = "type", havingValue = "s3")
    @ConditionalOnMissingBean
    public OssStorage ossStorage(OssProperties properties) {
        S3OssProperties s3 = properties.getS3();
        if (s3 != null) {
            S3OssStorage s3OssStorage = new S3OssStorage(s3);
            OssFactory.registerStorage("DEFAULT", s3OssStorage);
            return s3OssStorage;
        }
        throw new ServiceException("S3 OSS properties cannot be null");
    }
}
