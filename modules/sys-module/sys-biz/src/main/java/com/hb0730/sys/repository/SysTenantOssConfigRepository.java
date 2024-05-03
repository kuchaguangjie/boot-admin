package com.hb0730.sys.repository;

import com.hb0730.jpa.core.repository.BaseJpaRepository;
import com.hb0730.sys.domain.entity.SysOssConfig;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@Repository
public interface SysTenantOssConfigRepository extends BaseJpaRepository<SysOssConfig, String> {

    /**
     * 根据商户编码查询
     *
     * @param sysCode 商户编码
     * @return SysOssConfig
     */
    SysOssConfig findBySysCode(String sysCode);
}
