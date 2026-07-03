package com.matrix.system.controller;

import com.matrix.api.system.entity.po.SysLoginLog;
import com.matrix.api.system.entity.po.SysOperateLog;
import com.matrix.common.result.R;
import com.matrix.system.service.SysLoginLogService;
import com.matrix.system.service.SysOperateLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部日志保存控制器。
 *
 * <p>供 matrix-log 模块 RemoteLogService Feign 调用，
 * 桥接 matrix-common 实体与 system-api 实体的字段差异。</p>
 */
@Slf4j
@RestController
@RequestMapping("/sys")
@RequiredArgsConstructor
public class SysLogController {

    private final SysOperateLogService operateLogService;
    private final SysLoginLogService loginLogService;

    /**
     * 保存操作日志（内部 Feign 调用）。
     *
     * @param source matrix-common 的操作日志实体
     */
    @PostMapping("/operateLog/save")
    public R<Void> saveOperateLog(@RequestBody com.matrix.common.entity.SysOperateLog source) {
        SysOperateLog target = new SysOperateLog();
        target.setUsername(source.getOperName());
        target.setUserId(source.getUserId());
        target.setModule(source.getTitle());
        target.setName(source.getMethod());
        target.setType(source.getBusinessType());
        target.setRequestMethod(source.getRequestMethod());
        target.setRequestUrl(source.getOperUrl());
        target.setRequestParams(source.getOperParam());
        target.setRequestResult(source.getJsonResult());
        target.setIp(source.getOperIp());
        target.setStatus(source.getStatus());
        target.setErrorMsg(source.getErrorMsg());
        target.setOperateTime(source.getOperTime() != null
                ? source.getOperTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
                : null);
        operateLogService.save(target);
        return R.success();
    }

    /**
     * 保存登录日志（内部 Feign 调用）。
     *
     * @param source matrix-common 的登录日志实体
     */
    @PostMapping("/loginLog/save")
    public R<Void> saveLoginLog(@RequestBody com.matrix.common.entity.SysLoginLog source) {
        SysLoginLog target = new SysLoginLog();
        target.setUsername(source.getUserName());
        target.setIp(source.getIpaddr());
        target.setStatus(source.getStatus() != null ? Integer.valueOf(source.getStatus()) : null);
        target.setResult(source.getMsg());
        target.setLoginTime(source.getLoginTime() != null
                ? new java.util.Date(source.getLoginTime().getTime()).toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
                : null);
        loginLogService.save(target);
        return R.success();
    }
}
