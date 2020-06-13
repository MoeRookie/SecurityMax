package com.liqun.securitymax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.liqun.securitymax.R;
import com.liqun.securitymax.utils.ConstantValue;
import com.liqun.securitymax.utils.MD5Utils;
import com.liqun.securitymax.utils.SpUtils;
import com.liqun.securitymax.utils.ToastUtils;

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
        // 注册九宫格单个条目点击事件
        mGvFunc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override // position: 条目索引
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 开启对话框
                        showDialog();
                        break;
                    case 8: // 跳转"设置中心"模块界面
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    private void showDialog() {
        // 判断本地是否有存储密码(Sp 字符串)
        String psd = SpUtils.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            // 1.初始设置密码对话框
            showSetPsdDialog();
        } else {
            // 2.确认密码对话框
            showConfirmPsdDialog();
        }
    }

    /**
     * 确认密码对话框
     */
    private void showConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
        // 让对话框显示一个自定义的界面效果
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etConfirmPsd = view.findViewById(R.id.et_confirm_psd);
                String confirmPsd = etConfirmPsd.getText().toString().trim();
                if (!TextUtils.isEmpty(confirmPsd)) {
                    // 将存储在sp中32位的密码, 获取出来, 然后将输入的密码同样进行MD5, 然后与sp中存储的密码进行比对
                    String setPsd = SpUtils.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, "");
                    if (setPsd.equals(MD5Utils.encode(confirmPsd))) {
                        // 进入应用的手机防盗模块
                        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                        startActivity(intent);
                        // 跳转到新的界面以后需要去隐藏对话框
                        dialog.dismiss();
                    }else{
                        ToastUtils.show(getApplicationContext(), "确认密码错误");
                    }
                }else{
                    // 提示用户密码输入有为空的情况
                    ToastUtils.show(getApplicationContext(), "请输入密码");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框
     */
    private void showSetPsdDialog() {
        // 因为需要自己去定义对话框的展示样式, 所以需要调用 dialog.setView(view);
        // view是由自己编写的xml转换成的对象(xml--->view)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_psd, null);
        // 让对话框显示一个自定义的界面效果
        // 为了兼容低版本, 给对话框设置布局的时候, 让其没有内边距(Android系统默认提供出来的)
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etSetPsd = view.findViewById(R.id.et_set_psd);
                EditText etConfirmPsd = view.findViewById(R.id.et_confirm_psd);
                String setPsd = etSetPsd.getText().toString().trim();
                String confirmPsd = etConfirmPsd.getText().toString().trim();
                if (!TextUtils.isEmpty(setPsd) && !TextUtils.isEmpty(confirmPsd)) {
                    if (setPsd.equals(confirmPsd)) {
                        // 进入应用的手机防盗模块
                        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                        startActivity(intent);
                        // 跳转到新的界面以后需要去隐藏对话框
                        dialog.dismiss();
                        SpUtils.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD,
                                MD5Utils.encode(setPsd));
                    }else{
                        ToastUtils.show(getApplicationContext(), "确认密码错误");
                    }
                }else{
                    // 提示用户密码输入有为空的情况
                    ToastUtils.show(getApplicationContext(), "请输入密码");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
