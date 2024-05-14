package com.hb0730.data.config;

import com.hb0730.base.core.UserContext;
import com.hb0730.base.core.UserInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
@EnableJpaAuditing
@Configuration
public class AuditorConfig implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        UserInfo userInfo = UserContext.get();
        if (null != userInfo) {
            return Optional.of(userInfo.getUsername());
        }
        return Optional.empty();
    }
}
