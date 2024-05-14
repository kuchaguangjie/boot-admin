package com.hb0730.data.core.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.TenantId;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/14
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class BaseTenantEntity extends BaseEntity {

    /**
     * 租户字段
     */
    @TenantId
    private String sysCode;
}
