package com.liqun.securitymax.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity {
    private static final String TAG = ContactListActivity.class.getSimpleName();

    private ListView mLvContacts;
    private List<Map<String, String>> mContactList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 8. 填充数据适配器
            ContactAdapter adapter = new ContactAdapter();
            mLvContacts.setAdapter(adapter);
            super.handleMessage(msg);
        }
    };

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
                mContactList.clear();
                // 3.循环游标, 直到没有数据为止
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(0);
                    if (!TextUtils.isEmpty(contactId)) {
                        // 4.根据用户的唯一id,查询data表和mimetypes表生成的视图,获取data1和mimetype字段
                        Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                                new String[]{"data1", "mimetype"},
                                "raw_contact_id = ?", new String[]{contactId}, null);
                        HashMap<String, String> map = new HashMap<>();
                        // 5.循环获取每一个人的电话号码、姓名及数据类型
                        while (indexCursor.moveToNext()) {
                            String data = indexCursor.getString(0);
                            String type = indexCursor.getString(1);
                            // 6.区分类型给map填充数据
                            if ("vnd.android.cursor.item/phone_v2".equals(type)) {
                                if (!TextUtils.isEmpty(data)) {
                                    map.put("phone", data);
                                }
                            } else if ("vnd.android.cursor.item/name".equals(type)) {
                                if (!TextUtils.isEmpty(data)) {
                                    map.put("name", data);
                                }
                            }
                        }
                        indexCursor.close();
                        mContactList.add(map);
                    }
                }
                cursor.close();
                // 7.消息机制,发送一个空的消息,告知主线程可以去使用子线程已经填充好的数据集合
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        mLvContacts = findViewById(R.id.lv_contacts);
    }

    private class ContactAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mContactList.size();
        }

        @Override
        public Map<String, String> getItem(int position) {
            return mContactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_contact, null);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvPNum = view.findViewById(R.id.tv_pnum);
            tvName.setText(getItem(position).get("name"));
            tvPNum.setText(getItem(position).get("phone"));
            return view;
        }
    }
}
