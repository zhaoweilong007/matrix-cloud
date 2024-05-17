package com.matrix.api.resource.sms;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.matrix.common.model.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户短信验证码发送场景的枚举
 */
@Getter
@AllArgsConstructor
public enum SmsSceneEnum implements IntArrayValuable {

    ;


    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SmsSceneEnum::getScene).toArray();

    /**
     * 短信签名
     */
    private final SmsSignNameEnum signNameEnum;

    /**
     * 验证场景的编号
     */
    @JsonValue
    private final Integer scene;

    /**
     * 模板id
     */
    private final String templateId;

    /**
     * 描述
     */
    private final String description;

    @JsonCreator
    public static SmsSceneEnum getCodeByScene(Integer scene) {
        if (scene == null) {
            return null;
        }
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }

    public String getSmsCodeKey(String mobile) {
        return this.name().toLowerCase() + ":" + mobile;
    }

    @Override
    public int[] array() {
        return Arrays.stream(values()).mapToInt(SmsSceneEnum::getScene).toArray();
    }
}
