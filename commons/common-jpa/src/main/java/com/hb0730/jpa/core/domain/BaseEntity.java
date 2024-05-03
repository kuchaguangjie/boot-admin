package com.hb0730.jpa.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/3/23
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    /**
     * 创建时间
     */
    @Column(name = "created", updatable = false)
    @CreatedDate
    private Date created;
    /**
     * 创建人
     */
    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;
    /**
     * 修改时间
     */
    @Column(name = "modified", insertable = false)
    @LastModifiedDate
    private Date modified;
    /**
     * 修改人
     */
    @Column(name = "modified_by", insertable = false)
    @LastModifiedBy
    private String modifiedBy;
}