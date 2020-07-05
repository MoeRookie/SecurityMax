package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;

public class SetupOverActivity extends AppCompatActivity {

    private TextView mTvPNum;
    private TextView mTvResetSetup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setupOver = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setupOver) { // 密码输入成功,并且四个导航界面设置完成(停留在设置完成功能列表界面)
            setContentView(R.layout.activity_setup_over);
            initUI();
        }else{ // 密码输入成功,四个导航界面没有设置完成(跳转到导航界面的第一个)
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            // 开启了一个新的界面以后,关闭功能列表界面
            finish();
        }
    }

    private void initUI() {
        mTvPNum = findViewById(R.id.tv_pnum);
        // 设置联系人号码
        String pNum = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
        mTvPNum.setText(pNum);
        // 重新设置条目被点击
        mTvResetSetup = findViewById(R.id.tv_reset_setup);
        mTvResetSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
                startActivity(intent);
                // 开启了一个新的界面以后,关闭功能列表界面
                finish();
            }
        });
    }
}
