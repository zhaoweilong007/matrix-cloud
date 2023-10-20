package com.matrix.api.vo;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import com.matrix.translation.entity.SysUserTrans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 翻译基础vo
 **/
@Data
public class TransBaseVo extends BaseVo implements TransPojo {

    @Schema(description = "创建人id")
    @Trans(type = TransType.SIMPLE, target = SysUserTrans.class,
            fields = "userName", ref = "createName")
    private Long createdBy;

    @Schema(description = "创建人名称")
    private String createName;

    @Schema(description = "修改人id")
    @Trans(type = TransType.SIMPLE, target = SysUserTrans.class,
            fields = "userName", ref = "updateName")
    private Long updatedBy;

    @Schema(description = "更新人名称")
    private String updateName;

}
