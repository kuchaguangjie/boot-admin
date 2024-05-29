package com.hb0730.code.generator.core.config.strategy;

import com.hb0730.code.generator.core.config.IBuilder;
import com.hb0730.code.generator.core.config.StrategyConfig;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
public class BaseBuilder implements IBuilder<StrategyConfig> {
    private final StrategyConfig strategyConfig;

    public BaseBuilder(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }


    /**
     * entity 策略
     *
     * @return entity 策略
     */
    public EntityStrategy.Builder entityStrategyBuilder() {
        return this.strategyConfig.getEntityStrategyBuilder();
    }

    /**
     * dto 策略
     *
     * @return dto 策略
     */
    public DtoStrategy.Builder dtoStrategyBuilder() {
        return this.strategyConfig.getDtoStrategyBuilder();
    }

    /**
     * query 策略
     *
     * @return query 策略
     */
    public QueryStrategy.Builder queryStrategyBuilder() {
        return this.strategyConfig.getQueryStrategyBuilder();
    }

    /**
     * service 策略
     *
     * @return service 策略
     */
    public ServiceStrategy.Builder serviceStrategyBuilder() {
        return this.strategyConfig.getServiceStrategyBuilder();
    }

    /**
     * repository 策略
     *
     * @return repository 策略
     */
    public RepositoryStrategy.Builder repositoryStrategyBuilder() {
        return this.strategyConfig.getRepositoryStrategyBuilder();
    }

    /**
     * controller 策略
     *
     * @return controller 策略
     */
    public ControllerStrategy.Builder controllerStrategyBuilder() {
        return this.strategyConfig.getControllerStrategyBuilder();
    }

    @Override
    public StrategyConfig build() {
        return strategyConfig;
    }
}
