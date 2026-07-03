package com.matrix.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.api.system.entity.po.SysDictData;
import java.util.List;

/**
 * SysDictData 服务接口。
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 按字典类型查询数据列表。
     *
     * @param dictType 字典类型编码
     * @return 字典数据列表
     */
    List<SysDictData> listByType(String dictType);
}
