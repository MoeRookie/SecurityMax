package com.liqun.securitymax.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "重启手机成功了,并且监听到了相应的广播......");
        // 1.获取到开机后手机sim卡的序列号
        // 6.1. 获取sim卡序列号(TelephonyManager)
        TelephonyManager manager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 6.2. 获取sim卡序列号
        String simSerialNumber = manager.getSimSerialNumber();
        // 2.sp中存储的sim卡序列号
        String spSimSerialNumber =
                SpUtils.getString(context, ConstantValue.SIM_SERIAL_NUM, "");
        // 3.比对不一致
        if (!spSimSerialNumber.equals(simSerialNumber)) {
            // 4.获取到紧急联系人电话号码
            String contactPNum = SpUtils.getString(context, ConstantValue.CONTACT_PHONE, "");
            // 5.发送短信给紧急联系人
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contactPNum,null, "Sim Change !",
                    null, null);
        }
    }
}
