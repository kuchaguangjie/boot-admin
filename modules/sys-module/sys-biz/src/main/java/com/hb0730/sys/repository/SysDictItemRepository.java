package com.hb0730.sys.repository;

import com.hb0730.jpa.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysDictItem;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Repository
public interface SysDictItemRepository extends BaseJpaRepository<SysDictItem, String> {
}
