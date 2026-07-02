package com.matrix.ip.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import com.matrix.ip.core.Area;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP 工具类，基于 ip2region 本地 xdb 数据库进行 IP 地理定位。
 *
 * <p>使用方式：将 ip2region.xdb 文件放入 classpath 根目录，
 * 初始化时自动加载到内存。</p>
 *
 */
public class IPUtils {

    private static final Logger log = LoggerFactory.getLogger(IPUtils.class);

    /** ip2region 数据文件路径 */
    private static final String IP2REGION_DB_PATH = "ip2region.xdb";

    /** 未知地区 */
    private static final String UNKNOWN = "未知";

    private static volatile Searcher searcher;
    private static volatile boolean initialized = false;

    static {
        try {
            init();
        } catch (Exception e) {
            log.warn("Failed to initialize ip2region: {}. IP location will return unknown.", e.getMessage());
        }
    }

    /**
     * 初始化 ip2region 搜索引擎。
     */
    private static synchronized void init() {
        if (initialized) {
            return;
        }
        try {
            byte[] bytes = ResourceUtil.readBytes(IP2REGION_DB_PATH);
            searcher = Searcher.newWithBuffer(bytes);
            initialized = true;
            log.info("ip2region initialized successfully with {} bytes", bytes.length);
        } catch (Exception e) {
            log.warn("ip2region.xdb not found in classpath, IP location disabled: {}", e.getMessage());
        }
    }

    /**
     * 检查搜索引擎是否可用。
     */
    public static boolean isAvailable() {
        return initialized && searcher != null;
    }

    /**
     * 根据 IP 字符串查询区域编号。
     *
     * @param ip IP 地址
     * @return 区域编号，查询失败返回 null
     */
    public static Integer getAreaId(String ip) {
        if (!isAvailable() || ip == null || ip.isBlank()) {
            return null;
        }
        try {
            String region = searcher.search(ip.trim());
            if (region != null && !region.isEmpty() && !UNKNOWN.equals(region)) {
                // ip2region 返回格式: "国家|区域|省份|城市|ISP"
                // 尝试解析为数字 ID
                return parseRegionToAreaId(region);
            }
        } catch (Exception e) {
            log.debug("Failed to search IP: {}", ip, e);
        }
        return null;
    }

    /**
     * 根据 IP 字符串查询区域信息。
     *
     * @param ip IP 地址
     * @return 区域字符串，格式如 "中国|0|北京|北京市|电信"
     */
    public static String getRegion(String ip) {
        if (!isAvailable() || ip == null || ip.isBlank()) {
            return UNKNOWN;
        }
        try {
            String region = searcher.search(ip.trim());
            return region != null ? region : UNKNOWN;
        } catch (Exception e) {
            log.debug("Failed to search IP region: {}", ip, e);
            return UNKNOWN;
        }
    }

    /**
     * 根据 IP 字符串查询完整的 Area 对象。
     *
     * @param ip IP 地址
     * @return Area 对象，查询失败返回 null
     */
    public static Area getArea(String ip) {
        Integer areaId = getAreaId(ip);
        if (areaId == null) {
            return null;
        }
        return AreaUtils.getArea(areaId);
    }

    /**
     * 判断是否为内网 IP。
     *
     * @param ip IP 地址
     * @return 是否为内网 IP
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.isBlank()) {
            return false;
        }
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.isSiteLocalAddress()
                    || addr.isLoopbackAddress()
                    || addr.isLinkLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 尝试将 ip2region 返回的区域字符串解析为 area.csv 中的区域 ID。
     * 这里返回的是一个简化结果，实际映射关系需要根据 area.csv 中的数据匹配。
     *
     * @param region ip2region 区域字符串
     * @return 区域 ID，匹配失败返回 null
     */
    private static Integer parseRegionToAreaId(String region) {
        // ip2region 默认返回格式: 国家|区域|省份|城市|ISP
        String[] parts = region.split("\\|");
        if (parts.length >= 3) {
            String province = parts[2].trim();
            // 尝试在 area.csv 中匹配省份名
            for (Area area : AreaUtils.getRootArea().getChildren()) {
                if (area.getName().equals(province)
                        || province.startsWith(area.getName())
                        || area.getName().startsWith(province)) {
                    return area.getId();
                }
            }
        }
        return null;
    }
}
