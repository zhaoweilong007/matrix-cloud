package com.matrix.common.enums;

import com.matrix.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对一套 用户体系
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    /**
     * pc端
     */
    PC(1, "pc"),

    /**
     * app端
     */
    APP(2, "app"),

    /**
     * 微信小程序端
     */
    WX(3, "wx"),

    /**
     * 抖音小程序
     */
    TIKTOK(4, "tiktok");

    private final Integer value;


    private final String device;

    public static String getUserType(DeviceTypeEnum deviceTypeEnum) {
        switch (deviceTypeEnum) {
            case PC -> {
                return UserTypeEnum.SYS_USER.getUserType();
            }
            case WX -> {
                return UserTypeEnum.WX_USER.getUserType();
            }
            case APP -> {
                return UserTypeEnum.APP_USER.getUserType();
            }
        }
        return "unKnow";
    }

    public static UserTypeEnum getUserTypeEnum(DeviceTypeEnum deviceTypeEnum) {
        switch (deviceTypeEnum) {
            case PC -> {
                return UserTypeEnum.SYS_USER;
            }
            case WX -> {
                return UserTypeEnum.WX_USER;
            }
            case APP -> {
                return UserTypeEnum.APP_USER;
            }
        }
        throw new ServiceException(BusinessErrorTypeEnum.TYPE_NOTE_SUPPORT);
    }

    public String getUserType() {
        switch (this) {
            case PC -> {
                return UserTypeEnum.SYS_USER.getUserType();
            }
            case WX -> {
                return UserTypeEnum.WX_USER.getUserType();
            }
            case APP -> {
                return UserTypeEnum.APP_USER.getUserType();
            }
        }
        return "unKnow";
    }
}
