package com.matrix.resource.controller;

import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.util.MapUtils;
import com.matrix.api.resource.client.IpRegionApi;
import com.matrix.api.resource.vo.IpInfoVo;
import com.matrix.common.model.SysRegionDict;
import com.matrix.common.result.R;
import com.matrix.common.util.servlet.ServletUtils;
import com.matrix.resource.convert.ResourceConvert;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/8
 **/
@RestController
@RequestMapping(IpRegionApi.PREFIX)
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "ip区域服务", description = "ip区域服务")
public class IpRegionController implements IpRegionApi {

    private final Ip2regionSearcher regionSearcher;
    private final BeanSearcher beanSearcher;


    @Override
    public R<IpInfoVo> ipInfo(Long ip) {
        final IpInfo ipInfo = regionSearcher.memorySearch(ip);
        final IpInfoVo ipInfoVo = ResourceConvert.INSTANCE.convert(ipInfo);
        setCityCode(ipInfoVo);
        return R.success(ipInfoVo);
    }

    @Override
    public R<IpInfoVo> ipInfo() {
        final String clientIp = ServletUtils.getClientIP();
        final IpInfo ipInfo = regionSearcher.memorySearch(clientIp);
        final IpInfoVo ipInfoVo = ResourceConvert.INSTANCE.convert(ipInfo);
        setCityCode(ipInfoVo);
        return R.success(ipInfoVo);
    }


    private void setCityCode(IpInfoVo infoVo) {
        if (infoVo == null) {
            return;
        }
        if (StrUtil.isBlank(infoVo.getCity())) {
            return;
        }

        final SysRegionDict regionDict = beanSearcher.searchFirst(SysRegionDict.class,
                MapUtils.builder()
                        .field(SysRegionDict::getAreaName, infoVo.getCity())
                        .build()
        );

        if (regionDict != null) {
            infoVo.setCityCode(regionDict.getAreaCode());
        }

    }
}
