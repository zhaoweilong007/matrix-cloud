package com.matrix.translation.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.common.context.TenantContextHolder;
import com.matrix.translation.entity.SysUserTrans;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户翻译 Mapper，用于 easy-trans 跨租户查询用户信息
 */
@Mapper
public interface SysUserTransMapper extends BaseMapper<SysUserTrans> {

    /**
     * 查询用户列表，自动忽略租户隔离
     */
    @Override
    default List<SysUserTrans> selectList(Wrapper<SysUserTrans> queryWrapper) {
        Boolean oldIgnore = TenantContextHolder.isIgnore();
        try {
            TenantContextHolder.setIgnore(true);
            return this.selectList_(queryWrapper);
        } finally {
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

    @Select("""
                select user_name,id from sys_user ${ew.getCustomSqlSegment}
            """)
    List<SysUserTrans> selectList_(@Param("ew") Wrapper<SysUserTrans> queryWrapper);

    /**
     * 查询单个用户，自动忽略租户隔离
     */
    @Override
    default SysUserTrans selectOne(Wrapper<SysUserTrans> queryWrapper) {
        Boolean oldIgnore = TenantContextHolder.isIgnore();
        try {
            TenantContextHolder.setIgnore(true);
            return this.selectOne_(queryWrapper);
        } finally {
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

    @Select("""
              select user_name,id from sys_user ${ew.getCustomSqlSegment} limit 1
            """)
    SysUserTrans selectOne_(@Param("ew") Wrapper<SysUserTrans> queryWrapper);
}
