package com.hb0730.jpa.generator;

import com.hb0730.base.core.UserContext;
import com.hb0730.base.core.UserInfo;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

import static org.hibernate.generator.EventTypeSets.INSERT_ONLY;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/7
 */
public class TenantIdValueGeneration implements BeforeExecutionGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        UserInfo userInfo = UserContext.get();
        if (null != userInfo && null == currentValue) {
            return userInfo.getSysCode();
        }
        return currentValue;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return INSERT_ONLY;
    }
}
