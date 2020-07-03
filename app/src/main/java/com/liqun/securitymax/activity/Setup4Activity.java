package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.utils.ToastUtils;

public class Setup4Activity extends AppCompatActivity {

    private CheckBox mCbBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_4);
        initUI();
    }

    private void initUI() {
        mCbBox = findViewById(R.id.cb_box);
        // 1.是否选中状态的回显
        boolean openSecurity =
                SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        // 2.根据状态,修改mCbBox后续的文字显示
        mCbBox.setChecked(openSecurity);
        mCbBox.setText("安全设置已"+(openSecurity?"开启":"关闭"));
        // 3.点击过程中,监听选中状态发生改变过程
        mCbBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 4.isChecked点击后的状态,存储点击后的状态
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
                // 5.根据开启关闭状态,去修改显示的文字
                mCbBox.setText("安全设置已"+(isChecked?"开启":"关闭"));
            }
        });
    }

    public void nextPage(View view){
        boolean openSecurity = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if (openSecurity) {
            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
            overridePendingTransition(R.anim.anim_next_in, R.anim.anim_next_out);
        }else{
            ToastUtils.show(getApplication(),"请开启防盗保护");
        }

    }
    public void prePage(View view){
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);
    }
}
