package com.faker.mobilesafe.view.ui;

import com.faker.mobilesafe.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomTipsconfirmDialoog extends Dialog implements
		android.view.View.OnClickListener {

	private int layoutId;
	private TextView title;
	private TextView info;
	private Button confirm;
	private Button cancle;

	private String dialog_title;
	private String dialog_info;
	private String dialog_confirm;
	private String dialog_cancle;

	private CustomDialogListener listener;

	/**
	 * 按钮点击回调函数接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface CustomDialogListener {
		void onClick(View v);
	}

	private CustomTipsconfirmDialoog(Context context) {
		super(context);
	}

	public CustomTipsconfirmDialoog(Context context, int layoutId,
			CustomDialogListener listener) {
		super(context);
		this.layoutId = layoutId;
		this.listener = listener;
	}

	public CustomTipsconfirmDialoog(Context context, int layoutId, int theme,
			CustomDialogListener listener) {
		super(context, theme);
		this.layoutId = layoutId;
		this.listener = listener;
	}

	/**
	 * 设置对话框标题
	 * 
	 * @param title
	 */
	public CustomTipsconfirmDialoog setTitle(String title) {
		this.dialog_title = title;
		return this;
	}

	/**
	 * 设置提示内容
	 * 
	 * @param info
	 */
	public CustomTipsconfirmDialoog setInfo(String info) {
		this.dialog_info = info;
		return this;
	}

	/**
	 * 设置确定按钮名称
	 * 
	 * @param confirm
	 */
	public CustomTipsconfirmDialoog setConfirm(String confirm) {
		this.dialog_confirm = confirm;
		return this;
	}

	/**
	 * 设置取消按钮名称
	 * 
	 * @param cancle
	 */
	public CustomTipsconfirmDialoog setCancle(String cancle) {
		this.dialog_cancle = cancle;
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutId);
		confirm = (Button) findViewById(R.id.dialog_confirm);
		cancle = (Button) findViewById(R.id.dialog_cancle);
		title = (TextView) findViewById(R.id.dialog_title);
		info = (TextView) findViewById(R.id.dialog_context);
		if (title != null) {
			title.setText(dialog_title);
		}
		if (info != null) {
			info.setText(dialog_info);
		}
		if (confirm != null && cancle != null) {
			confirm.setText(dialog_confirm);
			cancle.setText(dialog_cancle);
			confirm.setOnClickListener(this);
			cancle.setOnClickListener(this);
			confirm.requestFocus();
		}
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);
	}

}
