package com.liqun.securitymax.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.engine.AddressDao;

public class QueryAddressActivity extends AppCompatActivity {

    private EditText mEtPhone;
    private Button mBtnQuery;
    private TextView mTvQueryResult;
    private String mAddress;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 04. 控件使用查询结果
            mTvQueryResult.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        initUI();
        setListener();
    }

    /**
     * 找到我们所关心的控件
     */
    private void initUI() {
        mEtPhone = findViewById(R.id.et_phone);
        mBtnQuery = findViewById(R.id.btn_query);
        mTvQueryResult = findViewById(R.id.tv_query_result);
    }

    /**
     * 设置相关控件的点击事件监听
     */
    private void setListener() {
        // 01. 点击查询, 注册按钮的点击事件
        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEtPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    // 02. 查询是耗时操作, 开启子线程
                    query(phone);
                }else{ // 抖动
                    Animation shake = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.shake);
                    mEtPhone.startAnimation(shake);
                }
            }
        });
        // 05. 实时查询(监听输入框文本变化)
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String phone = mEtPhone.getText().toString();
                query(phone);
            }
        });
    }

    /**
     * 耗时操作
     * 获取号码归属地
     * @param phone 待查询的电话号码
     */
    private void query(final String phone) {
        new Thread(){
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(phone);
                // 03. 消息机制, 告知主线程查询结束, 可以去使用查询结果
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
