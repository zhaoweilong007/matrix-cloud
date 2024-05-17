package com.matrix.api.resource.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@Data
@Schema(description = "营业执照vo")
public class BizLicenseOCRVo {

    @Schema(description = "统一社会信用代码（三合一之前为注册号）")
    @JSONField(alternateNames = {"RegNum"})
    private String regNum;

    @Schema(description = "公司名称")
    @JSONField(alternateNames = {"Name"})
    private String name;

    @Schema(description = "注册资本")
    @JSONField(alternateNames = {"Capital"})
    private String capital;

    @Schema(description = "法定代表人")
    @JSONField(alternateNames = {"Person"})
    private String person;

    @Schema(description = "地址")
    @JSONField(alternateNames = {"Address"})
    private String address;

    @Schema(description = "经营范围")
    @JSONField(alternateNames = {"Business"})
    private String business;

    @Schema(description = "主体类型")
    @JSONField(alternateNames = {"Type"})
    private String type;

    @Schema(description = "营业期限")
    @JSONField(alternateNames = {"Period"})
    private String period;

    @Schema(description = "组成形式")
    @JSONField(alternateNames = {"ComposingForm"})
    private String composingForm;

    @Schema(description = "成立日期")
    @JSONField(alternateNames = {"SetDate"})
    private String date;

    @Schema(description = "告警码")
    @JSONField(alternateNames = {"RecognizeWarnCode"})
    private Long[] recognizeWarnCode;

    @Schema(description = "告警码说明：")
    @JSONField(alternateNames = {"RecognizeWarnMsg"})
    private String[] recognizeWarnMsg;

    @Schema(description = "是否为副本。1为是，-1为不是。")
    @JSONField(alternateNames = {"IsDuplication"})
    private Long isDuplication;

    @Schema(description = "登记日期")
    @JSONField(alternateNames = {"RegistrationDate"})
    private String registrationDate;

    @Schema(description = "图片旋转角度(角度制)，文本的水平方向为0度；顺时针为正，角度范围是0-360度\n")
    @JSONField(alternateNames = {"Angle"})
    private Float angle;

    @Schema(description = "唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。\n")
    @JSONField(alternateNames = {"RequestId"})
    private String requestId;

}
