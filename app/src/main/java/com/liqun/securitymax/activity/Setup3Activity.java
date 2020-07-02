package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.utils.ToastUtils;

public class Setup3Activity extends AppCompatActivity {

    private EditText mEtPNum;
    private Button mBtnSelectPNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_3);
        initUI();
    }

    private void initUI() {
        // 显示电话号码的输入框
        mEtPNum = findViewById(R.id.et_pnum);
        // 获取联系人电话号码回显过程
        String pNum = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
        mEtPNum.setText(pNum);
        // 点击选择联系人的按钮
        mBtnSelectPNum = findViewById(R.id.btn_select_pnum);
        mBtnSelectPNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            // 1.返回当前界面的时候, 接收结果的方法
            String phone = data.getStringExtra("phone");
            // 2.将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            mEtPNum.setText(phone);
            // 3.存储联系人到sp中
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void nextPage(View view){
        // 1.点击按钮以后,需要获取输入框中的联系人,再做下一页操作
        String pNum = mEtPNum.getText().toString().trim();
        // 2.如果pNum不为空才可以跳转到下一个页面
        if (!TextUtils.isEmpty(pNum)) {
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
            // 3.如果现在是输入电话号码,则需要去保存
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, pNum);
        }else{
            ToastUtils.show(this, "请输入电话号码");
        }
    }
    public void prePage(View view){
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}
