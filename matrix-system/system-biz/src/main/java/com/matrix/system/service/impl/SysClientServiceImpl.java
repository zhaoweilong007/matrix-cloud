package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.matrix.api.system.entity.po.SysClient;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.system.mapper.SysClientMapper;
import com.matrix.system.service.SysClientService;
import org.springframework.stereotype.Service;

/**
 * SysClient 服务实现。
 *
 * <p>保存/更新时校验客户端 ID 唯一性。</p>
 */
@Service
public class SysClientServiceImpl extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<SysClientMapper, SysClient> implements SysClientService {

    @Override
    public boolean save(SysClient entity) {
        checkClientIdUnique(entity.getClientId(), null);
        return super.save(entity);
    }

    @Override
    public boolean updateById(SysClient entity) {
        checkClientIdUnique(entity.getClientId(), entity.getId());
        return super.updateById(entity);
    }

    /**
     * 校验客户端 ID 唯一性。
     *
     * @param clientId   客户端 ID
     * @param excludeId  排除的 ID（更新时排除自身）
     */
    private void checkClientIdUnique(String clientId, Long excludeId) {
        if (clientId == null) {
            return;
        }
        long count = this.count(Wrappers.<SysClient>lambdaQuery()
                .eq(SysClient::getClientId, clientId)
                .ne(excludeId != null, SysClient::getId, excludeId));
        if (count > 0) {
            throw new ServiceException(BusinessErrorTypeEnum.RECORD_EXIST);
        }
    }
}
