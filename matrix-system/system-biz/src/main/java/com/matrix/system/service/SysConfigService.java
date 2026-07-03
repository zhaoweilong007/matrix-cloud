package com.matrix.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.api.system.entity.po.SysConfig;

/**
 * SysConfig 服务接口。
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 按键名获取配置值（带缓存）。
     *
     * @param key 配置键名
     * @return 配置值，不存在返回 null
     */
    String getValueByKey(String key);

    /**
     * 刷新所有配置缓存。
     */
    void refreshCache();
}
