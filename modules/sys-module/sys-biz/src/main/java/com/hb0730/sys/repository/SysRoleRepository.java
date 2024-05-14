package com.hb0730.sys.repository;

import com.hb0730.data.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface SysRoleRepository extends BaseJpaRepository<SysRole, String> {


    /**
     * 根据编码查询
     *
     * @param code 编码
     * @param id   需要排除的id
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, String id);

    /**
     * 根据编码查询
     *
     * @param code 编码
     * @return 是否存在
     */
    boolean existsByCode(String code);


    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return 角色
     */
    @Query("select r from SysRole r join SysUserRole ur on r.id = ur.roleId where ur.userId = ?1")
    List<SysRole> findByUserId(String userId);

    /**
     * 是否存在用户
     *
     * @param roleId 角色id
     * @return 是否存在
     */
    default boolean existsUserByRoleId(String roleId) {
        return countUsersById(roleId) > 0;
    }

    /**
     * 统计用户数量
     *
     * @param roleId 角色id
     * @return 数量
     */
    @Query("select count(1) from SysUserRole where roleId = ?1")
    int countUsersById(String roleId);

}
