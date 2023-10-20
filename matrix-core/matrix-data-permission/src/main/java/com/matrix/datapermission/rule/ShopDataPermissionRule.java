package com.matrix.datapermission.rule;

import com.matrix.common.model.login.LoginUser;

/**
 * 门店数据规则 (只能查看本门店的数据）
 *
 * @author ZhaoWeiLong
 * @since 2023/8/25
 **/
public class ShopDataPermissionRule extends AbstractDataPermissionRule {

    public static final String SHOP_COLUMN = "shop_id";

    @Override
    public String getColumnName() {
        return SHOP_COLUMN;
    }

    @Override
    protected Long getColumnValue(LoginUser loginUser) {
        return loginUser.getShopId();
    }


}
