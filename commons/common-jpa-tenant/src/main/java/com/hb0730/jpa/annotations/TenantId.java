package com.hb0730.jpa.annotations;

import com.hb0730.jpa.generator.TenantIdValueGeneration;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 租户id,在新增时，会自动设置租户id
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/7
 */
@ValueGenerationType(generatedBy = TenantIdValueGeneration.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface TenantId {
}
