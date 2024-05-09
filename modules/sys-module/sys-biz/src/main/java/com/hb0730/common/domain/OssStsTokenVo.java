package com.hb0730.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * OSS 临时授权
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Data
@EqualsAndHashCode
@ToString
public class OssStsTokenVo implements Serializable {
    @Schema(description = "accessKey")
    private String accessKey;
    @Schema(description = "accessSecret")
    private String accessSecret;
    @Schema(description = "securityToken")
    private String securityToken;
    @Schema(description = "expiration", hidden = true)
    @JsonIgnore
    private String expiration;
}
