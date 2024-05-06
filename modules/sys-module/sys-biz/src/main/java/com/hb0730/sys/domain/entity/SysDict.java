package com.hb0730.sys.domain.entity;

import com.hb0730.jpa.core.domain.BaseEntity;
import com.hb0730.jpa.core.incrementer.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 字典
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "sys_dict")
public class SysDict extends BaseEntity {
    @Id
    @IdGenerator
    private String id;

    /**
     * 字典项
     */
    @OneToMany(mappedBy = "dict", cascade = {jakarta.persistence.CascadeType.ALL}, orphanRemoval = true)
    private List<SysDictItem> items;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    private String name;

    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空")
    private String type;
    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    @Column(name = "`is_enabled`", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;
}
