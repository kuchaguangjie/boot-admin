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
public class DictDto extends BaseDto {
    
    private String id;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    private String name;

    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空")
    private String type;
    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    private Boolean enabled;
}
