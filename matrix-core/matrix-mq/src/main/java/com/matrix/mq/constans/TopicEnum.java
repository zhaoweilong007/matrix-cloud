package com.matrix.mq.constans;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ZhaoWeiLong
 * @since 2023/4/18
 **/
@RequiredArgsConstructor
@Getter
public enum TopicEnum {


    FANGDX_ORDINARY("fangdx_ordinary"),
    ;

    private final String name;


}
