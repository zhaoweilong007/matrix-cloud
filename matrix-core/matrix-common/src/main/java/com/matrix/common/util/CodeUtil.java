package com.matrix.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * <p>
 * 编码工具类
 * 存在编码重复问题，待完善
 * </p>
 *
 * @author LeonZhou
 * @since 2023-07-07
 */
public class CodeUtil {


    public static String genOrderNo() {
        return "JY" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%04d", new Random().nextInt(999999999));
    }

    public static String genPrivateCustomerCode() {
        return "ZL" + genCustomerCode();
    }


    public static String genAgencyCustomerCode() {
        return "JG" + genCustomerCode();
    }


    public static String genHouseCode(String prefix) {
        return prefix + String.format("%04d", new Random().nextInt(99999));//5位随机码;
    }


    public static String genPlatformCustomerCode() {
        return "PT" + genCustomerCode();
    }

    //待确认 客户编码
    public static String genCustomerCode() {
        return String.format("%04d", new Random().nextInt(9999999));//年月日+7位随机码
    }

    //待确认 门店编码
    public static String genShopCode() {
        return "M" + String.format("%04d", new Random().nextInt(99999999));//M+8位随机码
    }

    //待确认 用户编码
    public static String genUserCode() {
        return String.format("%04d", new Random().nextInt(99999999));//8位随机码
    }
}
