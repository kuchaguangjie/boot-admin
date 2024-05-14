package com.hb0730.query.jpa.core.handler;

import com.hb0730.query.annotation.LikeLeft;
import com.hb0730.query.jpa.core.AbstractPredicateHandler;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

import java.lang.annotation.Annotation;

/**
 * 构建“like查询”({@code field like '%xxx'})场景的 {@link Predicate} 处理器.
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
public class LikeLeftPredicateHandler extends AbstractPredicateHandler {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return LikeLeft.class;
    }

    @Override
    public <Z, X> Predicate _buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, @Nonnull Object value) {
        return criteriaBuilder.and(
                super.buildEndsWithPredicate(criteriaBuilder, from, fieldName, value)
        );
    }
}
