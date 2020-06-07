package com.liqun.securitymax.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
/**
 * 能够获取焦点的自定义TextView
 */
public class FocusTextView extends TextView {
    // 通过Java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

    // 由系统调用(带属性+上下文环境)的构造方法
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // 由系统调用(带属性+上下文环境+布局文件中定义样式文件)的构造方法
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 重写获取焦点的方法(由系统调用,调用的时候默认就能获取焦点)
    @Override
    public boolean isFocused() {
        return true;
    }
}
