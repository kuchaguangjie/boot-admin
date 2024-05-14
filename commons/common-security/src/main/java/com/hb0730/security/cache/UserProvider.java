package com.hb0730.security.cache;

import com.hb0730.base.utils.StrUtil;
import com.hb0730.security.domain.dto.UserInfoDto;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/3/23
 */
public interface UserProvider {
    /**
     * 设置用户信息
     *
     * @param key key
     * @param dto 用户信息
     * @return 是否成功
     */
    boolean putUser(String key, UserInfoDto dto);

    /**
     * 获取用户信息
     *
     * @param key key
     * @return 用户信息
     */
    UserInfoDto getUser(String key);

    /**
     * 删除用户信息
     *
     * @param key key
     * @return 是否成功
     */
    boolean removeUser(String key);


    /**
     * 获取缓存key
     *
     * @param username 用户名
     * @param sysCode  系统编码
     * @return key
     */
    static String getCacheKey(String username, String sysCode) {
        if (StrUtil.isNotBlank(sysCode)) {
            return sysCode + ":" + username;
        }
        return username;
    }
}