 package com.liqun.securitymax.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AddressService extends Service {
    private static final String TAG = AddressService.class.getSimpleName();
    private TelephonyManager mTm;
    private MyPhoneStateListener mPhoneStateListener;
    @Override
    public void onCreate() {
        // 第一次开启服务以后, 就需要去管理吐司的显示
        // 电话状态的监听(服务开启的时候, 需要去做监听, 关闭的时候电话状态就不需要监听)
        // 01. 电话管理者对象
        mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        // 02. 监听电话状态
        mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    class MyPhoneStateListener extends PhoneStateListener{
        // 03. 手动重写, 电话状态发生改变会触发的方法
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                // 空闲状态, 没有任何活动
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "挂断电话,空闲了.......................");
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
        Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
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
