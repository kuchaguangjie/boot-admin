package com.hb0730.sys.domain.entity;

import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.jpa.core.domain.BaseEntity;
import com.hb0730.jpa.core.incrementer.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "sys_role")
public class SysRole extends BaseEntity {

    @Id
    @IdGenerator
    private String id;
    /**
     * 权限
     */
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
            name = "sys_role_permission",
            joinColumns = @jakarta.persistence.JoinColumn(name = "role_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "permission_id")
    )
    private List<SysPermission> permissions;
    /**
     * 用户
     */
    @ManyToMany(mappedBy = "roles")
    public List<SysUser> users;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否启用
     */
    @Column(name = "is_enabled", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled = true;


    /**
     * 获取权限id
     *
     * @return 权限id
     */
    public List<String> getPermissionIds() {
        if (CollectionUtil.isEmpty(permissions)) {
            return null;
        }
        return CollectionUtil.newArrayList(permissions.stream().map(SysPermission::getId).iterator());
    }
}
