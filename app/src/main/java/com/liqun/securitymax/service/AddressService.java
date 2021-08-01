package com.liqun.securitymax.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.liqun.securitymax.R;

public class AddressService extends Service {
    private static final String TAG = AddressService.class.getSimpleName();
    private TelephonyManager mTm;
    private MyPhoneStateListener mPhoneStateListener;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private View mToastView;

    @Override
    public void onCreate() {
        // 第一次开启服务以后, 就需要去管理吐司的显示
        // 电话状态的监听(服务开启的时候, 需要去做监听, 关闭的时候电话状态就不需要监听)
        // 01. 电话管理者对象
        mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        // 02. 监听电话状态
        mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        // 获取窗体对象
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        super.onCreate();
    }

    class MyPhoneStateListener extends PhoneStateListener {
        // 03. 手动重写, 电话状态发生改变会触发的方法
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                // 空闲状态, 没有任何活动
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "挂断电话,空闲了.......................");
                    // 挂断电话的时候窗体需要移除吐司
                    if (null != mWM && null != mToastView) {
                        mWM.removeView(mToastView);
                    }
                    break;
                // 摘机状态, 至少有个电话活动(该活动是拨打或者是通话)
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                // 响铃状态
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(TAG, "响铃了.......................");
                    showToast(phoneNumber);
                    break;
            }
            super.onCallStateChanged(state, phoneNumber);
        }
    }

    private void showToast(String phoneNumber) {
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        // 在响铃的时候显示toast, 和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE; 默认能够被触摸
        // 指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT + Gravity.TOP;
        // 吐司显示效果(吐司布局文件), xml->view(吐司), 将吐司挂载到windowManager窗体上
        mToastView = View.inflate(this, R.layout.view_toast, null);
        // 在窗体上挂载一个view(权限)
        mWM.addView(mToastView, mParams);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // 取消对电话状态的监听(开启服务的时候监听电话的对象)
        if (mTm != null && mPhoneStateListener != null) {
            mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }
}
