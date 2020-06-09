package com.liqun.securitymax.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liqun.securitymax.R;

public class SettingItemView extends RelativeLayout {
    private CheckBox mCbBox;
    private TextView mTvDes;
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
        mTvDes = findViewById(R.id.tv_des);
        mCbBox = findViewById(R.id.cb_box);
        // 获取自定义以及原生属性的操作,写在此处,从AttributeSet.attrs对象中获取
        initAttrs(attrs);
    }

    /**
     * 返回属性集合中自定义属性的属性值
     * @param attrs 构造方法中维护好的属性集合
     */
    private void initAttrs(AttributeSet attrs) {

    }

    /**
     * 判断是否开启的方法
     * @return 返回当前SettingItemView的选中状态 -> true(开启: checkBox返回true),false(关闭,checkBox返回false)
     */
    public boolean isCheck(){
        // 由checkBox的选中结果,决定当前条目是否开启
        return mCbBox.isChecked();
    }

    public void setCheck(boolean isCheck){
        // 当前条目在点击的过程中,mCbBox的选中状态也在跟随isCheck变化
        mCbBox.setChecked(isCheck);
        mTvDes.setText("自动更新已"+(isCheck?"开启":"关闭"));
    }
}
