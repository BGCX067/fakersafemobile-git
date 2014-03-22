package com.faker.mobilesafe.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.PhoneAddressQueryDao;

public class PhoneQueryActivity extends BaseActivity implements OnClickListener {

	private EditText et_query_number;
	private TextView tv_query_result_text;
	private TextView selectContact;
	private Button action_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_query);
		initUI();
	}

	/**
	 * 点击返回键
	 * 
	 * @param v
	 */
	public void onBack(View v) {
		onBackPressed();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.select_contact:
			Intent intent = new Intent(this, ContactListActivity.class);
			startActivityForResult(intent, 101);
			break;

		case R.id.action_query:
			PhoneAddressQueryDao queryService = new PhoneAddressQueryDao();
			String resultString = queryService.queryAddress(et_query_number
					.getText().toString());
			tv_query_result_text.setText(resultString);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 101) {
			if (data != null) {
				String number = data.getStringExtra("number");
				et_query_number.setText(number);
			}
		}
	}

	private void initUI() {
		action_query = getView(R.id.action_query);
		action_query.setOnClickListener(this);
		et_query_number = getView(R.id.phone_query_number);
		et_query_number.addTextChangedListener(new QueryEditWatcher());
		tv_query_result_text = getView(R.id.query_result_text);
		selectContact = getView(R.id.select_contact);
		selectContact.setOnClickListener(this);
	}

	private final class QueryEditWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (et_query_number.getText().toString().equals("")) {
				action_query.setEnabled(false);
			} else {
				action_query.setEnabled(true);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	}

}
