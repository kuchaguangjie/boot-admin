package com.hb0730.base;

import com.hb0730.base.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

import java.net.InetAddress;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/3/22
 */
@Slf4j
public class BootAdminApp implements ApplicationContextAware, ApplicationRunner {
    private ApplicationContext applicationContext;
    private static final AppInfo appInfo = new AppInfo();

    /**
     * 启动
     *
     * @param clazz 启动类
     * @param args  启动参数
     */
    public static void bootstrap(Class<?> clazz, String... args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("{} ~~启动开始~~", clazz.getCanonicalName());
        SpringApplication springApplication = new SpringApplication(clazz);
        springApplication.run(args);
        stopWatch.stop();
        log.info("{} ~~启动完成~~", clazz.getCanonicalName());
        appInfo.setName(AppUtil.getProperty("spring.application.name", "@"));
        appInfo.setVersion(AppUtil.getProperty("spring.application.version", ""));
        appInfo.setPort(AppUtil.getProperty("server.port", "8080"));
        appInfo.setContextPath(AppUtil.getProperty("server.servlet.context-path", ""));
        appInfo.setPid(StrUtil.getValue("", () -> new ApplicationPid().toString(), "@"));
        appInfo.setHost(StrUtil.getValue("", () -> InetAddress.getLocalHost().getHostName(), "@"));
        appInfo.setUsername(StrUtil.getValue("", () -> System.getProperty("user.name"), "@"));

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        doSomethingAfterBooted();
        doShowBootedInfo();
    }

    @Bean
    public DisposableBean disposableBean() {
        return () -> {
            log.info("~~应用关闭中~~");
            doSomethingWhenShutdown();
        };
    }


    protected void doSomethingAfterBooted() {
    }

    protected void doSomethingWhenShutdown() {
    }

    protected void doShowBootedInfo() {
        Environment env = applicationContext.getEnvironment();
        log.info("启动完成 -> {}:{}",
                env.getProperty("server.servlet.context-path", "''"),
                env.getProperty("server.port"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}