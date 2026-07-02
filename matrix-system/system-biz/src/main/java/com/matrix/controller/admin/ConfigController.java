package com.matrix.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.api.system.entity.dto.ConfigDto;
import com.matrix.api.system.entity.po.SysConfig;
import com.matrix.common.result.R;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/config")
@Tag(name = "配置管理")
@RequiredArgsConstructor
public class ConfigController {

    private static final String CONFIG_CACHE_PREFIX = "sys:config:";

    private final SysConfigService configService;

    @GetMapping("/page")
    @Operation(summary = "配置分页列表")
    public R<Page<SysConfig>> page(Page<SysConfig> page) {
        return R.success(configService.page(page, new LambdaQueryWrapper<SysConfig>().orderByAsc(SysConfig::getConfigKey)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "配置详情")
    public R<SysConfig> getById(@PathVariable Long id) { return R.success(configService.getById(id)); }

    @GetMapping("/get-by-key/{key}")
    @Operation(summary = "按键名获取配置值")
    public R<String> getByKey(@PathVariable String key) {
        String cached = RedisUtils.getCacheObject(CONFIG_CACHE_PREFIX + key);
        if (cached != null) return R.success(cached);
        SysConfig config = configService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
        if (config != null) {
            RedisUtils.setCacheObject(CONFIG_CACHE_PREFIX + key, config.getValue(), Duration.ofHours(1));
            return R.success(config.getValue());
        }
        return R.success(null);
    }

    @PostMapping
    @Operation(summary = "新增配置")
    public R<Boolean> create(@RequestBody ConfigDto dto) {
        SysConfig entity = new SysConfig(); entity.setName(dto.getName());
        entity.setConfigKey(dto.getConfigKey()); entity.setValue(dto.getValue());
        entity.setType(dto.getType()); entity.setVisible(dto.getVisible()); entity.setRemark(dto.getRemark());
        return R.success(configService.save(entity));
    }

    @PutMapping
    @Operation(summary = "修改配置")
    public R<Boolean> update(@RequestBody ConfigDto dto) {
        SysConfig entity = new SysConfig(); entity.setId(dto.getId()); entity.setName(dto.getName());
        entity.setConfigKey(dto.getConfigKey()); entity.setValue(dto.getValue());
        entity.setType(dto.getType()); entity.setVisible(dto.getVisible()); entity.setRemark(dto.getRemark());
        boolean ok = configService.updateById(entity);
        if (ok) RedisUtils.deleteObject(CONFIG_CACHE_PREFIX + dto.getConfigKey());
        return R.success(ok);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置")
    public R<Boolean> delete(@PathVariable Long id) {
        SysConfig config = configService.getById(id);
        if (config != null && config.getType() == 1) return R.fail("系统内置参数不可删除");
        boolean ok = configService.removeById(id);
        if (ok && config != null) RedisUtils.deleteObject(CONFIG_CACHE_PREFIX + config.getConfigKey());
        return R.success(ok);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新配置缓存")
    public R<Boolean> refresh() {
        configService.list().forEach(c -> RedisUtils.deleteObject(CONFIG_CACHE_PREFIX + c.getConfigKey()));
        return R.success(true);
    }
}
