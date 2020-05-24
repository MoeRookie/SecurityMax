package com.liqun.securitymax.activity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.StreamUtils;
import com.liqun.securitymax.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    /**
     * 更新版本的状态码
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序主界面的状态码
     */
    private static final int ENTER_HOME = 101;
    /**
     * url地址出错的状态码
     */
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;

    private TextView mTvVersionName;
    private int mLocalVersionCode;
    private String mVersionDes;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    // 弹出对话框,提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    // 进入应用程序主界面,activity的跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(getApplicationContext(),"url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(),"读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(getApplicationContext(),"json解析异常");
                    enterHome();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 弹出对话框,提示用户更新
     */
    private void showUpdateDialog() {
        // 对话框是依赖于 activity 存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置标题
        builder.setTitle("版本更新");
        // 设置描述内容
        builder.setMessage(mVersionDes);
        // 积极按钮,立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载apk,apk链接地址-downloadUrl
            }
        });
        // 消极按钮,稍后再说
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消对话框,进入主界面
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        // 在开启一个新的界面后,将导航界面关闭(导航界面只可见一次)
        finish();
    }

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
        checkVersion();
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                // 发送请求获取数据,参数则为请求json的链接地址
                // http://10.205.3.24:8080/update.json 测试阶段不是最优
                // 仅限于模拟器访问电脑的tomcat(10.0.2.2)
                try {
                    // 1.封装url地址
                    URL url = new URL("http://192.168.1.106:8080/update.json");
                    // 2.开启一个链接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 3.设置常见请求参数(请求头)
                    // 请求超时
                    conn.setConnectTimeout(2000);
                    // 读取超时
                    conn.setReadTimeout(2000);
                    // 默认就是get请求方式
                    // conn.setRequestMethod("POST");
                    // 4.获取请求成功的响应码
                    if (conn.getResponseCode() == 200) {
                        // 5.以流的形式,将数据获取下来
                        InputStream is = conn.getInputStream();
                        // 6.将流转换成字符串(工具类封装)
                        String json = StreamUtils.stream2String(is);
                        Log.e(TAG, json);
                        // 7.json解析
                        JSONObject jsonObject = new JSONObject(json);
                        // debug调试, 解决问题
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        String downloadUrl = jsonObject.getString("downloadUrl");
                        // 日志打印
                        Log.e(TAG, versionName);
                        Log.e(TAG, mVersionDes);
                        Log.e(TAG, versionCode);
                        Log.e(TAG, downloadUrl);
                        // 8.比对版本号(服务器版本号>本地版本号,提示用户更新)
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            // 提示用户更新,弹出对话框(UI),消息机制
                            msg.what = UPDATE_VERSION;
                        }else{
                            // 进入应用程序主界面
                            msg.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    // 指定睡眠时间,请求网络的时长超过5秒则不做处理
                    // 请求网络的时长不超过5秒,强制让其睡满5秒钟
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 5000) {
                        try {
                            Thread.sleep(5000-(endTime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
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
