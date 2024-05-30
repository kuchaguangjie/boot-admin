package com.hb0730.code.generator.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

class DatabaseMetaDataWrapperTest {


    /**
     * 获取数据源
     *
     * @return .
     */
    DataSource dataDriver() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/boot-admin?useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123456");
        return hikariDataSource;
    }


    /**
     * 获取表
     */
    @Test
    @DisplayName("获取表")
    void getTables() throws Exception {
        DataSource dataSource = dataDriver();
        DatabaseMetaDataWrapper databaseMetaDataWrapper = new DatabaseMetaDataWrapper(dataSource.getConnection(), "boot-admin");
        List<DatabaseMetaDataWrapper.Table> tables = databaseMetaDataWrapper.getTables();
        Assertions.assertNotNull(tables);
    }

    /**
     * 获取列信息
     *
     * @throws Exception .
     */
    @DisplayName("获取列信息")
    @Test
    void getColumn() throws Exception {
        DataSource dataSource = dataDriver();
        DatabaseMetaDataWrapper databaseMetaDataWrapper = new DatabaseMetaDataWrapper(dataSource.getConnection(), "boot-admin");
        Map<String, DatabaseMetaDataWrapper.Column> columns = databaseMetaDataWrapper.getColumnInfo("sys_user");
        Assertions.assertNotNull(columns);
    }
}