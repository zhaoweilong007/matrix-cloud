package com.matrix.datapermission.util;

import com.matrix.common.entity.BaseEntity;
import com.matrix.datapermission.annotation.DataPermission;
import com.matrix.datapermission.aop.DataPermissionContextHolder;
import com.matrix.datapermission.rule.DataPermissionRule;

import java.lang.annotation.Annotation;

/**
 * 数据权限 Util
 */
public class DataPermissionUtils {

    private static final DataPermission DATA_PERMISSION_DISABLE = new DataPermission() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return DataPermission.class;
        }

        @Override
        public boolean enable() {
            return false;
        }

        @Override
        public Class<? extends DataPermissionRule>[] includeRules() {
            return new Class[0];
        }

        @Override
        public Class<? extends DataPermissionRule>[] excludeRules() {
            return new Class[0];
        }

        @Override
        public Class<? extends BaseEntity>[] excludeTables() {
            return new Class[0];
        }
    };


    /**
     * 忽略数据权限，执行对应的逻辑
     *
     * @param runnable 逻辑
     */
    public static void executeIgnore(Runnable runnable) {
        DataPermissionContextHolder.add(DATA_PERMISSION_DISABLE);
        try {
            // 执行 runnable
            runnable.run();
        } finally {
            DataPermissionContextHolder.remove();
        }
    }


}
