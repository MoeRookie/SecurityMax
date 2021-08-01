package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.service.AddressService;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.ServiceUtils;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.view.SettingClickView;
import com.liqun.securitymax.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView mSivUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
        initAddress();
        initToastStyle();
    }

    /**
     * 版本更新相关
     */
    private void initUpdate() {
        mSivUpdate = findViewById(R.id.siv_update);
        // 获取已有的开关状态,用作显示
        boolean openUpdate = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        // 是否选中,根据上一次存储的结果去做决定
        mSivUpdate.setCheck(openUpdate);
        mSivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取之前的选中状态
                boolean isCheck = mSivUpdate.isCheck();
                // 如果之前是选中的, 点击过后变成未选中;如果之前是未选中, 点击过后变成选中的
                mSivUpdate.setCheck(!isCheck);
                // 将取反后的状态存储到相应的sp中
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }

    /**
     * 是否显示电话号码归属地的方法
     */
    private void initAddress() {
        final SettingItemView sivAddress = findViewById(R.id.siv_address);
        // 对服务是否开的状态做显示
        boolean isRunning = ServiceUtils.isRunning(this, AddressService.class.getName());
        sivAddress.setCheck(isRunning);
        // 点击过程中, 状态[是否开启电话号码归属地]的切换过程
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回点击前的选中状态
                boolean isCheck = sivAddress.isCheck();
                sivAddress.setCheck(!isCheck);
                if (!isCheck) {
                    // 开启服务, 管理吐司
                    startService(new Intent(getApplicationContext(), AddressService.class));
                }else{
                    // 关闭服务, 不需要显示吐司
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    private void initToastStyle() {
        SettingClickView scvToastStyle = findViewById(R.id.scv_toast_style);
        // 话述
        scvToastStyle.setTitle("设置归属地显示风格");
        // 1.创建描述文字所在的String类型数组
        String[] mToastStyleDesList = {"透明", "橙色", "蓝色", "灰色", "绿色"};
        // 2.SP获取吐司显示样式的索引值(int), 用于获取描述文字
        int toastStyleIndex = SpUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
        // 3.通过索引, 获取字符串数组重的文字, 显示给描述内容控件
        scvToastStyle.setDes(mToastStyleDesList[toastStyleIndex]);
    }
}
