package com.liqun.securitymax.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private static SharedPreferences sp;
    // 写

    /**
     * 写入boolean变量至sp中
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点的值
     */
    public static void putBoolean(Context ctx,String key,boolean value){
        // 存储节点文件的名称,读写方式
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }
    // 读

    /**
     * 读取boolean标识从sp中
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者从此节点读取到的结果
     */
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        // 存储节点文件的名称,读写方式
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
}