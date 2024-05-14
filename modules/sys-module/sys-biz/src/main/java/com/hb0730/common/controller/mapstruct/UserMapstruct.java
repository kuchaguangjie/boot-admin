package com.hb0730.common.controller.mapstruct;

import com.hb0730.basic.domain.entity.BasUser;
import com.hb0730.common.domain.vo.UserVo;
import com.hb0730.sys.domain.entity.SysUser;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/13
 */
@org.mapstruct.Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapstruct {

    /**
     * 转换
     *
     * @param entity 实体
     * @return vo
     */
    UserVo toVo(SysUser entity);

    /**
     * 转换
     *
     * @param entity 实体
     * @return vo
     */
    UserVo toVo(BasUser entity);
}
