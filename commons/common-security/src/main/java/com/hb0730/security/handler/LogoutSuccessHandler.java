package com.hb0730.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.hb0730.base.R;
import com.hb0730.base.utils.JsonUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.security.cache.TokenProvider;
import com.hb0730.security.cache.UserProvider;
import com.hb0730.security.service.cache.UserRoutesCache;
import com.hb0730.security.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/3/26
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    private final TokenProvider tokenProvider;
    private final UserProvider userProvider;
    private final UserRoutesCache userRoutesCache;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("退出成功 处理器>>>>>");
        String token = JwtUtil.getToken(request);
        if (null != token) {
            log.info("退出成功 处理器>>>>> token:{}", token);
            tokenProvider.removeToken(token);
            clearUserProvider(token);
            clearRoutes(token);

        }
//        removeUserProvider(authentication);

        String message = "退出成功";
        JakartaServletUtil.write(
                response,
                JsonUtil.DEFAULT.toJson(R.OK(message)),
                MediaType.APPLICATION_JSON_UTF8_VALUE
        );
    }

//    private void removeUserProvider(@Nullable Authentication authentication) {
//        if (null != authentication && authentication.getPrincipal() instanceof UserInfoDto user) {
//            log.info("退出监听器 退出事件 用户信息:{}", user);
//            userProvider.removeUser(getCacheKey(user.getUsername(), user.getSysCode()));
//            userRoutesCache.delete(user.getId());
//        }
//    }

    private void clearUserProvider(String token) {
        log.info("清除用户缓存:{}", token);
        if (StrUtil.isBlank(token)) {
            return;
        }
        Optional<String> username = JwtUtil.getUsername(token);
        Optional<String> sysCode = JwtUtil.getTenant(token);
        username.ifPresent(s -> userProvider.removeUser(UserProvider.getCacheKey(s, sysCode.orElse(null))));
    }

    private void clearRoutes(String token) {
        log.info("清除用户路由缓存:{}", token);
        if (StrUtil.isBlank(token)) {
            return;
        }
        JwtUtil.getUserid(token).ifPresent(userRoutesCache::delete);
    }
}