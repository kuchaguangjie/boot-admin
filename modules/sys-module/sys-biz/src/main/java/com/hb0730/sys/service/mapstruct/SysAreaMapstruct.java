package com.hb0730.sys.service.mapstruct;

import com.hb0730.base.mapstruct.BaseMapstruct;
import com.hb0730.sys.domain.dto.AreaDto;
import com.hb0730.sys.domain.entity.SysArea;
import com.hb0730.sys.domain.vo.AreaTreeVo;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@org.mapstruct.Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SysAreaMapstruct extends BaseMapstruct<AreaDto, SysArea> {

    /**
     * 转换
     *
     * @param list .
     * @return .
     */
    List<AreaTreeVo> toTreeVoList(List<SysArea> list);
}
