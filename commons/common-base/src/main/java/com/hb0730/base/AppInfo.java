package com.hb0730.base;

import lombok.Data;
import lombok.ToString;

/**
 * 应用信息
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/25
 */
@Data
@ToString
public class AppInfo {
    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用版本
     */
    private String version;
    /**
     * 应用端口
     */
    private String port;
    /**
     * 应用上下文
     */
    private String contextPath;
    /**
     * 应用PID
     */
    private String pid;
    /**
     * 主机
     */
    private String host;
    /**
     * 用户名
     */
    private String username;


}