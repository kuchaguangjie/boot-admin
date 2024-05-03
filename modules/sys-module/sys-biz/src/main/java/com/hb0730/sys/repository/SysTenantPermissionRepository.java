package com.hb0730.sys.repository;

import com.hb0730.jpa.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysTenantPermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface SysTenantPermissionRepository extends BaseJpaRepository<SysTenantPermission, String> {

    /**
     * 查询所有启用的权限
     *
     * @return 权限
     */
    List<SysTenantPermission> findAllByEnabledIsTrueOrderByRankAsc();

    /**
     * 根据父级id查询
     *
     * @param id 父级id
     * @return .
     */
    default boolean hasChildren(String id) {
        return existsByParentId(id);
    }

    /**
     * 根据父级id查询
     *
     * @param parentId 父级id
     * @return .
     */
    boolean existsByParentId(String parentId);


    /**
     * 根据产品id查询
     *
     * @param productId 产品id
     * @return .
     */
    @Query("select p from SysTenantPermission p,SysProductPermission r where p.id=r.permissionId AND r.productId = ?1")
    List<SysTenantPermission> findByProductId(String productId);

    /**
     * 删除角色权限
     *
     * @param roleId 角色id
     */
    @Modifying
    @Query("delete from SysRolePermission where roleId = ?1")
    void deleteRolePermissionByRoleId(String roleId);
}
