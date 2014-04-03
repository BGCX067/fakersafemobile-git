package com.faker.mobilesafe.view.activitys;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.util.Log;
import android.widget.*;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.CallLogBean;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.dao.PhoneAddressQueryDao;
import com.faker.mobilesafe.view.ui.adapter.CallLogListAdapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.view.View;
import android.view.View.OnClickListener;

public class CallLogActivity extends ListActivity implements OnClickListener {

    private Button add_comfirm;
    private TextView emptyText;

    private List<CallLogBean> callLogList;
    private List<CallLogBean> callLogSelected;

    private CallLogListAdapter adapter;

    private BlackNumberDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_black_number);

        add_comfirm = (Button) findViewById(R.id.add_comfirm);
        emptyText = (TextView) findViewById(R.id.empty_text);

        emptyText.setText("当前没有通话记录");
        initCallLog();
        adapter = new CallLogListAdapter(this, callLogList);
        setListAdapter(adapter);
        callLogSelected = new ArrayList<CallLogBean>();
        dao = new BlackNumberDao(this);

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
        for (CallLogBean bean : callLogSelected) {
            numbers.add(bean.getNumber());
        }
        dao.addBlackNumbers(numbers);
        onBackPressed();
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        CallLogBean bean = (CallLogBean) adapter.getItem(position);
        ImageView checkBox = (ImageView) v.findViewById(R.id.call_log_checkbox);
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
        super.onListItemClick(l, v, position, id);
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
            boolean isPhone = number.matches("^1[358]\\d{9}$");
            Log.i("address",address);
            if(isPhone){
                if (address.indexOf("黑龙江") > 0) {
                    address = address.substring(0, 5);
                } else {
                    address = address.substring(0, 4);
                }
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
