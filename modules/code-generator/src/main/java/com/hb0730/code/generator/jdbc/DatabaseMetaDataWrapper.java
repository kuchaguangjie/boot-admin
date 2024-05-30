package com.hb0730.code.generator.jdbc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 数据库数据元包装类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/30
 */
@Slf4j
public class DatabaseMetaDataWrapper {
    private final Connection connection;
    private final String schema;
    private final DatabaseMetaData databaseMetaData;
    private final String catalog;

    /**
     * 构造函数
     *
     * @param connection 连接
     * @param schema     模式,如Mysql的Database name
     */
    public DatabaseMetaDataWrapper(Connection connection, String schema) {
        if (connection == null) {
            throw new IllegalArgumentException("数据库连接不能为空");
        }
        this.connection = connection;
        this.schema = schema;
        try {
            this.databaseMetaData = connection.getMetaData();
            this.catalog = connection.getCatalog();
        } catch (Exception e) {
            throw new IllegalArgumentException("获取数据库元数据失败", e);
        }
    }


    /**
     * 关闭连接
     */
    public void closeConnection() {
        Optional.ofNullable(connection).ifPresent((con) -> {
            try {
                con.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        });
    }


    /**
     * 获取表信息
     *
     * @return 表信息
     */
    public List<Table> getTables() {
        return getTables(null, new String[]{"TABLE"});
    }

    /**
     * 获取表信息
     *
     * @param tablePattern 表名
     * @param types        类型
     * @return 表信息
     */
    public List<Table> getTables(String tablePattern, String[] types) {
        return getTables(catalog, schema, tablePattern, types);
    }

    /**
     * 获取表信息
     *
     * @param catalog       .
     * @param schemaPattern .
     * @param tablePattern  .
     * @param types         .
     * @return .
     */
    public List<Table> getTables(String catalog, String schemaPattern, String tablePattern, String[] types) {
        List<Table> tables = new ArrayList<>();
        try (ResultSet resultSet = databaseMetaData.getTables(catalog, schemaPattern, tablePattern, types)) {
            while (resultSet.next()) {
                Table table = new Table();
                table.name = resultSet.getString("TABLE_NAME");
                table.comment = resultSet.getString("REMARKS");
                table.type = resultSet.getString("TABLE_TYPE");
                tables.add(table);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("获取表信息失败", e);
        }
        return tables;
    }

    /**
     * 获取表信息
     *
     * @param tableName 表名
     * @return 表信息
     */
    public Table getTableInfo(String catalog, String schema, String tableName) {
        Table table = new Table();
        try (ResultSet resultSet = databaseMetaData.getTables(catalog, schema, tableName, new String[]{"TABLE", "VIEW"})) {
            table.name = tableName;
            while (resultSet.next()) {
                table.comment = resultSet.getString("REMARKS");
                table.type = resultSet.getString("TABLE_TYPE");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取表信息失败", e);
        }
        return table;
    }

    /**
     * 获取表字段信息
     *
     * @param tableName 表名
     * @return 字段信息
     */
    public Map<String, Column> getColumnInfo(String tableName) {
        return getColumnInfo(catalog, schema, tableName);
    }

    /**
     * 获取表字段信息
     *
     * @param tableName 表名
     * @return 字段信息
     */
    public Map<String, Column> getColumnInfo(String catalog, String schema, String tableName) {
        Set<String> primaryKeys = new java.util.HashSet<>();
        try (ResultSet resultSet = databaseMetaData.getPrimaryKeys(catalog, schema, tableName)) {
            while (resultSet.next()) {
                primaryKeys.add(resultSet.getString("COLUMN_NAME").toLowerCase());
            }
            if (primaryKeys.size() > 1) {
                log.warn("表【{}】存在多个主键", tableName);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取表【" + tableName + "】主键信息错误", e);
        }
        Map<String, Column> columns = new java.util.HashMap<>();
        try (ResultSet resultSet = databaseMetaData.getColumns(catalog, schema, tableName, null)) {
            while (resultSet.next()) {
                Column column = new Column();
                String name = resultSet.getString("COLUMN_NAME");
                column.name = name;
                column.type = resultSet.getString("TYPE_NAME");
                column.jdbcType = JDBCType.valueOf(resultSet.getInt("DATA_TYPE"));
                column.comment = resultSet.getString("REMARKS");
//                column.primaryKey = "YES".equalsIgnoreCase(resultSet.getString("IS_AUTOINCREMENT"));
                column.primaryKey = primaryKeys.contains(name);
                column.autoIncrement = "YES".equalsIgnoreCase(resultSet.getString("IS_AUTOINCREMENT"));
//                column.nullable = "YES".equalsIgnoreCase(resultSet.getString("IS_NULLABLE"));
                column.nullable = resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                column.size = resultSet.getInt("COLUMN_SIZE");
                column.decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
                column.defaultValue = resultSet.getString("COLUMN_DEF");
                columns.put(name.toLowerCase(), column);
            }
            return Collections.unmodifiableMap(columns);
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取表【" + tableName + "】字段信息错误", e);
        }
    }


    /**
     * 表信息
     */
    @Getter
    public static class Table {
        /**
         * 表名
         */
        private String name;
        /**
         * 表注释
         */
        private String comment;
        /**
         * 表类型，如VIEW，TABLE
         */
        private String type;

        private boolean isView() {
            return "VIEW".equalsIgnoreCase(type);
        }
    }

    /**
     * 列信息
     */
    @Getter
    public static class Column {
        /**
         * 列名
         */
        private String name;
        /**
         * 列类型
         */
        private String type;
        /**
         * JDBC类型
         */
        private JDBCType jdbcType;
        /**
         * 列注释
         */
        private String comment;
        /**
         * 是否主键
         */
        private boolean primaryKey;
        /**
         * 是否自增
         */
        private boolean autoIncrement;
        /**
         * 是否可空
         */
        private boolean nullable;
        /**
         * 长度
         */
        private int size;
        /**
         * 小数位数
         */
        private int decimalDigits;
        /**
         * 默认值
         */
        private String defaultValue;
    }
}
