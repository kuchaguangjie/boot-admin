package com.hb0730.basic.domain.entity;

import com.hb0730.jpa.core.BaseTenantEntity;
import com.hb0730.jpa.core.incrementer.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 岗位
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/7
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "bas_post")
public class BasPost extends BaseTenantEntity {

    @Id
    @IdGenerator
    private String id;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 岗位描述
     */
    private String description;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     */
    @Column(name = "is_enabled", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;

}
