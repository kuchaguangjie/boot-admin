package com.hb0730.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.jpa.core.service.BaseService;
import com.hb0730.sys.domain.dto.OssConfigDto;
import com.hb0730.sys.domain.entity.SysOssConfig;
import com.hb0730.sys.repository.SysTenantOssConfigRepository;
import com.hb0730.sys.service.mapstruct.SysOssConfigMapstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/30
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysTenantOssConfigService extends BaseService<SysTenantOssConfigRepository, SysOssConfig, String> {
    private final SysOssConfigMapstruct mapstruct;

    /**
     * 根据商户编码查询
     *
     * @param sysCode 商户编码
     * @return SysOssConfig
     */
    public OssConfigDto findBySysCode(String sysCode) {
        SysOssConfig sysOssConfig = baseRepository.findBySysCode(sysCode);
        return mapstruct.toDto(sysOssConfig);
    }

    /**
     * 保存
     *
     * @param dto dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(OssConfigDto dto) {
        SysOssConfig sysOssConfig = mapstruct.toEntity(dto);
        SysOssConfig entity = getById(dto.getId());
        if (StrUtil.isBlank(dto.getId())) {
            entity = sysOssConfig;
        } else {
            BeanUtil.copyProperties(sysOssConfig, entity, CopyOptions.create().setIgnoreNullValue(true));
        }
        save(entity);
    }
}
