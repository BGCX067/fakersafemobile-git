package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.TrafficBean;
import com.faker.mobilesafe.dao.TrafficDao;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.util.FormatUtil;

/**
 * User: LichFaker
 * Date: 14-4-6
 * Time: 下午1:59
 * Email: lichfaker@gmail.com
 */
public class TrafficActivity extends Activity implements View.OnClickListener {


    private Button backBtn; // 回退按钮
    private TextView used_traffic; // 已用流量
    private TextView total_traffic; // 总流量
    private Button setTotalBtn; // 设置流量套餐
    // 流量状态显示图标
    private ImageView traffic_state_image1;
    private ImageView traffic_state_image2;

    private TextView gUp; // 2G/3G上传流量
    private TextView gDown; // 2G/3G下载流量
    private TextView wifiUp; // wifi 上传流量
    private TextView wifiDown; // wifi下载流量
    private TextView dayTotal; // 当日使用总量

    private Button modifyUsed; // 修改已使用流量

    private boolean isSetTraffic; // 是否设置流量套餐

    private long currMobileTx;
    private long currMobileRx;
    private long currWifiTx;
    private long currWifiRx;

    private TrafficBean bean;

    private TrafficDao dao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_main);

        dao = new TrafficDao(this);

        initUi();

        initData();
    }

    private void initUi() {
        backBtn = (Button) findViewById(R.id.back);
        used_traffic = (TextView) findViewById(R.id.used_traffic);
        total_traffic = (TextView) findViewById(R.id.total_traffic);
        setTotalBtn = (Button) findViewById(R.id.set_traffic);

        traffic_state_image1 = (ImageView) findViewById(R.id.traffic_state_image1);
        traffic_state_image2 = (ImageView) findViewById(R.id.traffic_state_image2);

        gUp = (TextView) findViewById(R.id.gup);
        gDown = (TextView) findViewById(R.id.gdown);
        wifiUp = (TextView) findViewById(R.id.wifiUp);
        wifiDown = (TextView) findViewById(R.id.wifiDown);
        dayTotal = (TextView) findViewById(R.id.dayTotal);

        modifyUsed = (Button) findViewById(R.id.modify);
    }

    private void initData() {
        backBtn.setOnClickListener(this);

        /** 获取手机通过 2G/3G 发出的字节流量总数 */
        currMobileTx = TrafficStats.getMobileTxBytes();
        /** 获取手机通过 2G/3G 接收的字节流量总数 */
        currMobileRx = TrafficStats.getMobileRxBytes();
        /** 获取手机通过wifi接收的字节流量 */
        currWifiRx = TrafficStats.getTotalRxBytes() - currMobileRx;
        /** 获取手机通过wifi发出的字节流量 */
        currWifiTx = TrafficStats.getTotalTxBytes() - currMobileTx;

        String total = SafeSharedpreference.getString(this, ConstConfig.TRAFFIC_MOBILE_TOTAL, "0");

        isSetTraffic = SafeSharedpreference.getBoolean(this, ConstConfig.TRAFFIC_SET, false);
        if (isSetTraffic) {
            total_traffic.setVisibility(View.VISIBLE);
            total_traffic.setText("月流量套餐" + total + "M");
            setTotalBtn.setVisibility(View.GONE);

        } else {
            total_traffic.setVisibility(View.GONE);
            setTotalBtn.setVisibility(View.VISIBLE);
            setTotalBtn.setOnClickListener(this);
        }

        boolean isInit = SafeSharedpreference.getBoolean(this, "traffic_init", false);
        if (!isInit) {
            dao.insert(currMobileTx, currMobileRx, currWifiTx, currWifiRx);
            SafeSharedpreference.save(this, "traffic_init", true);
        }
        bean = dao.findAll();
        long mtx = currMobileTx - bean.getMobileTx();
        long mrx = currMobileRx - bean.getMobileRx();
        long wtx = currWifiTx - bean.getWifiTx();
        long wrx = currWifiRx - bean.getWifiRx();

        used_traffic.setText("已用 " + FormatUtil.formatTraffic(mtx + mrx + bean.getOffset()));

        gUp.setText("2G/3G发送: " + FormatUtil.formatTraffic(mtx));
        gDown.setText("2G/3G接收: " + FormatUtil.formatTraffic(mrx));
        wifiUp.setText("wifi发送: " + FormatUtil.formatTraffic(wtx));
        wifiDown.setText("wifi接收: " + FormatUtil.formatTraffic(wrx));
        dayTotal.setText("总使用量: " + FormatUtil.formatTraffic(mtx + mrx + wtx + wrx));

        if (isSetTraffic) {
            float p = (mtx + mrx + bean.getOffset()) / 1024f /1024f / Long.valueOf(total);
            if (p > 0.6) {
                traffic_state_image1.setImageResource(R.drawable.content_top_bg_02_red_1);
                traffic_state_image2.setImageResource(R.drawable.content_top_bg_03_flow_03);
            } else if (p > 0.3) {
                traffic_state_image1.setImageResource(R.drawable.content_top_bg_02_yellow);
                traffic_state_image2.setImageResource(R.drawable.content_top_bg_03_usage_04);
            } else {
                traffic_state_image1.setImageResource(R.drawable.content_top_bg_02_blue_1);
                traffic_state_image2.setImageResource(R.drawable.content_top_bg_03_usage_02);
            }
        }
        modifyUsed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (backBtn == v) {
            onBackPressed();
            finish();
        } else if (setTotalBtn == v) {
            // 设置流量套餐
            Intent intent = new Intent(this, TrafficSetActivity.class);
            startActivity(intent);
        } else if (modifyUsed == v) {
            // 修改当前使用量
            Intent intent = new Intent(this, TrafficModifyActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}