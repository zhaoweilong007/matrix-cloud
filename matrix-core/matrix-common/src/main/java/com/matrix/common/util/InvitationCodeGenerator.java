package com.matrix.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/1
 **/
public class InvitationCodeGenerator {
    public static String generateUniqueCode(long userId) {
        // 将用户ID转换为字节数组
        byte[] userIdBytes = String.valueOf(userId).getBytes(StandardCharsets.UTF_8);

        // 使用SHA-256哈希算法对用户ID进行哈希计算
        byte[] hashBytes = calculateSHA256(userIdBytes);

        // 将哈希结果转换为六位邀请码
        String invitationCode = toSixDigitCode(hashBytes);

        return invitationCode;
    }

    private static byte[] calculateSHA256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toSixDigitCode(byte[] hashBytes) {
        // 使用Base36进制将哈希结果转换为六位邀请码
        String base36Code = Long.toString(Math.abs(toLong(hashBytes)), 36);

        // 取前六位作为邀请码，并保证长度为六位
        StringBuilder invitationCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = i % base36Code.length();
            invitationCode.append(base36Code.charAt(index));
        }

        return invitationCode.toString().toUpperCase();
    }

    private static long toLong(byte[] bytes) {
        long result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }

//    public static void main(String[] args) {
//        System.out.println(generateUniqueCode(1686358638708563970L));
//        System.out.println(generateUniqueCode(1686356187284729858L));
//        System.out.println(generateUniqueCode(1686305396091322370L));
//        System.out.println(generateUniqueCode(1685936241156132865L));
//        System.out.println(generateUniqueCode(1685214139311505409L));
//    }
}
