package com.matrix.resource.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson2.JSON;
import com.matrix.api.resource.client.OcrApi;
import com.matrix.api.resource.enums.LicenseWarnInfoEnum;
import com.matrix.api.resource.enums.OcrErrorCodeEnum;
import com.matrix.api.resource.enums.WarnInfoEnum;
import com.matrix.api.resource.model.AdvancedInfo;
import com.matrix.api.resource.vo.BizLicenseOCRVo;
import com.matrix.api.resource.vo.IDCardOCRVo;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.result.R;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@RestController
@RequestMapping(value = OcrApi.PREFIX)
@Tag(name = "OCR服务", description = "OCR服务")
@Slf4j
@Validated
public class OcrController implements OcrApi {

    @Autowired(required = false)
    private OcrClient ocrClient;

    @Override
    public R<IDCardOCRVo> idCaredOCR(String imgUrl) {
        IDCardOCRRequest req = new IDCardOCRRequest();
        req.setImageUrl(imgUrl);
        req.setEnableRecognitionRectify(true);
        req.setEnableReflectDetail(false);
        req.setConfig("""
                {
                    "CopyWarn":true,
                    "BorderCheckWarn":true,
                    "ReshootWarn":true,
                    "DetectPsWarn":true,
                    "TempIdWarn":true,
                    "InvalidDateWarn":true,
                    "MultiCardDetect":true,
                    "Quality":true
                }
                """);
        try {
            IDCardOCRResponse resp = ocrClient.IDCardOCR(req);
            final String json = resp.getAdvancedInfo();
            final AdvancedInfo advancedInfo = JSON.parseObject(json, AdvancedInfo.class);
            Assert.isTrue(advancedInfo.getQuality() >= 50,
                    () -> new ServiceException(BusinessErrorTypeEnum.OCR_QUALITY_ERROR));
            Assert.isTrue(advancedInfo.getBorderCodeValue() <= 50,
                    () -> new ServiceException(BusinessErrorTypeEnum.OCR_BORDER_CODE_ERROR));
            if (ArrayUtil.isNotEmpty(advancedInfo.getWarnInfos())) {
                final String msg = Arrays.stream(advancedInfo.getWarnInfos())
                        .map(WarnInfoEnum::getWarnInfoEnum)
                        .filter(Objects::nonNull).map(WarnInfoEnum::getMsg)
                        .collect(Collectors.joining(","));
                log.warn("OCR识别警告：{}", msg);
                throw new ServiceException(BusinessErrorTypeEnum.OCR_CALL_ERROR, msg);
            }
            final IDCardOCRVo idCardOCRVo = JSON.parseObject(AbstractModel.toJsonString(resp),
                    IDCardOCRVo.class);
            return R.success(idCardOCRVo);
        } catch (TencentCloudSDKException e) {
            log.error("OCR调用失败 {}", e.toString());
            final OcrErrorCodeEnum errorCodeEnum = OcrErrorCodeEnum.getByCode(e.getErrorCode());
            if (errorCodeEnum != null) {
                throw new ServiceException(BusinessErrorTypeEnum.OCR_CALL_ERROR, errorCodeEnum.getMsg());
            } else {
                throw new ServiceException(BusinessErrorTypeEnum.OCR_CALL_ERROR);
            }
        }
    }

    @Override
    public R<BizLicenseOCRVo> bizLicenseOCR(String imgUrl) {
        BizLicenseOCRRequest req = new BizLicenseOCRRequest();
        req.setImageUrl(imgUrl);
        req.setEnableCopyWarn(true);
        try {
            BizLicenseOCRResponse resp = ocrClient.BizLicenseOCR(req);
            if (ArrayUtil.isNotEmpty(resp.getRecognizeWarnCode())) {
                final String msg = Arrays.stream(resp.getRecognizeWarnCode())
                        .map(LicenseWarnInfoEnum::getWarnInfoEnum)
                        .filter(Objects::nonNull).map(LicenseWarnInfoEnum::getMsg)
                        .collect(Collectors.joining(","));
                log.warn("OCR识别警告：{}", msg);
                throw new ServiceException(BusinessErrorTypeEnum.OCR_CALL_ERROR, msg);
            }
            final BizLicenseOCRVo bizLicenseOCRVo = JSON.parseObject(AbstractModel.toJsonString(resp),
                    BizLicenseOCRVo.class);
            return R.success(bizLicenseOCRVo);
        } catch (TencentCloudSDKException e) {
            log.error("OCR调用失败 {}", e.toString());
            final OcrErrorCodeEnum errorCodeEnum = OcrErrorCodeEnum.getByCode(e.getErrorCode());
            if (errorCodeEnum != null) {
                throw new ServiceException(BusinessErrorTypeEnum.OCR_CALL_ERROR, errorCodeEnum.getMsg());
            } else {
                throw new ServiceException(BusinessErrorTypeEnum.OCR_CALL_ERROR);
            }
        }
    }
}
