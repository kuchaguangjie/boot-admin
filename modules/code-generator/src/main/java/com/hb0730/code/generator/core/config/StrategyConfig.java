package com.hb0730.code.generator.core.config;

import cn.hutool.core.util.ReUtil;
import com.hb0730.code.generator.core.config.strategy.BaseBuilder;
import com.hb0730.code.generator.core.config.strategy.ControllerStrategy;
import com.hb0730.code.generator.core.config.strategy.DtoStrategy;
import com.hb0730.code.generator.core.config.strategy.EntityStrategy;
import com.hb0730.code.generator.core.config.strategy.QueryStrategy;
import com.hb0730.code.generator.core.config.strategy.RepositoryStrategy;
import com.hb0730.code.generator.core.config.strategy.ServiceStrategy;
import com.hb0730.code.generator.core.handler.IOutputFile;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 策略配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class StrategyConfig {
    /**
     * 过滤表前缀
     * <p>
     * example: addTablePrefix("t_","tb_") <br>
     * result: t_user -> user, tb_user -> user
     */
    private final Set<String> tablePrefix = new HashSet<>();

    /**
     * 过滤表后缀
     * <p>
     * example: addTableSuffix("_t","_tb") <br>
     * result: user_t -> user, user_tb -> user
     */
    private final Set<String> tableSuffix = new HashSet<>();

    /**
     * 过滤字段前缀
     * <p>
     * example: addFieldPrefix("f_","field_") <br>
     * result: f_user -> user, field_user -> user
     */
    private final Set<String> fieldPrefix = new HashSet<>();

    /**
     * 过滤字段后缀
     * <p>
     * example: addFieldSuffix("_f","_field") <br>
     * result: user_f -> user, user_field -> user
     */
    private final Set<String> fieldSuffix = new HashSet<>();
    /**
     * 排除表
     */
    public final Set<String> excludeTable = new HashSet<>();

    private EntityStrategy entityStrategy;
    private final EntityStrategy.Builder entityStrategyBuilder = new EntityStrategy.Builder(this);

    private DtoStrategy dtoStrategy;
    private final DtoStrategy.Builder dtoStrategyBuilder = new DtoStrategy.Builder(this);

    private QueryStrategy queryStrategy;
    private final QueryStrategy.Builder queryStrategyBuilder = new QueryStrategy.Builder(this);

    private RepositoryStrategy repositoryStrategy;
    private final RepositoryStrategy.Builder repositoryStrategyBuilder = new RepositoryStrategy.Builder(this);


    private ServiceStrategy serviceStrategy;
    private final ServiceStrategy.Builder serviceStrategyBuilder = new ServiceStrategy.Builder(this);

    private ControllerStrategy controllerStrategy;
    private final ControllerStrategy.Builder controllerStrategyBuilder = new ControllerStrategy.Builder(this);

    private IOutputFile outputFile = (path, ot) -> new File(path);

    /**
     * 排除表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchExcludeTable(String tableName) {
        return matchTable(tableName, this.getExcludeTable());
    }

    /**
     * 表名任意匹配
     *
     * @param tableName 表名
     * @param machTable 匹配的表名
     * @return 是否匹配
     */
    private boolean matchTable(String tableName, Set<String> machTable) {
        return machTable.stream().anyMatch(t -> tableNameMatches(t, tableName));
    }

    /**
     * 表名匹配
     *
     * @param matchTableName 匹配的表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    private boolean tableNameMatches(String matchTableName, String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || ReUtil.isMatch(matchTableName, dbTableName);
    }

    /**
     * 获取entity策略
     *
     * @return {@link EntityStrategy}
     */
    public EntityStrategy getEntityStrategy() {
        if (entityStrategy == null) {
            entityStrategy = entityStrategyBuilder.get();
        }
        return entityStrategy;
    }

    /**
     * 获取dto策略
     *
     * @return {@link DtoStrategy}
     */
    public DtoStrategy getDtoStrategy() {
        if (dtoStrategy == null) {
            dtoStrategy = dtoStrategyBuilder.get();
        }
        return dtoStrategy;
    }

    /**
     * 获取query策略
     *
     * @return {@link QueryStrategy}
     */
    public QueryStrategy getQueryStrategy() {
        if (queryStrategy == null) {
            queryStrategy = queryStrategyBuilder.get();
        }
        return queryStrategy;
    }

    /**
     * 获取repository策略
     *
     * @return {@link RepositoryStrategy}
     */
    public RepositoryStrategy getRepositoryStrategy() {
        if (repositoryStrategy == null) {
            repositoryStrategy = repositoryStrategyBuilder.get();
        }
        return repositoryStrategy;
    }

    /**
     * 获取service策略
     *
     * @return {@link ServiceStrategy}
     */
    public ServiceStrategy getServiceStrategy() {
        if (serviceStrategy == null) {
            serviceStrategy = serviceStrategyBuilder.get();
        }
        return serviceStrategy;
    }

    /**
     * 获取controller策略
     *
     * @return {@link ControllerStrategy}
     */
    public ControllerStrategy getControllerStrategy() {
        if (controllerStrategy == null) {
            controllerStrategy = controllerStrategyBuilder.get();
        }
        return controllerStrategy;
    }


    public static class Builder extends BaseBuilder {
        private final StrategyConfig strategyConfig;

        public Builder() {
            super(new StrategyConfig());
            this.strategyConfig = super.build();
        }

        /**
         * 增加过滤表前缀
         *
         * @param tablePrefix 过滤表前缀
         * @return this
         */
        public Builder addTablePrefix(@NotNull String... tablePrefix) {
            return addTablePrefix(Arrays.asList(tablePrefix));
        }

        public Builder addTablePrefix(@NotNull List<String> tablePrefixList) {
            this.strategyConfig.tablePrefix.addAll(tablePrefixList);
            return this;
        }

        /**
         * 增加过滤表后缀
         *
         * @param tableSuffix 过滤表后缀
         * @return this
         */
        public Builder addTableSuffix(String... tableSuffix) {
            return addTableSuffix(Arrays.asList(tableSuffix));
        }

        public Builder addTableSuffix(@NotNull List<String> tableSuffixList) {
            this.strategyConfig.tableSuffix.addAll(tableSuffixList);
            return this;
        }

        /**
         * 增加过滤字段前缀
         *
         * @param fieldPrefix 过滤字段前缀
         * @return this
         * @since 3.5.0
         */
        public Builder addFieldPrefix(@NotNull String... fieldPrefix) {
            return addFieldPrefix(Arrays.asList(fieldPrefix));
        }

        public Builder addFieldPrefix(@NotNull List<String> fieldPrefix) {
            this.strategyConfig.fieldPrefix.addAll(fieldPrefix);
            return this;
        }

        /**
         * 增加过滤字段后缀
         *
         * @param fieldSuffix 过滤字段后缀
         * @return this
         */
        public Builder addFieldSuffix(@NotNull String... fieldSuffix) {
            return addFieldSuffix(Arrays.asList(fieldSuffix));
        }

        public Builder addFieldSuffix(@NotNull List<String> fieldSuffixList) {
            this.strategyConfig.fieldSuffix.addAll(fieldSuffixList);
            return this;
        }


        /**
         * 增加排除表
         *
         * @param exclude 排除表
         * @return this
         */
        public Builder addExclude(@NotNull String... exclude) {
            return addExclude(Arrays.asList(exclude));
        }

        public Builder addExclude(@NotNull List<String> excludeList) {
            this.strategyConfig.excludeTable.addAll(excludeList);
            return this;
        }


        /**
         * 文件输出
         *
         * @param outputFile 输出文件
         * @return this
         */
        public Builder outputFile(IOutputFile outputFile) {
            this.strategyConfig.outputFile = outputFile;
            return this;
        }

        @Override
        public StrategyConfig build() {
            return strategyConfig;
        }
    }
}
