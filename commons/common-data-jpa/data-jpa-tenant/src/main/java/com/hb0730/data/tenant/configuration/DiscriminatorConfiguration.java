package com.hb0730.data.tenant.configuration;

import com.hb0730.data.tenant.hibernate.DiscriminatorTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/14
 */
@Configuration(proxyBeanMethods = false)
public class DiscriminatorConfiguration {

    @Bean
    public HibernatePropertiesCustomizer discriminatorTenantIdentifierResolver() {
        return new DiscriminatorTenantIdentifierResolver();
    }
}
