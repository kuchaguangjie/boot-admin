package com.hb0730.basic.repository;

import com.hb0730.basic.domain.entity.BasOrg;
import com.hb0730.data.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface BasOrgRepository extends BaseJpaRepository<BasOrg, String> {

    /**
     * 获取顶级机构
     *
     * @param sysCode 商户识别码
     * @return .
     */
    default BasOrg getTopOrg(String sysCode) {
        return findBySysCodeAndSystemIsTrue(sysCode);
    }

    /**
     * 查询厂商信息
     *
     * @param sysCode .
     * @return .
     */
    BasOrg findBySysCodeAndSystemIsTrue(String sysCode);

    /**
     * 根据系统编码查询数量
     *
     * @param sysCode .
     * @return .
     */
    int countBySysCode(String sysCode);


    /**
     * 根据产品查询是否存在
     *
     * @param product .
     * @return .
     */
    boolean existsByProduct(SysProduct product);


    /**
     * 根据产品查询
     *
     * @param product .
     * @return .
     */
    List<BasOrg> findByProduct(SysProduct product);


    /**
     * 是否存在子集
     *
     * @param parentId .
     * @return .
     */
    boolean existsByParentId(String parentId);

}
