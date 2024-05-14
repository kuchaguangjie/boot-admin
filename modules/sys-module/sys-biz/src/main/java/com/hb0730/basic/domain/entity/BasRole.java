package com.hb0730.basic.domain.entity;

import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.data.core.domain.BaseTenantEntity;
import com.hb0730.data.core.identifier.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "bas_role")
public class BasRole extends BaseTenantEntity {
    @Id
    @IdGenerator
    private String id;
    /**
     * 权限
     */
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(name = "bas_role_permission",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private List<BasPermission> permissions;
    /**
     * 用户
     */
    @ManyToMany(mappedBy = "roles")
    private List<BasUser> users;
    /**
     * 组织
     */
    @OneToOne
    @JoinColumn(name = "org_id")
    private BasOrg org;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否系统
     */
    @Column(name = "is_system", columnDefinition = "tinyint(1) default 0")
    private Boolean system = false;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled = true;


    public List<String> getPermissionIds() {
        if (CollectionUtil.isNotEmpty(permissions)) {
            return this.permissions.stream().map(BasPermission::getId).toList();
        }
        return null;
    }
}
