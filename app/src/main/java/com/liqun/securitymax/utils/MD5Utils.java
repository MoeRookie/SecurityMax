package com.liqun.securitymax.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    /**
     * 给指定字符串按照MD5算法去加密
     * @param psd 需要加密的明文密码
     * @return MD5加密后的32位串
     */
    public static String encode(String psd){
        try {
            psd = psd + "securitymax"; // 加盐处理
            // 1,指定加密算法类型
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 2,将需要加密的字符串转换为byte类型的数组, 然后进行随机哈希过程
            byte[] bs = digest.digest(psd.getBytes());
            // 3,循环遍历bs, 然后让其生成32位串, 固定写法
            StringBuilder sb = new StringBuilder();
            for (byte b : bs) {
                int i = b & 0xff;
                // int类型的i需要转换成16进制字符
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
