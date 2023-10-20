package com.matrix.translation.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.common.context.TenantContextHolder;
import com.matrix.translation.entity.SysUserTrans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/31
 **/
@Mapper
public interface SysUserTransMapper extends BaseMapper<SysUserTrans> {


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
