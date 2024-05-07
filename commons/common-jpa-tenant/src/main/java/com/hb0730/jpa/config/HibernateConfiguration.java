package com.hb0730.jpa.config;

import com.hb0730.base.core.UserContext;
import com.hb0730.base.core.UserInfo;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.jpa.handler.TenantLineHandler;
import com.hb0730.jpa.plugins.TenantSqlInterceptor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Configuration
@EnableConfigurationProperties(HibernateConfiguration.TenantProperties.class)
@Slf4j
public class HibernateConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "boot.admin.tenant", name = "enableTenantInterceptor", havingValue = "true", matchIfMissing = true)
    public TenantSqlInterceptor tenantSqlInterceptor(
            TenantProperties tenantProperties
    ) {
        return new TenantSqlInterceptor(
                new TenantLineHandler() {
                    @Override
                    public Expression getTenantId() {
                        UserInfo userInfo = UserContext.get();
                        if (userInfo == null) {
                            log.warn("无法获取有效的租户id - sysCode");
                            return new NullValue();
                        }
                        String sysCode = userInfo.getSysCode();
                        if (StrUtil.isBlank(sysCode)) {
                            log.warn("无法获取有效的租户id - sysCode");
                            return new NullValue();
                        }
                        return new StringValue(sysCode);
                    }

                    @Override
                    public String getTenantIdColumn() {
                        return tenantProperties.getTenantColumn();
                    }

                    @Override
                    public boolean ignoreTable(String tableName) {
                        return tenantProperties.getIgnoreTables().contains(tableName);
                    }
                }
        );
    }

    /**
     * 租户拦截器
     *
     * @return HibernatePropertiesCustomizer
     */
    @Bean
    @ConditionalOnProperty(prefix = "boot.admin.tenant", name = "enableTenantInterceptor", havingValue = "true", matchIfMissing = true)
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
            TenantSqlInterceptor tenantSqlInterceptor) {
        return hibernateProperties -> {
            hibernateProperties.put(
                    org.hibernate.cfg.AvailableSettings.STATEMENT_INSPECTOR,
                    tenantSqlInterceptor
            );
        };
    }


    @Data
    @ConfigurationProperties(prefix = "boot.admin.tenant")
    public static class TenantProperties implements Serializable {
        /**
         * 是否启用租户过滤器,
         */
        private boolean enableTenantInterceptor = false;
        /**
         * 非租户表
         */
        private List<String> ignoreTables = List.of(
                "sys_permission",
                "sys_product",
                "sys_product_permission",
                "sys_role",
                "sys_role_permission",
                "sys_user",
                "sys_user_role",
                "sys_notice",
                "sys_quartz_job",
                "sys_oss_config",
                "sys_dict",
                "sys_dict_item",
                "bas_user_role",
                "bas_role_permission",
                "bas_permission",
                "bas_organization",
                "bas_user",
                "bas_notice_record",
                "bas_role"
        );

        /**
         * 租户字段
         */
        private String tenantColumn = "sys_code";
    }
}
