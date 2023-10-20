package com.matrix.translation.core.impl;


import com.matrix.common.result.R;
import com.matrix.common.util.VUtils;
import com.matrix.common.vo.UserInfoVo;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.translation.annotation.Translation;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.api.client.IUserNameService;
import com.matrix.translation.constant.TransConstant;
import com.matrix.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户详细翻译实现  用户名称+机构名称+门店名称
 *
 * @author ZhaoWeiLong
 * @since 2023年7月31日
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.USER_ID_TO_INFO)
@Slf4j
@Component
public class UserInfoTranslationImpl implements TranslationInterface<Object> {


    public static final String USER_INFO_KEY = "matrix_user_info:%s#7d";
    private final IUserNameService userNameService;

    @Override
    public Object translation(Object key, Translation translation) {
        if (Objects.isNull(key)) {
            return null;
        }
        if (!Long.class.isAssignableFrom(key.getClass())) {
            log.warn("字段类型不支持转换：{}", key.getClass());
            return null;
        }
        final String userKey = String.format(USER_INFO_KEY, key);
        UserInfoVo userInfoVo = RedisUtils.getCacheObject(userKey);
        if (userInfoVo != null) {
            return userInfoVo;
        }
        final R<UserInfoVo> res = userNameService.selectUserInfoById((Long) key);
        if (VUtils.checkRes(res)) {
            userInfoVo = res.getData();
            RedisUtils.setCacheObject(userKey, userInfoVo);
        }
        return userInfoVo;
    }
}
