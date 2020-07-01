package com.liqun.securitymax.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
                    if (!TextUtils.isEmpty(contactId)) {
                        // 4.根据用户的唯一id,查询data表和mimetypes表生成的视图,获取data1和mimetype字段
                        Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                                new String[]{"data1", "mimetype"},
                                "raw_contact_id = ?", new String[]{contactId}, null);
                        while (indexCursor.moveToNext()) {
                            String data1 = indexCursor.getString(0);
                            String mimetype = indexCursor.getString(1);
                            Log.e(TAG, "data1 = " + data1);
                            Log.e(TAG, "mimetype = " + mimetype);
                        }
                        indexCursor.close();
                    }
                }
                cursor.close();
            }
        }.start();
    }

    private void initUI() {
        mLvContacts = findViewById(R.id.lv_contacts);
    }
}
