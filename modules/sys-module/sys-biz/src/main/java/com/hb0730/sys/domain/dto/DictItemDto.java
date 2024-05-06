package com.hb0730.sys.domain.dto;

import com.hb0730.common.api.domain.BaseDto;
import jakarta.validation.constraints.NotBlank;
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
public class DictItemDto extends BaseDto {
    private String id;
    /**
     * 字典id
     */
    @NotBlank(message = "字典id不能为空")
    private DictSmallDto dict;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典值
     */
    @NotBlank(message = "字典值不能为空")
    private String value;
    /**
     * 标签
     */
    @NotBlank(message = "标签不能为空")
    private String label;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private Boolean enabled;
}
