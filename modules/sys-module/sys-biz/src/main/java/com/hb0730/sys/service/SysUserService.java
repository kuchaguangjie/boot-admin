package com.hb0730.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import com.hb0730.sys.domain.dto.UserDto;
import com.hb0730.sys.domain.dto.UserRestPwdDto;
import com.hb0730.sys.domain.dto.UserSaveDto;
import com.hb0730.sys.domain.entity.SysUser;
import com.hb0730.sys.domain.query.UserQuery;
import com.hb0730.sys.repository.SysUserRepository;
import com.hb0730.sys.service.mapstruct.SysUserMapstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserService extends BaseService<SysUserRepository, SysUser, String> {
    private final SysUserMapstruct mapstruct;

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    public SysUser findByUsername(String username) {
        return baseRepository.findByUsername(username);
    }

    /**
     * 修改最后登录时间
     *
     * @param username 用户名
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeLastLoginTimeByUsername(String username) {
        baseRepository.changeLastLoginTimeByUsername(username);
    }


    /**
     * 用户名是否存在
     *
     * @param username 用户名
     * @param id       需要排除的id
     * @return 是否存在
     */
    public Boolean existsByUsername(String username, String id) {
        if (StrUtil.isBlank(id)) {
            return baseRepository.existsByUsername(username);
        }
        return baseRepository.existsByUsernameAndIdNot(username, id);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页数据
     */
    public JsfPage<UserDto> page(UserQuery query) {
        Specification<SysUser> specification = QueryHelper.ofBean(query);
        Pageable page = QueryHelper.toPage(query);
        Page<SysUser> pageData = findAll(specification, page);
        List<UserDto> res = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, res);
    }

    /**
     * 查询
     *
     * @param query 查询条件
     * @return 数据
     */
    public List<UserDto> list(UserQuery query) {
        Specification<SysUser> specification = QueryHelper.ofBean(query);
        List<SysUser> list = findAll(specification);
        return mapstruct.toDtoList(list);
    }

    /**
     * 保存
     *
     * @param dto 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(UserSaveDto dto) {
        SysUser entity = mapstruct.saveDtoToEntity(dto);
        save(entity);

//        baseMapper.insert(entity);
//        if (CollectionUtil.isNotEmpty(dto.getRoles())) {
//            baseMapper.saveUserRole(entity.getId(), dto.getRoles().stream().map(RoleSmallDto::getId).toList());
//        }
    }

    /**
     * 更新
     *
     * @param dto 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(UserSaveDto dto) {
        SysUser sysUser = mapstruct.saveDtoToEntity(dto);
        if (StrUtil.isBlank(sysUser.getId())) {
            throw new ServiceException("id不能为空");
        }
        SysUser entity = getById(sysUser.getId());
        if (entity == null) {
            throw new ServiceException("数据不存在");
        }

        // 账号不能修改，密码单独修改
        sysUser.setUsername(null);
        sysUser.setPassword(null);
        BeanUtil.copyProperties(sysUser, entity, CopyOptions.create().setIgnoreNullValue(true));
        updateById(entity);

//        // 更新用户角色
//        // 删除原有角色
//        baseMapper.deleteUserRoleByUserId(entity.getId());
//        // 保存新角色
//        if (CollectionUtil.isNotEmpty(dto.getRoles())) {
//            baseMapper.saveUserRole(entity.getId(), dto.getRoles().stream().map(RoleSmallDto::getId).toList());
//        }
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new ServiceException("数据不存在");
        }
        remove(user);
        // 删除用户角色
//        baseMapper.deleteUserRoleByUserId(id);
        // 删除用户部门
        // 删除用户岗位
    }

    /**
     * 重置密码
     *
     * @param dto 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(UserRestPwdDto dto) {
        if (dto.getId() == null) {
            throw new ServiceException("id不能为空");
        }
        if (StrUtil.isBlank(dto.getPassword())) {
            throw new ServiceException("密码不能为空");
        }
        changePassword(dto.getId(), dto.getPassword());
    }

    /**
     * 修改密码
     *
     * @param id       id
     * @param password 密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(String id, String password) {
        SysUser entity = getById(id);
        if (entity == null) {
            throw new ServiceException("数据不存在");
        }
        entity.setPassword(password);
        updateById(entity);
    }


}
