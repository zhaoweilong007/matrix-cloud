package com.matrix.common.util.ip;

import cn.hutool.core.util.StrUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;

/**
 * IP 工具类
 *
 */
@Slf4j
public class IpUtils {

    /**
     * 本地回环地址
     */
    public static final String LOCAL_IP = "127.0.0.1";

    /**
     * 局域网地址模式
     */
    private static final String[] LAN_PREFIXES = {"10.", "172.16.", "172.17.", "172.18.", "172.19.",
            "172.20.", "172.21.", "172.22.", "172.23.", "172.24.", "172.25.", "172.26.", "172.27.",
            "172.28.", "172.29.", "172.30.", "172.31.", "192.168."};

    /**
     * 判断是否为内网 IP
     */
    public static boolean isInternalIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        if (LOCAL_IP.equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return true;
        }
        for (String prefix : LAN_PREFIXES) {
            if (ip.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取主机名
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.debug("获取主机名失败: {}", e.getMessage());
            return "unknown";
        }
    }

    /**
     * 获取主机 IP
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.debug("获取主机IP失败: {}", e.getMessage());
            return "unknown";
        }
    }
}
