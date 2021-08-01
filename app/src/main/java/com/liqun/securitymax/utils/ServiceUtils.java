package com.liqun.securitymax.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

public class ServiceUtils {
    /**
     * 判断服务是否正在运行
     * @param ctx 上下文环境
     * @param serviceName 服务名称
     * @return 服务是否正在运行(true:是, false:否)
     */
    public static boolean isRunning(Context ctx, String serviceName){
        // 01.获取activityManager管理者对象, 可以去获取当前手机正在运行的所有服务
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        // 02.获取手机中正在运行的服务集合
        List<RunningServiceInfo> runningServices = am.getRunningServices(1000);
        // 03.遍历获取所有的服务, 拿到每一个服务的类名并和传递进来的类名做比对, 如果一致则说明服务正在运行
        for (RunningServiceInfo runningServiceInfo : runningServices) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
