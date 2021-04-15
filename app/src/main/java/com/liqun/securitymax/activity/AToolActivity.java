package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;

public class AToolActivity extends AppCompatActivity {

    private View mTvQueryPhoneAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        // 电话归属地查询方法
        initPhoneAddress();
    }

    private void initPhoneAddress() {
        mTvQueryPhoneAddress = findViewById(R.id.tv_query_phone_address);
        mTvQueryPhoneAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QueryAddressActivity.class));
            }
        });
    }
}
