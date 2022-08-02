package com.matrix.api.system.entity.vo;

import com.matrix.api.system.entity.po.SysAdmin;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.api.system.entity.po.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * 描述： 用户信息
 *
 * @author zwl
 * @since 2022/7/13 15:14
 **/
@Data
public class SysAdminUserInfo {

    private SysAdmin sysAdmin;

    private List<SysMenu> menus;

    private List<SysRole> roles;

}
