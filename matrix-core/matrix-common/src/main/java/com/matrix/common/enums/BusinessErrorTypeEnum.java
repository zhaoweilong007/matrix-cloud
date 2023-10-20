package com.matrix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.result.IResultCode;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/12 18:26
 **/
@Getter
@RequiredArgsConstructor
public enum BusinessErrorTypeEnum implements IResultCode {


    /**
     * 业务异常
     */
    INVITE_CODE_ERROR(2001, "邀请码无效"),
    USER_MOBILE_EXIST(2002, "用户手机号已存在"),
    AUTHENTICATION_FAILED(2003, "用户名或密码错误"),
    ROLE_CODE_DUPLICATED(2004, "角色编码重复"),
    USER_NOT_EXIST(2006, "用户不存在"),
    USER_DISABLE(2007, "用户已禁用"),
    USER_EXIST(2008, "用户已存在"),
    AUTH_MINI_USER_INFO_ERROR(2010, "获取用户信息失败"),
    RECORD_EXIST(2011, "记录已存在"),
    RECORD_NOT_EXIST(2012, "记录不存在"),
    STATE_NOT_MODIFIED(2013, "状态不可修改"),
    TYPE_NOTE_SUPPORT(2016, "type类型不支持"),
    SENSITIVE_CHECK_FAIL(2017, "敏感校验未通过"),
    NOT_CANCEL_APPROVAL(2018, "审批记录已更新无法取消"),
    NOT_DINGDINGID(2021, "暂无审批人员，无法提交"),
    CUSTOMER_NOT_EXIST(2022, "客户不存在"),
    USER_BINDING_NOT_FOUND(2034, "未查询到用户绑定信息"),
    MESSAGE_SEND_FAIL(2035, "消息通知发送失败"),
    NOTICE_NOT_FOUND(2036, "通知未查询到"),
    NOTIFY_SEND_TEMPLATE_PARAM_MISS(2037, "通知发送模块参数缺失"),
    NOTIFY_TEMPLATE_CODE_DUPLICATE(2038, "通知模板code重复"),
    NOTIFY_TEMPLATE_NOT_EXISTS(2039, "通知模板不存在"),
    NOTIFY_TEMPLATE_DISABLE(2040, "通知模板已禁用"),
    ILLEGAL_INPUT(2046, "不可全为空格或纯数字，不可连续11位数字"),
    ALREADY_SUBMIT_AUDIT(2049, "您已提交审核，请耐心等待系统审核"),
    PASSWORD_ERROR(2058, "旧密码错误"),
    HOUSE_ALREADY_DEAL(2061, "该房源已售，不可再次转成交"),
    MERCHANT_PHONE_EXIST(2070, "手机号已注册，不可重复注册"),
    AUDIT_EXIST(2062, "已存在数据，不可重复提交"),
    GET_WX_USER_INFO_ERROR(2064, "获取微信用户信息失败"),
    OPENID_NOT_BINDING(2061, "openId未查询到绑定账号"),

    SHOP_ALREADY_EXIST(2100, "门店已存在，请重新填写"),
    PARENT_SHOP_DELETE_FAILED(2101, "删除失败，门店下存在其他分店！"),
    SHOP_DELETE_FAILED(2102, "删除失败，分店下还有工作人员！"),
    USER_DELETE_FAILED(2103, "删除失败，禁用状态才可使用！"),
    SHOP_NOT_EXIST(2105, "门店不存在"),
    NOT_ALLOW_TO_CHECK(2114, "抱歉您暂无权限查看"),
    MERCHANT_NOT_AUTH(2115, "未认证加盟商，无法登录"),
    MERCHANT_DISABLE(2116, "加盟商已禁用无法登录"),
    DO_NOT_REPEAT_CREATE(2117, "请勿创建重复标签"),
    LABEL_COUNT_OVER_LIMIT(2118, "最多创建十个标签"),
    SHOP_STATUS_INVALID(2105, "该门店不存在或被禁用"),
    BRANCH_STATUS_INVALID(2105, "该分店不存在或被禁用"),


    REPEAT_SUBMIT(2999, "重复提交"),
    METHOD_NOTE_IMPL(3000, "方法未实现"),
    SUBMIT_EXIST(3001, "已提交记录，请等待审核"),
    STATE_UPDATED(3002, "状态已处理，无法操作"),
    CHANNEL_USER_NOT_EXIST(3003, "不是渠道相关人员"),
    MENU_EXIST(3004, "菜单名称已存在"),
    REFUSE_REASON_NOT_BLANK(3005, "驳回原因不可为空"),

    MENU_SELECT_ERROR(3005, "上级菜单不能选择自己"),

    MENU_HAS_CHILDREN(3006, "存在子菜单,不允许删除"),

    MENU_ASSIGN(3007, "菜单已分配,不允许删除"),

    ROLE_DELETE_ERROR(3008, "角色已分配不能删除"),

    ROLE_PERMISSION(3009, "没有权限访问角色数据！"),

    ROLE_EXIST(3010, "角色名称已存在"),

    DICT_EXIST(3011, "字典类型已存在"),

    DICT_ASSIGN(3012, "字典类型已分配，不能删除"),

    ROLE_NOT_EXIST(3013, "角色不存在"),

    MENU_BTN_EXIST(4005, "菜单权限已存在"),

    JPUSH_TOKEN_INVALID(5002, "一键登陆无效或过期"),
    MOBILE_IS_BINDING(5003, "手机号已经绑定无法重复绑定"),
    ERROR_APPLE_TOKEN_FAIL(5004, "苹果授权失败");


    public final int code;
    public final String msg;
}
