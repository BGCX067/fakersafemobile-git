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
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

/**
 * User:LichFaker
 * Date:14-4-9
 * Time:下午8:51
 * Email:lichfaker@gmail.com
 */
public class TrafficSetActivity extends Activity implements View.OnClickListener,TextWatcher{

    private Button backBtn;
    private EditText totalTraffic;
    private Button comfirm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_set);

        backBtn = (Button) findViewById(R.id.back);
        totalTraffic = (EditText) findViewById(R.id.set_traffic);
        comfirm = (Button) findViewById(R.id.modify);

        backBtn.setOnClickListener(this);
        totalTraffic.addTextChangedListener(this);
        comfirm.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v == backBtn){
            onBackPressed();
            finish();
        }else if(v == comfirm){
            // 设置流量套餐
            String total = totalTraffic.getText().toString();
            SafeSharedpreference.save(this, ConstConfig.TRAFFIC_MOBILE_TOTAL,total);
            SafeSharedpreference.save(this,ConstConfig.TRAFFIC_SET,true);
            onBackPressed();
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = totalTraffic.getText().toString();
        if("".equals(text)){
            comfirm.setEnabled(false);
        }else{
            comfirm.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}