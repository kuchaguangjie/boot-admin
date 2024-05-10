package com.hb0730.common.service;

import com.hb0730.common.domain.dto.AttachmentDto;
import com.hb0730.common.domain.entity.Attachment;
import com.hb0730.common.repository.AttachmentRepository;
import com.hb0730.common.service.mapstruct.AttachmentMapstruct;
import com.hb0730.jpa.core.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentService extends BaseService<AttachmentRepository, Attachment, String> {
    private final AttachmentMapstruct mapstruct;


    /**
     * 保存附件
     *
     * @param dto 附件信息
     */
    public void save(AttachmentDto dto) {
        Attachment attachment = mapstruct.toEntity(dto);
        baseRepository.save(attachment);
    }
}
