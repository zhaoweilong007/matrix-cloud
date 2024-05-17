package com.matrix.resource.convert;


import com.matrix.api.resource.vo.IpInfoVo;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/8
 **/
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
@SuppressWarnings("all")
public interface ResourceConvert {

    ResourceConvert INSTANCE = Mappers.getMapper(ResourceConvert.class);

    IpInfoVo convert(IpInfo info);

}
