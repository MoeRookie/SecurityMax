package com.liqun.securitymax.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liqun.securitymax.R;

public class SettingItemView extends RelativeLayout {
    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // xml -> view(将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的View中)
        View.inflate(context, R.layout.item_setting, this);
        // 自定义组合控件中的标题描述
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDes = findViewById(R.id.tv_des);
        CheckBox cbBox = findViewById(R.id.cb_box);
    }
}
