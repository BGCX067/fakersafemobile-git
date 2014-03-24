package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.TaskInfoBean;
import com.faker.mobilesafe.deal.TaskManagerService;
import com.faker.mobilesafe.view.ui.adapter.TaskManagerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class TaskManagerActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	protected static final int LOAD_DATA_SUCCESS = 0;
	protected static final int CHANGE_DATA = 1;
	private ListView lv_taskmanage;
	private Button clearButton;
	private RelativeLayout rl_loading;

	private TaskManagerService taskService;

	private Context context;
	private TaskManagerAdapter adapter;
	private int selectedCount;

	private List<TaskInfoBean> totalTasks;
	private List<TaskInfoBean> adviceKillTasks;
	private List<TaskInfoBean> unKillTasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);

		// INIT UI
		lv_taskmanage = (ListView) findViewById(R.id.lv_taskmanage);
		clearButton = (Button) findViewById(R.id.clear);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);

		// LOAD DATA
		context = this;
		taskService = new TaskManagerService(this);
		new Thread() {
			@Override
			public void run() {
				totalTasks = taskService.getTaskInfos();
				Message msg = new Message();
				msg.what = LOAD_DATA_SUCCESS;
				handler.sendMessage(msg);
			};
		}.start();

		clearButton.setOnClickListener(this);
		lv_taskmanage.setOnItemClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				unKillTasks = new ArrayList<TaskInfoBean>();
				adviceKillTasks = new ArrayList<TaskInfoBean>();
				for (TaskInfoBean bean : totalTasks) {
					if (bean.getPackageName().equals(getPackageName())) {
						unKillTasks.add(bean);
					} else if (bean.isUserApp()) {
						adviceKillTasks.add(bean);
					} else {
						unKillTasks.add(bean);
					}
				}
				adapter = new TaskManagerAdapter(context, unKillTasks,
						adviceKillTasks);
				lv_taskmanage.setAdapter(adapter);
				rl_loading.setVisibility(View.GONE);
				if (adviceKillTasks.size() > 0) {
					clearButton.setEnabled(true);
				}
				selectedCount = adviceKillTasks.size();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return;
		}
		if (adviceKillTasks.size() != 0) {
			if (position == adviceKillTasks.size() + 1) {
				return;
			}
		}
		TaskInfoBean bean = (TaskInfoBean) adapter.getItem(position);
		Log.i("lichfaker",
				bean.getAppName() + " " + position + bean.isChecked());
		ImageView checkBox = (ImageView) view.findViewById(R.id.clear_check);
		if (bean.isChecked()) {
			checkBox.setImageResource(R.drawable.checkbox_unchecked);
			bean.setChecked(false);
			selectedCount--;
		} else {
			checkBox.setImageResource(R.drawable.checkbox_checked);
			bean.setChecked(true);
			selectedCount++;
		}
		if (selectedCount > 0) {
			clearButton.setEnabled(true);
		} else {
			clearButton.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 关闭选中的进程
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (int i = totalTasks.size() - 1; i >= 0; i--) {
			TaskInfoBean bean = totalTasks.get(i);
			if (bean.isChecked()) {
				am.killBackgroundProcesses(bean.getPackageName());
				if(bean.isUserApp()){
					adviceKillTasks.remove(bean);
				}else{
					unKillTasks.remove(bean);
				}
				totalTasks.remove(bean);
			}
		}
		adapter.update(unKillTasks, adviceKillTasks);
	}
}
