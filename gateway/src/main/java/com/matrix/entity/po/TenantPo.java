package com.matrix.entity.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:15
 **/
@Data
public class TenantPo<T extends TenantPo<?>> extends Model<T> implements Serializable {
    private Long tenantId;
}
