package com.liqun.securitymax.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
    /**
     * 输入流 -> 字符串
     * @param is 输入流对象
     * @return 流转换成的字符串, 返回null代表异常
     */
    public static String stream2String(InputStream is) {
        // 1.在读取的过程中,将读取的内容存储到缓存中,然后一次性的转换成字符串返回
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 2.读流操作(读到没有为止[循环])
        byte[] buffer = new byte[1024];
        // 3.记录读取内容长度的临时变量
        int temp = -1;
        try {
            while ((temp = is.read(buffer)) != -1) {
                baos.write(buffer,0,temp);
            }
            // 返回读取到的数据
            return baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                baos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
