package com.hb0730.basic.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.basic.domain.dto.BasUserDto;
import com.hb0730.basic.domain.dto.BasUserRestPasswordDto;
import com.hb0730.basic.domain.dto.BasUserSaveDto;
import com.hb0730.basic.domain.entity.BasOrg;
import com.hb0730.basic.domain.entity.BasRole;
import com.hb0730.basic.domain.entity.BasUser;
import com.hb0730.basic.domain.query.BasUserQuery;
import com.hb0730.basic.repository.BasPostRepository;
import com.hb0730.basic.repository.BasRoleRepository;
import com.hb0730.basic.repository.BasUserRepository;
import com.hb0730.basic.service.mapstruct.BasUserMapstruct;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BasUserService extends BaseService<BasUserRepository, BasUser, String> {
    @Lazy
    @Resource
    private BasOrgService orgService;
    private final BasUserMapstruct mapstruct;
    @Resource
    @Lazy
    private BasRoleRepository basRoleRepository;
    @Resource
    @Lazy
    private BasPostRepository basPostRepository;


    /**
     * 是否系统用户
     *
     * @param username 用户名
     * @return 是否存在
     */
    public Boolean isSystemUser(String username) {
        return baseRepository.existsByUsernameAndSystemIsTrue(username);
    }

    /**
     * 根据用户名获取租户编码
     *
     * @param username 用户名
     * @return 租户编码
     */
    public String getSysCodeByUsername(String username) {
        return baseRepository.getSysCodeByUsername(username);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public BasUser findByUsername(String username) {
        return baseRepository.findByUsername(username);
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public BasUser getById(String id) {
        return baseRepository.findById(id).orElse(null);
    }

    /**
     * 修改最后登录时间
     *
     * @param username 用户名
     * @param sysCode  租户编码
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeLastLoginTimeByUsername(String username, String sysCode) {
        baseRepository.changeLastLoginTimeByUsername(username);
    }

    /**
     * 根据组织ID查询用户ID
     *
     * @param orgIds 组织ID
     * @return 用户ID
     */
    public Set<String> findUserIdsByOrgIds(List<String> orgIds) {
        return baseRepository.findUserIdByOrgIdIn(orgIds);
    }

    /**
     * 根据用户ID查询角色ID
     *
     * @param userIds 用户ID
     * @return 角色ID
     */
    public Set<String> findRoleIdsByUserIds(Collection<String> userIds) {
        return baseRepository.findRoleIdsByUserIdIn(userIds);
    }

    /**
     * 账号是否存在
     *
     * @param username 用户名
     * @param sysCode  租户编码
     * @param id       需要排除的用户ID
     * @return 是否存在
     */
    public Boolean existsUsername(String username, String sysCode, String id) {
        String _username = username;
        if (!username.contains("@")) {
            _username = username + "@" + sysCode;
        }
        if (StrUtil.isBlank(id)) {
            return baseRepository.existsByUsername(_username);
        }
        return baseRepository.existsByUsernameAndIdNot(_username, id);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页数据
     */
    public JsfPage<BasUserDto> page(BasUserQuery query) {
        Pageable page = QueryHelper.toPage(query);
        Specification<BasUser> specification = QueryHelper.ofBean(query);
        Page<BasUser> pageData = baseRepository.findAll(specification, page);
        List<BasUserDto> list = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, list);
    }

    /**
     * 查询用户列表
     *
     * @param query 查询条件
     * @return 用户列表
     */
    public List<BasUserDto> list(BasUserQuery query) {
        Specification<BasUser> specification = QueryHelper.ofBean(query);
        List<BasUser> list = baseRepository.findAll(specification);
        return mapstruct.toDtoList(list);
    }

    /**
     * 保存用户
     *
     * @param dto 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(BasUserSaveDto dto) {
        String username = dto.getUsername();
        if (existsUsername(username, dto.getSysCode(), dto.getId())) {
            throw new ServiceException("用户名已存在");
        }
        if (StrUtil.isBlank(dto.getPassword())) {
            throw new ServiceException("密码不能为空");
        }
        String orgId = dto.getOrgId();
        if (StrUtil.isBlank(orgId)) {
            throw new ServiceException("机构不能为空");
        }
        boolean accountNum = orgService.checkAccountNum(orgId);
        if (accountNum) {
            throw new ServiceException("账号数量已达上限");
        }
        dto.setUsername(username + "@" + dto.getSysCode());
        BasUser user = mapstruct.toEntity(dto);
        user.setSystem(false);
        // 关联信息
        fillRelation(user, dto);
        // 保存用户&角色
        save(user);
    }

    /**
     * 更新用户
     *
     * @param dto 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(BasUserSaveDto dto) {
        String username = dto.getUsername();
        if (existsUsername(username, dto.getSysCode(), dto.getId())) {
            throw new ServiceException("用户名已存在");
        }
        String orgId = dto.getOrgId();
        if (StrUtil.isBlank(orgId)) {
            throw new ServiceException("机构不能为空");
        }
        BasUser entity = getById(dto.getId());
        if (entity == null) {
            throw new ServiceException("用户不存在");
        }
        dto.setPassword(null);
        dto.setUsername(null);
        BasUser user = mapstruct.toEntity(dto);
        user.setSystem(false);
        // 关联信息
        fillRelation(user, dto);
        BeanUtil.copyProperties(user, entity, CopyOptions.create().setIgnoreNullValue(true));
        // 保存用户&角色
        updateById(entity);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        BasUser user = getById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (Boolean.TRUE.equals(user.getSystem())) {
            throw new ServiceException("系统用户不允许删除");
        }
        // 删除用户&角色
        baseRepository.delete(user);
    }

    /**
     * 重置密码
     *
     * @param dto 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void restPassword(BasUserRestPasswordDto dto) {
        BasUser user = getById(dto.getId());
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        restPassword(dto.getId(), dto.getPassword());
    }

    /**
     * 重置密码
     *
     * @param id 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void restPassword(String id, String password) {
        BasUser user = getById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        user.setPassword(password);
        updateById(user);
    }

    /**
     * 填充关联信息
     */
    public void fillRelation(BasUser user, BasUserSaveDto dto) {
        // 保存用户角色
        List<String> roleIds = dto.getRoleIds();
        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<BasRole> roles = basRoleRepository.findAllById(roleIds);
            user.setRoles(roles);
        } else {
            user.setRoles(null);
        }
        // 保存岗位
        List<String> postIds = dto.getPostIds();
        if (CollectionUtil.isNotEmpty(postIds)) {
            user.setPosts(basPostRepository.findAllById(postIds));
        } else {
            user.setPosts(null);
        }
        BasOrg basOrg = new BasOrg();
        basOrg.setId(dto.getOrgId());
        user.setOrg(basOrg);
    }

}
