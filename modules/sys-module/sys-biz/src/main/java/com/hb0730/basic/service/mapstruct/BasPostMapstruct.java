package com.hb0730.basic.service.mapstruct;

import com.hb0730.base.mapstruct.BaseMapstruct;
import com.hb0730.basic.domain.dto.BasPostDto;
import com.hb0730.basic.domain.entity.BasPost;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/7
 */
@org.mapstruct.Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BasPostMapstruct extends BaseMapstruct<BasPostDto, BasPost> {
}
