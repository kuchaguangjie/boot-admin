package com.hb0730.data.tenant.autoconfigure;

import com.hb0730.data.tenant.configuration.DiscriminatorConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/14
 */
@AutoConfiguration
@Import({
        DiscriminatorConfiguration.class
})
public class DataTenantAutoConfiguration {
}
