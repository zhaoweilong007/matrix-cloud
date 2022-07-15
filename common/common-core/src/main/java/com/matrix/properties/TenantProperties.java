package com.matrix.properties;

import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:34
 **/
@Data
public class TenantProperties {

    public static final String TENANT_KEY = "tenant_id";

    private Boolean enable;

    private List<String> ignoreTables;

}
