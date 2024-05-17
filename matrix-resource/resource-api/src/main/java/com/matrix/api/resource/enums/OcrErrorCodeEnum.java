package com.matrix.api.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@Getter
@AllArgsConstructor
public enum OcrErrorCodeEnum {

    CARD_SIDE_ERROR("FailedOperation.CardSideError", "身份证CardSide类型错误"),
    DOWNLOAD_ERROR("FailedOperation.DownLoadError", "文件下载失败"),
    EMPTY_IMAGE_ERROR("FailedOperation.EmptyImageError", "图片内容为空"),
    ID_CARD_INFO_ILLEGAL("FailedOperation.IdCardInfoIllegal", "第二代身份证信息不合法或缺失（身份证号、姓名字段校验非法等）"),
    ID_CARD_TOO_SMALL("FailedOperation.IdCardTooSmall", "图片分辨率过小或身份证在原图中的占比过小"),
    IMAGE_BLUR("FailedOperation.ImageBlur", "图片模糊"),
    IMAGE_DECODE_FAILED("FailedOperation.ImageDecodeFailed", "图片解码失败"),
    IMAGE_NO_ID_CARD("FailedOperation.ImageNoIdCard", "图片中未检测到第二代身份证或临时身份证"),
    IMAGE_SIZE_TOO_LARGE("FailedOperation.ImageSizeTooLarge", "图片尺寸过大，请参考输入参数中关于图片大小限制的说明"),
    MULTI_CARD_ERROR("FailedOperation.MultiCardError", "图片中存在两张及以上同面卡证，请上传卡证单面图片或一正一反双面图片"),
    OCR_FAILED("FailedOperation.OcrFailed", "OCR识别失败"),
    UNKNOWN_ERROR("FailedOperation.UnKnowError", "未知错误"),
    NO_BIZLICENSE("FailedOperation.NoBizLicense", "非营业执照"),
    //    UNOPEN_ERROR("FailedOperation.UnOpenError", "服务未开通"),
//    CONFIG_FORMAT_ERROR("InvalidParameter.ConfigFormatError", "Config不是有效的JSON格式"),
//    INVALID_PARAMETER_LIMIT("InvalidParameterValue.InvalidParameterValueLimit", "参数值错误"),
    TOO_LARGE_FILE_ERROR("LimitExceeded.TooLargeFileError", "文件内容太大");
//    IN_ARREARS("ResourceUnavailable.InArrears", "帐号已欠费"),
//    RESOURCE_PACKAGE_RUN_OUT("ResourceUnavailable.ResourcePackageRunOut", "账号资源包耗尽"),
//    CHARGE_STATUS_EXCEPTION("ResourcesSoldOut.ChargeStatusException", "计费状态异常");


    private final String code;
    private final String msg;

    public static OcrErrorCodeEnum getByCode(String code) {
        return
                Arrays.stream(OcrErrorCodeEnum.values())
                        .filter(e -> e.getCode().equals(code))
                        .findFirst()
                        .orElse(null);
    }
}
