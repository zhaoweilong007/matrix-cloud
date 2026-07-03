package com.matrix.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.matrix.api.system.entity.po.SysPost;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.system.mapper.SysPostMapper;
import com.matrix.system.service.SysPostService;
import org.springframework.stereotype.Service;

/**
 * SysPost 服务实现。
 *
 * <p>保存/更新时校验岗位编码唯一性。</p>
 */
@Service
public class SysPostServiceImpl extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Override
    public boolean save(SysPost entity) {
        checkCodeUnique(entity.getCode(), null);
        return super.save(entity);
    }

    @Override
    public boolean updateById(SysPost entity) {
        checkCodeUnique(entity.getCode(), entity.getId());
        return super.updateById(entity);
    }

    /**
     * 校验岗位编码唯一性。
     *
     * @param code       岗位编码
     * @param excludeId  排除的 ID（更新时排除自身）
     */
    private void checkCodeUnique(String code, Long excludeId) {
        if (code == null) {
            return;
        }
        long count = this.count(Wrappers.<SysPost>lambdaQuery()
                .eq(SysPost::getCode, code)
                .ne(excludeId != null, SysPost::getId, excludeId));
        if (count > 0) {
            throw new ServiceException(BusinessErrorTypeEnum.RECORD_EXIST);
        }
    }
}
