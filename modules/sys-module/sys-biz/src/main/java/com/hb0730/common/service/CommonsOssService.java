package com.hb0730.common.service;

import cn.hutool.core.bean.BeanUtil;
import com.hb0730.base.enums.OssTypeEnums;
import com.hb0730.base.enums.ValueEnum;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.event.OssRefreshEvent;
import com.hb0730.oss.core.OssStorage;
import com.hb0730.oss.core.aliyun.AliyunOssProperties;
import com.hb0730.oss.core.aliyun.AliyunOssStorage;
import com.hb0730.oss.core.s3.S3OssProperties;
import com.hb0730.oss.core.s3.S3OssStorage;
import com.hb0730.oss.factory.OssFactory;
import com.hb0730.sys.domain.dto.OssConfigDto;
import com.hb0730.sys.service.SysTenantOssConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Service
@RequiredArgsConstructor
public class CommonsOssService {
    private final SysTenantOssConfigService sysTenantOssConfigService;


    @EventListener(OssRefreshEvent.class)
    public void onOssRefreshEvent(OssRefreshEvent event) {
        String sysCode = event.getSysCode();
        OssConfigDto dto = sysTenantOssConfigService.findBySysCode(sysCode);
        if (null != dto) {
            registerStorage(dto);
        } else {
            OssFactory.removeStorage(sysCode);
        }
    }


    /**
     * 获取oss存储服务
     *
     * @param sysCode 商户编码，如果为空则获取默认
     * @return {@link OssStorage}
     */
    public Optional<OssStorage> getOssStorage(String sysCode) {
        if (StrUtil.isNotBlank(sysCode)) {
            Optional<OssStorage> storage = OssFactory.getStorage(sysCode);
            if (storage.isPresent()) {
                return storage;
            }
            OssConfigDto dto = sysTenantOssConfigService.findBySysCode(sysCode);
            if (null != dto) {
                OssStorage ossStorage = registerStorage(dto);
                if (null != ossStorage) {
                    return Optional.of(ossStorage);
                }
            }
        }
        return OssFactory.getStorage("DEFAULT");
    }

    /**
     * 注册存储
     *
     * @param dto dto
     * @return {@link OssStorage}
     */
    private OssStorage registerStorage(OssConfigDto dto) {
        OssStorage storage = null;
        String ossType = dto.getOssType();
        OssTypeEnums ossTypeEnums = ValueEnum.valueToEnum(OssTypeEnums.class, ossType);
        switch (Objects.requireNonNull(ossTypeEnums)) {
            case ALIYUN:
                AliyunOssProperties properties = BeanUtil.toBean(dto, AliyunOssProperties.class);
                storage = new AliyunOssStorage(properties);
                OssFactory.registerStorage(dto.getSysCode(), storage);
                break;
            case S3:
                S3OssProperties ossProperties = BeanUtil.toBean(dto, S3OssProperties.class);
                storage = new S3OssStorage(ossProperties);
                OssFactory.registerStorage(dto.getSysCode(), storage);
                break;
            default:
                break;
        }
        return storage;
    }
}
