package com.matrix.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.api.system.entity.po.SysDept;
import java.util.List;

/**
 * 部门服务接口。
 */
public interface SysDeptService extends IService<SysDept> {

    /** 构建部门树 */
    List<SysDept> buildTree();

    /** 判断是否有子部门 */
    boolean hasChildren(Long deptId);
}
