package com.matrix.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysDept;
import com.matrix.mapper.SysDeptMapper;
import com.matrix.service.SysDeptService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 部门服务实现。
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> buildTree() {
        List<SysDept> all = list(new LambdaQueryWrapper<SysDept>().orderByAsc(SysDept::getSort));
        Map<Long, List<SysDept>> parentMap = all.stream()
                .filter(d -> d.getParentId() != null && d.getParentId() != 0)
                .collect(Collectors.groupingBy(SysDept::getParentId));
        List<SysDept> roots = all.stream()
                .filter(d -> d.getParentId() == null || d.getParentId() == 0)
                .collect(Collectors.toList());
        for (SysDept root : roots) {
            attachChildren(root, parentMap);
        }
        return roots;
    }

    private void attachChildren(SysDept parent, Map<Long, List<SysDept>> parentMap) {
        List<SysDept> children = parentMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);
        for (SysDept child : children) {
            attachChildren(child, parentMap);
        }
    }

    @Override
    public boolean hasChildren(Long deptId) {
        return count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, deptId)) > 0;
    }
}
