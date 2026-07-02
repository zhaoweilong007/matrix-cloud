package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志实体。
 *
 * <p>记录每次登录尝试，不参与多租户隔离（超级管理员可查看所有日志）。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
@Schema(description = "登录日志")
public class SysLoginLog extends BaseEntity {

    /** 用户名 */
    private String username;

    /** 登录 IP */
    private String ip;

    /** 浏览器 UserAgent */
    private String userAgent;

    /** 登录状态：0-失败 1-成功 */
    private Integer status;

    /** 登录结果描述 */
    private String result;

    /** 登录时间 */
    private LocalDateTime loginTime;
}
