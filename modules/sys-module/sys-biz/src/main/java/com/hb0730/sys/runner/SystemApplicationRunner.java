package com.hb0730.sys.runner;

import com.hb0730.base.ApplicationRunner;
import com.hb0730.sys.service.SysTenantOssConfigService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 系统启动完成后执行
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/6
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemApplicationRunner implements ApplicationRunner {
    private final SysTenantOssConfigService sysTenantOssConfigService;

    @Override
    public void refresh(@Nullable String code) {
        // 刷新oss配置缓存
        sysTenantOssConfigService.refreshCache(code);

    }
}
