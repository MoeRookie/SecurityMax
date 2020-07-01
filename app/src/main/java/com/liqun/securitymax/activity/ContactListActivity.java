package com.liqun.securitymax.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;

public class ContactListActivity extends AppCompatActivity {
    private static final String TAG = ContactListActivity.class.getSimpleName();

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
        // 0.因为读取系统联系人,可能是一个耗时操作,因此需要放到子线程中去处理
        new Thread(){
            @Override
            public void run() {
                // 1.获取内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                // 2.做查询系统联系人数据库表的过程
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);
                // 3.循环游标, 直到没有数据为止
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(0);
                    Log.e(TAG, "contactId = "+contactId);
                }
            }
        }.start();
    }

    private void initUI() {
        mLvContacts = findViewById(R.id.lv_contacts);
    }
}
