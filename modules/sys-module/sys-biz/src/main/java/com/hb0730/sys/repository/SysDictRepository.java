package com.hb0730.sys.repository;

import com.hb0730.jpa.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysDict;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Repository
public interface SysDictRepository extends BaseJpaRepository<SysDict, String> {

    /**
     * 根据类型查询
     *
     * @param type 类型
     * @return 是否存在
     */
    boolean existsByType(String type);

    /**
     * 根据类型和id查询
     *
     * @param type 类型
     * @param id   id
     * @return 是否存在
     */
    boolean existsByTypeAndIdNot(String type, String id);
}
