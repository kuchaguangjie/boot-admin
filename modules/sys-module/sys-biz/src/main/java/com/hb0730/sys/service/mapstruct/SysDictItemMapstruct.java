package com.hb0730.sys.service.mapstruct;

import com.hb0730.base.mapstruct.BaseMapstruct;
import com.hb0730.common.domain.vo.SelectOptionVo;
import com.hb0730.sys.domain.dto.DictItemDto;
import com.hb0730.sys.domain.entity.SysDictItem;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@org.mapstruct.Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SysDictItemMapstruct extends BaseMapstruct<DictItemDto, SysDictItem> {


    /**
     * 转换为SelectOption
     *
     * @param dtoList .
     * @return .
     */
    List<SelectOptionVo> toSelectOption(List<DictItemDto> dtoList);
}
