package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.api.system.entity.po.SysConfig;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.system.mapper.SysConfigMapper;
import com.matrix.system.service.SysConfigService;
import java.time.Duration;
import org.springframework.stereotype.Service;

/**
 * SysConfig 服务实现。
 *
 * <p>配置值通过 Redis 缓存，写操作自动驱逐对应缓存。</p>
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private static final String CONFIG_CACHE_PREFIX = "sys:config:";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    @Override
    public String getValueByKey(String key) {
        String cached = RedisUtils.getCacheObject(CONFIG_CACHE_PREFIX + key);
        if (cached != null) {
            return cached;
        }
        SysConfig config = this.getOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, key).last("LIMIT 1"));
        if (config != null) {
            RedisUtils.setCacheObject(CONFIG_CACHE_PREFIX + key, config.getValue(), CACHE_TTL);
            return config.getValue();
        }
        return null;
    }

    @Override
    public void refreshCache() {
        this.list().forEach(c -> RedisUtils.deleteObject(CONFIG_CACHE_PREFIX + c.getConfigKey()));
    }
}
