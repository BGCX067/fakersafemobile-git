package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.view.ui.ClearEditText;

public class BlackNumInputActivity extends Activity implements OnClickListener,
		TextWatcher {

	private ClearEditText blackNumberEdit;
	private TextView tv_isBlack;
	private Button add_comfirm;

	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balck_number_input);

		blackNumberEdit = (ClearEditText) findViewById(R.id.number_input);
		tv_isBlack = (TextView) findViewById(R.id.is_black_hint);
		add_comfirm = (Button) findViewById(R.id.add_comfirm);

		dao = new BlackNumberDao(this);

		tv_isBlack.setText("");
		add_comfirm.setOnClickListener(this);
		blackNumberEdit.addTextChangedListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 添加黑名单
		String number = blackNumberEdit.getTextString();
		dao.addBalckNumber(number);
		onBackPressed();
		finish();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String number = blackNumberEdit.getTextString();
		if ("".equals(number)) {
			tv_isBlack.setText("");
			add_comfirm.setEnabled(false);
		} else if (dao.isBlackNumber(number)) {
			tv_isBlack.setText("该号码已经是黑名单");
			add_comfirm.setEnabled(false);
		} else {
			tv_isBlack.setText("");
			add_comfirm.setEnabled(true);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
}
