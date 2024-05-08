package com.hb0730.oss.factory;

import com.hb0730.oss.core.OssStorage;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Slf4j
public class OssFactory {
    private static final Map<String, OssStorage> STORAGE_MAP = new ConcurrentHashMap<>();

    /**
     * 注册存储
     *
     * @param sysCode 系统编码
     * @param storage 存储
     */
    public static void registerStorage(String sysCode, OssStorage storage) {
        STORAGE_MAP.put(sysCode, storage);
    }

    /**
     * 获取存储
     *
     * @param sysCode 系统编码
     * @return 存储
     */
    public static Optional<OssStorage> getStorage(String sysCode) {
        OssStorage ossStorage = STORAGE_MAP.get(sysCode);
        return Optional.ofNullable(ossStorage);
    }

    /**
     * 移除存储
     *
     * @param sysCode 系统编码
     */
    public static void removeStorage(String sysCode) {
        STORAGE_MAP.remove(sysCode);
    }
}
