package com.matrix.api.resource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/8
 **/
@Schema(description = "ip信息vo")
@Data
public class IpInfoVo implements Serializable {

    /**
     * 国家
     */
    @Schema(description = "国家")
    private String country;
    /**
     * 区域
     */
    @Schema(description = "区域")
    private String region;
    /**
     * 省
     */
    @Schema(description = "省")
    private String province;
    /**
     * 城市
     */
    @Schema(description = "城市")
    private String city;

    /**
     * 运营商
     */
    @Schema(description = "运营商")
    private String isp;


    /**
     * 城市code
     */
    @Schema(description = "城市code")
    private String cityCode;


    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddress() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(region);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.removeIf(Objects::isNull);
        return StringUtil.join(regionSet, StringPool.EMPTY);
    }

    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddressAndIsp() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(region);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.add(isp);
        regionSet.removeIf(Objects::isNull);
        return StringUtil.join(regionSet, StringPool.SPACE);
    }
}
