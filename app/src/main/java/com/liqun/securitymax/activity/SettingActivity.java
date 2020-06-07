package com.liqun.securitymax.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView mSivUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
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
}
