package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.widget.TextView;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.CallRecordBean;
import com.faker.mobilesafe.dao.CallRecordDao;
import com.faker.mobilesafe.view.ui.adapter.CallhistoryListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class InterceptCallrecordActivity extends ListActivity implements
        OnClickListener {

    private CallhistoryListAdapter adapter;

    private Button delete_all;
    private TextView emptyText;

    private List<CallRecordBean> call_list;

    private CallRecordDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intecepter_callhistory);
        dao = new CallRecordDao(this);
        call_list = dao.findAll();
        adapter = new CallhistoryListAdapter(this, call_list);
        setListAdapter(adapter);

        delete_all = (Button) findViewById(R.id.delete_all);
        if (call_list.size() > 0) {
            delete_all.setEnabled(true);
        }
        delete_all.setOnClickListener(this);

        emptyText = (TextView) findViewById(R.id.empty_text);
        emptyText.setText("烦扰您的骚扰电话\n管家帮您智能拦截");
    }

    public void onBack(View v) {
        onBackPressed();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        call_list = dao.findAll();
        adapter.update(call_list);
        if (call_list.size() > 0) {
            delete_all.setEnabled(true);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        call_list = dao.findAll();
        adapter.update(call_list);
        if (call_list.size() > 0) {
            delete_all.setEnabled(true);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        dao.deleteAll();
        delete_all.setEnabled(false);
        adapter.update(new ArrayList<CallRecordBean>());
    }
}
