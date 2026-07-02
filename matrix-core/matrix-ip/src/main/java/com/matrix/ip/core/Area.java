package com.matrix.ip.core;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 行政区域节点，支持树形结构。
 *
 */
@Data
public class Area {

    /** 全球根节点 ID */
    public static final Integer ID_GLOBAL = 0;
    /** 中国节点 ID */
    public static final Integer ID_CHINA = 1;

    /** 区域编号 */
    private Integer id;
    /** 区域名称 */
    private String name;
    /** 区域类型 */
    private Integer type;
    /** 父节点 */
    private Area parent;
    /** 子节点列表 */
    private List<Area> children = new ArrayList<>();
}
