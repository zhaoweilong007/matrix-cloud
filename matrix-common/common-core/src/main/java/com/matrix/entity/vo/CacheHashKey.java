package com.matrix.entity.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/8 15:51
 **/
@Data
public class CacheHashKey extends CacheKey{

    @NonNull
    private Object field;

    public CacheHashKey(@NonNull String key, @NonNull final Object field) {
        super(key);
        this.field = field;
    }

    public CacheHashKey(@NonNull String key, @NonNull final Object field, Duration expire) {
        super(key, expire);
        this.field = field;
    }

    public CacheKey tran() {
        return new CacheKey(StrUtil.join(":", new Object[]{this.getKey(), this.getField()}), this.getExpire());
    }
}
