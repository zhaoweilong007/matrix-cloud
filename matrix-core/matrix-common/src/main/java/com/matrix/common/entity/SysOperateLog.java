package com.matrix.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 操作日志记录
 * </p>
 *
 * @author zhaoweilong
 * @since 2023-07-01
 */
@Schema(description = "操作日志记录")
@Data
@TableName("sys_operate_log")
public class SysOperateLog extends BaseIdEntity {

    @Schema(description = "链路id")
    private String traceId;


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 操作模块
     */
    @Schema(description = "操作模块")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @Schema(description = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    /**
     * 业务类型数组
     */
    @Schema(description = "业务类型数组")
    @TableField(exist = false)
    private Integer[] businessTypes;

    /**
     * 请求方法
     */
    @Schema(description = "请求方法")
    private String method;

    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;

    /**
     * 操作人员
     */
    @Schema(description = "操作人员")
    private String operName;


    /**
     * 操作地点
     */
    @Schema(description = "操作地点")
    private String operLocation;

    /**
     * 请求url
     */
    @Schema(description = "请求url")
    private String operUrl;

    /**
     * 操作地址
     */
    @Schema(description = "操作地址")
    private String operIp;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String operParam;

    /**
     * 返回参数
     */
    @Schema(description = "返回参数")
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;

    /**
     * 错误消息
     */
    @Schema(description = "错误消息")
    private String errorMsg;

    /**
     * 操作时间
     */
    @Schema(description = "操作时间")
    private Date operTime;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();


    @Schema(hidden = true)
    private Long tenantId;

}
