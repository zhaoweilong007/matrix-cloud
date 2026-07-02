package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志实体。
 *
 * <p>记录用户操作行为，不参与多租户隔离。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_operate_log")
@Schema(description = "操作日志")
public class SysOperateLog extends BaseEntity {

    /** 操作人用户名 */
    private String username;

    /** 操作人用户 ID */
    private Long userId;

    /** 部门 ID */
    private Long deptId;

    /** 模块名 */
    private String module;

    /** 操作名称 */
    private String name;

    /** 操作类型 */
    private Integer type;

    /** 请求方法 */
    private String requestMethod;

    /** 请求 URL */
    private String requestUrl;

    /** 请求参数 */
    private String requestParams;

    /** 请求结果 */
    private String requestResult;

    /** 耗时（毫秒） */
    private Long costTime;

    /** 操作 IP */
    private String ip;

    /** 浏览器 UserAgent */
    private String userAgent;

    /** 浏览器名称 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 客户端标识 */
    private String clientKey;

    /** 状态：0-失败 1-成功 */
    private Integer status;

    /** 错误信息 */
    private String errorMsg;

    /** 操作时间 */
    private LocalDateTime operateTime;
}
