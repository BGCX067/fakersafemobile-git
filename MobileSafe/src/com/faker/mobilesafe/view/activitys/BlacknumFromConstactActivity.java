package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.ContactBean;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.deal.ContactInfoService;
import com.faker.mobilesafe.view.ui.adapter.BlackContactListAdapter;

public class BlacknumFromConstactActivity extends ListActivity {

    private Button comfirmButton;
    private TextView emptyText;

    private List<ContactBean> list_contacts; // 所有联系人列表

    private List<String> list_selected_contacts; // 当前选中的联系人

    private BlackContactListAdapter adapter;

    private Map<Integer, Boolean> isSelected; // 判断当前是否选中

    private BlackNumberDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_black_number);
        // 取得所有联系人
        ContactInfoService contactService = new ContactInfoService(this);
        list_contacts = contactService.getContacts();

        emptyText = (TextView) findViewById(R.id.empty_text);
        emptyText.setText("当前还没有联系人");
        adapter = new BlackContactListAdapter(this, list_contacts);
        setListAdapter(adapter);

        comfirmButton = (Button) findViewById(R.id.add_comfirm);
        comfirmButton.setOnClickListener(new ComfirmListener());
        initMap();
        list_selected_contacts = new ArrayList<String>();
        dao = new BlackNumberDao(this);
    }

    public void onBack(View v) {
        onBackPressed();
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ImageView contact_checkbox = (ImageView) v
                .findViewById(R.id.contact_checkbox);
        ContactBean bean = list_contacts.get(position);
        if (isSelected.get(position)) {
            contact_checkbox.setImageResource(R.drawable.checkbox_unchecked);
            isSelected.put(position, false);
            list_selected_contacts.remove(bean.getPhoneNumber());
        } else {
            contact_checkbox.setImageResource(R.drawable.checkbox_checked);
            isSelected.put(position, true);
            list_selected_contacts.add(bean.getPhoneNumber());
        }
        int count = list_selected_contacts.size();
        if (count > 0) {
            comfirmButton.setEnabled(true);
            comfirmButton.setText("确定(" + count + ")");
        } else {
            comfirmButton.setEnabled(false);
            comfirmButton.setText("确定");
        }
        super.onListItemClick(l, v, position, id);
    }


    @SuppressLint("UseSparseArrays")
    private void initMap() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list_contacts.size(); i++) {
            isSelected.put(i, false);
        }
    }

    private final class ComfirmListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            dao.addBlackNumbers(list_selected_contacts);
            onBackPressed();
            finish();
        }

    }
}
