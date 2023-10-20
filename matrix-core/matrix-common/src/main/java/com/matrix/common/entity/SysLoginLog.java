package com.matrix.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 系统访问记录
 * </p>
 *
 * @author zhaoweilong
 * @since 2023-07-01
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog extends BaseIdEntity {


    /**
     * 登录登出类型
     */
    private Integer type;

    /**
     * 登录来源
     */
    private Integer loginType;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private Date loginTime;

    /**
     * 租户id
     */
    private Long tenantId;
}
