package com.hb0730.common.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/13
 */
@Data
@EqualsAndHashCode
@ToString
public class UserVo implements java.io.Serializable {
    private String id;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "所属机构")
    private OrgSmallVo org;
    /**
     * 岗位
     */
    @Schema(description = "岗位")
    private List<SmallVo> posts;
    /**
     * 角色
     */
    @Schema(description = "角色")
    private List<SmallVo> roles;
    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
    /**
     * 手机
     */
    @Schema(description = "手机")
    private String phone;
    /**
     * 性别
     */
    @Schema(description = "性别")
    private Integer gender;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

    /**
     * 机构
     */
    @Data
    @EqualsAndHashCode
    @ToString
    public static class OrgSmallVo implements Serializable {
        private String id;
        private String name;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class SmallVo implements Serializable {
        private String code;
        private String name;
    }
}
