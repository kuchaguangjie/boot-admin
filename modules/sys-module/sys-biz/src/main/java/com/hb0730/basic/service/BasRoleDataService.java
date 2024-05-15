package com.hb0730.basic.service;

import com.hb0730.base.enums.DataScopeEnums;
import com.hb0730.base.enums.ValueEnum;
import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.basic.domain.entity.BasOrg;
import com.hb0730.basic.domain.entity.BasRole;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/15
 */
@Service
@Slf4j
public class BasRoleDataService {
    @Lazy
    @Resource
    private BasRoleService basRoleService;
    @Lazy
    @Resource
    private BasOrgService basOrgService;

    /**
     * 获取角色数据范围
     *
     * @param userId 用户id
     * @return 角色数据范围
     */
    public Set<String> getRoleDataScope(String userId, String orgId) {
        Set<String> dataScopes = new HashSet<>();
        List<BasRole> roles = basRoleService.findByUserId(userId);
        for (BasRole role : roles) {
            Integer dataScope = role.getDataScope();
            if (null == dataScope) {
                continue;
            }
            DataScopeEnums dataScopeEnums = ValueEnum.valueToEnum(DataScopeEnums.class, dataScope);
            switch (Objects.requireNonNull(dataScopeEnums)) {
                case THIS_LEVEL -> dataScopes.add(orgId);
                case THIS_LEVEL_CHILDREN -> {
                    List<String> orgIds = basOrgService.findChildrenIds(orgId);
                    orgIds.add(orgId);
                    dataScopes.addAll(orgIds);
                }
                case CUSTOMIZE -> {
                    List<String> customizeRole = getCustomizeRole(role.getId());
                    if (CollectionUtil.isNotEmpty(customizeRole)) {
                        dataScopes.addAll(customizeRole);
                    }
                }
            }
        }
        return dataScopes;
    }


    /**
     * 获取自定义数据范围
     *
     * @param roleId 角色id
     * @return 自定义数据范围
     */
    private List<String> getCustomizeRole(String roleId) {
        List<BasOrg> orgList = basOrgService.findByRoleId(roleId);
        if (CollectionUtil.isEmpty(orgList)) {
            return null;
        }
        return orgList.stream().map(BasOrg::getId).toList();
    }
}
