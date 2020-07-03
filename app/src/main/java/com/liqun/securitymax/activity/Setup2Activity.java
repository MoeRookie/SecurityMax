package com.liqun.securitymax.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.utils.ToastUtils;
import com.liqun.securitymax.view.SettingItemView;

public class Setup2Activity extends AppCompatActivity {

    private SettingItemView mSivSimBound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_2);

        initUI();
    }

    private void initUI() {
        mSivSimBound = findViewById(R.id.siv_sim_bound);
        // 1.回显(读取已有的绑定状态, 用作显示; sp中是否存储了sim卡的序列号)
        final String simSerialNum = SpUtils.getString(this, ConstantValue.SIM_SERIAL_NUM, "");
        // 2.判断sim卡序列号是否为""
        mSivSimBound.setCheck(!TextUtils.isEmpty(simSerialNum));

        mSivSimBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3.获取原有的状态
                boolean isCheck = mSivSimBound.isCheck();
                // 4.将原有状态取反
                // 5.状态设置给当前条目
                mSivSimBound.setCheck(!isCheck);
                if (!isCheck) {
                    // 6.存储sim卡序列号
                    // 6.1. 获取sim卡序列号(TelephonyManager)
                    TelephonyManager manager =
                            (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    // 6.2. 获取sim卡序列号
                    String simSerialNumber = manager.getSimSerialNumber();
                    // 6.3. 存储
                    SpUtils.putString(getApplicationContext(),ConstantValue.SIM_SERIAL_NUM, simSerialNumber);
                }else{
                    // 7.将存储sim卡序列号的节点从sp中删除
                    SpUtils.remove(getApplicationContext(), ConstantValue.SIM_SERIAL_NUM);
                }
            }
        });
    }

    public void nextPage(View view){
        String simSerialNum = SpUtils.getString(this, ConstantValue.SIM_SERIAL_NUM, "");
        if (!TextUtils.isEmpty(simSerialNum)) {
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_next_in, R.anim.anim_next_out);
        }else{
            ToastUtils.show(this, "请绑定 sim 卡");
        }
    }
    public void prePage(View view){
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);
    }
}
