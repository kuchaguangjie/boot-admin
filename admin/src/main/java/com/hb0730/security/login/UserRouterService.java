package com.hb0730.security.login;

import com.hb0730.basic.service.BasPermissionService;
import com.hb0730.security.domain.vo.RouteVO;
import com.hb0730.security.service.cache.UserRoutesCache;
import com.hb0730.sys.domain.dto.PermissionDto;
import com.hb0730.sys.service.SysPermissionService;
import com.hb0730.util.RouteUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserRouterService implements com.hb0730.security.service.UserRouterService {
    private final SysPermissionService sysPermissionService;
    private final BasPermissionService basPermissionService;
    @Lazy
    @Resource
    private UserRoutesCache userRoutesCache;

    @Override
    public List<RouteVO> getRoutes(String userId) {
        return getCacheRoutes(userId, sysPermissionService::userRoutes);
    }

    @Override
    public List<RouteVO> getRoutes(String userId, String sysCode) {
        return getCacheRoutes(userId, basPermissionService::userRoutes);
    }

    private List<RouteVO> getCacheRoutes(String userId, Function<String, List<PermissionDto>> func) {
        Optional<List<RouteVO>> routeOptional = userRoutesCache.get(userId);
        if (routeOptional.isPresent()) {
            return routeOptional.get();
        }
        List<PermissionDto> permissions = func.apply(userId);
        List<RouteVO> routes = getRoutes(permissions);
        userRoutesCache.put(userId, routes);
        return routes;
    }


    /**
     * 获取路由
     *
     * @param permissions 权限
     * @return 路由
     */
    private List<RouteVO> getRoutes(List<PermissionDto> permissions) {
        if (null == permissions) {
            return List.of();
        }
        return RouteUtil.buildRoutes(permissions);
    }
}
