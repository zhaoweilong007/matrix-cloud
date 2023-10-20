package com.matrix.common.constant;

import java.math.BigDecimal;

public class HouseConstants {

    public static final Integer TYPE_S = 2;//区分二手房源详情不同来源
    public static final Integer TYPE_PC = 1;//区分二手房源详情不同来源
    public static final Integer TYPE_MINI = 0;//区分二手房源详情不同来源
    public static final BigDecimal TOTAL_COLUMN_COUNT = new BigDecimal(27);//计算完整度用，该字段表示房源信息一共多少个字段,其中base 7 key 3 maintain 8 delegate 8 surveyFile 1

    public static final Integer MAX_CHECK_TIMES = 50;//查看位置 每日最大查看次数
    public static final Integer MAX_CALL_TIMES = 15;//每日可拨打最大次数

    public static final String ORDERCOMMON = "SN";//订单号常量 老系统订单

    public static final String SZ_CITY_CODE = "440300000000";//深圳市城市代码
}
