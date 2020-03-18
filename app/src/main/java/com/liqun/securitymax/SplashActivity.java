package com.liqun.securitymax;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView mTvVersionName;

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
