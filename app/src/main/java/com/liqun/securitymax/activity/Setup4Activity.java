package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.SpUtils;

public class Setup4Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_4);
    }
    public void nextPage(View view){
        Intent intent = new Intent(this, SetupOverActivity.class);
        startActivity(intent);
        finish();

        SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
    }
    public void prePage(View view){
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
    }
}
