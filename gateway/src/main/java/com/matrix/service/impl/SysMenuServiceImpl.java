package com.matrix.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.convert.ConvertMapper;
import com.matrix.entity.dto.SysMenuDto;
import com.matrix.entity.po.SysMenu;
import com.matrix.mapper.SysMenuMapper;
import com.matrix.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (SysMenu)表服务实现类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public Boolean create(SysMenuDto sysMenuDto) {
        updateLevel(sysMenuDto);
        SysMenu sysMenu = ConvertMapper.INSTALL.convert(sysMenuDto);
        return this.save(sysMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(SysMenuDto sysMenu) {
        if (sysMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            sysMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            SysMenu parentMenu = baseMapper.selectById(sysMenu.getParentId());
            if (parentMenu != null) {
                sysMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                sysMenu.setLevel(0);
            }
        }
    }

    @Override
    public Boolean update(SysMenuDto sysMenuDto) {
        updateLevel(sysMenuDto);
        SysMenu sysMenu = ConvertMapper.INSTALL.convert(sysMenuDto);
        return this.updateById(sysMenu);
    }

    @Override
    public SysMenu getItem(Long id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(Long id) {
        return this.delete(id);
    }

    @Override
    public PageDTO<SysMenu> list(Long parentId, PageDTO<SysMenu> page) {
        return baseMapper.selectPage(page, Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, parentId).orderByAsc(SysMenu::getSort));

    }

    @Override
    public List<SysMenu> treeList() {
        List<SysMenu> menus = this.list();
        return menus.stream().filter(sysMenu -> sysMenu.getParentId() == 0).peek(sysMenu -> {
            sysMenu.setChildren(getChildren(sysMenu.getId(), menus));
        }).collect(Collectors.toList());
    }

    private List<SysMenu> getChildren(Long id, List<SysMenu> menus) {
        List<SysMenu> children = menus.stream().filter(menu -> menu.getParentId().equals(id)).collect(Collectors.toList());
        for (SysMenu menu : children) {
            menu.setChildren(getChildren(menu.getId(), menus));
        }
        return children;
    }

    @Override
    public Boolean updateHidden(Long id, Integer hidden) {
        SysMenu sysMenu = this.getById(id);
        if (sysMenu == null) {
            return false;
        }
        return sysMenu.setHidden(hidden).updateById();
    }
}

