package com.hb0730.common.domain.query;

import com.hb0730.common.api.BaseQuery;
import com.hb0730.query.annotation.Equals;
import com.hb0730.query.annotation.Like;
import com.hb0730.query.annotation.LikeRight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 附件查询
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@Getter
@Setter
@Accessors(chain = true)
public class AttachmentQuery extends BaseQuery {
    @Schema(description = "文件名")
    @LikeRight
    private String displayName;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    @Like
    private String mediaType;
    /**
     * 商户号
     */
    @Schema(description = "商户号", hidden = true)
    @Equals(allowNull = true)
    private String sysCode;
}
