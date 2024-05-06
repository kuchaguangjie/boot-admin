package com.hb0730.sys.domain.entity;

import com.hb0730.jpa.core.domain.BaseEntity;
import com.hb0730.jpa.core.incrementer.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 字典项
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "sys_dict_item")
public class SysDictItem extends BaseEntity {
    /**
     * id
     */
    @Id
    @IdGenerator
    private String id;
    /**
     * 字典id
     */
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "dict_id")
    private SysDict dict;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典值
     */
    private String value;
    /**
     * 标签
     */
    private String label;
    /**
     * 排序
     */
    private Integer sort;
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
