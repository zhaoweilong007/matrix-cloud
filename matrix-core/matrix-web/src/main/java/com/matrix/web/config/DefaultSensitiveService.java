package com.matrix.web.config;

import com.matrix.common.service.ISensitiveService;

/**
 * 默认敏感词服务实现，始终返回 true（全部敏感）
 */
public class DefaultSensitiveService implements ISensitiveService {

    @Override
    public boolean isSensitive() {
        return true;
    }
}
