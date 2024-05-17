package com.matrix.api.resource.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@Data
@Schema(description = "身份证vo")
public class IDCardOCRVo {

    @Schema(description = "姓名（人像面）")
    @JSONField(alternateNames = {"Name"})
    private String name;

    @Schema(description = "性别（人像面）")
    @JSONField(alternateNames = {"Sex"})
    private String sex;

    @Schema(description = "民族（人像面）")
    @JSONField(alternateNames = {"Nation"})
    private String nation;

    @Schema(description = "出生日期（人像面）", example = "1987/1/1")
    @JSONField(alternateNames = {"Birth"})
    private String birth;

    @Schema(description = "地址（人像面）")
    @JSONField(alternateNames = {"Address"})
    private String address;

    @Schema(description = "身份证号（人像面）")
    @JSONField(alternateNames = {"IdNum"})
    private String idNum;

    @Schema(description = "发证机关（国徽面）")
    @JSONField(alternateNames = {"Authority"})
    private String authority;

    @Schema(description = "证件有效期（国徽面）")
    @JSONField(alternateNames = {"ValidDate"})
    private String validDate;

    @Schema(description = "唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId")
    @JSONField(alternateNames = {"RequestId"})
    private String requestId;
}
