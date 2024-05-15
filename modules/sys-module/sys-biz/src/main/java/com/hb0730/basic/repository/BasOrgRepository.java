package com.hb0730.basic.repository;

import com.hb0730.basic.domain.entity.BasOrg;
import com.hb0730.data.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysProduct;
import org.springframework.data.jpa.repository.Query;
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


    /**
     * 根据父级id查询
     *
     * @param parentId .
     * @return .
     */
    @Query("select id from BasOrg where parentId = ?1")
    List<String> findChildrenIds(String parentId);


    /**
     * 根据角色id查询
     *
     * @param roleId .
     * @return .
     */
    @Query("SELECT o FROM BasOrg o WHERE o.id IN (SELECT orgId FROM BasRoleData WHERE roleId = ?1)")
    List<BasOrg> findByRoleId(String roleId);

}
