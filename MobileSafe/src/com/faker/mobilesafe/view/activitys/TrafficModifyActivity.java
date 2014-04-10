package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.TrafficBean;
import com.faker.mobilesafe.dao.TrafficDao;

import java.text.DecimalFormat;

/**
 * User:LichFaker
 * Date:14-4-9
 * Time:下午8:52
 * Email:lichfaker@gmail.com
 */
public class TrafficModifyActivity extends Activity implements TextWatcher, View.OnClickListener {

    private EditText set_traffic;
    private Button modify;
    private Button back;

    private TrafficDao dao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_modify);

        set_traffic = (EditText) findViewById(R.id.set_traffic);
        modify = (Button) findViewById(R.id.modify);
        back = (Button) findViewById(R.id.back);

        set_traffic.addTextChangedListener(this);

        modify.setOnClickListener(this);
        back.setOnClickListener(this);

        dao = new TrafficDao(this);
        TrafficBean bean = dao.findAll();
        long total = bean.getMobileRx() + bean.getMobileTx() + bean.getOffset();
        DecimalFormat format = new DecimalFormat("##.##");
        set_traffic.setText(format.format(total / 1024f / 1024f));
        set_traffic.selectAll();
        modify.setEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = set_traffic.getText().toString();
        if ("".equals(str)) {
            modify.setEnabled(false);
        } else {
            modify.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v == modify) {
            // 修改的流量，单位字节
            long current = Long.valueOf(set_traffic.getText().toString()) * 1024 * 1024;
            TrafficDao dao = new TrafficDao(this);
            TrafficBean bean = dao.findAll();
            long history = bean.getMobileRx() + bean.getMobileTx();
            bean.setOffset(current - history);
            dao.update(bean);
            onBackPressed();
            finish();
        } else if (v == back) {
            onBackPressed();
            finish();
        }
    }
}