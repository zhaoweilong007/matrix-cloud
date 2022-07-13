package com.matrix.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.entity.dto.SysAdminLoginDto;
import com.matrix.entity.dto.SysAdminRegisterDto;
import com.matrix.entity.dto.UpdateAdminPasswordDto;
import com.matrix.entity.po.SysAdmin;
import com.matrix.entity.po.SysResource;
import com.matrix.entity.po.SysRole;
import com.matrix.entity.vo.SysAdminUserInfo;
import org.springframework.transaction.annotation.Transactional;

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
     * 注册功能
     */
    String register(SysAdminRegisterDto umsAdminParam);

    /**
     * 登录功能
     *
     */
    SaTokenInfo login(SysAdminLoginDto sysAdminLoginDto);

    /**
     * 刷新token的功能
     *
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户id获取用户信息
     */
    SysAdminUserInfo userInfo(Long id);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<SysAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    int update(Long id, SysAdmin admin);

    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对应角色
     */
    List<SysRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     */
    List<SysResource> getResourceList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordDto updatePasswordParam);


}

