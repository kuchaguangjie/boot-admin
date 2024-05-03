package com.hb0730.query.jpa.core.handler;


import com.hb0730.query.annotation.IsNull;
import com.hb0730.query.jpa.core.AbstractPredicateHandler;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

/**
 * 构建“是 NULL 条件”({@code field IS NULL})场景的 {@link Predicate} 处理器.
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/27
 */
public class IsNullPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<IsNull> getAnnotation() {
        return IsNull.class;
    }

    @Override
    public <Z, X> Predicate _buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, @Nonnull Object value) {
        return criteriaBuilder.and(super.buildIsNullPredicate(criteriaBuilder, from, value));
    }

}