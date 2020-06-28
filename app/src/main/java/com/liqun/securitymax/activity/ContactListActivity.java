package com.liqun.securitymax.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;

public class ContactListActivity extends AppCompatActivity {

    private ListView mLvContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        initData();
    }

    /**
     * 获取系统联系人数据的方法
     */
    private void initData() {

    }

    private void initUI() {
        mLvContacts = findViewById(R.id.lv_contacts);
    }
}
