package com.hb0730.query.jpa.core.handler;

import com.hb0730.base.context.UserContext;
import com.hb0730.base.context.UserInfo;
import com.hb0730.base.exception.QueryException;
import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.query.annotation.DataPermission;
import com.hb0730.query.jpa.core.AbstractPredicateHandler;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 数据权限处理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/15
 */
public class DataPermissionPredicateHandler extends AbstractPredicateHandler {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return DataPermission.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, @Nullable Object value, @Nullable Annotation annotation) {
        DataPermission dataPermission = (DataPermission) annotation;
        if (null == dataPermission) {
            return null;
        }
        //拿取当前用户上下文
        UserInfo userInfo = UserContext.get();
        if (null == userInfo) {
            throw new QueryException("数据权限处理异常!,用户上下文为空");
        }
        // 获取当前用户的数据权限
        List<String> dataScopes = userInfo.getDataScopes();
        if (CollectionUtil.isNotEmpty(dataScopes)) {
            if (StrUtil.isBlank(dataPermission.field())) {
                throw new QueryException("数据权限处理异常!,数据权限字段为空");
            }
            if (StrUtil.isNotBlank(dataPermission.joinName())) {
                return criteriaBuilder.and(
                        from.join(dataPermission.joinName()).get(dataPermission.field()).in(dataScopes)
                );
            } else {
                return criteriaBuilder.and(
                        from.get(dataPermission.field()).in(dataScopes)
                );
            }
        }
        return null;
    }
}
