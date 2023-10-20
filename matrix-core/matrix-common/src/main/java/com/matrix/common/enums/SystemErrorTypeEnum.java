package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.result.IResultCode;

@Getter
@RequiredArgsConstructor
public enum SystemErrorTypeEnum implements IResultCode {

    /**
     * 系统异常
     */
    FORBIDDEN(403, "没有访问权限"),
    UNAUTHORIZED(401, "认证失败，无法访问系统资源"),
    NOT_FOUND(404, "请求地址不存在"),
    TOO_MANY_REQUESTS(429, "请求太频繁，请稍后重试"),


    SYSTEM_ERROR(9999, "系统异常"),
    FEIGN_INVOKE_ERROR(9998, "内部服务异常"),
    SERVICE_NOTE_FOUND(9997, "服务未找到"),
    OPERATE_FAIL(9996, "操作失败"),

    SUCCESS(1000, "操作成功"),
    ARGUMENT_NOT_VALID(1001, "请求参数错误"),
    ERROR_SESSION(1003, "用户身份认证失败"),
    VERIFICATION_CODE_ERROR(1005, "验证码错误"),
    VERIFICATION_CODE_EXPIRE(1005, "验证码已失效"),
    REPEAT_SUBMIT(1006, "重复提交间隔时间不能小于1秒"),
    VALID_TENANT_FAIL(1007, "校验租户失败"),
    TENANT_NOT_FOUND(1008, "不存在租户编号"),
    PARAM_ERROR(1009, "参数错误"),
    ILLEGALITY_PARAM(1010, "非法参数"),
    ERROR_TOKEN_EMPTY(1011, "token为空"),
    ERROR_TOKEN_INVALID(1012, "token已失效"),
    SMS_HAS_SEND(1013, "操作频繁，请稍后重试"),
    SMS_EXPIRED(1014, "短信验证码已失效"),
    SMS_VALIDATOR_FAIL(1015, "短信验证码校验失败"),
    HEADER_DECRYPTION_FAILED(1016, "header解密失败"),
    SMS_SEND_FAIL(1017, "短信验证码发送失败"),
    TENANT_NOT_AUTH(1018, "未认证机构"),
    SHOP_NOT_FOUND(1019, "未找到门店信息"),
    SENSITIVE(1020, "内容不合法，包含敏感内容"),
    SENSITIVE_INVOKE_ERROR(1021, "敏感内容检测异常"),
    DATA_PERMISSION_ERROR(1022, "数据权限解析异常"),
    TYPE_NOT_IMPL(1023, "找不到类型对应的处理方法"),

    ERROR_CALL_AUTH(1136, "无访问权限");

    /**
     * 错误类型码
     */
    private final int code;
    /**
     * 错误类型描述信息
     */
    private final String msg;


}
