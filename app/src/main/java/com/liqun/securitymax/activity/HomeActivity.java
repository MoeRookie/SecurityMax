package com.liqun.securitymax.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;

public class HomeActivity extends AppCompatActivity {

    private GridView mGvFunc;
    private String[] mTitleStrs;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initData();
    }

    private void initUI() {
        mGvFunc = findViewById(R.id.gv_func);
    }

    /**
     * 初始化数据的方法
     */
    private void initData() {
        // 准备数据(文字(9组),图片(9张))
        mTitleStrs = new String[]{
                "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };

        mDrawableIds = new int[]{
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
        };
        // 九宫格控件设置数据适配器(等同ListView的数据适配器)
        mGvFunc.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        // 条目的总数(文字组数==图片张数)
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_gv_func, null);
            ImageView ivIcon = view.findViewById(R.id.iv_icon);
            TextView tvTitle = view.findViewById(R.id.tv_title);

            ivIcon.setBackgroundResource(mDrawableIds[position]);
            tvTitle.setText(mTitleStrs[position]);
            return view;
        }
    }
}
