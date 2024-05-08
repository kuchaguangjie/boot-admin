package com.hb0730.security.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商户信息
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Data
@EqualsAndHashCode
@ToString
public class TenantInfoDto implements Serializable {
    /**
     * 商户名称
     */
    private String name;
    /**
     * 商户logo
     */
    private String logo;

}
