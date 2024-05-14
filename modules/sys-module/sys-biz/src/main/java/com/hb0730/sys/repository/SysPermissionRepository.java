package com.hb0730.sys.repository;

import com.hb0730.data.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysPermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface SysPermissionRepository extends BaseJpaRepository<SysPermission, String> {

    /**
     * 是否存在子级
     *
     * @param parentId 父级id
     * @return 是否存在
     */
    default boolean hasChild(String parentId) {
        return existsByParentId(parentId);
    }

    /**
     * 根据父级id查询
     *
     * @param parentId 父级id
     * @return 是否存在
     */
    boolean existsByParentId(String parentId);

    /**
     * 根据权限id查询是否存在
     *
     * @param permissionId 权限id
     * @return 是否存在
     */
    default boolean existsRolePermissionByPermissionId(String permissionId) {
        return countRolePermissionByPermissionId(permissionId) > 0;
    }

    /**
     * 根据权限id查询角色权限数量
     *
     * @param permissionId 权限id
     * @return 数量
     */
    @Query("select count(1) from SysRolePermission where permissionId = ?1")
    int countRolePermissionByPermissionId(String permissionId);

    /**
     * 查询所有启用的权限
     *
     * @return 权限
     */
    List<SysPermission> findAllByEnabledIsTrueOrderByRank();

    /**
     * 根据角色id查询权限
     *
     * @param roleIds 角色id
     * @return 权限
     */
    @Query("select p from SysPermission p join SysRolePermission rp on p.id = rp.permissionId where rp.roleId in ?1 " +
            "AND p.enabled = true ORDER BY p.rank")
    List<SysPermission> findAllByRoleIdIn(List<String> roleIds);
}
