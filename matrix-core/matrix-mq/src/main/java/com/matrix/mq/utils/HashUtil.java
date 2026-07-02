package com.matrix.mq.utils;

import java.util.zip.CRC32;

/**
 * Hash 工具类
 */
public class HashUtil {

    /**
     * 计算字节数组的 CRC32 校验值
     */
    public static long crc32Code(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
}
