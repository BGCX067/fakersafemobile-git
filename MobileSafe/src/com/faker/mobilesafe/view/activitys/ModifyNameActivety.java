package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.view.ui.ClearEditText;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class ModifyNameActivety extends BaseActivity {

	private ClearEditText modify_editor;
	private String moudle_name;
	private TextView name_limit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_moudle_name);
		initData();
		initUI();
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void onBack(View v) {
		// 保存模块修改后的数据
		SafeSharedpreference.save(this, ConstConfig.MODULE_NAME, modify_editor
				.getText().toString());
		onBackPressed();
	}

	private void initUI() {
		modify_editor = getView(R.id.modify_name_editor);
		modify_editor.setText(moudle_name);
		modify_editor.addTextChangedListener(new MyTextWatcher());
		name_limit = getView(R.id.name_count_limit);
		name_limit.setText(String.valueOf(10 - moudle_name.length()));

	}

	private void initData() {
		moudle_name = SafeSharedpreference.getString(this,
				ConstConfig.MODULE_NAME, "手机防盗");
		
	}

	
	private final class MyTextWatcher implements TextWatcher {

		private int length;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			int n = modify_editor.getText().length();
			name_limit.setText(String.valueOf(10 - n));
			length = n;
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (length > 10) {
				s.delete(9, modify_editor.length() - 1);
				length = 10;
			}
		}

	}
}
