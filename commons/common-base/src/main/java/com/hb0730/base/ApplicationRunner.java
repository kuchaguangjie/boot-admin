package com.hb0730.base;

import jakarta.annotation.Nullable;
import org.springframework.boot.ApplicationArguments;

/**
 * 应用启动完成后执行
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/6
 */
public interface ApplicationRunner extends org.springframework.boot.ApplicationRunner {

    /**
     * 运行
     *
     * @param args 参数
     * @throws Exception 异常
     */
    @Override
    default void run(ApplicationArguments args) throws Exception {
        refresh();
    }

    /**
     * 刷新
     */
    default void refresh() {
        refresh(null);
    }

    /**
     * 刷新
     *
     * @param code 编码
     */
    void refresh(@Nullable String code);


    /**
     * 刷新缓存
     */
    interface RefreshCache {
        /**
         * 刷新缓存
         *
         * @param sysCode .
         */
        void refreshCache(@Nullable String sysCode);
    }
}
