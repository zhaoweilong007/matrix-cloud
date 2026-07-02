package com.matrix.datapermission.enums;

import com.matrix.common.util.string.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据权限
 *
 **/
@Getter
@RequiredArgsConstructor
public enum DataScopeType {

    /**
     * 全部数据权限
     */
    ALL("1", "", ""),

    /**
     * 门店及门店以下数据权限
     */
    SHOP("2", "shop_id", " #{#tableName}.#{#column} = #{#user.shopId ?: '0'} "),

    /**
     * 本分店下所有数据权限
     */
    BRANCH_SHOP("3", "branch_id", " #{#tableName}.#{#column} = #{#user.branchId ?: '0'} "),

    /**
     * 仅本人数据权限
     */
    SELF("4", "created_by", " #{#tableName}.#{#column} = #{#user.userId ?: '0'} ");

    private final String code;

    /**
     * 字段名
     */
    private final String column;

    /**
     * 语法 采用 spel 模板表达式
     */
    private final String sqlTemplate;

    /**
     * 根据编码查找数据权限类型
     *
     * @param code 编码
     * @return 数据权限类型，未找到返回 null
     */
    public static DataScopeType findCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (DataScopeType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
