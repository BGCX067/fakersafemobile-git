package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.AdminService;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.view.ui.ClearEditText;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LostSetupActivity extends BaseActivity implements OnTouchListener {

	private final static int REQUESTCODE = 1000;
	private boolean bDown; // 手指是否按下
	private boolean bSim; // 是否绑定sim卡
	private boolean bOpenlost; // 是否开启手机防盗
	private boolean bShow; // 是否隐藏手机防盗
	private float touchDownX = 0.0f;
	private float touchUpX = 0.0f;
	private ViewFlipper viewFlipper;
	private ClearEditText phonEditText;
	private TextView tv_power;
	private ImageView iv_power;
	private TextView tv_sim;
	private ImageView iv_sim;
	private TextView tv_openlost;
	private ImageView iv_openlost;
	private TextView tv_hidelost;
	private ImageView iv_hidelost;
	private ClearEditText reload_command;
	private AdminService adminService;
	private int pos = 0; // 当前显示页面所在的索引
	private boolean isActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_main);
		adminService =  AdminService.getInstance(this);
		initUI();
	}

	private void initUI() {
		viewFlipper = getView(R.id.body_flipper);
		viewFlipper.setOnTouchListener(this);
		tv_power = getView(R.id.power_hint_text);
		iv_power = getView(R.id.power_switch);
		if (adminService.isActive()) {
			tv_power.setText("已经获得权限");
			iv_power.setImageResource(R.drawable.switch_on_normal);
		}
		bSim = SafeSharedpreference.getBoolean(this, ConstConfig.ISBOUNDSIM,
				false);
		tv_sim = getView(R.id.bound_sim_text);
		iv_sim = getView(R.id.sim_switch);
		if (bSim) {
			// 如果绑定
			tv_sim.setText(R.string.setuptwo_text1);
			iv_sim.setImageResource(R.drawable.switch_on_normal);
		} else {
			tv_sim.setText(R.string.setuptwo_text);
			iv_sim.setImageResource(R.drawable.switch_off_normal);
		}

		String safenumber = SafeSharedpreference.getString(this,
				ConstConfig.SAFENUMBER);
		phonEditText = getView(R.id.safe_number_edit);
		phonEditText.setText(safenumber);

		bOpenlost = SafeSharedpreference.getBoolean(this,
				ConstConfig.ISOPENLOST, false);
		tv_openlost = getView(R.id.open_lost);
		iv_openlost = getView(R.id.open_lost_switch);
		if (bOpenlost) {
			// 如果已经开启
			tv_openlost.setText(R.string.setupthree_text1);
			iv_openlost.setImageResource(R.drawable.switch_on_normal);
		} else {
			tv_openlost.setText(R.string.setupthree_text);
			iv_openlost.setImageResource(R.drawable.switch_off_normal);
		}
		bShow = SafeSharedpreference.getBoolean(this, ConstConfig.ISSHOW, true);
		tv_hidelost = getView(R.id.hide_lost);
		iv_hidelost = getView(R.id.hide_lost_switch);
		if (bShow) {
			tv_hidelost.setText(R.string.setupfour_text);
			iv_hidelost.setImageResource(R.drawable.switch_off_normal);
		} else {
			tv_hidelost.setText(R.string.setupfour_text1);
			iv_hidelost.setImageResource(R.drawable.switch_on_normal);
		}
		reload_command = getView(R.id.reload_command);
		reload_command.setText(SafeSharedpreference.getString(this,
				ConstConfig.RELOAD_COMMAND, ConstConfig.RELOAD_COMMAND));
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && !bDown) {
			bDown = true;
			touchDownX = event.getX();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP && bDown) {
			bDown = false;
			touchUpX = event.getX();
			if (touchUpX - touchDownX >= 100) {
				if (pos == 0) {
					return false;
				}
				// 显示上一屏
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.animator.push_right_in));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.animator.push_right_out));
				viewFlipper.showPrevious();
				pos--;
			} else if (touchDownX - touchUpX >= 100) {
				if (pos == 3) {
					// 保存数据并进行跳转
					saveData();
					Intent intent = new Intent(LostSetupActivity.this,
							LostSetupResultActivity.class);
					startActivity(intent);
					if (isActive){
						adminService.activeAdmin(this);
					}
					LostSetupActivity.this.finish();
					return false;
				}
				// 显示下一屏
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.animator.push_left_in));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.animator.push_left_out));
				viewFlipper.showNext();
				pos++;
			}
			return true;
		}
		return false;
	}

	/**
	 * 将设置的数据保存到SharedPreference中
	 */
	private void saveData() {
		SafeSharedpreference.save(this, ConstConfig.ISBOUNDSIM, bSim);
		SafeSharedpreference.save(this, ConstConfig.ISLOSTSETUP, true);
		SafeSharedpreference.save(this, ConstConfig.SAFENUMBER, phonEditText
				.getText().toString());
		SafeSharedpreference.save(this, ConstConfig.ISOPENLOST, bOpenlost);
		SafeSharedpreference.save(this, ConstConfig.ISSHOW, bShow);
		SafeSharedpreference.save(this, ConstConfig.RELOAD_COMMAND,
				reload_command.getText().toString());
		// 如果绑定SIM卡就读取SIM卡唯一标识并保存
		if (bSim) {
			TelephonyManager tmManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			String simSerial = tmManager.getSimSerialNumber();
			SafeSharedpreference.save(this, ConstConfig.SIMSERIAL, simSerial);
		}
		
	}

	/**
	 * 获得系统授权
	 * 
	 * @param v
	 */
	public void activeAdmin(View v) {
		if (adminService.isActive()) {
			return;
		}
		if (tv_power.getText().toString()
				.equals(getString(R.string.setupone_power_on))) {
			tv_power.setText(R.string.setupone_power_off);
			iv_power.setImageResource(R.drawable.switch_off_normal);
			isActive = false;
		} else {
			tv_power.setText(R.string.setupone_power_on);
			iv_power.setImageResource(R.drawable.switch_on_normal);
			isActive = true;
		}
	}

	/**
	 * 绑定手机卡操作
	 * 
	 * @param v
	 */
	public void boundSim(View v) {
		if (!bSim) {
			// 绑定SIM卡
			iv_sim.setImageResource(R.drawable.switch_on_normal);
			tv_sim.setText(R.string.setuptwo_text1);
			bSim = true;
		} else {
			// 取消绑定
			iv_sim.setImageResource(R.drawable.switch_off_normal);
			tv_sim.setText(R.string.setuptwo_text);
			bSim = false;
		}
	}

	/**
	 * 开启手机防盗
	 * 
	 * @param v
	 */
	public void openLost(View v) {
		if (!bOpenlost) {
			// 首先判断是否具备开启防盗的条件
			if (!bSim) {
				Toast(this, "开启防盗失败，未绑定SIM卡", Toast.LENGTH_SHORT);
			} else if (phonEditText.getText().length() == 0) {
				Toast(this, "开启防盗失败，未设置安全号码", Toast.LENGTH_SHORT);
			} else {
				bOpenlost = true;
				tv_openlost.setText(R.string.setupthree_text1);
				iv_openlost.setImageResource(R.drawable.switch_on_normal);
			}
		} else {
			bOpenlost = false;
			tv_openlost.setText(R.string.setupthree_text);
			iv_openlost.setImageResource(R.drawable.switch_off_normal);
		}
	}

	/**
	 * 是否隐藏手机防盗模块
	 * 
	 * @param v
	 */
	public void hideLost(View v) {
		if (bShow) {
			tv_hidelost.setText(R.string.setupfour_text1);
			iv_hidelost.setImageResource(R.drawable.switch_on_normal);
			bShow = false;
		} else {
			tv_hidelost.setText(R.string.setupfour_text);
			iv_hidelost.setImageResource(R.drawable.switch_off_normal);
			bShow = true;
		}
	}

	/**
	 * 选择一个联系人
	 * 
	 * @param v
	 */
	public void selectContact(View v) {
		Intent intent = new Intent(this, ContactListActivity.class);
		startActivityForResult(intent, REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE) {
			if (data != null) {
				String number = data.getStringExtra("number");
				phonEditText.setText(number);
			}
		}
	}
}
