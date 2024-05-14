package com.hb0730.data.tenant.hibernate;

import com.hb0730.base.core.TenantContext;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 多租户鉴别器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/14
 */
@Component
public class DiscriminatorTenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>,
        HibernatePropertiesCustomizer {
    @Override
    public String resolveCurrentTenantIdentifier() {
        String sysCode = TenantContext.get();
        if (sysCode != null) {
            return sysCode;
        }
        return "";
    }

    @Override
    public @UnknownKeyFor @NonNull @Initialized boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(
                AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this
        );
    }
}
