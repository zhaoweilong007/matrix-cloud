package com.matrix.common.constant;

/**
 * 缓存组名称常量
 * <p>
 * key 格式为 cacheNames#ttl#maxIdleTime#maxSize
 * <p>
 * ttl 过期时间 如果设置为0则不过期 默认为0
 * maxIdleTime 最大空闲时间 根据LRU算法清理空闲数据 如果设置为0则不检测 默认为0
 * maxSize 组最大长度 根据LRU算法清理溢出数据 如果设置为0则无限长 默认为0
 * <p>
 * 例子: test#60s、test#0#60s、test#0#1m#1000、test#1h#0#500
 */
public interface CacheNames {


    /**
     * 系统配置
     */
    String SYS_CONFIG = "sys_config";

    /**
     * 数据字典
     */
    String SYS_DICT = "sys_dict";

    /**
     * OSS内容
     */
    String SYS_OSS = "sys_oss#30d";

    /**
     * OSS配置
     */
    String SYS_OSS_CONFIG = "sys_oss_config";

    /**
     * 小区名称
     */
    String COM_NAME = "coummunity_name";

    /**
     * 房间位置
     */
    String ROOM_NO = "community_room_no";

    /**
     * 小区信息
     */
    String COM_BASE = "coummunity_base";

    /**
     * 楼盘选项字典
     */
    String REM_OPTION_DIST = "rem_option_dist";

    /**
     * 消息模板
     */
    String MESSAGE_TEMPLATE = "message_template";
    /**
     * 区域类型
     */
    String SYS_REGION_TYPE = "sys_region_type:%s:";

    /**
     * 区域code
     */
    String SYS_REGION_CODE = "sys_region_code";

    String SYS_REGION_SEARCH = "sys_region_search:";

    String SYS_REGION_GROUP = "sys_region_group";

    /**
     * 地铁
     */
    String SUBWAY_CITY_CODE = "ums_subway_base";

    /**
     * 地铁
     */
    String OMS_SYSTEM_LABEL = "oms_system_label";


    /**
     * 老版用户id
     */
    String FANGDX_USER_ID = "fangdx_user_id:";


    String matrix_HOUSE_SHARE = "matrix_house_share:%s";


    String matrix_HOUSE_ACTION = "matrix_house_action";


    String matrix_HOUSE_ROLE = "matrix_house_role";

    String REGION_RANK_SORT = "region_rank_sort:%s";

    String REGION_RANK_SORT_DEFAULT = "region_rank_sort_default";

    String REGION_RANK_COUNT = "region_rank_count";

    String COMMUNITY_BASE_INFO = "community_base_info:";

}
