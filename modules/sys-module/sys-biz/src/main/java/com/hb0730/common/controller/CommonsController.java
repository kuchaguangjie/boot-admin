package com.hb0730.common.controller;

import com.hb0730.base.R;
import com.hb0730.common.domain.OssStsTokenVo;
import com.hb0730.common.service.CommonsOssService;
import com.hb0730.oss.core.OssStorage;
import com.hb0730.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@RestController
@RequestMapping("/commons")
@Tag(name = "公共接口")
@RequiredArgsConstructor
public class CommonsController {
    private final CommonsOssService commonsOssService;

    /**
     * 获取OSS临时授权
     *
     * @return 临时授权
     */
    @GetMapping("/oss/sts_token")
    @Operation(summary = "获取OSS临时授权,默认3分钟有效")
    public R<OssStsTokenVo> getOssStsToken() {
        Optional<OssStorage> ossStorage = commonsOssService.getOssStorage(SecurityUtil.getSysCode());
        if (ossStorage.isEmpty()) {
            return R.NG("未找到OSS配置");
        }
        OssStorage.OssStsToken stsToken = ossStorage.get().getStsToken();
        OssStsTokenVo vo = new OssStsTokenVo();
        vo.setAccessKey(stsToken.getAccessKey());
        vo.setAccessSecret(stsToken.getAccessSecret());
        vo.setSecurityToken(stsToken.getSecurityToken());
        vo.setExpiration(stsToken.getExpiration());
        return R.OK(vo);
    }

}
