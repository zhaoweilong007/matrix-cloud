package com.matrix.entity.vo;

import com.matrix.entity.po.SysAdmin;
import com.matrix.entity.po.SysMenu;
import com.matrix.entity.po.SysRole;
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
