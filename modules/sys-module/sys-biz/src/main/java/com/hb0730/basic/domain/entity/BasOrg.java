package com.hb0730.basic.domain.entity;

import com.hb0730.data.core.domain.BaseTenantEntity;
import com.hb0730.data.core.identifier.IdGenerator;
import com.hb0730.sys.domain.entity.SysProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "bas_organization")
public class BasOrg extends BaseTenantEntity {

    @Id
    @IdGenerator
    private String id;
    /**
     * 用户
     */
    @OneToMany(mappedBy = "org")
    private List<BasUser> users;
    /**
     * 父级id
     */
    private String parentId;
    /**
     * 机构名称
     */
    private String name;
    /**
     * logo
     */
    private String logo;
    /**
     * 产品
     */
    @OneToOne
    @JoinColumn(name = "product_id")
    private SysProduct product;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系电话
     */
    private String linkTel;
    /**
     * 联系邮箱
     */
    private String linkEmail;
    /**
     * 机构地址
     */
    private String address;
    /**
     * 到期时间
     */
    private Date usedEndTime;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 路径
     */
    private String path;
    /**
     * 机构类型
     * 1 商户
     * 2 机构
     * 3 部门
     */
    private Integer type = 2;
    /**
     * 备注
     */
    private String memo;
    /**
     * 是否系统机构
     */
    @Column(name = "`is_system`", columnDefinition = "tinyint(1) default 0")
    private Boolean system = false;

    /**
     * 是否启用
     */
    @Column(name = "`is_enabled`", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled = true;
    /**
     * 是否启用saas
     */
    @Column(name = "`is_saas`", columnDefinition = "tinyint(1) default 0")
    private Boolean saas = false;
}
