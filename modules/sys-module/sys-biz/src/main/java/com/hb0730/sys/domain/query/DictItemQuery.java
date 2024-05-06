package com.hb0730.sys.domain.query;

import com.hb0730.common.api.BaseQuery;
import com.hb0730.query.annotation.Equals;
import com.hb0730.query.annotation.Like;
import com.hb0730.query.jpa.annotation.Query;
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
public class DictItemQuery extends BaseQuery {

    /**
     * 字典id
     */
    @Query(value = "id", joinName = "dict")
    @Schema(description = "字典id")
    private String dictId;

    /**
     * 字典标签
     */
    @Like
    @Schema(description = "字典标签")
    private String label;

    /**
     * 状态
     */
    @Equals
    @Schema(description = "状态")
    private Boolean enabled;
}
