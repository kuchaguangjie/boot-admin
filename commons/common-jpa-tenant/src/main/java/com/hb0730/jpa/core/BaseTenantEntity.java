package com.hb0730.jpa.core;

import com.hb0730.jpa.core.domain.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class BaseTenantEntity extends BaseEntity {

    /**
     * 租户ID
     */
    @NotBlank(message = "租户代码不能为空")
    private String sysCode;
}
