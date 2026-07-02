package com.matrix.mq.constans;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Topic 枚举定义
 **/
@RequiredArgsConstructor
@Getter
public enum TopicEnum {
    FANGDX_ORDINARY("fangdx_ordinary"),
    ;

    private final String name;
}
