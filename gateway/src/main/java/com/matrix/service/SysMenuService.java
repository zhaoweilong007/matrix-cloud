package com.matrix.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.entity.dto.SysMenuDto;
import com.matrix.entity.po.SysMenu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (SysMenu)表服务接口
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 创建后台菜单
     */
    Boolean create(SysMenuDto sysMenuDto);

    /**
     * 修改后台菜单
     */
    Boolean update(SysMenuDto sysMenuDto);

    /**
     * 根据ID获取菜单详情
     */
    SysMenu getItem(Long id);

    /**
     * 根据ID删除菜单
     */
    Boolean delete(Long id);

    /**
     * 分页查询后台菜单
     */
    PageDTO<SysMenu> list(Long parentId, PageDTO<SysMenu> page);

    /**
     * 树形结构返回所有菜单列表
     */
    List<SysMenu> treeList();

    /**
     * 修改菜单显示状态
     */
    Boolean updateHidden(Long id, Integer hidden);

    /**
     * 分配菜单
     *
     * @param roleId  权限id
     * @param menuIds 菜单ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean assignMenu(Long roleId, List<Long> menuIds);

    List<SysMenu> getMenuByAdminId(Long adminId);

    List<SysMenu> getMenuByRoleId(Long roleId);
}

