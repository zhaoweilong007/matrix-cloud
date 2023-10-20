package com.matrix.translation.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.model.SysDictData;
import com.matrix.common.model.SysDictDataVo;
import com.matrix.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典服务
 */
@FeignClient(contextId = "IRemoteDictService", value = ServerNameConstants.SYSTEM, path = "/sys/sysDict")
public interface IRemoteDictService {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @GetMapping(value = "/translate/type")
    R<List<SysDictDataVo>> selectDictDataByTypeForTranslate(@RequestParam("dictType") String dictType);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @GetMapping(value = "/type")
    R<List<SysDictDataVo>> selectDictDataByType(@RequestParam("dictType") String dictType);


    /**
     * 根据字典类型查询字典数据
     *
     * @param dictTypes dict类型
     * @return {@link R}<{@link List}<{@link SysDictData}>>
     */
    /*@PostMapping("/types")
    R<Map<String,List<SysDictDataVo>>> dictType(@RequestBody List<String> dictTypes);*/
}
