package com.hb0730.sys.repository;

import com.hb0730.data.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysProduct;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface SysProductRepository extends BaseJpaRepository<SysProduct, String> {

    /**
     * 根据编码查询
     *
     * @param code 编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 根据编码和ID查询
     *
     * @param code 编码
     * @param id   ID
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, String id);
}
