package com.hb0730.sys.domain.query;

import com.hb0730.common.api.BaseQuery;
import com.hb0730.query.annotation.Equals;
import com.hb0730.query.annotation.IsNull;
import com.hb0730.query.annotation.Like;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class AreaQuery extends BaseQuery {
    @Equals
    @Schema(description = "父ID,为空则查询顶级")
    private String parentId;
    /**
     * 父级id为空
     */
    @Schema(description = "父级id为空", hidden = true)
    @IsNull("parentId")
    private String parentIdIsNull;

    /**
     * 名称
     */
    @Like
    @Schema(description = "名称")
    private String name;

    /**
     * 编码
     */
    @Equals
    @Schema(description = "编码")
    private String code;


    /**
     * 状态
     */
    @Equals
    @Schema(description = "状态")
    private Boolean enabled;
}
