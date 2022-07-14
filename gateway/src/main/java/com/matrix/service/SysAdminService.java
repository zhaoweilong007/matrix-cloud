package com.matrix.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.entity.dto.UpdateAdminPasswordDto;
import com.matrix.entity.po.SysAdmin;
import com.matrix.entity.vo.SysAdminUserInfo;

import java.util.List;

/**
 * (SysAdmin)表服务接口
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
public interface SysAdminService extends IService<SysAdmin> {

    /**
     * 根据用户名获取后台管理员
     */
    SysAdmin getAdminByUsername(String username);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<SysAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改密码
     */
    Boolean updatePassword(UpdateAdminPasswordDto updatePasswordParam);

    /**
     * 根据用户id获取用户信息
     */
    SysAdminUserInfo userInfo(Long id);


    Boolean updateHidden(Long id, Integer status);
}

