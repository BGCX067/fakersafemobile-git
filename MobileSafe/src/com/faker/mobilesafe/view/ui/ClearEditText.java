package com.faker.mobilesafe.view.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class ClearEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {

	private Drawable clearButton; // 清除按钮对象
	private boolean hasFoucs; // 是否获得焦点

	public ClearEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取Drawable Right的对象
		clearButton = getCompoundDrawables()[2];
		if (clearButton == null) {
			// 如果没有设置Drawable Right，则抛出异常
			throw new NullPointerException(
					"ClearEditText must add drawableRight attribute in XML");
		}
		clearButton.setBounds(0, 0, clearButton.getIntrinsicWidth(),
				clearButton.getIntrinsicHeight());
		// 默认隐藏清除按钮
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
	}

	/**
	 * 得到输入框中的字符串
	 * @return
	 */
	public String getTextString(){
		return getText().toString();
	}
	
	public boolean equal(ClearEditText ct){
		return getTextString().equals(ct.getTextString());
	}
	/**
	 * 设置清除按钮图标是否显示
	 * 
	 * @param b
	 */
	private void setClearIconVisible(boolean b) {
		Drawable right = b ? clearButton : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 通过判断点击的位置坐标是否在清除图标所在范围内来判断是否点击了清除按钮
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() >= (getWidth() - getTotalPaddingRight())
						&& (event.getX() <= ((getWidth() - getPaddingRight())));
//				boolean touchableY = event.getY() >= (getHeight() - getPaddingBottom())
//						&& event.getY() <= (getHeight() - getTotalPaddingBottom());
				if (touchable) {
					this.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 当输入框获得焦点，且有内容时才显示清除按钮
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
