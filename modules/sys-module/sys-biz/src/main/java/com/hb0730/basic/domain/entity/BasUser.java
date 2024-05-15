package com.hb0730.basic.domain.entity;

import com.hb0730.data.core.domain.BaseEntity;
import com.hb0730.data.core.identifier.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "bas_user")
public class BasUser extends BaseEntity {
    @Id
    @IdGenerator
    private String id;
    /**
     * 所属机构
     */
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private BasOrg org;

    /**
     * 所属岗位
     */
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(name = "bas_user_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<BasPost> posts;
    /**
     * 角色
     */
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(name = "bas_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<BasRole> roles;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别,0 保密,1 男,2 女
     */
    private Integer gender = 0;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 密码重置时间
     */
    private Date pwdResetTime;
    /**
     * 是否系统用户
     */
    @Column(name = "is_system", columnDefinition = "bit(1) default 0")
    public Boolean system = false;

    /**
     * 状态
     */
    @Column(name = "is_enabled", columnDefinition = "bit(1) default 1")
    private Boolean enabled = true;

    /**
     * 商户识别码
     */
    private String sysCode;
}
