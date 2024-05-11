package com.hb0730.common.domain.dto;

import com.hb0730.base.utils.TreeUtil;
import com.hb0730.sys.domain.dto.AreaDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class AreaTreeDto extends AreaDto implements TreeUtil.Node<AreaTreeDto, String> {
    private List<AreaTreeDto> children;

    @Override
    public List<AreaTreeDto> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<AreaTreeDto> children) {
        this.children = children;
    }
}
