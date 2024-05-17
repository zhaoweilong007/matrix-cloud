package com.matrix.api.resource.client;

import com.matrix.api.resource.vo.IpInfoVo;
import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/8
 **/
@FeignClient(contextId = "IpRegionApi", name = ServerNameConstants.RESOURCE, path = IpRegionApi.PREFIX)
public interface IpRegionApi {

    String PREFIX = "/ip/region";


    /**
     * 根据ip获取ip信息
     *
     * @param ip ip地址
     * @return {@link R}<{@link IpInfoVo}>
     */

    @Parameter(name = "ip", description = "ip地址", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "根据ip获取ip信息", description = "根据ip获取ip信息")
    @GetMapping("/byIp")
    R<IpInfoVo> ipInfo(@NotNull @RequestParam("ip") Long ip);

    /**
     * 获取ip信息
     *
     * @return {@link R}<{@link IpInfoVo}>
     */

    @Operation(summary = "获取ip信息", description = "获取ip信息")
    @GetMapping
    R<IpInfoVo> ipInfo();


}
