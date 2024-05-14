package com.hb0730.basic.repository;

import com.hb0730.basic.domain.entity.BasUser;
import com.hb0730.data.core.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface BasUserRepository extends BaseJpaRepository<BasUser, String> {

    /**
     * 是否存在系统用户
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsernameAndSystemIsTrue(String username);

    /**
     * 根据商户识别码统计用户数量
     *
     * @param sysCode .
     * @return .
     */
    int countBySysCode(String sysCode);

    /**
     * 根据机构ID查询是否存在
     *
     * @param orgId 机构ID
     * @return .
     */
    boolean existsByOrgId(String orgId);


    /**
     * 根据角色ID查询是否存在
     *
     * @param roleId 角色ID
     * @return .
     */
    default boolean existsByRoleId(String roleId) {
        return countByRoleId(roleId) > 0;
    }

    /**
     * 根据角色ID统计用户数量
     *
     * @param roleId 角色ID
     * @return .
     */
    @Query("SELECT COUNT(1) FROM BasUser u JOIN BasUserRole ur ON u.id = ur.userId WHERE ur.roleId = ?1")
    int countByRoleId(String roleId);

    /**
     * 根据用户名查询是否存在
     *
     * @param username 用户名
     * @return .
     */
    boolean existsByUsername(String username);

    /**
     * 根据用户名和ID查询是否存在
     *
     * @param username 用户名
     * @param id       ID
     * @return .
     */
    boolean existsByUsernameAndIdNot(String username, String id);

    /**
     * 根据用户名获取租户编码
     *
     * @param username 用户名
     * @return 租户编码
     */
    @Query(value = "SELECT sys_code FROM bas_user where username = ?1", nativeQuery = true)
    String getSysCodeByUsername(String username);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    BasUser findByUsername(String username);

    /**
     * 根据用户名修改最后登录时间
     *
     * @param username 用户名
     */
    @Modifying
    @Query("UPDATE BasUser u SET u.lastLoginTime = now() WHERE u.username = ?1")
    void changeLastLoginTimeByUsername(String username);

    /**
     * 根据组织ID查询用户ID
     *
     * @param orgIds 组织ID
     */
    @Query("SELECT u.id FROM BasUser u WHERE u.org.id IN ?1")
    Set<String> findUserIdByOrgIdIn(List<String> orgIds);

    /**
     * 根据用户ID查询角色ID
     *
     * @param userIds 用户ID
     */
    @Query("SELECT ur.roleId FROM BasUserRole ur WHERE ur.userId IN ?1")
    Set<String> findRoleIdsByUserIdIn(Collection<String> userIds);
}
