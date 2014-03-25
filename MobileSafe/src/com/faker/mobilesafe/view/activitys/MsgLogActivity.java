package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.MsgRecordBean;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.util.FormatUtil;
import com.faker.mobilesafe.view.ui.adapter.MsgLogListAdapter;

public class MsgLogActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private ListView lv_msg_log;
	private TextView emptyView;
	private Button addButton;

	private MsgLogListAdapter adapter;

	private List<MsgRecordBean> msgLists;

	private List<String> selectedList;

	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_black_from_msglog);
		// INIT UI
		lv_msg_log = (ListView) findViewById(R.id.lv_msg_log);
		emptyView = (TextView) findViewById(R.id.empty);
		addButton = (Button) findViewById(R.id.add_comfirm);

		// INIT DATA
		msgLists = new ArrayList<MsgRecordBean>();
		selectedList = new ArrayList<String>();
		initMsgList();
		adapter = new MsgLogListAdapter(this, msgLists);
		dao = new BlackNumberDao(this);

		lv_msg_log.setAdapter(adapter);
		lv_msg_log.setEmptyView(emptyView);
		lv_msg_log.setOnItemClickListener(this);
		addButton.setOnClickListener(this);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 添加黑名单
		dao.addBlackNumbers(selectedList);
		onBackPressed();
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MsgRecordBean bean = (MsgRecordBean) adapter.getItem(position);
		ImageView checkBox = (ImageView) view
				.findViewById(R.id.msg_log_checkbox);
		String number = bean.getNumber();
		if (selectedList.contains(number)) {
			checkBox.setImageResource(R.drawable.checkbox_unchecked);
			selectedList.remove(number);
		} else {
			checkBox.setImageResource(R.drawable.checkbox_checked);
			selectedList.add(number);
		}
		if (selectedList.size() > 0) {
			addButton.setEnabled(true);
		} else {
			addButton.setEnabled(false);
		}
	}

	private void initMsgList() {
		Uri uri = Uri.parse("content://sms/");
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(uri, new String[] { "* from threads --" }, null,
				null, null);
		// String[] names = cr.getColumnNames();
		// for(String name : names){
		// Log.i("lichfaker", name);
		// }
		while (c.moveToNext()) {
			MsgRecordBean bean = new MsgRecordBean();
			String dateLong = c.getString(c.getColumnIndex("date"));
			bean.setTime(FormatUtil.formatTime(Long.parseLong(dateLong)));
			String content = c.getString(c.getColumnIndex("snippet"));
			bean.setContent(content);
			String threads_id = c.getString(c.getColumnIndex("_id"));
			Cursor c1 = cr.query(uri, new String[] { "address" },
					"thread_id = ?", new String[] { threads_id }, null);
			if (c1.moveToFirst()) {
				bean.setNumber(c1.getString(0));
			}
//			Log.i("lichfaker", bean.toString());
			msgLists.add(bean);
		}
	}

}
