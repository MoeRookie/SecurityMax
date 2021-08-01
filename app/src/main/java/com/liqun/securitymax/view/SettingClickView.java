package com.liqun.securitymax.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liqun.securitymax.R;

public class SettingClickView extends RelativeLayout {
    private TextView mTvTitle,mTvDes;
    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // xml -> view(将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的View中)
        View.inflate(context, R.layout.item_setting_click, this);
        // 自定义组合控件中的标题描述
        mTvTitle = findViewById(R.id.tv_title);
        mTvDes = findViewById(R.id.tv_des);
    }

    /**
     * 设置标题内容
     * @param title 标题内容
     */
    public void setTitle(String title){
        mTvTitle.setText(title);
    }

    /**
     * 设置描述内容
     * @param des 描述内容
     */
    public void setDes(String des){
        mTvDes.setText(des);
    }
}
