package com.matrix.common.constant;

/**
 * 用户常量信息
 */
public interface UserConstants {

    /**
     * 是否为系统默认（是）
     */
    String YES = "Y";

    /**
     * 菜单正常状态
     */
    Integer MENU_NORMAL = 1;


    /**
     * 菜单类型（菜单）
     */
    String TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    String TYPE_BUTTON = "F";

    /**
     * Layout组件标识
     */
    String LAYOUT = "Layout";

    /**
     * 字典正常状态
     */
    Integer DICT_NORMAL = 1;

    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 2;

    int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 5;

    int PASSWORD_MAX_LENGTH = 20;

    /**
     * 管理员ID
     */
    Long ADMIN_ID = 1L;

    /**
     * 管理员角色key
     */
    String ADMIN_ROLE_KEY = "admin";

    /**
     * 默认头像
     */
    String DEFAULT_AVATAR_M = "mini-programs-icon/xftSecondHandHouses/common/matrix_author_m_img.png";

    String DEFAULT_AVATAR_F = "mini-programs-icon/xftSecondHandHouses/common/matrix_author_w_img.png";

    /**
     * 默认租户id
     */
    Long DEFAULT_TENANT = 0L;
}
