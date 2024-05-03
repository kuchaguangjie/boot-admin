package com.hb0730.sys.repository;

import com.hb0730.jpa.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface SysUserRepository extends BaseJpaRepository<SysUser, String> {

    /**
     * 用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 用户名是否存在
     *
     * @param username 用户名
     * @param id       需要排除的id
     * @return 是否存在
     */
    boolean existsByUsernameAndIdNot(String username, String id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 修改最后登录时间
     *
     * @param username 用户名
     */
    @Modifying
    @Query("update SysUser u set u.lastLoginTime = now() where u.username = ?1")
    void changeLastLoginTimeByUsername(String username);
}
