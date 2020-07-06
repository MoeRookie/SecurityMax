package com.liqun.securitymax.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 1.判断是否开启了防盗保护
        boolean openSecurity =
                SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        if (openSecurity) {
            // 2.获取短信内容
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            // 3.循环遍历短信数组
            for (Object o : pdus) {
                // 4.获取短信对象
                SmsMessage message = SmsMessage.createFromPdu((byte[]) o);
                // 5.获取短信对象的基本信息
                String sendPNum = message.getOriginatingAddress();
                String content = message.getMessageBody();
                // 6.判断是否包含播放音乐的关键字
                if (content.contains("#*alarm*#")) {
                    // 7.播放音乐(准备音乐<mediaPlayer>)
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setLooping(true); // 设置循环播放
                    player.start();
                }
            }
        }
    }
}
