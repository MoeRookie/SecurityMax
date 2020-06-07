package com.liqun.securitymax.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
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
        mSivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取之前的选中状态
                boolean isCheck = mSivUpdate.isCheck();
                // 如果之前是选中的, 点击过后变成未选中;如果之前是未选中, 点击过后变成选中的
                mSivUpdate.setCheck(!isCheck);
            }
        });
    }
}
