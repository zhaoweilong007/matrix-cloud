package com.matrix.translation.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.model.SysRegionDict;
import com.matrix.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 行政区域服务
 */
@FeignClient(contextId = "IAreaNameService", value = ServerNameConstants.SYSTEM)
public interface IAreaNameService {

    /**
     * 通过地区编码查询地区名称
     *
     * @param areaCode 地区编码
     * @return 结果
     */
    @GetMapping("/sys/region/selectRegionNameByCode")
    R<String> selectRegionNameByCode(@RequestParam("areaCode") String areaCode);

    @GetMapping("/sys/region/selectRegionNameByCodes")
    R<List<SysRegionDict>> selectRegionNameByCodes(@RequestBody List<String> areaCodes);
}
