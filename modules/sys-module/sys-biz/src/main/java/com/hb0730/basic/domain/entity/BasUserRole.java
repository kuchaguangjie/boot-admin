package com.hb0730.basic.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/28
 */
@Getter
@Setter
@Entity
@Table(name = "bas_user_role")
@AllArgsConstructor
@NoArgsConstructor
public class BasUserRole implements Serializable {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Id
    @Column(name = "role_id")
    private String roleId;
}
