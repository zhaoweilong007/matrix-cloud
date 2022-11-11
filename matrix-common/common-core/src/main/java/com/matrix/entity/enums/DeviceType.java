package com.matrix.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/26 17:41
 **/
@Getter
@RequiredArgsConstructor
public enum DeviceType {

    /**
     * pc端
     */
    PC("pc"),

    APP("app"),

    WX("wx");


    private final String device;


}
