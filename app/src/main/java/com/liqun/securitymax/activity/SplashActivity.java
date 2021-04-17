package com.liqun.securitymax.activity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.utils.StreamUtils;
import com.liqun.securitymax.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
    private RelativeLayout mRLRoot;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
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
                    ToastUtils.show(getApplicationContext(),"url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtils.show(getApplicationContext(),"读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtils.show(getApplicationContext(),"json解析异常");
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
                dialog.dismiss();
                // 下载apk,apk链接地址-downloadUrl
                downloadApk();
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
        // 点击取消事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 即使用户点击取消,也需要让其进入应用程序主界面
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        // apk下载链接地址,放置apk的所在路径
        // 1.判断sd卡是否可用,sd卡是否挂载上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 2.获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "SecurityMax.apk";
            // 3.发送请求,获取apk并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            // 4.发送请求,传递参数(下载地址,下载应用放置位置)
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    // 下载成功(下载过后放置在sd卡中的apk)
                    Log.e(TAG, "下载成功");
                    File file = responseInfo.result;
                    // 提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e(TAG, "下载失败");
                }

                // 刚刚开始下载的方法
                @Override
                public void onStart() {
                    super.onStart();
                    Log.e(TAG, "刚刚开始下载");
                }

                /**
                 * 下载过程中的方法
                 * @param total 下载apk的总大小
                 * @param current 当前下载位置
                 * @param isUploading 是否正在下载
                 */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.e(TAG, "下载中 . . .");
                    Log.e(TAG, "total = " + total);
                    Log.e(TAG, "current = " + current);
                }
            });
        }
    }

    /**
     * 安装对应apk
     * @param file 安装文件
     */
    private void installApk(File file) {
        // 系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        // 文件作为数据源
        // intent.setData(Uri.fromFile(file));
        // 设置安装的类型
        // intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivityForResult(intent,0);
    }

    // 开启一个activity后,返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
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
        initAnim();
        initDB();
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        initAddressDB("address.db");
    }

    /**
     * 拷贝数据库到files文件夹下
     * @param dbName 数据库名称
     */
    private void initAddressDB(String dbName) {
        // 01. 在files文件夹下创建同名dbName数据库文件
        File files = getFilesDir();
        File file = new File(files, dbName);
        if (file.exists()) {
            return;
        }
        InputStream is = null;
        FileOutputStream fos = null;
        // 02. 输入流读取第三方资产目录下的文件
        try {
            is = getAssets().open(dbName);
            // 03. 将读取的内容写入到指定文件夹的指定文件中去
            fos = new FileOutputStream(file);
            // 04. 定义每次读取内容的大小
            byte[] buffer = new byte[1024];
            int length = -1; // 保存每次读取到的内容
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fos != null && is != null) {
                try {
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加淡入动画效果
     */
    private void initAnim() {
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(3000);
        mRLRoot.setAnimation(anim);
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
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        }else{
            // 直接进入应用程序主界面
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }
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
                    URL url = new URL("http://172.20.10.4:8080/update.json");
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
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        // 日志打印
                        Log.e(TAG, versionName);
                        Log.e(TAG, mVersionDes);
                        Log.e(TAG, versionCode);
                        Log.e(TAG, mDownloadUrl);
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
        mRLRoot = findViewById(R.id.rl_root);
    }
}
