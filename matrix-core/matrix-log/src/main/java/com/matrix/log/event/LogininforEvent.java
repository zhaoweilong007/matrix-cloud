package com.matrix.log.event;

import com.matrix.common.entity.SysLoginLog;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录事件
 */

@Data
@AutoMapper(target = SysLoginLog.class)
public class LogininforEvent implements Serializable {


    /**
     * 登录登出类型 1:login、2:logout
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
     * 登录状态 0成功 1失败
     */
    private String status;

    /**
     * ip地址
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
     * 提示消息
     */
    private String msg;


}
