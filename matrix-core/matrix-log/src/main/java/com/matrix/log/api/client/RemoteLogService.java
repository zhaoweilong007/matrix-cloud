package com.matrix.log.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.entity.SysLoginLog;
import com.matrix.common.entity.SysOperateLog;
import com.matrix.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 日志服务
 */
@FeignClient(contextId = "RemoteLogService", value = ServerNameConstants.SYSTEM, path = "/sys")
public interface RemoteLogService {

    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @return 结果
     */
    @PostMapping("/operateLog/save")
    R<?> saveLog(@RequestBody SysOperateLog sysOperLog);

    /**
     * 保存访问记录
     *
     * @param sysLoginLog 访问实体
     * @return 结果
     */
    @PostMapping("/loginLog/save")
    R<?> saveLoginLog(@RequestBody SysLoginLog sysLoginLog);
}
