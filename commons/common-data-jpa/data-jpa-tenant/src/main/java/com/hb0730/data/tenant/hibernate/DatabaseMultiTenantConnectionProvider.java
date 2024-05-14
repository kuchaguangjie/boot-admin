package com.hb0730.data.tenant.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 独立数据库,租户连接提供者
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/14
 */
@Slf4j
public class DatabaseMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> implements HibernatePropertiesCustomizer {
    private final Map<String, DataSource> dataSources = new HashMap<>();
    private final DataSource defaultDataSource;
    private boolean initialized = false;

    public DatabaseMultiTenantConnectionProvider(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
        this.dataSources.put("default", defaultDataSource);
    }

    /**
     * 初始化,相关数据源
     */
    private void initialize() {
        initialized = true;
        // 加载
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return this.defaultDataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!initialized) {
            initialize();
        }
        DataSource dataSource = dataSources.get(tenantIdentifier);
        if (dataSource != null) {
            log.debug("change multi tenant datasource for tenant_id: {}", tenantIdentifier);
            return dataSource;
        }
        log.warn("tenant_id: {} not found, use default datasource", tenantIdentifier);
        return defaultDataSource;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(
                AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER,
                this
        );
    }
}
