package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;

public class SetupOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setupOver = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setupOver) { // 密码输入成功,并且四个导航界面设置完成(停留在设置完成功能列表界面)
            setContentView(R.layout.activity_setup_over);
        }else{ // 密码输入成功,四个导航界面没有设置完成(跳转到导航界面的第一个)
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            // 开启了一个新的界面以后,关闭功能列表界面
            finish();
        }
    }
}
