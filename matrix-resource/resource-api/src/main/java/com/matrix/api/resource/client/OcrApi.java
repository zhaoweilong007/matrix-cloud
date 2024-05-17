package com.matrix.api.resource.client;

import com.matrix.api.resource.vo.BizLicenseOCRVo;
import com.matrix.api.resource.vo.IDCardOCRVo;
import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@FeignClient(contextId = "OcrApi", value = ServerNameConstants.RESOURCE, path = OcrApi.PREFIX)
public interface OcrApi {

    String PREFIX = "/ocr";


    /**
     * 身份证识别(自动识别正反面)
     *
     * @param imgUrl 图片地址
     * @return {@link R}<{@link IDCardOCRVo}>
     */
    @Parameter(name = "imgUrl", description = "图片地址", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "身份证识别(自动识别正反面)", description = "身份证识别(自动识别正反面)")
    @GetMapping("/idCaredOCR")
    R<IDCardOCRVo> idCaredOCR(@RequestParam("imgUrl") String imgUrl);


    /**
     * 营业执照识别
     *
     * @param imgUrl 图片地址
     * @return {@link R}<{@link IDCardOCRVo}>
     */
    @Parameter(name = "imgUrl", description = "图片地址", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "营业执照识别", description = "营业执照识别")
    @GetMapping("/bizLicenseOCR")
    R<BizLicenseOCRVo> bizLicenseOCR(@RequestParam("imgUrl") String imgUrl);


}
