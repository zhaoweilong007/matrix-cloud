package com.matrix.jpush.domain;

import lombok.Data;

@Data
public class JPushVo {

    private String msg;
    private String title;
    private String platform;
    private String alias;
}
