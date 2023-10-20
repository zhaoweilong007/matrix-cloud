package com.matrix.web.config;

import com.matrix.common.service.ISensitiveService;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/19
 **/
public class DefaultSensitiveService implements ISensitiveService {

    @Override
    public boolean isSensitive() {
        return true;
    }
}
