package com.matrix.datapermission.aop;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.matrix.datapermission.annotation.DataPermission;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link DataPermission} 注解的 Context 上下文
 */
public class DataPermissionContextHolder {

    /**
     * 使用 List 存储嵌套调用的权限栈。
     * 重写 TTL 复制机制，当进行异步传递时执行深拷贝克隆，避免父子线程共享同一个 list 导致并发冲突
     */
    private static final ThreadLocal<LinkedList<DataPermission>> DATA_PERMISSIONS =
            new TransmittableThreadLocal<LinkedList<DataPermission>>() {
                @Override
                protected LinkedList<DataPermission> initialValue() {
                    return new LinkedList<>();
                }

                @Override
                public LinkedList<DataPermission> copy(LinkedList<DataPermission> parentValue) {
                    return parentValue != null ? new LinkedList<>(parentValue) : null;
                }

                @Override
                protected LinkedList<DataPermission> childValue(LinkedList<DataPermission> parentValue) {
                    return copy(parentValue);
                }
            };

    /**
     * 获得当前的 DataPermission 注解
     *
     * @return DataPermission 注解
     */
    public static DataPermission get() {
        return DATA_PERMISSIONS.get().peekLast();
    }

    /**
     * 入栈 DataPermission 注解
     *
     * @param dataPermission DataPermission 注解
     */
    public static void add(DataPermission dataPermission) {
        DATA_PERMISSIONS.get().addLast(dataPermission);
    }

    /**
     * 出栈 DataPermission 注解
     *
     * @return DataPermission 注解
     */
    public static DataPermission remove() {
        DataPermission dataPermission = DATA_PERMISSIONS.get().removeLast();
        // 无元素时，清空 ThreadLocal
        if (DATA_PERMISSIONS.get().isEmpty()) {
            DATA_PERMISSIONS.remove();
        }
        return dataPermission;
    }

    /**
     * 获得所有 DataPermission
     *
     * @return DataPermission 队列
     */
    public static List<DataPermission> getAll() {
        return DATA_PERMISSIONS.get();
    }

    /**
     * 清空上下文
     * <p>
     * 目前仅仅用于单测
     */
    public static void clear() {
        DATA_PERMISSIONS.remove();
    }
}
