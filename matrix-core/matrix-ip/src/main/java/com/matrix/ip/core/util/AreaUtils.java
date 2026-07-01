package com.matrix.ip.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.ip.core.Area;
import com.matrix.ip.core.enums.AreaTypeEnum;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 行政区划工具类，基于 classpath 下的 area.csv 文件。
 *
 * <p>CSV 格式：id,name,type,parentId</p>
 * <p>支持四级区域：国家 → 省份 → 城市 → 区县</p>
 *
 * @author matrix
 */
public class AreaUtils {

    private static final Logger log = LoggerFactory.getLogger(AreaUtils.class);

    /** CSV 资源路径 */
    private static final String AREA_CSV_PATH = "area.csv";

    /** 区域分隔符 */
    private static final String SEPARATOR = "/";

    /** 区域缓存：id → Area */
    private static final Map<Integer, Area> AREA_MAP = new LinkedHashMap<>();

    /** 全球根节点 */
    @Getter
    private static Area rootArea;

    static {
        init();
    }

    /**
     * 初始化：加载 area.csv 构建区域树。
     */
    private static void init() {
        try {
            String csvContent = ResourceUtil.readUtf8Str(AREA_CSV_PATH);
            CsvReader reader = CsvUtil.getReader();
            cn.hutool.core.text.csv.CsvData csvData = reader.read(new StringReader(csvContent));
            List<cn.hutool.core.text.csv.CsvRow> rows = csvData.getRows();

            if (CollUtil.isEmpty(rows)) {
                log.warn("area.csv is empty, area functionality disabled");
                return;
            }

            // 创建虚拟根节点
            rootArea = new Area();
            rootArea.setId(Area.ID_GLOBAL);
            rootArea.setName("全球");
            rootArea.setType(AreaTypeEnum.COUNTRY.getType());
            AREA_MAP.put(Area.ID_GLOBAL, rootArea);

            // 第一遍：解析所有节点
            for (cn.hutool.core.text.csv.CsvRow row : rows) {
                if (row.getOriginalLineNumber() == 1) {
                    continue; // 跳过 header
                }
                List<String> cells = row.getRawList();
                if (cells.size() < 4) {
                    continue;
                }
                Area area = new Area();
                area.setId(Integer.parseInt(cells.get(0).trim()));
                area.setName(cells.get(1).trim());
                area.setType(Integer.parseInt(cells.get(2).trim()));
                AREA_MAP.put(area.getId(), area);
            }

            // 第二遍：建立父子关系
            for (cn.hutool.core.text.csv.CsvRow row : rows) {
                if (row.getOriginalLineNumber() == 1) {
                    continue;
                }
                List<String> cells = row.getRawList();
                if (cells.size() < 4) {
                    continue;
                }
                Integer id = Integer.parseInt(cells.get(0).trim());
                Integer parentId = Integer.parseInt(cells.get(3).trim());
                Area area = AREA_MAP.get(id);
                Area parent = AREA_MAP.getOrDefault(parentId, rootArea);
                if (area != null && parent != null) {
                    area.setParent(parent);
                    parent.getChildren().add(area);
                }
            }

            log.info("Loaded {} areas from area.csv", AREA_MAP.size());
        } catch (Exception e) {
            log.error("Failed to load area.csv", e);
        }
    }

    /**
     * 根据 ID 获取区域。
     *
     * @param id 区域编号
     * @return 区域对象，不存在返回 null
     */
    public static Area getArea(Integer id) {
        return AREA_MAP.get(id);
    }

    /**
     * 格式化区域完整名称。
     * 例如：110105 → "北京市 北京市 朝阳区"
     *
     * @param id 区域编号
     * @return 完整名称路径
     */
    public static String format(Integer id) {
        return format(id, " ");
    }

    /**
     * 格式化区域完整名称。
     *
     * @param id        区域编号
     * @param separator 分隔符
     * @return 完整名称路径
     */
    public static String format(Integer id, String separator) {
        Area area = getArea(id);
        if (area == null) {
            return "";
        }
        List<String> names = new ArrayList<>();
        Area current = area;
        // 跳过中国节点（ID=1），从省份开始
        while (current != null && !Area.ID_GLOBAL.equals(current.getId())) {
            if (!Area.ID_CHINA.equals(current.getId())) {
                names.add(current.getName());
            }
            current = current.getParent();
        }
        Collections.reverse(names);
        return String.join(separator, names);
    }

    /**
     * 按路径名称字符串解析区域。
     * 例如："北京市/北京市/朝阳区"
     *
     * @param pathStr 路径字符串
     * @return 匹配的区域，未匹配返回 null
     */
    public static Area parseArea(String pathStr) {
        if (StrUtil.isBlank(pathStr)) {
            return null;
        }
        List<String> parts = StrUtil.split(pathStr, SEPARATOR);
        Area current = rootArea;
        for (String part : parts) {
            boolean found = false;
            if (current.getChildren() != null) {
                for (Area child : current.getChildren()) {
                    if (child.getName().equals(part.trim())) {
                        current = child;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return null;
            }
        }
        return current;
    }

    /**
     * 沿父链向上查找指定层级的父区域。
     *
     * @param id   起始区域编号
     * @param type 目标层级
     * @return 匹配的区域 ID，未匹配返回 null
     */
    public static Integer getParentIdByType(Integer id, AreaTypeEnum type) {
        Area area = getArea(id);
        if (area == null) {
            return null;
        }
        Area current = area;
        while (current != null) {
            if (current.getType().equals(type.getType())) {
                return current.getId();
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * 按类型过滤区域列表。
     *
     * @param type 区域类型
     * @param func 转换函数
     * @param <T>  返回类型
     * @return 过滤后的列表
     */
    public static <T> List<T> getByType(AreaTypeEnum type, Function<Area, T> func) {
        return AREA_MAP.values().stream()
                .filter(a -> a.getType().equals(type.getType()))
                .map(func)
                .collect(Collectors.toList());
    }
}
