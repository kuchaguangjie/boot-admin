package com.hb0730.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.base.utils.PasswordUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.basic.domain.entity.BasOrg;
import com.hb0730.basic.domain.entity.BasPermission;
import com.hb0730.basic.domain.entity.BasRole;
import com.hb0730.basic.domain.entity.BasUser;
import com.hb0730.basic.repository.BasRoleRepository;
import com.hb0730.basic.service.BasUserService;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import com.hb0730.sys.domain.dto.TenantBasicConfigDto;
import com.hb0730.sys.domain.dto.TenantDto;
import com.hb0730.sys.domain.dto.TenantSmallDto;
import com.hb0730.sys.domain.entity.SysProduct;
import com.hb0730.sys.domain.entity.SysTenant;
import com.hb0730.sys.domain.entity.SysTenantPermission;
import com.hb0730.sys.domain.query.TenantQuery;
import com.hb0730.sys.repository.SysTenantRepository;
import com.hb0730.sys.service.mapstruct.SysTenantBasicConfigMapstruct;
import com.hb0730.sys.service.mapstruct.SysTenantMapstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysTenantService extends BaseService<SysTenantRepository, SysTenant, String> {
    @Lazy
    @Resource
    private BasUserService basUserService;
    @Lazy
    @Resource
    private BasRoleRepository basRoleRepository;
    @Lazy
    @Resource
    private SysTenantPermissionService sysTenantPermissionService;
    private final SysTenantMapstruct mapstruct;
    private final SysTenantBasicConfigMapstruct mapstructBasicConfig;

    /**
     * 编码是否存在
     *
     * @param code 编码
     * @param id   需要排除的id
     * @return 是否存在
     */
    public Boolean existsCode(String code, String id) {
        if (StrUtil.isBlank(id)) {
            return baseRepository.existsBySysCode(code);
        }
        return baseRepository.existsBySysCodeAndIdNot(code, id);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页数据
     */
    public JsfPage<TenantDto> page(TenantQuery query) {
        Specification<SysTenant> specification = QueryHelper.ofBean(query);
        Pageable page = QueryHelper.toPage(query);
        Page<SysTenant> pageData = findAll(specification, page);
        List<TenantDto> res = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, res);

    }

    /**
     * 查询
     *
     * @param query 查询条件
     * @return 数据
     */
    public List<TenantDto> list(TenantQuery query) {
        Specification<SysTenant> specification = QueryHelper.ofBean(query);
        List<SysTenant> list = findAll(specification);
        return mapstruct.toDtoList(list);
    }

    /**
     * 保存
     *
     * @param dto 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(TenantSmallDto dto) {
        BasUser user = basUserService.findByUsername(dto.getLinkTel());
        if (null != user && StrUtil.isNotBlank(user.getUsername())) {
            throw new ServiceException("手机号已被使用");
        }
        SysTenant organization = convert(dto);
        baseRepository.saveAndFlush(organization);
        // 初始化用户角色
        initUserRoleProduct(organization);
    }

    /**
     * 更新
     *
     * @param dto 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(TenantSmallDto dto) {
        SysTenant tenant = getById(dto.getId());
        if (null == tenant) {
            throw new ServiceException("商户不存在");
        }
        boolean isEditProduct = false;
        boolean isEditLinkTel = false;
        if (!tenant.getLinkTel().equals(dto.getLinkTel())) {
            BasUser user = basUserService.findByUsername(dto.getLinkTel());
            if (null != user && StrUtil.isNotBlank(user.getUsername())) {
                throw new ServiceException("手机号已被使用");
            }
            isEditLinkTel = true;
        }
        SysProduct product = tenant.getProduct();
        if (null != product && !product.getId().equals(dto.getProductId())) {
            isEditProduct = true;
        }


        SysTenant organization = convert(dto);
        if (isEditProduct) {
            isUpdateProduct(organization, tenant);
        }
        if (isEditLinkTel) {
            isUpdateUser(organization, tenant);
        }

        BeanUtil.copyProperties(organization, dto, CopyOptions.create().ignoreNullValue());
        updateById(organization);
    }


    /**
     * 删除
     *
     * @param id .
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        SysTenant tenant = getById(id);
        if (null == tenant) {
            throw new ServiceException("商户不存在");
        }
        // 删除用户
        // 删除角色
        // 删除商户
    }

    /**
     * 重置密码
     *
     * @param id .
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String id) {
        SysTenant tenant = getById(id);
        if (null == tenant) {
            throw new ServiceException("商户不存在");
        }
        isUpdateUser(tenant, tenant);
    }

    /**
     * 获取商户基本配置
     *
     * @param id 商户id
     * @return 商户基本配置
     */
    public TenantBasicConfigDto getTenantBasicConfig(String id) {
        SysTenant tenant = getById(id);
        if (null == tenant) {
            throw new ServiceException("商户不存在");
        }
        return mapstructBasicConfig.toDto(tenant);
    }

    /**
     * 更新商户基本配置
     *
     * @param dto 商户基本配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTenantOrgBasicConfig(TenantBasicConfigDto dto) {
        SysTenant tenant = getById(dto.getId());
        if (null == tenant) {
            throw new ServiceException("商户不存在");
        }
        tenant.setUsedEndTime(dto.getUsedEndTime());
        updateById(tenant);
    }


    private SysTenant convert(TenantSmallDto dto) {
        SysTenant organization = new SysTenant();
        // 设置id
        organization.setId(dto.getId());
        // 设置商户识别码
        organization.setSysCode(dto.getSysCode());
        // 商户名称
        organization.setName(dto.getName());
        // 商户logo
        organization.setLogo(dto.getLogo());

        organization.setParentId(null);
        // 联系人
        organization.setLinkMan(dto.getLinkMan());
        // 联系电话
        organization.setLinkTel(dto.getLinkTel());
        // 联系邮箱
        organization.setLinkEmail(dto.getLinkEmail());
        // 机构地址
        organization.setAddress(dto.getAddress());
        // 总部标识
        organization.setSystem(true);
        // 启用saas
        organization.setSaas(true);
        // 产品ID
        SysProduct product = new SysProduct();
        product.setId(dto.getProductId());
        organization.setProduct(product);
        // 截止日期
        organization.setUsedEndTime(dto.getUsedEndTime());
        //网点等级
        organization.setLevel(1);
        // 机构路径
        organization.setPath(organization.getId());
        // 机构类型
        organization.setType(1);
        // 备注
        organization.setMemo(dto.getMemo());
        //是否启用
        Boolean enabled = dto.getEnabled();
        if (null == enabled) {
            enabled = true;
        }
        organization.setEnabled(enabled);
        // 创建人
        organization.setCreatedBy(dto.getCreatedBy());
        // 创建时间
        organization.setCreated(dto.getCreated());
        // 更新人
        organization.setModifiedBy(dto.getModifiedBy());
        // 更新时间
        organization.setModified(dto.getModified());
        return organization;
    }

    private void initUserRoleProduct(SysTenant organization) {
        // 1、为厂商自动创建管理角色，角色对应权限默认系统所有
        BasRole role = new BasRole();
//        String roleId = IdUtil.getSnowflakeNextIdStr();
//        role.setId(roleId);
        role.setCreated(new Date());
        role.setCreatedBy(organization.getCreatedBy());
        role.setCode(organization.getSysCode());
        role.setSysCode(organization.getSysCode());
        role.setName("管理角色");
        role.setSystem(true);
        role.setEnabled(true);

//        BasOrg basOrg = new BasOrg();
//        basOrg.setId(organization.getId());
//        basOrg.setName(organization.getName());
//        basOrg.setSysCode(organization.getSysCode());
        BasOrg basOrg = BeanUtil.toBean(organization, BasOrg.class);
        role.setOrg(basOrg);
        role.setDescription("系统自动创建-SYSTEM");
        // 1.1 角色-权限绑定
        List<SysTenantPermission> permissions =
                sysTenantPermissionService.findByProductId(organization.getProduct().getId());
        if (CollectionUtil.isEmpty(permissions)) {
            role.setPermissions(null);
        } else {
            List<BasPermission> rolePermission = permissions.stream().map(
                    permission -> {
                        BasPermission basPermission = new BasPermission();
                        basPermission.setId(permission.getId());
                        return basPermission;
                    }
            ).collect(Collectors.toList());
            role.setPermissions(rolePermission);
        }

        // 2、厂商自动开户，同时为账户绑定初始角色
        BasUser user = new BasUser();
        String userId = IdUtil.getSnowflakeNextIdStr();
        user.setId(userId);
        user.setCreated(new Date());
        user.setCreatedBy(organization.getCreatedBy());
        user.setUsername(organization.getLinkTel());
        String pwd = RandomUtil.randomString(9);
        log.info("creat: {}", pwd);
        user.setPassword(PasswordUtil.encoder(pwd));
        user.setNickname("管理员");
        user.setPhone(organization.getLinkTel());
        user.setSystem(true);
        user.setEnabled(true);
        user.setOrg(basOrg);
        user.setSysCode(organization.getSysCode());
        // 3、用户-角色绑定
        user.setRoles(List.of(role));

        // 4、保存
        basRoleRepository.saveAndFlush(role);
        basUserService.save(user);

    }

    /**
     * 更新了产品
     *
     * @param newOrg .
     * @param oldOrg .
     */
    private void isUpdateProduct(SysTenant newOrg, SysTenant oldOrg) {
        SysProduct product = newOrg.getProduct();
        // 查询新产品权限
        List<SysTenantPermission> newProductPermission = sysTenantPermissionService.findByProductId(product.getId());

        sysTenantPermissionService.checkPermission(newProductPermission, List.of(oldOrg.getId()), List.of(oldOrg.getSysCode()));
    }

    /**
     * 更新了预留电话
     *
     * @param newOrg .
     * @param oldOrg .
     */
    private void isUpdateUser(SysTenant newOrg, SysTenant oldOrg) {
        BasUser user = basUserService.findByUsername(oldOrg.getLinkTel());
        if (null == user) {
            throw new ServiceException("机构管理员角色信息异常~~");
        }
        user.setModified(newOrg.getModified());
        user.setModifiedBy(newOrg.getModifiedBy());
        user.setUsername(newOrg.getLinkTel());
        user.setPhone(newOrg.getLinkTel());
        String pwd = RandomUtil.randomString(9);
        log.info("creat: {}", pwd);
        user.setPassword(PasswordUtil.encoder(pwd));
        // 更新用户信息
        basUserService.updateById(user);
    }
}
