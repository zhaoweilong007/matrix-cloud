package com.matrix.common.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;


/**
 * 五星服务通知模板
 *
 * @author LeonZhou
 */
@Getter
@AllArgsConstructor
public enum PushMessageEnum {


    ;

    private final String title;
    private final String template;
    @Setter
    private AppEnum appEnum;

    public static PushMessageEnum ofValue(String name) {
        return Arrays.stream(values()).filter(pushMessageEnum -> pushMessageEnum.name().equals(name)).findFirst()
                .orElse(null);
    }

}
