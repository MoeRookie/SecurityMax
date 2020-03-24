package com.liqun.securitymax;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView mTvVersionName;
    private int mLocalVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initData();
    }

    /**
     * 初始化数据的方法
     */
    private void initData() {
        // 1.应用版本名称
        mTvVersionName.setText("版本名称: "+getVersionName());
        // 检测(本地版本号和服务器版本号比对)是否有更新,如果有更新,提示用户下载
        // 2.获取本地版本号
        mLocalVersionCode = getVersionCode();
        // 3.获取服务器版本号(客户端发请求,服务端给响应[json,xml])
        // http://www.ooxx.com/update.json 返回200: 请求成功,以流的方式将数据读取下来
        // json中的内容包含:
        /*
            更新版本的版本名称
            新版本的描述信息
            服务器版本号
            新版本apk下载地址
         */
    }

    /**
     * 获取版本号
     * @return 应用版本号,非0则代表获取成功
     */
    private int getVersionCode() {
        // 1.包管理者对象packageManager
        PackageManager packageManager = getPackageManager();
        // 2.从包的管理者对象中,获取指定包名的基本信息(版本名称、版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            // 3.获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称: 从项目主模块[module]的build.gradle文件中
     * @return 应用版本名称,返回null代表异常
     */
    private String getVersionName() {
        // 1.包管理者对象packageManager
        PackageManager packageManager = getPackageManager();
        // 2.从包的管理者对象中,获取指定包名的基本信息(版本名称、版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            // 3.获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 初始化UI的方法
     */
    private void initUI() {
        mTvVersionName = findViewById(R.id.tv_version_name);
    }
}
