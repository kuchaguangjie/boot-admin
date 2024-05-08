package com.hb0730.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@Getter
public class OssRefreshEvent extends ApplicationEvent {
    private final String sysCode;

    public OssRefreshEvent(String sysCode, Object source) {
        super(source);
        this.sysCode = sysCode;
    }
}
