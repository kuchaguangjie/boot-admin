package com.hb0730.common.service.mapstruct;

import com.hb0730.base.mapstruct.BaseMapstruct;
import com.hb0730.common.domain.dto.AttachmentDto;
import com.hb0730.common.domain.entity.Attachment;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/9
 */
@org.mapstruct.Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AttachmentMapstruct extends BaseMapstruct<AttachmentDto, Attachment> {
}
