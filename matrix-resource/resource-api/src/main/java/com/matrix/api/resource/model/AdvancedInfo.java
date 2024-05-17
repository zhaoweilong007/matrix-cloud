package com.matrix.api.resource.model;

import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @since 2023/12/21
 **/
@Data
public class AdvancedInfo {


    private Integer Quality;

    private Integer BorderCodeValue;

    private Long[] WarnInfos;

}
