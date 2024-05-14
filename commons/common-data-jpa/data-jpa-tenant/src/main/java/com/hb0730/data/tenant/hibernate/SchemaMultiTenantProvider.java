package com.hb0730.data.tenant.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Schema多租户提供者
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/14
 */
@Slf4j
public class SchemaMultiTenantProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {
    private final DataSource defaultDataSource;

    public SchemaMultiTenantProvider(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return defaultDataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String schema) throws SQLException {
        Connection connection = getAnyConnection();
        connection.setSchema(schema);
        log.debug("change schema for tenant_id: {}", schema);
        return connection;
    }

    @Override
    public void releaseConnection(String schema, Connection connection) throws SQLException {
        connection.setSchema("default");
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public @UnknownKeyFor @NonNull @Initialized boolean isUnwrappableAs(@UnknownKeyFor @NonNull @Initialized Class<@UnknownKeyFor @NonNull @Initialized ?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@UnknownKeyFor @NonNull @Initialized Class<T> unwrapType) {
        return null;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(
                AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER,
                this
        );
    }
}
