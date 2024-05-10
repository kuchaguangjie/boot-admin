package com.hb0730.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class FilePresignedUrlRespVO implements Serializable {
    /**
     * 上传地址
     */
    @Schema(description = "上传地址", example = "https://s3.cn-north-1.amazonaws.com" +
            ".cn/oss-cn-north-1/2024/5/10/20240510100000-1.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKI")
    private String uploadUrl;
    /**
     * 访问地址
     */
    @Schema(description = "访问地址", example = "https://oss-cn-north-1.amazonaws.com/2024/5/10/20240510100000-1.jpg")
    private String accessUrl;
}
