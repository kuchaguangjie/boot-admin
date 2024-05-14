package com.hb0730.security.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 刷新用户信息事件
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/13
 */
@Getter
public class RefreshUserEvent extends ApplicationEvent {
    private final String username;
    private final String sysCode;

    public RefreshUserEvent(Object source, String username, String sysCode) {
        super(source);
        this.username = username;
        this.sysCode = sysCode;
    }
}
