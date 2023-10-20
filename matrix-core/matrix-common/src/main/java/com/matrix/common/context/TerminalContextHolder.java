package com.matrix.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.matrix.common.enums.PlatformUserTypeEnum;

/**
 * 终端类型上下文
 *
 * @author ZhaoWeiLong
 * @since 2023/8/17
 **/
public class TerminalContextHolder {

    private static final ThreadLocal<PlatformUserTypeEnum> USERTYPE = new TransmittableThreadLocal<>();


    public static void setUsertype(PlatformUserTypeEnum usertype) {
        USERTYPE.set(usertype);
    }

    public static PlatformUserTypeEnum getUserType() {
        return USERTYPE.get();
    }

    public static void clear() {
        USERTYPE.remove();
    }


}
