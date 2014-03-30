package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.widget.TextView;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.MsgRecordBean;
import com.faker.mobilesafe.dao.MsgRecordDao;
import com.faker.mobilesafe.view.ui.adapter.BlackMsgAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class InterceptMessagerecordActivity extends ListActivity implements
		OnClickListener {

	private Button deleButton;
    private TextView emptyText;

	private MsgRecordDao dao;

	private List<MsgRecordBean> msgRecords;

	private BlackMsgAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intecepter_msghistory);
		// INIT UI
		deleButton = (Button) findViewById(R.id.delete_all);
        emptyText = (TextView) findViewById(R.id.empty_text);

        // INIT DATA
		dao = new MsgRecordDao(this);
		msgRecords = dao.findAll();
		adapter = new BlackMsgAdapter(this, msgRecords);
        setListAdapter(adapter);

		if(msgRecords.size() > 0){
			deleButton.setEnabled(true);
		}
		deleButton.setOnClickListener(this);

        emptyText.setText("烦扰您的骚扰短信\n管家帮您智能拦截");
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
