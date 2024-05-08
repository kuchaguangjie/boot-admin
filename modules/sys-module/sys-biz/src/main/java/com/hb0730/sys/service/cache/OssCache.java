package com.hb0730.sys.service.cache;

import com.hb0730.base.ApplicationRunner;
import com.hb0730.base.utils.JsonUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.cache.core.BootAdminCache;
import com.hb0730.cache.core.CacheUtil;
import com.hb0730.event.OssRefreshEvent;
import com.hb0730.sys.domain.entity.SysOssConfig;
import com.hb0730.sys.repository.SysTenantOssConfigRepository;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * 缓存
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/6
 */
@Component
@Slf4j
public class OssCache implements CacheUtil, ApplicationRunner.RefreshCache {
    @Resource
    private BootAdminCache cache;
    @Lazy
    @Resource
    private SysTenantOssConfigRepository repository;
    @Lazy
    @Resource
    private ApplicationContext applicationContext;


    @Getter
    enum KeyValue implements CacheUtil.KeyValue {
        /**
         * oss配置
         */
        OSS_CONFIG("oss_config", EXPIRE_TIME, "oss配置"),
        ;
        private final String prefix;
        /**
         * 过期时间（秒）
         */
        private final int expire;
        /**
         * 描述
         */
        private final String desc;

        KeyValue(String prefix, int expire, String desc) {
            this.prefix = prefix;
            this.expire = expire;
            this.desc = desc;
        }
    }


    /**
     * 设置
     *
     * @param sysOssConfig oss配置
     */
    public void set(SysOssConfig sysOssConfig) {
        if (sysOssConfig == null) {
            return;
        }
        String sysCode = sysOssConfig.getSysCode();
        String cacheKey = getCacheKey(
                KeyValue.OSS_CONFIG,
                sysCode
        );
        String json = JsonUtil.DEFAULT.toJson(sysOssConfig);
        log.info("设置oss配置缓存:{}", cacheKey);
        cache.setString(cacheKey, json, KeyValue.OSS_CONFIG.getExpire());
        applicationContext.publishEvent(new OssRefreshEvent(sysCode, this));
    }

    /**
     * 获取
     *
     * @param sysCode 商户编码
     * @return SysOssConfig
     */
    public Optional<SysOssConfig> get(String sysCode) {
        String cacheKey = getCacheKey(
                KeyValue.OSS_CONFIG,
                sysCode
        );
        String json = cache.getString(cacheKey);
        if (json != null) {
            return Optional.of(JsonUtil.DEFAULT.json2Obj(json, SysOssConfig.class));
        }
        return Optional.empty();
    }

    /**
     * 清理
     */
    public void clear() {
        log.info("清理oss配置缓存");
        Optional<Set<String>> keyOptional = cache.scanKeys(getCacheKey(KeyValue.OSS_CONFIG, "*"));
        keyOptional.ifPresent(keys -> {
            log.info("清理oss配置缓存:{}", keys);
            cache.del(keys);
        });
    }

    /**
     * 刷新缓存
     *
     * @param sysCode 商户编码,为空则刷新所有
     */
    @Override
    public void refreshCache(String sysCode) {
        if (StrUtil.isBlank(sysCode)) {
            _refreshCache();
        } else {
            _refreshCache(sysCode);
        }
    }

    private void _refreshCache(String sysCode) {
        SysOssConfig sysOssConfig = repository.findBySysCode(sysCode);
        this.set(sysOssConfig);
    }

    private void _refreshCache() {
        this.clear();
        repository.findAll().forEach(this::set);
    }
}
