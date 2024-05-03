package com.hb0730.sys.domain.entity;

import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.basic.domain.entity.BasPermission;
import com.hb0730.jpa.core.domain.BaseEntity;
import com.hb0730.jpa.core.incrementer.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "sys_product")
public class SysProduct extends BaseEntity {
    @Id
    @IdGenerator
    private String id;
    /**
     * 产品对于的菜单与权限
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_product_permission",
            joinColumns = @jakarta.persistence.JoinColumn(name = "product_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "permission_id")
    )
    private List<BasPermission> permissions;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 联系方式
     */
    private String contact;

    /**
     * 站点数量
     */
    private Integer siteNum;
    /**
     * 账号数量
     */
    private Integer accountNum;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    @Column(name = "is_enabled", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled = true;

    /**
     * 权限ID
     */
    public List<String> getPermissionIds() {
        if (CollectionUtil.isEmpty(permissions)) {
            return null;
        }
        return permissions.stream().map(BasPermission::getId).toList();
    }
}
