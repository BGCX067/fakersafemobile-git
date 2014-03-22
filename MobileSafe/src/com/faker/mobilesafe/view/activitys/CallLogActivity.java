package com.faker.mobilesafe.view.activitys;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.CallLogBean;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.dao.PhoneAddressQueryDao;
import com.faker.mobilesafe.view.ui.adapter.CallLogListAdapter;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CallLogActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private ListView lv_call_log;
	private TextView emptyView;
	private Button add_comfirm;

	private List<CallLogBean> callLogList;
	private List<CallLogBean> callLogSelected;

	private CallLogListAdapter adapter;

	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_black_from_calllog);

		lv_call_log = (ListView) findViewById(R.id.lv_call_log);
		emptyView = (TextView) findViewById(R.id.empty);
		add_comfirm = (Button) findViewById(R.id.add_comfirm);

		initCallLog();
		adapter = new CallLogListAdapter(this, callLogList);
		lv_call_log.setAdapter(adapter);
		lv_call_log.setEmptyView(emptyView);
		callLogSelected = new ArrayList<CallLogBean>();
		dao = new BlackNumberDao(this);

		lv_call_log.setOnItemClickListener(this);
		add_comfirm.setOnClickListener(this);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 将选中的信息添加到黑名单数据库
		List<String> numbers = new ArrayList<String>();
		for(CallLogBean bean : callLogSelected){
			numbers.add(bean.getNumber());
		}
		dao.addBlackNumbers(numbers);
		onBackPressed();
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CallLogBean bean = (CallLogBean) adapter.getItem(position);
		ImageView checkBox = (ImageView) view
				.findViewById(R.id.call_log_checkbox);
		if (callLogSelected.contains(bean)) {
			checkBox.setImageResource(R.drawable.checkbox_unchecked);
			callLogSelected.remove(bean);
		} else {
			checkBox.setImageResource(R.drawable.checkbox_checked);
			callLogSelected.add(bean);
		}
		if (callLogSelected.size() > 0) {
			add_comfirm.setEnabled(true);
			add_comfirm.setText("确  定 (" + callLogSelected.size() + ")");
		} else {
			add_comfirm.setEnabled(false);
			add_comfirm.setText("确  定");
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void initCallLog() {

		callLogList = new ArrayList<CallLogBean>();

		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, null, null, null);
		PhoneAddressQueryDao dao = new PhoneAddressQueryDao();
		SimpleDateFormat adFormat = new SimpleDateFormat("yyyy/MM/dd HH:MM");
		// String[] names = cursor.getColumnNames();
		// for(String name : names){
		// Log.i("lichfaker", name);
		// }
		while (cursor.moveToNext()) {
			CallLogBean bean = new CallLogBean();
			String name = cursor.getString(cursor
					.getColumnIndex(Calls.CACHED_NAME));
			bean.setName(name);
			String number = cursor.getString(cursor
					.getColumnIndex(Calls.NUMBER));
			bean.setNumber(number);
			// 根据电话号码查询归属地信息
			String address = dao.queryAddress(number);
			if (address.indexOf("黑龙江") > 0) {
				address = address.substring(0, 5);
			} else {
				address = address.substring(0, 4);
			}
			bean.setAddress(address);
			// 转换日期并记录
			String dateLong = cursor.getString(cursor
					.getColumnIndex(Calls.DATE));
			Date date = new Date(Long.parseLong(dateLong));
			String time = adFormat.format(date);
			bean.setTime(time);

			callLogList.add(bean);
		}
	}
}
