package com.hb0730.sys.service.mapstruct;

import com.hb0730.base.mapstruct.BaseMapstruct;
import com.hb0730.sys.domain.dto.PermissionSaveDto;
import com.hb0730.sys.domain.entity.SysPermission;
import com.hb0730.sys.domain.entity.SysTenantPermission;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/29
 */
@org.mapstruct.Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SysPermissionSaveMapstruct extends BaseMapstruct<PermissionSaveDto, SysPermission> {

    /**
     * 转换
     *
     * @param dto .
     * @return .
     */
    SysTenantPermission toTenant(PermissionSaveDto dto);
}
