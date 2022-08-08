package com.matrix.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/8 15:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheKey {
    private String key;
    private Duration expire;

    public CacheKey(String key) {
        this.key = key;
    }

    public static CacheKey of(String key, Duration expire) {
        return new CacheKey(key, expire);
    }
}
