package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.BlackNumberDao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InterceptBlacknumberActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout ly_from_call;
	private LinearLayout ly_from_msg;
	private LinearLayout ly_custom;
	private LinearLayout ly_from_constact;
	private LinearLayout ly_show_blackNumber;

	private TextView blackNum;

	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blacknumber);
		dao = new BlackNumberDao(this);
		initUI();
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	private void initUI() {
		ly_from_call = getView(R.id.blacknum_add_from_call);
		ly_from_msg = getView(R.id.blacknum_add_from_msg);
		ly_custom = getView(R.id.blacknum_add_from_custom);
		ly_from_constact = getView(R.id.blacknum_add_from_contact);
		ly_show_blackNumber = getView(R.id.look_up_black);
		blackNum = getView(R.id.black_number_count);

		ly_from_call.setOnClickListener(this);
		ly_from_msg.setOnClickListener(this);
		ly_custom.setOnClickListener(this);
		ly_from_constact.setOnClickListener(this);
		ly_show_blackNumber.setOnClickListener(this);
		blackNum.setText(dao.getCount());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent;
		switch (id) {
		case R.id.blacknum_add_from_call: // 从通话记录添加
			intent = new Intent(this, CallLogActivity.class);
			startActivity(intent);
			break;
		case R.id.blacknum_add_from_msg: // 从信息列表添加
			intent = new Intent(this, MsgLogActivity.class);
			startActivity(intent);
			break;
		case R.id.blacknum_add_from_custom: // 手动添加
			intent = new Intent(this, BlackNumInputActivity.class);
			startActivity(intent);
			break;
		case R.id.blacknum_add_from_contact: // 从通讯录添加
			intent = new Intent(this, BlacknumFromConstactActivity.class);
			startActivity(intent);
			break;
		case R.id.look_up_black: // 查看黑名单
			intent = new Intent(this, ShowBlacknumberListActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onResume() {
		blackNum.setText(dao.getCount());
		super.onResume();
	}
}
