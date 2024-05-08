package com.hb0730.oss.configuration;

import com.hb0730.base.exception.ServiceException;
import com.hb0730.oss.core.OssStorage;
import com.hb0730.oss.core.aliyun.AliyunOssProperties;
import com.hb0730.oss.core.aliyun.AliyunOssStorage;
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
public class AliyunOssConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "boot.admin.oss", name = "type", havingValue = "aliyun")
    @ConditionalOnMissingBean
    public OssStorage ossStorage(OssProperties properties) {
        AliyunOssProperties aliyun = properties.getAliyun();
        if (aliyun != null) {
            AliyunOssStorage aliyunOssStorage = new AliyunOssStorage(aliyun);
            OssFactory.registerStorage("DEFAULT", aliyunOssStorage);
            return aliyunOssStorage;
        }
        throw new ServiceException("Aliyun OSS properties cannot be null");
    }

}
