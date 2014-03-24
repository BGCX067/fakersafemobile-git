package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.MsgRecordBean;
import com.faker.mobilesafe.dao.MsgRecordDao;
import com.faker.mobilesafe.view.ui.adapter.BlackMsgAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InterceptMessagerecordActivity extends Activity implements
		OnClickListener {

	private ListView lv_msg_record;
	private TextView emptyView;
	private Button deleButton;

	private MsgRecordDao dao;

	private List<MsgRecordBean> msgRecords;

	private BlackMsgAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intecepter_msghistory);
		// INIT UI
		lv_msg_record = (ListView) findViewById(R.id.msghistory_listview);
		emptyView = (TextView) findViewById(R.id.empty);
		deleButton = (Button) findViewById(R.id.delete_all);

		// INIT DATA
		dao = new MsgRecordDao(this);
		msgRecords = dao.findAll();
		adapter = new BlackMsgAdapter(this, msgRecords);

		lv_msg_record.setAdapter(adapter);
		lv_msg_record.setEmptyView(emptyView);
		if(msgRecords.size() > 0){
			deleButton.setEnabled(true);
		}
		deleButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dao.deleteAll();
		adapter.update(new ArrayList<MsgRecordBean>());
		deleButton.setEnabled(false);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}
}
