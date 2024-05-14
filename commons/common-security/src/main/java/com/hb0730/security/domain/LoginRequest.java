package com.hb0730.security.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录请求
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/3/23
 */
@Getter
@Setter
public class LoginRequest implements Serializable {
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, hidden = true)
    private String username;
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}