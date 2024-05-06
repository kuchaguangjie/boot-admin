package com.hb0730.sys.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class DictSmallDto implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id不能为空")
    private String id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 类型
     */
    @NotBlank(message = "类型不能为空")
    private String type;
}
