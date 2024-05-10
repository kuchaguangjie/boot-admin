package com.hb0730.common.repository;

import com.hb0730.common.domain.entity.Attachment;
import com.hb0730.jpa.core.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/9
 */
@Repository
public interface AttachmentRepository extends BaseJpaRepository<Attachment, String> {
}
