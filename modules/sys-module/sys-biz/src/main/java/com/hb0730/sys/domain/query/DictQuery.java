package com.hb0730.sys.domain.query;

import com.hb0730.common.api.BaseQuery;
import com.hb0730.query.annotation.Equals;
import com.hb0730.query.annotation.Like;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class DictQuery extends BaseQuery {

    /**
     * 名称
     */
    @Like
    @Schema(description = "名称")
    private String name;

    /**
     * 类型
     */
    @Equals
    @Schema(description = "类型")
    private String type;


    /**
     * 状态
     */
    @Equals
    @Schema(description = "状态")
    private Boolean enabled;
}
