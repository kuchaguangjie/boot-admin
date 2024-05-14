package com.hb0730.common.controller;

import com.hb0730.base.R;
import com.hb0730.base.utils.PasswordUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.basic.domain.entity.BasUser;
import com.hb0730.basic.service.BasUserService;
import com.hb0730.common.controller.mapstruct.UserMapstruct;
import com.hb0730.common.domain.vo.ChangePasswordVo;
import com.hb0730.common.domain.vo.UserVo;
import com.hb0730.common.service.CommonsOssService;
import com.hb0730.security.domain.dto.UserInfoDto;
import com.hb0730.security.event.RefreshUserEvent;
import com.hb0730.security.utils.SecurityUtil;
import com.hb0730.sys.domain.entity.SysUser;
import com.hb0730.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/13
 */
@RestController
@RequestMapping("/common/user")
@Tag(name = "公共接口：当前用户")
@Slf4j
@RequiredArgsConstructor
public class CommonUserController {
    private final SysUserService sysUserService;
    private final BasUserService basUserService;
    private final UserMapstruct userMapstruct;
    private final CommonsOssService commonsOssService;
    private final ApplicationContext applicationContext;

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping
    @Operation(summary = "获取当前用户信息")
    public R<UserVo> getCurrentUserInfo() {
        String sysCode = SecurityUtil.getSysCode();
        String username = SecurityUtil.getUsername();
        UserVo vo = null;
        if (StrUtil.isBlank(sysCode)) {
            SysUser user = sysUserService.findByUsername(username);
            vo = userMapstruct.toVo(user);

        } else {
            BasUser user = basUserService.findByUsername(username);
            vo = userMapstruct.toVo(user);
        }
        return R.OK(vo);
    }

    /**
     * 更新用户信息
     *
     * @param vo 用户信息
     * @return 是否成功
     */
    @PutMapping
    @Operation(summary = "更新当前用户信息")
    public R<String> changeUserInfo(@RequestBody UserVo vo) {
        String username = vo.getUsername();
        if (StrUtil.isBlank(username)) {
            return R.NG("用户名不能为空");
        }
        UserInfoDto currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return R.NG("用户未登录");
        }

        if (!StrUtil.equals(username, currentUser.getUsername())) {
            return R.NG("无权修改他人信息");
        }

        if (Boolean.TRUE.equals(currentUser.getSystem())) {
            return R.NG("系统用户不允许修改");
        }

        updateUserInfo(vo);

        return R.OK("更新成功");
    }


    /**
     * 修改密码
     *
     * @return 是否成功
     */
    @PutMapping("/password")
    @Operation(summary = "修改当前用户密码")
    public R<String> changePassword(@RequestBody ChangePasswordVo vo) {
        UserInfoDto currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return R.NG("用户未登录");
        }
        if (Boolean.TRUE.equals(currentUser.getSystem())) {
            return R.NG("系统用户不允许修改密码");
        }
        String oldPassword = vo.getOldPassword();
        String password = getPassword();
        if (!PasswordUtil.matches(oldPassword, password)) {
            return R.NG("原密码错误");
        }

        String newPassword = vo.getNewPassword();
        if (StrUtil.isBlank(newPassword)) {
            return R.NG("新密码不能为空");
        }
        if (PasswordUtil.matches(newPassword, password)) {
            return R.NG("新密码不能与原密码相同");
        }
        // 更新密码
        updatePassword(PasswordUtil.encoder(newPassword));
        applicationContext.publishEvent(new RefreshUserEvent(this, currentUser.getUsername(),
                currentUser.getSysCode()));

        return R.OK("修改成功");
    }

    /**
     * 修改头像
     *
     * @return 是否成功
     */
    @PutMapping("/avatar")
    @Operation(summary = "修改当前用户头像")
    public R<String> changeAvatar(String avatarUrl) {
        UserInfoDto currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return R.NG("用户未登录");
        }
        if (Boolean.TRUE.equals(currentUser.getSystem())) {
            return R.NG("系统用户不允许修改头像");
        }
        if (StrUtil.isBlank(avatarUrl)) {
            return R.NG("头像地址不能为空");
        }
        // 更新头像
        updateAvatar(avatarUrl);
        applicationContext.publishEvent(new RefreshUserEvent(this, currentUser.getUsername(),
                currentUser.getSysCode()));
        return R.OK();
    }


    private String getPassword() {
        String sysCode = SecurityUtil.getSysCode();
        String username = SecurityUtil.getUsername();
        if (StrUtil.isBlank(sysCode)) {
            SysUser user = sysUserService.findByUsername(username);
            return user.getPassword();
        } else {
            BasUser user = basUserService.findByUsername(username);
            return user.getPassword();
        }
    }

    private void updatePassword(String password) {
        String sysCode = SecurityUtil.getSysCode();
        String username = SecurityUtil.getUsername();
        if (StrUtil.isBlank(sysCode)) {
            SysUser user = sysUserService.findByUsername(username);
            user.setPassword(password);
            sysUserService.updateById(user);
        } else {
            BasUser user = basUserService.findByUsername(username);
            user.setPassword(password);
            basUserService.updateById(user);
        }
    }

    private void updateUserInfo(UserVo vo) {
        String sysCode = SecurityUtil.getSysCode();
        String username = SecurityUtil.getUsername();
        if (StrUtil.isBlank(sysCode)) {
            SysUser user = sysUserService.findByUsername(username);
            user.setNickname(vo.getNickname());
            user.setEmail(vo.getEmail());
            user.setPhone(vo.getPhone());
            user.setGender(vo.getGender());
            sysUserService.updateById(user);
        } else {
            BasUser user = basUserService.findByUsername(username);
            user.setNickname(vo.getNickname());
            user.setEmail(vo.getEmail());
            user.setPhone(vo.getPhone());
            user.setGender(vo.getGender());
            basUserService.updateById(user);
        }
    }

    private void updateAvatar(String avatarUrl) {
        String sysCode = SecurityUtil.getSysCode();
        String username = SecurityUtil.getUsername();
        if (StrUtil.isBlank(sysCode)) {
            SysUser user = sysUserService.findByUsername(username);
            user.setAvatar(avatarUrl);
            sysUserService.updateById(user);
        } else {
            BasUser user = basUserService.findByUsername(username);
            user.setAvatar(avatarUrl);
            basUserService.updateById(user);
        }
    }
}
